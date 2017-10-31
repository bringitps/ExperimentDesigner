CREATE VIEW vwFirstTimeYieldReport
AS
SELECT     FirstTimeYieldRpt.FtyReportId AS 'Id', FirstTimeYieldRpt.FtyReportName AS 'Name', FirstTimeYieldRpt.FtyReportDescription AS 'Description',
FtyExp.ExpName AS 'Experiment',  CreatedByUser.UserName AS CreatedBy, FirstTimeYieldRpt.CreatedDate, LastModifiedByUser.UserName AS LastModifiedBy, FirstTimeYieldRpt.ModifiedDate

FROM   FirstTimeYieldReport FirstTimeYieldRpt

LEFT OUTER JOIN Experiment FtyExp ON  FirstTimeYieldRpt.ExpId = FtyExp.ExpId 
LEFT OUTER JOIN SysUser AS CreatedByUser ON FirstTimeYieldRpt.CreatedBy = CreatedByUser.UserId 
LEFT OUTER JOIN SysUser AS LastModifiedByUser ON FirstTimeYieldRpt.LastModifiedBy = LastModifiedByUser.UserId
WHERE FirstTimeYieldRpt.FtyReportIsActive = 1;