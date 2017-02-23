package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.ui.design.ExperimentDesign;
import com.bringit.experiment.util.Config;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
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
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;

public class ExperimentForm extends ExperimentDesign {

	private Experiment experiment;
	private List<ExperimentField> experimentFields;
	List <UnitOfMeasure> unitOfMeasures= new UnitOfMeasureDao().getAllUnitOfMeasures();
	String[] dbfieldTypes;
	
	private int lastNewItemId = 0;
	
	public ExperimentForm(int experimentId)
	{		
		
		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
			dbfieldTypes = configuration.getProperty("sqlserverdatatypes").split(",");
		
		this.tblExperimentFields.setContainerDataSource(null);
		this.tblExperimentFields.addContainerProperty("*", CheckBox.class, null);
		this.tblExperimentFields.addContainerProperty("Name", TextField.class, null);
		this.tblExperimentFields.addContainerProperty("Key", CheckBox.class, null);
		this.tblExperimentFields.addContainerProperty("Active", CheckBox.class, null);
		this.tblExperimentFields.addContainerProperty("DB Id", TextField.class, null);
		this.tblExperimentFields.addContainerProperty("DB DataType", ComboBox.class, null);
		this.tblExperimentFields.addContainerProperty("UoM", ComboBox.class, null);
		this.tblExperimentFields.setPageLength(0);
		
		this.txtExpDbTableNameId.addValidator(new Validator() {

            public void validate(Object value) throws InvalidValueException {
                if(!StringUtils.isAlphanumeric(((String) value).replaceAll("_", "")))
                    throw new InvalidValueException("Only AlphaNumeric and Underscores are allowed for DB Names");
            }
            
        });
		
		if(experimentId == -1) //New Experiment
		{
			this.chxActive.setValue(false);
			this.btnDelete.setEnabled(false);
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
				
				ComboBox cbxFieldType = new ComboBox("");
				
				for(int j=0; j<dbfieldTypes.length; j++)
				{
					cbxFieldType.addItem(dbfieldTypes[j]);
					cbxFieldType.setItemCaption(dbfieldTypes[j], dbfieldTypes[j]);
					cbxFieldType.setWidth(100, Unit.PIXELS);
				}
				
				cbxFieldType.setValue(this.experimentFields.get(i).getExpFieldType());
				cbxFieldType.setEnabled(false);
				itemValues[5] = cbxFieldType;
				
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
			this.tblExperimentFields.setPageLength(0);
			
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
		txtFieldName.setImmediate(true);
		txtFieldName.focus();
		txtFieldName.addStyleName("small");
		txtFieldName.setRequired(true);
		txtFieldName.setRequiredError("This field is mandatory");
		itemValues[1] = txtFieldName;
		
		CheckBox chxIsKey = new CheckBox();
		chxIsKey.addStyleName("small");
		itemValues[2] = chxIsKey;
		
		CheckBox chxActive = new CheckBox();
		chxActive.addStyleName("small");
		chxActive.setValue(true);
		itemValues[3] = chxActive;
		
		
		TextField txtExpDbFieldNameId = new TextField();
		txtExpDbFieldNameId.setRequired(true);
		txtExpDbFieldNameId.setRequiredError("This field is mandatory");
		txtExpDbFieldNameId.addStyleName("small");
		txtExpDbFieldNameId.addValidator(new Validator() {

            public void validate(Object value) throws InvalidValueException {
                if(!StringUtils.isAlphanumeric(((String) value).replaceAll("_", "")))
                    throw new InvalidValueException("Only AlphaNumeric and Underscores are allowed for DB Names");
            }
            
        });
		
		itemValues[4] = txtExpDbFieldNameId;
		
		ComboBox cbxFieldType = new ComboBox("");
		
		for(int j=0; j<dbfieldTypes.length; j++)
		{
			cbxFieldType.addItem(dbfieldTypes[j]);
			cbxFieldType.setItemCaption(dbfieldTypes[j], dbfieldTypes[j]);
			cbxFieldType.setWidth(100, Unit.PIXELS);
		}

		cbxFieldType.setNullSelectionAllowed(false);
		cbxFieldType.setImmediate(true);
		cbxFieldType.setRequired(true);
		cbxFieldType.setRequiredError("This field is mandatory");
		cbxFieldType.addStyleName("small");
		itemValues[5] = cbxFieldType;
		
		ComboBox cbxUnitOfMeasure = new ComboBox("");
		
		for(int j=0; j<unitOfMeasures.size(); j++)
		{
			cbxUnitOfMeasure.addItem(unitOfMeasures.get(j).getUomId());
			cbxUnitOfMeasure.setItemCaption(unitOfMeasures.get(j).getUomId(), unitOfMeasures.get(j).getUomAbbreviation());
			cbxUnitOfMeasure.setWidth(100, Unit.PIXELS);
		}
		
		cbxUnitOfMeasure.setNullSelectionAllowed(false);
		cbxUnitOfMeasure.setImmediate(true);
		cbxUnitOfMeasure.setRequired(true);
		cbxUnitOfMeasure.setRequiredError("This field is mandatory");
		cbxUnitOfMeasure.addStyleName("small");
		
		itemValues[6] = cbxUnitOfMeasure;
		
		this.lastNewItemId = this.lastNewItemId - 1;
		this.tblExperimentFields.addItem(itemValues, this.lastNewItemId);
		this.tblExperimentFields.select(this.lastNewItemId);
		this.tblExperimentFields.setPageLength(0);
		
	}
	
	private void removeExperimentFieldItem()
	{
		if(this.tblExperimentFields.getValue() != null && (int)this.tblExperimentFields.getValue() < 0)
			this.tblExperimentFields.removeItem((int)this.tblExperimentFields.getValue());
	}
	
	private void onSave()
	{
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		boolean validateReqFieldsResult = validateRequiredFields();
		boolean validateDbNameFieldsResult = validateDbNameFields();
		boolean validateDuplicateDbTableNameResult = validateDuplicateDbTableName();
		boolean validateDuplicatedDbNameFieldsResult = validateDuplicatedDbNameFields();
		
		
		//---Validate Required Fields---//
		if(itemIds.size() > 0 && validateReqFieldsResult && validateDbNameFieldsResult && validateDuplicateDbTableNameResult && validateDuplicatedDbNameFieldsResult)
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
			
			if(this.experiment.getExpId() != null )
				expDao.updateExperiment(experiment);
			else
			{
				this.experiment.setCreatedBy(sessionUser);
				this.experiment.setCreatedDate(this.experiment.getModifiedDate());
				expDao.addExperiment(experiment);
			}
			
			expDao.updateDBDataTable(experiment);
			
			
			//Save ExperimentFields
			ExperimentFieldDao expfieldDao = new ExperimentFieldDao();
			
			for (Object itemIdObj : itemIds) 
			{
				
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
				
				ExperimentField experimentField = new ExperimentField();
				experimentField.setExpFieldName(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
				experimentField.setExpFieldIsKey(((CheckBox)(tblRowItem.getItemProperty("Key").getValue())).getValue());
				experimentField.setExpFieldIsActive(((CheckBox)(tblRowItem.getItemProperty("Active").getValue())).getValue());
				experimentField.setExpDbFieldNameId(((TextField)(tblRowItem.getItemProperty("DB Id").getValue())).getValue());
				experimentField.setExpFieldType((String)((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).getValue());
				
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
		else
		{
			if(itemIds.size() <= 0)
				this.getUI().showNotification("Experiment must contain at least 1 Experiment Field", Type.WARNING_MESSAGE);
			else if(!validateReqFieldsResult)
				this.getUI().showNotification("Please fill in all required Fields", Type.WARNING_MESSAGE);
			else if(!validateDbNameFieldsResult)
				this.getUI().showNotification("Only AlphaNumeric and Underscores are allowed for DB Element Names", Type.WARNING_MESSAGE);
			else if(!validateDuplicatedDbNameFieldsResult)
				this.getUI().showNotification("Experiment Field DB Ids must be unique.", Type.WARNING_MESSAGE);
			else if(!validateDuplicateDbTableNameResult)
			{
				this.getUI().showNotification("DB Table Name already exists.", Type.WARNING_MESSAGE);
				this.txtExpDbTableNameId.selectAll();
			}
		}
    }
	
	private void onDelete()
	{
		PopupView pop = new PopupView();
	
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
	
	private boolean validateRequiredFields()
	{
		if(!this.txtExpName.isValid()) return false;
		
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
			
			if(!((TextField)(tblRowItem.getItemProperty("Name").getValue())).isValid()) return false;
			if(!((ComboBox)(tblRowItem.getItemProperty("DB DataType").getValue())).isValid()) return false;
			if(!((ComboBox)(tblRowItem.getItemProperty("UoM").getValue())).isValid()) return false;
			
		}
		
		return true;
	}
	
	private boolean validateDuplicateDbTableName()
	{
		List<Experiment> savedExperiments = new ExperimentDao().getAllExperiments();
		for(int i=0; i<savedExperiments.size(); i++)
		{
			if(this.txtExpDbTableNameId.getValue().equals(savedExperiments.get(i).getExpDbTableNameId()) && 
					this.experiment.getExpId() == savedExperiments.get(i).getExpId())
			return false;
		}
		return true;
	}
	
	private boolean validateDuplicatedDbNameFields()
	{
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		List<String> dbFieldNames = new ArrayList<String>();
	
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
			String dbFieldIdName = ((TextField)(tblRowItem.getItemProperty("DB Id").getValue())).getValue();
			
			if(itemId < 0 && dbFieldNames != null)
			{
				if(dbFieldNames.indexOf(dbFieldIdName) > -1)
					return false;
			}
			
			dbFieldNames.add(dbFieldIdName);			
		}
		
		return true;
	}
	

	private boolean validateDbNameFields()
	{
		if(StringUtils.isEmpty(this.txtExpDbTableNameId.getValue())) 
			return false;
		
		if((!StringUtils.isAlphanumeric(txtExpDbTableNameId.getValue().replaceAll("_", "")))) 
			return false;
		
		Collection itemIds = this.tblExperimentFields.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			if(itemId < 0)
			{
				Item tblRowItem = this.tblExperimentFields.getContainerDataSource().getItem(itemId);
					
				String dbFieldIdName = ((TextField)(tblRowItem.getItemProperty("DB Id").getValue())).getValue();

				if(StringUtils.isEmpty(dbFieldIdName)) 
					return false;
					
				if(!StringUtils.isAlphanumeric(dbFieldIdName.replaceAll("_", "")))
					return false;
			}
		}
		
		return true;
	}
	
	
	
}
