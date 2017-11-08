CREATE PROCEDURE [spFpyReportBuilder]


@FirstPassYieldReportId NVARCHAR(MAX)

AS

BEGIN
	
	DECLARE @GroupedByTimeRange BIT;					--Identifies if First Pass Yield report requires time grouping logic	
	DECLARE @FpyFamilyExpField NVARCHAR(MAX);			--Family field of First Pass Yield report
	DECLARE @FpyDateTimeExpField NVARCHAR(MAX);			--DateTime field of First Pass Yield report
	DECLARE @FpySerialNumberExpField NVARCHAR(MAX);		--SerialNumber field of First Pass Yield report
	DECLARE @FpyResultExpField NVARCHAR(MAX);			--Result field of First Pass Yield report
	DECLARE @FpyCsvInfoFields NVARCHAR(MAX);			--Info columns CSV String
	DECLARE @ExperimentTableName NVARCHAR(MAX);			--Data Warehouse table name
	DECLARE @ExperimentName NVARCHAR(MAX);				--Experiment name
	DECLARE @FpyReportTableName NVARCHAR(MAX);			--First Pass Yield table name
		
	--Check if First Pass Yield Report requires time grouping logic
	SET @GroupedByTimeRange = (SELECT TOP(1) FpyGroupByTimeRange FROM FirstPassYieldReport WHERE FpyReportId = @FirstPassYieldReportId);
	SET @FpyDateTimeExpField = (SELECT TOP(1) expField.ExpDbFieldNameId FROM FirstPassYieldReport fpyReport INNER JOIN ExperimentField expField ON fpyReport.DateTimeExpFieldId = expField.ExpFieldId WHERE FpyReportId = @FirstPassYieldReportId);
	SET @FpyResultExpField = (SELECT TOP(1) expField.ExpDbFieldNameId FROM FirstPassYieldReport fpyReport INNER JOIN ExperimentField expField ON fpyReport.ResultExpFieldId = expField.ExpFieldId WHERE FpyReportId = @FirstPassYieldReportId);
	SET @FpySerialNumberExpField = (SELECT TOP(1) expField.ExpDbFieldNameId FROM FirstPassYieldReport fpyReport INNER JOIN ExperimentField expField ON fpyReport.SerialNumberExpFieldId = expField.ExpFieldId WHERE FpyReportId = @FirstPassYieldReportId);
	SET @ExperimentTableName = (SELECT TOP(1) experiment.ExpDbTableNameId FROM FirstPassYieldReport fpyReport INNER JOIN Experiment experiment ON fpyReport.ExpId = experiment.ExpId WHERE FpyReportId = @FirstPassYieldReportId);
	SET @ExperimentName = (SELECT TOP(1) experiment.ExpName FROM FirstPassYieldReport fpyReport INNER JOIN Experiment experiment ON fpyReport.ExpId = experiment.ExpId WHERE FpyReportId = @FirstPassYieldReportId);
	SET @FpyReportTableName = (SELECT TOP(1) FpyReportDbRptTableNameId FROM FirstPassYieldReport WHERE FpyReportId = @FirstPassYieldReportId);

	--Load Info fields into CSV String
	SET @FpyCsvInfoFields = (SELECT STUFF( (SELECT ',' + expField.ExpDbFieldNameId FROM FirstPassYieldInfoField fpyInfoField 
							INNER JOIN ExperimentField expField ON fpyInfoField.ExpFieldId = expField.ExpFieldId
							WHERE fpyInfoField.FpyReportId = fpyInfoFieldTemp.FpyReportId FOR XML PATH('')),1,1,'')
							FROM FirstPassYieldInfoField AS fpyInfoFieldTemp WHERE fpyInfoFieldTemp.FpyReportId = @FirstPassYieldReportId
							GROUP BY fpyInfoFieldTemp.FpyReportId );
							
	IF(@GroupedByTimeRange = 1)
		BEGIN
			--Time grouping First Pass Yield logic		
			DECLARE @SqlTimeGroupFirstPassYield NVARCHAR(MAX);		--First Pass Yield time group Sql Query
			SET @SqlTimeGroupFirstPassYield = CONCAT('SELECT RANK() OVER (PARTITION BY ',  @FpySerialNumberExpField, ' ORDER BY ', @FpyDateTimeExpField, ', RecordId', 
													' , ', @FpySerialNumberExpField, ' ) SN_TIMES, RecordId, ',
													@FpyDateTimeExpField, ' AS ''fpy_datetime_tmp'',', @FpySerialNumberExpField, ' AS ''fpy_sn_tmp'',', @FpyResultExpField, ' AS ''fpy_result_tmp'',', 
													@FpyCsvInfoFields, ' INTO ##TimeGroupFpyTmp FROM ', @ExperimentTableName);
			
			--Results will be stored into ##TimeGroupFpyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##TimeGroupFpyTmp') IS NOT NULL
				DROP TABLE ##TimeGroupFpyTmp;
								
			EXECUTE sp_executesql @SqlTimeGroupFirstPassYield;

			--Get Minutes of Time Range
			DECLARE @TimeRangeMins INT;
			SET @TimeRangeMins = (SELECT TOP(1) FpyTimeRangeMin FROM FirstPassYieldReport WHERE FpyReportId = @FirstPassYieldReportId);
			
			--Get Result Pass value
			DECLARE @ResultPassValue NVARCHAR(MAX);
			SET @ResultPassValue = (SELECT TOP(1) FpyPassResultValue FROM FirstPassYieldReport WHERE FpyReportId = @FirstPassYieldReportId);
			
			--Csv of RecordIds to insert after First Pass Yield logic perform
			DECLARE @FpyTimeGroupRecordIds NVARCHAR(MAX);
			SET @FpyTimeGroupRecordIds = '';
			
			DECLARE @BatchingCounter INT;
			SET @BatchingCounter = 0;

			DECLARE @FirstRecordFoundId INT;
			DECLARE @PreviousDateTime DATETIME;
			DECLARE @SkipNextResult BIT;
			
			DECLARE @SnTimes INT;
			DECLARE @RecordId INT;
			DECLARE @FpyDatetime DATETIME;
			DECLARE @FpyResult VARCHAR(MAX);

			--Truncate First Pass Yield Table  
			DECLARE @SqlTruncateTimeGroupFpyResults NVARCHAR(MAX); 
			SET @SqlTruncateTimeGroupFpyResults = CONCAT('TRUNCATE TABLE ', @FpyReportTableName);
			EXECUTE sp_executesql @SqlTruncateTimeGroupFpyResults;
			
			DECLARE FpyResultCursor CURSOR FOR SELECT ##TimeGroupFpyTmp.SN_TIMES, ##TimeGroupFpyTmp.RecordId, ##TimeGroupFpyTmp.fpy_datetime_tmp, ##TimeGroupFpyTmp.fpy_result_tmp FROM ##TimeGroupFpyTmp;
			OPEN FpyResultCursor

				FETCH NEXT FROM FpyResultCursor INTO @SnTimes, @RecordId, @FpyDatetime, @FpyResult;

				WHILE @@FETCH_STATUS = 0 BEGIN
					
					--First record of each Serial Number found
					IF(@SnTimes = 1)
						BEGIN
							SET @FpyTimeGroupRecordIds = CONCAT(@FpyTimeGroupRecordIds, ',''', @RecordId,'''');										
							SET @FirstRecordFoundId = @RecordId;
							SET @SkipNextResult = 0;
						END;

					--Repeated Serial Numbers
					IF(@SnTimes > 1 AND @SkipNextResult = 0 AND @PreviousDateTime IS NOT NULL)
						BEGIN													
							--If Date Diff is valid for range of Time Grouping 
							IF(DATEDIFF(MINUTE,@PreviousDateTime,@FpyDatetime) <= @TimeRangeMins)
								BEGIN
									IF(@FpyResult = @ResultPassValue) --First record saved as pass will be the valid
										BEGIN										
											SET @FpyTimeGroupRecordIds = REPLACE(@FpyTimeGroupRecordIds, CONCAT(',''', @FirstRecordFoundId,''''), '');										
											SET @FpyTimeGroupRecordIds = CONCAT(@FpyTimeGroupRecordIds, ',''', @RecordId,'''');
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
					
					SET @PreviousDateTime = @FpyDatetime;

					FETCH NEXT FROM FpyResultCursor INTO @SnTimes, @RecordId, @FpyDatetime, @FpyResult;
				
					--Inserting by batches of 1000 results to improve performance
					SET @BatchingCounter = @BatchingCounter + 1;

					--Only perform when time group analysis has finished
					IF(@SnTimes > 1)
						BEGIN
							SET @BatchingCounter = @BatchingCounter - 1;					
						END

					--Batching inserts of 1000 records, it will be triggered only when Time group analysis has finished (SnTimes = 1)
					IF(@SnTimes = 1 AND @BatchingCounter % 1000 = 0 AND @FpyTimeGroupRecordIds <> '')
						BEGIN
							--Remove first "," of CSV string that contains RecordIds
							SET @FpyTimeGroupRecordIds = Stuff(@FpyTimeGroupRecordIds, 1, 1, '');

							--Insert batch of 1000 records for First Pass Yield Results (Grouped by Time Group)
							DECLARE @SqlInsertTimeGroupFpyResults NVARCHAR(MAX); 
							SET @SqlInsertTimeGroupFpyResults = CONCAT('INSERT INTO ', @FpyReportTableName, '(FpyRecordId, fpy_experiment, fpy_date_time, fpy_serial_number,fpy_result,', @FpyCsvInfoFields, ')',
												' SELECT RecordId, ''', @ExperimentName, ''',fpy_datetime_tmp, fpy_sn_tmp, fpy_result_tmp,', 
													@FpyCsvInfoFields, ' FROM ##TimeGroupFpyTmp WHERE RecordId IN (', @FpyTimeGroupRecordIds, ')');
														
							EXECUTE sp_executesql @SqlInsertTimeGroupFpyResults;
							SET @FpyTimeGroupRecordIds = '';
							SET @FirstRecordFoundId = NULL;
						END;
				END

			CLOSE FpyResultCursor;    
			DEALLOCATE FpyResultCursor;
			
			--Remove first "," of CSV string that contains RecordIds
			SET @FpyTimeGroupRecordIds = Stuff(@FpyTimeGroupRecordIds, 1, 1, '');

			--Insert last batch of First Pass Yield Results (Grouped by Time Group)
			SET @SqlInsertTimeGroupFpyResults = CONCAT('INSERT INTO ', @FpyReportTableName, '(FpyRecordId, fpy_experiment, fpy_date_time, fpy_serial_number,fpy_result,', @FpyCsvInfoFields, ')',
												' SELECT RecordId, ''', @ExperimentName, ''',fpy_datetime_tmp, fpy_sn_tmp, fpy_result_tmp,', 
													@FpyCsvInfoFields, ' FROM ##TimeGroupFpyTmp WHERE RecordId IN (', @FpyTimeGroupRecordIds, ')');
			
			IF(@FpyTimeGroupRecordIds <> '')
				BEGIN
					EXECUTE sp_executesql @SqlInsertTimeGroupFpyResults;
				END

			--Delete ##CommonFpyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##TimeGroupFpyTmp') IS NOT NULL
				DROP TABLE ##TimeGroupFpyTmp;
		END
	ELSE
		BEGIN	
			--Usual First Pass Yield logic (Without time grouping logic)	
			DECLARE @SqlCommonFirstPassYield NVARCHAR(MAX);		--First Pass Yield common Sql Query
			SET @SqlCommonFirstPassYield = CONCAT('SELECT RANK() OVER (PARTITION BY ',  @FpySerialNumberExpField, ' ORDER BY ', @FpyDateTimeExpField, ', RecordId', 
													' , ', @FpySerialNumberExpField, ' ) SN_TIMES, RecordId, ',
													@FpyDateTimeExpField, ' AS ''fpy_datetime_tmp'',', @FpySerialNumberExpField, ' AS ''fpy_sn_tmp'',', 
													@FpyResultExpField, ' AS ''fpy_result_tmp'',', 
													@FpyCsvInfoFields, ' INTO ##CommonFpyTmp FROM ', @ExperimentTableName);
			
			--Results will be stored into ##CommonFpyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##CommonFpyTmp') IS NOT NULL
				DROP TABLE ##CommonFpyTmp;
			EXECUTE sp_executesql @SqlCommonFirstPassYield;
			
			--Truncate First Pass Yield Table  
			DECLARE @SqlTruncateFpyResults NVARCHAR(MAX); 
			SET @SqlTruncateFpyResults = CONCAT('TRUNCATE TABLE ', @FpyReportTableName);
			EXECUTE sp_executesql @SqlTruncateFpyResults;
			
			--Insert First Pass Yield Results
			DECLARE @SqlInsertFpyResults NVARCHAR(MAX); 
			SET @SqlInsertFpyResults = CONCAT('INSERT INTO ', @FpyReportTableName, '(FpyRecordId, fpy_experiment, fpy_date_time, fpy_serial_number,fpy_result,', @FpyCsvInfoFields, ')',
												' SELECT RecordId, ''', @ExperimentName, ''',fpy_datetime_tmp, fpy_sn_tmp, fpy_result_tmp,', 
													@FpyCsvInfoFields, ' FROM ##CommonFpyTmp WHERE SN_TIMES = 1;');
			EXECUTE sp_executesql @SqlInsertFpyResults;
			
			--Delete ##CommonFpyTmp Temporal Table
			IF OBJECT_ID('TEMPDB.DBO.##CommonFpyTmp') IS NOT NULL
				DROP TABLE ##CommonFpyTmp;					
		END
END