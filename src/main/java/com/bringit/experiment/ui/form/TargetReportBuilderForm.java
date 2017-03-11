package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.TargetColumn;
import com.bringit.experiment.bll.TargetColumnGroup;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.ui.design.TargetReportBuilderDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.FontAwesome;
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

public class TargetReportBuilderForm extends TargetReportBuilderDesign {
	
	private TargetReport targetReport = new TargetReport();
	private Experiment experiment = new Experiment();
	private List<Experiment> experiments = new ExperimentDao().getActiveExperiments();
    private List<ExperimentField> expFields;
    private int lastNewItemId = 0;
	private List<Integer> dbIdOfTargetColumnGroupsToDelete = new ArrayList<Integer>();
	private List<Integer> dbIdOfTargetColumnsToDelete = new ArrayList<Integer>();
	
	
	public TargetReportBuilderForm(int targetReportId)
	{	
		enableComponents(false);
		
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
		
		gridInfoColGroup.setMargin(new MarginInfo(false, true, false, true));
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
		tblTargetReportInfoColumns.addContainerProperty("Experiment Field", ComboBox.class, null);
		tblTargetReportInfoColumns.addContainerProperty("Column Label", TextField.class, null);
		tblTargetReportInfoColumns.setPageLength(100);
		tblTargetReportInfoColumns.setWidth(100, Unit.PERCENTAGE);
		tblTargetReportInfoColumns.setHeight(150, Unit.PIXELS);
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

				if(tblTargetReportInfoColumns.getValue() != null && (int)tblTargetReportInfoColumns.getValue() < 0)
					tblTargetReportInfoColumns.removeItem((int)tblTargetReportInfoColumns.getValue());
			}

		});
		

		HorizontalSplitPanel splitTblPanel = new HorizontalSplitPanel();
		splitTblPanel.setSplitPosition(82, Unit.PERCENTAGE);
		splitTblPanel.setLocked(true);
		splitTblPanel.addComponent(tblTargetReportInfoColumns);
		splitTblPanel.addComponent(gridBtnsReportColumns);

		infoColGroupLayout.addComponent(splitTblPanel);
		
		cbxExperiment.setNullSelectionAllowed(false);
		for(int j=0; j<experiments.size(); j++)
		{
			cbxExperiment.addItem(experiments.get(j).getExpId());
			cbxExperiment.setItemCaption(experiments.get(j).getExpId(), experiments.get(j).getExpName());
		}
		
		btnAddColumnGroup.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addColumnGroup(-1);
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
					//dbIdOfTargetColumnGroupsToDelete
					tabColumnGroups.removeComponent(tabColumnGroups.getSelectedTab());
				}
			}

		});
		
		cbxExperiment.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbxExperiment.getValue()!=null)
				{
					experiment = new ExperimentDao().getExperimentById(Integer.parseInt(cbxExperiment.getValue().toString()));
					expFields = new ExperimentFieldDao().getActiveExperimentFields(experiment);
					enableComponents(true);
					if(targetReportId == -1)
					{
						chxActive.setValue(true);
						btnDeleteLayout.setEnabled(false);		
					}
				}
				else
				{
					experiment = new Experiment();
					enableComponents(false);
				}
			}   
	    });
		
		
		btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onSave();
			}

		});
		
		
		if(targetReportId == -1)
			chxActive.setValue(true);
		else
		{
			//Load Target Report Data
		}
		
		
	}
	
	private void onSave()
	{
		buildTargetReport();
		
		boolean validateReqFieldsResult = validateRequiredFields();
		/*boolean validateDbNameFieldsResult = validateDbNameFields();
		boolean validateDuplicateDbTableNameResult = validateDuplicateDbTableName();
		boolean validateDuplicatedDbNameFieldsResult = validateDuplicatedDbNameFields();
		boolean validateRestrictedDbTableFieldsResult = validateRestrictedDbTableFields();*/
		
		if(validateReqFieldsResult)
		{
				
		}
		else
		{
			//if(itemIds.size() <= 0)
			//	this.getUI().showNotification("Experiment must contain at least 1 Experiment Field", Type.WARNING_MESSAGE);
			//else 
			if(!validateReqFieldsResult)
				this.getUI().showNotification("Please fill in all required Fields", Type.WARNING_MESSAGE);
		}	
	}

	
	private void buildTargetReport()
	{
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		
		//New Target Report
		if(this.targetReport.getTargetReportId() == null)
		{
			this.targetReport.setTargetReportIsActive(this.chxActive.getValue());
			this.targetReport.setTargetReportName(this.txtTargetRptName.getValue());
			this.targetReport.setTargetReportDescription(this.txtTargetRptDescription.getValue());
			this.targetReport.setCreatedDate(new Date());
			this.targetReport.setModifiedDate(new Date());
			this.targetReport.setCreatedBy(sessionUser);
			this.targetReport.setLastModifiedBy(sessionUser);
			this.targetReport.setExperiment(experiment);
			
			List<TargetColumnGroup> targetRptColGroups = new ArrayList<TargetColumnGroup>();
			
			for(int i=0; i<this.tabColumnGroups.getComponentCount(); i++)
			{

				VerticalLayout infoColGroupLayout = (VerticalLayout)this.tabColumnGroups.getTab(i).getComponent();
				TargetColumnGroup targetRptColGroup = new TargetColumnGroup();
				
				//targetRptColGroup.setTargetColumnGroupIsFail(targetColumnGroupIsFail);
				
				HorizontalSplitPanel splitTblPanel = (HorizontalSplitPanel)infoColGroupLayout.getComponent(1);
				Table tblTargetReportInfoColumns = (Table)splitTblPanel.getFirstComponent();
				//infoColGroupLayout.getComponent(0).getCaption();
				System.out.println("Group: " + infoColGroupLayout.getCaption() + " Tbl Rows:" + tblTargetReportInfoColumns.getItemIds().size() + "\n");
			}
			//Information Column
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
	
	private void addColumnGroup(int fieldGroupId)
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
                	newTxtInfoColumnGroupName.setValue("Informatio");
                	tabColumnGroups.getTab(selectedTabLayout).setCaption("Informatio");
                	return;
                }
                else
                	tabColumnGroups.getTab(selectedTabLayout).setCaption(event.getText());
            }
        });
		
		gridNewColumnGroup.addComponent(newTxtInfoColumnGroupName, 0, 0);
		gridNewColumnGroup.setComponentAlignment(newTxtInfoColumnGroupName, Alignment.MIDDLE_LEFT);
		
		CheckBox chxIsFailGroup = new CheckBox();
		chxIsFailGroup.setStyleName("tiny");
		chxIsFailGroup.setCaption("Is Fail Group?");
		gridNewColumnGroup.addComponent(chxIsFailGroup, 1, 0);
		gridNewColumnGroup.setComponentAlignment(chxIsFailGroup, Alignment.MIDDLE_RIGHT);
		gridNewColumnGroup.setMargin(new MarginInfo(false, true, false, true));
		gridNewColumnGroup.setSpacing(true);
		
		newColumnGroupLayout.addComponent(gridNewColumnGroup);
		newColumnGroupLayout.setCaption("New Column Group #" + tabColumnGroups.getComponentCount());
		tabColumnGroups.addTab(newColumnGroupLayout);
		tabColumnGroups.setSelectedTab(newColumnGroupLayout);
		
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
		tblTargetReportColumns.addContainerProperty("Experiment Field", ComboBox.class, null);
		tblTargetReportColumns.addContainerProperty("Column Label", TextField.class, null);
		tblTargetReportColumns.addContainerProperty("Offset", TextField.class, null);
		tblTargetReportColumns.addContainerProperty("Goal", TextField.class, null);
		tblTargetReportColumns.addContainerProperty("Min", TextField.class, null);
		tblTargetReportColumns.addContainerProperty("Max", TextField.class, null);
		tblTargetReportColumns.setPageLength(100);
		tblTargetReportColumns.setWidth(100, Unit.PERCENTAGE);
		tblTargetReportColumns.setHeight(150, Unit.PIXELS);
		tblTargetReportColumns.setStyleName("tiny");
		tblTargetReportColumns.setSelectable(true);
		tblTargetReportColumns.setMultiSelect(false);
		
		HorizontalSplitPanel splitTblPanel = new HorizontalSplitPanel();
		splitTblPanel.setSplitPosition(82, Unit.PERCENTAGE);
		splitTblPanel.setLocked(true);
		splitTblPanel.addComponent(tblTargetReportColumns);
		splitTblPanel.addComponent(gridBtnsReportColumns);
		
		newColumnGroupLayout.addComponent(splitTblPanel);
		
		btnAddColumn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				addColumnTblItem(tblTargetReportColumns, null);
			}

		});
		
		btnRemoveColumn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				if(tblTargetReportColumns.getValue() != null && (int)tblTargetReportColumns.getValue() < 0)
					tblTargetReportColumns.removeItem((int)tblTargetReportColumns.getValue());
			}

		});
		
		
		
		if(fieldGroupId == -1)
		{
			//.setCaption("New Field Group");
			//tabFieldGroups.getSelectedTab().set
			/*
			headerGrid.addComponent(imgLogo, 0, 0);
			
			TextField txtField = new TextField();
			txtField.setCaption("Testing TextFields " + this.tabFieldGroups.getComponentCount() );
			VerticalLayout tab1 = new VerticalLayout();
			tab1.setId("Testing TextFields " + this.tabFieldGroups.getComponentCount() );
			tab1.addComponent(txtField);
			
			this.tabFieldGroups.addTab(tab1).setCaption("My Tab " + + this.tabFieldGroups.getComponentCount());
			*/
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
		
		TextField txtOffset = new TextField();
		txtOffset.setRequired(true);
		txtOffset.setStyleName("tiny");
		txtOffset.setWidth(65, Unit.PIXELS);
		itemValues[3] = txtOffset;

		TextField txtGoal = new TextField();
		txtGoal.setRequired(true);
		txtGoal.setStyleName("tiny");
		txtGoal.setWidth(65, Unit.PIXELS);
		itemValues[4] = txtGoal;
		
		TextField txtMin = new TextField();
		txtMin.setRequired(true);
		txtMin.setStyleName("tiny");
		txtMin.setWidth(65, Unit.PIXELS);
		itemValues[5] = txtMin;
		
		TextField txtMax = new TextField();
		txtMax.setRequired(true);
		txtMax.setStyleName("tiny");
		txtMax.setWidth(65, Unit.PIXELS);
		itemValues[6] = txtMax;
		
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
			lastNewItemId = lastNewItemId - 1;
			itemId = lastNewItemId;
		}
		
		targetReportColumnTable.addItem(itemValues, itemId);
		targetReportColumnTable.select(itemId);
		targetReportColumnTable.setPageLength(0);		
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

		int itemId = -1;
		if(targetColumn!=null)
		{
			itemId = targetColumn.getTargetColumnId();
			cbxExperimentField.setValue(targetColumn.getExperimentField().getExpFieldId());
			txtColumnLabel.setValue(targetColumn.getTargetColumnLabel());
		}
		else
		{
			lastNewItemId = lastNewItemId - 1;
			itemId = lastNewItemId;
		}
		
		targetReportColumnTable.addItem(itemValues, lastNewItemId);
		targetReportColumnTable.select(lastNewItemId);
		targetReportColumnTable.setPageLength(0);
	}
	
	private boolean validateRequiredFields()
	{
		if(!this.txtTargetRptName.isValid() || !this.cbxExperiment.isValid()) return false;
		
		/*
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
			
			if(!((TextField)(tblRowItem.getItemProperty("Name").getValue())).isValid()) return false;
			if(!((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).isValid()) return false;			
		}
		*/
		return true;
	}
}
