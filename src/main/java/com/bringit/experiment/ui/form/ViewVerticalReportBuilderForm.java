package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bringit.experiment.bll.CsvTemplateEnrichment;
import com.bringit.experiment.bll.CustomList;
import com.bringit.experiment.bll.CustomListValue;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FirstPassYieldInfoField;
import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.FirstTimeYieldInfoField;
import com.bringit.experiment.bll.FirstTimeYieldReport;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.TargetColumn;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.bll.ViewVerticalReport;
import com.bringit.experiment.bll.ViewVerticalReportByExperiment;
import com.bringit.experiment.bll.ViewVerticalReportByFpyRpt;
import com.bringit.experiment.bll.ViewVerticalReportByFtyRpt;
import com.bringit.experiment.bll.ViewVerticalReportByTargetRpt;
import com.bringit.experiment.bll.ViewVerticalReportColumn;
import com.bringit.experiment.bll.ViewVerticalReportColumnByExpField;
import com.bringit.experiment.bll.ViewVerticalReportColumnByFpyField;
import com.bringit.experiment.bll.ViewVerticalReportColumnByFtyField;
import com.bringit.experiment.bll.ViewVerticalReportFilterByExpField;
import com.bringit.experiment.bll.ViewVerticalReportFilterByFpyField;
import com.bringit.experiment.bll.ViewVerticalReportFilterByFtyField;
import com.bringit.experiment.dao.CsvTemplateEnrichmentDao;
import com.bringit.experiment.dao.CustomListDao;
import com.bringit.experiment.dao.CustomListValueDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FirstPassYieldInfoFieldDao;
import com.bringit.experiment.dao.FirstPassYieldReportDao;
import com.bringit.experiment.dao.FirstTimeYieldInfoFieldDao;
import com.bringit.experiment.dao.FirstTimeYieldReportDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.TargetColumnDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.dao.ViewVerticalReportByExperimentDao;
import com.bringit.experiment.dao.ViewVerticalReportByFpyRptDao;
import com.bringit.experiment.dao.ViewVerticalReportByFtyRptDao;
import com.bringit.experiment.dao.ViewVerticalReportByTargetRptDao;
import com.bringit.experiment.dao.ViewVerticalReportColumnByExpFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportColumnByFpyFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportColumnByFtyFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportColumnDao;
import com.bringit.experiment.dao.ViewVerticalReportDao;
import com.bringit.experiment.dao.ViewVerticalReportFilterByExpFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportFilterByFpyFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportFilterByFtyFieldDao;
import com.bringit.experiment.ui.design.ViewVerticalReportBuilderDesign;
import com.bringit.experiment.util.Config;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class ViewVerticalReportBuilderForm extends ViewVerticalReportBuilderDesign {

	private List<Experiment> activeExperiments = new ExperimentDao().getActiveExperiments();
	private List<FirstPassYieldReport> activeFpyReports = new FirstPassYieldReportDao().getAllFirstPassYieldReports();
	private List<FirstTimeYieldReport> activeFtyReports = new FirstTimeYieldReportDao().getAllFirstTimeYieldReports();
	private List<TargetReport> activeTargetReports = new TargetReportDao().getAllActiveTargetReports();
	private SystemSettings systemSettings;
	
	private OptionGroup optGrpPnlExperiments = new OptionGroup();
	private OptionGroup optGrpPnlFpyReports = new OptionGroup();
	private OptionGroup optGrpPnlFtyReports = new OptionGroup();
	private OptionGroup optGrpPnlTargetReports = new OptionGroup();

	private int lastDataSourceFilterItemId = 0;
	private int lastReportColumnItemId = 0;
	private int lastVerticalRptEnrichmentItemId = 0;
	 

	TreeTable tblVwVerticalRptCols = new TreeTable("Report Columns"); 
	
	String[] dbfieldTypes;
	
	public ViewVerticalReportBuilderForm()
	{
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		
		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
			dbfieldTypes = configuration.getProperty("sqlserverdatatypes").split(",");
			
		//START: Data source selection
		
		Panel pnlExperiments = new Panel("Experiments");
		VerticalLayout lytPnlExperiments = new VerticalLayout();
		optGrpPnlExperiments.setMultiSelect(true);
		optGrpPnlExperiments.setStyleName("small");
		pnlExperiments.setHeight(100, Unit.PERCENTAGE);
		pnlExperiments.setWidth(95, Unit.PERCENTAGE);
		pnlExperiments.setStyleName("well");
		pnlExperiments.setCaption("");
		
		for(int i=0; activeExperiments != null && i<activeExperiments.size(); i++)
		{
			optGrpPnlExperiments.addItem(activeExperiments.get(i).getExpId());
			optGrpPnlExperiments.setItemCaption(activeExperiments.get(i).getExpId(), activeExperiments.get(i).getExpName());
		}
		
		optGrpPnlExperiments.addValueChangeListener(new ValueChangeListener(){
           @Override
			public void valueChange(ValueChangeEvent event) {
        	   refreshExperimentDataSourceElements();
           }
        });
		
		lytPnlExperiments.addComponent(optGrpPnlExperiments);
		pnlExperiments.setContent(lytPnlExperiments);
		pnlExperimentDataSource.addComponent(pnlExperiments);
		pnlExperimentDataSource.setCaption(this.systemSettings.getExperimentPluralLabel());
		
		Panel pnlFpyReports = new Panel("First Pass Yield Reports");
		VerticalLayout lytPnlFpyReports = new VerticalLayout();
		optGrpPnlFpyReports.setMultiSelect(true);
		optGrpPnlFpyReports.setStyleName("small");
		pnlFpyReports.setHeight(100, Unit.PERCENTAGE);
		pnlFpyReports.setWidth(95, Unit.PERCENTAGE);
		pnlFpyReports.setStyleName("well");
		pnlFpyReports.setCaption("");
		
		for(int i=0; activeFpyReports != null && i<activeFpyReports.size(); i++)
		{
			optGrpPnlFpyReports.addItem(activeFpyReports.get(i).getFpyReportId());
			optGrpPnlFpyReports.setItemCaption(activeFpyReports.get(i).getFpyReportId(), activeFpyReports.get(i).getFpyReportName());
		}

		optGrpPnlFpyReports.addValueChangeListener(new ValueChangeListener(){
           @Override
			public void valueChange(ValueChangeEvent event) {
        	   refreshFpyRptDataSourceElements();
           }
        });
		
		lytPnlFpyReports.addComponent(optGrpPnlFpyReports);
		pnlFpyReports.setContent(lytPnlFpyReports);
		pnlFpyReportDataSource.addComponent(pnlFpyReports);

		Panel pnlFtyReports = new Panel("First Time Yield Reports");
		VerticalLayout lytPnlFtyReports = new VerticalLayout();
		optGrpPnlFtyReports.setMultiSelect(true);
		optGrpPnlFtyReports.setStyleName("small");
		pnlFtyReports.setHeight(100, Unit.PERCENTAGE);
		pnlFtyReports.setWidth(95, Unit.PERCENTAGE);
		pnlFtyReports.setStyleName("well");
		pnlFtyReports.setCaption("");
		
		for(int i=0; activeFtyReports != null && i<activeFtyReports.size(); i++)
		{
			optGrpPnlFtyReports.addItem(activeFtyReports.get(i).getFtyReportId());
			optGrpPnlFtyReports.setItemCaption(activeFtyReports.get(i).getFtyReportId(), activeFtyReports.get(i).getFtyReportName());
		}

		optGrpPnlFtyReports.addValueChangeListener(new ValueChangeListener(){
           @Override
			public void valueChange(ValueChangeEvent event) {
        	   refreshFtyRptDataSourceElements();
           }
        });
		
		lytPnlFtyReports.addComponent(optGrpPnlFtyReports);
		pnlFtyReports.setContent(lytPnlFtyReports);
		pnlFtyReportDataSource.addComponent(pnlFtyReports);
		
		Panel pnlTargetReports = new Panel("Target Reports");
		VerticalLayout lytPnlTargetReports = new VerticalLayout();
		optGrpPnlTargetReports.setMultiSelect(true);
		optGrpPnlTargetReports.setStyleName("small");
		pnlTargetReports.setHeight(100, Unit.PERCENTAGE);
		pnlTargetReports.setWidth(95, Unit.PERCENTAGE);
		pnlTargetReports.setStyleName("well");
		pnlTargetReports.setCaption("");

		for(int i=0; activeTargetReports != null && i<activeTargetReports.size(); i++)
		{
			optGrpPnlTargetReports.addItem(activeTargetReports.get(i).getTargetReportId());
			optGrpPnlTargetReports.setItemCaption(activeTargetReports.get(i).getTargetReportId(), activeTargetReports.get(i).getTargetReportName());
			
			if(activeTargetReports.get(i).getTargetReportWhatIf() != null && activeTargetReports.get(i).getTargetReportWhatIf())
				optGrpPnlTargetReports.setItemCaption(activeTargetReports.get(i).getTargetReportId(), "What if: " + activeTargetReports.get(i).getTargetReportName());
				
		}

		optGrpPnlTargetReports.addValueChangeListener(new ValueChangeListener(){
           @Override
			public void valueChange(ValueChangeEvent event) {
        	   refreshTargetRptDataSourceElements();
           }
        });
		
		lytPnlTargetReports.addComponent(optGrpPnlTargetReports);
		pnlTargetReports.setContent(lytPnlTargetReports);
		pnlTargetReportDataSource.addComponent(pnlTargetReports);

		//END: Data source selection
		
		//START: Load data tables
		loadDataSourceFilterTable();
		
		
		//START: Data source filters
		this.btnAddFilter.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addDataSourceFilter(null, null);
			}

		});
	
		
		//START: Load data tables
		loadReportColumnsTable();
				
		
		this.btnAddRptColumn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addReportColumn(null);
			}

		});
		
		//START: Load report columns enrichment
		loadTblEnrichmentRulesData();
		
		this.btnAddEnrichment.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addTblEnrichmentRule(null);
			}

		});
		
		this.btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onSave();
			}
		});
		
		//START: load report columns using tree
		loadReportColumns2Table();
		

		this.btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addReportColumn2(null);
			}

		});
	}

	private void refreshExperimentDataSourceElements()
	{
		//System.out.println(optGrpPnlExperiments.getValue());
		
		Set<Item> selectedOptGrpPnlExperiments = (Set<Item>) optGrpPnlExperiments.getValue();
		for (Object selectedExperiment : selectedOptGrpPnlExperiments)
		{
			//System.out.println(selectedExperiment.toString());
			//sysRole.setRoleMenuAccess((sysRole.getRoleMenuAccess() == null ? "" : sysRole.getRoleMenuAccess()) + selectedOptionGroup.toString() + "\n");
		}

	}
	
	private void refreshFpyRptDataSourceElements()
	{
		//System.out.println(optGrpPnlFpyReports.getValue());
	}
	
	private void refreshFtyRptDataSourceElements()
	{
		//System.out.println(optGrpPnlFtyReports.getValue());
		
	}
	
	private void refreshTargetRptDataSourceElements()
	{
		//System.out.println(optGrpPnlTargetReports.getValue());
		
	}

	private void loadDataSourceFilterTable()
	{
		this.tblReportFilters.setContainerDataSource(null);
		this.tblReportFilters.setStyleName("small");
		this.tblReportFilters.addContainerProperty("*", CheckBox.class, null);
		this.tblReportFilters.addContainerProperty("Data Source", ComboBox.class, null);
		this.tblReportFilters.addContainerProperty("Source Column/Field", ComboBox.class, null);
		this.tblReportFilters.addContainerProperty("Filter Operator", ComboBox.class, null);
		this.tblReportFilters.addContainerProperty("Filter Value", TextField.class, null);
		this.tblReportFilters.addContainerProperty("Filter Value 2", TextField.class, null);
		this.tblReportFilters.addContainerProperty("Custom List", ComboBox.class, null);
		this.tblReportFilters.addContainerProperty("Custom List Value", ComboBox.class, null);
		this.tblReportFilters.addContainerProperty("Expression", ComboBox.class, null);
		this.tblReportFilters.setEditable(true);
		this.tblReportFilters.setPageLength(0);
		this.tblReportFilters.setColumnWidth("*", 20);
	}
	
	private void addDataSourceFilter(String dataSourceType, Integer dataSourceFilterId)
	{
		
		Integer itemId = dataSourceFilterId;
		if(itemId == null)
		{	
			this.lastDataSourceFilterItemId = this.lastDataSourceFilterItemId - 1;
			itemId = this.lastDataSourceFilterItemId;
		}
		
		Object[] itemValues = new Object[9];
		
		//Dummy initial column
		CheckBox chxSelect = new CheckBox();
		chxSelect.setStyleName("tiny");
		chxSelect.setVisible(false);
		chxSelect.setHeight(20, Unit.PIXELS);
		itemValues[0] = chxSelect;
		
		//Loading data sources
		ComboBox cbxDataSource = new ComboBox("");
		cbxDataSource.setStyleName("tiny");
		cbxDataSource.setRequired(true);
		cbxDataSource.setRequiredError("This field is required.");
		
		Set<Item> selectedOptGrpPnlExperiments = (Set<Item>) optGrpPnlExperiments.getValue();
		for (Object selectedExperiment : selectedOptGrpPnlExperiments)
		{
			for(int i=0; activeExperiments != null && i<activeExperiments.size(); i++)
			{
				int selectedExperimentId = Integer.parseInt(selectedExperiment.toString());				
				if(activeExperiments.get(i).getExpId() == selectedExperimentId)
				{
					cbxDataSource.addItem("exp_" + selectedExperimentId);
					cbxDataSource.setItemCaption("exp_" + selectedExperimentId, this.systemSettings.getExperimentLabel() + " : " + activeExperiments.get(i).getExpName());
					break;
				}
			}
		}

		Set<Item> selectedOptGrpPnlFpyRpts = (Set<Item>) optGrpPnlFpyReports.getValue();
		for (Object selectedFpyRpt : selectedOptGrpPnlFpyRpts)
		{
			for(int i=0; activeFpyReports != null && i<activeFpyReports.size(); i++)
			{
				int selectedFpyReportId = Integer.parseInt(selectedFpyRpt.toString());
				if(activeFpyReports.get(i).getFpyReportId() == selectedFpyReportId)
				{
					cbxDataSource.addItem("fpy_" + selectedFpyReportId);
					cbxDataSource.setItemCaption("fpy_" + selectedFpyReportId, " FPY : " + activeFpyReports.get(i).getFpyReportName());
					break;
				}
			}
		}
		
		Set<Item> selectedOptGrpPnlFtyRpts = (Set<Item>) optGrpPnlFtyReports.getValue();
		for (Object selectedFtyRpt : selectedOptGrpPnlFtyRpts)
		{
			for(int i=0; activeFtyReports != null && i<activeFtyReports.size(); i++)
			{
				int selectedFtyReportId = Integer.parseInt(selectedFtyRpt.toString());
				if(activeFtyReports.get(i).getFtyReportId() == selectedFtyReportId)
				{
					cbxDataSource.addItem("fty_" + selectedFtyReportId);
					cbxDataSource.setItemCaption("fty_" + selectedFtyReportId, " FTY : " + activeFtyReports.get(i).getFtyReportName());
					break;
				}
			}
		}	
		
		Set<Item> selectedOptGrpPnlTargetRpts = (Set<Item>) optGrpPnlTargetReports.getValue();
		for (Object selectedTargetRpt : selectedOptGrpPnlTargetRpts)
		{
			for(int i=0; activeTargetReports != null && i<activeTargetReports.size(); i++)
			{
				int selectedTargetReportId = Integer.parseInt(selectedTargetRpt.toString());
				if(activeTargetReports.get(i).getTargetReportId() == selectedTargetReportId)
				{
					cbxDataSource.addItem("tgt_" + selectedTargetReportId);
					cbxDataSource.setItemCaption("tgt_" + selectedTargetReportId, " Target : " + activeTargetReports.get(i).getTargetReportName());
					
					if(activeTargetReports.get(i).getTargetReportWhatIf() != null && activeTargetReports.get(i).getTargetReportWhatIf())
						cbxDataSource.setItemCaption("tgt_" + selectedTargetReportId, " Target (What if) : " + activeTargetReports.get(i).getTargetReportName());
					
					break;
				}
			}
		}

		cbxDataSource.setWidth(150, Unit.PIXELS);
		cbxDataSource.setHeight(20, Unit.PIXELS);
		itemValues[1] = cbxDataSource;
		
		//Loading source fields
		ComboBox cbxDataSourceField = new ComboBox("");
		cbxDataSourceField.setStyleName("tiny");
		cbxDataSourceField.setRequired(true);
		cbxDataSourceField.setRequiredError("This field is required.");
		cbxDataSourceField.setWidth(100, Unit.PIXELS);				
		cbxDataSourceField.setHeight(20, Unit.PIXELS);		
		itemValues[2] = cbxDataSourceField;
		
		//Loading filter operator fields
		ComboBox cbxFilterOperator = new ComboBox("");
		cbxFilterOperator.setStyleName("tiny");
		cbxFilterOperator.setHeight(20, Unit.PIXELS);
		itemValues[3] = cbxFilterOperator;

		TextField txtFilterValue1 = new TextField("");
		txtFilterValue1.setStyleName("tiny");
		txtFilterValue1.setHeight(20, Unit.PIXELS);
		itemValues[4] = txtFilterValue1;

		TextField txtFilterValue2 = new TextField("");
		txtFilterValue2.setStyleName("tiny");
		txtFilterValue2.setHeight(20, Unit.PIXELS);
		itemValues[5] = txtFilterValue2;
		
		//Loading custom lists
		ComboBox cbxCustomLists = new ComboBox("");
		cbxCustomLists.setStyleName("tiny");
		cbxCustomLists.setHeight(20, Unit.PIXELS);
		itemValues[6] = cbxCustomLists;
		
		//Loading custom list values
		ComboBox cbxCustomListValues = new ComboBox("");
		cbxCustomListValues.setStyleName("tiny");
		cbxCustomListValues.setHeight(20, Unit.PIXELS);
		itemValues[7] = cbxCustomListValues;
		
		List<CustomList> customLists = new CustomListDao().getAllCustomLists();

		for(int i=0; i<customLists.size(); i++)
		{
			cbxCustomLists.addItem(customLists.get(i).getCustomListId());
			cbxCustomLists.setItemCaption(customLists.get(i).getCustomListId(), customLists.get(i).getCustomListName());
			cbxCustomLists.setWidth(100, Unit.PERCENTAGE);
		}
				
		cbxCustomLists.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeFilterCustomList(cbxCustomLists, cbxCustomListValues);
            }
        });
		
		
		//Loading expression values
		ComboBox cbxExpression = new ComboBox("");
		cbxExpression.setStyleName("tiny");
		cbxExpression.addItem("and");
		cbxExpression.setItemCaption("and", "AND");
		cbxExpression.addItem("or");
		cbxExpression.setItemCaption("or", "OR");		
		cbxExpression.setHeight(20, Unit.PIXELS);
		itemValues[8] = cbxExpression;				

		cbxDataSource.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeFilterCbxDataSource(cbxDataSource, cbxDataSourceField, cbxFilterOperator);
            }
        });
		
		this.tblReportFilters.addItem(itemValues, itemId);
		this.tblReportFilters.select(itemId);
		
	}
	
	private void addReportColumn(Integer reportColumnId)
	{
		//Get all selected data sources 
		Integer itemId = reportColumnId;
		if(itemId == null)
		{	
			this.lastReportColumnItemId = this.lastReportColumnItemId - 1;
			itemId = this.lastReportColumnItemId;
		}
		
		Object[] rptColItemValues = new Object[5];
		
		//Dummy initial column
		CheckBox chxSelect = new CheckBox();
		chxSelect.setStyleName("tiny");
		chxSelect.setVisible(false);
		chxSelect.setHeight(20, Unit.PIXELS);
		rptColItemValues[0] = chxSelect;

		TextField txtRptColumnName = new TextField("");
		txtRptColumnName.setStyleName("tiny");
		txtRptColumnName.setHeight(20, Unit.PIXELS);
		rptColItemValues[1] = txtRptColumnName;
		
		TextField txtRptDbColumnName = new TextField("");
		txtRptDbColumnName.setStyleName("tiny");
		txtRptDbColumnName.setHeight(20, Unit.PIXELS);
		rptColItemValues[2] = txtRptDbColumnName;
				
		//Loading field types				
		ComboBox cbxDataType = new ComboBox("");
		cbxDataType.setStyleName("tiny");
		cbxDataType.setRequired(true);
		cbxDataType.setRequiredError("This field is required.");
		cbxDataType.setHeight(20, Unit.PIXELS);
		
		for(int j=0; j<dbfieldTypes.length; j++)
		{
			cbxDataType.addItem(dbfieldTypes[j]);
			cbxDataType.setItemCaption(dbfieldTypes[j], dbfieldTypes[j]);
		}
		rptColItemValues[3] = cbxDataType;
		
		OptionGroup optGrpDataSourceFieldCols = new OptionGroup();
		optGrpDataSourceFieldCols.setMultiSelect(true);
		optGrpDataSourceFieldCols.setStyleName("small");
		rptColItemValues[4] = optGrpDataSourceFieldCols;
		

		cbxDataType.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeRptColumnDataType(cbxDataType, optGrpDataSourceFieldCols);
            }
        });
		

		this.tblReportColumns.addItem(rptColItemValues, itemId);
		this.tblReportColumns.select(itemId);
	}
	
	private void onChangeRptColumnDataType(ComboBox cbxDataType, OptionGroup optGrpDataSourceFieldCols)
	{
		optGrpDataSourceFieldCols.setContainerDataSource(null);
		
		Set<Item> selectedDataSrcExperiments = (Set<Item>) optGrpPnlExperiments.getValue();
		for (Object selectedDataSrcExperiment : selectedDataSrcExperiments)
		{
			Integer selectedExperimentId = Integer.parseInt(selectedDataSrcExperiment.toString());
			Experiment selectedExperiment = new ExperimentDao().getExperimentById(selectedExperimentId);
			List<ExperimentField> selectedExperimentFields = new ExperimentFieldDao().getAllExperimentFieldsByExperiment(selectedExperiment);
			
			for(int i=0; selectedExperimentFields!=null && i<selectedExperimentFields.size(); i++)
			{
				if(selectedExperimentFields.get(i).getExpFieldType().equals(cbxDataType.getValue().toString()))
				{
					optGrpDataSourceFieldCols.addItem("expfield_" + selectedExperimentFields.get(i).getExpFieldId());
					optGrpDataSourceFieldCols.setItemCaption("expfield_" + selectedExperimentFields.get(i).getExpFieldId(), selectedExperiment.getExpName() + " / " + selectedExperimentFields.get(i).getExpFieldName());
				}
				else
				{
					String selectedDataType = cbxDataType.getValue().toString();
					String experimentFieldDataType = selectedExperimentFields.get(i).getExpFieldType();
					
					if((selectedDataType.startsWith("varchar") || selectedDataType.startsWith("char")
	                || selectedDataType.startsWith("text") || selectedDataType.startsWith("nvarchar")
	                || selectedDataType.startsWith("nchar") || selectedDataType.startsWith("ntext")))
					{
						if(selectedDataType.equals("nvarchar(max)") || selectedDataType.equals("varchar(max)") || selectedDataType.equals("text")
								&& (experimentFieldDataType.startsWith("varchar") || experimentFieldDataType.startsWith("char")
						                || experimentFieldDataType.startsWith("text") || experimentFieldDataType.startsWith("nvarchar")
						                || experimentFieldDataType.startsWith("nchar") || experimentFieldDataType.startsWith("ntext")))
						{
							optGrpDataSourceFieldCols.addItem("expfield_" + selectedExperimentFields.get(i).getExpFieldId());
							optGrpDataSourceFieldCols.setItemCaption("expfield_" + selectedExperimentFields.get(i).getExpFieldId(), selectedExperiment.getExpName() + " / " + selectedExperimentFields.get(i).getExpFieldName());
						}
						else
						{
							if((selectedDataType.startsWith("varchar") || selectedDataType.startsWith("char")
					                || selectedDataType.startsWith("text") || selectedDataType.startsWith("nvarchar")
					                || selectedDataType.startsWith("nchar") || selectedDataType.startsWith("ntext"))
									&& (experimentFieldDataType.startsWith("varchar") || experimentFieldDataType.startsWith("char")
							                || experimentFieldDataType.startsWith("text") || experimentFieldDataType.startsWith("nvarchar")
							                || experimentFieldDataType.startsWith("nchar") || experimentFieldDataType.startsWith("ntext")))
							{
								String selectedDataTypeStringStrLength = selectedDataType.replaceAll("\\D+","");
								String expFieldDataTypeStringStrLength = experimentFieldDataType.replaceAll("\\D+","");	
								
								if(selectedDataTypeStringStrLength != null && selectedDataTypeStringStrLength.length() > 0 && expFieldDataTypeStringStrLength != null && expFieldDataTypeStringStrLength.length() > 0)
								{
									if(Integer.parseInt(selectedDataTypeStringStrLength)>Integer.parseInt(expFieldDataTypeStringStrLength))
									{
										optGrpDataSourceFieldCols.addItem("expfield_" + selectedExperimentFields.get(i).getExpFieldId());
										optGrpDataSourceFieldCols.setItemCaption("expfield_" + selectedExperimentFields.get(i).getExpFieldId(), selectedExperiment.getExpName() + " / " + selectedExperimentFields.get(i).getExpFieldName());
									
									}
								}
							}							
						}
					}
				}
			}
		}
		
		Set<Item> selectedDataSrcFpyReports = (Set<Item>) optGrpPnlFpyReports.getValue();
		for (Object selectedDataSrcFpyReport : selectedDataSrcFpyReports)
		{
			Integer selectedDataSrcFpyReportId = Integer.parseInt(selectedDataSrcFpyReport.toString());
			FirstPassYieldReport selectedFpyReport = new FirstPassYieldReportDao().getFirstPassYieldReportById(selectedDataSrcFpyReportId);
			List<FirstPassYieldInfoField> selectedFpyInfoFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(selectedDataSrcFpyReportId);
			
			for(int i=0; selectedFpyInfoFields!=null && i<selectedFpyInfoFields.size(); i++)
			{
				if(selectedFpyInfoFields.get(i).getExperimentField().getExpFieldType().equals(cbxDataType.getValue().toString()))
				{
					optGrpDataSourceFieldCols.addItem("fpyfield_" + selectedFpyInfoFields.get(i).getExperimentField().getExpFieldId());
					optGrpDataSourceFieldCols.setItemCaption("fpyfield_" + selectedFpyInfoFields.get(i).getExperimentField().getExpFieldId(), "FPY: " + selectedFpyReport.getFpyReportName() + " / " + selectedFpyInfoFields.get(i).getExperimentField().getExpFieldName());
				}
				else
				{
					String selectedDataType = cbxDataType.getValue().toString();
					String experimentFieldDataType = selectedFpyInfoFields.get(i).getExperimentField().getExpFieldType();
					
					if((selectedDataType.startsWith("varchar") || selectedDataType.startsWith("char")
	                || selectedDataType.startsWith("text") || selectedDataType.startsWith("nvarchar")
	                || selectedDataType.startsWith("nchar") || selectedDataType.startsWith("ntext")))
					{
						if(selectedDataType.equals("nvarchar(max)") || selectedDataType.equals("varchar(max)") || selectedDataType.equals("text")
								&& (experimentFieldDataType.startsWith("varchar") || experimentFieldDataType.startsWith("char")
						                || experimentFieldDataType.startsWith("text") || experimentFieldDataType.startsWith("nvarchar")
						                || experimentFieldDataType.startsWith("nchar") || experimentFieldDataType.startsWith("ntext")))
						{
							optGrpDataSourceFieldCols.addItem("fpyfield_" + selectedFpyInfoFields.get(i).getExperimentField().getExpFieldId());
							optGrpDataSourceFieldCols.setItemCaption("fpyfield_" + selectedFpyInfoFields.get(i).getExperimentField().getExpFieldId(), "FPY: " + selectedFpyReport.getFpyReportName() + " / " + selectedFpyInfoFields.get(i).getExperimentField().getExpFieldName());
						}
						else
						{
							if((selectedDataType.startsWith("varchar") || selectedDataType.startsWith("char")
					                || selectedDataType.startsWith("text") || selectedDataType.startsWith("nvarchar")
					                || selectedDataType.startsWith("nchar") || selectedDataType.startsWith("ntext"))
									&& (experimentFieldDataType.startsWith("varchar") || experimentFieldDataType.startsWith("char")
							                || experimentFieldDataType.startsWith("text") || experimentFieldDataType.startsWith("nvarchar")
							                || experimentFieldDataType.startsWith("nchar") || experimentFieldDataType.startsWith("ntext")))
							{
								String selectedDataTypeStringStrLength = selectedDataType.replaceAll("\\D+","");
								String expFieldDataTypeStringStrLength = experimentFieldDataType.replaceAll("\\D+","");	
								
								if(selectedDataTypeStringStrLength != null && selectedDataTypeStringStrLength.length() > 0 && expFieldDataTypeStringStrLength != null && expFieldDataTypeStringStrLength.length() > 0)
								{
									if(Integer.parseInt(selectedDataTypeStringStrLength)>Integer.parseInt(expFieldDataTypeStringStrLength))
									{
										optGrpDataSourceFieldCols.addItem("fpyfield_" + selectedFpyInfoFields.get(i).getExperimentField().getExpFieldId());
										optGrpDataSourceFieldCols.setItemCaption("fpyfield_" + selectedFpyInfoFields.get(i).getExperimentField().getExpFieldId(), "FPY: " + selectedFpyReport.getFpyReportName() + " / " + selectedFpyInfoFields.get(i).getExperimentField().getExpFieldName());
									}
								}
							}							
						}
					}
				}
			}			


			if(cbxDataType.getValue().toString().contains("date"))
			{
				optGrpDataSourceFieldCols.addItem("fpyfield_datetime");
				optGrpDataSourceFieldCols.setItemCaption("fpyfield_datetime", "FPY: " + selectedFpyReport.getFpyReportName() + " / Datetime ");
			}
			if((cbxDataType.getValue().toString().startsWith("varchar") || cbxDataType.getValue().toString().startsWith("char")
	                || cbxDataType.getValue().toString().startsWith("text") || cbxDataType.getValue().toString().startsWith("nvarchar")
	                || cbxDataType.getValue().toString().startsWith("nchar") || cbxDataType.getValue().toString().startsWith("ntext")))
			{
				optGrpDataSourceFieldCols.addItem("fpyfield_sn");
				optGrpDataSourceFieldCols.setItemCaption("fpyfield_sn", "FPY: " + selectedFpyReport.getFpyReportName() + " / Serial Number");

				optGrpDataSourceFieldCols.addItem("fpyfield_result");
				optGrpDataSourceFieldCols.setItemCaption("fpyfield_result", "FPY: " + selectedFpyReport.getFpyReportName() + " / Result");
			}
		}		
		
		
		Set<Item> selectedDataSrcFtyReports = (Set<Item>) optGrpPnlFtyReports.getValue();
		for (Object selectedDataSrcFtyReport : selectedDataSrcFtyReports)
		{
			Integer selectedDataSrcFtyReportId = Integer.parseInt(selectedDataSrcFtyReport.toString());
			FirstTimeYieldReport selectedFpyReport = new FirstTimeYieldReportDao().getFirstTimeYieldReportById(selectedDataSrcFtyReportId);
			List<FirstTimeYieldInfoField> selectedFtyInfoFields = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(selectedDataSrcFtyReportId);
			
			for(int i=0; selectedFtyInfoFields!=null && i<selectedFtyInfoFields.size(); i++)
			{
				if(selectedFtyInfoFields.get(i).getExperimentField().getExpFieldType().equals(cbxDataType.getValue().toString()))
				{
					optGrpDataSourceFieldCols.addItem("ftyfield_" + selectedFtyInfoFields.get(i).getExperimentField().getExpFieldId());
					optGrpDataSourceFieldCols.setItemCaption("ftyfield_" + selectedFtyInfoFields.get(i).getExperimentField().getExpFieldId(), "FTY: " + selectedFpyReport.getFtyReportName() + " / " + selectedFtyInfoFields.get(i).getExperimentField().getExpFieldName());
				}
				else
				{
					String selectedDataType = cbxDataType.getValue().toString();
					String experimentFieldDataType = selectedFtyInfoFields.get(i).getExperimentField().getExpFieldType();
					
					if((selectedDataType.startsWith("varchar") || selectedDataType.startsWith("char")
	                || selectedDataType.startsWith("text") || selectedDataType.startsWith("nvarchar")
	                || selectedDataType.startsWith("nchar") || selectedDataType.startsWith("ntext")))
					{
						if(selectedDataType.equals("nvarchar(max)") || selectedDataType.equals("varchar(max)") || selectedDataType.equals("text")
								&& (experimentFieldDataType.startsWith("varchar") || experimentFieldDataType.startsWith("char")
						                || experimentFieldDataType.startsWith("text") || experimentFieldDataType.startsWith("nvarchar")
						                || experimentFieldDataType.startsWith("nchar") || experimentFieldDataType.startsWith("ntext")))
						{
							optGrpDataSourceFieldCols.addItem("ftyfield_" + selectedFtyInfoFields.get(i).getExperimentField().getExpFieldId());
							optGrpDataSourceFieldCols.setItemCaption("ftyfield_" + selectedFtyInfoFields.get(i).getExperimentField().getExpFieldId(), "FTY: " + selectedFpyReport.getFtyReportName() + " / " + selectedFtyInfoFields.get(i).getExperimentField().getExpFieldName());
						}
						else
						{
							if((selectedDataType.startsWith("varchar") || selectedDataType.startsWith("char")
					                || selectedDataType.startsWith("text") || selectedDataType.startsWith("nvarchar")
					                || selectedDataType.startsWith("nchar") || selectedDataType.startsWith("ntext"))
									&& (experimentFieldDataType.startsWith("varchar") || experimentFieldDataType.startsWith("char")
							                || experimentFieldDataType.startsWith("text") || experimentFieldDataType.startsWith("nvarchar")
							                || experimentFieldDataType.startsWith("nchar") || experimentFieldDataType.startsWith("ntext")))
							{
								String selectedDataTypeStringStrLength = selectedDataType.replaceAll("\\D+","");
								String expFieldDataTypeStringStrLength = experimentFieldDataType.replaceAll("\\D+","");	
								
								if(selectedDataTypeStringStrLength != null && selectedDataTypeStringStrLength.length() > 0 && expFieldDataTypeStringStrLength != null && expFieldDataTypeStringStrLength.length() > 0)
								{
									if(Integer.parseInt(selectedDataTypeStringStrLength)>Integer.parseInt(expFieldDataTypeStringStrLength))
									{
										optGrpDataSourceFieldCols.addItem("ftyfield_" + selectedFtyInfoFields.get(i).getExperimentField().getExpFieldId());
										optGrpDataSourceFieldCols.setItemCaption("ftyfield_" + selectedFtyInfoFields.get(i).getExperimentField().getExpFieldId(), "FTY: " + selectedFpyReport.getFtyReportName() + " / " + selectedFtyInfoFields.get(i).getExperimentField().getExpFieldName());
									}
								}
							}							
						}
					}
				}
			}
			
			if(cbxDataType.getValue().toString().contains("date"))
			{
				optGrpDataSourceFieldCols.addItem("ftyfield_datetime");
				optGrpDataSourceFieldCols.setItemCaption("ftyfield_datetime", "FTY: " + selectedFpyReport.getFtyReportName() + " / Datetime ");
			}
			
			if((cbxDataType.getValue().toString().startsWith("varchar") || cbxDataType.getValue().toString().startsWith("char")
	                || cbxDataType.getValue().toString().startsWith("text") || cbxDataType.getValue().toString().startsWith("nvarchar")
	                || cbxDataType.getValue().toString().startsWith("nchar") || cbxDataType.getValue().toString().startsWith("ntext")))
			{
				optGrpDataSourceFieldCols.addItem("ftyfield_sn");
				optGrpDataSourceFieldCols.setItemCaption("ftyfield_sn", "FTY: " + selectedFpyReport.getFtyReportName() + " / Serial Number");

				optGrpDataSourceFieldCols.addItem("ftyfield_result");
				optGrpDataSourceFieldCols.setItemCaption("ftyfield_result", "FTY: " + selectedFpyReport.getFtyReportName() + " / Result");
			}
		}
		
		Set<Item> selectedDataSrcTargetReports = (Set<Item>) optGrpPnlTargetReports.getValue();
		for (Object selectedDataSrcTargetReport : selectedDataSrcTargetReports)
		{
			Integer selectedDataSrcTargetReportId = Integer.parseInt(selectedDataSrcTargetReport.toString());
			TargetReport selectedTargetReport = new TargetReportDao().getTargetReportById(selectedDataSrcTargetReportId);
			List<TargetColumn> selectedTargetColumns = new TargetColumnDao().getTargetColumnByTargetReportId(selectedDataSrcTargetReportId);
			
			for(int i=0; selectedTargetColumns!=null && i<selectedTargetColumns.size(); i++)
			{
				if(selectedTargetColumns.get(i).getExperimentField().getExpFieldType().equals(cbxDataType.getValue().toString()))
				{
					optGrpDataSourceFieldCols.addItem("tgtfield_" + selectedTargetColumns.get(i).getExperimentField().getExpFieldId());
					optGrpDataSourceFieldCols.setItemCaption("tgtfield_" + selectedTargetColumns.get(i).getExperimentField().getExpFieldId(), "Target: " + selectedTargetReport.getTargetReportName() + " / " + selectedTargetColumns.get(i).getExperimentField().getExpFieldName());
				} 
				else
				{
					String selectedDataType = cbxDataType.getValue().toString();
					String experimentFieldDataType = selectedTargetColumns.get(i).getExperimentField().getExpFieldType();
					
					if((selectedDataType.startsWith("varchar") || selectedDataType.startsWith("char")
	                || selectedDataType.startsWith("text") || selectedDataType.startsWith("nvarchar")
	                || selectedDataType.startsWith("nchar") || selectedDataType.startsWith("ntext")))
					{
						if(selectedDataType.equals("nvarchar(max)") || selectedDataType.equals("varchar(max)") || selectedDataType.equals("text")
								&& (experimentFieldDataType.startsWith("varchar") || experimentFieldDataType.startsWith("char")
						                || experimentFieldDataType.startsWith("text") || experimentFieldDataType.startsWith("nvarchar")
						                || experimentFieldDataType.startsWith("nchar") || experimentFieldDataType.startsWith("ntext")))
						{
							optGrpDataSourceFieldCols.addItem("tgtfield_" + selectedTargetColumns.get(i).getExperimentField().getExpFieldId());
							optGrpDataSourceFieldCols.setItemCaption("tgtfield_" + selectedTargetColumns.get(i).getExperimentField().getExpFieldId(), "Target: " + selectedTargetReport.getTargetReportName() + " / " + selectedTargetColumns.get(i).getExperimentField().getExpFieldName());
						}
						else
						{
							if((selectedDataType.startsWith("varchar") || selectedDataType.startsWith("char")
					                || selectedDataType.startsWith("text") || selectedDataType.startsWith("nvarchar")
					                || selectedDataType.startsWith("nchar") || selectedDataType.startsWith("ntext"))
									&& (experimentFieldDataType.startsWith("varchar") || experimentFieldDataType.startsWith("char")
							                || experimentFieldDataType.startsWith("text") || experimentFieldDataType.startsWith("nvarchar")
							                || experimentFieldDataType.startsWith("nchar") || experimentFieldDataType.startsWith("ntext")))
							{
								String selectedDataTypeStringStrLength = selectedDataType.replaceAll("\\D+","");
								String expFieldDataTypeStringStrLength = experimentFieldDataType.replaceAll("\\D+","");	
								
								if(selectedDataTypeStringStrLength != null && selectedDataTypeStringStrLength.length() > 0 && expFieldDataTypeStringStrLength != null && expFieldDataTypeStringStrLength.length() > 0)
								{
									if(Integer.parseInt(selectedDataTypeStringStrLength)>Integer.parseInt(expFieldDataTypeStringStrLength))
									{
										optGrpDataSourceFieldCols.addItem("tgtfield_" + selectedTargetColumns.get(i).getExperimentField().getExpFieldId());
										optGrpDataSourceFieldCols.setItemCaption("tgtfield_" + selectedTargetColumns.get(i).getExperimentField().getExpFieldId(), "Target: " + selectedTargetReport.getTargetReportName() + " / " + selectedTargetColumns.get(i).getExperimentField().getExpFieldName());
									}
								}
							}							
						}
					}
				}
			}		
		}		
	}
	
	private void loadTblEnrichmentRulesData()
	{
		this.tblColumnsEnrichment.setContainerDataSource(null);
		this.tblColumnsEnrichment.setStyleName("small");
		this.tblColumnsEnrichment.addContainerProperty("*", CheckBox.class, null);
		this.tblColumnsEnrichment.addContainerProperty("Report Column", ComboBox.class, null);
		this.tblColumnsEnrichment.addContainerProperty("Operator", ComboBox.class, null);
		this.tblColumnsEnrichment.addContainerProperty("Value", TextField.class, null);
		this.tblColumnsEnrichment.addContainerProperty("Enrichment Type", ComboBox.class, null);
		this.tblColumnsEnrichment.addContainerProperty("Custom List", ComboBox.class, null);
		this.tblColumnsEnrichment.addContainerProperty("List Value", ComboBox.class, null);
		this.tblColumnsEnrichment.addContainerProperty("Static Value", TextField.class, null);
		this.tblColumnsEnrichment.setEditable(true);
		this.tblColumnsEnrichment.setPageLength(0);
		this.tblColumnsEnrichment.setColumnWidth("*", 20);
	}

	private void addTblEnrichmentRule(Integer verticalRptEnrichmentRuleId)
	{		
		Integer itemId = verticalRptEnrichmentRuleId;
		if(itemId == null)
		{	
			this.lastVerticalRptEnrichmentItemId = this.lastVerticalRptEnrichmentItemId - 1;
			itemId = this.lastVerticalRptEnrichmentItemId;
		}
		
		Object[] itemValues = new Object[8];
		
		//Dummy Initial Column
		CheckBox chxSelect = new CheckBox();
		chxSelect.setStyleName("tiny");
		chxSelect.setVisible(false);
		chxSelect.setHeight(20, Unit.PIXELS);
		itemValues[0] = chxSelect;
		
		//Fill Report Columns ComboBox
		ComboBox cbxRptColumnSource = new ComboBox("");
		cbxRptColumnSource.setStyleName("tiny");
		cbxRptColumnSource.setRequired(true);
		cbxRptColumnSource.setRequiredError("This field is required.");
		
		Collection rptColumnItemIds = this.tblReportColumns.getContainerDataSource().getItemIds();
		
		for (Object rptColumnItemIdObj : rptColumnItemIds) 
		{
			int rptColumnItemId = (int)rptColumnItemIdObj;
			Item tblEnrichmentRuleRowItem = this.tblReportColumns.getContainerDataSource().getItem(rptColumnItemId);
			cbxRptColumnSource.addItem(rptColumnItemId);
			cbxRptColumnSource.setItemCaption(rptColumnItemId, ((TextField)(tblEnrichmentRuleRowItem.getItemProperty("Column Name").getValue())).getValue().toString());
			cbxRptColumnSource.setWidth(100, Unit.PERCENTAGE);
		}
		
		cbxRptColumnSource.setHeight(20, Unit.PIXELS);
		itemValues[1] = cbxRptColumnSource;
		
				
		//Fill Operator ComboBox
		ComboBox cbxOperator = new ComboBox("");
		cbxOperator.setContainerDataSource(null);
		cbxOperator.setStyleName("tiny");		
		cbxOperator.addItem("contains");
		cbxOperator.setItemCaption("contains", "contains");
		cbxOperator.addItem("doesnotcontain");
		cbxOperator.setItemCaption("doesnotcontain", "does not contain");
		cbxOperator.addItem("doesnotstartwith");
		cbxOperator.setItemCaption("doesnotstartwith", "does not start with");
		cbxOperator.addItem("endswith");
		cbxOperator.setItemCaption("endswith", "ends with");
		cbxOperator.addItem("is");
		cbxOperator.setItemCaption("is", "is");
		cbxOperator.addItem("isempty");
		cbxOperator.setItemCaption("isempty", "is empty");
		cbxOperator.addItem("isnot");
		cbxOperator.setItemCaption("isnot", "is not");
		cbxOperator.addItem("isnotempty");
		cbxOperator.setItemCaption("isnotempty", "is not empty");
		cbxOperator.addItem("startswith");
		cbxOperator.setItemCaption("startswith", "starts with");
		cbxOperator.addItem("matchesregex");
		cbxOperator.setItemCaption("matchesregex", "matches Regular Expression");
		cbxOperator.select("is");
		cbxOperator.setHeight(20, Unit.PIXELS);
		itemValues[2] = cbxOperator;
		
		//Comparison Value TextField
		TextField txtComparisonValue = new TextField();
		txtComparisonValue.addStyleName("tiny");
		txtComparisonValue.setWidth(100, Unit.PIXELS);
		txtComparisonValue.setHeight(20, Unit.PIXELS);
		itemValues[3] = txtComparisonValue;
		
		//Fill Enrichment Type ComboBox
		ComboBox cbxEnrichmentType = new ComboBox("");
		cbxEnrichmentType.setContainerDataSource(null);
		cbxEnrichmentType.setStyleName("tiny");		
		cbxEnrichmentType.addItem("customlist");
		cbxEnrichmentType.setItemCaption("customlist", "Custom List XREF");
		cbxEnrichmentType.addItem("staticvalue");
		cbxEnrichmentType.setItemCaption("staticvalue", "Static Value");
		cbxEnrichmentType.addItem("positivenumber");
		cbxEnrichmentType.setItemCaption("positivenumber", "Cast to Positive Number");
		cbxEnrichmentType.addItem("negativenumber");
		cbxEnrichmentType.setItemCaption("negativenumber", "Cast to Negative Number");
		cbxEnrichmentType.select("customlist");
		cbxEnrichmentType.setId(itemId.toString()); //Item Id to be reused on Change event

		cbxEnrichmentType.setHeight(20, Unit.PIXELS);
		itemValues[4] = cbxEnrichmentType;
		
		//Fill Custom List ComboBox
		ComboBox cbxCustomList = new ComboBox("");
		cbxCustomList.setContainerDataSource(null);
		cbxCustomList.setStyleName("tiny");		
		cbxCustomList.setId(itemId.toString()); //Item Id to be reused on Change event
		List<CustomList> customLists = new CustomListDao().getAllCustomLists();

		for(int i=0; i<customLists.size(); i++)
		{
			cbxCustomList.addItem(customLists.get(i).getCustomListId());
			cbxCustomList.setItemCaption(customLists.get(i).getCustomListId(), customLists.get(i).getCustomListName());
			cbxCustomList.setWidth(100, Unit.PERCENTAGE);
		}
		
		cbxCustomList.setHeight(20, Unit.PIXELS);
		itemValues[5] = cbxCustomList;
		
		//Fill Custom List Value ComboBox
		ComboBox cbxCustomListValue = new ComboBox("");
		cbxCustomListValue.setContainerDataSource(null);
		cbxCustomListValue.setStyleName("tiny");	
		cbxCustomListValue.setHeight(20, Unit.PIXELS);
		itemValues[6] = cbxCustomListValue;
		
		//Static Value TextField
		TextField txtStaticValue = new TextField();
		txtStaticValue.addStyleName("tiny");
		txtStaticValue.setWidth(100, Unit.PIXELS);
		txtStaticValue.setEnabled(false);
		txtStaticValue.setHeight(20, Unit.PIXELS);
		itemValues[7] = txtStaticValue;
		

		cbxEnrichmentType.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeEnrichmentType(cbxCustomList, cbxCustomListValue, txtStaticValue, cbxEnrichmentType);
            }
        });

		cbxCustomList.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeCustomList(cbxCustomListValue, cbxCustomList);
            }
        });		
		
		this.tblColumnsEnrichment.addItem(itemValues, itemId);		
	}
	
	private void onChangeEnrichmentType(ComboBox cbxCustomList, ComboBox cbxCustomListValue, TextField txtStaticValue, ComboBox cbxEnrichmentType)
	{
		if(cbxEnrichmentType.getValue() != null && !cbxEnrichmentType.getValue().toString().isEmpty())
		{
			if("customlist".equals(cbxEnrichmentType.getValue()))
			{
				cbxCustomList.setEnabled(true);
				cbxCustomListValue.setEnabled(true);
				txtStaticValue.setValue("");
				txtStaticValue.setEnabled(false);
			}
			if("staticvalue".equals(cbxEnrichmentType.getValue()))
			{
				cbxCustomList.setValue(null);
				cbxCustomList.setEnabled(false);
				cbxCustomListValue.setValue(null);
				cbxCustomListValue.setEnabled(false);
				txtStaticValue.setEnabled(true);
			}
			if(!"customlist".equals(cbxEnrichmentType.getValue()) && !"staticvalue".equals(cbxEnrichmentType.getValue()))
			{
				cbxCustomList.setValue(null);
				cbxCustomList.setEnabled(false);
				cbxCustomListValue.setValue(null);
				cbxCustomListValue.setEnabled(false);
				txtStaticValue.setValue("");
				txtStaticValue.setEnabled(false);
			}
			
		}
	}

	private void onChangeCustomList(ComboBox cbxCustomListValue, ComboBox cbxCustomList)
	{
		cbxCustomListValue.setContainerDataSource(null);
		
		if(cbxCustomList.getValue() != null && !cbxCustomList.getValue().toString().isEmpty())
		{
			Integer selectedCustomListId = (Integer)cbxCustomList.getValue();
			
			List<CustomListValue> customListValues = new CustomListValueDao().getAllCustomListValuesByCustomList(new CustomListDao().getCustomListById(selectedCustomListId));
			for(int i=0; i<customListValues.size(); i++)
			{
				cbxCustomListValue.addItem(customListValues.get(i).getCustomListValueId());
				cbxCustomListValue.setItemCaption(customListValues.get(i).getCustomListValueId(), customListValues.get(i).getCustomListValueString());
			}
		}
	}
	
	private void onChangeFilterCbxDataSource(ComboBox cbxDataSource, ComboBox cbxDataSourceField, ComboBox cbxFilterOperator)
	{		
		Integer dataSourceId = Integer.parseInt((cbxDataSource.getValue().toString().substring(4)));
		
		switch(cbxDataSource.getValue().toString().substring(0, 4))
		{
		case "exp_":
			
			List<ExperimentField> expFields = new ExperimentFieldDao().getActiveExperimentFields(new ExperimentDao().getExperimentById(dataSourceId));
			cbxDataSourceField.setContainerDataSource(null);
			for(int i=0; expFields != null && i<expFields.size(); i++)
			{
				cbxDataSourceField.addItem("exp_" + expFields.get(i).getExpFieldId());
				cbxDataSourceField.setItemCaption("exp_" + expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
			}
			break;

		case "fpy_":
			List<FirstPassYieldInfoField> fpyFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(dataSourceId);
			cbxDataSourceField.setContainerDataSource(null);
			for(int i=0; fpyFields != null && i<fpyFields.size(); i++)
			{
				cbxDataSourceField.addItem("fpy_" + fpyFields.get(i).getFpyInfoFieldId());
				cbxDataSourceField.setItemCaption("fpy_" + fpyFields.get(i).getFpyInfoFieldId(), fpyFields.get(i).getFpyInfoFieldLabel());
			}

			cbxDataSourceField.addItem("fpy_datetime");
			cbxDataSourceField.setItemCaption("fpy_datetime", "Datetime (FPY)");

			cbxDataSourceField.addItem("fpy_sn");
			cbxDataSourceField.setItemCaption("fpy_sn", "Serial Number (FPY)");

			cbxDataSourceField.addItem("fpy_result");
			cbxDataSourceField.setItemCaption("fpy_result", "Result (FPY)");
			
			break;

		case "fty_":
			List<FirstTimeYieldInfoField> ftyFields = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(dataSourceId);
			cbxDataSourceField.setContainerDataSource(null);
			for(int i=0; ftyFields != null && i<ftyFields.size(); i++)
			{
				cbxDataSourceField.addItem("fty_" + ftyFields.get(i).getFtyInfoFieldId());
				cbxDataSourceField.setItemCaption("fty_" + ftyFields.get(i).getFtyInfoFieldId(), ftyFields.get(i).getFtyInfoFieldLabel());

				cbxDataSourceField.addItem("fty_datetime");
				cbxDataSourceField.setItemCaption("fty_datetime", "Datetime (FTY)");

				cbxDataSourceField.addItem("fty_sn");
				cbxDataSourceField.setItemCaption("fty_sn", "Serial Number (FTY)");

				cbxDataSourceField.addItem("fty_result");
				cbxDataSourceField.setItemCaption("fty_result", "Result (FTY)");			
			}
			break;

		case "tgt_":
			List<TargetColumn> tgtCols = new TargetColumnDao().getTargetColumnByTargetReportId(dataSourceId);
			cbxDataSourceField.setContainerDataSource(null);
			for(int i=0; tgtCols != null && i<tgtCols.size(); i++)
			{
				cbxDataSourceField.addItem("tgt_" + tgtCols.get(i).getTargetColumnId());
				cbxDataSourceField.setItemCaption("tgt_" + tgtCols.get(i).getTargetColumnId(), tgtCols.get(i).getTargetColumnLabel());
			}
			break;
				
			default:
					break;
		}
		
		cbxDataSourceField.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeFilterCbxDataSourceField(cbxDataSourceField, cbxFilterOperator);
            }
        });
	}

	private void onChangeFilterCbxDataSourceField(ComboBox cbxDataSourceField, ComboBox cbxFilterOperator)
	{
		Integer filterDataSourceFieldId = -1;
		
		if(!cbxDataSourceField.getValue().toString().equals("fpy_sn") && !cbxDataSourceField.getValue().toString().equals("fpy_result") && !cbxDataSourceField.getValue().toString().equals("fpy_datetime") 
				&& !cbxDataSourceField.getValue().toString().equals("fty_sn") && !cbxDataSourceField.getValue().toString().equals("fty_result") && !cbxDataSourceField.getValue().toString().equals("fty_datetime"))
			filterDataSourceFieldId = Integer.parseInt((cbxDataSourceField.getValue().toString().substring(4)));
		
		//Get Data Field Type
		String dataFieldType = "varchar";

		switch(cbxDataSourceField.getValue().toString().substring(0, 4))
		{
		
		case "exp_":
			
			ExperimentField expField = new ExperimentFieldDao().getExperimentFieldById(filterDataSourceFieldId);
			dataFieldType = expField.getExpFieldType();
			break;

		case "fpy_":
			if(cbxDataSourceField.getValue().toString().equals("fpy_datetime"))
				dataFieldType = "datetime";
			else if(cbxDataSourceField.getValue().toString().equals("fpy_sn") || cbxDataSourceField.getValue().toString().equals("fpy_result"))
				dataFieldType = "varchar";
			else
			{
				ExperimentField fpyInfoExpField = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldById(filterDataSourceFieldId).getExperimentField(); 
				dataFieldType = fpyInfoExpField.getExpFieldType();
			}
			break;

		case "fty_":
			if(cbxDataSourceField.getValue().toString().equals("fty_datetime"))
				dataFieldType = "datetime";
			else if(cbxDataSourceField.getValue().toString().equals("fty_sn") || cbxDataSourceField.getValue().toString().equals("fty_result"))
				dataFieldType = "varchar";
			else
			{
				ExperimentField ftyInfoExpField = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldById(filterDataSourceFieldId).getExperimentField(); 
				dataFieldType = ftyInfoExpField.getExpFieldType();
			}
			break;

		case "tgt_":
			
			ExperimentField tgtColExpField = new TargetColumnDao().getTargetColumnById(filterDataSourceFieldId).getExperimentField(); 
			dataFieldType = tgtColExpField.getExpFieldType();
			break;
				
			default:
					break;
		}


		if(dataFieldType.contains("date"))
			fillCbxDateFilterOperators(cbxFilterOperator);
		else if(dataFieldType.startsWith("varchar") || dataFieldType.startsWith("char")
                || dataFieldType.startsWith("text") || dataFieldType.startsWith("nvarchar")
                || dataFieldType.startsWith("nchar") || dataFieldType.startsWith("ntext"))
				fillCbxStringFilterOperators(cbxFilterOperator);
		else 
			fillCbxNumericFilterOperators(cbxFilterOperator);		
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

    private void fillCbxNumericFilterOperators(ComboBox cbxNumericFilterOperators) 
    {
    	cbxNumericFilterOperators.setContainerDataSource(null);
        
        cbxNumericFilterOperators.addItem("isempty");
        cbxNumericFilterOperators.setItemCaption("isempty", "is empty");
        
        cbxNumericFilterOperators.addItem("isnotempty");
        cbxNumericFilterOperators.setItemCaption("isnotempty", "is not empty");

    	cbxNumericFilterOperators.addItem("equalto");
    	cbxNumericFilterOperators.setItemCaption("equalto", "equal to");

    	cbxNumericFilterOperators.addItem("lessthan");
    	cbxNumericFilterOperators.setItemCaption("lessthan", "less than");

    	cbxNumericFilterOperators.addItem("greaterthan");
    	cbxNumericFilterOperators.setItemCaption("greaterthan", "greater than");
        
    	cbxNumericFilterOperators.addItem("lessthanorequalto");
        cbxNumericFilterOperators.setItemCaption("lessthanorequalto", "less than or equal to");
        
        cbxNumericFilterOperators.addItem("greaterthanorequalto");
        cbxNumericFilterOperators.setItemCaption("greaterthanorequalto", "greater than or equal to");

        cbxNumericFilterOperators.addItem("between");
        cbxNumericFilterOperators.setItemCaption("between", "between");
        
        cbxNumericFilterOperators.addItem("notbetween");
        cbxNumericFilterOperators.setItemCaption("notbetween", "not between");
        
        cbxNumericFilterOperators.addItem("notequalto");
        cbxNumericFilterOperators.setItemCaption("notequalto", "not equal to");

        cbxNumericFilterOperators.addItem("notlessthan");
        cbxNumericFilterOperators.setItemCaption("notlessthan", "not less than");

        cbxNumericFilterOperators.addItem("notgreaterthan");
        cbxNumericFilterOperators.setItemCaption("notgreaterthan", "not greater than");
        
        cbxNumericFilterOperators.addItem("notequalto");
        cbxNumericFilterOperators.setItemCaption("notequalto", "not equal to");

        cbxNumericFilterOperators.addItem("notlessthanorequalto");
        cbxNumericFilterOperators.setItemCaption("notlessthanorequalto", "not less than or equal to");

        cbxNumericFilterOperators.addItem("notgreaterthanorequalto");
        cbxNumericFilterOperators.setItemCaption("notgreaterthanorequalto", "not greater than or equal to");
        
        cbxNumericFilterOperators.select("isnotempty");
    }   
    
    private void onChangeFilterCustomList(ComboBox cbxFilterCustomList, ComboBox cbxFilterCustomListValues)
	{
    	cbxFilterCustomListValues.setContainerDataSource(null);
		if(cbxFilterCustomList.getValue() != null && !cbxFilterCustomList.getValue().toString().isEmpty())
		{
			Integer selectedFilterCustomListId = (Integer)cbxFilterCustomList.getValue();
			
			List<CustomListValue> customListValues = new CustomListValueDao().getAllCustomListValuesByCustomList(new CustomListDao().getCustomListById(selectedFilterCustomListId));
			for(int i=0; i<customListValues.size(); i++)
			{
				cbxFilterCustomListValues.addItem(customListValues.get(i).getCustomListValueId());
				cbxFilterCustomListValues.setItemCaption(customListValues.get(i).getCustomListValueId(), customListValues.get(i).getCustomListValueString());
			}
		}
	}

	private void loadReportColumnsTable()
	{
		this.tblReportColumns.setContainerDataSource(null);
		this.tblReportColumns.setStyleName("small");
		this.tblReportColumns.addContainerProperty("*", CheckBox.class, null);
		this.tblReportColumns.addContainerProperty("Column Name", TextField.class, null);
		this.tblReportColumns.addContainerProperty("DB Column Name", TextField.class, null);
		this.tblReportColumns.addContainerProperty("Data Type", ComboBox.class, null);
		this.tblReportColumns.addContainerProperty("Data Source Column/Field", OptionGroup.class, null);
		this.tblReportColumns.setEditable(true);
		this.tblReportColumns.setPageLength(0);
		this.tblReportColumns.setColumnWidth("*", 20);
	}
	
	private void onSave()
	{
		ViewVerticalReport vwVerticalRpt = new ViewVerticalReport();
		vwVerticalRpt.setVwVerticalRptDbTableNameId(this.txtFtyRptCustomId.getValue());
		vwVerticalRpt.setVwVerticalRptName(this.txtFtyRptName.getValue());
		vwVerticalRpt.setVwVerticalRptDescription(this.txtFtyDescription.getValue());
		vwVerticalRpt.setVwVerticalRptIsActive(true);
		
		new ViewVerticalReportDao().addVwVerticalReport(vwVerticalRpt);
		
		Set<Item> selectedOptGrpPnlExperiments = (Set<Item>) optGrpPnlExperiments.getValue();
		for (Object selectedExperiment : selectedOptGrpPnlExperiments)
		{
			for(int i=0; activeExperiments != null && i<activeExperiments.size(); i++)
			{
				int selectedExperimentId = Integer.parseInt(selectedExperiment.toString());				
				if(activeExperiments.get(i).getExpId() == selectedExperimentId)
				{
					ViewVerticalReportByExperiment vwVerticalRptByExperiment = new ViewVerticalReportByExperiment();
					vwVerticalRptByExperiment.setExperiment(activeExperiments.get(i));
					vwVerticalRptByExperiment.setViewVerticalReport(vwVerticalRpt);
					new ViewVerticalReportByExperimentDao().addVwVerticalReportByExperiment(vwVerticalRptByExperiment);
					
					//Save filters attached to Experiment
					Collection dataSourceFiltersItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
					
					for (Object dataSourceFiltersItemIdObj : dataSourceFiltersItemIds) 
					{
						Item tblDataSourceFilterRowItem = this.tblReportFilters.getContainerDataSource().getItem(dataSourceFiltersItemIdObj);
						String dataSourceId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Data Source").getValue())).getValue().toString();
						if(("exp_" + selectedExperimentId).equals(dataSourceId))
						{
							ViewVerticalReportFilterByExpField vwVerticalRptFilterByExpField = new ViewVerticalReportFilterByExpField();
							vwVerticalRptFilterByExpField.setVwVerticalReportByExperiment(vwVerticalRptByExperiment);
							vwVerticalRptFilterByExpField.setExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString().substring(4))));
							vwVerticalRptFilterByExpField.setVwVerticalRptFilteByExpFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
							vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
							vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
							vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
						
							if(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == null || ((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == "")
								vwVerticalRptFilterByExpField.setVwVerticalRptFilteByExpFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
						
							new ViewVerticalReportFilterByExpFieldDao().addVwVerticalReportFilterByExpField(vwVerticalRptFilterByExpField);
						}
					}
					
					break;
				}
			}
		}

		Set<Item> selectedOptGrpPnlFpyRpts = (Set<Item>) optGrpPnlFpyReports.getValue();
		for (Object selectedFpyRpt : selectedOptGrpPnlFpyRpts)
		{
			for(int i=0; activeFpyReports != null && i<activeFpyReports.size(); i++)
			{
				int selectedFpyReportId = Integer.parseInt(selectedFpyRpt.toString());
				if(activeFpyReports.get(i).getFpyReportId() == selectedFpyReportId)
				{
					ViewVerticalReportByFpyRpt vwVerticalRptByFpyRpt = new ViewVerticalReportByFpyRpt();
					vwVerticalRptByFpyRpt.setFpyRpt(activeFpyReports.get(i));
					vwVerticalRptByFpyRpt.setViewVerticalReport(vwVerticalRpt);
					new ViewVerticalReportByFpyRptDao().addVwVerticalReportByFpyRpt(vwVerticalRptByFpyRpt);
					
					//Save filters attached to FPY
					Collection dataSourceFiltersItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
					
					for (Object dataSourceFiltersItemIdObj : dataSourceFiltersItemIds) 
					{
						Item tblDataSourceFilterRowItem = this.tblReportFilters.getContainerDataSource().getItem(dataSourceFiltersItemIdObj);
						String dataSourceId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Data Source").getValue())).getValue().toString();
						if(("fpy_" + selectedFpyReportId).equals(dataSourceId))
						{
							ViewVerticalReportFilterByFpyField vwVerticalRptFilterByFpyField = new ViewVerticalReportFilterByFpyField();
							vwVerticalRptFilterByFpyField.setVwVerticalFpyRpt(vwVerticalRptByFpyRpt);
						
							String fpyInfoFieldStrId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString();
							if(fpyInfoFieldStrId.equals("fpy_datetime") ||fpyInfoFieldStrId.equals("fpy_sn") ||fpyInfoFieldStrId.equals("fpy_result"))
							{
								vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyIsDateTimeExpField(fpyInfoFieldStrId.equals("fpy_datetime"));
								vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyIsResultExpField(fpyInfoFieldStrId.equals("fpy_result"));
								vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpySNExpField(fpyInfoFieldStrId.equals("fpy_sn"));
							}
							else
								vwVerticalRptFilterByFpyField.setFpyInfoField(new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldById(Integer.parseInt(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString().substring(4))));
						
							vwVerticalRptFilterByFpyField.setVwVerticalRptFilteByFpyFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
							vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
							vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
							vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
						
							if(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == null || ((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == "")
								vwVerticalRptFilterByFpyField.setVwVerticalRptFilteByFpyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
						
							new ViewVerticalReportFilterByFpyFieldDao().addVwVerticalReportFilterByFpyField(vwVerticalRptFilterByFpyField);
						}
					}				
					
					break;
				}
			}
		}
		
		Set<Item> selectedOptGrpPnlFtyRpts = (Set<Item>) optGrpPnlFtyReports.getValue();
		for (Object selectedFtyRpt : selectedOptGrpPnlFtyRpts)
		{
			for(int i=0; activeFtyReports != null && i<activeFtyReports.size(); i++)
			{
				int selectedFtyReportId = Integer.parseInt(selectedFtyRpt.toString());
				if(activeFtyReports.get(i).getFtyReportId() == selectedFtyReportId)
				{
					ViewVerticalReportByFtyRpt vwVerticalRptByFtyRpt = new ViewVerticalReportByFtyRpt();
					vwVerticalRptByFtyRpt.setFtyRpt(activeFtyReports.get(i));
					vwVerticalRptByFtyRpt.setViewVerticalReport(vwVerticalRpt);
					new ViewVerticalReportByFtyRptDao().addVwVerticalReportByFtyRpt(vwVerticalRptByFtyRpt);
					
					//Save filters attached to FTY
					Collection dataSourceFiltersItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
					
					for (Object dataSourceFiltersItemIdObj : dataSourceFiltersItemIds) 
					{
						Item tblDataSourceFilterRowItem = this.tblReportFilters.getContainerDataSource().getItem(dataSourceFiltersItemIdObj);
						String dataSourceId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Data Source").getValue())).getValue().toString();
						if(("fty_" + selectedFtyReportId).equals(dataSourceId))
						{
							ViewVerticalReportFilterByFtyField vwVerticalRptFilterByFtyField = new ViewVerticalReportFilterByFtyField();
							vwVerticalRptFilterByFtyField.setVwVerticalFtyRpt(vwVerticalRptByFtyRpt);
						
							String ftyInfoFieldStrId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString();
							if(ftyInfoFieldStrId.equals("fty_datetime") || ftyInfoFieldStrId.equals("fty_sn") ||ftyInfoFieldStrId.equals("fty_result"))
							{
								vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyIsDateTimeExpField(ftyInfoFieldStrId.equals("fty_datetime"));
								vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyIsResultExpField(ftyInfoFieldStrId.equals("fty_result"));
								vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtySNExpField(ftyInfoFieldStrId.equals("fty_sn"));
							}
							else
								vwVerticalRptFilterByFtyField.setFtyInfoField(new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldById(Integer.parseInt(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString().substring(4))));
						
							vwVerticalRptFilterByFtyField.setVwVerticalRptFilteByFtyFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
							vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
							vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
							vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
						
							if(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == null || ((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == "")
								vwVerticalRptFilterByFtyField.setVwVerticalRptFilteByFtyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
						
							new ViewVerticalReportFilterByFtyFieldDao().addVwVerticalReportFilterByFtyField(vwVerticalRptFilterByFtyField);
						}
					}
					
					break;
				}
			}
		}	
		
		Set<Item> selectedOptGrpPnlTargetRpts = (Set<Item>) optGrpPnlTargetReports.getValue();
		for (Object selectedTargetRpt : selectedOptGrpPnlTargetRpts)
		{
			for(int i=0; activeTargetReports != null && i<activeTargetReports.size(); i++)
			{
				int selectedTargetReportId = Integer.parseInt(selectedTargetRpt.toString());
				if(activeTargetReports.get(i).getTargetReportId() == selectedTargetReportId)
				{
					ViewVerticalReportByTargetRpt vwVerticalRptByTargetRpt = new ViewVerticalReportByTargetRpt();
					vwVerticalRptByTargetRpt.setTargetRpt(activeTargetReports.get(i));
					vwVerticalRptByTargetRpt.setViewVerticalReport(vwVerticalRpt);
					new ViewVerticalReportByTargetRptDao().addVwVerticalReportByTargetRpt(vwVerticalRptByTargetRpt);
					break;
				}
			}
		}
		
		//Save report columns
		Collection rptColumnsItemIds = this.tblReportColumns.getContainerDataSource().getItemIds();
		
		for (Object rptColumnsItemIdObj : rptColumnsItemIds) 
		{
			Item tblRptColumnRowItem = this.tblReportColumns.getContainerDataSource().getItem(rptColumnsItemIdObj);
			
			String rptColumnName = ((TextField)(tblRptColumnRowItem.getItemProperty("Column Name").getValue())).getValue();
			String rptDBColumnName = ((TextField)(tblRptColumnRowItem.getItemProperty("DB Column Name").getValue())).getValue();
			String rptColumnDataType = ((ComboBox)(tblRptColumnRowItem.getItemProperty("Data Type").getValue())).getValue().toString();
			
			ViewVerticalReportColumn vwRptColumn =  new ViewVerticalReportColumn();
			vwRptColumn.setViewVerticalReport(vwVerticalRpt);
			vwRptColumn.setVwVerticalRptColumnName(rptColumnName);
			vwRptColumn.setVwVerticalRptColumnDbId(rptDBColumnName);
			vwRptColumn.setVwVerticalRptColumnDataType(rptColumnDataType);
			
			new ViewVerticalReportColumnDao().addVwVerticalReportColumn(vwRptColumn);
			
			OptionGroup rptColumnDsFieldCol = ((OptionGroup)(tblRptColumnRowItem.getItemProperty("Data Source Column/Field").getValue()));
			
			Set<Item> selectedRptColumnDsFieldCols = (Set<Item>) rptColumnDsFieldCol.getValue();
			for (Object selectedRptColumnDsFieldCol : selectedRptColumnDsFieldCols)
			{
				if(selectedRptColumnDsFieldCol.toString().startsWith("expfield_"))
				{
					int selectedExpFieldId = Integer.parseInt(selectedRptColumnDsFieldCol.toString().substring(9));				
					ViewVerticalReportColumnByExpField vwVerticalRptColByExpField = new ViewVerticalReportColumnByExpField();
					vwVerticalRptColByExpField.setExperimentField(new ExperimentFieldDao().getExperimentFieldById(selectedExpFieldId));
					vwVerticalRptColByExpField.setVwVerticalReportColumn(vwRptColumn);
					new ViewVerticalReportColumnByExpFieldDao().addVwVerticalReportColumnByExpField(vwVerticalRptColByExpField);
					
				}

				if(selectedRptColumnDsFieldCol.toString().startsWith("fpyfield_"))
				{
					ViewVerticalReportColumnByFpyField vwVerticalRptColByFpyField = new ViewVerticalReportColumnByFpyField();
					if("fpyfield_sn".equals(selectedRptColumnDsFieldCol.toString()) || "fpyfield_result".equals(selectedRptColumnDsFieldCol.toString()) || "fpyfield_datetime".equals(selectedRptColumnDsFieldCol.toString()))
					{
						vwVerticalRptColByFpyField.setVwVerticalRptFilterByFpySNExpField("fpyfield_sn".equals(selectedRptColumnDsFieldCol.toString()));
						vwVerticalRptColByFpyField.setVwVerticalRptFilterByFpyIsResultExpField("fpyfield_result".equals(selectedRptColumnDsFieldCol.toString()));
						vwVerticalRptColByFpyField.setVwVerticalRptFilterByFpyIsDateTimeExpField("fpyfield_datetime".equals(selectedRptColumnDsFieldCol.toString()));
					}
					else
					{
						int selectedFpyFieldId = Integer.parseInt(selectedRptColumnDsFieldCol.toString().substring(9));				
						vwVerticalRptColByFpyField.setFirstPassYieldInfoField(new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldById(selectedFpyFieldId));
					}
					vwVerticalRptColByFpyField.setVwVerticalReportColumn(vwRptColumn);
					new ViewVerticalReportColumnByFpyFieldDao().addVwVerticalReportColumnByFpyField(vwVerticalRptColByFpyField);
				}
				
				if(selectedRptColumnDsFieldCol.toString().startsWith("ftyfield_"))
				{
					ViewVerticalReportColumnByFtyField vwVerticalRptColByFtyField = new ViewVerticalReportColumnByFtyField();
					if("ftyfield_sn".equals(selectedRptColumnDsFieldCol.toString()) || "ftyfield_result".equals(selectedRptColumnDsFieldCol.toString()) || "ftyfield_datetime".equals(selectedRptColumnDsFieldCol.toString()))
					{
						vwVerticalRptColByFtyField.setVwVerticalRptFilterByFtySNExpField("ftyfield_sn".equals(selectedRptColumnDsFieldCol.toString()));
						vwVerticalRptColByFtyField.setVwVerticalRptFilterByFtyIsResultExpField("ftyfield_result".equals(selectedRptColumnDsFieldCol.toString()));
						vwVerticalRptColByFtyField.setVwVerticalRptFilterByFtyIsDateTimeExpField("ftyfield_datetime".equals(selectedRptColumnDsFieldCol.toString()));
					}
					else
					{
						int selectedFtyFieldId = Integer.parseInt(selectedRptColumnDsFieldCol.toString().substring(9));				
						vwVerticalRptColByFtyField.setFirstTimeYieldInfoField(new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldById(selectedFtyFieldId));
					}
					vwVerticalRptColByFtyField.setVwVerticalReportColumn(vwRptColumn);
					new ViewVerticalReportColumnByFtyFieldDao().addVwVerticalReportColumnByFtyField(vwVerticalRptColByFtyField);
				}
			}
			
		}
		
	}

	private void loadReportColumns2Table()
	{
		tblVwVerticalRptCols = new TreeTable("Report Columns");
		tblVwVerticalRptCols.setStyleName("small");
		tblVwVerticalRptCols.setHeight(100, Unit.PERCENTAGE);
		tblVwVerticalRptCols.setWidth(100, Unit.PERCENTAGE);
		
		tblVwVerticalRptCols.addContainerProperty("Column Name", TextField.class, null);
		tblVwVerticalRptCols.addContainerProperty("DB Column Name", TextField.class, null);
		tblVwVerticalRptCols.addContainerProperty("Data Type", ComboBox.class, null);
		tblVwVerticalRptCols.addContainerProperty("Data Source Table/Report", ComboBox.class, null);
		tblVwVerticalRptCols.addContainerProperty("Data Source Field", ComboBox.class, null);
		tblVwVerticalRptCols.addContainerProperty("To Map", CheckBox.class, null);
		tblVwVerticalRptCols.setEditable(true);
		tblVwVerticalRptCols.setPageLength(0);
		
		lytReportColumns2.addComponent(tblVwVerticalRptCols);
		
	}

	private void addReportColumn2(Integer reportColumnId)
	{
		//Get all selected data sources 
		Integer itemId = reportColumnId;
		if(itemId == null)
		{	
			this.lastReportColumnItemId = this.lastReportColumnItemId - 1;
			itemId = this.lastReportColumnItemId;
		}
		
		Object[] rptColItemValues = new Object[6];
		
		TextField txtRptColumnName = new TextField("");
		txtRptColumnName.setStyleName("tiny");
		txtRptColumnName.setId(itemId.toString());
		txtRptColumnName.setRequired(true);
		txtRptColumnName.setRequiredError("This field is required.");
		txtRptColumnName.setHeight(20, Unit.PIXELS);
		txtRptColumnName.setWidth(100, Unit.PERCENTAGE);
		txtRptColumnName.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				refreshChildColNameTblVwVerticalRptCols(txtRptColumnName);
			}});
		
		rptColItemValues[0] = txtRptColumnName;
		
		TextField txtRptDbColumnName = new TextField("");
		txtRptDbColumnName.setStyleName("tiny");
		txtRptDbColumnName.setId(itemId.toString());
		txtRptDbColumnName.setRequired(true);
		txtRptDbColumnName.setRequiredError("This field is required.");
		txtRptDbColumnName.setHeight(20, Unit.PIXELS);
		txtRptDbColumnName.setWidth(100, Unit.PERCENTAGE);		
		txtRptDbColumnName.addValidator(new Validator() {

            public void validate(Object value) throws InvalidValueException {
                if(!StringUtils.isAlphanumeric(((String) value).replaceAll("_", "")))
                    throw new InvalidValueException("Only AlphaNumeric and Underscores are allowed for DB Names");
            }
            
        });
		
		txtRptDbColumnName.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				refreshChildDbColNameTblVwVerticalRptCols(txtRptColumnName);
			}});
		rptColItemValues[1] = txtRptDbColumnName;
				
		//Loading field types				
		ComboBox cbxDataType = new ComboBox("");
		cbxDataType.setStyleName("tiny");
		cbxDataType.setId(itemId.toString());
		cbxDataType.setRequired(true);
		cbxDataType.setRequiredError("This field is required.");
		cbxDataType.setHeight(20, Unit.PIXELS);
		cbxDataType.setWidth(100, Unit.PERCENTAGE);
		
		for(int j=0; j<dbfieldTypes.length; j++)
		{
			cbxDataType.addItem(dbfieldTypes[j]);
			cbxDataType.setItemCaption(dbfieldTypes[j], dbfieldTypes[j]);
		}
				
		rptColItemValues[2] = cbxDataType;

		cbxDataType.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	refreshChildCbxDataTypeTblVwVerticalRptCols(cbxDataType);
            }
        });
		
		ComboBox cbxDataSource = new ComboBox("");
		cbxDataSource.setStyleName("tiny");
		cbxDataSource.setHeight(20, Unit.PIXELS);
		cbxDataSource.setWidth(100, Unit.PERCENTAGE);
		cbxDataSource.setReadOnly(true);
		rptColItemValues[3] = cbxDataSource;

		ComboBox cbxDataSourceField = new ComboBox("");
		cbxDataSourceField.setStyleName("tiny");
		cbxDataSourceField.setHeight(20, Unit.PIXELS);
		cbxDataSourceField.setWidth(100, Unit.PERCENTAGE);
		cbxDataSourceField.setReadOnly(true);
		rptColItemValues[4] = cbxDataSourceField;

		CheckBox chxMappedData = new CheckBox();
		chxMappedData.setStyleName("tiny");
		chxMappedData.setVisible(true);
		chxMappedData.setValue(false);
		chxMappedData.setReadOnly(true);
		chxMappedData.setHeight(20, Unit.PIXELS);
		chxMappedData.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[5] = chxMappedData;

		tblVwVerticalRptCols.addItem(rptColItemValues, itemId);
		tblVwVerticalRptCols.select(itemId);
				
		//Add child entries
		Set<Item> selectedOptGrpPnlExperiments = (Set<Item>) optGrpPnlExperiments.getValue();
		for (Object selectedExperiment : selectedOptGrpPnlExperiments)
		{
			for(int i=0; activeExperiments != null && i<activeExperiments.size(); i++)
			{
				int selectedExperimentId = Integer.parseInt(selectedExperiment.toString());				
				if(activeExperiments.get(i).getExpId() == selectedExperimentId)
				{
					this.lastReportColumnItemId = this.lastReportColumnItemId - 1;
					addChildRptCol(this.lastReportColumnItemId, itemId, selectedExperimentId, null,	null, null, null, null, null, null);
					break;
				}
			}
		}
		
		Set<Item> selectedOptGrpPnlFpyRpts = (Set<Item>) optGrpPnlFpyReports.getValue();
		for (Object selectedFpyRpt : selectedOptGrpPnlFpyRpts)
		{
			for(int i=0; activeFpyReports != null && i<activeFpyReports.size(); i++)
			{
				int selectedFpyReportId = Integer.parseInt(selectedFpyRpt.toString());
				if(activeFpyReports.get(i).getFpyReportId() == selectedFpyReportId)
				{
					this.lastReportColumnItemId = this.lastReportColumnItemId - 1;
					addChildRptCol(this.lastReportColumnItemId, itemId, null, null,	selectedFpyReportId, null, null, null, null, null);
					break;
				}
			}
		}
		
		Set<Item> selectedOptGrpPnlFtyRpts = (Set<Item>) optGrpPnlFtyReports.getValue();
		for (Object selectedFtyRpt : selectedOptGrpPnlFtyRpts)
		{
			for(int i=0; activeFtyReports != null && i<activeFtyReports.size(); i++)
			{
				int selectedFtyReportId = Integer.parseInt(selectedFtyRpt.toString());
				if(activeFtyReports.get(i).getFtyReportId() == selectedFtyReportId)
				{
					this.lastReportColumnItemId = this.lastReportColumnItemId - 1;
					addChildRptCol(this.lastReportColumnItemId, itemId, null, null,	null, null, selectedFtyReportId, null, null, null);
					break;
				}
			}
		}	
		
		Set<Item> selectedOptGrpPnlTargetRpts = (Set<Item>) optGrpPnlTargetReports.getValue();
		for (Object selectedTargetRpt : selectedOptGrpPnlTargetRpts)
		{
			for(int i=0; activeTargetReports != null && i<activeTargetReports.size(); i++)
			{
				int selectedTargetReportId = Integer.parseInt(selectedTargetRpt.toString());
				if(activeTargetReports.get(i).getTargetReportId() == selectedTargetReportId)
				{
					this.lastReportColumnItemId = this.lastReportColumnItemId - 1;
					addChildRptCol(this.lastReportColumnItemId, itemId, null, null,	null, null, null, null, selectedTargetReportId, null);
					break;
				}
			}
		}
		
		
		for (Object rptColItemId: tblVwVerticalRptCols.getContainerDataSource().getItemIds()) 
		{
			tblVwVerticalRptCols.setCollapsed(rptColItemId, false);
			if (!tblVwVerticalRptCols.hasChildren(rptColItemId))
				tblVwVerticalRptCols.setChildrenAllowed(rptColItemId, false);
		}
		
	}

	private void addChildRptCol(Integer rptColumnId, Integer parentRptColumnId, Integer expDataSourceId, Integer expDataSourceFieldId, 
			Integer fpyDataSourceId, Integer fpyDataSourceFieldId, Integer ftyDataSourceId, Integer ftyDataSourceFieldId, 
			Integer tgtDataSourceId, Integer tgtDataSourceFieldId)
	{
		Object[] rptColItemValues = new Object[6];
		
		TextField txtRptColumnName = new TextField("");
		txtRptColumnName.setStyleName("tiny");
		txtRptColumnName.setRequired(true);
		txtRptColumnName.setReadOnly(true);
		txtRptColumnName.setRequiredError("This field is required.");
		txtRptColumnName.setHeight(20, Unit.PIXELS);
		txtRptColumnName.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[0] = txtRptColumnName;
		
		TextField txtRptDbColumnName = new TextField("");
		txtRptDbColumnName.setStyleName("tiny");
		txtRptDbColumnName.setRequired(true);
		txtRptDbColumnName.setReadOnly(true);
		txtRptDbColumnName.setRequiredError("This field is required.");
		txtRptDbColumnName.setHeight(20, Unit.PIXELS);
		txtRptDbColumnName.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[1] = txtRptDbColumnName;
				
		//Loading field types				
		ComboBox cbxDataType = new ComboBox("");
		cbxDataType.setStyleName("tiny");
		cbxDataType.setRequired(true);
		cbxDataType.setRequiredError("This field is required.");
		cbxDataType.setHeight(20, Unit.PIXELS);
		cbxDataType.setWidth(100, Unit.PERCENTAGE);
		
		for(int j=0; j<dbfieldTypes.length; j++)
		{
			cbxDataType.addItem(dbfieldTypes[j]);
			cbxDataType.setItemCaption(dbfieldTypes[j], dbfieldTypes[j]);
		}
		rptColItemValues[2] = cbxDataType;

		ComboBox cbxDataSource = new ComboBox("");
		cbxDataSource.setStyleName("tiny");
		cbxDataSource.setHeight(20, Unit.PIXELS);
		cbxDataSource.setWidth(100, Unit.PERCENTAGE);
		
		if(expDataSourceId != null)
		{
			Experiment experiment = new ExperimentDao().getExperimentById(expDataSourceId);
			cbxDataSource.addItem("exp_" + expDataSourceId);
			cbxDataSource.setItemCaption("exp_" + expDataSourceId, experiment.getExpName());		
			cbxDataSource.setValue("exp_" + expDataSourceId);
		}
		
		if(fpyDataSourceId != null)
		{
			FirstPassYieldReport fpyRpt = new FirstPassYieldReportDao().getFirstPassYieldReportById(fpyDataSourceId);
			cbxDataSource.addItem("fpy_" + fpyDataSourceId);
			cbxDataSource.setItemCaption("fpy_" + fpyDataSourceId, "FPY: " + fpyRpt.getFpyReportName());	
			cbxDataSource.setValue("fpy_" + fpyDataSourceId);
		}
		
		if(ftyDataSourceId != null)
		{
			FirstTimeYieldReport ftyRpt = new FirstTimeYieldReportDao().getFirstTimeYieldReportById(ftyDataSourceId);
			cbxDataSource.addItem("fty_" + ftyDataSourceId);
			cbxDataSource.setItemCaption("fty_" + ftyDataSourceId, "FTY: " + ftyRpt.getFtyReportName());	
			cbxDataSource.setValue("fty_" + ftyDataSourceId);
		}

		if(tgtDataSourceId != null)
		{
			TargetReport tgtRpt = new TargetReportDao().getTargetReportById(tgtDataSourceId);
			cbxDataSource.addItem("tgt_" + tgtDataSourceId);
			cbxDataSource.setItemCaption("tgt_" + tgtDataSourceId, "TARGET: " + tgtRpt.getTargetReportName());
			cbxDataSource.setValue("tgt_" + tgtDataSourceId);				
		}
		
		cbxDataSource.setReadOnly(true);
		rptColItemValues[3] = cbxDataSource;

		ComboBox cbxDataFieldSource = new ComboBox("");
		cbxDataFieldSource.setStyleName("tiny");
		cbxDataFieldSource.setHeight(20, Unit.PIXELS);
		cbxDataFieldSource.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[4] = cbxDataFieldSource;
		
		CheckBox chxMappedData = new CheckBox();
		chxMappedData.setStyleName("tiny");
		chxMappedData.setVisible(true);
		chxMappedData.setValue(true);
		chxMappedData.setReadOnly(true);
		chxMappedData.setHeight(20, Unit.PIXELS);
		chxMappedData.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[5] = chxMappedData;

		tblVwVerticalRptCols.addItem(rptColItemValues, rptColumnId);
		tblVwVerticalRptCols.select(rptColumnId);		
		tblVwVerticalRptCols.setParent(rptColumnId, parentRptColumnId);
		
	}
	
	private void refreshChildColNameTblVwVerticalRptCols(TextField txtColName)
	{
		Integer parentItemId = Integer.parseInt(txtColName.getId());
	
		for (Object rptColItemId: tblVwVerticalRptCols.getContainerDataSource().getItemIds()) 
		{
			if(rptColItemId.toString().equals(parentItemId.toString()))
			{
				Item tblVwVerticalParentRptColRowItem = tblVwVerticalRptCols.getContainerDataSource().getItem(rptColItemId);
				
				String rptParentColumnName = ((TextField)(tblVwVerticalParentRptColRowItem.getItemProperty("Column Name").getValue())).getValue();
				
				for (Object rptChildColItemId: tblVwVerticalRptCols.getChildren(rptColItemId)) 
				{
					Item tblVwVerticalRptColRowItem = tblVwVerticalRptCols.getContainerDataSource().getItem(rptChildColItemId);
					
					TextField txtRptChildColumnName = (TextField)(tblVwVerticalRptColRowItem.getItemProperty("Column Name").getValue());
					txtRptChildColumnName.setReadOnly(false);
					txtRptChildColumnName.setValue(rptParentColumnName);
					txtRptChildColumnName.setReadOnly(true);				
				}				
			}
		}		
	}
	
	private void refreshChildDbColNameTblVwVerticalRptCols(TextField txtDbColName)
	{
		Integer parentItemId = Integer.parseInt(txtDbColName.getId());
		
		for (Object rptColItemId: tblVwVerticalRptCols.getContainerDataSource().getItemIds()) 
		{
			if(rptColItemId.toString().equals(parentItemId.toString()))
			{
				Item tblVwVerticalParentRptColRowItem = tblVwVerticalRptCols.getContainerDataSource().getItem(rptColItemId);
				
				String rptParentColumnName = ((TextField)(tblVwVerticalParentRptColRowItem.getItemProperty("DB Column Name").getValue())).getValue();
				
				for (Object rptChildColItemId: tblVwVerticalRptCols.getChildren(rptColItemId)) 
				{
					Item tblVwVerticalRptColRowItem = tblVwVerticalRptCols.getContainerDataSource().getItem(rptChildColItemId);
					
					TextField txtRptChildDbColumnName = (TextField)(tblVwVerticalRptColRowItem.getItemProperty("DB Column Name").getValue());
					txtRptChildDbColumnName.setReadOnly(false);
					txtRptChildDbColumnName.setValue(rptParentColumnName);
					txtRptChildDbColumnName.setReadOnly(true);										
				}				
			}
		}		
	}
	
	private void refreshChildCbxDataTypeTblVwVerticalRptCols(ComboBox cbxDataType)
	{
		Integer parentItemId = Integer.parseInt(cbxDataType.getId());
	
		for (Object rptColItemId: tblVwVerticalRptCols.getContainerDataSource().getItemIds()) 
		{
			if(rptColItemId.toString().equals(parentItemId.toString()))
			{
				Item tblVwVerticalParentRptColRowItem = tblVwVerticalRptCols.getContainerDataSource().getItem(rptColItemId);
				
				String rptParentDataType = ((ComboBox)(tblVwVerticalParentRptColRowItem.getItemProperty("Data Type").getValue())).getValue().toString();
				
				for (Object rptChildColItemId: tblVwVerticalRptCols.getChildren(rptColItemId)) 
				{
					Item tblVwVerticalRptColRowItem = tblVwVerticalRptCols.getContainerDataSource().getItem(rptChildColItemId);
					
					ComboBox cbxRptChildDataType = (ComboBox)(tblVwVerticalRptColRowItem.getItemProperty("Data Type").getValue());
					cbxRptChildDataType.setReadOnly(false);
					cbxRptChildDataType.setValue(rptParentDataType);
					cbxRptChildDataType.setReadOnly(true);				

					ComboBox cbxDataSource = (ComboBox)(tblVwVerticalRptColRowItem.getItemProperty("Data Source Table/Report").getValue());
					ComboBox cbxDataSourceField = (ComboBox)(tblVwVerticalRptColRowItem.getItemProperty("Data Source Field").getValue());
					
					//Fill cbxDataSourceFields 
					switch(cbxDataSource.getValue().toString().substring(0, 4))
					{
						case "exp_":
							fillCbxDataSourceFields(cbxDataType.getValue().toString(), cbxDataSourceField, Integer.parseInt(cbxDataSource.getValue().toString().substring(4)), null, null, null);
							break;
	
						case "fpy_":
							fillCbxDataSourceFields(cbxDataType.getValue().toString(), cbxDataSourceField, null, Integer.parseInt(cbxDataSource.getValue().toString().substring(4)), null, null);
							break;
	
						case "fty_":
							fillCbxDataSourceFields(cbxDataType.getValue().toString(), cbxDataSourceField, null, null, Integer.parseInt(cbxDataSource.getValue().toString().substring(4)), null);
							break;
	
						case "tgt_":
							fillCbxDataSourceFields(cbxDataType.getValue().toString(), cbxDataSourceField, null, null, null, Integer.parseInt(cbxDataSource.getValue().toString().substring(4)));
							break;
							
					}
				}				
			}
		}		
	}
	
	
	private void fillCbxDataSourceFields(String dataFieldType, ComboBox cbxDataSourceFields, Integer expDataSourceId, Integer fpyDataSourceId, 
			Integer ftyDataSourceId, Integer tgtDataSourceId)
	{		
		if(expDataSourceId != null)
		{
			Experiment selectedExperiment = new ExperimentDao().getExperimentById(expDataSourceId);
			List<ExperimentField> selectedExperimentFields = new ExperimentFieldDao().getAllExperimentFieldsByExperiment(selectedExperiment);
			
			for(int i=0; selectedExperimentFields!=null && i<selectedExperimentFields.size(); i++)
			{
				System.out.println("Adding Exp field:" + expDataSourceId + "expfield_" + selectedExperimentFields.get(i).getExpFieldId() + "_" + selectedExperimentFields.get(i).getExpFieldName() + "_" + selectedExperimentFields.get(i).getExpFieldType());
				
				addItemIfTypeMatches(dataFieldType, "expfield_" + selectedExperimentFields.get(i).getExpFieldId(), selectedExperimentFields.get(i).getExpFieldName(), selectedExperimentFields.get(i).getExpFieldType(), cbxDataSourceFields);
			}
		}
		
		if(fpyDataSourceId != null)
		{
			List<FirstPassYieldInfoField> fpyFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(fpyDataSourceId);
			for(int i=0; fpyFields != null && i<fpyFields.size(); i++)
				addItemIfTypeMatches(dataFieldType, "fpyfield_" + fpyFields.get(i).getFpyInfoFieldId(), fpyFields.get(i).getFpyInfoFieldLabel(), fpyFields.get(i).getExperimentField().getExpFieldType(), cbxDataSourceFields);
	
			addItemIfTypeMatches(dataFieldType, "fpy_datetime", "Datetime (FPY)", "datetime", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fpy_sn", "Serial Number (FPY)", "nvarchar(max)", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fpy_result", "Result (FPY)", "nvarchar(max)", cbxDataSourceFields);
		}
		
		if(ftyDataSourceId != null)
		{
			List<FirstTimeYieldInfoField> ftyFields = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(ftyDataSourceId);
			for(int i=0; ftyFields != null && i<ftyFields.size(); i++)
				addItemIfTypeMatches(dataFieldType, "ftyfield_" + ftyFields.get(i).getFtyInfoFieldId(), ftyFields.get(i).getFtyInfoFieldLabel(), ftyFields.get(i).getExperimentField().getExpFieldType(), cbxDataSourceFields);
	
			addItemIfTypeMatches(dataFieldType, "fty_datetime", "Datetime (FTY)", "datetime", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fty_sn", "Serial Number (FTY)", "nvarchar(max)", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fty_result", "Result (FTY)", "nvarchar(max)", cbxDataSourceFields);
		}
		
		if(tgtDataSourceId != null)
		{
			List<TargetColumn> targetColumns = new TargetColumnDao().getTargetColumnByTargetReportId(tgtDataSourceId);
			
			for(int i=0; targetColumns!=null && i<targetColumns.size(); i++)
				addItemIfTypeMatches(dataFieldType, "tgtfield_" + targetColumns.get(i).getTargetColumnId(), targetColumns.get(i).getTargetColumnLabel(), targetColumns.get(i).getExperimentField().getExpFieldType(), cbxDataSourceFields);
		}				
	}
	
	private void addItemIfTypeMatches(String dataType, String dataSourceFieldId, String dataSourceFieldName, String dataSourceFieldType, ComboBox cbxDataSourceField)
	{
		if(dataType.equals(dataSourceFieldType) || (dataType.equals("float") && dataSourceFieldType.equals("int")))
		{
			cbxDataSourceField.addItem(dataSourceFieldId);
			cbxDataSourceField.setItemCaption(dataSourceFieldId, dataSourceFieldName);
		}
		else if(dataType.equals("nvarchar(max)") || dataType.equals("varchar(max)") || dataType.equals("text")
				&& (dataSourceFieldType.startsWith("varchar") || dataSourceFieldType.startsWith("char")
		                || dataSourceFieldType.startsWith("text") || dataSourceFieldType.startsWith("nvarchar")
		                || dataSourceFieldType.startsWith("nchar") || dataSourceFieldType.startsWith("ntext")))			
		{
			cbxDataSourceField.addItem(dataSourceFieldId);
			cbxDataSourceField.setItemCaption(dataSourceFieldId, dataSourceFieldName);
		}
		else if((dataType.startsWith("varchar") || dataType.startsWith("char")
                || dataType.startsWith("text") || dataType.startsWith("nvarchar")
                || dataType.startsWith("nchar") || dataType.startsWith("ntext"))
				&& (dataSourceFieldType.startsWith("varchar") || dataSourceFieldType.startsWith("char")
		                || dataSourceFieldType.startsWith("text") || dataSourceFieldType.startsWith("nvarchar")
		                || dataSourceFieldType.startsWith("nchar") || dataSourceFieldType.startsWith("ntext")))
		{
			String selectedDataTypeStringStrLength = dataType.replaceAll("\\D+","");
			String dataSourcFieldDataTypeStringStrLength = dataSourceFieldType.replaceAll("\\D+","");	
			
			if(selectedDataTypeStringStrLength != null && selectedDataTypeStringStrLength.length() > 0 && dataSourcFieldDataTypeStringStrLength != null && dataSourcFieldDataTypeStringStrLength.length() > 0)
			{
				if(Integer.parseInt(selectedDataTypeStringStrLength)>Integer.parseInt(dataSourcFieldDataTypeStringStrLength))
				{
					cbxDataSourceField.addItem(dataSourceFieldId);
					cbxDataSourceField.setItemCaption(dataSourceFieldId, dataSourceFieldName);
				}
			}
		}
		
	}
	
}