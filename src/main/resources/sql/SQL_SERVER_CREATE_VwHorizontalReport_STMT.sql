CREATE VIEW vwHorizontalReport
AS
SELECT     VwHorizontalRpt.VwHorizontalRptId AS 'Id', VwHorizontalRpt.VwHorizontalRptName AS 'Name', VwHorizontalRpt.VwHorizontalRptDescription AS 'Description',
      CreatedByUser.UserName AS CreatedBy, VwHorizontalRpt.CreatedDate, LastModifiedByUser.UserName AS LastModifiedBy, VwHorizontalRpt.ModifiedDate
FROM   ViewHorizontalReport VwHorizontalRpt
LEFT OUTER JOIN SysUser AS CreatedByUser ON VwHorizontalRpt.CreatedBy = CreatedByUser.UserId 
LEFT OUTER JOIN SysUser AS LastModifiedByUser ON VwHorizontalRpt.LastModifiedBy = LastModifiedByUser.UserId

WHERE VwHorizontalRptIsActive = 1;