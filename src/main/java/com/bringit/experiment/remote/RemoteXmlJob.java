package com.bringit.experiment.remote;

import com.bringit.experiment.bll.DataFile;
import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.XmlDataLoadExecutionResult;
import com.bringit.experiment.bll.XmlTemplate;

import com.bringit.experiment.dao.BatchExperimentRecordsInsertDao;
import com.bringit.experiment.dao.DataFileDao;
import com.bringit.experiment.dao.XmlDataLoadExecutionResultDao;
import com.bringit.experiment.data.ExperimentParser;

import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.FTPUtil;

import com.jcraft.jsch.ChannelSftp;
import com.vaadin.ui.Notification.Type;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPFile;

import org.quartz.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by msilay on 2/22/17.
 */
@DisallowConcurrentExecution
public class RemoteXmlJob implements Job {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // pull the JobDetails to get the type of Remote Connection.
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        XmlTemplate jobData = (XmlTemplate) jobDataMap.get("jobData");

        FilesRepository filesRepository = jobData.getInboundFileRepo();
        FilesRepository outboundRepo = jobData.getProcessedFileRepo();
        FilesRepository exceptionRepo = jobData.getExceptionFileRepo();

        System.out.println("Running XML Job");
        if (filesRepository.isFileRepoIsSftp()) {
            System.out.println("SFTP Repo");
            FTPUtil sftp = new FTPUtil(filesRepository.getFileRepoHost(), Integer.parseInt(filesRepository.getFileRepoPort()),
                    filesRepository.getFileRepoUser(), filesRepository.getFileRepoPass());

            List<ChannelSftp.LsEntry> files =  sftp.secureGetFileList(filesRepository.getFileRepoPath());
            for (ChannelSftp.LsEntry file: files) {

                if (file.getFilename().endsWith(".xml")) {
                	
                    InputStream is = sftp.secureGetFile(filesRepository.getFileRepoPath()+"/"+file.getFilename());
                    
                    
                    if (is != null) {
                        if(new DataFileDao().getDataFileByName(file.getFilename()) != null)
                		{           
                        	DataFile dataFile = new DataFile();
                        	Date createdDate = new Date();
                            dataFile.setCreatedDate(createdDate);
                            dataFile.setLastModifiedDate(createdDate);
                 			dataFile.setDataFileIsXml(true);
                 			dataFile.setDataFileIsCsv(false);
                 			dataFile.setDataFileName(file.getFilename());
                 			new DataFileDao().addDataFile(dataFile);
                 			
                        	moveFileToRepo(exceptionRepo, is, file.getFilename());
                            dataFile.setFileRepoId(exceptionRepo);
                        	
                            saveExecutionResult(dataFile, file.getFilename(), jobData, true, "Data File is already processed. File Name: " + file.getFilename());
                            return; 
                		}
                    	
                        System.out.println("Received Input file and passing to parser: "+file.getFilename());
                        //--  Data File --//
                        
                        DataFile dataFile = new DataFile();
                    	Date createdDate = new Date();
                        dataFile.setCreatedDate(createdDate);
                        dataFile.setLastModifiedDate(createdDate);
            			dataFile.setDataFileIsXml(true);
            			dataFile.setDataFileIsCsv(false);
            			dataFile.setDataFileName(file.getFilename());
            			new DataFileDao().addDataFile(dataFile);

                        dataFile = new DataFileDao().getDataFileByName(file.getFilename());
                        
                        ResponseObj sftpResponse = processFile(is, jobData, file.getFilename(), dataFile);

                        if (0 == sftpResponse.getCode()) {
                        	
                            // Send file to outbound
                            moveFileToRepo(outboundRepo, is, file.getFilename());
                            sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());

                            dataFile.setFileRepoId(outboundRepo);
                            saveExecutionResult(dataFile, file.getFilename(), jobData, false, "");
                            System.out.println("Removed file from SFTP server");
                        } else {
                            // Send file to Exception
                            moveFileToRepo(exceptionRepo, is, file.getFilename());
                            sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());
                            dataFile.setFileRepoId(exceptionRepo);
                            saveExecutionResult(dataFile, file.getFilename(), jobData, true, sftpResponse.getDescription());
                            System.out.println("Removed file from SFTP server");
                        }
                        //b) Update Data File Repo dataFile.setFileRepoId();
                        new DataFileDao().updateDataFile(dataFile);

                    } else {
                        sendTransferError(jobData, file.getFilename());
                    }
                }
            }
        } else if  (filesRepository.isFileRepoIsFtp()) {
            System.out.println("FTP Repo");
            FTPUtil ftp = new FTPUtil(filesRepository.getFileRepoHost(), Integer.parseInt(filesRepository.getFileRepoPort()),
                    filesRepository.getFileRepoUser(), filesRepository.getFileRepoPass());

            FTPFile[] ftpFiles = ftp.simpleGetFileList(filesRepository.getFileRepoPath());
            for (FTPFile ftpFile: ftpFiles) {

                if (ftpFile.getName().endsWith(".xml")) {
                    System.out.println("FTP FILE: "+ftpFile.getName());
                    InputStream is = ftp.simpleGetFile(filesRepository.getFileRepoPath(), ftpFile.getName());
                    if (is != null) {
                    	
                    	if(new DataFileDao().getDataFileByName(ftpFile.getName()) != null)
                		{           
                        	DataFile dataFile = new DataFile();
                        	Date createdDate = new Date();
                            dataFile.setCreatedDate(createdDate);
                            dataFile.setLastModifiedDate(createdDate);
                 			dataFile.setDataFileIsXml(true);
                 			dataFile.setDataFileIsCsv(false);
                 			dataFile.setDataFileName(ftpFile.getName());
                 			new DataFileDao().addDataFile(dataFile);
                 			
                        	moveFileToRepo(exceptionRepo, is, ftpFile.getName());
                            dataFile.setFileRepoId(exceptionRepo);
                        	
                            saveExecutionResult(dataFile, ftpFile.getName(), jobData, true, "Data File is already processed. File Name: " + ftpFile.getName());
                            return; 
                		}
                    	
                        DataFile dataFile = new DataFile();
                    	Date createdDate = new Date();
                        dataFile.setCreatedDate(createdDate);
                        dataFile.setLastModifiedDate(createdDate);
                        dataFile.setDataFileIsXml(true);
                        dataFile.setDataFileIsCsv(false);
                        dataFile.setDataFileName(ftpFile.getName());
                        new DataFileDao().addDataFile(dataFile);

                        dataFile = new DataFileDao().getDataFileByName(ftpFile.getName());
                        System.out.println("Filename : "+ftpFile.getName());
                        System.out.println("Trying to process file");
                        ResponseObj ftpResponse = processFile(is, jobData, ftpFile.getName(), dataFile);
                        System.out.println("Processed File: "+ftpResponse.getDetail());
                        
                        // original stream has been read.  Need to get a new stream to move file. 
                        try {
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        
                        InputStream copyStream = ftp.simpleGetFile(filesRepository.getFileRepoPath(), ftpFile.getName());
                        System.out.println(new Date() + " FTP Response Code: " + ftpResponse.getCode());
                        if (0 == ftpResponse.getCode()) {
                            // Send file to outbound
                            moveFileToRepo(outboundRepo, copyStream, ftpFile.getName());
                            ftp.deleteFile(ftpFile.getName(),filesRepository.getFileRepoPath());

                            dataFile.setFileRepoId(outboundRepo);
                            saveExecutionResult(dataFile, ftpFile.getName(), jobData, false, "");
                            System.out.println("Removed file from FTP server");
                        } else {
                            // Send file to Exception
                            moveFileToRepo(exceptionRepo, copyStream, ftpFile.getName());
                            ftp.deleteFile(ftpFile.getName(), filesRepository.getFileRepoPath());

                            dataFile.setFileRepoId(exceptionRepo);
                            saveExecutionResult(dataFile, ftpFile.getName(), jobData, true, ftpResponse.getDescription());
                            System.out.println("Removed file from FTP server");
                        }

                        new DataFileDao().updateDataFile(dataFile);
                        
                        try {
							copyStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        
                    } else {
                        System.out.println("Null Input Stream");
                        sendTransferError(jobData, ftpFile.getName());
                    }
                }
            }

        } else if (filesRepository.isFileRepoIsLocal()) {
            File dir = new File(filesRepository.getFileRepoPath());
            String[] extensions = new String[] { "xml" };
            System.out.println("Getting all .xml files in " + filesRepository.getFileRepoPath());
            List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, false);
            for (File file: files) {

                try {
                    InputStream is = new FileInputStream(file);
                    System.out.println("Reading input file and passing to parser: "+ file.getName());
                    
                    if(new DataFileDao().getDataFileByName(file.getName()) != null)
            		{           
                    	DataFile dataFile = new DataFile();
                    	Date createdDate = new Date();
                        dataFile.setCreatedDate(createdDate);
                        dataFile.setLastModifiedDate(createdDate);
             			dataFile.setDataFileIsXml(true);
             			dataFile.setDataFileIsCsv(false);
             			dataFile.setDataFileName(file.getName());
             			new DataFileDao().addDataFile(dataFile);
             			
                    	moveFileToRepo(exceptionRepo, is, file.getName());
                        dataFile.setFileRepoId(exceptionRepo);
                    	
                        saveExecutionResult(dataFile, file.getName(), jobData, true, "Data File is already processed. File Name: " + file.getName());
                        return; 
            		}
                    
                    DataFile dataFile = new DataFile();
                	Date createdDate = new Date();
                    dataFile.setCreatedDate(createdDate);
                    dataFile.setLastModifiedDate(createdDate);
                    dataFile.setDataFileIsXml(true);
                    dataFile.setDataFileIsCsv(false);
                    dataFile.setDataFileName(file.getName());
                    new DataFileDao().addDataFile(dataFile);

                    dataFile = new DataFileDao().getDataFileByName(file.getName());

                    ResponseObj localResponse = processFile(is, jobData, file.getName(), dataFile);

                    if (0 == localResponse.getCode()) {
                        // Send file to outbound
                        moveFileToRepo(outboundRepo, is, file.getName());
                        file.delete();

                        dataFile.setFileRepoId(outboundRepo);
                        saveExecutionResult(dataFile, file.getName(), jobData, false, "");
                        
                        System.out.println("Removed file from local server");
                    } else {
                        // Send file to Exception
                        moveFileToRepo(exceptionRepo, is, file.getName());
                        file.delete();

                        dataFile.setFileRepoId(exceptionRepo);
                        saveExecutionResult(dataFile, file.getName(), jobData, true, localResponse.getDescription());
                        System.out.println("Removed file from local server");
                    }

                    new DataFileDao().updateDataFile(dataFile);
                } catch (Exception e) {
                    System.out.println("Error processing local file: "+file.getName());
                }

            }
        }

        System.out.println("JOB run..." + new Date());
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
            System.out.println("Error moving file to repo: "+filename);
        }

    }

    private ResponseObj processFile(InputStream is, XmlTemplate xmlTemplate, String filename, DataFile dataFile) {

        try {

        	/*
        	if(new DataFileDao().getDataFileByName(filename) != null)
    		{
        		ResponseObj responseObj = new ResponseObj();
        		responseObj.setCode(101);
        		responseObj.setDescription("Data File is already processed. File Name: " + filename);
        		responseObj.setDetail("Data File is already processed. File Name: " + filename);
        		return responseObj; 
    		}
        	*/
        	
        	
            Config config = new Config();
            String strBatchSize = config.getProperty("batchinsert");
            int iBatchSize = Integer.parseInt(strBatchSize);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            System.out.println("Document Builder Factory Instance ");

            factory.setNamespaceAware(true);
            factory.setValidating(false);

            DocumentBuilder domBuilder = factory.newDocumentBuilder();
            System.out.println("Document Builder Instance of Available Bytes: " + is.available());

            Document doc = domBuilder.parse(is);
            System.out.println("Starting Parsing & Validation: "+filename);
            
            //1)Parsing & Validation
            ResponseObj responseObj = new ExperimentParser().parseXmlDocument(doc, xmlTemplate);
            System.out.println("Successfully parsed file: "+filename);
            
            //2)Batch Insert
            ResponseObj batchResponse = new BatchExperimentRecordsInsertDao().executeExperimentBatchRecordsInsert(responseObj.getCsvInsertColumns(),
                    responseObj.getCsvInsertValues(), xmlTemplate, null, null, dataFile, xmlTemplate.getExperiment(), iBatchSize);

            if (responseObj.getCode() == 0) {
                System.out.println("Records Successfully inserted. DataFile: "+filename);

            } else {
                sendTransferError(xmlTemplate, filename);
            }

            return batchResponse;

        } catch (Exception ex) {
        	
            System.out.println("Error parsing file: "+filename + "\n Ex Details:" + ex);
            ex.printStackTrace();
            sendTransferError(xmlTemplate, filename);

        }

        return null;
    }

    public void sendTransferError(XmlTemplate xmlTemplate, String filename) {
        try {
            // TODO: replace this with the email logic when an error occurs.
            System.out.println("Error processing file: "+filename);
        } catch(Exception ex) {
            System.out.println("Error sending ERROR email: "+ex);
        }

    }
    
    private void saveExecutionResult(DataFile dataFile, String fileName, XmlTemplate xmlTemplate, boolean exception, String exceptionDetails)
    {
    	XmlDataLoadExecutionResult xmlDataLoadExecResult = new XmlDataLoadExecutionResult();
		xmlDataLoadExecResult.setDataFile(dataFile);
		xmlDataLoadExecResult.setXmlDataLoadExecException(exception);
		xmlDataLoadExecResult.setXmlDataLoadExecExeptionDetails(exceptionDetails);
		xmlDataLoadExecResult.setXmlDataLoadExecTime(new Date());
		xmlDataLoadExecResult.setXmlTemplate(xmlTemplate);
		new XmlDataLoadExecutionResultDao().addXmlDataLoadExecutionResult(xmlDataLoadExecResult);		
    }
}