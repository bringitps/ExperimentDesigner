package com.bringit.experiment.dao;

import java.sql.ResultSet;

import com.bringit.experiment.util.Config;

public class DataBaseViewDao {
	
	public ResultSet getViewResults(String viewName)
	{
		Config configuration = new Config();
		String dbHost = configuration.getProperty("dbhost");
		String dbPort = configuration.getProperty("dbport");
		String dbDatabase = configuration.getProperty("dbdatabase");
		String dbUsername = configuration.getProperty("dbusername");
		String dbPassword = configuration.getProperty("dbpassword");
		
		//Haz tu magia Kor
		String query = null;
    	
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			query = " SELECT * FROM " + viewName;
		}
    	else
    		return null;
		
		return null;
    }
}
