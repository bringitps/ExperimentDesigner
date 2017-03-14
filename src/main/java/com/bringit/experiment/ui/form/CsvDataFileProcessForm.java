package com.bringit.experiment.ui.form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.bringit.experiment.bll.DataFile;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.CsvDataLoadExecutionResult;
import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.dao.BatchExperimentRecordsInsertDao;
import com.bringit.experiment.dao.DataFileDao;
import com.bringit.experiment.dao.CsvDataLoadExecutionResultDao;
import com.bringit.experiment.dao.CsvTemplateDao;
import com.bringit.experiment.data.ExperimentParser;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.ui.design.CsvDataFileProcessDesign;
import com.bringit.experiment.util.Config;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Upload.Receiver;

public class CsvDataFileProcessForm extends CsvDataFileProcessDesign{

	private File tempFile;
    private List<ExperimentField> expFields;
	private Document csvDocument = null;
	private List<CsvTemplate> csvTemplates = new CsvTemplateDao().getAllActiveCsvTemplates();
	private String loadedFileName = null;
	private CsvTemplate csvTemplate = new CsvTemplate();
	
	public CsvDataFileProcessForm()
	{
		uploadFile();
		fillCombos();
		btnRun.setEnabled(false);
		
		cbxCsvTemplate.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) 
			{
				if(cbxCsvTemplate.getValue()!=null && csvDocument!=null)
					enableRunButton(true);
				else
					enableRunButton(false);
			}   
	    });

		btnRun.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onRun();
			}

		});
		
		btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				closeModalWindow();
			}

		});
				
	}

	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
	
	private void enableRunButton(boolean enabled)
	{
		btnRun.setEnabled(enabled);
	}
	
	private void setLoadedFile(String fileName)
	{
		if(new DataFileDao().getDataFileByName(fileName) == null)
		{
			lblLoadedFile.setValue("Loaded File: " + fileName);
			loadedFileName = fileName;
		}
		else
		{
			loadedFileName = null;
			this.getUI().showNotification("Selected Data File is already processed.", Type.WARNING_MESSAGE);		
		}
	}
	
	private void onRun()
	{
		if(loadedFileName != null)
		{
			this.txtCsvDataFileLoadResults.setReadOnly(false);
		
			this.txtCsvDataFileLoadResults.setValue("===Starting Process===\n\n");

			this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 1 of 3. Validating & Parsing CSV File.\n");
			csvTemplate = new CsvTemplateDao().getCsvTemplateById((int)cbxCsvTemplate.getValue());
			ResponseObj parseCsvResponse = new ExperimentParser().parseCSV(tempFile, csvTemplate);
			tempFile.delete(); // delete the temp file 
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			DataFile dataFile = new DataFile();
			dataFile.setCreatedBy(sessionUser);
			dataFile.setDataFileIsCsv(true);
			dataFile.setDataFileIsCsv(false);
			dataFile.setDataFileName(loadedFileName);
			dataFile.setLastModifiedBy(sessionUser);
			new DataFileDao().addDataFile(dataFile);
			
			if(parseCsvResponse.getCode() == 0)
			{
				this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 1 of 3. Result (OK)\n");
				this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 2 of 3. Saving information into Data Warehouse\n");
				
				//Execute Batch Insert
				
				Config configuration = new Config();
				int insertBatchSize = Integer.parseInt(configuration.getProperty("batchinsert"));
				
				ResponseObj batchInsertResponse = new BatchExperimentRecordsInsertDao().executeExperimentBatchRecordsInsert(parseCsvResponse.getCsvInsertColumns(),
						parseCsvResponse.getCsvInsertValues(), null, csvTemplate, sessionUser, dataFile, csvTemplate.getExperiment(), insertBatchSize);
				
				if(batchInsertResponse.getCode() == 0)
				{
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 2 of 3. Result (OK)\n");
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Execution Details: " + batchInsertResponse.getDescription() + "\n");

					
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 3 of 3. Saving Log Execution Information & Moving File\n");
					
					CsvDataLoadExecutionResult csvDataLoadExecResult = new CsvDataLoadExecutionResult();
					csvDataLoadExecResult.setDataFile(dataFile);
					csvDataLoadExecResult.setCsvDataLoadExecException(false);
					csvDataLoadExecResult.setCsvDataLoadExecTime(new Date());
					csvDataLoadExecResult.setCsvTemplate(csvTemplate);
					new CsvDataLoadExecutionResultDao().addCsvDataLoadExecutionResult(csvDataLoadExecResult);
					
					//Save File into Processed Folder
					//Pending Matt's code
				}	
				else
				{
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 2 of 3. Result (Error)\n");
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Error Details: " + batchInsertResponse.getDescription() + "\n");
				
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 3 of 3. Saving Log Execution Information & Moving File\n");


					CsvDataLoadExecutionResult csvDataLoadExecResult = new CsvDataLoadExecutionResult();
					csvDataLoadExecResult.setDataFile(dataFile);
					csvDataLoadExecResult.setCsvDataLoadExecException(true);
					csvDataLoadExecResult.setCsvDataLoadExecExeptionDetails(batchInsertResponse.getDescription());
					csvDataLoadExecResult.setCsvDataLoadExecTime(new Date());
					csvDataLoadExecResult.setCsvTemplate(csvTemplate);
					new CsvDataLoadExecutionResultDao().addCsvDataLoadExecutionResult(csvDataLoadExecResult);
					
					//Save File into Execptioned Folder (Rename)
					//Pending Matt's code
				}
			}
			else
			{
				this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 1 of 3. Result (Error)\n");
				this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Error Details: " + parseCsvResponse.getDescription() + "\n");
				
				CsvDataLoadExecutionResult csvDataLoadExecResult = new CsvDataLoadExecutionResult();
				csvDataLoadExecResult.setDataFile(dataFile);
				csvDataLoadExecResult.setCsvDataLoadExecException(true);
				csvDataLoadExecResult.setCsvDataLoadExecExeptionDetails(parseCsvResponse.getDescription());
				csvDataLoadExecResult.setCsvDataLoadExecTime(new Date());
				csvDataLoadExecResult.setCsvTemplate(csvTemplate);
				new CsvDataLoadExecutionResultDao().addCsvDataLoadExecutionResult(csvDataLoadExecResult);
				
				//Save File into Execptioned Folder (Rename)
				//Pending Matt's code
			
			}
			this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "\n\n=== Process Finished ===\n");
			this.txtCsvDataFileLoadResults.setReadOnly(true);
			this.txtCsvDataFileLoadResults.setSelectionRange(this.txtCsvDataFileLoadResults.getValue().length()-1, 1);
		}	
	}
	
	@SuppressWarnings("deprecation")
	private void uploadFile() {

        class MyReceiver implements Receiver {
            
            public OutputStream receiveUpload(String filename, String mimeType) {
            	setLoadedFile(filename);
            	
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
        upCsvDataFile.setReceiver(uploader);
        upCsvDataFile.addFinishedListener(new Upload.FinishedListener() {
	        @Override
	        public void uploadFinished(Upload.FinishedEvent finishedEvent) {
				try {
				

	    			if(tempFile != null)
	    			{
	    				if(loadedFileName != null && cbxCsvTemplate.getValue()!=null)
	    					enableRunButton(true);
	    				else
	    					enableRunButton(false);
	    				
			            
	    			}
	    			//tempFile.delete();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	        	
	        }
	      });
		
	}
	
	private void fillCombos() 
	{
		for(int i=0; i<csvTemplates.size(); i++)
		{
			cbxCsvTemplate.addItem(csvTemplates.get(i).getCsvTemplateId());
			cbxCsvTemplate.setItemCaption(csvTemplates.get(i).getCsvTemplateId(), csvTemplates.get(i).getCsvTemplateName());
		}
	}
}
