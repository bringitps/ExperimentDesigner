CREATE  PROCEDURE [dbo].[spHorizontalReport]

@Report_ID NVARCHAR(MAX)

AS

BEGIN

	DECLARE @ReportTableName NVARCHAR(MAX);--Variable to store the Table Name
	DECLARE @DropTable NVARCHAR(MAX);--Drop existing table
	DECLARE @DropTableRpt NVARCHAR(MAX);
	DECLARE @KeyField NVARCHAR(MAX);

	SELECT @ReportTableName = (SELECT VwHorizontalRptDbTableNameId FROM ViewHorizontalReport WHERE VwHorizontalRptId = @Report_ID)--Get table name
	SELECT @KeyField = STUFF(--
                        (
							
							SELECT CONCAT(vwHorizontalRptKeyColumnDbId,' ',vwHorizontalRptKeyColumnDataType,', ') FROM ViewHorizontalReport WHERE VwHorizontalRptId = @Report_ID

							FOR xml path('')
						), 1, 0, '')

	
	IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES 
           WHERE TABLE_NAME = @ReportTableName)
	BEGIN
		SELECT @DropTable = 'DROP TABLE ' + @ReportTableName--Variable to store the drop statement
		--SELECT @DropTableRpt = 'DROP TABLE ' + @ReportTableName + 'RepTable'
		EXECUTE sp_executesql @DropTable;--execute drop
		PRINT 'Drop table: ' + @DropTableRpt
		--EXECUTE sp_executesql @DropTableRpt;
	END

	DECLARE @ReportColums NVARCHAR(MAX);

	SELECT @ReportColums = STUFF(--
                        (
							SELECT CONCAT(VwHorizontalRptColumnDbId,' ',VwHorizontalRptColumnDataType,',') FROM ViewHorizontalReportColumn WHERE VwHorizontalReportId = @Report_ID
							FOR xml path('')
						), 1, 0, '')
	
	DECLARE @CreateTable NVARCHAR(MAX);
	SELECT @CreateTable = 'CREATE TABLE ' + @ReportTableName + '(vwRecordId int IDENTITY(1,1) NOT NULL PRIMARY KEY,' + @KeyField + LEFT(@ReportColums, LEN(@ReportColums) - 1) + ')'--Get create table estatement
	--print @CreateTable
	EXECUTE sp_executesql @CreateTable;

	CREATE TABLE #KeysValues(
		Keys VARCHAR(MAX)
	)

	DECLARE @SelExpKeys NVARCHAR(MAX)
	DECLARE @SelFpyKeys NVARCHAR(MAX)
	DECLARE @SelFtyKeys NVARCHAR(MAX)
	DECLARE @SelFnyKeys NVARCHAR(MAX)

	SELECT @SelExpKeys = STUFF(--
                        (

							SELECT CONCAT(' INSERT INTO #KeysValues SELECT DISTINCT ', ExpField.ExpDbFieldNameId, ' FROM ',Expe.ExpDbTableNameId) FROM ViewHorizontalReportByExperiment
							LEFT JOIN Experiment AS Expe ON ViewHorizontalReportByExperiment.ExperimentId = Expe.ExpId
							left join ExperimentField AS ExpField ON ViewHorizontalReportByExperiment.ExperimentKeyFieldId = ExpField.ExpFieldId
							WHERE ViewHorizontalReportByExperiment.VwHorizontalReportId = @Report_ID
							FOR xml path('')
						), 1, 0, '')

	SELECT @SelFpyKeys = STUFF(--
                        (

							SELECT CONCAT(' INSERT INTO #KeysValues SELECT DISTINCT ', 
							CASE 

								WHEN ViewHorizontalReportByFpyRpt.VwHorizontalFpyKeyIsDateTimeExpField = 1 THEN ' fpy_date_time '
								WHEN ViewHorizontalReportByFpyRpt.VwHorizontalFpyKeyIsResultExpField = 1 THEN ' fpy_result '
								WHEN ViewHorizontalReportByFpyRpt.VwHorizontalFpyKeyIsSNExpField = 1 THEN ' fpy_serial_number '

							END,
							ExperimentField.ExpDbFieldNameId, 
							' FROM ',fpy.FpyReportDbRptTableNameId) FROM ViewHorizontalReportByFpyRpt
							LEFT JOIN FirstPassYieldReport AS fpy ON ViewHorizontalReportByFpyRpt.fpyRptId = fpy.FpyReportId
							left join FirstPassYieldInfoField AS fpyInfoField ON ViewHorizontalReportByFpyRpt.FpyKeyInfoFieldId = fpyInfoField.FpyInfoFieldId
							left join ExperimentField ON fpyInfoField.ExpFieldId = ExperimentField.ExpFieldId
							WHERE ViewHorizontalReportByFpyRpt.VwHorizontalReportId = @Report_ID
							FOR xml path('')
						), 1, 0, '')

	SELECT @SelFtyKeys = STUFF(--
                        (

							SELECT CONCAT(' INSERT INTO #KeysValues SELECT DISTINCT ', 
							CASE 

								WHEN ViewHorizontalReportByftyRpt.VwHorizontalftyKeyIsDateTimeExpField = 1 THEN ' fty_date_time '
								WHEN ViewHorizontalReportByftyRpt.VwHorizontalftyKeyIsResultExpField = 1 THEN ' fty_result '
								WHEN ViewHorizontalReportByftyRpt.VwHorizontalftyKeyIsSNExpField = 1 THEN ' fty_serial_number '

							END,
							ExperimentField.ExpDbFieldNameId, 
							' FROM ',fty.ftyReportDbRptTableNameId) FROM ViewHorizontalReportByftyRpt
							LEFT JOIN FirstTimeYieldReport AS fty ON ViewHorizontalReportByftyRpt.ftyRptId = fty.ftyReportId
							left join FirstTimeYieldInfoField AS ftyInfoField ON ViewHorizontalReportByftyRpt.ftyKeyInfoFieldId = ftyInfoField.ftyInfoFieldId
							left join ExperimentField ON ftyInfoField.ExpFieldId = ExperimentField.ExpFieldId
							WHERE ViewHorizontalReportByftyRpt.VwHorizontalReportId = @Report_ID
							FOR xml path('')
						), 1, 0, '')

	SELECT @SelFnyKeys = STUFF(--
                        (

							SELECT CONCAT(' INSERT INTO #KeysValues SELECT DISTINCT ', 
							CASE 

								WHEN ViewHorizontalReportByfnyRpt.VwHorizontalfnyKeyIsDateTimeExpField = 1 THEN ' fny_date_time '
								WHEN ViewHorizontalReportByfnyRpt.VwHorizontalfnyKeyIsResultExpField = 1 THEN ' fny_result '
								WHEN ViewHorizontalReportByfnyRpt.VwHorizontalfnyKeyIsSNExpField = 1 THEN ' fny_serial_number '

							END,
							ExperimentField.ExpDbFieldNameId, 
							' FROM ',fny.fnyReportDbRptTableNameId) FROM ViewHorizontalReportByfnyRpt
							LEFT JOIN FinalPassYieldReport AS fny ON ViewHorizontalReportByfnyRpt.fnyRptId = fny.fnyReportId
							left join FinalPassYieldInfoField AS fnyInfoField ON ViewHorizontalReportByfnyRpt.fnyKeyInfoFieldId = fnyInfoField.fnyInfoFieldId
							left join ExperimentField ON fnyInfoField.ExpFieldId = ExperimentField.ExpFieldId
							WHERE ViewHorizontalReportByfnyRpt.VwHorizontalReportId = @Report_ID
							FOR xml path('')
						), 1, 0, '')
	
	print 'insert exp keys: '
	EXECUTE sp_executesql @SelExpKeys
	print 'insert fpy keys: '
	EXECUTE sp_executesql @SelFpyKeys
	print 'insert fty keys: '
	EXECUTE sp_executesql @SelFtyKeys
	print 'insert fny keys: '
	EXECUTE sp_executesql @SelFnyKeys
	
	CREATE TABLE #KeysValDisc(
		Keys VARCHAR(MAX)
	)
	
	
	print 'insert Genaral keys: '
	INSERT INTO #KeysValDisc SELECT DISTINCT Keys FROM #KeysValues

	DECLARE @InsertColumns NVARCHAR(MAX)
	SELECT @InsertColumns = STUFF(--
                        (

			SELECT CONCAT(Expe.ExpDbTableNameId,Fpy.FpyReportDbRptTableNameId,'.'
				,CASE 
					WHEN ViewHorizontalReportColumn.VwHorizontalFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
					WHEN ViewHorizontalReportColumn.VwHorizontalFnyIsResultExpField = 1 THEN 'fny_result'
					WHEN ViewHorizontalReportColumn.VwHorizontalFnyIsSNExpField = 1 THEN 'fny_serial_number'
					WHEN ViewHorizontalReportColumn.VwHorizontalFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
					WHEN ViewHorizontalReportColumn.VwHorizontalFpyIsResultExpField = 1 THEN 'fpy_result'
					WHEN ViewHorizontalReportColumn.VwHorizontalFpyIsSNExpField = 1 THEN 'fpy_serial_number'
					WHEN ViewHorizontalReportColumn.VwHorizontalFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
					WHEN ViewHorizontalReportColumn.VwHorizontalFtyIsResultExpField = 1 THEN 'fty_result'
					WHEN ViewHorizontalReportColumn.VwHorizontalFtyIsSNExpField = 1 THEN 'fty_serial_number'
				END
				,ExpFieldFPY.ExpDbFieldNameId,ExpFieldFNY.ExpDbFieldNameId,ExpFieldFTY.ExpDbFieldNameId
				,ExpField.ExpDbFieldNameId,' AS ',ViewHorizontalReportColumn.VwHorizontalRptColumnDbId, ', ') FROM ViewHorizontalReportColumn
			left join Experiment AS Expe ON ViewHorizontalReportColumn.ExperimentId = Expe.ExpId
			LEFT JOIN ExperimentField AS ExpField ON ViewHorizontalReportColumn.ExpFieldId = ExpField.ExpFieldId
			LEFT JOIN FirstPassYieldReport AS Fpy ON ViewHorizontalReportColumn.fpyRptId = Fpy.FpyReportId
			LEFT JOIN FirstPassYieldInfoField AS FpyInf ON ViewHorizontalReportColumn.FpyInfoFieldId = FpyInf.FpyInfoFieldId
			left join ExperimentField AS ExpFieldFPY ON FpyInf.ExpFieldId = ExpFieldFPY.ExpFieldId
			LEFT JOIN FinalPassYieldReport AS Fny ON ViewHorizontalReportColumn.fnyRptId = Fny.FnyReportId
			LEFT JOIN FinalPassYieldInfoField AS FnyInf ON ViewHorizontalReportColumn.FnyInfoFieldId = FnyInf.FnyInfoFieldId
			left join ExperimentField AS ExpFieldFNY ON FnyInf.ExpFieldId = ExpFieldFNY.ExpFieldId
			LEFT JOIN FirstTimeYieldReport AS Fty ON ViewHorizontalReportColumn.FtyRptId = Fty.FtyReportId
			LEFT JOIN FirstTimeYieldInfoField AS FtyInf ON ViewHorizontalReportColumn.FtyInfoFieldId = FtyInf.FtyInfoFieldId
			left join ExperimentField AS ExpFieldFTY ON FtyInf.ExpFieldId = ExpFieldFTY.ExpFieldId
			WHERE VwHorizontalReportId = @Report_ID
			FOR xml path('')
						), 1, 0, '')
	
	DECLARE @ExpJoins NVARCHAR(MAX)
	DECLARE @ExpJoinsCount INT
	
	SELECT @ExpJoinsCount = (
			SELECT COUNT(*) FROM ViewHorizontalReport
			inner JOIN ViewHorizontalReportByExperiment AS HorzontalExp ON ViewHorizontalReport.VwHorizontalRptId = HorzontalExp.VwHorizontalReportId
			LEFT JOIN Experiment AS Expe ON HorzontalExp.ExperimentId = Expe.ExpId
			left join ExperimentField AS ExpField ON HorzontalExp.ExperimentKeyFieldId = ExpField.ExpFieldId
			WHERE VwHorizontalRptId = @Report_ID
	)
	IF(@ExpJoinsCount > 0)
	BEGIN
		SELECT @ExpJoins = STUFF(--
                        (

			SELECT CONCAT(' LEFT JOIN ', Expe.ExpDbTableNameId, ' ON #KeysValDisc.Keys = ',Expe.ExpDbTableNameId,'.',ExpField.ExpDbFieldNameId) FROM ViewHorizontalReport
			inner JOIN ViewHorizontalReportByExperiment AS HorzontalExp ON ViewHorizontalReport.VwHorizontalRptId = HorzontalExp.VwHorizontalReportId
			LEFT JOIN Experiment AS Expe ON HorzontalExp.ExperimentId = Expe.ExpId
			left join ExperimentField AS ExpField ON HorzontalExp.ExperimentKeyFieldId = ExpField.ExpFieldId
			WHERE VwHorizontalRptId = @Report_ID

						FOR xml path('')
						), 1, 0, '')
	END
	ELSE
	BEGIN
		SELECT @ExpJoins = ''
	END

	DECLARE @FpyJoins NVARCHAR(MAX)
	DECLARE @FpyJoinsCount INT
	SELECT @FpyJoinsCount = (SELECT COUNT(*) FROM ViewHorizontalReport
						  inner JOIN ViewHorizontalReportByFpyRpt AS HorzontalfPY ON ViewHorizontalReport.VwHorizontalRptId = HorzontalfPY.VwHorizontalReportId
						  LEFT JOIN FirstPassYieldReport AS fPY ON HorzontalfPY.fpyRptId = fPY.FpyReportId
						  LEFT JOIN FirstPassYieldInfoField AS fPYiNFO ON HorzontalfPY.FpyKeyInfoFieldId = fPYiNFO.FpyInfoFieldId
						  left join ExperimentField ON fPYiNFO.ExpFieldId = ExperimentField.ExpFieldId
						  WHERE VwHorizontalRptId = @Report_ID)
	IF(@FpyJoinsCount > 0)
	BEGIN
		SELECT @FpyJoins = STUFF(--
                        (

						  SELECT CONCAT(' LEFT JOIN ', fPY.FpyReportDbRptTableNameId, ' ON #KeysValDisc.Keys = ',fPY.FpyReportDbRptTableNameId,'.',
							CASE
								WHEN HorzontalfPY.VwHorizontalFpyKeyIsDateTimeExpField = 1 THEN 'fpy_date_time'
								WHEN HorzontalfPY.VwHorizontalFpyKeyIsResultExpField = 1 THEN 'fpy_result'
								WHEN HorzontalfPY.VwHorizontalFpyKeyIsSNExpField = 1 THEN 'fpy_serial_number'
							END, 
							ExperimentField.ExpDbFieldNameId) FROM ViewHorizontalReport
						  inner JOIN ViewHorizontalReportByFpyRpt AS HorzontalfPY ON ViewHorizontalReport.VwHorizontalRptId = HorzontalfPY.VwHorizontalReportId
						  LEFT JOIN FirstPassYieldReport AS fPY ON HorzontalfPY.fpyRptId = fPY.FpyReportId
						  LEFT JOIN FirstPassYieldInfoField AS fPYiNFO ON HorzontalfPY.FpyKeyInfoFieldId = fPYiNFO.FpyInfoFieldId
						  left join ExperimentField ON fPYiNFO.ExpFieldId = ExperimentField.ExpFieldId
						  WHERE VwHorizontalRptId = @Report_ID
						     
						FOR xml path('')
						), 1, 0, '')
	END
	ELSE
	BEGIN
		SELECT @FpyJoins = ''
	END

	DECLARE @FtyJoins NVARCHAR(MAX)
	DECLARE @FtyJoinsCount INT
	SELECT @FtyJoinsCount = (SELECT COUNT(*) FROM ViewHorizontalReport
						  inner JOIN ViewHorizontalReportByFtyRpt AS HorzontalFty ON ViewHorizontalReport.VwHorizontalRptId = HorzontalFty.VwHorizontalReportId
						  LEFT JOIN FirstTimeYieldReport AS Fty ON HorzontalFty.FtyRptId = Fty.FtyReportId
						  LEFT JOIN FirstTimeYieldInfoField AS FtyiNFO ON HorzontalFty.FtyKeyInfoFieldId = FtyiNFO.FtyInfoFieldId
						  left join ExperimentField ON FtyiNFO.ExpFieldId = ExperimentField.ExpFieldId
						  WHERE VwHorizontalRptId = @Report_ID)
	IF(@FtyJoinsCount > 0)
	BEGIN
		SELECT @FtyJoins = STUFF(--
                        (

						  SELECT CONCAT(' LEFT JOIN ', Fty.FtyReportDbRptTableNameId, ' ON #KeysValDisc.Keys = ',Fty.FtyReportDbRptTableNameId,'.',
							CASE
								WHEN HorzontalFty.VwHorizontalFtyKeyIsDateTimeExpField = 1 THEN 'Fty_date_time'
								WHEN HorzontalFty.VwHorizontalFtyKeyIsResultExpField = 1 THEN 'Fty_result'
								WHEN HorzontalFty.VwHorizontalFtyKeyIsSNExpField = 1 THEN 'Fty_serial_number'
							END, 
							ExperimentField.ExpDbFieldNameId) FROM ViewHorizontalReport
						  inner JOIN ViewHorizontalReportByFtyRpt AS HorzontalFty ON ViewHorizontalReport.VwHorizontalRptId = HorzontalFty.VwHorizontalReportId
						  LEFT JOIN FirstTimeYieldReport AS Fty ON HorzontalFty.FtyRptId = Fty.FtyReportId
						  LEFT JOIN FirstTimeYieldInfoField AS FtyiNFO ON HorzontalFty.FtyKeyInfoFieldId = FtyiNFO.FtyInfoFieldId
						  left join ExperimentField ON FtyiNFO.ExpFieldId = ExperimentField.ExpFieldId
						  WHERE VwHorizontalRptId = @Report_ID
						     
						FOR xml path('')
						), 1, 0, '')
	END
	ELSE
	BEGIN
		SELECT @FtyJoins = ''
	END

	DECLARE @FnyJoins NVARCHAR(MAX)
	DECLARE @FnyJoinsCount INT
	SELECT @FnyJoinsCount = (SELECT COUNT(*) FROM ViewHorizontalReport
						  inner JOIN ViewHorizontalReportByFnyRpt AS HorzontalFny ON ViewHorizontalReport.VwHorizontalRptId = HorzontalFny.VwHorizontalReportId
						  LEFT JOIN FinalPassYieldReport AS Fny ON HorzontalFny.FnyRptId = Fny.FnyReportId
						  LEFT JOIN FinalPassYieldInfoField AS FnyiNFO ON HorzontalFny.FnyKeyInfoFieldId = FnyiNFO.FnyInfoFieldId
						  left join ExperimentField ON FnyiNFO.ExpFieldId = ExperimentField.ExpFieldId
						  WHERE VwHorizontalRptId = @Report_ID)
	IF(@FnyJoinsCount > 0)
	BEGIN
		SELECT @FnyJoins = STUFF(--
                        (

						  SELECT CONCAT(' LEFT JOIN ', Fny.FnyReportDbRptTableNameId, ' ON #KeysValDisc.Keys = ',Fny.FnyReportDbRptTableNameId,'.',
							CASE
								WHEN HorzontalFny.VwHorizontalFnyKeyIsDateTimeExpField = 1 THEN 'Fny_date_time'
								WHEN HorzontalFny.VwHorizontalFnyKeyIsResultExpField = 1 THEN 'Fny_result'
								WHEN HorzontalFny.VwHorizontalFnyKeyIsSNExpField = 1 THEN 'Fny_serial_number'
							END, 
							ExperimentField.ExpDbFieldNameId) FROM ViewHorizontalReport
						  inner JOIN ViewHorizontalReportByFnyRpt AS HorzontalFny ON ViewHorizontalReport.VwHorizontalRptId = HorzontalFny.VwHorizontalReportId
						  LEFT JOIN FinalPassYieldReport AS Fny ON HorzontalFny.FnyRptId = Fny.FnyReportId
						  LEFT JOIN FinalPassYieldInfoField AS FnyiNFO ON HorzontalFny.FnyKeyInfoFieldId = FnyiNFO.FnyInfoFieldId
						  left join ExperimentField ON FnyiNFO.ExpFieldId = ExperimentField.ExpFieldId
						  WHERE VwHorizontalRptId = @Report_ID
						     
						FOR xml path('')
						), 1, 0, '')
	END
	ELSE
	BEGIN
		SELECT @FnyJoins = ''
	END

	DECLARE @ExpFilters NVARCHAR(MAX)
	DECLARE @ExpFiltCount NVARCHAR(MAX)
	SELECT @ExpFiltCount =	(SELECT COUNT(*)
							FROM [StationMVIS].[dbo].[ViewHorizontalReport]
							LEFT JOIN ViewHorizontalReportByExperiment ON ViewHorizontalReport.VwHorizontalRptId = ViewHorizontalReportByExperiment.VwHorizontalReportId
							inner JOIN ViewHorizontalReportFilterByExpField ON ViewHorizontalReportByExperiment.VwHorizontalRptByExperimentId = ViewHorizontalReportFilterByExpField.VwHorizontalReportByExperimentId
							LEFT JOIN ExperimentField ON ViewHorizontalReportFilterByExpField.ExpFieldId = ExperimentField.ExpFieldId
							LEFT JOIN Experiment ON ExperimentField.ExpId = Experiment.ExpId
							WHERE [VwHorizontalRptId] = @Report_ID )
	
	IF(@ExpFiltCount > 0)
	BEGIN
		SELECT @ExpFilters = STUFF(--
                        (
							SELECT CONCAT(
							CASE 
							WHEN ViewHorizontalReportFilterByExpField.VwHorizontalRptFilterByExpFieldOperation = 'is' then (Experiment.ExpDbTableNameId + '.' + ExperimentField.ExpDbFieldNameId + ' = ' + '''' + ViewHorizontalReportFilterByExpField.VwHorizontalRptFilterByExpFieldValue1 + '''' +' ' + ViewHorizontalReportFilterByExpField.VwHorizontalRptFilterByExpFieldExpression)
							END,' '
										)
							FROM [StationMVIS].[dbo].[ViewHorizontalReport]
							LEFT JOIN ViewHorizontalReportByExperiment ON ViewHorizontalReport.VwHorizontalRptId = ViewHorizontalReportByExperiment.VwHorizontalReportId
							inner JOIN ViewHorizontalReportFilterByExpField ON ViewHorizontalReportByExperiment.VwHorizontalRptByExperimentId = ViewHorizontalReportFilterByExpField.VwHorizontalReportByExperimentId
							LEFT JOIN ExperimentField ON ViewHorizontalReportFilterByExpField.ExpFieldId = ExperimentField.ExpFieldId
							LEFT JOIN Experiment ON ExperimentField.ExpId = Experiment.ExpId
							WHERE [VwHorizontalRptId] = @Report_ID 
						FOR xml path('')
						), 1, 0, '')
	END
	ELSE
	BEGIN
		SELECT @ExpFilters = ''
	END

	DECLARE @FpyFilters NVARCHAR(MAX)
	DECLARE @FPYFiltCount NVARCHAR(MAX)

	SELECT @FPYFiltCount = (SELECT COUNT(*)
							FROM [StationMVIS].[dbo].[ViewHorizontalReport]
							LEFT JOIN ViewHorizontalReportByFpyRpt ON ViewHorizontalReport.VwHorizontalRptId = ViewHorizontalReportByFpyRpt.VwHorizontalReportId
							INNER JOIN ViewHorizontalReportFilterByFpyField ON ViewHorizontalReportByFpyRpt.VwHorizontalRptByFpyId = ViewHorizontalReportFilterByFpyField.VwHorizontalFpyRptId
							LEFT JOIN FirstPassYieldInfoField ON ViewHorizontalReportFilterByFpyField.FpyInfoFieldId = FirstPassYieldInfoField.FpyInfoFieldId
							LEFT JOIN FirstPassYieldReport ON FirstPassYieldInfoField.FpyReportId = FirstPassYieldReport.FpyReportId
							left join ExperimentField ON FirstPassYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
							WHERE [VwHorizontalRptId] = @Report_ID)
	IF(@FPYFiltCount > 0)
	BEGIN
		SELECT @FpyFilters = STUFF(--
                        (
							SELECT CONCAT(
										CASE 
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'contains' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' LIKE ' + '''' + '%' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'doesnotcontain' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' NOT LIKE ' + '''' + '%' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'doesnotstartwith' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' NOT LIKE ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'endswith' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' LIKE ' + '''' + '%' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1  + '''' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'is' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' = ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'isempty' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' IS NULL OR ' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' = ' + '''' + '''' 
												+ ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'isnot' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' <> ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'isnotempty' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' IS NOT NULL OR ' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' <> ' + '''' + '''' 
												+ ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'startwith' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' LIKE ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'between' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' BETWEEN ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1  + '''' + ' AND ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue2 + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'on' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' BETWEEN ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1  + '''' + ' AND ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'before' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' < ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'after' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' > ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'onorbefore' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' <= ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'onorafter' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' >= ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'today' then (' CONVERT (date, ' + FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ') = CONVERT (date, SYSDATETIME()) ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'currentweek' then (' CONVERT (date, ' + FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ') BETWEEN dateadd(day, 1-datepart(dw, SYSDATETIME()), CONVERT(date,SYSDATETIME())) AND dateadd(day, 8-datepart(dw, getdate()), CONVERT(date,SYSDATETIME())) ' + ' ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'currentmonth' then (' YEAR( ' + FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' ) = YEAR(SYSDATETIME()) AND MONTH(  ' + 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId +
												' ) =  MONTH(SYSDATETIME()) ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression + ' '
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'currentyear' then (' YEAR( ' + FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' ) = YEAR(SYSDATETIME()) ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression + ' '
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'onorbeforendaysago' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(DAY, - ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'onorbeforenweeksago' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(WEEK, - ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'onorbeforenmonthsago' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(MONTH, - ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'onorbeforenyearsago' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(YEAR, - ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'lastndays' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(DAY, - ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'lastnweeks' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(WEEK, - ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'lastnmonths' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(MONTH, - ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldOperation = 'lastnyears' then (FirstPassYieldReport.FpyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
												WHEN ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(YEAR, - ' + '''' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFpyField.VwHorizontalRptFilterByFpyFieldExpression
												)

										END,' '
										)
							FROM [StationMVIS].[dbo].[ViewHorizontalReport]
							LEFT JOIN ViewHorizontalReportByFpyRpt ON ViewHorizontalReport.VwHorizontalRptId = ViewHorizontalReportByFpyRpt.VwHorizontalReportId
							INNER JOIN ViewHorizontalReportFilterByFpyField ON ViewHorizontalReportByFpyRpt.VwHorizontalRptByFpyId = ViewHorizontalReportFilterByFpyField.VwHorizontalFpyRptId
							LEFT JOIN FirstPassYieldInfoField ON ViewHorizontalReportFilterByFpyField.FpyInfoFieldId = FirstPassYieldInfoField.FpyInfoFieldId
							LEFT JOIN FirstPassYieldReport ON FirstPassYieldInfoField.FpyReportId = FirstPassYieldReport.FpyReportId
							left join ExperimentField ON FirstPassYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
							WHERE [VwHorizontalRptId] = @Report_ID 
							FOR xml path('')
						), 1, 0, '')
	END
	ELSE
	BEGIN
		SELECT @FpyFilters = ''
	END

	DECLARE @FtyFilters NVARCHAR(MAX)
	DECLARE @FtyFiltCount NVARCHAR(MAX)

	SELECT @FtyFiltCount = (SELECT COUNT(*)
							FROM [StationMVIS].[dbo].[ViewHorizontalReport]
							LEFT JOIN ViewHorizontalReportByFtyRpt ON ViewHorizontalReport.VwHorizontalRptId = ViewHorizontalReportByFtyRpt.VwHorizontalReportId
							INNER JOIN ViewHorizontalReportFilterByFtyField ON ViewHorizontalReportByFtyRpt.VwHorizontalRptByFtyId = ViewHorizontalReportFilterByFtyField.VwHorizontalFtyRptId
							LEFT JOIN FirstTimeYieldInfoField ON ViewHorizontalReportFilterByFtyField.FtyInfoFieldId = FirstTimeYieldInfoField.FtyInfoFieldId
							LEFT JOIN FirstTimeYieldReport ON FirstTimeYieldInfoField.FtyReportId = FirstTimeYieldReport.FtyReportId
							left join ExperimentField ON FirstTimeYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
							WHERE [VwHorizontalRptId] = @Report_ID)
	IF(@FtyFiltCount > 0)
	BEGIN
		SELECT @FtyFilters = STUFF(--
                        (
							SELECT CONCAT(
										CASE 
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'contains' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' LIKE ' + '''' + '%' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'doesnotcontain' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' NOT LIKE ' + '''' + '%' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'doesnotstartwith' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' NOT LIKE ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'endswith' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' LIKE ' + '''' + '%' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1  + '''' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'is' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' = ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'isempty' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' IS NULL OR ' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' = ' + '''' + '''' 
												+ ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'isnot' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' <> ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'isnotempty' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' IS NOT NULL OR ' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' <> ' + '''' + '''' 
												+ ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'startwith' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' LIKE ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'between' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' BETWEEN ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1  + '''' + ' AND ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue2 + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'on' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' BETWEEN ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1  + '''' + ' AND ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'before' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' < ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'after' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' > ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'onorbefore' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' <= ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'onorafter' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' >= ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'today' then (' CONVERT (date, ' + FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ') = CONVERT (date, SYSDATETIME()) ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'currentweek' then (' CONVERT (date, ' + FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ') BETWEEN dateadd(day, 1-datepart(dw, SYSDATETIME()), CONVERT(date,SYSDATETIME())) AND dateadd(day, 8-datepart(dw, getdate()), CONVERT(date,SYSDATETIME())) ' + ' ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'currentmonth' then (' YEAR( ' + FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' ) = YEAR(SYSDATETIME()) AND MONTH(  ' + 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId +
												' ) =  MONTH(SYSDATETIME()) ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression + ' '
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'currentyear' then (' YEAR( ' + FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' ) = YEAR(SYSDATETIME()) ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression + ' '
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'onorbeforendaysago' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(DAY, - ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'onorbeforenweeksago' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(WEEK, - ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'onorbeforenmonthsago' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(MONTH, - ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'onorbeforenyearsago' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(YEAR, - ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'lastndays' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(DAY, - ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'lastnweeks' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(WEEK, - ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'lastnmonths' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(MONTH, - ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldOperation = 'lastnyears' then (FirstTimeYieldReport.FtyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsDateTimeExpField = 1 THEN 'Fty_date_time'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsResultExpField = 1 THEN 'Fty_result'
												WHEN ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyIsSNExpField = 1 THEN 'Fty_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(YEAR, - ' + '''' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFtyField.VwHorizontalRptFilterByFtyFieldExpression
												)

										END,' '
										)
							FROM [StationMVIS].[dbo].[ViewHorizontalReport]
							LEFT JOIN ViewHorizontalReportByFtyRpt ON ViewHorizontalReport.VwHorizontalRptId = ViewHorizontalReportByFtyRpt.VwHorizontalReportId
							INNER JOIN ViewHorizontalReportFilterByFtyField ON ViewHorizontalReportByFtyRpt.VwHorizontalRptByFtyId = ViewHorizontalReportFilterByFtyField.VwHorizontalFtyRptId
							LEFT JOIN FirstTimeYieldInfoField ON ViewHorizontalReportFilterByFtyField.FtyInfoFieldId = FirstTimeYieldInfoField.FtyInfoFieldId
							LEFT JOIN FirstTimeYieldReport ON FirstTimeYieldInfoField.FtyReportId = FirstTimeYieldReport.FtyReportId
							left join ExperimentField ON FirstTimeYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
							WHERE [VwHorizontalRptId] = @Report_ID 
							FOR xml path('')
						), 1, 0, '')
	END
	ELSE
	BEGIN
		SELECT @FtyFilters = ''
	END

	DECLARE @FnyFilters NVARCHAR(MAX)
	DECLARE @FnyFiltCount NVARCHAR(MAX)

	SELECT @FnyFiltCount = (SELECT COUNT(*)
							FROM [StationMVIS].[dbo].[ViewHorizontalReport]
							LEFT JOIN ViewHorizontalReportByFnyRpt ON ViewHorizontalReport.VwHorizontalRptId = ViewHorizontalReportByFnyRpt.VwHorizontalReportId
							INNER JOIN ViewHorizontalReportFilterByFnyField ON ViewHorizontalReportByFnyRpt.VwHorizontalRptByFnyId = ViewHorizontalReportFilterByFnyField.VwHorizontalFnyRptId
							LEFT JOIN FinalPassYieldInfoField ON ViewHorizontalReportFilterByFnyField.FnyInfoFieldId = FinalPassYieldInfoField.FnyInfoFieldId
							LEFT JOIN FinalPassYieldReport ON FinalPassYieldInfoField.FnyReportId = FinalPassYieldReport.FnyReportId
							left join ExperimentField ON FinalPassYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
							WHERE [VwHorizontalRptId] = @Report_ID)
	IF(@FnyFiltCount > 0)
	BEGIN
		SELECT @FnyFilters = STUFF(--
                        (
							SELECT CONCAT(
										CASE 
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'contains' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' LIKE ' + '''' + '%' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'doesnotcontain' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' NOT LIKE ' + '''' + '%' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'doesnotstartwith' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' NOT LIKE ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'endswith' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' LIKE ' + '''' + '%' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1  + '''' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'is' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' = ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'isempty' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' IS NULL OR ' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' = ' + '''' + '''' 
												+ ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'isnot' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' <> ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'isnotempty' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' IS NOT NULL OR ' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' <> ' + '''' + '''' 
												+ ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'startwith' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' LIKE ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '%' + '''' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'between' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' BETWEEN ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1  + '''' + ' AND ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue2 + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'on' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' BETWEEN ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1  + '''' + ' AND ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'before' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' < ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'after' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' > ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'onorbefore' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' <= ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'onorafter' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' >= ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'today' then (' CONVERT (date, ' + FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ') = CONVERT (date, SYSDATETIME()) ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'currentweek' then (' CONVERT (date, ' + FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ') BETWEEN dateadd(day, 1-datepart(dw, SYSDATETIME()), CONVERT(date,SYSDATETIME())) AND dateadd(day, 8-datepart(dw, getdate()), CONVERT(date,SYSDATETIME())) ' + ' ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'currentmonth' then (' YEAR( ' + FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' ) = YEAR(SYSDATETIME()) AND MONTH(  ' + 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId +
												' ) =  MONTH(SYSDATETIME()) ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression + ' '
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'currentyear' then (' YEAR( ' + FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ ' ) = YEAR(SYSDATETIME()) ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression + ' '
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'onorbeforendaysago' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(DAY, - ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'onorbeforenweeksago' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(WEEK, - ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'onorbeforenmonthsago' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(MONTH, - ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'onorbeforenyearsago' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  <= DATEADD(YEAR, - ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'lastndays' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(DAY, - ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'lastnweeks' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(WEEK, - ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'lastnmonths' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(MONTH, - ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)
											WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldOperation = 'lastnyears' then (FinalPassYieldReport.FnyReportDbRptTableNameId + '.' 
												+	CASE
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsDateTimeExpField = 1 THEN 'Fny_date_time'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsResultExpField = 1 THEN 'Fny_result'
												WHEN ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyIsSNExpField = 1 THEN 'Fny_serial_number'
												END + ExperimentField.ExpDbFieldNameId 
												+ '  BETWEEN DATEADD(YEAR, - ' + '''' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewHorizontalReportFilterByFnyField.VwHorizontalRptFilterByFnyFieldExpression
												)

										END,' '
										)
							FROM [StationMVIS].[dbo].[ViewHorizontalReport]
							LEFT JOIN ViewHorizontalReportByFnyRpt ON ViewHorizontalReport.VwHorizontalRptId = ViewHorizontalReportByFnyRpt.VwHorizontalReportId
							INNER JOIN ViewHorizontalReportFilterByFnyField ON ViewHorizontalReportByFnyRpt.VwHorizontalRptByFnyId = ViewHorizontalReportFilterByFnyField.VwHorizontalFnyRptId
							LEFT JOIN FinalPassYieldInfoField ON ViewHorizontalReportFilterByFnyField.FnyInfoFieldId = FinalPassYieldInfoField.FnyInfoFieldId
							LEFT JOIN FinalPassYieldReport ON FinalPassYieldInfoField.FnyReportId = FinalPassYieldReport.FnyReportId
							left join ExperimentField ON FinalPassYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
							WHERE [VwHorizontalRptId] = @Report_ID 
							FOR xml path('')
						), 1, 0, '')
	END
	ELSE
	BEGIN
		SELECT @FnyFilters = ''
	END

	DECLARE @EnrichmentsCount int

	Select @EnrichmentsCount = (
									
								SELECT count(*) FROM ViewHorizontalReportColumn
								INNER JOIN ViewHorizontalReportColumnByEnrichment ON ViewHorizontalReportColumn.VwHorizontalRptColumnId = ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnId
								WHERE ViewHorizontalReportColumn.VwHorizontalReportId = @Report_ID

								)
	
	IF(@EnrichmentsCount < 1)
	BEGIN
		DECLARE @InsertToRptTable NVARCHAR(MAX)
		SELECT @InsertToRptTable = 'INSERT INTO ' + @ReportTableName + ' SELECT DISTINCT #KeysValDisc.Keys, ' + LEFT(@InsertColumns, LEN(@InsertColumns) - 1) + ' FROM #KeysValDisc ' 
		+ @ExpJoins + @FpyJoins + @FtyJoins + @FnyJoins
		+ ' WHERE ' + @ExpFilters + @FpyFilters + @FtyFilters + @FnyFilters + ' 1 = 1 '
		EXECUTE sp_executesql @InsertToRptTable
	END
	ELSE
	BEGIN
		DECLARE @ReportColumsEnrichment nvarchar(MAX)
		SELECT @ReportColumsEnrichment = STUFF(--
			(
				SELECT CONCAT(VwHorizontalRptColumnDbId,' varchar(MAX),') FROM ViewHorizontalReportColumn WHERE VwHorizontalReportId = @Report_ID
				FOR xml path('')
			), 1, 0, '')
		
		DECLARE @RepTable nvarchar(MAX)
		SELECT @RepTable = 'RepTable' + @ReportTableName

		IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES 
		WHERE TABLE_NAME = @RepTable)
		BEGIN
			DECLARE @DropTableRep nvarchar(MAX)
			SELECT @DropTableRep = 'DROP TABLE RepTable' + @ReportTableName
			EXECUTE sp_executesql @DropTableRep
		END

		DECLARE @CreateTableRep NVARCHAR(MAX);
		SELECT @CreateTableRep = 'CREATE TABLE RepTable' + @ReportTableName + ' ( ' + @KeyField + LEFT(@ReportColumsEnrichment, LEN(@ReportColumsEnrichment) - 1) + ')'--Get create table estatement
		print 'Create Rep Table: ' + @CreateTableRep
		EXECUTE sp_executesql @CreateTableRep;



		DECLARE @InsertToRptTableRep NVARCHAR(MAX)
		SELECT @InsertToRptTableRep = 'INSERT INTO RepTable' + @ReportTableName + ' SELECT DISTINCT #KeysValDisc.Keys, ' + LEFT(@InsertColumns, LEN(@InsertColumns) - 1) + ' FROM #KeysValDisc ' 
		+ @ExpJoins + @FpyJoins + @FtyJoins + @FnyJoins
		+ ' WHERE ' + @ExpFilters + @FpyFilters + @FtyFilters + @FnyFilters + ' 1 = 1 '
		PRINT 'INSERT IN REP TABLE: ' + @InsertToRptTableRep
		EXECUTE sp_executesql @InsertToRptTableRep
		
		DECLARE @ApplyEnrichment NVARCHAR(MAX)
		SELECT @ApplyEnrichment = STUFF(--
			(
				SELECT CONCAT(' UPDATE RepTable',@ReportTableName,' SET ',ViewHorizontalReportColumn.VwHorizontalRptColumnDbId,' = ','''',ViewHorizontalReportColumnByEnrichment.vwHorizontalRptColumnEnrichmentStaticValue,'''',' WHERE ',ViewHorizontalReportColumn.VwHorizontalRptColumnDbId,

				CASE
					WHEN ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentOperation = 'contains' THEN CONCAT(' LIKE ','''','%',ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentValue,'%','''')
					WHEN ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentOperation = 'doesnotcontain' THEN CONCAT(' NOT LIKE ','''','%',ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentValue,'%','''')
					WHEN ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentOperation = 'doesnotstartwith' THEN CONCAT(' NOT LIKE ','''',ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentValue,'%','''')
					WHEN ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentOperation = 'endswith' THEN CONCAT(' LIKE ','''','%',ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentValue,'''')
					WHEN ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentOperation = 'is' THEN CONCAT(' = ','''',ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentValue,'''')
					WHEN ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentOperation = 'isempty' THEN CONCAT(' IS NULL OR ',ViewHorizontalReportColumn.VwHorizontalRptColumnDbId,' = ','''','''')
					WHEN ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentOperation = 'isnot' THEN CONCAT(' <> ','''',ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentValue,'''')
					WHEN ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentOperation = 'isnotempty' THEN CONCAT(' IS NOT NULL OR ',ViewHorizontalReportColumn.VwHorizontalRptColumnDbId,' <> ','''','''')
					WHEN ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentOperation = 'startswith' THEN CONCAT(' LIKE ','''',ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnEnrichmentValue,'%','''')
				END
				
				) FROM ViewHorizontalReportColumn
				INNER JOIN ViewHorizontalReportColumnByEnrichment ON ViewHorizontalReportColumn.VwHorizontalRptColumnId = ViewHorizontalReportColumnByEnrichment.VwHorizontalRptColumnId
				WHERE ViewHorizontalReportColumn.VwHorizontalReportId = @Report_ID
			FOR xml path('')
			), 1, 0, '')
		print 'applying enrichment: '
		EXECUTE sp_executesql @ApplyEnrichment

		DECLARE @InsertFromRptToTable nvarchar(MAX)

		SELECT @InsertFromRptToTable = ' INSERT INTO ' + @ReportTableName + ' SELECT * FROM RepTable' + @ReportTableName
		
		print 'inserting on table: ' + @InsertFromRptToTable
		EXECUTE sp_executesql @InsertFromRptToTable

	END

PRINT @InsertToRptTable
	--SELECT * FROM #KeysValues
END
--EXECUTE spHorizontalReport 4
GO


