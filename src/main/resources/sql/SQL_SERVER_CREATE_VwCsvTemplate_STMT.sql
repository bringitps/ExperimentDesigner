CREATE VIEW vwCsvTemplate as
SELECT  dbo.CsvTemplate.CsvTemplateId AS 'Id',
 dbo.CsvTemplate.CsvTemplateName AS 'Name',
 Experiment.ExpName as 'Experiment Name', Experiment.ExpDbTableNameId AS 'Table Name',
               CreatedByUser.UserName AS 'CreatedBy', Job.JobExecRepeatName AS 'Job',
                dbo.CsvTemplate.CreatedDate, dbo.CsvTemplate.ModifiedDate, 
              InFileRepo.FileRepoHost, InFileRepo.FileRepoPath
FROM     dbo.CsvTemplate LEFT OUTER JOIN
               dbo.FilesRepository AS InFileRepo ON dbo.CsvTemplate.InboundFileRepoId = InFileRepo.FileRepoId LEFT OUTER JOIN
               dbo.SysUser AS CreatedByUser ON dbo.CsvTemplate.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
               dbo.Experiment AS Experiment ON dbo.CsvTemplate.ExperimentId = Experiment.ExpId LEFT OUTER JOIN
               dbo.JobExecutionRepeat AS Job ON dbo.CsvTemplate.JobExecRepeatId = Job.JobExecRepeatId
               WHERE CsvTemplate.CsvTemplateIsActive = 1;