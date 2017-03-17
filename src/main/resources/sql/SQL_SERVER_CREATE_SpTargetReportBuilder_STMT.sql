CREATE PROCEDURE spTargetReportBuilder
@TargetReportId NVARCHAR(MAX),
@ExperimentId NVARCHAR(MAX),
@DateFieldName NVARCHAR(MAX),
@FromDate NVARCHAR(MAX),
@ToDate NVARCHAR(MAX),
@CmId NVARCHAR(MAX),
@ExpFieldName NVARCHAR(MAX),
@ExpFieldValue NVARCHAR(MAX)
AS
BEGIN

DECLARE @NonInfoColumns VARCHAR(MAX);
DECLARE @ExperimentTableName NVARCHAR(MAX);
DECLARE @Query NVARCHAR(MAX);
DECLARE @AnlysisColumns VARCHAR(MAX);
DECLARE @InfoColumns NVARCHAR(MAX);
DECLARE @CmIdFilter NVARCHAR(MAX) = '';
DECLARE @DateFilter NVARCHAR(MAX) = '';
DECLARE @ExpFieldFilter NVARCHAR(MAX) = '';
DECLARE @WhereClause NVARCHAR(MAX) = '';

 SELECT @ExperimentTableName = STUFF(
					(	SELECT CONCAT(ExpDbTableNameId,'') FROM 
							Experiment
					        WHERE ExpId = @ExperimentId
							FOR xml path('')
					)
						, 1
                        , 0
                        , '')
 SELECT @NonInfoColumns = STUFF(
                        (   SELECT CONCAT(ExpF.ExpDbFieldNameId, ' AS ', '''', TCol.TargetColumnLabel, '''', ', ',' CASE WHEN ', TColGr.TargetColumnGroupIsFail ,' = 0 THEN ', '''', 'Pass', '''', ' WHEN ', ExpF.ExpDbFieldNameId, ' < ', Tcol.TargetColumnMinValue, ' OR ', ExpF.ExpDbFieldNameId, ' > ', Tcol.TargetColumnMaxValue, ' THEN', '''', 'Fail', '''', ' ELSE', '''', 'Pass', '''', ' END AS ', '''',TCol.TargetColumnLabel, ' Result', '''', ', ') FROM 
							TargetColumn 
							AS TCol 
							LEFT JOIN 
							ExperimentField 
							AS ExpF 
							ON TCol.ExpFieldId = ExpF.ExpFieldId 
							LEFT JOIN
							TargetColumnGroup 
							AS TColGr
							ON TCol.TargetColumnGroupId = TColGr.TargetColumnGroupId
							LEFT JOIN
							Experiment
							AS Exper
							ON ExpF.ExpId = Exper.ExpId
							LEFT JOIN
							TargetReport 
							AS TarR
							ON Exper.ExpId = TarR.ExpId
							WHERE TarR.TargetReportId = @TargetReportId AND TCol.TargetColumnIsInfo = 0 ORDER BY TColGr.TargetColumnGroupPosition, TCol.TargetColumnGroupId
                            FOR xml path('')
                        )
                        , 1
                        , 0
                        , '')
SELECT @AnlysisColumns = STUFF(
                        (   SELECT CONCAT(' CASE WHEN ', TColGr.TargetColumnGroupIsFail, ' = 0 THEN 1 WHEN ', ExpF.ExpDbFieldNameId, ' < ', Tcol.TargetColumnMinValue, ' OR ', ExpF.ExpDbFieldNameId, ' > ', Tcol.TargetColumnMaxValue, ' THEN 0 ELSE 1 END * ') FROM 
							TargetColumn 
							AS TCol 
							LEFT JOIN 
							ExperimentField 
							AS ExpF 
							ON TCol.ExpFieldId = ExpF.ExpFieldId 
							LEFT JOIN
							TargetColumnGroup 
							AS TColGr
							ON TCol.TargetColumnGroupId = TColGr.TargetColumnGroupId
							LEFT JOIN
							Experiment
							AS Exper
							ON ExpF.ExpId = Exper.ExpId
							LEFT JOIN
							TargetReport 
							AS TarR
							ON Exper.ExpId = TarR.ExpId
							WHERE TarR.TargetReportId = @TargetReportId AND TCol.TargetColumnIsInfo = 0 ORDER BY TColGr.TargetColumnGroupPosition, TCol.TargetColumnGroupId
                            FOR xml path('')
                        )
                        , 1
                        , 0
                        , '')
SELECT @InfoColumns = STUFF(
                        (   SELECT CONCAT(ExpF.ExpDbFieldNameId, ', ') FROM 
							TargetColumn 
							AS TCol 
							LEFT JOIN 
							ExperimentField 
							AS ExpF 
							ON TCol.ExpFieldId = ExpF.ExpFieldId 
							LEFT JOIN
							TargetColumnGroup 
							AS TColGr
							ON TCol.TargetColumnGroupId = TColGr.TargetColumnGroupId
							LEFT JOIN
							Experiment
							AS Exper
							ON ExpF.ExpId = Exper.ExpId
							LEFT JOIN
							TargetReport 
							AS TarR
							ON Exper.ExpId = TarR.ExpId
							WHERE TarR.TargetReportId = @TargetReportId AND TCol.TargetColumnIsInfo = 1 ORDER BY TColGr.TargetColumnGroupPosition, TCol.TargetColumnGroupId
                            FOR xml path('')
                        )
                        , 1
                        , 0
                        , ''),
 @Query = 'SELECT RecordId AS Id,' + @InfoColumns + replace(replace(@NonInfoColumns,'&lt;', '<' ), '&gt;', '>') + 'CASE (' + replace(replace(LEFT(@AnlysisColumns, LEN(@AnlysisColumns) - 1),'&lt;', '<' ), '&gt;', '>') + ') WHEN 0 THEN ' + '''' + 'FAIL' + '''' + ' ELSE ' + '''' + 'PASS' + '''' + ' END ' + ' AS ' + '''' + 'Test Result' + '''' + ' FROM ' + @ExperimentTableName 
 --+ ' WHERE CmId = ' + @CmV  + ' AND' + @ExpFieldName + 'LIKE ' + '''' + '%' + @ExpFieldValue + '%' + '''' + ' AND ' + @Date + ' BETWEEN ' + '''' + @FDate + '''' + ' AND ' + '''' + @TDate + ''''
 
 IF(@CmId <> '') BEGIN SET @CmIdFilter = 'CmId = ' + @CmId END; 
 IF(@DateFieldName <> '') BEGIN SET @DateFilter = @DateFieldName + ' BETWEEN ' + '''' + @FromDate + '''' + ' AND ' + '''' + @ToDate + ''''; END;
 IF(@ExpFieldName <> '') BEGIN SET @ExpFieldFilter = @ExpFieldName + '= ' + '''' + @ExpFieldValue + ''''; END;

  IF(@CmId <> '' AND @DateFieldName = '' AND @ExpFieldName = '') 
	BEGIN SET @WhereClause = 'WHERE ' + @CmIdFilter; END;
 
  IF(@CmId <> '' AND @DateFieldName <> '' AND @ExpFieldName = '') 
	BEGIN SET @WhereClause = 'WHERE ' + @CmIdFilter + ' AND ' + @DateFilter; END;
	
  IF(@CmId <> '' AND @DateFieldName <> '' AND @ExpFieldName <> '') 
	BEGIN SET @WhereClause = 'WHERE ' + @CmIdFilter+ ' AND ' + @DateFilter+ ' AND ' + @ExpFieldFilter; END;
	
  IF(@CmId = '' AND @DateFieldName <> '' AND @ExpFieldName = '') 
	BEGIN SET @WhereClause = 'WHERE ' + @DateFilter; END;
	
  IF(@CmId = '' AND @DateFieldName <> '' AND @ExpFieldName <> '') 
	BEGIN SET @WhereClause = 'WHERE ' + @DateFilter + ' AND ' + @ExpFieldFilter; END;

  IF(@CmId = '' AND @DateFieldName = '' AND @ExpFieldName <> '') 
	BEGIN SET @WhereClause = 'WHERE ' + @ExpFieldFilter; END;

	
  IF(@WhereClause <> '') 
	BEGIN SET @Query = @Query + ' ' + @WhereClause END;


	
 --SELECT @Query;
 --SET @Info = @Query;
 
 EXECUTE sp_executesql @Query;
 END