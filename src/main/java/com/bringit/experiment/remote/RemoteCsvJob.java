package com.bringit.experiment.remote;

import com.bringit.experiment.bll.*;
import com.bringit.experiment.dao.BatchExperimentRecordsInsertDao;
import com.bringit.experiment.dao.CsvDataLoadExecutionResultDao;
import com.bringit.experiment.dao.DataFileDao;
import com.bringit.experiment.dao.XmlDataLoadExecutionResultDao;
import com.bringit.experiment.data.ExperimentParser;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.FTPUtil;
import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.quartz.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by msilay on 3/8/17.
 */
@DisallowConcurrentExecution
public class RemoteCsvJob implements Job {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // pull the JobDetails to get the type of Remote Connection.
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        CsvTemplate jobData = (CsvTemplate) jobDataMap.get("jobData");

        FilesRepository filesRepository = jobData.getInboundFileRepo();
        FilesRepository outboundRepo = jobData.getProcessedFileRepo();
        FilesRepository exceptionRepo = jobData.getExceptionFileRepo();

        if (filesRepository.isFileRepoIsSftp()) {
            FTPUtil sftp = new FTPUtil(filesRepository.getFileRepoHost(), Integer.parseInt(filesRepository.getFileRepoPort()),
                    filesRepository.getFileRepoUser(), filesRepository.getFileRepoPass());

            List<ChannelSftp.LsEntry> files =  sftp.secureGetFileList(filesRepository.getFileRepoPath());
            for (ChannelSftp.LsEntry file: files) {

                if (file.getFilename().endsWith(".csv") && (jobData.getCsvTemplatePrefix() == null || (jobData.getCsvTemplatePrefix() != null && jobData.getCsvTemplatePrefix().isEmpty()) || (jobData.getCsvTemplatePrefix() != null && file.getFilename().startsWith(jobData.getCsvTemplatePrefix())))) {
                    InputStream is = sftp.secureGetFile(filesRepository.getFileRepoPath()+"/"+file.getFilename());
                    if (is != null) {
                        System.out.println("Received Input file and passing to parser: "+file.getFilename());
                        //--  Data File --//

                        if (new DataFileDao().getDataFileByName(file.getFilename()) != null) {
                        	
                        	System.out.println(new Date().toString() + " Filename already processed: "+file.getFilename());
     	                      
                            DataFile dataFile = new DataFile();
                            Date createdDate = new Date();
                            dataFile.setCreatedDate(createdDate);
                            dataFile.setLastModifiedDate(createdDate);
                            dataFile.setDataFileIsXml(true);
                            dataFile.setDataFileIsCsv(false);
                            dataFile.setDataFileName(file.getFilename());
                            new DataFileDao().addDataFile(dataFile);
                            
                            System.out.println(new Date().toString() + " Moving to Exception Folder");
     	                    
                            moveFileToRepo(exceptionRepo, is, file.getFilename());
                            sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());
                        	
                            System.out.println(new Date().toString() + " Removed from Inbound Folder");
    	                      
                            dataFile.setFileRepoId(exceptionRepo);

                            saveExecutionResult(dataFile, file.getFilename(), jobData, true, "Data File is already processed. File Name: " + file.getFilename(), 0);

    	                    new DataFileDao().updateDataFile(dataFile);
    	                    
                        }
                        else
                        {	
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
	
	                        try {
	                            is.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                        InputStream copyStream = sftp.secureGetFile(filesRepository.getFileRepoPath() + "/" + file.getFilename());
	
	                        if (0 == sftpResponse.getCode()) {
	
	                            // Send file to outbound
	                            moveFileToRepo(outboundRepo, copyStream, file.getFilename());
	                            sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());
	
	                            dataFile.setFileRepoId(outboundRepo);
	                            Integer totalRecords = StringUtils.isNumeric(sftpResponse.getDetail().toString()) ? Integer.parseInt(sftpResponse.getDetail().toString()) : 0;
	                            saveExecutionResult(dataFile, file.getFilename(), jobData, false, "", totalRecords);
	                            
	                            System.out.println("Removed file from SFTP server");
	                        } else {
	                            // Send file to Exception
	                            moveFileToRepo(exceptionRepo, copyStream, file.getFilename());
	                            sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());
	                            dataFile.setFileRepoId(exceptionRepo);
	                            saveExecutionResult(dataFile, file.getFilename(), jobData, true, sftpResponse.getDescription(), 0);
		                        System.out.println("Removed file from SFTP server");
	                        }
	                        //b) Update Data File Repo dataFile.setFileRepoId();
	                        try {
	                            copyStream.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                        new DataFileDao().updateDataFile(dataFile);
                        }
                    } else {
                        sendTransferError(jobData, file.getFilename());
                    }
                }
            }
        } else if  (filesRepository.isFileRepoIsFtp()) {
            FTPUtil ftp = new FTPUtil(filesRepository.getFileRepoHost(), Integer.parseInt(filesRepository.getFileRepoPort()),
                    filesRepository.getFileRepoUser(), filesRepository.getFileRepoPass());

            FTPFile[] ftpFiles = ftp.simpleGetFileList(filesRepository.getFileRepoPath());
            for (FTPFile ftpFile: ftpFiles) {

                if (ftpFile.getName().endsWith(".csv") && (jobData.getCsvTemplatePrefix() == null || (jobData.getCsvTemplatePrefix() != null && jobData.getCsvTemplatePrefix().isEmpty()) || (jobData.getCsvTemplatePrefix() != null && ftpFile.getName().startsWith(jobData.getCsvTemplatePrefix())))) {

                    InputStream is = ftp.simpleGetFile(filesRepository.getFileRepoPath(), ftpFile.getName());
                    if (is != null) {

                        if (new DataFileDao().getDataFileByName(ftpFile.getName()) != null) {
                        	
                        	System.out.println(new Date().toString() + " Filename already processed: "+ftpFile.getName());
   	                      
                            DataFile dataFile = new DataFile();
                            Date createdDate = new Date();
                            dataFile.setCreatedDate(createdDate);
                            dataFile.setLastModifiedDate(createdDate);
                            dataFile.setDataFileIsXml(true);
                            dataFile.setDataFileIsCsv(false);
                            dataFile.setDataFileName(ftpFile.getName());
                            new DataFileDao().addDataFile(dataFile);

                            System.out.println(new Date().toString() + " Moving to Exception Folder");
     	                    
                            moveFileToRepo(exceptionRepo, is, ftpFile.getName());
                            ftp.deleteFile(ftpFile.getName(), filesRepository.getFileRepoPath());
                            
                            System.out.println(new Date().toString() + " Removed from Inbound Folder");
     	                    
                            dataFile.setFileRepoId(exceptionRepo);

                            saveExecutionResult(dataFile, ftpFile.getName(), jobData, true, "Data File is already processed. File Name: " + ftpFile.getName(), 0);

    	                    new DataFileDao().updateDataFile(dataFile);
    	                    
                        }
                        else
                        {
	                        DataFile dataFile = new DataFile();
	                        Date createdDate = new Date();
	                        dataFile.setCreatedDate(createdDate);
	                        dataFile.setLastModifiedDate(createdDate);
	                        dataFile.setDataFileIsXml(true);
	                        dataFile.setDataFileIsCsv(false);
	                        dataFile.setDataFileName(ftpFile.getName());
	                        new DataFileDao().addDataFile(dataFile);
	
	                        dataFile = new DataFileDao().getDataFileByName(ftpFile.getName());
	                        System.out.println("Filename : " + ftpFile.getName());
	                        System.out.println("Trying to process file");
	
	                        ResponseObj ftpResponse = processFile(is, jobData, ftpFile.getName(), dataFile);
	                        System.out.println("Processed File: " + ftpResponse.getDetail());
	
	                        // original stream has been read.  Need to get a new stream to move file.
	                        try {
	                            is.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                        InputStream copyStream = ftp.simpleGetFile(filesRepository.getFileRepoPath(), ftpFile.getName());
	
	                        if (0 == ftpResponse.getCode()) {
	                            // Send file to outbound
	                            moveFileToRepo(outboundRepo, copyStream, ftpFile.getName());
	                            ftp.deleteFile(ftpFile.getName(), filesRepository.getFileRepoPath());
	
	                            dataFile.setFileRepoId(outboundRepo);
	                            Integer totalRecords = StringUtils.isNumeric(ftpResponse.getDetail().toString()) ? Integer.parseInt(ftpResponse.getDetail().toString()) : 0;
		                        saveExecutionResult(dataFile, ftpFile.getName(), jobData, false, "", totalRecords);
	                            System.out.println("Removed file from FTP server");
	                        } else {
	                            // Send file to Exception
	                            moveFileToRepo(exceptionRepo, copyStream, ftpFile.getName());
	                            ftp.deleteFile(ftpFile.getName(), filesRepository.getFileRepoPath());
	
	                            dataFile.setFileRepoId(exceptionRepo);
	                            saveExecutionResult(dataFile, ftpFile.getName(), jobData, true, ftpResponse.getDescription(), 0);
	                            System.out.println("Removed file from FTP server");
	                        }
	
	                        try {
	                            copyStream.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                        new DataFileDao().updateDataFile(dataFile);
                        }
                    } else {
                        sendTransferError(jobData, ftpFile.getName());
                    }
                }
            }

        } else if (filesRepository.isFileRepoIsLocal()) {
            File dir = new File(filesRepository.getFileRepoPath());
            String[] extensions = new String[] { "csv" };
            System.out.println("Getting  all csv files in " + filesRepository.getFileRepoPath());
            List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, false);
            for (File file: files) {

            	if(jobData.getCsvTemplatePrefix() == null || (jobData.getCsvTemplatePrefix() != null && jobData.getCsvTemplatePrefix().isEmpty()) || (jobData.getCsvTemplatePrefix() != null && file.getName().startsWith(jobData.getCsvTemplatePrefix())))
            	{	
	                try {
	                    InputStream is = new FileInputStream(file);
	                    System.out.println("Reading input file and passing to parser: "+ file.getName());
	
	                    if(new DataFileDao().getDataFileByName(file.getName()) != null)
	                    {
	                    	System.out.println(new Date().toString() + " Filename already processed: "+file.getName());
   	                    
	                        DataFile dataFile = new DataFile();
	                        Date createdDate = new Date();
	                        dataFile.setCreatedDate(createdDate);
	                        dataFile.setLastModifiedDate(createdDate);
	                        dataFile.setDataFileIsXml(true);
	                        dataFile.setDataFileIsCsv(false);
	                        dataFile.setDataFileName(file.getName());
	                        new DataFileDao().addDataFile(dataFile);
	
	                        System.out.println(new Date().toString() + " Moving to Exception Folder");
	   	                    
	                        moveFileToRepo(exceptionRepo, is, file.getName());
	                        file.delete();
	                        
	                        System.out.println(new Date().toString() + " Removed from Inbound Folder");
	   	                    
	                        dataFile.setFileRepoId(exceptionRepo);
	
	                        saveExecutionResult(dataFile, file.getName(), jobData, true, "Data File is already processed. File Name: " + file.getName(), 0);

		                    new DataFileDao().updateDataFile(dataFile);
		                   
	                    }
	                    else
	                    {
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
		
		                    is.close();
		                    InputStream copyStream = new FileInputStream(file);
		
		                    if (0 == localResponse.getCode()) {
		                        // Send file to outbound
		                        moveFileToRepo(outboundRepo, copyStream, file.getName());
		                        file.delete();
		
		                        dataFile.setFileRepoId(outboundRepo);
		                        Integer totalRecords = StringUtils.isNumeric(localResponse.getDetail().toString()) ? Integer.parseInt(localResponse.getDetail().toString()) : 0;
		                        saveExecutionResult(dataFile, file.getName(), jobData, false, "", totalRecords);
		                        System.out.println("Removed file from local server");
		                    } else {
		                        // Send file to Exception
		                        moveFileToRepo(exceptionRepo, copyStream, file.getName());
		                        file.delete();
		
		                        dataFile.setFileRepoId(exceptionRepo);
		                        saveExecutionResult(dataFile, file.getName(), jobData, true, localResponse.getDescription(), 0);
			                    System.out.println("Removed file from local server");
		                    }
		
		                    copyStream.close();
		                    new DataFileDao().updateDataFile(dataFile);
	                    }
	                } catch (Exception e) {
	                    System.out.println("Error processing local file: "+file.getName());
	                }
	
	            }
            }
        }

        System.out.println("JOB run...");
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

    private ResponseObj processFile(InputStream is, CsvTemplate csvTemplate, String filename, DataFile dataFile) {

        try {

            Config config = new Config();
            String strBatchSize = config.getProperty("batchinsert");
            int iBatchSize = Integer.parseInt(strBatchSize);

            //1)Parsing & Validation
            ResponseObj responseObj = new ExperimentParser().parseCSV(is, csvTemplate, filename);

            //2)Batch Insert
            ResponseObj batchResponse = new BatchExperimentRecordsInsertDao().executeExperimentBatchRecordsInsert(responseObj.getCsvInsertColumns(),
                    responseObj.getCsvInsertValues(), null, csvTemplate, null, dataFile, csvTemplate.getExperiment(), iBatchSize);

            System.out.println("Filename : "+filename+" - "+responseObj.getDetail());

            if (responseObj.getCode() == 0) {
                System.out.println("Successfully parsed file: "+filename);

            } else {
                sendTransferError(csvTemplate, filename);
            }

            return batchResponse;

        } catch (Exception ex) {
            System.out.println("Error parsing file: "+filename);
            sendTransferError(csvTemplate, filename);

        }

        return null;
    }

    public void sendTransferError(CsvTemplate csvTemplate, String filename) {
        try {
            // TODO: replace this with the email logic when an error occurs.
            System.out.println("Error processing file: "+filename);
        } catch(Exception ex) {
            System.out.println("Error sending ERROR email: "+ex);
        }

    }

    public void sendTransferError(XmlTemplate xmlTemplate, String filename) {
        try {
            // TODO: replace this with the email logic when an error occurs.
            System.out.println("Error processing file: "+filename);
        } catch(Exception ex) {
            System.out.println("Error sending ERROR email: "+ex);
        }

    }

    private void saveExecutionResult(DataFile dataFile, String fileName, CsvTemplate csvTemplate, boolean exception, String exceptionDetails, Integer totalRecords)
    {

        CsvDataLoadExecutionResult csvDataLoadExecResult = new CsvDataLoadExecutionResult();
        csvDataLoadExecResult.setDataFile(dataFile);
        csvDataLoadExecResult.setCsvDataLoadExecException(exception);
        csvDataLoadExecResult.setCsvDataLoadExecExeptionDetails(exceptionDetails);
        csvDataLoadExecResult.setCsvDataLoadExecTime(new Date());
        csvDataLoadExecResult.setCsvDataLoadTotalRecords(totalRecords);
        csvDataLoadExecResult.setCsvTemplate(csvTemplate);
        new CsvDataLoadExecutionResultDao().addCsvDataLoadExecutionResult(csvDataLoadExecResult);
    }
}
