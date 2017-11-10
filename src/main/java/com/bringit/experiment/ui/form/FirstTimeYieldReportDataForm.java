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

import com.bringit.experiment.bll.FirstTimeYieldInfoField;
import com.bringit.experiment.bll.FirstTimeYieldReport;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.FirstTimeYieldInfoFieldDao;
import com.bringit.experiment.dao.FirstTimeYieldReportDao;
import com.bringit.experiment.dao.FirstTimeYieldReportJobDataDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.ui.design.FirstTimeYieldReportDataDesign;
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

public class FirstTimeYieldReportDataForm extends FirstTimeYieldReportDataDesign{

	private SystemSettings systemSettings;
	
	FirstTimeYieldReport ftyReport = new FirstTimeYieldReport();
	private SQLContainer vaadinTblContainer;
	SysRole sysRoleSession = (SysRole) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");
	
	private String sqlQuery = "";    
	String firstWhereClause = "";
	List<String> andSqlWhereClause = new ArrayList<String>();
	
	public FirstTimeYieldReportDataForm(Integer ftyReportId)
	{	
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		
		this.ftyReport = new FirstTimeYieldReportDao().getFirstTimeYieldReportById(ftyReportId);
		this.lblFtyRptTitle.setValue(lblFtyRptTitle.getValue() + " - " + this.ftyReport.getFtyReportName()); 

		this.cbxDateFieldsFilter.setContainerDataSource(null);
		this.cbxFtyFieldFilter.setContainerDataSource(null);

		//Set filters
		this.cbxFtyFieldFilter.addItem("fty_experiment");
		this.cbxFtyFieldFilter.setItemCaption("fty_experiment", this.systemSettings.getExperimentLabel());
		
		this.cbxFtyFieldFilter.addItem("fty_serial_number");
		this.cbxFtyFieldFilter.setItemCaption("fty_serial_number", "Serial Number");
	
		this.cbxFtyFieldFilter.addItem("fty_result");
		this.cbxFtyFieldFilter.setItemCaption("fty_result", "Result");
	
		this.cbxDateFieldsFilter.addItem("fty_date_time");
		this.cbxDateFieldsFilter.setItemCaption("fty_date_time", "Datetime");
		
		List<FirstTimeYieldInfoField> ftyInfoFields = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(ftyReportId);		
	
		for(int i=0; ftyInfoFields != null && i<ftyInfoFields.size(); i++)
		{
			if(ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("varchar") || ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("char")
					|| ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("text") ||ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("nvarchar") 
					|| ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("nchar") || ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("ntext"))
			{
				this.cbxFtyFieldFilter.addItem(ftyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
				this.cbxFtyFieldFilter.setItemCaption(ftyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), ftyInfoFields.get(i).getFtyInfoFieldLabel());
			}
			else if(ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("date"))
			{
				this.cbxDateFieldsFilter.addItem(ftyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
				this.cbxDateFieldsFilter.setItemCaption(ftyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), ftyInfoFields.get(i).getFtyInfoFieldLabel());
			}	
		}
		
		bindFtyReportRptTable();
		
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
				
				if(cbxFtyFieldFilter.getValue() != null )
				{
					vaadinTblContainer.addContainerFilter(new Like(cbxFtyFieldFilter.getValue(), "%" + txtExpFieldFilter.getValue().trim() + "%"));
               		andSqlWhereClause.add(cbxFtyFieldFilter.getValue() + " LIKE '%" + txtExpFieldFilter.getValue().trim() + "%'");
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
			                
			                 TableQuery tblQuery = new TableQuery(ftyReport.getFtyReportDbRptTableNameId(), connectionPool, new MSSQLGenerator());
			                 List<OrderBy> tblOrderByRecordId = Arrays.asList(new OrderBy("FtyRecordId", false));
			                 StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(ftyReport.getFtyReportDbRptTableNameId(), null, tblOrderByRecordId, 0, 0, null);
			                 sqlQuery = sh.getQueryString();
			                 
			                 firstWhereClause = andSqlWhereClause.get(0);
			                 String sqlWhereClause = " WHERE " + firstWhereClause;
			                 
			                 for(int i=1; i < andSqlWhereClause.size(); i++)
			                 	 sqlWhereClause += " AND (" + andSqlWhereClause.get(i) + ")";
			                 
			                 String sqlColumnLabels = "";
			                 List<Object> asList = new ArrayList<Object>(Arrays.asList(tblFtyDataReport.getVisibleColumns()));
			                 for(int i=0; i<asList.size(); i++)
			                 	sqlColumnLabels += "\"" + asList.get(i).toString() + "\" AS \"" +  tblFtyDataReport.getColumnHeader(asList.get(i)) + "\",";
			                 
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
		        
		        Integer totalRecords = tblFtyDataReport.size();
		    	if (ftyReport.getFtyReportDbRptTableLastUpdate() != null)
		            lblLastRefreshDate.setValue("Last Refresh Date: " + ftyReport.getFtyReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
		    
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
		 
		FirstTimeYieldReportJobDataDao ftyReportJobDataDao = new FirstTimeYieldReportJobDataDao();
		Map<String, Object> result = ftyReportJobDataDao.ftyProcedureJob(ftyReport.getFtyReportId());
		vaadinTblContainer.refresh();
		ftyReport = new FirstTimeYieldReportDao().getFirstTimeYieldReportById(ftyReport.getFtyReportId());
	
		
		Integer totalRecords = this.tblFtyDataReport.size();
		if (ftyReport.getFtyReportDbRptTableLastUpdate() != null)
            this.lblLastRefreshDate.setValue("Last Refresh Date: " + ftyReport.getFtyReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
    
		if (Constants.SUCCESS == result.get("status")) {
			this.getUI().showNotification("First Time Yield Report '" + ftyReport.getFtyReportName() + "' has been Refresh Successfully.", Notification.Type.HUMANIZED_MESSAGE);
			this.btnApplyFilters.click();		
		} else {
			String msgToDisplay = result.get("statusMessage").toString();
			if (Constants.JOB_NOT_EXECUTED.equalsIgnoreCase(msgToDisplay)) {
				msgToDisplay = "same First Time Yield Report is getting refresh by another user";
			}
			this.getUI().showNotification("First Time Yield Report '" + ftyReport.getFtyReportName() + "' can't refresh due to "+ msgToDisplay + ".", Notification.Type.WARNING_MESSAGE);
		} 
	}
	
	private void bindFtyReportRptTable()
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
		    
		    	TableQuery tblQuery = new TableQuery(this.ftyReport.getFtyReportDbRptTableNameId(), connectionPool, new MSSQLGenerator());
				tblQuery.setVersionColumn("FtyRecordId");

				vaadinTblContainer = new SQLContainer(tblQuery);
				
				tblFtyDataReport.setContainerDataSource(vaadinTblContainer);
				
				tblFtyDataReport.setColumnHeader("fty_experiment", this.systemSettings.getExperimentLabel());				
				tblFtyDataReport.setColumnHeader("fty_serial_number", "Serial Number");
				tblFtyDataReport.setColumnHeader("fty_result", "Result");
				tblFtyDataReport.setColumnHeader("fty_date_time", "Datetime");
				tblFtyDataReport.setConverter("fpy_date_time", new StringToDateConverter() {
				    @Override
				    protected DateFormat getFormat(Locale locale) {
						return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    }
				});
				
				
				List<FirstTimeYieldInfoField> ftyInfoFields = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(this.ftyReport.getFtyReportId());		


				String[] ftyRptVisibleCols = new String[4 + (ftyInfoFields != null ? ftyInfoFields.size() : 0)];
				ftyRptVisibleCols[0] = "fty_experiment";
				ftyRptVisibleCols[1] = "fty_date_time";
				ftyRptVisibleCols[2] = "fty_serial_number";
				ftyRptVisibleCols[3] = "fty_result";
				
				for(int i=0; ftyInfoFields != null && i<ftyInfoFields.size(); i++)
				{
					ftyRptVisibleCols[i+4] = ftyInfoFields.get(i).getExperimentField().getExpDbFieldNameId();
					tblFtyDataReport.setColumnHeader(ftyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), ftyInfoFields.get(i).getFtyInfoFieldLabel());
						
					if(ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("float") || 
							ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("decimal") || 
							ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("int"))
					{
						tblFtyDataReport.setConverter(ftyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), new StringToDoubleConverter() {
						    @Override
						    protected NumberFormat getFormat(Locale locale) {
						    	NumberFormat format = NumberFormat.getNumberInstance();
						    	format.setGroupingUsed(false);
						    	return format;
						    }
						});
						
					}
					
					if(ftyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("date"))
					{
						tblFtyDataReport.setConverter(ftyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), new StringToDateConverter() {
						    @Override
						    protected DateFormat getFormat(Locale locale) {
								return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    }
						});
					}
				}
				
				tblFtyDataReport.setVisibleColumns(ftyRptVisibleCols);
				tblFtyDataReport.setColumnCollapsingAllowed(true);
				
				tblFtyDataReport.setCellStyleGenerator(new Table.CellStyleGenerator() {
				    @Override
					public String getStyle(Table source, Object itemId, Object propertyId) {	
				    	
				    	if(propertyId != null && "fty_result".equals(propertyId.toString().trim().toLowerCase()))
				    	{
				    		Item item = source.getItem(itemId);
				    		String testResult = (String) item.getItemProperty("fty_result").getValue();
				    		if(ftyReport.getFtyPassResultValue().toLowerCase().equals(testResult.trim().toLowerCase()))
				    			return "highlight-green";
				    		else 
				    			return "highlight-red";
				    	}
				    	
				    	return null;
					}
				});
				
				StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(ftyReport.getFtyReportDbRptTableNameId(), null, null, 0, 0, null);
                sqlQuery = sh.getQueryString();
                
				String sqlColumnLabels = "";
                List<Object> asList = new ArrayList<Object>(Arrays.asList(tblFtyDataReport.getVisibleColumns()));
                for(int i=0; i<asList.size(); i++)
                	sqlColumnLabels += "\"" + asList.get(i).toString() + "\" AS \"" +  tblFtyDataReport.getColumnHeader(asList.get(i)) + "\",";
                
                sqlColumnLabels = sqlColumnLabels.substring(0, sqlColumnLabels.length() - 1);
                
                sqlQuery = sqlQuery.replace("SELECT *", "SELECT " + sqlColumnLabels);
                
                if(!firstWhereClause.isEmpty())
                	sqlQuery += " WHERE " + firstWhereClause;
                
                sqlResultAttachCsvFileDownloaderToButton(this.btnExportExcel);
				
		    } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		    
		    Integer totalRecords = tblFtyDataReport.size();
	    	if (ftyReport.getFtyReportDbRptTableLastUpdate() != null)
	            this.lblLastRefreshDate.setValue("Last Refresh Date: " + ftyReport.getFtyReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
	  
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
                 }, lblFtyRptTitle.getValue().trim() + ".csv"));
         downloader.extend(downloadBtn);
		
	}
	
}
