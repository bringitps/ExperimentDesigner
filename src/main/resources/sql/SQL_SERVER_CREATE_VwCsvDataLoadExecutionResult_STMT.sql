CREATE VIEW vwXmlDataLoadExecutionResult as
SELECT  
CsvDataLoadExecutionResult.CsvDataLoadExecId AS 'Id',
CsvDataLoadExecutionResult.CsvDataLoadExecTime AS 'Execution Date',
DataFile.DataFileName AS 'FileName',
CONCAT(FileRepo.FileRepoHost, FileRepo.FileRepoPath) AS 'Location',
CreatedByUser.UserName AS 'Created By',
CsvTemplate.CsvTemplateName AS 'Template',
Experiment.ExpName AS 'Experiment',
(CASE WHEN CsvDataLoadExecutionResult.CsvDataLoadExecException = 1 THEN 'Yes' ELSE 'No' END) AS 'Exception',
CsvDataLoadExecutionResult.CsvDataLoadExecExceptionDetails AS 'Exception Details'


FROM     CsvDataLoadExecutionResult LEFT OUTER JOIN
               DataFile AS DataFile ON CsvDataLoadExecutionResult.DataFileId = DataFile.DataFileId LEFT OUTER JOIN
               SysUser AS CreatedByUser ON DataFile.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
               FilesRepository AS FileRepo ON DataFile.FileRepoId = FileRepo.FileRepoId LEFT OUTER JOIN
               CsvTemplate AS CsvTemplate ON CsvDataLoadExecutionResult.CsvTemplateId = CsvTemplate.CsvTemplateId LEFT OUTER JOIN
			   Experiment AS Experiment ON CsvTemplate.ExperimentId = Experiment.ExpId;
