CREATE VIEW [dbo].[vwCsvTemplate] as
SELECT  CsvTemplate.CsvTemplateId AS 'Id',
 CsvTemplate.CsvTemplateName AS 'Name',
 Experiment.ExpName as 'Experiment Name',
               CreatedByUser.UserName AS 'CreatedBy', Job.JobExecRepeatName AS 'Job',
                CsvTemplate.CreatedDate, CsvTemplate.ModifiedDate, 
              InFileRepo.FileRepoHost, InFileRepo.FileRepoPath, CsvTemplate.InboundFileRepoId AS 'Inbound', CsvTemplate.ProcessedFileRepoId 'Processed', CsvTemplate.ExceptionFileRepoId AS 'Exception', CMF.CmName AS 'CmName'
FROM     CsvTemplate LEFT OUTER JOIN
               FilesRepository AS InFileRepo ON CsvTemplate.InboundFileRepoId = InFileRepo.FileRepoId LEFT OUTER JOIN
               SysUser AS CreatedByUser ON CsvTemplate.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
               Experiment AS Experiment ON CsvTemplate.ExperimentId = Experiment.ExpId LEFT OUTER JOIN
               JobExecutionRepeat AS Job ON CsvTemplate.JobExecRepeatId = Job.JobExecRepeatId LEFT OUTER JOIN
			   ContractManufacturer AS CMF ON CsvTemplate.CmId = CMF.CmId
               WHERE CsvTemplate.CsvTemplateIsActive = 1;
GO
