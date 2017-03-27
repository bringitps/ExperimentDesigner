package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.dao.CsvTemplateDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.ui.design.ContractManufacturerConfigDesign;
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

public class ContractManufacturerConfigForm extends ContractManufacturerConfigDesign{
	
	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();
	
	public ContractManufacturerConfigForm()
	{
		loadTblData("","");
		
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
				if(cbxContractManufacturerFilters.getValue() != null) 
					loadTblData(cbxContractManufacturerFilters.getValue().toString(), txtSearch.getValue());				
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
		
		tblContractManufacturer.setContainerDataSource(null);
		tblContractManufacturer.addContainerProperty("*", CheckBox.class, null);
		tblContractManufacturer.addContainerProperty("Name", TextField.class, null);
		tblContractManufacturer.addContainerProperty("Abbreviation", TextField.class, null);
		tblContractManufacturer.addContainerProperty("Description", TextField.class, null);
		tblContractManufacturer.addContainerProperty("E-mail", TextField.class, null);
		tblContractManufacturer.setEditable(true);
		tblContractManufacturer.setPageLength(0);
		tblContractManufacturer.setColumnWidth("*", 20);

		cbxContractManufacturerFilters.setContainerDataSource(null);
		cbxContractManufacturerFilters.addItem("Name");
		cbxContractManufacturerFilters.addItem("Abbreviation");
		cbxContractManufacturerFilters.addItem("Description");
		cbxContractManufacturerFilters.addItem("E-mail");
		
		if(filterName.isEmpty())
			cbxContractManufacturerFilters.select("Name");
		else
			cbxContractManufacturerFilters.select(filterName.trim());
		
		Object[] itemValues = new Object[5];

		List<ContractManufacturer> contractManufacturers = new ContractManufacturerDao().getAllContractManufacturers();
		
		
		for(int i=0; contractManufacturers != null && i<contractManufacturers.size(); i++)
		{
			if(filterName.isEmpty() 
					|| (filterName.trim().equals("Name") && contractManufacturers.get(i).getCmName().toLowerCase().contains(filterValue.trim().toLowerCase()))
					|| (filterName.trim().equals("Abbreviation") && contractManufacturers.get(i).getCmAbbreviation().toLowerCase().contains(filterValue.trim().toLowerCase()))
					|| (filterName.trim().equals("Description") && contractManufacturers.get(i).getCmDescription().toLowerCase().contains(filterValue.trim().toLowerCase()))
					|| (filterName.trim().equals("E-mail") && contractManufacturers.get(i).getCmEmail().toLowerCase().contains(filterValue.trim().toLowerCase())))
			{
				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				itemValues[0] = chxSelect;
	
				TextField txtCmName = new TextField();
				txtCmName.setStyleName("tiny");
				txtCmName.setValue(contractManufacturers.get(i).getCmName());		
				txtCmName.setWidth(97, Unit.PERCENTAGE);
				itemValues[1] = txtCmName;
				
				TextField txtCmAbbreviation = new TextField();
				txtCmAbbreviation.setStyleName("tiny");
				txtCmAbbreviation.setValue(contractManufacturers.get(i).getCmAbbreviation());		
				txtCmAbbreviation.setWidth(97, Unit.PERCENTAGE);
				itemValues[2] = txtCmAbbreviation;
				
				TextField txtCmDescription = new TextField();
				txtCmDescription.setStyleName("tiny");
				txtCmDescription.setValue(contractManufacturers.get(i).getCmDescription());		
				txtCmDescription.setWidth(97, Unit.PERCENTAGE);
				itemValues[3] = txtCmDescription;
				
				TextField txtCmEmail = new TextField();
				txtCmEmail.setStyleName("tiny");
				txtCmEmail.setValue(contractManufacturers.get(i).getCmEmail());		
				txtCmEmail.setWidth(97, Unit.PERCENTAGE);
				itemValues[4] = txtCmEmail;
				
				tblContractManufacturer.addItem(itemValues, contractManufacturers.get(i).getCmId());
			}
		}
	}
	
	private void addContractManufacturerRow()
	{
		Object[] itemValues = new Object[5];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		itemValues[0] = chxSelect;


		TextField txtCmName = new TextField();
		txtCmName.setStyleName("tiny");
		txtCmName.setValue("");		
		txtCmName.setWidth(97, Unit.PERCENTAGE);
		itemValues[1] = txtCmName;
		
		TextField txtCmAbbreviation = new TextField();
		txtCmAbbreviation.setStyleName("tiny");
		txtCmAbbreviation.setValue("");		
		txtCmAbbreviation.setWidth(97, Unit.PERCENTAGE);
		itemValues[2] = txtCmAbbreviation;
		
		TextField txtCmDescription = new TextField();
		txtCmDescription.setStyleName("tiny");
		txtCmDescription.setValue("");		
		txtCmDescription.setWidth(97, Unit.PERCENTAGE);
		itemValues[3] = txtCmDescription;
		
		TextField txtCmEmail = new TextField();
		txtCmEmail.setStyleName("tiny");
		txtCmEmail.setValue("");		
		txtCmEmail.setWidth(97, Unit.PERCENTAGE);
		itemValues[4] = txtCmEmail;
		
		/*
		itemValues[1] = new String();
		itemValues[2] = new String();
		itemValues[3] = new String();
		itemValues[4] = new String();
		*/
		
		this.lastNewItemId = this.lastNewItemId - 1;
		tblContractManufacturer.addItem(itemValues, this.lastNewItemId);
		tblContractManufacturer.select(this.lastNewItemId);
	}
	
	private void deleteContractManufacturerRow()
	{
		//dbIdOfItemsToDelete.add((int)tblContractManufacturer.getValue());
		//tblContractManufacturer.removeItem((int)tblContractManufacturer.getValue());
		
		boolean hasTemplatesLinked = false;
		if(this.tblContractManufacturer.getValue() != null && (int)this.tblContractManufacturer.getValue() > 0 )
		{
			if(new CsvTemplateDao().getCsvTemplatesByCmId((int)this.tblContractManufacturer.getValue()).size() == 0
					&& new XmlTemplateDao().getXmlTemplatesByCmId((int)this.tblContractManufacturer.getValue()).size() == 0)
				dbIdOfItemsToDelete.add((int)this.tblContractManufacturer.getValue());
			else
				hasTemplatesLinked = true;
		}
		
		if(!hasTemplatesLinked && this.tblContractManufacturer.getValue() != null)
			this.tblContractManufacturer.removeItem((int)this.tblContractManufacturer.getValue());
		else if(hasTemplatesLinked)
			this.getUI().showNotification("Contract Manufacturer Record can not be deleted. \nThere are Xml or Csv Templates linked.", Type.WARNING_MESSAGE);
			
	}
	


	private void saveContractManufacturerRows()
	{
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateEmailAddressesResult = validateEmailAddresses();
		boolean validateDuplicatedNamesResult = validateDuplicatedNames();
		
		if(validateRequiredFieldsResult && validateEmailAddressesResult && validateDuplicatedNamesResult)
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
				contractManufacturer.setCmName(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
				contractManufacturer.setCmAbbreviation(((TextField)(tblRowItem.getItemProperty("Abbreviation").getValue())).getValue());
				contractManufacturer.setCmDescription(((TextField)(tblRowItem.getItemProperty("Description").getValue())).getValue());
				contractManufacturer.setCmEmail(((TextField)(tblRowItem.getItemProperty("E-mail").getValue())).getValue());
							
				if(itemId > 0)
				{
					contractManufacturer.setCmId(itemId);
					contractManufacturerDao.updateContractManufacturer(contractManufacturer);
				}
				else
					contractManufacturerDao.addContractManufacturer(contractManufacturer);
			}

			this.getUI().showNotification("Data Saved.", Type.HUMANIZED_MESSAGE);
			cbxContractManufacturerFilters.select("Name");
			txtSearch.setValue("");
			loadTblData("","");
		}
		else if(!validateRequiredFieldsResult)
			this.getUI().showNotification("Name must be set for New Contract Manufacturer Records", Type.WARNING_MESSAGE);
		else if(!validateEmailAddressesResult)	
			this.getUI().showNotification("Invalid E-mail addresses found. Please check.", Type.WARNING_MESSAGE);
		else if(!validateDuplicatedNamesResult)
			this.getUI().showNotification("Name of Contract Manufacturer can not be duplicated.", Type.WARNING_MESSAGE);
	}
	
	private boolean validateRequiredFields()
	{	
		Collection itemIds = tblContractManufacturer.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblContractManufacturer.getContainerDataSource().getItem(itemId);
			
			if(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue().isEmpty())
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
			
			if(!isValidEmailAddress(((TextField)(tblRowItem.getItemProperty("E-mail").getValue())).getValue()))
			{
				tblContractManufacturer.select(itemId);
				return false;
			}
		}
		
		return true;
	}
	

	private boolean validateDuplicatedNames()
	{
		List<String> fileRepoNames = new ArrayList<String>();
		
		Collection itemIds = this.tblContractManufacturer.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblContractManufacturer.getContainerDataSource().getItem(itemId);
			
			if(fileRepoNames.indexOf(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue()) >= 0)
			{
				tblContractManufacturer.select(itemId);
				return false;
			}
			else
				fileRepoNames.add(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
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
