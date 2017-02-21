package com.bringit.experiment.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.vaadin.ui.Table;

public class VaadinControls {

	public static void bindDbViewRsToVaadinTable(Table vaadinTable, ResultSet dbVwResultSet, int columnIdIndex)
	{
		vaadinTable.setContainerDataSource(null);
		vaadinTable.removeAllItems();
		
		ResultSetMetaData dbVwMetaData;
		try {
				dbVwMetaData = dbVwResultSet.getMetaData();
			
				for(int i=0; i<dbVwMetaData.getColumnCount(); i++)
					vaadinTable.addContainerProperty(dbVwMetaData.getColumnLabel(i+1), String.class, null);
				 
			    while (dbVwResultSet.next()) {
			    	Object[] itemValues = new Object[dbVwMetaData.getColumnCount()];
			    	for(int i=0; i<dbVwMetaData.getColumnCount(); i++)
			    		itemValues[i] = dbVwResultSet.getString(i+1);
			    	
			    vaadinTable.addItem(itemValues, dbVwResultSet.getString(columnIdIndex));
			    } 
		    
			}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
	    }   
	}
}

