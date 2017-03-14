CREATE VIEW vwCsvTemplate as
SELECT  CsvTemplate.CsvTemplateId AS 'Id',
 CsvTemplate.CsvTemplateName AS 'Name',
 Experiment.ExpName as 'Experiment Name',
               CreatedByUser.UserName AS 'CreatedBy', Job.JobExecRepeatName AS 'Job',
                CsvTemplate.CreatedDate, CsvTemplate.ModifiedDate, 
              InFileRepo.FileRepoHost, InFileRepo.FileRepoPath
FROM     CsvTemplate LEFT OUTER JOIN
               FilesRepository AS InFileRepo ON CsvTemplate.InboundFileRepoId = InFileRepo.FileRepoId LEFT OUTER JOIN
               SysUser AS CreatedByUser ON CsvTemplate.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
               Experiment AS Experiment ON CsvTemplate.ExperimentId = Experiment.ExpId LEFT OUTER JOIN
               JobExecutionRepeat AS Job ON CsvTemplate.JobExecRepeatId = Job.JobExecRepeatId
               WHERE CsvTemplate.CsvTemplateIsActive = 1;
GO
