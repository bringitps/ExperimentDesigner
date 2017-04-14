package com.bringit.experiment.dao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.sql.connection.Connect;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class ExecuteQueryDao {

	/*
	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void executeQuery(String query) {

        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }
	*/
	public void executeQueryFile(String filePath) {

		Config configuration = new Config();
		String dbHost = configuration.getProperty("dbhost");
		String dbPort = configuration.getProperty("dbport");
		String dbDatabase = configuration.getProperty("dbdatabase");
		String dbUsername = configuration.getProperty("dbusername");
		String dbPassword = configuration.getProperty("dbpassword");
		try {
			
			Connection conn = Connect.getConnection(dbHost, dbPort, dbDatabase, dbUsername, dbPassword);
		    Statement stmt = conn.createStatement();
			
		    byte[] encoded;
		    String query = "";
			try {
				encoded = Files.readAllBytes(Paths.get(filePath));
				query = new String(encoded, StandardCharsets.UTF_8);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
		   stmt.execute(query);
		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public ResultSet getSqlSelectQueryResults(String sqlSelectQuery)
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
		    rs = stmt.executeQuery(sqlSelectQuery);
		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
    }
	
	public ResultSet executeStoredProcedure(String spName, List<String> spParameters)
	{
		CallableStatement callableStatement = null;
		ResultSet rs = null;

		Config configuration = new Config();
		String dbHost = configuration.getProperty("dbhost");
		String dbPort = configuration.getProperty("dbport");
		String dbDatabase = configuration.getProperty("dbdatabase");
		String dbUsername = configuration.getProperty("dbusername");
		String dbPassword = configuration.getProperty("dbpassword");
		try {
			Connection conn = Connect.getConnection(dbHost, dbPort, dbDatabase, dbUsername, dbPassword);
			
			String spSqlCall = "";
			
			if(configuration.getProperty("dbms").equals("sqlserver"))
			{
				spSqlCall = "EXEC " + spName + " ";
				PreparedStatement ps = conn.prepareStatement(spSqlCall);
				ps.setEscapeProcessing(true);
				
				for(int i=0; spParameters!=null && i<spParameters.size(); i++)		
				{
					if((i+1)==spParameters.size())
						spSqlCall += "?";			
					else
						spSqlCall += "?,";				
				}
			}
			PreparedStatement preparedStmt = conn.prepareStatement(spSqlCall);
			preparedStmt.setEscapeProcessing(true);
		
			for(int i=0; spParameters!=null && i<spParameters.size(); i++)		
				preparedStmt.setString((i+1), spParameters.get(i));
			preparedStmt.setFetchSize(10000);
			rs = preparedStmt.executeQuery();
			return rs;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	

	public ResponseObj executeUpdateQuery(String sqlUpdateQuery)
	{
		ResponseObj responseObj = new ResponseObj();
		responseObj.setCode(0);
		
		Config configuration = new Config();
		String dbHost = configuration.getProperty("dbhost");
		String dbPort = configuration.getProperty("dbport");
		String dbDatabase = configuration.getProperty("dbdatabase");
		String dbUsername = configuration.getProperty("dbusername");
		String dbPassword = configuration.getProperty("dbpassword");
		try {
			Connection conn = Connect.getConnection(dbHost, dbPort, dbDatabase, dbUsername, dbPassword);
		    Statement stmt = conn.createStatement();
		    stmt.executeUpdate(sqlUpdateQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseObj.setCode(101);
			responseObj.setDescription(e.getMessage());
		}
		
		return responseObj;
    }
	
	
	
	
}
