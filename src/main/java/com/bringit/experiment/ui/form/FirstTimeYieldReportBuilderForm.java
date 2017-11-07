package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FirstTimeYieldInfoField;
import com.bringit.experiment.bll.FirstTimeYieldReport;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FirstTimeYieldInfoFieldDao;
import com.bringit.experiment.dao.FirstTimeYieldReportDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.ui.design.FirstTimeYieldReportBuilderDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class FirstTimeYieldReportBuilderForm extends FirstTimeYieldReportBuilderDesign{

	private Experiment experiment = new Experiment();
	private FirstTimeYieldReport ftyReport = new FirstTimeYieldReport();
	private List<Experiment> experiments = new ExperimentDao().getActiveExperiments();
	private List<ExperimentField> expFields;
	private int lastNewItemId = 0;		
	private List<Integer> dbIdOfFpyInfoFieldsToDelete = new ArrayList<Integer>();
	private SystemSettings systemSettings;
	
	public FirstTimeYieldReportBuilderForm(int ftyReportId)
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
		if(ftyReportId != -1)
		{
			this.ftyReport = new FirstTimeYieldReportDao().getFirstTimeYieldReportById(ftyReportId);
			this.chxActive.setValue(this.ftyReport.getFtyReportIsActive());
			this.txtFtyRptName.setValue(this.ftyReport.getFtyReportName());
			this.txtFtyRptCustomId.setValue(this.ftyReport.getFtyReportDbRptTableNameId().replace("fty#", ""));
			this.cbxExperiment.setValue(this.ftyReport.getExperiment().getExpId());
			
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
						
			this.txtFtyDescription.setValue(this.ftyReport.getFtyReportDescription());
			this.cbxDateTimeField.setValue(this.ftyReport.getDateTimeExpField().getExpFieldId());
			this.cbxSerialNumberField.setValue(this.ftyReport.getSerialNumberExpField().getExpFieldId());
			this.cbxResultField.setValue(this.ftyReport.getResultExpField().getExpFieldId());
			this.chxGroupByTimeRange.setValue(this.ftyReport.getFpyGroupByTimeRange());
			
			this.txtGroupTimeRange.setValue(this.ftyReport.getFtyTimeRangeMin().toString());
			//this.txtGroupTimeRange.setVisible(this.chxGroupByTimeRange.getValue());
			
			this.txtFtyRptPassValue.setValue(this.ftyReport.getFtyPassResultValue());
			this.txtFtyRptFailValue.setValue(this.ftyReport.getFtyFailResultValue());			
			
			List<FirstTimeYieldInfoField> ftyInfoFields =  new FirstTimeYieldInfoFieldDao().getFirstTimeYieldInfoFieldByReportById(ftyReportId);
			
			if(ftyInfoFields != null)
			{
				for(int i=0; i<ftyInfoFields.size(); i++)
					addColumnTblItem(this.tblInformationFields, ftyInfoFields.get(i));
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
					
					if(ftyReportId != -1)
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
	
	private void addColumnTblItem(Table ftyTblInformationFields, FirstTimeYieldInfoField ftyInformationField)
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
		
		if(ftyInformationField!=null)
		{
			itemId = ftyInformationField.getFtyInfoFieldId();
			cbxExperimentField.setValue(ftyInformationField.getExperimentField().getExpFieldId());
			txtInfoFieldLabel.setValue(ftyInformationField.getFtyInfoFieldLabel());
		}
		else
		{
			cbxExperimentField.focus();
			lastNewItemId = lastNewItemId - 1;
			itemId = lastNewItemId;
		}
		
		ftyTblInformationFields.addItem(itemValues, itemId);
		ftyTblInformationFields.select(itemId);
	}
	
	private void enableComponents(boolean enable)
	{
		this.btnSave.setEnabled(enable);
		this.colGroupLayout.setEnabled(enable);
		this.ftyFieldGroupsLayout.setEnabled(enable);
		this.gbxYieldFields.setEnabled(enable);
		this.btnDelete.setEnabled(enable);
		this.txtGroupTimeRange.setEnabled(enable);
	}
	
	private void onSave()
	{		
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateNotDuplicatedFpyInfoFieldsResult = validateNotDuplicatedFtyInfoFields();
		boolean validateTimeRangeResult = validateTimeRange(); 

		if(validateRequiredFieldsResult && validateNotDuplicatedFpyInfoFieldsResult && validateTimeRangeResult)
		{
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			this.ftyReport.setLastModifiedBy(sessionUser);
			this.ftyReport.setModifiedDate(new Date());				
			
			boolean isNewRecord = false;
			FirstTimeYieldInfoFieldDao ftyInfoFieldDao = new FirstTimeYieldInfoFieldDao();
		
			//Delete Records from DataBase
			for(int i=0; dbIdOfFpyInfoFieldsToDelete != null && i<dbIdOfFpyInfoFieldsToDelete.size(); i++)
			{
				if(dbIdOfFpyInfoFieldsToDelete.get(i) > 0)
					ftyInfoFieldDao.deleteFirstTimeYieldInfoField(dbIdOfFpyInfoFieldsToDelete.get(i));
			}
			
			this.ftyReport.setFtyReportIsActive(this.chxActive.getValue());
			this.ftyReport.setFtyReportName(this.txtFtyRptName.getValue());
			this.ftyReport.setFtyReportDbRptTableNameId("fpy#" + this.txtFtyRptCustomId.getValue());
			this.ftyReport.setExperiment(new ExperimentDao().getExperimentById(Integer.parseInt(this.cbxExperiment.getValue().toString())));
			this.ftyReport.setFtyReportDescription(this.txtFtyDescription.getValue());
			this.ftyReport.setDateTimeExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(this.cbxDateTimeField.getValue().toString())));
			this.ftyReport.setSerialNumberExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(this.cbxSerialNumberField.getValue().toString())));
			this.ftyReport.setResultExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(this.cbxResultField.getValue().toString())));
			this.ftyReport.setFtyGroupByTimeRange(this.chxGroupByTimeRange.getValue());
			this.ftyReport.setFtyTimeRangeMin(Integer.parseInt(this.txtGroupTimeRange.getValue().toString()));
			this.ftyReport.setFtyPassResultValue(this.txtFtyRptPassValue.getValue());
			this.ftyReport.setFtyFailResultValue(this.txtFtyRptFailValue.getValue());
			
			this.ftyReport.setFtyReportDbRptTableLastUpdate(new Date());
			
			if(this.ftyReport.getFtyReportId() != null)
				new FirstTimeYieldReportDao().updateFirstTimeYieldReport(this.ftyReport);
			else
			{
				isNewRecord = true;
				this.ftyReport.setCreatedBy(sessionUser);
				this.ftyReport.setCreatedDate(new Date());		
				new FirstTimeYieldReportDao().addFirstTimeYieldReport(this.ftyReport);
			}
			
			Collection itemIds = this.tblInformationFields.getContainerDataSource().getItemIds();
			List<FirstTimeYieldInfoField> ftyInfoFields = new ArrayList<FirstTimeYieldInfoField>(); 
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblInformationFields.getContainerDataSource().getItem(itemId);
				
				FirstTimeYieldInfoField ftyInfoField = new FirstTimeYieldInfoField();
				ftyInfoField.setFtyReport(this.ftyReport);
				ftyInfoField.setExperimentField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue().toString())));
				ftyInfoField.setFtyInfoFieldLabel(((TextField)(tblRowItem.getItemProperty("Label").getValue())).getValue());
								
				if(itemId > 0)
				{
					ftyInfoField.setFtyInfoFieldId(itemId);
					ftyInfoFieldDao.updateFirstTimeYieldInfoField(ftyInfoField);
				}
				else
					ftyInfoFieldDao.addFirstTimeYieldInfoField(ftyInfoField);
				
				ftyInfoFields.add(ftyInfoField);
			}
			
			new FirstTimeYieldReportDao().saveDBFtyRptTable(this.ftyReport, ftyInfoFields);
			//Execute Store Procedure to refresh data
			
			
			if(isNewRecord)
			{
				WebApplication webApp = (WebApplication)this.getParent().getParent();
				webApp.reloadMainForm("first time yield report");
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
		if(!this.txtFtyRptName.isValid() || !this.txtFtyRptCustomId.isValid() || !this.cbxExperiment.isValid() 
				|| !this.cbxDateTimeField.isValid() || !this.cbxSerialNumberField.isValid() || !this.cbxResultField.isValid()
				|| !this.txtFtyRptPassValue.isValid() || !this.txtFtyRptFailValue.isValid())
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
	
	private boolean validateNotDuplicatedFtyInfoFields()
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
		this.ftyReport.setFtyReportIsActive(false);
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		this.ftyReport.setLastModifiedBy(sessionUser);
		this.ftyReport.setModifiedDate(new Date());		
		new FirstTimeYieldReportDao().deleteDBFtyRptTable(this.ftyReport);
		new FirstTimeYieldReportDao().updateFirstTimeYieldReport(this.ftyReport);		
		
		WebApplication webApp = (WebApplication)this.getParent().getParent();
		webApp.reloadMainForm("first time yield report");
		closeModalWindow();
    }
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}	
}
