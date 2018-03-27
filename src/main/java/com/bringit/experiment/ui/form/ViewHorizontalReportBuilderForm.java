package com.bringit.experiment.ui.form;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.bringit.experiment.bll.CustomList;
import com.bringit.experiment.bll.CustomListValue;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FinalPassYieldInfoField;
import com.bringit.experiment.bll.FinalPassYieldReport;
import com.bringit.experiment.bll.FirstPassYieldInfoField;
import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.FirstTimeYieldInfoField;
import com.bringit.experiment.bll.FirstTimeYieldReport;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.TargetColumn;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.bll.ViewHorizontalReport;
import com.bringit.experiment.bll.ViewHorizontalReportByExperiment;
import com.bringit.experiment.bll.ViewHorizontalReportByFnyRpt;
import com.bringit.experiment.bll.ViewHorizontalReportByFpyRpt;
import com.bringit.experiment.bll.ViewHorizontalReportByFtyRpt;
import com.bringit.experiment.bll.ViewHorizontalReportByTargetRpt;
import com.bringit.experiment.bll.ViewHorizontalReportColumn;
import com.bringit.experiment.bll.ViewHorizontalReportColumnByEnrichment;
import com.bringit.experiment.bll.ViewHorizontalReportFilterByExpField;
import com.bringit.experiment.bll.ViewHorizontalReportFilterByFnyField;
import com.bringit.experiment.bll.ViewHorizontalReportFilterByFpyField;
import com.bringit.experiment.bll.ViewHorizontalReportFilterByFtyField;
import com.bringit.experiment.bll.ViewHorizontalReportFilterByTargetColumn;
import com.bringit.experiment.bll.ViewVerticalReportColumn;
import com.bringit.experiment.bll.ViewVerticalReportColumnByEnrichment;
import com.bringit.experiment.bll.ViewVerticalReportFilterByExpField;
import com.bringit.experiment.bll.ViewVerticalReportFilterByFnyField;
import com.bringit.experiment.bll.ViewVerticalReportFilterByFpyField;
import com.bringit.experiment.bll.ViewVerticalReportFilterByFtyField;
import com.bringit.experiment.bll.ViewVerticalReportFilterByTargetColumn;
import com.bringit.experiment.dao.CustomListDao;
import com.bringit.experiment.dao.CustomListValueDao;
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
import com.bringit.experiment.dao.ViewHorizontalReportByExperimentDao;
import com.bringit.experiment.dao.ViewHorizontalReportByFnyRptDao;
import com.bringit.experiment.dao.ViewHorizontalReportByFpyRptDao;
import com.bringit.experiment.dao.ViewHorizontalReportByFtyRptDao;
import com.bringit.experiment.dao.ViewHorizontalReportByTargetRptDao;
import com.bringit.experiment.dao.ViewHorizontalReportColumnByEnrichmentDao;
import com.bringit.experiment.dao.ViewHorizontalReportColumnDao;
import com.bringit.experiment.dao.ViewHorizontalReportDao;
import com.bringit.experiment.dao.ViewHorizontalReportFilterByExpFieldDao;
import com.bringit.experiment.dao.ViewHorizontalReportFilterByFnyFieldDao;
import com.bringit.experiment.dao.ViewHorizontalReportFilterByFpyFieldDao;
import com.bringit.experiment.dao.ViewHorizontalReportFilterByFtyFieldDao;
import com.bringit.experiment.dao.ViewHorizontalReportFilterByTargetColumnDao;
import com.bringit.experiment.dao.ViewVerticalReportByExperimentDao;
import com.bringit.experiment.dao.ViewVerticalReportByFnyRptDao;
import com.bringit.experiment.dao.ViewVerticalReportByFpyRptDao;
import com.bringit.experiment.dao.ViewVerticalReportByFtyRptDao;
import com.bringit.experiment.dao.ViewVerticalReportByTargetRptDao;
import com.bringit.experiment.dao.ViewVerticalReportColumnByEnrichmentDao;
import com.bringit.experiment.dao.ViewVerticalReportColumnDao;
import com.bringit.experiment.dao.ViewVerticalReportDao;
import com.bringit.experiment.dao.ViewVerticalReportFilterByExpFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportFilterByFnyFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportFilterByFpyFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportFilterByFtyFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportFilterByTargetColumnDao;
import com.bringit.experiment.ui.design.ViewHorizontalReportBuilderDesign;
import com.bringit.experiment.util.Config;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;

public class ViewHorizontalReportBuilderForm extends ViewHorizontalReportBuilderDesign{

	private Boolean isNewRecord = false;
	private ViewHorizontalReport savedVwHorizontalRpt = new ViewHorizontalReport();
	
	private List<Experiment> activeExperiments = new ExperimentDao().getActiveExperiments();
	private List<FirstPassYieldReport> activeFpyReports = new FirstPassYieldReportDao().getAllFirstPassYieldReports();
	private List<FinalPassYieldReport> activeFnyReports = new FinalPassYieldReportDao().getAllFinalPassYieldReports();
	private List<FirstTimeYieldReport> activeFtyReports = new FirstTimeYieldReportDao().getAllFirstTimeYieldReports();
	private List<TargetReport> activeTargetReports = new TargetReportDao().getAllActiveTargetReports();
	private SystemSettings systemSettings;
	
	private OptionGroup optGrpPnlExperiments = new OptionGroup();
	private OptionGroup optGrpPnlFpyReports = new OptionGroup();
	private OptionGroup optGrpPnlFnyReports = new OptionGroup();
	private OptionGroup optGrpPnlFtyReports = new OptionGroup();
	private OptionGroup optGrpPnlTargetReports = new OptionGroup();

	private int lastDataSourceFilterItemId = 0;
	private int lastReportColumnItemId = 0;
	private int lastHorizontalRptEnrichmentItemId = 0;
	
	String[] dbfieldTypes;

	private List<Integer> dbIdOfDataSourceFilterToDelete = new ArrayList<Integer>();
	private List<Integer> dbIdOfVwVertRptColumnsToDelete = new ArrayList<Integer>();
	private List<Integer> dbIdOfVwVertRptEnrichmentToDelete = new ArrayList<Integer>();
	
	//Saved data
	//Data Sources
	private List<ViewHorizontalReportByExperiment> vwHorizontalRptByExperimentList = new ArrayList<ViewHorizontalReportByExperiment>();
	private List<Integer> vwHorizontalRptByExperimentDbIds =  new ArrayList<Integer>();
	private List<ViewHorizontalReportByFpyRpt> vwHorizontalRptByFpyRptList = new ArrayList<ViewHorizontalReportByFpyRpt>();
	private List<Integer> vwHorizontalRptByFpyRptDbIds =  new ArrayList<Integer>();	
	private List<ViewHorizontalReportByFnyRpt> vwHorizontalRptByFnyRptList = new ArrayList<ViewHorizontalReportByFnyRpt>();
	private List<Integer> vwHorizontalRptByFnyRptDbIds =  new ArrayList<Integer>();
	private List<ViewHorizontalReportByFtyRpt> vwHorizontalRptByFtyRptList = new ArrayList<ViewHorizontalReportByFtyRpt>();
	private List<Integer> vwHorizontalRptByFtyRptDbIds =  new ArrayList<Integer>();
	private List<ViewHorizontalReportByTargetRpt> vwHorizontalRptByTgtRptList = new ArrayList<ViewHorizontalReportByTargetRpt>();
	private List<Integer> vwHorizontalRptByTargetDbIds =  new ArrayList<Integer>();
	
	//Data Source filters
	private List<ViewHorizontalReportFilterByExpField> vwHorizontalRptFiltersByExpField = new ArrayList<ViewHorizontalReportFilterByExpField>();
	private List<ViewHorizontalReportFilterByFpyField> vwHorizontalRptFiltersByFpyField = new ArrayList<ViewHorizontalReportFilterByFpyField>();
	private List<ViewHorizontalReportFilterByFnyField> vwHorizontalRptFiltersByFnyField = new ArrayList<ViewHorizontalReportFilterByFnyField>();
	private List<ViewHorizontalReportFilterByFtyField> vwHorizontalRptFiltersByFtyField = new ArrayList<ViewHorizontalReportFilterByFtyField>();
	private List<ViewHorizontalReportFilterByTargetColumn> vwHorizontalRptFiltersByTgtCol = new ArrayList<ViewHorizontalReportFilterByTargetColumn>();
	
	//Report Columns
	private List<ViewHorizontalReportColumn> vwHorizontalRptColumns = new ArrayList<ViewHorizontalReportColumn>();
	
	//Report Columns Enrichment
	private List<ViewHorizontalReportColumnByEnrichment> vwHorizontalRptColumnsByEnrichment = new ArrayList<ViewHorizontalReportColumnByEnrichment>();
		
	
	public ViewHorizontalReportBuilderForm(Integer vwHorizontalRptId)
	{
		if(vwHorizontalRptId == null || vwHorizontalRptId == -1)
			isNewRecord = true;
		else
		{
			//Load report data from DB
			this.savedVwHorizontalRpt = new ViewHorizontalReportDao().getVwHorizontalRptById(vwHorizontalRptId);
			
			//Load data sources from DB
			vwHorizontalRptByExperimentList = new ViewHorizontalReportByExperimentDao().getAllVwHorizontalReportByExperimentByRptId(vwHorizontalRptId);
			vwHorizontalRptByFpyRptList = new ViewHorizontalReportByFpyRptDao().getAllVwHorizontalReportByFpyRptById(vwHorizontalRptId);
			vwHorizontalRptByFnyRptList = new ViewHorizontalReportByFnyRptDao().getAllVwHorizontalReportByFnyRptById(vwHorizontalRptId);
			vwHorizontalRptByFtyRptList = new ViewHorizontalReportByFtyRptDao().getAllVwHorizontalReportByFtyRptById(vwHorizontalRptId);
			vwHorizontalRptByTgtRptList = new ViewHorizontalReportByTargetRptDao().getAllVwHorizontalReportByTargetRptById(vwHorizontalRptId);
		
			//Load Filters for selected data sources
			for(int i=0; vwHorizontalRptByExperimentList != null && i<vwHorizontalRptByExperimentList.size(); i++)
			{
				vwHorizontalRptByExperimentDbIds.add(vwHorizontalRptByExperimentList.get(i).getExperiment().getExpId());
				List<ViewHorizontalReportFilterByExpField> currentVwHorizontalRptFiltersByExpField = new ViewHorizontalReportFilterByExpFieldDao().getAllVwHorizontalReportFiltersByExpRptId(vwHorizontalRptByExperimentList.get(i).getVwHorizontalRptByExperimentId());
				for(int j=0; currentVwHorizontalRptFiltersByExpField!= null && j<currentVwHorizontalRptFiltersByExpField.size(); j++)
					vwHorizontalRptFiltersByExpField.add(currentVwHorizontalRptFiltersByExpField.get(j));
			}

			for(int i=0; vwHorizontalRptByFpyRptList != null && i<vwHorizontalRptByFpyRptList.size(); i++)
			{
				vwHorizontalRptByFpyRptDbIds.add(vwHorizontalRptByFpyRptList.get(i).getFpyRpt().getFpyReportId());
				List<ViewHorizontalReportFilterByFpyField> currentVwHorizontalRptFiltersByFpyField = new ViewHorizontalReportFilterByFpyFieldDao().getAllVwHorizontalReportFiltersByFpyRptId(vwHorizontalRptByFpyRptList.get(i).getVwHorizontalRptByFpyId());
				for(int j=0; currentVwHorizontalRptFiltersByFpyField != null && j<currentVwHorizontalRptFiltersByFpyField.size(); j++)
					vwHorizontalRptFiltersByFpyField.add(currentVwHorizontalRptFiltersByFpyField.get(j));
			}

			for(int i=0; vwHorizontalRptByFnyRptList != null && i<vwHorizontalRptByFnyRptList.size(); i++)
			{
				vwHorizontalRptByFnyRptDbIds.add(vwHorizontalRptByFnyRptList.get(i).getFnyRpt().getFnyReportId());
				List<ViewHorizontalReportFilterByFnyField> currentVwHorizontalRptFiltersByFnyField = new ViewHorizontalReportFilterByFnyFieldDao().getAllVwHorizontalReportFiltersByFnyRptId(vwHorizontalRptByFnyRptList.get(i).getVwHorizontalRptByFnyId());
				for(int j=0; currentVwHorizontalRptFiltersByFnyField != null && j<currentVwHorizontalRptFiltersByFnyField.size(); j++)
					vwHorizontalRptFiltersByFnyField.add(currentVwHorizontalRptFiltersByFnyField.get(j));
			}

			for(int i=0; vwHorizontalRptByFtyRptList != null && i<vwHorizontalRptByFtyRptList.size(); i++)
			{
				vwHorizontalRptByFtyRptDbIds.add(vwHorizontalRptByFtyRptList.get(i).getFtyRpt().getFtyReportId());
				List<ViewHorizontalReportFilterByFtyField> currentVwHorizontalRptFiltersByFtyField = new ViewHorizontalReportFilterByFtyFieldDao().getAllVwHorizontalReportFiltersByFtyRptId(vwHorizontalRptByFtyRptList.get(i).getVwHorizontalRptByFtyId());
				for(int j=0; currentVwHorizontalRptFiltersByFtyField != null && j<currentVwHorizontalRptFiltersByFtyField.size(); j++)
					vwHorizontalRptFiltersByFtyField.add(currentVwHorizontalRptFiltersByFtyField.get(j));
			}

			for(int i=0; vwHorizontalRptByTgtRptList != null && i<vwHorizontalRptByTgtRptList.size(); i++)
			{
				vwHorizontalRptByTargetDbIds.add(vwHorizontalRptByTgtRptList.get(i).getTargetRpt().getTargetReportId());
				List<ViewHorizontalReportFilterByTargetColumn> currentVwHorizontalRptFiltersByTgtColumn = new ViewHorizontalReportFilterByTargetColumnDao().getAllVwHorizontalReportFiltersByTargetRptId(vwHorizontalRptByTgtRptList.get(i).getVwHorizontalRptByTargetRptId());
				for(int j=0; currentVwHorizontalRptFiltersByTgtColumn != null && j<currentVwHorizontalRptFiltersByTgtColumn.size(); j++)
					vwHorizontalRptFiltersByTgtCol.add(currentVwHorizontalRptFiltersByTgtColumn.get(j));
			}
			
			//Load report columns 
			vwHorizontalRptColumns = new ViewHorizontalReportColumnDao().getAllVwHorizontalReportColumnsByRptId(vwHorizontalRptId);
			
			//Load report columns enrichment
			for(int i=0; vwHorizontalRptColumns != null && i<vwHorizontalRptColumns.size(); i++)
			{
				List<ViewHorizontalReportColumnByEnrichment> currentVwHorizontalRptColumnsByEnrichment = new ViewHorizontalReportColumnByEnrichmentDao().getAllVwHorizontalRptColsByEnrichmentByColId(vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnId());

				for(int j=0; currentVwHorizontalRptColumnsByEnrichment != null && j<currentVwHorizontalRptColumnsByEnrichment.size(); j++)
					vwHorizontalRptColumnsByEnrichment.add(currentVwHorizontalRptColumnsByEnrichment.get(j));
			}

			
			//Load Report Header information
			this.txtVwHorizontalRptName.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptName());
			this.txtVwHorizontalRptCustomId.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptDbTableNameId().replace("vwhorizontal#", ""));
			this.txtVwHorizontalDescription.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptDescription());
			this.chxActive.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptIsActive());
		}
		
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
			
			if(vwHorizontalRptId != null && vwHorizontalRptId > -1 && vwHorizontalRptByExperimentDbIds.indexOf(activeExperiments.get(i).getExpId()) != -1)
				optGrpPnlExperiments.select(activeExperiments.get(i).getExpId());			
		}
		
		optGrpPnlExperiments.addValueChangeListener(new ValueChangeListener(){
           @Override
			public void valueChange(ValueChangeEvent event) {
        	   //refreshExperimentDataSourceElements();
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
			
			if(vwHorizontalRptId != null && vwHorizontalRptId > -1 && vwHorizontalRptByFpyRptDbIds.indexOf(activeFpyReports.get(i).getFpyReportId()) != -1)
				optGrpPnlFpyReports.select(activeFpyReports.get(i).getFpyReportId());
		}

		optGrpPnlFpyReports.addValueChangeListener(new ValueChangeListener(){
           @Override
			public void valueChange(ValueChangeEvent event) {
        	   //refreshFpyRptDataSourceElements();
           }
        });
		
		lytPnlFpyReports.addComponent(optGrpPnlFpyReports);
		pnlFpyReports.setContent(lytPnlFpyReports);
		pnlFpyReportDataSource.addComponent(pnlFpyReports);
		
		
		Panel pnlFnyReports = new Panel("Final Pass Yield Reports");
		VerticalLayout lytPnlFnyReports = new VerticalLayout();
		optGrpPnlFnyReports.setMultiSelect(true);
		optGrpPnlFnyReports.setStyleName("small");
		pnlFnyReports.setHeight(100, Unit.PERCENTAGE);
		pnlFnyReports.setWidth(95, Unit.PERCENTAGE);
		pnlFnyReports.setStyleName("well");
		pnlFnyReports.setCaption("");
		
		for(int i=0; activeFnyReports != null && i<activeFnyReports.size(); i++)
		{
			optGrpPnlFnyReports.addItem(activeFnyReports.get(i).getFnyReportId());
			optGrpPnlFnyReports.setItemCaption(activeFnyReports.get(i).getFnyReportId(), activeFnyReports.get(i).getFnyReportName());
			
			if(vwHorizontalRptId != null && vwHorizontalRptId > -1 && vwHorizontalRptByFnyRptDbIds.indexOf(activeFnyReports.get(i).getFnyReportId()) != -1)
				optGrpPnlFnyReports.select(activeFnyReports.get(i).getFnyReportId());
		}

		optGrpPnlFnyReports.addValueChangeListener(new ValueChangeListener(){
           @Override
			public void valueChange(ValueChangeEvent event) {
        	   //refreshFnyRptDataSourceElements();
           }
        });
		
		lytPnlFnyReports.addComponent(optGrpPnlFnyReports);
		pnlFnyReports.setContent(lytPnlFnyReports);
		pnlFnyReportDataSource.addComponent(pnlFnyReports);
		
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

			if(vwHorizontalRptId != null && vwHorizontalRptId > -1 && vwHorizontalRptByFtyRptDbIds.indexOf(activeFtyReports.get(i).getFtyReportId()) != -1)
				optGrpPnlFtyReports.select(activeFtyReports.get(i).getFtyReportId());
		}

		optGrpPnlFtyReports.addValueChangeListener(new ValueChangeListener(){
           @Override
			public void valueChange(ValueChangeEvent event) {
        	   //refreshFtyRptDataSourceElements();
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
			
			if(vwHorizontalRptId != null && vwHorizontalRptId > -1 && vwHorizontalRptByTargetDbIds.indexOf(activeTargetReports.get(i).getTargetReportId()) != -1)
				optGrpPnlTargetReports.select(activeTargetReports.get(i).getTargetReportId());
		}

		optGrpPnlTargetReports.addValueChangeListener(new ValueChangeListener(){
           @Override
			public void valueChange(ValueChangeEvent event) {
        	   //refreshTargetRptDataSourceElements();
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
				addDataSourceFilter(null,null,null,null,null,null,null);
			}

		});
		
		this.btnRemoveFilter.addClickListener((new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(tblReportFilters.getValue() != null)
				{
					dbIdOfDataSourceFilterToDelete.add(Integer.parseInt(tblReportFilters.getValue().toString()));
					tblReportFilters.removeItem(tblReportFilters.getValue());
				}	
			}
		}));
			
		//START: load report key columns
		loadReportKeyColumnsTable();		
		
		this.btnAddVwHorizontalRptColumn.addClickListener(new Button.ClickListener() {
		
			@Override
			public void buttonClick(ClickEvent event) {
				addReportColumn(null, null);
			}

		});
		
		
		
	}
	
	private void loadDataSourceFilterTable()
	{
		this.tblReportFilters.setContainerDataSource(null);
		this.tblReportFilters.setStyleName("small");
		this.tblReportFilters.addContainerProperty("*", CheckBox.class, null);
		this.tblReportFilters.addContainerProperty("Data Source", ComboBox.class, null);
		this.tblReportFilters.addContainerProperty("Source Column/Field", ComboBox.class, null);
		this.tblReportFilters.addContainerProperty("Filter Operator", ComboBox.class, null);
		this.tblReportFilters.addContainerProperty("Filter Value", GridLayout.class, null);
		//this.tblReportFilters.addContainerProperty("Filter Value 2", TextField.class, null);
		this.tblReportFilters.addContainerProperty("Custom List", ComboBox.class, null);
		this.tblReportFilters.addContainerProperty("Custom List Value", ComboBox.class, null);
		this.tblReportFilters.addContainerProperty("Expression", ComboBox.class, null);
		this.tblReportFilters.setEditable(true);
		this.tblReportFilters.setPageLength(0);
		this.tblReportFilters.setColumnWidth("*", 20);
		this.tblReportFilters.setColumnWidth("Filter Value", 260);
	}
	
	private void addDataSourceFilter(String dataSourceType, Integer dataSourceFilterId, ViewHorizontalReportFilterByExpField vwHorizontalRptFiltersByExpField, ViewHorizontalReportFilterByFpyField vwHorizontalRptFiltersByFpyField, ViewHorizontalReportFilterByFnyField vwHorizontalRptFiltersByFnyField, ViewHorizontalReportFilterByFtyField vwHorizontalRptFiltersByFtyField, ViewHorizontalReportFilterByTargetColumn vwHorizontalRptFiltersByTargetColumn)
	{
		//System.out.println("Data source type: " + dataSourceType + " Data source filter id: " + dataSourceFilterId);
		
		Integer itemId = dataSourceFilterId;
		if(itemId == null)
		{	
			this.lastDataSourceFilterItemId = this.lastDataSourceFilterItemId - 1;
			itemId = this.lastDataSourceFilterItemId;
		}
		
		Object[] itemValues = new Object[8];
		
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
		

		Set<Item> selectedOptGrpPnlFnyRpts = (Set<Item>) optGrpPnlFnyReports.getValue();
		for (Object selectedFnyRpt : selectedOptGrpPnlFnyRpts)
		{
			for(int i=0; activeFnyReports != null && i<activeFnyReports.size(); i++)
			{
				int selectedFnyReportId = Integer.parseInt(selectedFnyRpt.toString());
				if(activeFnyReports.get(i).getFnyReportId() == selectedFnyReportId)
				{
					cbxDataSource.addItem("fny_" + selectedFnyReportId);
					cbxDataSource.setItemCaption("fny_" + selectedFnyReportId, " FNY : " + activeFnyReports.get(i).getFnyReportName());
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

		cbxDataSource.setHeight(20, Unit.PIXELS);
		itemValues[1] = cbxDataSource;
		
		//Loading source fields
		ComboBox cbxDataSourceField = new ComboBox("");
		cbxDataSourceField.setStyleName("tiny");
		cbxDataSourceField.setRequired(true);
		cbxDataSourceField.setRequiredError("This field is required.");
		cbxDataSourceField.setHeight(20, Unit.PIXELS);		
		itemValues[2] = cbxDataSourceField;
		
		
		//Loading filter operator fields
		ComboBox cbxFilterOperator = new ComboBox("");
		cbxFilterOperator.setStyleName("tiny");
		cbxFilterOperator.setHeight(20, Unit.PIXELS);		
		itemValues[3] = cbxFilterOperator;

		GridLayout gridFilterValues = new GridLayout(2,1);
		gridFilterValues.setWidth(240, Unit.PIXELS);
		TextField txtFilterValue1 = new TextField("");
		txtFilterValue1.setCaption(null);	
		txtFilterValue1.setStyleName("tiny");
		txtFilterValue1.setHeight(20, Unit.PIXELS);
		txtFilterValue1.setWidth(120, Unit.PIXELS);
		txtFilterValue1.setInputPrompt("Value1");
		//itemValues[4] = txtFilterValue1;
		gridFilterValues.addComponent(txtFilterValue1, 0, 0);

		TextField txtFilterValue2 = new TextField("");
		txtFilterValue2.setCaption(null);	
		txtFilterValue2.setStyleName("tiny");
		txtFilterValue2.setHeight(20, Unit.PIXELS);
		txtFilterValue1.setWidth(120, Unit.PIXELS);
		txtFilterValue2.setInputPrompt("Value2");
		
		gridFilterValues.setCaption(null);		
		gridFilterValues.addComponent(txtFilterValue2, 1, 0);
		txtFilterValue2.setVisible(false);
		itemValues[4] = gridFilterValues;
		
		//Loading custom lists
		ComboBox cbxCustomLists = new ComboBox("");
		cbxCustomLists.setStyleName("tiny");
		cbxCustomLists.setHeight(20, Unit.PIXELS);
		itemValues[5] = cbxCustomLists;
		
		//Loading custom list values
		ComboBox cbxCustomListValues = new ComboBox("");
		cbxCustomListValues.setStyleName("tiny");
		cbxCustomListValues.setHeight(20, Unit.PIXELS);
		itemValues[6] = cbxCustomListValues;
		
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
		itemValues[7] = cbxExpression;				

		cbxDataSource.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeFilterCbxDataSource(cbxDataSource, cbxDataSourceField, cbxFilterOperator, gridFilterValues);
            }
        });
		
		
		if(itemId != null && dataSourceType!=null)
		{				
			//Adding Data Source Filters
			if(dataSourceType.equals("exp_"))
			{
				cbxDataSource.setValue("exp_" + vwHorizontalRptFiltersByExpField.getVwHorizontalReportByExperiment().getExperiment().getExpId());
				cbxDataSourceField.setValue("exp_" + vwHorizontalRptFiltersByExpField.getExpField().getExpFieldId());				
				cbxFilterOperator.setValue(vwHorizontalRptFiltersByExpField.getVwHorizontalRptFilterByExpFieldOperation());
				String filterOperator = cbxFilterOperator.getValue().toString();
				if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					txtFilterValue1.setValue(vwHorizontalRptFiltersByExpField.getVwHorizontalRptFilterByExpFieldValue1());
					txtFilterValue2.setValue(vwHorizontalRptFiltersByExpField.getVwHorizontalRptFilterByExpFieldValue2());
				}
				else if(filterOperator.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);

		            DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	fromDateField.setValue(df.parse(vwHorizontalRptFiltersByExpField.getVwHorizontalRptFilterByExpFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
		            	toDateField.setValue(df.parse(vwHorizontalRptFiltersByExpField.getVwHorizontalRptFilterByExpFieldValue2()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		            
				}
				else if(filterOperator.equals("after") || filterOperator.equals("before")
	    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
	    				|| filterOperator.equals("onorbefore"))
				{
					DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
					
					DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	dtFilterValue.setValue(df.parse(vwHorizontalRptFiltersByExpField.getVwHorizontalRptFilterByExpFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				}
				else
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(vwHorizontalRptFiltersByExpField.getVwHorizontalRptFilterByExpFieldValue1());
				}
				
				if(vwHorizontalRptFiltersByExpField.getCustomList() != null)
				{
					cbxCustomLists.setValue(vwHorizontalRptFiltersByExpField.getCustomList().getCustomListId());					
					cbxCustomListValues.setValue(vwHorizontalRptFiltersByExpField.getVwHorizontalRptFilterByExpFieldValue1());
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(null);
					txtFilterValue1.setReadOnly(true);
				}
				
				cbxExpression.setValue(vwHorizontalRptFiltersByExpField.getVwHorizontalRptFilterByExpFieldExpression());
			}
			
			if(dataSourceType.equals("fpy_"))
			{
				cbxDataSource.setValue("fpy_" + vwHorizontalRptFiltersByFpyField.getVwHorizontalFpyRpt().getFpyRpt().getFpyReportId());
				
				if(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyIsDateTimeExpField())
					cbxDataSourceField.setValue("fpy_datetime");
				else if(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyIsResultExpField())
					cbxDataSourceField.setValue("fpy_sn");
				else if(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpySNExpField())
					cbxDataSourceField.setValue("fpy_result");
				else
					cbxDataSourceField.setValue("fpy_" +  vwHorizontalRptFiltersByFpyField.getFpyInfoField().getFpyInfoFieldId());	
				
				cbxFilterOperator.setValue(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyFieldOperation());
				
				String filterOperator = cbxFilterOperator.getValue().toString();
				if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					txtFilterValue1.setValue(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyFieldValue1());
					txtFilterValue2.setValue(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyFieldValue2());
				}
				else if(filterOperator.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);

		            DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	fromDateField.setValue(df.parse(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
		            	toDateField.setValue(df.parse(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyFieldValue2()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		            
				}
				else if(filterOperator.equals("after") || filterOperator.equals("before")
	    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
	    				|| filterOperator.equals("onorbefore"))
				{
					DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
					
					DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	dtFilterValue.setValue(df.parse(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				}
				else
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyFieldValue1());
				}
				
				if(vwHorizontalRptFiltersByFpyField.getCustomList() != null)
				{
					cbxCustomLists.setValue(vwHorizontalRptFiltersByFpyField.getCustomList().getCustomListId());			
					cbxCustomListValues.setValue(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyFieldValue1());
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(null);
					txtFilterValue1.setReadOnly(true);				
				}
				
				cbxExpression.setValue(vwHorizontalRptFiltersByFpyField.getVwHorizontalRptFilterByFpyFieldExpression());
			}
				
			if(dataSourceType.equals("fny_"))
			{
				cbxDataSource.setValue("fny_" + vwHorizontalRptFiltersByFnyField.getVwHorizontalFnyRpt().getFnyRpt().getFnyReportId());
				if(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyIsDateTimeExpField())
					cbxDataSourceField.setValue("fny_datetime");
				else if(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyIsResultExpField())
					cbxDataSourceField.setValue("fny_sn");
				else if(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnySNExpField())
					cbxDataSourceField.setValue("fny_result");
				else
					cbxDataSourceField.setValue("fny_" +  vwHorizontalRptFiltersByFnyField.getFnyInfoField().getFnyInfoFieldId());	
				
				cbxFilterOperator.setValue(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyFieldOperation());
				
				String filterOperator = cbxFilterOperator.getValue().toString();
				if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					txtFilterValue1.setValue(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyFieldValue1());
					txtFilterValue2.setValue(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyFieldValue2());
				}
				else if(filterOperator.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);

		            DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	fromDateField.setValue(df.parse(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
		            	toDateField.setValue(df.parse(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyFieldValue2()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		            
				}
				else if(filterOperator.equals("after") || filterOperator.equals("before")
	    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
	    				|| filterOperator.equals("onorbefore"))
				{
					DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
					
					DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	dtFilterValue.setValue(df.parse(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				}
				else
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyFieldValue1());
				}
				
				if(vwHorizontalRptFiltersByFnyField.getCustomList() != null)
				{
					cbxCustomLists.setValue(vwHorizontalRptFiltersByFnyField.getCustomList().getCustomListId());				
					cbxCustomListValues.setValue(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyFieldValue1());
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(null);
					txtFilterValue1.setReadOnly(true);				
				}
				
				cbxExpression.setValue(vwHorizontalRptFiltersByFnyField.getVwHorizontalRptFilterByFnyFieldExpression());
			}

			if(dataSourceType.equals("fty_"))
			{
				cbxDataSource.setValue("fty_" + vwHorizontalRptFiltersByFtyField.getVwHorizontalFtyRpt().getFtyRpt().getFtyReportId());
				if(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyIsDateTimeExpField())
					cbxDataSourceField.setValue("fty_datetime");
				else if(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyIsResultExpField())
					cbxDataSourceField.setValue("fty_sn");
				else if(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtySNExpField())
					cbxDataSourceField.setValue("fty_result");
				else
					cbxDataSourceField.setValue("fty_" +  vwHorizontalRptFiltersByFtyField.getFtyInfoField().getFtyInfoFieldId());
				
				cbxFilterOperator.setValue(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyFieldOperation());
				
				String filterOperator = cbxFilterOperator.getValue().toString();
				if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					txtFilterValue1.setValue(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyFieldValue1());
					txtFilterValue2.setValue(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyFieldValue2());
				}
				else if(filterOperator.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);

		            DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	fromDateField.setValue(df.parse(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
		            	toDateField.setValue(df.parse(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyFieldValue2()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		            
				}
				else if(filterOperator.equals("after") || filterOperator.equals("before")
	    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
	    				|| filterOperator.equals("onorbefore"))
				{
					DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
					
					DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	dtFilterValue.setValue(df.parse(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				}
				else
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyFieldValue1());
				}
				
				if(vwHorizontalRptFiltersByFtyField.getCustomList() != null)
				{
					cbxCustomLists.setValue(vwHorizontalRptFiltersByFtyField.getCustomList().getCustomListId());		
					cbxCustomListValues.setValue(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyFieldValue1());
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(null);
					txtFilterValue1.setReadOnly(true);						
				}

				cbxExpression.setValue(vwHorizontalRptFiltersByFtyField.getVwHorizontalRptFilterByFtyFieldExpression());
			}
			
			if(dataSourceType.equals("tgt_"))
			{
				cbxDataSource.setValue("tgt_" + vwHorizontalRptFiltersByTargetColumn.getVwHorizontalTargetRpt().getTargetRpt().getTargetReportId());
				cbxDataSourceField.setValue("tgt_" +  vwHorizontalRptFiltersByTargetColumn.getTargetColumn().getTargetColumnId());		
				
				
				cbxFilterOperator.setValue(vwHorizontalRptFiltersByTargetColumn.getVwHorizontalRptFilterByTargetColumnOperation());
				
				String filterOperator = cbxFilterOperator.getValue().toString();
				if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					txtFilterValue1.setValue(vwHorizontalRptFiltersByTargetColumn.getVwHorizontalRptFilterByTargetColumnValue1());
					txtFilterValue2.setValue(vwHorizontalRptFiltersByTargetColumn.getVwHorizontalRptFilterByTargetColumnValue2());
				}
				else if(filterOperator.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);

		            DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	fromDateField.setValue(df.parse(vwHorizontalRptFiltersByTargetColumn.getVwHorizontalRptFilterByTargetColumnValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
		            	toDateField.setValue(df.parse(vwHorizontalRptFiltersByTargetColumn.getVwHorizontalRptFilterByTargetColumnValue2()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		            
				}
				else if(filterOperator.equals("after") || filterOperator.equals("before")
	    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
	    				|| filterOperator.equals("onorbefore"))
				{
					DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
					
					DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	dtFilterValue.setValue(df.parse(vwHorizontalRptFiltersByTargetColumn.getVwHorizontalRptFilterByTargetColumnValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				}
				else
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(vwHorizontalRptFiltersByTargetColumn.getVwHorizontalRptFilterByTargetColumnValue1());
				}	
				
				if(vwHorizontalRptFiltersByTargetColumn.getCustomList() != null)
				{
					cbxCustomLists.setValue(vwHorizontalRptFiltersByTargetColumn.getCustomList().getCustomListId());				
					cbxCustomListValues.setValue(vwHorizontalRptFiltersByTargetColumn.getVwHorizontalRptFilterByTargetColumnValue1());
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(null);
					txtFilterValue1.setReadOnly(true);				
				}
				
				cbxExpression.setValue(vwHorizontalRptFiltersByTargetColumn.getVwHorizontalRptFilterByTargetColumnExpression());
			}
			
		}
		
		this.tblReportFilters.addItem(itemValues, itemId);
		this.tblReportFilters.select(itemId);
		
	}	

	private void onChangeFilterCbxDataSource(ComboBox cbxDataSource, ComboBox cbxDataSourceField, ComboBox cbxFilterOperator, GridLayout gridFilterValues)
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

		case "fny_":
			List<FinalPassYieldInfoField> fnyFields = new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldByReportById(dataSourceId);
			cbxDataSourceField.setContainerDataSource(null);
			for(int i=0; fnyFields != null && i<fnyFields.size(); i++)
			{
				cbxDataSourceField.addItem("fny_" + fnyFields.get(i).getFnyInfoFieldId());
				cbxDataSourceField.setItemCaption("fny_" + fnyFields.get(i).getFnyInfoFieldId(), fnyFields.get(i).getFnyInfoFieldLabel());
			}

			cbxDataSourceField.addItem("fny_datetime");
			cbxDataSourceField.setItemCaption("fny_datetime", "Datetime (FPY)");

			cbxDataSourceField.addItem("fny_sn");
			cbxDataSourceField.setItemCaption("fny_sn", "Serial Number (FPY)");

			cbxDataSourceField.addItem("fny_result");
			cbxDataSourceField.setItemCaption("fny_result", "Result (FPY)");
			
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
            	onChangeFilterCbxDataSourceField(cbxDataSourceField, cbxFilterOperator, gridFilterValues);
            }
        });
	}

	private void onChangeFilterCbxDataSourceField(ComboBox cbxDataSourceField, ComboBox cbxFilterOperator, GridLayout gridFilterValues)
	{
		Integer filterDataSourceFieldId = -1;
		
		if(!cbxDataSourceField.getValue().toString().equals("fpy_sn") && !cbxDataSourceField.getValue().toString().equals("fpy_result") && !cbxDataSourceField.getValue().toString().equals("fpy_datetime") 
				&& !cbxDataSourceField.getValue().toString().equals("fny_sn") && !cbxDataSourceField.getValue().toString().equals("fny_result") && !cbxDataSourceField.getValue().toString().equals("fny_datetime")
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

		case "fny_":
			if(cbxDataSourceField.getValue().toString().equals("fny_datetime"))
				dataFieldType = "datetime";
			else if(cbxDataSourceField.getValue().toString().equals("fny_sn") || cbxDataSourceField.getValue().toString().equals("fny_result"))
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
			fillCbxDateFilterOperators(cbxFilterOperator, gridFilterValues);
		else if(dataFieldType.startsWith("varchar") || dataFieldType.startsWith("char")
                || dataFieldType.startsWith("text") || dataFieldType.startsWith("nvarchar")
                || dataFieldType.startsWith("nchar") || dataFieldType.startsWith("ntext"))
				fillCbxStringFilterOperators(cbxFilterOperator, gridFilterValues);
		else 
			fillCbxNumericFilterOperators(cbxFilterOperator, gridFilterValues);		
	}
    
    private void fillCbxDateFilterOperators(ComboBox cbxDateFilterOperators, GridLayout gridFilterValues) 
    {
    	cbxDateFilterOperators.setContainerDataSource(null);

    	cbxDateFilterOperators.addItem("after");
    	cbxDateFilterOperators.setItemCaption("after", "after");

    	cbxDateFilterOperators.addItem("before");
    	cbxDateFilterOperators.setItemCaption("before", "before");
    	
    	cbxDateFilterOperators.addItem("customrange");
    	cbxDateFilterOperators.setItemCaption("customrange", "custom range");
    	
    	cbxDateFilterOperators.addItem("on");
    	cbxDateFilterOperators.setItemCaption("on", "on");

    	cbxDateFilterOperators.addItem("onorafter");
    	cbxDateFilterOperators.setItemCaption("onorafter", "on or after");

    	cbxDateFilterOperators.addItem("onorbefore");
    	cbxDateFilterOperators.setItemCaption("onorbefore", "on or before");

    	cbxDateFilterOperators.addItem("today");
    	cbxDateFilterOperators.setItemCaption("today", "today");
    	cbxDateFilterOperators.addItem("currentweek");
    	cbxDateFilterOperators.setItemCaption("currentweek", "on current week");
    	cbxDateFilterOperators.addItem("currentmonth");
    	cbxDateFilterOperators.setItemCaption("currentmonth", "on current month");
    	cbxDateFilterOperators.addItem("currentyear");
    	cbxDateFilterOperators.setItemCaption("currentyear", "on current year");
    	
    	
    	cbxDateFilterOperators.addItem("onorbeforendaysago");
    	cbxDateFilterOperators.setItemCaption("onorbeforendaysago", "on or before N days ago");
    	cbxDateFilterOperators.addItem("onorbeforenweeksago");
    	cbxDateFilterOperators.setItemCaption("onorbeforenweeksago", "on or before N weeks ago");
    	cbxDateFilterOperators.addItem("onorbeforenmonthsago");
    	cbxDateFilterOperators.setItemCaption("onorbeforenmonthsago", "on or before N months ago");
    	cbxDateFilterOperators.addItem("onorbeforenyearsago");
    	cbxDateFilterOperators.setItemCaption("onorbeforenyearsago", "on or before N years ago");
    	
    	cbxDateFilterOperators.addItem("lastndays");
    	cbxDateFilterOperators.setItemCaption("lastndays", "last N days");
    	cbxDateFilterOperators.addItem("lastnweeks");
    	cbxDateFilterOperators.setItemCaption("lastnweeks", "last N weeks");
    	cbxDateFilterOperators.addItem("lastnmonths");
    	cbxDateFilterOperators.setItemCaption("lastnmonths", "last N months");
    	cbxDateFilterOperators.addItem("lastnyears");
    	cbxDateFilterOperators.setItemCaption("lastnyears", "last N years");    	
    	
    	cbxDateFilterOperators.select("customrange");    

		gridFilterValues.removeAllComponents();
		DateField fromDateField = new DateField();
    	fromDateField.setWidth(95, Sizeable.UNITS_PIXELS);
    	fromDateField.setHeight(25, Sizeable.UNITS_PIXELS);
    	fromDateField.setStyleName("tiny");
		gridFilterValues.addComponent(fromDateField, 0, 0);
    	
    	DateField toDateField = new DateField();
    	toDateField.setWidth(95, Sizeable.UNITS_PIXELS);
    	toDateField.setHeight(25, Sizeable.UNITS_PIXELS);
    	toDateField.setStyleName("tiny");
		gridFilterValues.addComponent(toDateField, 1, 0);
		
    	cbxDateFilterOperators.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeCbxFilterOperator("date", cbxDateFilterOperators, gridFilterValues);
            }
        });
    	
    }
    
    private void fillCbxStringFilterOperators(ComboBox cbxStringFilterOperators, GridLayout gridFilterValues) 
    {
    	cbxStringFilterOperators.setContainerDataSource(null);

    	cbxStringFilterOperators.addItem("contains");
    	cbxStringFilterOperators.setItemCaption("contains", "contains");

    	cbxStringFilterOperators.addItem("doesnotcontain");
    	cbxStringFilterOperators.setItemCaption("doesnotcontain", "does not contain");

    	cbxStringFilterOperators.addItem("doesnotstartwith");
    	cbxStringFilterOperators.setItemCaption("doesnotstartwith", "does not start with");
        
    	cbxStringFilterOperators.addItem("endswith");
    	cbxStringFilterOperators.setItemCaption("endswith", "ends with");
        
    	cbxStringFilterOperators.addItem("is");
    	cbxStringFilterOperators.setItemCaption("is", "is");
        
    	cbxStringFilterOperators.addItem("isempty");
    	cbxStringFilterOperators.setItemCaption("isempty", "is empty");
        
    	cbxStringFilterOperators.addItem("isnot");
    	cbxStringFilterOperators.setItemCaption("isnot", "is not");
        
    	cbxStringFilterOperators.addItem("isnotempty");
    	cbxStringFilterOperators.setItemCaption("isnotempty", "is not empty");
        
    	cbxStringFilterOperators.addItem("startswith");
    	cbxStringFilterOperators.setItemCaption("startswith", "starts with");
        
    	cbxStringFilterOperators.select("is");
        
    	cbxStringFilterOperators.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeCbxFilterOperator("string", cbxStringFilterOperators, gridFilterValues);
            }
        });
    	
    	gridFilterValues.removeAllComponents();
		TextField txtFilterValue1 = new TextField("");
		txtFilterValue1.setCaption(null);	
		txtFilterValue1.setStyleName("tiny");
		txtFilterValue1.setHeight(20, Unit.PIXELS);
		txtFilterValue1.setWidth(110, Unit.PIXELS);
		txtFilterValue1.setInputPrompt("Value1");
		gridFilterValues.addComponent(txtFilterValue1, 0, 0);
    }    

    private void fillCbxNumericFilterOperators(ComboBox cbxNumericFilterOperators, GridLayout gridFilterValues) 
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
        
        cbxNumericFilterOperators.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeCbxFilterOperator("numeric", cbxNumericFilterOperators, gridFilterValues);
            }
        });
       
    }   
    
    private void onChangeCbxFilterOperator(String filterDataType, ComboBox cbxSelectedFilter, GridLayout gridFilterValues)
    {
    	if(cbxSelectedFilter.getValue() == null)
    		return;
    	
    	if(filterDataType.equals("string"))
    	{
    		gridFilterValues.removeAllComponents();
    		TextField txtFilterValue1 = new TextField("");
    		txtFilterValue1.setCaption(null);	
    		txtFilterValue1.setStyleName("tiny");
    		txtFilterValue1.setHeight(20, Unit.PIXELS);
    		txtFilterValue1.setWidth(110, Unit.PIXELS);
    		txtFilterValue1.setInputPrompt("Value1");
    		gridFilterValues.addComponent(txtFilterValue1, 0, 0);

    		TextField txtFilterValue2 = new TextField("");
    		txtFilterValue2.setCaption(null);	
    		txtFilterValue2.setStyleName("tiny");
    		txtFilterValue2.setHeight(20, Unit.PIXELS);
    		txtFilterValue2.setWidth(110, Unit.PIXELS);
    		txtFilterValue2.setInputPrompt("Value2");
    		gridFilterValues.setCaption(null);		
    		gridFilterValues.addComponent(txtFilterValue2, 1, 0);
    		txtFilterValue2.setVisible(false);
    		
    		if(cbxSelectedFilter.getValue().toString().equals("isempty") || cbxSelectedFilter.getValue().toString().equals("isnotempty"))
    			txtFilterValue1.setVisible(false);
    	}    	

    	if(filterDataType.equals("numeric"))
    	{
    		gridFilterValues.removeAllComponents();
    		TextField txtFilterValue1 = new TextField("");
    		txtFilterValue1.setCaption(null);	
    		txtFilterValue1.setStyleName("tiny");
    		txtFilterValue1.setHeight(20, Unit.PIXELS);
    		txtFilterValue1.setWidth(110, Unit.PIXELS);
    		txtFilterValue1.setInputPrompt("Value1");
    		gridFilterValues.addComponent(txtFilterValue1, 0, 0);

    		TextField txtFilterValue2 = new TextField("");
    		txtFilterValue2.setCaption(null);	
    		txtFilterValue2.setStyleName("tiny");
    		txtFilterValue2.setHeight(20, Unit.PIXELS);
    		txtFilterValue2.setWidth(110, Unit.PIXELS);
    		txtFilterValue2.setInputPrompt("Value2");
    		gridFilterValues.setCaption(null);		
    		gridFilterValues.addComponent(txtFilterValue2, 1, 0);
    		txtFilterValue2.setVisible(false);
    		
    		if(cbxSelectedFilter.getValue().toString().equals("between") || cbxSelectedFilter.getValue().toString().equals("notbetween"))
    			txtFilterValue2.setVisible(true);
    		if(cbxSelectedFilter.getValue().toString().equals("isempty") || cbxSelectedFilter.getValue().toString().equals("isnotempty"))
    			txtFilterValue1.setVisible(false);
    	}

    	if(filterDataType.equals("date"))
    	{
    		gridFilterValues.removeAllComponents();
    		
    		if(cbxSelectedFilter.getValue().toString().equals("customrange"))
    		{
    			DateField fromDateField = new DateField();
    	    	fromDateField.setWidth(95, Sizeable.UNITS_PIXELS);
    	    	fromDateField.setHeight(25, Sizeable.UNITS_PIXELS);
    	    	fromDateField.setStyleName("tiny");
        		gridFilterValues.addComponent(fromDateField, 0, 0);
    	    	
    	    	DateField toDateField = new DateField();
    	    	toDateField.setWidth(95, Sizeable.UNITS_PIXELS);
    	    	toDateField.setHeight(25, Sizeable.UNITS_PIXELS);
    	    	toDateField.setStyleName("tiny");
        		gridFilterValues.addComponent(toDateField, 1, 0);
    		}
    		
    		if(cbxSelectedFilter.getValue().toString().equals("after") || cbxSelectedFilter.getValue().toString().equals("before")
    				|| cbxSelectedFilter.getValue().toString().equals("on") || cbxSelectedFilter.getValue().toString().equals("onorafter")
    				|| cbxSelectedFilter.getValue().toString().equals("onorbefore"))
    		{
    			DateField fromDateField = new DateField();
    	    	fromDateField.setWidth(95, Sizeable.UNITS_PIXELS);
    	    	fromDateField.setHeight(25, Sizeable.UNITS_PIXELS);
    	    	fromDateField.setStyleName("tiny");
        		gridFilterValues.addComponent(fromDateField, 0, 0);
    	    }
    		
    		if(cbxSelectedFilter.getValue().toString().equals("onorbeforendaysago") || cbxSelectedFilter.getValue().toString().equals("onorbeforenweeksago")
    				|| cbxSelectedFilter.getValue().toString().equals("onorbeforenmonthsago") || cbxSelectedFilter.getValue().toString().equals("onorbeforenyearsago")
    				|| cbxSelectedFilter.getValue().toString().equals("lastndays") || cbxSelectedFilter.getValue().toString().equals("lastnweeks")
    				|| cbxSelectedFilter.getValue().toString().equals("lastnmonths") || cbxSelectedFilter.getValue().toString().equals("lastnyears"))
    		{
        		TextField txtFilterValue1 = new TextField("");
        		txtFilterValue1.setCaption(null);	
        		txtFilterValue1.setStyleName("tiny");
        		txtFilterValue1.setHeight(20, Unit.PIXELS);
        		txtFilterValue1.setWidth(110, Unit.PIXELS);
        		txtFilterValue1.setInputPrompt("N");
        		gridFilterValues.addComponent(txtFilterValue1, 0, 0);
    	    }

    	}    	

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

	private void loadReportKeyColumnsTable()
	{
		tblKeyColumnMapping.setContainerDataSource(null);
		tblKeyColumnMapping.setStyleName("small");
		tblKeyColumnMapping.setHeight(100, Unit.PERCENTAGE);
		tblKeyColumnMapping.setWidth(100, Unit.PERCENTAGE);
		tblKeyColumnMapping.addContainerProperty("Data Source Table/Report", ComboBox.class, null);
		tblKeyColumnMapping.addContainerProperty("Data Source Field", ComboBox.class, null);
		tblKeyColumnMapping.setEditable(true);
		tblKeyColumnMapping.setPageLength(0);		
	}
		
	private void addReportColumn(Integer reportColumnId, ViewHorizontalReportColumn vwHorizontalRptCol)
	{
		VerticalLayout vwHorizontalColumnPnl = new VerticalLayout();
		vwHorizontalColumnPnl.setStyleName("well");
		vwHorizontalColumnPnl.setWidth(200, Unit.PIXELS);
		vwHorizontalColumnPnl.setSpacing(true);
		
		TextField txtColumnName = new TextField();
		txtColumnName.setCaption("Column Name");
		txtColumnName.setWidth(90, Unit.PERCENTAGE);
		txtColumnName.setStyleName("tiny");
		
		TextField txtColumnDbId = new TextField();
		txtColumnDbId.setCaption("Column Db Id");
		txtColumnDbId.setWidth(90, Unit.PERCENTAGE);
		txtColumnDbId.setStyleName("tiny");
		
		ComboBox cbxDataSource = new ComboBox();
		cbxDataSource.setCaption("Data Source");
		cbxDataSource.setWidth(90, Unit.PERCENTAGE);
		cbxDataSource.setStyleName("tiny");
		
		ComboBox cbxDataSourceFieldColumn = new ComboBox();
		cbxDataSourceFieldColumn.setCaption("Data Source Field/Column");
		cbxDataSourceFieldColumn.setWidth(90, Unit.PERCENTAGE);
		cbxDataSourceFieldColumn.setStyleName("tiny");
		
		Button btnRemoveColumn = new Button();
		btnRemoveColumn.setCaption("Remove");
		btnRemoveColumn.setStyleName("tiny");
		btnRemoveColumn.addStyleName("danger");

		vwHorizontalColumnPnl.addComponent(txtColumnName);
		vwHorizontalColumnPnl.setComponentAlignment(txtColumnName, Alignment.MIDDLE_CENTER);
		vwHorizontalColumnPnl.addComponent(txtColumnDbId);
		vwHorizontalColumnPnl.setComponentAlignment(txtColumnDbId, Alignment.MIDDLE_CENTER);
		vwHorizontalColumnPnl.addComponent(cbxDataSource);
		vwHorizontalColumnPnl.setComponentAlignment(cbxDataSource, Alignment.MIDDLE_CENTER);
		vwHorizontalColumnPnl.addComponent(cbxDataSourceFieldColumn);
		vwHorizontalColumnPnl.setComponentAlignment(cbxDataSourceFieldColumn, Alignment.MIDDLE_CENTER);
		vwHorizontalColumnPnl.addComponent(btnRemoveColumn);
		vwHorizontalColumnPnl.setComponentAlignment(btnRemoveColumn, Alignment.MIDDLE_CENTER);
		
		this.lytVwHorizontalRptColumnsPnls.addComponent(vwHorizontalColumnPnl);
		txtColumnName.focus();
	}
}
