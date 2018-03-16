CREATE VIEW vwVerticalReport
AS
SELECT     VwVerticalRpt.VwVerticalRptId AS 'Id', VwVerticalRpt.VwVerticalRptName AS 'Name', VwVerticalRpt.VwVerticalRptDescription AS 'Description',
      CreatedByUser.UserName AS CreatedBy, VwVerticalRpt.CreatedDate, LastModifiedByUser.UserName AS LastModifiedBy, VwVerticalRpt.ModifiedDate
FROM   ViewVerticalReport VwVerticalRpt
LEFT OUTER JOIN SysUser AS CreatedByUser ON VwVerticalRpt.CreatedBy = CreatedByUser.UserId 
LEFT OUTER JOIN SysUser AS LastModifiedByUser ON VwVerticalRpt.LastModifiedBy = LastModifiedByUser.UserId

WHERE VwVerticalRptIsActive = 1;