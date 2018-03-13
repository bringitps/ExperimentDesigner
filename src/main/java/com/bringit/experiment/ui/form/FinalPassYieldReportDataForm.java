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

import com.bringit.experiment.bll.FinalPassYieldInfoField;
import com.bringit.experiment.bll.FinalPassYieldReport;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.FinalPassYieldInfoFieldDao;
import com.bringit.experiment.dao.FinalPassYieldReportDao;
import com.bringit.experiment.dao.FinalPassYieldReportJobDataDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.ui.design.FinalPassYieldReportDataDesign;
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

public class FinalPassYieldReportDataForm extends FinalPassYieldReportDataDesign{

private SystemSettings systemSettings;
	
	FinalPassYieldReport fnyReport = new FinalPassYieldReport();
	private SQLContainer vaadinTblContainer;
	SysRole sysRoleSession = (SysRole) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");
	
	private String sqlQuery = "";    
	String firstWhereClause = "";
	List<String> andSqlWhereClause = new ArrayList<String>();
	
	public FinalPassYieldReportDataForm(Integer fnyReportId)
	{	
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		
		this.fnyReport = new FinalPassYieldReportDao().getFinalPassYieldReportById(fnyReportId);
		this.lblFnyRptTitle.setValue(lblFnyRptTitle.getValue() + " - " + this.fnyReport.getFnyReportName()); 

		this.cbxDateFieldsFilter.setContainerDataSource(null);
		this.cbxFnyFieldFilter.setContainerDataSource(null);

		//Set filters
		this.cbxFnyFieldFilter.addItem("fny_experiment");
		this.cbxFnyFieldFilter.setItemCaption("fny_experiment", this.systemSettings.getExperimentLabel());
		
		this.cbxFnyFieldFilter.addItem("fny_serial_number");
		this.cbxFnyFieldFilter.setItemCaption("fny_serial_number", "Serial Number");
	
		this.cbxFnyFieldFilter.addItem("fny_result");
		this.cbxFnyFieldFilter.setItemCaption("fny_result", "Result");
	
		this.cbxDateFieldsFilter.addItem("fny_date_time");
		this.cbxDateFieldsFilter.setItemCaption("fny_date_time", "Datetime");
		
		List<FinalPassYieldInfoField> fnyInfoFields = new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldByReportById(fnyReportId);		
	
		for(int i=0; fnyInfoFields != null && i<fnyInfoFields.size(); i++)
		{
			if(fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("varchar") || fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("char")
					|| fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("text") ||fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("nvarchar") 
					|| fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("nchar") || fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().startsWith("ntext"))
			{
				this.cbxFnyFieldFilter.addItem(fnyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
				this.cbxFnyFieldFilter.setItemCaption(fnyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), fnyInfoFields.get(i).getFnyInfoFieldLabel());
			}
			else if(fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("date"))
			{
				this.cbxDateFieldsFilter.addItem(fnyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
				this.cbxDateFieldsFilter.setItemCaption(fnyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), fnyInfoFields.get(i).getFnyInfoFieldLabel());
			}	
		}
		
		bindFnyReportRptTable();
		
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
				
				if(cbxFnyFieldFilter.getValue() != null )
				{
					vaadinTblContainer.addContainerFilter(new Like(cbxFnyFieldFilter.getValue(), "%" + txtExpFieldFilter.getValue().trim() + "%"));
               		andSqlWhereClause.add(cbxFnyFieldFilter.getValue() + " LIKE '%" + txtExpFieldFilter.getValue().trim() + "%'");
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
			                
			                 TableQuery tblQuery = new TableQuery(fnyReport.getFnyReportDbRptTableNameId(), connectionPool, new MSSQLGenerator());
			                 List<OrderBy> tblOrderByRecordId = Arrays.asList(new OrderBy("FnyRecordId", false));
			                 StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(fnyReport.getFnyReportDbRptTableNameId(), null, tblOrderByRecordId, 0, 0, null);
			                 sqlQuery = sh.getQueryString();
			                 
			                 firstWhereClause = andSqlWhereClause.get(0);
			                 String sqlWhereClause = " WHERE " + firstWhereClause;
			                 
			                 for(int i=1; i < andSqlWhereClause.size(); i++)
			                 	 sqlWhereClause += " AND (" + andSqlWhereClause.get(i) + ")";
			                 
			                 String sqlColumnLabels = "";
			                 List<Object> asList = new ArrayList<Object>(Arrays.asList(tblFnyDataReport.getVisibleColumns()));
			                 for(int i=0; i<asList.size(); i++)
			                 	sqlColumnLabels += "\"" + asList.get(i).toString() + "\" AS \"" +  tblFnyDataReport.getColumnHeader(asList.get(i)) + "\",";
			                 
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
		        
		        Integer totalRecords = tblFnyDataReport.size();
		    	if (fnyReport.getFnyReportDbRptTableLastUpdate() != null)
		            lblLastRefreshDate.setValue("Last Refresh Date: " + fnyReport.getFnyReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
		    
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
		 
		FinalPassYieldReportJobDataDao fnyReportJobDataDao = new FinalPassYieldReportJobDataDao();
		Map<String, Object> result = fnyReportJobDataDao.fnyProcedureJob(fnyReport.getFnyReportId());
		vaadinTblContainer.refresh();
		fnyReport = new FinalPassYieldReportDao().getFinalPassYieldReportById(fnyReport.getFnyReportId());
	
		
		Integer totalRecords = this.tblFnyDataReport.size();
		if (fnyReport.getFnyReportDbRptTableLastUpdate() != null)
            this.lblLastRefreshDate.setValue("Last Refresh Date: " + fnyReport.getFnyReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
    
		if (Constants.SUCCESS == result.get("status")) {
			this.getUI().showNotification("Final Pass Yield Report '" + fnyReport.getFnyReportName() + "' has been Refresh Successfully.", Notification.Type.HUMANIZED_MESSAGE);
			this.btnApplyFilters.click();		
		} else {
			String msgToDisplay = result.get("statusMessage").toString();
			if (Constants.JOB_NOT_EXECUTED.equalsIgnoreCase(msgToDisplay)) {
				msgToDisplay = "same Final Pass Yield Report is getting refresh by another user";
			}
			this.getUI().showNotification("Final Pass Yield Report '" + fnyReport.getFnyReportName() + "' can't refresh due to "+ msgToDisplay + ".", Notification.Type.WARNING_MESSAGE);
		} 
	}
	
	private void bindFnyReportRptTable()
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
		    
		    	TableQuery tblQuery = new TableQuery(this.fnyReport.getFnyReportDbRptTableNameId(), connectionPool, new MSSQLGenerator());
				tblQuery.setVersionColumn("FpyRecordId");

				vaadinTblContainer = new SQLContainer(tblQuery);
				
				tblFnyDataReport.setContainerDataSource(vaadinTblContainer);
				
				tblFnyDataReport.setColumnHeader("fny_experiment", this.systemSettings.getExperimentLabel());				
				tblFnyDataReport.setColumnHeader("fny_serial_number", "Serial Number");
				tblFnyDataReport.setColumnHeader("fny_result", "Result");
				tblFnyDataReport.setColumnHeader("fny_date_time", "Datetime");
				tblFnyDataReport.setConverter("fny_date_time", new StringToDateConverter() {
				    @Override
				    protected DateFormat getFormat(Locale locale) {
						return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    }
				});
				
				
				List<FinalPassYieldInfoField> fnyInfoFields = new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldByReportById(this.fnyReport.getFnyReportId());		


				String[] fnyRptVisibleCols = new String[4 + (fnyInfoFields != null ? fnyInfoFields.size() : 0)];
				fnyRptVisibleCols[0] = "fny_experiment";
				fnyRptVisibleCols[1] = "fny_date_time";
				fnyRptVisibleCols[2] = "fny_serial_number";
				fnyRptVisibleCols[3] = "fny_result";
				
				for(int i=0; fnyInfoFields != null && i<fnyInfoFields.size(); i++)
				{
					fnyRptVisibleCols[i+4] = fnyInfoFields.get(i).getExperimentField().getExpDbFieldNameId();
					tblFnyDataReport.setColumnHeader(fnyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), fnyInfoFields.get(i).getFnyInfoFieldLabel());
						
					if(fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("float") || 
							fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("decimal") || 
							fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("int"))
					{
						tblFnyDataReport.setConverter(fnyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), new StringToDoubleConverter() {
						    @Override
						    protected NumberFormat getFormat(Locale locale) {
						    	NumberFormat format = NumberFormat.getNumberInstance();
						    	format.setGroupingUsed(false);
						    	return format;
						    }
						});
						
					}
					
					if(fnyInfoFields.get(i).getExperimentField().getExpFieldType().toLowerCase().contains("date"))
					{
						tblFnyDataReport.setConverter(fnyInfoFields.get(i).getExperimentField().getExpDbFieldNameId(), new StringToDateConverter() {
						    @Override
						    protected DateFormat getFormat(Locale locale) {
								return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    }
						});
					}
				}
				
				tblFnyDataReport.setVisibleColumns(fnyRptVisibleCols);
				tblFnyDataReport.setColumnCollapsingAllowed(true);
				
				tblFnyDataReport.setCellStyleGenerator(new Table.CellStyleGenerator() {
				    @Override
					public String getStyle(Table source, Object itemId, Object propertyId) {	
				    	
				    	if(propertyId != null && "fny_result".equals(propertyId.toString().trim().toLowerCase()))
				    	{
				    		Item item = source.getItem(itemId);
				    		String testResult = (String) item.getItemProperty("fny_result").getValue();
				    		if(fnyReport.getFnyPassResultValue().toLowerCase().equals(testResult.trim().toLowerCase()))
				    			return "highlight-green";
				    		else 
				    			return "highlight-red";
				    	}
				    	
				    	return null;
					}
				});
				
				StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(fnyReport.getFnyReportDbRptTableNameId(), null, null, 0, 0, null);
                sqlQuery = sh.getQueryString();
                
				String sqlColumnLabels = "";
                List<Object> asList = new ArrayList<Object>(Arrays.asList(tblFnyDataReport.getVisibleColumns()));
                for(int i=0; i<asList.size(); i++)
                	sqlColumnLabels += "\"" + asList.get(i).toString() + "\" AS \"" +  tblFnyDataReport.getColumnHeader(asList.get(i)) + "\",";
                
                sqlColumnLabels = sqlColumnLabels.substring(0, sqlColumnLabels.length() - 1);
                
                sqlQuery = sqlQuery.replace("SELECT *", "SELECT " + sqlColumnLabels);
                
                if(!firstWhereClause.isEmpty())
                	sqlQuery += " WHERE " + firstWhereClause;
                
                sqlResultAttachCsvFileDownloaderToButton(this.btnExportExcel);
				
		    } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		    
		    Integer totalRecords = tblFnyDataReport.size();
	    	if (fnyReport.getFnyReportDbRptTableLastUpdate() != null)
	            this.lblLastRefreshDate.setValue("Last Refresh Date: " + fnyReport.getFnyReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
	  
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
                 }, lblFnyRptTitle.getValue().trim() + ".csv"));
         downloader.extend(downloadBtn);
		
	}
	
}
