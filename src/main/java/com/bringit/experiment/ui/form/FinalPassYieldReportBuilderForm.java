package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FinalPassYieldInfoField;
import com.bringit.experiment.bll.FinalPassYieldReport;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FinalPassYieldInfoFieldDao;
import com.bringit.experiment.dao.FinalPassYieldReportDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.ui.design.FinalPassYieldReportBuilderDesign;
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

public class FinalPassYieldReportBuilderForm extends FinalPassYieldReportBuilderDesign {

	private Experiment experiment = new Experiment();
	private FinalPassYieldReport fnyReport = new FinalPassYieldReport();
	private List<Experiment> experiments = new ExperimentDao().getActiveExperiments();
	private List<ExperimentField> expFields;
	private int lastNewItemId = 0;		
	private List<Integer> dbIdOfFnyInfoFieldsToDelete = new ArrayList<Integer>();
	private SystemSettings systemSettings;
	
	public FinalPassYieldReportBuilderForm(int fnyReportId)
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
		if(fnyReportId != -1)
		{
			this.fnyReport = new FinalPassYieldReportDao().getFinalPassYieldReportById(fnyReportId);
			this.chxActive.setValue(this.fnyReport.getFnyReportIsActive());
			this.txtFnyRptName.setValue(this.fnyReport.getFnyReportName());
			this.txtFnyRptCustomId.setValue(this.fnyReport.getFnyReportDbRptTableNameId().replace("fny#", ""));
			this.cbxExperiment.setValue(this.fnyReport.getExperiment().getExpId());
			
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
						
			this.txtFnyDescription.setValue(this.fnyReport.getFnyReportDescription());
			this.cbxDateTimeField.setValue(this.fnyReport.getDateTimeExpField().getExpFieldId());
			this.cbxSerialNumberField.setValue(this.fnyReport.getSerialNumberExpField().getExpFieldId());
			this.cbxResultField.setValue(this.fnyReport.getResultExpField().getExpFieldId());
			this.chxGroupByTimeRange.setValue(this.fnyReport.getFnyGroupByTimeRange());
			
			this.txtGroupTimeRange.setValue(this.fnyReport.getFnyTimeRangeMin().toString());
			
			this.txtFnyRptPassValue.setValue(this.fnyReport.getFnyPassResultValue());
			this.txtFnyRptFailValue.setValue(this.fnyReport.getFnyFailResultValue());			
			
			List<FinalPassYieldInfoField> fnyInfoFields =  new FinalPassYieldInfoFieldDao().getFinalPassYieldInfoFieldByReportById(fnyReportId);
			
			if(fnyInfoFields != null)
			{
				for(int i=0; i<fnyInfoFields.size(); i++)
					addColumnTblItem(this.tblInformationFields, fnyInfoFields.get(i));
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
					dbIdOfFnyInfoFieldsToDelete.add(Integer.parseInt(tblInformationFields.getValue().toString()));
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
					
					if(fnyReportId != -1)
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
	
	private void addColumnTblItem(Table fnyTblInformationFields, FinalPassYieldInfoField fnyInformationField)
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
		
		if(fnyInformationField!=null)
		{
			itemId = fnyInformationField.getFnyInfoFieldId();
			cbxExperimentField.setValue(fnyInformationField.getExperimentField().getExpFieldId());
			txtInfoFieldLabel.setValue(fnyInformationField.getFnyInfoFieldLabel());
		}
		else
		{
			cbxExperimentField.focus();
			lastNewItemId = lastNewItemId - 1;
			itemId = lastNewItemId;
		}
		
		fnyTblInformationFields.addItem(itemValues, itemId);
		fnyTblInformationFields.select(itemId);
	}
	
	private void enableComponents(boolean enable)
	{
		this.btnSave.setEnabled(enable);
		this.colGroupLayout.setEnabled(enable);
		this.fnyFieldGroupsLayout.setEnabled(enable);
		this.gbxYieldFields.setEnabled(enable);
		this.btnDelete.setEnabled(enable);
		this.txtGroupTimeRange.setEnabled(enable);
	}
	
	private void onSave()
	{		
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateNotDuplicatedFnyInfoFieldsResult = validateNotDuplicatedFpyInfoFields();
		boolean validateTimeRangeResult = validateTimeRange(); 

		if(validateRequiredFieldsResult && validateNotDuplicatedFnyInfoFieldsResult && validateTimeRangeResult)
		{
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			this.fnyReport.setLastModifiedBy(sessionUser);
			this.fnyReport.setModifiedDate(new Date());				
			
			boolean isNewRecord = false;
			FinalPassYieldInfoFieldDao fnyInfoFieldDao = new FinalPassYieldInfoFieldDao();
		
			//Delete Records from DataBase
			for(int i=0; dbIdOfFnyInfoFieldsToDelete != null && i<dbIdOfFnyInfoFieldsToDelete.size(); i++)
			{
				if(dbIdOfFnyInfoFieldsToDelete.get(i) > 0)
					fnyInfoFieldDao.deleteFinalPassYieldInfoField(dbIdOfFnyInfoFieldsToDelete.get(i));
			}
			
			this.fnyReport.setFnyReportIsActive(this.chxActive.getValue());
			this.fnyReport.setFnyReportName(this.txtFnyRptName.getValue());
			this.fnyReport.setFnyReportDbRptTableNameId("fny#" + this.txtFnyRptCustomId.getValue());
			this.fnyReport.setExperiment(new ExperimentDao().getExperimentById(Integer.parseInt(this.cbxExperiment.getValue().toString())));
			this.fnyReport.setFnyReportDescription(this.txtFnyDescription.getValue());
			this.fnyReport.setDateTimeExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(this.cbxDateTimeField.getValue().toString())));
			this.fnyReport.setSerialNumberExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(this.cbxSerialNumberField.getValue().toString())));
			this.fnyReport.setResultExpField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(this.cbxResultField.getValue().toString())));
			this.fnyReport.setFnyGroupByTimeRange(this.chxGroupByTimeRange.getValue());
			this.fnyReport.setFnyTimeRangeMin(Integer.parseInt(this.txtGroupTimeRange.getValue().toString()));
			this.fnyReport.setFnyPassResultValue(this.txtFnyRptPassValue.getValue());
			this.fnyReport.setFnyFailResultValue(this.txtFnyRptFailValue.getValue());
			
			this.fnyReport.setFnyReportDbRptTableLastUpdate(new Date());
			
			if(this.fnyReport.getFnyReportId() != null)
				new FinalPassYieldReportDao().updateFinalPassYieldReport(this.fnyReport);
			else
			{
				isNewRecord = true;
				this.fnyReport.setCreatedBy(sessionUser);
				this.fnyReport.setCreatedDate(new Date());	
				new FinalPassYieldReportDao().addFinalPassYieldReport(this.fnyReport);
			}
			
			Collection itemIds = this.tblInformationFields.getContainerDataSource().getItemIds();
			List<FinalPassYieldInfoField> fnyInfoFields = new ArrayList<FinalPassYieldInfoField>(); 
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblInformationFields.getContainerDataSource().getItem(itemId);
				
				FinalPassYieldInfoField fnyInfoField = new FinalPassYieldInfoField();
				fnyInfoField.setFnyReport(this.fnyReport);
				fnyInfoField.setExperimentField(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue().toString())));
				fnyInfoField.setFnyInfoFieldLabel(((TextField)(tblRowItem.getItemProperty("Label").getValue())).getValue());
								
				if(itemId > 0)
				{
					fnyInfoField.setFnyInfoFieldId(itemId);
					fnyInfoFieldDao.updateFinalPassYieldInfoField(fnyInfoField);
				}
				else
					fnyInfoFieldDao.addFinalPassYieldInfoField(fnyInfoField);
				
				fnyInfoFields.add(fnyInfoField);
			}
			
			new FinalPassYieldReportDao().saveDBFnyRptTable(this.fnyReport, fnyInfoFields);
			//Execute Store Procedure to refresh data
			
			
			if(isNewRecord)
			{
				WebApplication webApp = (WebApplication)this.getParent().getParent();
				webApp.reloadMainForm("final pass yield report");
			}
			
			closeModalWindow();		
		}
		else
		{
			if(!validateRequiredFieldsResult)
				this.getUI().showNotification("Please fill in all required fields.", Type.WARNING_MESSAGE);
			else if(!validateNotDuplicatedFnyInfoFieldsResult)
				this.getUI().showNotification("Same " + this.systemSettings.getExperimentLabel() +" field can not be added two or more times.", Type.WARNING_MESSAGE);
			else if(!validateTimeRangeResult)
				this.getUI().showNotification("Time range must be a valid number.", Type.WARNING_MESSAGE);				
		}	
	}
	
	private boolean validateRequiredFields()
	{
		if(!this.txtFnyRptName.isValid() || !this.txtFnyRptCustomId.isValid() || !this.cbxExperiment.isValid() 
				|| !this.cbxDateTimeField.isValid() || !this.cbxSerialNumberField.isValid() || !this.cbxResultField.isValid()
				|| !this.txtFnyRptPassValue.isValid() || !this.txtFnyRptFailValue.isValid())
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
		this.fnyReport.setFnyReportIsActive(false);
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		this.fnyReport.setLastModifiedBy(sessionUser);
		this.fnyReport.setModifiedDate(new Date());		
		new FinalPassYieldReportDao().deleteDBFnyRptTable(this.fnyReport);
		new FinalPassYieldReportDao().updateFinalPassYieldReport(this.fnyReport);		
		
		WebApplication webApp = (WebApplication)this.getParent().getParent();
		webApp.reloadMainForm("final pass yield report");
		closeModalWindow();
    }
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
	
}
