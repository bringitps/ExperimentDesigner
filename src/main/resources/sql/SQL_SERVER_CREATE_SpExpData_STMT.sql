CREATE PROCEDURE [spExpData]

@ExperimentId NVARCHAR(MAX)

AS

BEGIN

DECLARE @Table NVARCHAR(MAX);
DECLARE @RptTable NVARCHAR(MAX);
DECLARE @Fields NVARCHAR(MAX);
DECLARE @Query NVARCHAR(MAX);
DECLARE @Delete NVARCHAR(MAX);

SELECT @RptTable = STUFF(
                        (   SELECT CONCAT(ExpDbRptTableNameId, '') FROM 
							Experiment
							WHERE ExpId = @ExperimentId
                            FOR xml path('')
                        ), 1, 0, '')

SELECT @Table = STUFF(
                        (   SELECT CONCAT(ExpDbTableNameId, '') FROM 
							Experiment 
							WHERE ExpId = @ExperimentId
                            FOR xml path('')
                        ), 1, 0, '')

SELECT @Fields = STUFF(
                        (   SELECT CONCAT('ExpTb.', ExpDbFieldNameId, ', ') FROM
							ExperimentField 
							WHERE ExpId = @ExperimentId
                            FOR xml path('')
                        ), 1, 0, ''),
 @Delete = 'TRUNCATE TABLE ' + @RptTable,
 @Query = 'INSERT INTO ' + @RptTable + '(RecordId, Comments, CmName, CreatedBy, LastModifiedBy, CreatedDate, LastModifiedDate, DataFileName, ' + LEFT(@Fields, LEN(@Fields) - 1) + ')' +' SELECT ExpTb.RecordId, ExpTb.Comments, Cm.CmName, CreatedBy.UserFullName, LastMod.UserFullName, ExpTb.CreatedDate, ExpTb.LastModifiedDate, Df.DataFileName, ' + LEFT(@Fields, LEN(@Fields) - 1) + ' FROM ' + @Table + ' AS ExpTb LEFT JOIN SysUser AS CreatedBy ON ExpTb.CreatedBy = CreatedBy.UserId LEFT JOIN SysUser AS LastMod ON ExpTb.LastModifiedBy = LastMod.UserId LEFT JOIN ContractManufacturer AS Cm ON ExpTb.CmId = Cm.CmId LEFT JOIN DataFile AS Df On ExpTb.DataFileId = Df.DataFileId'
--SELECT @Query;
--SELECT @Delete;
EXECUTE sp_executesql @Delete;
EXECUTE sp_executesql @Query;
END