package com.bringit.experiment.ui.form;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.CsvTemplateEnrichment;
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
import com.bringit.experiment.dao.CsvTemplateEnrichmentDao;
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
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class ViewVerticalReportBuilderForm extends ViewVerticalReportBuilderDesign {

	private Boolean isNewRecord = false;
	private ViewVerticalReport savedVwVerticalRpt = new ViewVerticalReport();
	
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
	private int lastVerticalRptEnrichmentItemId = 0;
	 
	private List<Integer> dbIdOfDataSourceFilterToDelete = new ArrayList<Integer>();
	private List<Integer> dbIdOfVwVertRptColumnsToDelete = new ArrayList<Integer>();
	private List<Integer> dbIdOfVwVertRptEnrichmentToDelete = new ArrayList<Integer>();
	
	TreeTable tblVwVerticalRptCols = new TreeTable("Report Columns"); 
	
	String[] dbfieldTypes;
	
	//Saved data
	//Data Sources
	private List<ViewVerticalReportByExperiment> vwVerticalRptByExperimentList = new ArrayList<ViewVerticalReportByExperiment>();
	private List<Integer> vwVerticalRptByExperimentDbIds =  new ArrayList<Integer>();
	private List<ViewVerticalReportByFpyRpt> vwVerticalRptByFpyRptList = new ArrayList<ViewVerticalReportByFpyRpt>();
	private List<Integer> vwVerticalRptByFpyRptDbIds =  new ArrayList<Integer>();	
	private List<ViewVerticalReportByFnyRpt> vwVerticalRptByFnyRptList = new ArrayList<ViewVerticalReportByFnyRpt>();
	private List<Integer> vwVerticalRptByFnyRptDbIds =  new ArrayList<Integer>();
	private List<ViewVerticalReportByFtyRpt> vwVerticalRptByFtyRptList = new ArrayList<ViewVerticalReportByFtyRpt>();
	private List<Integer> vwVerticalRptByFtyRptDbIds =  new ArrayList<Integer>();
	private List<ViewVerticalReportByTargetRpt> vwVerticalRptByTgtRptList = new ArrayList<ViewVerticalReportByTargetRpt>();
	private List<Integer> vwVerticalRptByTargetDbIds =  new ArrayList<Integer>();
	
	//Data Source filters
	private List<ViewVerticalReportFilterByExpField> vwVerticalRptFiltersByExpField = new ArrayList<ViewVerticalReportFilterByExpField>();
	private List<ViewVerticalReportFilterByFpyField> vwVerticalRptFiltersByFpyField = new ArrayList<ViewVerticalReportFilterByFpyField>();
	private List<ViewVerticalReportFilterByFnyField> vwVerticalRptFiltersByFnyField = new ArrayList<ViewVerticalReportFilterByFnyField>();
	private List<ViewVerticalReportFilterByFtyField> vwVerticalRptFiltersByFtyField = new ArrayList<ViewVerticalReportFilterByFtyField>();
	private List<ViewVerticalReportFilterByTargetColumn> vwVerticalRptFiltersByTgtCol = new ArrayList<ViewVerticalReportFilterByTargetColumn>();
	
	//Report Columns
	private List<ViewVerticalReportColumn> vwVerticalRptColumns = new ArrayList<ViewVerticalReportColumn>();
	
	//Report Columns Mapping
	private List<ViewVerticalReportColumnByExpField> vwVerticalRptColumnsByExpField = new ArrayList<ViewVerticalReportColumnByExpField>();
	private List<ViewVerticalReportColumnByFpyField> vwVerticalRptColumnsByFpyField = new ArrayList<ViewVerticalReportColumnByFpyField>();
	private List<ViewVerticalReportColumnByFnyField> vwVerticalRptColumnsByFnyField = new ArrayList<ViewVerticalReportColumnByFnyField>();
	private List<ViewVerticalReportColumnByFtyField> vwVerticalRptColumnsByFtyField = new ArrayList<ViewVerticalReportColumnByFtyField>();
	private List<ViewVerticalReportColumnByTargetColumn> vwVerticalRptColumnsByTgtCol = new ArrayList<ViewVerticalReportColumnByTargetColumn>();
		
	//Report Columns Enrichment
	private List<ViewVerticalReportColumnByEnrichment> vwVerticalRptColumnsByEnrichment = new ArrayList<ViewVerticalReportColumnByEnrichment>();
		
	public ViewVerticalReportBuilderForm(Integer vwVerticalRptId)
	{
		if(vwVerticalRptId == null || vwVerticalRptId == -1)
			isNewRecord = true;
		else
		{	
			//Load report data from DB
			this.savedVwVerticalRpt = new ViewVerticalReportDao().getVwVerticalRptById(vwVerticalRptId);
			
			//Load data sources from DB
			vwVerticalRptByExperimentList = new ViewVerticalReportByExperimentDao().getAllVwVerticalReportByExperimentByRptId(vwVerticalRptId);
			vwVerticalRptByFpyRptList = new ViewVerticalReportByFpyRptDao().getAllVwVerticalReportByFpyRptById(vwVerticalRptId);
			vwVerticalRptByFnyRptList = new ViewVerticalReportByFnyRptDao().getAllVwVerticalReportByFnyRptById(vwVerticalRptId);
			vwVerticalRptByFtyRptList = new ViewVerticalReportByFtyRptDao().getAllVwVerticalReportByFtyRptById(vwVerticalRptId);
			vwVerticalRptByTgtRptList = new ViewVerticalReportByTargetRptDao().getAllVwVerticalReportByTargetRptById(vwVerticalRptId);
		
			//Load Filters for selected data sources
			for(int i=0; vwVerticalRptByExperimentList != null && i<vwVerticalRptByExperimentList.size(); i++)
			{
				vwVerticalRptByExperimentDbIds.add(vwVerticalRptByExperimentList.get(i).getExperiment().getExpId());
				List<ViewVerticalReportFilterByExpField> currentVwVerticalRptFiltersByExpField = new ViewVerticalReportFilterByExpFieldDao().getAllVwVerticalReportFiltersByExpRptId(vwVerticalRptByExperimentList.get(i).getVwVerticalRptByExperimentId());
				for(int j=0; currentVwVerticalRptFiltersByExpField!= null && j<currentVwVerticalRptFiltersByExpField.size(); j++)
					vwVerticalRptFiltersByExpField.add(currentVwVerticalRptFiltersByExpField.get(j));
			}

			for(int i=0; vwVerticalRptByFpyRptList != null && i<vwVerticalRptByFpyRptList.size(); i++)
			{
				vwVerticalRptByFpyRptDbIds.add(vwVerticalRptByFpyRptList.get(i).getFpyRpt().getFpyReportId());
				List<ViewVerticalReportFilterByFpyField> currentVwVerticalRptFiltersByFpyField = new ViewVerticalReportFilterByFpyFieldDao().getAllVwVerticalReportFiltersByFpyRptId(vwVerticalRptByFpyRptList.get(i).getVwVerticalRptByFpyId());
				for(int j=0; currentVwVerticalRptFiltersByFpyField != null && j<currentVwVerticalRptFiltersByFpyField.size(); j++)
					vwVerticalRptFiltersByFpyField.add(currentVwVerticalRptFiltersByFpyField.get(j));
			}

			for(int i=0; vwVerticalRptByFnyRptList != null && i<vwVerticalRptByFnyRptList.size(); i++)
			{
				vwVerticalRptByFnyRptDbIds.add(vwVerticalRptByFnyRptList.get(i).getFnyRpt().getFnyReportId());
				List<ViewVerticalReportFilterByFnyField> currentVwVerticalRptFiltersByFnyField = new ViewVerticalReportFilterByFnyFieldDao().getAllVwVerticalReportFiltersByFnyRptId(vwVerticalRptByFnyRptList.get(i).getVwVerticalRptByFnyId());
				for(int j=0; currentVwVerticalRptFiltersByFnyField != null && j<currentVwVerticalRptFiltersByFnyField.size(); j++)
					vwVerticalRptFiltersByFnyField.add(currentVwVerticalRptFiltersByFnyField.get(j));
			}

			for(int i=0; vwVerticalRptByFtyRptList != null && i<vwVerticalRptByFtyRptList.size(); i++)
			{
				vwVerticalRptByFtyRptDbIds.add(vwVerticalRptByFtyRptList.get(i).getFtyRpt().getFtyReportId());
				List<ViewVerticalReportFilterByFtyField> currentVwVerticalRptFiltersByFtyField = new ViewVerticalReportFilterByFtyFieldDao().getAllVwVerticalReportFiltersByFtyRptId(vwVerticalRptByFtyRptList.get(i).getVwVerticalRptByFtyId());
				for(int j=0; currentVwVerticalRptFiltersByFtyField != null && j<currentVwVerticalRptFiltersByFtyField.size(); j++)
					vwVerticalRptFiltersByFtyField.add(currentVwVerticalRptFiltersByFtyField.get(j));
			}

			for(int i=0; vwVerticalRptByTgtRptList != null && i<vwVerticalRptByTgtRptList.size(); i++)
			{
				vwVerticalRptByTargetDbIds.add(vwVerticalRptByTgtRptList.get(i).getTargetRpt().getTargetReportId());
				List<ViewVerticalReportFilterByTargetColumn> currentVwVerticalRptFiltersByTgtColumn = new ViewVerticalReportFilterByTargetColumnDao().getAllVwVerticalReportFiltersByTargetRptId(vwVerticalRptByTgtRptList.get(i).getVwVerticalRptByTargetRptId());
				for(int j=0; currentVwVerticalRptFiltersByTgtColumn != null && j<currentVwVerticalRptFiltersByTgtColumn.size(); j++)
					vwVerticalRptFiltersByTgtCol.add(currentVwVerticalRptFiltersByTgtColumn.get(j));
			}
			
			//Load report columns 
			vwVerticalRptColumns = new ViewVerticalReportColumnDao().getAllVwVerticalReportColumnsByRptId(vwVerticalRptId);
			
			//Load report columns mapping
			for(int i=0; vwVerticalRptColumns != null && i<vwVerticalRptColumns.size(); i++)
			{
				List<ViewVerticalReportColumnByExpField> currentVwVerticalRptColumnsByExpField = new ViewVerticalReportColumnByExpFieldDao().getAllVwVerticalRptColsByExpFieldByColId(vwVerticalRptColumns.get(i).getVwVerticalRptColumnId());
				List<ViewVerticalReportColumnByFpyField> currentVwVerticalRptColumnsByFpyField = new ViewVerticalReportColumnByFpyFieldDao().getAllVwVerticalRptColsByFpyFieldByColId(vwVerticalRptColumns.get(i).getVwVerticalRptColumnId());
				List<ViewVerticalReportColumnByFnyField> currentVwVerticalRptColumnsByFnyField = new ViewVerticalReportColumnByFnyFieldDao().getAllVwVerticalRptColsByFnyFieldByColId(vwVerticalRptColumns.get(i).getVwVerticalRptColumnId());
				List<ViewVerticalReportColumnByFtyField> currentVwVerticalRptColumnsByFtyField = new ViewVerticalReportColumnByFtyFieldDao().getAllVwVerticalRptColsByFtyFieldByColId(vwVerticalRptColumns.get(i).getVwVerticalRptColumnId());
				List<ViewVerticalReportColumnByTargetColumn> currentVwVerticalRptColumnsByTgtCol = new ViewVerticalReportColumnByTargetColumnDao().getAllVwVerticalRptColsByTargetColumnByColId(vwVerticalRptColumns.get(i).getVwVerticalRptColumnId());

				for(int j=0; currentVwVerticalRptColumnsByExpField != null && j<currentVwVerticalRptColumnsByExpField.size(); j++)
					vwVerticalRptColumnsByExpField.add(currentVwVerticalRptColumnsByExpField.get(j));
				
				for(int j=0; currentVwVerticalRptColumnsByFpyField != null && j<currentVwVerticalRptColumnsByFpyField.size(); j++)
					vwVerticalRptColumnsByFpyField.add(currentVwVerticalRptColumnsByFpyField.get(j));				

				for(int j=0; currentVwVerticalRptColumnsByFnyField != null && j<currentVwVerticalRptColumnsByFnyField.size(); j++)
					vwVerticalRptColumnsByFnyField.add(currentVwVerticalRptColumnsByFnyField.get(j));

				for(int j=0; currentVwVerticalRptColumnsByFtyField != null && j<currentVwVerticalRptColumnsByFtyField.size(); j++)
					vwVerticalRptColumnsByFtyField.add(currentVwVerticalRptColumnsByFtyField.get(j));

				for(int j=0; currentVwVerticalRptColumnsByTgtCol != null && j<currentVwVerticalRptColumnsByTgtCol.size(); j++)
					vwVerticalRptColumnsByTgtCol.add(currentVwVerticalRptColumnsByTgtCol.get(j));
			}
			
			//Load report columns enrichment
			for(int i=0; vwVerticalRptColumns != null && i<vwVerticalRptColumns.size(); i++)
			{
				List<ViewVerticalReportColumnByEnrichment> currentVwVerticalRptColumnsByEnrichment = new ViewVerticalReportColumnByEnrichmentDao().getAllVwVerticalRptColsByEnrichmentByColId(vwVerticalRptColumns.get(i).getVwVerticalRptColumnId());

				for(int j=0; currentVwVerticalRptColumnsByEnrichment != null && j<currentVwVerticalRptColumnsByEnrichment.size(); j++)
					vwVerticalRptColumnsByEnrichment.add(currentVwVerticalRptColumnsByEnrichment.get(j));
			}
			
			//Load Report Header information
			this.txtVwVerticalRptName.setValue(this.savedVwVerticalRpt.getVwVerticalRptName());
			this.txtVwVerticalRptCustomId.setValue(this.savedVwVerticalRpt.getVwVerticalRptDbTableNameId().replace("vwvertical#", ""));
			this.txtVwVerticalDescription.setValue(this.savedVwVerticalRpt.getVwVerticalRptDescription());
			this.chxActive.setValue(this.savedVwVerticalRpt.getVwVerticalRptIsActive());
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
			
			if(vwVerticalRptId != null && vwVerticalRptId > -1 && vwVerticalRptByExperimentDbIds.indexOf(activeExperiments.get(i).getExpId()) != -1)
				optGrpPnlExperiments.select(activeExperiments.get(i).getExpId());			
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
			
			if(vwVerticalRptId != null && vwVerticalRptId > -1 && vwVerticalRptByFpyRptDbIds.indexOf(activeFpyReports.get(i).getFpyReportId()) != -1)
				optGrpPnlFpyReports.select(activeFpyReports.get(i).getFpyReportId());
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
			
			if(vwVerticalRptId != null && vwVerticalRptId > -1 && vwVerticalRptByFnyRptDbIds.indexOf(activeFnyReports.get(i).getFnyReportId()) != -1)
				optGrpPnlFnyReports.select(activeFnyReports.get(i).getFnyReportId());
		}

		optGrpPnlFnyReports.addValueChangeListener(new ValueChangeListener(){
           @Override
			public void valueChange(ValueChangeEvent event) {
        	   refreshFnyRptDataSourceElements();
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

			if(vwVerticalRptId != null && vwVerticalRptId > -1 && vwVerticalRptByFtyRptDbIds.indexOf(activeFtyReports.get(i).getFtyReportId()) != -1)
				optGrpPnlFtyReports.select(activeFtyReports.get(i).getFtyReportId());
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
			
			if(vwVerticalRptId != null && vwVerticalRptId > -1 && vwVerticalRptByTargetDbIds.indexOf(activeTargetReports.get(i).getTargetReportId()) != -1)
				optGrpPnlTargetReports.select(activeTargetReports.get(i).getTargetReportId());
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

		//START: load report columns using tree
		loadReportColumnsTable();
				
		
		this.btnAddVwVerticalRptColumn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addReportColumn(null, null);
			}

		});
		
		this.btnRemoveVwVerticalRptColumn.addClickListener((new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(tblVwVerticalRptCols.getValue() != null)
				{
					Item tblRptColumnRowItem = tblVwVerticalRptCols.getContainerDataSource().getItem(tblVwVerticalRptCols.getValue());
						
					if (tblVwVerticalRptCols.hasChildren(tblVwVerticalRptCols.getValue()))
					{
						dbIdOfVwVertRptColumnsToDelete.add(Integer.parseInt(tblVwVerticalRptCols.getValue().toString()));
						
						List<Object> rptChildColItemIdToDelete = new ArrayList<Object>();
						
						for (Object rptChildColItemId: tblVwVerticalRptCols.getChildren(tblVwVerticalRptCols.getValue())) 
							rptChildColItemIdToDelete.add(rptChildColItemId);
						
						for(int i=0; i<rptChildColItemIdToDelete.size(); i++)
						{
							tblVwVerticalRptCols.removeItem(rptChildColItemIdToDelete.get(i));	
							dbIdOfVwVertRptColumnsToDelete.add(Integer.parseInt(rptChildColItemIdToDelete.get(i).toString()));
						}

						tblVwVerticalRptCols.removeItem(tblVwVerticalRptCols.getValue());
						
					}
					
				}	
			}
		}));
		
		//START: Load report columns enrichment
		loadTblEnrichmentRulesData();
		
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
		
		//Load data in UI for update
		if(vwVerticalRptId != null && vwVerticalRptId > -1)
		{			
			//Load Data Source Filters
			for(int i=0; vwVerticalRptFiltersByExpField != null && i<vwVerticalRptFiltersByExpField.size(); i++)
				addDataSourceFilter("exp_", vwVerticalRptFiltersByExpField.get(i).getVwVerticalRptFilterByExpFieldId(), vwVerticalRptFiltersByExpField.get(i), null, null, null, null);
			
			for(int i=0; vwVerticalRptFiltersByFpyField != null && i<vwVerticalRptFiltersByFpyField.size(); i++)
				addDataSourceFilter("fpy_", vwVerticalRptFiltersByFpyField.get(i).getVwVerticalRptFilterByFpyFieldId(), null, vwVerticalRptFiltersByFpyField.get(i), null, null, null);
			
			for(int i=0; vwVerticalRptFiltersByFnyField != null && i<vwVerticalRptFiltersByFnyField.size(); i++)
				addDataSourceFilter("fny_", vwVerticalRptFiltersByFnyField.get(i).getVwVerticalRptFilterByFnyFieldId(), null, null, vwVerticalRptFiltersByFnyField.get(i), null, null);
			
			for(int i=0; vwVerticalRptFiltersByFtyField != null && i<vwVerticalRptFiltersByFtyField.size(); i++)
				addDataSourceFilter("fty_", vwVerticalRptFiltersByFtyField.get(i).getVwVerticalRptFilterByFtyFieldId(), null, null, null, vwVerticalRptFiltersByFtyField.get(i), null);
			
			for(int i=0; vwVerticalRptFiltersByTgtCol != null && i<vwVerticalRptFiltersByTgtCol.size(); i++)
				addDataSourceFilter("tgt_", vwVerticalRptFiltersByTgtCol.get(i).getVwVerticalRptFilterByTargetColumnId(), null, null, null, null, vwVerticalRptFiltersByTgtCol.get(i));
			
			
			//Load Vertical Rpt Columns
			for(int i=0; i<vwVerticalRptColumns.size(); i++)
				addReportColumn(vwVerticalRptColumns.get(i).getVwVerticalRptColumnId(), vwVerticalRptColumns.get(i));
			
			//Load Rpt Columns Enrichment
			for(int i=0; i<vwVerticalRptColumnsByEnrichment.size(); i++)
				addTblEnrichmentRule(vwVerticalRptColumnsByEnrichment.get(i).getVwVerticalRptColumnByEnrichmentId(), vwVerticalRptColumnsByEnrichment.get(i));			
		}
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

	private void refreshFnyRptDataSourceElements()
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
	
	private void addDataSourceFilter(String dataSourceType, Integer dataSourceFilterId, ViewVerticalReportFilterByExpField vwVerticalRptFiltersByExpField, ViewVerticalReportFilterByFpyField vwVerticalRptFiltersByFpyField, ViewVerticalReportFilterByFnyField vwVerticalRptFiltersByFnyField, ViewVerticalReportFilterByFtyField vwVerticalRptFiltersByFtyField, ViewVerticalReportFilterByTargetColumn vwVerticalRptFiltersByTargetColumn)
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
				cbxDataSource.setValue("exp_" + vwVerticalRptFiltersByExpField.getVwVerticalReportByExperiment().getExperiment().getExpId());
				cbxDataSourceField.setValue("exp_" + vwVerticalRptFiltersByExpField.getExpField().getExpFieldId());				
				cbxFilterOperator.setValue(vwVerticalRptFiltersByExpField.getVwVerticalRptFilterByExpFieldOperation());
				String filterOperator = cbxFilterOperator.getValue().toString();
				if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					txtFilterValue1.setValue(vwVerticalRptFiltersByExpField.getVwVerticalRptFilterByExpFieldValue1());
					txtFilterValue2.setValue(vwVerticalRptFiltersByExpField.getVwVerticalRptFilterByExpFieldValue2());
				}
				else if(filterOperator.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);

		            DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	fromDateField.setValue(df.parse(vwVerticalRptFiltersByExpField.getVwVerticalRptFilterByExpFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
		            	toDateField.setValue(df.parse(vwVerticalRptFiltersByExpField.getVwVerticalRptFilterByExpFieldValue2()));
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
		            	dtFilterValue.setValue(df.parse(vwVerticalRptFiltersByExpField.getVwVerticalRptFilterByExpFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				}
				else
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(vwVerticalRptFiltersByExpField.getVwVerticalRptFilterByExpFieldValue1());
				}
				
				if(vwVerticalRptFiltersByExpField.getCustomList() != null)
				{
					cbxCustomLists.setValue(vwVerticalRptFiltersByExpField.getCustomList().getCustomListId());					
					cbxCustomListValues.setValue(vwVerticalRptFiltersByExpField.getVwVerticalRptFilterByExpFieldValue1());
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(null);
					txtFilterValue1.setReadOnly(true);
				}
				
				cbxExpression.setValue(vwVerticalRptFiltersByExpField.getVwVerticalRptFilterByExpFieldExpression());
			}
			
			if(dataSourceType.equals("fpy_"))
			{
				cbxDataSource.setValue("fpy_" + vwVerticalRptFiltersByFpyField.getVwVerticalFpyRpt().getFpyRpt().getFpyReportId());
				
				if(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyIsDateTimeExpField())
					cbxDataSourceField.setValue("fpy_datetime");
				else if(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyIsResultExpField())
					cbxDataSourceField.setValue("fpy_sn");
				else if(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpySNExpField())
					cbxDataSourceField.setValue("fpy_result");
				else
					cbxDataSourceField.setValue("fpy_" +  vwVerticalRptFiltersByFpyField.getFpyInfoField().getFpyInfoFieldId());	
				
				cbxFilterOperator.setValue(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyFieldOperation());
				
				String filterOperator = cbxFilterOperator.getValue().toString();
				if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					txtFilterValue1.setValue(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyFieldValue1());
					txtFilterValue2.setValue(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyFieldValue2());
				}
				else if(filterOperator.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);

		            DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	fromDateField.setValue(df.parse(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
		            	toDateField.setValue(df.parse(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyFieldValue2()));
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
		            	dtFilterValue.setValue(df.parse(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				}
				else
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyFieldValue1());
				}
				
				if(vwVerticalRptFiltersByFpyField.getCustomList() != null)
				{
					cbxCustomLists.setValue(vwVerticalRptFiltersByFpyField.getCustomList().getCustomListId());			
					cbxCustomListValues.setValue(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyFieldValue1());
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(null);
					txtFilterValue1.setReadOnly(true);				
				}
				
				cbxExpression.setValue(vwVerticalRptFiltersByFpyField.getVwVerticalRptFilterByFpyFieldExpression());
			}
				
			if(dataSourceType.equals("fny_"))
			{
				cbxDataSource.setValue("fny_" + vwVerticalRptFiltersByFnyField.getVwVerticalFnyRpt().getFnyRpt().getFnyReportId());
				if(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyIsDateTimeExpField())
					cbxDataSourceField.setValue("fny_datetime");
				else if(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyIsResultExpField())
					cbxDataSourceField.setValue("fny_sn");
				else if(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnySNExpField())
					cbxDataSourceField.setValue("fny_result");
				else
					cbxDataSourceField.setValue("fny_" +  vwVerticalRptFiltersByFnyField.getFnyInfoField().getFnyInfoFieldId());	
				
				cbxFilterOperator.setValue(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyFieldOperation());
				
				String filterOperator = cbxFilterOperator.getValue().toString();
				if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					txtFilterValue1.setValue(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyFieldValue1());
					txtFilterValue2.setValue(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyFieldValue2());
				}
				else if(filterOperator.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);

		            DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	fromDateField.setValue(df.parse(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
		            	toDateField.setValue(df.parse(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyFieldValue2()));
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
		            	dtFilterValue.setValue(df.parse(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				}
				else
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyFieldValue1());
				}
				
				if(vwVerticalRptFiltersByFnyField.getCustomList() != null)
				{
					cbxCustomLists.setValue(vwVerticalRptFiltersByFnyField.getCustomList().getCustomListId());				
					cbxCustomListValues.setValue(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyFieldValue1());
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(null);
					txtFilterValue1.setReadOnly(true);				
				}
				
				cbxExpression.setValue(vwVerticalRptFiltersByFnyField.getVwVerticalRptFilterByFnyFieldExpression());
			}

			if(dataSourceType.equals("fty_"))
			{
				cbxDataSource.setValue("fty_" + vwVerticalRptFiltersByFtyField.getVwVerticalFtyRpt().getFtyRpt().getFtyReportId());
				if(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyIsDateTimeExpField())
					cbxDataSourceField.setValue("fty_datetime");
				else if(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyIsResultExpField())
					cbxDataSourceField.setValue("fty_sn");
				else if(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtySNExpField())
					cbxDataSourceField.setValue("fty_result");
				else
					cbxDataSourceField.setValue("fty_" +  vwVerticalRptFiltersByFtyField.getFtyInfoField().getFtyInfoFieldId());
				
				cbxFilterOperator.setValue(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyFieldOperation());
				
				String filterOperator = cbxFilterOperator.getValue().toString();
				if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					txtFilterValue1.setValue(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyFieldValue1());
					txtFilterValue2.setValue(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyFieldValue2());
				}
				else if(filterOperator.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);

		            DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	fromDateField.setValue(df.parse(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
		            	toDateField.setValue(df.parse(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyFieldValue2()));
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
		            	dtFilterValue.setValue(df.parse(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyFieldValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				}
				else
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyFieldValue1());
				}
				
				if(vwVerticalRptFiltersByFtyField.getCustomList() != null)
				{
					cbxCustomLists.setValue(vwVerticalRptFiltersByFtyField.getCustomList().getCustomListId());		
					cbxCustomListValues.setValue(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyFieldValue1());
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(null);
					txtFilterValue1.setReadOnly(true);						
				}

				cbxExpression.setValue(vwVerticalRptFiltersByFtyField.getVwVerticalRptFilterByFtyFieldExpression());
			}
			
			if(dataSourceType.equals("tgt_"))
			{
				cbxDataSource.setValue("tgt_" + vwVerticalRptFiltersByTargetColumn.getVwVerticalTargetRpt().getTargetRpt().getTargetReportId());
				cbxDataSourceField.setValue("tgt_" +  vwVerticalRptFiltersByTargetColumn.getTargetColumn().getTargetColumnId());		
				
				
				cbxFilterOperator.setValue(vwVerticalRptFiltersByTargetColumn.getVwVerticalRptFilterByTargetColumnOperation());
				
				String filterOperator = cbxFilterOperator.getValue().toString();
				if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
					txtFilterValue1.setValue(vwVerticalRptFiltersByTargetColumn.getVwVerticalRptFilterByTargetColumnValue1());
					txtFilterValue2.setValue(vwVerticalRptFiltersByTargetColumn.getVwVerticalRptFilterByTargetColumnValue2());
				}
				else if(filterOperator.equals("customrange"))
				{
					DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
					DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);

		            DateFormat df = new SimpleDateFormat("yyyyMMdd");
		            try {
		            	fromDateField.setValue(df.parse(vwVerticalRptFiltersByTargetColumn.getVwVerticalRptFilterByTargetColumnValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            try {
		            	toDateField.setValue(df.parse(vwVerticalRptFiltersByTargetColumn.getVwVerticalRptFilterByTargetColumnValue2()));
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
		            	dtFilterValue.setValue(df.parse(vwVerticalRptFiltersByTargetColumn.getVwVerticalRptFilterByTargetColumnValue1()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}									
				}
				else
				{
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(vwVerticalRptFiltersByTargetColumn.getVwVerticalRptFilterByTargetColumnValue1());
				}	
				
				if(vwVerticalRptFiltersByTargetColumn.getCustomList() != null)
				{
					cbxCustomLists.setValue(vwVerticalRptFiltersByTargetColumn.getCustomList().getCustomListId());				
					cbxCustomListValues.setValue(vwVerticalRptFiltersByTargetColumn.getVwVerticalRptFilterByTargetColumnValue1());
					txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
					txtFilterValue1.setValue(null);
					txtFilterValue1.setReadOnly(true);				
				}
				
				cbxExpression.setValue(vwVerticalRptFiltersByTargetColumn.getVwVerticalRptFilterByTargetColumnExpression());
			}
			
		}
		
		this.tblReportFilters.addItem(itemValues, itemId);
		this.tblReportFilters.select(itemId);
		
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

	private void addTblEnrichmentRule(Integer verticalRptEnrichmentRuleId, ViewVerticalReportColumnByEnrichment vwVerticalRptColumnsByEnrichment)
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
		
		Collection rptColumnItemIds = this.tblVwVerticalRptCols.getContainerDataSource().getItemIds();
		
		for (Object rptColumnItemIdObj : rptColumnItemIds) 
		{
			//int rptColumnItemId = (int)rptColumnItemIdObj;
			Item tblEnrichmentRuleRowItem = this.tblVwVerticalRptCols.getContainerDataSource().getItem(rptColumnItemIdObj);
			
			if (tblVwVerticalRptCols.hasChildren(rptColumnItemIdObj))
			{
				cbxRptColumnSource.addItem(((TextField)(tblEnrichmentRuleRowItem.getItemProperty("DB Column Name").getValue())).getValue().toString());
				cbxRptColumnSource.setItemCaption(((TextField)(tblEnrichmentRuleRowItem.getItemProperty("DB Column Name").getValue())).getValue().toString(), ((TextField)(tblEnrichmentRuleRowItem.getItemProperty("Column Name").getValue())).getValue().toString());
				cbxRptColumnSource.setWidth(100, Unit.PERCENTAGE);
			}
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
		
		if(itemId != null && vwVerticalRptColumnsByEnrichment != null)
		{
			cbxRptColumnSource.setValue(vwVerticalRptColumnsByEnrichment.getVwVerticalReportColumn().getVwVerticalRptColumnDbId());
			cbxOperator.setValue(vwVerticalRptColumnsByEnrichment.getVwVerticalRptColumnEnrichmentOperation());
			txtComparisonValue.setValue(vwVerticalRptColumnsByEnrichment.getVwVerticalRptColumnEnrichmentValue());
			cbxEnrichmentType.setValue(vwVerticalRptColumnsByEnrichment.getVwVerticalRptColumnEnrichmentType());
			cbxCustomList.setValue(vwVerticalRptColumnsByEnrichment.getCustomListValue());
			cbxCustomListValue.setValue(vwVerticalRptColumnsByEnrichment.getCustomListValue());
			txtStaticValue.setValue(vwVerticalRptColumnsByEnrichment.getVwVerticalRptColumnEnrichmentStaticValue());			
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
		
	private void loadReportColumnsTable()
	{
		tblVwVerticalRptCols = new TreeTable("Report Columns");
		tblVwVerticalRptCols.setStyleName("small");
		tblVwVerticalRptCols.setHeight(100, Unit.PERCENTAGE);
		tblVwVerticalRptCols.setWidth(100, Unit.PERCENTAGE);
		tblVwVerticalRptCols.addContainerProperty("Column Name", TextField.class, null);
		tblVwVerticalRptCols.addContainerProperty("Key", CheckBox.class, null);
		tblVwVerticalRptCols.addContainerProperty("DB Column Name", TextField.class, null);
		tblVwVerticalRptCols.addContainerProperty("Data Type", ComboBox.class, null);
		tblVwVerticalRptCols.addContainerProperty("Data Source Table/Report", ComboBox.class, null);
		tblVwVerticalRptCols.addContainerProperty("Data Source Field", ComboBox.class, null);
		tblVwVerticalRptCols.addContainerProperty("To Map", CheckBox.class, null);
		tblVwVerticalRptCols.setEditable(true);
		tblVwVerticalRptCols.setSelectable(true);
		tblVwVerticalRptCols.setPageLength(0);
		
		lytVwVerticalRptColumnsTbl.addComponent(tblVwVerticalRptCols);
		
	}

	private void addReportColumn(Integer reportColumnId, ViewVerticalReportColumn vwVerticalRptCol)
	{
		//Get all selected data sources 
		Integer itemId = reportColumnId;
		if(itemId == null)
		{	
			this.lastReportColumnItemId = this.lastReportColumnItemId - 1;
			itemId = this.lastReportColumnItemId;
		}
		
		Object[] rptColItemValues = new Object[7];
				
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
		

		CheckBox chxKeyColumn = new CheckBox();
		chxKeyColumn.setStyleName("tiny");
		chxKeyColumn.setVisible(true);
		chxKeyColumn.setValue(false);
		chxKeyColumn.setHeight(20, Unit.PIXELS);
		chxKeyColumn.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[1] = chxKeyColumn;
		
		
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
		rptColItemValues[2] = txtRptDbColumnName;
				
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
				
		rptColItemValues[3] = cbxDataType;

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
		rptColItemValues[4] = cbxDataSource;

		ComboBox cbxDataSourceField = new ComboBox("");
		cbxDataSourceField.setStyleName("tiny");
		cbxDataSourceField.setHeight(20, Unit.PIXELS);
		cbxDataSourceField.setWidth(100, Unit.PERCENTAGE);
		cbxDataSourceField.setReadOnly(true);
		rptColItemValues[5] = cbxDataSourceField;

		CheckBox chxMappedData = new CheckBox();
		chxMappedData.setStyleName("tiny");
		chxMappedData.setVisible(true);
		chxMappedData.setValue(false);
		chxMappedData.setReadOnly(true);
		chxMappedData.setHeight(20, Unit.PIXELS);
		chxMappedData.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[6] = chxMappedData;
		
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
					addChildRptCol(this.lastReportColumnItemId, itemId, selectedExperimentId, null,	null, null, null, null, null, null, null, null);
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
					addChildRptCol(this.lastReportColumnItemId, itemId, null, null,	selectedFpyReportId, null, null, null, null, null, null, null);
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
					this.lastReportColumnItemId = this.lastReportColumnItemId - 1;
					addChildRptCol(this.lastReportColumnItemId, itemId, null, null,	null, null, selectedFnyReportId, null, null, null, null, null);
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
					addChildRptCol(this.lastReportColumnItemId, itemId, null, null,	null, null, null, null, selectedFtyReportId, null, null, null);
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
					addChildRptCol(this.lastReportColumnItemId, itemId, null, null, null, null,	null, null, null, null, selectedTargetReportId, null);
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
		

		if(itemId != null && vwVerticalRptCol != null)
		{
			txtRptColumnName.setValue(vwVerticalRptCol.getVwVerticalRptColumnName());
			chxKeyColumn.setValue(vwVerticalRptCol.getVwVerticalRptColumnIsKey());
			txtRptDbColumnName.setValue(vwVerticalRptCol.getVwVerticalRptColumnDbId());
			cbxDataType.setValue(vwVerticalRptCol.getVwVerticalRptColumnDataType());
				
			for (Object rptChildColItemId: tblVwVerticalRptCols.getChildren(itemId)) 
			{
				Item tblVwVerticalRptColRowItem = tblVwVerticalRptCols.getContainerDataSource().getItem(rptChildColItemId);
			

				ComboBox childCbxDataSource = (ComboBox)(tblVwVerticalRptColRowItem.getItemProperty("Data Source Table/Report").getValue());
				ComboBox childCbxDataSourceField = (ComboBox)(tblVwVerticalRptColRowItem.getItemProperty("Data Source Field").getValue());

				Integer dataSourceId = Integer.parseInt(childCbxDataSource.getValue().toString().substring(4));
										
				if(childCbxDataSource.getValue().toString().startsWith("exp_"))
				{
					for(int i=0; vwVerticalRptColumnsByExpField!= null && i<vwVerticalRptColumnsByExpField.size(); i++)
					{
						if(vwVerticalRptColumnsByExpField.get(i).getExperimentField()!= null && vwVerticalRptColumnsByExpField.get(i).getExperimentField().getExperiment().getExpId() == dataSourceId)
							childCbxDataSourceField.setValue("expfield_"+ vwVerticalRptColumnsByExpField.get(i).getExperimentField().getExpFieldId());
					}
				}
				else if(childCbxDataSource.getValue().toString().startsWith("fpy_"))
				{
					for(int i=0; vwVerticalRptColumnsByFpyField!= null && i<vwVerticalRptColumnsByFpyField.size(); i++)
					{
						if(vwVerticalRptColumnsByFpyField.get(i).getFpyRpt().getFpyReportId() == dataSourceId)
						{
							if(vwVerticalRptColumnsByFpyField.get(i).getVwVerticalRptColumnByFpyIsDateTimeExpField() != null
									&& vwVerticalRptColumnsByFpyField.get(i).getVwVerticalRptColumnByFpyIsDateTimeExpField())
								childCbxDataSourceField.setValue("fpy_datetime");
							else if(vwVerticalRptColumnsByFpyField.get(i).getVwVerticalRptColumnByFpyIsSNExpField() != null
									&& vwVerticalRptColumnsByFpyField.get(i).getVwVerticalRptColumnByFpyIsSNExpField())
								childCbxDataSourceField.setValue("fpy_sn");
							else if(vwVerticalRptColumnsByFpyField.get(i).getVwVerticalRptColumnByFpyIsResultExpField() != null
									&& vwVerticalRptColumnsByFpyField.get(i).getVwVerticalRptColumnByFpyIsResultExpField())
								childCbxDataSourceField.setValue("fpy_result");
							else
								childCbxDataSourceField.setValue("fpyfield_"+ vwVerticalRptColumnsByFpyField.get(i).getFirstPassYieldInfoField().getFpyInfoFieldId());
						}						
					}					
				}
				else if(childCbxDataSource.getValue().toString().startsWith("fny_"))
				{
					for(int i=0; vwVerticalRptColumnsByFnyField!= null && i<vwVerticalRptColumnsByFnyField.size(); i++)
					{
						if(vwVerticalRptColumnsByFnyField.get(i).getFnyRpt().getFnyReportId() == dataSourceId)
						{
							if(vwVerticalRptColumnsByFnyField.get(i).getVwVerticalRptColumnByFnyIsDateTimeExpField() != null
									&& vwVerticalRptColumnsByFnyField.get(i).getVwVerticalRptColumnByFnyIsDateTimeExpField())
								childCbxDataSourceField.setValue("fny_datetime");
							else if(vwVerticalRptColumnsByFnyField.get(i).getVwVerticalRptColumnByFnyIsDateTimeExpField() != null
									&& vwVerticalRptColumnsByFnyField.get(i).getVwVerticalRptColumnByFnyIsSNExpField())
								childCbxDataSourceField.setValue("fny_sn");
							else if(vwVerticalRptColumnsByFnyField.get(i).getVwVerticalRptColumnByFnyIsDateTimeExpField() != null
									&& vwVerticalRptColumnsByFnyField.get(i).getVwVerticalRptColumnByFnyIsResultExpField())
								childCbxDataSourceField.setValue("fny_result");
							else
								childCbxDataSourceField.setValue("fnyfield_"+ vwVerticalRptColumnsByFnyField.get(i).getFinalPassYieldInfoField().getFnyInfoFieldId());
						}						
					}	
				}
				else if(childCbxDataSource.getValue().toString().startsWith("fty_"))
				{
					for(int i=0; vwVerticalRptColumnsByFtyField!= null && i<vwVerticalRptColumnsByFtyField.size(); i++)
					{
						if(vwVerticalRptColumnsByFtyField.get(i).getFtyRpt().getFtyReportId() == dataSourceId)
						{
							if(vwVerticalRptColumnsByFtyField.get(i).getVwVerticalRptColumnByFtyIsDateTimeExpField() != null
									&& vwVerticalRptColumnsByFtyField.get(i).getVwVerticalRptColumnByFtyIsDateTimeExpField())
								childCbxDataSourceField.setValue("fty_datetime");
							else if(vwVerticalRptColumnsByFtyField.get(i).getVwVerticalRptColumnByFtyIsDateTimeExpField() != null
									&& vwVerticalRptColumnsByFtyField.get(i).getVwVerticalRptColumnByFtyIsSNExpField())
								childCbxDataSourceField.setValue("fty_sn");
							else if(vwVerticalRptColumnsByFtyField.get(i).getVwVerticalRptColumnByFtyIsDateTimeExpField() != null
									&& vwVerticalRptColumnsByFtyField.get(i).getVwVerticalRptColumnByFtyIsResultExpField())
								childCbxDataSourceField.setValue("fty_result");
							else
								childCbxDataSourceField.setValue("ftyfield_"+ vwVerticalRptColumnsByFtyField.get(i).getFirstTimeYieldInfoField().getFtyInfoFieldId());
						}						
					}
				}
				else if(childCbxDataSource.getValue().toString().startsWith("tgt_"))
				{
					for(int i=0; vwVerticalRptColumnsByTgtCol!= null && i<vwVerticalRptColumnsByTgtCol.size(); i++)
					{
						if(vwVerticalRptColumnsByTgtCol.get(i).getTargetColumn() != null && vwVerticalRptColumnsByTgtCol.get(i).getTargetColumn().getTargetColumnGroup().getTargetReport().getTargetReportId() == dataSourceId)
							childCbxDataSourceField.setValue("tgtfield_"+ vwVerticalRptColumnsByTgtCol.get(i).getTargetColumn().getTargetColumnId());
					}
					
				}				
			}					
		}		
	}

	private void addChildRptCol(Integer rptColumnId, Integer parentRptColumnId, Integer expDataSourceId, Integer expDataSourceFieldId, 
			Integer fpyDataSourceId, Integer fpyDataSourceFieldId, Integer fnyDataSourceId, Integer fnyDataSourceFieldId, Integer ftyDataSourceId, Integer ftyDataSourceFieldId, 
			Integer tgtDataSourceId, Integer tgtDataSourceFieldId)
	{
		Object[] rptColItemValues = new Object[7];

		TextField txtRptColumnName = new TextField("");
		txtRptColumnName.setStyleName("tiny");
		txtRptColumnName.setRequired(true);
		txtRptColumnName.setReadOnly(true);
		txtRptColumnName.setRequiredError("This field is required.");
		txtRptColumnName.setHeight(20, Unit.PIXELS);
		txtRptColumnName.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[0] = txtRptColumnName;
		

		CheckBox chxKeyColumn = new CheckBox();
		chxKeyColumn.setStyleName("tiny");
		chxKeyColumn.setVisible(true);
		chxKeyColumn.setValue(false);
		chxKeyColumn.setReadOnly(true);
		chxKeyColumn.setHeight(20, Unit.PIXELS);
		chxKeyColumn.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[1] = chxKeyColumn;
		
		
		TextField txtRptDbColumnName = new TextField("");
		txtRptDbColumnName.setStyleName("tiny");
		txtRptDbColumnName.setRequired(true);
		txtRptDbColumnName.setReadOnly(true);
		txtRptDbColumnName.setRequiredError("This field is required.");
		txtRptDbColumnName.setHeight(20, Unit.PIXELS);
		txtRptDbColumnName.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[2] = txtRptDbColumnName;
				
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
		rptColItemValues[3] = cbxDataType;

		ComboBox cbxDataSource = new ComboBox("");
		cbxDataSource.setStyleName("tiny");
		cbxDataSource.setHeight(20, Unit.PIXELS);
		cbxDataSource.setWidth(100, Unit.PERCENTAGE);
		
		Integer cbxDataSourceFieldValue = null;
		if(expDataSourceId != null)
		{
			Experiment experiment = new ExperimentDao().getExperimentById(expDataSourceId);
			cbxDataSource.addItem("exp_" + expDataSourceId);
			cbxDataSource.setItemCaption("exp_" + expDataSourceId, experiment.getExpName());		
			cbxDataSource.setValue("exp_" + expDataSourceId);
			
			//Load from Update use case
			if(parentRptColumnId > 0)
			{
				for(int i=0; i<vwVerticalRptColumnsByExpField.size(); i++)
				{
					if(vwVerticalRptColumnsByExpField.get(i).getVwVerticalReportColumn().getVwVerticalRptColumnId() == parentRptColumnId)
						cbxDataSourceFieldValue =  vwVerticalRptColumnsByExpField.get(i).getExperimentField().getExpFieldId();					
				}
			}
		}
		
		if(fpyDataSourceId != null)
		{
			FirstPassYieldReport fpyRpt = new FirstPassYieldReportDao().getFirstPassYieldReportById(fpyDataSourceId);
			cbxDataSource.addItem("fpy_" + fpyDataSourceId);
			cbxDataSource.setItemCaption("fpy_" + fpyDataSourceId, "FPY: " + fpyRpt.getFpyReportName());	
			cbxDataSource.setValue("fpy_" + fpyDataSourceId);
		}
		
		if(fnyDataSourceId != null)
		{
			FinalPassYieldReport fnyRpt = new FinalPassYieldReportDao().getFinalPassYieldReportById(fnyDataSourceId);
			cbxDataSource.addItem("fny_" + fnyDataSourceId);
			cbxDataSource.setItemCaption("fny_" + fnyDataSourceId, "FNY: " + fnyRpt.getFnyReportName());	
			cbxDataSource.setValue("fny_" + fnyDataSourceId);
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
		rptColItemValues[4] = cbxDataSource;

		ComboBox cbxDataFieldSource = new ComboBox("");
		cbxDataFieldSource.setStyleName("tiny");
		cbxDataFieldSource.setHeight(20, Unit.PIXELS);
		cbxDataFieldSource.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[5] = cbxDataFieldSource;
		
		CheckBox chxMappedData = new CheckBox();
		chxMappedData.setStyleName("tiny");
		chxMappedData.setVisible(true);
		chxMappedData.setValue(true);
		chxMappedData.setReadOnly(true);
		chxMappedData.setHeight(20, Unit.PIXELS);
		chxMappedData.setWidth(100, Unit.PERCENTAGE);
		rptColItemValues[6] = chxMappedData;	
		
		
		tblVwVerticalRptCols.addItem(rptColItemValues, rptColumnId);
		tblVwVerticalRptCols.select(rptColumnId);		
		tblVwVerticalRptCols.setParent(rptColumnId, parentRptColumnId);
		
		if(cbxDataSourceFieldValue != null)
			cbxDataFieldSource.setValue(cbxDataSourceFieldValue);
		
	}
	
	private void refreshChildColNameTblVwVerticalRptCols(TextField txtColName)
	{
		Integer parentItemId = Integer.parseInt(txtColName.getId());
	
		for (Object rptColItemId: tblVwVerticalRptCols.getContainerDataSource().getItemIds()) 
		{
			if(rptColItemId.toString().equals(parentItemId.toString()))
			{
				Item tblVwVerticalParentRptColRowItem = tblVwVerticalRptCols.getContainerDataSource().getItem(rptColItemId);
				
				if(tblVwVerticalParentRptColRowItem == null)
					return;
				
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
							fillCbxDataSourceFields(cbxDataType.getValue().toString(), cbxDataSourceField, Integer.parseInt(cbxDataSource.getValue().toString().substring(4)), null, null, null, null);
							break;

						case "fpy_":
							fillCbxDataSourceFields(cbxDataType.getValue().toString(), cbxDataSourceField, null, Integer.parseInt(cbxDataSource.getValue().toString().substring(4)), null, null, null);
							break;

						case "fny_":
							fillCbxDataSourceFields(cbxDataType.getValue().toString(), cbxDataSourceField, null, null, Integer.parseInt(cbxDataSource.getValue().toString().substring(4)), null, null);
							break;
	
						case "fty_":
							fillCbxDataSourceFields(cbxDataType.getValue().toString(), cbxDataSourceField, null, null, null, Integer.parseInt(cbxDataSource.getValue().toString().substring(4)), null);
							break;
	
						case "tgt_":
							fillCbxDataSourceFields(cbxDataType.getValue().toString(), cbxDataSourceField, null, null, null, null, Integer.parseInt(cbxDataSource.getValue().toString().substring(4)));
							break;
							
					}
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
		}
		
		if(fnyDataSourceId != null)
		{
			List<FinalPassYieldInfoField> fnyFields = new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldByReportById(fnyDataSourceId);
			for(int i=0; fnyFields != null && i<fnyFields.size(); i++)
				addItemIfTypeMatches(dataFieldType, "fnyfield_" + fnyFields.get(i).getFnyInfoFieldId(), fnyFields.get(i).getFnyInfoFieldLabel(), fnyFields.get(i).getExperimentField().getExpFieldType(), cbxDataSourceFields);
	
			addItemIfTypeMatches(dataFieldType, "fny_datetime", "Datetime (FNY)", "datetime", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fny_sn", "Serial Number (FNY)", "nvarchar(max)", cbxDataSourceFields);
			addItemIfTypeMatches(dataFieldType, "fny_result", "Result (FNY)", "nvarchar(max)", cbxDataSourceFields);
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
			//Validate Report Columns Added and Key selected
			Collection rptColumnsItemIdsToValidate = this.tblVwVerticalRptCols.getContainerDataSource().getItemIds();
			
			for (Object rptColumnsItemIdObj : rptColumnsItemIdsToValidate) 
			{
				rptColumnsAdded = true;
				Item tblRptColumnRowItem = this.tblVwVerticalRptCols.getContainerDataSource().getItem(rptColumnsItemIdObj);
				Boolean rptColumnIsKey = ((CheckBox)(tblRptColumnRowItem.getItemProperty("Key").getValue())).getValue();
				
				if(rptColumnIsKey)
				{
					isKeySelected = true;
					totalKeysSelected++;
				}
				
			}
			
			if(!rptColumnsAdded)
			{
				this.getUI().showNotification("No columns added to report.", Type.WARNING_MESSAGE);
				return;
			}
			
			if(!isKeySelected)
			{

				this.getUI().showNotification("Key field must be selected.", Type.WARNING_MESSAGE);
				return;
			}
			
			if(totalKeysSelected > 1)
			{
				this.getUI().showNotification("Only 1 key field is allowed.", Type.WARNING_MESSAGE);
				return;
			}
			
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
			
			ViewVerticalReport vwVerticalRpt = new ViewVerticalReport();
			
			if(isNewRecord)
			{
				vwVerticalRpt.setVwVerticalRptDbTableNameId("vwvertical#" + this.txtVwVerticalRptCustomId.getValue());
				vwVerticalRpt.setVwVerticalRptName(this.txtVwVerticalRptName.getValue());
				vwVerticalRpt.setVwVerticalRptDescription(this.txtVwVerticalDescription.getValue());
				vwVerticalRpt.setVwVerticalRptIsActive(true);
			
				new ViewVerticalReportDao().addVwVerticalReport(vwVerticalRpt);
			}
			else
			{				
				this.savedVwVerticalRpt.setVwVerticalRptDbTableNameId("vwvertical#" + this.txtVwVerticalRptCustomId.getValue());
				this.savedVwVerticalRpt.setVwVerticalRptName(this.txtVwVerticalRptName.getValue());
				this.savedVwVerticalRpt.setVwVerticalRptDescription(this.txtVwVerticalDescription.getValue());
				this.savedVwVerticalRpt.setVwVerticalRptIsActive(true);			
				new ViewVerticalReportDao().updateVwVerticalReport(this.savedVwVerticalRpt);
				vwVerticalRpt = new ViewVerticalReportDao().getVwVerticalRptById(this.savedVwVerticalRpt.getVwVerticalRptId());
			}
			
			
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
								
								String filterOperator = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
								GridLayout gridFilterValues = (GridLayout)tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue();
								
								if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
									vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldValue1(txtFilterValue1.getValue().toString());
									vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldValue2(txtFilterValue2.getValue().toString());
								}
								else if(filterOperator.equals("customrange"))
								{
									DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
									DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
									vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldValue1(new SimpleDateFormat("yyyyMMdd").format(fromDateField.getValue()));
									vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldValue2(new SimpleDateFormat("yyyyMMdd").format(toDateField.getValue()));
								}
								else if(filterOperator.equals("after") || filterOperator.equals("before")
					    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
					    				|| filterOperator.equals("onorbefore"))
								{
									DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
									vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldValue1(new SimpleDateFormat("yyyyMMdd").format(dtFilterValue.getValue()));									
								}
								else
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldValue1(txtFilterValue1.getValue());
								
									if(txtFilterValue1.getValue() == null || txtFilterValue1.getValue() == "")
										vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
								}
								
								//vwVerticalRptFilterByExpField.setVwVerticalRptFilteByExpFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
								//vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
								
								if(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue() != null)
									vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
								else
									vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldExpression("and");
									
								vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
							
								
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
							
								String filterOperator = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
								GridLayout gridFilterValues = (GridLayout)tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue();
								
								if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
									vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldValue1(txtFilterValue1.getValue().toString());
									vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldValue2(txtFilterValue2.getValue().toString());
								}
								else if(filterOperator.equals("customrange"))
								{
									DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
									DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
									vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldValue1(new SimpleDateFormat("yyyyMMdd").format(fromDateField.getValue()));
									vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldValue2(new SimpleDateFormat("yyyyMMdd").format(toDateField.getValue()));
									
								}
								else if(filterOperator.equals("after") || filterOperator.equals("before")
					    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
					    				|| filterOperator.equals("onorbefore"))
								{
									DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
									vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldValue1(new SimpleDateFormat("yyyyMMdd").format(dtFilterValue.getValue()));									
								}
								else
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldValue1(txtFilterValue1.getValue());

									if(txtFilterValue1.getValue() == null || txtFilterValue1.getValue() == "")
										vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
								
								}
								
								//vwVerticalRptFilterByFpyField.setVwVerticalRptFilteByFpyFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
								//vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
								if(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue() != null)
									vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
								else
									vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldExpression("and");
								
								vwVerticalRptFilterByFpyField.setVwVerticalRptFilterByFpyFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
							
								//if(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == null || ((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == "")
								//	vwVerticalRptFilterByFpyField.setVwVerticalRptFilteByFpyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
							
								new ViewVerticalReportFilterByFpyFieldDao().addVwVerticalReportFilterByFpyField(vwVerticalRptFilterByFpyField);
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
						ViewVerticalReportByFnyRpt vwVerticalRptByFnyRpt = new ViewVerticalReportByFnyRpt();
						vwVerticalRptByFnyRpt.setFnyRpt(activeFnyReports.get(i));
						vwVerticalRptByFnyRpt.setViewVerticalReport(vwVerticalRpt);
						new ViewVerticalReportByFnyRptDao().addVwVerticalReportByFnyRpt(vwVerticalRptByFnyRpt);
						
						//Save filters attached to FPY
						Collection dataSourceFiltersItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
						
						for (Object dataSourceFiltersItemIdObj : dataSourceFiltersItemIds) 
						{
							Item tblDataSourceFilterRowItem = this.tblReportFilters.getContainerDataSource().getItem(dataSourceFiltersItemIdObj);
							String dataSourceId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Data Source").getValue())).getValue().toString();
							if(("fny_" + selectedFnyReportId).equals(dataSourceId))
							{
								ViewVerticalReportFilterByFnyField vwVerticalRptFilterByFnyField = new ViewVerticalReportFilterByFnyField();
								vwVerticalRptFilterByFnyField.setVwVerticalFnyRpt(vwVerticalRptByFnyRpt);
							
								String fnyInfoFieldStrId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString();
								if(fnyInfoFieldStrId.equals("fny_datetime") ||fnyInfoFieldStrId.equals("fny_sn") ||fnyInfoFieldStrId.equals("fny_result"))
								{
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyIsDateTimeExpField(fnyInfoFieldStrId.equals("fny_datetime"));
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyIsResultExpField(fnyInfoFieldStrId.equals("fny_result"));
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnySNExpField(fnyInfoFieldStrId.equals("fny_sn"));
								}
								else
									vwVerticalRptFilterByFnyField.setFnyInfoField(new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldById(Integer.parseInt(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString().substring(4))));
							
								String filterOperator = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
								GridLayout gridFilterValues = (GridLayout)tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue();
								
								if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldValue1(txtFilterValue1.getValue().toString());
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldValue2(txtFilterValue2.getValue().toString());
								}
								else if(filterOperator.equals("customrange"))
								{
									DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
									DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldValue1(new SimpleDateFormat("yyyyMMdd").format(fromDateField.getValue()));
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldValue2(new SimpleDateFormat("yyyyMMdd").format(toDateField.getValue()));
									
								}
								else if(filterOperator.equals("after") || filterOperator.equals("before")
					    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
					    				|| filterOperator.equals("onorbefore"))
								{
									DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldValue1(dtFilterValue.getValue().toString());									
								}
								else
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldValue1(txtFilterValue1.getValue());
								
									if(txtFilterValue1.getValue() == null || txtFilterValue1.getValue() == "")
										vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
								}
								
								
								//vwVerticalRptFilterByFnyField.setVwVerticalRptFilteByFnyFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
								//vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
								if(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue() != null)
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
								else
									vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldExpression("and");
								
								vwVerticalRptFilterByFnyField.setVwVerticalRptFilterByFnyFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
							
								//if(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == null || ((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == "")
								//	vwVerticalRptFilterByFnyField.setVwVerticalRptFilteByFnyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
							
								new ViewVerticalReportFilterByFnyFieldDao().addVwVerticalReportFilterByFnyField(vwVerticalRptFilterByFnyField);
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
							
								String filterOperator = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
								GridLayout gridFilterValues = (GridLayout)tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue();
								
								if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
									vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldValue1(txtFilterValue1.getValue().toString());
									vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldValue2(txtFilterValue2.getValue().toString());
								}
								else if(filterOperator.equals("customrange"))
								{
									DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
									DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
									vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldValue1(new SimpleDateFormat("yyyyMMdd").format(fromDateField.getValue()));
									vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldValue2(new SimpleDateFormat("yyyyMMdd").format(toDateField.getValue()));
									
								}
								else if(filterOperator.equals("after") || filterOperator.equals("before")
					    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
					    				|| filterOperator.equals("onorbefore"))
								{
									DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
									vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldValue1(new SimpleDateFormat("yyyyMMdd").format(dtFilterValue.getValue()));									
								}
								else
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldValue1(txtFilterValue1.getValue());
								
									if(txtFilterValue1.getValue() == null || txtFilterValue1.getValue() == "")
										vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
								}
								
								//vwVerticalRptFilterByFtyField.setVwVerticalRptFilteByFtyFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
								//vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
								
								if(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue() != null)
									vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
								else
									vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldExpression("and");
								
	
								vwVerticalRptFilterByFtyField.setVwVerticalRptFilterByFtyFieldOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
							
								//if(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == null || ((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue() == "")
								//	vwVerticalRptFilterByFtyField.setVwVerticalRptFilteByFtyFieldValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
							
								new ViewVerticalReportFilterByFtyFieldDao().addVwVerticalReportFilterByFtyField(vwVerticalRptFilterByFtyField);
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
						ViewVerticalReportByTargetRpt vwVerticalRptByTargetRpt = new ViewVerticalReportByTargetRpt();
						vwVerticalRptByTargetRpt.setTargetRpt(activeTargetReports.get(i));
						vwVerticalRptByTargetRpt.setViewVerticalReport(vwVerticalRpt);
						new ViewVerticalReportByTargetRptDao().addVwVerticalReportByTargetRpt(vwVerticalRptByTargetRpt);
						
						//Save filters attached to FTY
						Collection dataSourceFiltersItemIds = this.tblReportFilters.getContainerDataSource().getItemIds();
						
						for (Object dataSourceFiltersItemIdObj : dataSourceFiltersItemIds) 
						{
							Item tblDataSourceFilterRowItem = this.tblReportFilters.getContainerDataSource().getItem(dataSourceFiltersItemIdObj);
							String dataSourceId = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Data Source").getValue())).getValue().toString();
							if(("tgt_" + selectedTargetReportId).equals(dataSourceId))
							{
								ViewVerticalReportFilterByTargetColumn vwVerticalRptFilterByTgtCol = new ViewVerticalReportFilterByTargetColumn();
								vwVerticalRptFilterByTgtCol.setVwVerticalTargetRpt(vwVerticalRptByTargetRpt);
								vwVerticalRptFilterByTgtCol.setTargetColumn(new TargetColumnDao().getTargetColumnById(Integer.parseInt(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Source Column/Field").getValue())).getValue().toString().substring(4))));
								
								String filterOperator = ((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString();
								GridLayout gridFilterValues = (GridLayout)tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue();
								
								if(filterOperator.equals("between") || filterOperator.equals("notbetween"))
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									TextField txtFilterValue2 = (TextField)gridFilterValues.getComponent(1, 0);
									vwVerticalRptFilterByTgtCol.setVwVerticalRptFilterByTargetColumnValue1(txtFilterValue1.getValue().toString());
									vwVerticalRptFilterByTgtCol.setVwVerticalRptFilterByTargetColumnValue2(txtFilterValue2.getValue().toString());
								}
								else if(filterOperator.equals("customrange"))
								{
									DateField fromDateField = (DateField)gridFilterValues.getComponent(0, 0);
									DateField toDateField = (DateField)gridFilterValues.getComponent(1, 0);
									vwVerticalRptFilterByTgtCol.setVwVerticalRptFilterByTargetColumnValue1(new SimpleDateFormat("yyyyMMdd").format(fromDateField.getValue()));
									vwVerticalRptFilterByTgtCol.setVwVerticalRptFilterByTargetColumnValue2(new SimpleDateFormat("yyyyMMdd").format(toDateField.getValue()));
								}
								else if(filterOperator.equals("after") || filterOperator.equals("before")
					    				|| filterOperator.equals("on") || filterOperator.equals("onorafter")
					    				|| filterOperator.equals("onorbefore"))
								{
									DateField dtFilterValue = (DateField)gridFilterValues.getComponent(0, 0);
									vwVerticalRptFilterByTgtCol.setVwVerticalRptFilterByTargetColumnValue1(new SimpleDateFormat("yyyyMMdd").format(dtFilterValue.getValue()));									
								}
								else
								{
									TextField txtFilterValue1 = (TextField)gridFilterValues.getComponent(0, 0);
									vwVerticalRptFilterByTgtCol.setVwVerticalRptFilterByTargetColumnValue1(txtFilterValue1.getValue());
								
									if(txtFilterValue1.getValue() == null || txtFilterValue1.getValue() == "")
										vwVerticalRptFilterByTgtCol.setVwVerticalRptFilterByTargetColumnValue1(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Custom List Value").getValue())).getValue().toString());
								}
								
								//vwVerticalRptFilterByExpField.setVwVerticalRptFilteByExpFieldValue1(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value").getValue())).getValue());
								//vwVerticalRptFilterByExpField.setVwVerticalRptFilterByExpFieldValue2(((TextField)(tblDataSourceFilterRowItem.getItemProperty("Filter Value 2").getValue())).getValue());
								
								if(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue() != null)
									vwVerticalRptFilterByTgtCol.setVwVerticalRptFilterByTargetColumnExpression(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Expression").getValue())).getValue().toString());
								else
									vwVerticalRptFilterByTgtCol.setVwVerticalRptFilterByTargetColumnExpression("and");
									
								vwVerticalRptFilterByTgtCol.setVwVerticalRptFilterByTargetColumnOperation(((ComboBox)(tblDataSourceFilterRowItem.getItemProperty("Filter Operator").getValue())).getValue().toString());
							
								
								new ViewVerticalReportFilterByTargetColumnDao().addVwVerticalReportFilterByTargetColumn(vwVerticalRptFilterByTgtCol);
							}
						}
						
						
						break;
					}
				}
			}
				
			//Load report columns into Matrix to be reused in Enrichment
			List<String> vwEnrichmentRptColumnId = new ArrayList<String>(); 
			List<ViewVerticalReportColumn> vwEnrichmentRptColumn = new ArrayList<ViewVerticalReportColumn>(); 
			
			
			//Save report columns
			Collection rptColumnsItemIds = this.tblVwVerticalRptCols.getContainerDataSource().getItemIds();
			
			for (Object rptColumnsItemIdObj : rptColumnsItemIds) 
			{
				Item tblRptColumnRowItem = this.tblVwVerticalRptCols.getContainerDataSource().getItem(rptColumnsItemIdObj);
				
				rptColumnsAdded = true;
				
				if (tblVwVerticalRptCols.hasChildren(rptColumnsItemIdObj))
				{
					//Save report columns
					String rptColumnName = ((TextField)(tblRptColumnRowItem.getItemProperty("Column Name").getValue())).getValue();
					String rptDBColumnName = ((TextField)(tblRptColumnRowItem.getItemProperty("DB Column Name").getValue())).getValue();
					String rptColumnDataType = ((ComboBox)(tblRptColumnRowItem.getItemProperty("Data Type").getValue())).getValue().toString();
					Boolean rptColumnIsKey = ((CheckBox)(tblRptColumnRowItem.getItemProperty("Key").getValue())).getValue();
					
					ViewVerticalReportColumn vwRptColumn =  new ViewVerticalReportColumn();
					vwRptColumn.setViewVerticalReport(vwVerticalRpt);
					vwRptColumn.setVwVerticalRptColumnIsKey(rptColumnIsKey);
					vwRptColumn.setVwVerticalRptColumnName(rptColumnName);
					vwRptColumn.setVwVerticalRptColumnDbId(rptDBColumnName);
					vwRptColumn.setVwVerticalRptColumnDataType(rptColumnDataType);
					
					new ViewVerticalReportColumnDao().addVwVerticalReportColumn(vwRptColumn);
					
					vwEnrichmentRptColumnId.add(rptDBColumnName);
					vwEnrichmentRptColumn.add(vwRptColumn);
					
					//Save linked sources of column
					for (Object rptChildColItemId: tblVwVerticalRptCols.getChildren(rptColumnsItemIdObj)) 
					{
						Item tblVwVerticalChildRptColRowItem = tblVwVerticalRptCols.getContainerDataSource().getItem(rptChildColItemId);
						String dataSourceStrId = ((ComboBox)(tblVwVerticalChildRptColRowItem.getItemProperty("Data Source Table/Report").getValue())).getValue().toString();
						
						if(((ComboBox)(tblVwVerticalChildRptColRowItem.getItemProperty("Data Source Field").getValue())).getValue() != null)
						{
							String dataSourceFieldStrId = ((ComboBox)(tblVwVerticalChildRptColRowItem.getItemProperty("Data Source Field").getValue())).getValue().toString();
							
							if(dataSourceStrId.startsWith("exp"))
							{
								Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldStrId.substring(9));
								ViewVerticalReportColumnByExpField vwVerticalRptColByExpField = new ViewVerticalReportColumnByExpField();
								vwVerticalRptColByExpField.setExperimentField(new ExperimentFieldDao().getExperimentFieldById(dataSourceFieldId));
								vwVerticalRptColByExpField.setVwVerticalReportColumn(vwRptColumn);
								new ViewVerticalReportColumnByExpFieldDao().addVwVerticalReportColumnByExpField(vwVerticalRptColByExpField);
							}	
		
							if(dataSourceStrId.startsWith("fpy"))
							{
								ViewVerticalReportColumnByFpyField vwVerticalRptColByFpyField = new ViewVerticalReportColumnByFpyField();
								vwVerticalRptColByFpyField.setFpyRpt(new FirstPassYieldReportDao().getFirstPassYieldReportById(Integer.parseInt(dataSourceStrId.substring(4))));
								vwVerticalRptColByFpyField.setVwVerticalRptColumnByFpyIsSNExpField(false);
								vwVerticalRptColByFpyField.setVwVerticalRptColumnByFpyIsResultExpField(false);
								vwVerticalRptColByFpyField.setVwVerticalRptColumnByFpyIsDateTimeExpField(false);
								if("fpy_sn".equals(dataSourceFieldStrId) || "fpy_result".equals(dataSourceFieldStrId) || "fpy_datetime".equals(dataSourceFieldStrId))
								{
									vwVerticalRptColByFpyField.setVwVerticalRptColumnByFpyIsSNExpField("fpy_sn".equals(dataSourceFieldStrId));
									vwVerticalRptColByFpyField.setVwVerticalRptColumnByFpyIsResultExpField("fpy_result".equals(dataSourceFieldStrId));
									vwVerticalRptColByFpyField.setVwVerticalRptColumnByFpyIsDateTimeExpField("fpy_datetime".equals(dataSourceFieldStrId));
								}
								else
								{
									int selectedFpyFieldId = Integer.parseInt(dataSourceFieldStrId.substring(9));				
									vwVerticalRptColByFpyField.setFirstPassYieldInfoField(new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldById(selectedFpyFieldId));
								}
								vwVerticalRptColByFpyField.setVwVerticalReportColumn(vwRptColumn);
								new ViewVerticalReportColumnByFpyFieldDao().addVwVerticalReportColumnByFpyField(vwVerticalRptColByFpyField);		
							}
		
							if(dataSourceStrId.startsWith("fny"))
							{
								ViewVerticalReportColumnByFnyField vwVerticalRptColByFnyField = new ViewVerticalReportColumnByFnyField();
								vwVerticalRptColByFnyField.setFnyRpt(new FinalPassYieldReportDao().getFinalPassYieldReportById(Integer.parseInt(dataSourceStrId.substring(4))));
								vwVerticalRptColByFnyField.setVwVerticalRptColumnByFnyIsSNExpField(false);
								vwVerticalRptColByFnyField.setVwVerticalRptColumnByFnyIsResultExpField(false);
								vwVerticalRptColByFnyField.setVwVerticalRptColumnByFnyIsDateTimeExpField(false);
								if("fny_sn".equals(dataSourceFieldStrId) || "fny_result".equals(dataSourceFieldStrId) || "fny_datetime".equals(dataSourceFieldStrId))
								{
									vwVerticalRptColByFnyField.setVwVerticalRptColumnByFnyIsSNExpField("fny_sn".equals(dataSourceFieldStrId));
									vwVerticalRptColByFnyField.setVwVerticalRptColumnByFnyIsResultExpField("fny_result".equals(dataSourceFieldStrId));
									vwVerticalRptColByFnyField.setVwVerticalRptColumnByFnyIsDateTimeExpField("fny_datetime".equals(dataSourceFieldStrId));
								}
								else
								{
									int selectedFnyFieldId = Integer.parseInt(dataSourceFieldStrId.substring(9));				
									vwVerticalRptColByFnyField.setFinalPassYieldInfoField(new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldById(selectedFnyFieldId));
								}
								vwVerticalRptColByFnyField.setVwVerticalReportColumn(vwRptColumn);
								new ViewVerticalReportColumnByFnyFieldDao().addVwVerticalReportColumnByFnyField(vwVerticalRptColByFnyField);			
							}	
		
							if(dataSourceStrId.startsWith("fty"))
							{
								ViewVerticalReportColumnByFtyField vwVerticalRptColByFtyField = new ViewVerticalReportColumnByFtyField();
								vwVerticalRptColByFtyField.setFtyRpt(new FirstTimeYieldReportDao().getFirstTimeYieldReportById(Integer.parseInt(dataSourceStrId.substring(4))));
								vwVerticalRptColByFtyField.setVwVerticalRptColumnByFtyIsSNExpField(false);
								vwVerticalRptColByFtyField.setVwVerticalRptColumnByFtyIsResultExpField(false);
								vwVerticalRptColByFtyField.setVwVerticalRptColumnByFtyIsDateTimeExpField(false);
						
								if("fty_sn".equals(dataSourceFieldStrId) || "fty_result".equals(dataSourceFieldStrId) || "fty_datetime".equals(dataSourceFieldStrId))
								{
									vwVerticalRptColByFtyField.setVwVerticalRptColumnByFtyIsSNExpField("fty_sn".equals(dataSourceFieldStrId));
									vwVerticalRptColByFtyField.setVwVerticalRptColumnByFtyIsResultExpField("fty_result".equals(dataSourceFieldStrId));
									vwVerticalRptColByFtyField.setVwVerticalRptColumnByFtyIsDateTimeExpField("fty_datetime".equals(dataSourceFieldStrId));
								}
								else
								{
									int selectedFtyFieldId = Integer.parseInt(dataSourceFieldStrId.substring(9));				
									vwVerticalRptColByFtyField.setFirstTimeYieldInfoField(new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldById(selectedFtyFieldId));
								}
								vwVerticalRptColByFtyField.setVwVerticalReportColumn(vwRptColumn);
								new ViewVerticalReportColumnByFtyFieldDao().addVwVerticalReportColumnByFtyField(vwVerticalRptColByFtyField);			
							}			
							
							if(dataSourceStrId.startsWith("tgt"))
							{
								Integer dataSourceFieldId = Integer.parseInt(dataSourceFieldStrId.substring(9));
								ViewVerticalReportColumnByTargetColumn vwVerticalRptColByTargetColumn = new ViewVerticalReportColumnByTargetColumn();
								vwVerticalRptColByTargetColumn.setTargetColumn(new TargetColumnDao().getTargetColumnById(dataSourceFieldId));
								vwVerticalRptColByTargetColumn.setVwVerticalReportColumn(vwRptColumn);
								new ViewVerticalReportColumnByTargetColumnDao().addVwVerticalReportColumnByTargetColumn(vwVerticalRptColByTargetColumn);	
							}
						}									
					}	
				}
				
			}
			
			//Enrichment Rules
			Collection enrichmentRuleItemIds = this.tblColumnsEnrichment.getContainerDataSource().getItemIds();
			
			ViewVerticalReportColumnByEnrichmentDao vwVertRptColEnrichmentDao = new ViewVerticalReportColumnByEnrichmentDao();
			
			for (Object enrichmentRuleItemIdObj : enrichmentRuleItemIds) 
			{
				int enrichmentRuleItemId = (int)enrichmentRuleItemIdObj;
				Item tblEnrichmentRuleRowItem = this.tblColumnsEnrichment.getContainerDataSource().getItem(enrichmentRuleItemId);
				
				ViewVerticalReportColumnByEnrichment vwVertRptColByEnrichment = new ViewVerticalReportColumnByEnrichment();
				Integer vwEnrichmentRptColumnIndex = vwEnrichmentRptColumnId.indexOf(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("Report Column").getValue())).getValue().toString());
				vwVertRptColByEnrichment.setVwVerticalReportColumn(vwEnrichmentRptColumn.get(vwEnrichmentRptColumnIndex));
				vwVertRptColByEnrichment.setVwVerticalRptColumnEnrichmentOperation(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("Operator").getValue())).getValue().toString());			
				vwVertRptColByEnrichment.setVwVerticalRptColumnEnrichmentValue(((TextField)(tblEnrichmentRuleRowItem.getItemProperty("Value").getValue())).getValue().toString());			
				vwVertRptColByEnrichment.setVwVerticalRptColumnEnrichmentType((((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("Enrichment Type").getValue())).getValue().toString()));			
				
				String customListValueStrId = "";
				if(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("List Value").getValue())).getValue() != null)
					customListValueStrId = ((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("List Value").getValue())).getValue().toString();
	
				if(!customListValueStrId.isEmpty())
					vwVertRptColByEnrichment.setCustomListValue((new CustomListValueDao().getCustomListValueById(Integer.parseInt(customListValueStrId))));			
				
				vwVertRptColByEnrichment.setVwVerticalRptColumnEnrichmentStaticValue(((TextField)(tblEnrichmentRuleRowItem.getItemProperty("Static Value").getValue())).getValue().toString());			
				
				vwVertRptColEnrichmentDao.addVwVerticalReportColumnByEnrichment(vwVertRptColByEnrichment);
			}
						
			if(isNewRecord)
			{
				WebApplication webApp = (WebApplication)this.getParent().getParent();
				webApp.reloadMainForm("vertical view report");
			}
			else
			{
				ViewVerticalReportColumnByEnrichmentDao vwVerticalRptColEnrichmentDao = new ViewVerticalReportColumnByEnrichmentDao();
				for(int i=0; vwVerticalRptColumnsByEnrichment != null &&i<vwVerticalRptColumnsByEnrichment.size(); i++)
					vwVerticalRptColEnrichmentDao.deleteVwVerticalReportColumnByEnrichment(vwVerticalRptColumnsByEnrichment.get(i).getVwVerticalRptColumnByEnrichmentId()); 
				
				ViewVerticalReportColumnByExpFieldDao vwVerticalRptColumnByExpFieldDao = new ViewVerticalReportColumnByExpFieldDao();
				for(int i=0; vwVerticalRptColumnsByExpField != null &&i<vwVerticalRptColumnsByExpField.size(); i++)
					vwVerticalRptColumnByExpFieldDao.deleteVwVerticalReportColumnByExpField(vwVerticalRptColumnsByExpField.get(i).getVwVerticalRptColumnByExpFieldId());
				
				ViewVerticalReportColumnByFpyFieldDao vwVerticalRptColumnByFpyFieldDao = new ViewVerticalReportColumnByFpyFieldDao();
				for(int i=0; vwVerticalRptColumnsByFpyField != null &&i<vwVerticalRptColumnsByFpyField.size(); i++)
					vwVerticalRptColumnByFpyFieldDao.deleteVwVerticalReportColumnByFpyField(vwVerticalRptColumnsByFpyField.get(i).getVwVerticalRptColumnByFpyFieldId());
				
				ViewVerticalReportColumnByFnyFieldDao vwVerticalRptColumnByFnyFieldDao = new ViewVerticalReportColumnByFnyFieldDao();
				for(int i=0; vwVerticalRptColumnsByFnyField != null &&i<vwVerticalRptColumnsByFnyField.size(); i++)
					vwVerticalRptColumnByFnyFieldDao.deleteVwVerticalReportColumnByFnyField(vwVerticalRptColumnsByFnyField.get(i).getVwVerticalRptColumnByFnyFieldId());
				
				ViewVerticalReportColumnByFtyFieldDao vwVerticalRptColumnByFtyFieldDao = new ViewVerticalReportColumnByFtyFieldDao();
				for(int i=0; vwVerticalRptColumnsByFtyField != null &&i<vwVerticalRptColumnsByFtyField.size(); i++)
					vwVerticalRptColumnByFtyFieldDao.deleteVwVerticalReportColumnByFtyField(vwVerticalRptColumnsByFtyField.get(i).getVwVerticalRptColumnByFtyFieldId());
				
				ViewVerticalReportColumnByTargetColumnDao vwVerticalReportColumnByTargetColumnDao = new ViewVerticalReportColumnByTargetColumnDao();
				for(int i=0; vwVerticalRptColumnsByTgtCol != null &&i<vwVerticalRptColumnsByTgtCol.size(); i++)
					vwVerticalReportColumnByTargetColumnDao.deleteVwVerticalReportColumnByTargetColumn(vwVerticalRptColumnsByTgtCol.get(i).getVwVerticalRptColumnByTargetColumnId());
				
				ViewVerticalReportFilterByExpFieldDao vwVerticalReportFilterByExpFieldDao = new ViewVerticalReportFilterByExpFieldDao();
				for(int i=0; vwVerticalRptFiltersByExpField != null &&i<vwVerticalRptFiltersByExpField.size(); i++)
					vwVerticalReportFilterByExpFieldDao.deleteVwVerticalReportFilterByExpField(vwVerticalRptFiltersByExpField.get(i).getVwVerticalRptFilterByExpFieldId());
				
				ViewVerticalReportFilterByFpyFieldDao vwVerticalReportFilterByFpyFieldDao = new ViewVerticalReportFilterByFpyFieldDao();
				for(int i=0; vwVerticalRptFiltersByFpyField != null &&i<vwVerticalRptFiltersByFpyField.size(); i++)
					vwVerticalReportFilterByFpyFieldDao.deleteVwVerticalReportFilterByFpyField(vwVerticalRptFiltersByFpyField.get(i).getVwVerticalRptFilterByFpyFieldId());
				
				ViewVerticalReportFilterByFnyFieldDao vwVerticalReportFilterByFnyFieldDao = new ViewVerticalReportFilterByFnyFieldDao();
				for(int i=0; vwVerticalRptFiltersByFnyField != null &&i<vwVerticalRptFiltersByFnyField.size(); i++)
					vwVerticalReportFilterByFnyFieldDao.deleteVwVerticalReportFilterByFnyField(vwVerticalRptFiltersByFnyField.get(i).getVwVerticalRptFilterByFnyFieldId());
				
				ViewVerticalReportFilterByFtyFieldDao vwVerticalReportFilterByFtyFieldDao = new ViewVerticalReportFilterByFtyFieldDao();
				for(int i=0; vwVerticalRptFiltersByFtyField != null &&i<vwVerticalRptFiltersByFtyField.size(); i++)
					vwVerticalReportFilterByFtyFieldDao.deleteVwVerticalReportFilterByFtyField(vwVerticalRptFiltersByFtyField.get(i).getVwVerticalRptFilterByFtyFieldId());
				
				ViewVerticalReportFilterByTargetColumnDao vwVerticalReportFilterByTargetColumnDao = new ViewVerticalReportFilterByTargetColumnDao();
				for(int i=0; vwVerticalRptFiltersByTgtCol != null &&i<vwVerticalRptFiltersByTgtCol.size(); i++)
					vwVerticalReportFilterByTargetColumnDao.deleteVwVerticalReportFilterByTargetColumn(vwVerticalRptFiltersByTgtCol.get(i).getVwVerticalRptFilterByTargetColumnId());
				
				ViewVerticalReportColumnDao vwVerticalReportColumnDao = new ViewVerticalReportColumnDao();
				for(int i=0; vwVerticalRptColumns != null && i<vwVerticalRptColumns.size(); i++)
					vwVerticalReportColumnDao.deleteVwVerticalReportColumn(vwVerticalRptColumns.get(i).getVwVerticalRptColumnId());
				
				ViewVerticalReportByExperimentDao vwVerticalReportByExperimentDao = new ViewVerticalReportByExperimentDao();
				for(int i=0; vwVerticalRptByExperimentList != null && i<vwVerticalRptByExperimentList.size(); i++)
					vwVerticalReportByExperimentDao.deleteVwVerticalReportByExperiment(vwVerticalRptByExperimentList.get(i).getVwVerticalRptByExperimentId());

				ViewVerticalReportByFpyRptDao vwVerticalReportByFpyRptDao = new ViewVerticalReportByFpyRptDao();
				for(int i=0; vwVerticalRptByFpyRptList != null && i<vwVerticalRptByFpyRptList.size(); i++)
					vwVerticalReportByFpyRptDao.deleteVwVerticalReportByFpyRpt(vwVerticalRptByFpyRptList.get(i).getVwVerticalRptByFpyId());

				ViewVerticalReportByFtyRptDao vwVerticalReportByFtyRptDao = new ViewVerticalReportByFtyRptDao();
				for(int i=0; vwVerticalRptByFtyRptList != null && i<vwVerticalRptByFtyRptList.size(); i++)
					vwVerticalReportByFtyRptDao.deleteVwVerticalReportByFtyRpt(vwVerticalRptByFtyRptList.get(i).getVwVerticalRptByFtyId());
				
				ViewVerticalReportByFnyRptDao vwVerticalReportByFnyRptDao = new ViewVerticalReportByFnyRptDao();
				for(int i=0; vwVerticalRptByFnyRptList != null && i<vwVerticalRptByFnyRptList.size(); i++)
					vwVerticalReportByFnyRptDao.deleteVwVerticalReportByFnyRpt(vwVerticalRptByFnyRptList.get(i).getVwVerticalRptByFnyId());
				
				ViewVerticalReportByTargetRptDao vwVerticalReportByTargetRptDao = new ViewVerticalReportByTargetRptDao();
				for(int i=0; vwVerticalRptByTgtRptList != null && i<vwVerticalRptByTgtRptList.size(); i++)
					vwVerticalReportByTargetRptDao.deleteVwVerticalReportByTargetRpt(vwVerticalRptByTgtRptList.get(i).getVwVerticalRptByTargetRptId());
				
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
		if(!this.txtVwVerticalRptName.isValid() || !this.txtVwVerticalRptCustomId.isValid())
			return false;		
		
		Collection itemIds = this.tblVwVerticalRptCols.getContainerDataSource().getItemIds();
		if(itemIds != null)
		{
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblVwVerticalRptCols.getContainerDataSource().getItem(itemId);
	
				if(((TextField)(tblRowItem.getItemProperty("Column Name").getValue())).getValue() == null)
					return false;
				if(((TextField)(tblRowItem.getItemProperty("DB Column Name").getValue())).getValue() == null)
					return false;
				if(((ComboBox)(tblRowItem.getItemProperty("Data Type").getValue())).getValue() == null)
					return false;
			}
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
		Collection itemIds = this.tblVwVerticalRptCols.getContainerDataSource().getItemIds();
		List<String> addedReportColumnNames = new ArrayList<String>(); 
		List<String> addedReportColumnDBNames = new ArrayList<String>();
		
		for (Object itemIdObj : itemIds) 
		{
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblVwVerticalRptCols.getContainerDataSource().getItem(itemId);
			
			if (tblVwVerticalRptCols.hasChildren(itemIdObj))
			{
				if(((TextField)(tblRowItem.getItemProperty("Column Name").getValue())).getValue() != null &&
						addedReportColumnNames.indexOf(((TextField)(tblRowItem.getItemProperty("Column Name").getValue())).getValue()) == -1)
						addedReportColumnNames.add(((TextField)(tblRowItem.getItemProperty("Column Name").getValue())).getValue());
				else 
					return false;
	
				if(((TextField)(tblRowItem.getItemProperty("DB Column Name").getValue())).getValue() != null &&
						addedReportColumnDBNames.indexOf(((TextField)(tblRowItem.getItemProperty("DB Column Name").getValue())).getValue()) == -1)
					addedReportColumnDBNames.add(((TextField)(tblRowItem.getItemProperty("DB Column Name").getValue())).getValue());
				else 
					return false;
			}
		}
		
		return true;
	}

	private void onDelete()
	{
		this.savedVwVerticalRpt.setVwVerticalRptIsActive(false);
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		this.savedVwVerticalRpt.setLastModifiedBy(sessionUser);
		this.savedVwVerticalRpt.setModifiedDate(new Date());	
		new ViewVerticalReportDao().updateVwVerticalReport(this.savedVwVerticalRpt);
		//new FirstPassYieldReportDao().deleteDBFpyRptTable(this.fpyReport);
		//new FirstPassYieldReportDao().updateFirstPassYieldReport(this.fpyReport);		
		
		WebApplication webApp = (WebApplication)this.getParent().getParent();
		webApp.reloadMainForm("vertical view report");
		closeModalWindow();
    }
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
}