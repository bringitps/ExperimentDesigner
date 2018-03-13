CREATE VIEW vwFinalPassYieldReport
AS
SELECT     FinalPassYieldRpt.FnyReportId AS 'Id', FinalPassYieldRpt.FnyReportName AS 'Name', FinalPassYieldRpt.FnyReportDescription AS 'Description',
FnyExp.ExpName AS 'Experiment',  CreatedByUser.UserName AS CreatedBy, FinalPassYieldRpt.CreatedDate, LastModifiedByUser.UserName AS LastModifiedBy, FinalPassYieldRpt.ModifiedDate

FROM   FinalPassYieldReport FinalPassYieldRpt

LEFT OUTER JOIN Experiment FnyExp ON  FinalPassYieldRpt.ExpId = FnyExp.ExpId 
LEFT OUTER JOIN SysUser AS CreatedByUser ON FinalPassYieldRpt.CreatedBy = CreatedByUser.UserId 
LEFT OUTER JOIN SysUser AS LastModifiedByUser ON FinalPassYieldRpt.LastModifiedBy = LastModifiedByUser.UserId
WHERE FinalPassYieldRpt.FnyReportIsActive = 1;