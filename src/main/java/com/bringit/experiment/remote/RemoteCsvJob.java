package com.bringit.experiment.remote;

import com.bringit.experiment.bll.CsvDataLoadExecutionResult;
import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.bll.DataFile;
import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.dao.BatchExperimentRecordsInsertDao;
import com.bringit.experiment.dao.CsvDataLoadExecutionResultDao;
import com.bringit.experiment.dao.DataFileDao;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.data.ExperimentParser;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.FTPUtil;
import com.jcraft.jsch.ChannelSftp;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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

        //File Filter Criteria
        String fileFilterCriteria = "NoCriteria";
        if(jobData.getCsvTemplatePrefix() != null)
        	fileFilterCriteria = "Prefix";
        if(jobData.getCsvTemplateSuffix() != null)
        	fileFilterCriteria = "Suffix";
        if(jobData.getCsvTemplateRegex() != null)
        	fileFilterCriteria = "Regex";
        
        FilesRepository filesRepository = jobData.getInboundFileRepo();
        FilesRepository outboundRepo = jobData.getProcessedFileRepo();
        FilesRepository exceptionRepo = jobData.getExceptionFileRepo();

        if (filesRepository.isFileRepoIsSftp()) {
            FTPUtil sftp = new FTPUtil(filesRepository.getFileRepoHost(), Integer.parseInt(filesRepository.getFileRepoPort()),
                    filesRepository.getFileRepoUser(), filesRepository.getFileRepoPass());
            
            if(jobData.getCsvTemplateTransformTxtFound() != null && jobData.getCsvTemplateTransformTxtFound())
            {
            	//Change file extension from TXT to CSV
            	List<ChannelSftp.LsEntry> files =  sftp.secureGetFileList(filesRepository.getFileRepoPath());
                
                for (ChannelSftp.LsEntry file: files) {
                	
                    if (file.getFilename().endsWith(".txt") && ("NoCriteria".equals(fileFilterCriteria) || ("Prefix".equals(fileFilterCriteria) && file.getFilename().startsWith(jobData.getCsvTemplatePrefix()))
                    			|| ("Suffix".equals(fileFilterCriteria) && file.getFilename().replaceAll(".txt","").endsWith(jobData.getCsvTemplateSuffix()))
                    			|| ("Regex".equals(fileFilterCriteria) && Pattern.matches(jobData.getCsvTemplateRegex(), file.getFilename().replaceAll(".txt","")))))
                    {
                    	InputStream is = sftp.secureGetFile(filesRepository.getFileRepoPath()+"/"+file.getFilename());
                        moveFileToRepo(filesRepository, is, file.getFilename());
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());   
                        System.out.println("File renamed: " + file.getFilename());                 	
                    }
                }
            }
                        
            List<ChannelSftp.LsEntry> files =  sftp.secureGetFileList(filesRepository.getFileRepoPath());
            boolean filesProcessed = false;
            
            for (ChannelSftp.LsEntry file: files) {
            	
                if (file.getFilename().endsWith(".csv") && ("NoCriteria".equals(fileFilterCriteria) || ("Prefix".equals(fileFilterCriteria) && file.getFilename().startsWith(jobData.getCsvTemplatePrefix()))
                			|| ("Suffix".equals(fileFilterCriteria) && file.getFilename().replaceAll(".csv","").endsWith(jobData.getCsvTemplateSuffix()))
                			|| ("Regex".equals(fileFilterCriteria) && Pattern.matches(jobData.getCsvTemplateRegex(), file.getFilename().replaceAll(".csv",""))))){
                	
                	//&& (jobData.getCsvTemplatePrefix() == null || (jobData.getCsvTemplatePrefix() != null && jobData.getCsvTemplatePrefix().isEmpty()) || (jobData.getCsvTemplatePrefix() != null && file.getFilename().startsWith(jobData.getCsvTemplatePrefix())))) {
                    
                	
                	InputStream is = sftp.secureGetFile(filesRepository.getFileRepoPath()+"/"+file.getFilename());
                    if (is != null) {
                    	filesProcessed = true;
                        System.out.println("Received Input file and passing to parser: "+file.getFilename());
                        //--  Data File --//

                        if (new DataFileDao().getDataFileByName(file.getFilename()) != null) {
                        	
                        	System.out.println(new Date().toString() + " Filename already processed: "+file.getFilename());
     	                      
                            DataFile dataFile = new DataFile();
                            Date createdDate = new Date();
                            dataFile.setCreatedDate(createdDate);
                            dataFile.setLastModifiedDate(createdDate);
                            dataFile.setDataFileIsXml(false);
                            dataFile.setDataFileIsCsv(true);
                            dataFile.setDataFileName(file.getFilename());
                            new DataFileDao().addDataFile(dataFile);
                            
                            System.out.println(new Date().toString() + " Moving to Exception Folder");
     	                    
                            moveFileToRepo(exceptionRepo, is, file.getFilename());
                            sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());
                        	
                            System.out.println(new Date().toString() + " Removed from Inbound Folder");
    	                      
                            dataFile.setFileRepoId(exceptionRepo);

                            saveExecutionResult(dataFile, null, dataFile, file.getFilename(), jobData, true, "Data File is already processed. File Name: " + file.getFilename(), 0, 0, 0);

    	                    new DataFileDao().updateDataFile(dataFile);
    	                    
    	                    try {
	                            is.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
                        }
                        else
                        {	
	                        DataFile dataFile = new DataFile();
	                        Date createdDate = new Date();
	                        dataFile.setCreatedDate(createdDate);
	                        dataFile.setLastModifiedDate(createdDate);
	                        dataFile.setDataFileIsXml(false);
	                        dataFile.setDataFileIsCsv(true);
	                        dataFile.setDataFileName(file.getFilename());
	                        dataFile.setFileRepoId(outboundRepo);
	                        new DataFileDao().addDataFile(dataFile);
	
	                        dataFile = new DataFileDao().getDataFileByName(file.getFilename());
	
	                        ResponseObj sftpResponse = processFile(is, jobData, file.getFilename(), dataFile);
	
	                        try {
	                            is.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                        InputStream copyStream = sftp.secureGetFile(filesRepository.getFileRepoPath() + "/" + file.getFilename());
	
	                        DataFile dataFileProcessed = new DataFile();
	            			DataFile dataFileException = new DataFile();
	            			
	                        
	                        if (0 == sftpResponse.getCode()) {
	
	                        	
	                        	if(sftpResponse.getCsvRowException() != null && sftpResponse.getCsvRowException().size() > 0)
	        					{
	        						//Split files into 2 CSV files
	        						splitCsvExceptionFiles(jobData.getProcessedFileRepo(), jobData.getExceptionFileRepo(), copyStream, file.getFilename(), sftpResponse);
	        					
	        						dataFileProcessed.setDataFileIsCsv(true);
	        						dataFileProcessed.setDataFileName("Partial - " + file.getFilename());
	        						dataFileProcessed.setFileRepoId(jobData.getProcessedFileRepo());
	        						new DataFileDao().addDataFile(dataFileProcessed);
	        												
	        						dataFileException.setDataFileIsCsv(true);
	        						dataFileException.setDataFileName("Failed - " + file.getFilename());
	        						dataFileException.setFileRepoId(jobData.getExceptionFileRepo());
	        						new DataFileDao().addDataFile(dataFileException);
	        					
	        						Integer processedRecords =  (sftpResponse.getCsvInsertValues() != null ? sftpResponse.getCsvInsertValues().size() : 0);
		        					Integer exceptionRecords =  (sftpResponse.getCsvRowException() != null ? sftpResponse.getCsvRowException().size() : 0);
		        					Integer totalRecords = processedRecords + exceptionRecords;
		        					
	    							dataFile.setFileRepoId(exceptionRepo);
	    							new DataFileDao().updateDataFile(dataFile);
	    							
	    							saveExecutionResult(dataFile, dataFileProcessed, dataFileException, file.getFilename(), jobData, true, "Some records could not be processed.", totalRecords, processedRecords, exceptionRecords);
	    							moveFileToRepo(exceptionRepo, copyStream, file.getFilename());	
	    						}
	    						else
	    						{
	    							Integer totalRecords = StringUtils.isNumeric(sftpResponse.getDetail().toString()) ? Integer.parseInt(sftpResponse.getDetail().toString()) : 0;
		                            saveExecutionResult(dataFile, dataFile, null, file.getFilename(), jobData, false, "", totalRecords, totalRecords, 0);
	    							moveFileToRepo(outboundRepo, copyStream, file.getFilename());					
	    						}
	                        	
	                        	
	                            // Send file to outbound
	                            //moveFileToRepo(outboundRepo, copyStream, file.getFilename());
	                            sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());
	
	                            //dataFile.setFileRepoId(outboundRepo);
	                            //Integer totalRecords = StringUtils.isNumeric(sftpResponse.getDetail().toString()) ? Integer.parseInt(sftpResponse.getDetail().toString()) : 0;
	                            //saveExecutionResult(dataFile, file.getFilename(), jobData, false, "", totalRecords);
	                            
	                            System.out.println("Removed file from SFTP server");
	                            
	                            //Getting data refreshed into replication tables
	                            Integer experimentId = jobData.getExperiment().getExpId();
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

	                        } else {
	                            // Send file to Exception
	                            moveFileToRepo(exceptionRepo, copyStream, file.getFilename());
	                            sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());
	                            dataFile.setFileRepoId(exceptionRepo);
	                            saveExecutionResult(dataFile, null, dataFile, file.getFilename(), jobData, true, sftpResponse.getDescription(), 0, 0, 0);
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
                else
                {
                	System.out.println("File " + file.getFilename() + " does not match criteria for CSV Template: " + jobData.getCsvTemplateName());
                }
                
            }
            
            if(!filesProcessed)
            {
            	saveExecutionResult(null, null, null, null, jobData, true, "There are no files matching criteria for CSV Template: " + jobData.getCsvTemplateName(), 0, 0, 0);                
            }
            
        } else if  (filesRepository.isFileRepoIsFtp()) {
            FTPUtil ftp = new FTPUtil(filesRepository.getFileRepoHost(), Integer.parseInt(filesRepository.getFileRepoPort()),
                    filesRepository.getFileRepoUser(), filesRepository.getFileRepoPass());

            if(jobData.getCsvTemplateTransformTxtFound() != null && jobData.getCsvTemplateTransformTxtFound())
            {
            	//Change file extension from TXT to CSV
            	FTPFile[] ftpFiles = ftp.simpleGetFileList(filesRepository.getFileRepoPath());
            	 for (FTPFile ftpFile: ftpFiles) {
            		 if (ftpFile.getName().endsWith(".txt") && ("NoCriteria".equals(fileFilterCriteria) || ("Prefix".equals(fileFilterCriteria) && ftpFile.getName().startsWith(jobData.getCsvTemplatePrefix()))
                 			|| ("Suffix".equals(fileFilterCriteria) && ftpFile.getName().replaceAll(".txt","").endsWith(jobData.getCsvTemplateSuffix()))
                 			|| ("Regex".equals(fileFilterCriteria) && Pattern.matches(jobData.getCsvTemplateRegex(), ftpFile.getName().replaceAll(".txt","")))))
            		 {
            			 InputStream is = ftp.simpleGetFile(filesRepository.getFileRepoPath(), ftpFile.getName());
                         moveFileToRepo(filesRepository, is, ftpFile.getName().replaceAll(".txt", ".csv"));
                         try {
                             is.close();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                         ftp.deleteFile(ftpFile.getName(), filesRepository.getFileRepoPath());
                         System.out.println("File renamed: " + ftpFile.getName());
            		 }
            	 } 
            }
            
            
            FTPFile[] ftpFiles = ftp.simpleGetFileList(filesRepository.getFileRepoPath());
            boolean filesProcessed = false;
            for (FTPFile ftpFile: ftpFiles) {

                if (ftpFile.getName().endsWith(".csv") && ("NoCriteria".equals(fileFilterCriteria) || ("Prefix".equals(fileFilterCriteria) && ftpFile.getName().startsWith(jobData.getCsvTemplatePrefix()))
            			|| ("Suffix".equals(fileFilterCriteria) && ftpFile.getName().replaceAll(".csv","").endsWith(jobData.getCsvTemplateSuffix()))
            			|| ("Regex".equals(fileFilterCriteria) && Pattern.matches(jobData.getCsvTemplateRegex(), ftpFile.getName().replaceAll(".csv",""))))){ 
                		//&& (jobData.getCsvTemplatePrefix() == null || (jobData.getCsvTemplatePrefix() != null && jobData.getCsvTemplatePrefix().isEmpty()) || (jobData.getCsvTemplatePrefix() != null && ftpFile.getName().startsWith(jobData.getCsvTemplatePrefix())))) {

                    InputStream is = ftp.simpleGetFile(filesRepository.getFileRepoPath(), ftpFile.getName());
                    if (is != null) {
                    	filesProcessed = true;
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

                            saveExecutionResult(dataFile, null, dataFile, ftpFile.getName(), jobData, true, "Data File is already processed. File Name: " + ftpFile.getName(), 0, 0, 0);

    	                    new DataFileDao().updateDataFile(dataFile);
    	                    
    	                    // original stream has been read.  Need to get a new stream to move file.
	                        try {
	                            is.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
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
	                        dataFile.setFileRepoId(outboundRepo);
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
	
	                        DataFile dataFileProcessed = new DataFile();
	            			DataFile dataFileException = new DataFile();
	            			
	                        
	                        if (0 == ftpResponse.getCode()) {
	                        	
	                        	if(ftpResponse.getCsvRowException() != null && ftpResponse.getCsvRowException().size() > 0)
	        					{
	        						//Split files into 2 CSV files
	        						splitCsvExceptionFiles(jobData.getProcessedFileRepo(), jobData.getExceptionFileRepo(), copyStream, ftpFile.getName(), ftpResponse);
	        					
	        						dataFileProcessed.setDataFileIsCsv(true);
	        						dataFileProcessed.setDataFileName("Partial - " + ftpFile.getName());
	        						dataFileProcessed.setFileRepoId(jobData.getProcessedFileRepo());
	        						new DataFileDao().addDataFile(dataFileProcessed);
	        												
	        						dataFileException.setDataFileIsCsv(true);
	        						dataFileException.setDataFileName("Failed - " + ftpFile.getName());
	        						dataFileException.setFileRepoId(jobData.getExceptionFileRepo());
	        						new DataFileDao().addDataFile(dataFileException);
	        					
	        						Integer processedRecords =  (ftpResponse.getCsvInsertValues() != null ? ftpResponse.getCsvInsertValues().size() : 0);
		        					Integer exceptionRecords =  (ftpResponse.getCsvRowException() != null ? ftpResponse.getCsvRowException().size() : 0);
		        					Integer totalRecords = processedRecords + exceptionRecords;
		        					
	    							dataFile.setFileRepoId(exceptionRepo);
	    							new DataFileDao().updateDataFile(dataFile);
	    							
	    							saveExecutionResult(dataFile, dataFileProcessed, dataFileException, ftpFile.getName(), jobData, true, "Some records could not be processed.", totalRecords, processedRecords, exceptionRecords);
	    							moveFileToRepo(exceptionRepo, copyStream, ftpFile.getName());	
	    						}
	    						else
	    						{
	    							Integer totalRecords = StringUtils.isNumeric(ftpResponse.getDetail().toString()) ? Integer.parseInt(ftpResponse.getDetail().toString()) : 0;
		                            saveExecutionResult(dataFile, dataFile, null, ftpFile.getName(), jobData, false, "", totalRecords, totalRecords, 0);
	    							moveFileToRepo(outboundRepo, copyStream, ftpFile.getName());					
	    						}
	                        	
	                            // Send file to outbound
	                            //moveFileToRepo(outboundRepo, copyStream, ftpFile.getName());
	                            ftp.deleteFile(ftpFile.getName(), filesRepository.getFileRepoPath());
	
	                            //dataFile.setFileRepoId(outboundRepo);
	                            //Integer totalRecords = StringUtils.isNumeric(ftpResponse.getDetail().toString()) ? Integer.parseInt(ftpResponse.getDetail().toString()) : 0;
		                        //saveExecutionResult(dataFile, ftpFile.getName(), jobData, false, "", totalRecords);
	                            System.out.println("Removed file from FTP server");
	                            
	                            //Getting data refreshed into replication tables
	                            Integer experimentId = jobData.getExperiment().getExpId();
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
	                            
	                        } else {
	                            // Send file to Exception
	                            moveFileToRepo(exceptionRepo, copyStream, ftpFile.getName());
	                            ftp.deleteFile(ftpFile.getName(), filesRepository.getFileRepoPath());
	
	                            dataFile.setFileRepoId(exceptionRepo);
	                            saveExecutionResult(dataFile, null, dataFile, ftpFile.getName(), jobData, true, ftpResponse.getDescription(), 0, 0, 0);
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
                else
                	System.out.println("File " + ftpFile.getName() + " does not match criteria for CSV Template: " + jobData.getCsvTemplateName());
            }
            if(!filesProcessed)
            {
            	saveExecutionResult(null, null, null, null, jobData, true, "There are no files matching criteria for CSV Template: " + jobData.getCsvTemplateName(), 0, 0, 0);                
            }
            
        } else if (filesRepository.isFileRepoIsLocal()) {
            File dir = new File(filesRepository.getFileRepoPath());
            
            if(jobData.getCsvTemplateTransformTxtFound() != null && jobData.getCsvTemplateTransformTxtFound())
            {
            	//Change file extension from TXT to CSV
            	String[] extensions = new String[] { "txt" };
                System.out.println("Getting  all txt files in " + filesRepository.getFileRepoPath());
                
                List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, false);
                boolean filesProcessed = false;
                for (File file: files) {
                	if ("NoCriteria".equals(fileFilterCriteria) || ("Prefix".equals(fileFilterCriteria) && file.getName().startsWith(jobData.getCsvTemplatePrefix()))
                 			|| ("Suffix".equals(fileFilterCriteria) && file.getName().replaceAll(".txt","").endsWith(jobData.getCsvTemplateSuffix()))
                 			|| ("Regex".equals(fileFilterCriteria) && Pattern.matches(jobData.getCsvTemplateRegex(), file.getName().replaceAll(".txt",""))))
                	 {
                		InputStream is = null;
						try {
							is = new FileInputStream(file);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                		moveFileToRepo(filesRepository, is, file.getName().replaceAll(".txt", ".csv"));
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        file.delete();
                        System.out.println("File renamed: " + file.getName());
                	 }
                }
            }
            
            String[] extensions = new String[] { "csv" };
            System.out.println("Getting  all csv files in " + filesRepository.getFileRepoPath());
            
            List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, false);
            boolean filesProcessed = false;
            for (File file: files) {
            	//System.out.println(fileFilterCriteria);
            	//System.out.println("Regular Expression:"  + jobData.getCsvTemplateRegex() + " File Name: "  + file.getName().replaceAll(".csv",""));
            	
            	//if(jobData.getCsvTemplatePrefix() == null || (jobData.getCsvTemplatePrefix() != null && jobData.getCsvTemplatePrefix().isEmpty()) || (jobData.getCsvTemplatePrefix() != null && file.getName().startsWith(jobData.getCsvTemplatePrefix())))
            	//{
            	System.out.println("Next file: "+ file.getName());
            	
            	System.out.println("File Filter Criteria: "+ fileFilterCriteria + " Regex: " + jobData.getCsvTemplateRegex() + " File Name: " + file.getName().replaceAll(".csv",""));
            	
            	 if ("NoCriteria".equals(fileFilterCriteria) || ("Prefix".equals(fileFilterCriteria) && file.getName().startsWith(jobData.getCsvTemplatePrefix()))
             			|| ("Suffix".equals(fileFilterCriteria) && file.getName().replaceAll(".csv","").endsWith(jobData.getCsvTemplateSuffix()))
             			|| ("Regex".equals(fileFilterCriteria) && Pattern.matches(jobData.getCsvTemplateRegex(), file.getName().replaceAll(".csv",""))))
            	 { 
                    try {
                    	filesProcessed = true;
	                    InputStream is = new FileInputStream(file);
	                    System.out.println("Reading input file and passing to parser: "+ file.getName());
	
	                    if(is != null)
	                    {
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
		
		                        saveExecutionResult(dataFile, null, dataFile, file.getName(), jobData, true, "Data File is already processed. File Name: " + file.getName(), 0, 0, 0);
	
			                    new DataFileDao().updateDataFile(dataFile);
			                    
			                    try {
		                            is.close();
		                        } catch (IOException e) {
		                            e.printStackTrace();
		                        }
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
		                        dataFile.setFileRepoId(outboundRepo);
			                    new DataFileDao().addDataFile(dataFile);
			
			                    dataFile = new DataFileDao().getDataFileByName(file.getName());
			
			                    ResponseObj localResponse = processFile(is, jobData, file.getName(), dataFile);
			
			                    is.close();
			                    InputStream copyStream = new FileInputStream(file);
			                    
			                    DataFile dataFileProcessed = new DataFile();
		            			DataFile dataFileException = new DataFile();
		            			
			                    if (0 == localResponse.getCode()) {
			                       
			                    	if(localResponse.getCsvRowException() != null && localResponse.getCsvRowException().size() > 0)
		        					{
		        						//Split files into 2 CSV files
		        						splitCsvExceptionFiles(jobData.getProcessedFileRepo(), jobData.getExceptionFileRepo(), copyStream, file.getName(), localResponse);
		        					
		        						dataFileProcessed.setDataFileIsCsv(true);
		        						dataFileProcessed.setDataFileName("Partial - " + file.getName());
		        						dataFileProcessed.setFileRepoId(jobData.getProcessedFileRepo());
		        						new DataFileDao().addDataFile(dataFileProcessed);
		        												
		        						dataFileException.setDataFileIsCsv(true);
		        						dataFileException.setDataFileName("Failed - " + file.getName());
		        						dataFileException.setFileRepoId(jobData.getExceptionFileRepo());
		        						new DataFileDao().addDataFile(dataFileException);
		        					
		        						Integer processedRecords =  (localResponse.getCsvInsertValues() != null ? localResponse.getCsvInsertValues().size() : 0);
			        					Integer exceptionRecords =  (localResponse.getCsvRowException() != null ? localResponse.getCsvRowException().size() : 0);
			        					Integer totalRecords = processedRecords + exceptionRecords;
			        						
		    							dataFile.setFileRepoId(exceptionRepo);
		    							new DataFileDao().updateDataFile(dataFile);
		    							
		    							saveExecutionResult(dataFile, dataFileProcessed, dataFileException, file.getName(), jobData, true, "Some records could not be processed.", totalRecords, processedRecords, exceptionRecords);
		    							copyStream.close(); //Already used for Split
		    							
		    							copyStream = new FileInputStream(file); //Reload file					                    
		    							moveFileToRepo(exceptionRepo, copyStream, file.getName());	
		    						}
		    						else
		    						{
		    							Integer totalRecords = StringUtils.isNumeric(localResponse.getDetail().toString()) ? Integer.parseInt(localResponse.getDetail().toString()) : 0;
			                            saveExecutionResult(dataFile, dataFile, null, file.getName(), jobData, false, "", totalRecords, totalRecords, 0);
		    							moveFileToRepo(outboundRepo, copyStream, file.getName());					
		    						}
			                    	
			                    	// Send file to outbound
			                        //moveFileToRepo(outboundRepo, copyStream, file.getName());
			                        file.delete();
			
			                        //dataFile.setFileRepoId(outboundRepo);
			                        //Integer totalRecords = StringUtils.isNumeric(localResponse.getDetail().toString()) ? Integer.parseInt(localResponse.getDetail().toString()) : 0;
			                        //saveExecutionResult(dataFile, file.getName(), jobData, false, "", totalRecords);
			                        System.out.println("Removed file from local server");
			                        
			                        //Getting data refreshed into replication tables
		                            Integer experimentId = jobData.getExperiment().getExpId();
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
	
			                    } else {
			                        // Send file to Exception
			                        moveFileToRepo(exceptionRepo, copyStream, file.getName());
			                        file.delete();
			
			                        dataFile.setFileRepoId(exceptionRepo);
			                        saveExecutionResult(dataFile, null, dataFile, file.getName(), jobData, true, localResponse.getDescription(), 0, 0, 0);
				                    System.out.println("Removed file from local server");
			                    }
			
			                    copyStream.close();
			                    new DataFileDao().updateDataFile(dataFile);
			                    System.out.println("Data File Updated. Looking for new file");
				                
		                    }
	                    }	                    
	                } catch (Exception e) {
	                    System.out.println("Error processing local file: "+file.getName());
	                }
	
	            }
            	else
                    System.out.println("File " + file.getName() + " does not match criteria for CSV Template: " + jobData.getCsvTemplateName());
            		 
            }
            if(!filesProcessed)
            {
            	saveExecutionResult(null, null, null, null, jobData, true, "There are no files matching criteria for CSV Template: " + jobData.getCsvTemplateName(), 0, 0, 0);                
            }	
        }
        
        System.out.println("JOB Execution finished. CSV Template: " + jobData.getCsvTemplateName() + "  " +new Date());
        
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

            if(responseObj.getCode() != 0 || responseObj.getCsvInsertValues() == null || responseObj.getCsvInsertValues().size() <=0)
            	return responseObj;
                        
            //2)Batch Insert
            ResponseObj batchResponse = new BatchExperimentRecordsInsertDao().executeExperimentBatchRecordsInsert(responseObj.getCsvInsertColumns(),
                    responseObj.getCsvInsertValues(), null, csvTemplate, null, dataFile, csvTemplate.getExperiment(), iBatchSize);

            System.out.println("Filename : "+filename+" - "+responseObj.getDetail());

            if (responseObj.getCode() == 0) {
                System.out.println("Successfully parsed file: "+filename);

            } else {
                sendTransferError(csvTemplate, filename);
            }

            if(responseObj.getCsvRowException() != null && responseObj.getCsvRowException().size() > 0)
			{
            	batchResponse.setCsvInsertValues(responseObj.getCsvInsertValues());
            	batchResponse.setCsvRowException(responseObj.getCsvRowException());
            	batchResponse.setCsvRowExceptionDetails(responseObj.getCsvRowExceptionDetails());
			}
            
            return batchResponse;

        } catch (Exception ex) {
        	
            System.out.println("Error parsing file: "+filename);
            ex.printStackTrace();
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

    private void saveExecutionResult(DataFile originalDataFile, DataFile processedDataFile, DataFile exceptionDataFile, String fileName, CsvTemplate csvTemplate, boolean exception, String exceptionDetails, Integer totalRecords, Integer processedRecords, Integer exceptionRecords)
    {
        CsvDataLoadExecutionResult csvDataLoadExecResult = new CsvDataLoadExecutionResult();
        csvDataLoadExecResult.setDataFile(originalDataFile);
        csvDataLoadExecResult.setDataFileProcessed(processedDataFile);
        csvDataLoadExecResult.setDataFileException(exceptionDataFile);        
        csvDataLoadExecResult.setCsvDataLoadExecException(exception);
        csvDataLoadExecResult.setCsvDataLoadExecExeptionDetails(exceptionDetails);
        csvDataLoadExecResult.setCsvDataLoadExecTime(new Date());
        csvDataLoadExecResult.setCsvDataLoadTotalRecords(totalRecords);
        csvDataLoadExecResult.setCsvDataLoadTotalRecordsException(exceptionRecords);
        csvDataLoadExecResult.setCsvDataLoadTotalRecordsProcessed(processedRecords);
        csvDataLoadExecResult.setCsvTemplate(csvTemplate);
        new CsvDataLoadExecutionResultDao().addCsvDataLoadExecutionResult(csvDataLoadExecResult);
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
