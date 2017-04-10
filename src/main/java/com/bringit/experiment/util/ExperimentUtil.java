package com.bringit.experiment.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.ui.form.ExperimentManagementForm;
import com.bringit.experiment.ui.form.XmlTemplateManagementForm;

public class ExperimentUtil {

	public static String buildSqlSelectQueryByExperiment(Experiment experiment, List<ExperimentField> experimentFields)
	{
		String sqlSelectQuery = "";
		Config configuration = new Config();
		
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			sqlSelectQuery = "SELECT " + experiment.getExpDbTableNameId() + ".RecordId AS Id,";
			for(int i=0; experimentFields != null && i<experimentFields.size(); i++)
				sqlSelectQuery +=  experiment.getExpDbTableNameId() + "." + experimentFields.get(i).getExpDbFieldNameId() + " AS '" + experimentFields.get(i).getExpFieldName() + "',";
			
			sqlSelectQuery += "Cm.CmName AS 'Contract Manufacturer'," + experiment.getExpDbTableNameId() + ".Comments," + experiment.getExpDbTableNameId() + ".CreatedDate, ";
			sqlSelectQuery += experiment.getExpDbTableNameId() + ".LastModifiedDate, CreatedByUser.UserName AS CreatedBy, LastModifiedByUser.UserName AS LastModifiedBy, ";
			sqlSelectQuery += " DataFile.DataFileName AS FileName, CONCAT(FileRepo.FileRepoHost, FileRepo.FileRepoPath) AS Location ";
			sqlSelectQuery += " FROM " + experiment.getExpDbTableNameId();
			sqlSelectQuery += " LEFT OUTER JOIN ContractManufacturer AS Cm ON " + experiment.getExpDbTableNameId() + ".CmId = Cm.CmId ";
			sqlSelectQuery += " LEFT OUTER JOIN SysUser AS CreatedByUser ON " + experiment.getExpDbTableNameId() + ".CreatedBy = CreatedByUser.UserId ";
			sqlSelectQuery += " LEFT OUTER JOIN SysUser AS LastModifiedByUser ON " + experiment.getExpDbTableNameId() + ".LastModifiedBy = CreatedByUser.UserId ";
			sqlSelectQuery += " LEFT OUTER JOIN DataFile AS DataFile ON " + experiment.getExpDbTableNameId() + ".DataFileId = DataFile.DataFileId ";
			sqlSelectQuery += " LEFT OUTER JOIN FilesRepository AS FileRepo ON DataFile.FileRepoId = FileRepo.FileRepoId ";
			sqlSelectQuery += " ORDER BY " + experiment.getExpDbTableNameId() + ".LastModifiedDate DESC;"; 
			    	
		}
		else
			return null;
		
		return sqlSelectQuery;
	}
	
	public static String buildFilteredSqlSelectQueryByExperiment(Experiment experiment, List<ExperimentField> experimentFields, String columnName, String filterValue)
	{

		String sqlSelectQuery = "";
		Config configuration = new Config();
		
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			sqlSelectQuery = "SELECT * FROM ( SELECT " + experiment.getExpDbTableNameId() + ".RecordId AS Id,";
			for(int i=0; experimentFields != null && i<experimentFields.size(); i++)
				sqlSelectQuery +=  experiment.getExpDbTableNameId() + "." + experimentFields.get(i).getExpDbFieldNameId() + " AS '" + experimentFields.get(i).getExpFieldName() + "',";
			
			sqlSelectQuery += "Cm.CmName AS 'Contract Manufacturer'," + experiment.getExpDbTableNameId() + ".Comments AS Comments," + experiment.getExpDbTableNameId() + ".CreatedDate AS CreatedDate, ";
			sqlSelectQuery += experiment.getExpDbTableNameId() + ".LastModifiedDate AS LastModifiedDate, CreatedByUser.UserName AS CreatedBy, LastModifiedByUser.UserName AS LastModifiedBy, ";
			sqlSelectQuery += " DataFile.DataFileName AS FileName, CONCAT(FileRepo.FileRepoHost, FileRepo.FileRepoPath) AS Location ";
			sqlSelectQuery += " FROM " + experiment.getExpDbTableNameId();
			sqlSelectQuery += " LEFT OUTER JOIN ContractManufacturer AS Cm ON " + experiment.getExpDbTableNameId() + ".CmId = Cm.CmId ";
			sqlSelectQuery += " LEFT OUTER JOIN SysUser AS CreatedByUser ON " + experiment.getExpDbTableNameId() + ".CreatedBy = CreatedByUser.UserId ";
			sqlSelectQuery += " LEFT OUTER JOIN SysUser AS LastModifiedByUser ON " + experiment.getExpDbTableNameId() + ".LastModifiedBy = CreatedByUser.UserId ";
			sqlSelectQuery += " LEFT OUTER JOIN DataFile AS DataFile ON " + experiment.getExpDbTableNameId() + ".DataFileId = DataFile.DataFileId ";
			sqlSelectQuery += " LEFT OUTER JOIN FilesRepository AS FileRepo ON DataFile.FileRepoId = FileRepo.FileRepoId ) AS ExperimentDataTable";
			sqlSelectQuery += " WHERE \"" + columnName + "\"  LIKE '%" + filterValue + "%'";
			sqlSelectQuery += " ORDER BY LastModifiedDate DESC;"; 
			    	
		}
		else
			return null;
		
		return sqlSelectQuery;
	}
	
	
	public static String buildDateFilteredSqlSelectQueryByExperiment(Experiment experiment, List<ExperimentField> experimentFields, String stringFilterColumnName, String stringFilterValue, String dateFilterColumnName, String dateFilterOperator, Date dateFilterValue1, Date dateFilterValue2)
	{

	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sqlSelectQuery = "";
		Config configuration = new Config();
		
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			sqlSelectQuery = "SELECT * FROM ( SELECT " + experiment.getExpDbTableNameId() + ".RecordId AS Id,";
			for(int i=0; experimentFields != null && i<experimentFields.size(); i++)
				sqlSelectQuery +=  experiment.getExpDbTableNameId() + "." + experimentFields.get(i).getExpDbFieldNameId() + " AS '" + experimentFields.get(i).getExpFieldName() + "',";
			
			sqlSelectQuery += "Cm.CmName AS 'Contract Manufacturer'," + experiment.getExpDbTableNameId() + ".Comments AS Comments," + experiment.getExpDbTableNameId() + ".CreatedDate AS CreatedDate, ";
			sqlSelectQuery += experiment.getExpDbTableNameId() + ".LastModifiedDate AS LastModifiedDate, CreatedByUser.UserName AS CreatedBy, LastModifiedByUser.UserName AS LastModifiedBy, ";
			sqlSelectQuery += " DataFile.DataFileName AS FileName, CONCAT(FileRepo.FileRepoHost, FileRepo.FileRepoPath) AS Location ";
			sqlSelectQuery += " FROM " + experiment.getExpDbTableNameId();
			sqlSelectQuery += " LEFT OUTER JOIN ContractManufacturer AS Cm ON " + experiment.getExpDbTableNameId() + ".CmId = Cm.CmId ";
			sqlSelectQuery += " LEFT OUTER JOIN SysUser AS CreatedByUser ON " + experiment.getExpDbTableNameId() + ".CreatedBy = CreatedByUser.UserId ";
			sqlSelectQuery += " LEFT OUTER JOIN SysUser AS LastModifiedByUser ON " + experiment.getExpDbTableNameId() + ".LastModifiedBy = CreatedByUser.UserId ";
			sqlSelectQuery += " LEFT OUTER JOIN DataFile AS DataFile ON " + experiment.getExpDbTableNameId() + ".DataFileId = DataFile.DataFileId ";
			sqlSelectQuery += " LEFT OUTER JOIN FilesRepository AS FileRepo ON DataFile.FileRepoId = FileRepo.FileRepoId ) AS ExperimentDataTable";
			
			if(dateFilterOperator.equals("between") || dateFilterOperator.equals("on"))
			{
				if(dateFilterOperator.equals("on"))
					dateFilterValue2 = (Date) dateFilterValue1.clone();

				dateFilterValue2.setHours(23);
				dateFilterValue2.setMinutes(59);
				dateFilterValue2.setSeconds(59);
				
				sqlSelectQuery += " WHERE \"" + dateFilterColumnName + "\"  BETWEEN '" + df.format(dateFilterValue1) + "' AND '" + df.format(dateFilterValue2) + "' ";
			}
			else
			{
				String sqlDateFilterOperator = "";
				
				 switch (dateFilterOperator) 
				 {
		         	case "before":  
		         		sqlDateFilterOperator = "<";
		        	 	break;
		         	case "after":  
		         		sqlDateFilterOperator = ">";
		         		dateFilterValue1.setHours(23);
		         		dateFilterValue1.setMinutes(59);
		         		dateFilterValue1.setSeconds(59);
	        	 	 	break;
		         	case "onorbefore":  
		         		sqlDateFilterOperator = "<=";
		         		dateFilterValue1.setHours(23);
		         		dateFilterValue1.setMinutes(59);
		         		dateFilterValue1.setSeconds(59);
		        	 	break;
		         	case "onorafter":  
		         		sqlDateFilterOperator = ">=";
	        	 		break;
				 }
				
				sqlSelectQuery += " WHERE \"" + dateFilterColumnName + "\" " + sqlDateFilterOperator + " '" + df.format(dateFilterValue1) + "' ";
			}
			if(stringFilterColumnName != null && stringFilterValue != null)
				sqlSelectQuery += " AND \"" + stringFilterColumnName + "\"  LIKE '%" + stringFilterValue + "%'";
			
			sqlSelectQuery += " ORDER BY \"" + dateFilterColumnName + "\" DESC;";
			    	
		}
		else
			return null;
		
		return sqlSelectQuery;
	}

	
	public static String buildEqualsFilteredSqlSelectQueryByExperiment(Experiment experiment, List<ExperimentField> experimentFields, String columnName, String filterValue)
	{

		String sqlSelectQuery = "";
		Config configuration = new Config();
		
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			sqlSelectQuery = "SELECT * FROM ( SELECT " + experiment.getExpDbTableNameId() + ".RecordId AS Id,";
			for(int i=0; experimentFields != null && i<experimentFields.size(); i++)
				sqlSelectQuery +=  experiment.getExpDbTableNameId() + "." + experimentFields.get(i).getExpDbFieldNameId() + " AS '" + experimentFields.get(i).getExpFieldName() + "',";
			
			sqlSelectQuery += "Cm.CmName AS 'Contract Manufacturer'," +experiment.getExpDbTableNameId() + ".Comments AS Comments," + experiment.getExpDbTableNameId() + ".CreatedDate AS CreatedDate, ";
			sqlSelectQuery += experiment.getExpDbTableNameId() + ".LastModifiedDate AS LastModifiedDate, CreatedByUser.UserName AS CreatedBy, LastModifiedByUser.UserName AS LastModifiedBy, ";
			sqlSelectQuery += " DataFile.DataFileName AS FileName, CONCAT(FileRepo.FileRepoHost, FileRepo.FileRepoPath) AS Location ";
			sqlSelectQuery += " FROM " + experiment.getExpDbTableNameId();
			sqlSelectQuery += " LEFT OUTER JOIN ContractManufacturer AS Cm ON " + experiment.getExpDbTableNameId() + ".CmId = Cm.CmId ";
			sqlSelectQuery += " LEFT OUTER JOIN SysUser AS CreatedByUser ON " + experiment.getExpDbTableNameId() + ".CreatedBy = CreatedByUser.UserId ";
			sqlSelectQuery += " LEFT OUTER JOIN SysUser AS LastModifiedByUser ON " + experiment.getExpDbTableNameId() + ".LastModifiedBy = CreatedByUser.UserId ";
			sqlSelectQuery += " LEFT OUTER JOIN DataFile AS DataFile ON " + experiment.getExpDbTableNameId() + ".DataFileId = DataFile.DataFileId ";
			sqlSelectQuery += " LEFT OUTER JOIN FilesRepository AS FileRepo ON DataFile.FileRepoId = FileRepo.FileRepoId ) AS ExperimentDataTable";
			sqlSelectQuery += " WHERE \"" + columnName + "\"  = '" + filterValue + "'";
			sqlSelectQuery += " ORDER BY LastModifiedDate DESC;"; 
			    	
		}
		else
			return null;
		
		return sqlSelectQuery;
	}

	public static String buildSqlUpdateQueryByExperiment(Experiment experiment, List<String> experimentFieldIdsXRef, List<String> experimentFieldValuesXRef, String recordId)
	{

		String sqlSelectQuery = "";
		Config configuration = new Config();
		
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			sqlSelectQuery = "UPDATE " + experiment.getExpDbTableNameId() + " SET ";
			for(int i=0; experimentFieldIdsXRef != null && i<experimentFieldIdsXRef.size(); i++)
			{
				sqlSelectQuery +=  experimentFieldIdsXRef.get(i) + " = ";
				
				if(experimentFieldValuesXRef.get(i) != null)
					sqlSelectQuery += "'" + experimentFieldValuesXRef.get(i) + "'";
				else
					sqlSelectQuery += "null";
					
				if((i+1) < experimentFieldIdsXRef.size())
					sqlSelectQuery += ", ";
			}
			
			sqlSelectQuery += " WHERE \"RecordId\"  = '" + recordId + "';";
			    	
		}
		else
			return null;
		
		return sqlSelectQuery;
	}

	public static String buildSqlSelectQueryExperimentChangesLog(String ExperimentId, String dbRecordId)
	{
		String sqlSelectQuery = "SELECT CASE ExpFieldValueUpdateLog.DbTableRecordCommentsUpdate WHEN 1 THEN 'Comments' ELSE ExpField.ExpFieldName END AS 'Updated Field', ";
		sqlSelectQuery += " ExpFieldValueUpdateLog.ExpOldCreatedDate AS 'Previous Modification Date', ";
		sqlSelectQuery += " ExpFieldValueUpdateLog.ExpOldValue AS 'Previous Value', ";
		sqlSelectQuery += " ExpFieldValueUpdateLog.ExpNewCreatedDate AS 'New Modification Date', "; 
		sqlSelectQuery += " ExpFieldValueUpdateLog.ExpNewValue AS 'New Value', ";
		sqlSelectQuery += " SysUser.UserName AS 'Modified By' ";
		sqlSelectQuery += " FROM ExperimentMeasureFieldValueUpdateLog AS ExpFieldValueUpdateLog ";
		sqlSelectQuery += " LEFT JOIN ExperimentField ExpField ON ExpFieldValueUpdateLog.ExpFieldId = ExpField.ExpFieldId ";
		sqlSelectQuery += " LEFT JOIN Experiment Experiment ON ExpField.ExpId = Experiment.ExpId ";
		sqlSelectQuery += " LEFT JOIN SysUser SysUser ON  ExpFieldValueUpdateLog.CreatedBy = SysUser.UserId ";
		sqlSelectQuery += " WHERE ExpFieldValueUpdateLog.ExpId = " + ExperimentId + " AND ExpFieldValueUpdateLog.DbExperimentDataTableRecordId = " + dbRecordId;
		sqlSelectQuery += " ORDER BY ExpFieldValueUpdateLog.ExpNewCreatedDate DESC ";
		return sqlSelectQuery;
	}
}
