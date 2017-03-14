CREATE VIEW vwTargetReport
AS
SELECT     TargetRpt.TargetReportId AS 'Id', TargetRpt.TargetReportName AS 'Name', TargetRpt.TargetReportDescription AS 'Description',
      TargetExp.ExpName AS 'Experiment', CreatedByUser.UserName AS CreatedBy, TargetRpt.CreatedDate, LastModifiedByUser.UserName AS LastModifiedBy, TargetRpt.ModifiedDate
FROM   TargetReport TargetRpt
LEFT OUTER JOIN Experiment TargetExp ON  TargetRpt.ExpId = TargetExp.ExpId 
LEFT OUTER JOIN SysUser AS CreatedByUser ON TargetRpt.CreatedBy = CreatedByUser.UserId 
LEFT OUTER JOIN SysUser AS LastModifiedByUser ON TargetRpt.LastModifiedBy = LastModifiedByUser.UserId

WHERE TargetReportIsActive = 1;