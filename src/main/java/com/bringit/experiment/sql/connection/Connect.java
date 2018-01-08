package com.bringit.experiment.sql.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.bringit.experiment.util.Config;

public class Connect {
	
	public static Connection getConnection(String dbhost,String dbport, String dbname, String usr, String pwd){
	    Connection conn = null;
		Config configuration = new Config();
	    
	    try {
	    	
	    		if(configuration.getProperty("dbms").equals("sqlserver"))
	    		{
			
	    			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    			String dbURL = "jdbc:sqlserver://"+dbhost+":"+dbport+";databaseName="+dbname;
	    			String user = usr;
	    			String pass = pwd;
	    			conn = DriverManager.getConnection(dbURL, user, pass);
	    		}
	
	    } catch (SQLException ex) {
	        ex.printStackTrace();

			System.out.println("SQL exception: State" + ex.getSQLState() + " Error code: " + ex.getErrorCode() + " Message: " + ex.getMessage());
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Class not found exception.");
		} 
	    return conn;
	}
}