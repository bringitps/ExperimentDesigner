package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FirstPassYieldInfoField;
import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.TargetColumn;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FirstPassYieldInfoFieldDao;
import com.bringit.experiment.dao.FirstPassYieldReportDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.TargetColumnDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.ui.design.FirstPassYieldReportBuilderDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

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
		
		
		cbxExperiment.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbxExperiment.getValue()!=null)
				{
					experiment = new ExperimentDao().getExperimentById(Integer.parseInt(cbxExperiment.getValue().toString()));
					expFields = new ExperimentFieldDao().getActiveExperimentFields(experiment);
					enableComponents(true);
					chxActive.setValue(true);	
					
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
	}
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
	private void onSave()
	{
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
		
		if(this.fpyReport.getFpyReportId() != null)
			new FirstPassYieldReportDao().updateFirstPassYieldReport(this.fpyReport);
		else
		{
			isNewRecord = true;
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
		
		if(isNewRecord)
		{
			WebApplication webApp = (WebApplication)this.getParent().getParent();
			webApp.reloadMainForm("first pass yield report");
		}
		
		
		
	}
	
	
}
