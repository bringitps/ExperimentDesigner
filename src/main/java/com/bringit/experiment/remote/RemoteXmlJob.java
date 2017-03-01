package com.bringit.experiment.remote;

import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.XmlTemplate;

import com.bringit.experiment.data.ExperimentParser2;
import com.bringit.experiment.data.ResponseObj;
import com.bringit.experiment.util.FTPUtil;

import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.quartz.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by msilay on 2/22/17.
 */
public class RemoteXmlJob implements Job {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // pull the JobDetails to get the type of Remote Connection.
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        XmlTemplate jobData = (XmlTemplate) jobDataMap.get("jobData");

        FilesRepository filesRepository = jobData.getInboundFileRepo();
        FilesRepository outboundRepo = jobData.getExceptionFileRepo();
        FilesRepository exceptionRepo = jobData.getExceptionFileRepo();

        if (filesRepository.isFileRepoIsSftp()) {
            FTPUtil sftp = new FTPUtil(filesRepository.getFileRepoHost(), Integer.parseInt(filesRepository.getFileRepoPort()),
                    filesRepository.getFileRepoUser(), filesRepository.getFileRepoPass());

            List<ChannelSftp.LsEntry> files =  sftp.secureGetFileList(filesRepository.getFileRepoPath());
            for (ChannelSftp.LsEntry file: files) {

                if (file.getFilename().endsWith(".xml")) {
                    InputStream is = sftp.secureGetFile(filesRepository.getFileRepoPath()+"/"+file.getFilename());
                    if (is != null) {
                        System.out.println("Received Input file and passing to parser: "+file.getFilename());
                        boolean isSuccess = processFile(is, jobData, file.getFilename());
                        if (isSuccess) {
                            // Send file to outbound
                            moveFileToRepo(outboundRepo, is, file.getFilename());
                            sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());
                            System.out.println("Removed file from SFTP server");
                        } else {
                            // Send file to Exception
                            moveFileToRepo(exceptionRepo, is, file.getFilename());
                            sftp.secureRemoveFile(filesRepository.getFileRepoPath(), file.getFilename());
                            System.out.println("Removed file from SFTP server");
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

                if (ftpFile.getName().endsWith(".xml")) {

                    InputStream is = ftp.simpleGetFile(filesRepository.getFileRepoPath(), ftpFile.getName());
                    if (is != null) {
                        System.out.println("Filename : "+ftpFile.getName());
                        boolean isSuccess = processFile(is, jobData, ftpFile.getName());
                        if (isSuccess) {
                            // Send file to outbound
                            moveFileToRepo(outboundRepo, is, ftpFile.getName());
                            ftp.deleteFile(filesRepository.getFileRepoPath(), ftpFile.getName());
                            System.out.println("Removed file from FTP server");
                        } else {
                            // Send file to Exception
                            moveFileToRepo(exceptionRepo, is, ftpFile.getName());
                            ftp.deleteFile(filesRepository.getFileRepoPath(), ftpFile.getName());
                            System.out.println("Removed file from FTP server");
                        }
                    } else {
                        sendTransferError(jobData, ftpFile.getName());
                    }
                }
            }

        } else if (filesRepository.isFileRepoIsLocal()) {
            File dir = new File(filesRepository.getFileRepoPath());
            String[] extensions = new String[] { "xml" };
            System.out.println("Getting all .txt and .jsp files in " + filesRepository.getFileRepoPath()
                    + " including those in subdirectories");
            List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, false);
            for (File file: files) {

                try {
                    InputStream is = new FileInputStream(file);
                    System.out.println("Reading input file and passing to parser: "+ file.getName());
                    boolean isSuccess = processFile(is, jobData, file.getName());
                    if (isSuccess) {
                        // Send file to outbound
                        moveFileToRepo(outboundRepo, is, file.getName());
                        file.delete();
                        System.out.println("Removed file from local server");
                    } else {
                        // Send file to Exception
                        moveFileToRepo(exceptionRepo, is, file.getName());
                        file.delete();
                        System.out.println("Removed file from local server");
                    }
                } catch (Exception e) {
                    System.out.println("Error processing local file: "+file.getName());
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

    private boolean processFile(InputStream is, XmlTemplate xmlTemplate, String filename) {
        boolean isSuccess = false;
        try {

            ExperimentParser2 parser = new ExperimentParser2();
            ResponseObj responseObj = parser.parseXML(is, filename);
            System.out.println("Filename : "+filename+" - "+responseObj.getDetail());
            if (responseObj.getCode() == 0) {
                System.out.println("Successfully parsed file: "+filename);
                isSuccess = true;
            } else {
                sendTransferError(xmlTemplate, filename);
                isSuccess = false;
            }

        } catch (Exception ex) {
            System.out.println("Error parsing file: "+filename);
            sendTransferError(xmlTemplate, filename);
            isSuccess = false;
        }

        return isSuccess;
    }

    public void sendTransferError(XmlTemplate xmlTemplate, String filename) {
        try {
            // TODO: replace this with the email logic when an error occurs.
            System.out.println("Error processing file: "+filename);
        } catch(Exception ex) {
            System.out.println("Error sending ERROR email: "+ex);
        }

    }
}