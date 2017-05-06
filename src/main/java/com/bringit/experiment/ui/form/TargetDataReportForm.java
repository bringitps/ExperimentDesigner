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
import com.bringit.experiment.dao.TargetColumnDao;
import com.bringit.experiment.dao.TargetColumnGroupDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.dao.TargetReportJobDataDao;
import com.bringit.experiment.ui.design.TargetDataReportDesign;
import com.bringit.experiment.util.Config;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.data.util.sqlcontainer.query.generator.MSSQLGenerator;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TargetDataReportForm extends TargetDataReportDesign{

	private TargetReport targetRpt = new TargetReport();
	Experiment experiment = new Experiment();
	List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
	List<ContractManufacturer> contractManufacturers = new ArrayList<ContractManufacturer>();
	private SQLContainer vaadinTblContainer;
	SysRole sysRoleSession = (SysRole) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");

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
    				dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Result" );
        			dbRptTableTypes.add("varchar(20)");    				
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
		
		cbxDateFieldsFilter.addItem("CreatedDate");
		cbxDateFieldsFilter.setItemCaption("CreatedDate", "Created Date");

		if(cbxDateFieldsFilter.size() == 1)
			cbxDateFieldsFilter.select("CreatedDate");				
	
		cbxDateFieldsFilter.addItem("LastModifiedDate");
		cbxDateFieldsFilter.setItemCaption("LastModifiedDate", "Last Modified Date");

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
		List<Compare.Equal> filterList= new ArrayList<>();
		if (!"sys_admin".equalsIgnoreCase(sysRoleSession.getRoleName())) {
			List<ContractManufacturer> contractManufacturersFilter = new CmForSysRoleDao().getListOfCmForBysysRoleId(sysRoleSession.getRoleId());
			for (ContractManufacturer con : contractManufacturersFilter) {
				filterList.add(new Compare.Equal("CmName", con.getCmName()));

			}
			vaadinTblContainer.addContainerFilter(new Or(filterList.toArray(new Compare.Equal[filterList.size()])));

		}


		this.btnApplyFilters.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
		
				//System.out.println("Filtering Data...");
				vaadinTblContainer.removeAllContainerFilters();
				
				if(cbxDateFieldsFilter.getValue() != null && dtFilter1.getValue() != null && dtFilter2.getValue() != null)
				{
					Date dateFilterValue1 = dtFilter1.getValue();
					Date dateFilterValue2 = dtFilter2.getValue();

					dateFilterValue2.setHours(23);
					dateFilterValue2.setMinutes(59);
					dateFilterValue2.setSeconds(59);
					vaadinTblContainer.addContainerFilter(new Between(cbxDateFieldsFilter.getValue(), dateFilterValue1, dateFilterValue2)); 	
					
				}
				
				if(cbxExpFieldFilter.getValue() != null )
					vaadinTblContainer.addContainerFilter(new Like(cbxExpFieldFilter.getValue(), "%" + txtExpFieldFilter.getValue().trim() + "%")); 	
				
				if(cbxContractManufacturer.getValue() != null )
					vaadinTblContainer.addContainerFilter(new Compare.Equal("CmName",cbxContractManufacturer.getValue()));

				List<Compare.Equal> filterList= new ArrayList<>();
				if (!"sys_admin".equalsIgnoreCase(sysRoleSession.getRoleName())) {
					List<ContractManufacturer> contractManufacturersFilter = new CmForSysRoleDao().getListOfCmForBysysRoleId(sysRoleSession.getRoleId());
					for (ContractManufacturer con : contractManufacturersFilter) {
						filterList.add(new Compare.Equal("CmName", con.getCmName()));

					}
					vaadinTblContainer.addContainerFilter(new Or(filterList.toArray(new Compare.Equal[filterList.size()])));
				}
			}
		});
		
		
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
		experimentJobDataDao.targetProcedureJob(this.targetRpt.getTargetReportId());
		vaadinTblContainer.refresh();
		lblLastRefreshDate.setValue("Last Refresh Date: " + targetRpt.getTargetReportDbRptTableLastUpdate());
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
		    				dbRptTableCols.add(targetRptCols.get(j).getTargetColumnLabel().replaceAll(" ", "_") + "_Result" );
		        			dbRptTableTypes.add("varchar(20)");    				
		    			}
		    		}
		    	}
		    	
				dbRptTableCols.add("Result");
				dbRptTableTypes.add("varchar(20)");
				
				
				for(int i=0; i<dbRptTableCols.size(); i++)
				{
					tblTargetDataReport.setColumnHeader(dbRptTableCols.get(i), dbRptTableCols.get(i).replaceAll("_", " "));
					
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
				
				
		    } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		    
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
		if(tblTargetDataReport.getItemIds() != null)
		{
			System.out.println("Starting export: " + new Date());
			
			ExcelExport xlsExport = new ExcelExport(tblTargetDataReport, new XSSFWorkbook(), targetRpt.getTargetReportName().trim(), null, targetRpt.getTargetReportName().trim() + ".xlsx", false);
			xlsExport.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			xlsExport.setUseTableFormatPropertyValue(false);
			xlsExport.export();
			
			System.out.println("Finishing export: " + new Date());			
		}
	}
}
