package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.ui.design.UnitOfMeasureConfigDesign;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

public class UnitOfMeasureConfigForm extends UnitOfMeasureConfigDesign{

	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();
	
	public UnitOfMeasureConfigForm()
	{
		loadTblData();
		
		this.btnAddUom.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addUomRow();
			}

		});
	
		this.btnDeleteUom.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deleteUomRow();
			}

		});
		
		this.btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				saveUomRows();
			}

		});

		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				Filterable filter= (Filterable) (tblUnitsOfMeasure.getContainerDataSource());
                filter.removeAllContainerFilters();

                String filterString = txtSearch.getValue();
                Like filterLike = new Like(cbxUnitsOfMeasureFilters.getValue().toString(), "%" + filterString + "%");
                filterLike.setCaseSensitive(false);
                
                if (filterString.length() > 0 && !cbxUnitsOfMeasureFilters.getValue().toString().isEmpty()) {
                    filter.addContainerFilter(filterLike);
                }
			}
			});

		btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				 loadTblData();
			}

		});
	}
	
	private void loadTblData()
	{
		dbIdOfItemsToDelete = new ArrayList<Integer>();
		
		this.tblUnitsOfMeasure.setContainerDataSource(null);
		this.tblUnitsOfMeasure.addContainerProperty("*", CheckBox.class, null);
		this.tblUnitsOfMeasure.addContainerProperty("Name", TextField.class, null);
		this.tblUnitsOfMeasure.addContainerProperty("Abbreviation", TextField.class, null);
		this.tblUnitsOfMeasure.setEditable(true);
		this.tblUnitsOfMeasure.setPageLength(0);
		tblUnitsOfMeasure.setColumnWidth("*", 20);

		cbxUnitsOfMeasureFilters.addItem("Name");
		cbxUnitsOfMeasureFilters.addItem("Abbreviation");
		cbxUnitsOfMeasureFilters.select("Name");
		
		Object[] itemValues = new Object[3];

		List<UnitOfMeasure> unitOfMeasures = new UnitOfMeasureDao().getAllUnitOfMeasures();
		
		for(int i=0; unitOfMeasures != null && i<unitOfMeasures.size(); i++)
		{
			CheckBox chxSelect = new CheckBox();
			chxSelect.setVisible(false);
			itemValues[0] = chxSelect;
						
			TextField txtUomName = new TextField();
			txtUomName.setStyleName("tiny");
			txtUomName.setValue(unitOfMeasures.get(i).getUomName());		
			txtUomName.setWidth(97, Unit.PERCENTAGE);
			itemValues[1] = txtUomName;
			
			TextField txtUomAbbreviation = new TextField();
			txtUomAbbreviation.setStyleName("tiny");
			txtUomAbbreviation.setValue(unitOfMeasures.get(i).getUomAbbreviation());		
			txtUomAbbreviation.setWidth(97, Unit.PERCENTAGE);			
			itemValues[2] = txtUomAbbreviation;
			
			this.tblUnitsOfMeasure.addItem(itemValues, unitOfMeasures.get(i).getUomId());
		}
	}
	
	private void addUomRow()
	{
		Object[] itemValues = new Object[3];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		itemValues[0] = chxSelect;
		
		TextField txtUomName = new TextField();
		txtUomName.setStyleName("tiny");
		txtUomName.setValue("");		
		txtUomName.setWidth(97, Unit.PERCENTAGE);
		itemValues[1] = txtUomName;
		
		TextField txtUomAbbreviation = new TextField();
		txtUomAbbreviation.setStyleName("tiny");
		txtUomAbbreviation.setValue("");		
		txtUomAbbreviation.setWidth(97, Unit.PERCENTAGE);			
		itemValues[2] = txtUomAbbreviation;
		
		this.lastNewItemId = this.lastNewItemId - 1;
		this.tblUnitsOfMeasure.addItem(itemValues, this.lastNewItemId);
		this.tblUnitsOfMeasure.select(this.lastNewItemId);
	}

	private void deleteUomRow()
	{
		boolean hasExperimentFieldsLinked = false;
		if(this.tblUnitsOfMeasure.getValue() != null && (int)this.tblUnitsOfMeasure.getValue() > 0 )
		{
			if(new ExperimentFieldDao().getAllExperimentFieldsByUnitOfMeasureId((int)this.tblUnitsOfMeasure.getValue()).size() <= 0)
				dbIdOfItemsToDelete.add((int)this.tblUnitsOfMeasure.getValue());
			else
				hasExperimentFieldsLinked = true;
		}
		
		if(!hasExperimentFieldsLinked && this.tblUnitsOfMeasure.getValue() != null)
			this.tblUnitsOfMeasure.removeItem((int)this.tblUnitsOfMeasure.getValue());
		else if(hasExperimentFieldsLinked)
			this.getUI().showNotification("Unit Of Measure Record can not be deleted. \nThere are Experiment Fields linked.", Type.WARNING_MESSAGE);
			
	}

	private void saveUomRows()
	{
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateDuplicatedNamesResult = validateDuplicatedNames();
		
		if(validateRequiredFieldsResult && validateDuplicatedNamesResult)
		{
			UnitOfMeasureDao uomDao = new UnitOfMeasureDao();
		
			//Delete Items in DB
			for(int i = 0; i<dbIdOfItemsToDelete.size(); i++)
				uomDao.deleteUnitOfMeasure((int)dbIdOfItemsToDelete.get(i));
		
			Collection itemIds = this.tblUnitsOfMeasure.getContainerDataSource().getItemIds();
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblUnitsOfMeasure.getContainerDataSource().getItem(itemId);
				
				UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
				
				unitOfMeasure.setUomName(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
				unitOfMeasure.setUomAbbreviation(((TextField)(tblRowItem.getItemProperty("Abbreviation").getValue())).getValue());
							
				if(itemId > 0)
				{
					unitOfMeasure.setUomId(itemId);
					uomDao.updateUnitOfMeasure(unitOfMeasure);
				}
				else
					uomDao.addUnitOfMeasure(unitOfMeasure);
			}

			this.getUI().showNotification("Data Saved.", Type.HUMANIZED_MESSAGE);
			loadTblData();
		}
		else if(!validateRequiredFieldsResult)
			this.getUI().showNotification("Name and Abbreviation must be set for New Unit Of Measure Records", Type.WARNING_MESSAGE);
		else if(!validateDuplicatedNamesResult)
			this.getUI().showNotification("Name of Unit Of Measure can not be duplicated.", Type.WARNING_MESSAGE);
	}
	
	private boolean validateRequiredFields()
	{	
		Collection itemIds = this.tblUnitsOfMeasure.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblUnitsOfMeasure.getContainerDataSource().getItem(itemId);
			
			if(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue().isEmpty())
			{
				tblUnitsOfMeasure.select(itemId);
				return false;
			}
			if(((TextField)(tblRowItem.getItemProperty("Abbreviation").getValue())).getValue().isEmpty())
			{
				tblUnitsOfMeasure.select(itemId);
				return false;
			}
		}
		
		return true;
	}

	private boolean validateDuplicatedNames()
	{
		List<String> fileRepoNames = new ArrayList<String>();
		
		Collection itemIds = this.tblUnitsOfMeasure.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblUnitsOfMeasure.getContainerDataSource().getItem(itemId);
			
			if(fileRepoNames.indexOf(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue()) >= 0)
			{
				tblUnitsOfMeasure.select(itemId);
				return false;
			}
			else
				fileRepoNames.add(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
		}
		
		return true;
	}

}
