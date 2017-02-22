package com.bringit.experiment.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bringit.experiment.sql.connection.Connect;
import com.bringit.experiment.util.Config;

public class DataBaseViewDao {
	
	public ResultSet getViewResults(String viewName)
	{
		ResultSet rs = null;
		Config configuration = new Config();
		String dbHost = configuration.getProperty("dbhost");
		String dbPort = configuration.getProperty("dbport");
		String dbDatabase = configuration.getProperty("dbdatabase");
		String dbUsername = configuration.getProperty("dbusername");
		String dbPassword = configuration.getProperty("dbpassword");
		try {
			Connection conn = Connect.getConnection(dbHost, dbPort, dbDatabase, dbUsername, dbPassword);
		    Statement stmt = conn.createStatement();
	
		    String query ="";

			if(configuration.getProperty("dbms").equals("sqlserver"))
			{
				query = " SELECT * FROM " + viewName;
			}
	    	else
	    		return null;
			
		    rs = stmt.executeQuery(query);

		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
    }
	
	public ResultSet getFilteredViewResults(String viewName, String columnName, String filterValue)
	{
		ResultSet rs = null;
		Config configuration = new Config();
		String dbHost = configuration.getProperty("dbhost");
		String dbPort = configuration.getProperty("dbport");
		String dbDatabase = configuration.getProperty("dbdatabase");
		String dbUsername = configuration.getProperty("dbusername");
		String dbPassword = configuration.getProperty("dbpassword");
		try {
			Connection conn = Connect.getConnection(dbHost, dbPort, dbDatabase, dbUsername, dbPassword);
		    Statement stmt = conn.createStatement();
	
		    String query ="";

			if(configuration.getProperty("dbms").equals("sqlserver"))
				query = " SELECT * FROM " + viewName + " WHERE \"" + columnName + "\" LIKE '%" + filterValue + "%'";
			else
	    		return null;
			
		    rs = stmt.executeQuery(query);

		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
    }
}
