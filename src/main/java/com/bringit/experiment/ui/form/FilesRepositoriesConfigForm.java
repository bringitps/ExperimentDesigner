package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.UnitOfMeasure;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FilesRepositoryDao;
import com.bringit.experiment.dao.UnitOfMeasureDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.ui.design.FilesRepositoriesConfigDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class FilesRepositoriesConfigForm extends FilesRepositoriesConfigDesign {

	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();
	
	public FilesRepositoriesConfigForm()
	{
		loadTblData();
		
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
		
	}
	
	private void loadTblData()
	{
		this.tblFilesRepository.setContainerDataSource(null);
		this.tblFilesRepository.addContainerProperty("*", CheckBox.class, null);
		this.tblFilesRepository.addContainerProperty("Name", String.class, null);
		this.tblFilesRepository.addContainerProperty("Is Local", CheckBox.class, null);
		this.tblFilesRepository.addContainerProperty("Is FTP", CheckBox.class, null);
		this.tblFilesRepository.addContainerProperty("Is SFTP", CheckBox.class, null);
		this.tblFilesRepository.addContainerProperty("Path", String.class, null);
		this.tblFilesRepository.addContainerProperty("Host", String.class, null);
		this.tblFilesRepository.addContainerProperty("Port", String.class, null);
		this.tblFilesRepository.addContainerProperty("User", String.class, null);
		this.tblFilesRepository.addContainerProperty("Pass", String.class, null);
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

			itemValues[1] = filesRepositories.get(i).getFileRepoName();
			
			CheckBox chxIsLocal = new CheckBox();
			chxIsLocal.setValue(filesRepositories.get(i).isFileRepoIsLocal());
			itemValues[2] = chxIsLocal;
			
			CheckBox chxIsFtp = new CheckBox();
			chxIsFtp.setValue(filesRepositories.get(i).isFileRepoIsFtp());
			itemValues[3] = chxIsFtp;
			
			CheckBox chxIsSftp = new CheckBox();
			chxIsSftp.setValue(filesRepositories.get(i).isFileRepoIsSftp());
			itemValues[4] = chxIsSftp;
			
			itemValues[5] = filesRepositories.get(i).getFileRepoPath();
			itemValues[6] = filesRepositories.get(i).getFileRepoHost();
			itemValues[7] = filesRepositories.get(i).getFileRepoPort();
			itemValues[8] = filesRepositories.get(i).getFileRepoUser();
			itemValues[9] = filesRepositories.get(i).getFileRepoPass();
			
			this.tblFilesRepository.addItem(itemValues, filesRepositories.get(i).getFileRepoId());
		}
		
	}
	
	private void addFileRepoRow()
	{
		Object[] itemValues = new Object[10];

		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		itemValues[0] = chxSelect;

		itemValues[1] = "";
		
		CheckBox chxIsLocal = new CheckBox();
		itemValues[2] = chxIsLocal;
		
		CheckBox chxIsFtp = new CheckBox();
		itemValues[3] = chxIsFtp;
		
		CheckBox chxIsSftp = new CheckBox();
		itemValues[4] = chxIsSftp;
		
		itemValues[5] = "";
		itemValues[6] = "";
		itemValues[7] = "";
		itemValues[8] = "";
		itemValues[9] = "";
		
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
		if(validateRequiredFieldsResult)
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
				filesRepository.setFileRepoName((String)(tblRowItem.getItemProperty("Name").getValue()));
				filesRepository.setFileRepoIsLocal(((CheckBox)(tblRowItem.getItemProperty("Is Local").getValue())).getValue());
				filesRepository.setFileRepoIsFtp(((CheckBox)(tblRowItem.getItemProperty("Is FTP").getValue())).getValue());
				filesRepository.setFileRepoIsSftp(((CheckBox)(tblRowItem.getItemProperty("Is SFTP").getValue())).getValue());
				filesRepository.setFileRepoPath((String)(tblRowItem.getItemProperty("Path").getValue()));
				filesRepository.setFileRepoHost((String)(tblRowItem.getItemProperty("Host").getValue()));
				filesRepository.setFileRepoPort((String)(tblRowItem.getItemProperty("Port").getValue()));
				filesRepository.setFileRepoUser((String)(tblRowItem.getItemProperty("User").getValue()));
				filesRepository.setFileRepoPass((String)(tblRowItem.getItemProperty("Pass").getValue()));
								
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
		else
			this.getUI().showNotification("Name must be set for New Files Repository Records", Type.WARNING_MESSAGE);
		
	}
	
	private boolean validateRequiredFields()
	{	
		Collection itemIds = this.tblFilesRepository.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblFilesRepository.getContainerDataSource().getItem(itemId);
			
			if(((String)(tblRowItem.getItemProperty("Name").getValue())).isEmpty())
			{
				tblFilesRepository.select(itemId);
				return false;
			}
		}
		
		return true;
	}
	
	
	
	
}
