package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FirstPassYieldInfoField;
import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FirstPassYieldInfoFieldDao;
import com.bringit.experiment.dao.FirstPassYieldReportDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.ui.design.FirstPassYieldReportBuilderDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class FirstPassYieldReportBuilderForm extends FirstPassYieldReportBuilderDesign{

	private Experiment experiment = new Experiment();
	private FirstPassYieldReport fpyReport = new FirstPassYieldReport();
	private List<Experiment> experiments = new ExperimentDao().getActiveExperiments();
	private List<ExperimentField> expFields;
	private int lastNewItemId = 0;		
	private List<Integer> dbIdOfFpyInfoFieldsToDelete = new ArrayList<Integer>();
	private SystemSettings systemSettings;
	
	public FirstPassYieldReportBuilderForm(int fpyReportId)
	{
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		this.cbxExperiment.setCaption(this.systemSettings.getExperimentLabel());
		
		enableComponents(false);
		cbxExperiment.setNullSelectionAllowed(false);
		
		tblInformationFields.setContainerDataSource(null);
		tblInformationFields.addContainerProperty("*", CheckBox.class, null);
		tblInformationFields.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field", ComboBox.class, null);
		tblInformationFields.addContainerProperty("Label", TextField.class, null);
		tblInformationFields.setPageLength(100);
		tblInformationFields.setStyleName("tiny");
		tblInformationFields.setSelectable(true);
		tblInformationFields.setMultiSelect(false);
		
		for(int j=0; j<experiments.size(); j++)
		{
			cbxExperiment.addItem(experiments.get(j).getExpId());
			cbxExperiment.setItemCaption(experiments.get(j).getExpId(), experiments.get(j).getExpName());
		}
		
		//Load Report 
		if(fpyReportId != -1)
		{
			this.fpyReport = new FirstPassYieldReportDao().getFirstPassYieldReportById(fpyReportId);
			this.chxActive.setValue(this.fpyReport.getFpyReportIsActive());
			this.txtFpyRptName.setValue(this.fpyReport.getFpyReportName());
			this.txtFpyRptCustomId.setValue(this.fpyReport.getFpyReportDbRptTableNameId().replace("fpy#", ""));
			this.cbxExperiment.setValue(this.fpyReport.getExperiment().getExpId());
			
			experiment = new ExperimentDao().getExperimentById(Integer.parseInt(cbxExperiment.getValue().toString()));
			expFields = new ExperimentFieldDao().getActiveExperimentFields(experiment);
			cbxDateTimeField.setContainerDataSource(null);
			cbxSerialNumberField.setContainerDataSource(null);
			cbxResultField.setContainerDataSource(null);
			
			for(int i=0; i<expFields.size(); i++)
			{
				if(expFields.get(i).getExpFieldType().contains("date"))
				{
					cbxDateTimeField.addItem(expFields.get(i).getExpFieldId());
					cbxDateTimeField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
				}
				
				cbxSerialNumberField.addItem(expFields.get(i).getExpFieldId());
				cbxSerialNumberField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
					
				cbxResultField.addItem(expFields.get(i).getExpFieldId());
				cbxResultField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());						
			}			
						
			this.txtFpyDescription.setValue(this.fpyReport.getFpyReportDescription());
			this.cbxDateTimeField.setValue(this.fpyReport.getDateTimeExpField().getExpFieldId());
			this.cbxSerialNumberField.setValue(this.fpyReport.getSerialNumberExpField().getExpFieldId());
			this.cbxResultField.setValue(this.fpyReport.getResultExpField().getExpFieldId());
			this.chxGroupByTimeRange.setValue(this.fpyReport.getFpyGroupByTimeRange());
			
			this.txtGroupTimeRange.setValue(this.fpyReport.getFpyTimeRangeMin().toString());
			this.txtGroupTimeRange.setVisible(this.chxGroupByTimeRange.getValue());
			
			this.txtFpyRptPassValue.setValue(this.fpyReport.getFpyPassResultValue());
			this.txtFpyRptFailValue.setValue(this.fpyReport.getFpyFailResultValue());			
			
			List<FirstPassYieldInfoField> fpyInfoFields =  new FirstPassYieldInfoFieldDao().getFirstPassYieldInfoFieldByReportById(fpyReportId);
			
			if(fpyInfoFields != null)
			{
				for(int i=0; i<fpyInfoFields.size(); i++)
					addColumnTblItem(this.tblInformationFields, fpyInfoFields.get(i));
			}
			
			enableComponents(true);
		}
		
		//Elements events
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
		
		btnAddField.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addColumnTblItem(tblInformationFields, null);
			}

		});
		
		
		btnRemoveField.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(tblInformationFields.getValue() != null)
				{
					dbIdOfFpyInfoFieldsToDelete.add(Integer.parseInt(tblInformationFields.getValue().toString()));
					tblInformationFields.removeItem((int)tblInformationFields.getValue());
				}	
			}

		});
		
		chxGroupByTimeRange.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {				
				txtGroupTimeRange.setEnabled(chxGroupByTimeRange.getValue());				
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
					chxActive.setValue(true);	
					txtGroupTimeRange.setEnabled(chxGroupByTimeRange.getValue());
					
					if(fpyReportId != -1)
						btnDeleteLayout.setEnabled(true);	
					else
						btnDeleteLayout.setEnabled(false);					
					
					cbxDateTimeField.setContainerDataSource(null);
					cbxSerialNumberField.setContainerDataSource(null);
					cbxResultField.setContainerDataSource(null);
					
					for(int i=0; i<expFields.size(); i++)
					{
						if(expFields.get(i).getExpFieldType().contains("date"))
						{
							cbxDateTimeField.addItem(expFields.get(i).getExpFieldId());
							cbxDateTimeField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
						}
						
						cbxSerialNumberField.addItem(expFields.get(i).getExpFieldId());
						cbxSerialNumberField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
							
						cbxResultField.addItem(expFields.get(i).getExpFieldId());
						cbxResultField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());						
					}					
				}
			}
		});
		
		btnDelete.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onDelete();				
			}

		});
		
		Validator intValidator = new Validator() {

            public void validate(Object value) throws InvalidValueException {
               try
               {
            	   if(!((String) value).matches("^-?\\d+$"))  
            	      throw new InvalidValueException("Invalid Number");
               }
               catch(Exception e)
               {
            	   throw new InvalidValueException("Invalid Number");
               }
            }
        };
        
        this.txtGroupTimeRange.addValidator(intValidator);
	}
	
	private void addColumnTblItem(Table fpyTblInformationFields, FirstPassYieldInfoField fpyInformationField)
	{
		Object[] itemValues = new Object[3];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		chxSelect.setStyleName("tiny");				
		itemValues[0] = chxSelect;
	
		ComboBox cbxExperimentField = new ComboBox("");
		cbxExperimentField.setWidth(97, Unit.PERCENTAGE);
		cbxExperimentField.setStyleName("tiny");
		cbxExperimentField.setNullSelectionAllowed(false);
		cbxExperimentField.setRequired(true);
		cbxExperimentField.setRequiredError("Please select one " + this.systemSettings.getExperimentLabel() + " field.");
		for(int i=0; i<expFields.size(); i++)
		{
			cbxExperimentField.addItem(expFields.get(i).getExpFieldId());
			cbxExperimentField.setItemCaption(expFields.get(i).getExpFieldId(), expFields.get(i).getExpFieldName());
		}
		
		itemValues[1] = cbxExperimentField;
		
		TextField txtInfoFieldLabel = new TextField();
		txtInfoFieldLabel.setRequired(true);
		txtInfoFieldLabel.setStyleName("tiny");
		txtInfoFieldLabel.setWidth(97, Unit.PERCENTAGE);
		
		itemValues[2] = txtInfoFieldLabel;


		cbxExperimentField.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				txtInfoFieldLabel.setValue(cbxExperimentField.getValue()!=null ? cbxExperimentField.getItemCaption(cbxExperimentField.getValue()) : "");				
			}
		});
		
		
		int itemId = -1;
		
		if(fpyInformationField!=null)
		{
			itemId = fpyInformationField.getFpyInfoFieldId();
			cbxExperimentField.setValue(fpyInformationField.getExperimentField().getExpFieldId());
			txtInfoFieldLabel.setValue(fpyInformationField.getFpyInfoFieldLabel());
		}
		else
		{
			cbxExperimentField.focus();
			lastNewItemId = lastNewItemId - 1;
			itemId = lastNewItemId;
		}
		
		fpyTblInformationFields.addItem(itemValues, itemId);
		fpyTblInformationFields.select(itemId);
	}
	
	private void enableComponents(boolean enable)
	{
		this.btnSave.setEnabled(enable);
		this.colGroupLayout.setEnabled(enable);
		this.fpyFieldGroupsLayout.setEnabled(enable);
		this.gbxYieldFields.setEnabled(enable);
		this.btnDelete.setEnabled(enable);
		this.txtGroupTimeRange.setEnabled(enable);
	}
	
	private void onSave()
	{		
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateNotDuplicatedFpyInfoFieldsResult = validateNotDuplicatedFpyInfoFields();
		boolean validateTimeRangeResult = validateTimeRange(); 

		if(validateRequiredFieldsResult && validateNotDuplicatedFpyInfoFieldsResult && validateTimeRangeResult)
		{
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			this.fpyReport.setLastModifiedBy(sessionUser);
			this.fpyReport.setModifiedDate(new Date());				
			
			boolean isNewRecord = false;
			FirstPassYieldInfoFieldDao fpyInfoFieldDao = new FirstPassYieldInfoFieldDao();
		
			//Delete Records from DataBase
			for(int i=0; dbIdOfFpyInfoFieldsToDelete != null && i<dbIdOfFpyInfoFieldsToDelete.size(); i++)
			{
				if(dbIdOfFpyInfoFieldsToDelete.get(i) > 0)
					fpyInfoFieldDao.deleteFirstPassYieldInfoField(dbIdOfFpyInfoFieldsToDelete.get(i));
			}
			
			this.fpyReport.setFpyReportIsActive(this.chxActive.getValue());
			this.fpyReport.setFpyReportName(this.txtFpyRptName.getValue());
			this.fpyReport.setFpyReportDbRptTableNameId("fpy#" + this.txtFpyRptCustomId.getValue());
			this.fpyReport.setExperiment(new ExperimentDao().getExperimentById(Integer.parseInt(this.cbxExperiment.getValue().toString())));
			this.fpyReport.setFpyReportDescription(this.txtFpyDescription.getValue());
			this.fpyReport.setDateTimeExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(this.cbxDateTimeField.getValue().toString())));
			this.fpyReport.setSerialNumberExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(this.cbxSerialNumberField.getValue().toString())));
			this.fpyReport.setResultExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(this.cbxResultField.getValue().toString())));
			this.fpyReport.setFpyGroupByTimeRange(this.chxGroupByTimeRange.getValue());
			this.fpyReport.setFpyTimeRangeMin(Integer.parseInt(this.txtGroupTimeRange.getValue().toString()));
			this.fpyReport.setFpyPassResultValue(this.txtFpyRptPassValue.getValue());
			this.fpyReport.setFpyFailResultValue(this.txtFpyRptFailValue.getValue());
			
			this.fpyReport.setFpyReportDbRptTableLastUpdate(new Date());
			
			if(this.fpyReport.getFpyReportId() != null)
				new FirstPassYieldReportDao().updateFirstPassYieldReport(this.fpyReport);
			else
			{
				isNewRecord = true;
				this.fpyReport.setCreatedBy(sessionUser);
				this.fpyReport.setCreatedDate(new Date());	
				new FirstPassYieldReportDao().addFirstPassYieldReport(this.fpyReport);
			}
			
			Collection itemIds = this.tblInformationFields.getContainerDataSource().getItemIds();
			List<FirstPassYieldInfoField> fpyInfoFields = new ArrayList<FirstPassYieldInfoField>(); 
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblInformationFields.getContainerDataSource().getItem(itemId);
				
				FirstPassYieldInfoField fpyInfoField = new FirstPassYieldInfoField();
				fpyInfoField.setFpyReport(this.fpyReport);
				fpyInfoField.setExperimentField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue().toString())));
				fpyInfoField.setFpyInfoFieldLabel(((TextField)(tblRowItem.getItemProperty("Label").getValue())).getValue());
								
				if(itemId > 0)
				{
					fpyInfoField.setFpyInfoFieldId(itemId);
					fpyInfoFieldDao.updateFirstPassYieldInfoField(fpyInfoField);
				}
				else
					fpyInfoFieldDao.addFirstPassYieldInfoField(fpyInfoField);
				
				fpyInfoFields.add(fpyInfoField);
			}
			
			new FirstPassYieldReportDao().saveDBFpyRptTable(this.fpyReport, fpyInfoFields);
			//Execute Store Procedure to refresh data
			
			
			if(isNewRecord)
			{
				WebApplication webApp = (WebApplication)this.getParent().getParent();
				webApp.reloadMainForm("first pass yield report");
			}
			
			closeModalWindow();		
		}
		else
		{
			if(!validateRequiredFieldsResult)
				this.getUI().showNotification("Please fill in all required fields.", Type.WARNING_MESSAGE);
			else if(!validateNotDuplicatedFpyInfoFieldsResult)
				this.getUI().showNotification("Same " + this.systemSettings.getExperimentLabel() +" field can not be added two or more times.", Type.WARNING_MESSAGE);
			else if(!validateTimeRangeResult)
				this.getUI().showNotification("Time range must be a valid number.", Type.WARNING_MESSAGE);				
		}	
	}
	
	private boolean validateRequiredFields()
	{
		if(!this.txtFpyRptName.isValid() || !this.txtFpyRptCustomId.isValid() || !this.cbxExperiment.isValid() 
				|| !this.cbxDateTimeField.isValid() || !this.cbxSerialNumberField.isValid() || !this.cbxResultField.isValid()
				|| !this.txtFpyRptPassValue.isValid() || !this.txtFpyRptFailValue.isValid())
			return false;		
		
		Collection itemIds = this.tblInformationFields.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblInformationFields.getContainerDataSource().getItem(itemId);
			
			if(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue() == null)
				return false;
		}
		
		return true;
	}
	
	private boolean validateNotDuplicatedFpyInfoFields()
	{
		Collection itemIds = this.tblInformationFields.getContainerDataSource().getItemIds();
		List<Integer> addedExperimentFieldIds = new ArrayList<Integer>(); 
		
		for (Object itemIdObj : itemIds) 
		{
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblInformationFields.getContainerDataSource().getItem(itemId);
			
			if((((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue()) != null &&
					addedExperimentFieldIds.indexOf(Integer.parseInt(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue().toString())) == -1)
				addedExperimentFieldIds.add(Integer.parseInt(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue().toString()));
			else 
				return false;
		}
		
		return true;
	}
	
	private boolean validateTimeRange()
	{
 	   return ((String) this.txtGroupTimeRange.getValue()).matches("^-?\\d+$");  
	}
	
	private void onDelete()
	{
		this.fpyReport.setFpyReportIsActive(false);
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		this.fpyReport.setLastModifiedBy(sessionUser);
		this.fpyReport.setModifiedDate(new Date());		
		new FirstPassYieldReportDao().deleteDBFpyRptTable(this.fpyReport);
		new FirstPassYieldReportDao().updateFirstPassYieldReport(this.fpyReport);		
		
		WebApplication webApp = (WebApplication)this.getParent().getParent();
		webApp.reloadMainForm("first pass yield report");
		closeModalWindow();
    }
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
}
