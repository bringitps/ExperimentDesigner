CREATE VIEW vwCsvTemplate as
SELECT  CsvTemplate.CsvTemplateId AS 'Id',
 CsvTemplate.CsvTemplateName AS 'Name',
 Experiment.ExpName as 'Experiment',
			  CMF.CmName as 'Contract Manofacturer',
              Job.JobExecRepeatName AS 'Exec Repeat',
              InFileRepo.FileRepoHost 'Inbound host', InFileRepo.FileRepoPath 'Inbound path', InFileProc.FileRepoHost AS 'Processed host', InFileProc.FileRepoPath AS 'Processed path', 
			  InFileExce.FileRepoHost AS 'Exception host', InFileExce.FileRepoPath AS 'Exception path', CreatedByUser.UserName AS 'CreatedBy', 
			  CsvTemplate.CreatedDate, ModifiedByUser.UserName AS 'ModifiedBy', CsvTemplate.ModifiedDate
FROM     CsvTemplate LEFT OUTER JOIN
               FilesRepository AS InFileRepo ON CsvTemplate.InboundFileRepoId = InFileRepo.FileRepoId LEFT OUTER JOIN
			   FilesRepository AS InFileProc ON CsvTemplate.ProcessedFileRepoId = InFileProc.FileRepoId LEFT OUTER JOIN
			   FilesRepository AS InFileExce ON CsvTemplate.ExceptionFileRepoId = InFileExce.FileRepoId LEFT OUTER JOIN
               SysUser AS CreatedByUser ON CsvTemplate.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
			   SysUser AS ModifiedByUser ON CsvTemplate.LastModifiedBy = ModifiedByUser.UserId LEFT OUTER JOIN
               Experiment AS Experiment ON CsvTemplate.ExperimentId = Experiment.ExpId LEFT OUTER JOIN
               JobExecutionRepeat AS Job ON CsvTemplate.JobExecRepeatId = Job.JobExecRepeatId LEFT OUTER JOIN
			   ContractManufacturer AS CMF ON CsvTemplate.CmId = CMF.CmId
               WHERE CsvTemplate.CsvTemplateIsActive = 1;