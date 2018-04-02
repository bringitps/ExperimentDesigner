CREATE PROCEDURE [dbo].[spVerticalReport]

@Report_ID NVARCHAR(MAX)

AS

BEGIN

	DECLARE @ReportTableName NVARCHAR(MAX);--Variable to store the Table Name
	DECLARE @DropTable NVARCHAR(MAX);--Drop existing table
	DECLARE @DropTableRpt NVARCHAR(MAX);

	SELECT @ReportTableName = (SELECT VwVerticalRptDbTableNameId FROM ViewVerticalReport WHERE VwVerticalRptId = @Report_ID)--Get table name

	IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES 
           WHERE TABLE_NAME = @ReportTableName)
	BEGIN
		SELECT @DropTable = 'DROP TABLE ' + @ReportTableName--Variable to store the drop statement
		--SELECT @DropTableRpt = 'DROP TABLE ' + @ReportTableName + 'RepTable'
		EXECUTE sp_executesql @DropTable;--execute drop
		PRINT 'Drop table: ' + @DropTableRpt
		--EXECUTE sp_executesql @DropTableRpt;
	END

	DECLARE @ReportColums NVARCHAR(MAX);--Variable to store the report columns and data tipye (Create Table format)
	SELECT @ReportColums =STUFF(--
                        (   SELECT CONCAT(VwVerticalRptColumnDbId,' ',VwVerticalRptColumnDataType,', ') FROM ViewVerticalReportColumn
							WHERE VwVerticalReportId = @Report_ID
                            FOR xml path('')
                        ), 1, 0, '')
	
	DECLARE @TableColums NVARCHAR(MAX);

	SELECT @TableColums =STUFF(--
                        (   SELECT CONCAT(VwVerticalRptColumnDbId,', ') FROM ViewVerticalReportColumn
							WHERE VwVerticalReportId = @Report_ID
                            FOR xml path('')
                        ), 1, 0, '')

	DECLARE @CreateTable NVARCHAR(MAX);--Variable to store the create table statement

	SELECT @CreateTable = 'CREATE TABLE ' + @ReportTableName + '(Station varchar(MAX), ' + LEFT(@ReportColums, LEN(@ReportColums) - 1) + ')'--Get create table estatement
	PRINT 'CREATE TABLE : ' + @CreateTable
	EXECUTE sp_executesql @CreateTable;--Execute create table 

	CREATE TABLE #StructTable(
		Station varchar(MAX),
		DataSource varchar(MAX),
		ColumnsCSV varchar(MAX),
		Filters varchar(MAX)
	)
	
	INSERT INTO #StructTable
	SELECT 
	Experiment.ExpName,Experiment.ExpDbTableNameId,
                       expcol = STUFF((SELECT ',' + 
							ExperimentField.ExpDbFieldNameId
							FROM 
					   
							ViewVerticalReportColumn 
							inner join ViewVerticalReportColumnByExpField on ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByExpField.VwVerticalRptColumnId
							inner join ExperimentField on ViewVerticalReportColumnByExpField.ExpFieldId = ExperimentField.ExpFieldId
							WHERE Experiment.ExpId = ExperimentField.ExpId AND ViewVerticalReportColumn.VwVerticalReportId = @Report_ID FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 1, ''),
					   filters = STUFF(( 
							SELECT ' ' + 
							CASE 
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'contains'				then (ExperimentField.ExpDbFieldNameId + ' LIKE ' + '''' + '%' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '%' +'''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'doesnotcontain'		then (ExperimentField.ExpDbFieldNameId + ' NOT LIKE ' + '''' + '%' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '%' +'''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'doesnotstartwith'		then (ExperimentField.ExpDbFieldNameId + ' NOT LIKE ' + '''' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '%' +'''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'endswith'				then (ExperimentField.ExpDbFieldNameId + ' LIKE ' + '''' + '%' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'is'					then (ExperimentField.ExpDbFieldNameId + ' = ' + '''' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'isempty'				then (ExperimentField.ExpDbFieldNameId + ' IS NULL OR '+ ExperimentField.ExpDbFieldNameId + ' = ' + '''' + '''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'isnot'				then (ExperimentField.ExpDbFieldNameId + ' <> ' + '''' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'isnotempty'			then (ExperimentField.ExpDbFieldNameId + ' IS NOT NULL OR '+ ExperimentField.ExpDbFieldNameId + ' <> ' + '''' + '''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'startwith'			then (ExperimentField.ExpDbFieldNameId + ' LIKE ' + '''' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '%' +'''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'between'				then (ExperimentField.ExpDbFieldNameId + ' BETWEEN ' + '''' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '''' + ' AND ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue2 + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'on'					then (ExperimentField.ExpDbFieldNameId + ' BETWEEN ' + '''' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '''' + ' AND ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'before'				then (ExperimentField.ExpDbFieldNameId + ' < ' + '''' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'after'				then (ExperimentField.ExpDbFieldNameId + ' > ' + '''' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'onorbefore'			then (ExperimentField.ExpDbFieldNameId + ' <= ' + '''' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'onorafter'			then (ExperimentField.ExpDbFieldNameId + ' >= ' + '''' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + '''' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'today'				then (' CONVERT (date, ' + ExperimentField.ExpDbFieldNameId + ' ) ' + ' = ' + ' CONVERT (date, SYSDATETIME()) ' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'currentweek'			then (' CONVERT (date, ' + ExperimentField.ExpDbFieldNameId + ' ) ' + ' BETWEEN dateadd(day, 1-datepart(dw, SYSDATETIME()), CONVERT(date,SYSDATETIME())) AND dateadd(day, 8-datepart(dw, getdate()), CONVERT(date,SYSDATETIME())) ' + ' ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'currentmonth'			then (' YEAR(' + ExperimentField.ExpDbFieldNameId + ') = YEAR(SYSDATETIME()) AND MONTH(' + ExperimentField.ExpDbFieldNameId + ') =  MONTH(SYSDATETIME()) ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'currentyear'			then (' YEAR(' + ExperimentField.ExpDbFieldNameId + ') = YEAR(SYSDATETIME()) ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'onorbeforendaysago'	then (ExperimentField.ExpDbFieldNameId + ' <= DATEADD(DAY, -' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + ', CONVERT (date,SYSDATETIME())) ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'onorbeforenweeksago'	then (ExperimentField.ExpDbFieldNameId + ' <= DATEADD(WEEK, -' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + ', CONVERT (date,SYSDATETIME())) ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'onorbeforenmonthsago'	then (ExperimentField.ExpDbFieldNameId + ' <= DATEADD(MONTH, -' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + ', CONVERT (date,SYSDATETIME())) ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'onorbeforenyearsago'	then (ExperimentField.ExpDbFieldNameId + ' <= DATEADD(YEAR, -' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + ', CONVERT (date,SYSDATETIME())) ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'lastndays'			then (ExperimentField.ExpDbFieldNameId + ' BETWEEN DATEADD(DAY, -' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'lastnweeks'			then (ExperimentField.ExpDbFieldNameId + ' BETWEEN DATEADD(WEEK, -' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'lastnmonths'			then (ExperimentField.ExpDbFieldNameId + ' BETWEEN DATEADD(MONTH, -' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
								WHEN ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation = 'lastnyears'			then (ExperimentField.ExpDbFieldNameId + ' BETWEEN DATEADD(YEAR, -' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression)
							END
							
							FROM 
							ViewVerticalReportColumn 
							inner join ViewVerticalReport on ViewVerticalReportColumn.VwVerticalReportId = ViewVerticalReport.VwVerticalRptId
							inner join ViewVerticalReportColumnByExpField on ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByExpField.VwVerticalRptColumnId
							inner join ExperimentField on ViewVerticalReportColumnByExpField.ExpFieldId = ExperimentField.ExpFieldId
							inner join Experiment on ExperimentField.ExpId = Experiment.ExpId
							WHERE ExperimentField.ExpFieldId = ViewVerticalReportFilterByExpField.ExpFieldId AND 
							ViewVerticalReportColumn.VwVerticalReportId = @Report_ID AND ViewVerticalReport.VwVerticalRptId = ViewVerticalReportByExperiment.VwVerticalReportId
							AND ViewVerticalReportFilterByExpField.VwVerticalReportByExperimentId = ViewVerticalReportByExperiment.VwVerticalRptByExperimentId
							FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 0, '')
	FROM 
	ViewVerticalReportColumn 
	inner join ViewVerticalReport on ViewVerticalReportColumn.VwVerticalReportId = ViewVerticalReport.VwVerticalRptId
	inner join ViewVerticalReportColumnByExpField on ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByExpField.VwVerticalRptColumnId
	inner join ExperimentField on ViewVerticalReportColumnByExpField.ExpFieldId = ExperimentField.ExpFieldId
	inner join Experiment on ExperimentField.ExpId = Experiment.ExpId
	--inner join ViewVerticalReportFilterByExpField on ExperimentField.ExpFieldId = ViewVerticalReportFilterByExpField.ExpFieldId
	inner join ViewVerticalReportFilterByExpField on ExperimentField.ExpFieldId = ViewVerticalReportFilterByExpField.ExpFieldId
	inner join ViewVerticalReportByExperiment on ViewVerticalReport.VwVerticalRptId = ViewVerticalReportByExperiment.VwVerticalReportId
	WHERE ViewVerticalReportColumn.VwVerticalReportId = @Report_ID
	GROUP BY Experiment.ExpDbTableNameId,ViewVerticalReportFilterByExpField.ExpFieldId,ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue1,ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldExpression,ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldOperation,Experiment.ExpId,ViewVerticalReportFilterByExpField.VwVerticalRptFilterByExpFieldValue2,ViewVerticalReport.VwVerticalRptId,ViewVerticalReportByExperiment.VwVerticalReportId
	,ViewVerticalReportFilterByExpField.VwVerticalReportByExperimentId,ViewVerticalReportByExperiment.VwVerticalRptByExperimentId
	,Experiment.ExpName
	
	INSERT INTO #StructTable
		SELECT Experiment.ExpName,FirstPassYieldReport.FpyReportDbRptTableNameId,
						fpycol = STUFF((SELECT ',' +
							CASE 
								WHEN ViewVerticalReportColumnByFpyField.VwVerticalRptColumnByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
								WHEN ViewVerticalReportColumnByFpyField.VwVerticalRptColumnByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
								WHEN ViewVerticalReportColumnByFpyField.VwVerticalRptColumnByFpyIsResultExpField = 1 THEN 'fpy_result'
								ELSE ExperimentField.ExpDbFieldNameId
							END
							FROM ViewVerticalReport
							INNER JOIN ViewVerticalReportColumn ON ViewVerticalReport.VwVerticalRptId = ViewVerticalReportColumn.VwVerticalReportId
							INNER JOIN ViewVerticalReportColumnByFpyField ON ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByFpyField.VwVerticalRptColumnId
							WHERE ViewVerticalReportColumnByFpyField.fpyRptId = FirstPassYieldReport.FpyReportId and ViewVerticalReport.VwVerticalRptId = ViewVerticalReportByFpyRpt.VwVerticalReportId
							FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 1, ''),
							fpyfil = STUFF((SELECT DISTINCT ',' +
							CASE

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'contains' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' LIKE ' + '''' + '%' + VwVerticalRptFilterByFpyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'doesnotcontain' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' NOT LIKE ' + '''' + '%' + VwVerticalRptFilterByFpyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'doesnotstartwith' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' NOT LIKE ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'endswith' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' LIKE ' + '''' + '%' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'is' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' = ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'isempty' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' IS NULL OR ' + 
								CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' = ' + '''' + ''''
								 + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'isnot' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <> ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'isnotempty' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' IS NOT NULL OR ' +
								
								CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <> ' + '''' + ''''
								
								+ ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'startwith' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' LIKE ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'between' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' BETWEEN ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ' AND ' + '''' + VwVerticalRptFilterByFpyFieldValue2 + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'on' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' BETWEEN ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ' AND ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'before' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' < ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'after' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' > ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'onorbefore' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'onorafter' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' >= ' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'today' THEN
								(' CONVERT (date, ' +
								 CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) ' +  ' = ' +  + ' CONVERT (date, SYSDATETIME()) ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'currentweek' THEN
								(' CONVERT (date, ' +
								 CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) ' + ' BETWEEN dateadd(day, 1-datepart(dw, SYSDATETIME()), CONVERT(date,SYSDATETIME())) AND dateadd(day, 8-datepart(dw, getdate()), CONVERT(date,SYSDATETIME()))  ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'currentmonth' THEN
								(' YEAR( ' +
								 CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) = YEAR(SYSDATETIME()) AND MONTH( ' + 
								CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END
								 + ' ) =  MONTH(SYSDATETIME())  ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'currentyear' THEN
								(' YEAR( ' +
								 CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) = YEAR(SYSDATETIME()) ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'onorbeforendaysago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(DAY, -' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'onorbeforenweeksago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(WEEK, -' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'onorbeforenmonthsago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(MONTH, -' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'onorbeforenyearsago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(YEAR, -' + '''' + VwVerticalRptFilterByFpyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'lastndays' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(DAY, -' + VwVerticalRptFilterByFpyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'lastnweeks' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(WEEK, -' + VwVerticalRptFilterByFpyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'lastnmonths' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(MONTH, -' + VwVerticalRptFilterByFpyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFpyFieldExpression)

								WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyFieldOperation = 'lastnyears' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField = 1 THEN 'fpy_date_time'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField = 1 THEN 'fpy_serial_number'
									WHEN ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField = 1 THEN 'fpy_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(YEAR, -' + VwVerticalRptFilterByFpyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFpyFieldExpression)

							END
							FROM ViewVerticalReportColumn 
							inner join ViewVerticalReport on ViewVerticalReportColumn.VwVerticalReportId = ViewVerticalReport.VwVerticalRptId
							INNER JOIN ViewVerticalReportColumnByFpyField ON ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByFpyField.VwVerticalRptColumnId
							--INNER JOIN FirstPassYieldReport ON ViewVerticalReportColumnByFpyField.fpyRptId = FirstPassYieldReport.FpyReportId
							INNER JOIN FirstPassYieldInfoField ON ViewVerticalReportColumnByFpyField.FpyInfoFieldId = FirstPassYieldInfoField.FpyInfoFieldId
							inner JOIN ExperimentField ON FirstPassYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
							inner join ViewVerticalReportFilterByFpyField on ViewVerticalReportByFpyRpt.VwVerticalRptByFpyId = ViewVerticalReportFilterByFpyField.VwVerticalFpyRptId
							WHERE ViewVerticalReportByFpyRpt.fpyRptId = FirstPassYieldReport.FpyReportId  
							--ExperimentField.ExpFieldId = ViewVerticalReportFilterByFpyField.FpyInfoFieldId
							and ViewVerticalReportByFpyRpt.VwVerticalRptByFpyId = ViewVerticalReportFilterByFpyField.VwVerticalFpyRptId
							FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 1, '')
						
  FROM ViewVerticalReport
  INNER JOIN ViewVerticalReportColumn ON ViewVerticalReport.VwVerticalRptId = ViewVerticalReportColumn.VwVerticalReportId
  INNER JOIN ViewVerticalReportColumnByFpyField ON ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByFpyField.VwVerticalRptColumnId
  INNER JOIN FirstPassYieldReport ON ViewVerticalReportColumnByFpyField.fpyRptId = FirstPassYieldReport.FpyReportId
  INNER JOIN FirstPassYieldInfoField ON ViewVerticalReportColumnByFpyField.FpyInfoFieldId = FirstPassYieldInfoField.FpyInfoFieldId
  inner JOIN ExperimentField ON FirstPassYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
  inner JOIN ViewVerticalReportByFpyRpt ON ViewVerticalReport.VwVerticalRptId = ViewVerticalReportByFpyRpt.VwVerticalReportId
  inner join ViewVerticalReportFilterByFpyField on ViewVerticalReportByFpyRpt.VwVerticalRptByFpyId = ViewVerticalReportFilterByFpyField.VwVerticalFpyRptId
  inner join Experiment on ExperimentField.ExpId = Experiment.ExpId
  WHERE ViewVerticalReport.VwVerticalRptId = 20
  GROUP BY ViewVerticalReportColumn.VwVerticalReportId,FirstPassYieldReport.FpyReportId,FirstPassYieldReport.FpyReportDbRptTableNameId,ExperimentField.ExpDbFieldNameId,ViewVerticalReportByFpyRpt.VwVerticalReportId
  ,ViewVerticalReportByFpyRpt.fpyRptId
  ,ViewVerticalReportFilterByFpyField.FpyInfoFieldId
  ,ViewVerticalReportFilterByFpyField.VwVerticalFpyRptId
  ,ViewVerticalReportByFpyRpt.VwVerticalRptByFpyId
  ,ExperimentField.ExpFieldId
  ,ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsDateTimeExpField,ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsSNExpField,ViewVerticalReportFilterByFpyField.VwVerticalRptFilterByFpyIsResultExpField
  ,Experiment.ExpName

  INSERT INTO #StructTable
  	SELECT Experiment.ExpName,FirstTimeYieldReport.FtyReportDbRptTableNameId,
						ftycol = STUFF((SELECT ',' +
							CASE 
								WHEN ViewVerticalReportColumnByFtyField.VwVerticalRptColumnByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
								WHEN ViewVerticalReportColumnByFtyField.VwVerticalRptColumnByFtyIsSNExpField = 1 THEN 'fty_serial_number'
								WHEN ViewVerticalReportColumnByFtyField.VwVerticalRptColumnByFtyIsResultExpField = 1 THEN 'fty_result'
								ELSE ExperimentField.ExpDbFieldNameId
							END
							FROM ViewVerticalReport
							INNER JOIN ViewVerticalReportColumn ON ViewVerticalReport.VwVerticalRptId = ViewVerticalReportColumn.VwVerticalReportId
							INNER JOIN ViewVerticalReportColumnByFtyField ON ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByFtyField.VwVerticalRptColumnId
							WHERE ViewVerticalReportColumnByFtyField.ftyRptId = FirstTimeYieldReport.FtyReportId and ViewVerticalReport.VwVerticalRptId = ViewVerticalReportByFtyRpt.VwVerticalReportId
							FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 1, ''),
							ftyfil = STUFF((SELECT DISTINCT ',' +
							CASE

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'contains' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' LIKE ' + '''' + '%' + VwVerticalRptFilterByFtyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'doesnotcontain' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' NOT LIKE ' + '''' + '%' + VwVerticalRptFilterByFtyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'doesnotstartwith' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' NOT LIKE ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'endswith' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' LIKE ' + '''' + '%' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'is' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' = ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'isempty' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' IS NULL OR ' + 
								CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' = ' + '''' + ''''
								 + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'isnot' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <> ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'isnotempty' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' IS NOT NULL OR ' +
								
								CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <> ' + '''' + ''''
								
								+ ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'startwith' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' LIKE ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'between' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' BETWEEN ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ' AND ' + '''' + VwVerticalRptFilterByFtyFieldValue2 + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'on' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' BETWEEN ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ' AND ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'before' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' < ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'after' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' > ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'onorbefore' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'onorafter' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' >= ' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'today' THEN
								(' CONVERT (date, ' +
								 CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) ' +  ' = ' +  + ' CONVERT (date, SYSDATETIME()) ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'currentweek' THEN
								(' CONVERT (date, ' +
								 CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) ' + ' BETWEEN dateadd(day, 1-datepart(dw, SYSDATETIME()), CONVERT(date,SYSDATETIME())) AND dateadd(day, 8-datepart(dw, getdate()), CONVERT(date,SYSDATETIME()))  ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'currentmonth' THEN
								(' YEAR( ' +
								 CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) = YEAR(SYSDATETIME()) AND MONTH( ' + 
								CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END
								 + ' ) =  MONTH(SYSDATETIME())  ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'currentyear' THEN
								(' YEAR( ' +
								 CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) = YEAR(SYSDATETIME()) ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'onorbeforendaysago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(DAY, -' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'onorbeforenweeksago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(WEEK, -' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'onorbeforenmonthsago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(MONTH, -' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'onorbeforenyearsago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(YEAR, -' + '''' + VwVerticalRptFilterByFtyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'lastndays' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(DAY, -' + VwVerticalRptFilterByFtyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'lastnweeks' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(WEEK, -' + VwVerticalRptFilterByFtyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'lastnmonths' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(MONTH, -' + VwVerticalRptFilterByFtyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFtyFieldExpression)

								WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyFieldOperation = 'lastnyears' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField = 1 THEN 'fty_date_time'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField = 1 THEN 'fty_serial_number'
									WHEN ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField = 1 THEN 'fty_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(YEAR, -' + VwVerticalRptFilterByFtyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFtyFieldExpression)

							END
							FROM ViewVerticalReportColumn 
							inner join ViewVerticalReport on ViewVerticalReportColumn.VwVerticalReportId = ViewVerticalReport.VwVerticalRptId
							INNER JOIN ViewVerticalReportColumnByFtyField ON ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByFtyField.VwVerticalRptColumnId
							--INNER JOIN FirstTimeYieldReport ON ViewVerticalReportColumnByFtyField.ftyRptId = FirstTimeYieldReport.FtyReportId
							INNER JOIN FirstTimeYieldInfoField ON ViewVerticalReportColumnByFtyField.FtyInfoFieldId = FirstTimeYieldInfoField.FtyInfoFieldId
							inner JOIN ExperimentField ON FirstTimeYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
							inner join ViewVerticalReportFilterByFtyField on ViewVerticalReportByFtyRpt.VwVerticalRptByFtyId = ViewVerticalReportFilterByFtyField.VwVerticalftyRptId
							WHERE ViewVerticalReportByFtyRpt.ftyRptId = FirstTimeYieldReport.FtyReportId  
							--ExperimentField.ExpFieldId = ViewVerticalReportFilterByFtyField.FtyInfoFieldId
							and ViewVerticalReportByFtyRpt.VwVerticalRptByFtyId = ViewVerticalReportFilterByFtyField.VwVerticalftyRptId
							FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 1, '')
						
  FROM ViewVerticalReport
  INNER JOIN ViewVerticalReportColumn ON ViewVerticalReport.VwVerticalRptId = ViewVerticalReportColumn.VwVerticalReportId
  INNER JOIN ViewVerticalReportColumnByFtyField ON ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByFtyField.VwVerticalRptColumnId
  INNER JOIN FirstTimeYieldReport ON ViewVerticalReportColumnByFtyField.ftyRptId = FirstTimeYieldReport.FtyReportId
  INNER JOIN FirstTimeYieldInfoField ON ViewVerticalReportColumnByFtyField.FtyInfoFieldId = FirstTimeYieldInfoField.FtyInfoFieldId
  inner JOIN ExperimentField ON FirstTimeYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
  inner JOIN ViewVerticalReportByFtyRpt ON ViewVerticalReport.VwVerticalRptId = ViewVerticalReportByFtyRpt.VwVerticalReportId
  inner join ViewVerticalReportFilterByFtyField on ViewVerticalReportByFtyRpt.VwVerticalRptByFtyId = ViewVerticalReportFilterByFtyField.VwVerticalftyRptId
  inner join Experiment on ExperimentField.ExpId = Experiment.ExpId
  WHERE ViewVerticalReport.VwVerticalRptId = 20
  GROUP BY ViewVerticalReportColumn.VwVerticalReportId,FirstTimeYieldReport.FtyReportId,FirstTimeYieldReport.FtyReportDbRptTableNameId,ExperimentField.ExpDbFieldNameId,ViewVerticalReportByFtyRpt.VwVerticalReportId
  ,ViewVerticalReportByFtyRpt.ftyRptId
  ,ViewVerticalReportFilterByFtyField.FtyInfoFieldId
  ,ViewVerticalReportFilterByFtyField.VwVerticalftyRptId
  ,ViewVerticalReportByFtyRpt.VwVerticalRptByFtyId
  ,ExperimentField.ExpFieldId
  ,ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsDateTimeExpField,ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsSNExpField,ViewVerticalReportFilterByFtyField.VwVerticalRptFilterByFtyIsResultExpField
  ,Experiment.ExpName

 INSERT INTO #StructTable
  SELECT Experiment.ExpName,FinalPassYieldReport.FnyReportDbRptTableNameId,
						fnycol = STUFF((SELECT ',' +
							CASE 
								WHEN ViewVerticalReportColumnByFnyField.VwVerticalRptColumnByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
								WHEN ViewVerticalReportColumnByFnyField.VwVerticalRptColumnByFnyIsSNExpField = 1 THEN 'fny_serial_number'
								WHEN ViewVerticalReportColumnByFnyField.VwVerticalRptColumnByFnyIsResultExpField = 1 THEN 'fny_result'
								ELSE ExperimentField.ExpDbFieldNameId
							END
							FROM ViewVerticalReport
							INNER JOIN ViewVerticalReportColumn ON ViewVerticalReport.VwVerticalRptId = ViewVerticalReportColumn.VwVerticalReportId
							INNER JOIN ViewVerticalReportColumnByFnyField ON ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByFnyField.VwVerticalRptColumnId
							WHERE ViewVerticalReportColumnByFnyField.fnyRptId = FinalPassYieldReport.FnyReportId and ViewVerticalReport.VwVerticalRptId = ViewVerticalReportByFnyRpt.VwVerticalReportId
							FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 1, ''),
							fnyfil = STUFF((SELECT DISTINCT ',' +
							CASE

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'contains' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' LIKE ' + '''' + '%' + VwVerticalRptFilterByFnyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'doesnotcontain' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' NOT LIKE ' + '''' + '%' + VwVerticalRptFilterByFnyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'doesnotstartwith' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' NOT LIKE ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'endswith' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' LIKE ' + '''' + '%' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'is' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' = ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'isempty' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' IS NULL OR ' + 
								CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' = ' + '''' + ''''
								 + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'isnot' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <> ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'isnotempty' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' IS NOT NULL OR ' +
								
								CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <> ' + '''' + ''''
								
								+ ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'startwith' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' LIKE ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '%' + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'between' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' BETWEEN ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ' AND ' + '''' + VwVerticalRptFilterByFnyFieldValue2 + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'on' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' BETWEEN ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ' AND ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'before' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' < ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'after' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' > ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'onorbefore' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'onorafter' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' >= ' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ' ' +VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'today' THEN
								(' CONVERT (date, ' +
								 CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) ' +  ' = ' +  + ' CONVERT (date, SYSDATETIME()) ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'currentweek' THEN
								(' CONVERT (date, ' +
								 CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) ' + ' BETWEEN dateadd(day, 1-datepart(dw, SYSDATETIME()), CONVERT(date,SYSDATETIME())) AND dateadd(day, 8-datepart(dw, getdate()), CONVERT(date,SYSDATETIME()))  ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'currentmonth' THEN
								(' YEAR( ' +
								 CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) = YEAR(SYSDATETIME()) AND MONTH( ' + 
								CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END
								 + ' ) =  MONTH(SYSDATETIME())  ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'currentyear' THEN
								(' YEAR( ' +
								 CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' ) = YEAR(SYSDATETIME()) ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'onorbeforendaysago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(DAY, -' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'onorbeforenweeksago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(WEEK, -' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'onorbeforenmonthsago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(MONTH, -' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'onorbeforenyearsago' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + ' <= DATEADD(YEAR, -' + '''' + VwVerticalRptFilterByFnyFieldValue1 + '''' + ', CONVERT (date,SYSDATETIME())) ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'lastndays' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(DAY, -' + VwVerticalRptFilterByFnyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'lastnweeks' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(WEEK, -' + VwVerticalRptFilterByFnyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'lastnmonths' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(MONTH, -' + VwVerticalRptFilterByFnyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFnyFieldExpression)

								WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyFieldOperation = 'lastnyears' THEN
								(CASE
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField = 1 THEN 'fny_date_time'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField = 1 THEN 'fny_serial_number'
									WHEN ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField = 1 THEN 'fny_result'
									ELSE ExperimentField.ExpDbFieldNameId
								END + '  BETWEEN DATEADD(YEAR, -' + VwVerticalRptFilterByFnyFieldValue1 + ', CONVERT (date,SYSDATETIME())) AND CONVERT (date,SYSDATETIME()) ' + VwVerticalRptFilterByFnyFieldExpression)

							END
							FROM ViewVerticalReportColumn 
							inner join ViewVerticalReport on ViewVerticalReportColumn.VwVerticalReportId = ViewVerticalReport.VwVerticalRptId
							INNER JOIN ViewVerticalReportColumnByFnyField ON ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByFnyField.VwVerticalRptColumnId
							--INNER JOIN FinalPassYieldReport ON ViewVerticalReportColumnByFnyField.fnyRptId = FinalPassYieldReport.FnyReportId
							INNER JOIN FinalPassYieldInfoField ON ViewVerticalReportColumnByFnyField.FnyInfoFieldId = FinalPassYieldInfoField.FnyInfoFieldId
							inner JOIN ExperimentField ON FinalPassYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
							inner join ViewVerticalReportFilterByFnyField on ViewVerticalReportByFnyRpt.VwVerticalRptByFnyId = ViewVerticalReportFilterByFnyField.VwVerticalfnyRptId
							WHERE ViewVerticalReportByFnyRpt.fnyRptId = FinalPassYieldReport.FnyReportId  
							--ExperimentField.ExpFieldId = ViewVerticalReportFilterByFnyField.FnyInfoFieldId
							and ViewVerticalReportByFnyRpt.VwVerticalRptByFnyId = ViewVerticalReportFilterByFnyField.VwVerticalfnyRptId
							FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 1, '')
						
  FROM ViewVerticalReport
  INNER JOIN ViewVerticalReportColumn ON ViewVerticalReport.VwVerticalRptId = ViewVerticalReportColumn.VwVerticalReportId
  INNER JOIN ViewVerticalReportColumnByFnyField ON ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByFnyField.VwVerticalRptColumnId
  INNER JOIN FinalPassYieldReport ON ViewVerticalReportColumnByFnyField.fnyRptId = FinalPassYieldReport.FnyReportId
  INNER JOIN FinalPassYieldInfoField ON ViewVerticalReportColumnByFnyField.FnyInfoFieldId = FinalPassYieldInfoField.FnyInfoFieldId
  inner JOIN ExperimentField ON FinalPassYieldInfoField.ExpFieldId = ExperimentField.ExpFieldId
  inner JOIN ViewVerticalReportByFnyRpt ON ViewVerticalReport.VwVerticalRptId = ViewVerticalReportByFnyRpt.VwVerticalReportId
  inner join ViewVerticalReportFilterByFnyField on ViewVerticalReportByFnyRpt.VwVerticalRptByFnyId = ViewVerticalReportFilterByFnyField.VwVerticalfnyRptId
  inner join Experiment on ExperimentField.ExpId = Experiment.ExpId
  WHERE ViewVerticalReport.VwVerticalRptId = 20
  GROUP BY ViewVerticalReportColumn.VwVerticalReportId,FinalPassYieldReport.FnyReportId,FinalPassYieldReport.FnyReportDbRptTableNameId,ExperimentField.ExpDbFieldNameId,ViewVerticalReportByFnyRpt.VwVerticalReportId
  ,ViewVerticalReportByFnyRpt.fnyRptId
  ,ViewVerticalReportFilterByFnyField.FnyInfoFieldId
  ,ViewVerticalReportFilterByFnyField.VwVerticalfnyRptId
  ,ViewVerticalReportByFnyRpt.VwVerticalRptByFnyId
  ,ExperimentField.ExpFieldId
  ,ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsDateTimeExpField,ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsSNExpField,ViewVerticalReportFilterByFnyField.VwVerticalRptFilterByFnyIsResultExpField
  ,Experiment.ExpName

	--PRINT @InsertData
	--EXECUTE sp_executesql @InsertData;
		CREATE TABLE #StructTable0(
		Station varchar(MAX),
		DataSource varchar(MAX),
		ColumnsCSV varchar(MAX),
		Filters varchar(MAX)
	)
	INSERT INTO #StructTable0
	SELECT Station,DataSource, ColumnsCSV, STUFF((SELECT 
												Filters from #StructTable WHERE DataSource = ST.DataSource GROUP BY DataSource,Filters
												FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 0, '')
	
	FROM #StructTable ST
	GROUP BY DataSource, Station, ColumnsCSV
	
	

	DECLARE @ColumnsFromTables NVARCHAR(MAX);

	SELECT @ColumnsFromTables = STUFF(--
                        (SELECT CONCAT('SELECT ','''',Station,'''',',', ColumnsCSV , ' FROM ', DataSource, ' WHERE ', Filters,' 1=1 UNION ALL ') FROM #StructTable0
						FOR xml path('')
                        ), 1, 0, '')

	
	DECLARE @EnrichmentCount INT;

	SELECT @EnrichmentCount = (SELECT COUNT(ViewVerticalReportColumnByEnrichment.vwVerticalRptColumnEnrichmentStaticValue)
							   FROM ViewVerticalReport
							   join ViewVerticalReportColumn on ViewVerticalReport.VwVerticalRptId = ViewVerticalReportColumn.VwVerticalReportId
							   inner join ViewVerticalReportColumnByExpField on ViewVerticalReportColumn.VwVerticalRptColumnId = ViewVerticalReportColumnByExpField.VwVerticalRptColumnId
							   inner join ExperimentField on ViewVerticalReportColumnByExpField.ExpFieldId = ExperimentField.ExpFieldId
							   left join ViewVerticalReportColumnByEnrichment on ViewVerticalReportColumn.VwVerticalRptColumnId =  ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnId
							   where ViewVerticalReport.VwVerticalRptId = @Report_ID AND ViewVerticalReportColumnByEnrichment.vwVerticalRptColumnEnrichmentStaticValue is not null)

	DECLARE @InsertData NVARCHAR(MAX);
	SELECT @InsertData = 'INSERT INTO ' + @ReportTableName + ' ' + LEFT(@ColumnsFromTables, LEN(@ColumnsFromTables) - 9)

	IF (@EnrichmentCount < 1)
	BEGIN
		PRINT @InsertData
		EXECUTE sp_executesql @InsertData
	END
	ELSE
	BEGIN
		DECLARE @ReportTableNameRep NVARCHAR(MAX);
		SELECT @ReportTableNameRep = @ReportTableName + 'RepTable'
		IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES 
		WHERE TABLE_NAME = @ReportTableNameRep)
		BEGIN
			SELECT @DropTableRpt = 'DROP TABLE ' + @ReportTableNameRep
			PRINT 'Drop table: ' + @DropTableRpt
			EXECUTE sp_executesql @DropTableRpt;
		END
		DECLARE @ColumnsForEnrichment NVARCHAR(MAX);
		SELECT @ColumnsForEnrichment =STUFF(--
        (	SELECT CONCAT(VwVerticalRptColumnDbId,' VARCHAR(MAX), ') 
			FROM ViewVerticalReportColumn
			WHERE VwVerticalReportId = @Report_ID
			FOR xml path('')
        ), 1, 0, '')

		DECLARE @ReplicateTable NVARCHAR(MAX);
		SELECT @ReplicateTable = 'CREATE TABLE ' + @ReportTableName + 'RepTable( ' + LEFT(@ColumnsForEnrichment, LEN(@ColumnsForEnrichment) - 1) + ' )' 
		EXECUTE sp_executesql @ReplicateTable
		
		DECLARE @InsertDataRpt NVARCHAR(MAX);
		SELECT @InsertDataRpt = 'INSERT INTO ' + @ReportTableName + 'RepTable ' + LEFT(@ColumnsFromTables, LEN(@ColumnsFromTables) - 9)
		EXECUTE sp_executesql @InsertDataRpt
		
		DECLARE @Enrichment NVARCHAR(MAX);

		SELECT @Enrichment = STUFF(--
			(
				SELECT 
				CONCAT(' UPDATE ',@ReportTableName,'RepTable SET ', ViewVerticalReportColumn.VwVerticalRptColumnName,' = ','''',ViewVerticalReportColumnByEnrichment.vwVerticalRptColumnEnrichmentStaticValue,'''',' WHERE ',ViewVerticalReportColumn.VwVerticalRptColumnName,
				CASE 
					WHEN ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentOperation = 'contains' THEN CONCAT(' LIKE ','''',' ',ViewVerticalReportColumnByEnrichment.vwVerticalRptColumnEnrichmentStaticValue,'%','''')
					WHEN ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentOperation = 'doesnotcontain' THEN CONCAT(' NOT LIKE ','''',' ',ViewVerticalReportColumnByEnrichment.vwVerticalRptColumnEnrichmentStaticValue,'%','''')
					WHEN ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentOperation = 'doesnotstartwith' THEN CONCAT(' NOT LIKE ','''',ViewVerticalReportColumnByEnrichment.vwVerticalRptColumnEnrichmentStaticValue,'%','''')
					WHEN ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentOperation = 'endswith' THEN CONCAT(' LIKE ','''','%',ViewVerticalReportColumnByEnrichment.vwVerticalRptColumnEnrichmentStaticValue,'''')
					WHEN ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentOperation = 'is' THEN  CONCAT(' = ','''',ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentValue,'''')
					WHEN ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentOperation = 'isempty' THEN  CONCAT(' IS NULL OR ',ViewVerticalReportColumn.VwVerticalRptColumnName,' = ','''','''')
					WHEN ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentOperation = 'isnot' THEN  CONCAT(' <> ','''',ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentValue,'''')
					WHEN ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentOperation = 'isnotempty' THEN  CONCAT(' IS NULL OR ',ViewVerticalReportColumn.VwVerticalRptColumnName,' <> ','''','''')
					WHEN ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentOperation = 'startswith' THEN  CONCAT(' LIKE ','''',ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnEnrichmentValue,'%','''')
				END
				)
				FROM ViewVerticalReport
				inner join ViewVerticalReportColumn on ViewVerticalReport.VwVerticalRptId = ViewVerticalReportColumn.VwVerticalReportId
				inner join ViewVerticalReportColumnByEnrichment on ViewVerticalReportColumn.VwVerticalRptColumnId =  ViewVerticalReportColumnByEnrichment.VwVerticalRptColumnId
				where ViewVerticalReport.VwVerticalRptId = @Report_ID
				FOR xml path('')
			), 1, 0, '')

		EXECUTE sp_executesql @Enrichment

		DECLARE @InsertDataTable NVARCHAR(MAX);
		SELECT @InsertDataTable = 'INSERT INTO ' + @ReportTableName + ' SELECT * FROM ' + @ReportTableName + 'RepTable '
		SELECT * FROM #StructTable
		--EXECUTE sp_executesql @InsertDataTable
		
		--PRINT @Enrichment
	END
	DECLARE @SELECT NVARCHAR(MAX);
	SELECT @SELECT = 'SELECT * FROM ' + @ReportTableName
	--SELECT * FROM #StructTable0
	
	--EXECUTE sp_executesql @SELECT
	--PRINT @InsertData
	--PRINT @EnrichmentCount
	--SELECT * FROM #StructTable0
	--LEFT(@ReportColums, LEN(@ReportColums) - 1)
	--EXECUTE SPVerticalReport 21

END


GO


