package com.bringit.experiment.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;

public class VaadinControls {

	public static void bindDbViewRsToVaadinTable(Table vaadinTable, ResultSet dbVwResultSet, int columnIdIndex)
	{
		SystemSettings systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		
		vaadinTable.setContainerDataSource(null);
		vaadinTable.removeAllItems();
		
		ResultSetMetaData dbVwMetaData;
		try {
				dbVwMetaData = dbVwResultSet.getMetaData();
			
				for(int i=0; i<dbVwMetaData.getColumnCount(); i++)
				{
					String columnLabel = dbVwMetaData.getColumnLabel(i+1);

					if("Experiment".equals(columnLabel))
						columnLabel = systemSettings.getExperimentLabel();
					
					vaadinTable.addContainerProperty(columnLabel, String.class, null);
				}
				 
			    while (dbVwResultSet.next()) {
			    	String[] itemValues = new String[dbVwMetaData.getColumnCount()];
			    	for(int i=0; i<dbVwMetaData.getColumnCount(); i++)
			    		itemValues[i] = dbVwResultSet.getString(dbVwMetaData.getColumnLabel(i+1));
			    	
			    	vaadinTable.addItem(itemValues, dbVwResultSet.getString(columnIdIndex));
			    } 
		    
			}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
	    }   
	}
	
	public static void bindDbViewStringFiltersToVaadinComboBox(ComboBox vaadinComboBox, ResultSet dbVwResultSet)
	{	
		vaadinComboBox.setContainerDataSource(null);
		vaadinComboBox.removeAllItems();
		Config configuration = new Config();
		
		ResultSetMetaData dbVwMetaData;
		try {
				dbVwMetaData = dbVwResultSet.getMetaData();
			
				for(int i=0; i<dbVwMetaData.getColumnCount(); i++)
				{
					if(configuration.getProperty("dbms").equals("sqlserver"))
					{
						if(dbVwMetaData.getColumnTypeName(i+1).startsWith("varchar") || dbVwMetaData.getColumnTypeName(i+1).startsWith("char")
								|| dbVwMetaData.getColumnTypeName(i+1).startsWith("text") ||dbVwMetaData.getColumnTypeName(i+1).startsWith("nvarchar") 
								|| dbVwMetaData.getColumnTypeName(i+1).startsWith("nchar") || dbVwMetaData.getColumnTypeName(i+1).startsWith("ntext"))
						{
							vaadinComboBox.addItem(dbVwMetaData.getColumnLabel(i+1));
							vaadinComboBox.setItemCaption(dbVwMetaData.getColumnLabel(i+1), dbVwMetaData.getColumnLabel(i+1));

							if(vaadinComboBox.size() == 1)
								vaadinComboBox.select(dbVwMetaData.getColumnLabel(i+1));
						}
					
					}
				}
				
			
			}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
	    }   
		
	}
	
	public static void bindDbViewDateFiltersToVaadinComboBox(ComboBox vaadinComboBox, ResultSet dbVwResultSet)
	{	
		vaadinComboBox.setContainerDataSource(null);
		vaadinComboBox.removeAllItems();
		Config configuration = new Config();
		
		ResultSetMetaData dbVwMetaData;
		try {
				dbVwMetaData = dbVwResultSet.getMetaData();
			
				for(int i=0; i<dbVwMetaData.getColumnCount(); i++)
				{
					if(configuration.getProperty("dbms").equals("sqlserver"))
					{
						if(dbVwMetaData.getColumnTypeName(i+1).contains("date"))
						{
							vaadinComboBox.addItem(dbVwMetaData.getColumnLabel(i+1));
							vaadinComboBox.setItemCaption(dbVwMetaData.getColumnLabel(i+1), dbVwMetaData.getColumnLabel(i+1));

							if(vaadinComboBox.size() == 1)
								vaadinComboBox.select(dbVwMetaData.getColumnLabel(i+1));
							
							if(dbVwMetaData.getColumnLabel(i+1).toLowerCase().trim().equals("lastmodifieddate"))
								vaadinComboBox.select(dbVwMetaData.getColumnLabel(i+1));
						}
					
					}
				}
				
			
			}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
	    }   
		
	}
	
}