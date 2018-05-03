package com.bringit.experiment.ui.form;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FinalPassYieldInfoField;
import com.bringit.experiment.bll.FinalPassYieldReport;
import com.bringit.experiment.bll.FirstPassYieldInfoField;
import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.FirstTimeYieldInfoField;
import com.bringit.experiment.bll.FirstTimeYieldReport;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.TargetColumn;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FinalPassYieldInfoFieldDao;
import com.bringit.experiment.dao.FinalPassYieldReportDao;
import com.bringit.experiment.dao.FirstPassYieldInfoFieldDao;
import com.bringit.experiment.dao.FirstPassYieldReportDao;
import com.bringit.experiment.dao.FirstTimeYieldInfoFieldDao;
import com.bringit.experiment.dao.FirstTimeYieldReportDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.TargetColumnDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.ui.design.XBarRDesign;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.Validator;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.Or;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;


@JavaScript({ "vaadin://themes/mytheme/QCSPCChartGWTWar/FrequencyHistogramForExperimentDesigner.js", "vaadin://themes/mytheme/QCSPCChartGWTWar/qcspcchartgwt/qcspcchartgwt.nocache.js"})
@StyleSheet({"vaadin://themes/mytheme/QCSPCChartGWTWar/QCSPCChartGWT.css"})
public class XBarRForm extends XBarRDesign{

	private List<Experiment> activeExperiments = new ExperimentDao().getActiveExperiments();
	private List<FirstPassYieldReport> activeFpyReports = new FirstPassYieldReportDao().getAllFirstPassYieldReports();
	private List<FinalPassYieldReport> activeFnyReports = new FinalPassYieldReportDao().getAllFinalPassYieldReports();
	private List<FirstTimeYieldReport> activeFtyReports = new FirstTimeYieldReportDao().getAllFirstTimeYieldReports();
	private List<TargetReport> activeTargetReports = new TargetReportDao().getAllActiveTargetReports();
	private SystemSettings systemSettings = new SystemSettingsDao().getCurrentSystemSettings();;

    Integer filterCnt = 1;
    boolean filtersApplied = false;
    

	List<String> srcFieldDbId = new ArrayList<String>();
	List<String> srcFieldType = new ArrayList<String>();
    
    private String sqlQuery = "";    
    String firstWhereClause = "";
    List<String> orSqlWhereClause = new ArrayList<String>();
    List<String> andSqlWhereClause = new ArrayList<String>();
    
	public XBarRForm()
	{	
		//Loading Data Source Types
		cbxDataSourceType.setContainerDataSource(null);
		cbxDataSourceType.addItem(this.systemSettings.getExperimentLabel());
		cbxDataSourceType.addItem("First Pass Yield");
		cbxDataSourceType.addItem("First Time Yield");
		cbxDataSourceType.addItem("Final Pass Yield");
		cbxDataSourceType.addItem("Target Report (What If)");
		
		cbxDataSourceType.addValueChangeListener(new ValueChangeListener(){
	           @Override
				public void valueChange(ValueChangeEvent event) {
	        	   onChangeCbxDataSourceType(cbxDataSourceType, cbxDataSource);
	           }
	        });
		
		Validator floatValidator = new Validator() {

            public void validate(Object value) throws InvalidValueException {
               try
               {
            	   if(!((String) value).matches("[-+]?[0-9]*\\.?[0-9]+"))  
                	   throw new InvalidValueException("Invalid Number");
               }
               catch(Exception e)
               {
            	   throw new InvalidValueException("Invalid Number");
               }
            }
        };
        
        txtIncrementMins.addValidator(floatValidator);
        txtIncrementMins.setValue("5");
        
        //Fill Filter Comboboxes
        fillCbxExpressions(this.cbxExpression1);
        fillCbxStringFilterOperators(this.cbxFilterOperator1);

        fillCbxSourceFields(cbxSourceField1);
        
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
        

        cbxSourceField1.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	updateFilterSearchField(cbxSourceField1);
            }
        });
        
        cbxSpecLowerLimitSrc.setContainerDataSource(null);
        txtSpecLowerLimit.addValidator(floatValidator);
        cbxSpecLowerLimitSrc.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	if(cbxSpecLowerLimitSrc.getValue() != null)
            	{
            		if(cbxSpecLowerLimitSrc.getValue().equals("fixed_value"))
            		{
                		txtSpecLowerLimit.setReadOnly(false);
                		txtSpecLowerLimit.setValue("");
            		}
            		else
            		{
            			//Execute SQL Query to get Lower Limit
                		txtSpecLowerLimit.setReadOnly(false);
                		
                		if(cbxDataSource.getValue() != null)
                		{
                			String dataSourceDbTableName = "";
                			String dataSourceLowerLimitDbTableColumnName = cbxSpecLowerLimitSrc.getValue().toString().substring(8);
                			int dataSourceId = Integer.parseInt(cbxDataSource.getValue().toString().substring(4));
                			
                			if(cbxDataSource.getValue().toString().startsWith("exp"))
                				dataSourceDbTableName = new ExperimentDao().getExperimentById(dataSourceId).getExpDbRptTableNameId();
                   		
                			if(cbxDataSource.getValue().toString().startsWith("fpy"))
                				dataSourceDbTableName = new FirstPassYieldReportDao().getFirstPassYieldReportById(dataSourceId).getFpyReportDbRptTableNameId();
                		
                			if(cbxDataSource.getValue().toString().startsWith("fty"))
                				dataSourceDbTableName = new FirstTimeYieldReportDao().getFirstTimeYieldReportById(dataSourceId).getFtyReportDbRptTableNameId();
                    		
                			if(cbxDataSource.getValue().toString().startsWith("fny"))
                				dataSourceDbTableName = new FinalPassYieldReportDao().getFinalPassYieldReportById(dataSourceId).getFnyReportDbRptTableNameId();

                			if(cbxDataSource.getValue().toString().startsWith("tgt"))
                				dataSourceDbTableName = new TargetReportDao().getTargetReportById(dataSourceId).getTargetReportDbRptTableNameId();
                			
                			String sqlQuery = "SELECT MIN(" + dataSourceLowerLimitDbTableColumnName + ") as LowerLimit FROM " + dataSourceDbTableName + ";";
                			
                			ResultSet dwLowerLimitResultSet = new ExecuteQueryDao().getSqlSelectQueryResults(sqlQuery);
                     		if(dwLowerLimitResultSet != null)
                     		{ 
                     			try 
                     			{
                     				while (dwLowerLimitResultSet.next()) 
                     					txtSpecLowerLimit.setValue(dwLowerLimitResultSet.getString("LowerLimit"));
                     			}
                     			catch (SQLException e) 
                     			{
                 					// TODO Auto-generated catch block
                 					e.printStackTrace();
                 				}  
                     		}
                		}
                		
                		txtSpecLowerLimit.setReadOnly(true);
            		}
            	}
            	else
            	{
            		txtSpecLowerLimit.setReadOnly(false);
            		txtSpecLowerLimit.setValue("");
            		txtSpecLowerLimit.setReadOnly(true);
            	}
            }
        });

        cbxSpecUpperLimitSrc.setContainerDataSource(null);
        txtSpecUpperLimit.addValidator(floatValidator);
        cbxSpecUpperLimitSrc.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	if(cbxSpecUpperLimitSrc.getValue() != null)
            	{
            		if(cbxSpecUpperLimitSrc.getValue().equals("fixed_value"))
            		{
                		txtSpecUpperLimit.setReadOnly(false);
                		txtSpecUpperLimit.setValue("");
            		}
            		else
            		{
            			//Execute SQL Query to get Upper Limit
                		txtSpecUpperLimit.setReadOnly(false);
                		
                		if(cbxDataSource.getValue() != null)
                		{
                			String dataSourceDbTableName = "";
                			String dataSourceUpperLimitDbTableColumnName = cbxSpecUpperLimitSrc.getValue().toString().substring(8);
                			int dataSourceId = Integer.parseInt(cbxDataSource.getValue().toString().substring(4));
                			
                			if(cbxDataSource.getValue().toString().startsWith("exp"))
                				dataSourceDbTableName = new ExperimentDao().getExperimentById(dataSourceId).getExpDbRptTableNameId();
                   		
                			if(cbxDataSource.getValue().toString().startsWith("fpy"))
                				dataSourceDbTableName = new FirstPassYieldReportDao().getFirstPassYieldReportById(dataSourceId).getFpyReportDbRptTableNameId();
                		
                			if(cbxDataSource.getValue().toString().startsWith("fty"))
                				dataSourceDbTableName = new FirstTimeYieldReportDao().getFirstTimeYieldReportById(dataSourceId).getFtyReportDbRptTableNameId();
                    		
                			if(cbxDataSource.getValue().toString().startsWith("fny"))
                				dataSourceDbTableName = new FinalPassYieldReportDao().getFinalPassYieldReportById(dataSourceId).getFnyReportDbRptTableNameId();

                			if(cbxDataSource.getValue().toString().startsWith("tgt"))
                				dataSourceDbTableName = new TargetReportDao().getTargetReportById(dataSourceId).getTargetReportDbRptTableNameId();
                			
                			String sqlQuery = "SELECT Max(" + dataSourceUpperLimitDbTableColumnName + ") as UpperLimit FROM " + dataSourceDbTableName + ";";
                			
                			ResultSet dwUpperLimitResultSet = new ExecuteQueryDao().getSqlSelectQueryResults(sqlQuery);
                     		if(dwUpperLimitResultSet != null)
                     		{ 
                     			try 
                     			{
                     				while (dwUpperLimitResultSet.next()) 
                     					txtSpecUpperLimit.setValue(dwUpperLimitResultSet.getString("UpperLimit"));
                     			}
                     			catch (SQLException e) 
                     			{
                 					// TODO Auto-generated catch block
                 					e.printStackTrace();
                 				}  
                     		}
                		}
                		txtSpecUpperLimit.setReadOnly(true);
            		}
            	}
            	else
            	{
            		txtSpecUpperLimit.setReadOnly(false);
            		txtSpecUpperLimit.setValue("");
            		txtSpecUpperLimit.setReadOnly(true);
            	}
            }
        });

        this.btnRunReport.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	
            	if(cbxDataSourceType.getValue() == null || cbxDataSource.getValue() == null || cbxMeasurementFieldColumn.getValue() == null || cbxDatetimeFieldColumn.getValue() == null
            	|| !(txtSpecLowerLimit.getValue() != null && !txtSpecLowerLimit.getValue().isEmpty()) || !(txtSpecUpperLimit.getValue() != null && !txtSpecUpperLimit.getValue().isEmpty())
            	|| !(txtCapabilityLSLValue.getValue() != null && !txtCapabilityLSLValue.getValue().isEmpty()) || !(txtIncrementMins.getValue() != null && !txtIncrementMins.getValue().isEmpty())
            	|| !(txtCapabilityUSLValue.getValue() != null && !txtCapabilityUSLValue.getValue().isEmpty()))
            	{
            		 getUI().showNotification("Please fill in all the required fields to run report.", Type.WARNING_MESSAGE);
     	      		return;
            	}
            	
            	if(cbxDataSource.getValue() != null)
        		{
            		spcChartLayout.removeAllComponents();
            		
        			String dataSourceDbTableName = "";
        			String dataSourceMeasurementDbTableColumnName = cbxMeasurementFieldColumn.getValue().toString();
        			String dataSourceDatetimeDbTableColumnName = cbxDatetimeFieldColumn.getValue().toString();
        			
        			int dataSourceId = Integer.parseInt(cbxDataSource.getValue().toString().substring(4));
        			
        			if(cbxDataSource.getValue().toString().startsWith("exp"))
        				dataSourceDbTableName = new ExperimentDao().getExperimentById(dataSourceId).getExpDbRptTableNameId();
           		
        			if(cbxDataSource.getValue().toString().startsWith("fpy"))
        				dataSourceDbTableName = new FirstPassYieldReportDao().getFirstPassYieldReportById(dataSourceId).getFpyReportDbRptTableNameId();
        		
        			if(cbxDataSource.getValue().toString().startsWith("fty"))
        				dataSourceDbTableName = new FirstTimeYieldReportDao().getFirstTimeYieldReportById(dataSourceId).getFtyReportDbRptTableNameId();
            		
        			if(cbxDataSource.getValue().toString().startsWith("fny"))
        				dataSourceDbTableName = new FinalPassYieldReportDao().getFinalPassYieldReportById(dataSourceId).getFnyReportDbRptTableNameId();

        			if(cbxDataSource.getValue().toString().startsWith("tgt"))
        				dataSourceDbTableName = new TargetReportDao().getTargetReportById(dataSourceId).getTargetReportDbRptTableNameId();
        			
        			if(!validateSelectedFilters())
        				return;
        			
        			String sqlWhereClause = getSelectedFiltersSQL();
        			
        			//String sqlQuery = "SELECT STUFF((SELECT ',' + CAST(" + dataSourceMeasurementDbTableColumnName + " as varchar(10))" + 
        		    //" FROM " + dataSourceDbTableName + " " + sqlWhereClause + " FOR XML PATH('')), 1 , 1 , '' ) AS measurementValues;";
        			
        			String sqlQuery = "SELECT " + dataSourceMeasurementDbTableColumnName + " AS Measurement," + dataSourceDatetimeDbTableColumnName + " AS Datetime" +
        		    " FROM " + dataSourceDbTableName + " " + sqlWhereClause + ";";
        		
        			System.out.println(sqlQuery);
        			
        			//String sqlQuery = "SELECT Max(" + dataSourceUpperLimitDbTableColumnName + ") as UpperLimit FROM " + dataSourceDbTableName + ";";
        			
        			ResultSet dwMeasurementResultSet = new ExecuteQueryDao().getSqlSelectQueryResults(sqlQuery);
        			String csvMeasurementValues = "";
        			
             		if(dwMeasurementResultSet != null)
             		{ 
             			try 
             			{
             				while (dwMeasurementResultSet.next()) 
             					csvMeasurementValues = dwMeasurementResultSet.getString("Measurement");
             			}
             			catch (SQLException e) 
             			{
         					// TODO Auto-generated catch block
         					e.printStackTrace();
         				}  
             		}
             		
             		//Build Report Unique Name
                	SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
            		String themeResourcesPath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/VAADIN/themes/mytheme/";
            		String reportUniqueFileName = "QCSPCChartGWTWar/XBarRRpt-" + System.currentTimeMillis() + "__" + sessionUser.getUserId() + ".html";
            		String reportUniqueFileNameFullPath = themeResourcesPath + reportUniqueFileName;
                    
            		String reportTemplateContent = getFrequencyHistogramHtmlCode();//readFile(themeResourcesPath + "QCSPCChartGWTWar/FrequencyHistogramForExperimentDesigner.html");

            		/*
        			String frequencyBins = "";
        					
            		if(csvMeasurementValues != "")
            		{
            			List<String> strArrayMeasurementValues = Arrays.asList(csvMeasurementValues.split("\\s*,\\s*"));
            			List<Float> floatArrayMeasurementValues = new ArrayList<Float>();
            			
            			for(String s : strArrayMeasurementValues)
            				floatArrayMeasurementValues.add(Float.parseFloat(s));

            			Collections.sort(floatArrayMeasurementValues, Collections.reverseOrder());

            			Integer intInitialValue = floatArrayMeasurementValues.get(floatArrayMeasurementValues.size()-1).intValue();
            			Integer intFinalValue = floatArrayMeasurementValues.get(0).intValue();
            			
            			
            			float barWidth = Float.parseFloat(txtIncrementMins.getValue());
            			float initialValue = Float.parseFloat(intInitialValue.toString());
            			float finalValue = Float.parseFloat(intFinalValue.toString());
            			
            			for(float i=initialValue; i<finalValue; i=i+barWidth)
            				frequencyBins = frequencyBins + i + ",";
            			
            			frequencyBins = frequencyBins.substring(0, frequencyBins.length() - 1);
            		}
            		
            		
            		//Load values from Database
            		reportTemplateContent = reportTemplateContent.replace("#LowerLimit", txtSpecLowerLimit.getValue());
            		reportTemplateContent = reportTemplateContent.replace("#UpperLimit", txtSpecUpperLimit.getValue());
            		reportTemplateContent = reportTemplateContent.replace("#CsvData", csvMeasurementValues);
            		reportTemplateContent = reportTemplateContent.replace("#FrequencyBins", frequencyBins);
            		*/
            		try {
    					Files.write( Paths.get(reportUniqueFileNameFullPath), reportTemplateContent.getBytes(), StandardOpenOption.CREATE);

    	                ThemeResource tr = new ThemeResource(reportUniqueFileName);
    	        		BrowserFrame embedded = new BrowserFrame("X-Bar R", tr);
    	        		embedded.setWidth("800px");
    	        		embedded.setHeight("620px");
    	        		spcChartLayout.addComponent(embedded);
    	        		
                     } catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
                    	
        		}
            }

        });
       
        
    }
	
	private void onChangeCbxDataSourceType(ComboBox cbxDataSourceType, ComboBox cbxDataSource)
	{
		//Loading Data Sources
		cbxDataSource.setContainerDataSource(null);
		if(cbxDataSourceType.getValue() == null)
			return;
		
		for(int i=0; cbxDataSourceType.getValue().toString().equals(this.systemSettings.getExperimentLabel()) && activeExperiments != null && i<activeExperiments.size(); i++)
		{
			cbxDataSource.addItem("exp_" + activeExperiments.get(i).getExpId());
			cbxDataSource.setItemCaption("exp_" + activeExperiments.get(i).getExpId(), this.systemSettings.getExperimentLabel() + " : " + activeExperiments.get(i).getExpName());
		}
		
		for(int i=0; cbxDataSourceType.getValue().toString().equals("First Pass Yield") &&  activeFpyReports != null && i<activeFpyReports.size(); i++)
		{
			cbxDataSource.addItem("fpy_" + activeFpyReports.get(i).getFpyReportId());
			cbxDataSource.setItemCaption("fpy_" + activeFpyReports.get(i).getFpyReportId(), " FPY : " + activeFpyReports.get(i).getFpyReportName());
		}
		
		for(int i=0; cbxDataSourceType.getValue().toString().equals("Final Pass Yield") &&  activeFnyReports != null && i<activeFnyReports.size(); i++)
		{
			cbxDataSource.addItem("fny_" + activeFnyReports.get(i).getFnyReportId());
			cbxDataSource.setItemCaption("fny_" + activeFnyReports.get(i).getFnyReportId(), " FNY : " + activeFnyReports.get(i).getFnyReportName());
		}
		
		for(int i=0; cbxDataSourceType.getValue().toString().equals("First Time Yield") &&  activeFtyReports != null && i<activeFtyReports.size(); i++)
		{		
			cbxDataSource.addItem("fty_" + activeFtyReports.get(i).getFtyReportId());
			cbxDataSource.setItemCaption("fty_" + activeFtyReports.get(i).getFtyReportId(), " FTY : " + activeFtyReports.get(i).getFtyReportName());
		}	
		
		for(int i=0; cbxDataSourceType.getValue().toString().equals("Target Report (What If)") &&  activeTargetReports != null && i<activeTargetReports.size(); i++)
		{
			cbxDataSource.addItem("tgt_" + activeTargetReports.get(i).getTargetReportId());
			cbxDataSource.setItemCaption("tgt_" + activeTargetReports.get(i).getTargetReportId(), " Target : " + activeTargetReports.get(i).getTargetReportName());
		}
		
		cbxDataSource.addValueChangeListener(new ValueChangeListener(){
	           @Override
				public void valueChange(ValueChangeEvent event) {
	        	   onChangeCbxDataSource(cbxDataSource, cbxMeasurementFieldColumn, cbxDatetimeFieldColumn);
	           }
	        });
	}
	
	public String readFile(String filename)
	{
	    String content = null;
	    File file = new File(filename); // For example, foo.txt
	    FileReader reader = null;
	    try {
	        reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        content = new String(chars);
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if(reader != null){
	            try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	    return content;
	}
	
	
	private void onChangeCbxDataSource(ComboBox cbxDataSource, ComboBox cbxDataSourceField, ComboBox cbxDataSourceDateTimeField)
	{		
		Integer dataSourceId = Integer.parseInt((cbxDataSource.getValue().toString().substring(4)));
		cbxSpecLowerLimitSrc.setContainerDataSource(null);
		cbxSpecLowerLimitSrc.addItem("fixed_value");
		cbxSpecLowerLimitSrc.setItemCaption("fixed_value", "Fixed value");
		cbxSpecUpperLimitSrc.setContainerDataSource(null);
		cbxSpecUpperLimitSrc.addItem("fixed_value");
		cbxSpecUpperLimitSrc.setItemCaption("fixed_value", "Fixed value");
		
		switch(cbxDataSource.getValue().toString().substring(0, 4))
		{
		case "exp_":
			
			List<ExperimentField> expFields = new ExperimentFieldDao().getActiveExperimentFields(new ExperimentDao().getExperimentById(dataSourceId));
			cbxDataSourceField.setContainerDataSource(null);
			for(int i=0; expFields != null && i<expFields.size(); i++)
			{
				if(expFields.get(i).getExpFieldType().equals("float") || expFields.get(i).getExpFieldType().equals("int"))
				{
					cbxDataSourceField.addItem(expFields.get(i).getExpDbFieldNameId());
					cbxDataSourceField.setItemCaption(expFields.get(i).getExpDbFieldNameId(), expFields.get(i).getExpFieldName());

					cbxSpecLowerLimitSrc.addItem("exp_min_" + expFields.get(i).getExpDbFieldNameId());
					cbxSpecLowerLimitSrc.setItemCaption("exp_min_" + expFields.get(i).getExpDbFieldNameId(), "MIN OF " + expFields.get(i).getExpFieldName());

					cbxSpecLowerLimitSrc.addItem("exp_max_" + expFields.get(i).getExpDbFieldNameId());
					cbxSpecLowerLimitSrc.setItemCaption("exp_max_" + expFields.get(i).getExpDbFieldNameId(), "MAX OF " + expFields.get(i).getExpFieldName());

					cbxSpecUpperLimitSrc.addItem("exp_max_" + expFields.get(i).getExpDbFieldNameId());
					cbxSpecUpperLimitSrc.setItemCaption("exp_max_" + expFields.get(i).getExpDbFieldNameId(), "MAX OF " + expFields.get(i).getExpFieldName());
				
					cbxSpecUpperLimitSrc.addItem("exp_min_" + expFields.get(i).getExpDbFieldNameId());
					cbxSpecUpperLimitSrc.setItemCaption("exp_min_" + expFields.get(i).getExpDbFieldNameId(), "MIN OF " + expFields.get(i).getExpFieldName());
				}
				else if(expFields.get(i).getExpFieldType().contains("date"))
				{
					cbxDataSourceDateTimeField.addItem(expFields.get(i).getExpDbFieldNameId());
					cbxDataSourceDateTimeField.setItemCaption(expFields.get(i).getExpDbFieldNameId(), expFields.get(i).getExpFieldName());
				}
			}
			break;

		case "fpy_":
			List<FirstPassYieldInfoField> fpyFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(dataSourceId);
			cbxDataSourceField.setContainerDataSource(null);
			for(int i=0; fpyFields != null && i<fpyFields.size(); i++)
			{
				if(fpyFields.get(i).getExperimentField().getExpFieldType().equals("float") || fpyFields.get(i).getExperimentField().getExpFieldType().equals("int"))
				{
					cbxDataSourceField.addItem(fpyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxDataSourceField.setItemCaption(fpyFields.get(i).getExperimentField().getExpDbFieldNameId(), fpyFields.get(i).getFpyInfoFieldLabel());
				
					cbxSpecLowerLimitSrc.addItem("fpy_min_" + fpyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecLowerLimitSrc.setItemCaption("fpy_min_" + fpyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MIN OF " + fpyFields.get(i).getFpyInfoFieldLabel());

					cbxSpecLowerLimitSrc.addItem("fpy_max_" + fpyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecLowerLimitSrc.setItemCaption("fpy_max_" + fpyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MAX OF " + fpyFields.get(i).getFpyInfoFieldLabel());

					cbxSpecUpperLimitSrc.addItem("fpy_max_" + fpyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecUpperLimitSrc.setItemCaption("fpy_max_" + fpyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MAX OF " + fpyFields.get(i).getFpyInfoFieldLabel());
			
					cbxSpecUpperLimitSrc.addItem("fpy_min_" + fpyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecUpperLimitSrc.setItemCaption("fpy_min_" + fpyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MIN OF " + fpyFields.get(i).getFpyInfoFieldLabel());
				}
				else if(fpyFields.get(i).getExperimentField().getExpFieldType().contains("date"))
				{
					cbxDataSourceDateTimeField.addItem(fpyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxDataSourceDateTimeField.setItemCaption(fpyFields.get(i).getExperimentField().getExpDbFieldNameId(), fpyFields.get(i).getFpyInfoFieldLabel());
				}
				
			}

			cbxDataSourceField.addItem("datetime");
			cbxDataSourceField.setItemCaption("datetime", "Datetime (FPY)");

			cbxDataSourceField.addItem("sn");
			cbxDataSourceField.setItemCaption("sn", "Serial Number (FPY)");

			cbxDataSourceField.addItem("result");
			cbxDataSourceField.setItemCaption("result", "Result (FPY)");
			
			break;

		case "fny_":
			List<FinalPassYieldInfoField> fnyFields = new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldByReportById(dataSourceId);
			cbxDataSourceField.setContainerDataSource(null);
			for(int i=0; fnyFields != null && i<fnyFields.size(); i++)
			{
				if(fnyFields.get(i).getExperimentField().getExpFieldType().equals("float") || fnyFields.get(i).getExperimentField().getExpFieldType().equals("int"))
				{
					cbxDataSourceField.addItem(fnyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxDataSourceField.setItemCaption(fnyFields.get(i).getExperimentField().getExpDbFieldNameId(), fnyFields.get(i).getFnyInfoFieldLabel());

					cbxSpecLowerLimitSrc.addItem("fny_min_" + fnyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecLowerLimitSrc.setItemCaption("fny_min_" + fnyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MIN OF " + fnyFields.get(i).getFnyInfoFieldLabel());

					cbxSpecLowerLimitSrc.addItem("fny_max_" + fnyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecLowerLimitSrc.setItemCaption("fny_max_" + fnyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MAX OF " + fnyFields.get(i).getFnyInfoFieldLabel());

					cbxSpecUpperLimitSrc.addItem("fny_max_" + fnyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecUpperLimitSrc.setItemCaption("fny_max_" + fnyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MAX OF " + fnyFields.get(i).getFnyInfoFieldLabel());
			
					cbxSpecUpperLimitSrc.addItem("fny_min_" + fnyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecUpperLimitSrc.setItemCaption("fny_min_" + fnyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MIN OF " + fnyFields.get(i).getFnyInfoFieldLabel());
				}
				else if(fnyFields.get(i).getExperimentField().getExpFieldType().contains("date"))
				{
					cbxDataSourceDateTimeField.addItem(fnyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxDataSourceDateTimeField.setItemCaption(fnyFields.get(i).getExperimentField().getExpDbFieldNameId(), fnyFields.get(i).getFnyInfoFieldLabel());
				}
			}

			cbxDataSourceField.addItem("datetime");
			cbxDataSourceField.setItemCaption("datetime", "Datetime (FNY)");

			cbxDataSourceField.addItem("sn");
			cbxDataSourceField.setItemCaption("sn", "Serial Number (FNY)");

			cbxDataSourceField.addItem("result");
			cbxDataSourceField.setItemCaption("result", "Result (FNY)");
			
			break;

		case "fty_":
			List<FirstTimeYieldInfoField> ftyFields = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(dataSourceId);
			cbxDataSourceField.setContainerDataSource(null);
			for(int i=0; ftyFields != null && i<ftyFields.size(); i++)
			{
				cbxDataSourceField.addItem(ftyFields.get(i).getExperimentField().getExpDbFieldNameId());
				cbxDataSourceField.setItemCaption(ftyFields.get(i).getExperimentField().getExpDbFieldNameId(), ftyFields.get(i).getFtyInfoFieldLabel());

				if(ftyFields.get(i).getExperimentField().getExpFieldType().equals("float") || ftyFields.get(i).getExperimentField().getExpFieldType().equals("int"))
				{
					cbxSpecLowerLimitSrc.addItem("fty_min_" + ftyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecLowerLimitSrc.setItemCaption("fty_min_" + ftyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MIN OF " + ftyFields.get(i).getFtyInfoFieldLabel());

					cbxSpecLowerLimitSrc.addItem("fty_max_" + ftyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecLowerLimitSrc.setItemCaption("fty_max_" + ftyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MAX OF " + ftyFields.get(i).getFtyInfoFieldLabel());

					cbxSpecUpperLimitSrc.addItem("fty_max_" + ftyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecUpperLimitSrc.setItemCaption("fty_max_" + ftyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MAX OF " + ftyFields.get(i).getFtyInfoFieldLabel());
			
					cbxSpecUpperLimitSrc.addItem("fty_min_" + ftyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxSpecUpperLimitSrc.setItemCaption("fty_min_" + ftyFields.get(i).getExperimentField().getExpDbFieldNameId(), "MIN OF " + ftyFields.get(i).getFtyInfoFieldLabel());
				}
				else if(ftyFields.get(i).getExperimentField().getExpFieldType().contains("date"))
				{
					cbxDataSourceDateTimeField.addItem(ftyFields.get(i).getExperimentField().getExpDbFieldNameId());
					cbxDataSourceDateTimeField.setItemCaption(ftyFields.get(i).getExperimentField().getExpDbFieldNameId(), ftyFields.get(i).getFtyInfoFieldLabel());
				}
			}

			cbxDataSourceField.addItem("fty_datetime");
			cbxDataSourceField.setItemCaption("fty_datetime", "Datetime (FTY)");

			cbxDataSourceField.addItem("fty_sn");
			cbxDataSourceField.setItemCaption("fty_sn", "Serial Number (FTY)");

			cbxDataSourceField.addItem("fty_result");
			cbxDataSourceField.setItemCaption("fty_result", "Result (FTY)");			

			break;

		case "tgt_":
			List<TargetColumn> tgtCols = new TargetColumnDao().getTargetColumnByTargetReportId(dataSourceId);
			cbxDataSourceField.setContainerDataSource(null);
			for(int i=0; tgtCols != null && i<tgtCols.size(); i++)
			{
				if(tgtCols.get(i).getExperimentField().getExpFieldType().equals("float") || tgtCols.get(i).getExperimentField().getExpFieldType().equals("int"))
				{
					cbxDataSourceField.addItem(tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"));
					cbxDataSourceField.setItemCaption(tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"), tgtCols.get(i).getTargetColumnLabel());
				
					cbxSpecLowerLimitSrc.addItem("tgt_min_" + tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"));
					cbxSpecLowerLimitSrc.setItemCaption("tgt_min_" + tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"), "MIN OF " + tgtCols.get(i).getExperimentField().getExpFieldName());

					cbxSpecLowerLimitSrc.addItem("tgt_max_" + tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"));
					cbxSpecLowerLimitSrc.setItemCaption("tgt_max_" + tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"), "MAX OF " + tgtCols.get(i).getExperimentField().getExpFieldName());

					cbxSpecUpperLimitSrc.addItem("tgt_max_" + tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"));
					cbxSpecUpperLimitSrc.setItemCaption("tgt_max_" + tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"), "MAX OF " + tgtCols.get(i).getExperimentField().getExpFieldName());
				
					cbxSpecUpperLimitSrc.addItem("tgt_min_" + tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"));
					cbxSpecUpperLimitSrc.setItemCaption("tgt_min_" + tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"), "MIN OF " + tgtCols.get(i).getExperimentField().getExpFieldName());
				}
				else if(tgtCols.get(i).getExperimentField().getExpFieldType().contains("date"))
				{
					cbxDataSourceDateTimeField.addItem(tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"));
					cbxDataSourceDateTimeField.setItemCaption(tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"), tgtCols.get(i).getTargetColumnLabel());
				}
			}
			break;
				
			default:
					break;
		}
		
		//Remove all filters
		if(multiFilterGrid.getRows() == 1)
			fillCbxSourceFields(cbxSourceField1);
		else
		{
			while(multiFilterGrid.getRows() > 1)
				multiFilterGrid.removeRow(multiFilterGrid.getRows()-1);
			
			fillCbxSourceFields(cbxSourceField1);
		}
		//multiFilterGrid.removeAllComponents();
	}
	
    private String getSelectedFiltersSQL()
    {
    	if(multiFilterGrid.getRows() == 1 && ((ComboBox)multiFilterGrid.getComponent(0, 0)).getValue() == null)
    	 	return "";
    	
    	String filterExpression = null;

    	List<Filter> orFilterList = new ArrayList<>();
    	List<Filter> andFilterList = new ArrayList<>();

    	andSqlWhereClause =  new ArrayList<String>();
    	orSqlWhereClause =  new ArrayList<String>();
    	
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
    	             return "";
       		}
    		
    		ComboBox cbxFilterOperatorField = (ComboBox)multiFilterGrid.getComponent(1, i);
    		if(cbxFilterOperatorField.getValue() == null)
       		{
    	             this.getUI().showNotification("Invalid Filter Expression.", Type.WARNING_MESSAGE);
    	             filtersApplied = false;
    	             return "";
       		}
    		
    		Integer experimentFieldTypeRefIndex = srcFieldDbId.indexOf(cbxExperimentField.getValue());
    		
    		if(srcFieldType.get(experimentFieldTypeRefIndex).contains("date"))
    		{
    			HorizontalLayout dateFieldsLayout = (HorizontalLayout)multiFilterGrid.getComponent(2, i);
    			fromDateFilterField = (DateField)dateFieldsLayout.getComponent(0);
    			toDateFilterField = (DateField)dateFieldsLayout.getComponent(1);
    			
   			 	if ("between".equals(cbxFilterOperatorField.getValue()) && toDateFilterField == null) 
   			 	{
   	                this.getUI().showNotification("From Date and To Date should be set.", Type.WARNING_MESSAGE);
   	                filtersApplied = false;
   	                return "";
   	            }    			
    		}
    		else
    			txtStringFilterField = (TextField)multiFilterGrid.getComponent(2, i);
    		
    		ComboBox cbxExpressionField = (ComboBox)multiFilterGrid.getComponent(3, i);

    		if((i+1) < multiFilterGrid.getRows() && cbxExpressionField.getValue() == null)
    		{
	             this.getUI().showNotification("Invalid Filter Expression.", Type.WARNING_MESSAGE);
	             filtersApplied = false;
	             return "";
    		}
    		
			if(!srcFieldType.get(experimentFieldTypeRefIndex).contains("date"))
    		{
    			switch (cbxFilterOperatorField.getValue().toString().trim()) {
                case "contains":
                   	if("and".equals(filterExpression))
                   	{                   		
                   		andFilterList.add(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%"));
                		andSqlWhereClause.add(cbxExperimentField.getValue() + " LIKE '%" + txtStringFilterField.getValue().trim() + "%'");
                   	}
                	else
                	{
                		orFilterList.add(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%"));
                		if(i==0)
                			firstWhereClause = cbxExperimentField.getValue() + " LIKE '%" + txtStringFilterField.getValue().trim() + "%'";
                		else
                			orSqlWhereClause.add(cbxExperimentField.getValue() + " LIKE '%" + txtStringFilterField.getValue().trim() + "%'");
                	}
                    break;
                case "doesnotcontain": 
                	if("and".equals(filterExpression))
                	{
                		andFilterList.add(new Not(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%")));
                		andSqlWhereClause.add(cbxExperimentField.getValue() + " NOT LIKE '%" + txtStringFilterField.getValue().trim() + "%'");
                	}
                	else
                	{
                		orFilterList.add(new Not(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim() + "%")));
                		if(i==0)
                			firstWhereClause = cbxExperimentField.getValue() + " NOT LIKE '%" + txtStringFilterField.getValue().trim() + "%'";
                		else
                			orSqlWhereClause.add(cbxExperimentField.getValue() + " NOT LIKE '%" + txtStringFilterField.getValue().trim() + "%'");
                	}
                	break;
                case "doesnotstartwith":
            		if("and".equals(filterExpression))
            		{
            			andFilterList.add(new Not(new Like(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim() + "%")));
            			andSqlWhereClause.add(cbxExperimentField.getValue() + " NOT LIKE '" + txtStringFilterField.getValue().trim() + "%'");
            		}
                	else
                	{
                		orFilterList.add(new Not(new Like(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim() + "%")));
                		if(i==0)
                			firstWhereClause = cbxExperimentField.getValue() + " NOT LIKE '" + txtStringFilterField.getValue().trim() + "%'";
                		else
                			orSqlWhereClause.add(cbxExperimentField.getValue() + " NOT LIKE '" + txtStringFilterField.getValue().trim() + "%'");
                	}
                	break;
                case "endswith":
              		if("and".equals(filterExpression))
              		{
              			andFilterList.add(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim()));
              			andSqlWhereClause.add(cbxExperimentField.getValue() + "  LIKE '%" + txtStringFilterField.getValue().trim() + "'");
                	}
                	else
                	{
                		orFilterList.add(new Like(cbxExperimentField.getValue(), "%" + txtStringFilterField.getValue().trim()));
                		if(i==0)
                			firstWhereClause = cbxExperimentField.getValue() + "  LIKE '%" + txtStringFilterField.getValue().trim() + "'";
                		else
                			orSqlWhereClause.add(cbxExperimentField.getValue() + "  LIKE '%" + txtStringFilterField.getValue().trim() + "'");
                	}
                    break;
                case "is":
            		if("and".equals(filterExpression))
            		{
            			andFilterList.add(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim()));
              			andSqlWhereClause.add(cbxExperimentField.getValue() + "  = '" + txtStringFilterField.getValue().trim() + "'");
            		}
                	else
                	{
                		orFilterList.add(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim()));
                		if(i==0)
                			firstWhereClause = cbxExperimentField.getValue() + "  = '" + txtStringFilterField.getValue().trim() + "'";
                		else
                			orSqlWhereClause.add(cbxExperimentField.getValue() + "  = '" + txtStringFilterField.getValue().trim() + "'");
                	}
                	break;
                case "isempty":
                	if("and".equals(filterExpression))
                	{
                		List<Filter> emptyFilterList = new ArrayList<>();
                		emptyFilterList.add(new IsNull(cbxExperimentField.getValue()));
                		emptyFilterList.add(new Compare.Equal(cbxExperimentField.getValue(), ""));
                		andFilterList.add(new Or(emptyFilterList.toArray(new Filter[emptyFilterList.size()])));
                		andSqlWhereClause.add("(" + cbxExperimentField.getValue() + " IS NULL OR " + cbxExperimentField.getValue() + " = '')");
                	}
                	else
                	{
                		List<Filter> emptyFilterList = new ArrayList<>();
                		emptyFilterList.add(new IsNull(cbxExperimentField.getValue()));
                		emptyFilterList.add(new Compare.Equal(cbxExperimentField.getValue(), ""));
                		orFilterList.add(new Or(emptyFilterList.toArray(new Filter[emptyFilterList.size()])));                		
                		if(i==0)
                			firstWhereClause = "(" + cbxExperimentField.getValue() + " IS NULL OR " + cbxExperimentField.getValue() + " = '')";
                		else
                			orSqlWhereClause.add("(" + cbxExperimentField.getValue() + " IS NULL OR " + cbxExperimentField.getValue() + " = '')");
                	}
                    break;
                case "isnot":
            		if("and".equals(filterExpression))
            		{
            			andFilterList.add(new Not(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim())));
            			andSqlWhereClause.add(cbxExperimentField.getValue() + "  <> '" + txtStringFilterField.getValue().trim() + "'");
                    }
                	else
                	{
                		orFilterList.add(new Not(new Compare.Equal(cbxExperimentField.getValue(), txtStringFilterField.getValue().trim())));
                		if(i==0)
                			firstWhereClause = cbxExperimentField.getValue() + "  <> '" + txtStringFilterField.getValue().trim() + "'";
                		else
                			orSqlWhereClause.add(cbxExperimentField.getValue() + "  <> '" + txtStringFilterField.getValue().trim() + "'");
                	}
                    break;
                case "isnotempty":
                	if("and".equals(filterExpression))
                	{
                		List<Filter> notEmptyFilterList = new ArrayList<>();
                		notEmptyFilterList.add(new Not(new IsNull(cbxExperimentField.getValue())));
                		notEmptyFilterList.add(new Not(new Compare.Equal(cbxExperimentField.getValue(), "")));
                		andFilterList.add(new And(notEmptyFilterList.toArray(new Filter[notEmptyFilterList.size()])));                		
                		andSqlWhereClause.add("(" + cbxExperimentField.getValue() + " IS NOT NULL OR " + cbxExperimentField.getValue() + " <> '')");
                	}
                	else
                	{
                		List<Filter> notEmptyFilterList = new ArrayList<>();
                		notEmptyFilterList.add(new Not(new IsNull(cbxExperimentField.getValue())));
                		notEmptyFilterList.add(new Not(new Compare.Equal(cbxExperimentField.getValue(), "")));
                		orFilterList.add(new And(notEmptyFilterList.toArray(new Filter[notEmptyFilterList.size()])));                		
                		if(i==0)
                			firstWhereClause = "(" + cbxExperimentField.getValue() + " IS NOT NULL OR " + cbxExperimentField.getValue() + " <> '')";
                		else
                			orSqlWhereClause.add("(" + cbxExperimentField.getValue() + " IS NOT NULL OR " + cbxExperimentField.getValue() + " <> '')");
                	}
                    break;
                case "startswith":
                   	if("and".equals(filterExpression))
                   	{
                   		andFilterList.add(new Like(cbxExperimentField.getValue(),  txtStringFilterField.getValue().trim() + "%"));
                   		andSqlWhereClause.add(cbxExperimentField.getValue() + " LIKE '" + txtStringFilterField.getValue().trim() + "%'");
                	}	
                	else
                	{
                		orFilterList.add(new Like(cbxExperimentField.getValue(),  txtStringFilterField.getValue().trim() + "%"));
                		if(i==0)
                			firstWhereClause = cbxExperimentField.getValue() + " LIKE '" + txtStringFilterField.getValue().trim() + "%'";
                		else
                			orSqlWhereClause.add(cbxExperimentField.getValue() + " LIKE '" + txtStringFilterField.getValue().trim() + "%'");                		
                	}
                	break;
    			}
    		}
    		else
    		{
    		    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		    
    			Date dateFilterValue1 = new Date(fromDateFilterField.getValue().getTime());
                Date dateFilterValue2 = new Date(fromDateFilterField.getValue().getTime());

                
                if (cbxFilterOperatorField.getValue().equals("between")) {
                    dateFilterValue2 = new Date(toDateFilterField.getValue().getTime());
                    dateFilterValue2.setHours(23);
                    dateFilterValue2.setMinutes(59);
                    dateFilterValue2.setSeconds(59);
                    if("and".equals(filterExpression))
                    {
                    	andFilterList.add(new Between(cbxExperimentField.getValue(), dateFilterValue1, dateFilterValue2));
                		andSqlWhereClause.add(cbxExperimentField.getValue() + " BETWEEN '" + df.format(dateFilterValue1) + "' AND '" + df.format(dateFilterValue2) + "'");
                    }
                    else
                    {
                    	orFilterList.add(new Between(cbxExperimentField.getValue(), dateFilterValue1, dateFilterValue2));
                    	if(i==0)
                			firstWhereClause = cbxExperimentField.getValue() + " BETWEEN '" + df.format(dateFilterValue1) + "' AND '" + df.format(dateFilterValue2) + "'";
                		else
                			orSqlWhereClause.add(cbxExperimentField.getValue() + " BETWEEN '" + df.format(dateFilterValue1) + "' AND '" + df.format(dateFilterValue2) + "'");
                    }
                } else {
                    String sqlDateFilterOperator = "";

                    switch (cbxFilterOperatorField.getValue().toString().trim()) {
                        case "on":
                        	dateFilterValue2.setHours(23);
                            dateFilterValue2.setMinutes(59);
                            dateFilterValue2.setSeconds(59);
                       		
                           	if("and".equals(filterExpression))
                           	{
                              	andFilterList.add(new Between(cbxExperimentField.getValue(), dateFilterValue1, dateFilterValue2));
                        		andSqlWhereClause.add(cbxExperimentField.getValue() + " BETWEEN '" + df.format(dateFilterValue1) + "' AND '" + df.format(dateFilterValue2) + "'");
                            }
                           	else
                           	{
                              	orFilterList.add(new Between(cbxExperimentField.getValue(), dateFilterValue1, dateFilterValue2));
                              	if(i==0)
                        			firstWhereClause = cbxExperimentField.getValue() + " BETWEEN '" + df.format(dateFilterValue1) + "' AND '" + df.format(dateFilterValue2) + "'";
                        		else
                        			orSqlWhereClause.add(cbxExperimentField.getValue() + " BETWEEN '" + df.format(dateFilterValue1) + "' AND '" + df.format(dateFilterValue2) + "'");
                           	}
        	                break;
                        case "before":
                         	if("and".equals(filterExpression))
                         	{
                         		andFilterList.add(new Compare.Less(cbxExperimentField.getValue(), dateFilterValue1));
                           		andSqlWhereClause.add(cbxExperimentField.getValue() + " < '" + df.format(dateFilterValue1) + "'");
                         	}
    	                    else
    	                    {
    	                    	orFilterList.add(new Compare.Less(cbxExperimentField.getValue(), dateFilterValue1));
    	                    	if(i==0)
    	                			firstWhereClause = cbxExperimentField.getValue() + " < '" + df.format(dateFilterValue1) + "'";
    	                		else
    	                			orSqlWhereClause.add(cbxExperimentField.getValue() + " < '" + df.format(dateFilterValue1) + "'");
    	                    }
    	                    break;
                        case "after":
                        	dateFilterValue1.setHours(23);
                            dateFilterValue1.setMinutes(59);
                            dateFilterValue1.setSeconds(59);
                       		if("and".equals(filterExpression))
                            {
                                andFilterList.add(new Compare.Greater(cbxExperimentField.getValue(), dateFilterValue1));
                           		andSqlWhereClause.add(cbxExperimentField.getValue() + " > '" + df.format(dateFilterValue1) + "'");
                            }
    	                    else
    	                    {
                                orFilterList.add(new Compare.Greater(cbxExperimentField.getValue(), dateFilterValue1));
                                if(i==0)
                        			firstWhereClause = cbxExperimentField.getValue() + " > '" + df.format(dateFilterValue1) + "'";
                        		else
                        			orSqlWhereClause.add(cbxExperimentField.getValue() + " > '" + df.format(dateFilterValue1) + "'");
    	                    }
    	                    break;
                        case "onorbefore":
                        	dateFilterValue1.setHours(23);
                            dateFilterValue1.setMinutes(59);
                            dateFilterValue1.setSeconds(59);
                        	
                            if("and".equals(filterExpression))
                            {
                                andFilterList.add(new Compare.LessOrEqual(cbxExperimentField.getValue(), dateFilterValue1));
                           		andSqlWhereClause.add(cbxExperimentField.getValue() + " <= '" + df.format(dateFilterValue1) + "'");
                            }
    	                    else
    	                    {
                            	orFilterList.add(new Compare.LessOrEqual(cbxExperimentField.getValue(), dateFilterValue1));
                            	if(i==0)
                        			firstWhereClause = cbxExperimentField.getValue() + " <= '" + df.format(dateFilterValue1) + "'";
                        		else
                        			orSqlWhereClause.add(cbxExperimentField.getValue() + " <= '" + df.format(dateFilterValue1) + "'");
    	                    }
    	                    break;
                        case "onorafter":
                            if("and".equals(filterExpression))
                            {
                            	andFilterList.add(new Compare.GreaterOrEqual(cbxExperimentField.getValue(), dateFilterValue1));
                           		andSqlWhereClause.add(cbxExperimentField.getValue() + " >= '" + df.format(dateFilterValue1) + "'");
                            }
    	                    else
    	                    {
    	                    	orFilterList.add(new Compare.GreaterOrEqual(cbxExperimentField.getValue(), dateFilterValue1));
    	                    	if(i==0)
    	                			firstWhereClause = cbxExperimentField.getValue() + " >= '" + df.format(dateFilterValue1) + "'";
    	                		else
    	                			orSqlWhereClause.add(cbxExperimentField.getValue() + " >= '" + df.format(dateFilterValue1) + "'");
    	                    }
    	                    break;
                    }
                }	    			
    		}	    		
		
			if(cbxExpressionField.getValue() != null)
				filterExpression = cbxExpressionField.getValue().toString();
			else
				filterExpression = "and";
    	}
    	
    	String sqlWhereClause = " WHERE " + firstWhereClause;
        
        for(int i=0; i < andSqlWhereClause.size(); i++)
        	 sqlWhereClause += " AND (" + andSqlWhereClause.get(i) + ")";
        
        for(int i=0; i < orSqlWhereClause.size(); i++)
        	 sqlWhereClause += " OR (" + orSqlWhereClause.get(i) + ")";
        
        System.out.println("SQL Where Clause: " + sqlWhereClause);
    	filtersApplied = true;  
    	return sqlWhereClause;
    }
	

    private boolean validateSelectedFilters()
    {
    	if(multiFilterGrid.getRows() == 1 && ((ComboBox)multiFilterGrid.getComponent(0, 0)).getValue() == null)
    	 	return true;
    	
    	
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
    	             return false;
       		}
    		
    		ComboBox cbxFilterOperatorField = (ComboBox)multiFilterGrid.getComponent(1, i);
    		if(cbxFilterOperatorField.getValue() == null)
       		{
    	             this.getUI().showNotification("Invalid Filter Expression.", Type.WARNING_MESSAGE);
    	             filtersApplied = false;
    	             return false;
       		}
    		
    		Integer experimentFieldTypeRefIndex = srcFieldDbId.indexOf(cbxExperimentField.getValue());
    		
    		if(srcFieldType.get(experimentFieldTypeRefIndex).contains("date"))
    		{
    			HorizontalLayout dateFieldsLayout = (HorizontalLayout)multiFilterGrid.getComponent(2, i);
    			fromDateFilterField = (DateField)dateFieldsLayout.getComponent(0);
    			toDateFilterField = (DateField)dateFieldsLayout.getComponent(1);
    			
   			 	if ("between".equals(cbxFilterOperatorField.getValue()) && toDateFilterField == null) 
   			 	{
   	                this.getUI().showNotification("From Date and To Date should be set.", Type.WARNING_MESSAGE);
   	                filtersApplied = false;
   	                return false;
   	            }    			
    		}
    		else
    			txtStringFilterField = (TextField)multiFilterGrid.getComponent(2, i);
    		
    		ComboBox cbxExpressionField = (ComboBox)multiFilterGrid.getComponent(3, i);

    		if((i+1) < multiFilterGrid.getRows() && cbxExpressionField.getValue() == null)
    		{
	             this.getUI().showNotification("Invalid Filter Expression.", Type.WARNING_MESSAGE);
	             filtersApplied = false;
	             return false;
    		}
    	}
    	return true;
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

    private void attachNewFilterRow()
    {
    	if(cbxDataSource.getValue() == null)
    		return;
    	
    	Integer totalFilterRows = multiFilterGrid.getRows();
    	String newComponentsId = "" + (filterCnt + 1);
    	
    	multiFilterGrid.insertRow(totalFilterRows);
    	
    	ComboBox newCbxSourceField = new ComboBox();
    	newCbxSourceField.setId("cbxSourceField" + newComponentsId);
    	newCbxSourceField.setHeight(cbxSourceField1.getHeight(), UNITS_PIXELS);
    	newCbxSourceField.setWidth(cbxSourceField1.getWidth(), UNITS_PIXELS);
    	newCbxSourceField.setStyleName(cbxSourceField1.getStyleName());
    	
    	newCbxSourceField.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	updateFilterSearchField(newCbxSourceField);
            }
        });
    	
    	fillCbxSourceFields(newCbxSourceField);
    	multiFilterGrid.addComponent(newCbxSourceField, 0, totalFilterRows);
    	
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
    	{
    		ComboBox cbxExperimentField = (ComboBox)multiFilterGrid.getComponent(0, 0);    		
    		
			Integer selectedGridFilterRow = multiFilterGrid.getComponentArea(cbxExperimentField).getRow1();
			multiFilterGrid.removeComponent(0, selectedGridFilterRow);
			multiFilterGrid.removeComponent(2, selectedGridFilterRow);
			
			ComboBox selectedCbxFilterOperatorField = (ComboBox)multiFilterGrid.getComponent(1, selectedGridFilterRow);
			selectedCbxFilterOperatorField.setContainerDataSource(null);
			fillCbxStringFilterOperators(selectedCbxFilterOperatorField);
			
			TextField newTxtStringFilterField = new TextField();
	    	newTxtStringFilterField.setHeight(txtStringFilter1.getHeight(), UNITS_PIXELS);
	    	newTxtStringFilterField.setWidth(txtStringFilter1.getWidth(), UNITS_PIXELS);
	    	newTxtStringFilterField.setStyleName(txtStringFilter1.getStyleName());
	    	multiFilterGrid.addComponent(newTxtStringFilterField, 2, selectedGridFilterRow);
			

	    	ComboBox newCbxSourceField = new ComboBox();
	    	newCbxSourceField.setId("cbxSourceField");
	    	newCbxSourceField.setHeight(cbxSourceField1.getHeight(), UNITS_PIXELS);
	    	newCbxSourceField.setWidth(cbxSourceField1.getWidth(), UNITS_PIXELS);
	    	newCbxSourceField.setStyleName(cbxSourceField1.getStyleName());
	    	
	    	newCbxSourceField.addValueChangeListener(new ValueChangeListener() {

	            @Override
	            public void valueChange(ValueChangeEvent event) {
	            	updateFilterSearchField(newCbxSourceField);
	            }
	        });
	    	
	    	fillCbxSourceFields(newCbxSourceField);
	    	multiFilterGrid.addComponent(newCbxSourceField, 0, 0);    	   
    		
    		return;
    	}
    	
    	Integer gridFilterRow = multiFilterGrid.getComponentArea(clickedButton).getRow1();    	
    	multiFilterGrid.removeRow(gridFilterRow);
    	
    	if(multiFilterGrid.getRows() > 1)
    		multiFilterGrid.setHeight(multiFilterGrid.getHeight() - 35, UNITS_PIXELS);
    }
    
	private void updateFilterSearchField(ComboBox cbxSourceField)
	{
		if(cbxSourceField.getValue() != null)
		{
			if(cbxDataSource.getValue() == null)
				return;
			
			Integer dataSourceId = Integer.parseInt((cbxDataSource.getValue().toString().substring(4)));
			
			switch(cbxDataSource.getValue().toString().substring(0, 4))
			{
			case "exp_":
				
				List<ExperimentField> expFields = new ExperimentFieldDao().getActiveExperimentFields(new ExperimentDao().getExperimentById(dataSourceId));
				for(int i=0; expFields != null && i<expFields.size(); i++)
				{
					srcFieldDbId.add(expFields.get(i).getExpDbFieldNameId());
					srcFieldType.add(expFields.get(i).getExpFieldType());
				}
				break;

			case "fpy_":
				List<FirstPassYieldInfoField> fpyFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(dataSourceId);
				for(int i=0; fpyFields != null && i<fpyFields.size(); i++)
				{
					srcFieldDbId.add(fpyFields.get(i).getExperimentField().getExpDbFieldNameId());
					srcFieldType.add(fpyFields.get(i).getExperimentField().getExpFieldType());
				}

				srcFieldDbId.add("datetime");
				srcFieldType.add("datetime");
				srcFieldDbId.add("sn");
				srcFieldType.add("nvarchar(MAX)");
				srcFieldDbId.add("result");
				srcFieldType.add("nvarchar(MAX)");
				
				break;

			case "fny_":
				List<FinalPassYieldInfoField> fnyFields = new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldByReportById(dataSourceId);
				for(int i=0; fnyFields != null && i<fnyFields.size(); i++)
				{
					srcFieldDbId.add(fnyFields.get(i).getExperimentField().getExpDbFieldNameId());
					srcFieldType.add(fnyFields.get(i).getExperimentField().getExpFieldType());
				}

				srcFieldDbId.add("datetime");
				srcFieldType.add("datetime");
				srcFieldDbId.add("sn");
				srcFieldType.add("nvarchar(MAX)");
				srcFieldDbId.add("result");
				srcFieldType.add("nvarchar(MAX)");
				
				break;

			case "fty_":
				List<FirstTimeYieldInfoField> ftyFields = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(dataSourceId);
				for(int i=0; ftyFields != null && i<ftyFields.size(); i++)
				{
					srcFieldDbId.add(ftyFields.get(i).getExperimentField().getExpDbFieldNameId());
					srcFieldType.add(ftyFields.get(i).getExperimentField().getExpFieldType());
				}

				srcFieldDbId.add("datetime");
				srcFieldType.add("datetime");
				srcFieldDbId.add("sn");
				srcFieldType.add("nvarchar(MAX)");
				srcFieldDbId.add("result");
				srcFieldType.add("nvarchar(MAX)");			

				break;

			case "tgt_":
				List<TargetColumn> tgtCols = new TargetColumnDao().getTargetColumnByTargetReportId(dataSourceId);
				cbxSourceField.setContainerDataSource(null);
				for(int i=0; tgtCols != null && i<tgtCols.size(); i++)
				{
					srcFieldDbId.add(tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"));
					srcFieldType.add(tgtCols.get(i).getExperimentField().getExpFieldType());
				}
				break;
					
				default:
						break;
			}
			
			
			Integer sourceFieldTypeRefIndex = srcFieldDbId.indexOf(cbxSourceField.getValue().toString().trim());
			
			if(srcFieldType.get(sourceFieldTypeRefIndex).contains("date"))
			{	
				Integer selectedGridFilterRow = multiFilterGrid.getComponentArea(cbxSourceField).getRow1();
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
				Integer selectedGridFilterRow = multiFilterGrid.getComponentArea(cbxSourceField).getRow1();
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
	}
    
	private void fillCbxSourceFields(ComboBox cbxSourceField)
	{
		if(cbxDataSource.getValue() == null)
			return;
		
		Integer dataSourceId = Integer.parseInt((cbxDataSource.getValue().toString().substring(4)));

		switch(cbxDataSource.getValue().toString().substring(0, 4))
		{
		case "exp_":
			
			List<ExperimentField> expFields = new ExperimentFieldDao().getActiveExperimentFields(new ExperimentDao().getExperimentById(dataSourceId));
			cbxSourceField.setContainerDataSource(null);
			for(int i=0; expFields != null && i<expFields.size(); i++)
			{
				cbxSourceField.addItem(expFields.get(i).getExpDbFieldNameId());
				cbxSourceField.setItemCaption(expFields.get(i).getExpDbFieldNameId(), expFields.get(i).getExpFieldName());
			}
			break;

		case "fpy_":
			List<FirstPassYieldInfoField> fpyFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(dataSourceId);
			cbxSourceField.setContainerDataSource(null);
			for(int i=0; fpyFields != null && i<fpyFields.size(); i++)
			{
				cbxSourceField.addItem(fpyFields.get(i).getExperimentField().getExpDbFieldNameId());
				cbxSourceField.setItemCaption(fpyFields.get(i).getExperimentField().getExpDbFieldNameId(), fpyFields.get(i).getFpyInfoFieldLabel());
			}

			cbxSourceField.addItem("datetime");
			cbxSourceField.setItemCaption("datetime", "Datetime (FPY)");

			cbxSourceField.addItem("sn");
			cbxSourceField.setItemCaption("sn", "Serial Number (FPY)");

			cbxSourceField.addItem("result");
			cbxSourceField.setItemCaption("result", "Result (FPY)");
			
			break;

		case "fny_":
			List<FinalPassYieldInfoField> fnyFields = new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldByReportById(dataSourceId);
			cbxSourceField.setContainerDataSource(null);
			for(int i=0; fnyFields != null && i<fnyFields.size(); i++)
			{
				cbxSourceField.addItem(fnyFields.get(i).getExperimentField().getExpDbFieldNameId());
				cbxSourceField.setItemCaption(fnyFields.get(i).getExperimentField().getExpDbFieldNameId(), fnyFields.get(i).getFnyInfoFieldLabel());
			}

			cbxSourceField.addItem("datetime");
			cbxSourceField.setItemCaption("datetime", "Datetime (FNY)");

			cbxSourceField.addItem("sn");
			cbxSourceField.setItemCaption("sn", "Serial Number (FNY)");

			cbxSourceField.addItem("result");
			cbxSourceField.setItemCaption("result", "Result (FNY)");
			
			break;

		case "fty_":
			List<FirstTimeYieldInfoField> ftyFields = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(dataSourceId);
			cbxSourceField.setContainerDataSource(null);
			for(int i=0; ftyFields != null && i<ftyFields.size(); i++)
			{
				cbxSourceField.addItem(ftyFields.get(i).getExperimentField().getExpDbFieldNameId());
				cbxSourceField.setItemCaption(ftyFields.get(i).getExperimentField().getExpDbFieldNameId(), ftyFields.get(i).getFtyInfoFieldLabel());

			}

			cbxSourceField.addItem("fty_datetime");
			cbxSourceField.setItemCaption("fty_datetime", "Datetime (FTY)");

			cbxSourceField.addItem("fty_sn");
			cbxSourceField.setItemCaption("fty_sn", "Serial Number (FTY)");

			cbxSourceField.addItem("fty_result");
			cbxSourceField.setItemCaption("fty_result", "Result (FTY)");			

			break;

		case "tgt_":
			List<TargetColumn> tgtCols = new TargetColumnDao().getTargetColumnByTargetReportId(dataSourceId);
			cbxSourceField.setContainerDataSource(null);
			for(int i=0; tgtCols != null && i<tgtCols.size(); i++)
			{
				cbxSourceField.addItem(tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"));
				cbxSourceField.setItemCaption(tgtCols.get(i).getTargetColumnLabel().replaceAll(" ", "_"), tgtCols.get(i).getTargetColumnLabel());
			}
			break;
				
			default:
					break;
		}
		
		
	}
	
	private String getFrequencyHistogramHtmlCode()
	{
		String htmlCode = "<html> " +
		  "<head> " +
		 
		   "<script type=\"text/javascript\" language=\"javascript\"> " +

		   " var TimeXBarR= " + 
		   " {" + 
		   "		    \"StaticProperties\": {" + 
		   "		        \"Canvas\": {" + 
		   "		            \"Width\": 800," + 
		   "		            \"Height\": 550" + 
		   "		        }" + 
		   "		    }," + 
		   "		    \"SPCChart\": {" + 
		   "		        \"InitChartProperties\": {" + 
		   "		            \"SPCChartType\": \"MEAN_RANGE_CHART\"," + 
		   "		            \"ChartMode\": \"Time\"," + 
		   "		            \"NumSamplesPerSubgroup\": 5," + 
		   "		            \"NumDatapointsInView\": 12," + 
		   "		            \"TimeIncrementMinutes\": 15" + 
		   "		        }," + 
		   "	        " + 
		   "		        \"Scrollbar\": {" + 
		   "		            \"EnableScrollBar\": true," + 
		   "		            \"ScrollbarPosition\": \"SCROLLBAR_POSITION_MAX\"" + 
		   "		        }," + 
		   "		        \"TableSetup\": {" + 
		   "		            \"HeaderStringsLevel\": \"HEADER_STRINGS_LEVEL2\"," + 
		   "		            \"EnableInputStringsDisplay\": true," + 
		   "		            \"EnableCategoryValues\": false," + 
		   "		            \"EnableCalculatedValues\": false," + 
		   "		            \"EnableTotalSamplesValues\": false," + 
		   "		            \"EnableNotes\": false," + 
		   "		            \"EnableTimeValues\": false," + 
		   "		            \"EnableNotesToolTip\": false," + 
		   "		            \"EnableAlarmStatusValues\": false," + 
		   "		            \"TableBackgroundMode\": \"TABLE_NO_COLOR_BACKGROUND\"," + 
		   "		            \"TableAlarmEmphasisMode\": \"ALARM_HIGHLIGHT_BAR\"," + 
		   "		            \"ChartAlarmEmphasisMode\": \"ALARM_HIGHLIGHT_SYMBOL\"," + 
		   "		            \"ChartData\": {" + 
		   "		                \"Title\": \"Variable Control Chart (X-Bar R)\"," + 
		   "		                \"PartNumber\": \"283501\"," + 
		   "		                \"ChartNumber\": \"17\"," + 
		   "		                \"PartName\": \"Transmission Casing Bolt\"," + 
		   "		                \"Operation\": \"Threading\"," + 
		   "		                \"SpecificationLimits\": \"27.0 to 35.0\"," + 
		   "		                \"Operator\": \"J. Fenamore\"," + 
		   "		                \"Machine\": \"#11\"," + 
		   "		                \"Gauge\": \"#8645\"," + 
		   "		                \"UnitOfMeasure\": \"0.0001 inch\"," + 
		   "		                \"ZeroEquals\": \"zero\"," + 
		   "		                \"DateString\": \"7/04/2013\"," + 
		   "		                \"NotesMessage\": \"Control limits prepared May 10\"," + 
		   "		                \"NotesHeader\": \"NOTES\"," + 
		   "		                \"CalculatedItemDecimals\": 1," + 
		   "		                \"ProcessCapabilityDecimals\": 2," + 
		   "		                \"SampleItemDecimals\": 1," + 
		   "		                \"ProcessCapabilitySetup\": {" + 
		   "		                    \"LSLValue\": 27," + 
		   "		                    \"USLValue\": 35," + 
		   "		                    \"EnableCPK\": false," + 
		   "		                    \"EnableCPM\": false," + 
		   "		                    \"EnablePPK\": false" + 
		   "		                }" + 
		   "		            }" + 
		   "		        }," + 
		   "		        \"Events\": {" + 
		   "		            \"EnableDataToolTip\": true," + 
		   "		            \"EnableNotesToolTip\": true" + 
		   "" + 
		   "		        }," + 
		   "		        \"SampleData\": {" + 
		   "		            \"SampleIntervalRecords\": [" + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        27.53131515148628," + 
		   "		                        33.95771604022404," + 
		   "		                        24.310097827061817," + 
		   "		                        28.282642847792765," + 
		   "		                        30.2908518818265" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 0," + 
		   "		                    \"TimeStamp\": 1371830829074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        27.444285005240214," + 
		   "		                        34.38930645615096," + 
		   "		                        28.0203674441636," + 
		   "		                        33.27153359969366," + 
		   "		                        36.8305571558275" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 1," + 
		   "		                    \"TimeStamp\": 1371831729074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        35.21321620109259," + 
		   "		                        32.93940741018088," + 
		   "		                        33.66485557976163," + 
		   "		                        34.17314124609133," + 
		   "		                        24.576683179863725" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 2," + 
		   "		                    \"TimeStamp\": 1371832629074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        27.898302097237174," + 
		   "		                        25.906531082892915," + 
		   "		                        26.950768095191137," + 
		   "		                        30.812058501916457," + 
		   "		                        31.085075984847936" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 3," + 
		   "		                    \"TimeStamp\": 1371833529074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        22.94549873989527," + 
		   "		                        30.867679835728896," + 
		   "		                        27.79692256857592," + 
		   "		                        32.57962619388354," + 
		   "		                        24.601635858609224" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 4," + 
		   "		                    \"TimeStamp\": 1371834429074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        25.757197681039116," + 
		   "		                        26.404591401588813," + 
		   "		                        35.894483711583206," + 
		   "		                        32.12852859671644," + 
		   "		                        36.29883496126227" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 5," + 
		   "		                    \"TimeStamp\": 1371835329074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        34.04503169439933," + 
		   "		                        25.44613390413118," + 
		   "		                        22.724021438882726," + 
		   "		                        33.57327330013773," + 
		   "		                        27.11566620945375" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 6," + 
		   "		                    \"TimeStamp\": 1371836229074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        33.893820803904475," + 
		   "		                        30.88079320043272," + 
		   "		                        30.28128557255706," + 
		   "		                        22.866135928123242," + 
		   "		                        22.66263807377607" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 7," + 
		   "		                    \"TimeStamp\": 1371837129074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        35.85689222409033," + 
		   "		                        25.66069012425097," + 
		   "		                        36.52188973776906," + 
		   "		                        37.2538370091988," + 
		   "		                        37.09011776089278" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 8," + 
		   "		                    \"TimeStamp\": 1371838029074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        30.749686153841764," + 
		   "		                        36.508559444966785," + 
		   "		                        26.13767810973511," + 
		   "		                        37.08032465061903," + 
		   "		                        35.66542199068421" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 9," + 
		   "		                    \"TimeStamp\": 1371838929074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        35.27238495008025," + 
		   "		                        33.12277092009595," + 
		   "		                        29.12017445753296," + 
		   "		                        27.993405024903374," + 
		   "		                        25.50152822137062" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 10," + 
		   "		                    \"TimeStamp\": 1371839829074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        28.100740427272193," + 
		   "		                        35.26755280950981," + 
		   "		                        30.216381858411253," + 
		   "		                        31.518942973184913," + 
		   "		                        24.976185060190754" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 11," + 
		   "		                    \"TimeStamp\": 1371840729074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        28.121815138825482," + 
		   "		                        29.3505524617516," + 
		   "		                        23.23952375446977," + 
		   "		                        23.409277423044397," + 
		   "		                        23.291225859674306" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 12," + 
		   "		                    \"TimeStamp\": 1371841629074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        28.602514772277242," + 
		   "		                        25.362590653075816," + 
		   "		                        22.72198910384308," + 
		   "		                        22.920387724885945," + 
		   "		                        28.912077965025972" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 13," + 
		   "		                    \"TimeStamp\": 1371842529074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        32.40532551477631," + 
		   "		                        26.948806188170593," + 
		   "		                        29.284169867825277," + 
		   "		                        32.70409548305437," + 
		   "		                        25.773609178760466" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 14," + 
		   "		                    \"TimeStamp\": 1371843429074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        29.548933086870605," + 
		   "		                        36.24857495241145," + 
		   "		                        28.036723548132613," + 
		   "		                        37.31376242884538," + 
		   "		                        29.73936566095378" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 15," + 
		   "		                    \"TimeStamp\": 1371844329074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        35.34918665138229," + 
		   "		                        33.60402762568151," + 
		   "		                        29.69203231293511," + 
		   "		                        24.756849726860697," + 
		   "		                        27.34428622213201" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 16," + 
		   "		                    \"TimeStamp\": 1371845229074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        33.727605556103605," + 
		   "		                        27.50821643159519," + 
		   "		                        33.61161600390977," + 
		   "		                        34.36241767438651," + 
		   "		                        27.55491000970097" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 17," + 
		   "		                    \"TimeStamp\": 1371846129074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        34.26565897109985," + 
		   "		                        28.056929833331772," + 
		   "		                        27.183128112177673," + 
		   "		                        33.17121401058127," + 
		   "		                        34.191900955504686" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 18," + 
		   "		                    \"TimeStamp\": 1371847029074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }," + 
		   "		                {" + 
		   "		                    \"SampleValues\": [" + 
		   "		                        30.56585901649224," + 
		   "		                        26.764807472584284," + 
		   "		                        30.22766077749437," + 
		   "		                        29.43260723522982," + 
		   "		                        27.080310485264213" + 
		   "		                    ]," + 
		   "		                    \"BatchCount\": 19," + 
		   "		                    \"TimeStamp\": 1371847929074," + 
		   "		                    \"Note\": \"\"" + 
		   "		                }" + 
		   "		            ]" + 
		   "		        }," + 
		   "		        \"Methods\": {" + 
		   "		            \"AutoCalculateControlLimits\": true," + 
		   "		            \"AutoScaleYAxes\": true," + 
		   "		            \"RebuildUsingCurrentData\": true" + 
		   "		        }" + 
		   "		    }" + 
		   "		};" +
		   "</script>" + 
		   
		   
		   "   <script>" + 
		   "   " + 
		   "    function defineChartUsingJSON( )" + 
		   "   {" + 
		   "     var s = JSON.stringify(TimeXBarR);" + 
		   "     return s;" + 
		   "   }" + 
		   "  " + 
		   "    </script>  " + 
		   "  " + 
		   "    <!--                                           -->" + 
		   "    <!-- This script loads your compiled module.   -->" + 
		   "    <!-- If you add any GWT meta tags, they must   -->" + 
		   "    <!-- be added before this line.                -->" + 
		   "    <!--                                           -->" + 
		   "   <script type=\"text/javascript\" language=\"javascript\" src=\"qcspcchartgwt/qcspcchartgwt.nocache.js\"></script> " + 
		   "  </head>" + 
		   "" + 
		   "" + 
		   "  <body>" + 
		   "  " + 
		   "    <!-- OPTIONAL: include this if you want history support -->" + 
		   "    <iframe src=\"javascript:''\" id=\"__gwt_historyFrame\" tabIndex='-1' style=\"position:absolute;width:0;height:0;border:0\"></iframe>" + 
		   "    " + 
		   "    <!-- RECOMMENDED if your web app will not function without JavaScript enabled -->" + 
		   "    <noscript>" + 
		   "      <div style=\"width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif\">" + 
		   "        Your web browser must have JavaScript enabled" + 
		   "        in order for this application to display correctly." + 
		   "      </div>" + 
		   "    </noscript>" + 
		   "" + 
		   "  </body>"+
		   "</html>";
		
		return htmlCode;
	}
	
	
}
