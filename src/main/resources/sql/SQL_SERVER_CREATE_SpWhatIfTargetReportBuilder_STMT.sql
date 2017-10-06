CREATE PROCEDURE [spWhatIfTargetReportBuilder]

@TargetReportId NVARCHAR(MAX)

AS
BEGIN

DECLARE @NonInfoColumns NVARCHAR(MAX);
DECLARE @NewNonInfoColumns NVARCHAR(MAX);
DECLARE @ExperimentTableName NVARCHAR(MAX);
DECLARE @Query NVARCHAR(MAX);
DECLARE @Delete NVARCHAR(MAX);
DECLARE @AnlysisColumns NVARCHAR(MAX);
DECLARE @InfoColumns NVARCHAR(MAX);
DECLARE @TableRPT NVARCHAR(MAX) = '';

/* Get Target Report table name */
SELECT @TableRPT = STUFF(
                        (   SELECT CONCAT(TargetReportDbRptTableNameId, '') FROM TargetReport WHERE TargetReportId = @TargetReportId
                            FOR xml path('')
                        ), 1, 0, '')

/* Experiment Table name */
SELECT @ExperimentTableName = STUFF(
					(	SELECT CONCAT(Experiment.ExpDbTableNameId,'') FROM 
							TargetReport 
							INNER JOIN Experiment ON TargetReport.ExpId = Experiment.ExpId
					        WHERE TargetReport.TargetReportId = @TargetReportId
							FOR xml path('')
					)
						, 1
                        , 0
                        , '')

/* Bkp 
SELECT @NonInfoColumns = STUFF(
                        (   SELECT CONCAT(ExpF.ExpDbFieldNameId, ',''', TCol.TargetColumnWhatIfCurrentMin, ''',''', TCol.TargetColumnWhatIfCurrentMax, ''', CASE WHEN ', TColGr.TargetColumnGroupIsFail ,' = 0 THEN ', '''', 'Pass', '''', ' WHEN ', ExpF.ExpDbFieldNameId, ' < ', Tcol.TargetColumnWhatIfCurrentMin, ' OR ', ExpF.ExpDbFieldNameId, ' > ', Tcol.TargetColumnWhatIfCurrentMax, ' THEN ', '''', 'Fail', '''', ' ELSE ', '''', 'Pass', '''', ' END ', ', ') FROM 
							
							TargetColumn 
							AS TCol 
							INNER JOIN 
							ExperimentField 
							AS ExpF 
							ON TCol.ExpFieldId = ExpF.ExpFieldId 
							INNER JOIN
							TargetColumnGroup 
							AS TColGr
							ON TCol.TargetColumnGroupId = TColGr.TargetColumnGroupId
							INNER JOIN							
							TargetReport 
							AS TarR
							ON TColGr.TargetReportId = TarR.TargetReportId
							WHERE TarR.TargetReportId = @TargetReportId AND TCol.TargetColumnIsInfo = 0 ORDER BY TColGr.TargetColumnGroupPosition, TCol.TargetColumnGroupId
                            FOR xml path('')
                        )
                        , 1
                        , 0
                        , '')
*/

SELECT @NonInfoColumns = STUFF(
                        (   SELECT CONCAT(ExpF.ExpDbFieldNameId, ',''', TCol.TargetColumnWhatIfCurrentMin, ''',''', TCol.TargetColumnWhatIfCurrentMax, ''', CASE WHEN ', TColGr.TargetColumnGroupIsFail ,' = 0 THEN ', '''', 'Pass', '''', ' WHEN ', ExpF.ExpDbFieldNameId, ' < ', Tcol.TargetColumnWhatIfCurrentMin, ' OR ', ExpF.ExpDbFieldNameId, ' > ', Tcol.TargetColumnWhatIfCurrentMax, ' THEN ', '''', 'Fail', '''', ' ELSE ', '''', 'Pass', '''', ' END ', ', '
							,'''', TCol.TargetColumnWhatIfNewMin, ''',''', TCol.TargetColumnWhatIfNewMax, ''', CASE WHEN ', TColGr.TargetColumnGroupIsFail ,' = 0 THEN ', '''', 'Pass', '''', ' WHEN ', ExpF.ExpDbFieldNameId, ' < ', Tcol.TargetColumnWhatIfNewMin, ' OR ', ExpF.ExpDbFieldNameId, ' > ', Tcol.TargetColumnWhatIfNewMax, ' THEN ', '''', 'Fail', '''', ' ELSE ', '''', 'Pass', '''', ' END ', ', ') FROM 
							
							TargetColumn 
							AS TCol 
							INNER JOIN 
							ExperimentField 
							AS ExpF 
							ON TCol.ExpFieldId = ExpF.ExpFieldId 
							INNER JOIN
							TargetColumnGroup 
							AS TColGr
							ON TCol.TargetColumnGroupId = TColGr.TargetColumnGroupId
							INNER JOIN							
							TargetReport 
							AS TarR
							ON TColGr.TargetReportId = TarR.TargetReportId
							WHERE TarR.TargetReportId = @TargetReportId AND TCol.TargetColumnIsInfo = 0 ORDER BY TColGr.TargetColumnGroupPosition, TCol.TargetColumnGroupId
                            FOR xml path('')
                        )
                        , 1
                        , 0
                        , '')
/*
SELECT @NewNonInfoColumns = STUFF(
                        (   SELECT CONCAT('''', TCol.TargetColumnWhatIfNewMin, ''',''', TCol.TargetColumnWhatIfNewMax, ''', CASE WHEN ', TColGr.TargetColumnGroupIsFail ,' = 0 THEN ', '''', 'Pass', '''', ' WHEN ', ExpF.ExpDbFieldNameId, ' < ', Tcol.TargetColumnWhatIfNewMin, ' OR ', ExpF.ExpDbFieldNameId, ' > ', Tcol.TargetColumnWhatIfNewMax, ' THEN ', '''', 'Fail', '''', ' ELSE ', '''', 'Pass', '''', ' END ', ', ') FROM 
							
							TargetColumn 
							AS TCol 
							INNER JOIN 
							ExperimentField 
							AS ExpF 
							ON TCol.ExpFieldId = ExpF.ExpFieldId 
							INNER JOIN
							TargetColumnGroup 
							AS TColGr
							ON TCol.TargetColumnGroupId = TColGr.TargetColumnGroupId
							INNER JOIN							
							TargetReport 
							AS TarR
							ON TColGr.TargetReportId = TarR.TargetReportId
							WHERE TarR.TargetReportId = @TargetReportId AND TCol.TargetColumnIsInfo = 0 ORDER BY TColGr.TargetColumnGroupPosition, TCol.TargetColumnGroupId
                            FOR xml path('')
                        )
                        , 1
                        , 0
                        , '')
						
*/
SELECT @AnlysisColumns = STUFF(
                        (   SELECT CONCAT(' CASE WHEN ', TColGr.TargetColumnGroupIsFail, ' = 0 THEN 1 WHEN ', ExpF.ExpDbFieldNameId, ' < ', Tcol.TargetColumnWhatIfNewMin, ' OR ', ExpF.ExpDbFieldNameId, ' > ', Tcol.TargetColumnWhatIfNewMax, ' THEN 0 ELSE 1 END * ') FROM 
							
							TargetColumn 
							AS TCol 
							INNER JOIN 
							ExperimentField 
							AS ExpF 
							ON TCol.ExpFieldId = ExpF.ExpFieldId 
							INNER JOIN
							TargetColumnGroup 
							AS TColGr
							ON TCol.TargetColumnGroupId = TColGr.TargetColumnGroupId
							INNER JOIN							
							TargetReport 
							AS TarR
							ON TColGr.TargetReportId = TarR.TargetReportId
							WHERE TarR.TargetReportId = @TargetReportId AND TCol.TargetColumnIsInfo = 0 ORDER BY TColGr.TargetColumnGroupPosition, TCol.TargetColumnGroupId
                            FOR xml path('')
                        )
                        , 1
                        , 0
                        , '')

/* Bkp
SELECT @AnlysisColumns = STUFF(
                        (   SELECT CONCAT(' CASE WHEN ', TColGr.TargetColumnGroupIsFail, ' = 0 THEN 1 WHEN ', ExpF.ExpDbFieldNameId, ' < ', Tcol.TargetColumnWhatIfCurrentMin, ' OR ', ExpF.ExpDbFieldNameId, ' > ', Tcol.TargetColumnWhatIfCurrentMax, ' THEN 0 ELSE 1 END * ') FROM 
							
							TargetColumn 
							AS TCol 
							INNER JOIN 
							ExperimentField 
							AS ExpF 
							ON TCol.ExpFieldId = ExpF.ExpFieldId 
							INNER JOIN
							TargetColumnGroup 
							AS TColGr
							ON TCol.TargetColumnGroupId = TColGr.TargetColumnGroupId
							INNER JOIN							
							TargetReport 
							AS TarR
							ON TColGr.TargetReportId = TarR.TargetReportId
							WHERE TarR.TargetReportId = @TargetReportId AND TCol.TargetColumnIsInfo = 0 ORDER BY TColGr.TargetColumnGroupPosition, TCol.TargetColumnGroupId
                            FOR xml path('')
                        )
                        , 1
                        , 0
                        , '')
*/
SELECT @InfoColumns = STUFF(
                        (   SELECT CONCAT(ExpF.ExpDbFieldNameId, ', ') FROM 
							TargetColumn 
							AS TCol 
							INNER JOIN 
							ExperimentField 
							AS ExpF 
							ON TCol.ExpFieldId = ExpF.ExpFieldId 
							INNER JOIN
							TargetColumnGroup 
							AS TColGr
							ON TCol.TargetColumnGroupId = TColGr.TargetColumnGroupId
							INNER JOIN							
							TargetReport 
							AS TarR
							ON TColGr.TargetReportId = TarR.TargetReportId
							WHERE TarR.TargetReportId = @TargetReportId AND TCol.TargetColumnIsInfo = 1 ORDER BY TColGr.TargetColumnGroupPosition, TCol.TargetColumnGroupId
                            FOR xml path('')
                        )
                        , 1
                        , 0
                        , ''),
 @Delete = 'TRUNCATE TABLE ' + @TableRPT,
 @Query = 'INSERT INTO ' + @TableRPT + ' SELECT ExpTb.RecordId, ExpTb.Comments, Cm.CmName, CreatedBy.UserFullName, LastMod.UserFullName, ExpTb.CreatedDate, ExpTb.LastModifiedDate, Df.DataFileName, ' + @InfoColumns + replace(replace(@NonInfoColumns,'&lt;', '<' ), '&gt;', '>') + 'CASE (' + replace(replace(LEFT(@AnlysisColumns, LEN(@AnlysisColumns) - 1),'&lt;', '<' ), '&gt;', '>') + ') WHEN 0 THEN ' + '''' + 'FAIL' + '''' + ' ELSE ' + '''' + 'PASS' + '''' + ' END ' + ' FROM ' + @ExperimentTableName + ' AS ExpTb LEFT JOIN SysUser AS CreatedBy ON ExpTb.CreatedBy = CreatedBy.UserId LEFT JOIN SysUser AS LastMod ON ExpTb.LastModifiedBy = LastMod.UserId LEFT JOIN ContractManufacturer AS Cm ON ExpTb.CmId = Cm.CmId LEFT JOIN DataFile AS Df On ExpTb.DataFileId = Df.DataFileId' 

 --SELECT @Query;
 --SELECT @NonInfoColumns;
 --SELECT @InfoColumns;
 --SELECT @AnlysisColumns;
 --SET @Info = @Query;
 EXECUTE sp_executesql @Delete;
 EXECUTE sp_executesql @Query;
 END