CREATE VIEW vwCsvDataLoadExecutionResult as
SELECT  
CsvDataLoadExecutionResult.CsvDataLoadExecId AS 'Id',
CsvDataLoadExecutionResult.CsvDataLoadExecTime AS 'Execution Date',
DataFileOriginal.DataFileName AS 'Original FileName',
DataFileProcessed.DataFileName AS 'Processed FileName',
DataFileException.DataFileName AS 'Exception FileName',
CreatedByUser.UserName AS 'Created By',
CsvTemplate.CsvTemplateName AS 'Template',
Experiment.ExpName AS 'Experiment',
CsvDataLoadExecutionResult.CsvDataLoadTotalRecords AS 'Total Records in File',
CsvDataLoadExecutionResult.CsvDataLoadTotalRecordsProcessed AS 'Total Records Processed',
CsvDataLoadExecutionResult.CsvDataLoadTotalRecordsException AS 'Total Records Exception',
(CASE WHEN CsvDataLoadExecutionResult.CsvDataLoadExecException = 1 THEN 'Yes' ELSE 'No' END) AS 'Exception',
CsvDataLoadExecutionResult.CsvDataLoadExecExceptionDetails AS 'Exception Details'


FROM     CsvDataLoadExecutionResult LEFT OUTER JOIN
               DataFile AS DataFileOriginal ON CsvDataLoadExecutionResult.DataFileId = DataFileOriginal.DataFileId LEFT OUTER JOIN
               DataFile AS DataFileProcessed ON CsvDataLoadExecutionResult.DataFileProcessedId = DataFileProcessed.DataFileId LEFT OUTER JOIN
               DataFile AS DataFileException ON CsvDataLoadExecutionResult.DataFileExceptionId = DataFileException.DataFileId LEFT OUTER JOIN
               SysUser AS CreatedByUser ON DataFileOriginal.CreatedBy = CreatedByUser.UserId LEFT OUTER JOIN
               CsvTemplate AS CsvTemplate ON CsvDataLoadExecutionResult.CsvTemplateId = CsvTemplate.CsvTemplateId LEFT OUTER JOIN
			   Experiment AS Experiment ON CsvTemplate.ExperimentId = Experiment.ExpId;