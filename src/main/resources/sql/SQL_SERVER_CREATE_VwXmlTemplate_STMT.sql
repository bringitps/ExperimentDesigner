CREATE VIEW vwXmlTemplate as
SELECT  XmlTemplate.XmlTemplateId AS 'Id',
 XmlTemplate.XmlTemplateName AS 'Name',
 Experiment.ExpName as 'Experiment',
			  CMF.CmName as 'Contract Manufacturer',
              Job.JobExecRepeatName AS 'Exec Repeat',
              InFileRepo.FileRepoHost 'Inbound host', InFileRepo.FileRepoPath 'Inbound path', InFileProc.FileRepoHost AS 'Processed host', InFileProc.FileRepoPath AS 'Processed path', 
			  InFileExce.FileRepoHost AS 'Exception host', InFileExce.FileRepoPath AS 'Exception path', CreatedByUser.UserName AS 'CreatedBy', 
			  XmlTemplate.CreatedDate, ModifiedByUser.UserName AS 'ModifiedBy', XmlTemplate.ModifiedDate
FROM     XmlTemplate LEFT OUTER JOIN
               FilesRepository AS InFileRepo ON XmlTemplate.InboundFileRepoId = InFileRepo.FileRepoId LEFT OUTER JOIN
			   FilesRepository AS InFileProc ON XmlTemplate.ProcessedFileRepoId = InFileProc.FileRepoId LEFT OUTER JOIN
			   FilesRepository AS InFileExce ON XmlTemplate.ExceptionFileRepoId = InFileExce.FileRepoId LEFT OUTER JOIN
               SysUser AS CreatedByUser ON XmlTemplate.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
			   SysUser AS ModifiedByUser ON XmlTemplate.LastModifiedBy = ModifiedByUser.UserId LEFT OUTER JOIN
               Experiment AS Experiment ON XmlTemplate.ExperimentId = Experiment.ExpId LEFT OUTER JOIN
               JobExecutionRepeat AS Job ON XmlTemplate.JobExecRepeatId = Job.JobExecRepeatId LEFT OUTER JOIN
			   ContractManufacturer AS CMF ON XmlTemplate.CmId = CMF.CmId
               WHERE XmlTemplate.XmlTemplateIsActive = 1;