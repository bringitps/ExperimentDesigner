package com.bringit.experiment.ui.form;

import com.bringit.experiment.bll.CmForSysRole;
import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.TargetColumn;
import com.bringit.experiment.bll.TargetColumnGroup;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.CmForSysRoleDao;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.TargetColumnDao;
import com.bringit.experiment.dao.TargetColumnGroupDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.dao.TargetReportJobDataDao;
import com.bringit.experiment.ui.design.TargetDataReportDesign;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.Constants;
import com.opencsv.CSVWriter;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Item;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

public class TargetDataReportForm extends TargetDataReportDesign{

	private TargetReport targetRpt = new TargetReport();
	Experiment experiment = new Experiment();
	List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
	List<ContractManufacturer> contractManufacturers = new ArrayList<ContractManufacturer>();
	private SQLContainer vaadinTblContainer;
	SysRole sysRoleSession = (SysRole) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");

	private String sqlQuery = "";    
	String firstWhereClause = "";
	List<String> andSqlWhereClause = new ArrayList<String>();
	
	public TargetDataReportForm(Integer targetDataReportId)
	{
		targetRpt = new TargetReportDao().getTargetReportById(targetDataReportId);
		lblTargetRptTitle.setValue(lblTargetRptTitle.getValue() + " - " + targetRpt.getTargetReportName()); // Attach RPT Table last updated date

		if (targetRpt.getTargetReportDbRptTableLastUpdate() != null)
			lblLastRefreshDate.setValue("Last Refresh Date: " + targetRpt.getTargetReportDbRptTableLastUpdate());

		//Add the button "Refresh Data Now" to run SP and get data refreshed 
		//If this target data report is being refreshed hide "Refresh Data Now" button

		btnRefreshButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				refreshData();
			}

		});

		if(targetRpt.getTargetReportWhatIf() != null && targetRpt.getTargetReportWhatIf())
		{
			this.cbxContractManufacturer.setVisible(false);
			this.btnViewChart.setVisible(false);
		}
		
		/*
		experimentFields = new ExperimentFieldDao().getActiveExperimentFields(targetRpt.getExperiment());
		
		cbxDateFieldsFilter.setContainerDataSource(null);
		cbxExpFieldFilter.setContainerDataSource(null);

		cbxDateFieldsFilter.addItem("CreatedDate");
		cbxDateFieldsFilter.setItemCaption("CreatedDate", "Created Date");

		cbxDateFieldsFilter.addItem("LastModifiedDate");
		cbxDateFieldsFilter.setItemCaption("LastModifiedDate", "Last Modified Date");

		cbxDateFieldsFilter.setInvalidAllowed(false);
		cbxExpFieldFilter.setInvalidAllowed(false);
		
		for(int i=0; experimentFields != null &&i<experimentFields.size(); i++)
		{
			if(experimentFields.get(i).getExpFieldType().trim().contains("date"))
			{
				cbxDateFieldsFilter.addItem(experimentFields.get(i).getExpDbFieldNameId());
				cbxDateFieldsFilter.setItemCaption(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());
			}
			else 	if(experimentFields.get(i).getExpFieldType().trim().startsWith("varchar") || experimentFields.get(i).getExpFieldType().trim().startsWith("char")
					|| experimentFields.get(i).getExpFieldType().trim().startsWith("text") ||experimentFields.get(i).getExpFieldType().trim().startsWith("nvarchar") 
					|| experimentFields.get(i).getExpFieldType().trim().startsWith("nchar") || experimentFields.get(i).getExpFieldType().trim().startsWith("ntext"))
		
			{
				cbxExpFieldFilter.addItem(experimentFields.get(i).getExpDbFieldNameId());
				cbxExpFieldFilter.setItemCaption(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());
			}
		}
		
		contractManufacturers = new ContractManufacturerDao().getAllContractManufacturers();
		for(int i=0; contractManufacturers!=null && i<contractManufacturers.size(); i++)
		{
			cbxContractManufacturer.addItem(contractManufacturers.get(i).getCmId());
			cbxContractManufacturer.setItemCaption(contractManufacturers.get(i).getCmId(), contractManufacturers.get(i).getCmName());
		}
		cbxContractManufacturer.setInvalidAllowed(false);
		*/
		
		List<TargetColumnGroup> targetRptColGroups = new TargetColumnGroupDao().getTargetColumnGroupsByReportId(targetRpt.getTargetReportId());
		List<String> dbRptTableCols = new ArrayList<String>();
    	List<String> dbRptTableTypes = new ArrayList<String>();
    	
		dbRptTableCols.add("RecordId" );
		dbRptTableTypes.add("int");
		
    	for(int i=0; i<targetRptColGroups.size(); i++)
    	{
    		List<TargetColumn> targetRptCols = new TargetColumnDao().getTargetColumnsByColGroupById(targetRptColGroups.get(i).getTargetColumnGroupId());
    		
    		for(int j=0; j<targetRptCols.size(); j++)
    		{
    			dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_"));
    			dbRptTableTypes.add(targetRptCols.get(j).getExperimentField().getExpFieldType());
    		
    			if(!targetRptCols.get(j).getTargetColumnIsInfo())
    			{
    				if(targetRpt.getTargetReportWhatIf() != null && targetRpt.getTargetReportWhatIf())
    				{	
    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Current_Min");
    					dbRptTableTypes.add("varchar(50)");

    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Current_Max");
    					dbRptTableTypes.add("varchar(50)");
                	
    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Current_Result");
    					dbRptTableTypes.add("varchar(20)");
                    
    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_New_Min");
    					dbRptTableTypes.add("varchar(50)");

    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_New_Max");
    					dbRptTableTypes.add("varchar(50)");
                	
    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_New_Result");
    					dbRptTableTypes.add("varchar(20)");
    				}
    				else
    				{
    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Result" );
    					dbRptTableTypes.add("varchar(20)");
    				}
    			}
    		}
    	}
    	
		dbRptTableCols.add("Result");
		dbRptTableTypes.add("varchar(20)");
		
		cbxExpFieldFilter.setContainerDataSource(null);
		cbxDateFieldsFilter.setContainerDataSource(null);
		
		for(int i=0; i<dbRptTableCols.size(); i++)
		{
			if(dbRptTableTypes.get(i).toLowerCase().startsWith("varchar") || dbRptTableTypes.get(i).toLowerCase().startsWith("char")
					|| dbRptTableTypes.get(i).toLowerCase().startsWith("text") ||dbRptTableTypes.get(i).toLowerCase().startsWith("nvarchar") 
					|| dbRptTableTypes.get(i).toLowerCase().startsWith("nchar") || dbRptTableTypes.get(i).toLowerCase().startsWith("ntext"))
			{
				cbxExpFieldFilter.addItem(dbRptTableCols.get(i));
				cbxExpFieldFilter.setItemCaption(dbRptTableCols.get(i), dbRptTableCols.get(i).replaceAll("_", " "));
			}
			else if(dbRptTableTypes.get(i).toLowerCase().contains("date"))
			{
				cbxDateFieldsFilter.addItem(dbRptTableCols.get(i));
				cbxDateFieldsFilter.setItemCaption(dbRptTableCols.get(i), dbRptTableCols.get(i).replaceAll("_", " "));
			}			
		}
		
		if(targetRpt.getTargetReportWhatIf() == null || !targetRpt.getTargetReportWhatIf())
		{
			cbxDateFieldsFilter.addItem("CreatedDate");
			cbxDateFieldsFilter.setItemCaption("CreatedDate", "Created Date");

			if(cbxDateFieldsFilter.size() == 1)
				cbxDateFieldsFilter.select("CreatedDate");				
	
			cbxDateFieldsFilter.addItem("LastModifiedDate");
			cbxDateFieldsFilter.setItemCaption("LastModifiedDate", "Last Modified Date");
		}

		if (!"sys_admin".equalsIgnoreCase(sysRoleSession.getRoleName())) {
			List<CmForSysRole> cmForSysRole =  new CmForSysRoleDao().getListOfCmForSysRoleBysysRoleId(sysRoleSession.getRoleId());
			cmForSysRole.forEach(x-> contractManufacturers.add(x.getContractManufacturer()));
		} else {
			contractManufacturers = new ContractManufacturerDao().getAllContractManufacturers();
		}

		for(int i=0; contractManufacturers!=null && i<contractManufacturers.size(); i++)
		{
			cbxContractManufacturer.addItem(contractManufacturers.get(i).getCmName());
			cbxContractManufacturer.setItemCaption(contractManufacturers.get(i).getCmName(), contractManufacturers.get(i).getCmName());
		}
		cbxContractManufacturer.setInvalidAllowed(false);
	
		bindTargetReportRptTable();

		//To do:
		//Include Container Filters to Table according to CM Restrictions
		//1) Get Role of Session
		//SysRole sysRoleSession = (SysRole)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");
		//2) Get CmNames String array
		//3) Set static filter to data loaded
		//1 Container Filter by 1 CmName
		//Equal Operator needs to be used vaadinTblContainer.addContainerFilter(new Compare.Equal(this.cbxDateFieldsFilter.getValue(), dateFilterValue1));
		
		 //If there is no Contract Manufacturer loaded into system, there should not have restrictions
        List<ContractManufacturer> allContractManufacturersLoaded = new ContractManufacturerDao().getAllContractManufacturers();
        if(allContractManufacturersLoaded != null && allContractManufacturersLoaded.size() >0)
        { 
			List<Compare.Equal> filterList= new ArrayList<>();
			if (!"sys_admin".equalsIgnoreCase(sysRoleSession.getRoleName())) {
				List<ContractManufacturer> contractManufacturersFilter = new CmForSysRoleDao().getListOfCmForBysysRoleId(sysRoleSession.getRoleId());
				for (ContractManufacturer con : contractManufacturersFilter) {
					filterList.add(new Compare.Equal("CmName", con.getCmName()));
	
				}
				vaadinTblContainer.addContainerFilter(new Or(filterList.toArray(new Compare.Equal[filterList.size()])));
	
			}
        }

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
				
				if(cbxExpFieldFilter.getValue() != null )
				{
					vaadinTblContainer.addContainerFilter(new Like(cbxExpFieldFilter.getValue(), "%" + txtExpFieldFilter.getValue().trim() + "%"));
               		andSqlWhereClause.add(cbxExpFieldFilter.getValue() + " LIKE '%" + txtExpFieldFilter.getValue().trim() + "%'");
				}
				
				if(cbxContractManufacturer.getValue() != null )
				{
					vaadinTblContainer.addContainerFilter(new Compare.Equal("CmName",cbxContractManufacturer.getValue()));
					andSqlWhereClause.add("CmName = '" + cbxContractManufacturer.getValue() + "'");
				}

				 //If there is no Contract Manufacturer loaded into system, there should not have restrictions
		        List<ContractManufacturer> allContractManufacturersLoaded = new ContractManufacturerDao().getAllContractManufacturers();
		        if(allContractManufacturersLoaded != null && allContractManufacturersLoaded.size() >0)
		        { 		        	
					List<Compare.Equal> filterList= new ArrayList<>();
					if (!"sys_admin".equalsIgnoreCase(sysRoleSession.getRoleName())) {
						List<ContractManufacturer> contractManufacturersFilter = new CmForSysRoleDao().getListOfCmForBysysRoleId(sysRoleSession.getRoleId());
						for (ContractManufacturer con : contractManufacturersFilter) {
							filterList.add(new Compare.Equal("CmName", con.getCmName()));
							andSqlWhereClause.add("CmName = '" + con.getCmName() + "'");
	
						}
						vaadinTblContainer.addContainerFilter(new Or(filterList.toArray(new Compare.Equal[filterList.size()])));
					}
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
			                
			                 TableQuery tblQuery = new TableQuery(targetRpt.getTargetReportDbRptTableNameId(), connectionPool, new MSSQLGenerator());
			                 List<OrderBy> tblOrderByRecordId = Arrays.asList(new OrderBy("RecordId", false));
			                 StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(targetRpt.getTargetReportDbRptTableNameId(), null, tblOrderByRecordId, 0, 0, null);
			                 sqlQuery = sh.getQueryString();
			                 
			                 firstWhereClause = andSqlWhereClause.get(0);
			                 String sqlWhereClause = " WHERE " + firstWhereClause;
			                 
			                 for(int i=1; i < andSqlWhereClause.size(); i++)
			                 	 sqlWhereClause += " AND (" + andSqlWhereClause.get(i) + ")";
			                 
			                 String sqlColumnLabels = "";
			                 List<Object> asList = new ArrayList<Object>(Arrays.asList(tblTargetDataReport.getVisibleColumns()));
			                 for(int i=0; i<asList.size(); i++)
			                 	sqlColumnLabels += "\"" + asList.get(i).toString() + "\" AS \"" +  tblTargetDataReport.getColumnHeader(asList.get(i)) + "\",";
			                 
			                 sqlColumnLabels = sqlColumnLabels.substring(0, sqlColumnLabels.length() - 1);
			                 
			                 sqlQuery = sqlQuery.replace("SELECT *", "SELECT " + sqlColumnLabels);
			                 sqlQuery = sqlQuery.replace("ORDER BY", " " + sqlWhereClause + " ORDER BY");
			                 
			                 //System.out.println("Built Query: " + sqlQuery);
			                 
			             } catch (SQLException e) {
			                 // TODO Auto-generated catch block
			                 e.printStackTrace();
			             }
			         }
		        }	
		        
		        Integer totalRecords = tblTargetDataReport.size();
		    	if (targetRpt.getTargetReportDbRptTableLastUpdate() != null)
		            lblLastRefreshDate.setValue("Last Refresh Date: " + targetRpt.getTargetReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
		    
			}
			
			
		});
		
		
		if(targetRpt.getTargetReportWhatIfDateColumnLabel() != null)
		{
		    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			vaadinTblContainer.removeAllContainerFilters();
			this.cbxDateFieldsFilter.select(targetRpt.getTargetReportWhatIfDateColumnLabel().replaceAll(" ", "_"));
			this.dtFilter1.setValue(targetRpt.getTargetReportWhatIfDateFrom());
			this.dtFilter2.setValue(targetRpt.getTargetReportWhatIfDateTo());
			
			Date dateFilterValue1 = dtFilter1.getValue();
			Date dateFilterValue2 = dtFilter2.getValue();

			dateFilterValue2.setHours(23);
			dateFilterValue2.setMinutes(59);
			dateFilterValue2.setSeconds(59);
			vaadinTblContainer.addContainerFilter(new Between(cbxDateFieldsFilter.getValue(), dateFilterValue1, dateFilterValue2)); 	
			andSqlWhereClause.add(cbxDateFieldsFilter.getValue() + " BETWEEN '" + df.format(dateFilterValue1) + "' AND '" + df.format(dateFilterValue2) + "'");
		}
		
		
		/*		
		//Call SP with TargetReportId
		List<String> spParams = new ArrayList<String>();
		spParams.add(targetRpt.getTargetReportId().toString());
		spParams.add(targetRpt.getExperiment().getExpId().toString());
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
				
		ResultSet spResults = new ExecuteQueryDao().executeStoredProcedure("spTargetReportBuilder", spParams);
		
		if(spResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(tblTargetDataReport, spResults, 1);
		
		this.btnApplyFilters.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				// spTargetReportBuilder 
			 	//	@TargetReportId NVARCHAR(MAX),
				//	@ExperimentId NVARCHAR(MAX),
				//	@DateFieldName NVARCHAR(MAX),
				//	@FromDate NVARCHAR(MAX),
				//	@ToDate NVARCHAR(MAX),
				//	@CmId NVARCHAR(MAX),
				//	@ExpFieldName NVARCHAR(MAX),
				//	@ExpFieldValue NVARCHAR(MAX)
				
				String dateFieldName = "";
				String fromDate = "";
				String toDate = "";
				String cmId = "";
				String expFieldName = "";
				String expFieldValue = "";
				
				if(cbxContractManufacturer.getValue() != null && !cbxContractManufacturer.getValue().toString().isEmpty())
					cmId = cbxContractManufacturer.getValue().toString().trim();
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
				if(cbxDateFieldsFilter.getValue() != null && !cbxDateFieldsFilter.getValue().toString().isEmpty()
						&& dtFilter1.getValue() != null && dtFilter2.getValue() != null)
				{
					dateFieldName = cbxDateFieldsFilter.getValue().toString();
					fromDate = df.format(dtFilter1.getValue());
					Date toDate24hours = dtFilter2.getValue();
					toDate24hours.setHours(23);
					toDate24hours.setMinutes(59);
					toDate24hours.setSeconds(59);
					toDate = df.format(toDate24hours);
				}
				
				if(cbxExpFieldFilter.getValue() != null && !cbxExpFieldFilter.getValue().toString().isEmpty()
						&& txtExpFieldFilter.getValue() != null && !txtExpFieldFilter.getValue().isEmpty())
				{
					expFieldName = cbxExpFieldFilter.getValue().toString();
					expFieldValue = txtExpFieldFilter.getValue();
				}
				
				//Call SP with TargetReportId
				List<String> spParams = new ArrayList<String>();
				spParams.add(targetRpt.getTargetReportId().toString());
				spParams.add(targetRpt.getExperiment().getExpId().toString());
				spParams.add(dateFieldName);
				spParams.add(fromDate);
				spParams.add(toDate);
				spParams.add(cmId);
				spParams.add(expFieldName);
				spParams.add(expFieldValue);
						
				ResultSet spResults = new ExecuteQueryDao().executeStoredProcedure("spTargetReportBuilder", spParams);
				
				if(spResults != null)
					VaadinControls.bindDbViewRsToVaadinTable(tblTargetDataReport, spResults, 1);
			}

		});
		*/
		
		this.btnViewChart.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openViewChartModalWindow();
			}

		});

		this.btnExportExcel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				exportExperimentDataReportToExcel();
			}

		});
	}

	private void refreshData() {
		TargetReportJobDataDao experimentJobDataDao = new TargetReportJobDataDao();
		Map<String, Object> result = experimentJobDataDao.targetProcedureJob(this.targetRpt.getTargetReportId());
		vaadinTblContainer.refresh();
		targetRpt = new TargetReportDao().getTargetReportById(targetRpt.getTargetReportId());
	
		
		Integer totalRecords = tblTargetDataReport.size();
		if (targetRpt.getTargetReportDbRptTableLastUpdate() != null)
            this.lblLastRefreshDate.setValue("Last Refresh Date: " + targetRpt.getTargetReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
    
		if (Constants.SUCCESS == result.get("status")) {
			this.getUI().showNotification("TargetReport '" + targetRpt.getTargetReportName() + "' has been Refresh Successfully.", Notification.Type.HUMANIZED_MESSAGE);
			this.btnApplyFilters.click();		
		} else {
			String msgToDisplay = result.get("statusMessage").toString();
			if (Constants.JOB_NOT_EXECUTED.equalsIgnoreCase(msgToDisplay)) {
				msgToDisplay = "same TargetReport is getting refresh by another user";
			}
			this.getUI().showNotification("TargetReport '" + targetRpt.getTargetReportName() + "' can't refresh due to "+ msgToDisplay + ".", Notification.Type.WARNING_MESSAGE);
		}
	}

	private void bindTargetReportRptTable()
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
		    
		    	TableQuery tblQuery = new TableQuery(targetRpt.getTargetReportDbRptTableNameId(), connectionPool, new MSSQLGenerator());
				tblQuery.setVersionColumn("RecordId");

				vaadinTblContainer = new SQLContainer(tblQuery);
				
				tblTargetDataReport.setContainerDataSource(vaadinTblContainer);
				
				List<TargetColumnGroup> targetRptColGroups = new TargetColumnGroupDao().getTargetColumnGroupsByReportId(targetRpt.getTargetReportId());
				List<String> dbRptTableCols = new ArrayList<String>();
		    	List<String> dbRptTableTypes = new ArrayList<String>();
		    	List<String> dbRptTableUoms =  new ArrayList<String>();
		    	
				dbRptTableCols.add("RecordId" );
				dbRptTableTypes.add("int");
				dbRptTableUoms.add("");			    			
    			
		    	for(int i=0; i<targetRptColGroups.size(); i++)
		    	{
		    		List<TargetColumn> targetRptCols = new TargetColumnDao().getTargetColumnsByColGroupById(targetRptColGroups.get(i).getTargetColumnGroupId());
		    		
		    		for(int j=0; j<targetRptCols.size(); j++)
		    		{
		    			dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_"));
		    			dbRptTableTypes.add(targetRptCols.get(j).getExperimentField().getExpFieldType());
		    			dbRptTableUoms.add(targetRptCols.get(j).getExperimentField().getUnitOfMeasure() != null ? targetRptCols.get(j).getExperimentField().getUnitOfMeasure().getUomAbbreviation() : "");
		    			
		    			if(!targetRptCols.get(j).getTargetColumnIsInfo())
		    			{
		    				if(targetRpt.getTargetReportWhatIf() != null && targetRpt.getTargetReportWhatIf())
		    				{	
		    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Current_Min");
		    					dbRptTableTypes.add("varchar(50)");
		    					dbRptTableUoms.add("");
		    					
		    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Current_Max");
		    					dbRptTableTypes.add("varchar(50)");
		    					dbRptTableUoms.add("");
		    					
		    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Current_Result");
		    					dbRptTableTypes.add("varchar(20)");
		    					dbRptTableUoms.add("");
		    					
		    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_New_Min");
		    					dbRptTableTypes.add("varchar(50)");
		    					dbRptTableUoms.add("");
		    					
		    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_New_Max");
		    					dbRptTableTypes.add("varchar(50)");
		    					dbRptTableUoms.add("");
		    					
		    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_New_Result");
		    					dbRptTableTypes.add("varchar(20)");
		    					dbRptTableUoms.add("");
		    					
		    				}
		    				else
		    				{
		    					dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Result" );
		    					dbRptTableTypes.add("varchar(20)");
		    					dbRptTableUoms.add("");
		    				}
		    			}
		    		}
		    	}
		    	
				dbRptTableCols.add("Result");
				dbRptTableTypes.add("varchar(20)");
				dbRptTableUoms.add("");			    			
    			
				for(int i=0; i<dbRptTableCols.size(); i++)
				{
					if(dbRptTableUoms.get(i).isEmpty())
						tblTargetDataReport.setColumnHeader(dbRptTableCols.get(i), dbRptTableCols.get(i).replaceAll("_", " "));
					else
						tblTargetDataReport.setColumnHeader(dbRptTableCols.get(i), dbRptTableCols.get(i).replaceAll("_", " ") + " [" + dbRptTableUoms.get(i) + "]");
						
					if(dbRptTableTypes.get(i).toLowerCase().contains("float") || 
							dbRptTableTypes.get(i).toLowerCase().contains("decimal") || 
							dbRptTableTypes.get(i).toLowerCase().contains("int"))
					{
						tblTargetDataReport.setConverter(dbRptTableCols.get(i), new StringToDoubleConverter() {
						    @Override
						    protected NumberFormat getFormat(Locale locale) {
						    	NumberFormat format = NumberFormat.getNumberInstance();
						    	format.setGroupingUsed(false);
						    	return format;
						    }
						});
						
					}
					
					if(dbRptTableTypes.get(i).toLowerCase().contains("date"))
					{
						tblTargetDataReport.setConverter(dbRptTableCols.get(i), new StringToDateConverter() {
						    @Override
						    protected DateFormat getFormat(Locale locale) {
								return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    }
						});
					}
				}
				
				tblTargetDataReport.setColumnHeader("RecordId", "Id");
				
				String[] targetRptCols = new String[dbRptTableCols.size()];
				for(int i=0; i<dbRptTableCols.size(); i++)
					targetRptCols[i] = dbRptTableCols.get(i);
				
				tblTargetDataReport.setVisibleColumns(targetRptCols);
				
				
				tblTargetDataReport.setCellStyleGenerator(new Table.CellStyleGenerator() {
				    @Override
					public String getStyle(Table source, Object itemId, Object propertyId) {	
				    	
				    	if(propertyId != null && "result".equals(propertyId.toString().trim().toLowerCase()))
				    	{
				    		Item item = source.getItem(itemId);
				    		String testResult = (String) item.getItemProperty("Result").getValue();
				    		if("pass".equals(testResult.trim().toLowerCase()))
				    			return "highlight-green";
				    		else 
				    			return "highlight-red";
				    	}
				    	
				    	return null;
					}
				});
				
				
				if(targetRpt.getTargetReportWhatIf() != null && targetRpt.getTargetReportWhatIf() && targetRpt.getTargetReportWhatIfDateColumnLabel() != null)
				{
					targetRpt.getTargetReportWhatIfDateTo().setHours(23);
					targetRpt.getTargetReportWhatIfDateTo().setMinutes(59);
					targetRpt.getTargetReportWhatIfDateTo().setSeconds(59);
					
				    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					firstWhereClause = targetRpt.getTargetReportWhatIfDateColumnLabel().replaceAll(" ", "_") + " BETWEEN '" + df.format(targetRpt.getTargetReportWhatIfDateFrom()) + "' AND '" + df.format(targetRpt.getTargetReportWhatIfDateTo()) + "'";
				}
				 
				
				StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(targetRpt.getTargetReportDbRptTableNameId(), null, null, 0, 0, null);
                sqlQuery = sh.getQueryString();
                
				String sqlColumnLabels = "";
                List<Object> asList = new ArrayList<Object>(Arrays.asList(tblTargetDataReport.getVisibleColumns()));
                for(int i=0; i<asList.size(); i++)
                	sqlColumnLabels += "\"" + asList.get(i).toString() + "\" AS \"" +  tblTargetDataReport.getColumnHeader(asList.get(i)) + "\",";
                
                sqlColumnLabels = sqlColumnLabels.substring(0, sqlColumnLabels.length() - 1);
                
                sqlQuery = sqlQuery.replace("SELECT *", "SELECT " + sqlColumnLabels);
                
                if(!firstWhereClause.isEmpty())
                	sqlQuery += " WHERE " + firstWhereClause;
                
                sqlResultAttachCsvFileDownloaderToButton(this.btnExportExcel);
				
		    } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		    
		    Integer totalRecords = tblTargetDataReport.size();
	    	if (targetRpt.getTargetReportDbRptTableLastUpdate() != null)
	            this.lblLastRefreshDate.setValue("Last Refresh Date: " + targetRpt.getTargetReportDbRptTableLastUpdate() + "  [Total Records: " + totalRecords + "]");
	    }
	}

	
	private void openViewChartModalWindow()
	{
		 Window viewChartModalWindow = new Window(this.targetRpt.getTargetReportName());
		 viewChartModalWindow.setModal(true);
		 viewChartModalWindow.setResizable(false);
		 viewChartModalWindow.setContent(new TargetDataChartForm(this.targetRpt, this.experiment, this.experimentFields, this.contractManufacturers, cbxDateFieldsFilter.getValue(), dtFilter1.getValue(), dtFilter2.getValue(), cbxContractManufacturer.getValue(), cbxExpFieldFilter.getValue(), txtExpFieldFilter.getValue()));
		 viewChartModalWindow.setWidth(90, Unit.PERCENTAGE);
		 viewChartModalWindow.setHeight(90, Unit.PERCENTAGE);
		 viewChartModalWindow.center();
		 viewChartModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				//filterExperimentDataResults();
			}
		});
		 this.getUI().addWindow(viewChartModalWindow);
    }
	
	private void exportExperimentDataReportToExcel()
	{
		/*
		if(tblTargetDataReport.getItemIds() != null)
		{
			System.out.println("Starting export: " + new Date());
			
			ExcelExport xlsExport = new ExcelExport(tblTargetDataReport, new XSSFWorkbook(), targetRpt.getTargetReportName().trim(), null, targetRpt.getTargetReportName().trim() + ".xlsx", false);
			xlsExport.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			xlsExport.setUseTableFormatPropertyValue(false);
			xlsExport.export();
			
			System.out.println("Finishing export: " + new Date());			
		}
		*/
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
                 }, lblTargetRptTitle.getValue().trim() + ".csv"));
         downloader.extend(downloadBtn);
		
	}
}
