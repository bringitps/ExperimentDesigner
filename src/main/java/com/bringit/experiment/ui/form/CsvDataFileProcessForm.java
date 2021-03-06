package com.bringit.experiment.ui.form;

import com.bringit.experiment.bll.CmForSysRole;
import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.CsvDataLoadExecutionResult;
import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.bll.DataFile;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.BatchExperimentRecordsInsertDao;
import com.bringit.experiment.dao.CmForSysRoleDao;
import com.bringit.experiment.dao.CsvDataLoadExecutionResultDao;
import com.bringit.experiment.dao.CsvTemplateDao;
import com.bringit.experiment.dao.DataFileDao;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.data.ExperimentParser;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.ui.design.CsvDataFileProcessDesign;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.FTPUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Window;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CsvDataFileProcessForm extends CsvDataFileProcessDesign {

	private File tempFile;
	private InputStream isFile;
    private List<ExperimentField> expFields;
	private Document csvDocument = null;
	private List<CsvTemplate> csvTemplates ;
	private String loadedFileName = null;
	private CsvTemplate csvTemplate = new CsvTemplate();
	SysRole sysRoleSession = (SysRole)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");


	private void loadXmlTemplates () {
		csvTemplates = new ArrayList<>();

		if (!"sys_admin".equalsIgnoreCase(sysRoleSession.getRoleName())) {
			List<CmForSysRole> cmForSysRole =  new CmForSysRoleDao().getListOfCmForSysRoleBysysRoleId(sysRoleSession.getRoleId());
			List<ContractManufacturer> contractManufacturerList = new ArrayList<>();
			cmForSysRole.forEach(x-> contractManufacturerList.add(x.getContractManufacturer()));
			csvTemplates = new CsvTemplateDao().getAllCsvTemplatesByContractManager(contractManufacturerList);
			System.out.println("xmlTemplates = " + csvTemplates);
		} else {
			csvTemplates = new CsvTemplateDao().getAllActiveCsvTemplates();
		}
	}

	public CsvDataFileProcessForm()
	{
		loadXmlTemplates ();
		uploadFile();
		fillCombos();
		btnRun.setEnabled(false);
		
		cbxCsvTemplate.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) 
			{
				if(cbxCsvTemplate.getValue()!=null && loadedFileName!=null)
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

	        FilesRepository processedRepo = csvTemplate.getProcessedFileRepo();
	        FilesRepository exceptionRepo = csvTemplate.getExceptionFileRepo();
	        FilesRepository archivingRepo = csvTemplate.getArchivingFileRepo();
			
			ResponseObj parseCsvResponse = new ExperimentParser().parseCSV(tempFile, csvTemplate);
			try {
				isFile = new FileInputStream(tempFile);	
                	
                //Move file to Archive Repository
                if(archivingRepo != null)
                {
                	moveFileToRepo(archivingRepo, new FileInputStream(tempFile), loadedFileName);
    				System.out.println("File moved to archiving folder " + loadedFileName);    				
                }
                
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				isFile = null;
			}
			
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			DataFile dataFile = new DataFile();
			dataFile.setCreatedBy(sessionUser);
			dataFile.setDataFileIsCsv(true);
			dataFile.setDataFileName(loadedFileName);
			dataFile.setLastModifiedBy(sessionUser);
			dataFile.setFileRepoId(processedRepo);
			new DataFileDao().addDataFile(dataFile);
			
			DataFile dataFileProcessed = new DataFile();
			DataFile dataFileException = new DataFile();
			
			System.out.println("Parse CSV Response" + parseCsvResponse.getCode() );
			if(parseCsvResponse.getCode() == 0)
			{
				this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 1 of 3. Result (OK)\n");
				this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 2 of 3. Saving information into Data Warehouse\n");
				
				//Execute Batch Insert
				
				Config configuration = new Config();
				int insertBatchSize = Integer.parseInt(configuration.getProperty("batchinsert"));
				ResponseObj batchInsertResponse = parseCsvResponse;
				
				if(parseCsvResponse.getCsvInsertValues() != null && parseCsvResponse.getCsvInsertValues().size() > 0)
				{
					batchInsertResponse = new BatchExperimentRecordsInsertDao().executeExperimentBatchRecordsInsert(parseCsvResponse.getCsvInsertColumns(),
						parseCsvResponse.getCsvInsertValues(), null, csvTemplate, sessionUser, dataFile, csvTemplate.getExperiment(), insertBatchSize);
				}
				else
					batchInsertResponse.setDescription("Some records could not be processed.");
				
				if(batchInsertResponse.getCode() == 0)
				{
					if(parseCsvResponse.getCsvRowException() != null && parseCsvResponse.getCsvRowException().size() > 0)
					{
						//Split files into 2 CSV files
						splitCsvExceptionFiles(csvTemplate.getProcessedFileRepo(), csvTemplate.getExceptionFileRepo(), isFile, loadedFileName, parseCsvResponse);
					
						dataFileProcessed.setCreatedBy(sessionUser);
						dataFileProcessed.setDataFileIsCsv(true);
						dataFileProcessed.setDataFileName("Partial - " + loadedFileName);
						dataFileProcessed.setLastModifiedBy(sessionUser);
						dataFileProcessed.setFileRepoId(csvTemplate.getProcessedFileRepo());
						new DataFileDao().addDataFile(dataFileProcessed);
												
						dataFileException.setCreatedBy(sessionUser);
						dataFileException.setDataFileIsCsv(true);
						dataFileException.setDataFileName("Failed - " + loadedFileName);
						dataFileException.setLastModifiedBy(sessionUser);
						dataFileException.setFileRepoId(csvTemplate.getExceptionFileRepo());
						new DataFileDao().addDataFile(dataFileException);

						batchInsertResponse.setDescription("Some records could not be processed.");
						
						System.out.println("Original file is split");
						
						try {
							
						isFile = new FileInputStream(tempFile);
						}
						 catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								isFile = null;
							}
					}
					
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 2 of 3. Result (OK)\n");
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Execution Details: " + batchInsertResponse.getDescription() + "\n");

					
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 3 of 3. Saving Log Execution Information & Moving File\n");
					
					Integer totalRecordsProcessed =  (parseCsvResponse.getCsvInsertValues() != null ? parseCsvResponse.getCsvInsertValues().size() : 0);
					Integer totalRecordsException =  (parseCsvResponse.getCsvRowException() != null ? parseCsvResponse.getCsvRowException().size() : 0);
					Integer fileTotalRecords = totalRecordsProcessed + totalRecordsException;
					
					
					CsvDataLoadExecutionResult csvDataLoadExecResult = new CsvDataLoadExecutionResult();
					csvDataLoadExecResult.setDataFile(dataFile);
					csvDataLoadExecResult.setCsvDataLoadExecException(false);
					csvDataLoadExecResult.setCsvDataLoadExecTime(new Date());
					csvDataLoadExecResult.setCsvDataLoadTotalRecords(fileTotalRecords);
					csvDataLoadExecResult.setCsvTemplate(csvTemplate);
					new CsvDataLoadExecutionResultDao().addCsvDataLoadExecutionResult(csvDataLoadExecResult);
					
					//Save File into Processed Folder
					if(isFile != null)
					{
						if(parseCsvResponse.getCsvRowException() != null && parseCsvResponse.getCsvRowException().size() > 0)
						{
							csvDataLoadExecResult.setCsvDataLoadExecException(true);
							csvDataLoadExecResult.setCsvDataLoadExecExeptionDetails("Some records could not be processed.");
							
							csvDataLoadExecResult.setCsvDataLoadTotalRecords(fileTotalRecords);
							csvDataLoadExecResult.setCsvDataLoadTotalRecordsException(totalRecordsException);
							csvDataLoadExecResult.setCsvDataLoadTotalRecordsProcessed(totalRecordsProcessed);

							csvDataLoadExecResult.setDataFileProcessed(dataFileProcessed);
							csvDataLoadExecResult.setDataFileException(dataFileException);

							moveFileToRepo(csvTemplate.getExceptionFileRepo(), isFile, loadedFileName);
						}
						else
						{
							csvDataLoadExecResult.setCsvDataLoadTotalRecordsProcessed(parseCsvResponse.getCsvInsertValues().size());
							csvDataLoadExecResult.setCsvDataLoadTotalRecordsException(0);
							csvDataLoadExecResult.setDataFileProcessed(dataFile);	
							moveFileToRepo(processedRepo, isFile, loadedFileName);						
						}
						
						new CsvDataLoadExecutionResultDao().updateCsvDataLoadExecutionResult(csvDataLoadExecResult);
												
                        //Getting data refreshed into replication tables
						Integer experimentId = csvTemplate.getExperiment().getExpId();
				        List<String> spExpParams = new ArrayList<String>();
				        spExpParams.add(experimentId.toString());
				        new ExecuteQueryDao().executeStoredProcedure("spExpData", spExpParams);

    			        List<TargetReport> targetReportsForExperiment = new TargetReportDao().getAllActiveTargetReportsByExperimentId(experimentId);
				        for(int i=0; targetReportsForExperiment != null && i<targetReportsForExperiment.size(); i++)
				        {
				        	Integer targetReportId = targetReportsForExperiment.get(i).getTargetReportId();
				        	List<String> spTargetRptParams = new ArrayList<String>();
				        	spTargetRptParams.add(targetReportId.toString());
				        	new ExecuteQueryDao().executeStoredProcedure("spTargetReportBuilder", spTargetRptParams);
				        }
				        
				        System.out.println("Original file is moved");
					}
				}	
				else
				{
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 2 of 3. Result (Error)\n");
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Error Details: " + batchInsertResponse.getDescription() + "\n");
				
					this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 3 of 3. Saving Log Execution Information & Moving File\n");


					CsvDataLoadExecutionResult csvDataLoadExecResult = new CsvDataLoadExecutionResult();
					csvDataLoadExecResult.setDataFile(dataFile);
					csvDataLoadExecResult.setDataFileException(dataFile);
					csvDataLoadExecResult.setCsvDataLoadExecException(true);
					csvDataLoadExecResult.setCsvDataLoadExecExeptionDetails(batchInsertResponse.getDescription());
					csvDataLoadExecResult.setCsvDataLoadExecTime(new Date());
					csvDataLoadExecResult.setCsvDataLoadTotalRecords(0);
					csvDataLoadExecResult.setCsvTemplate(csvTemplate);
					new CsvDataLoadExecutionResultDao().addCsvDataLoadExecutionResult(csvDataLoadExecResult);
					
					//Save File into Execptioned Folder (Rename)
					if(isFile != null)
					{
						moveFileToRepo(exceptionRepo, isFile, loadedFileName);
						dataFile.setFileRepoId(exceptionRepo);
						new DataFileDao().updateDataFile(dataFile);
					}
				}
			}
			else
			{
				this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Step 1 of 3. Result (Error)\n");
				this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "Error Details: " + parseCsvResponse.getDescription() + "\n");
				
				CsvDataLoadExecutionResult csvDataLoadExecResult = new CsvDataLoadExecutionResult();
				csvDataLoadExecResult.setDataFile(dataFile);
				csvDataLoadExecResult.setDataFileException(dataFile);
				csvDataLoadExecResult.setCsvDataLoadExecException(true);
				csvDataLoadExecResult.setCsvDataLoadExecExeptionDetails(parseCsvResponse.getDescription());
				csvDataLoadExecResult.setCsvDataLoadExecTime(new Date());
				csvDataLoadExecResult.setCsvDataLoadTotalRecords(0);
				csvDataLoadExecResult.setCsvTemplate(csvTemplate);
				
				new CsvDataLoadExecutionResultDao().addCsvDataLoadExecutionResult(csvDataLoadExecResult);
				
				//Save File into Execptioned Folder (Rename)
				if(isFile != null)
				{
					moveFileToRepo(exceptionRepo, isFile, loadedFileName);
					dataFile.setFileRepoId(exceptionRepo);
					new DataFileDao().updateDataFile(dataFile);
				}
			
			}

			tempFile.delete(); // delete the temp file 
			
			this.txtCsvDataFileLoadResults.setValue(this.txtCsvDataFileLoadResults.getValue() + "\n\n=== Process Finished ===\n");
			this.txtCsvDataFileLoadResults.setReadOnly(true);
			this.txtCsvDataFileLoadResults.setSelectionRange(this.txtCsvDataFileLoadResults.getValue().length()-1, 1);
			enableRunButton(false);
			
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
	
	private void moveFileToRepo(FilesRepository repo, InputStream is, String filename) {
        try {

            if (repo.isFileRepoIsSftp()) {
                FTPUtil sftp = new FTPUtil(repo.getFileRepoHost(), Integer.parseInt(repo.getFileRepoPort()),
                        repo.getFileRepoUser(), repo.getFileRepoPass());

                boolean didMove = sftp.secureSend(repo.getFileRepoPath(), is, filename);

            } else if (repo.isFileRepoIsFtp()) {
                FTPUtil ftp = new FTPUtil(repo.getFileRepoHost(), Integer.parseInt(repo.getFileRepoPort()),
                        repo.getFileRepoUser(), repo.getFileRepoPass());

                boolean didMove = ftp.simpleSendFile(repo.getFileRepoPath(), is, filename);

            } else if (repo.isFileRepoIsLocal()) {
                FileOutputStream fos = new FileOutputStream(repo.getFileRepoPath() + "/" + filename);
                IOUtils.copy(is, fos);
                is.close();
            }

        } catch (Exception ex) {
            System.out.println("Error moving file to repo: "+filename+". Details: " + ex);
        }

    }

	private void splitCsvExceptionFiles(FilesRepository processedRepo, FilesRepository exceptionRepo, InputStream isCsv, String filename, ResponseObj parseCsvResponse)
	{
		ByteArrayOutputStream outExceptionCsv = new ByteArrayOutputStream();
		ByteArrayOutputStream outPartialProcessedCsv = new ByteArrayOutputStream();
		
		CSVReader originalCsvReader = new CSVReader(new InputStreamReader(isCsv));
		CSVWriter exceptionCsvWriter = new CSVWriter(new OutputStreamWriter(outExceptionCsv), ',');
		CSVWriter partialProcessedCsvWriter = new CSVWriter(new OutputStreamWriter(outPartialProcessedCsv), ',');
		String[] originalCsvEntry = null;
		
		int rowCount = 0;
		try {
			while ((originalCsvEntry = originalCsvReader.readNext()) != null) {
			    String[] exceptionCsvEntry = new String[originalCsvEntry.length + 1];
				
				if(rowCount==0)
				{
					exceptionCsvEntry[0] = "Exception Details";

				    for(int i=0; i<originalCsvEntry.length; i++)
				    	exceptionCsvEntry[i+1] = originalCsvEntry[i];
				    
				    exceptionCsvWriter.writeNext(exceptionCsvEntry);
				    partialProcessedCsvWriter.writeNext(originalCsvEntry);
				}
				else
				{
					int rowExceptionIndex = parseCsvResponse.getCsvRowException().indexOf(rowCount);
					if(rowExceptionIndex == -1)
					    partialProcessedCsvWriter.writeNext(originalCsvEntry);
					else
					{
						exceptionCsvEntry[0] = parseCsvResponse.getCsvRowExceptionDetails().get(rowExceptionIndex);

					    for(int i=0; i<originalCsvEntry.length; i++)
					    	exceptionCsvEntry[i+1] = originalCsvEntry[i];
					    
					    exceptionCsvWriter.writeNext(exceptionCsvEntry);				
					}
					
				}
				rowCount++;
			}
			
			originalCsvReader.close();
			partialProcessedCsvWriter.close();
			exceptionCsvWriter.close();
			
			//Saving Partial Processed File
			moveFileToRepo(processedRepo, new ByteArrayInputStream(outPartialProcessedCsv.toByteArray()), "Partial - " + filename);
			moveFileToRepo(exceptionRepo, new ByteArrayInputStream(outExceptionCsv.toByteArray()), "Failed - " + filename);
			

			/*
			FileOutputStream fosExceptionCsv = new FileOutputStream(exceptionRepo.getFileRepoPath() + "/Failed - " + filename);
			IOUtils.copy(new ByteArrayInputStream(outExceptionCsv.toByteArray()), fosExceptionCsv);
			fosExceptionCsv.close();
			
			FileOutputStream fosPartialProcessedCsv = new FileOutputStream(processedRepo.getFileRepoPath() + "/Partial - " + filename);
			IOUtils.copy(new ByteArrayInputStream(outPartialProcessedCsv.toByteArray()), fosPartialProcessedCsv);
			fosPartialProcessedCsv.close();*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
