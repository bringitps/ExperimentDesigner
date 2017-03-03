package com.bringit.experiment.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.DataFile;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.sql.connection.Connect;
import com.bringit.experiment.util.Config;

public class BatchExperimentRecordsInsertDao {

	public ResponseObj executeExperimentBatchRecordsInsert(String csvInsertColumns, List<String> csvInsertValues, SysUser sysUser, DataFile dataFile, Experiment experiment, int batchSize)
	{
		ResponseObj responseObj = new ResponseObj();
		responseObj.setCode(0);
		responseObj.setDescription("");
		
		//ResultSet rs = null;
		Config configuration = new Config();
		String dbHost = configuration.getProperty("dbhost");
		String dbPort = configuration.getProperty("dbport");
		String dbDatabase = configuration.getProperty("dbdatabase");
		String dbUsername = configuration.getProperty("dbusername");
		String dbPassword = configuration.getProperty("dbpassword");
		try {
			Connection conn = Connect.getConnection(dbHost, dbPort, dbDatabase, dbUsername, dbPassword);
		    Statement stmt = conn.createStatement();
		    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			int processedBatchNumber = 0;
			if(configuration.getProperty("dbms").equals("sqlserver"))
			{
				for(int i=0; i<csvInsertValues.size(); i++)
				{
					if( (i+1) % batchSize == 0 || (i+1) == csvInsertValues.size())
					{
						String sqlQuery = " INSERT INTO " + experiment.getExpDbTableNameId() + " (" + csvInsertColumns 
								+ ",CreatedBy,lastModifiedBy,CreatedDate,LastModifiedDate,DataFileId) "
								+ " VALUES (" + csvInsertValues.get(i) + ",'" + sysUser.getUserId() + "','" 
								+ sysUser.getUserId() + "','" +  df.format(new Date()) + "','" +  df.format(new Date()) + "','"
								+ dataFile.getDataFileId() +"');";
						//System.out.println(sqlQuery);
						stmt.addBatch(sqlQuery);
						stmt.executeBatch();
						stmt.close();
						conn.close();
						conn = Connect.getConnection(dbHost, dbPort, dbDatabase, dbUsername, dbPassword);
					    stmt = conn.createStatement();
					    processedBatchNumber++;
					    responseObj.setDescription(responseObj.getDescription() + "Batch #" + processedBatchNumber + " Loaded Successfully. \n");
					}
					else
					{
						String sqlQuery = " INSERT INTO " + experiment.getExpDbTableNameId() + " (" + csvInsertColumns 
								+ ",CreatedBy,LastModifiedBy,CreatedDate,LastModifiedDate,DataFileId) "
								+ " VALUES (" + csvInsertValues.get(i) + ",'" + sysUser.getUserId() + "','" 
								+ sysUser.getUserId() + "','" +  df.format(new Date()) + "','" +  df.format(new Date()) + "','"
								+ dataFile.getDataFileId() +"');";
						
						//System.out.println("New Query:" + sqlQuery);
						
						stmt.addBatch(sqlQuery);	
					}
				}				
			}
	    	else
	    		return null;

		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseObj.setCode(101);
			responseObj.setDescription("Error at saving information into DB. Details: " + e.getMessage());
		}
		
		return responseObj;
		
    }
}
