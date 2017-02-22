package com.bringit.experiment.ui.form;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.ui.design.ExperimentDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;

public class ExperimentForm extends ExperimentDesign {

	private Experiment experiment;
	private List<ExperimentField> experimentFields;
	List <UnitOfMeasure> unitOfMeasures= new UnitOfMeasureDao().getAllUnitOfMeasures();
	private int lastNewItemId = 0;
	
	public ExperimentForm(int experimentId)
	{	
		if(experimentId == -1) //New Experiment
		{
			this.btnDelete.setVisible(false);
			this.experiment = new Experiment();
		}
		else
		{
			//Loading Header Info
			this.experiment = new ExperimentDao().getExperimentById(experimentId);
			this.txtExpName.setValue(this.experiment.getExpName());
			this.txtExpDbTableNameId.setValue(this.experiment.getExpDbTableNameId());
			this.chxActive.setValue(this.experiment.isExpIsActive());
			this.txtExpInstructions.setValue(this.experiment.getExpInstructions());
			this.txtExpComments.setValue(this.experiment.getExpComments());
			
			this.experimentFields = new ExperimentFieldDao().getAllExperimentFieldsByExperiment(this.experiment);
			 
			this.tblExperimentFields.setContainerDataSource(null);
			this.tblExperimentFields.addContainerProperty("*", CheckBox.class, null);
			this.tblExperimentFields.addContainerProperty("Name", TextField.class, null);
			this.tblExperimentFields.addContainerProperty("Key", CheckBox.class, null);
			this.tblExperimentFields.addContainerProperty("Active", CheckBox.class, null);
			this.tblExperimentFields.addContainerProperty("DB Id", TextField.class, null);
			this.tblExperimentFields.addContainerProperty("DB DataType", TextField.class, null);
			this.tblExperimentFields.addContainerProperty("UoM", ComboBox.class, null);

			Object[] itemValues = new Object[7];

			//Loading Child Fields
			
			for(int i=0; i<this.experimentFields.size(); i++)
			{	
				//tblExperimentFields.select(itemId);
				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				itemValues[0] = chxSelect;
				
				TextField txtFieldName = new TextField();
				txtFieldName.setValue(this.experimentFields.get(i).getExpFieldName());
				txtFieldName.setEnabled(false);
				txtFieldName.addStyleName("small");
				itemValues[1] = txtFieldName;
				
				CheckBox chxIsKey = new CheckBox();
				chxIsKey.setValue(this.experimentFields.get(i).isExpFieldIsKey() ? true : false);
				chxIsKey.addStyleName("small");
				itemValues[2] = chxIsKey;
				
				CheckBox chxActive = new CheckBox();
				chxActive.setValue(this.experimentFields.get(i).isExpFieldIsActive() ? true : false);
				chxActive.addStyleName("small");
				itemValues[3] = chxActive;
				
				TextField txtExpDbFieldNameId = new TextField();
				txtExpDbFieldNameId.setValue(this.experimentFields.get(i).getExpDbFieldNameId());
				txtExpDbFieldNameId.setEnabled(false);
				txtExpDbFieldNameId.addStyleName("small");
				itemValues[4] = txtExpDbFieldNameId;
				
				TextField txtExpFieldType = new TextField();
				txtExpFieldType.setValue(this.experimentFields.get(i).getExpFieldType());
				txtExpFieldType.setEnabled(false);
				txtExpFieldType.addStyleName("small");
				itemValues[5] = txtExpFieldType;
				
				ComboBox cbxUnitOfMeasure = new ComboBox("");
				
				for(int j=0; j<unitOfMeasures.size(); j++)
				{
					cbxUnitOfMeasure.addItem(unitOfMeasures.get(j).getUomId());
					cbxUnitOfMeasure.setItemCaption(unitOfMeasures.get(j).getUomId(), unitOfMeasures.get(j).getUomAbbreviation());
					cbxUnitOfMeasure.setWidth(100, Unit.PIXELS);
				}
				
				cbxUnitOfMeasure.setValue(this.experimentFields.get(i).getUnitOfMeasure().getUomId());
				cbxUnitOfMeasure.setNullSelectionAllowed(false);
				cbxUnitOfMeasure.setImmediate(true);
				cbxUnitOfMeasure.addStyleName("small");
				
				itemValues[6] = cbxUnitOfMeasure;
				
				this.tblExperimentFields.addItem(itemValues, this.experimentFields.get(i).getExpFieldId());
		    }
			
			this.tblExperimentFields.setEditable(true);
		}
		btnAddField.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addField();
			}

		});
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
		btnDeleteField.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				removeExperimentFieldItem();
			}
		});

	}
	
	private void addField(){
		
		Object[] itemValues = new Object[7];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		itemValues[0] = chxSelect;
		
		TextField txtFieldName = new TextField();
		txtFieldName.focus();
		txtFieldName.addStyleName("small");
		itemValues[1] = txtFieldName;
		
		CheckBox chxIsKey = new CheckBox();
		chxIsKey.addStyleName("small");
		itemValues[2] = chxIsKey;
		
		CheckBox chxActive = new CheckBox();
		chxActive.addStyleName("small");
		chxActive.setValue(true);
		itemValues[3] = chxActive;
		
		
		TextField txtExpDbFieldNameId = new TextField();
		txtExpDbFieldNameId.addStyleName("small");
		itemValues[4] = txtExpDbFieldNameId;
		
		TextField txtExpFieldType = new TextField();
		txtExpFieldType.addStyleName("small");
		itemValues[5] = txtExpFieldType;
				
		ComboBox cbxUnitOfMeasure = new ComboBox("");
		
		for(int j=0; j<unitOfMeasures.size(); j++)
		{
			cbxUnitOfMeasure.addItem(unitOfMeasures.get(j).getUomId());
			cbxUnitOfMeasure.setItemCaption(unitOfMeasures.get(j).getUomId(), unitOfMeasures.get(j).getUomAbbreviation());
			cbxUnitOfMeasure.setWidth(100, Unit.PIXELS);
		}
		
		cbxUnitOfMeasure.setNullSelectionAllowed(false);
		cbxUnitOfMeasure.setImmediate(true);
		cbxUnitOfMeasure.addStyleName("small");
		
		itemValues[6] = cbxUnitOfMeasure;
		
		this.lastNewItemId = this.lastNewItemId - 1;
		this.tblExperimentFields.addItem(itemValues, this.lastNewItemId);
		this.tblExperimentFields.select(this.lastNewItemId);
	}
	
	private void removeExperimentFieldItem()
	{
		if(this.tblExperimentFields.getValue() != null && (int)this.tblExperimentFields.getValue() < 0)
			this.tblExperimentFields.removeItem((int)this.tblExperimentFields.getValue());
	}
	
	private void onSave()
	{
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		
		//Save experiment
		this.experiment.setExpName(this.txtExpName.getValue());
		this.experiment.setExpDbTableNameId(this.txtExpDbTableNameId.getValue());
		this.experiment.setExpIsActive(this.chxActive.getValue());
		this.experiment.setExpInstructions(this.txtExpInstructions.getValue());
		this.experiment.setExpComments(this.txtExpComments.getValue());
		this.experiment.setLastModifiedBy(sessionUser);
		this.experiment.setModifiedDate(new Date());
		ExperimentDao expDao = new ExperimentDao();
		
		if(this.experiment.getExpId() > 0)
			expDao.updateExperiment(experiment);
		else
		{
			expDao.addExperiment(experiment);
			this.experiment.setCreatedBy(sessionUser);
			this.experiment.setCreatedDate(this.experiment.getModifiedDate());
		}
		
		expDao.updateDBDataTable(experiment);
		
		
		//Save ExperimentFields
		ExperimentFieldDao expfieldDao = new ExperimentFieldDao();
		
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		for (Object itemIdObj : itemIds) 
		{
			
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
			
			ExperimentField experimentField = new ExperimentField();
			experimentField.setExpFieldName(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
			experimentField.setExpFieldIsKey(((CheckBox)(tblRowItem.getItemProperty("Key").getValue())).getValue());
			experimentField.setExpFieldIsActive(((CheckBox)(tblRowItem.getItemProperty("Active").getValue())).getValue());
			experimentField.setExpDbFieldNameId(((TextField)(tblRowItem.getItemProperty("DB Id").getValue())).getValue());
			experimentField.setExpFieldType(((TextField)(tblRowItem.getItemProperty("DB DataType").getValue())).getValue());
			
			UnitOfMeasure selectedUnitOfMeasure = new UnitOfMeasure();
			selectedUnitOfMeasure.setUomId((int)((ComboBox)(tblRowItem.getItemProperty("UoM").getValue())).getValue());
			experimentField.setUnitOfMeasure(selectedUnitOfMeasure);

			experimentField.setExperiment(experiment);
			
			if(itemId > 0)
			{
				experimentField.setExpFieldId(itemId);
				expfieldDao.updateExperimentField(experimentField);
			}
			else
				expfieldDao.addExperimentField(experimentField);
			
			expfieldDao.updateDBDataTableField(experimentField);
		}
		
		closeModalWindow();
    }
	
	private void onDelete()
	{
		this.experiment.setExpIsActive(false);
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		this.experiment.setLastModifiedBy(sessionUser);
		this.experiment.setModifiedDate(new Date());
		ExperimentDao expDao = new ExperimentDao();
		expDao.updateExperiment(experiment);
		closeModalWindow();
    }
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
}
