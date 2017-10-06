package com.bringit.experiment.ui.form;

import com.bringit.experiment.bll.CmForSysRole;
import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.bll.CsvTemplateColumns;
import com.bringit.experiment.bll.CsvTemplateEnrichment;
import com.bringit.experiment.bll.CustomList;
import com.bringit.experiment.bll.CustomListValue;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.CmForSysRoleDao;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.dao.CsvTemplateColumnsDao;
import com.bringit.experiment.dao.CsvTemplateDao;
import com.bringit.experiment.dao.CsvTemplateEnrichmentDao;
import com.bringit.experiment.dao.CustomListDao;
import com.bringit.experiment.dao.CustomListValueDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FilesRepositoryDao;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.remote.RemoteFileUtil;
import com.bringit.experiment.ui.design.CsvTemplateDesign;
import com.opencsv.CSVReader;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class CsvTemplateForm extends CsvTemplateDesign {

	private CsvTemplate csvt;
	private List<CsvTemplateColumns> csvCols;
	private List<FilesRepository> repos =  new FilesRepositoryDao().getAllFilesRepositorys();
	private List<JobExecutionRepeat> jobs = new JobExecutionRepeatDao().getAllJobExecutionRepeats();
	private List<Experiment> experiments = new ExperimentDao().getActiveExperiments();
	private List<ContractManufacturer> contractManufacturers;
	private int lastNewItemId = 0;
    private File tempFile;
    private List<ExperimentField> expFields;
	private List<Integer> dbIdOfCsvTemplateNodeItemsToDelete = new ArrayList<Integer>();
	SysRole sysRoleSession = (SysRole)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");

    private SystemSettings systemSettings;

    private String[] csvTemplateColumns;
    private List<Integer> enrichmentRuleItemIdToDelete = new ArrayList<Integer>();
	
    private int lastNewEnrichmentRuleItemId = 0;
    
	private void loadSpecificContractManufacturer() {
		contractManufacturers = new ArrayList<>();

		if (!"sys_admin".equalsIgnoreCase(sysRoleSession.getRoleName())) {
			List<CmForSysRole> cmForSysRole =  new CmForSysRoleDao().getListOfCmForSysRoleBysysRoleId(sysRoleSession.getRoleId());
			cmForSysRole.forEach(x-> contractManufacturers.add(x.getContractManufacturer()));
		} else {
			contractManufacturers = new ContractManufacturerDao().getAllContractManufacturers();
		}


	}

	public CsvTemplateForm(int csvId)
	{
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		this.comboCsvTExperiment.setCaption(this.systemSettings.getExperimentLabel());
		loadSpecificContractManufacturer();

		this.tblCsvCols.setContainerDataSource(null);
		this.tblCsvCols.addContainerProperty("*", CheckBox.class, null);
		this.tblCsvCols.addContainerProperty("Csv Column", TextField.class, null);
		this.tblCsvCols.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field", ComboBox.class, null);		
		this.tblCsvCols.addContainerProperty("Mandatory", CheckBox.class, null);
		this.tblCsvCols.addContainerProperty("Datetime Format", TextField.class, null);
		this.tblCsvCols.setPageLength(0);
		fillCombos();
		uploadFile();
		
		if(csvId == -1) //New
		{
			this.btnDelete.setEnabled(false);
			this.csvt = new CsvTemplate();
			this.chxActive.setValue(true);
			this.chxActive.setEnabled(false);
			this.btnDelete.setEnabled(false);
			//we disable all the components until an experiment is selected
			enableComponents(false); 
		}
		else
		{
			csvt = new CsvTemplateDao().getCsvTemplateById(csvId);
			this.txtCsvTName.setValue(csvt.getCsvTemplateName());
			this.comboCsvTExperiment.setValue(csvt.getExperiment().getExpId());
			this.comboCsvTExperiment.setEnabled(false);
			this.chxActive.setValue(csvt.isCsvTemplateIsActive());
			this.chxNotScheduled.setValue(csvt.getCsvTemplateNotScheduled());
			if(csvt.getContractManufacturer() != null) this.cbxContractManufacturer.setValue(csvt.getContractManufacturer().getCmId());
			if(csvt.getJobExecRepeat() != null) this.comboCsvjobScheduler.setValue(csvt.getJobExecRepeat().getJobExecRepeatId());
			if(csvt.getInboundFileRepo() != null) this.comboCsvTinRepo.setValue(csvt.getInboundFileRepo().getFileRepoId());
			if(csvt.getProcessedFileRepo() != null) this.comboCsvoutRepo.setValue(csvt.getProcessedFileRepo().getFileRepoId());
			if(csvt.getExceptionFileRepo() != null) this.comboCsvTerrRepo.setValue(csvt.getExceptionFileRepo().getFileRepoId());
			this.txtCsvTComments.setValue(csvt.getCsvTemplateComments());
			if(csvt.getCsvTemplateTransformTxtFound() != null) this.chxTransformTxt.setValue(csvt.getCsvTemplateTransformTxtFound());
			if(csvt.getCsvTemplateColumnsRepeated() != null) this.chxColumnRepeat.setValue(csvt.getCsvTemplateColumnsRepeated());
			
			String opFilterSavedCriteria = "Prefix"; 
			
			if(csvt.getCsvTemplatePrefix() != null)
			{
				opFilterSavedCriteria = "Prefix"; 
				this.txtCsvTFilterCriteria.setValue(csvt.getCsvTemplatePrefix());				
			}
			
			if(csvt.getCsvTemplateSuffix() != null)
			{
				opFilterSavedCriteria = "Suffix"; 
				this.txtCsvTFilterCriteria.setValue(csvt.getCsvTemplateSuffix());				
			}
			
			if(csvt.getCsvTemplateRegex() != null)
			{
				opFilterSavedCriteria = "Regular"; 
				this.txtCsvTFilterCriteria.setValue(csvt.getCsvTemplateRegex());				
			}
			
			Collection itemIds = this.opFilterCriteriaType.getItemIds();
			for (Object itemIdObj : itemIds) 
			{
				if(((String)itemIdObj).startsWith(opFilterSavedCriteria))
					this.opFilterCriteriaType.select(itemIdObj);				
			}
			
			
			this.dtCsvTstart.setValue(csvt.getCsvTemplateExecStartDate());
			this.dtCsvTend.setValue(csvt.getCsvTemplateExecEndDate());
			this.cbxStartHour.setValue(csvt.getCsvTemplateExecStartHour());
			
			
			if(csvt.getCsvTemplateNotScheduled() != null)
			{
				cbxStartHour.setEnabled(!csvt.getCsvTemplateNotScheduled());
				comboCsvjobScheduler.setEnabled(!csvt.getCsvTemplateNotScheduled());
				dtCsvTend.setEnabled(!csvt.getCsvTemplateNotScheduled());
				dtCsvTstart.setEnabled(!csvt.getCsvTemplateNotScheduled());				
			}
						
			
			//this.csvCols = new CsvTemplateColumnsDao().getAllCsvTemplateColumnssByTemplateId(csvt.getCsvTemplateId());
			
			this.csvCols = new CsvTemplateColumnsDao().getAllCsvTemplateColumnsMappingDetailsByTemplateId(csvt.getCsvTemplateId());
			
			if(this.csvCols != null)
			{
				this.csvTemplateColumns = new String[this.csvCols.size()];
				for(int i=0; i<this.csvCols.size(); i++)
					this.csvTemplateColumns[i] =  this.csvCols.get(i).getCsvTemplateColumnName();
			}
			
			this.expFields = new ExperimentFieldDao().getActiveExperimentFields(csvt.getExperiment());
			
			Object[] itemValues = new Object[5];
			for(int i=0; i<this.csvCols.size(); i++)
			{	
				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				itemValues[0] = chxSelect;
				
				TextField txtCsvColName = new TextField();
				txtCsvColName.setValue(this.csvCols.get(i).getCsvTemplateColumnName());
				txtCsvColName.setReadOnly(true);
				txtCsvColName.addStyleName("tiny");
				txtCsvColName.setWidth(100, Unit.PERCENTAGE);
				itemValues[1] = txtCsvColName;
				
				ComboBox cbxExpFields = new ComboBox("");
				for(int j=0; j<expFields.size(); j++)
				{
					cbxExpFields.addItem(expFields.get(j).getExpFieldId());
					cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName() + " [ " + expFields.get(j).getExpFieldType() + " ]");
					cbxExpFields.setWidth(100, Unit.PERCENTAGE);
				}
				
				cbxExpFields.setNullSelectionAllowed(true);
				cbxExpFields.addStyleName("tiny");
				cbxExpFields.setId(this.csvCols.get(i).getCsvTemplateColumnId().toString()); //Id to be reused for DateTime mask logic
				
				if(this.csvCols.get(i).getExpField() != null)
					cbxExpFields.setValue(this.csvCols.get(i).getExpField().getExpFieldId());
				itemValues[2] = cbxExpFields;
				
				CheckBox chxMandatory = new CheckBox();
				chxMandatory.addStyleName("tiny");
				
				if(this.csvCols.get(i).getCsvTemplateColumnMandatory() != null)
					chxMandatory.setValue(this.csvCols.get(i).getCsvTemplateColumnMandatory());
					
				itemValues[3] = chxMandatory;
				
				TextField txtDateTimeMask = new TextField();
				
				if(this.csvCols.get(i).getCsvTemplateColumnDatetimeMask() != null)
					txtDateTimeMask.setValue(this.csvCols.get(i).getCsvTemplateColumnDatetimeMask());
				
				txtDateTimeMask.addStyleName("tiny");
				txtDateTimeMask.setWidth(100, Unit.PERCENTAGE);
				if(this.csvCols.get(i).getExpField() != null && !this.csvCols.get(i).getExpField().getExpFieldType().contains("date"))
					txtDateTimeMask.setEnabled(false);
				
				txtDateTimeMask.setId(this.csvCols.get(i).getCsvTemplateColumnId().toString()); //Id to be reused for DateTime mask logic
				txtDateTimeMask.addValueChangeListener(new ValueChangeListener() {

		            @Override
		            public void valueChange(ValueChangeEvent event) {
		            	onChangeDateTimeMask(txtDateTimeMask);
		            }
		        });
				
				itemValues[4] = txtDateTimeMask;
				
				cbxExpFields.addValueChangeListener(new ValueChangeListener() {

		            @Override
		            public void valueChange(ValueChangeEvent event) {
		            	onChangeExperimentField(cbxExpFields);
		            }
		        });
				
				this.tblCsvCols.addItem(itemValues, this.csvCols.get(i).getCsvTemplateColumnId());
								
			}
	
		}
		
		//Added at 9/4
		//Enrichment Rules feature added to Bit-Exp
		loadTblEnrichmentRulesData();
		
		
		comboCsvTExperiment.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(comboCsvTExperiment.getValue()!=null){
					enableComponents(true);
				}
				
			}   
	    });
		
		chxNotScheduled.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				cbxStartHour.setEnabled(!chxNotScheduled.getValue());
				comboCsvjobScheduler.setEnabled(!chxNotScheduled.getValue());
				dtCsvTstart.setEnabled(!chxNotScheduled.getValue());
				dtCsvTend.setEnabled(!chxNotScheduled.getValue());
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
		
		this.btnAddEnrichmentRule.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addTblEnrichmentRule(null);
			}

		});
		
		this.btnDeleteEnrichmentRule.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deleteTblEnrichmentRule();
			}

		});
	}
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
	@SuppressWarnings("deprecation")
	protected void onSave() {

		//System.out.println("Option Criteria Type Value: " + this.opFilterCriteriaType.getValue());
		
		Collection itemIds = this.tblCsvCols.getContainerDataSource().getItemIds();
		boolean validateCsvTemplateNameResult = validateCsvTemplateName();		
		boolean validateReqFieldsResult = validateRequiredFields();		
		boolean validateNonRepeatedExperimentFieldsResult = validateNonRepeatedExperimentFields();
		boolean validateCsvTemplateEnrichment = validateCsvTemplateEnrichment();
		boolean validateDateTimeMasksResult = validateDateTimeMasks();
		
		//---Validate Required Fields---//
		if(itemIds.size() > 0 && validateReqFieldsResult && validateNonRepeatedExperimentFieldsResult 
				&& validateCsvTemplateNameResult && validateCsvTemplateEnrichment && validateDateTimeMasksResult)
		{
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			
			//Save template
			this.csvt.setExperiment(new ExperimentDao().getExperimentById((int) this.comboCsvTExperiment.getValue()));
			this.csvt.setCsvTemplateName(this.txtCsvTName.getValue());
			this.csvt.setCsvTemplateIsActive(this.chxActive.getValue());
			
			String opFilterSavedCriteria = this.opFilterCriteriaType.getValue().toString().trim(); 

			if(opFilterSavedCriteria.startsWith("Prefix"))
			{
				this.csvt.setCsvTemplatePrefix(this.txtCsvTFilterCriteria.getValue());
				this.csvt.setCsvTemplateSuffix(null);
				this.csvt.setCsvTemplateRegex(null);				
			}

			if(opFilterSavedCriteria.startsWith("Suffix"))
			{
				this.csvt.setCsvTemplatePrefix(null);
				this.csvt.setCsvTemplateSuffix(this.txtCsvTFilterCriteria.getValue());
				this.csvt.setCsvTemplateRegex(null);				
			}

			if(opFilterSavedCriteria.startsWith("Regular"))
			{
				this.csvt.setCsvTemplatePrefix(null);
				this.csvt.setCsvTemplateSuffix(null);
				this.csvt.setCsvTemplateRegex(this.txtCsvTFilterCriteria.getValue());				
			}
			
			//this.csvt.setCsvTemplatePrefix(this.txtCsvTFilterCriteria.getValue());
			
			this.csvt.setCsvTemplateComments(this.txtCsvTComments.getValue());
			this.csvt.setCsvTemplateTransformTxtFound(this.chxTransformTxt.getValue());
			this.csvt.setCsvTemplateColumnsRepeated(this.chxColumnRepeat.getValue());
			
			this.csvt.setExceptionFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboCsvTerrRepo.getValue()));
			this.csvt.setProcessedFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboCsvoutRepo.getValue()));
			this.csvt.setInboundFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboCsvTinRepo.getValue()));
		
			//if(this.comboCsvjobScheduler.getValue()!=null) 
			//	this.csvt.setJobExecRepeat(new JobExecutionRepeatDao().getJobExecutionRepeatById((int) this.comboCsvjobScheduler.getValue()));
			
			if(this.cbxContractManufacturer.getValue()!=null)
				this.csvt.setContractManufacturer(new ContractManufacturerDao().getContractManufacturerById((int) this.cbxContractManufacturer.getValue()));
			else
				this.csvt.setContractManufacturer(null);
			
			System.out.println("Job Exec Repeat Value: " + this.comboCsvjobScheduler.getValue());
			if(this.comboCsvjobScheduler.getValue()!=null) 
				this.csvt.setJobExecRepeat(new JobExecutionRepeatDao().getJobExecutionRepeatById((int) this.comboCsvjobScheduler.getValue()));
			else
				this.csvt.setJobExecRepeat(null);
			
		
			//Commented due to Issue found at saving Not Scheduled Templates
			//System.out.println("Job Exec Repeat Value Selected: " + this.csvt.getJobExecRepeat().getJobExecRepeatId());
			this.csvt.setLastModifiedBy(sessionUser);
			this.csvt.setModifiedDate(new Date());
			
			this.csvt.setCsvTemplateNotScheduled(chxNotScheduled.getValue());
			
			if(chxNotScheduled.getValue())
			{
				this.csvt.setJobExecRepeat(null);
				this.csvt.setCsvTemplateExecStartDate(null);
				this.csvt.setCsvTemplateExecEndDate(null);
				this.csvt.setCsvTemplateExecStartHour(null);
			}
			else
			{
				this.csvt.setCsvTemplateExecStartDate(this.dtCsvTstart.getValue());
				this.csvt.setCsvTemplateExecEndDate(this.dtCsvTend.getValue());
				this.csvt.setCsvTemplateExecStartHour((int) this.cbxStartHour.getValue());
			}
			
			if(this.csvt.getCsvTemplateId() != null ) {
				new CsvTemplateDao().updateCsvTemplate(csvt);
				
				CsvTemplate csvTemplateWithId = new CsvTemplateDao().getActiveCsvTemplateByName(this.txtCsvTName.getValue());
				
				// added logic to schedule the CSV Tempate
				RemoteFileUtil remoteFileUtil = RemoteFileUtil.getInstance();
				
				if(csvTemplateWithId.isCsvTemplateIsActive() && (csvTemplateWithId.getCsvTemplateNotScheduled() == null || !csvTemplateWithId.getCsvTemplateNotScheduled()))
				{
					remoteFileUtil.cancelJob(csvt);
					remoteFileUtil.updateJob(csvTemplateWithId);
				}
				else
					remoteFileUtil.cancelJob(csvt);				
			} else
			{
				this.csvt.setCreatedBy(sessionUser);
				this.csvt.setCreatedDate(this.csvt.getModifiedDate());
				new CsvTemplateDao().addCsvTemplate(csvt);
				CsvTemplate csvTemplateWithId = new CsvTemplateDao().getActiveCsvTemplateByName(this.txtCsvTName.getValue());
				
				RemoteFileUtil remoteFileUtil = RemoteFileUtil.getInstance();

				if(csvt.getCsvTemplateNotScheduled() == null || !csvt.getCsvTemplateNotScheduled())
					remoteFileUtil.updateJob(csvTemplateWithId);
			}
			

			CsvTemplateColumnsDao csvTempColsDao = new CsvTemplateColumnsDao();
			
			//Remove deprecated CsvTemplateColumns
			if(this.csvt.getCsvTemplateId() != null && dbIdOfCsvTemplateNodeItemsToDelete.size() > 0)
			{
				for(int i=0; i<dbIdOfCsvTemplateNodeItemsToDelete.size(); i++)
					csvTempColsDao.deleteCsvTemplateColumns(dbIdOfCsvTemplateNodeItemsToDelete.get(i));
			}
			
			for (Object itemIdObj : itemIds) 
			{
				
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblCsvCols.getContainerDataSource().getItem(itemId);
				
				CsvTemplateColumns csvColumn = new CsvTemplateColumns();
				csvColumn.setCsvTemplateColumnName(((TextField)(tblRowItem.getItemProperty("Csv Column").getValue())).getValue());			
				csvColumn.setCsvTemplateColumnMandatory(((CheckBox)(tblRowItem.getItemProperty("Mandatory").getValue())).getValue());
				csvColumn.setCsvTemplate(csvt);
				
				if(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue() != null)
				{
					ExperimentField selectedExpField = new ExperimentField();
					selectedExpField.setExpFieldId((int)((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue());

					csvColumn.setExpField(selectedExpField);
				}
				else
					csvColumn.setExpField(null);
				
				csvColumn.setCsvTemplateColumnDatetimeMask(((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).getValue());
				
				
				if(itemId > 0)
				{
					csvColumn.setCsvTemplateColumnId(itemId);
					
					csvTempColsDao.updateCsvTemplateColumns(csvColumn);
				}
				else
					csvTempColsDao.addCsvTemplateColumns(csvColumn);
				
			}
			

			//Added at 9/4
			//Enrichment Rules feature added to Bit-Exp
			Collection enrichmentRuleItemIds = this.tblEnrichmentRules.getContainerDataSource().getItemIds();
			
			CsvTemplateEnrichmentDao csvTemplateEnrichmentDao = new CsvTemplateEnrichmentDao();
			
			for (Object enrichmentRuleItemIdObj : enrichmentRuleItemIds) 
			{
				int enrichmentRuleItemId = (int)enrichmentRuleItemIdObj;
				Item tblEnrichmentRuleRowItem = this.tblEnrichmentRules.getContainerDataSource().getItem(enrichmentRuleItemId);
				
				CsvTemplateEnrichment csvTemplateEnrichment = new CsvTemplateEnrichment();
				csvTemplateEnrichment.setCsvTemplate(csvt);
				csvTemplateEnrichment.setCsvTemplateEnrichmentColumnNameSource(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("CSV Column Source").getValue())).getValue().toString());			
				csvTemplateEnrichment.setCsvTemplateEnrichmentOperation(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("Operator").getValue())).getValue().toString());			
				csvTemplateEnrichment.setCsvTemplateEnrichmentValue(((TextField)(tblEnrichmentRuleRowItem.getItemProperty("Value").getValue())).getValue().toString());			
				csvTemplateEnrichment.setCsvTemplateEnrichmentType((((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("Enrichment Type").getValue())).getValue().toString()));			
				
				String customListValueStrId = "";
				if(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("List Value").getValue())).getValue() != null)
					customListValueStrId = ((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("List Value").getValue())).getValue().toString();

				if(!customListValueStrId.isEmpty())
					csvTemplateEnrichment.setCustomListValue(new CustomListValueDao().getCustomListValueById(Integer.parseInt(customListValueStrId)));			
				
				csvTemplateEnrichment.setCsvTemplateEnrichmentStaticValue(((TextField)(tblEnrichmentRuleRowItem.getItemProperty("Static Value").getValue())).getValue().toString());			
				
				String experimentFieldDestinationStrId = "";
				if(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field Destination").getValue())).getValue() != null)
					experimentFieldDestinationStrId = ((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field Destination").getValue())).getValue().toString();
				
				if(!experimentFieldDestinationStrId.isEmpty())
					csvTemplateEnrichment.setExpFieldDestination(new ExperimentFieldDao().getExperimentFieldById(Integer.parseInt(experimentFieldDestinationStrId)));
				
				if(enrichmentRuleItemId > 0)
				{
					csvTemplateEnrichment.setCsvTemplateEnrichmentId(enrichmentRuleItemId);
					csvTemplateEnrichmentDao.updateCsvTemplateEnrichment(csvTemplateEnrichment);
				}
				else
					csvTemplateEnrichmentDao.addCsvTemplateEnrichment(csvTemplateEnrichment);
			}
			
			if(this.enrichmentRuleItemIdToDelete.size() > 0)
			{
				for(int i=0; i<this.enrichmentRuleItemIdToDelete.size(); i++)
				{
					if(this.enrichmentRuleItemIdToDelete.get(i)>0)
						csvTemplateEnrichmentDao.deleteCsvTemplateEnrichment(this.enrichmentRuleItemIdToDelete.get(i));
				}
			}
			
			closeModalWindow();
		}
		else
		{
			if(itemIds.size() <= 0)
				this.getUI().showNotification("There are no csv columns mapped on your CsvTemplate", Type.WARNING_MESSAGE);
			else if(!validateReqFieldsResult)
				this.getUI().showNotification("Please fill in all required Fields", Type.WARNING_MESSAGE);
			else if(!validateNonRepeatedExperimentFieldsResult)
				this.getUI().showNotification("You can not map 1 " + this.systemSettings.getExperimentLabel() + " Field to 2 or more CSV Columns.", Type.WARNING_MESSAGE);
			else if(!validateCsvTemplateNameResult)
				this.getUI().showNotification("Name is already selected for another Csv Template. Please rename CsvTemplate", Type.WARNING_MESSAGE);
			else if(!validateCsvTemplateEnrichment)
				this.getUI().showNotification("Please fill in all required Fields for Csv Template Enrichment", Type.WARNING_MESSAGE);
			else if(!validateDateTimeMasksResult)
				this.getUI().showNotification("Some Datetime Formats are invalid.", Type.WARNING_MESSAGE);
		}
	}
	
	private boolean validateRequiredFields()
	{
		if(!this.txtCsvTName.isValid()) return false;
		if(!this.txtCsvTFilterCriteria.isValid()) return false;
		if(!this.comboCsvoutRepo.isValid()) return false;
		if(!this.comboCsvTerrRepo.isValid()) return false;
		if(!this.comboCsvTinRepo.isValid()) return false;
		if(!this.comboCsvTExperiment.isValid()) return false;
		
		if(!this.chxNotScheduled.getValue())
		{
			if(!this.cbxStartHour.isValid()) return false;
			if(!this.comboCsvjobScheduler.isValid()) return false;
			if(!this.dtCsvTstart.isValid()) return false;
		}
		
		return true;
	}
	
	private boolean validateCsvTemplateName()
	{
		if(this.csvt.getCsvTemplateId() == null && new CsvTemplateDao().getActiveCsvTemplateByName(this.txtCsvTName.getValue()) != null)
			return false;
		return true;
	}

	private boolean validateNonRepeatedExperimentFields()
	{
		List<Integer> selectedExpFields = new ArrayList<Integer>();
		
		Collection itemIds = this.tblCsvCols.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblCsvCols.getContainerDataSource().getItem(itemId);
			
			if(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue() != null)
			{
				if(selectedExpFields.indexOf(((int)((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue())) > -1)
					return false;
				else
					selectedExpFields.add(((int)((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue()));
			}
		}
		return true;
	}
	
	private boolean validateCsvTemplateEnrichment()
	{
		Collection enrichmentRuleItemIds = this.tblEnrichmentRules.getContainerDataSource().getItemIds();
		
		for (Object enrichmentRuleItemIdObj : enrichmentRuleItemIds) 
		{
			int enrichmentRuleItemId = (int)enrichmentRuleItemIdObj;
			Item tblEnrichmentRuleRowItem = this.tblEnrichmentRules.getContainerDataSource().getItem(enrichmentRuleItemId);
			
			String csvColumnSourceValue = "";
			if(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("CSV Column Source").getValue())).getValue() != null)
				csvColumnSourceValue = ((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty("CSV Column Source").getValue())).getValue().toString();
			if(csvColumnSourceValue.isEmpty())
				return false;
			
			String experimentFieldDestinationStrId = "";
			if(((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field Destination").getValue())).getValue() != null)
				experimentFieldDestinationStrId = ((ComboBox)(tblEnrichmentRuleRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field Destination").getValue())).getValue().toString();
			if(experimentFieldDestinationStrId.isEmpty())
				return false;
		}
			
		return true;
	}
	
	private boolean validateDateTimeMasks()
	{
		Collection itemIds = this.tblCsvCols.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblCsvCols.getContainerDataSource().getItem(itemId);
			if(((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())) != null)
			{
				String dateTimeMaskValue = ((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).getValue();
				if(dateTimeMaskValue != null && !dateTimeMaskValue.isEmpty())
				{
					try
					{					
						DateFormat dateFormat = new SimpleDateFormat(dateTimeMaskValue);	
						String dateStr = dateFormat.format(new Date());
						Date dateConverted = new SimpleDateFormat(dateTimeMaskValue).parse(dateStr);
						System.out.println("Date converted: " + dateConverted);					
					}
					catch(Exception e)
					{
						((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).selectAll();
						((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).focus();
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	private void uploadFile() {

        class MyReceiver implements Receiver {
            //private static final long serialVersionUID = -1276759102490466761L;

            public OutputStream receiveUpload(String filename, String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Output stream to write to
                try {
                    // Open the file for writing.
                	tempFile = File.createTempFile("temp", ".csv");
                    fos = new FileOutputStream(tempFile);
                }  catch (IOException e) {
      	          e.printStackTrace();
    	          return null;
                }
                return fos; // Return the output stream to write to
            }
        };
        final MyReceiver uploader = new MyReceiver(); 
        upCsv.setReceiver(uploader);
	    upCsv.addFinishedListener(new Upload.FinishedListener() {
	        @Override
	        public void uploadFinished(Upload.FinishedEvent finishedEvent) {
	        	CSVReader reader = null;
				try {
				
					reader = new CSVReader(new FileReader(tempFile));
					//reader = new CSVReader(new InputStreamReader(new FileInputStream(tempFile), "UTF-8"));
	    			if(reader != null)
	    			{
	    				String[] csvHeader = reader.readNext();
	    				fillNodes(csvHeader);

	    				//Added at 9/4
	    				//Enrichment Rules feature added to Bit-Exp
	    				csvTemplateColumns = csvHeader;
	    				resetTblEnrichmentRulesData();
		            	
	    				comboCsvTExperiment.setEnabled(false);
			            reader.close();
	    			}
	    		
	    			tempFile.delete();
				} catch ( IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	      });
		
	}
	
	private void enableComponents(boolean b) {
		this.txtCsvTName.setEnabled(b);
		this.txtCsvTComments.setEnabled(b);
		this.opFilterCriteriaType.setEnabled(b);
		this.txtCsvTFilterCriteria.setEnabled(b);
		this.cbxContractManufacturer.setEnabled(b);
		this.comboCsvoutRepo.setEnabled(b);
		this.comboCsvTerrRepo.setEnabled(b);
		this.comboCsvTinRepo.setEnabled(b);
		this.tblCsvCols.setEnabled(b);
		this.upCsv.setEnabled(b);
		this.comboCsvjobScheduler.setEnabled(b);
		this.cbxStartHour.setEnabled(b);
		
		this.csvTEnrichmentRulesLayout.setEnabled(b);
	}
	
	private void fillNodes(String[] columns) {
		
		if(this.csvt.getCsvTemplateId() != null && this.csvt.getCsvTemplateId() > 0)
		{
			//Remove deprecated Nodes from UI and store Ids to delete from DB at saving
			int loopNodeCnt = 0;
			Collection itemIds = this.tblCsvCols.getContainerDataSource().getItemIds();
			
			for (Object itemIdObj : itemIds) 
			{	
				int itemId = (int)itemIdObj;
				if(itemId > 0)
					dbIdOfCsvTemplateNodeItemsToDelete.add(itemId);
			}			
			
			this.tblCsvCols.setContainerDataSource(null);
			this.tblCsvCols.addContainerProperty("*", CheckBox.class, null);
			this.tblCsvCols.addContainerProperty("Csv Column", TextField.class, null);
			this.tblCsvCols.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field", ComboBox.class, null);		
			this.tblCsvCols.addContainerProperty("Mandatory", CheckBox.class, null);
			this.tblCsvCols.addContainerProperty("Datetime Format", TextField.class, null);
			this.tblCsvCols.setPageLength(0);
			
		}
		
		ExperimentDao expdao = new ExperimentDao();
		Experiment expNew = expdao.getExperimentById((int)(this.comboCsvTExperiment.getValue()));
		expFields = new ExperimentFieldDao().getActiveExperimentFields(expNew);

		//Same column name scenario
		List<String> addedCsvColumnNameMtx = new ArrayList<String>();
		List<Integer> addedCsvColumnTimesMtx = new ArrayList<Integer>();
		
		Object[] itemValues = new Object[5];
		for(int i=0; i<columns.length; i++)
		{
			String csvColumnName = columns[i];
			Integer csvColumnAddedTimes = 0;
			
			if(this.chxColumnRepeat.getValue() != null && this.chxColumnRepeat.getValue())
			{
				if(addedCsvColumnNameMtx.indexOf(columns[i]) != -1)
				{
					csvColumnAddedTimes = addedCsvColumnTimesMtx.get(addedCsvColumnNameMtx.indexOf(csvColumnName));
					csvColumnAddedTimes = csvColumnAddedTimes + 1;
				
					csvColumnName = csvColumnName + "_" + csvColumnAddedTimes;
				}
			}
			
			CheckBox chxSelect = new CheckBox();
			chxSelect.setVisible(false);
			itemValues[0] = chxSelect;
			
			TextField txtCsvColName = new TextField();
			txtCsvColName.setValue(csvColumnName);
			txtCsvColName.setReadOnly(true);
			txtCsvColName.addStyleName("tiny");
			txtCsvColName.setWidth(100, Unit.PERCENTAGE);
			itemValues[1] = txtCsvColName;
			

			ComboBox cbxExpFields = new ComboBox("");
			for(int j=0; j<expFields.size(); j++)
			{
				cbxExpFields.addItem(expFields.get(j).getExpFieldId());
				cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName() + " [ " + expFields.get(j).getExpFieldType() + " ]");
				cbxExpFields.setWidth(100, Unit.PERCENTAGE);
			}
			
			cbxExpFields.setNullSelectionAllowed(true);
			cbxExpFields.addStyleName("tiny");
			itemValues[2] = cbxExpFields;
			

			CheckBox chxMandatory = new CheckBox();
			chxMandatory.addStyleName("tiny");
			chxMandatory.setValue(false);				
			itemValues[3] = chxMandatory;
			
			TextField txtDateTimeMask = new TextField();
			txtDateTimeMask.addStyleName("tiny");
			txtDateTimeMask.setWidth(100, Unit.PERCENTAGE);
			txtDateTimeMask.setEnabled(false);

			itemValues[4] = txtDateTimeMask;
						
			this.lastNewItemId = this.lastNewItemId - 1;

			cbxExpFields.setId(""+this.lastNewItemId); //Id to be reused for DateTime mask logic
			cbxExpFields.addValueChangeListener(new ValueChangeListener() {

	            @Override
	            public void valueChange(ValueChangeEvent event) {
	            	onChangeExperimentField(cbxExpFields);
	            }
	        });

			txtDateTimeMask.setId(""+this.lastNewItemId); //Id to be reused for DateTime mask logic
			txtDateTimeMask.addValueChangeListener(new ValueChangeListener() {

	            @Override
	            public void valueChange(ValueChangeEvent event) {
	            	onChangeDateTimeMask(txtDateTimeMask);
	            }
	        });

			this.tblCsvCols.addItem(itemValues, this.lastNewItemId);

			if(this.chxColumnRepeat.getValue() != null && this.chxColumnRepeat.getValue())
			{
				if(addedCsvColumnNameMtx.indexOf(columns[i]) != -1)
					addedCsvColumnTimesMtx.set(addedCsvColumnNameMtx.indexOf(columns[i]), csvColumnAddedTimes);
				else
				{
					addedCsvColumnNameMtx.add(columns[i]);
					addedCsvColumnTimesMtx.add(0);
				}
			}
		}
	}

	private void fillCombos() {
		//Experiments
		for(int j=0; j<experiments.size(); j++)
		{
			this.comboCsvTExperiment.addItem(experiments.get(j).getExpId());
			this.comboCsvTExperiment.setItemCaption(experiments.get(j).getExpId(), experiments.get(j).getExpName());
			//this.comboCsvTExperiment.setWidth(100, Unit.PIXELS);
		}
		
		this.comboCsvTExperiment.setNullSelectionAllowed(false);
		this.comboCsvTExperiment.setImmediate(true);
		//this.comboCsvTExperiment.addStyleName("small");
		
		
		//Contract Manufacturer
		
		this.cbxContractManufacturer.setNullSelectionAllowed(true);
		this.cbxContractManufacturer.setImmediate(true);
		//this.cbxContractManufacturer.addStyleName("small");
		
		for(int i=0; contractManufacturers!=null && i<contractManufacturers.size(); i++)
		{
			this.cbxContractManufacturer.addItem(contractManufacturers.get(i).getCmId());
			this.cbxContractManufacturer.setItemCaption(contractManufacturers.get(i).getCmId(), contractManufacturers.get(i).getCmName());
		}
		
		//File Repos
		for(int j=0; j<repos.size(); j++)
		{
			this.comboCsvTinRepo.addItem(repos.get(j).getFileRepoId());
			this.comboCsvTinRepo.setItemCaption(repos.get(j).getFileRepoId(), "(" + repos.get(j).getFileRepoName() + ") " + repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			//this.comboCsvTinRepo.setWidth(100, Unit.PIXELS);
			
			this.comboCsvoutRepo.addItem(repos.get(j).getFileRepoId());
			this.comboCsvoutRepo.setItemCaption(repos.get(j).getFileRepoId(), "(" + repos.get(j).getFileRepoName() + ") " + repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			//this.comboCsvoutRepo.setWidth(100, Unit.PIXELS);
			
			this.comboCsvTerrRepo.addItem(repos.get(j).getFileRepoId());
			this.comboCsvTerrRepo.setItemCaption(repos.get(j).getFileRepoId(), "(" + repos.get(j).getFileRepoName() + ") " + repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			//this.comboCsvTerrRepo.setWidth(100, Unit.PIXELS);
		}
		
		
		this.comboCsvTinRepo.setNullSelectionAllowed(true);
		this.comboCsvTinRepo.setImmediate(true);
		//this.comboCsvTinRepo.addStyleName("small");
		
		this.comboCsvoutRepo.setNullSelectionAllowed(true);
		this.comboCsvoutRepo.setImmediate(true);
		//this.comboCsvoutRepo.addStyleName("small");
		
		this.comboCsvTerrRepo.setNullSelectionAllowed(true);
		this.comboCsvTerrRepo.setImmediate(true);
		//this.comboCsvTerrRepo.addStyleName("small");
		
		//Jobs
		for(int j=0; j<jobs.size(); j++)
		{
			this.comboCsvjobScheduler.addItem(jobs.get(j).getJobExecRepeatId());
			this.comboCsvjobScheduler.setItemCaption(jobs.get(j).getJobExecRepeatId(), jobs.get(j).getJobExecRepeatName());
		}
		
		this.comboCsvjobScheduler.setNullSelectionAllowed(true);
		this.comboCsvjobScheduler.setImmediate(true);
		//this.comboCsvjobScheduler.addStyleName("small");
		
		//Hour Execution Start
		this.cbxStartHour.setNullSelectionAllowed(false);
		for(int i=0; i<24; i++)
		{
			this.cbxStartHour.addItem(i);
			this.cbxStartHour.setItemCaption(i, (i<10 ? "0" :"") + i + ":00");
		}
		
	}
	
	private void onDelete()
	{	 
		this.csvt.setCsvTemplateIsActive(false);
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		this.csvt.setLastModifiedBy(sessionUser);
		this.csvt.setModifiedDate(new Date());
		this.csvt.setContractManufacturer(null);
		CsvTemplateDao csvDao = new CsvTemplateDao();
		csvDao.updateCsvTemplate(csvt);

        RemoteFileUtil rfu = RemoteFileUtil.getInstance();
        rfu.cancelJob(this.csvt);

		closeModalWindow();
    }
	
	private void loadTblEnrichmentRulesData()
	{
		this.tblEnrichmentRules.setContainerDataSource(null);
		this.tblEnrichmentRules.setStyleName("small");
		this.tblEnrichmentRules.addContainerProperty("*", CheckBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("CSV Column Source", ComboBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("Operator", ComboBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("Value", TextField.class, null);
		this.tblEnrichmentRules.addContainerProperty("Enrichment Type", ComboBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("Custom List", ComboBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("List Value", ComboBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("Static Value", TextField.class, null);
		this.tblEnrichmentRules.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field Destination", ComboBox.class, null);
		this.tblEnrichmentRules.setEditable(true);
		this.tblEnrichmentRules.setPageLength(0);
		this.tblEnrichmentRules.setColumnWidth("*", 20);
		
		if(this.csvt.getCsvTemplateId() != null && this.csvt.getCsvTemplateId() > -1)
		{
			List<CsvTemplateEnrichment> csvTemplateEnrichmentRules = new CsvTemplateEnrichmentDao().getAllCsvTemplateEnrichmentByTemplateId(this.csvt.getCsvTemplateId());
			
			for(int i=0; i<csvTemplateEnrichmentRules.size(); i++)
				addTblEnrichmentRule(csvTemplateEnrichmentRules.get(i).getCsvTemplateEnrichmentId());
		}
	}
	
	private void resetTblEnrichmentRulesData()
	{
		Collection enrichmentRuleItemIds = this.tblEnrichmentRules.getContainerDataSource().getItemIds();
		
		for (Object enrichmentRuleItemIdObj : enrichmentRuleItemIds) 
			enrichmentRuleItemIdToDelete.add(Integer.parseInt(enrichmentRuleItemIdObj.toString()));
		
		this.tblEnrichmentRules.setContainerDataSource(null);
		this.tblEnrichmentRules.setStyleName("small");
		this.tblEnrichmentRules.addContainerProperty("*", CheckBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("CSV Column Source", ComboBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("Operator", ComboBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("Value", TextField.class, null);
		this.tblEnrichmentRules.addContainerProperty("Enrichment Type", ComboBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("Custom List", ComboBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("List Value", ComboBox.class, null);
		this.tblEnrichmentRules.addContainerProperty("Static Value", TextField.class, null);
		this.tblEnrichmentRules.addContainerProperty(this.systemSettings.getExperimentLabel() + " Field Destination", ComboBox.class, null);
		this.tblEnrichmentRules.setEditable(true);
		this.tblEnrichmentRules.setPageLength(0);
		this.tblEnrichmentRules.setColumnWidth("*", 20);		
		
		
	}

	private void addTblEnrichmentRule(Integer csvEnrichmentRuleId)
	{
		
		Integer itemId = csvEnrichmentRuleId;
		if(itemId == null)
		{	
			this.lastNewEnrichmentRuleItemId = this.lastNewEnrichmentRuleItemId - 1;
			itemId = this.lastNewEnrichmentRuleItemId;
		}
		
		Object[] itemValues = new Object[9];
		
		//Dummy Initial Column
		CheckBox chxSelect = new CheckBox();
		chxSelect.setStyleName("tiny");
		chxSelect.setVisible(false);
		chxSelect.setHeight(20, Unit.PIXELS);
		itemValues[0] = chxSelect;
		
		//Fill CSV Column ComboBox
		ComboBox cbxCsvColumnSource = new ComboBox("");
		cbxCsvColumnSource.setStyleName("tiny");
		cbxCsvColumnSource.setRequired(true);
		cbxCsvColumnSource.setRequiredError("This field is required.");
		for(int i=0; i<this.csvTemplateColumns.length; i++)
		{
			cbxCsvColumnSource.addItem(this.csvTemplateColumns[i]);
			cbxCsvColumnSource.setItemCaption(this.csvTemplateColumns[i], this.csvTemplateColumns[i]);
			cbxCsvColumnSource.setWidth(100, Unit.PERCENTAGE);
		}
		cbxCsvColumnSource.setHeight(20, Unit.PIXELS);
		itemValues[1] = cbxCsvColumnSource;
		
				
		//Fill Operator ComboBox
		ComboBox cbxOperator = new ComboBox("");
		cbxOperator.setContainerDataSource(null);
		cbxOperator.setStyleName("tiny");		
		cbxOperator.addItem("contains");
		cbxOperator.setItemCaption("contains", "contains");
		cbxOperator.addItem("doesnotcontain");
		cbxOperator.setItemCaption("doesnotcontain", "does not contain");
		cbxOperator.addItem("doesnotstartwith");
		cbxOperator.setItemCaption("doesnotstartwith", "does not start with");
		cbxOperator.addItem("endswith");
		cbxOperator.setItemCaption("endswith", "ends with");
		cbxOperator.addItem("is");
		cbxOperator.setItemCaption("is", "is");
		cbxOperator.addItem("isempty");
		cbxOperator.setItemCaption("isempty", "is empty");
		cbxOperator.addItem("isnot");
		cbxOperator.setItemCaption("isnot", "is not");
		cbxOperator.addItem("isnotempty");
		cbxOperator.setItemCaption("isnotempty", "is not empty");
		cbxOperator.addItem("startswith");
		cbxOperator.setItemCaption("startswith", "starts with");
		cbxOperator.addItem("matchesregex");
		cbxOperator.setItemCaption("matchesregex", "matches Regular Expression");
		cbxOperator.select("is");
		cbxOperator.setHeight(20, Unit.PIXELS);
		itemValues[2] = cbxOperator;
		
		//Comparison Value TextField
		TextField txtComparisonValue = new TextField();
		txtComparisonValue.addStyleName("tiny");
		txtComparisonValue.setWidth(100, Unit.PIXELS);
		txtComparisonValue.setHeight(20, Unit.PIXELS);
		itemValues[3] = txtComparisonValue;
		
		//Fill Enrichment Type ComboBox
		ComboBox cbxEnrichmentType = new ComboBox("");
		cbxEnrichmentType.setContainerDataSource(null);
		cbxEnrichmentType.setStyleName("tiny");		
		cbxEnrichmentType.addItem("customlist");
		cbxEnrichmentType.setItemCaption("customlist", "Custom List XREF");
		cbxEnrichmentType.addItem("staticvalue");
		cbxEnrichmentType.setItemCaption("staticvalue", "Static Value");
		cbxEnrichmentType.addItem("positivenumber");
		cbxEnrichmentType.setItemCaption("positivenumber", "Cast to Positive Number");
		cbxEnrichmentType.addItem("negativenumber");
		cbxEnrichmentType.setItemCaption("negativenumber", "Cast to Negative Number");
		cbxEnrichmentType.select("customlist");
		cbxEnrichmentType.setId(itemId.toString()); //Item Id to be reused on Change event

		cbxEnrichmentType.setHeight(20, Unit.PIXELS);
		itemValues[4] = cbxEnrichmentType;
		
		//Fill Custom List ComboBox
		ComboBox cbxCustomList = new ComboBox("");
		cbxCustomList.setContainerDataSource(null);
		cbxCustomList.setStyleName("tiny");		
		cbxCustomList.setId(itemId.toString()); //Item Id to be reused on Change event
		List<CustomList> customLists = new CustomListDao().getAllCustomLists();

		for(int i=0; i<customLists.size(); i++)
		{
			cbxCustomList.addItem(customLists.get(i).getCustomListId());
			cbxCustomList.setItemCaption(customLists.get(i).getCustomListId(), customLists.get(i).getCustomListName());
			cbxCustomList.setWidth(100, Unit.PERCENTAGE);
		}
		

		cbxCustomList.setHeight(20, Unit.PIXELS);
		itemValues[5] = cbxCustomList;
		
		//Fill Custom List Value ComboBox
		ComboBox cbxCustomListValue = new ComboBox("");
		cbxCustomListValue.setContainerDataSource(null);
		cbxCustomListValue.setStyleName("tiny");	
		cbxCustomListValue.setHeight(20, Unit.PIXELS);
		itemValues[6] = cbxCustomListValue;
		
		//Static Value TextField
		TextField txtStaticValue = new TextField();
		txtStaticValue.addStyleName("tiny");
		txtStaticValue.setWidth(100, Unit.PIXELS);
		txtStaticValue.setEnabled(false);
		txtStaticValue.setHeight(20, Unit.PIXELS);
		itemValues[7] = txtStaticValue;
					
		//Experiment Field ComboBox
		ExperimentDao experimentDao = new ExperimentDao();
		Experiment selectedExperiment = experimentDao.getExperimentById((int)(this.comboCsvTExperiment.getValue()));
		List <ExperimentField> enrichmentExpFields = new ExperimentFieldDao().getActiveExperimentFields(selectedExperiment);
		ComboBox cbxExperimentField = new ComboBox("");
		cbxExperimentField.setStyleName("tiny");
		cbxExperimentField.setRequired(true);
		cbxExperimentField.setRequiredError("This field is required.");
		for(int i=0; i<enrichmentExpFields.size(); i++)
		{
			cbxExperimentField.addItem(enrichmentExpFields.get(i).getExpFieldId());
			cbxExperimentField.setItemCaption(enrichmentExpFields.get(i).getExpFieldId(), enrichmentExpFields.get(i).getExpFieldName() + " [ " + expFields.get(i).getExpFieldType() + " ]");
			cbxExperimentField.setWidth(100, Unit.PERCENTAGE);
		}
		cbxExperimentField.setHeight(20, Unit.PIXELS);
		itemValues[8] = cbxExperimentField;
		
		if(itemId > 0)
		{
			CsvTemplateEnrichment csvTemplateEnrichment = new CsvTemplateEnrichmentDao().getCsvTemplateEnrichmentById(itemId);
			cbxCsvColumnSource.setValue(csvTemplateEnrichment.getCsvTemplateEnrichmentColumnNameSource());
			cbxOperator.setValue(csvTemplateEnrichment.getCsvTemplateEnrichmentOperation());
			txtComparisonValue.setValue(csvTemplateEnrichment.getCsvTemplateEnrichmentValue());
			cbxEnrichmentType.setValue(csvTemplateEnrichment.getCsvTemplateEnrichmentType());
			
			if(csvTemplateEnrichment.getCustomListValue() != null)
			{	
				cbxCustomList.setValue(csvTemplateEnrichment.getCustomListValue().getCustomList().getCustomListId());
				List<CustomListValue> customListValues = new CustomListValueDao().getAllCustomListValuesByCustomList(new CustomListDao().getCustomListById(csvTemplateEnrichment.getCustomListValue().getCustomList().getCustomListId()));
				for(int i=0; i<customListValues.size(); i++)
				{
					cbxCustomListValue.addItem(customListValues.get(i).getCustomListValueId());
					cbxCustomListValue.setItemCaption(customListValues.get(i).getCustomListValueId(), customListValues.get(i).getCustomListValueString());
				}
				cbxCustomListValue.setValue(csvTemplateEnrichment.getCustomListValue().getCustomListValueId());
			}
			txtStaticValue.setValue(csvTemplateEnrichment.getCsvTemplateEnrichmentStaticValue());
			cbxExperimentField.setValue(csvTemplateEnrichment.getExpFieldDestination().getExpFieldId());
			
			if("customlist".equals(cbxEnrichmentType.getValue()))
			{
				cbxCustomList.setEnabled(true);
				cbxCustomListValue.setEnabled(true);
				txtStaticValue.setEnabled(false);
			}
			if("staticvalue".equals(cbxEnrichmentType.getValue()))
			{
				cbxCustomList.setEnabled(false);
				cbxCustomListValue.setEnabled(false);
				txtStaticValue.setEnabled(true);
			}
			if(!"customlist".equals(cbxEnrichmentType.getValue()) && !"staticvalue".equals(cbxEnrichmentType.getValue()))
			{
				cbxCustomList.setEnabled(false);
				cbxCustomListValue.setEnabled(false);
				txtStaticValue.setEnabled(false);
			}
			
		}
		

		cbxEnrichmentType.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeEnrichmentType(cbxEnrichmentType);
            }
        });

		cbxCustomList.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	onChangeCustomList(cbxCustomList);
            }
        });
		
		
		this.tblEnrichmentRules.addItem(itemValues, itemId);
		
	}
	
	private void deleteTblEnrichmentRule()
	{
		if(this.tblEnrichmentRules.getValue() != null)
		{
			if((int)this.tblEnrichmentRules.getValue() > 0)
			{
				//Validate that Custom List Value is not linked to another dependencies
				this.enrichmentRuleItemIdToDelete.add((int)this.tblEnrichmentRules.getValue());
			}
			
			this.tblEnrichmentRules.removeItem((int)this.tblEnrichmentRules.getValue());
		}
	}
	
	private void onChangeCustomList(ComboBox cbxCustomList)
	{
		Item tblEnrichmentRulesItem = this.tblEnrichmentRules.getItem(Integer.parseInt(cbxCustomList.getId()));
		
		ComboBox cbxCustomListValue = (ComboBox)tblEnrichmentRulesItem.getItemProperty("List Value").getValue();
		cbxCustomListValue.setContainerDataSource(null);
		
		if(cbxCustomList.getValue() != null && !cbxCustomList.getValue().toString().isEmpty())
		{
			Integer selectedCustomListId = (Integer)cbxCustomList.getValue();
			
			List<CustomListValue> customListValues = new CustomListValueDao().getAllCustomListValuesByCustomList(new CustomListDao().getCustomListById(selectedCustomListId));
			for(int i=0; i<customListValues.size(); i++)
			{
				cbxCustomListValue.addItem(customListValues.get(i).getCustomListValueId());
				cbxCustomListValue.setItemCaption(customListValues.get(i).getCustomListValueId(), customListValues.get(i).getCustomListValueString());
			}
		}
	}
	
	private void onChangeEnrichmentType(ComboBox cbxEnrichmentType)
	{
		Item tblEnrichmentRulesItem = this.tblEnrichmentRules.getItem(Integer.parseInt(cbxEnrichmentType.getId()));
		
		if(cbxEnrichmentType.getValue() != null && !cbxEnrichmentType.getValue().toString().isEmpty())
		{
			ComboBox cbxCustomList = (ComboBox)tblEnrichmentRulesItem.getItemProperty("Custom List").getValue();
			ComboBox cbxCustomListValue = (ComboBox)tblEnrichmentRulesItem.getItemProperty("List Value").getValue();
			TextField txtStaticValue = (TextField)tblEnrichmentRulesItem.getItemProperty("Static Value").getValue();
			
			if("customlist".equals(cbxEnrichmentType.getValue()))
			{
				cbxCustomList.setEnabled(true);
				cbxCustomListValue.setEnabled(true);
				txtStaticValue.setValue("");
				txtStaticValue.setEnabled(false);
			}
			if("staticvalue".equals(cbxEnrichmentType.getValue()))
			{
				cbxCustomList.setValue(null);
				cbxCustomList.setEnabled(false);
				cbxCustomListValue.setValue(null);
				cbxCustomListValue.setEnabled(false);
				txtStaticValue.setEnabled(true);
			}
			if(!"customlist".equals(cbxEnrichmentType.getValue()) && !"staticvalue".equals(cbxEnrichmentType.getValue()))
			{
				cbxCustomList.setValue(null);
				cbxCustomList.setEnabled(false);
				cbxCustomListValue.setValue(null);
				cbxCustomListValue.setEnabled(false);
				txtStaticValue.setValue("");
				txtStaticValue.setEnabled(false);
			}
			
		}
	}

	private void onChangeExperimentField(ComboBox cbxExpFields)
	{
		int tblRowItemId = Integer.parseInt(cbxExpFields.getId());
		
		Item tblRowItem = this.tblCsvCols.getContainerDataSource().getItem(tblRowItemId);
		
		if(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())) != null && !((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())) .isEmpty())
		{
			int experimentFieldId = (int)((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue();
			ExperimentField selectedExpField = new ExperimentFieldDao().getExperimentFieldById(experimentFieldId);
			
			if("datetime".equals(selectedExpField.getExpFieldType()))
			{
				((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).setValue("yyyy-MM-dd HH:mm:ss");
				((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).setEnabled(true);
			}
			if("date".equals(selectedExpField.getExpFieldType()))
			{
				((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).setValue("yyyy-MM-dd");
				((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).setEnabled(true);
			}
			if(!selectedExpField.getExpFieldType().contains("date"))
			{
				((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).setEnabled(false);
				((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).setValue("");					
			}			
		}
		else
		{
			((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).setEnabled(false);
			((TextField)(tblRowItem.getItemProperty("Datetime Format").getValue())).setValue("");		
		}
	}

	private void onChangeDateTimeMask(TextField txtDateTimeMask)
	{
		int tblRowItemId = Integer.parseInt(txtDateTimeMask.getId());
		
		Item tblRowItem = this.tblCsvCols.getContainerDataSource().getItem(tblRowItemId);
		
		if(((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())) != null)
		{
			int experimentFieldId = (int)((ComboBox)(tblRowItem.getItemProperty(this.systemSettings.getExperimentLabel() + " Field").getValue())).getValue();
			ExperimentField selectedExpField = new ExperimentFieldDao().getExperimentFieldById(experimentFieldId);
			
			if(selectedExpField.getExpFieldType().contains("date"))
			{
				try
				{
					DateFormat dateFormat = new SimpleDateFormat(txtDateTimeMask.getValue());	
					String dateStr = dateFormat.format(new Date());
					Date dateConverted = new SimpleDateFormat(txtDateTimeMask.getValue()).parse(dateStr);
					System.out.println("Date converted: " + dateConverted);					
				}
				catch(Exception e)
				{
					this.getUI().showNotification("Invalid Format: " + txtDateTimeMask.getValue(), Type.WARNING_MESSAGE);
					txtDateTimeMask.focus();
					txtDateTimeMask.selectAll();
				}
			}
		}
	}
}