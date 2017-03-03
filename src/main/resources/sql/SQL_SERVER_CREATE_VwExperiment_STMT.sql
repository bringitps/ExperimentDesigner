CREATE VIEW vwExperiment
AS
SELECT        Experiment.ExpId, Experiment.ExpName, Experiment.ExpDbTableNameId, Experiment.ExpComments, CreatedByUser.UserName AS CreatedBy, Experiment.CreatedDate, 
                         LastModifiedByUser.UserName AS LastModifiedBy, Experiment.ModifiedDate
FROM            Experiment AS Experiment LEFT OUTER JOIN
                         SysUser AS CreatedByUser ON Experiment.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
                         SysUser AS LastModifiedByUser ON Experiment.LastModifiedBy = LastModifiedByUser.UserId
				WHERE Experiment.ExpIsActive = 1;