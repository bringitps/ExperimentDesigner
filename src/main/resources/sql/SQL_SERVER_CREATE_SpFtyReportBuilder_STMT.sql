CREATE PROCEDURE [spFtyReportBuilder]


@FirstTimeYieldReportId NVARCHAR(MAX)

AS

BEGIN
	
	DECLARE @FullTimeGroupFtySqlInsert NVARCHAR(MAX);	--Contains all SQL Insert Statements to be executed for time grouping
	SET @FullTimeGroupFtySqlInsert = '';

	DECLARE @GroupedByTimeRange BIT;					--Identifies if First Time Yield report requires time grouping logic	
	DECLARE @FtyFamilyExpField NVARCHAR(MAX);			--Family field of First Time Yield report
	DECLARE @FtyDateTimeExpField NVARCHAR(MAX);			--DateTime field of First Time Yield report
	DECLARE @FtySerialNumberExpField NVARCHAR(MAX);		--SerialNumber field of First Time Yield report
	DECLARE @FtyResultExpField NVARCHAR(MAX);			--Result field of First Time Yield report
	DECLARE @FtyCsvInfoFields NVARCHAR(MAX);			--Info columns CSV String
	DECLARE @ExperimentTableName NVARCHAR(MAX);			--Data Warehouse table name
	DECLARE @ExperimentName NVARCHAR(MAX);				--Experiment name
	DECLARE @FtyReportTableName NVARCHAR(MAX);			--First Time Yield table name
		
	--Check if First Time Yield Report requires time grouping logic
	SET @GroupedByTimeRange = (SELECT TOP(1) FtyGroupByTimeRange FROM FirstTimeYieldReport WHERE FtyReportId = @FirstTimeYieldReportId);
	SET @FtyDateTimeExpField = (SELECT TOP(1) expField.ExpDbFieldNameId FROM FirstTimeYieldReport ftyReport INNER JOIN ExperimentField expField ON ftyReport.DateTimeExpFieldId = expField.ExpFieldId WHERE FtyReportId = @FirstTimeYieldReportId);
	SET @FtyResultExpField = (SELECT TOP(1) expField.ExpDbFieldNameId FROM FirstTimeYieldReport ftyReport INNER JOIN ExperimentField expField ON ftyReport.ResultExpFieldId = expField.ExpFieldId WHERE FtyReportId = @FirstTimeYieldReportId);
	SET @FtySerialNumberExpField = (SELECT TOP(1) expField.ExpDbFieldNameId FROM FirstTimeYieldReport ftyReport INNER JOIN ExperimentField expField ON ftyReport.SerialNumberExpFieldId = expField.ExpFieldId WHERE FtyReportId = @FirstTimeYieldReportId);
	SET @ExperimentTableName = (SELECT TOP(1) experiment.ExpDbTableNameId FROM FirstTimeYieldReport ftyReport INNER JOIN Experiment experiment ON ftyReport.ExpId = experiment.ExpId WHERE FtyReportId = @FirstTimeYieldReportId);
	SET @ExperimentName = (SELECT TOP(1) experiment.ExpName FROM FirstTimeYieldReport ftyReport INNER JOIN Experiment experiment ON ftyReport.ExpId = experiment.ExpId WHERE FtyReportId = @FirstTimeYieldReportId);
	SET @FtyReportTableName = (SELECT TOP(1) FtyReportDbRptTableNameId FROM FirstTimeYieldReport WHERE FtyReportId = @FirstTimeYieldReportId);
	
	--Load Info fields into CSV String
	SET @FtyCsvInfoFields = (SELECT STUFF( (SELECT ',' + expField.ExpDbFieldNameId FROM FirstTimeYieldInfoField ftyInfoField 
							INNER JOIN ExperimentField expField ON ftyInfoField.ExpFieldId = expField.ExpFieldId
							WHERE ftyInfoField.FtyReportId = ftyInfoFieldTemp.FtyReportId FOR XML PATH('')),1,1,'')
							FROM FirstTimeYieldInfoField AS ftyInfoFieldTemp WHERE ftyInfoFieldTemp.FtyReportId = @FirstTimeYieldReportId
							GROUP BY ftyInfoFieldTemp.FtyReportId );
							
	IF(@GroupedByTimeRange = 1)
		BEGIN
			--Time grouping First Time Yield logic		
			DECLARE @SqlTimeGroupFirstTimeYield NVARCHAR(MAX);		--First Time Yield time group Sql Query
			SET @SqlTimeGroupFirstTimeYield = CONCAT('SELECT RANK() OVER (PARTITION BY ',  @FtySerialNumberExpField, ' ORDER BY ', @FtyDateTimeExpField,' DESC, RecordId DESC', 
													' , ', @FtySerialNumberExpField, ' ) SN_TIMES, RecordId, ',
													@FtyDateTimeExpField, ' AS ''fty_datetime_tmp'',', @FtySerialNumberExpField, ' AS ''fty_sn_tmp'',', 
													@FtyResultExpField, ' AS ''fty_result_tmp'',', 
													@FtyCsvInfoFields,' INTO ##TimeGroupFtyTmp FROM ', @ExperimentTableName);
			
			--Results will be stored into ##TimeGroupFtyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##TimeGroupFtyTmp') IS NOT NULL
				DROP TABLE ##TimeGroupFtyTmp;
								
			EXECUTE sp_executesql @SqlTimeGroupFirstTimeYield;

			--Get Minutes of Time Range
			DECLARE @TimeRangeMins INT;
			SET @TimeRangeMins = (SELECT TOP(1) FtyTimeRangeMin FROM FirstTimeYieldReport WHERE FtyReportId = @FirstTimeYieldReportId);
			
			--Get Result Pass value
			DECLARE @ResultPassValue NVARCHAR(MAX);
			SET @ResultPassValue = (SELECT TOP(1) FtyPassResultValue FROM FirstTimeYieldReport WHERE FtyReportId = @FirstTimeYieldReportId);
			
			--Csv of RecordIds to insert after First Time Yield logic perform
			DECLARE @FtyTimeGroupRecordIds NVARCHAR(MAX);
			SET @FtyTimeGroupRecordIds = '';
			
			DECLARE @BatchingCounter INT;
			SET @BatchingCounter = 0;

			DECLARE @FirstRecordFoundId INT;
			DECLARE @PreviousDateTime DATETIME;
			DECLARE @SkipNextResult BIT;
			
			DECLARE @SnTimes INT;
			DECLARE @RecordId INT;
			DECLARE @FtyDatetime DATETIME;
			DECLARE @FtyResult VARCHAR(MAX);

			--Truncate First Pass Yield Table  
			DECLARE @SqlTruncateTimeGroupFtyResults NVARCHAR(MAX); 
			SET @SqlTruncateTimeGroupFtyResults = CONCAT('TRUNCATE TABLE ', @FtyReportTableName);
			EXECUTE sp_executesql @SqlTruncateTimeGroupFtyResults;
			
			DECLARE FtyResultCursor CURSOR FOR SELECT ##TimeGroupFtyTmp.SN_TIMES, ##TimeGroupFtyTmp.RecordId, ##TimeGroupFtyTmp.fty_datetime_tmp, ##TimeGroupFtyTmp.fty_result_tmp FROM ##TimeGroupFtyTmp;
			OPEN FtyResultCursor

				FETCH NEXT FROM FtyResultCursor INTO @SnTimes, @RecordId, @FtyDatetime, @FtyResult;

				WHILE @@FETCH_STATUS = 0 BEGIN
					
					--First record of each Serial Number found
					IF(@SnTimes = 1)
						BEGIN
							SET @FtyTimeGroupRecordIds = CONCAT(@FtyTimeGroupRecordIds, ',''', @RecordId,'''');										
							SET @FirstRecordFoundId = @RecordId;
						END;

					--Repeated Serial Numbers
					IF(@SnTimes > 1 AND @SkipNextResult = 0 AND @PreviousDateTime IS NOT NULL)
						BEGIN													
							--If Date Diff is valid for range of Time Grouping 
							IF(DATEDIFF(MINUTE,@PreviousDateTime,@FtyDatetime) <= @TimeRangeMins)
								BEGIN
									IF(@FtyResult = @ResultPassValue) --First record saved as pass will be the valid
										BEGIN										
											SET @FtyTimeGroupRecordIds = REPLACE(@FtyTimeGroupRecordIds, CONCAT(',''', @FirstRecordFoundId,''''), '');										
											SET @FtyTimeGroupRecordIds = CONCAT(@FtyTimeGroupRecordIds, ',''', @RecordId,'''');
											SET @FirstRecordFoundId = @RecordId;
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
					
					SET @PreviousDateTime = @FtyDatetime;

					FETCH NEXT FROM FtyResultCursor INTO @SnTimes, @RecordId, @FtyDatetime, @FtyResult;
				
					--Inserting by batches of 1000 results to improve performance
					SET @BatchingCounter = @BatchingCounter + 1;

					--Only perform when time group analysis has finished
					IF(@SnTimes > 1)
						BEGIN
							SET @BatchingCounter = @BatchingCounter - 1;					
						END

					--Batching inserts of 1000 records, it will be triggered only when Time group analysis has finished (SnTimes = 1)
					IF(@SnTimes = 1 AND @BatchingCounter % 1000 = 0 AND @FtyTimeGroupRecordIds <> '')
						BEGIN
							--Remove first "," of CSV string that contains RecordIds
							SET @FtyTimeGroupRecordIds = Stuff(@FtyTimeGroupRecordIds, 1, 1, '');

							--Insert batch of 1000 records for First Time Yield Results (Grouped by Time Group)
							DECLARE @SqlInsertTimeGroupFtyResults NVARCHAR(MAX); 
							SET @SqlInsertTimeGroupFtyResults = CONCAT('INSERT INTO ', @FtyReportTableName, '(FtyRecordId, fty_experiment, fty_date_time, fty_serial_number,fty_result,', @FtyCsvInfoFields, ')',
												' SELECT RecordId, ''', @ExperimentName, ''',fty_datetime_tmp, fty_sn_tmp, fty_result_tmp,', 
													@FtyCsvInfoFields, ' FROM ##TimeGroupFtyTmp WHERE RecordId IN (', @FtyTimeGroupRecordIds, ')');
														
							SET @FullTimeGroupFtySqlInsert = CONCAT(@FullTimeGroupFtySqlInsert, ';', @SqlInsertTimeGroupFtyResults);

							SET @FtyTimeGroupRecordIds = '';
							SET @FirstRecordFoundId = NULL;
						END;
				END

			CLOSE FtyResultCursor;    
			DEALLOCATE FtyResultCursor;
			
			--Remove first "," of CSV string that contains RecordIds
			SET @FtyTimeGroupRecordIds = Stuff(@FtyTimeGroupRecordIds, 1, 1, '');

			--Insert last batch of First Time Yield Results (Grouped by Time Group)
			SET @SqlInsertTimeGroupFtyResults = CONCAT('INSERT INTO ', @FtyReportTableName, '(FtyRecordId, fty_experiment, fty_date_time, fty_serial_number,fty_result,', @FtyCsvInfoFields, ')',
												' SELECT RecordId, ''', @ExperimentName, ''',fty_datetime_tmp, fty_sn_tmp, fty_result_tmp,', 
													@FtyCsvInfoFields, ' FROM ##TimeGroupFtyTmp WHERE RecordId IN (', @FtyTimeGroupRecordIds, ')');
			
			IF(@FtyTimeGroupRecordIds <> '')
				BEGIN
					SET @FullTimeGroupFtySqlInsert = CONCAT(@FullTimeGroupFtySqlInsert, ';', @SqlInsertTimeGroupFtyResults);
				END

			IF(@FullTimeGroupFtySqlInsert <> '')
				BEGIN
					EXECUTE sp_executesql @FullTimeGroupFtySqlInsert;
				END

			--Delete ##CommonFpyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##TimeGroupFtyTmp') IS NOT NULL
				DROP TABLE ##TimeGroupFtyTmp; 
			--SELECT 'Time Grouped FTY';
		END
	ELSE
		BEGIN	
			
			--Usual First Time Yield logic (Without time grouping logic)	
			DECLARE @SqlCommonFirstTimeYield NVARCHAR(MAX);		--First Time Yield common Sql Query
			SET @SqlCommonFirstTimeYield = CONCAT('SELECT RANK() OVER (PARTITION BY ',  @FtySerialNumberExpField, ' ORDER BY ', @FtyDateTimeExpField,' DESC, RecordId DESC', 
													' , ', @FtySerialNumberExpField, ' ) SN_TIMES, RecordId, ',
													@FtyDateTimeExpField, ' AS ''fty_datetime_tmp'',', @FtySerialNumberExpField, ' AS ''fty_sn_tmp'',', 
													@FtyResultExpField, ' AS ''fty_result_tmp'',', 
													@FtyCsvInfoFields, ' INTO ##CommonFtyTmp FROM ', @ExperimentTableName);
			--Results will be stored into ##CommonFtyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##CommonFtyTmp') IS NOT NULL
				DROP TABLE ##CommonFtyTmp;
			EXECUTE sp_executesql @SqlCommonFirstTimeYield;
			
			--Truncate First Time Yield Table  
			DECLARE @SqlTruncateFtyResults NVARCHAR(MAX); 
			SET @SqlTruncateFtyResults = CONCAT('TRUNCATE TABLE ', @FtyReportTableName);
			EXECUTE sp_executesql @SqlTruncateFtyResults;
			
			--Insert First Time Yield Results
			DECLARE @SqlInsertFtyResults NVARCHAR(MAX); 
			SET @SqlInsertFtyResults = CONCAT('INSERT INTO ', @FtyReportTableName, '(FtyRecordId, fty_experiment, fty_date_time, fty_serial_number,fty_result,', @FtyCsvInfoFields, ')',
												' SELECT RecordId, ''', @ExperimentName, ''',fty_datetime_tmp, fty_sn_tmp, fty_result_tmp,', 
													@FtyCsvInfoFields, ' FROM ##CommonFtyTmp WHERE SN_TIMES = 1;');
			EXECUTE sp_executesql @SqlInsertFtyResults;
			
			--Delete ##CommonFtyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##CommonFtyTmp') IS NOT NULL
				DROP TABLE ##CommonFtyTmp;	
		END
END