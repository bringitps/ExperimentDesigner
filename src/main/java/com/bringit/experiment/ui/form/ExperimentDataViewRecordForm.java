package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.ExperimentFieldValueUpdateLog;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentFieldValueUpdateLogDao;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.ui.design.ExperimentDataViewRecordDesign;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.ExperimentUtil;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table.TableContextClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class ExperimentDataViewRecordForm extends ExperimentDataViewRecordDesign{

	Integer recordId = -1;
	Experiment experiment = new Experiment();
	List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();

	List<CheckBox> chxRecordDataFields = new ArrayList<CheckBox>(); 
	List<DateField> dtRecordDataFields = new ArrayList<DateField>(); 
	List<TextField> txtRecordDataFields = new ArrayList<TextField>(); 	


	List<ExperimentField> expFieldXRef = new ArrayList<ExperimentField>();
	List<String> recordColumnLabelXRef = new ArrayList<String>();
	List<String> recordColumnValueXRef = new ArrayList<String>();
	Config configuration = new Config();
	
	
	public ExperimentDataViewRecordForm(Experiment experiment, List<ExperimentField> experimentFields, Integer recordId)
	{
		this.recordId = recordId;
		this.experiment = experiment;
		this.experimentFields = experimentFields;
		
		this.lblExperimentTitle.setValue(" -" + experiment.getExpName());
		
		
		String sqlSelectQuery = ExperimentUtil.buildEqualsFilteredSqlSelectQueryByExperiment(this.experiment, this.experimentFields, "Id", this.recordId.toString()); 
		ResultSet experimentDataRecordResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectQuery);
		if(experimentDataRecordResults != null)
		{
			ResultSetMetaData dbQryMetaData;
			try {
				dbQryMetaData = experimentDataRecordResults.getMetaData();
				experimentDataRecordResults.next();
				for(int i=0; i<dbQryMetaData.getColumnCount(); i++)
				{
					recordColumnLabelXRef.add(dbQryMetaData.getColumnLabel(i+1));	
					recordColumnValueXRef.add(experimentDataRecordResults.getString(i+1));
					expFieldXRef.add(new ExperimentField());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}

		buildGridRecordData();
		
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
		
	}
	
	private void buildGridRecordData()
	{
		GridLayout gridRecordData = new GridLayout();
		gridRecordData.setRows((this.experimentFields.size()/2) + 1);
		gridRecordData.setColumns(3);
		gridRecordData.setWidth(100, Unit.PERCENTAGE);
		gridRecordData.setSpacing(true);
		gridRecordData.setMargin(true);
		gridRecordData.setStyleName("tiny");
		
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			for(int i=0; i<this.experimentFields.size(); i++)
			{
				if(experimentFields.get(i).getExpFieldType().contains("date"))
				{
					DateField dtRecordDateField = new DateField();
					dtRecordDateField.setCaption(experimentFields.get(i).getExpFieldName());
					dtRecordDateField.setId(experimentFields.get(i).getExpDbFieldNameId());
					dtRecordDateField.setStyleName("tiny");
					dtRecordDateField.setResolution(Resolution.SECOND);
					
					int recordColumnValueXRefIndex = recordColumnLabelXRef.indexOf(experimentFields.get(i).getExpFieldName());
					if( recordColumnValueXRefIndex > -1 && recordColumnValueXRef.get(recordColumnValueXRefIndex) != null)
					{
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
							dtRecordDateField.setValue(df.parse(recordColumnValueXRef.get(recordColumnValueXRefIndex)));
						} catch (ReadOnlyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						} catch (ConversionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						}						
						expFieldXRef.set(recordColumnValueXRefIndex, experimentFields.get(i));
					}
					dtRecordDataFields.add(dtRecordDateField);
					gridRecordData.addComponent(dtRecordDateField);					
				}
				else if(experimentFields.get(i).getExpFieldType().equals("bit"))
				{
					CheckBox chxRecordDataField = new CheckBox();
					chxRecordDataField.setCaption(experimentFields.get(i).getExpFieldName());
					chxRecordDataField.setId(experimentFields.get(i).getExpDbFieldNameId());
					chxRecordDataField.setStyleName("tiny");
					int recordColumnValueXRefIndex = recordColumnLabelXRef.indexOf(experimentFields.get(i).getExpFieldName());
					if( recordColumnValueXRefIndex > -1)
					{
						chxRecordDataField.setValue(recordColumnValueXRef.get(recordColumnValueXRefIndex).trim().equals("1") ? true : false);
						expFieldXRef.set(recordColumnValueXRefIndex, experimentFields.get(i));
					}
					chxRecordDataFields.add(chxRecordDataField);
					gridRecordData.addComponent(chxRecordDataField);
				}
				else
				{
					TextField txtRecordDataField = new TextField();
					txtRecordDataField.setCaption(experimentFields.get(i).getExpFieldName());
					txtRecordDataField.setId(experimentFields.get(i).getExpDbFieldNameId());
					txtRecordDataField.setStyleName("tiny");
					int recordColumnValueXRefIndex = recordColumnLabelXRef.indexOf(experimentFields.get(i).getExpFieldName());
					if( recordColumnValueXRefIndex > -1)
					{
						txtRecordDataField.setValue(recordColumnValueXRef.get(recordColumnValueXRefIndex));
						expFieldXRef.set(recordColumnValueXRefIndex, experimentFields.get(i));
					}
					txtRecordDataFields.add(txtRecordDataField);
					gridRecordData.addComponent(txtRecordDataField);
				}
			}
			
			this.pnlExperimentFields.setContent(gridRecordData);
			
			int recordColumnValueXRefIndex = recordColumnLabelXRef.indexOf("Comments");
			if( recordColumnValueXRefIndex > -1 
					&& recordColumnValueXRef.get(recordColumnValueXRefIndex) != null 
					&& recordColumnValueXRef.get(recordColumnValueXRefIndex).toString() != "" )
				this.txtComments.setValue(recordColumnValueXRef.get(recordColumnValueXRefIndex));
		}
	}
	
	private void onSave()
	{
		List<String> updateRecordColumnLabelXRef = new ArrayList<String>();
		List<String> updateRecordDbFieldNameIdXRef = new ArrayList<String>();
		List<String> updateRecordDbFieldValueXRef = new ArrayList<String>();
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		
		Date updateDate = new Date();
		DateFormat dfUpdateDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String updateDateStr = dfUpdateDate.format(updateDate);
		try {
			updateDate = dfUpdateDate.parse(updateDateStr);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			
			for(int i=0; i<chxRecordDataFields.size(); i++)
			{
				updateRecordDbFieldNameIdXRef.add(chxRecordDataFields.get(i).getId());
				updateRecordDbFieldValueXRef.add(chxRecordDataFields.get(i).getValue() ? "1" : "0");
				updateRecordColumnLabelXRef.add(chxRecordDataFields.get(i).getCaption().trim());
			}
	
			for(int i=0; i<dtRecordDataFields.size(); i++)
			{
				updateRecordDbFieldNameIdXRef.add(dtRecordDataFields.get(i).getId());
				updateRecordDbFieldValueXRef.add(dtRecordDataFields.get(i).getValue() != null ? df.format(dtRecordDataFields.get(i).getValue())  : null);
				updateRecordColumnLabelXRef.add(dtRecordDataFields.get(i).getCaption().trim());
			}
	
			for(int i=0; i<txtRecordDataFields.size(); i++)
			{
				updateRecordDbFieldNameIdXRef.add(txtRecordDataFields.get(i).getId());
				updateRecordDbFieldValueXRef.add(txtRecordDataFields.get(i).getValue());
				updateRecordColumnLabelXRef.add(txtRecordDataFields.get(i).getCaption().trim());
			}
			
			updateRecordDbFieldNameIdXRef.add("Comments");
			updateRecordDbFieldValueXRef.add(this.txtComments.getValue());
			updateRecordColumnLabelXRef.add("Comments");
			
			updateRecordDbFieldNameIdXRef.add("LastModifiedDate");
			updateRecordDbFieldValueXRef.add(df.format(updateDate));
			updateRecordColumnLabelXRef.add("LastModifiedDate");

			updateRecordDbFieldNameIdXRef.add("LastModifiedBy");
			updateRecordDbFieldValueXRef.add(sessionUser.getUserId().toString());
			updateRecordColumnLabelXRef.add("LastModifiedBy");
		}
		
		String sqlUpdateQuery = ExperimentUtil.buildSqlUpdateQueryByExperiment(this.experiment, updateRecordDbFieldNameIdXRef, updateRecordDbFieldValueXRef, this.recordId.toString());
		ResponseObj updateRecordRespObj = new ExecuteQueryDao().executeUpdateQuery(sqlUpdateQuery);
		if(updateRecordRespObj.getCode() == 0)
		{
			Date lastModifiedDate = new Date();
			
			int lastModifiedDateXRefIndex = recordColumnLabelXRef.indexOf("LastModifiedDate");
			if(lastModifiedDateXRefIndex > -1) 
			{
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					lastModifiedDate = df.parse(recordColumnValueXRef.get(lastModifiedDateXRefIndex));
				} catch (ReadOnlyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				} catch (ConversionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}			
				
			}
			
			for(int i=0; i<recordColumnLabelXRef.size(); i++)
			{
				if(!recordColumnLabelXRef.get(i).toString().trim().equals("LastModifiedDate") && !recordColumnLabelXRef.get(i).toString().trim().equals("CreatedDate"))
				{
					int updatedDataXRefIndex = updateRecordColumnLabelXRef.indexOf(recordColumnLabelXRef.get(i));
					if(updatedDataXRefIndex > -1)
					{ 
						if(recordColumnValueXRef.get(i) == null || updateRecordDbFieldValueXRef.get(updatedDataXRefIndex) == null)
						{
							if(recordColumnValueXRef.get(i) != updateRecordDbFieldValueXRef.get(updatedDataXRefIndex))
							{
								saveExperimentFieldValueUpdateLog(sessionUser, expFieldXRef.get(i), this.recordId, recordColumnLabelXRef.get(i).toString().trim().equals("Comments"), (recordColumnValueXRef.get(i) != null ? recordColumnValueXRef.get(i) : "null"), 
										lastModifiedDate, (updateRecordDbFieldValueXRef.get(updatedDataXRefIndex) != null ? updateRecordDbFieldValueXRef.get(updatedDataXRefIndex) : "null"), updateDate);			
							}
						}
						else if(!recordColumnValueXRef.get(i).toString().trim().equals(updateRecordDbFieldValueXRef.get(updatedDataXRefIndex)))
						{
							saveExperimentFieldValueUpdateLog(sessionUser, expFieldXRef.get(i), this.recordId, recordColumnLabelXRef.get(i).toString().trim().equals("Comments"), recordColumnValueXRef.get(i).toString().trim(), 
								lastModifiedDate, updateRecordDbFieldValueXRef.get(updatedDataXRefIndex), updateDate);			
						}
					}
				}
			}
			
			
			closeModalWindow();
		}
		else
			this.getUI().showNotification("Unexpected error at saving data. Exception Details:" + updateRecordRespObj.getDescription(), Type.ERROR_MESSAGE);
		
	}
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
	private void saveExperimentFieldValueUpdateLog(SysUser sysUser, ExperimentField expField, int dbExpTableRecordId, boolean isCommentsFieldUpdate, String oldValue, Date oldDate, String newValue, Date newDate)
	{
		if(expField.getExpFieldId() != null || isCommentsFieldUpdate)
		{
			ExperimentFieldValueUpdateLog expFieldValueUpdateLog = new ExperimentFieldValueUpdateLog(null, newValue, newDate, newValue, newDate, (expField.getExpFieldId() != null? expField : null), dbExpTableRecordId, isCommentsFieldUpdate, sysUser);

			new ExperimentFieldValueUpdateLogDao().addExperimentFieldValueUpdateLog(expFieldValueUpdateLog);
			
			/*ExperimentFieldValueUpdateLog expFieldValueUpdateLog = new ExperimentFieldValueUpdateLog();
			expFieldValueUpdateLog.setCreatedBy(sysUser);
			if(!isCommentsFieldUpdate) expFieldValueUpdateLog.setExperimentField(expField);
			expFieldValueUpdateLog.setDbTableRecordCommentsUpdate(isCommentsFieldUpdate);			
			expFieldValueUpdateLog.setDbExperimentDataTableRecordId(recordId);
			expFieldValueUpdateLog.setExpNewCreatedDate(newDate);
			expFieldValueUpdateLog.setExpNewValue(newValue);
			expFieldValueUpdateLog.setExpOldCreatedDate(oldDate);
			expFieldValueUpdateLog.setExpOldValue(oldValue);
			new ExperimentFieldValueUpdateLogDao().addExperimentFieldValueUpdateLog(expFieldValueUpdateLog);*/
		}
	}
}
