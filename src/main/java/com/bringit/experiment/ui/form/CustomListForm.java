package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.CustomList;
import com.bringit.experiment.bll.CustomListValue;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.CustomListDao;
import com.bringit.experiment.dao.CustomListValueDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.ui.design.CustomListDesign;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class CustomListForm extends CustomListDesign{

	private CustomList customList;
	private List<CustomListValue> customListValues;
	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();
	
	public CustomListForm(int customListId)
	{
		if(customListId == -1) //New Experiment
		{
			this.btnDelete.setEnabled(false);
			this.customList = new CustomList();
			this.customListValues = null;
		}
		else
		{
			this.customList = new CustomListDao().getCustomListById(customListId);
			this.customListValues = new CustomListValueDao().getAllCustomListValuesByCustomList(this.customList);
			this.txtCustomListName.setValue(this.customList.getCustomListName());
			this.txtCustomListDescription.setValue(this.customList.getCustomListDescription());
		}
		
		loadTblData();
		
		this.txtCustomListName.focus();
		
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
		
		
		btnDelete.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onDelete();
			}

		});
		
		
		btnAddValue.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addCustomListValue();
			}

		});
		

		btnDeleteValue.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				deleteCustomListValue();
			}
		});

	}
	
	private void loadTblData()
	{
		this.tblCustomListValues.setContainerDataSource(null);
		this.tblCustomListValues.addContainerProperty("*", CheckBox.class, null);
		this.tblCustomListValues.addContainerProperty("Value", TextField.class, null);
		this.tblCustomListValues.setEditable(true);
		this.tblCustomListValues.setPageLength(0);
		tblCustomListValues.setColumnWidth("*", 20);
		
		for(int i=0; this.customListValues != null && i<this.customListValues.size(); i++)
		{
			Object[] itemValues = new Object[2];
			
			CheckBox chxSelect = new CheckBox();
			chxSelect.setVisible(false);
			itemValues[0] = chxSelect;
			
			TextField txtCustomListValue = new TextField();
			txtCustomListValue.setStyleName("tiny");
			txtCustomListValue.setValue(this.customListValues.get(i).getCustomListValueString());		
			txtCustomListValue.setWidth(97, Unit.PERCENTAGE);
			itemValues[1] = txtCustomListValue;
			
			this.lastNewItemId = this.lastNewItemId - 1;
			this.tblCustomListValues.addItem(itemValues, this.customListValues.get(i).getCustomListValueId());
		}
	}
	
	private void onSave()
	{
		Collection itemIds = this.tblCustomListValues.getContainerDataSource().getItemIds();
		if(!this.txtCustomListName.getValue().isEmpty())
		{
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");

			this.customList.setCustomListName(this.txtCustomListName.getValue());
			this.customList.setCustomListDescription(this.txtCustomListDescription.getValue());
			this.customList.setLastModifiedBy(sessionUser);
			this.customList.setModifiedDate(new Date());
			
			CustomListDao customListDao = new CustomListDao();
			
			if(this.customList.getCustomListId() != null )
				customListDao.updateCustomList(this.customList);
			else
			{
				this.customList.setCreatedBy(sessionUser);
				this.customList.setCreatedDate(this.customList.getModifiedDate());
				customListDao.addCustomList(this.customList);
			}
		
			CustomListValueDao customListValueDao = new CustomListValueDao();
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblCustomListValues.getContainerDataSource().getItem(itemId);
				
				CustomListValue customListValue = new CustomListValue();
				customListValue.setCustomListValueString(((TextField)(tblRowItem.getItemProperty("Value").getValue())).getValue());
				customListValue.setCustomList(this.customList);
				
				
				if(itemId > 0)
				{
					customListValue.setCustomListValueId(itemId);
					customListValueDao.updateCustomListValue(customListValue);
				}
				else
					customListValueDao.addCustomListValue(customListValue);
			}
			
			if(this.dbIdOfItemsToDelete.size() > 0)
			{
				for(int i=0; i<this.dbIdOfItemsToDelete.size(); i++)
				{
					if(this.dbIdOfItemsToDelete.get(i)>0)
						customListValueDao.deleteCustomListValue(this.dbIdOfItemsToDelete.get(i));
				}
			}
			
			WebApplication webApp = (WebApplication)this.getParent().getParent();
			webApp.reloadMainForm("custom lists");
			closeModalWindow();
		}
		else
			this.getUI().showNotification("Please fill in all required Fields", Type.WARNING_MESSAGE);
	}
	
	private void onDelete()
	{
		CustomListDao customListDao = new CustomListDao();
		CustomListValueDao customListValueDao = new CustomListValueDao();
		
		//Validate if Custom List is not being used for dependencies across system modules
		Collection itemIds = this.tblCustomListValues.getContainerDataSource().getItemIds();
		for (Object itemIdObj : itemIds) 
		{
			int itemId = (int)itemIdObj;
			if(itemId > 0)
				customListValueDao.deleteCustomListValue(itemId);
		}
		
		customListDao.deleteCustomList(this.customList.getCustomListId());
		
		WebApplication webApp = (WebApplication)this.getParent().getParent();
		webApp.reloadMainForm("custom lists");
		closeModalWindow();
	}

	private void addCustomListValue()
	{
		Object[] itemValues = new Object[2];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		itemValues[0] = chxSelect;
		
		TextField txtCustomListValue = new TextField();
		txtCustomListValue.setStyleName("tiny");
		txtCustomListValue.setValue("");		
		txtCustomListValue.setWidth(97, Unit.PERCENTAGE);
		itemValues[1] = txtCustomListValue;
		
		this.lastNewItemId = this.lastNewItemId - 1;
		this.tblCustomListValues.addItem(itemValues, this.lastNewItemId);
		this.tblCustomListValues.select(this.lastNewItemId);
		txtCustomListValue.focus();
	}

	private void deleteCustomListValue()
	{
		if(this.tblCustomListValues.getValue() != null)
		{
			if((int)this.tblCustomListValues.getValue() > 0)
			{
				//Validate that Custom List Value is not linked to another dependencies
				dbIdOfItemsToDelete.add((int)this.tblCustomListValues.getValue());
			}
			
			this.tblCustomListValues.removeItem((int)this.tblCustomListValues.getValue());
		}
	
	}
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
}
