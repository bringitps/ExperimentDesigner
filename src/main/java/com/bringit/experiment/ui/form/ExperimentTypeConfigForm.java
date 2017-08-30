package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bringit.experiment.bll.ExperimentType;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.ExperimentTypeDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.ui.design.ExperimentTypeConfigDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class ExperimentTypeConfigForm extends ExperimentTypeConfigDesign {
	
	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();

    private SystemSettings systemSettings;
    
	public ExperimentTypeConfigForm()
	{
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		this.lblFormTitle.setValue("- " + this.systemSettings.getExperimentTypeLabel());
		this.btnAddExpType.setCaption("Add " + this.systemSettings.getExperimentTypeLabel());
		this.btnDeleteExpType.setCaption("Delete " + this.systemSettings.getExperimentTypeLabel());
		
		loadTblData("","");
		
		this.btnAddExpType.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addExpTypeRow();
			}

		});
	
		this.btnDeleteExpType.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deleteExpTypeRow();
			}

		});
		
		
		this.btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				saveExpTypeRows();
			}

		});

		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				if(cbxExperimentTypeFilters.getValue() != null)
					loadTblData(cbxExperimentTypeFilters.getValue().toString(), txtSearch.getValue());
			}
			});

		btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				 loadTblData("","");
			}

		});
	}
	
	
	
	private void loadTblData(String filterName, String filterValue)
	{
		dbIdOfItemsToDelete = new ArrayList<Integer>();
		
		tblExperimentType.setContainerDataSource(null);
		tblExperimentType.addContainerProperty("*", CheckBox.class, null);
		tblExperimentType.addContainerProperty("Name", TextField.class, null);
		tblExperimentType.addContainerProperty("Description", TextField.class, null);
		tblExperimentType.setEditable(true);
		tblExperimentType.setPageLength(0);
		tblExperimentType.setColumnWidth("*", 20);

		cbxExperimentTypeFilters.setContainerDataSource(null);
		cbxExperimentTypeFilters.addItem("Name");
		cbxExperimentTypeFilters.addItem("Description");
		
		if(filterName.isEmpty())
			cbxExperimentTypeFilters.select("Name");
		else
			cbxExperimentTypeFilters.select(filterName.trim());
			
		Object[] itemValues = new Object[3];

		List<ExperimentType> experimentTypes = new ExperimentTypeDao().getAllExperimentTypes();
		
		for(int i=0; experimentTypes != null && i<experimentTypes.size(); i++)
		{
			if(filterName.isEmpty() 
					|| (filterName.trim().equals("Name") && experimentTypes.get(i).getExpTypeName().toLowerCase().contains(filterValue.trim().toLowerCase()))
					|| (filterName.trim().equals("Description") && experimentTypes.get(i).getExpTypeDescription().toLowerCase().contains(filterValue.trim().toLowerCase())))
			{
				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				itemValues[0] = chxSelect;
							
				TextField txtExpTypeName = new TextField();
				txtExpTypeName.setStyleName("tiny");
				txtExpTypeName.setValue(experimentTypes.get(i).getExpTypeName());		
				txtExpTypeName.setWidth(97, Unit.PERCENTAGE);
				itemValues[1] = txtExpTypeName;
				
				TextField txtExpTypeDescription = new TextField();
				txtExpTypeDescription.setStyleName("tiny");
				txtExpTypeDescription.setValue(experimentTypes.get(i).getExpTypeDescription());		
				txtExpTypeDescription.setWidth(97, Unit.PERCENTAGE);			
				itemValues[2] = txtExpTypeDescription;
				
				tblExperimentType.addItem(itemValues, experimentTypes.get(i).getExpTypeId());
			}
		}
	}
	
	private void addExpTypeRow()
	{
		Object[] itemValues = new Object[3];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		itemValues[0] = chxSelect;
		
		TextField txtExpTypeName = new TextField();
		txtExpTypeName.setStyleName("tiny");
		txtExpTypeName.setValue("");		
		txtExpTypeName.setWidth(97, Unit.PERCENTAGE);
		itemValues[1] = txtExpTypeName;
		
		TextField txtExpTypeDescription = new TextField();
		txtExpTypeDescription.setStyleName("tiny");
		txtExpTypeDescription.setValue("");		
		txtExpTypeDescription.setWidth(97, Unit.PERCENTAGE);			
		itemValues[2] = txtExpTypeDescription;
		
		this.lastNewItemId = this.lastNewItemId - 1;
		tblExperimentType.addItem(itemValues, this.lastNewItemId);
		tblExperimentType.select(this.lastNewItemId);
	}

	private void deleteExpTypeRow()
	{
		dbIdOfItemsToDelete.add((int)tblExperimentType.getValue());
		tblExperimentType.removeItem((int)tblExperimentType.getValue());
	}

	private void saveExpTypeRows()
	{
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateDuplicatedNamesResult = validateDuplicatedNames();
		
		if(validateRequiredFieldsResult && validateDuplicatedNamesResult)
		{
			ExperimentTypeDao expTypeDao = new ExperimentTypeDao();
		
			//Delete Items in DB
			for(int i = 0; i<dbIdOfItemsToDelete.size(); i++)
				expTypeDao.deleteExperimentType((int)dbIdOfItemsToDelete.get(i));
		
			Collection itemIds = tblExperimentType.getContainerDataSource().getItemIds();
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = tblExperimentType.getContainerDataSource().getItem(itemId);
				
				ExperimentType experimentType = new ExperimentType();
				
				experimentType.setExpTypeName(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
				experimentType.setExpTypeDescription(((TextField)(tblRowItem.getItemProperty("Description").getValue())).getValue());
							
				if(itemId > 0)
				{
					experimentType.setExpTypeId(itemId);
					expTypeDao.updateExperimentType(experimentType);
				}
				else
					expTypeDao.addExperimentType(experimentType);
			}

			this.getUI().showNotification("Data Saved.", Type.HUMANIZED_MESSAGE);
			cbxExperimentTypeFilters.select("Name");
			txtSearch.setValue("");
			loadTblData("","");
		}
		else if(!validateRequiredFieldsResult)
			this.getUI().showNotification("Name and Description must be set for New Experiment Type Records", Type.WARNING_MESSAGE);
		else if(!validateDuplicatedNamesResult)
			this.getUI().showNotification("Name of Experiment Type can not be duplicated.", Type.WARNING_MESSAGE);
	}
	
	private boolean validateRequiredFields()
	{	
		Collection itemIds = tblExperimentType.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblExperimentType.getContainerDataSource().getItem(itemId);
			
			if(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue().isEmpty())
			{
				tblExperimentType.select(itemId);
				return false;
			}
			if(((TextField)(tblRowItem.getItemProperty("Description").getValue())).getValue().isEmpty())
			{
				tblExperimentType.select(itemId);
				return false;
			}
		}
		
		return true;
	}

	private boolean validateDuplicatedNames()
	{
		List<String> fileRepoNames = new ArrayList<String>();
		
		Collection itemIds = tblExperimentType.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblExperimentType.getContainerDataSource().getItem(itemId);
			
			if(fileRepoNames.indexOf(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue()) >= 0)
			{
				tblExperimentType.select(itemId);
				return false;
			}
			else
				fileRepoNames.add(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
		}
		
		return true;
	}

}
