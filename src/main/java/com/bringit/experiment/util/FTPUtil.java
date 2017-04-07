package com.bringit.experiment.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.commons.net.ftp.FTP;
import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * Created with IntelliJ IDEA.
 * User: msilay
 * Date: 9/25/13
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class FTPUtil {

    private String ftpHost;
    private int ftpPort;
    private String ftpUserName;
    private String ftpPassword;

    public FTPUtil(String host, int port, String userName, String password) {
        ftpHost = host;
        ftpPort = port;
        ftpUserName = userName;
        ftpPassword = password;
    }

    /**
     * Sftp put a file on the remote directory.  This will lookup the file path on
     * the local system.
     *
     * @param remoteDir String the directory on the ftp server
     * @param filePath  String path to the file on local system.
     * @return 0 for success 1 for failure
     */
    public boolean secureSendFile(String remoteDir, String filePath) {
        try {
            File f = new File(filePath);
            System.out.println("Storing file as remote filename: " + f.getName());
            return secureSend(remoteDir, new FileInputStream(f), f.getName());
        } catch (Exception et) {
            System.out.println("Storing remote file failed. " + et.toString());
        }
        return false;
    }

    /**
     * Securely FTP a file to the remote directory.
     *
     * @param remoteDir String the directory on the ftp server
     * @param input     InputStream to put
     * @param fileName  String filename
     * @return boolean success or failure
     */
    public boolean secureSend(String remoteDir, InputStream input, String fileName) {
        boolean success = true;
        try {
            ChannelSftp c = getSecureChannel();

            /**
             * Change to the remote directory
             */
            System.out.println("Changing to FTP remote dir: " + remoteDir);
            c.cd(remoteDir);

            /**
             * Send the file we generated
             */
            try {
                c.put(input, fileName);
            } catch (Exception et) {
                System.out.println("Storing remote file failed. " + et.toString());
                success = false;
            }

            /**
             * Disconnect from the FTP server
             */
            try {
                c.quit();
            } catch (Exception exc) {
                System.out.println("Unable to disconnect from FTP server. " + exc.toString());
            }
        } catch (Exception ex) {
            System.out.println("Unable to do a secure send: " + ex);
            success = false;
        }
        return success;
    }

    /**
     * This will delete a file relative to the remote directory.  If you pass
     * the file path it should start after the remote directory.
     * <p/>
     * For example: filename = filename
     * Actual Remote Path = //ftpRemoteDirectory//folder//filename
     *
     * @param dir      String directory path on remote ftp server.
     * @param filename String path relative to the default remote directory.
     * @return boolean for success or failure
     */
    public boolean secureRemoveFile(String dir, String filename) {
        boolean success;
        try {
            ChannelSftp c = getSecureChannel();
            try {
                c.cd(dir);
                c.rm(filename);
                success = true;
            } catch (Exception e) {
                System.out.println("Error during secure remove: " + e);
                success = false;
            }

            try {
                c.quit();
            } catch (Exception exc) {
                System.out.println("Unable to disconnect from FTP server. " + exc.toString());
            }
        } catch (Exception ex) {
            System.out.println("Error during get file: " + ex);
            success = false;
        }
        return success;
    }

    /**
     * Get an ArrayList of all files in the specified directory.  The path is
     * relative to the Remote directory.
     *
     * @param path String path relative to the class level remote directory.
     * @return ArrayList<ChannelSftp.LsEntry> a list of LsEntry objects excluding "." and ".."
     */
    public ArrayList<ChannelSftp.LsEntry> secureGetFileList(String path) {
        ArrayList<ChannelSftp.LsEntry> file_list = new ArrayList<ChannelSftp.LsEntry>();
        try {
            ChannelSftp c = getSecureChannel();
            /**
             * Get the list of files in the remote server directory
             */
            Vector files = c.ls(path);
            if (files.size() == 0) {
                System.out.println("No files are available for download.");
            } else { //Otherwise download all files except for the . and .. entries
                for (Object file : files) {
                    ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) file;
                    SftpATTRS attrs = lsEntry.getAttrs();

                    if (!lsEntry.getFilename().equals(".") && !lsEntry.getFilename().equals("..")) {
                        if (!attrs.isDir()) {
                            String filename = lsEntry.getFilename();

                            System.out.println("Adding file " + lsEntry.getFilename());
                            file_list.add(lsEntry);
                        }
                    }
                }
            }

            /**
             * Disconnect from the FTP server
             */
            try {
                c.quit();
            } catch (Exception exc) {
                System.out.println("Unable to disconnect from FTP server. " + exc.toString());
            }
        } catch (Exception exc) {
            System.out.println("Unable to disconnect from FTP server. " + exc.toString());
        }
        return file_list;
    }

    /**
     * Get the specified file from the Remote FTP server.  This file path will be relative
     * to the Remote Directory and must contain the filename.
     *
     * @param filePath  Sting path relative to the Remote Directory.
     * @param localPath String path including file name where it will be stored locally.
     */
    public void secureGetFile(String filePath, String localPath) {
        try {
            ChannelSftp c = getSecureChannel();

            try {
                File f = new File(localPath);
                c.get(filePath, new FileOutputStream(f));
            } catch (Exception ex) {
                System.out.println("Error during get file: " + ex);

            }

            try {
                c.quit();
            } catch (Exception exc) {
                System.out.println("Unable to disconnect from FTP server. " + exc.toString());
            }
        } catch (Exception e) {
            System.out.println("Exception during secure put: " + e);
        }
    }

    /**
     * Get the specified file from the Remote FTP server.  This file path will be relative
     * to the Remote Directory and must contain the filename.
     *
     * @param filePath Sting path relative to the Remote Directory.
     * @return InputStream file
     */
    public InputStream secureGetFile(String filePath) {
        InputStream input = null;
        try {
            ChannelSftp c = getSecureChannel();

            try {
                input = c.get(filePath);
            } catch (Exception ex) {
                System.out.println("Error during get file: " + ex);
            }

            try {
                c.quit();
            } catch (Exception exc) {
                System.out.println("Unable to disconnect from FTP server. " + exc.toString());
            }
        } catch (Exception e) {
            System.out.println("Exception during secure put: " + e);
        }
        return input;
    }
    
    public boolean simpleSendFile(String directory, InputStream is, String fileName) {
        FTPClient ftp = new FTPClient();

        try {
            ftp.connect(ftpHost);
            System.out.println("Connected to " + ftpHost + ".");
            System.out.println("Dir: "+directory+ ", Name: "+fileName);
            // After connection attempt, you should check the reply code to verify
            // success.
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("FTP server refused connection.");
            }

            if (!ftp.login(ftpUserName, ftpPassword)) {
                ftp.logout();
                System.out.println("FTP server not logged in.");
            }
            System.out.println("Remote system is " + ftp.getSystemName());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
            ftp.setControlKeepAliveTimeout(600);
            ftp.changeWorkingDirectory(directory);
            ftp.setUseEPSVwithIPv4(true);
            ftp.setRemoteVerificationEnabled(false);
            boolean didStore = true;
            
            try
            {
            	didStore = ftp.storeFile(fileName, is);
            }
            catch(Exception ex1)
            {
            	try
                {
            		
            		ftp.enterLocalActiveMode();
                    ftp.setControlKeepAliveTimeout(600);
                    ftp.changeWorkingDirectory(directory);
                    System.out.println("Trying in Active Mode");
                	
                	didStore = ftp.storeFile(fileName, is);
                }
                catch(Exception ex2)
                {
                	System.out.println("Full Issue info: ");
                	ex2.printStackTrace();
                	didStore = false;
                }
            }
            
            System.out.println("Did Store: "+didStore);
            is.close();
            ftp.logout();
            return true;
        } catch (Exception e) {
            System.out.println("Unable to do simple send FTP file. "+ e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (Exception ex) {
                    System.out.println("Unable to close connection in finally of simple send. "+ ex);
                }
            }
        }
        return false;
    }

    public boolean simpleSendFile(String directory, String filePath, String fileName) {
        FTPClient ftp = new FTPClient();
        InputStream input;
        try {
            ftp.connect(ftpHost);
            System.out.println("Connected to " + ftpHost + ".");
            System.out.println("Dir: "+directory+ ", Path: "+filePath+ ", Name: "+fileName);
            // After connection attempt, you should check the reply code to verify
            // success.
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("FTP server refused connection.");
            }

            if (!ftp.login(ftpUserName, ftpPassword)) {
                ftp.logout();
                System.out.println("FTP server not logged in.");
            }
            System.out.println("Remote system is " + ftp.getSystemName());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
            ftp.setControlKeepAliveTimeout(600);
            ftp.changeWorkingDirectory(directory);
            ftp.setUseEPSVwithIPv4(true);
            ftp.setRemoteVerificationEnabled(false);
            input = new FileInputStream(filePath);
            
            boolean didStore = true;
            
            try
            {
            	didStore = ftp.storeFile(fileName, input);
            }
            catch(Exception ex1)
            {
            	try
                {
            		
            		ftp.enterLocalActiveMode();
                    ftp.setControlKeepAliveTimeout(600);
                    ftp.changeWorkingDirectory(directory);
                    System.out.println("Trying in Active Mode");
                	didStore = ftp.storeFile(fileName, input);
                }
                catch(Exception ex2)
                {
                	System.out.println("Full Issue info: ");
                	ex2.printStackTrace();
                	didStore = false;
                }
            }
            
            System.out.println("Did Store: "+didStore);
            input.close();
            ftp.logout();
            return true;
        } catch (Exception e) {
            System.out.println("Unable to do simple send FTP file. "+ e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (Exception ex) {
                    System.out.println("Unable to close connection in finally of simple send. "+ ex);
                }
            }
        }
        return false;
    }

    public FTPFile[] simpleGetFileList(String directory) {
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(ftpHost);
            System.out.println("Connected to " + ftpHost + ".");

            // After connection attempt, you should check the reply code to verify
            // success.
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("FTP server refused connection.");
            }

            if (!ftp.login(ftpUserName, ftpPassword)) {
                ftp.logout();
                System.out.println("FTP server not logged in.");
            }

            System.out.println("Remote system is " + ftp.getSystemName());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

        	// Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(directory);
            ftp.setControlKeepAliveTimeout(600);
            ftp.setRemoteVerificationEnabled(false);
            ftp.setUseEPSVwithIPv4(true);
            try
            {
                return ftp.listFiles();
            }	
            catch(Exception ex1)
            {
            	try
                {
            		ftp.enterLocalActiveMode();
                    ftp.setControlKeepAliveTimeout(600);
                    ftp.changeWorkingDirectory(directory);
                    return ftp.listFiles();
                }
                catch(Exception ex2)
                {
                }
            }

        } catch (Exception e) {
            System.out.println("Unable to get file list: " + e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (Exception ex) {
                    System.out.println("Unable to close connection in finally of simple send. "+ex);
                }
            }
        }

        return null;
    }

    public boolean simpleGetFile(String directory, String filename, FileOutputStream fos) {
        System.out.println("looking for: " + filename + " in " + directory);
        FTPClient ftp = new FTPClient();
        boolean fStream = false;
        try {

            ftp.connect(ftpHost);
            System.out.println("Connected to " + ftpHost + ".");

            // After connection attempt, you should check the reply code to verify
            // success.
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("FTP server refused connection.");
            }

            if (!ftp.login(ftpUserName, ftpPassword)) {
                ftp.logout();
                System.out.println("FTP server not logged in.");
            }

            System.out.println("Remote system is " + ftp.getSystemName());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(directory);
            ftp.setRemoteVerificationEnabled(false);
            ftp.setUseEPSVwithIPv4(true);
            try
            {
                ftp.retrieveFile(filename, fos);
            }
            catch(Exception ex1)
            {
            	try
                {
            		ftp.enterLocalActiveMode();
                    ftp.setControlKeepAliveTimeout(600);
                    ftp.changeWorkingDirectory(directory);
                    ftp.retrieveFile(filename, fos);
                }
                catch(Exception ex2)
                {
                }
            }
            
            fStream = true;
            //ftp.logout();
        } catch (Exception e) {
            System.out.println("Unable to perform simple connect: " + e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                }
                catch (IOException f) {
                    System.out.println("Unable to close connection in finally of simple get File. "+ f);
                } catch (Exception e) {
                    System.out.println("Unable to close connection in finally of simple get File. "+ e);
                }
            }
        }
        return fStream;
    }

    public InputStream simpleGetFile(String directory, String filename) {
        System.out.println("looking for: " + filename + " in " + directory);
        FTPClient ftp = new FTPClient();
        InputStream fStream = null;
        try {

            ftp.connect(ftpHost);
            System.out.println("Connected to " + ftpHost + ".");

            // After connection attempt, you should check the reply code to verify
            // success.
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("FTP server refused connection.");
            }

            if (!ftp.login(ftpUserName, ftpPassword)) {
                ftp.logout();
                System.out.println("FTP server not logged in.");
            }

            System.out.println("Remote system is " + ftp.getSystemName());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(directory);
            ftp.setRemoteVerificationEnabled(false);
            ftp.setUseEPSVwithIPv4(true);
            // Changed to fix unreliable behavior

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            try
            {
                ftp.retrieveFile(filename, outputStream);
            }
            catch(Exception ex1)
            {
            	try
                {
            		ftp.enterLocalActiveMode();
                    ftp.setControlKeepAliveTimeout(600);
                    ftp.changeWorkingDirectory(directory);
                    ftp.retrieveFile(filename, outputStream);
                }
                catch(Exception ex2)
                {
                }
            }
            
            
            //boolean cpc = ftp.completePendingCommand();
            //System.out.println("complete pending command: "+cpc);

            fStream = new ByteArrayInputStream(outputStream.toByteArray());

            outputStream.close();

            //fStream = ftp.retrieveFileStream(filename);
            return fStream;
            //ftp.logout();
        } catch (Exception e) {
            System.out.println("Unable to perform simple connect: " + e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                }
                catch (IOException f) {
                    System.out.println("Unable to close connection in finally of simple get file. "+ f);
                } catch (Exception e) {
                    System.out.println("Unable to close connection in finally of simple get file. "+ e);
                }
            }
        }
        return fStream;
    }

    public boolean deleteFile(String filename, String directory) {
        System.out.println("looking for: " + filename + " in " + directory);
        FTPClient ftp = new FTPClient();

        try {

            ftp.connect(ftpHost);
            System.out.println("Connected to " + ftpHost + ".");

            // After connection attempt, you should check the reply code to verify
            // success.
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("FTP server refused connection.");
            }

            if (!ftp.login(ftpUserName, ftpPassword)) {
                ftp.logout();
                System.out.println("FTP server not logged in.");
            }

            System.out.println("Remote system is " + ftp.getSystemName());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(directory);
            ftp.setRemoteVerificationEnabled(false);
            ftp.setUseEPSVwithIPv4(true);
            boolean didmove = true;
            
            try
            {
            	didmove =  ftp.deleteFile(filename);
            }
            catch(Exception ex1)
            {
            	try
                {
            		ftp.enterLocalActiveMode();
                    ftp.setControlKeepAliveTimeout(600);
                    ftp.changeWorkingDirectory(directory);
                    didmove =  ftp.deleteFile(filename);
                }
                catch(Exception ex2)
                {
                	didmove = false;
                }
            }
            
            
            System.out.println("did delete: "+didmove);
            return didmove;
            //ftp.logout();
        } catch (Exception e) {
            System.out.println("Unable to perform simple connect: " + e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                }
                catch (IOException f) {
                    System.out.println("Unable to close connection in finally of simple get file. "+ f);
                } catch (Exception e) {
                    System.out.println("Unable to close connection in finally of simple get file. "+ e);
                }
            }
        }
        return false;
    }

    public boolean moveFile(String filename, String directory, InputStream is) {
        System.out.println("looking for: " + filename + " in " + directory);
        FTPClient ftp = new FTPClient();

        try {

            ftp.connect(ftpHost);
            System.out.println("Connected to " + ftpHost + ".");

            // After connection attempt, you should check the reply code to verify
            // success.
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("FTP server refused connection.");
            }

            if (!ftp.login(ftpUserName, ftpPassword)) {
                ftp.logout();
                System.out.println("FTP server not logged in.");
            }

            System.out.println("Remote system is " + ftp.getSystemName());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(directory);
            ftp.setControlKeepAliveTimeout(600);
            ftp.setRemoteVerificationEnabled(false);
            ftp.setUseEPSVwithIPv4(true);
            boolean didmove = true;
            
            try
            {
            	didmove =  ftp.storeFile(filename, is);
            }	
            catch(Exception ex1)
            {
            	try
                {
            		ftp.enterLocalActiveMode();
                    ftp.setControlKeepAliveTimeout(600);
                    ftp.changeWorkingDirectory(directory);
                    didmove =  ftp.storeFile(filename, is);
                }
                catch(Exception ex2)
                {
                	didmove = false;
                }
            }
            
            System.out.println("did move: "+didmove);
            return didmove;
            //ftp.logout();
        } catch (Exception e) {
            System.out.println("Unable to perform simple connect: " + e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                }
                catch (IOException f) {
                    System.out.println("Unable to close connection in finally of simple get file. "+ f);
                } catch (Exception e) {
                    System.out.println("Unable to close connection in finally of simple get file. "+ e);
                }
            }
        }
        return false;
    }

    private ChannelSftp getSecureChannel
            () {
        try {
            JSch jsch = new JSch();
            Session session;
            Channel channel;

            //Create a session sending through our username and password
            session = jsch.getSession(ftpUserName, ftpHost, ftpPort);
            System.out.println("Session created.");
            session.setPassword(ftpPassword);
            //Security.addProvider(new com.sun.crypto.provider.SunJCE());

            //
            //Setup Strict HostKeyChecking to no so we dont get the
            //unknown host key exception
            //
            java.util.Properties config = new java.util.Properties();
            
            //Line Added to avoid "Algoritm Negotiation Fail Exception"
            config.put("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
            
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("Session connected.");

            //
            //Open the SFTP channel
            //
            System.out.println("Opening Channel.");
            channel = session.openChannel("sftp");
            channel.connect(30000);
            return (ChannelSftp) channel;

        } catch (Exception e) {
            System.out.println("Unable to create secure Channel: " + e);
        }

        return null;
    }
    

    private ChannelSftp getSecureChannel(int timeout) 
    {
    	
    	try {
    		JSch jsch = new JSch();
		    Session session;
		    Channel channel;
		    //Create a session sending through our username and password
		    session = jsch.getSession(ftpUserName, ftpHost, ftpPort);
		    session.setTimeout(timeout);
		    System.out.println("Session created.");
		    session.setPassword(ftpPassword);
		    //Security.addProvider(new com.sun.crypto.provider.SunJCE());
		
		    //
		    //Setup Strict HostKeyChecking to no so we dont get the
		    //unknown host key exception
		    //
		    java.util.Properties config = new java.util.Properties();
		    
		    //Line Added to avoid "Algoritm Negotiation Fail Exception"
		    config.put("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
		    
		    config.put("StrictHostKeyChecking", "no");
		    session.setConfig(config);
		    session.connect();
		    System.out.println("Session connected.");
		
		    //
		    //Open the SFTP channel
		    //
		    System.out.println("Opening Channel.");
		    channel = session.openChannel("sftp");
		    channel.connect(timeout);
		    return (ChannelSftp) channel;

    	} catch (Exception e) {
    		System.out.println("Unable to create secure Channel: " + e);
    	}

    	return null;
    }
    

    public boolean checkConnection()
    {
		FTPClient ftp = new FTPClient();
		try 
		{
			ftp.setConnectTimeout(30000);
		    ftp.connect(ftpHost);
		    
		    int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) 
                return false;
            
            boolean success = ftp.login(ftpUserName, ftpPassword);
            if (!success) 
                return false;
            
            ftp.logout();
        }
		catch(Exception e)
		{
			 return false;
		}
		
        return true;
    }
    
    public boolean secureCheckConnection()
    {
    	try {
            ChannelSftp c = getSecureChannel(10000);
            if(c==null)
            	return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean checkDirectoryExists(String directory)
    {
		FTPClient ftp = new FTPClient();
		
		try 
		{
			ftp.setConnectTimeout(30000);
		    ftp.connect(ftpHost);
		    
		    int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) 
                return false;
            

            boolean success = ftp.login(ftpUserName, ftpPassword);
            if (!success) 
                return false;
            
            
		    ftp.changeWorkingDirectory(directory);
            ftp.setRemoteVerificationEnabled(false);
		    ftp.setUseEPSVwithIPv4(true);
		    
		    int returnCode = ftp.getReplyCode();
		    
			if (returnCode == 550) 
			{
	            ftp.logout();
				return false;
			}

            ftp.logout();
		}
		catch(Exception e)
		{
			 return false;
		}
        return true;
    }
    
    
    public boolean secureCheckDirectoryExists(String directory)
    {
        ChannelSftp c = getSecureChannel(10000);
        if(c == null)
        	return false;
        
    	try {
			SftpATTRS attrs = c.lstat(directory);
			return true;
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
}

