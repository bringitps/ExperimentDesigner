package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.TargetColumn;
import com.bringit.experiment.bll.TargetColumnGroup;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.TargetColumnDao;
import com.bringit.experiment.dao.TargetColumnGroupDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.ui.design.TargetReportBuilderDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.Tree.TreeTargetDetails;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class TargetReportBuilderForm extends TargetReportBuilderDesign {
	
	private TargetReport targetReport = new TargetReport();
	private Experiment experiment = new Experiment();
	private List<Experiment> experiments = new ExperimentDao().getActiveExperiments();
    private List<ExperimentField> expFields;
    private int lastNewItemId = 0;
	private List<Integer> dbIdOfTargetColumnGroupsToDelete = new ArrayList<Integer>();
	private List<Integer> dbIdOfTargetColumnsToDelete = new ArrayList<Integer>();
	private String selectedDateRangeFieldColumnLabel = null;
	
	private SystemSettings systemSettings;
	
	public TargetReportBuilderForm(int targetReportId)
	{	
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		this.cbxExperiment.setCaption(this.systemSettings.getExperimentLabel());
		
		enableComponents(false);
		cbxExperiment.setNullSelectionAllowed(false);
		colGroupLayout.setMargin(new MarginInfo(false, true, true, true));
		
		for(int j=0; j<experiments.size(); j++)
		{
			cbxExperiment.addItem(experiments.get(j).getExpId());
			cbxExperiment.setItemCaption(experiments.get(j).getExpId(), experiments.get(j).getExpName());
		}
		
		cbxExperiment.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbxExperiment.getValue()!=null)
				{
					experiment = new ExperimentDao().getExperimentById(Integer.parseInt(cbxExperiment.getValue().toString()));
					expFields = new ExperimentFieldDao().getActiveExperimentFields(experiment);
					enableComponents(true);
					tabColumnGroups.removeAllComponents();
					chxActive.setValue(true);	
					
					if(targetReportId != -1)
					{
						btnDeleteLayout.setEnabled(true);	
						//Add all Groups to be deleted						
					}
					else
					{
						if(chxWhatIf.getValue() != null && chxWhatIf.getValue())
							addWhatIfColumnGroup(null, null, true);
						else
							addColumnGroup(null, null, true);
						btnDeleteLayout.setEnabled(false);
					}
					
					//What If
					cbxDefaultDateRangeSelectExpField.setContainerDataSource(null);
					for(int i=0; i<expFields.size(); i++)
					{
						if(expFields.get(i).getExpFieldType().contains("date"))
						{
							cbxDefaultDateRangeSelectExpField.addItem(expFields.get(i).getExpFieldId());
							cbxDefaultDateRangeSelectExpField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
						}
					}
					//cbxDefaultDateRangeSelectExpField.addItem("CreatedDate");
					//cbxDefaultDateRangeSelectExpField.addItem("LastModifiedDate");			
					
					if(chxWhatIf.getValue() != null && chxWhatIf.getValue())
					{
						DateFormat df = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");			    		   
						txtTargetRptName.setValue(experiment.getExpName().replaceAll("&", " ").replaceAll("-"," ") + " " + df.format(new Date()));
					}
				        
				}
				else
				{
					experiment = new Experiment();
					enableComponents(false);
				}
			}   
	    });		
		
		chxWhatIf.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				txtTargetRptName.setEnabled(!chxWhatIf.getValue());
				
				if(cbxExperiment.getValue()!=null)
				{
					if(chxWhatIf.getValue())
					{
						DateFormat df = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");			    		   
						txtTargetRptName.setValue(experiment.getExpName().replace("&", " ") + " " + df.format(new Date()));
						
						gridDefaultDateFilter.setVisible(true);
					}
					else
					{
						gridDefaultDateFilter.setVisible(false);						
					}
				}
			}
		});		
		
		btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onSave();
			}

		});

		btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				closeModalWindow();
			}

		});
		
		btnAddColumnGroup.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(chxWhatIf.getValue() != null && chxWhatIf.getValue())
					addWhatIfColumnGroup(null, null, false);
				else
					addColumnGroup(null, null, false);
			}

		});
		
		btnDeleteColumnGroup.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				//addColumnGroup(-1);
				if(!tabColumnGroups.getSelectedTab().getCaption().toLowerCase().trim().equals("information"))
				{
					//Get Ids to Delete
					//dbIdOfTargetColumnsToDelete
					dbIdOfTargetColumnGroupsToDelete.add(Integer.parseInt(tabColumnGroups.getSelectedTab().getId()));
					tabColumnGroups.removeComponent(tabColumnGroups.getSelectedTab());
				}
			}

		});

		btnDelete.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onDelete();
			}

		});
		
		
		
		if(targetReportId == -1)
		{
			chxActive.setValue(true);
			gridDefaultDateFilter.setVisible(false);
		}
		else
		{
			//Load Target Report Data
			this.targetReport = new TargetReportDao().getTargetReportById(targetReportId);
			loadTargetReportData();
			enableComponents(true);
			btnDeleteLayout.setEnabled(true);	
			this.chxWhatIf.setEnabled(false);
		}
		
		this.txtTargetRptName.addValidator(new Validator() {
			public void validate(Object value) throws InvalidValueException {
                if(!StringUtils.isAlphanumeric(((String) value).trim().replaceAll("_", "").replaceAll(" ", "")))
                    throw new InvalidValueException("Only AlphaNumeric characters, Spaces and Underscores are allowed for Target Report Name");
            }
            
        });
		
	}

	private void onDelete()
	{
		this.targetReport.setTargetReportIsActive(false);
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		this.targetReport.setLastModifiedBy(sessionUser);
		this.targetReport.setModifiedDate(new Date());		
		new TargetReportDao().deleteDBRptTable(this.targetReport);
		new TargetReportDao().updateTargetReport(this.targetReport);
		
		WebApplication webApp = (WebApplication)this.getParent().getParent();
		webApp.reloadMainForm("target report");
		closeModalWindow();
    }
	
	private void loadTargetReportData()
	{
		this.cbxExperiment.setValue(this.targetReport.getExperiment().getExpId());
		this.txtTargetRptName.setValue(this.targetReport.getTargetReportName());
		this.txtTargetRptDescription.setValue(this.targetReport.getTargetReportDescription());
		this.chxActive.setValue(this.targetReport.getTargetReportIsActive());
		
		//What If 
		if(this.targetReport.getTargetReportWhatIf() != null && this.targetReport.getTargetReportWhatIf())
		{
			this.chxWhatIf.setValue(this.targetReport.getTargetReportWhatIf());
			if(this.targetReport.getTargetReportWhatIfDateExpField() != null)
			{
				this.cbxDefaultDateRangeSelectExpField.setValue(this.targetReport.getTargetReportWhatIfDateExpField().getExpFieldId());
				this.dtDefaultFilterFrom.setValue(this.targetReport.getTargetReportWhatIfDateFrom());
				this.dtDefaultFilterTo.setValue(this.targetReport.getTargetReportWhatIfDateTo());
			}
			txtTargetRptName.setEnabled(false);		
			gridDefaultDateFilter.setVisible(true);	
		}
		
		experiment = new ExperimentDao().getExperimentById(Integer.parseInt(cbxExperiment.getValue().toString()));
		expFields = new ExperimentFieldDao().getActiveExperimentFields(experiment);
		
		List<TargetColumnGroup> targetRptColGroups = new TargetColumnGroupDao().getTargetColumnGroupsByReportId(this.targetReport.getTargetReportId());
		
		if(targetRptColGroups != null)
		{
			for(int i=0; i<targetRptColGroups.size(); i++)
			{
				if(this.targetReport.getTargetReportWhatIf() != null && this.targetReport.getTargetReportWhatIf())
					addWhatIfColumnGroup(targetRptColGroups.get(i), new TargetColumnDao().getTargetColumnsByColGroupById(targetRptColGroups.get(i).getTargetColumnGroupId()), targetRptColGroups.get(i).getTargetColumnGroupName().toLowerCase().trim().equals("information"));
				else
					addColumnGroup(targetRptColGroups.get(i), new TargetColumnDao().getTargetColumnsByColGroupById(targetRptColGroups.get(i).getTargetColumnGroupId()), targetRptColGroups.get(i).getTargetColumnGroupName().toLowerCase().trim().equals("information"));
			}
		}
	}
	
	private void onSave()
	{
		boolean isNewRecord = false;
		boolean validateTargetColCntResult = validateTargetColumnCnt();
		boolean validateReqFieldsResult = validateRequiredFields();
		boolean validateNumberColsResult = validateNumberColsResult();
		boolean validateDuplicatedColNamesResult = validateDuplicatedColNames();
		boolean validateTargetColsNamesResult = validateTargetColsNames();
		boolean validateTargetReportNameResult = validateTargetReportName();
		boolean validateWhatIfSelectedDateRangeFieldResult = true;
		
		if(this.chxWhatIf.getValue() != null && this.chxWhatIf.getValue() && this.cbxDefaultDateRangeSelectExpField.getValue() != null)
			validateWhatIfSelectedDateRangeFieldResult = validateWhatIfSelectedDateFilterField(cbxDefaultDateRangeSelectExpField.getValue().toString());
		
		
		if(validateTargetColCntResult && validateReqFieldsResult && validateNumberColsResult && validateDuplicatedColNamesResult && validateTargetColsNamesResult && validateTargetReportNameResult && validateWhatIfSelectedDateRangeFieldResult)
		{		
			//Delete Records from DataBase
			if(dbIdOfTargetColumnGroupsToDelete.size() > 0)
			{
				for(int i=0; i<dbIdOfTargetColumnGroupsToDelete.size(); i++)
				{
					if(dbIdOfTargetColumnGroupsToDelete.get(i) > 0)
					{
						List<TargetColumn> targetReporColumnsToDelete = new TargetColumnDao().getTargetColumnsByColGroupById(dbIdOfTargetColumnGroupsToDelete.get(i));
						for(int j=0; targetReporColumnsToDelete != null && j<targetReporColumnsToDelete.size(); j++)
						{
							if(dbIdOfTargetColumnsToDelete.indexOf(targetReporColumnsToDelete.get(j).getTargetColumnId()) == -1)
								dbIdOfTargetColumnsToDelete.add(targetReporColumnsToDelete.get(j).getTargetColumnId());
						}
					}
				}
			}
			
			for(int i=0; i<dbIdOfTargetColumnsToDelete.size(); i++)
			{
				if(dbIdOfTargetColumnsToDelete.get(i) > 0)
					new TargetColumnDao().deleteTargetColumn(dbIdOfTargetColumnsToDelete.get(i));
			}
			
			for(int i=0; i<dbIdOfTargetColumnGroupsToDelete.size(); i++)
			{
				if(dbIdOfTargetColumnGroupsToDelete.get(i) > 0)
					new TargetColumnGroupDao().deleteTargetColumnGroup(dbIdOfTargetColumnGroupsToDelete.get(i));
			}
			
			List<TargetColumnGroup> targetRptColGroups = new ArrayList<TargetColumnGroup>();
			List<TargetColumn> targetRptColumns = new ArrayList<TargetColumn>();
			
			buildTargetReport(targetRptColGroups, targetRptColumns);
			
			String prevDbRptTable = this.targetReport.getTargetReportDbRptTableNameId();
			String currDbRptTable = this.targetReport.getTargetReportName().replaceAll(" ", "_");
			
			//New line added to store RPT DB Table Name
			//DB RPT Table Name = Target Report replacing spaces by "_"
			this.targetReport.setTargetReportDbRptTableNameId("targetrpt#" + currDbRptTable);
			
			if(this.targetReport.getTargetReportId() != null)
				new TargetReportDao().updateTargetReport(this.targetReport);
			else
			{
				isNewRecord = true;
				new TargetReportDao().addTargetReport(this.targetReport);
			}
			
			for(int i=0; i<targetRptColGroups.size(); i++)
			{
				if(targetRptColGroups.get(i).getTargetColumnGroupId() != null)
					new TargetColumnGroupDao().updateTargetColumnGroup(targetRptColGroups.get(i));
				else
					new TargetColumnGroupDao().addTargetColumnGroup(targetRptColGroups.get(i));
			}
		
			for(int i=0; i<targetRptColumns.size(); i++)
			{
				if(targetRptColumns.get(i).getTargetColumnId() != null)
					new TargetColumnDao().updateTargetColumn(targetRptColumns.get(i));
				else
					new TargetColumnDao().addTargetColumn(targetRptColumns.get(i));
			}

			if(prevDbRptTable != null)
				new TargetReportDao().deleteDBRptTableByName(prevDbRptTable);
	
			new TargetReportDao().deleteDBRptTable(this.targetReport);
			
			
			Integer targetReportId = this.targetReport.getTargetReportId();
			if(this.targetReport.getTargetReportWhatIf() != null && this.targetReport.getTargetReportWhatIf())
			{
				new TargetReportDao().updateWhatIfDBRptTable(this.targetReport);
				
				List<String> spTargetRptParams = new ArrayList<String>();
				spTargetRptParams.add(targetReportId.toString());
				new ExecuteQueryDao().executeStoredProcedure("spWhatIfTargetReportBuilder", spTargetRptParams);
			}
			else
			{
				new TargetReportDao().updateDBRptTable(this.targetReport);
				
				List<String> spTargetRptParams = new ArrayList<String>();
				spTargetRptParams.add(targetReportId.toString());
				new ExecuteQueryDao().executeStoredProcedure("spTargetReportBuilder", spTargetRptParams);
			}
          
			if(isNewRecord)
			{
				WebApplication webApp = (WebApplication)this.getParent().getParent();
				webApp.reloadMainForm("target report");
			}
			
			closeModalWindow();	
		}
		else
		{
			if(!validateTargetColCntResult)
				this.getUI().showNotification("Column Groups should contain at least one column.", Type.WARNING_MESSAGE);
			else if(!validateNumberColsResult)
				this.getUI().showNotification("Invalid Numbers.", Type.WARNING_MESSAGE);
			else if(!validateReqFieldsResult)
				this.getUI().showNotification("Please fill in all required Fields.", Type.WARNING_MESSAGE);
			else if(!validateDuplicatedColNamesResult)
				this.getUI().showNotification("Column names can not be duplicated.", Type.WARNING_MESSAGE);
			else if(!validateTargetColsNamesResult)
				this.getUI().showNotification("Only AlphaNumeric characters, Spaces and Underscores are allowed for Column names.", Type.WARNING_MESSAGE);
			else if(!validateTargetReportNameResult)
				this.getUI().showNotification("Invalid Report name. \"Target Report\" is not allowed. Duplicated names are not allowed. Only AlphaNumeric characters, Spaces and Underscores are allowed.", Type.WARNING_MESSAGE);
			else if(!validateWhatIfSelectedDateRangeFieldResult)
				this.getUI().showNotification("Selected field as \"Default date range\" must be added into Information Column Group.", Type.WARNING_MESSAGE);
		}	
	}

	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
	private void buildTargetReport(List<TargetColumnGroup> targetRptColGroups, List<TargetColumn> targetRptColumns)
	{
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		
			this.targetReport.setTargetReportIsActive(this.chxActive.getValue());
			this.targetReport.setTargetReportName(this.txtTargetRptName.getValue());
			this.targetReport.setTargetReportDescription(this.txtTargetRptDescription.getValue());
			this.targetReport.setModifiedDate(new Date());
			this.targetReport.setLastModifiedBy(sessionUser);
			this.targetReport.setExperiment(experiment);
			
			//What If 
			this.targetReport.setTargetReportWhatIf(this.chxWhatIf.getValue());
			if(this.cbxDefaultDateRangeSelectExpField.getValue() != null)
			{
				this.targetReport.setTargetReportWhatIfDateExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(this.cbxDefaultDateRangeSelectExpField.getValue().toString())));
				this.targetReport.setTargetReportWhatIfDateColumnLabel(this.selectedDateRangeFieldColumnLabel);
				this.targetReport.setTargetReportWhatIfDateFrom(this.dtDefaultFilterFrom.getValue());
				this.targetReport.setTargetReportWhatIfDateTo(this.dtDefaultFilterTo.getValue());
			}
			
			if(this.targetReport.getTargetReportId() == null)
			{
				this.targetReport.setCreatedDate(new Date());
				this.targetReport.setCreatedBy(sessionUser);				
			}
			
			for(int i=0; i<this.tabColumnGroups.getComponentCount(); i++)
			{

				VerticalLayout colGroupLayout = (VerticalLayout)this.tabColumnGroups.getTab(i).getComponent();
				TargetColumnGroup targetRptColGroup = new TargetColumnGroup();
				
				//Group Column Data
				GridLayout gridInfoColGroup = (GridLayout)colGroupLayout.getComponent(0);
				TextField txtInfoColGroupName = (TextField)gridInfoColGroup.getComponent(0, 0);
				CheckBox chxIsFailGroup = (CheckBox)gridInfoColGroup.getComponent(1, 0);
				
				if(colGroupLayout.getId() != null && !colGroupLayout.getId().isEmpty())
					targetRptColGroup.setTargetColumnGroupId(Integer.parseInt(colGroupLayout.getId()));
				
				targetRptColGroup.setTargetColumnGroupName(txtInfoColGroupName.getValue());
				targetRptColGroup.setTargetColumnGroupIsFail(chxIsFailGroup.getValue());
				targetRptColGroup.setTargetColumnGroupPosition(i);
				targetRptColGroup.setTargetReport(this.targetReport);
				
				HorizontalSplitPanel splitTblPanel = (HorizontalSplitPanel)colGroupLayout.getComponent(1);
				Table tblTargetReportInfoColumns = (Table)splitTblPanel.getFirstComponent();
				
				Collection itemIds = tblTargetReportInfoColumns.getContainerDataSource().getItemIds();
				int cntPosition = 0;
				
				for (Object itemIdObj : itemIds) 
				{	
					int itemId = (int)itemIdObj;
					Item tblRowItem = tblTargetReportInfoColumns.getContainerDataSource().getItem(itemId);
					
					TargetColumn targetRptColumn = new TargetColumn();
					
					if(itemId > -1)
						targetRptColumn.setTargetColumnId(itemId);
					
					if(targetRptColGroup.getTargetColumnGroupName().toLowerCase().equals("information"))
					{
						targetRptColumn.setTargetColumnIsInfo(true);
						targetRptColumn.setExperimentField(new ExperimentFieldDao().getExperimentFieldById((Integer)((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue()));			
					}
					else
					{
						targetRptColumn.setTargetColumnIsInfo(false);
						
						if(this.chxWhatIf.getValue() != null && this.chxWhatIf.getValue())
						{
							if(tblRowItem.getItemProperty("Current Min Field").getValue() != null)
								targetRptColumn.setTargetColumnWhatIfCurrMinExpField(new ExperimentFieldDao().getExperimentFieldById((Integer)((ComboBox)(tblRowItem.getItemProperty("Current Min Field").getValue())).getValue()));
							
							targetRptColumn.setTargetColumnWhatIfCurrentMin(Float.parseFloat(((TextField)(tblRowItem.getItemProperty("Current Min").getValue())).getValue()));
							
							if(tblRowItem.getItemProperty("Current Max Field").getValue() != null)
								targetRptColumn.setTargetColumnWhatIfCurrMaxExpField(new ExperimentFieldDao().getExperimentFieldById((Integer)((ComboBox)(tblRowItem.getItemProperty("Current Max Field").getValue())).getValue()));
							
							targetRptColumn.setTargetColumnWhatIfCurrentMax(Float.parseFloat(((TextField)(tblRowItem.getItemProperty("Current Max").getValue())).getValue()));
							targetRptColumn.setTargetColumnWhatIfNewMin(Float.parseFloat(((TextField)(tblRowItem.getItemProperty("New Min").getValue())).getValue()));
							targetRptColumn.setTargetColumnWhatIfNewMax(Float.parseFloat(((TextField)(tblRowItem.getItemProperty("New Max").getValue())).getValue()));
							targetRptColumn.setExperimentField(new ExperimentFieldDao().getExperimentFieldById((Integer)((ComboBox)(tblRowItem.getItemProperty("Measurement").getValue())).getValue()));			
							
						}
						else
						{
							targetRptColumn.setTargetColumnOffset(Float.parseFloat(((TextField)(tblRowItem.getItemProperty("Offset ±").getValue())).getValue()));
							targetRptColumn.setTargetColumnGoalValue(Float.parseFloat(((TextField)(tblRowItem.getItemProperty("Goal").getValue())).getValue()));
							targetRptColumn.setTargetColumnMinValue(Float.parseFloat(((TextField)(tblRowItem.getItemProperty("Min").getValue())).getValue()));
							targetRptColumn.setTargetColumnMaxValue(Float.parseFloat(((TextField)(tblRowItem.getItemProperty("Max").getValue())).getValue()));
							targetRptColumn.setExperimentField(new ExperimentFieldDao().getExperimentFieldById((Integer)((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue()));			
						}
					}
					
					targetRptColumn.setTargetColumnLabel(((TextField)(tblRowItem.getItemProperty("Column Label").getValue())).getValue());
					targetRptColumn.setTargetColumnGroup(targetRptColGroup);
					targetRptColumn.setTargetColumnPosition(cntPosition);
					targetRptColumns.add(targetRptColumn);
					cntPosition++;
				}
				
				
				targetRptColGroup.setTargetReport(this.targetReport);
				targetRptColGroups.add(targetRptColGroup);				
				
		}
	}
	
	
	private void enableComponents(boolean enable)
	{
		if(!enable)
			this.tabColumnGroups.removeAllComponents();
		
		this.btnSave.setEnabled(enable);
		this.colGroupLayout.setEnabled(enable);
		this.targetFieldGroupsLayout.setEnabled(enable);
	}
	
	
	private void addColumnGroup(TargetColumnGroup targetColumnGroup, List<TargetColumn> targetRptColumns, boolean isInfoColumnGroup)
	{
		if(isInfoColumnGroup)
		{
			VerticalLayout infoColGroupLayout = new VerticalLayout();
			GridLayout gridInfoColGroup = new GridLayout();
			gridInfoColGroup.setRows(1);
			gridInfoColGroup.setColumns(2);
			gridInfoColGroup.setWidth(85, Unit.PERCENTAGE);
			gridInfoColGroup.setHeight(62, Unit.PIXELS);
			
			TextField txtInfoColGroupName = new TextField();
			txtInfoColGroupName.setCaption("Field Group Name:");
			txtInfoColGroupName.setRequired(true);
			txtInfoColGroupName.setValue("Information");
			txtInfoColGroupName.setEnabled(false);
			txtInfoColGroupName.setStyleName("tiny");
			txtInfoColGroupName.setWidth(100, Unit.PERCENTAGE);
			gridInfoColGroup.addComponent(txtInfoColGroupName, 0, 0);
			
			CheckBox chxIsFailGroup = new CheckBox();
			chxIsFailGroup.setCaption("Is Fail Group?");
			chxIsFailGroup.setEnabled(false);
			chxIsFailGroup.setStyleName("tiny");
			gridInfoColGroup.addComponent(chxIsFailGroup, 1, 0);
			gridInfoColGroup.setComponentAlignment(chxIsFailGroup, Alignment.MIDDLE_RIGHT);
			
			//gridInfoColGroup.setMargin(new MarginInfo(false, true, false, true));
			gridInfoColGroup.setSpacing(true);
					
			infoColGroupLayout.addComponent(gridInfoColGroup);
			infoColGroupLayout.setCaption("Information");
			tabColumnGroups.addTab(infoColGroupLayout);

			GridLayout gridBtnsReportColumns = new GridLayout();
			gridBtnsReportColumns.setRows(2);
			gridBtnsReportColumns.setColumns(1);
			gridBtnsReportColumns.setSizeFull();
			gridBtnsReportColumns.setSpacing(true);
			
			Button btnAddReportColumn = new Button();
			btnAddReportColumn.setCaption("Add Column");
			btnAddReportColumn.setIcon(FontAwesome.PLUS_CIRCLE);
			btnAddReportColumn.setStyleName("tiny");
			btnAddReportColumn.setStyleName("friendly", true);
			btnAddReportColumn.setWidth(90, Unit.PERCENTAGE);
			
			Button btnRemoveReportColumn = new Button();
			btnRemoveReportColumn.setCaption("Delete Column");
			btnRemoveReportColumn.setIcon(FontAwesome.CLOSE);
			btnRemoveReportColumn.setStyleName("tiny");
			btnRemoveReportColumn.setStyleName("danger", true);
			btnRemoveReportColumn.setWidth(90, Unit.PERCENTAGE);
			
			gridBtnsReportColumns.addComponent(btnAddReportColumn);
			gridBtnsReportColumns.setComponentAlignment(btnAddReportColumn, Alignment.MIDDLE_CENTER);
			gridBtnsReportColumns.addComponent(btnRemoveReportColumn);
			gridBtnsReportColumns.setComponentAlignment(btnRemoveReportColumn, Alignment.MIDDLE_CENTER);
			
			Table tblTargetReportInfoColumns = new Table();
			tblTargetReportInfoColumns.setContainerDataSource(null);
			tblTargetReportInfoColumns.addContainerProperty("*", CheckBox.class, null);
			tblTargetReportInfoColumns.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field", ComboBox.class, null);
			tblTargetReportInfoColumns.addContainerProperty("Column Label", TextField.class, null);
			tblTargetReportInfoColumns.setPageLength(100);
			tblTargetReportInfoColumns.setWidth(100, Unit.PERCENTAGE);
			tblTargetReportInfoColumns.setHeight(155, Unit.PIXELS);
			tblTargetReportInfoColumns.setStyleName("tiny");
			tblTargetReportInfoColumns.setSelectable(true);
			tblTargetReportInfoColumns.setMultiSelect(false);
			

			
					
			btnAddReportColumn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					addInfoColTblItem(tblTargetReportInfoColumns, null);
				}
			});
			
			btnRemoveReportColumn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {

					if(tblTargetReportInfoColumns.getValue() != null)
					{
						dbIdOfTargetColumnsToDelete.add(Integer.parseInt(tblTargetReportInfoColumns.getValue().toString()));
						tblTargetReportInfoColumns.removeItem((int)tblTargetReportInfoColumns.getValue());
					}	
				}

			});
			

			HorizontalSplitPanel splitTblPanel = new HorizontalSplitPanel();
			splitTblPanel.setSplitPosition(82, Unit.PERCENTAGE);
			splitTblPanel.setLocked(true);
			splitTblPanel.addComponent(tblTargetReportInfoColumns);
			splitTblPanel.addComponent(gridBtnsReportColumns);
			infoColGroupLayout.setMargin(true);
			infoColGroupLayout.addComponent(splitTblPanel);
			
			if(targetColumnGroup != null)
			{
				infoColGroupLayout.setId(targetColumnGroup.getTargetColumnGroupId().toString());
				for(int i=0; i<targetRptColumns.size(); i++)
					addInfoColTblItem(tblTargetReportInfoColumns, targetRptColumns.get(i));
			}
		}
		else
		{
			VerticalLayout newColumnGroupLayout = new VerticalLayout();
			GridLayout gridNewColumnGroup = new GridLayout();
			gridNewColumnGroup.setRows(1);
			gridNewColumnGroup.setColumns(2);
			gridNewColumnGroup.setWidth(85, Unit.PERCENTAGE);
			gridNewColumnGroup.setHeight(62, Unit.PIXELS);
			
			TextField newTxtInfoColumnGroupName = new TextField();
			newTxtInfoColumnGroupName.setCaption("Column Group Name:");
			newTxtInfoColumnGroupName.setRequired(true);
			newTxtInfoColumnGroupName.setValue("New Column Group #" + tabColumnGroups.getComponentCount());
			newTxtInfoColumnGroupName.setStyleName("tiny");
			newTxtInfoColumnGroupName.setWidth(100, Unit.PERCENTAGE);
			newTxtInfoColumnGroupName.addListener(new TextChangeListener() {
	            
	            public void textChange(TextChangeEvent event) {
	            	VerticalLayout selectedTabLayout = (VerticalLayout)event.getComponent().getParent().getParent();
	            	System.out.println(event.getText());
	                if(event.getText().toLowerCase().trim().equals("information"))
	                {
	                	newTxtInfoColumnGroupName.setValue("Information");
	                	tabColumnGroups.getTab(selectedTabLayout).setCaption("Information");
	                	return;
	                }
	                else
	                	tabColumnGroups.getTab(selectedTabLayout).setCaption(event.getText());
	            }
	        });
			
			gridNewColumnGroup.addComponent(newTxtInfoColumnGroupName, 0, 0);
			//gridNewColumnGroup.setComponentAlignment(newTxtInfoColumnGroupName, Alignment.MIDDLE_LEFT);

			
			CheckBox chxIsFailGroup = new CheckBox();
			chxIsFailGroup.setStyleName("tiny");
			chxIsFailGroup.setCaption("Is Fail Group?");
			gridNewColumnGroup.addComponent(chxIsFailGroup, 1, 0);
			gridNewColumnGroup.setComponentAlignment(chxIsFailGroup, Alignment.MIDDLE_RIGHT);
			//gridNewColumnGroup.setMargin(new MarginInfo(false, true, false, true));
			gridNewColumnGroup.setSpacing(true);
			
			newColumnGroupLayout.addComponent(gridNewColumnGroup);
			
			GridLayout gridBtnsReportColumns = new GridLayout();
			gridBtnsReportColumns.setRows(2);
			gridBtnsReportColumns.setColumns(1);
			gridBtnsReportColumns.setSizeFull();
			gridBtnsReportColumns.setSpacing(true);
			
			Button btnAddColumn = new Button();
			btnAddColumn.setCaption("Add Column");
			btnAddColumn.setIcon(FontAwesome.PLUS_CIRCLE);
			btnAddColumn.setStyleName("tiny");
			btnAddColumn.setStyleName("friendly", true);
			btnAddColumn.setWidth(90, Unit.PERCENTAGE);
			
			Button btnRemoveColumn = new Button();
			btnRemoveColumn.setCaption("Delete Column");
			btnRemoveColumn.setIcon(FontAwesome.CLOSE);
			btnRemoveColumn.setStyleName("tiny");
			btnRemoveColumn.setStyleName("danger", true);
			btnRemoveColumn.setWidth(90, Unit.PERCENTAGE);
			
			gridBtnsReportColumns.addComponent(btnAddColumn);
			gridBtnsReportColumns.setComponentAlignment(btnAddColumn, Alignment.MIDDLE_CENTER);
			gridBtnsReportColumns.addComponent(btnRemoveColumn);
			gridBtnsReportColumns.setComponentAlignment(btnRemoveColumn, Alignment.MIDDLE_CENTER);
			
			Table tblTargetReportColumns = new Table();
			tblTargetReportColumns.setContainerDataSource(null);
			tblTargetReportColumns.addContainerProperty("*", CheckBox.class, null);
			tblTargetReportColumns.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field", ComboBox.class, null);
			tblTargetReportColumns.addContainerProperty("Column Label", TextField.class, null);
			tblTargetReportColumns.addContainerProperty("Offset ±", TextField.class, null);
			tblTargetReportColumns.addContainerProperty("Goal", TextField.class, null);
			tblTargetReportColumns.addContainerProperty("Min", TextField.class, null);
			tblTargetReportColumns.addContainerProperty("Max", TextField.class, null);
			tblTargetReportColumns.setPageLength(100);
			tblTargetReportColumns.setWidth(100, Unit.PERCENTAGE);
			tblTargetReportColumns.setHeight(155, Unit.PIXELS);
			tblTargetReportColumns.setStyleName("tiny");
			tblTargetReportColumns.setSelectable(true);
			tblTargetReportColumns.setMultiSelect(false);
			
			HorizontalSplitPanel splitTblPanel = new HorizontalSplitPanel();
			splitTblPanel.setSplitPosition(82, Unit.PERCENTAGE);
			splitTblPanel.setLocked(true);
			splitTblPanel.addComponent(tblTargetReportColumns);
			splitTblPanel.addComponent(gridBtnsReportColumns);
			newColumnGroupLayout.setMargin(true);
			newColumnGroupLayout.addComponent(splitTblPanel);

			
			tblTargetReportColumns.setImmediate(true);
			
		
			
			btnAddColumn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
	
					addColumnTblItem(tblTargetReportColumns, null);
				}
	
			});
			
			btnRemoveColumn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
	
					if(tblTargetReportColumns.getValue() != null)
					{
						dbIdOfTargetColumnsToDelete.add(Integer.parseInt(tblTargetReportColumns.getValue().toString()));
						tblTargetReportColumns.removeItem((int)tblTargetReportColumns.getValue());
					}
				}
	
			});
			
			
			if(targetColumnGroup != null)
			{
				newColumnGroupLayout.setId(targetColumnGroup.getTargetColumnGroupId().toString());
				newTxtInfoColumnGroupName.setValue(targetColumnGroup.getTargetColumnGroupName());
				chxIsFailGroup.setValue(targetColumnGroup.getTargetColumnGroupIsFail());
				newColumnGroupLayout.setCaption(targetColumnGroup.getTargetColumnGroupName());
				
				for(int i=0; i<targetRptColumns.size(); i++)
					addColumnTblItem(tblTargetReportColumns, targetRptColumns.get(i));
			}
			else
				newColumnGroupLayout.setCaption("New Column Group #" + tabColumnGroups.getComponentCount());
			
			tabColumnGroups.addTab(newColumnGroupLayout);
			tabColumnGroups.setSelectedTab(newColumnGroupLayout);
			
		}
		
	}
	
	
	private void addColumnTblItem(Table targetReportColumnTable, TargetColumn targetColumn)
	{
		Object[] itemValues = new Object[7];

		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		chxSelect.setStyleName("tiny");				
		itemValues[0] = chxSelect;
		
		ComboBox cbxExperimentField = new ComboBox("");
		cbxExperimentField.setWidth(160, Unit.PIXELS);
		cbxExperimentField.setStyleName("tiny");
		cbxExperimentField.setNullSelectionAllowed(false);
		cbxExperimentField.setRequired(true);
		
		for(int i=0; i<expFields.size(); i++)
		{
			cbxExperimentField.addItem(expFields.get(i).getExpFieldId());
			cbxExperimentField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
		}
		
		itemValues[1] = cbxExperimentField;
		
		TextField txtColumnLabel = new TextField();
		txtColumnLabel.setRequired(true);
		txtColumnLabel.setStyleName("tiny");
		txtColumnLabel.setWidth(120, Unit.PIXELS);
		
		itemValues[2] = txtColumnLabel;
		
		cbxExperimentField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				System.out.println("ExperimentField changed: " + cbxExperimentField.getValue() + " Caption: " + cbxExperimentField.getItemCaption(cbxExperimentField.getValue()));
				if(cbxExperimentField.getValue()!=null)
					txtColumnLabel.setValue(cbxExperimentField.getItemCaption(cbxExperimentField.getValue()));
				else
					txtColumnLabel.setValue("");
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

		TextField txtOffset = new TextField();
		txtOffset.setRequired(true);
		txtOffset.setStyleName("tiny");
		txtOffset.setWidth(65, Unit.PIXELS);
		txtOffset.addValidator(floatValidator);
		itemValues[3] = txtOffset;

		TextField txtGoal = new TextField();
		txtGoal.setRequired(true);
		txtGoal.setStyleName("tiny");
		txtGoal.setWidth(65, Unit.PIXELS);
		txtGoal.addValidator(floatValidator);	
		itemValues[4] = txtGoal;
		
		TextField txtMin = new TextField();
		txtMin.setEnabled(false);
		txtMin.setStyleName("tiny");
		txtMin.setWidth(65, Unit.PIXELS);
		txtMin.addValidator(floatValidator);
		itemValues[5] = txtMin;
		
		TextField txtMax = new TextField();
		txtMax.setEnabled(false);
		txtMax.setStyleName("tiny");
		txtMax.setWidth(65, Unit.PIXELS);
		txtMax.addValidator(floatValidator);	
		itemValues[6] = txtMax;
		
		txtOffset.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    if(txtGoal.isValid() && txtOffset.isValid())
                    {	
                    	Float minValue = Float.parseFloat(txtGoal.getValue()) - Float.parseFloat(txtOffset.getValue());
                    	txtMin.setValue(minValue.toString());
                    	Float maxValue = Float.parseFloat(txtGoal.getValue()) + Float.parseFloat(txtOffset.getValue());
                    	txtMax.setValue(maxValue.toString());
                    }
                }
        });
		

		txtGoal.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    if(txtGoal.isValid() && txtOffset.isValid())
                    {	
                    	Float minValue = Float.parseFloat(txtGoal.getValue()) - Float.parseFloat(txtOffset.getValue());
                    	txtMin.setValue(minValue.toString());
                    	Float maxValue = Float.parseFloat(txtGoal.getValue()) + Float.parseFloat(txtOffset.getValue());
                    	txtMax.setValue(maxValue.toString());
                    }
                }
        });
		
		int itemId = -1;
		
		if(targetColumn!=null)
		{
			itemId = targetColumn.getTargetColumnId();
			cbxExperimentField.setValue(targetColumn.getExperimentField().getExpFieldId());
			txtColumnLabel.setValue(targetColumn.getTargetColumnLabel());
			txtOffset.setValue(targetColumn.getTargetColumnOffset().toString());
			txtGoal.setValue(targetColumn.getTargetColumnGoalValue().toString());
			txtMin.setValue(targetColumn.getTargetColumnMinValue().toString());
			txtMax.setValue(targetColumn.getTargetColumnMaxValue().toString());
		}
		else
		{
			cbxExperimentField.focus();
			lastNewItemId = lastNewItemId - 1;
			itemId = lastNewItemId;
		}
		
		targetReportColumnTable.addItem(itemValues, itemId);
		targetReportColumnTable.select(itemId);
	}
	
	
	private void addInfoColTblItem(Table targetReportColumnTable, TargetColumn targetColumn)
	{
		Object[] itemValues = new Object[3];

		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		chxSelect.setStyleName("tiny");				
		itemValues[0] = chxSelect;
		
		ComboBox cbxExperimentField = new ComboBox("");
		cbxExperimentField.setWidth(240, Unit.PIXELS);
		cbxExperimentField.setStyleName("tiny");
		cbxExperimentField.setNullSelectionAllowed(false);
		cbxExperimentField.setRequired(true);
		
		//cbxExperimentField.addItem("CreatedDate");
		//cbxExperimentField.addItem("LastModifiedDate");			
		
		for(int i=0; i<expFields.size(); i++)
		{
			cbxExperimentField.addItem(expFields.get(i).getExpFieldId());
			cbxExperimentField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
		}
		
		itemValues[1] = cbxExperimentField;
		
		TextField txtColumnLabel = new TextField();
		txtColumnLabel.setRequired(true);
		txtColumnLabel.setStyleName("tiny");
		txtColumnLabel.setWidth(200, Unit.PIXELS);
		
		itemValues[2] = txtColumnLabel;

		cbxExperimentField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				System.out.println("ExperimentField changed: " + cbxExperimentField.getValue() + " Caption: " + cbxExperimentField.getItemCaption(cbxExperimentField.getValue()));
				if(cbxExperimentField.getValue()!=null)
					txtColumnLabel.setValue(cbxExperimentField.getItemCaption(cbxExperimentField.getValue()));
				else
					txtColumnLabel.setValue("");
			}
		});
		
		int itemId = -1;
		if(targetColumn!=null)
		{
			itemId = targetColumn.getTargetColumnId();
			cbxExperimentField.setValue(targetColumn.getExperimentField().getExpFieldId());
			txtColumnLabel.setValue(targetColumn.getTargetColumnLabel());
		}
		else
		{
			cbxExperimentField.focus();
			lastNewItemId = lastNewItemId - 1;
			itemId = lastNewItemId;
		}
		
		targetReportColumnTable.addItem(itemValues, itemId);
		targetReportColumnTable.select(itemId);
	}
	
	private void addWhatIfColumnGroup(TargetColumnGroup targetColumnGroup, List<TargetColumn> targetRptColumns, boolean isInfoColumnGroup)
	{
		if(isInfoColumnGroup)
		{
			VerticalLayout infoColGroupLayout = new VerticalLayout();
			GridLayout gridInfoColGroup = new GridLayout();
			gridInfoColGroup.setRows(1);
			gridInfoColGroup.setColumns(2);
			gridInfoColGroup.setWidth(85, Unit.PERCENTAGE);
			gridInfoColGroup.setHeight(62, Unit.PIXELS);
			
			TextField txtInfoColGroupName = new TextField();
			txtInfoColGroupName.setCaption("Field Group Name:");
			txtInfoColGroupName.setRequired(true);
			txtInfoColGroupName.setValue("Information");
			txtInfoColGroupName.setEnabled(false);
			txtInfoColGroupName.setStyleName("tiny");
			txtInfoColGroupName.setWidth(100, Unit.PERCENTAGE);
			gridInfoColGroup.addComponent(txtInfoColGroupName, 0, 0);
			
			CheckBox chxIsFailGroup = new CheckBox();
			chxIsFailGroup.setCaption("Is Fail Group?");
			chxIsFailGroup.setEnabled(false);
			chxIsFailGroup.setStyleName("tiny");
			gridInfoColGroup.addComponent(chxIsFailGroup, 1, 0);
			gridInfoColGroup.setComponentAlignment(chxIsFailGroup, Alignment.MIDDLE_RIGHT);
			
			//gridInfoColGroup.setMargin(new MarginInfo(false, true, false, true));
			gridInfoColGroup.setSpacing(true);
					
			infoColGroupLayout.addComponent(gridInfoColGroup);
			infoColGroupLayout.setCaption("Information");
			tabColumnGroups.addTab(infoColGroupLayout);

			GridLayout gridBtnsReportColumns = new GridLayout();
			gridBtnsReportColumns.setRows(2);
			gridBtnsReportColumns.setColumns(1);
			gridBtnsReportColumns.setSizeFull();
			gridBtnsReportColumns.setSpacing(true);
			
			Button btnAddReportColumn = new Button();
			btnAddReportColumn.setCaption("Add Column");
			btnAddReportColumn.setIcon(FontAwesome.PLUS_CIRCLE);
			btnAddReportColumn.setStyleName("tiny");
			btnAddReportColumn.setStyleName("friendly", true);
			btnAddReportColumn.setWidth(90, Unit.PERCENTAGE);
			
			Button btnRemoveReportColumn = new Button();
			btnRemoveReportColumn.setCaption("Delete Column");
			btnRemoveReportColumn.setIcon(FontAwesome.CLOSE);
			btnRemoveReportColumn.setStyleName("tiny");
			btnRemoveReportColumn.setStyleName("danger", true);
			btnRemoveReportColumn.setWidth(90, Unit.PERCENTAGE);
			
			gridBtnsReportColumns.addComponent(btnAddReportColumn);
			gridBtnsReportColumns.setComponentAlignment(btnAddReportColumn, Alignment.MIDDLE_CENTER);
			gridBtnsReportColumns.addComponent(btnRemoveReportColumn);
			gridBtnsReportColumns.setComponentAlignment(btnRemoveReportColumn, Alignment.MIDDLE_CENTER);
			
			Table tblTargetReportInfoColumns = new Table();
			tblTargetReportInfoColumns.setContainerDataSource(null);
			tblTargetReportInfoColumns.addContainerProperty("*", CheckBox.class, null);
			tblTargetReportInfoColumns.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field", ComboBox.class, null);
			tblTargetReportInfoColumns.addContainerProperty("Column Label", TextField.class, null);
			tblTargetReportInfoColumns.setPageLength(100);
			tblTargetReportInfoColumns.setWidth(100, Unit.PERCENTAGE);
			tblTargetReportInfoColumns.setHeight(155, Unit.PIXELS);
			tblTargetReportInfoColumns.setStyleName("tiny");
			tblTargetReportInfoColumns.setSelectable(true);
			tblTargetReportInfoColumns.setMultiSelect(false);
					
			btnAddReportColumn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					addInfoColTblItem(tblTargetReportInfoColumns, null);
				}
			});
			
			btnRemoveReportColumn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {

					if(tblTargetReportInfoColumns.getValue() != null)
					{
						dbIdOfTargetColumnsToDelete.add(Integer.parseInt(tblTargetReportInfoColumns.getValue().toString()));
						tblTargetReportInfoColumns.removeItem((int)tblTargetReportInfoColumns.getValue());
					}	
				}

			});
			

			HorizontalSplitPanel splitTblPanel = new HorizontalSplitPanel();
			splitTblPanel.setSplitPosition(82, Unit.PERCENTAGE);
			splitTblPanel.setLocked(true);
			splitTblPanel.addComponent(tblTargetReportInfoColumns);
			splitTblPanel.addComponent(gridBtnsReportColumns);
			infoColGroupLayout.setMargin(true);
			infoColGroupLayout.addComponent(splitTblPanel);
			
			if(targetColumnGroup != null)
			{
				infoColGroupLayout.setId(targetColumnGroup.getTargetColumnGroupId().toString());
				for(int i=0; i<targetRptColumns.size(); i++)
					addInfoColTblItem(tblTargetReportInfoColumns, targetRptColumns.get(i));
			}
		}
		else
		{
			VerticalLayout newColumnGroupLayout = new VerticalLayout();
			GridLayout gridNewColumnGroup = new GridLayout();
			gridNewColumnGroup.setRows(1);
			gridNewColumnGroup.setColumns(2);
			gridNewColumnGroup.setWidth(85, Unit.PERCENTAGE);
			gridNewColumnGroup.setHeight(62, Unit.PIXELS);
			
			TextField newTxtInfoColumnGroupName = new TextField();
			newTxtInfoColumnGroupName.setCaption("Column Group Name:");
			newTxtInfoColumnGroupName.setRequired(true);
			newTxtInfoColumnGroupName.setValue("New Column Group #" + tabColumnGroups.getComponentCount());
			newTxtInfoColumnGroupName.setStyleName("tiny");
			newTxtInfoColumnGroupName.setWidth(100, Unit.PERCENTAGE);
			newTxtInfoColumnGroupName.addListener(new TextChangeListener() {
	            
	            public void textChange(TextChangeEvent event) {
	            	VerticalLayout selectedTabLayout = (VerticalLayout)event.getComponent().getParent().getParent();
	            	System.out.println(event.getText());
	                if(event.getText().toLowerCase().trim().equals("information"))
	                {
	                	newTxtInfoColumnGroupName.setValue("Information");
	                	tabColumnGroups.getTab(selectedTabLayout).setCaption("Information");
	                	return;
	                }
	                else
	                	tabColumnGroups.getTab(selectedTabLayout).setCaption(event.getText());
	            }
	        });
			
			gridNewColumnGroup.addComponent(newTxtInfoColumnGroupName, 0, 0);
			//gridNewColumnGroup.setComponentAlignment(newTxtInfoColumnGroupName, Alignment.MIDDLE_LEFT);

			
			CheckBox chxIsFailGroup = new CheckBox();
			chxIsFailGroup.setStyleName("tiny");
			chxIsFailGroup.setCaption("Is Fail Group?");
			gridNewColumnGroup.addComponent(chxIsFailGroup, 1, 0);
			gridNewColumnGroup.setComponentAlignment(chxIsFailGroup, Alignment.MIDDLE_RIGHT);
			//gridNewColumnGroup.setMargin(new MarginInfo(false, true, false, true));
			gridNewColumnGroup.setSpacing(true);
			
			newColumnGroupLayout.addComponent(gridNewColumnGroup);
			
			GridLayout gridBtnsReportColumns = new GridLayout();
			gridBtnsReportColumns.setRows(2);
			gridBtnsReportColumns.setColumns(1);
			gridBtnsReportColumns.setSizeFull();
			gridBtnsReportColumns.setSpacing(true);
			
			Button btnAddColumn = new Button();
			btnAddColumn.setCaption("Add Column");
			btnAddColumn.setIcon(FontAwesome.PLUS_CIRCLE);
			btnAddColumn.setStyleName("tiny");
			btnAddColumn.setStyleName("friendly", true);
			btnAddColumn.setWidth(90, Unit.PERCENTAGE);
			
			Button btnRemoveColumn = new Button();
			btnRemoveColumn.setCaption("Delete Column");
			btnRemoveColumn.setIcon(FontAwesome.CLOSE);
			btnRemoveColumn.setStyleName("tiny");
			btnRemoveColumn.setStyleName("danger", true);
			btnRemoveColumn.setWidth(90, Unit.PERCENTAGE);
			
			gridBtnsReportColumns.addComponent(btnAddColumn);
			gridBtnsReportColumns.setComponentAlignment(btnAddColumn, Alignment.MIDDLE_CENTER);
			gridBtnsReportColumns.addComponent(btnRemoveColumn);
			gridBtnsReportColumns.setComponentAlignment(btnRemoveColumn, Alignment.MIDDLE_CENTER);
			
			Table tblTargetReportColumns = new Table();
			tblTargetReportColumns.setContainerDataSource(null);
			tblTargetReportColumns.addContainerProperty("*", CheckBox.class, null);
			//tblTargetReportColumns.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field", ComboBox.class, null);
			tblTargetReportColumns.addContainerProperty("Measurement", ComboBox.class, null);
			tblTargetReportColumns.addContainerProperty("Column Label", TextField.class, null);
			//tblTargetReportColumns.addContainerProperty("Offset ±", TextField.class, null);
			//tblTargetReportColumns.addContainerProperty("Goal", TextField.class, null);
			//tblTargetReportColumns.addContainerProperty("Min", TextField.class, null);
			//tblTargetReportColumns.addContainerProperty("Max", TextField.class, null);
			
			tblTargetReportColumns.addContainerProperty("Current Min Field", ComboBox.class, null);
			tblTargetReportColumns.addContainerProperty("Current Min", TextField.class, null);
			tblTargetReportColumns.addContainerProperty("Current Max Field", ComboBox.class, null);
			tblTargetReportColumns.addContainerProperty("Current Max", TextField.class, null);
			tblTargetReportColumns.addContainerProperty("New Min", TextField.class, null);
			tblTargetReportColumns.addContainerProperty("New Max", TextField.class, null);
			
			tblTargetReportColumns.setPageLength(100);
			tblTargetReportColumns.setWidth(100, Unit.PERCENTAGE);
			tblTargetReportColumns.setHeight(155, Unit.PIXELS);
			tblTargetReportColumns.setStyleName("small");
			tblTargetReportColumns.setSelectable(true);
			tblTargetReportColumns.setMultiSelect(false);
			
			HorizontalSplitPanel splitTblPanel = new HorizontalSplitPanel();
			splitTblPanel.setSplitPosition(82, Unit.PERCENTAGE);
			splitTblPanel.setLocked(true);
			splitTblPanel.addComponent(tblTargetReportColumns);
			splitTblPanel.addComponent(gridBtnsReportColumns);
			newColumnGroupLayout.setMargin(true);
			newColumnGroupLayout.addComponent(splitTblPanel);

			
			tblTargetReportColumns.setImmediate(true);
			
		
			
			btnAddColumn.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
	
					addWhatIfColumnTblItem(tblTargetReportColumns, null);
				}
	
			});
			
			btnRemoveColumn.addClickListener(new Button.ClickListener() { 
				
				@Override
				public void buttonClick(ClickEvent event) {
	
					if(tblTargetReportColumns.getValue() != null)
					{
						dbIdOfTargetColumnsToDelete.add(Integer.parseInt(tblTargetReportColumns.getValue().toString()));
						tblTargetReportColumns.removeItem((int)tblTargetReportColumns.getValue());
					}
				}
	
			});
			
			
			if(targetColumnGroup != null)
			{
				newColumnGroupLayout.setId(targetColumnGroup.getTargetColumnGroupId().toString());
				newTxtInfoColumnGroupName.setValue(targetColumnGroup.getTargetColumnGroupName());
				chxIsFailGroup.setValue(targetColumnGroup.getTargetColumnGroupIsFail());
				newColumnGroupLayout.setCaption(targetColumnGroup.getTargetColumnGroupName());
				
				for(int i=0; i<targetRptColumns.size(); i++)
					addWhatIfColumnTblItem(tblTargetReportColumns, targetRptColumns.get(i));
			}
			else
				newColumnGroupLayout.setCaption("New Column Group #" + tabColumnGroups.getComponentCount());
			
			tabColumnGroups.addTab(newColumnGroupLayout);
			tabColumnGroups.setSelectedTab(newColumnGroupLayout);
			
		}
		
	}
	
	
	private void addWhatIfColumnTblItem(Table targetReportColumnTable, TargetColumn targetColumn)
	{
		Object[] itemValues = new Object[9];

		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		chxSelect.setStyleName("tiny");				
		itemValues[0] = chxSelect;
		
		ComboBox cbxExperimentField = new ComboBox("");
		cbxExperimentField.setWidth(160, Unit.PIXELS);
		cbxExperimentField.setStyleName("tiny");
		cbxExperimentField.setNullSelectionAllowed(false);
		cbxExperimentField.setRequired(true);
		
		for(int i=0; i<expFields.size(); i++)
		{
			if("float".equals(expFields.get(i).getExpFieldType()) || "decimal".equals(expFields.get(i).getExpFieldType()) || "int".equals(expFields.get(i).getExpFieldType()))
			{
				cbxExperimentField.addItem(expFields.get(i).getExpFieldId());
				cbxExperimentField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
			}
		}
		
		itemValues[1] = cbxExperimentField;
		
		TextField txtColumnLabel = new TextField();
		txtColumnLabel.setRequired(true);
		txtColumnLabel.setStyleName("tiny");
		txtColumnLabel.setWidth(95, Unit.PERCENTAGE);
		
		itemValues[2] = txtColumnLabel;
		
		cbxExperimentField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				System.out.println("ExperimentField changed: " + cbxExperimentField.getValue() + " Caption: " + cbxExperimentField.getItemCaption(cbxExperimentField.getValue()));
				if(cbxExperimentField.getValue()!=null)
					txtColumnLabel.setValue(cbxExperimentField.getItemCaption(cbxExperimentField.getValue()));
				else
					txtColumnLabel.setValue("");
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

		
		ComboBox cbxCurrentLowerLimitField = new ComboBox("");
		cbxCurrentLowerLimitField.setWidth(95, Unit.PERCENTAGE);
		cbxCurrentLowerLimitField.setStyleName("tiny");
		cbxCurrentLowerLimitField.setNullSelectionAllowed(false);
		cbxCurrentLowerLimitField.setRequired(true);
		
		for(int i=0; i<expFields.size(); i++)
		{		
			if("float".equals(expFields.get(i).getExpFieldType()) || "decimal".equals(expFields.get(i).getExpFieldType()) || "int".equals(expFields.get(i).getExpFieldType()))
			{
				cbxCurrentLowerLimitField.addItem(expFields.get(i).getExpFieldId());
				cbxCurrentLowerLimitField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
			}
		}
		
		itemValues[3] = cbxCurrentLowerLimitField;

		
        TextField txtCurrentLowerLimit = new TextField();
        txtCurrentLowerLimit.setRequired(true);
        txtCurrentLowerLimit.setStyleName("tiny");
        txtCurrentLowerLimit.setWidth(95, Unit.PERCENTAGE);
        txtCurrentLowerLimit.addValidator(floatValidator);
		
		cbxCurrentLowerLimitField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbxCurrentLowerLimitField.getValue()!=null)
				{
					//Get last value of selected field
					ExperimentField selectedExperimentField = new ExperimentFieldDao().getExperimentFieldById((Integer) cbxCurrentLowerLimitField.getValue());
					String sqlSelectFieldLastValue = "SELECT TOP 1 " + selectedExperimentField.getExpDbFieldNameId() 
					+ " FROM " + selectedExperimentField.getExperiment().getExpDbRptTableNameId() 
					+ " WHERE " + selectedExperimentField.getExpDbFieldNameId()  + " IS NOT NULL ORDER BY RecordId DESC ";
					
					String lastValue = "0.0";
					ResultSet fieldLastValueResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectFieldLastValue);
             		if(fieldLastValueResults != null)
             		{  
             			try {
							fieldLastValueResults.next();
							lastValue = fieldLastValueResults.getString(1);
						} catch (SQLException e) {
							// TODO Auto-generated catch block							
						}
             		}             		
             		txtCurrentLowerLimit.setValue(lastValue);					
				}
			}   
	    });	
		
        
        itemValues[4] = txtCurrentLowerLimit;
        
        ComboBox cbxCurrentUpperLimitField = new ComboBox("");
        cbxCurrentUpperLimitField.setWidth(95, Unit.PERCENTAGE);
        cbxCurrentUpperLimitField.setStyleName("tiny");
        cbxCurrentUpperLimitField.setNullSelectionAllowed(false);
        cbxCurrentUpperLimitField.setRequired(true);
		
		for(int i=0; i<expFields.size(); i++)
		{		
			if("float".equals(expFields.get(i).getExpFieldType()) || "decimal".equals(expFields.get(i).getExpFieldType()) || "int".equals(expFields.get(i).getExpFieldType()))
			{
				cbxCurrentUpperLimitField.addItem(expFields.get(i).getExpFieldId());
				cbxCurrentUpperLimitField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
			}
		}
				
		itemValues[5] = cbxCurrentUpperLimitField;
        
        TextField txtCurrentUpperLimit = new TextField();
        txtCurrentUpperLimit.setRequired(true);
        txtCurrentUpperLimit.setStyleName("tiny");
        txtCurrentUpperLimit.setWidth(95, Unit.PERCENTAGE);
        txtCurrentUpperLimit.addValidator(floatValidator);
		
		
		cbxCurrentUpperLimitField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbxCurrentUpperLimitField.getValue()!=null)
				{
					//Get last value of selected field
					ExperimentField selectedExperimentField = new ExperimentFieldDao().getExperimentFieldById((Integer) cbxCurrentUpperLimitField.getValue());
					String sqlSelectFieldLastValue = "SELECT TOP 1 " + selectedExperimentField.getExpDbFieldNameId() 
					+ " FROM " + selectedExperimentField.getExperiment().getExpDbRptTableNameId() 
					+ " WHERE " + selectedExperimentField.getExpDbFieldNameId()  + " IS NOT NULL ORDER BY RecordId DESC ";
					
					String lastValue = "0.0";
					ResultSet fieldLastValueResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectFieldLastValue);
             		if(fieldLastValueResults != null)
             		{  
             			try {
							fieldLastValueResults.next();
							lastValue = fieldLastValueResults.getString(1);
						} catch (SQLException e) {
							// TODO Auto-generated catch block							
						}
             		}             		
             		txtCurrentUpperLimit.setValue(lastValue);					
				}
			}   
	    });	

        itemValues[6] = txtCurrentUpperLimit;

        TextField txtNewLowerLimit = new TextField();
        txtNewLowerLimit.setRequired(true);
        txtNewLowerLimit.setStyleName("tiny");
        txtNewLowerLimit.setWidth(95, Unit.PERCENTAGE);
        txtNewLowerLimit.addValidator(floatValidator);
		itemValues[7] = txtNewLowerLimit;
		
        TextField txtNewUpperLimit = new TextField();
        txtNewUpperLimit.setRequired(true);
        txtNewUpperLimit.setStyleName("tiny");
        txtNewUpperLimit.setWidth(95, Unit.PERCENTAGE);
        txtNewUpperLimit.addValidator(floatValidator);
		itemValues[8] = txtNewUpperLimit;
		
		
        /*
		TextField txtOffset = new TextField();
		txtOffset.setRequired(true);
		txtOffset.setStyleName("tiny");
		txtOffset.setWidth(65, Unit.PIXELS);
		txtOffset.addValidator(floatValidator);
		itemValues[3] = txtOffset;

		TextField txtGoal = new TextField();
		txtGoal.setRequired(true);
		txtGoal.setStyleName("tiny");
		txtGoal.setWidth(65, Unit.PIXELS);
		txtGoal.addValidator(floatValidator);	
		itemValues[4] = txtGoal;
		
		TextField txtMin = new TextField();
		//txtMin.setEnabled(false);
		txtMin.setStyleName("tiny");
		txtMin.setWidth(65, Unit.PIXELS);
		txtMin.addValidator(floatValidator);
		itemValues[5] = txtMin;
		
		TextField txtMax = new TextField();
		//txtMax.setEnabled(false);
		txtMax.setStyleName("tiny");
		txtMax.setWidth(65, Unit.PIXELS);
		txtMax.addValidator(floatValidator);	
		itemValues[6] = txtMax;
		
		txtOffset.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    if(txtGoal.isValid() && txtOffset.isValid())
                    {	
                    	Float minValue = Float.parseFloat(txtGoal.getValue()) - Float.parseFloat(txtOffset.getValue());
                    	txtMin.setValue(minValue.toString());
                    	Float maxValue = Float.parseFloat(txtGoal.getValue()) + Float.parseFloat(txtOffset.getValue());
                    	txtMax.setValue(maxValue.toString());
                    }
                }
        });
		

		txtGoal.addListener(new ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    if(txtGoal.isValid() && txtOffset.isValid())
                    {	
                    	Float minValue = Float.parseFloat(txtGoal.getValue()) - Float.parseFloat(txtOffset.getValue());
                    	txtMin.setValue(minValue.toString());
                    	Float maxValue = Float.parseFloat(txtGoal.getValue()) + Float.parseFloat(txtOffset.getValue());
                    	txtMax.setValue(maxValue.toString());
                    }
                }
        });
		*/
		int itemId = -1;
		
		if(targetColumn!=null)
		{
			itemId = targetColumn.getTargetColumnId();
			cbxExperimentField.setValue(targetColumn.getExperimentField().getExpFieldId());
			txtColumnLabel.setValue(targetColumn.getTargetColumnLabel());
			
			if(targetColumn.getTargetColumnWhatIfCurrMinExpField() != null)
				cbxCurrentLowerLimitField.setValue(targetColumn.getTargetColumnWhatIfCurrMinExpField().getExpFieldId());
	        
			txtCurrentLowerLimit.setValue(targetColumn.getTargetColumnWhatIfCurrentMin().toString());
			
			if(targetColumn.getTargetColumnWhatIfCurrMaxExpField() != null)
				cbxCurrentUpperLimitField.setValue(targetColumn.getTargetColumnWhatIfCurrMaxExpField().getExpFieldId());
			
			txtCurrentUpperLimit.setValue(targetColumn.getTargetColumnWhatIfCurrentMax().toString());
			txtNewLowerLimit.setValue(targetColumn.getTargetColumnWhatIfNewMin().toString());
			txtNewUpperLimit.setValue(targetColumn.getTargetColumnWhatIfNewMax().toString());
			
			/*txtOffset.setValue(targetColumn.getTargetColumnOffset().toString());
			txtGoal.setValue(targetColumn.getTargetColumnGoalValue().toString());
			txtMin.setValue(targetColumn.getTargetColumnMinValue().toString());
			txtMax.setValue(targetColumn.getTargetColumnMaxValue().toString());*/
		}
		else
		{
			cbxExperimentField.focus();
			lastNewItemId = lastNewItemId - 1;
			itemId = lastNewItemId;
		}
		
		targetReportColumnTable.addItem(itemValues, itemId);
		targetReportColumnTable.select(itemId);
	}
		
	
	private boolean validateRequiredFields()
	{
		if(!this.txtTargetRptName.isValid() || !this.cbxExperiment.isValid()) return false;
		
		if(this.chxWhatIf.getValue() != null && this.chxWhatIf.getValue())
		{	
			if(this.dtDefaultFilterFrom.isEmpty())
			{
				this.dtDefaultFilterFrom.focus();				
				return false;
			}

			if(this.dtDefaultFilterTo.isEmpty())
			{
				this.dtDefaultFilterTo.focus();				
				return false;
			}
			
			for(int i=0; i<this.tabColumnGroups.getComponentCount(); i++)
			{
				VerticalLayout colGroupLayout = (VerticalLayout)this.tabColumnGroups.getTab(i).getComponent();
				HorizontalSplitPanel splitTblPanel = (HorizontalSplitPanel)colGroupLayout.getComponent(1);
				Table tblTargetReportColumns = (Table)splitTblPanel.getFirstComponent();
				
				Collection itemIds = tblTargetReportColumns.getContainerDataSource().getItemIds();
				for (Object itemIdObj : itemIds) 
				{	
					int itemId = (int)itemIdObj;
					Item tblRowItem = tblTargetReportColumns.getContainerDataSource().getItem(itemId);
					boolean notEmpty = true;
					if(!colGroupLayout.getCaption().toLowerCase().equals("information"))
					{
						if(((TextField)(tblRowItem.getItemProperty("Current Min").getValue())).getValue().isEmpty()) notEmpty = false;
						if(((TextField)(tblRowItem.getItemProperty("Current Max").getValue())).getValue().isEmpty()) notEmpty = false;
						if(((TextField)(tblRowItem.getItemProperty("New Min").getValue())).getValue().isEmpty()) notEmpty = false;
						if(((TextField)(tblRowItem.getItemProperty("New Max").getValue())).getValue().isEmpty()) notEmpty = false;
						if(!((ComboBox)(tblRowItem.getItemProperty("Measurement").getValue())).isValid()) notEmpty = false;						
					}
					else
					{
						if(!((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).isValid()) notEmpty = false;
					}
					if(((TextField)(tblRowItem.getItemProperty("Column Label").getValue())).getValue().isEmpty()) notEmpty = false;
					//if(!((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).isValid()) notEmpty = false;			
					
					if(!notEmpty)
					{
						this.tabColumnGroups.setSelectedTab(i);
						return false;
					}	
				}
			}	
		}
		else
		{
			for(int i=0; i<this.tabColumnGroups.getComponentCount(); i++)
			{
				VerticalLayout colGroupLayout = (VerticalLayout)this.tabColumnGroups.getTab(i).getComponent();
				HorizontalSplitPanel splitTblPanel = (HorizontalSplitPanel)colGroupLayout.getComponent(1);
				Table tblTargetReportColumns = (Table)splitTblPanel.getFirstComponent();
				
				Collection itemIds = tblTargetReportColumns.getContainerDataSource().getItemIds();
				for (Object itemIdObj : itemIds) 
				{	
					int itemId = (int)itemIdObj;
					Item tblRowItem = tblTargetReportColumns.getContainerDataSource().getItem(itemId);
					boolean notEmpty = true;
					if(!colGroupLayout.getCaption().toLowerCase().equals("information"))
					{
						if(((TextField)(tblRowItem.getItemProperty("Offset ±").getValue())).getValue().isEmpty()) notEmpty = false;
						if(((TextField)(tblRowItem.getItemProperty("Goal").getValue())).getValue().isEmpty()) notEmpty = false;
						if(((TextField)(tblRowItem.getItemProperty("Min").getValue())).getValue().isEmpty()) notEmpty = false;
						if(((TextField)(tblRowItem.getItemProperty("Max").getValue())).getValue().isEmpty()) notEmpty = false;
					}
			
					if(((TextField)(tblRowItem.getItemProperty("Column Label").getValue())).getValue().isEmpty()) notEmpty = false;
					if(!((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).isValid()) notEmpty = false;			
				
					if(!notEmpty)
					{
						this.tabColumnGroups.setSelectedTab(i);
						return false;
					}	
				}
			}
		}
		return true;
	}
	
	
	private boolean validateDuplicatedColNames()
	{	
		List<String> addedColNames = new ArrayList<String>();
		
		for(int i=0; i<this.tabColumnGroups.getComponentCount(); i++)
		{
			VerticalLayout colGroupLayout = (VerticalLayout)this.tabColumnGroups.getTab(i).getComponent();
			HorizontalSplitPanel splitTblPanel = (HorizontalSplitPanel)colGroupLayout.getComponent(1);
			Table tblTargetReportColumns = (Table)splitTblPanel.getFirstComponent();
			
			Collection itemIds = tblTargetReportColumns.getContainerDataSource().getItemIds();
			for (Object itemIdObj : itemIds) 
			{	
				int itemId = (int)itemIdObj;
				Item tblRowItem = tblTargetReportColumns.getContainerDataSource().getItem(itemId);
				String addedColName =((TextField)(tblRowItem.getItemProperty("Column Label").getValue())).getValue().trim().toLowerCase();
			
				if(addedColNames.indexOf(addedColName) >= 0)
				{
					this.tabColumnGroups.setSelectedTab(i);
					tblTargetReportColumns.select(itemId);
					return false;
				}
				else
					addedColNames.add(addedColName);					
			}
		}
		
		return true;
	}

	
	private boolean validateTargetColumnCnt()
	{
		for(int i=0; i<this.tabColumnGroups.getComponentCount(); i++)
		{

			VerticalLayout colGroupLayout = (VerticalLayout)this.tabColumnGroups.getTab(i).getComponent();
			HorizontalSplitPanel splitTblPanel = (HorizontalSplitPanel)colGroupLayout.getComponent(1);
			Table tblTargetReportColumns = (Table)splitTblPanel.getFirstComponent();
			
			Collection itemIds = tblTargetReportColumns.getContainerDataSource().getItemIds();
			if(itemIds.size() <= 0)
			{
				this.tabColumnGroups.setSelectedTab(i);
				return false;
			}
		}
		return true;
	}
	
	
	private boolean validateNumberColsResult()
	{
		for(int i=0; i<this.tabColumnGroups.getComponentCount(); i++)
		{
			VerticalLayout colGroupLayout = (VerticalLayout)this.tabColumnGroups.getTab(i).getComponent();
			HorizontalSplitPanel splitTblPanel = (HorizontalSplitPanel)colGroupLayout.getComponent(1);
			Table tblTargetReportColumns = (Table)splitTblPanel.getFirstComponent();
			
			Collection itemIds = tblTargetReportColumns.getContainerDataSource().getItemIds();
			for (Object itemIdObj : itemIds) 
			{	
				int itemId = (int)itemIdObj;
				Item tblRowItem = tblTargetReportColumns.getContainerDataSource().getItem(itemId);
				boolean isValid = true;
				if(!colGroupLayout.getCaption().toLowerCase().equals("information"))
				{
					if(this.chxWhatIf.getValue() != null && this.chxWhatIf.getValue())
					{
						if(!((TextField)(tblRowItem.getItemProperty("Current Min").getValue())).isValid()) isValid = false;
						if(!((TextField)(tblRowItem.getItemProperty("Current Max").getValue())).isValid()) isValid = false;
						if(!((TextField)(tblRowItem.getItemProperty("New Min").getValue())).isValid()) isValid = false;
						if(!((TextField)(tblRowItem.getItemProperty("New Max").getValue())).isValid()) isValid = false;	
					}
					else
					{					
						if(!((TextField)(tblRowItem.getItemProperty("Offset ±").getValue())).isValid()) isValid = false;
						if(!((TextField)(tblRowItem.getItemProperty("Goal").getValue())).isValid()) isValid = false;
						if(!((TextField)(tblRowItem.getItemProperty("Min").getValue())).isValid()) isValid = false;
						if(!((TextField)(tblRowItem.getItemProperty("Max").getValue())).isValid()) isValid = false;
					}
				}
				if(!isValid)
				{
					this.tabColumnGroups.setSelectedTab(i);
					return false;
				}
			}
		}		
		return true;
	}

	//New function added to validate Column Names
	private boolean validateTargetColsNames()
	{		
		for(int i=0; i<this.tabColumnGroups.getComponentCount(); i++)
		{
			VerticalLayout colGroupLayout = (VerticalLayout)this.tabColumnGroups.getTab(i).getComponent();
			HorizontalSplitPanel splitTblPanel = (HorizontalSplitPanel)colGroupLayout.getComponent(1);
			Table tblTargetReportColumns = (Table)splitTblPanel.getFirstComponent();
			
			Collection itemIds = tblTargetReportColumns.getContainerDataSource().getItemIds();
			for (Object itemIdObj : itemIds) 
			{	
				int itemId = (int)itemIdObj;
				Item tblRowItem = tblTargetReportColumns.getContainerDataSource().getItem(itemId);
				String addedColName =((TextField)(tblRowItem.getItemProperty("Column Label").getValue())).getValue().trim().toLowerCase();
			
				if(StringUtils.isEmpty(addedColName)) 
					return false;
					
				if(!StringUtils.isAlphanumeric(addedColName.replaceAll("_", "").replaceAll(" ", "")))
					return false;									
			}
		}
		
		return true;
	}

	//New function added to validate Report Name
	private boolean validateTargetReportName()
	{
		if(this.txtTargetRptName.getValue().toLowerCase().trim().equals("target report"))
			return false;
		
		TargetReport existentTargetRpt = new TargetReportDao().getTargetReportByName(this.txtTargetRptName.getValue().trim());
		
		if(existentTargetRpt != null && (this.targetReport == null || (this.targetReport.getTargetReportId() != existentTargetRpt.getTargetReportId())))
			return false;
					
		if(StringUtils.isEmpty(this.txtTargetRptName.getValue().trim())) 
			return false;
			
		if(!StringUtils.isAlphanumeric(this.txtTargetRptName.getValue().trim().replaceAll("_", "").replaceAll(" ", "")))
			return false;	
		
		return true;
	}

	private boolean validateWhatIfSelectedDateFilterField(String selectedWhatIfDateFilterFieldId)
	{
		VerticalLayout colGroupLayout = (VerticalLayout)this.tabColumnGroups.getTab(0).getComponent();
		HorizontalSplitPanel splitTblPanel = (HorizontalSplitPanel)colGroupLayout.getComponent(1);
		Table tblTargetReportColumns = (Table)splitTblPanel.getFirstComponent();
		
		Collection itemIds = tblTargetReportColumns.getContainerDataSource().getItemIds();
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblTargetReportColumns.getContainerDataSource().getItem(itemId);
			if(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue().toString().equals(selectedWhatIfDateFilterFieldId)) 
			{
				selectedDateRangeFieldColumnLabel = ((TextField)(tblRowItem.getItemProperty("Column Label").getValue())).getValue().trim();
				return true;
			}
		}
		this.tabColumnGroups.setSelectedTab(0);
		return false;
	}
}
