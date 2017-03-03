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
import com.bringit.experiment.bll.XmlDataLoadExecutionResult;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.dao.BatchExperimentRecordsInsertDao;
import com.bringit.experiment.dao.DataFileDao;
import com.bringit.experiment.dao.XmlDataLoadExecutionResultDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.data.ExperimentParser;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.ui.design.XmlDataFileProcessDesign;
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

public class XmlDataFileProcessForm extends XmlDataFileProcessDesign{

	private File tempFile;
    private List<ExperimentField> expFields;
	private Document xmlDocument = null;
	private List<XmlTemplate> xmlTemplates = new XmlTemplateDao().getAllActiveXmlTemplates();
	private String loadedFileName = null;
	private XmlTemplate xmlTemplate = new XmlTemplate();
	
	public XmlDataFileProcessForm()
	{
		uploadFile();
		fillCombos();
		btnRun.setEnabled(false);
		
		cbxXmlTemplate.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) 
			{
				if(cbxXmlTemplate.getValue()!=null && xmlDocument!=null)
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
			this.txtXmlDataFileLoadResults.setReadOnly(false);
		
			this.txtXmlDataFileLoadResults.setValue("===Starting Process===\n\n");

			this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Step 1 of 3. Validating & Parsing XML File.\n");
			xmlTemplate = new XmlTemplateDao().getXmlTemplateById((int)cbxXmlTemplate.getValue());
			ResponseObj parseXmlResponse = new ExperimentParser().parseXmlDocument(loadedFileName, xmlDocument, xmlTemplate);
			
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			DataFile dataFile = new DataFile();
			dataFile.setCreatedBy(sessionUser);
			dataFile.setDataFileIsXml(true);
			dataFile.setDataFileIsCsv(false);
			dataFile.setDataFileName(loadedFileName);
			dataFile.setLastModifiedBy(sessionUser);
			new DataFileDao().addDataFile(dataFile);
			
			if(parseXmlResponse.getCode() == 0)
			{
				this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Step 1 of 1. Result (OK)\n");
				this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Step 2 of 1. Saving information into Data Warehouse\n");
				
				//Execute Batch Insert
				
				Config configuration = new Config();
				int insertBatchSize = Integer.parseInt(configuration.getProperty("batchinsert"));
				
				ResponseObj batchInsertResponse = new BatchExperimentRecordsInsertDao().executeExperimentBatchRecordsInsert(parseXmlResponse.getCsvInsertColumns(),
						parseXmlResponse.getCsvInsertValues(), sessionUser, dataFile, xmlTemplate.getExperiment(), insertBatchSize);
				
				if(batchInsertResponse.getCode() == 0)
				{
					this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Step 2 of 3. Result (OK)\n");
					this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Execution Details: " + batchInsertResponse.getDescription() + "\n");

					
					this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Step 3 of 3. Saving Log Execution Information & Moving File\n");
					
					XmlDataLoadExecutionResult xmlDataLoadExecResult = new XmlDataLoadExecutionResult();
					xmlDataLoadExecResult.setDataFile(dataFile);
					xmlDataLoadExecResult.setXmlDataLoadExecException(false);
					xmlDataLoadExecResult.setXmlDataLoadExecTime(new Date());
					xmlDataLoadExecResult.setXmlTemplate(xmlTemplate);
					new XmlDataLoadExecutionResultDao().addXmlDataLoadExecutionResult(xmlDataLoadExecResult);
					
					//Save File into Processed Folder
					//Pending Matt's code
				}	
				else
				{
					this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Step 2 of 3. Result (Error)\n");
					this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Error Details: " + batchInsertResponse.getDescription() + "\n");
				
					this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Step 3 of 3. Saving Log Execution Information & Moving File\n");


					XmlDataLoadExecutionResult xmlDataLoadExecResult = new XmlDataLoadExecutionResult();
					xmlDataLoadExecResult.setDataFile(dataFile);
					xmlDataLoadExecResult.setXmlDataLoadExecException(true);
					xmlDataLoadExecResult.setXmlDataLoadExecExeptionDetails(batchInsertResponse.getDescription());
					xmlDataLoadExecResult.setXmlDataLoadExecTime(new Date());
					xmlDataLoadExecResult.setXmlTemplate(xmlTemplate);
					new XmlDataLoadExecutionResultDao().addXmlDataLoadExecutionResult(xmlDataLoadExecResult);
					
					//Save File into Execptioned Folder (Rename)
					//Pending Matt's code
				}
			}
			else
			{
				this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Step 1 of 3. Result (Error)\n");
				this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "Error Details: " + parseXmlResponse.getDescription() + "\n");
				
				XmlDataLoadExecutionResult xmlDataLoadExecResult = new XmlDataLoadExecutionResult();
				xmlDataLoadExecResult.setDataFile(dataFile);
				xmlDataLoadExecResult.setXmlDataLoadExecException(true);
				xmlDataLoadExecResult.setXmlDataLoadExecExeptionDetails(parseXmlResponse.getDescription());
				xmlDataLoadExecResult.setXmlDataLoadExecTime(new Date());
				xmlDataLoadExecResult.setXmlTemplate(xmlTemplate);
				new XmlDataLoadExecutionResultDao().addXmlDataLoadExecutionResult(xmlDataLoadExecResult);
				
				//Save File into Execptioned Folder (Rename)
				//Pending Matt's code
			
			}
			this.txtXmlDataFileLoadResults.setValue(this.txtXmlDataFileLoadResults.getValue() + "\n\n=== Process Finished ===\n");
			this.txtXmlDataFileLoadResults.setReadOnly(true);
			this.txtXmlDataFileLoadResults.setSelectionRange(this.txtXmlDataFileLoadResults.getValue().length()-1, 1);
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
                	tempFile = File.createTempFile("temp", ".xml");
                    fos = new FileOutputStream(tempFile);
                }  catch (IOException e) {
      	          e.printStackTrace();
    	          return null;
                }
                return fos; // Return the output stream to write to
            }
        };
        final MyReceiver uploader = new MyReceiver(); 
        upXmlDataFile.setReceiver(uploader);
        upXmlDataFile.addFinishedListener(new Upload.FinishedListener() {
	        @Override
	        public void uploadFinished(Upload.FinishedEvent finishedEvent) {
        	    FileReader reader;
				try {
				
					reader = new FileReader(tempFile);
				
	            
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	    			xmlDocument = docBuilder.parse(tempFile);
	    			if(xmlDocument != null)
	    			{
	    				if(loadedFileName != null && cbxXmlTemplate.getValue()!=null && xmlDocument!=null)
	    					enableRunButton(true);
	    				else
	    					enableRunButton(false);
	    				
			            reader.close();
	    			}
	    		
	    			tempFile.delete();
				} catch (ParserConfigurationException | SAXException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	        	
	        }
	      });
		
	}
	
	private void fillCombos() 
	{
		//XML Templates
		for(int i=0; i<xmlTemplates.size(); i++)
		{
			cbxXmlTemplate.addItem(xmlTemplates.get(i).getXmlTemplateId());
			cbxXmlTemplate.setItemCaption(xmlTemplates.get(i).getXmlTemplateId(), xmlTemplates.get(i).getXmlTemplateName());
		}
	}
}
