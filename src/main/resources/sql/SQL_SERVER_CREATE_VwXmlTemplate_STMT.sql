CREATE VIEW vwXmlTemplate as
SELECT  XmlTemplate.XmlTemplateId AS 'Id',
 XmlTemplate.XmlTemplateName AS 'Name',
 Experiment.ExpName as 'Experiment Name',
               CreatedByUser.UserName AS 'CreatedBy', Job.JobExecRepeatName AS 'Job',
                XmlTemplate.CreatedDate, XmlTemplate.ModifiedDate, 
              InFileRepo.FileRepoHost, InFileRepo.FileRepoPath
FROM     XmlTemplate LEFT OUTER JOIN
               FilesRepository AS InFileRepo ON XmlTemplate.InboundFileRepoId = InFileRepo.FileRepoId LEFT OUTER JOIN
               SysUser AS CreatedByUser ON XmlTemplate.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
               Experiment AS Experiment ON XmlTemplate.ExperimentId = Experiment.ExpId LEFT OUTER JOIN
               JobExecutionRepeat AS Job ON XmlTemplate.JobExecRepeatId = Job.JobExecRepeatId
               WHERE XmlTemplate.XmlTemplateIsActive = 1;