package com.bringit.experiment.sql.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	
	public static Connection getConnection(String dbhost,String dbport, String dbname, String usr, String pwd){
	    Connection conn = null;
	    
	    try {
	    	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        String dbURL = "jdbc:sqlserver://"+dbhost+":"+dbport+";databaseName="+dbname;
	        String user = usr;
	        String pass = pwd;
	        conn = DriverManager.getConnection(dbURL, user, pass);
	
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    return conn;
	}
}
