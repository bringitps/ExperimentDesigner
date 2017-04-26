package com.bringit.experiment.ui.form;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.ui.design.ExperimentDataReportDesign;
import com.bringit.experiment.util.Config;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.data.util.sqlcontainer.query.generator.MSSQLGenerator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;

import com.vaadin.addon.tableexport.ExcelExport;

//import com.vaadin.addon.tableexport.TableExport;

public class ExperimentDataReportForm extends ExperimentDataReportDesign{

	Experiment experiment = new Experiment();
	List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
	int selectedRecordId = -1;
	private SQLContainer vaadinTblContainer;
    
	public ExperimentDataReportForm(int experimentId)
	{
		experiment = new ExperimentDao().getExperimentById(experimentId);
		experimentFields = new ExperimentFieldDao().getActiveExperimentFields(experiment);
		
		this.lblExperimentTitle.setValue(" - " + experiment.getExpName()); // Attach RPT Table last updated date 
		
		//Add the button "Refresh Data Now" to run SP and get data refreshed 
		//If this experiment data report is being refreshed hide "Refresh Data Now" button
		
		bindExperimentRptTable();


		//To do:
		//Include Container Filters to Table according to CM Restrictions
		//1) Get Role of Session
		//SysRole sysRoleSession = (SysRole)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");
		//2) Get CmNames String array 
		//3) Set static filter to data loaded
		//1 Container Filter by 1 CmName
		//Equal Operator needs to be used vaadinTblContainer.addContainerFilter(new Compare.Equal(this.cbxDateFieldsFilter.getValue(), dateFilterValue1));
		
		
		cbxExperimentDataReportFilters.setContainerDataSource(null);
		cbxDateFieldsFilter.setContainerDataSource(null);
		
		for(int i=0; experimentFields !=null && i<experimentFields.size(); i++)
		{
			if(experimentFields.get(i).getExpFieldType().startsWith("varchar") || experimentFields.get(i).getExpFieldType().startsWith("char")
					|| experimentFields.get(i).getExpFieldType().startsWith("text") ||experimentFields.get(i).getExpFieldType().startsWith("nvarchar") 
					|| experimentFields.get(i).getExpFieldType().startsWith("nchar") || experimentFields.get(i).getExpFieldType().startsWith("ntext"))
			{
				cbxExperimentDataReportFilters.addItem(experimentFields.get(i).getExpDbFieldNameId());
				cbxExperimentDataReportFilters.setItemCaption(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());

				if(cbxExperimentDataReportFilters.size() == 1)
					cbxExperimentDataReportFilters.select(experimentFields.get(i).getExpDbFieldNameId());
			}
			else if(experimentFields.get(i).getExpFieldType().contains("date"))
			{
				cbxDateFieldsFilter.addItem(experimentFields.get(i).getExpDbFieldNameId());
				cbxDateFieldsFilter.setItemCaption(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());

				if(cbxDateFieldsFilter.size() == 1)
					cbxDateFieldsFilter.select(experimentFields.get(i).getExpDbFieldNameId());				
			}
		}
		
		cbxDateFieldsFilter.addItem("CreatedDate");
		cbxDateFieldsFilter.setItemCaption("CreatedDate", "Created Date");

		if(cbxDateFieldsFilter.size() == 1)
			cbxDateFieldsFilter.select("CreatedDate");				
	
		cbxDateFieldsFilter.addItem("LastModifiedDate");
		cbxDateFieldsFilter.setItemCaption("LastModifiedDate", "Last Modified Date");
		
		/*String sqlSelectQuery =  ExperimentUtil.buildSqlSelectQueryByExperiment(experiment, experimentFields);
		ResultSet experimentDataResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectQuery);
		if(experimentDataResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblExperimentDataReport, experimentDataResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxExperimentDataReportFilters, experimentDataResults);
			VaadinControls.bindDbViewDateFiltersToVaadinComboBox(cbxDateFieldsFilter, experimentDataResults);
		}
		*/
		
		if(cbxDateFieldsFilter.getItemIds().size() <= 0)
			gridDateFilters.setVisible(false);
		else
			fillCbxDateFilterOperators();
		
		enableDateFilterComponents(false);
		
		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterExperimentDataResults();
			}
			});
		
		cbxDateFilterOperators.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbxDateFilterOperators.getValue()!=null && cbxDateFilterOperators.getValue().equals("between"))
					showFilter2(true);
				else
					showFilter2(false);
			}   
	    });
		
		btnOkDateFilters.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				filterExperimentDataResults();
			}

		});
		
		chxDateFilters.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(chxDateFilters.getValue()!=null && chxDateFilters.getValue())
					enableDateFilterComponents(true);
				else
					enableDateFilterComponents(false);
			}   
	    });
		
		btnExportExcel.addClickListener(new Button.ClickListener() {
	
			@Override
			public void buttonClick(ClickEvent event) {
				exportExperimentDataReportToExcel();
			}
		
		});
	
		btnViewRecordHistory.addClickListener(new Button.ClickListener() {
	
			@Override
			public void buttonClick(ClickEvent event) {
				viewSelectedRecordHistory();
			}
		
		});
		
		tblExperimentDataReport.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
            	selectedRecordId = Integer.parseInt(event.getItemId().toString());
                if (event.isDoubleClick())
                	openDataViewRecordCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
                else 
                	tblExperimentDataReport.select(event.getItemId());
            }
        });
		
	}
	
	private void filterExperimentDataResults()
	{
		//System.out.println("Filtering Data...");
		vaadinTblContainer.removeAllContainerFilters();
		
		if(this.chxDateFilters.getValue() && this.cbxDateFieldsFilter.getValue() != null && this.cbxDateFilterOperators.getValue() != null
				&& this.dtFilter1.getValue() != null)
		{
			if(this.cbxDateFilterOperators.getValue().equals("between") && this.dtFilter2.getValue() == null)
			{
				this.getUI().showNotification("From Date and To Date should be set.", Type.WARNING_MESSAGE);
				return;
			}
			
			Date dateFilterValue1 = this.dtFilter1.getValue();
			Date dateFilterValue2 = this.dtFilter2.getValue();

			if(this.cbxDateFilterOperators.getValue().equals("between"))
			{
				dateFilterValue2.setHours(23);
				dateFilterValue2.setMinutes(59);
				dateFilterValue2.setSeconds(59);
				vaadinTblContainer.addContainerFilter(new Between(this.cbxDateFieldsFilter.getValue(), dateFilterValue1, dateFilterValue2)); 	
			}
			else
			{
				String sqlDateFilterOperator = "";
				
				 switch (this.cbxDateFilterOperators.getValue().toString().trim()) 
				 {
		         	case "on":
						vaadinTblContainer.addContainerFilter(new Compare.Equal(this.cbxDateFieldsFilter.getValue(), dateFilterValue1));
						break;
		         	case "before":
						vaadinTblContainer.addContainerFilter(new Compare.Less(this.cbxDateFieldsFilter.getValue(), dateFilterValue1));
						break;
		         	case "after":  
		         		dateFilterValue1.setHours(23);
		         		dateFilterValue1.setMinutes(59);
		         		dateFilterValue1.setSeconds(59);
		         		vaadinTblContainer.addContainerFilter(new Compare.Greater(this.cbxDateFieldsFilter.getValue(), dateFilterValue1));
						break;
		         	case "onorbefore":  
		         		dateFilterValue1.setHours(23);
		         		dateFilterValue1.setMinutes(59);
		         		dateFilterValue1.setSeconds(59);
		         		vaadinTblContainer.addContainerFilter(new Compare.LessOrEqual(this.cbxDateFieldsFilter.getValue(), dateFilterValue1));
						break;
		         	case "onorafter":  
		         		vaadinTblContainer.addContainerFilter(new Compare.GreaterOrEqual(this.cbxDateFieldsFilter.getValue(), dateFilterValue1));
						break;
				 }
			}
		}
		
		if(this.cbxExperimentDataReportFilters.getValue() != null )
		{
			Like like = new Like(this.cbxExperimentDataReportFilters.getValue(), "%" + this.txtSearch.getValue().trim() + "%");
			vaadinTblContainer.addContainerFilter(like); 	
		}
		
        /*
		if(this.cbxDateFieldsFilter.getValue() != null && this.cbxDateFilterOperators.getValue() != null
				&& this.dtFilter1.getValue() != null)
		{
			if(this.cbxDateFilterOperators.getValue().equals("between") && this.dtFilter2.getValue() == null)
			{
				this.getUI().showNotification("From Date and To Date should be set.", Type.WARNING_MESSAGE);
				return;
			}
			
			ResultSet experimentDataResults = null;
			String sqlSelectQuery = null;
			
			if(this.cbxExperimentDataReportFilters.getValue() != null)
				sqlSelectQuery = ExperimentUtil.buildDateFilteredSqlSelectQueryByExperiment(experiment, experimentFields, (String)this.cbxExperimentDataReportFilters.getValue(), 
						this.txtSearch.getValue(), (String)this.cbxDateFieldsFilter.getValue(), (String)this.cbxDateFilterOperators.getValue(), this.dtFilter1.getValue(), this.dtFilter2.getValue());
			else
				sqlSelectQuery = ExperimentUtil.buildDateFilteredSqlSelectQueryByExperiment(experiment, experimentFields, null, null, 
						 (String)this.cbxDateFieldsFilter.getValue(), (String)this.cbxDateFilterOperators.getValue(), this.dtFilter1.getValue(), this.dtFilter2.getValue());
			//this.txtSearch.setValue(sqlSelectQuery);
			experimentDataResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectQuery);
			
			if(experimentDataResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblExperimentDataReport, experimentDataResults, 1);
		}
		else if(this.cbxExperimentDataReportFilters.getValue() != null )
		{
			String sqlSelectQuery =  ExperimentUtil.buildFilteredSqlSelectQueryByExperiment(experiment, experimentFields, (String)this.cbxExperimentDataReportFilters.getValue(), this.txtSearch.getValue());
			ResultSet experimentDataResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectQuery);
			
			if(experimentDataResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblExperimentDataReport, experimentDataResults, 1);
		}
		*/
	}
	
	private void fillCbxDateFilterOperators()
	{
		cbxDateFilterOperators.setContainerDataSource(null);
		
		cbxDateFilterOperators.addItem("on");
		cbxDateFilterOperators.setItemCaption("on", "on");
		
		cbxDateFilterOperators.addItem("onorbefore");
		cbxDateFilterOperators.setItemCaption("onorbefore", "on or before");

		cbxDateFilterOperators.addItem("onorafter");
		cbxDateFilterOperators.setItemCaption("onorafter", "on or after");

		cbxDateFilterOperators.addItem("before");
		cbxDateFilterOperators.setItemCaption("before", "before");

		cbxDateFilterOperators.addItem("after");
		cbxDateFilterOperators.setItemCaption("after", "after");
		
		cbxDateFilterOperators.addItem("between");
		cbxDateFilterOperators.setItemCaption("between", "is between");

		cbxDateFilterOperators.select("between");
	}

	private void showFilter2(boolean visible)
	{
		dtFilter2.setValue(null);
		dtFilter2.setVisible(visible);
	}
	
	private void enableDateFilterComponents(boolean enabled)
	{
		this.cbxDateFieldsFilter.setEnabled(enabled);
		this.cbxDateFilterOperators.setEnabled(enabled);
		this.dtFilter1.setEnabled(enabled);
		this.dtFilter2.setEnabled(enabled);
		this.btnOkDateFilters.setEnabled(enabled);
	}
	
	private void exportExperimentDataReportToExcel()
	{
		if(this.tblExperimentDataReport.getItemIds() != null)
		{
			System.out.println("Starting export: " + new Date());
			
			ExcelExport xlsExport = new ExcelExport(tblExperimentDataReport, new XSSFWorkbook(), lblExperimentTitle.getValue().trim(), null, lblExperimentTitle.getValue().trim() + ".xlsx", false);
			xlsExport.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			xlsExport.setUseTableFormatPropertyValue(false);
			xlsExport.export();
			System.out.println("Finishing export: " + new Date());
			
		}
	}
	
	private void openDataViewRecordCRUDModalWindow(int experimentRecordId)
	{
		 Window dataViewRecordCRUDModalWindow = new Window("View Record - " + this.experiment.getExpName());
		 dataViewRecordCRUDModalWindow.setModal(true);
		 dataViewRecordCRUDModalWindow.setResizable(false);
		 dataViewRecordCRUDModalWindow.setContent(new ExperimentDataViewRecordForm(this.experiment, this.experimentFields, experimentRecordId));
		 dataViewRecordCRUDModalWindow.setWidth(993, Unit.PIXELS);
		 dataViewRecordCRUDModalWindow.setHeight(660, Unit.PIXELS);
		 dataViewRecordCRUDModalWindow.center();
		 dataViewRecordCRUDModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				filterExperimentDataResults();
			}
		});
		 this.getUI().addWindow(dataViewRecordCRUDModalWindow);
    }
	
	private void viewSelectedRecordHistory()
	{
		if(this.selectedRecordId == -1)
			return;
		
		Window dataViewRecordChangesHistoryModalWindow = new Window("View Record History - " + this.experiment.getExpName());
		dataViewRecordChangesHistoryModalWindow.setModal(true);
		dataViewRecordChangesHistoryModalWindow.setResizable(false);
		dataViewRecordChangesHistoryModalWindow.setContent(new ExperimentRecordChangesHistoryForm(this.experiment, this.selectedRecordId));
		dataViewRecordChangesHistoryModalWindow.setWidth(940, Unit.PIXELS);
		dataViewRecordChangesHistoryModalWindow.setHeight(480, Unit.PIXELS);
		dataViewRecordChangesHistoryModalWindow.center();
		this.getUI().addWindow(dataViewRecordChangesHistoryModalWindow);
	
	}
	
	private void bindExperimentRptTable()
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
		    
		    	TableQuery tblQuery = new TableQuery(experiment.getExpDbRptTableNameId(), connectionPool, new MSSQLGenerator());
				tblQuery.setVersionColumn("RecordId");
				
				vaadinTblContainer = new SQLContainer(tblQuery);
				
				tblExperimentDataReport.setContainerDataSource(vaadinTblContainer);
				
				if(experimentFields!= null)
				{
					String[] expFieldDbId = new String[experimentFields.size()+3];
					
					expFieldDbId[0] = "RecordId";
					tblExperimentDataReport.setColumnHeader("RecordId", "Id");
					tblExperimentDataReport.setConverter("RecordId", new StringToDoubleConverter() {
					    @Override
					    protected NumberFormat getFormat(Locale locale) {
					    	NumberFormat format = NumberFormat.getNumberInstance();
					    	format.setGroupingUsed(false);
					    	return format;
					    }
					});
					
					
					for(int i=0; i<experimentFields.size(); i++)
					{
						expFieldDbId[i+1] = experimentFields.get(i).getExpDbFieldNameId();
						tblExperimentDataReport.setColumnHeader(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());
					
						
						if(experimentFields.get(i).getExpFieldType().toLowerCase().contains("float") || 
								experimentFields.get(i).getExpFieldType().toLowerCase().contains("decimal") || 
								experimentFields.get(i).getExpFieldType().toLowerCase().contains("int"))
						{
							tblExperimentDataReport.setConverter(experimentFields.get(i).getExpDbFieldNameId(), new StringToDoubleConverter() {
							    @Override
							    protected NumberFormat getFormat(Locale locale) {
							    	NumberFormat format = NumberFormat.getNumberInstance();
							    	format.setGroupingUsed(false);
							    	return format;
							    }
							});
							
						}
						
						if(experimentFields.get(i).getExpFieldType().toLowerCase().contains("date"))
						{
							tblExperimentDataReport.setConverter(experimentFields.get(i).getExpDbFieldNameId(), new StringToDateConverter() {
							    @Override
							    protected DateFormat getFormat(Locale locale) {
									return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    }
							});
						}
					}

					expFieldDbId[expFieldDbId.length-2] = "CreatedDate";
					expFieldDbId[expFieldDbId.length-1] = "LastModifiedDate";
						    
					tblExperimentDataReport.setConverter("CreatedDate", new StringToDateConverter() {
						 @Override
						    protected DateFormat getFormat(Locale locale) {
								return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    }
					});

					tblExperimentDataReport.setConverter("LastModifiedDate", new StringToDateConverter() {
						 @Override
						    protected DateFormat getFormat(Locale locale) {
								return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    }
					});
					
					
					tblExperimentDataReport.setVisibleColumns(expFieldDbId);
				}
				
		    } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		    
		}
	}

}