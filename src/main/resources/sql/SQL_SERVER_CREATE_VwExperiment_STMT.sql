CREATE VIEW vwExperiment
AS
SELECT        Experiment.ExpId AS Id, Experiment.ExpName AS Name, Experiment.ExpDbTableNameId AS DbTableNameId, Experiment.ExpComments AS Comments, CreatedByUser.UserName AS CreatedBy, Experiment.CreatedDate, 
                         LastModifiedByUser.UserName AS LastModifiedBy, Experiment.ModifiedDate
FROM            Experiment AS Experiment LEFT OUTER JOIN
                         SysUser AS CreatedByUser ON Experiment.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
                         SysUser AS LastModifiedByUser ON Experiment.LastModifiedBy = LastModifiedByUser.UserId
				WHERE Experiment.ExpIsActive = 1;