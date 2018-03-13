CREATE PROCEDURE [spFnyReportBuilder]


@FinalPassYieldReportId NVARCHAR(MAX)

AS

BEGIN
	
	DECLARE @FullTimeGroupFnySqlInsert NVARCHAR(MAX);	--Contains all SQL Insert Statements to be executed for time grouping
	SET @FullTimeGroupFnySqlInsert = '';

	DECLARE @GroupedByTimeRange BIT;					--Identifies if Final Pass Yield report requires time grouping logic	
	DECLARE @FnyFamilyExpField NVARCHAR(MAX);			--Family field of Final Pass Yield report
	DECLARE @FnyDateTimeExpField NVARCHAR(MAX);			--DateTime field of Final Pass Yield report
	DECLARE @FnySerialNumberExpField NVARCHAR(MAX);		--SerialNumber field of Final Pass Yield report
	DECLARE @FnyResultExpField NVARCHAR(MAX);			--Result field of Final Pass Yield report
	DECLARE @FnyCsvInfoFields NVARCHAR(MAX);			--Info columns CSV String
	DECLARE @ExperimentTableName NVARCHAR(MAX);			--Data Warehouse table name
	DECLARE @ExperimentName NVARCHAR(MAX);				--Experiment name
	DECLARE @FnyReportTableName NVARCHAR(MAX);			--Final Pass Yield table name
		
	--Check if Final Pass Yield Report requires time grouping logic
	SET @GroupedByTimeRange = (SELECT TOP(1) FnyGroupByTimeRange FROM FinalPassYieldReport WHERE FnyReportId = @FinalPassYieldReportId);
	SET @FnyDateTimeExpField = (SELECT TOP(1) expField.ExpDbFieldNameId FROM FinalPassYieldReport fnyReport INNER JOIN ExperimentField expField ON fnyReport.DateTimeExpFieldId = expField.ExpFieldId WHERE FnyReportId = @FinalPassYieldReportId);
	SET @FnyResultExpField = (SELECT TOP(1) expField.ExpDbFieldNameId FROM FinalPassYieldReport fnyReport INNER JOIN ExperimentField expField ON fnyReport.ResultExpFieldId = expField.ExpFieldId WHERE fnyReportId = @FinalPassYieldReportId);
	SET @FnySerialNumberExpField = (SELECT TOP(1) expField.ExpDbFieldNameId FROM FinalPassYieldReport fnyReport INNER JOIN ExperimentField expField ON fnyReport.SerialNumberExpFieldId = expField.ExpFieldId WHERE fnyReportId = @FinalPassYieldReportId);
	SET @ExperimentTableName = (SELECT TOP(1) experiment.ExpDbTableNameId FROM FinalPassYieldReport fnyReport INNER JOIN Experiment experiment ON fnyReport.ExpId = experiment.ExpId WHERE fnyReportId = @FinalPassYieldReportId);
	SET @ExperimentName = (SELECT TOP(1) experiment.ExpName FROM FinalPassYieldReport fnyReport INNER JOIN Experiment experiment ON fnyReport.ExpId = experiment.ExpId WHERE fnyReportId = @FinalPassYieldReportId);
	SET @fnyReportTableName = (SELECT TOP(1) fnyReportDbRptTableNameId FROM FinalPassYieldReport WHERE fnyReportId = @FinalPassYieldReportId);

	--Load Info fields into CSV String
	SET @FnyCsvInfoFields = (SELECT STUFF( (SELECT ',' + expField.ExpDbFieldNameId FROM FinalPassYieldInfoField fnyInfoField 
							INNER JOIN ExperimentField expField ON fnyInfoField.ExpFieldId = expField.ExpFieldId
							WHERE fnyInfoField.fnyReportId = fnyInfoFieldTemp.fnyReportId FOR XML PATH('')),1,1,'')
							FROM FinalPassYieldInfoField AS fnyInfoFieldTemp WHERE fnyInfoFieldTemp.fnyReportId = @FinalPassYieldReportId
							GROUP BY fnyInfoFieldTemp.fnyReportId );
							
	IF(@GroupedByTimeRange = 1)
		BEGIN
			--Time grouping Final Pass Yield logic		
			DECLARE @SqlTimeGroupFinalPassYield NVARCHAR(MAX);		--Final Pass Yield time group Sql Query
			SET @SqlTimeGroupFinalPassYield = CONCAT('SELECT RANK() OVER (PARTITION BY ',  @FnySerialNumberExpField, ' ORDER BY ', @FnyDateTimeExpField, ' DESC, RecordId DESC', 
													' , ', @FnySerialNumberExpField, ' ) SN_TIMES, RecordId, ',
													@FnyDateTimeExpField, ' AS ''fny_datetime_tmp'',', @FnySerialNumberExpField, ' AS ''fny_sn_tmp'',', @FnyResultExpField, ' AS ''fny_result_tmp'',', 
													@FnyCsvInfoFields, ' INTO ##TimeGroupFnyTmp FROM ', @ExperimentTableName);
			
			--Results will be stored into ##TimeGroupFnyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##TimeGroupFnyTmp') IS NOT NULL
				DROP TABLE ##TimeGroupFnyTmp;
								
			EXECUTE sp_executesql @SqlTimeGroupFinalPassYield;

			--Get Minutes of Time Range
			DECLARE @TimeRangeMins INT;
			SET @TimeRangeMins = (SELECT TOP(1) FnyTimeRangeMin FROM FinalPassYieldReport WHERE fnyReportId = @FinalPassYieldReportId);
			
			--Get Result Pass value
			DECLARE @ResultPassValue NVARCHAR(MAX);
			SET @ResultPassValue = (SELECT TOP(1) FnyPassResultValue FROM FinalPassYieldReport WHERE fnyReportId = @FinalPassYieldReportId);
			
			--Csv of RecordIds to insert after Final Pass Yield logic perform
			DECLARE @FnyTimeGroupRecordIds NVARCHAR(MAX);
			SET @FnyTimeGroupRecordIds = '';
			
			DECLARE @BatchingCounter INT;
			SET @BatchingCounter = 0;

			DECLARE @FirstRecordFoundId INT;
			DECLARE @PreviousDateTime DATETIME;
			DECLARE @SkipNextResult BIT;
			
			DECLARE @SnTimes INT;
			DECLARE @RecordId INT;
			DECLARE @FnyDatetime DATETIME;
			DECLARE @FnyResult VARCHAR(MAX);

			--Truncate FinalPass Yield Table  
			DECLARE @SqlTruncateTimeGroupFnyResults NVARCHAR(MAX); 
			SET @SqlTruncateTimeGroupFnyResults = CONCAT('TRUNCATE TABLE ', @fnyReportTableName);
			EXECUTE sp_executesql @SqlTruncateTimeGroupFnyResults;
			
			DECLARE FnyResultCursor CURSOR FOR SELECT ##TimeGroupFnyTmp.SN_TIMES, ##TimeGroupFnyTmp.RecordId, ##TimeGroupFnyTmp.fny_datetime_tmp, ##TimeGroupFnyTmp.fny_result_tmp FROM ##TimeGroupFnyTmp;
			OPEN FnyResultCursor

				FETCH NEXT FROM FnyResultCursor INTO @SnTimes, @RecordId, @FnyDatetime, @FnyResult;

				WHILE @@FETCH_STATUS = 0 BEGIN
					
					--First record of each Serial Number found
					IF(@SnTimes = 1)
						BEGIN
							SET @FnyTimeGroupRecordIds = CONCAT(@FnyTimeGroupRecordIds, ',''', @RecordId,'''');										
							SET @FirstRecordFoundId = @RecordId;
							SET @SkipNextResult = 0;
						END;

					--Repeated Serial Numbers
					IF(@SnTimes > 1 AND @SkipNextResult = 0 AND @PreviousDateTime IS NOT NULL)
						BEGIN													
							--If Date Diff is valid for range of Time Grouping 
							IF(DATEDIFF(MINUTE,@PreviousDateTime,@FnyDatetime) <= @TimeRangeMins)
								BEGIN
									IF(@FnyResult = @ResultPassValue) --First record saved as pass will be the valid
										BEGIN										
											SET @FnyTimeGroupRecordIds = REPLACE(@FnyTimeGroupRecordIds, CONCAT(',''', @FirstRecordFoundId,''''), '');										
											SET @FnyTimeGroupRecordIds = CONCAT(@FnyTimeGroupRecordIds, ',''', @RecordId,'''');
											SET @SkipNextResult = 1; --Skip next records found for same serial number
										END
									ELSE	--It will continue until a Pass record is found
										BEGIN
											SET @SkipNextResult = 0;
										END;
								END
							ELSE --After range limit is reached, next results for same serial number are ignored
								BEGIN
									SET @SkipNextResult = 1;
								END
						END
					
					SET @PreviousDateTime = @FnyDatetime;

					FETCH NEXT FROM FnyResultCursor INTO @SnTimes, @RecordId, @FnyDatetime, @FnyResult;
				
					--Inserting by batches of 1000 results to improve performance
					SET @BatchingCounter = @BatchingCounter + 1;

					--Only perform when time group analysis has finished
					IF(@SnTimes > 1)
						BEGIN
							SET @BatchingCounter = @BatchingCounter - 1;					
						END

					--Batching inserts of 1000 records, it will be triggered only when Time group analysis has finished (SnTimes = 1)
					IF(@SnTimes = 1 AND @BatchingCounter % 1000 = 0 AND @FnyTimeGroupRecordIds <> '')
						BEGIN
							--Remove first "," of CSV string that contains RecordIds
							SET @FnyTimeGroupRecordIds = Stuff(@FnyTimeGroupRecordIds, 1, 1, '');

							--Insert batch of 1000 records for Final Pass Yield Results (Grouped by Time Group)
							DECLARE @SqlInsertTimeGroupFnyResults NVARCHAR(MAX); 
							SET @SqlInsertTimeGroupFnyResults = CONCAT('INSERT INTO ', @fnyReportTableName, '(FnyRecordId, fny_experiment, fny_date_time, fny_serial_number,fny_result,', @FnyCsvInfoFields, ')',
												' SELECT RecordId, ''', @ExperimentName, ''',fny_datetime_tmp, fny_sn_tmp, fny_result_tmp,', 
													@FnyCsvInfoFields, ' FROM ##TimeGroupFnyTmp WHERE RecordId IN (', @FnyTimeGroupRecordIds, ')');
														
							SET @FullTimeGroupFnySqlInsert = CONCAT(@FullTimeGroupFnySqlInsert, ';', @SqlInsertTimeGroupFnyResults);

							SET @FnyTimeGroupRecordIds = '';
							SET @FirstRecordFoundId = NULL;
						END;
				END

			CLOSE FnyResultCursor;    
			DEALLOCATE FnyResultCursor;
			
			--Remove first "," of CSV string that contains RecordIds
			SET @FnyTimeGroupRecordIds = Stuff(@FnyTimeGroupRecordIds, 1, 1, '');

			--Insert last batch of Final Pass Yield Results (Grouped by Time Group)
			SET @SqlInsertTimeGroupFnyResults = CONCAT('INSERT INTO ', @fnyReportTableName, '(FnyRecordId, fny_experiment, fny_date_time, fny_serial_number,fny_result,', @FnyCsvInfoFields, ')',
												' SELECT RecordId, ''', @ExperimentName, ''',fny_datetime_tmp, fny_sn_tmp, fny_result_tmp,', 
													@FnyCsvInfoFields, ' FROM ##TimeGroupFnyTmp WHERE RecordId IN (', @FnyTimeGroupRecordIds, ')');
			
			IF(@FnyTimeGroupRecordIds <> '')
				BEGIN							
					SET @FullTimeGroupFnySqlInsert = CONCAT(@FullTimeGroupFnySqlInsert, ';', @SqlInsertTimeGroupFnyResults);
				END

			IF(@FullTimeGroupFnySqlInsert <> '')
				BEGIN
					EXECUTE sp_executesql @FullTimeGroupFnySqlInsert;
				END

			--Delete ##CommonFnyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##TimeGroupFnyTmp') IS NOT NULL
				DROP TABLE ##TimeGroupFnyTmp;
		END
	ELSE
		BEGIN	
			--Usual Final Pass Yield logic (Without time grouping logic)	
			DECLARE @SqlCommonFinalPassYield NVARCHAR(MAX);		--Final Pass Yield common Sql Query
			SET @SqlCommonFinalPassYield = CONCAT('SELECT RANK() OVER (PARTITION BY ',  @FnySerialNumberExpField, ' ORDER BY ', @FnyDateTimeExpField, ' DESC, RecordId DESC', 
													' , ', @FnySerialNumberExpField, ' ) SN_TIMES, RecordId, ',
													@FnyDateTimeExpField, ' AS ''fny_datetime_tmp'',', @FnySerialNumberExpField, ' AS ''fny_sn_tmp'',', 
													@FnyResultExpField, ' AS ''fny_result_tmp'',', 
													@FnyCsvInfoFields, ' INTO ##CommonFnyTmp FROM ', @ExperimentTableName);
			
			--Results will be stored into ##CommonFnyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##CommonFnyTmp') IS NOT NULL
				DROP TABLE ##CommonFnyTmp;
			EXECUTE sp_executesql @SqlCommonFinalPassYield;
			
			--Truncate Final Pass Yield Table  
			DECLARE @SqlTruncateFnyResults NVARCHAR(MAX); 
			SET @SqlTruncateFnyResults = CONCAT('TRUNCATE TABLE ', @fnyReportTableName);
			EXECUTE sp_executesql @SqlTruncateFnyResults;
			
			--Insert Final Pass Yield Results
			DECLARE @SqlInsertFnyResults NVARCHAR(MAX); 
			SET @SqlInsertFnyResults = CONCAT('INSERT INTO ', @fnyReportTableName, '(FnyRecordId, fny_experiment, fny_date_time, fny_serial_number,fny_result,', @FnyCsvInfoFields, ')',
												' SELECT RecordId, ''', @ExperimentName, ''',fny_datetime_tmp, fny_sn_tmp, fny_result_tmp,', 
													@FnyCsvInfoFields, ' FROM ##CommonFnyTmp WHERE SN_TIMES = 1;');
			EXECUTE sp_executesql @SqlInsertFnyResults;
			
			--Delete ##CommonFnyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##CommonFnyTmp') IS NOT NULL
				DROP TABLE ##CommonFnyTmp;					
		END
END