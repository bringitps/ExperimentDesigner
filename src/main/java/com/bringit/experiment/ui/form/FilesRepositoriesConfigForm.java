package com.bringit.experiment.ui.form;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FilesRepositoryDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.ui.design.FilesRepositoriesConfigDesign;
import com.bringit.experiment.util.FTPUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class FilesRepositoriesConfigForm extends FilesRepositoriesConfigDesign {

	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();
	
	public FilesRepositoriesConfigForm()
	{
		loadTblData();
		/*
		tblFilesRepository.setCellStyleGenerator(new Table.CellStyleGenerator() {
		       @Override
				public String getStyle(Table source, Object itemId, Object propertyId) {
					// TODO Auto-generated method stub
					return "tiny";
				}
		    });
		*/
		this.btnAddFileRepo.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addFileRepoRow();
			}

		});
	
		this.btnDeleteFileRepo.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deleteFileRepoRow();
			}

		});
		
		this.btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				saveFileRepoRows();
			}

		});

		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				Filterable filter= (Filterable) (tblFilesRepository.getContainerDataSource());
                filter.removeAllContainerFilters();

                String filterString = txtSearch.getValue();
                Like filterLike = new Like(cbxFilesRepositoriesFilters.getValue().toString(), "%" + filterString + "%");
                filterLike.setCaseSensitive(false);
                
                if (filterString.length() > 0 && !cbxFilesRepositoriesFilters.getValue().toString().isEmpty()) {
                    filter.addContainerFilter(filterLike);
                }
			}
			});
		

		btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				 loadTblData();
			}

		});
		
	}
	
	private void loadTblData()
	{
		dbIdOfItemsToDelete = new ArrayList<Integer>();
		
		this.tblFilesRepository.setContainerDataSource(null);
		this.tblFilesRepository.addContainerProperty("*", CheckBox.class, null);
		this.tblFilesRepository.addContainerProperty("Name", TextField.class, null);
		this.tblFilesRepository.addContainerProperty("Is Local", CheckBox.class, null);
		this.tblFilesRepository.addContainerProperty("Is FTP", CheckBox.class, null);
		this.tblFilesRepository.addContainerProperty("Is SFTP", CheckBox.class, null);
		this.tblFilesRepository.addContainerProperty("Path", TextField.class, null);
		this.tblFilesRepository.addContainerProperty("Host", TextField.class, null);
		this.tblFilesRepository.addContainerProperty("Port", TextField.class, null);
		this.tblFilesRepository.addContainerProperty("User", TextField.class, null);
		this.tblFilesRepository.addContainerProperty("Pass", PasswordField.class, null);
		tblFilesRepository.setColumnWidth("*", 20);
		this.tblFilesRepository.setEditable(true);
		this.tblFilesRepository.setPageLength(100);
		
		cbxFilesRepositoriesFilters.addItem("Name");
		cbxFilesRepositoriesFilters.addItem("Path");
		cbxFilesRepositoriesFilters.addItem("Host");
		cbxFilesRepositoriesFilters.addItem("User");
		cbxFilesRepositoriesFilters.select("Path");
		
		Object[] itemValues = new Object[10];

		List<FilesRepository> filesRepositories = new FilesRepositoryDao().getAllFilesRepositorys();
		
		for(int i=0; filesRepositories != null && i<filesRepositories.size(); i++)
		{
			CheckBox chxSelect = new CheckBox();
			chxSelect.setVisible(false);
			itemValues[0] = chxSelect;
			
			TextField txtFileRepoName = new TextField();
			txtFileRepoName.setStyleName("tiny");
			txtFileRepoName.setValue(filesRepositories.get(i).getFileRepoName());		
			txtFileRepoName.setWidth(97, Unit.PERCENTAGE);
			itemValues[1] = txtFileRepoName;
			
			CheckBox chxIsLocal = new CheckBox();
			chxIsLocal.setValue(filesRepositories.get(i).isFileRepoIsLocal());
			itemValues[2] = chxIsLocal;
			
			CheckBox chxIsFtp = new CheckBox();
			chxIsFtp.setValue(filesRepositories.get(i).isFileRepoIsFtp());
			itemValues[3] = chxIsFtp;
			
			CheckBox chxIsSftp = new CheckBox();
			chxIsSftp.setValue(filesRepositories.get(i).isFileRepoIsSftp());
			itemValues[4] = chxIsSftp;
			
			TextField txtFileRepoPath = new TextField();
			txtFileRepoPath.setStyleName("tiny");
			txtFileRepoPath.setValue(filesRepositories.get(i).getFileRepoPath());		
			txtFileRepoPath.setWidth(97, Unit.PERCENTAGE);
			itemValues[5] = txtFileRepoPath;

			TextField txtFileRepoHost = new TextField();
			txtFileRepoHost.setStyleName("tiny");
			txtFileRepoHost.setValue(filesRepositories.get(i).getFileRepoHost());
			txtFileRepoHost.setWidth(97, Unit.PERCENTAGE);
			itemValues[6] = txtFileRepoHost;

			TextField txtFileRepoPort = new TextField();
			txtFileRepoPort.setStyleName("tiny");
			txtFileRepoPort.setValue(filesRepositories.get(i).getFileRepoPort());
			txtFileRepoPort.setWidth(97, Unit.PERCENTAGE);
			itemValues[7] = txtFileRepoPort;

			TextField txtFileRepoUser = new TextField();
			txtFileRepoUser.setStyleName("tiny");
			txtFileRepoUser.setValue(filesRepositories.get(i).getFileRepoUser());
			txtFileRepoUser.setWidth(97, Unit.PERCENTAGE);
			itemValues[8] = txtFileRepoUser;

			PasswordField txtFileRepoPass = new PasswordField();
			txtFileRepoPass.setStyleName("tiny");
			txtFileRepoPass.setValue(filesRepositories.get(i).getFileRepoPass());
			txtFileRepoPass.setWidth(97, Unit.PERCENTAGE);
			itemValues[9] = txtFileRepoPass;
			
			this.tblFilesRepository.addItem(itemValues, filesRepositories.get(i).getFileRepoId());			
		}
		
	}
	
	private void addFileRepoRow()
	{
		Object[] itemValues = new Object[10];

		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		itemValues[0] = chxSelect;

		TextField txtFileRepoName = new TextField();
		txtFileRepoName.setStyleName("tiny");
		txtFileRepoName.setValue("");
		itemValues[1] = txtFileRepoName;
		
		CheckBox chxIsLocal = new CheckBox();
		itemValues[2] = chxIsLocal;
		
		CheckBox chxIsFtp = new CheckBox();
		itemValues[3] = chxIsFtp;
		
		CheckBox chxIsSftp = new CheckBox();
		itemValues[4] = chxIsSftp;
		
		TextField txtFileRepoPath = new TextField();
		txtFileRepoPath.setStyleName("tiny");
		txtFileRepoPath.setValue("");
		itemValues[5] = txtFileRepoPath;

		TextField txtFileRepoHost = new TextField();
		txtFileRepoHost.setStyleName("tiny");
		txtFileRepoHost.setValue("");
		itemValues[6] = txtFileRepoHost;

		TextField txtFileRepoPort = new TextField();
		txtFileRepoPort.setStyleName("tiny");
		txtFileRepoPort.setValue("");
		itemValues[7] = txtFileRepoPort;

		TextField txtFileRepoUser = new TextField();
		txtFileRepoUser.setStyleName("tiny");
		txtFileRepoUser.setValue("");
		itemValues[8] = txtFileRepoUser;

		PasswordField txtFileRepoPass = new PasswordField();
		txtFileRepoPass.setStyleName("tiny");
		txtFileRepoPass.setValue("");
		itemValues[9] = txtFileRepoPass;
		
		/*
		itemValues[5] = "";
		itemValues[6] = "";
		itemValues[7] = "";
		itemValues[8] = "";
		itemValues[9] = "";
		*/
		this.lastNewItemId = this.lastNewItemId - 1;
		this.tblFilesRepository.addItem(itemValues, this.lastNewItemId);
		this.tblFilesRepository.select(this.lastNewItemId);
	}

	
	private void deleteFileRepoRow()
	{
		boolean hasXmlTemplateLinked = false;
		if(this.tblFilesRepository.getValue() != null && (int)this.tblFilesRepository.getValue() > 0 )
		{
			if(new XmlTemplateDao().getXmlTemplatesByFileRepoId((int)this.tblFilesRepository.getValue()).size() <= 0)
				dbIdOfItemsToDelete.add((int)this.tblFilesRepository.getValue());
			else
				hasXmlTemplateLinked = true;
		}
		
		if(!hasXmlTemplateLinked && this.tblFilesRepository.getValue() != null)
			this.tblFilesRepository.removeItem((int)this.tblFilesRepository.getValue());
		else if(hasXmlTemplateLinked)
			this.getUI().showNotification("Files Repository Record can not be deleted. \nThere are XML Templates linked.", Type.WARNING_MESSAGE);
	}
	
	private void saveFileRepoRows()
	{
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateDuplicatedNamesResult = validateDuplicatedNames();
		boolean validateUniqueFileRepoProtocolResult = validateUniqueFileRepoProtocol();
		boolean validateFileRepoConnectionResult = false;
		
		if(validateUniqueFileRepoProtocolResult)
			validateFileRepoConnectionResult = validateFileRepoConnection();
		
		if(validateRequiredFieldsResult && validateDuplicatedNamesResult && validateUniqueFileRepoProtocolResult && validateFileRepoConnectionResult)
		{
			FilesRepositoryDao filesRepoDao = new FilesRepositoryDao();
		
			//Delete Items in DB
			for(int i = 0; i<dbIdOfItemsToDelete.size(); i++)
				filesRepoDao.deleteFilesRepository((int)dbIdOfItemsToDelete.get(i));
		
			Collection itemIds = this.tblFilesRepository.getContainerDataSource().getItemIds();
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblFilesRepository.getContainerDataSource().getItem(itemId);
				
				FilesRepository filesRepository = new FilesRepository();
				filesRepository.setFileRepoName(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
				filesRepository.setFileRepoIsLocal(((CheckBox)(tblRowItem.getItemProperty("Is Local").getValue())).getValue());
				filesRepository.setFileRepoIsFtp(((CheckBox)(tblRowItem.getItemProperty("Is FTP").getValue())).getValue());
				filesRepository.setFileRepoIsSftp(((CheckBox)(tblRowItem.getItemProperty("Is SFTP").getValue())).getValue());
				filesRepository.setFileRepoPath(((TextField)(tblRowItem.getItemProperty("Path").getValue())).getValue());
				filesRepository.setFileRepoHost(((TextField)(tblRowItem.getItemProperty("Host").getValue())).getValue());
				filesRepository.setFileRepoPort(((TextField)(tblRowItem.getItemProperty("Port").getValue())).getValue());
				filesRepository.setFileRepoUser(((TextField)(tblRowItem.getItemProperty("User").getValue())).getValue());
				filesRepository.setFileRepoPass(((PasswordField)(tblRowItem.getItemProperty("Pass").getValue())).getValue());
								
				if(itemId > 0)
				{
					filesRepository.setFileRepoId(itemId);
					filesRepoDao.updateFilesRepository(filesRepository);
				}
				else
					filesRepoDao.addFilesRepository(filesRepository);
			}
			
			this.getUI().showNotification("Data Saved.", Type.HUMANIZED_MESSAGE);
			loadTblData();
		}
		else if(!validateRequiredFieldsResult)
			this.getUI().showNotification("Name must be set for New Files Repository Records", Type.WARNING_MESSAGE);
		else if(!validateDuplicatedNamesResult)
			this.getUI().showNotification("Name of File Repository can not be duplicated.", Type.WARNING_MESSAGE);
		else if(!validateUniqueFileRepoProtocolResult)
			this.getUI().showNotification("Only 1 communication protocol should be selected.", Type.WARNING_MESSAGE);
		else if(!validateFileRepoConnectionResult)
			this.getUI().showNotification("Invalid File Repository. Connection could not be established.", Type.WARNING_MESSAGE);			
	}
	
	private boolean validateRequiredFields()
	{	
		Collection itemIds = this.tblFilesRepository.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblFilesRepository.getContainerDataSource().getItem(itemId);
			
			if(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue().isEmpty())
			{
				tblFilesRepository.select(itemId);
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateDuplicatedNames()
	{
		List<String> fileRepoNames = new ArrayList<String>();
		
		Collection itemIds = this.tblFilesRepository.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblFilesRepository.getContainerDataSource().getItem(itemId);
			
			if(fileRepoNames.indexOf(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue()) >= 0)
			{
				tblFilesRepository.select(itemId);
				return false;
			}
			else
				fileRepoNames.add(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
		}
		
		return true;
	}	
	
	

	private boolean validateUniqueFileRepoProtocol()
	{
		List<String> fileRepoNames = new ArrayList<String>();
		
		Collection itemIds = this.tblFilesRepository.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblFilesRepository.getContainerDataSource().getItem(itemId);
			
			boolean isLocal = ((CheckBox)(tblRowItem.getItemProperty("Is Local").getValue())).getValue();
			boolean isFTP = ((CheckBox)(tblRowItem.getItemProperty("Is FTP").getValue())).getValue();
			boolean isSFTP = ((CheckBox)(tblRowItem.getItemProperty("Is SFTP").getValue())).getValue();
			
			if((isLocal && isFTP) || (isLocal && isSFTP) || (isFTP && isSFTP))
			{
				tblFilesRepository.select(itemId);
				return false;
			}						
		}
		
		return true;
	}
	
	private boolean validateFileRepoConnection()
	{
		List<String> fileRepoNames = new ArrayList<String>();
	
		Collection itemIds = this.tblFilesRepository.getContainerDataSource().getItemIds();
	
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblFilesRepository.getContainerDataSource().getItem(itemId);
		
			boolean isLocal = ((CheckBox)(tblRowItem.getItemProperty("Is Local").getValue())).getValue();
			boolean isFTP = ((CheckBox)(tblRowItem.getItemProperty("Is FTP").getValue())).getValue();
			boolean isSFTP = ((CheckBox)(tblRowItem.getItemProperty("Is SFTP").getValue())).getValue();
		
			if(isLocal)
			{
				String localPath = (((TextField)(tblRowItem.getItemProperty("Path").getValue())).getValue());
				File localFolder = new File(localPath);
				
				if (localFolder == null || !localFolder.exists() || !localFolder.isDirectory()) 
				{
					tblFilesRepository.select(itemId);
					return false;				
				}
				else
				{
					//Test Write-Delete Operations
					String fileExtension = "txt";
					String fileName = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), fileExtension);
					File newFile = new File(localPath, fileName);
					try {
						newFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						tblFilesRepository.select(itemId);
						return false;
					}
					
					
					try {
						if(!newFile.delete())
						{
							tblFilesRepository.select(itemId);
							return false;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						tblFilesRepository.select(itemId);
						return false;
					}
				}
			}	
			else if(isFTP || isSFTP)
			{
				String ftpPath = ((TextField)(tblRowItem.getItemProperty("Path").getValue())).getValue();
				String ftpHost = ((TextField)(tblRowItem.getItemProperty("Host").getValue())).getValue();
				String ftpPort = ((TextField)(tblRowItem.getItemProperty("Port").getValue())).getValue();
				String ftpUser = ((TextField)(tblRowItem.getItemProperty("User").getValue())).getValue();
				String ftpPass = ((PasswordField)(tblRowItem.getItemProperty("Pass").getValue())).getValue();
				
				FTPUtil ftp = new FTPUtil(ftpHost, Integer.parseInt(ftpPort),
						ftpUser, ftpPass);
				
				String fileExtension = "txt";
				String fileName = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), fileExtension);

				//Test Write-Delete Operations
				
				String fileText = "Testing connectivity.";
				InputStream is = new ByteArrayInputStream(fileText.getBytes(StandardCharsets.UTF_8));
				
				if(isFTP)
				{
					if(ftp.simpleSendFile(ftpPath, is, fileName))
					{
						if(!ftp.deleteFile(fileName, ftpPath))
							return false;
					}
					else 
						return false;
				}
				else
				{
					if(ftp.secureSend(ftpPath, is, fileName))
					{
						if(!ftp.secureRemoveFile(fileName, ftpPath))
							return false;
					}
					else 
						return false;
				}
				
			}
		}
	
		return true;
	}
}
