package com.bringit.experiment.ui.form;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.ViewHorizontalReport;
import com.bringit.experiment.bll.ViewHorizontalReportColumn;
import com.bringit.experiment.bll.ViewHorizontalReportColumn;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.ViewHorizontalReportColumnDao;
import com.bringit.experiment.dao.ViewHorizontalReportDao;
import com.bringit.experiment.dao.ViewHorizontalReportJobDataDao;
import com.bringit.experiment.dao.ViewHorizontalReportColumnDao;
import com.bringit.experiment.dao.ViewHorizontalReportDao;
import com.bringit.experiment.dao.ViewHorizontalReportJobDataDao;
import com.bringit.experiment.ui.design.ViewHorizontalReportDataDesign;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.Constants;
import com.opencsv.CSVWriter;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.data.util.sqlcontainer.query.generator.MSSQLGenerator;
import com.vaadin.data.util.sqlcontainer.query.generator.StatementHelper;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;

public class ViewHorizontalReportDataForm extends ViewHorizontalReportDataDesign {

	private SystemSettings systemSettings;
	
	ViewHorizontalReport vwHorizontalReport = new ViewHorizontalReport();
	private SQLContainer vaadinTblContainer;
	SysRole sysRoleSession = (SysRole) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");
	
	private String sqlQuery = "";    
	String firstWhereClause = "";
	List<String> andSqlWhereClause = new ArrayList<String>();

	public ViewHorizontalReportDataForm(Integer vwHorizontalReportId)
	{			
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		
		this.vwHorizontalReport = new ViewHorizontalReportDao().getVwHorizontalRptById(vwHorizontalReportId);
		this.lblVwHorizontalRptTitle.setValue(lblVwHorizontalRptTitle.getValue() + " - " + this.vwHorizontalReport.getVwHorizontalRptName()); 

		this.cbxDateFieldsFilter.setContainerDataSource(null);
		this.cbxVwHorizontalFieldFilter.setContainerDataSource(null);
		
		
		List<ViewHorizontalReportColumn> vwHorizontalRptColumns = new ViewHorizontalReportColumnDao().getAllVwHorizontalReportColumnsByRptId(vwHorizontalReportId);
		
		for(int i=0; vwHorizontalRptColumns != null && i<vwHorizontalRptColumns.size(); i++)
		{
			if(vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().startsWith("varchar") || vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().startsWith("char")
					|| vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().startsWith("text") || vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().startsWith("nvarchar") 
					|| vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().startsWith("nchar") || vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().startsWith("ntext"))
			{
				this.cbxVwHorizontalFieldFilter.addItem(vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDbId());
				this.cbxVwHorizontalFieldFilter.setItemCaption(vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDbId(), vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnName());
			}
			else if(vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().startsWith("date"))
			{
				this.cbxDateFieldsFilter.addItem(vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDbId());
				this.cbxDateFieldsFilter.setItemCaption(vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnDbId(), vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnName());
			}	
		}
		
		bindVWHorizontalReportRptTable();
		
		//Elements events
		this.btnApplyFilters.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
		
				vaadinTblContainer.removeAllContainerFilters();
				
				if(cbxDateFieldsFilter.getValue() != null && dtFilter1.getValue() != null && dtFilter2.getValue() != null)
				{
	    		    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date dateFilterValue1 = dtFilter1.getValue();
					Date dateFilterValue2 = dtFilter2.getValue();

					dateFilterValue2.setHours(23);
					dateFilterValue2.setMinutes(59);
					dateFilterValue2.setSeconds(59);
					vaadinTblContainer.addContainerFilter(new Between(cbxDateFieldsFilter.getValue(), dateFilterValue1, dateFilterValue2)); 	
					andSqlWhereClause.add(cbxDateFieldsFilter.getValue() + " BETWEEN '" + df.format(dateFilterValue1) + "' AND '" + df.format(dateFilterValue2) + "'");
				}
				
				if(cbxDateFieldsFilter.getValue() != null )
				{
					vaadinTblContainer.addContainerFilter(new Like(cbxDateFieldsFilter.getValue(), "%" + txtExpFieldFilter.getValue().trim() + "%"));
               		andSqlWhereClause.add(cbxDateFieldsFilter.getValue() + " LIKE '%" + txtExpFieldFilter.getValue().trim() + "%'");
				}
						        
		        if(andSqlWhereClause != null && andSqlWhereClause.size() > 0)
		        {
		        	Config configuration = new Config();
	
			         if (configuration.getProperty("dbms").equals("sqlserver")) {
			             String dbHost = configuration.getProperty("dbhost");
			             String dbPort = configuration.getProperty("dbport");
			             String dbDatabase = configuration.getProperty("dbdatabase");
			             String dbUsername = configuration.getProperty("dbusername");
			             String dbPassword = configuration.getProperty("dbpassword");
	
			             SimpleJDBCConnectionPool connectionPool;
	
			             try {
			                 connectionPool = new SimpleJDBCConnectionPool("com.microsoft.sqlserver.jdbc.SQLServerDriver",
			                         "jdbc:sqlserver://" + dbHost + ":" + dbPort + ";databaseName=" + dbDatabase,
			                         dbUsername, dbPassword);
			                
			                 TableQuery tblQuery = new TableQuery(vwHorizontalReport.getVwHorizontalRptDbTableNameId(), connectionPool, new MSSQLGenerator());
			                 List<OrderBy> tblOrderByRecordId = Arrays.asList(new OrderBy("RecordId", false));
			                 StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(vwHorizontalReport.getVwHorizontalRptDbTableNameId(), null, null/*tblOrderByRecordId*/, 0, 0, null);
			                 sqlQuery = sh.getQueryString();
			                 
			                 firstWhereClause = andSqlWhereClause.get(0);
			                 String sqlWhereClause = " WHERE " + firstWhereClause;
			                 
			                 for(int i=1; i < andSqlWhereClause.size(); i++)
			                 	 sqlWhereClause += " AND (" + andSqlWhereClause.get(i) + ")";
			                 
			                 String sqlColumnLabels = "";
			                 List<Object> asList = new ArrayList<Object>(Arrays.asList(tblVwHorizontalDataReport.getVisibleColumns()));
			                 for(int i=0; i<asList.size(); i++)
			                 	sqlColumnLabels += "\"" + asList.get(i).toString() + "\" AS \"" +  tblVwHorizontalDataReport.getColumnHeader(asList.get(i)) + "\",";
			                 
			                 sqlColumnLabels = sqlColumnLabels.substring(0, sqlColumnLabels.length() - 1);
			                 
			                 sqlQuery = sqlQuery.replace("SELECT *", "SELECT " + sqlColumnLabels);
			                 sqlQuery = sqlQuery.replace("ORDER BY", " " + sqlWhereClause + " ORDER BY");
			                 
			                 System.out.println("Built Query: " + sqlQuery);
			                 
			             } catch (SQLException e) {
			                 // TODO Auto-generated catch block
			                 e.printStackTrace();
			             }
			         }
		        }	
		  
		        Integer totalRecords = tblVwHorizontalDataReport.size();
		    	if (vwHorizontalReport.getVwHorizontalRptDbTableLastUpdate() != null)
		    		lblLastRefreshDate.setValue("Last Refresh Date: " + vwHorizontalReport.getVwHorizontalRptDbTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
		  }
			
		});
		
		this.btnRefreshButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				refreshData();
			}

		});
		
	}	
	
	private void refreshData() {
		 
		ViewHorizontalReportJobDataDao vwHorizontalReportJobDataDao = new ViewHorizontalReportJobDataDao();
		Map<String, Object> result = vwHorizontalReportJobDataDao.vwHorizontalProcedureJob(vwHorizontalReport.getVwHorizontalRptId());
		
		if(vaadinTblContainer != null)
			vaadinTblContainer.refresh();
		else
			bindVWHorizontalReportRptTable();

		vwHorizontalReport = new ViewHorizontalReportDao().getVwHorizontalRptById(vwHorizontalReport.getVwHorizontalRptId());
	
		
		Integer totalRecords = this.tblVwHorizontalDataReport.size();
		if (vwHorizontalReport.getVwHorizontalRptDbTableLastUpdate() != null)
            this.lblLastRefreshDate.setValue("Last Refresh Date: " + vwHorizontalReport.getVwHorizontalRptDbTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
    
		if (Constants.SUCCESS == result.get("status")) {
			this.getUI().showNotification("View Horizontal Report '" + vwHorizontalReport.getVwHorizontalRptName() + "' has been Refresh Successfully.", Notification.Type.HUMANIZED_MESSAGE);
			this.btnApplyFilters.click();		
		} else {
			String msgToDisplay = result.get("statusMessage").toString();
			if (Constants.JOB_NOT_EXECUTED.equalsIgnoreCase(msgToDisplay)) {
				msgToDisplay = "same View Horizontal Report is getting refresh by another user";
			}
			this.getUI().showNotification("View Horizontal Report '" + vwHorizontalReport.getVwHorizontalRptName() + "' can't refresh due to "+ msgToDisplay + ".", Notification.Type.WARNING_MESSAGE);
		} 
	}
	
	private void bindVWHorizontalReportRptTable()
	{
		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			String dbHost = configuration.getProperty("dbhost");
			String dbPort = configuration.getProperty("dbport");
			String dbDatabase = configuration.getProperty("dbdatabase");
			String dbUsername = configuration.getProperty("dbusername");
			String dbPassword = configuration.getProperty("dbpassword");
			
			SimpleJDBCConnectionPool connectionPool;
		    
			//Validate that DB Table of View Horizontal Report exists
			String validateDbTableSqlQuery = "SELECT 1 FROM sys.Objects WHERE  Object_id = OBJECT_ID('" + this.vwHorizontalReport.getVwHorizontalRptDbTableNameId() + "')";
			
			ResultSet validateDbTableSqlResults = new ExecuteQueryDao().getSqlSelectQueryResults(validateDbTableSqlQuery);
     		if(validateDbTableSqlResults == null)
     			return;
     		else
     		{
     			try {
					validateDbTableSqlResults.last();
	     			if(validateDbTableSqlResults.getRow() == 0)
	     				return;
	     			//System.out.println("Total Rows " +  numRows);	     			
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				} 
     			
     		
			
		    try {
		    	connectionPool = new SimpleJDBCConnectionPool("com.microsoft.sqlserver.jdbc.SQLServerDriver",
						"jdbc:sqlserver://"+dbHost+":"+dbPort+";databaseName="+dbDatabase,
						dbUsername, dbPassword);
		    
		    	TableQuery tblQuery = new TableQuery(this.vwHorizontalReport.getVwHorizontalRptDbTableNameId(), connectionPool, new MSSQLGenerator());
				tblQuery.setVersionColumn("vwRecordId");

				vaadinTblContainer = new SQLContainer(tblQuery);
				
				tblVwHorizontalDataReport.setContainerDataSource(vaadinTblContainer);
				
				List<ViewHorizontalReportColumn> vwHorizontalReportColumns = new ViewHorizontalReportColumnDao().getAllVwHorizontalReportColumnsByRptId(this.vwHorizontalReport.getVwHorizontalRptId());
				if(vwHorizontalReportColumns != null)
				{
					String[] vwHorizontalReportVisibleColumns = new String[vwHorizontalReportColumns.size()]; 
					for(int i=0; i<vwHorizontalReportColumns.size(); i++)
					{
						this.tblVwHorizontalDataReport.setColumnHeader(vwHorizontalReportColumns.get(i).getVwHorizontalRptColumnDbId(), vwHorizontalReportColumns.get(i).getVwHorizontalRptColumnName());
						vwHorizontalReportVisibleColumns[i] = vwHorizontalReportColumns.get(i).getVwHorizontalRptColumnDbId();
					
						if(vwHorizontalReportColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().contains("float") || 
								vwHorizontalReportColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().contains("decimal") || 
								vwHorizontalReportColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().contains("int"))
						{
							tblVwHorizontalDataReport.setConverter(vwHorizontalReportColumns.get(i).getVwHorizontalRptColumnDbId(), new StringToDoubleConverter() {
							    @Override
							    protected NumberFormat getFormat(Locale locale) {
							    	NumberFormat format = NumberFormat.getNumberInstance();
							    	format.setGroupingUsed(false);
							    	return format;
							    }
							});
							
						}
						
						if(vwHorizontalReportColumns.get(i).getVwHorizontalRptColumnDataType().toLowerCase().contains("date"))
						{
							tblVwHorizontalDataReport.setConverter(vwHorizontalReportColumns.get(i).getVwHorizontalRptColumnDbId(), new StringToDateConverter() {
							    @Override
							    protected DateFormat getFormat(Locale locale) {
									return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    }
							});
						}
					}
					
					tblVwHorizontalDataReport.setVisibleColumns(vwHorizontalReportVisibleColumns);					
				}
				
				tblVwHorizontalDataReport.setColumnCollapsingAllowed(true);				
				
				StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(vwHorizontalReport.getVwHorizontalRptDbTableNameId(), null, null, 0, 0, null);
                sqlQuery = sh.getQueryString();
                
				String sqlColumnLabels = "";
                List<Object> asList = new ArrayList<Object>(Arrays.asList(tblVwHorizontalDataReport.getVisibleColumns()));
                for(int i=0; i<asList.size(); i++)
                	sqlColumnLabels += "\"" + asList.get(i).toString() + "\" AS \"" +  tblVwHorizontalDataReport.getColumnHeader(asList.get(i)) + "\",";
                
                sqlColumnLabels = sqlColumnLabels.substring(0, sqlColumnLabels.length() - 1);
                
                sqlQuery = sqlQuery.replace("SELECT *", "SELECT " + sqlColumnLabels);
                
                if(!firstWhereClause.isEmpty())
                	sqlQuery += " WHERE " + firstWhereClause;
                
                sqlResultAttachCsvFileDownloaderToButton(this.btnExportExcel);
				
		    } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
     		}
		    Integer totalRecords = tblVwHorizontalDataReport.size();
	    	if (vwHorizontalReport.getVwHorizontalRptDbTableLastUpdate() != null)
	            this.lblLastRefreshDate.setValue("Last Refresh Date: " + vwHorizontalReport.getVwHorizontalRptDbTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
	  
		}
	}
	
	public void sqlResultAttachCsvFileDownloaderToButton(Button downloadBtn)
	{	
		 FileDownloader downloader = new FileDownloader(new StreamResource(
                 new StreamResource.StreamSource() {
                     @Override
                     public InputStream getStream() {
                    	InputStream is = null;
                     	ResultSet experimentDataRecordResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlQuery);
                 		if(experimentDataRecordResults != null)
                 		{                 			
                 			try {
                 				ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
             					CSVWriter writer = new CSVWriter(new OutputStreamWriter(csvOutputStream), ',',
                     					CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, "\n");
                 				Boolean includeHeaders = true;

             					writer.writeAll(experimentDataRecordResults, includeHeaders);
             					writer.close();
             					
             					is = new ByteArrayInputStream(csvOutputStream.toByteArray());
             					
             				} catch (SQLException | IOException e) {
             					// TODO Auto-generated catch block
             					e.printStackTrace();
             				}

                 			
                       }
                          return is;
                     	}
                 }, lblVwHorizontalRptTitle.getValue().trim() + ".csv"));
         downloader.extend(downloadBtn);
		
	}
	
	
	
	
}
