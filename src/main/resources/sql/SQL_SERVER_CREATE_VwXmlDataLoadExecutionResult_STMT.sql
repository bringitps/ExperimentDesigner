CREATE VIEW vwXmlDataLoadExecutionResult as
SELECT  
XmlDataLoadExecutionResult.XmlDataLoadExecId AS 'Id',
XmlDataLoadExecutionResult.XmlDataLoadExecTime AS 'Execution Date',
DataFile.DataFileName AS 'FileName',
CONCAT(FileRepo.FileRepoHost, FileRepo.FileRepoPath) AS 'Location',
CreatedByUser.UserName AS 'Created By',
XmlTemplate.XmlTemplateName AS 'Template',
Experiment.ExpName AS 'Experiment',
(CASE WHEN XmlDataLoadExecutionResult.XmlDataLoadExecException = 1 THEN 'Yes' ELSE 'No' END) AS 'Exception',
XmlDataLoadExecutionResult.XmlDataLoadExecExceptionDetails AS 'Exception Details'


FROM     XmlDataLoadExecutionResult LEFT OUTER JOIN
               DataFile AS DataFile ON XmlDataLoadExecutionResult.DataFileId = DataFile.DataFileId LEFT OUTER JOIN
               SysUser AS CreatedByUser ON DataFile.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
               FilesRepository AS FileRepo ON DataFile.FileRepoId = FileRepo.FileRepoId LEFT OUTER JOIN
               XmlTemplate AS XmlTemplate ON XmlDataLoadExecutionResult.XmlTemplateId = XmlTemplate.XmlTemplateId LEFT OUTER JOIN
			   Experiment AS Experiment ON XmlTemplate.ExperimentId = Experiment.ExpId;