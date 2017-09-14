package com.bringit.experiment.ui.form;

import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.CmForSysRoleDao;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.ExperimentJobDataDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.ui.design.ExperimentDataReportDesign;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.Constants;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.data.util.sqlcontainer.query.generator.MSSQLGenerator;
import com.vaadin.data.util.sqlcontainer.query.generator.StatementHelper;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

//import com.vaadin.addon.tableexport.TableExport;

public class ExperimentDataReportForm extends ExperimentDataReportDesign {

    Experiment experiment = new Experiment();
    List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
    List<String> experimentFieldDbIdXRef = new ArrayList<String>();
    List<String> experimentFieldTypeXRef = new ArrayList<String>();
    
    int selectedRecordId = -1;
    private SQLContainer vaadinTblContainer;
    SysRole sysRoleSession = (SysRole) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");

    Integer filterCnt = 1;
    boolean filtersApplied = false;
    

    private SystemSettings systemSettings;

    
    public ExperimentDataReportForm(int experimentId) {
    	
		//Rename entities
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
    	
        experiment = new ExperimentDao().getExperimentById(experimentId);
        experimentFields = new ExperimentFieldDao().getActiveExperimentFields(experiment);

        this.lblExperimentTitle.setValue(" - " + experiment.getExpName()); // Attach RPT Table last updated date

        if (experiment.getExpDbRptTableLastUpdate() != null)
            this.lblrefreshDate.setValue("Last Refresh Date: " + experiment.getExpDbRptTableLastUpdate());

        
        //Fill Filter Comboboxes
        fillCbxExpressions(this.cbxExpression1);
        fillCbxStringFilterOperators(this.cbxFilterOperator1);
        
        
        //Add the button "Refresh Data Now" to run SP and get data refreshed
        //If this experiment data report is being refreshed hide "Refresh Data Now" button

        btnRefreshButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                refreshData();
            }

        });


        bindExperimentRptTable();

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
	            List<ContractManufacturer> contractManufacturers = new CmForSysRoleDao().getListOfCmForBysysRoleId(sysRoleSession.getRoleId());
	                for (ContractManufacturer con : contractManufacturers) {
		                filterList.add(new Compare.Equal("CmName", con.getCmName()));
		            }
		            vaadinTblContainer.addContainerFilter(new Or(filterList.toArray(new Compare.Equal[filterList.size()])));
	        }
        }
        
        for (int i = 0; experimentFields != null && i < experimentFields.size(); i++) 
        {
        	experimentFieldDbIdXRef.add(experimentFields.get(i).getExpDbFieldNameId());
        	experimentFieldTypeXRef.add(experimentFields.get(i).getExpFieldType());
        }

        experimentFieldDbIdXRef.add("CreatedDate");
        experimentFieldTypeXRef.add("datetime");
        
        experimentFieldDbIdXRef.add("LastModifiedDate");
        experimentFieldTypeXRef.add("datetime");
        
        fillCbxExperimentFields(cbxExperimentField1);
    	
        
		/*String sqlSelectQuery =  ExperimentUtil.buildSqlSelectQueryByExperiment(experiment, experimentFields);
		ResultSet experimentDataResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectQuery);
		if(experimentDataResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblExperimentDataReport, experimentDataResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxExperimentDataReportFilters, experimentDataResults);
			VaadinControls.bindDbViewDateFiltersToVaadinComboBox(cbxDateFieldsFilter, experimentDataResults);
		}
		*/

       
        //enableDateFilterComponents(false);


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

        tblExperimentDataReport.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                selectedRecordId = Integer.parseInt(event.getItemId().toString());
                if (event.isDoubleClick())
                    openDataViewRecordCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
                else
                    tblExperimentDataReport.select(event.getItemId());
            }
        });

        
        btnAddFilters.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	attachNewFilterRow();
            }

        });


        btnDeleteFilterRow1.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	removeFilterRow(event.getButton());
            }

        });
        
        cbxExperimentField1.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	updateFilterSearchField(cbxExperimentField1);
            }
        });
        
        btnApplyFilters.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                applySelectedFilters();
            }

        });
        
    }

    private void applySelectedFilters()
    {
    	String filterExpression = null;
        
    	List<Filter> orFilterList = new ArrayList<>();
    	List<Filter> andFilterList = new ArrayList<>();
    	
    	for(int i=0; i<multiFilterGrid.getRows(); i++)
    	{
    		TextField txtStringFilterField = null;
    		
    		DateField fromDateFilterField = null;
    		DateField toDateFilterField = null;
    		
    		ComboBox cbxExperimentField = (ComboBox)multiFilterGrid.getComponent(0, i);
    		if(cbxExperimentField.getValue() == null)
       		{
    	             this.getUI().showNotification("Invalid Filter Expression.", Type.WARNING_MESSAGE);
    	             filtersApplied = false;
    	             return;
       		}
    		
    		ComboBox cbxFilterOperatorField = (ComboBox)multiFilterGrid.getComponent(1, i);
    		if(cbxFilterOperatorField.getValue() == null)
       		{
    	             this.getUI().showNotification("Invalid Filter Expression.", Type.WARNING_MESSAGE);
    	             filtersApplied = false;
    	             return;
       		}
    		
    		Integer experimentFieldTypeRefIndex = experimentFieldDbIdXRef.indexOf(cbxExperimentField.getValue());
    		
    		if(experimentFieldTypeXRef.get(experimentFieldTypeRefIndex).contains("date"))
    		{
    			HorizontalLayout dateFieldsLayout = (HorizontalLayout)multiFilterGrid.getComponent(2, i);
    			fromDateFilterField = (DateField)dateFieldsLayout.getComponent(0);
    			toDateFilterField = (DateField)dateFieldsLayout.getComponent(1);
    			
   			 	if ("between".equals(cbxFilterOperatorField.getValue()) && toDateFilterField == null) 
   			 	{
   	                this.getUI().showNotification("From Date and To Date should be set.", Type.WARNING_MESSAGE);
   	                filtersApplied = false;
   	                return;
   	            }    			
    		}
    		else
    			txtStringFilterField = (TextField)multiFilterGrid.getComponent(2, i);
    		
    		ComboBox cbxExpressionField = (ComboBox)multiFilterGrid.getComponent(3, i);

    		if((i+1) < multiFilterGrid.getRows() && cbxExpressionField.getValue() == null)
    		{
	             this.getUI().showNotification("Invalid Filter Expression.", Type.WARNING_MESSAGE);
	             filtersApplied = false;
	             return;
    		}
    		
			if(!experimentFieldTypeXRef.get(experimentFieldTypeRefIndex).contains("date"))
    		{
    			switch (cbxFilterOperatorField.getValue().toString().trim()) {
                case "contains":
                   	if("and".equals(filterExpression))
                   		andFilterList.add(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%"));//vaadinTblContainer.addContainerFilter(new And(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%")));
                	else
                		orFilterList.add(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%"));//vaadinTblContainer.addContainerFilter(new Or(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%")));
                    break;
                case "doesnotcontain": 
                	if("and".equals(filterExpression))
                		andFilterList.add(new Not(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%")));//vaadinTblContainer.addContainerFilter(new And(new Not(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%"))));
                	else
                		orFilterList.add(new Not(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%")));//vaadinTblContainer.addContainerFilter(new Or(new Not(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%"))));
                	break;
                case "doesnotstartwith":
            		if("and".equals(filterExpression))
            			andFilterList.add(new Not(new Like(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim() + "%")));//vaadinTblContainer.addContainerFilter(new And(new Not(new Like(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim() + "%"))));
                	else
                		orFilterList.add(new Not(new Like(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim() + "%")));//vaadinTblContainer.addContainerFilter(new Or(new Not(new Like(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim() + "%"))));
                	break;
                case "endswith":
              		if("and".equals(filterExpression))
              			andFilterList.add(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim()));//vaadinTblContainer.addContainerFilter(new And(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim())));
                	else
                		orFilterList.add(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim()));//vaadinTblContainer.addContainerFilter(new Or(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim())));
                    break;
                case "is":
            		if("and".equals(filterExpression))
            			andFilterList.add(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim()));//vaadinTblContainer.addContainerFilter(new And(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim())));
                	else
                		orFilterList.add(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim()));//orFilters.add(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim()));
                	break;
                case "isempty":
                	if("and".equals(filterExpression))
                		andFilterList.add(new IsNull(cbxExperimentField.getValue()));//vaadinTblContainer.addContainerFilter(new And(new IsNull(cbxExperimentField.getValue())));
                	else
                		orFilterList.add(new IsNull(cbxExperimentField.getValue()));//vaadinTblContainer.addContainerFilter(new Or(new IsNull(cbxExperimentField.getValue())));
                    break;
                case "isnot":
            		if("and".equals(filterExpression))
            			andFilterList.add(new Not(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim())));//vaadinTblContainer.addContainerFilter(new And(new Not(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim()))));
                	else
                		orFilterList.add(new Not(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim())));//vaadinTblContainer.addContainerFilter(new Or(new Not(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim()))));
                    break;
                case "isnotempty":
                	if("and".equals(filterExpression))
                		andFilterList.add(new Not(new IsNull(cbxExperimentField.getValue())));//vaadinTblContainer.addContainerFilter(new And(new Not(new IsNull(cbxExperimentField.getValue()))));
                	else
                		orFilterList.add(new Not(new IsNull(cbxExperimentField.getValue())));//vaadinTblContainer.addContainerFilter(new Or(new Not(new IsNull(cbxExperimentField.getValue()))));
                    break;
                case "startswith":
                   	if("and".equals(filterExpression))
                   		andFilterList.add(new Like(cbxExperimentField.getValue(),  txtStringFilterField.getValue().trim() + "%"));//vaadinTblContainer.addContainerFilter(new And(new Like(cbxExperimentField.getValue(),  txtStringFilterField.getValue().trim() + "%")));
                	else
                		orFilterList.add(new Like(cbxExperimentField.getValue(),  txtStringFilterField.getValue().trim() + "%"));//vaadinTblContainer.addContainerFilter(new Or(new Like(cbxExperimentField.getValue(),  txtStringFilterField.getValue().trim() + "%")));
                	break;
    			}
    		}
    		else
    		{
    			Date dateFilterValue1 = fromDateFilterField.getValue();
                Date dateFilterValue2 = toDateFilterField.getValue();

                if (cbxFilterOperatorField.getValue().equals("between")) {
                    dateFilterValue2.setHours(23);
                    dateFilterValue2.setMinutes(59);
                    dateFilterValue2.setSeconds(59);
                    if("and".equals(filterExpression))
                    	andFilterList.add(new Between(cbxExperimentField.getValue(), dateFilterValue1, dateFilterValue2));//vaadinTblContainer.addContainerFilter(new And(new Between(cbxExperimentField.getValue(), dateFilterValue1, dateFilterValue2)));
                    else
                    	orFilterList.add(new Between(cbxExperimentField.getValue(), dateFilterValue1, dateFilterValue2));//vaadinTblContainer.addContainerFilter(new Or(new Between(cbxExperimentField.getValue(), dateFilterValue1, dateFilterValue2)));
                } else {
                    String sqlDateFilterOperator = "";

                    switch (cbxFilterOperatorField.getValue().toString().trim()) {
                        case "on":
                           	if("and".equals(filterExpression))
                           		andFilterList.add(new Compare.Equal(cbxExperimentField.getValue(), dateFilterValue1));//vaadinTblContainer.addContainerFilter(new And(new Compare.Equal(cbxExperimentField.getValue(), dateFilterValue1)));
                           	else
        	                	orFilterList.add(new Compare.Equal(cbxExperimentField.getValue(), dateFilterValue1));//vaadinTblContainer.addContainerFilter(new Or(new Compare.Equal(cbxExperimentField.getValue(), dateFilterValue1)));
        	                break;
                        case "before":
                         	if("and".equals(filterExpression))
                         		andFilterList.add(new Compare.Less(cbxExperimentField.getValue(), dateFilterValue1));//vaadinTblContainer.addContainerFilter(new And(new Compare.Less(cbxExperimentField.getValue(), dateFilterValue1)));
    	                    else
    	                    	orFilterList.add(new Compare.Less(cbxExperimentField.getValue(), dateFilterValue1));//vaadinTblContainer.addContainerFilter(new Or(new Compare.Less(cbxExperimentField.getValue(), dateFilterValue1)));
    	                    break;
                        case "after":
                            dateFilterValue1.setHours(23);
                            dateFilterValue1.setMinutes(59);
                            dateFilterValue1.setSeconds(59);
                            if("and".equals(filterExpression))
                            	andFilterList.add(new Compare.Greater(cbxExperimentField.getValue(), dateFilterValue1));//vaadinTblContainer.addContainerFilter(new And(new Compare.Greater(cbxExperimentField.getValue(), dateFilterValue1)));
    	                    else
    	                    	orFilterList.add(new Compare.Greater(cbxExperimentField.getValue(), dateFilterValue1));//vaadinTblContainer.addContainerFilter(new Or(new Compare.Greater(cbxExperimentField.getValue(), dateFilterValue1)));
    	                    break;
                        case "onorbefore":
                            dateFilterValue1.setHours(23);
                            dateFilterValue1.setMinutes(59);
                            dateFilterValue1.setSeconds(59);
                            if("and".equals(filterExpression))
                            	andFilterList.add(new Compare.LessOrEqual(cbxExperimentField.getValue(), dateFilterValue1));//vaadinTblContainer.addContainerFilter(new And(new Compare.LessOrEqual(cbxExperimentField.getValue(), dateFilterValue1)));
    	                    else
    	                    	orFilterList.add(new Compare.LessOrEqual(cbxExperimentField.getValue(), dateFilterValue1));//vaadinTblContainer.addContainerFilter(new Or(new Compare.LessOrEqual(cbxExperimentField.getValue(), dateFilterValue1)));
    	                    break;
                        case "onorafter":
                            if("and".equals(filterExpression))
                            	andFilterList.add(new Compare.GreaterOrEqual(cbxExperimentField.getValue(), dateFilterValue1));//vaadinTblContainer.addContainerFilter(new And(new Compare.GreaterOrEqual(cbxExperimentField.getValue(), dateFilterValue1)));
    	                    else
    	                    	orFilterList.add(new Compare.GreaterOrEqual(cbxExperimentField.getValue(), dateFilterValue1));//vaadinTblContainer.addContainerFilter(new Or(new Compare.GreaterOrEqual(cbxExperimentField.getValue(), dateFilterValue1)));
    	                    break;
                    }
                }	    			
    		}	    		
		
			if(cbxExpressionField.getValue() != null)
				filterExpression = cbxExpressionField.getValue().toString();
			else
				filterExpression = "and";
    	}
    	
    	 //If there is no Contract Manufacturer loaded into system, there should not have restrictions
        List<ContractManufacturer> allContractManufacturersLoaded = new ContractManufacturerDao().getAllContractManufacturers();
        if(allContractManufacturersLoaded != null && allContractManufacturersLoaded.size() >0)
        { 
	    	 if (!"sys_admin".equalsIgnoreCase(sysRoleSession.getRoleName())) {
	             List<ContractManufacturer> contractManufacturers = new CmForSysRoleDao().getListOfCmForBysysRoleId(sysRoleSession.getRoleId());
	             for (ContractManufacturer con : contractManufacturers) {
	            	 orFilterList.add(new Compare.Equal("CmName", con.getCmName()));
	             }
	         }
        }
        
     	vaadinTblContainer.removeAllContainerFilters();
     	
        if(orFilterList.size() > 0)
        	vaadinTblContainer.addContainerFilter(new Or(orFilterList.toArray(new Filter[orFilterList.size()])));
    	
    	if(andFilterList.size() > 0)
    		vaadinTblContainer.addContainerFilter(new And(andFilterList.toArray(new Filter[andFilterList.size()])));
    	
    	filtersApplied = true;        
    }
    
    private void attachNewFilterRow()
    {
    	Integer totalFilterRows = multiFilterGrid.getRows();
    	String newComponentsId = "" + (filterCnt + 1);
    	
    	multiFilterGrid.insertRow(totalFilterRows);
    	
    	ComboBox newCbxExperimentField = new ComboBox();
    	newCbxExperimentField.setId("cbxExperimentField" + newComponentsId);
    	newCbxExperimentField.setHeight(cbxExperimentField1.getHeight(), UNITS_PIXELS);
    	newCbxExperimentField.setWidth(cbxExperimentField1.getWidth(), UNITS_PIXELS);
    	newCbxExperimentField.setStyleName(cbxExperimentField1.getStyleName());
    	

    	newCbxExperimentField.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	updateFilterSearchField(newCbxExperimentField);
            }
        });
    	
    	fillCbxExperimentFields(newCbxExperimentField);
    	multiFilterGrid.addComponent(newCbxExperimentField, 0, totalFilterRows);
    	
    	ComboBox newCbxFilterOperatorField = new ComboBox();
    	newCbxFilterOperatorField.setId("cbxFilterOperator" + newComponentsId);
    	newCbxFilterOperatorField.setHeight(cbxFilterOperator1.getHeight(), UNITS_PIXELS);
    	newCbxFilterOperatorField.setWidth(cbxFilterOperator1.getWidth(), UNITS_PIXELS);
    	newCbxFilterOperatorField.setStyleName(cbxFilterOperator1.getStyleName());
    	fillCbxStringFilterOperators(newCbxFilterOperatorField);
    	multiFilterGrid.addComponent(newCbxFilterOperatorField, 1, totalFilterRows);
    	
    	TextField newTxtStringFilterField = new TextField();
    	newTxtStringFilterField.setId("txtStringFilter" + newComponentsId);
    	newTxtStringFilterField.setHeight(txtStringFilter1.getHeight(), UNITS_PIXELS);
    	newTxtStringFilterField.setWidth(txtStringFilter1.getWidth(), UNITS_PIXELS);
    	newTxtStringFilterField.setStyleName(txtStringFilter1.getStyleName());
    	multiFilterGrid.addComponent(newTxtStringFilterField, 2, totalFilterRows);
    	
    	ComboBox newCbxExpressionField = new ComboBox();
    	newCbxExpressionField.setId("cbxExpression" + newComponentsId);
    	newCbxExpressionField.setHeight(cbxExpression1.getHeight(), UNITS_PIXELS);
    	newCbxExpressionField.setWidth(cbxExpression1.getWidth(), UNITS_PIXELS);
    	newCbxExpressionField.setStyleName(cbxExpression1.getStyleName());
    	fillCbxExpressions(newCbxExpressionField);
    	multiFilterGrid.addComponent(newCbxExpressionField, 3, totalFilterRows);
    	
    	Button newBtnDeleteFilterRow = new Button();
    	newBtnDeleteFilterRow.setId("btnDeleteFilterRow" + newComponentsId);
    	newBtnDeleteFilterRow.setHeight(btnDeleteFilterRow1.getHeight(), UNITS_PIXELS);
    	newBtnDeleteFilterRow.setWidth(btnDeleteFilterRow1.getWidth(), UNITS_PIXELS);
    	newBtnDeleteFilterRow.setStyleName(btnDeleteFilterRow1.getStyleName());
    	newBtnDeleteFilterRow.setIcon(btnDeleteFilterRow1.getIcon());
    	newBtnDeleteFilterRow.setCaption(btnDeleteFilterRow1.getCaption());
    	
    	newBtnDeleteFilterRow.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	removeFilterRow(event.getButton());
            }

        });
    	
    	multiFilterGrid.addComponent(newBtnDeleteFilterRow, 4, totalFilterRows);
    	
    	if(totalFilterRows >= 1)
    		multiFilterGrid.setHeight(multiFilterGrid.getHeight() + 35, UNITS_PIXELS);
    	
    	filterCnt++;
    }
    
    private void removeFilterRow(Button clickedButton)
    {
    	if(multiFilterGrid.getRows() == 1)
    		return;
    	
    	Integer gridFilterRow = multiFilterGrid.getComponentArea(clickedButton).getRow1();    	
    	multiFilterGrid.removeRow(gridFilterRow);
    	
    	if(multiFilterGrid.getRows() > 1)
    		multiFilterGrid.setHeight(multiFilterGrid.getHeight() - 35, UNITS_PIXELS);
    }
    
	private void updateFilterSearchField(ComboBox cbxExperimentField)
	{
		Integer experimentFieldTypeRefIndex = experimentFieldDbIdXRef.indexOf(cbxExperimentField.getValue());
		
		if(experimentFieldTypeXRef.get(experimentFieldTypeRefIndex).contains("date"))
		{	
			Integer selectedGridFilterRow = multiFilterGrid.getComponentArea(cbxExperimentField).getRow1();
			multiFilterGrid.removeComponent(2, selectedGridFilterRow);
			
			ComboBox selectedCbxFilterOperatorField = (ComboBox)multiFilterGrid.getComponent(1, selectedGridFilterRow);
			selectedCbxFilterOperatorField.setContainerDataSource(null);
			fillCbxDateFilterOperators(selectedCbxFilterOperatorField);
			
			HorizontalLayout dateFilterFieldLayout = new HorizontalLayout();
			dateFilterFieldLayout.setWidth(195, Sizeable.UNITS_PIXELS);
			
	    	DateField fromDateField = new DateField();
	    	fromDateField.setWidth(95, Sizeable.UNITS_PIXELS);
	    	fromDateField.setHeight(25, Sizeable.UNITS_PIXELS);
	    	fromDateField.setStyleName(txtStringFilter1.getStyleName());
	    	dateFilterFieldLayout.addComponent(fromDateField);
	    	
	    	DateField toDateField = new DateField();
	    	toDateField.setWidth(95, Sizeable.UNITS_PIXELS);
	    	toDateField.setHeight(25, Sizeable.UNITS_PIXELS);
	    	toDateField.setStyleName(txtStringFilter1.getStyleName());
	    	dateFilterFieldLayout.addComponent(toDateField);
	    
	    	multiFilterGrid.addComponent(dateFilterFieldLayout, 2, selectedGridFilterRow);
			
		}
		else
		{
			Integer selectedGridFilterRow = multiFilterGrid.getComponentArea(cbxExperimentField).getRow1();
			multiFilterGrid.removeComponent(2, selectedGridFilterRow);
			
			ComboBox selectedCbxFilterOperatorField = (ComboBox)multiFilterGrid.getComponent(1, selectedGridFilterRow);
			selectedCbxFilterOperatorField.setContainerDataSource(null);
			fillCbxStringFilterOperators(selectedCbxFilterOperatorField);
			
			TextField newTxtStringFilterField = new TextField();
	    	newTxtStringFilterField.setHeight(txtStringFilter1.getHeight(), UNITS_PIXELS);
	    	newTxtStringFilterField.setWidth(txtStringFilter1.getWidth(), UNITS_PIXELS);
	    	newTxtStringFilterField.setStyleName(txtStringFilter1.getStyleName());
	    	multiFilterGrid.addComponent(newTxtStringFilterField, 2, selectedGridFilterRow);
	    	
		}
		
	}
    
    private void refreshData() {
        ExperimentJobDataDao experimentJobDataDao = new ExperimentJobDataDao();
        Map<String, Object> result = experimentJobDataDao.experimentProcedureJob(this.experiment.getExpId());
        vaadinTblContainer.refresh();
        experiment = new ExperimentDao().getExperimentById(experiment.getExpId());
        
        this.lblrefreshDate.setValue("Last Refresh Date: " + experiment.getExpDbRptTableLastUpdate());

        Integer totalRecords = 1 + tblExperimentDataReport.size();
        this.lblrefreshDate.setValue(this.lblrefreshDate.getValue() + "  [Total Records: " + totalRecords + "]"); 
        
        if (Constants.SUCCESS == result.get("status")) {
            this.getUI().showNotification(this.systemSettings.getExperimentLabel() + " '" + experiment.getExpName() + "' has been Refresh Successfully.", Notification.Type.HUMANIZED_MESSAGE);
        } else {
            String msgToDisplay = result.get("statusMessage").toString();
            if (Constants.JOB_NOT_EXECUTED.equalsIgnoreCase(msgToDisplay)) {
                msgToDisplay = "same experiment is getting refresh by another user";
            }
            this.getUI().showNotification(this.systemSettings.getExperimentLabel() + " '" + experiment.getExpName() + "' can't refresh due to "+ msgToDisplay + ".", Notification.Type.WARNING_MESSAGE);
        }
    }

    private void fillCbxExpressions(ComboBox cbxExpressions)
    {
    	cbxExpressions.setContainerDataSource(null);

    	cbxExpressions.addItem("and");
    	cbxExpressions.setItemCaption("and", "and");

    	cbxExpressions.addItem("or");
    	cbxExpressions.setItemCaption("or", "or");   	
    	
    	cbxExpressions.select("and");
    }    
    
    private void fillCbxDateFilterOperators(ComboBox cbxStringFilterOperators) 
    {
    	cbxStringFilterOperators.setContainerDataSource(null);

    	cbxStringFilterOperators.addItem("after");
    	cbxStringFilterOperators.setItemCaption("after", "after");

    	cbxStringFilterOperators.addItem("before");
    	cbxStringFilterOperators.setItemCaption("before", "before");
    	
    	cbxStringFilterOperators.addItem("between");
    	cbxStringFilterOperators.setItemCaption("between", "is between");
    	
    	cbxStringFilterOperators.addItem("on");
    	cbxStringFilterOperators.setItemCaption("on", "on");

    	cbxStringFilterOperators.addItem("onorafter");
    	cbxStringFilterOperators.setItemCaption("onorafter", "on or after");

    	cbxStringFilterOperators.addItem("onorbefore");
    	cbxStringFilterOperators.setItemCaption("onorbefore", "on or before");

    	cbxStringFilterOperators.select("between");
    }
    
    private void fillCbxStringFilterOperators(ComboBox cbxDateFilterOperators) 
    {
    	cbxDateFilterOperators.setContainerDataSource(null);

    	cbxDateFilterOperators.addItem("contains");
        cbxDateFilterOperators.setItemCaption("contains", "contains");

        cbxDateFilterOperators.addItem("doesnotcontain");
        cbxDateFilterOperators.setItemCaption("doesnotcontain", "does not contain");

        cbxDateFilterOperators.addItem("doesnotstartwith");
        cbxDateFilterOperators.setItemCaption("doesnotstartwith", "does not start with");
        
        cbxDateFilterOperators.addItem("endswith");
        cbxDateFilterOperators.setItemCaption("endswith", "ends with");
        
        cbxDateFilterOperators.addItem("is");
        cbxDateFilterOperators.setItemCaption("is", "is");
        
        cbxDateFilterOperators.addItem("isempty");
        cbxDateFilterOperators.setItemCaption("isempty", "is empty");
        
        cbxDateFilterOperators.addItem("isnot");
        cbxDateFilterOperators.setItemCaption("isnot", "is not");
        
        cbxDateFilterOperators.addItem("isnotempty");
        cbxDateFilterOperators.setItemCaption("isnotempty", "is not empty");
        
    	cbxDateFilterOperators.addItem("startswith");
        cbxDateFilterOperators.setItemCaption("startswith", "starts with");
        
        cbxDateFilterOperators.select("is");
    }    

    private void fillCbxExperimentFields(ComboBox cbxExperimentFields)
    {
    	for (int i = 0; experimentFields != null && i < experimentFields.size(); i++) {
            if (experimentFields.get(i).getExpFieldType().startsWith("varchar") || experimentFields.get(i).getExpFieldType().startsWith("char")
                    || experimentFields.get(i).getExpFieldType().startsWith("text") || experimentFields.get(i).getExpFieldType().startsWith("nvarchar")
                    || experimentFields.get(i).getExpFieldType().startsWith("nchar") || experimentFields.get(i).getExpFieldType().startsWith("ntext")
                    || experimentFields.get(i).getExpFieldType().contains("date")) 
            {
            	cbxExperimentFields.addItem(experimentFields.get(i).getExpDbFieldNameId());
            	cbxExperimentFields.setItemCaption(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());
            }
        }

     	cbxExperimentFields.addItem("CreatedDate");
    	cbxExperimentFields.setItemCaption("CreatedDate", "Created Date");

     	cbxExperimentFields.addItem("LastModifiedDate");
    	cbxExperimentFields.setItemCaption("LastModifiedDate", "Last Modified Date");
    }    
    
    /*
    private void showFilter2(boolean visible) {
        dtFilter2.setValue(null);
        dtFilter2.setVisible(visible);
    }

    */

    
    private void exportExperimentDataReportToExcel() {
        if (this.tblExperimentDataReport.getItemIds() != null) {
            System.out.println("Starting export: " + new Date());

            ExcelExport xlsExport = new ExcelExport(tblExperimentDataReport, new XSSFWorkbook(), lblExperimentTitle.getValue().trim(), null, lblExperimentTitle.getValue().trim() + ".xlsx", false);
            xlsExport.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            xlsExport.setUseTableFormatPropertyValue(false);
            xlsExport.export();
            System.out.println("Finishing export: " + new Date());

        }
    }

    private void openDataViewRecordCRUDModalWindow(int experimentRecordId) {
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
                if(filtersApplied)
                	applySelectedFilters();
            }
        });
        this.getUI().addWindow(dataViewRecordCRUDModalWindow);
    }

    private void viewSelectedRecordHistory() {
        if (this.selectedRecordId == -1)
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

    private void bindExperimentRptTable() {
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

                TableQuery tblQuery = new TableQuery(experiment.getExpDbRptTableNameId(), connectionPool, new MSSQLGenerator());
                //tblQuery.setVersionColumn("RecordId");
                List<OrderBy> tblOrderByRecordId = Arrays.asList(new OrderBy("RecordId", false));
                //tblQuery.setOrderBy(tblOrderByRecordId);
                StatementHelper sh = tblQuery.getSqlGenerator().generateSelectQuery(experiment.getExpDbRptTableNameId(), null, tblOrderByRecordId, 0, 0, "COUNT(*)");
                
                //System.out.println(sh.getQueryString());
                
                
                
                vaadinTblContainer = new SQLContainer(tblQuery);

                tblExperimentDataReport.setContainerDataSource(vaadinTblContainer);
                
                tblExperimentDataReport.setSortContainerPropertyId("RecordId");
                tblExperimentDataReport.setSortAscending(false);
                
                Integer totalRecords = 1 + tblExperimentDataReport.size();
                this.lblrefreshDate.setValue(this.lblrefreshDate.getValue() + "  [Total Records: " + totalRecords + "]"); 
                
                if (experimentFields != null) {
                    String[] expFieldDbId = new String[experimentFields.size() + 3];

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


                    for (int i = 0; i < experimentFields.size(); i++) {
                        expFieldDbId[i + 1] = experimentFields.get(i).getExpDbFieldNameId();
                        
                        if(experimentFields.get(i).getUnitOfMeasure() != null)
                        	tblExperimentDataReport.setColumnHeader(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName() + " [" + experimentFields.get(i).getUnitOfMeasure().getUomAbbreviation() + "]");
                        else	
                        	tblExperimentDataReport.setColumnHeader(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());
                        
                        if (experimentFields.get(i).getExpFieldType().toLowerCase().contains("float") ||
                                experimentFields.get(i).getExpFieldType().toLowerCase().contains("decimal") ||
                                experimentFields.get(i).getExpFieldType().toLowerCase().contains("int")) {
                            tblExperimentDataReport.setConverter(experimentFields.get(i).getExpDbFieldNameId(), new StringToDoubleConverter() {
                                @Override
                                protected NumberFormat getFormat(Locale locale) {
                                    NumberFormat format = NumberFormat.getNumberInstance();
                                    format.setGroupingUsed(false);
                                    return format;
                                }
                            });

                        }

                        if (experimentFields.get(i).getExpFieldType().toLowerCase().contains("date")) {
                            tblExperimentDataReport.setConverter(experimentFields.get(i).getExpDbFieldNameId(), new StringToDateConverter() {
                                @Override
                                protected DateFormat getFormat(Locale locale) {
                                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                }
                            });
                        }
                    }

                    expFieldDbId[expFieldDbId.length - 2] = "CreatedDate";
                    expFieldDbId[expFieldDbId.length - 1] = "LastModifiedDate";

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