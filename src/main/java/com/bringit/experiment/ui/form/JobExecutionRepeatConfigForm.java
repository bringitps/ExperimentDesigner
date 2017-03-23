package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.ui.design.JobExecutionRepeatConfigDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

public class JobExecutionRepeatConfigForm extends JobExecutionRepeatConfigDesign{
	
	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();
	
	public JobExecutionRepeatConfigForm()
	{
		loadTblData();
		
		this.btnAddJobExecRepeat.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addJobExecRepeatRow();
			}

		});
	
		this.btnDeleteJobExecRepeat.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deleteJobExecRepeatRow();
			}

		});
		
		this.btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				saveJobExecRepeatRows();
			}

		});

		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				Filterable filter= (Filterable) (tblJobExecutionRepeat.getContainerDataSource());
                filter.removeAllContainerFilters();

                String filterString = txtSearch.getValue();
                Like filterLike = new Like(cbxJobExecRepeatFilters.getValue().toString(), "%" + filterString + "%");
                filterLike.setCaseSensitive(false);
                
                if (filterString.length() > 0 && !cbxJobExecRepeatFilters.getValue().toString().isEmpty()) {
                    filter.addContainerFilter(filterLike);
                }
			}
			});
	}
	
	
	private void loadTblData()
	{
		tblJobExecutionRepeat.setContainerDataSource(null);
		tblJobExecutionRepeat.addContainerProperty("*", CheckBox.class, null);
		tblJobExecutionRepeat.addContainerProperty("Name", TextField.class, null);
		tblJobExecutionRepeat.addContainerProperty("Milliseconds", TextField.class, null);
		tblJobExecutionRepeat.setEditable(true);
		tblJobExecutionRepeat.setPageLength(0);
		tblJobExecutionRepeat.setColumnWidth("*", 15);
		
		cbxJobExecRepeatFilters.addItem("Name");
		cbxJobExecRepeatFilters.addItem("Milliseconds");
		cbxJobExecRepeatFilters.select("Name");
		
		Object[] itemValues = new Object[3];

		List<JobExecutionRepeat> jobExecutionRepeats = new JobExecutionRepeatDao().getAllJobExecutionRepeats();
		
		for(int i=0; jobExecutionRepeats != null && i<jobExecutionRepeats.size(); i++)
		{
			CheckBox chxSelect = new CheckBox();
			chxSelect.setVisible(false);
			chxSelect.setWidth(10, Unit.PIXELS);
			itemValues[0] = chxSelect;
			
			TextField txtJobExecRepeatName = new TextField();
			txtJobExecRepeatName.setStyleName("tiny");
			txtJobExecRepeatName.setValue(jobExecutionRepeats.get(i).getJobExecRepeatName());			
			txtJobExecRepeatName.setWidth(95, Unit.PERCENTAGE);
			itemValues[1] = txtJobExecRepeatName;
			
			TextField txtMilliSeconds = new TextField();
			txtMilliSeconds.setStyleName("tiny");
			txtMilliSeconds.addValidator(new Validator() {

	            public void validate(Object value) throws InvalidValueException {
	                if(!StringUtils.isNumeric((String) value))
	                    throw new InvalidValueException("Only Digits are allowed for Milliseconds");
	            }
	            
	        });
			txtMilliSeconds.setValue(((Integer)jobExecutionRepeats.get(i).getJobExecRepeatMilliseconds()).toString());
			txtMilliSeconds.setWidth(95, Unit.PERCENTAGE);
			itemValues[2] = txtMilliSeconds;

			this.tblJobExecutionRepeat.addItem(itemValues, jobExecutionRepeats.get(i).getJobExecRepeatId());
		}
	}
	

	private void addJobExecRepeatRow()
	{
		Object[] itemValues = new Object[3];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		itemValues[0] = chxSelect;

		TextField txtJobExecRepeatName = new TextField();
		txtJobExecRepeatName.setStyleName("tiny");
		txtJobExecRepeatName.setValue("");
		txtJobExecRepeatName.setWidth(95, Unit.PERCENTAGE);
		itemValues[1] = txtJobExecRepeatName;
		
		TextField txtMilliSeconds = new TextField();
		txtMilliSeconds.setWidth(95, Unit.PERCENTAGE);
		txtMilliSeconds.setStyleName("tiny");
		txtMilliSeconds.addValidator(new Validator() {

            public void validate(Object value) throws InvalidValueException {
                if(!StringUtils.isNumeric((String) value))
                    throw new InvalidValueException("Only Digits are allowed for Milliseconds");
            }
            
        });
		
		itemValues[2] = txtMilliSeconds;
		
		this.lastNewItemId = this.lastNewItemId - 1;
		this.tblJobExecutionRepeat.addItem(itemValues, this.lastNewItemId);
		this.tblJobExecutionRepeat.select(this.lastNewItemId);
	}
	

	private void deleteJobExecRepeatRow()
	{
		boolean hasXmlTemplatesLinked = false;
		if(this.tblJobExecutionRepeat.getValue() != null && (int)this.tblJobExecutionRepeat.getValue() > 0 )
		{
			if(new XmlTemplateDao().getXmlTemplatesByJobExecRepeatId((int)this.tblJobExecutionRepeat.getValue()).size() <= 0)
				dbIdOfItemsToDelete.add((int)this.tblJobExecutionRepeat.getValue());
			else
				hasXmlTemplatesLinked = true;
		}
		
		if(!hasXmlTemplatesLinked && this.tblJobExecutionRepeat.getValue() != null)
			this.tblJobExecutionRepeat.removeItem((int)this.tblJobExecutionRepeat.getValue());
		else if(hasXmlTemplatesLinked)
			this.getUI().showNotification("Job Execution Repeat record can not be deleted. \nThere are XML Templates linked.", Type.WARNING_MESSAGE);
			
	}
	
	
	private void saveJobExecRepeatRows()
	{
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateTextFieldNumberResult = validateTextFieldNumber();
		
		if(validateRequiredFieldsResult && validateTextFieldNumberResult)
		{
			JobExecutionRepeatDao jobExecRepeatDao = new JobExecutionRepeatDao();
		
			//Delete Items in DB
			for(int i = 0; i<dbIdOfItemsToDelete.size(); i++)
				jobExecRepeatDao.deleteJobExecutionRepeat((int)dbIdOfItemsToDelete.get(i));
		
			Collection itemIds = this.tblJobExecutionRepeat.getContainerDataSource().getItemIds();
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblJobExecutionRepeat.getContainerDataSource().getItem(itemId);
				
				JobExecutionRepeat jobExecRepeat = new JobExecutionRepeat();
				jobExecRepeat.setJobExecRepeatName(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
				jobExecRepeat.setJobExecRepeatMilliseconds(Integer.parseInt(((TextField)(tblRowItem.getItemProperty("Milliseconds").getValue())).getValue()));
							
				if(itemId > 0)
				{
					jobExecRepeat.setJobExecRepeatId(itemId);
					jobExecRepeatDao.updateJobExecutionRepeat(jobExecRepeat);
				}
				else
					jobExecRepeatDao.addJobExecutionRepeat(jobExecRepeat);
			}

			this.getUI().showNotification("Data Saved.", Type.HUMANIZED_MESSAGE);
			loadTblData();
		}
		else if(!validateRequiredFieldsResult)
			this.getUI().showNotification("Name and Milliseconds must be set for New Job Execution Repeat Records", Type.WARNING_MESSAGE);
		else if(!validateTextFieldNumberResult)
			this.getUI().showNotification("Invalid value for Milliseconds", Type.WARNING_MESSAGE);
		
	}
	
	
	private boolean validateRequiredFields()
	{	
		Collection itemIds = this.tblJobExecutionRepeat.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblJobExecutionRepeat.getContainerDataSource().getItem(itemId);
			
			if(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue().isEmpty())
			{
				tblJobExecutionRepeat.select(itemId);
				return false;
			}
			if(((TextField)(tblRowItem.getItemProperty("Milliseconds").getValue())).getValue().isEmpty())
			{
				tblJobExecutionRepeat.select(itemId);
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateTextFieldNumber()
	{
		Collection itemIds = this.tblJobExecutionRepeat.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblJobExecutionRepeat.getContainerDataSource().getItem(itemId);
			
			if(!((TextField)(tblRowItem.getItemProperty("Milliseconds").getValue())).isValid())
			{
				tblJobExecutionRepeat.select(itemId);
				return false;
			}
		}
		
		return true;
	}
	
}
