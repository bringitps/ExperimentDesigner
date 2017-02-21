package com.bringit.experiment.ui.form;

import java.util.Collection;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.ui.design.ExperimentDesign;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class ExperimentForm extends ExperimentDesign {

	private Experiment experiment;
	private List<ExperimentField> experimentFields;
	List <UnitOfMeasure> unitOfMeasures= new UnitOfMeasureDao().getAllUnitOfMeasures();
	
	public ExperimentForm(int experimentId)
	{
		if(experimentId == -1) //New Experiment
		{
			this.btnDelete.setVisible(false);
			experiment = new Experiment();
		}
		else
		{
			//Loading Header Info
			experiment = new ExperimentDao().getExperimentById(experimentId);
			this.txtExpName.setValue(experiment.getExpName());
			this.txtExpDbTableNameId.setValue(experiment.getExpDbTableNameId());
			this.chxActive.setValue(experiment.isExpIsActive());
			this.txtExpInstructions.setValue(experiment.getExpInstructions());
			this.txtExpComments.setValue(experiment.getExpComments());
			
			this.experimentFields = new ExperimentFieldDao().getAllExperimentFieldsByExperiment(experiment);
			 
			this.tblExperimentFields.setContainerDataSource(null);
			this.tblExperimentFields.addContainerProperty("Name", TextField.class, null);
			this.tblExperimentFields.addContainerProperty("Key", CheckBox.class, null);
			this.tblExperimentFields.addContainerProperty("Active", CheckBox.class, null);
			this.tblExperimentFields.addContainerProperty("DB Id", String.class, null);
			this.tblExperimentFields.addContainerProperty("DB DataType", String.class, null);
			this.tblExperimentFields.addContainerProperty("UoM", ComboBox.class, null);

			Object[] itemValues = new Object[6];

			//Loading Child Fields
			
			for(int i=0; i<this.experimentFields.size(); i++)
			{
				
				TextField txtFieldName = new TextField();
				txtFieldName.setValue(this.experimentFields.get(i).getExpFieldName());
				itemValues[0] = txtFieldName;
				
				CheckBox chxIsKey = new CheckBox();
				chxIsKey.setValue(this.experimentFields.get(i).isExpFieldIsKey() ? true : false);
				itemValues[1] = chxIsKey;
				
				CheckBox chxActive = new CheckBox();
				chxActive.setValue(this.experimentFields.get(i).isExpFieldIsActive() ? true : false);
				itemValues[2] = chxActive;
				
				itemValues[3] = this.experimentFields.get(i).getExpDbFieldNameId();
				itemValues[4] = this.experimentFields.get(i).getExpFieldType();
				
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
				
				itemValues[5] = cbxUnitOfMeasure;
				
				this.tblExperimentFields.addItem(itemValues, this.experimentFields.get(i).getExpFieldId());
		    }
			
			//this.tblExperimentFields.setEditable(true);
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
		

	}
	public void addField(){
		
		this.tblExperimentFields.setContainerDataSource(null);
		this.tblExperimentFields.addContainerProperty("Name", TextField.class, null);
		this.tblExperimentFields.addContainerProperty("Key", CheckBox.class, null);
		this.tblExperimentFields.addContainerProperty("Active", CheckBox.class, null);
		this.tblExperimentFields.addContainerProperty("DB Id", String.class, null);
		this.tblExperimentFields.addContainerProperty("DB DataType", String.class, null);
		this.tblExperimentFields.addContainerProperty("UoM", ComboBox.class, null);
		Object[] itemValues = new Object[6];
		
		
		TextField txtFieldName = new TextField();
		itemValues[0] = txtFieldName;
		
		CheckBox chxIsKey = new CheckBox();
		itemValues[1] = chxIsKey;
		
		CheckBox chxActive = new CheckBox();
		itemValues[2] = chxActive;
		
		itemValues[3] = "dbfieldname";
		itemValues[4] = "fieldtype";
		
		ComboBox cbxUnitOfMeasure = new ComboBox("");
		
		for(int j=0; j<unitOfMeasures.size(); j++)
		{
			cbxUnitOfMeasure.addItem(unitOfMeasures.get(j).getUomId());
			cbxUnitOfMeasure.setItemCaption(unitOfMeasures.get(j).getUomId(), unitOfMeasures.get(j).getUomAbbreviation());
			cbxUnitOfMeasure.setWidth(100, Unit.PIXELS);
		}
		
		cbxUnitOfMeasure.setNullSelectionAllowed(false);
		cbxUnitOfMeasure.setImmediate(true);
		
		itemValues[5] = cbxUnitOfMeasure;
		
		this.tblExperimentFields.addItem(itemValues, this.tblExperimentFields.lastItemId());
	}
	public void onSave()
	{
		//Save experiment
		experiment.setExpName(this.txtExpName.getValue());
		experiment.setExpDbTableNameId(this.txtExpDbTableNameId.getValue());
		experiment.setExpIsActive(this.chxActive.getValue());
		experiment.setExpInstructions(this.txtExpInstructions.getValue());
		experiment.setExpComments(this.txtExpComments.getValue());
		ExperimentDao expDao = new ExperimentDao();
		expDao.addExperiment(experiment);
		//Save ExperimentFields
		ExperimentFieldDao expfieldDao = new ExperimentFieldDao();
		
		Collection<?> collItems = this.tblExperimentFields.getItemIds();
		for (Object object : collItems) {
			Item itm = this.tblExperimentFields.getItem(object);
		}
		/*List<ExperimentField> fields = 
		for (ExperimentField experimentField : fields) {
			expfieldDao.addExperimentField(experimentField);
			
		}
		*/
    }
	
}
