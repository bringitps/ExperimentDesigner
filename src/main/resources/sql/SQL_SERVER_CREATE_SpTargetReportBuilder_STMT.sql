CREATE PROCEDURE [spTargetReportBuilder]

@TargetReportId NVARCHAR(MAX)

AS
BEGIN

DECLARE @NonInfoColumns VARCHAR(MAX);
DECLARE @ExperimentTableName NVARCHAR(MAX);
DECLARE @Query NVARCHAR(MAX);
DECLARE @Delete NVARCHAR(MAX);
DECLARE @AnlysisColumns VARCHAR(MAX);
DECLARE @InfoColumns NVARCHAR(MAX);
DECLARE @TableRPT NVARCHAR(MAX) = '';
  
 SELECT @TableRPT = STUFF(
                        (   SELECT CONCAT(TargetReportDbRptTableNameId, '') FROM TargetReport WHERE TargetReportId = @TargetReportId
                            FOR xml path('')
                        ), 1, 0, '')
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
 SELECT @NonInfoColumns = STUFF(
                        (   SELECT CONCAT(ExpF.ExpDbFieldNameId,', ',' CASE WHEN ', TColGr.TargetColumnGroupIsFail ,' = 0 THEN ', '''', 'Pass', '''', ' WHEN ', ExpF.ExpDbFieldNameId, ' < ', Tcol.TargetColumnMinValue, ' OR ', ExpF.ExpDbFieldNameId, ' > ', Tcol.TargetColumnMaxValue, ' THEN ', '''', 'Fail', '''', ' ELSE ', '''', 'Pass', '''', ' END ', ', ') FROM 
							
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
SELECT @AnlysisColumns = STUFF(
                        (   SELECT CONCAT(' CASE WHEN ', TColGr.TargetColumnGroupIsFail, ' = 0 THEN 1 WHEN ', ExpF.ExpDbFieldNameId, ' < ', Tcol.TargetColumnMinValue, ' OR ', ExpF.ExpDbFieldNameId, ' > ', Tcol.TargetColumnMaxValue, ' THEN 0 ELSE 1 END * ') FROM 
							
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