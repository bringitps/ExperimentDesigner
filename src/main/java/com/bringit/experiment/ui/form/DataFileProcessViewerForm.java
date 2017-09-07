package com.bringit.experiment.ui.form;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.bringit.experiment.bll.CsvDataLoadExecutionResult;
import com.bringit.experiment.bll.DataFile;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.XmlDataLoadExecutionResult;
import com.bringit.experiment.dao.CsvDataLoadExecutionResultDao;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.ui.design.DataFileProcessViewerDesign;
import com.bringit.experiment.util.FTPUtil;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

public class DataFileProcessViewerForm extends DataFileProcessViewerDesign{

	public DataFileProcessViewerForm(int csvDataLoadExecutionResultId, int xmlDataLoadExecutionResultId)
	{
		if(csvDataLoadExecutionResultId != -1)
		{
			//this.tblDataFileProcessViewer.setCaption("Csv Data File Load - Result Files");
			this.tblDataFileProcessViewer.setContainerDataSource(null);
			this.tblDataFileProcessViewer.addContainerProperty("Csv File", Label.class, null);
			this.tblDataFileProcessViewer.addContainerProperty("Type", Label.class, null);
			this.tblDataFileProcessViewer.addContainerProperty("Location", Label.class, null);		
			this.tblDataFileProcessViewer.addContainerProperty("Download", Button.class, null);
			this.tblDataFileProcessViewer.setPageLength(0);
			
			CsvDataLoadExecutionResult csvDataLoadExecResult = new CsvDataLoadExecutionResultDao().getCsvDataLoadExecutionResultById(csvDataLoadExecutionResultId);
			System.out.println("Data Load Execution: " + csvDataLoadExecResult.getCsvDataLoadTotalRecords());
			System.out.println("Data Load: " + csvDataLoadExecResult.getDataFile().getDataFileName());
			
			if(csvDataLoadExecResult != null)
			{
				Object[] itemValues = new Object[4];
				
				//Original File
				if(csvDataLoadExecResult.getDataFile() != null && csvDataLoadExecResult.getDataFile().getFileRepoId() != null)
				{
					Label lblCsvFileName = new Label();
					lblCsvFileName.setValue(csvDataLoadExecResult.getDataFile().getDataFileName());
					lblCsvFileName.setReadOnly(true);
					lblCsvFileName.addStyleName("tiny");
					lblCsvFileName.setWidth(100, Unit.PERCENTAGE);
					itemValues[0] = lblCsvFileName;
					
					Label lblCsvFileType = new Label();
					lblCsvFileType.setValue("Original");
					lblCsvFileType.setReadOnly(true);
					lblCsvFileType.addStyleName("tiny");
					lblCsvFileType.setWidth(100, Unit.PERCENTAGE);
					itemValues[1] = lblCsvFileType;
					
					Label lblCsvFileLocation = new Label();
					lblCsvFileLocation.setValue(csvDataLoadExecResult.getDataFile().getFileRepoId().getFileRepoHost() +  "/" + csvDataLoadExecResult.getDataFile().getFileRepoId().getFileRepoPath());
					lblCsvFileLocation.setReadOnly(true);
					lblCsvFileLocation.addStyleName("tiny");
					lblCsvFileLocation.setWidth(100, Unit.PERCENTAGE);
					itemValues[2] = lblCsvFileLocation;
					
					Button btnCsvFileDownload = new Button();
					btnCsvFileDownload.setCaption("Download");
					btnCsvFileDownload.setReadOnly(true);
					btnCsvFileDownload.addStyleName("link");
					btnCsvFileDownload.setWidth(100, Unit.PERCENTAGE);
					
					attachFileDownloaderToButton(btnCsvFileDownload, csvDataLoadExecResult.getDataFile());
					
					itemValues[3] = btnCsvFileDownload;
					
					this.tblDataFileProcessViewer.addItem(itemValues, csvDataLoadExecResult.getDataFile().getDataFileId());					
			
				}
				
				//Processed File
				if(csvDataLoadExecResult.getDataFileProcessed() != null && csvDataLoadExecResult.getDataFileProcessed().getFileRepoId() != null)
				{
					Label lblCsvFileName = new Label();
					lblCsvFileName.setValue(csvDataLoadExecResult.getDataFileProcessed().getDataFileName());
					lblCsvFileName.setReadOnly(true);
					lblCsvFileName.addStyleName("tiny");
					lblCsvFileName.setWidth(100, Unit.PERCENTAGE);
					itemValues[0] = lblCsvFileName;
					
					Label lblCsvFileType = new Label();
					lblCsvFileType.setValue("Processed");
					lblCsvFileType.setReadOnly(true);
					lblCsvFileType.addStyleName("tiny");
					lblCsvFileType.setWidth(100, Unit.PERCENTAGE);
					itemValues[1] = lblCsvFileType;
					
					
					Label lblCsvFileLocation = new Label();
					lblCsvFileLocation.setValue(csvDataLoadExecResult.getDataFileProcessed().getFileRepoId().getFileRepoHost() +  "/" + csvDataLoadExecResult.getDataFileProcessed().getFileRepoId().getFileRepoPath());
					lblCsvFileLocation.setReadOnly(true);
					lblCsvFileLocation.addStyleName("tiny");
					lblCsvFileLocation.setWidth(100, Unit.PERCENTAGE);
					itemValues[2] = lblCsvFileLocation;
					
					Button btnCsvFileDownload = new Button();
					btnCsvFileDownload.setCaption("Download");
					btnCsvFileDownload.setReadOnly(true);
					btnCsvFileDownload.addStyleName("link");
					btnCsvFileDownload.setWidth(100, Unit.PERCENTAGE);
					attachFileDownloaderToButton(btnCsvFileDownload, csvDataLoadExecResult.getDataFileProcessed());
					
					itemValues[3] = btnCsvFileDownload;
				
					this.tblDataFileProcessViewer.addItem(itemValues, csvDataLoadExecResult.getDataFileProcessed().getDataFileId());					
			
				}

				//Exception File
				if(csvDataLoadExecResult.getDataFileException() != null && csvDataLoadExecResult.getDataFileException().getFileRepoId() != null)
				{
					Label lblCsvFileName = new Label();
					lblCsvFileName.setValue(csvDataLoadExecResult.getDataFileException().getDataFileName());
					lblCsvFileName.setReadOnly(true);
					lblCsvFileName.addStyleName("tiny");
					lblCsvFileName.setWidth(100, Unit.PERCENTAGE);
					itemValues[0] = lblCsvFileName;
					
					Label lblCsvFileType = new Label();
					lblCsvFileType.setValue("Exception");
					lblCsvFileType.setReadOnly(true);
					lblCsvFileType.addStyleName("tiny");
					lblCsvFileType.setWidth(100, Unit.PERCENTAGE);
					itemValues[1] = lblCsvFileType;
					
					Label lblCsvFileLocation = new Label();
					lblCsvFileLocation.setValue(csvDataLoadExecResult.getDataFileException().getFileRepoId().getFileRepoHost() +  "/" + csvDataLoadExecResult.getDataFileException().getFileRepoId().getFileRepoPath());
					lblCsvFileLocation.setReadOnly(true);
					lblCsvFileLocation.addStyleName("tiny");
					lblCsvFileLocation.setWidth(100, Unit.PERCENTAGE);
					itemValues[2] = lblCsvFileLocation;
					
					Button btnCsvFileDownload = new Button();
					btnCsvFileDownload.setCaption("Download");
					btnCsvFileDownload.setReadOnly(true);
					btnCsvFileDownload.addStyleName("link");
					btnCsvFileDownload.setWidth(100, Unit.PERCENTAGE);
					attachFileDownloaderToButton(btnCsvFileDownload, csvDataLoadExecResult.getDataFileException());
					
					itemValues[3] = btnCsvFileDownload;
					
					this.tblDataFileProcessViewer.addItem(itemValues, csvDataLoadExecResult.getDataFileException().getDataFileId());					
				}
			}
			
		}
		
		if(xmlDataLoadExecutionResultId != -1)
		{
			//this.tblDataFileProcessViewer.setCaption("Xml Data File Load - Result Files");
			
		}
	}
	
	public void attachFileDownloaderToButton(Button downloadBtn, DataFile dataFile)
	{
		
		FilesRepository repo = dataFile.getFileRepoId();
		String filename = dataFile.getDataFileName();
		
	

            if (repo.isFileRepoIsSftp()) {
                FTPUtil sftp = new FTPUtil(repo.getFileRepoHost(), Integer.parseInt(repo.getFileRepoPort()),
                        repo.getFileRepoUser(), repo.getFileRepoPass());
               
                FileDownloader downloader = new FileDownloader(new StreamResource(
                        new StreamResource.StreamSource() {
                            @Override
                            public InputStream getStream() {
                            	 InputStream is = null;
                            	 try
                            	 {
                            		 is=sftp.secureGetFile(repo.getFileRepoPath() + "/" + filename);
                            	 }
                            	 catch(Exception e)
                            	 {
                            		System.out.println("Error downloading file: "+filename + " \nDetails:" + e.getMessage());
    							    getUI().showNotification("Error downloading file: "+filename + " <br>Details:" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
    							 }
                            	 
                                 return is;
                            }
                        }, filename));
                downloader.extend(downloadBtn);
                
                
            } else if (repo.isFileRepoIsFtp()) {
                FTPUtil ftp = new FTPUtil(repo.getFileRepoHost(), Integer.parseInt(repo.getFileRepoPort()),
                        repo.getFileRepoUser(), repo.getFileRepoPass());

                
                FileDownloader downloader = new FileDownloader(new StreamResource(
                        new StreamResource.StreamSource() {
                            @Override
                            public InputStream getStream() {
                            	
                            	InputStream is = null;
		                       	try
		                       	{
		                       		is = ftp.simpleGetFile(repo.getFileRepoPath(), filename);
		                       	}
		                       	catch(Exception e)
		                       	{
		                       		System.out.println("Error downloading file: "+filename + " \nDetails:" + e.getMessage());
								    getUI().showNotification("Error downloading file : "+filename + " <br>Details:" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
								}
                            	return is;
                            }
                        }, filename));
                downloader.extend(downloadBtn);
                
            } else if (repo.isFileRepoIsLocal()) {
                
                FileDownloader downloader = new FileDownloader(new StreamResource(
                        new StreamResource.StreamSource() {
                            @Override
                            public InputStream getStream() {
                                InputStream is = null;
								try {
									is = new FileInputStream(repo.getFileRepoPath() + "/" + filename);
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									System.out.println("Error downloading file: "+filename + " \nDetails:" + e.getMessage());
							        getUI().showNotification("Error downloading file: "+filename + " <br>Details:" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
							    }
                                return is;
                            }
                        }, filename));
                downloader.extend(downloadBtn);                             
            }
	
		
	}
	
}
