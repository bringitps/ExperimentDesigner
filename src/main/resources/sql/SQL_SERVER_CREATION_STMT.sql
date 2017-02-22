CREATE VIEW [dbo].[vwExperiment]
AS
SELECT        Experiment.ExpId, Experiment.ExpName, Experiment.ExpDbTableNameId, Experiment.ExpIsActive, Experiment.ExpComments, CreatedByUser.UserName AS CreatedBy, Experiment.CreatedDate, 
                         LastModifiedByUser.UserName AS LastModifiedBy, Experiment.ModifiedDate
FROM            dbo.Experiment AS Experiment LEFT OUTER JOIN
                         dbo.SysUser AS CreatedByUser ON Experiment.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
                         dbo.SysUser AS LastModifiedByUser ON Experiment.LastModifiedBy = LastModifiedByUser.UserId


CREATE VIEW vwXmlTemplate as
SELECT  dbo.XmlTemplate.XmlTemplateId AS 'Id',
 dbo.XmlTemplate.XmlTemplateName AS 'Name',
 Experiment.ExpName as 'Experiment Name', Experiment.ExpDbTableNameId AS 'Table Name',
               CreatedByUser.UserName AS 'CreatedBy', Job.JobExecRepeatLabel AS 'Job',
                dbo.XmlTemplate.CreatedDate, dbo.XmlTemplate.ModifiedDate, 
              InFileRepo.FileRepoHost, InFileRepo.FileRepoPath
FROM     dbo.XmlTemplate LEFT OUTER JOIN
               dbo.FilesRepository AS InFileRepo ON dbo.XmlTemplate.InboundFileRepoId = InFileRepo.FileRepoId LEFT OUTER JOIN
               dbo.SysUser AS CreatedByUser ON dbo.XmlTemplate.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
               dbo.Experiment AS Experiment ON dbo.XmlTemplate.ExperimentId = Experiment.ExpId LEFT OUTER JOIN
               dbo.JobExecutionRepeat AS Job ON dbo.XmlTemplate.JobExecRepeatId = Job.JobExecRepeatId