package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.ui.design.ContractManufacturerConfigDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class ContractManufacturerConfigForm extends ContractManufacturerConfigDesign{
	
	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();
	
	public ContractManufacturerConfigForm()
	{
		loadTblData();
		
		this.btnAddJobExecRepeat.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addContractManufacturerRow();
			}

		});
	
		this.btnDeleteJobExecRepeat.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deleteContractManufacturerRow();
			}

		});
		
		this.btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				saveContractManufacturerRows();				
			}

		});

		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				Filterable filter= (Filterable) (tblContractManufacturer.getContainerDataSource());
                filter.removeAllContainerFilters();

                String filterString = txtSearch.getValue();
                Like filterLike = new Like(cbxContractManufacturerFilters.getValue().toString(), "%" + filterString + "%");
                filterLike.setCaseSensitive(false);
                
                if (filterString.length() > 0 && !cbxContractManufacturerFilters.getValue().toString().isEmpty()) {
                    filter.addContainerFilter(filterLike);
                }
			}
			});
	}
	
	private void loadTblData()
	{
		tblContractManufacturer.setContainerDataSource(null);
		tblContractManufacturer.addContainerProperty("*", CheckBox.class, null);
		tblContractManufacturer.addContainerProperty("Name", String.class, null);
		tblContractManufacturer.addContainerProperty("Abbreviation", String.class, null);
		tblContractManufacturer.addContainerProperty("Description", String.class, null);
		tblContractManufacturer.addContainerProperty("E-mail", String.class, null);
		tblContractManufacturer.setEditable(true);
		tblContractManufacturer.setPageLength(0);

		cbxContractManufacturerFilters.addItem("Name");
		cbxContractManufacturerFilters.addItem("Abbreviation");
		cbxContractManufacturerFilters.addItem("Description");
		cbxContractManufacturerFilters.select("E-mail");
		
		Object[] itemValues = new Object[5];

		List<ContractManufacturer> contractManufacturers = new ContractManufacturerDao().getAllContractManufacturers();
		
		
		for(int i=0; contractManufacturers != null && i<contractManufacturers.size(); i++)
		{
			CheckBox chxSelect = new CheckBox();
			chxSelect.setVisible(false);
			itemValues[0] = chxSelect;

			itemValues[1] = contractManufacturers.get(i).getCmName();
			itemValues[2] = contractManufacturers.get(i).getCmAbbreviation();
			itemValues[3] = contractManufacturers.get(i).getCmDescription();
			itemValues[4] = contractManufacturers.get(i).getCmEmail();
			
			tblContractManufacturer.addItem(itemValues, contractManufacturers.get(i).getCmId());
		}
	}
	
	private void addContractManufacturerRow()
	{
		Object[] itemValues = new Object[5];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		itemValues[0] = chxSelect;

		itemValues[1] = new String();
		itemValues[2] = new String();
		itemValues[3] = new String();
		itemValues[4] = new String();

		this.lastNewItemId = this.lastNewItemId - 1;
		tblContractManufacturer.addItem(itemValues, this.lastNewItemId);
		tblContractManufacturer.select(this.lastNewItemId);
	}
	
	private void deleteContractManufacturerRow()
	{
		dbIdOfItemsToDelete.add((int)tblContractManufacturer.getValue());
		tblContractManufacturer.removeItem((int)tblContractManufacturer.getValue());
		
		/*
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
		*/	
	}
	


	private void saveContractManufacturerRows()
	{
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateEmailAddressesResult = validateEmailAddresses();
		
		if(validateRequiredFieldsResult && validateEmailAddressesResult)
		{
			ContractManufacturerDao contractManufacturerDao = new ContractManufacturerDao();
		
			//Delete Items in DB
			for(int i = 0; i<dbIdOfItemsToDelete.size(); i++)
				contractManufacturerDao.deleteContractManufacturer((int)dbIdOfItemsToDelete.get(i));
		
			Collection itemIds = tblContractManufacturer.getContainerDataSource().getItemIds();
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = tblContractManufacturer.getContainerDataSource().getItem(itemId);
				
				ContractManufacturer contractManufacturer = new ContractManufacturer();
				contractManufacturer.setCmName((String)(tblRowItem.getItemProperty("Name").getValue()));
				contractManufacturer.setCmAbbreviation((String)(tblRowItem.getItemProperty("Abbreviation").getValue()));
				contractManufacturer.setCmDescription((String)(tblRowItem.getItemProperty("Description").getValue()));
				contractManufacturer.setCmEmail((String)(tblRowItem.getItemProperty("E-mail").getValue()));
							
				if(itemId > 0)
				{
					contractManufacturer.setCmId(itemId);
					contractManufacturerDao.updateContractManufacturer(contractManufacturer);
				}
				else
					contractManufacturerDao.addContractManufacturer(contractManufacturer);
			}

			this.getUI().showNotification("Data Saved.", Type.HUMANIZED_MESSAGE);
			loadTblData();
		}
		else if(!validateRequiredFieldsResult)
			this.getUI().showNotification("Name must be set for New Contract Manufacturer Records", Type.WARNING_MESSAGE);
		else if(!validateEmailAddressesResult)	
			this.getUI().showNotification("Invalid E-mail addresses found. Please check.", Type.WARNING_MESSAGE);
	}
	
	private boolean validateRequiredFields()
	{	
		Collection itemIds = tblContractManufacturer.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblContractManufacturer.getContainerDataSource().getItem(itemId);
			
			if(((String)(tblRowItem.getItemProperty("Name").getValue())).isEmpty())
			{
				tblContractManufacturer.select(itemId);
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateEmailAddresses()
	{
		Collection itemIds = tblContractManufacturer.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblContractManufacturer.getContainerDataSource().getItem(itemId);
			
			if(!isValidEmailAddress((String)(tblRowItem.getItemProperty("E-mail").getValue())))
			{
				tblContractManufacturer.select(itemId);
				return false;
			}
		}
		
		return true;
	}

	public boolean isValidEmailAddress(String email) 
	{
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}
	
}
