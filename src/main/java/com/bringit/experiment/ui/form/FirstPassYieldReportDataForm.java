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

import com.bringit.experiment.bll.FirstPassYieldInfoField;
import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.FirstPassYieldInfoFieldDao;
import com.bringit.experiment.dao.FirstPassYieldReportDao;
import com.bringit.experiment.dao.FirstPassYieldReportJobDataDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.ui.design.FirstPassYieldReportDataDesign;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.Constants;
import com.opencsv.CSVWriter;
import com.vaadin.data.Item;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;

public class FirstPassYieldReportDataForm extends FirstPassYieldReportDataDesign{

	private SystemSettings systemSettings;
	
	FirstPassYieldReport fpyReport = new FirstPassYieldReport();
	private SQLContainer vaadinTblContainer;
	SysRole sysRoleSession = (SysRole) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");
	
	private String sqlQuery = "";    
	String firstWhereClause = "";
	List<String> andSqlWhereClause = new ArrayList<String>();
	
	public FirstPassYieldReportDataForm(Integer fpyReportId)
	{	
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		
		this.fpyReport = new FirstPassYieldReportDao().getFirstPassYieldReportById(fpyReportId);
		this.lblFpyRptTitle.setValue(lblFpyRptTitle.getValue() + " - " + this.fpyReport.getFpyReportName()); 

		this.cbxDateFieldsFilter.setContainerDataSource(null);
		this.cbxFpyFieldFilter.setContainerDataSource(null);

		//Set filters
		this.cbxFpyFieldFilter.addItem("fpy_experiment");
		this.cbxFpyFieldFilter.setItemCaption("fpy_experiment", this.systemSettings.getExperimentLabel());
		
		this.cbxFpyFieldFilter.addItem("fpy_serial_number");
		this.cbxFpyFieldFilter.setItemCaption("fpy_serial_number", "Serial Number");
	
		this.cbxFpyFieldFilter.addItem("fpy_result");
		this.cbxFpyFieldFilter.setItemCaption("fpy_result", "Result");
	
		this.cbxDateFieldsFilter.addItem("fpy_date_time");
		this.cbxDateFieldsFilter.setItemCaption("fpy_date_time", "Datetime");
		
		List<FirstPassYieldInfoField> fpyInfoFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(fpyReportId);		
	
		for(int i=0; fpyInfoFields != null && i<fpyInfoFields.size(); i++)
		{
			if(fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("varchar") || fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("char")
					|| fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("text") ||fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("nvarchar") 
					|| fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("nchar") || fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("ntext"))
			{
				this.cbxFpyFieldFilter.addItem(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
				this.cbxFpyFieldFilter.setItemCaption(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), fpyInfoFields.get(i).getFpyInfoFieldLabel());
			}
			else if(fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("date"))
			{
				this.cbxDateFieldsFilter.addItem(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
				this.cbxDateFieldsFilter.setItemCaption(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), fpyInfoFields.get(i).getFpyInfoFieldLabel());
			}	
		}
		
		bindFpyReportRptTable();
		
		//Elements events
		this.btnApplyFilters.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
		
				//System.out.println("Filtering Data...");
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
				
				if(cbxFpyFieldFilter.getValue() != null )
				{
					vaadinTblContainer.addContainerFilter(new Like(cbxFpyFieldFilter.getValue(), "%" + txtExpFieldFilter.getValue().trim() + "%"));
               		andSqlWhereClause.add(cbxFpyFieldFilter.getValue() + " LIKE '%" + txtExpFieldFilter.getValue().trim() + "%'");
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
			                
			                 TableQuery tblQuery = new TableQuery(fpyReport.getFpyReportDbRptTableNameId(), connectionPool, new MSSQLGenerator());
			                 List<OrderBy> tblOrderByRecordId = Arrays.asList(new OrderBy("FpyRecordId", false));
			                 StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(fpyReport.getFpyReportDbRptTableNameId(), null, tblOrderByRecordId, 0, 0, null);
			                 sqlQuery = sh.getQueryString();
			                 
			                 firstWhereClause = andSqlWhereClause.get(0);
			                 String sqlWhereClause = " WHERE " + firstWhereClause;
			                 
			                 for(int i=1; i < andSqlWhereClause.size(); i++)
			                 	 sqlWhereClause += " AND (" + andSqlWhereClause.get(i) + ")";
			                 
			                 String sqlColumnLabels = "";
			                 List<Object> asList = new ArrayList<Object>(Arrays.asList(tblFpyDataReport.getVisibleColumns()));
			                 for(int i=0; i<asList.size(); i++)
			                 	sqlColumnLabels += "\"" + asList.get(i).toString() + "\" AS \"" +  tblFpyDataReport.getColumnHeader(asList.get(i)) + "\",";
			                 
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
		        
		        Integer totalRecords = tblFpyDataReport.size();
		    	if (fpyReport.getFpyReportDbRptTableLastUpdate() != null)
		            lblLastRefreshDate.setValue("Last Refresh Date: " + fpyReport.getFpyReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
		    
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
		 
		FirstPassYieldReportJobDataDao fpyReportJobDataDao = new FirstPassYieldReportJobDataDao();
		Map<String, Object> result = fpyReportJobDataDao.fpyProcedureJob(fpyReport.getFpyReportId());
		vaadinTblContainer.refresh();
		fpyReport = new FirstPassYieldReportDao().getFirstPassYieldReportById(fpyReport.getFpyReportId());
	
		
		Integer totalRecords = this.tblFpyDataReport.size();
		if (fpyReport.getFpyReportDbRptTableLastUpdate() != null)
            this.lblLastRefreshDate.setValue("Last Refresh Date: " + fpyReport.getFpyReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
    
		if (Constants.SUCCESS == result.get("status")) {
			this.getUI().showNotification("First Pass Yield Report '" + fpyReport.getFpyReportName() + "' has been Refresh Successfully.", Notification.Type.HUMANIZED_MESSAGE);
			this.btnApplyFilters.click();		
		} else {
			String msgToDisplay = result.get("statusMessage").toString();
			if (Constants.JOB_NOT_EXECUTED.equalsIgnoreCase(msgToDisplay)) {
				msgToDisplay = "same First Pass Yield Report is getting refresh by another user";
			}
			this.getUI().showNotification("First Pass Yield Report '" + fpyReport.getFpyReportName() + "' can't refresh due to "+ msgToDisplay + ".", Notification.Type.WARNING_MESSAGE);
		} 
	}
	
	private void bindFpyReportRptTable()
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
		    
		    try {
		    	connectionPool = new SimpleJDBCConnectionPool("com.microsoft.sqlserver.jdbc.SQLServerDriver",
						"jdbc:sqlserver://"+dbHost+":"+dbPort+";databaseName="+dbDatabase,
						dbUsername, dbPassword);
		    
		    	TableQuery tblQuery = new TableQuery(this.fpyReport.getFpyReportDbRptTableNameId(), connectionPool, new MSSQLGenerator());
				tblQuery.setVersionColumn("FpyRecordId");

				vaadinTblContainer = new SQLContainer(tblQuery);
				
				tblFpyDataReport.setContainerDataSource(vaadinTblContainer);
				
				tblFpyDataReport.setColumnHeader("fpy_experiment", this.systemSettings.getExperimentLabel());				
				tblFpyDataReport.setColumnHeader("fpy_serial_number", "Serial Number");
				tblFpyDataReport.setColumnHeader("fpy_result", "Result");
				tblFpyDataReport.setColumnHeader("fpy_date_time", "Datetime");
				
				List<FirstPassYieldInfoField> fpyInfoFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(this.fpyReport.getFpyReportId());		


				String[] fpyRptVisibleCols = new String[4 + (fpyInfoFields != null ? fpyInfoFields.size() : 0)];
				fpyRptVisibleCols[0] = "fpy_experiment";
				fpyRptVisibleCols[1] = "fpy_date_time";
				fpyRptVisibleCols[2] = "fpy_serial_number";
				fpyRptVisibleCols[3] = "fpy_result";
				
				for(int i=0; fpyInfoFields != null && i<fpyInfoFields.size(); i++)
				{
					fpyRptVisibleCols[i+4] = fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId();
					tblFpyDataReport.setColumnHeader(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), fpyInfoFields.get(i).getFpyInfoFieldLabel());
						
					if(fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("float") || 
							fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("decimal") || 
							fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("int"))
					{
						tblFpyDataReport.setConverter(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), new StringToDoubleConverter() {
						    @Override
						    protected NumberFormat getFormat(Locale locale) {
						    	NumberFormat format = NumberFormat.getNumberInstance();
						    	format.setGroupingUsed(false);
						    	return format;
						    }
						});
						
					}
					
					if(fpyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("date"))
					{
						tblFpyDataReport.setConverter(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), new StringToDateConverter() {
						    @Override
						    protected DateFormat getFormat(Locale locale) {
								return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    }
						});
					}
				}
				
				tblFpyDataReport.setVisibleColumns(fpyRptVisibleCols);
				tblFpyDataReport.setColumnCollapsingAllowed(true);
				
				tblFpyDataReport.setCellStyleGenerator(new Table.CellStyleGenerator() {
				    @Override
					public String getStyle(Table source, Object itemId, Object propertyId) {	
				    	
				    	if(propertyId != null && "fpy_result".equals(propertyId.toString().trim().toLowerCase()))
				    	{
				    		Item item = source.getItem(itemId);
				    		String testResult = (String) item.getItemProperty("fpy_result").getValue();
				    		if(fpyReport.getFpyPassResultValue().toLowerCase().equals(testResult.trim().toLowerCase()))
				    			return "highlight-green";
				    		else 
				    			return "highlight-red";
				    	}
				    	
				    	return null;
					}
				});
				
				StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(fpyReport.getFpyReportDbRptTableNameId(), null, null, 0, 0, null);
                sqlQuery = sh.getQueryString();
                
				String sqlColumnLabels = "";
                List<Object> asList = new ArrayList<Object>(Arrays.asList(tblFpyDataReport.getVisibleColumns()));
                for(int i=0; i<asList.size(); i++)
                	sqlColumnLabels += "\"" + asList.get(i).toString() + "\" AS \"" +  tblFpyDataReport.getColumnHeader(asList.get(i)) + "\",";
                
                sqlColumnLabels = sqlColumnLabels.substring(0, sqlColumnLabels.length() - 1);
                
                sqlQuery = sqlQuery.replace("SELECT *", "SELECT " + sqlColumnLabels);
                
                if(!firstWhereClause.isEmpty())
                	sqlQuery += " WHERE " + firstWhereClause;
                
                sqlResultAttachCsvFileDownloaderToButton(this.btnExportExcel);
				
		    } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		    
		    Integer totalRecords = tblFpyDataReport.size();
	    	if (fpyReport.getFpyReportDbRptTableLastUpdate() != null)
	            this.lblLastRefreshDate.setValue("Last Refresh Date: " + fpyReport.getFpyReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
	  
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
                 }, lblFpyRptTitle.getValue().trim() + ".csv"));
         downloader.extend(downloadBtn);
		
	}
}
