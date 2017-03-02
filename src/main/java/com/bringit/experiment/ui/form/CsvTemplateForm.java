package com.bringit.experiment.ui.form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.bll.CsvTemplateColumns;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FilesRepositoryDao;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.CsvTemplateColumnsDao;
import com.bringit.experiment.dao.CsvTemplateDao;
import com.bringit.experiment.ui.design.CsvTemplateDesign;
import com.opencsv.CSVReader;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;

import com.vaadin.ui.TextField;

import com.vaadin.ui.Upload;

import com.vaadin.ui.Upload.Receiver;

import com.vaadin.ui.Window;

public class CsvTemplateForm extends CsvTemplateDesign {

	private CsvTemplate csvt;
	private List<CsvTemplateColumns> csvNodes;
	private List<FilesRepository> repos =  new FilesRepositoryDao().getAllFilesRepositorys();
	private List<JobExecutionRepeat> jobs = new JobExecutionRepeatDao().getAllJobExecutionRepeats();
	private List<Experiment> experiments = new ExperimentDao().getActiveExperiments();
	private int lastNewItemId = 0;
    private File tempFile;
    private List<ExperimentField> expFields;
	private List<Integer> dbIdOfCsvTemplateNodeItemsToDelete = new ArrayList<Integer>();
	
	public CsvTemplateForm(int csvId)
	{
		this.tblCsvNodes.setContainerDataSource(null);
		this.tblCsvNodes.addContainerProperty("*", CheckBox.class, null);
		this.tblCsvNodes.addContainerProperty("Csv Node Name", TextField.class, null);
		this.tblCsvNodes.addContainerProperty("Experiment Field", ComboBox.class, null);		
		this.tblCsvNodes.setPageLength(0);
		fillCombos();
		uploadFile();
		
		if(csvId == -1) //New
		{
			this.btnDelete.setEnabled(false);
			this.csvt = new CsvTemplate();
			this.chxActive.setValue(true);
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
			if(csvt.getJobExecRepeat() != null) this.comboCsvjobScheduler.setValue(csvt.getJobExecRepeat().getJobExecRepeatId());
			if(csvt.getInboundFileRepo() != null) this.comboCsvTinRepo.setValue(csvt.getInboundFileRepo().getFileRepoId());
			if(csvt.getProcessedFileRepo() != null) this.comboCsvoutRepo.setValue(csvt.getProcessedFileRepo().getFileRepoId());
			if(csvt.getExceptionFileRepo() != null) this.comboCsvTerrRepo.setValue(csvt.getExceptionFileRepo().getFileRepoId());
			this.txtCsvTComments.setValue(csvt.getCsvTemplateComments());
			this.txtCsvTPrefix.setValue(csvt.getCsvTemplatePrefix());
			
			this.csvNodes = new CsvTemplateColumnsDao().getAllCsvTemplateColumnssByTemplateId(csvt.getCsvTemplateId());
			this.expFields = new ExperimentFieldDao().getActiveExperimentFields(csvt.getExperiment());
			
			Object[] itemValues = new Object[3];
			for(int i=0; i<this.csvNodes.size(); i++)
			{		
				
				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				itemValues[0] = chxSelect;
				
				TextField txtCsvColName = new TextField();
				txtCsvColName.setValue(this.csvNodes.get(i).getCsvTemplateColumnName());
				txtCsvColName.setReadOnly(true);
				txtCsvColName.addStyleName("tiny");
				txtCsvColName.setWidth(100, Unit.PERCENTAGE);
				itemValues[1] = txtCsvColName;

				ComboBox cbxExpFields = new ComboBox("");
				for(int j=0; j<expFields.size(); j++)
				{
					cbxExpFields.addItem(expFields.get(j).getExpFieldId());
					cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName());
					cbxExpFields.setWidth(100, Unit.PERCENTAGE);
				}
				
				cbxExpFields.setNullSelectionAllowed(false);
				cbxExpFields.addStyleName("tiny");
				if(this.csvNodes.get(i).getExpField() != null)
					cbxExpFields.setValue(this.csvNodes.get(i).getExpField().getExpFieldId());
				itemValues[2] = cbxExpFields;
				
				this.tblCsvNodes.addItem(itemValues, this.csvNodes.get(i).getCsvTemplateColumnId());
			}
	
		}
		comboCsvTExperiment.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(comboCsvTExperiment.getValue()!=null){
					enableComponents(true);
				}
				
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
		
	}
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
	@SuppressWarnings("deprecation")
	protected void onSave() {
		Collection itemIds = this.tblCsvNodes.getContainerDataSource().getItemIds();
		boolean validateReqFieldsResult = validateRequiredFields();		
		
		//---Validate Required Fields---//
		if(itemIds.size() > 0 && validateReqFieldsResult)
		{
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			
			//Save template
			this.csvt.setExperiment(new ExperimentDao().getExperimentById((int) this.comboCsvTExperiment.getValue()));
			this.csvt.setCsvTemplateName(this.txtCsvTName.getValue());
			this.csvt.setCsvTemplatePrefix(this.txtCsvTPrefix.getValue());
			this.csvt.setCsvTemplateComments(this.txtCsvTComments.getValue());
			this.csvt.setExceptionFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboCsvTerrRepo.getValue()));
			this.csvt.setProcessedFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboCsvoutRepo.getValue()));
			this.csvt.setInboundFileRepo(new FilesRepositoryDao().getFilesRepositoryById((int) this.comboCsvTinRepo.getValue()));
			this.csvt.setJobExecRepeat(new JobExecutionRepeatDao().getJobExecutionRepeatById((int) this.comboCsvjobScheduler.getValue()));
			this.csvt.setLastModifiedBy(sessionUser);
			this.csvt.setModifiedDate(new Date());
			if(this.csvt.getCsvTemplateId() != null )
				new CsvTemplateDao().updateCsvTemplate(csvt);
			else
			{
				this.csvt.setCreatedBy(sessionUser);
				this.csvt.setCreatedDate(this.csvt.getModifiedDate());
				new CsvTemplateDao().addCsvTemplate(csvt);
			}
			
			
			//Save CsvTemplateNodes

			CsvTemplateColumnsDao csvTempNodeDao = new CsvTemplateColumnsDao();
			
			//Remove deprecated CsvTemplateNodes
			if(this.csvt.getCsvTemplateId() != null && dbIdOfCsvTemplateNodeItemsToDelete.size() > 0)
			{
				for(int i=0; i<dbIdOfCsvTemplateNodeItemsToDelete.size(); i++)
					csvTempNodeDao.deleteCsvTemplateColumns(dbIdOfCsvTemplateNodeItemsToDelete.get(i));
			}
			
			for (Object itemIdObj : itemIds) 
			{
				
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblCsvNodes.getContainerDataSource().getItem(itemId);
				
				CsvTemplateColumns csvNode = new CsvTemplateColumns();
				csvNode.setCsvTemplateColumnName(((TextField)(tblRowItem.getItemProperty("Csv Node Name").getValue())).getValue());			
				csvNode.setCsvTemplate(csvt);
				
				if(((ComboBox)(tblRowItem.getItemProperty("Experiment Field").getValue())).getValue() != null){
				
					ExperimentField selectedExpField = new ExperimentField();
					selectedExpField.setExpFieldId((int)((ComboBox)(tblRowItem.getItemProperty("Experiment Field").getValue())).getValue());

					csvNode.setExpField(selectedExpField);
				}
				if(itemId > 0)
				{
					csvNode.setCsvTemplateColumnId(itemId);
					
					csvTempNodeDao.updateCsvTemplateColumns(csvNode);
				}
				else
					csvTempNodeDao.addCsvTemplateColumns(csvNode);
				
			}
			
			
		
			closeModalWindow();
		}
		else
		{
			if(itemIds.size() <= 0)
				this.getUI().showNotification("There are no csv nodes mapped on your CsvTemplate", Type.WARNING_MESSAGE);
			else if(!validateReqFieldsResult)
				this.getUI().showNotification("Please fill in all required Fields", Type.WARNING_MESSAGE);
		}
	}
	
	private boolean validateRequiredFields()
	{
		if(!this.txtCsvTName.isValid()) return false;
		if(!this.txtCsvTPrefix.isValid()) return false;
		if(!((ComboBox)(this.comboCsvoutRepo)).isValid()) return false;
		if(!((ComboBox)(this.comboCsvTerrRepo)).isValid()) return false;
		if(!((ComboBox)(this.comboCsvTinRepo)).isValid()) return false;
		if(!((ComboBox)(this.comboCsvTExperiment)).isValid()) return false;
		
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
	    			if(reader != null)
	    			{
		            	fillNodes(reader.readNext());
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
		this.txtCsvTPrefix.setEnabled(b);
		this.comboCsvoutRepo.setEnabled(b);
		this.comboCsvTerrRepo.setEnabled(b);
		this.comboCsvTinRepo.setEnabled(b);
		this.tblCsvNodes.setEnabled(b);
		this.upCsv.setEnabled(b);
		this.comboCsvjobScheduler.setEnabled(b);
	}
	
	private void fillNodes(String[] columns) {
		
		if(this.csvt.getCsvTemplateId() != null && this.csvt.getCsvTemplateId() > 0)
		{
			//Remove deprecated Nodes from UI and store Ids to delete from DB at saving
			int loopNodeCnt = 0;
			Collection itemIds = this.tblCsvNodes.getContainerDataSource().getItemIds();
			
			for (Object itemIdObj : itemIds) 
			{	
				int itemId = (int)itemIdObj;
				if(itemId > 0)
					dbIdOfCsvTemplateNodeItemsToDelete.add(itemId);
			}			
			
			this.tblCsvNodes.setContainerDataSource(null);
			this.tblCsvNodes.addContainerProperty("*", CheckBox.class, null);
			this.tblCsvNodes.addContainerProperty("Csv Node Name", TextField.class, null);
			this.tblCsvNodes.addContainerProperty("Experiment Field", ComboBox.class, null);		
			this.tblCsvNodes.setPageLength(0);
			
		}
		
		ExperimentDao expdao = new ExperimentDao();
		Experiment expNew = expdao.getExperimentById((int)(this.comboCsvTExperiment.getValue()));
		expFields = new ExperimentFieldDao().getActiveExperimentFields(expNew);
	
		Object[] itemValues = new Object[3];
		for(int i=0; i<columns.length; i++)
		{
			CheckBox chxSelect = new CheckBox();
			chxSelect.setVisible(false);
			itemValues[0] = chxSelect;
			
			TextField txtCsvColName = new TextField();
			txtCsvColName.setValue(columns[i]);
			txtCsvColName.setReadOnly(true);
			txtCsvColName.addStyleName("tiny");
			txtCsvColName.setWidth(100, Unit.PERCENTAGE);
			itemValues[1] = txtCsvColName;
			

			ComboBox cbxExpFields = new ComboBox("");
			for(int j=0; j<expFields.size(); j++)
			{
				cbxExpFields.addItem(expFields.get(j).getExpFieldId());
				cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName());
				cbxExpFields.setWidth(100, Unit.PERCENTAGE);
			}
			
			cbxExpFields.setNullSelectionAllowed(false);
			cbxExpFields.addStyleName("tiny");
			itemValues[2] = cbxExpFields;
			
			this.lastNewItemId = this.lastNewItemId - 1;
			this.tblCsvNodes.addItem(itemValues, this.lastNewItemId);
			
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
		this.comboCsvTExperiment.addStyleName("small");
		
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
		this.comboCsvTinRepo.addStyleName("small");
		
		this.comboCsvoutRepo.setNullSelectionAllowed(true);
		this.comboCsvoutRepo.setImmediate(true);
		this.comboCsvoutRepo.addStyleName("small");
		
		this.comboCsvTerrRepo.setNullSelectionAllowed(true);
		this.comboCsvTerrRepo.setImmediate(true);
		this.comboCsvTerrRepo.addStyleName("small");
		
		//Jobs
		for(int j=0; j<jobs.size(); j++)
		{
			this.comboCsvjobScheduler.addItem(jobs.get(j).getJobExecRepeatId());
			this.comboCsvjobScheduler.setItemCaption(jobs.get(j).getJobExecRepeatId(), jobs.get(j).getJobExecRepeatName());
		}
		
		this.comboCsvjobScheduler.setNullSelectionAllowed(true);
		this.comboCsvjobScheduler.setImmediate(true);
		this.comboCsvjobScheduler.addStyleName("small");
		
	}
	private void onDelete()
	{	 
		this.csvt.setCsvTemplateIsActive(false);
		SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		this.csvt.setLastModifiedBy(sessionUser);
		this.csvt.setModifiedDate(new Date());
		CsvTemplateDao csvDao = new CsvTemplateDao();
		csvDao.updateCsvTemplate(csvt);
		closeModalWindow();
    }
	
}
