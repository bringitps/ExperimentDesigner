CREATE VIEW vwExpFieldValueUpdateLog AS SELECT 
	   CASE ExpFieldValueUpdateLog.DbTableRecordCommentsUpdate WHEN 1 THEN 'Comments' ELSE ExpField.ExpFieldName END AS 'Updated Field',
	   ExpFieldValueUpdateLog.ExpOldCreatedDate AS 'Previous Modification Date', 
	   ExpFieldValueUpdateLog.ExpOldValue AS 'Previous Value',
	   ExpFieldValueUpdateLog.ExpNewCreatedDate AS 'New Modification Date', 
	   ExpFieldValueUpdateLog.ExpNewValue AS 'New Value',
	   SysUser.UserName AS 'Modified By' 
  FROM ExperimentMeasureFieldValueUpdateLog AS ExpFieldValueUpdateLog
  LEFT JOIN ExperimentField ExpField ON ExpFieldValueUpdateLog.ExpFieldId = ExpField.ExpFieldId
  LEFT JOIN Experiment Experiment ON ExpField.ExpId = Experiment.ExpId
  LEFT JOIN SysUser SysUser ON  ExpFieldValueUpdateLog.CreatedBy = SysUser.UserId;