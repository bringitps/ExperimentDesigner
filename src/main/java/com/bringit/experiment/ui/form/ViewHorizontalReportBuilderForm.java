package com.bringit.experiment.ui.form;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.bringit.experiment.WebApplication;
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
import com.bringit.experiment.bll.SysUser;
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
import com.bringit.experiment.bll.ViewVerticalReport;
import com.bringit.experiment.bll.ViewVerticalReportByExperiment;
import com.bringit.experiment.bll.ViewVerticalReportByFnyRpt;
import com.bringit.experiment.bll.ViewVerticalReportByFpyRpt;
import com.bringit.experiment.bll.ViewVerticalReportByFtyRpt;
import com.bringit.experiment.bll.ViewVerticalReportByTargetRpt;
import com.bringit.experiment.bll.ViewVerticalReportColumn;
import com.bringit.experiment.bll.ViewVerticalReportColumnByEnrichment;
import com.bringit.experiment.bll.ViewVerticalReportColumnByExpField;
import com.bringit.experiment.bll.ViewVerticalReportColumnByFnyField;
import com.bringit.experiment.bll.ViewVerticalReportColumnByFpyField;
import com.bringit.experiment.bll.ViewVerticalReportColumnByFtyField;
import com.bringit.experiment.bll.ViewVerticalReportColumnByTargetColumn;
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
import com.bringit.experiment.dao.ViewVerticalReportColumnByExpFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportColumnByFnyFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportColumnByFpyFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportColumnByFtyFieldDao;
import com.bringit.experiment.dao.ViewVerticalReportColumnByTargetColumnDao;
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
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
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
	private List<String> vwHorizontalRptByExperimentKeyColStrIds = new ArrayList<String>();
	private List<ViewHorizontalReportByFpyRpt> vwHorizontalRptByFpyRptList = new ArrayList<ViewHorizontalReportByFpyRpt>();
	private List<Integer> vwHorizontalRptByFpyRptDbIds =  new ArrayList<Integer>();	
	private List<String> vwHorizontalRptByFpyRptKeyColStrIds = new ArrayList<String>();
	private List<ViewHorizontalReportByFnyRpt> vwHorizontalRptByFnyRptList = new ArrayList<ViewHorizontalReportByFnyRpt>();
	private List<Integer> vwHorizontalRptByFnyRptDbIds =  new ArrayList<Integer>();
	private List<String> vwHorizontalRptByFnyRptKeyColStrIds = new ArrayList<String>();
	private List<ViewHorizontalReportByFtyRpt> vwHorizontalRptByFtyRptList = new ArrayList<ViewHorizontalReportByFtyRpt>();
	private List<Integer> vwHorizontalRptByFtyRptDbIds =  new ArrayList<Integer>();
	private List<String> vwHorizontalRptByFtyRptKeyColStrIds = new ArrayList<String>();
	private List<ViewHorizontalReportByTargetRpt> vwHorizontalRptByTgtRptList = new ArrayList<ViewHorizontalReportByTargetRpt>();
	private List<Integer> vwHorizontalRptByTargetDbIds =  new ArrayList<Integer>();
	private List<String> vwHorizontalRptByTargetRptKeyColStrIds = new ArrayList<String>();
	
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

		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		
		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
			dbfieldTypes = configuration.getProperty("sqlserverdatatypes").split(",");
		 
		for(int j=0; j<dbfieldTypes.length; j++)
		{
			cbxVwHorizontalRptKeyColDataType.addItem(dbfieldTypes[j]);
			cbxVwHorizontalRptKeyColDataType.setItemCaption(dbfieldTypes[j], dbfieldTypes[j]);
		}
		
		//START: load report key columns
		loadReportKeyColumnsTable();
		
		cbxVwHorizontalRptKeyColDataType.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	refreshColumnKeysTable();
            }
        });
		
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
				vwHorizontalRptByExperimentKeyColStrIds.add("expfield_" + vwHorizontalRptByExperimentList.get(i).getExpKeyField().getExpFieldId());
				List<ViewHorizontalReportFilterByExpField> currentVwHorizontalRptFiltersByExpField = new ViewHorizontalReportFilterByExpFieldDao().getAllVwHorizontalReportFiltersByExpRptId(vwHorizontalRptByExperimentList.get(i).getVwHorizontalRptByExperimentId());
				for(int j=0; currentVwHorizontalRptFiltersByExpField!= null && j<currentVwHorizontalRptFiltersByExpField.size(); j++)
					vwHorizontalRptFiltersByExpField.add(currentVwHorizontalRptFiltersByExpField.get(j));
			}

			for(int i=0; vwHorizontalRptByFpyRptList != null && i<vwHorizontalRptByFpyRptList.size(); i++)
			{
				vwHorizontalRptByFpyRptDbIds.add(vwHorizontalRptByFpyRptList.get(i).getFpyRpt().getFpyReportId());
				
				if(vwHorizontalRptByFpyRptList.get(i).getFpyKeyInfoField() != null)
					vwHorizontalRptByFpyRptKeyColStrIds.add("fpyfield_" + vwHorizontalRptByFpyRptList.get(i).getFpyKeyInfoField().getFpyInfoFieldId());
				else if(vwHorizontalRptByFpyRptList.get(i).getVwHorizontalFpyKeyIsDateTimeExpField() != null && vwHorizontalRptByFpyRptList.get(i).getVwHorizontalFpyKeyIsDateTimeExpField())
					vwHorizontalRptByFpyRptKeyColStrIds.add("fpy_datetime");
				else if(vwHorizontalRptByFpyRptList.get(i).getVwHorizontalFpyKeyIsResultExpField() != null && vwHorizontalRptByFpyRptList.get(i).getVwHorizontalFpyKeyIsResultExpField())
					vwHorizontalRptByFpyRptKeyColStrIds.add("fpy_result");
				else if(vwHorizontalRptByFpyRptList.get(i).getVwHorizontalFpyKeyIsSNExpField() != null && vwHorizontalRptByFpyRptList.get(i).getVwHorizontalFpyKeyIsSNExpField())
					vwHorizontalRptByFpyRptKeyColStrIds.add("fpy_sn");
			
				List<ViewHorizontalReportFilterByFpyField> currentVwHorizontalRptFiltersByFpyField = new ViewHorizontalReportFilterByFpyFieldDao().getAllVwHorizontalReportFiltersByFpyRptId(vwHorizontalRptByFpyRptList.get(i).getVwHorizontalRptByFpyId());
				for(int j=0; currentVwHorizontalRptFiltersByFpyField != null && j<currentVwHorizontalRptFiltersByFpyField.size(); j++)
					vwHorizontalRptFiltersByFpyField.add(currentVwHorizontalRptFiltersByFpyField.get(j));
			}

			for(int i=0; vwHorizontalRptByFnyRptList != null && i<vwHorizontalRptByFnyRptList.size(); i++)
			{
				vwHorizontalRptByFnyRptDbIds.add(vwHorizontalRptByFnyRptList.get(i).getFnyRpt().getFnyReportId());
				
				if(vwHorizontalRptByFnyRptList.get(i).getFnyKeyInfoField() != null)
					vwHorizontalRptByFnyRptKeyColStrIds.add("fnyfield_" + vwHorizontalRptByFnyRptList.get(i).getFnyKeyInfoField().getFnyInfoFieldId());
				else if(vwHorizontalRptByFnyRptList.get(i).getVwHorizontalFnyKeyIsDateTimeExpField() != null && vwHorizontalRptByFnyRptList.get(i).getVwHorizontalFnyKeyIsDateTimeExpField())
					vwHorizontalRptByFnyRptKeyColStrIds.add("fny_datetime");
				else if(vwHorizontalRptByFnyRptList.get(i).getVwHorizontalFnyKeyIsResultExpField() != null && vwHorizontalRptByFnyRptList.get(i).getVwHorizontalFnyKeyIsResultExpField())
					vwHorizontalRptByFnyRptKeyColStrIds.add("fny_result");
				else if(vwHorizontalRptByFnyRptList.get(i).getVwHorizontalFnyKeyIsSNExpField() != null && vwHorizontalRptByFnyRptList.get(i).getVwHorizontalFnyKeyIsSNExpField())
					vwHorizontalRptByFnyRptKeyColStrIds.add("fny_sn");
				
				List<ViewHorizontalReportFilterByFnyField> currentVwHorizontalRptFiltersByFnyField = new ViewHorizontalReportFilterByFnyFieldDao().getAllVwHorizontalReportFiltersByFnyRptId(vwHorizontalRptByFnyRptList.get(i).getVwHorizontalRptByFnyId());
				for(int j=0; currentVwHorizontalRptFiltersByFnyField != null && j<currentVwHorizontalRptFiltersByFnyField.size(); j++)
					vwHorizontalRptFiltersByFnyField.add(currentVwHorizontalRptFiltersByFnyField.get(j));
			}

			for(int i=0; vwHorizontalRptByFtyRptList != null && i<vwHorizontalRptByFtyRptList.size(); i++)
			{
				vwHorizontalRptByFtyRptDbIds.add(vwHorizontalRptByFtyRptList.get(i).getFtyRpt().getFtyReportId());
				
				if(vwHorizontalRptByFtyRptList.get(i).getFtyKeyInfoField() != null)
					vwHorizontalRptByFtyRptKeyColStrIds.add("ftyfield_" + vwHorizontalRptByFtyRptList.get(i).getFtyKeyInfoField().getFtyInfoFieldId());
				else if(vwHorizontalRptByFtyRptList.get(i).getVwHorizontalFtyKeyIsDateTimeExpField() != null && vwHorizontalRptByFtyRptList.get(i).getVwHorizontalFtyKeyIsDateTimeExpField())
					vwHorizontalRptByFtyRptKeyColStrIds.add("fty_datetime");
				else if(vwHorizontalRptByFtyRptList.get(i).getVwHorizontalFtyKeyIsResultExpField() != null && vwHorizontalRptByFtyRptList.get(i).getVwHorizontalFtyKeyIsResultExpField())
					vwHorizontalRptByFtyRptKeyColStrIds.add("fty_result");
				else if(vwHorizontalRptByFtyRptList.get(i).getVwHorizontalFtyKeyIsSNExpField() != null && vwHorizontalRptByFtyRptList.get(i).getVwHorizontalFtyKeyIsSNExpField())
					vwHorizontalRptByFtyRptKeyColStrIds.add("fty_sn");
				
				List<ViewHorizontalReportFilterByFtyField> currentVwHorizontalRptFiltersByFtyField = new ViewHorizontalReportFilterByFtyFieldDao().getAllVwHorizontalReportFiltersByFtyRptId(vwHorizontalRptByFtyRptList.get(i).getVwHorizontalRptByFtyId());
				for(int j=0; currentVwHorizontalRptFiltersByFtyField != null && j<currentVwHorizontalRptFiltersByFtyField.size(); j++)
					vwHorizontalRptFiltersByFtyField.add(currentVwHorizontalRptFiltersByFtyField.get(j));
			}

			for(int i=0; vwHorizontalRptByTgtRptList != null && i<vwHorizontalRptByTgtRptList.size(); i++)
			{
				vwHorizontalRptByTargetDbIds.add(vwHorizontalRptByTgtRptList.get(i).getTargetRpt().getTargetReportId());
				vwHorizontalRptByTargetRptKeyColStrIds.add("tgtfield_" + vwHorizontalRptByTgtRptList.get(i).getTargetKeyColumn().getTargetColumnId());
				
				List<ViewHorizontalReportFilterByTargetColumn> currentVwHorizontalRptFiltersByTgtColumn = new ViewHorizontalReportFilterByTargetColumnDao().getAllVwHorizontalReportFiltersByTargetRptId(vwHorizontalRptByTgtRptList.get(i).getVwHorizontalRptByTargetRptId());
				for(int j=0; currentVwHorizontalRptFiltersByTgtColumn != null && j<currentVwHorizontalRptFiltersByTgtColumn.size(); j++)
					vwHorizontalRptFiltersByTgtCol.add(currentVwHorizontalRptFiltersByTgtColumn.get(j));
			}
		}
		
		
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
        	   refreshColumnKeysTable();
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
        	   refreshColumnKeysTable();
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
        	   refreshColumnKeysTable();
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
        	   refreshColumnKeysTable();
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

		//START: Load data source filter tables
		loadDataSourceFilterTable();

		//START: Load report columns enrichment
		loadTblEnrichmentRulesData();
		
		if(!isNewRecord)
		{			
			//Load Data Source Filters
			for(int i=0; vwHorizontalRptFiltersByExpField != null && i<vwHorizontalRptFiltersByExpField.size(); i++)
				addDataSourceFilter("exp_", vwHorizontalRptFiltersByExpField.get(i).getVwHorizontalRptFilterByExpFieldId(), vwHorizontalRptFiltersByExpField.get(i), null, null, null, null);
			
			for(int i=0; vwHorizontalRptFiltersByFpyField != null && i<vwHorizontalRptFiltersByFpyField.size(); i++)
				addDataSourceFilter("fpy_", vwHorizontalRptFiltersByFpyField.get(i).getVwHorizontalRptFilterByFpyFieldId(), null, vwHorizontalRptFiltersByFpyField.get(i), null, null, null);
			
			for(int i=0; vwHorizontalRptFiltersByFnyField != null && i<vwHorizontalRptFiltersByFnyField.size(); i++)
				addDataSourceFilter("fny_", vwHorizontalRptFiltersByFnyField.get(i).getVwHorizontalRptFilterByFnyFieldId(), null, null, vwHorizontalRptFiltersByFnyField.get(i), null, null);
			
			for(int i=0; vwHorizontalRptFiltersByFtyField != null && i<vwHorizontalRptFiltersByFtyField.size(); i++)
				addDataSourceFilter("fty_", vwHorizontalRptFiltersByFtyField.get(i).getVwHorizontalRptFilterByFtyFieldId(), null, null, null, vwHorizontalRptFiltersByFtyField.get(i), null);
			
			for(int i=0; vwHorizontalRptFiltersByTgtCol != null && i<vwHorizontalRptFiltersByTgtCol.size(); i++)
				addDataSourceFilter("tgt_", vwHorizontalRptFiltersByTgtCol.get(i).getVwHorizontalRptFilterByTargetColumnId(), null, null, null, null, vwHorizontalRptFiltersByTgtCol.get(i));
			
			
			//Load Report Header information
			this.txtVwHorizontalRptName.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptName());
			this.txtVwHorizontalRptCustomId.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptDbTableNameId().replace("vwhorizontal#", ""));
			this.txtVwHorizontalDescription.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptDescription());
			this.chxActive.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptIsActive());
			
			//Load Key Column information
			this.txtVwHorizontalRptKeyColName.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptKeyColumnName());
			this.txtVwHorizontalRptKeyColCustomId.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptKeyColumnDbId());
			this.cbxVwHorizontalRptKeyColDataType.setValue(this.savedVwHorizontalRpt.getVwHorizontalRptKeyColumnDataType());
			
			//Load report columns 
			vwHorizontalRptColumns = new ViewHorizontalReportColumnDao().getAllVwHorizontalReportColumnsByRptId(vwHorizontalRptId);
			
			for(int i=0; vwHorizontalRptColumns != null && i<vwHorizontalRptColumns.size(); i++)
				addReportColumn(vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnId(), vwHorizontalRptColumns.get(i));
			
			//Load report columns enrichment
			for(int i=0; vwHorizontalRptColumns != null && i<vwHorizontalRptColumns.size(); i++)
			{
				List<ViewHorizontalReportColumnByEnrichment> currentVwHorizontalRptColumnsByEnrichment = new ViewHorizontalReportColumnByEnrichmentDao().getAllVwHorizontalRptColsByEnrichmentByColId(vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnId());

				for(int j=0; currentVwHorizontalRptColumnsByEnrichment != null && j<currentVwHorizontalRptColumnsByEnrichment.size(); j++)
					vwHorizontalRptColumnsByEnrichment.add(currentVwHorizontalRptColumnsByEnrichment.get(j));
			}
			
			//Load Rpt Columns Enrichment
			for(int i=0; i<vwHorizontalRptColumnsByEnrichment.size(); i++)
				addTblEnrichmentRule(vwHorizontalRptColumnsByEnrichment.get(i).getVwHorizontalRptColumnByEnrichmentId(), vwHorizontalRptColumnsByEnrichment.get(i));			
		
		}
		
		
		this.cbxVwHorizontalRptKeyColDataType.addValueChangeListener(new ValueChangeListener(){
	           @Override
				public void valueChange(ValueChangeEvent event) {
	        	   refreshColumnKeysTable();
	           }
	        });		
		
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
		
		
		this.btnAddEnrichment.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addTblEnrichmentRule(null, null);
			}

		});

		this.btnRemoveEnrichment.addClickListener((new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(tblColumnsEnrichment.getValue() != null)
				{
					dbIdOfVwVertRptEnrichmentToDelete.add(Integer.parseInt(tblColumnsEnrichment.getValue().toString()));
					tblColumnsEnrichment.removeItem(tblColumnsEnrichment.getValue());
				}	
			}
		}));
		
		this.btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onSave();
			}
		});
		
		

		this.btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				closeModalWindow();
			}

		});
		
		this.btnDelete.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onDelete();
			}

		});		
		
		
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
	
	private void addTblEnrichmentRule(Integer horizontalRptEnrichmentRuleId, ViewHorizontalReportColumnByEnrichment vwHorizontalRptColumnsByEnrichment)
	{		
		Integer itemId = horizontalRptEnrichmentRuleId;
		if(itemId == null)
		{	
			this.lastHorizontalRptEnrichmentItemId = this.lastHorizontalRptEnrichmentItemId - 1;
			itemId = this.lastHorizontalRptEnrichmentItemId;
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
		
		for(int i=0; i<lytVwHorizontalRptColumnsPnls.getComponentCount(); i++)
		{
			VerticalLayout pnlRptColumn = (VerticalLayout)lytVwHorizontalRptColumnsPnls.getComponent(i);

			TextField txtVwHorizontalRptColumnName = (TextField)pnlRptColumn.getComponent(0);
			TextField txtVwHorizontalRptColumnDbId = (TextField)pnlRptColumn.getComponent(1);
			ComboBox cbxVwHorizontalRptColDataSource = (ComboBox)pnlRptColumn.getComponent(2);
			ComboBox cbxVwHorizontalRptColDataSourceField = (ComboBox)pnlRptColumn.getComponent(3);
			
			cbxRptColumnSource.addItem(txtVwHorizontalRptColumnDbId.getValue().toString());
			cbxRptColumnSource.setItemCaption(txtVwHorizontalRptColumnDbId.getValue().toString(), txtVwHorizontalRptColumnName.getValue().toString());
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
		
		if(itemId != null && vwHorizontalRptColumnsByEnrichment != null)
		{
			cbxRptColumnSource.setValue(vwHorizontalRptColumnsByEnrichment.getVwHorizontalReportColumn().getVwHorizontalRptColumnDbId());
			cbxOperator.setValue(vwHorizontalRptColumnsByEnrichment.getVwHorizontalRptColumnEnrichmentOperation());
			txtComparisonValue.setValue(vwHorizontalRptColumnsByEnrichment.getVwHorizontalRptColumnEnrichmentValue());
			cbxEnrichmentType.setValue(vwHorizontalRptColumnsByEnrichment.getVwHorizontalRptColumnEnrichmentType());
			cbxCustomList.setValue(vwHorizontalRptColumnsByEnrichment.getCustomListValue());
			cbxCustomListValue.setValue(vwHorizontalRptColumnsByEnrichment.getCustomListValue());
			txtStaticValue.setValue(vwHorizontalRptColumnsByEnrichment.getVwHorizontalRptColumnEnrichmentStaticValue());			
		}
		
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
	
	private void refreshColumnKeysTable()
	{				
		List<String> currentDataSourcesIds = new ArrayList<String>();
		List<String> changedDataSourcesIds = new ArrayList<String>();
		List<String> changedDataSourcesNames = new ArrayList<String>();
		List<String> dataSourcesIdsToAdd = new ArrayList<String>();
		List<String> dataSourcesNamesToAdd = new ArrayList<String>();
		List<String> dataSourcesIdsToDelete = new ArrayList<String>();
		
		if(this.tblKeyColumnMapping.getContainerDataSource().getItemIds() != null)
		{
			for (Object rptKeyColMappingItemId: this.tblKeyColumnMapping.getContainerDataSource().getItemIds()) 
			{
				Item tblVwHorizontalRptKeyColMappingRowItem = tblKeyColumnMapping.getContainerDataSource().getItem(rptKeyColMappingItemId);
				
				ComboBox cbxDataSource = (ComboBox)(tblVwHorizontalRptKeyColMappingRowItem.getItemProperty("Data Source Table/Report").getValue());
				//ComboBox cbxDataSourceField = (ComboBox)(tblVwHorizontalRptKeyColMappingRowItem.getItemProperty("Data Source Field").getValue());
				if(cbxDataSource != null)
					currentDataSourcesIds.add(cbxDataSource.getValue().toString());
			}
		}
		
		Set<Item> selectedOptGrpPnlExperiments = (Set<Item>) optGrpPnlExperiments.getValue();
		for (Object selectedExperiment : selectedOptGrpPnlExperiments)
		{
			for(int i=0; activeExperiments != null && i<activeExperiments.size(); i++)
			{
				int selectedExperimentId = Integer.parseInt(selectedExperiment.toString());				
				if(activeExperiments.get(i).getExpId() == selectedExperimentId)
				{
					changedDataSourcesIds.add("exp_" + selectedExperimentId);
					changedDataSourcesNames.add("EXP : " + activeExperiments.get(i).getExpName());
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
					changedDataSourcesIds.add("fpy_" + selectedFpyReportId);
					changedDataSourcesNames.add("FPY : " + activeFpyReports.get(i).getFpyReportName());					
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
					changedDataSourcesIds.add("fny_" + selectedFnyReportId);
					changedDataSourcesNames.add("FNY : " + activeFnyReports.get(i).getFnyReportName());
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
					changedDataSourcesIds.add("fty_" + selectedFtyReportId);
					changedDataSourcesNames.add("FTY : " + activeFtyReports.get(i).getFtyReportName());
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
					changedDataSourcesIds.add("tgt_" + selectedTargetReportId);
					changedDataSourcesNames.add("Target : " + activeTargetReports.get(i).getTargetReportName());
					break;
				}
			}
		}
		
		//Identify new datasources
		for(int i=0; i<changedDataSourcesIds.size(); i++)
		{
			if(currentDataSourcesIds.indexOf(changedDataSourcesIds.get(i)) == -1)
			{
				dataSourcesIdsToAdd.add(changedDataSourcesIds.get(i));
				dataSourcesNamesToAdd.add(changedDataSourcesNames.get(i));
			}
		}		

		//Identify datasources to remove
		for(int i=0; i<currentDataSourcesIds.size(); i++)
		{
			if(changedDataSourcesIds.indexOf(currentDataSourcesIds.get(i)) == -1)
				dataSourcesIdsToDelete.add(currentDataSourcesIds.get(i));
		}
		
		
		for(int i=0; i<dataSourcesIdsToAdd.size(); i++)
		{
			Object[] rptKeyColMapItemValues = new Object[2];

			//Loading field types				
			ComboBox cbxDataSource = new ComboBox("");
			cbxDataSource.setStyleName("tiny");
			cbxDataSource.setRequired(true);
			cbxDataSource.setRequiredError("This field is required.");
			cbxDataSource.setHeight(20, Unit.PIXELS);
			cbxDataSource.setWidth(100, Unit.PERCENTAGE);	
			cbxDataSource.addItem(dataSourcesIdsToAdd.get(i));
			cbxDataSource.setItemCaption(dataSourcesIdsToAdd.get(i), dataSourcesNamesToAdd.get(i));
			cbxDataSource.setValue(dataSourcesIdsToAdd.get(i));
			cbxDataSource.setReadOnly(true);
			rptKeyColMapItemValues[0] = cbxDataSource;
			
			ComboBox cbxDataSourceField = new ComboBox("");
			cbxDataSourceField.setStyleName("tiny");
			cbxDataSourceField.setRequired(true);
			cbxDataSourceField.setRequiredError("This field is required.");
			cbxDataSourceField.setHeight(20, Unit.PIXELS);
			cbxDataSourceField.setWidth(100, Unit.PERCENTAGE);
			rptKeyColMapItemValues[1] = cbxDataSourceField;
			
			tblKeyColumnMapping.addItem(rptKeyColMapItemValues, dataSourcesIdsToAdd.get(i));
		}
		
		for(int i=0; i<dataSourcesIdsToDelete.size(); i++)
			tblKeyColumnMapping.removeItem(dataSourcesIdsToDelete.get(i));
		
		if(this.cbxVwHorizontalRptKeyColDataType != null && this.cbxVwHorizontalRptKeyColDataType.getValue() != null)
		{
			if(this.tblKeyColumnMapping.getContainerDataSource().getItemIds() != null)
			{
				for (Object rptKeyColMappingItemId: this.tblKeyColumnMapping.getContainerDataSource().getItemIds()) 
				{
					Item tblVwHorizontalRptKeyColMappingRowItem = tblKeyColumnMapping.getContainerDataSource().getItem(rptKeyColMappingItemId);
					ComboBox cbxDataSource = (ComboBox)(tblVwHorizontalRptKeyColMappingRowItem.getItemProperty("Data Source Table/Report").getValue());
					ComboBox cbxDataSourceField = (ComboBox)(tblVwHorizontalRptKeyColMappingRowItem.getItemProperty("Data Source Field").getValue());
					String dataSourceIdStr = cbxDataSource.getValue().toString();
					
					Integer expDataSourceId = dataSourceIdStr.startsWith("exp") ? Integer.parseInt(dataSourceIdStr.substring(4)) : null;
					Integer fpyDataSourceId = dataSourceIdStr.startsWith("fpy") ? Integer.parseInt(dataSourceIdStr.substring(4)) : null;
					Integer fnyDataSourceId = dataSourceIdStr.startsWith("fny") ? Integer.parseInt(dataSourceIdStr.substring(4)) : null;
					Integer ftyDataSourceId = dataSourceIdStr.startsWith("fty") ? Integer.parseInt(dataSourceIdStr.substring(4)) : null;
					Integer tgtDataSourceId = dataSourceIdStr.startsWith("tgt") ? Integer.parseInt(dataSourceIdStr.substring(4)) : null;

					cbxDataSourceField.setContainerDataSource(null);
					
					fillCbxDataSourceFields(this.cbxVwHorizontalRptKeyColDataType.getValue().toString(), cbxDataSourceField, expDataSourceId, fpyDataSourceId, fnyDataSourceId, ftyDataSourceId, tgtDataSourceId);
				}
			}			
		}	
	}
	
	private void fillCbxDataSourceFields(String dataFieldType, ComboBox cbxDataSourceFields, Integer expDataSourceId, Integer fpyDataSourceId, 
			Integer fnyDataSourceId, Integer ftyDataSourceId, Integer tgtDataSourceId)
	{		
		if(expDataSourceId != null)
		{
			Experiment selectedExperiment = new ExperimentDao().getExperimentById(expDataSourceId);
			List<ExperimentField> selectedExperimentFields = new ExperimentFieldDao().getAllExperimentFieldsByExperiment(selectedExperiment);
			
			for(int i=0; selectedExperimentFields!=null && i<selectedExperimentFields.size(); i++)
				addItemIfTypeMatches(dataFieldType, "expfield_" + selectedExperimentFields.get(i).getExpFieldId(), selectedExperimentFields.get(i).getExpFieldName(), selectedExperimentFields.get(i).getExpFieldType(), cbxDataSourceFields);
		
			if(vwHorizontalRptByExperimentDbIds.indexOf(expDataSourceId) != -1 && vwHorizontalRptByExperimentKeyColStrIds.size() > 0)
				cbxDataSourceFields.setValue(vwHorizontalRptByExperimentKeyColStrIds.get(vwHorizontalRptByExperimentDbIds.indexOf(expDataSourceId)));
		}
		
		if(fnyDataSourceId != null)
		{
			List<FinalPassYieldInfoField> fnyFields = new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldByReportById(fnyDataSourceId);
			for(int i=0; fnyFields != null && i<fnyFields.size(); i++)
				addItemIfTypeMatches(dataFieldType, "fnyfield_" + fnyFields.get(i).getFnyInfoFieldId(), fnyFields.get(i).getFnyInfoFieldLabel(), fnyFields.get(i).getExperimentField().getExpFieldType(), cbxDataSourceFields);
	
			addItemIfTypeMatches(dataFieldType, "fny_datetime", "Datetime (FNY)", "datetime", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fny_sn", "Serial Number (FNY)", "nvarchar(max)", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fny_result", "Result (FNY)", "nvarchar(max)", cbxDataSourceFields);
		
			if(vwHorizontalRptByFnyRptDbIds.indexOf(fnyDataSourceId) != -1 && vwHorizontalRptByFnyRptKeyColStrIds.size() > 0)
				cbxDataSourceFields.setValue(vwHorizontalRptByFnyRptKeyColStrIds.get(vwHorizontalRptByFnyRptDbIds.indexOf(fnyDataSourceId)));
		}
		
		if(fpyDataSourceId != null)
		{
			List<FirstPassYieldInfoField> fpyFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(fpyDataSourceId);
			for(int i=0; fpyFields != null && i<fpyFields.size(); i++)
				addItemIfTypeMatches(dataFieldType, "fpyfield_" + fpyFields.get(i).getFpyInfoFieldId(), fpyFields.get(i).getFpyInfoFieldLabel(), fpyFields.get(i).getExperimentField().getExpFieldType(), cbxDataSourceFields);
	
			addItemIfTypeMatches(dataFieldType, "fpy_datetime", "Datetime (FPY)", "datetime", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fpy_sn", "Serial Number (FPY)", "nvarchar(max)", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fpy_result", "Result (FPY)", "nvarchar(max)", cbxDataSourceFields);
	
			if(vwHorizontalRptByFpyRptDbIds.indexOf(fpyDataSourceId) != -1 && vwHorizontalRptByFpyRptKeyColStrIds.size() > 0)
				cbxDataSourceFields.setValue(vwHorizontalRptByFpyRptKeyColStrIds.get(vwHorizontalRptByFpyRptDbIds.indexOf(fpyDataSourceId)));
		}
		
		if(ftyDataSourceId != null)
		{
			List<FirstTimeYieldInfoField> ftyFields = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(ftyDataSourceId);
			for(int i=0; ftyFields != null && i<ftyFields.size(); i++)
				addItemIfTypeMatches(dataFieldType, "ftyfield_" + ftyFields.get(i).getFtyInfoFieldId(), ftyFields.get(i).getFtyInfoFieldLabel(), ftyFields.get(i).getExperimentField().getExpFieldType(), cbxDataSourceFields);
	
			addItemIfTypeMatches(dataFieldType, "fty_datetime", "Datetime (FTY)", "datetime", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fty_sn", "Serial Number (FTY)", "nvarchar(max)", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fty_result", "Result (FTY)", "nvarchar(max)", cbxDataSourceFields);
			
			if(vwHorizontalRptByFtyRptDbIds.indexOf(ftyDataSourceId) != -1 && vwHorizontalRptByFtyRptKeyColStrIds.size() > 0)
				cbxDataSourceFields.setValue(vwHorizontalRptByFtyRptKeyColStrIds.get(vwHorizontalRptByFtyRptDbIds.indexOf(ftyDataSourceId)));
		}
		
		if(tgtDataSourceId != null)
		{
			List<TargetColumn> targetColumns = new TargetColumnDao().getTargetColumnByTargetReportId(tgtDataSourceId);
			
			for(int i=0; targetColumns!=null && i<targetColumns.size(); i++)
				addItemIfTypeMatches(dataFieldType, "tgtfield_" + targetColumns.get(i).getTargetColumnId(), targetColumns.get(i).getTargetColumnLabel(), targetColumns.get(i).getExperimentField().getExpFieldType(), cbxDataSourceFields);
		
			if(vwHorizontalRptByTargetDbIds.indexOf(tgtDataSourceId) != -1 && vwHorizontalRptByTargetRptKeyColStrIds.size() > 0)
				cbxDataSourceFields.setValue(vwHorizontalRptByTargetRptKeyColStrIds.get(vwHorizontalRptByTargetDbIds.indexOf(ftyDataSourceId)));
		
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
	
	private void addReportColumn(Integer reportColumnId, ViewHorizontalReportColumn vwHorizontalRptCol)
	{
		VerticalLayout vwHorizontalColumnPnl = new VerticalLayout();
		vwHorizontalColumnPnl.setStyleName("well");
		vwHorizontalColumnPnl.setWidth(200, Unit.PIXELS);
		vwHorizontalColumnPnl.setSpacing(true);
		
		TextField txtColumnName = new TextField();
		txtColumnName.setCaption("Column Name");
		txtColumnName.setWidth(90, Unit.PERCENTAGE);
		txtColumnName.setRequired(true);
		txtColumnName.setStyleName("tiny");
		
		TextField txtColumnDbId = new TextField();
		txtColumnDbId.setCaption("Column Db Id");
		txtColumnDbId.setWidth(90, Unit.PERCENTAGE);
		txtColumnDbId.setRequired(true);
		txtColumnDbId.setStyleName("tiny");
		
		ComboBox cbxDataSource = new ComboBox();
		cbxDataSource.setCaption("Data Source");
		cbxDataSource.setWidth(90, Unit.PERCENTAGE);
		cbxDataSource.setRequired(true);
		cbxDataSource.setStyleName("tiny");
		
		ComboBox cbxDataSourceFieldColumn = new ComboBox();
		cbxDataSourceFieldColumn.setCaption("Data Source Field/Column");
		cbxDataSourceFieldColumn.setWidth(90, Unit.PERCENTAGE);
		cbxDataSourceFieldColumn.setRequired(true);
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
		
		Set<Item> selectedOptGrpPnlExperiments = (Set<Item>) optGrpPnlExperiments.getValue();
		for (Object selectedExperiment : selectedOptGrpPnlExperiments)
		{
			for(int i=0; activeExperiments != null && i<activeExperiments.size(); i++)
			{
				int selectedExperimentId = Integer.parseInt(selectedExperiment.toString());				
				if(activeExperiments.get(i).getExpId() == selectedExperimentId)
				{
					cbxDataSource.addItem("exp_" + selectedExperimentId);
					cbxDataSource.setItemCaption("exp_" + selectedExperimentId, "EXP : " + activeExperiments.get(i).getExpName());
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
					cbxDataSource.setItemCaption("fpy_" + selectedFpyReportId, "FPY : " + activeFpyReports.get(i).getFpyReportName());
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
					cbxDataSource.setItemCaption("fny_" + selectedFnyReportId, "FPY : " + activeFnyReports.get(i).getFnyReportName());
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
					cbxDataSource.setItemCaption("fty_" + selectedFtyReportId, "FTY : " + activeFtyReports.get(i).getFtyReportName());
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
					cbxDataSource.setItemCaption("tgt_" + selectedTargetReportId, "Target : " + activeTargetReports.get(i).getTargetReportName());
					break;
				}
			}
		}
		
		cbxDataSource.addValueChangeListener(new ValueChangeListener(){
	           @Override
				public void valueChange(ValueChangeEvent event) {
	        	   refreshDataSourceFields(cbxDataSource, cbxDataSourceFieldColumn);
	           }
	        });
		
		if(reportColumnId != null)
		{
			txtColumnName.setValue(vwHorizontalRptCol.getVwHorizontalRptColumnName());
			txtColumnDbId.setValue(vwHorizontalRptCol.getVwHorizontalRptColumnDbId());
			
			if(vwHorizontalRptCol.getExperiment() != null)
			{
				cbxDataSource.setValue("exp_" + vwHorizontalRptCol.getExperiment().getExpId());
				cbxDataSourceFieldColumn.setValue("expfield_" + vwHorizontalRptCol.getExpField().getExpFieldId());
			}

			if(vwHorizontalRptCol.getFpyRpt() != null)
			{
				cbxDataSource.setValue("fpy_" + vwHorizontalRptCol.getFpyRpt().getFpyReportId());
				
				if(vwHorizontalRptCol.getFpyInfoField() != null)
					cbxDataSourceFieldColumn.setValue("fpyfield_" + vwHorizontalRptCol.getFpyInfoField().getFpyInfoFieldId());
				else if(vwHorizontalRptCol.getVwHorizontalFpyIsDateTimeExpField() != null && vwHorizontalRptCol.getVwHorizontalFpyIsDateTimeExpField())
					cbxDataSourceFieldColumn.setValue("fpy_datetime");
				else if(vwHorizontalRptCol.getVwHorizontalFpyIsResultExpField() != null && vwHorizontalRptCol.getVwHorizontalFpyIsResultExpField())
					cbxDataSourceFieldColumn.setValue("fpy_result");
				else if(vwHorizontalRptCol.getVwHorizontalFpyIsSNExpField() != null && vwHorizontalRptCol.getVwHorizontalFpyIsSNExpField())
					cbxDataSourceFieldColumn.setValue("fpy_sn");				
			}
			
			if(vwHorizontalRptCol.getFtyRpt() != null)
			{
				cbxDataSource.setValue("fty_" + vwHorizontalRptCol.getFtyRpt().getFtyReportId());
				
				if(vwHorizontalRptCol.getFtyInfoField() != null)
					cbxDataSourceFieldColumn.setValue("ftyfield_" + vwHorizontalRptCol.getFtyInfoField().getFtyInfoFieldId());
				else if(vwHorizontalRptCol.getVwHorizontalFtyIsDateTimeExpField() != null && vwHorizontalRptCol.getVwHorizontalFtyIsDateTimeExpField())
					cbxDataSourceFieldColumn.setValue("fty_datetime");
				else if(vwHorizontalRptCol.getVwHorizontalFtyIsResultExpField() != null && vwHorizontalRptCol.getVwHorizontalFtyIsResultExpField())
					cbxDataSourceFieldColumn.setValue("fty_result");
				else if(vwHorizontalRptCol.getVwHorizontalFtyIsSNExpField() != null && vwHorizontalRptCol.getVwHorizontalFtyIsSNExpField())
					cbxDataSourceFieldColumn.setValue("fty_sn");
			}
			
			if(vwHorizontalRptCol.getFnyRpt() != null)
			{				
				cbxDataSource.setValue("fny_" + vwHorizontalRptCol.getFnyRpt().getFnyReportId());
				
				if(vwHorizontalRptCol.getFnyInfoField() != null)
					cbxDataSourceFieldColumn.setValue("fnyfield_" + vwHorizontalRptCol.getFnyInfoField().getFnyInfoFieldId());
				else if(vwHorizontalRptCol.getVwHorizontalFnyIsDateTimeExpField() != null && vwHorizontalRptCol.getVwHorizontalFnyIsDateTimeExpField())
					cbxDataSourceFieldColumn.setValue("fny_datetime");
				else if(vwHorizontalRptCol.getVwHorizontalFnyIsResultExpField() != null && vwHorizontalRptCol.getVwHorizontalFnyIsResultExpField())
					cbxDataSourceFieldColumn.setValue("fny_result");
				else if(vwHorizontalRptCol.getVwHorizontalFnyIsSNExpField() != null && vwHorizontalRptCol.getVwHorizontalFnyIsSNExpField())
					cbxDataSourceFieldColumn.setValue("fny_sn");
			}
			
			if(vwHorizontalRptCol.getTargetRpt() != null)
			{
				cbxDataSource.setValue("tgt_" + vwHorizontalRptCol.getTargetRpt().getTargetReportId());
				cbxDataSourceFieldColumn.setValue("tgtfield_" + vwHorizontalRptCol.getTargetColumn().getTargetColumnId());				
			}
			
			//cbxDataSource
			//cbxDataSourceFieldColumn
		}
		
		this.lytVwHorizontalRptColumnsPnls.addComponent(vwHorizontalColumnPnl);
		txtColumnName.focus();
		
		btnRemoveColumn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				//addDataSourceFilter(null,null,null,null,null,null,null);
				//vwHorizontalColumnPnl.
				lytVwHorizontalRptColumnsPnls.removeComponent(vwHorizontalColumnPnl);
			}

		});
	}
	
	private void refreshDataSourceFields(ComboBox cbxDataSource, ComboBox cbxDataSourceFieldColumn)
	{
		cbxDataSourceFieldColumn.setContainerDataSource(null);
		if(cbxDataSource.getValue() == null)
			return;
		
		Integer dataSourceId = Integer.parseInt(cbxDataSource.getValue().toString().substring(4));
		
		if(cbxDataSource.getValue() != null && cbxDataSource.getValue().toString().startsWith("exp"))
		{
			Experiment selectedExperiment = new ExperimentDao().getExperimentById(dataSourceId);
			List<ExperimentField> selectedExperimentFields = new ExperimentFieldDao().getAllExperimentFieldsByExperiment(selectedExperiment);
			
			for(int i=0; selectedExperimentFields!=null && i<selectedExperimentFields.size(); i++)
			{
				cbxDataSourceFieldColumn.addItem( "expfield_" + selectedExperimentFields.get(i).getExpFieldId());
				cbxDataSourceFieldColumn.setItemCaption( "expfield_" + selectedExperimentFields.get(i).getExpFieldId(), selectedExperimentFields.get(i).getExpFieldName());
			}
		}
		
		if(cbxDataSource.getValue() != null && cbxDataSource.getValue().toString().startsWith("fny"))
		{
			List<FinalPassYieldInfoField> fnyFields = new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldByReportById(dataSourceId);
			for(int i=0; fnyFields != null && i<fnyFields.size(); i++)
			{
				cbxDataSourceFieldColumn.addItem("fnyfield_" + fnyFields.get(i).getFnyInfoFieldId());
				cbxDataSourceFieldColumn.setItemCaption("fnyfield_" + fnyFields.get(i).getFnyInfoFieldId(), fnyFields.get(i).getFnyInfoFieldLabel());
			}

			cbxDataSourceFieldColumn.addItem("fny_datetime");
			cbxDataSourceFieldColumn.setItemCaption("fny_datetime", "Datetime (FNY)");
			cbxDataSourceFieldColumn.addItem("fny_sn");
			cbxDataSourceFieldColumn.setItemCaption("fny_sn", "Serial Number (FNY)");
			cbxDataSourceFieldColumn.addItem("fny_result");
			cbxDataSourceFieldColumn.setItemCaption("fny_result", "Result (FNY)");
		}
		
		if(cbxDataSource.getValue() != null && cbxDataSource.getValue().toString().startsWith("fpy"))
		{
			List<FirstPassYieldInfoField> fpyFields = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(dataSourceId);
			for(int i=0; fpyFields != null && i<fpyFields.size(); i++)
			{
				cbxDataSourceFieldColumn.addItem("fpyfield_" + fpyFields.get(i).getFpyInfoFieldId());
				cbxDataSourceFieldColumn.setItemCaption("fpyfield_" + fpyFields.get(i).getFpyInfoFieldId(), fpyFields.get(i).getFpyInfoFieldLabel());
			}

			cbxDataSourceFieldColumn.addItem("fpy_datetime");
			cbxDataSourceFieldColumn.setItemCaption("fpy_datetime", "Datetime (FPY)");
			cbxDataSourceFieldColumn.addItem("fpy_sn");
			cbxDataSourceFieldColumn.setItemCaption("fpy_sn", "Serial Number (FPY)");
			cbxDataSourceFieldColumn.addItem("fpy_result");
			cbxDataSourceFieldColumn.setItemCaption("fpy_result", "Result (FPY)");
		}
		
		if(cbxDataSource.getValue() != null && cbxDataSource.getValue().toString().startsWith("fty"))
		{
			List<FirstTimeYieldInfoField> ftyFields = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(dataSourceId);
			for(int i=0; ftyFields != null && i<ftyFields.size(); i++)
			{
				cbxDataSourceFieldColumn.addItem("ftyfield_" + ftyFields.get(i).getFtyInfoFieldId());
				cbxDataSourceFieldColumn.setItemCaption("ftyfield_" + ftyFields.get(i).getFtyInfoFieldId(), ftyFields.get(i).getFtyInfoFieldLabel());
			}

			cbxDataSourceFieldColumn.addItem("fty_datetime");
			cbxDataSourceFieldColumn.setItemCaption("fty_datetime", "Datetime (FTY)");
			cbxDataSourceFieldColumn.addItem("fty_sn");
			cbxDataSourceFieldColumn.setItemCaption("fty_sn", "Serial Number (FTY)");
			cbxDataSourceFieldColumn.addItem("fty_result");
			cbxDataSourceFieldColumn.setItemCaption("fty_result", "Result (FTY)");
		}
		
		if(cbxDataSource.getValue() != null && cbxDataSource.getValue().toString().startsWith("tgt"))
		{
			List<TargetColumn> targetColumns = new TargetColumnDao().getTargetColumnByTargetReportId(dataSourceId);
			
			for(int i=0; targetColumns!=null && i<targetColumns.size(); i++)
			{
				cbxDataSourceFieldColumn.addItem("tgtfield_" + targetColumns.get(i).getTargetColumnId());
				cbxDataSourceFieldColumn.setItemCaption("tgtfield_" + targetColumns.get(i).getTargetColumnId(), targetColumns.get(i).getTargetColumnLabel());
			}
		}				
	}
	
	private void onSave()
	{
		boolean dataSourcesSelected = false;
		boolean rptColumnsAdded = false;
		boolean isKeySelected = false;
		int totalKeysSelected = 0;
		
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateNotDuplicatedReportColumnsResult = validateNotDuplicatedReportColumns();
		
		if(validateRequiredFieldsResult && validateNotDuplicatedReportColumnsResult)
		{
			Set<Item> selectedOptGrpPnlExperimentsToValidate = (Set<Item>) optGrpPnlExperiments.getValue();
			for (Object selectedExperiment : selectedOptGrpPnlExperimentsToValidate)
			{
				for(int i=0; activeExperiments != null && i<activeExperiments.size(); i++)
				{
					int selectedExperimentId = Integer.parseInt(selectedExperiment.toString());				
					if(activeExperiments.get(i).getExpId() == selectedExperimentId)
					{
						dataSourcesSelected = true;
						break;
					}
				}
			}
			
			Set<Item> selectedOptGrpPnlFpyRptsToValidate = (Set<Item>) optGrpPnlFpyReports.getValue();
			for (Object selectedFpyRpt : selectedOptGrpPnlFpyRptsToValidate)
			{
				for(int i=0; activeFpyReports != null && i<activeFpyReports.size(); i++)
				{
					int selectedFpyReportId = Integer.parseInt(selectedFpyRpt.toString());
					if(activeFpyReports.get(i).getFpyReportId() == selectedFpyReportId)
					{
						dataSourcesSelected = true;
						break;
					}
				}
			}
			
			Set<Item> selectedOptGrpPnlFnyRptsToValidate = (Set<Item>) optGrpPnlFnyReports.getValue();
			for (Object selectedFnyRpt : selectedOptGrpPnlFnyRptsToValidate)
			{
				for(int i=0; activeFnyReports != null && i<activeFnyReports.size(); i++)
				{
					int selectedFnyReportId = Integer.parseInt(selectedFnyRpt.toString());
					if(activeFnyReports.get(i).getFnyReportId() == selectedFnyReportId)
					{
						dataSourcesSelected = true;
						break;
					}
				}
			}
			
			Set<Item> selectedOptGrpPnlFtyRptsToValidate = (Set<Item>) optGrpPnlFtyReports.getValue();
			for (Object selectedFtyRpt : selectedOptGrpPnlFtyRptsToValidate)
			{
				for(int i=0; activeFtyReports != null && i<activeFtyReports.size(); i++)
				{
					int selectedFtyReportId = Integer.parseInt(selectedFtyRpt.toString());
					if(activeFtyReports.get(i).getFtyReportId() == selectedFtyReportId)
					{
						dataSourcesSelected = true;
						break;
					}
				}
			}
			
			Set<Item> selectedOptGrpPnlTargetRptsToValidate = (Set<Item>) optGrpPnlTargetReports.getValue();
			for (Object selectedTargetRpt : selectedOptGrpPnlTargetRptsToValidate)
			{
				for(int i=0; activeTargetReports != null && i<activeTargetReports.size(); i++)
				{
					int selectedTargetReportId = Integer.parseInt(selectedTargetRpt.toString());
					if(activeTargetReports.get(i).getTargetReportId() == selectedTargetReportId)
					{
						dataSourcesSelected = true;
						break;
					}
				}
			}
			

			if(!dataSourcesSelected)
			{
				this.getUI().showNotification("At least 1 data source must be selected.", Type.WARNING_MESSAGE);
				return;
			}
			
			ViewHorizontalReport vwHorizontalRpt = new ViewHorizontalReport();
			
			if(isNewRecord)
			{
				vwHorizontalRpt.setVwHorizontalRptDbTableNameId("vwhorizontal#" + this.txtVwHorizontalRptCustomId.getValue());
				vwHorizontalRpt.setVwHorizontalRptName(this.txtVwHorizontalRptName.getValue());
				vwHorizontalRpt.setVwHorizontalRptDescription(this.txtVwHorizontalDescription.getValue());
				vwHorizontalRpt.setVwHorizontalRptIsActive(true);
				vwHorizontalRpt.setVwHorizontalRptKeyColumnName(this.txtVwHorizontalRptKeyColName.getValue());
				vwHorizontalRpt.setVwHorizontalRptKeyColumnDbId(this.txtVwHorizontalRptKeyColCustomId.getValue());
				vwHorizontalRpt.setVwHorizontalRptKeyColumnDataType(this.cbxVwHorizontalRptKeyColDataType.getValue().toString());
				new ViewHorizontalReportDao().addVwHorizontalReport(vwHorizontalRpt);
			}
			else
			{				
				this.savedVwHorizontalRpt.setVwHorizontalRptDbTableNameId("vwhorizontal#" + this.txtVwHorizontalRptCustomId.getValue());
				this.savedVwHorizontalRpt.setVwHorizontalRptName(this.txtVwHorizontalRptName.getValue());
				this.savedVwHorizontalRpt.setVwHorizontalRptDescription(this.txtVwHorizontalDescription.getValue());
				this.savedVwHorizontalRpt.setVwHorizontalRptIsActive(true);		
				this.savedVwHorizontalRpt.setVwHorizontalRptKeyColumnName(this.txtVwHorizontalRptKeyColName.getValue());
				this.savedVwHorizontalRpt.setVwHorizontalRptKeyColumnDbId(this.txtVwHorizontalRptKeyColCustomId.getValue());
				this.savedVwHorizontalRpt.setVwHorizontalRptKeyColumnDataType(this.cbxVwHorizontalRptKeyColDataType.getValue().toString());	
				new ViewHorizontalReportDao().updateVwHorizontalReport(this.savedVwHorizontalRpt);
				vwHorizontalRpt = new ViewHorizontalReportDao().getVwHorizontalRptById(this.savedVwHorizontalRpt.getVwHorizontalRptId());
			}
			
			
			Set<Item> selectedOptGrpPnlExperiments = (Set<Item>) optGrpPnlExperiments.getValue();
			for (Object selectedExperiment : selectedOptGrpPnlExperiments)
			{
				for(int i=0; activeExperiments != null && i<activeExperiments.size(); i++)
				{
					int selectedExperimentId = Integer.parseInt(selectedExperiment.toString());				
					if(activeExperiments.get(i).getExpId() == selectedExperimentId)
					{					
						ViewHorizontalReportByExperiment vwHorizontalRptByExperiment = new ViewHorizontalReportByExperiment();
						vwHorizontalRptByExperiment.setExperiment(activeExperiments.get(i));
						vwHorizontalRptByExperiment.setViewHorizontalReport(vwHorizontalRpt);
						
						//Add Key column for Experiment
						Collection rptKeyColumnsMapItemIds = this.tblKeyColumnMapping.getContainerDataSource().getItemIds();
						
						for (Object rptKeyColumnMapItemIdObj : rptKeyColumnsMapItemIds) 
						{
							Item tblKeyColumnMapRowItem = this.tblKeyColumnMapping.getContainerDataSource().getItem(rptKeyColumnMapItemIdObj);
							String dataSourceId = ((ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Table/Report").getValue())).getValue().toString();
							String dataSourceFieldIdStr = ((ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Field").getValue())).getValue().toString();
							
							if(dataSourceId.startsWith("exp") && Integer.parseInt(dataSourceId.substring(4)) == selectedExperimentId && dataSourceFieldIdStr.startsWith("expfield_"))
							{
								Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldIdStr.substring(9));
								vwHorizontalRptByExperiment.setExpKeyField(new ExperimentFieldDao().getExperimentFieldById(dataSourceFieldId));
							}
						}
						
						new ViewHorizontalReportByExperimentDao().addVwHorizontalReportByExperiment(vwHorizontalRptByExperiment);
						
						
						//Save filters attached to Experiment
						Collection dataSourceFiltersItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
						
						for (Object dataSourceFiltersItemIdObj : dataSourceFiltersItemIds) 
						{
							Item tblDataSourceFilterRowItem = this.tblReportFilters.getContainerDataSource().getItem(dataSourceFiltersItemIdObj);
							String dataSourceId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Data Source").getValue())).getValue().toString();
							if(("exp_" + selectedExperimentId).equals(dataSourceId))
							{
								ViewHorizontalReportFilterByExpField vwHorizontalRptFilterByExpField = new ViewHorizontalReportFilterByExpField();
								vwHorizontalRptFilterByExpField.setVwHorizontalReportByExperiment(vwHorizontalRptByExperiment);
								vwHorizontalRptFilterByExpField.setExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString().substring(4))));
								
								String filterOperator = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
								GridLayout gridFilterValues = (GridLayout)tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue();
								
								if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
									vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldValue1(txtFilterValue1.getValue().toString());
									vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldValue2(txtFilterValue2.getValue().toString());
								}
								else if(filterOperator.equals("customrange"))
								{
									DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
									DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
									vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldValue1(new SimpleDateFormat("yyyyMMdd").format(fromDateField.getValue()));
									vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldValue2(new SimpleDateFormat("yyyyMMdd").format(toDateField.getValue()));
								}
								else if(filterOperator.equals("after") || filterOperator.equals("before")
					    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
					    				|| filterOperator.equals("onorbefore"))
								{
									DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
									vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldValue1(new SimpleDateFormat("yyyyMMdd").format(dtFilterValue.getValue()));									
								}
								else
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldValue1(txtFilterValue1.getValue());
								
									if(txtFilterValue1.getValue() == null || txtFilterValue1.getValue() == "")
										vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
								}
								
								if(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue() != null)
									vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
								else
									vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldExpression("and");
									
								vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
							
								
								new ViewHorizontalReportFilterByExpFieldDao().addVwHorizontalReportFilterByExpField(vwHorizontalRptFilterByExpField);
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
						ViewHorizontalReportByFpyRpt vwHorizontalRptByFpyRpt = new ViewHorizontalReportByFpyRpt();
						vwHorizontalRptByFpyRpt.setFpyRpt(activeFpyReports.get(i));
						vwHorizontalRptByFpyRpt.setViewHorizontalReport(vwHorizontalRpt);

						//Add Key column for FPY
						Collection rptKeyColumnsMapItemIds = this.tblKeyColumnMapping.getContainerDataSource().getItemIds();
						
						for (Object rptKeyColumnMapItemIdObj : rptKeyColumnsMapItemIds) 
						{
							Item tblKeyColumnMapRowItem = this.tblKeyColumnMapping.getContainerDataSource().getItem(rptKeyColumnMapItemIdObj);
							String dataSourceId = ((ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Table/Report").getValue())).getValue().toString();
							String dataSourceFieldIdStr = ((ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Field").getValue())).getValue().toString();
							
							if(dataSourceId.startsWith("fpy") && Integer.parseInt(dataSourceId.substring(4)) == selectedFpyReportId && dataSourceFieldIdStr.startsWith("fpy"))
							{
								if(dataSourceFieldIdStr.equals("fpy_datetime") || dataSourceFieldIdStr.equals("fpy_sn") || dataSourceFieldIdStr.equals("fpy_result"))
								{
									vwHorizontalRptByFpyRpt.setVwHorizontalFpyKeyIsDateTimeExpField(dataSourceFieldIdStr.equals("fty_datetime"));
									vwHorizontalRptByFpyRpt.setVwHorizontalFpyKeyIsResultExpField(dataSourceFieldIdStr.equals("fty_result"));
									vwHorizontalRptByFpyRpt.setVwHorizontalFpyKeyIsSNExpField(dataSourceFieldIdStr.equals("fty_sn"));
								}
								else
								{	
									Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldIdStr.substring(9));
									vwHorizontalRptByFpyRpt.setFpyKeyInfoField(new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldById(dataSourceFieldId));
								}
							}
						}

						new ViewHorizontalReportByFpyRptDao().addVwHorizontalReportByFpyRpt(vwHorizontalRptByFpyRpt);
						
						//Save filters attached to FPY
						Collection dataSourceFiltersItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
						
						for (Object dataSourceFiltersItemIdObj : dataSourceFiltersItemIds) 
						{
							Item tblDataSourceFilterRowItem = this.tblReportFilters.getContainerDataSource().getItem(dataSourceFiltersItemIdObj);
							String dataSourceId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Data Source").getValue())).getValue().toString();
							if(("fpy_" + selectedFpyReportId).equals(dataSourceId))
							{
								ViewHorizontalReportFilterByFpyField vwHorizontalRptFilterByFpyField = new ViewHorizontalReportFilterByFpyField();
								vwHorizontalRptFilterByFpyField.setVwHorizontalFpyRpt(vwHorizontalRptByFpyRpt);
							
								String fpyInfoFieldStrId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString();
								if(fpyInfoFieldStrId.equals("fpy_datetime") ||fpyInfoFieldStrId.equals("fpy_sn") ||fpyInfoFieldStrId.equals("fpy_result"))
								{
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyIsDateTimeExpField(fpyInfoFieldStrId.equals("fpy_datetime"));
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyIsResultExpField(fpyInfoFieldStrId.equals("fpy_result"));
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpySNExpField(fpyInfoFieldStrId.equals("fpy_sn"));
								}
								else
									vwHorizontalRptFilterByFpyField.setFpyInfoField(new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldById(Integer.parseInt(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString().substring(4))));
							
								String filterOperator = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
								GridLayout gridFilterValues = (GridLayout)tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue();
								
								if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyFieldValue1(txtFilterValue1.getValue().toString());
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyFieldValue2(txtFilterValue2.getValue().toString());
								}
								else if(filterOperator.equals("customrange"))
								{
									DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
									DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyFieldValue1(new SimpleDateFormat("yyyyMMdd").format(fromDateField.getValue()));
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyFieldValue2(new SimpleDateFormat("yyyyMMdd").format(toDateField.getValue()));
									
								}
								else if(filterOperator.equals("after") || filterOperator.equals("before")
					    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
					    				|| filterOperator.equals("onorbefore"))
								{
									DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyFieldValue1(new SimpleDateFormat("yyyyMMdd").format(dtFilterValue.getValue()));									
								}
								else
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyFieldValue1(txtFilterValue1.getValue());

									if(txtFilterValue1.getValue() == null || txtFilterValue1.getValue() == "")
										vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
								
								}
								
								if(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue() != null)
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
								else
									vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyFieldExpression("and");
								
								vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilterByFpyFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
							
								//if(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == null || ((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == "")
								//	vwHorizontalRptFilterByFpyField.setVwHorizontalRptFilteByFpyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
							
								new ViewHorizontalReportFilterByFpyFieldDao().addVwHorizontalReportFilterByFpyField(vwHorizontalRptFilterByFpyField);
							}
						}				
						
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
						ViewHorizontalReportByFnyRpt vwHorizontalRptByFnyRpt = new ViewHorizontalReportByFnyRpt();
						vwHorizontalRptByFnyRpt.setFnyRpt(activeFnyReports.get(i));
						vwHorizontalRptByFnyRpt.setViewHorizontalReport(vwHorizontalRpt);

						//Add Key column for FNY
						Collection rptKeyColumnsMapItemIds = this.tblKeyColumnMapping.getContainerDataSource().getItemIds();
						
						for (Object rptKeyColumnMapItemIdObj : rptKeyColumnsMapItemIds) 
						{
							Item tblKeyColumnMapRowItem = this.tblKeyColumnMapping.getContainerDataSource().getItem(rptKeyColumnMapItemIdObj);
							String dataSourceId = ((ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Table/Report").getValue())).getValue().toString();
							String dataSourceFieldIdStr = ((ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Field").getValue())).getValue().toString();
							
							if(dataSourceId.startsWith("fny") && Integer.parseInt(dataSourceId.substring(4)) == selectedFnyReportId && dataSourceFieldIdStr.startsWith("fny"))
							{
								if(dataSourceFieldIdStr.equals("fny_datetime") || dataSourceFieldIdStr.equals("fny_sn") || dataSourceFieldIdStr.equals("fny_result"))
								{
									vwHorizontalRptByFnyRpt.setVwHorizontalFnyKeyIsDateTimeExpField(dataSourceFieldIdStr.equals("fny_datetime"));
									vwHorizontalRptByFnyRpt.setVwHorizontalFnyKeyIsResultExpField(dataSourceFieldIdStr.equals("fny_result"));
									vwHorizontalRptByFnyRpt.setVwHorizontalFnyKeyIsSNExpField(dataSourceFieldIdStr.equals("fny_sn"));
								}
								else
								{	
									Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldIdStr.substring(9));
									vwHorizontalRptByFnyRpt.setFnyKeyInfoField(new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldById(dataSourceFieldId));
								}
							}
						}

						new ViewHorizontalReportByFnyRptDao().addVwHorizontalReportByFnyRpt(vwHorizontalRptByFnyRpt);
						
						//Save filters attached to FNY
						Collection dataSourceFiltersItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
						
						for (Object dataSourceFiltersItemIdObj : dataSourceFiltersItemIds) 
						{
							Item tblDataSourceFilterRowItem = this.tblReportFilters.getContainerDataSource().getItem(dataSourceFiltersItemIdObj);
							String dataSourceId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Data Source").getValue())).getValue().toString();
							if(("fny_" + selectedFnyReportId).equals(dataSourceId))
							{
								ViewHorizontalReportFilterByFnyField vwHorizontalRptFilterByFnyField = new ViewHorizontalReportFilterByFnyField();
								vwHorizontalRptFilterByFnyField.setVwHorizontalFnyRpt(vwHorizontalRptByFnyRpt);
							
								String fnyInfoFieldStrId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString();
								if(fnyInfoFieldStrId.equals("fny_datetime") ||fnyInfoFieldStrId.equals("fny_sn") ||fnyInfoFieldStrId.equals("fny_result"))
								{
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyIsDateTimeExpField(fnyInfoFieldStrId.equals("fny_datetime"));
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyIsResultExpField(fnyInfoFieldStrId.equals("fny_result"));
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnySNExpField(fnyInfoFieldStrId.equals("fny_sn"));
								}
								else
									vwHorizontalRptFilterByFnyField.setFnyInfoField(new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldById(Integer.parseInt(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString().substring(4))));
							
								String filterOperator = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
								GridLayout gridFilterValues = (GridLayout)tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue();
								
								if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldValue1(txtFilterValue1.getValue().toString());
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldValue2(txtFilterValue2.getValue().toString());
								}
								else if(filterOperator.equals("customrange"))
								{
									DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
									DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldValue1(new SimpleDateFormat("yyyyMMdd").format(fromDateField.getValue()));
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldValue2(new SimpleDateFormat("yyyyMMdd").format(toDateField.getValue()));
									
								}
								else if(filterOperator.equals("after") || filterOperator.equals("before")
					    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
					    				|| filterOperator.equals("onorbefore"))
								{
									DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldValue1(dtFilterValue.getValue().toString());									
								}
								else
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldValue1(txtFilterValue1.getValue());
								
									if(txtFilterValue1.getValue() == null || txtFilterValue1.getValue() == "")
										vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
								}
								
								
								//vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilteByFnyFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
								//vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
								if(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue() != null)
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
								else
									vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldExpression("and");
								
								vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilterByFnyFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
							
								//if(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == null || ((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == "")
								//	vwHorizontalRptFilterByFnyField.setVwHorizontalRptFilteByFnyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
							
								new ViewHorizontalReportFilterByFnyFieldDao().addVwHorizontalReportFilterByFnyField(vwHorizontalRptFilterByFnyField);
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
						ViewHorizontalReportByFtyRpt vwHorizontalRptByFtyRpt = new ViewHorizontalReportByFtyRpt();
						vwHorizontalRptByFtyRpt.setFtyRpt(activeFtyReports.get(i));
						vwHorizontalRptByFtyRpt.setViewHorizontalReport(vwHorizontalRpt);
						
						//Add Key column for FTY
						Collection rptKeyColumnsMapItemIds = this.tblKeyColumnMapping.getContainerDataSource().getItemIds();
						
						for (Object rptKeyColumnMapItemIdObj : rptKeyColumnsMapItemIds) 
						{
							Item tblKeyColumnMapRowItem = this.tblKeyColumnMapping.getContainerDataSource().getItem(rptKeyColumnMapItemIdObj);
							String dataSourceId = ((ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Table/Report").getValue())).getValue().toString();
							String dataSourceFieldIdStr = ((ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Field").getValue())).getValue().toString();
							
							if(dataSourceId.startsWith("fty") && Integer.parseInt(dataSourceId.substring(4)) == selectedFtyReportId && dataSourceFieldIdStr.startsWith("fty"))
							{
								if(dataSourceFieldIdStr.equals("fty_datetime") || dataSourceFieldIdStr.equals("fty_sn") || dataSourceFieldIdStr.equals("fty_result"))
								{
									vwHorizontalRptByFtyRpt.setVwHorizontalFtyKeyIsDateTimeExpField(dataSourceFieldIdStr.equals("fty_datetime"));
									vwHorizontalRptByFtyRpt.setVwHorizontalFtyKeyIsResultExpField(dataSourceFieldIdStr.equals("fty_result"));
									vwHorizontalRptByFtyRpt.setVwHorizontalFtyKeyIsSNExpField(dataSourceFieldIdStr.equals("fty_sn"));
								}
								else
								{	
									Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldIdStr.substring(9));
									vwHorizontalRptByFtyRpt.setFtyKeyInfoField(new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldById(dataSourceFieldId));
								}
							}
						}

						new ViewHorizontalReportByFtyRptDao().addVwHorizontalReportByFtyRpt(vwHorizontalRptByFtyRpt);
						
						//Save filters attached to FTY
						Collection dataSourceFiltersItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
						
						for (Object dataSourceFiltersItemIdObj : dataSourceFiltersItemIds) 
						{
							Item tblDataSourceFilterRowItem = this.tblReportFilters.getContainerDataSource().getItem(dataSourceFiltersItemIdObj);
							String dataSourceId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Data Source").getValue())).getValue().toString();
							if(("fty_" + selectedFtyReportId).equals(dataSourceId))
							{
								ViewHorizontalReportFilterByFtyField vwHorizontalRptFilterByFtyField = new ViewHorizontalReportFilterByFtyField();
								vwHorizontalRptFilterByFtyField.setVwHorizontalFtyRpt(vwHorizontalRptByFtyRpt);
							
								String ftyInfoFieldStrId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString();
								if(ftyInfoFieldStrId.equals("fty_datetime") || ftyInfoFieldStrId.equals("fty_sn") ||ftyInfoFieldStrId.equals("fty_result"))
								{
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyIsDateTimeExpField(ftyInfoFieldStrId.equals("fty_datetime"));
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyIsResultExpField(ftyInfoFieldStrId.equals("fty_result"));
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtySNExpField(ftyInfoFieldStrId.equals("fty_sn"));
								}
								else
									vwHorizontalRptFilterByFtyField.setFtyInfoField(new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldById(Integer.parseInt(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString().substring(4))));
							
								String filterOperator = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
								GridLayout gridFilterValues = (GridLayout)tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue();
								
								if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldValue1(txtFilterValue1.getValue().toString());
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldValue2(txtFilterValue2.getValue().toString());
								}
								else if(filterOperator.equals("customrange"))
								{
									DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
									DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldValue1(new SimpleDateFormat("yyyyMMdd").format(fromDateField.getValue()));
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldValue2(new SimpleDateFormat("yyyyMMdd").format(toDateField.getValue()));
									
								}
								else if(filterOperator.equals("after") || filterOperator.equals("before")
					    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
					    				|| filterOperator.equals("onorbefore"))
								{
									DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldValue1(new SimpleDateFormat("yyyyMMdd").format(dtFilterValue.getValue()));									
								}
								else
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldValue1(txtFilterValue1.getValue());
								
									if(txtFilterValue1.getValue() == null || txtFilterValue1.getValue() == "")
										vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
								}
								
								//vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilteByFtyFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
								//vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
								
								if(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue() != null)
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
								else
									vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldExpression("and");
								
	
								vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilterByFtyFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
							
								//if(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == null || ((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == "")
								//	vwHorizontalRptFilterByFtyField.setVwHorizontalRptFilteByFtyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
							
								new ViewHorizontalReportFilterByFtyFieldDao().addVwHorizontalReportFilterByFtyField(vwHorizontalRptFilterByFtyField);
							}
						}
						
						break;
					}
				}
			}	
						
			
			//Filters for Target Report are pending
			Set<Item> selectedOptGrpPnlTargetRpts = (Set<Item>) optGrpPnlTargetReports.getValue();
			for (Object selectedTargetRpt : selectedOptGrpPnlTargetRpts)
			{
				for(int i=0; activeTargetReports != null && i<activeTargetReports.size(); i++)
				{
					int selectedTargetReportId = Integer.parseInt(selectedTargetRpt.toString());
					if(activeTargetReports.get(i).getTargetReportId() == selectedTargetReportId)
					{
						ViewHorizontalReportByTargetRpt vwHorizontalRptByTargetRpt = new ViewHorizontalReportByTargetRpt();
						vwHorizontalRptByTargetRpt.setTargetRpt(activeTargetReports.get(i));
						vwHorizontalRptByTargetRpt.setViewHorizontalReport(vwHorizontalRpt);
						
						//Add Key column for Target
						Collection rptKeyColumnsMapItemIds = this.tblKeyColumnMapping.getContainerDataSource().getItemIds();
						
						for (Object rptKeyColumnMapItemIdObj : rptKeyColumnsMapItemIds) 
						{
							Item tblKeyColumnMapRowItem = this.tblKeyColumnMapping.getContainerDataSource().getItem(rptKeyColumnMapItemIdObj);
							String dataSourceId = ((ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Table/Report").getValue())).getValue().toString();
							String dataSourceFieldIdStr = ((ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Field").getValue())).getValue().toString();
							
							if(dataSourceId.startsWith("tgt") && Integer.parseInt(dataSourceId.substring(4)) == selectedTargetReportId && dataSourceFieldIdStr.startsWith("tgtfield_"))
							{
								Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldIdStr.substring(9));
								vwHorizontalRptByTargetRpt.setTargetKeyColumn(new TargetColumnDao().getTargetColumnById(dataSourceFieldId));
							}
						}
						
						new ViewHorizontalReportByTargetRptDao().addVwHorizontalReportByTargetRpt(vwHorizontalRptByTargetRpt);
						
						//Save filters attached to Target
						Collection dataSourceFiltersItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
						
						for (Object dataSourceFiltersItemIdObj : dataSourceFiltersItemIds) 
						{
							Item tblDataSourceFilterRowItem = this.tblReportFilters.getContainerDataSource().getItem(dataSourceFiltersItemIdObj);
							String dataSourceId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Data Source").getValue())).getValue().toString();
							if(("tgt_" + selectedTargetReportId).equals(dataSourceId))
							{
								ViewHorizontalReportFilterByTargetColumn vwHorizontalRptFilterByTgtCol = new ViewHorizontalReportFilterByTargetColumn();
								vwHorizontalRptFilterByTgtCol.setVwHorizontalTargetRpt(vwHorizontalRptByTargetRpt);
								vwHorizontalRptFilterByTgtCol.setTargetColumn(new TargetColumnDao().getTargetColumnById(Integer.parseInt(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString().substring(4))));
								
								String filterOperator = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
								GridLayout gridFilterValues = (GridLayout)tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue();
								
								if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
									vwHorizontalRptFilterByTgtCol.setVwHorizontalRptFilterByTargetColumnValue1(txtFilterValue1.getValue().toString());
									vwHorizontalRptFilterByTgtCol.setVwHorizontalRptFilterByTargetColumnValue2(txtFilterValue2.getValue().toString());
								}
								else if(filterOperator.equals("customrange"))
								{
									DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
									DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
									vwHorizontalRptFilterByTgtCol.setVwHorizontalRptFilterByTargetColumnValue1(new SimpleDateFormat("yyyyMMdd").format(fromDateField.getValue()));
									vwHorizontalRptFilterByTgtCol.setVwHorizontalRptFilterByTargetColumnValue2(new SimpleDateFormat("yyyyMMdd").format(toDateField.getValue()));
								}
								else if(filterOperator.equals("after") || filterOperator.equals("before")
					    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
					    				|| filterOperator.equals("onorbefore"))
								{
									DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
									vwHorizontalRptFilterByTgtCol.setVwHorizontalRptFilterByTargetColumnValue1(new SimpleDateFormat("yyyyMMdd").format(dtFilterValue.getValue()));									
								}
								else
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									vwHorizontalRptFilterByTgtCol.setVwHorizontalRptFilterByTargetColumnValue1(txtFilterValue1.getValue());
								
									if(txtFilterValue1.getValue() == null || txtFilterValue1.getValue() == "")
										vwHorizontalRptFilterByTgtCol.setVwHorizontalRptFilterByTargetColumnValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
								}
								
								//vwHorizontalRptFilterByExpField.setVwHorizontalRptFilteByExpFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
								//vwHorizontalRptFilterByExpField.setVwHorizontalRptFilterByExpFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
								
								if(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue() != null)
									vwHorizontalRptFilterByTgtCol.setVwHorizontalRptFilterByTargetColumnExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
								else
									vwHorizontalRptFilterByTgtCol.setVwHorizontalRptFilterByTargetColumnExpression("and");
									
								vwHorizontalRptFilterByTgtCol.setVwHorizontalRptFilterByTargetColumnOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
							
								
								new ViewHorizontalReportFilterByTargetColumnDao().addVwHorizontalReportFilterByTargetColumn(vwHorizontalRptFilterByTgtCol);
							}
						}
						
						
						break;
					}
				}
			}
				
			//Load report columns into Matrix to be reused in Enrichment
			List<String> vwEnrichmentRptColumnId = new ArrayList<String>(); 
			List<ViewHorizontalReportColumn> vwEnrichmentRptColumn = new ArrayList<ViewHorizontalReportColumn>(); 
			

			for(int i=0; i<lytVwHorizontalRptColumnsPnls.getComponentCount(); i++)
			{
				VerticalLayout pnlRptColumn = (VerticalLayout)lytVwHorizontalRptColumnsPnls.getComponent(i);

				TextField txtVwHorizontalRptColumnName = (TextField)pnlRptColumn.getComponent(0);
				TextField txtVwHorizontalRptColumnDbId = (TextField)pnlRptColumn.getComponent(1);
				ComboBox cbxVwHorizontalRptColDataSource = (ComboBox)pnlRptColumn.getComponent(2);
				ComboBox cbxVwHorizontalRptColDataSourceField = (ComboBox)pnlRptColumn.getComponent(3);
				
				String dataSourceStrId = cbxVwHorizontalRptColDataSource.getValue().toString();
				String dataSourceFieldStrId = cbxVwHorizontalRptColDataSourceField.getValue().toString();
				
				ViewHorizontalReportColumn vwHorizontalRptColumn = new ViewHorizontalReportColumn();
				vwHorizontalRptColumn.setViewHorizontalReport(vwHorizontalRpt);
				vwHorizontalRptColumn.setVwHorizontalRptColumnDbId(txtVwHorizontalRptColumnDbId.getValue());
				vwHorizontalRptColumn.setVwHorizontalRptColumnName(txtVwHorizontalRptColumnName.getValue());
				vwHorizontalRptColumn.setExpField(null);
				vwHorizontalRptColumn.setFnyInfoField(null);
				vwHorizontalRptColumn.setFpyInfoField(null);
				vwHorizontalRptColumn.setFtyInfoField(null);
				vwHorizontalRptColumn.setTargetColumn(null);
				vwHorizontalRptColumn.setVwHorizontalFnyIsDateTimeExpField(false);
				vwHorizontalRptColumn.setVwHorizontalFnyIsResultExpField(false);
				vwHorizontalRptColumn.setVwHorizontalFnyIsSNExpField(false);
				vwHorizontalRptColumn.setVwHorizontalFpyIsDateTimeExpField(false);
				vwHorizontalRptColumn.setVwHorizontalFpyIsResultExpField(false);
				vwHorizontalRptColumn.setVwHorizontalFpyIsSNExpField(false);
				vwHorizontalRptColumn.setVwHorizontalFtyIsDateTimeExpField(false);
				vwHorizontalRptColumn.setVwHorizontalFtyIsResultExpField(false);
				vwHorizontalRptColumn.setVwHorizontalFtyIsSNExpField(false);
				
				
				if(dataSourceStrId.startsWith("exp"))
				{
					Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldStrId.substring(9));
					ExperimentField expField = new ExperimentFieldDao().getExperimentFieldById(dataSourceFieldId);
					vwHorizontalRptColumn.setExpField(expField);
					vwHorizontalRptColumn.setExperiment(expField.getExperiment());
					vwHorizontalRptColumn.setVwHorizontalRptColumnDataType(expField.getExpFieldType());
				}
				
				if(dataSourceStrId.startsWith("fpy"))
				{
					Integer fpyRptId = Integer.parseInt(dataSourceStrId.substring(4));
					vwHorizontalRptColumn.setFpyRpt(new FirstPassYieldReportDao().getFirstPassYieldReportById(fpyRptId));
					
					if("fpy_sn".equals(dataSourceFieldStrId) || "fpy_result".equals(dataSourceFieldStrId) || "fpy_datetime".equals(dataSourceFieldStrId))
					{
						vwHorizontalRptColumn.setVwHorizontalFpyIsSNExpField("fpy_sn".equals(dataSourceFieldStrId));
						vwHorizontalRptColumn.setVwHorizontalFpyIsResultExpField("fpy_result".equals(dataSourceFieldStrId));
						vwHorizontalRptColumn.setVwHorizontalFpyIsDateTimeExpField("fpy_datetime".equals(dataSourceFieldStrId));
						vwHorizontalRptColumn.setVwHorizontalRptColumnDataType((("fpy_sn".equals(dataSourceFieldStrId) || "fpy_result".equals(dataSourceFieldStrId) ? "nvarchar(max)" : "datetime")));
					}
					else
					{
						Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldStrId.substring(9));
						FirstPassYieldInfoField fpyInfoField = new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldById(dataSourceFieldId);
						vwHorizontalRptColumn.setFpyInfoField(fpyInfoField);
						vwHorizontalRptColumn.setVwHorizontalRptColumnDataType(fpyInfoField.getExperimentField().getExpFieldType());
					}
				}
			
				if(dataSourceStrId.startsWith("fny"))
				{
					Integer fnyRptId = Integer.parseInt(dataSourceStrId.substring(4));
					vwHorizontalRptColumn.setFnyRpt(new FinalPassYieldReportDao().getFinalPassYieldReportById(fnyRptId));
					
					if("fny_sn".equals(dataSourceFieldStrId) || "fny_result".equals(dataSourceFieldStrId) || "fny_datetime".equals(dataSourceFieldStrId))
					{
						vwHorizontalRptColumn.setVwHorizontalFnyIsSNExpField("fny_sn".equals(dataSourceFieldStrId));
						vwHorizontalRptColumn.setVwHorizontalFnyIsResultExpField("fny_result".equals(dataSourceFieldStrId));
						vwHorizontalRptColumn.setVwHorizontalFnyIsDateTimeExpField("fny_datetime".equals(dataSourceFieldStrId));	
						vwHorizontalRptColumn.setVwHorizontalRptColumnDataType((("fny_sn".equals(dataSourceFieldStrId) || "fny_result".equals(dataSourceFieldStrId) ? "nvarchar(max)" : "datetime")));
					}
					else
					{
						Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldStrId.substring(9));
						FinalPassYieldInfoField fnyInfoField = new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldById(dataSourceFieldId);
						vwHorizontalRptColumn.setFnyInfoField(fnyInfoField);
						vwHorizontalRptColumn.setVwHorizontalRptColumnDataType(fnyInfoField.getExperimentField().getExpFieldType());
					}
				}
				
				if(dataSourceStrId.startsWith("fty"))
				{
					Integer ftyRptId = Integer.parseInt(dataSourceStrId.substring(4));
					vwHorizontalRptColumn.setFtyRpt(new FirstTimeYieldReportDao().getFirstTimeYieldReportById(ftyRptId));
					
					if("fty_sn".equals(dataSourceFieldStrId) || "fty_result".equals(dataSourceFieldStrId) || "fty_datetime".equals(dataSourceFieldStrId))
					{
						vwHorizontalRptColumn.setVwHorizontalFtyIsSNExpField("fty_sn".equals(dataSourceFieldStrId));
						vwHorizontalRptColumn.setVwHorizontalFtyIsResultExpField("fty_result".equals(dataSourceFieldStrId));
						vwHorizontalRptColumn.setVwHorizontalFtyIsDateTimeExpField("fty_datetime".equals(dataSourceFieldStrId));						
						vwHorizontalRptColumn.setVwHorizontalRptColumnDataType((("fty_sn".equals(dataSourceFieldStrId) || "fty_result".equals(dataSourceFieldStrId) ? "nvarchar(max)" : "datetime")));
					}
					else
					{
						Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldStrId.substring(9));
						FirstTimeYieldInfoField ftyInfoField = new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldById(dataSourceFieldId);
						vwHorizontalRptColumn.setFtyInfoField(ftyInfoField);
						vwHorizontalRptColumn.setVwHorizontalRptColumnDataType(ftyInfoField.getExperimentField().getExpFieldType());
					}
				}
				
				if(dataSourceStrId.startsWith("tgt"))
				{
					Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldStrId.substring(9));
					TargetColumn targetColumn = new TargetColumnDao().getTargetColumnById(dataSourceFieldId);
					vwHorizontalRptColumn.setTargetColumn(targetColumn);
					vwHorizontalRptColumn.setTargetRpt(targetColumn.getTargetColumnGroup().getTargetReport());
					vwHorizontalRptColumn.setVwHorizontalRptColumnDataType(targetColumn.getExperimentField().getExpFieldType());
				}
				
				new ViewHorizontalReportColumnDao().addVwHorizontalReportColumn(vwHorizontalRptColumn);
			
				vwEnrichmentRptColumnId.add(txtVwHorizontalRptColumnDbId.getValue());
				vwEnrichmentRptColumn.add(vwHorizontalRptColumn);
			}
			
			
			//Enrichment Rules
			Collection enrichmentRuleItemIds = this.tblColumnsEnrichment.getContainerDataSource().getItemIds();
			
			ViewHorizontalReportColumnByEnrichmentDao vwHorizontalRptColEnrichmentDao = new ViewHorizontalReportColumnByEnrichmentDao();
			
			for (Object enrichmentRuleItemIdObj : enrichmentRuleItemIds) 
			{
				int enrichmentRuleItemId = (int)enrichmentRuleItemIdObj;
				Item tblEnrichmentRuleRowItem = this.tblColumnsEnrichment.getContainerDataSource().getItem(enrichmentRuleItemId);
				
				ViewHorizontalReportColumnByEnrichment vwHorizontalRptColByEnrichment = new ViewHorizontalReportColumnByEnrichment();
				Integer vwEnrichmentRptColumnIndex = vwEnrichmentRptColumnId.indexOf(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("Report Column").getValue())).getValue().toString());
				vwHorizontalRptColByEnrichment.setVwHorizontalReportColumn(vwEnrichmentRptColumn.get(vwEnrichmentRptColumnIndex));
				vwHorizontalRptColByEnrichment.setVwHorizontalRptColumnEnrichmentOperation(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("Operator").getValue())).getValue().toString());			
				vwHorizontalRptColByEnrichment.setVwHorizontalRptColumnEnrichmentValue(((TextField)(tblEnrichmentRuleRowItem.getItemProperty("Value").getValue())).getValue().toString());			
				vwHorizontalRptColByEnrichment.setVwHorizontalRptColumnEnrichmentType((((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("Enrichment Type").getValue())).getValue().toString()));			
				
				String customListValueStrId = "";
				if(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("List Value").getValue())).getValue() != null)
					customListValueStrId = ((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("List Value").getValue())).getValue().toString();
	
				if(!customListValueStrId.isEmpty())
					vwHorizontalRptColByEnrichment.setCustomListValue((new CustomListValueDao().getCustomListValueById(Integer.parseInt(customListValueStrId))));			
				
				vwHorizontalRptColByEnrichment.setVwHorizontalRptColumnEnrichmentStaticValue(((TextField)(tblEnrichmentRuleRowItem.getItemProperty("Static Value").getValue())).getValue().toString());			
				
				vwHorizontalRptColEnrichmentDao.addVwHorizontalReportColumnByEnrichment(vwHorizontalRptColByEnrichment);
			}
						
			if(isNewRecord)
			{
				WebApplication webApp = (WebApplication)this.getParent().getParent();
				webApp.reloadMainForm("horizontal view report");
			}
			else
			{
				
				ViewHorizontalReportColumnByEnrichmentDao deleteVwHorizontalRptColEnrichmentDao = new ViewHorizontalReportColumnByEnrichmentDao();
				for(int i=0; vwHorizontalRptColumnsByEnrichment != null &&i<vwHorizontalRptColumnsByEnrichment.size(); i++)
					deleteVwHorizontalRptColEnrichmentDao.deleteVwHorizontalReportColumnByEnrichment(vwHorizontalRptColumnsByEnrichment.get(i).getVwHorizontalRptColumnByEnrichmentId()); 
			
				ViewHorizontalReportFilterByExpFieldDao vwHorizontalReportFilterByExpFieldDao = new ViewHorizontalReportFilterByExpFieldDao();
				for(int i=0; vwHorizontalRptFiltersByExpField != null &&i<vwHorizontalRptFiltersByExpField.size(); i++)
					vwHorizontalReportFilterByExpFieldDao.deleteVwHorizontalReportFilterByExpField(vwHorizontalRptFiltersByExpField.get(i).getVwHorizontalRptFilterByExpFieldId());
				
				ViewHorizontalReportFilterByFpyFieldDao vwHorizontalReportFilterByFpyFieldDao = new ViewHorizontalReportFilterByFpyFieldDao();
				for(int i=0; vwHorizontalRptFiltersByFpyField != null &&i<vwHorizontalRptFiltersByFpyField.size(); i++)
					vwHorizontalReportFilterByFpyFieldDao.deleteVwHorizontalReportFilterByFpyField(vwHorizontalRptFiltersByFpyField.get(i).getVwHorizontalRptFilterByFpyFieldId());
				
				ViewHorizontalReportFilterByFnyFieldDao vwHorizontalReportFilterByFnyFieldDao = new ViewHorizontalReportFilterByFnyFieldDao();
				for(int i=0; vwHorizontalRptFiltersByFnyField != null &&i<vwHorizontalRptFiltersByFnyField.size(); i++)
					vwHorizontalReportFilterByFnyFieldDao.deleteVwHorizontalReportFilterByFnyField(vwHorizontalRptFiltersByFnyField.get(i).getVwHorizontalRptFilterByFnyFieldId());
				
				ViewHorizontalReportFilterByFtyFieldDao vwHorizontalReportFilterByFtyFieldDao = new ViewHorizontalReportFilterByFtyFieldDao();
				for(int i=0; vwHorizontalRptFiltersByFtyField != null &&i<vwHorizontalRptFiltersByFtyField.size(); i++)
					vwHorizontalReportFilterByFtyFieldDao.deleteVwHorizontalReportFilterByFtyField(vwHorizontalRptFiltersByFtyField.get(i).getVwHorizontalRptFilterByFtyFieldId());
				
				ViewHorizontalReportFilterByTargetColumnDao vwHorizontalReportFilterByTargetColumnDao = new ViewHorizontalReportFilterByTargetColumnDao();
				for(int i=0; vwHorizontalRptFiltersByTgtCol != null &&i<vwHorizontalRptFiltersByTgtCol.size(); i++)
					vwHorizontalReportFilterByTargetColumnDao.deleteVwHorizontalReportFilterByTargetColumn(vwHorizontalRptFiltersByTgtCol.get(i).getVwHorizontalRptFilterByTargetColumnId());
				
				ViewHorizontalReportColumnDao vwHorizontalReportColumnDao = new ViewHorizontalReportColumnDao();
				for(int i=0; vwHorizontalRptColumns != null && i<vwHorizontalRptColumns.size(); i++)
					vwHorizontalReportColumnDao.deleteVwHorizontalReportColumn(vwHorizontalRptColumns.get(i).getVwHorizontalRptColumnId());
				
				ViewHorizontalReportByExperimentDao vwHorizontalReportByExperimentDao = new ViewHorizontalReportByExperimentDao();
				for(int i=0; vwHorizontalRptByExperimentList != null && i<vwHorizontalRptByExperimentList.size(); i++)
					vwHorizontalReportByExperimentDao.deleteVwHorizontalReportByExperiment(vwHorizontalRptByExperimentList.get(i).getVwHorizontalRptByExperimentId());

				ViewHorizontalReportByFpyRptDao vwHorizontalReportByFpyRptDao = new ViewHorizontalReportByFpyRptDao();
				for(int i=0; vwHorizontalRptByFpyRptList != null && i<vwHorizontalRptByFpyRptList.size(); i++)
					vwHorizontalReportByFpyRptDao.deleteVwHorizontalReportByFpyRpt(vwHorizontalRptByFpyRptList.get(i).getVwHorizontalRptByFpyId());

				ViewHorizontalReportByFtyRptDao vwHorizontalReportByFtyRptDao = new ViewHorizontalReportByFtyRptDao();
				for(int i=0; vwHorizontalRptByFtyRptList != null && i<vwHorizontalRptByFtyRptList.size(); i++)
					vwHorizontalReportByFtyRptDao.deleteVwHorizontalReportByFtyRpt(vwHorizontalRptByFtyRptList.get(i).getVwHorizontalRptByFtyId());
				
				ViewHorizontalReportByFnyRptDao vwHorizontalReportByFnyRptDao = new ViewHorizontalReportByFnyRptDao();
				for(int i=0; vwHorizontalRptByFnyRptList != null && i<vwHorizontalRptByFnyRptList.size(); i++)
					vwHorizontalReportByFnyRptDao.deleteVwHorizontalReportByFnyRpt(vwHorizontalRptByFnyRptList.get(i).getVwHorizontalRptByFnyId());
				
				ViewHorizontalReportByTargetRptDao vwHorizontalReportByTargetRptDao = new ViewHorizontalReportByTargetRptDao();
				for(int i=0; vwHorizontalRptByTgtRptList != null && i<vwHorizontalRptByTgtRptList.size(); i++)
					vwHorizontalReportByTargetRptDao.deleteVwHorizontalReportByTargetRpt(vwHorizontalRptByTgtRptList.get(i).getVwHorizontalRptByTargetRptId());
				
			}
			
			closeModalWindow();		
			
		}
		else
		{
			if(!validateRequiredFieldsResult)
				this.getUI().showNotification("Please fill in all required fields.", Type.WARNING_MESSAGE);
			else if(!validateNotDuplicatedReportColumnsResult)
				this.getUI().showNotification("Column names for report must be unique.", Type.WARNING_MESSAGE);
		}
	}	

	private boolean validateRequiredFields()
	{
		if(!this.txtVwHorizontalRptName.isValid() || !this.txtVwHorizontalRptCustomId.isValid() || !this.txtVwHorizontalRptKeyColCustomId.isValid() || !this.txtVwHorizontalRptKeyColName.isValid())
			return false;		
		
		//Validate Key Column Map
		//Add Key column for Target
		Collection rptKeyColumnsMapItemIds = this.tblKeyColumnMapping.getContainerDataSource().getItemIds();
		
		for (Object rptKeyColumnMapItemIdObj : rptKeyColumnsMapItemIds) 
		{
			Item tblKeyColumnMapRowItem = this.tblKeyColumnMapping.getContainerDataSource().getItem(rptKeyColumnMapItemIdObj);
			ComboBox cbxKeyColumnMapField = (ComboBox)(tblKeyColumnMapRowItem.getItemProperty("Data Source Table/Report").getValue());

			if(!cbxKeyColumnMapField.isValid())
				return false;
		}
		
		//Validate Horizontal Columns
		for(int i=0; i<lytVwHorizontalRptColumnsPnls.getComponentCount(); i++)
		{
			VerticalLayout pnlRptColumn = (VerticalLayout)lytVwHorizontalRptColumnsPnls.getComponent(i);

			TextField txtVwHorizontalRptColumnName = (TextField)pnlRptColumn.getComponent(0);
			TextField txtVwHorizontalRptColumnDbId = (TextField)pnlRptColumn.getComponent(1);
			ComboBox cbxVwHorizontalRptColDataSource = (ComboBox)pnlRptColumn.getComponent(2);
			ComboBox cbxVwHorizontalRptColDataSourceField = (ComboBox)pnlRptColumn.getComponent(3);
			
			if(!txtVwHorizontalRptColumnName.isValid() || !txtVwHorizontalRptColumnDbId.isValid() || !cbxVwHorizontalRptColDataSource.isValid() || !cbxVwHorizontalRptColDataSourceField.isValid())
				return false;	
		}
		
		Collection filterItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
		if(filterItemIds != null)
		{
			for (Object itemIdObj : filterItemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblReportFilters.getContainerDataSource().getItem(itemId);
	
				if(((ComboBox)(tblRowItem.getItemProperty("Data Source").getValue())).getValue() == null)
					return false;
				if(((ComboBox)(tblRowItem.getItemProperty("Source Column/Field").getValue())).getValue() == null)
					return false;
				if(((ComboBox)(tblRowItem.getItemProperty("Filter Operator").getValue())).getValue() == null)
					return false;
				
				String filterOperatorSelected = ((ComboBox)(tblRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
				GridLayout gridFilterValues = (GridLayout)tblRowItem.getItemProperty("Filter Value").getValue();
				
				if(filterOperatorSelected.equals("between") || filterOperatorSelected.equals("notbetween"))
				{
					TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					if(txtFilterValue1.getValue() == null || txtFilterValue2.getValue() == null || txtFilterValue1.getValue().isEmpty() || txtFilterValue2.getValue().isEmpty())
						return false;
				}
				else if(filterOperatorSelected.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
					if(fromDateField.getValue() == null || toDateField.getValue() == null)
						return false;
				}
				else if(filterOperatorSelected.equals("after") || filterOperatorSelected.equals("before")
	    				|| filterOperatorSelected.equals("on") || filterOperatorSelected.equals("onorafter")
	    				|| filterOperatorSelected.equals("onorbefore"))
				{
					DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
					if(dtFilterValue.getValue() == null)
						return false;									
				}
				else if(!filterOperatorSelected.equals("today"))
				{
					TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					if(txtFilterValue1.getValue() == null)
						return false;
				}
			}
		}

		Collection enrichmentItemIds = this.tblColumnsEnrichment.getContainerDataSource().getItemIds();
		if(enrichmentItemIds != null)
		{
			for (Object itemIdObj : enrichmentItemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblColumnsEnrichment.getContainerDataSource().getItem(itemId);
	
				if(((ComboBox)(tblRowItem.getItemProperty("Report Column").getValue())).getValue() == null)
					return false;
				if(((ComboBox)(tblRowItem.getItemProperty("Operator").getValue())).getValue() == null)
					return false;
				if(((TextField)(tblRowItem.getItemProperty("Value").getValue())).getValue() == null)
					return false;
				if(((ComboBox)(tblRowItem.getItemProperty("Enrichment Type").getValue())).getValue() == null)
					return false;
			}
		}
		return true;
	}

	private boolean validateNotDuplicatedReportColumns()
	{		
		List<String> addedReportColumnNames = new ArrayList<String>(); 
		List<String> addedReportColumnDBNames = new ArrayList<String>();
		
		//Validate Horizontal Columns
		for(int i=0; i<lytVwHorizontalRptColumnsPnls.getComponentCount(); i++)
		{
			VerticalLayout pnlRptColumn = (VerticalLayout)lytVwHorizontalRptColumnsPnls.getComponent(i);

			TextField txtVwHorizontalRptColumnName = (TextField)pnlRptColumn.getComponent(0);
			TextField txtVwHorizontalRptColumnDbId = (TextField)pnlRptColumn.getComponent(1);
			
			if(txtVwHorizontalRptColumnName != null)
			{
				if(addedReportColumnNames.indexOf(txtVwHorizontalRptColumnName.getValue()) == -1)
					addedReportColumnNames.add(txtVwHorizontalRptColumnName.getValue());
				else
					return false;			
			}
			
			if(txtVwHorizontalRptColumnDbId != null)
			{
				if(addedReportColumnDBNames.indexOf(txtVwHorizontalRptColumnDbId.getValue()) == -1)
					addedReportColumnDBNames.add(txtVwHorizontalRptColumnDbId.getValue());
				else
					return false;
			}
		}
		
		return true;
	}
	
	
	private void onDelete()
	{
		this.savedVwHorizontalRpt.setVwHorizontalRptIsActive(false);
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		this.savedVwHorizontalRpt.setLastModifiedBy(sessionUser);
		this.savedVwHorizontalRpt.setModifiedDate(new Date());	
		new ViewHorizontalReportDao().updateVwHorizontalReport(this.savedVwHorizontalRpt);
		
		WebApplication webApp = (WebApplication)this.getParent().getParent();
		webApp.reloadMainForm("horizontal view report");
		closeModalWindow();
    }
	
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
}
