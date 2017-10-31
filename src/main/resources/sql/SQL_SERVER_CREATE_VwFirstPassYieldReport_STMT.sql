CREATE VIEW vwFirstPassYieldReport
AS
SELECT     FirstPassYieldRpt.FpyReportId AS 'Id', FirstPassYieldRpt.FpyReportName AS 'Name', FirstPassYieldRpt.FpyReportDescription AS 'Description',
FpyExp.ExpName AS 'Experiment',  CreatedByUser.UserName AS CreatedBy, FirstPassYieldRpt.CreatedDate, LastModifiedByUser.UserName AS LastModifiedBy, FirstPassYieldRpt.ModifiedDate

FROM   FirstPassYieldReport FirstPassYieldRpt

LEFT OUTER JOIN Experiment FpyExp ON  FirstPassYieldRpt.ExpId = FpyExp.ExpId 
LEFT OUTER JOIN SysUser AS CreatedByUser ON FirstPassYieldRpt.CreatedBy = CreatedByUser.UserId 
LEFT OUTER JOIN SysUser AS LastModifiedByUser ON FirstPassYieldRpt.LastModifiedBy = LastModifiedByUser.UserId
WHERE FirstPassYieldRpt.FpyReportIsActive = 1;