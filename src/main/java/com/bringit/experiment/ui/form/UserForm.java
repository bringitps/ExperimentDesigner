package com.bringit.experiment.ui.form;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.FilesRepository;
import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UserRole;
import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.FilesRepositoryDao;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.SysRoleDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.UserRoleDao;
import com.bringit.experiment.dao.XmlTemplateNodeDao;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.remote.RemoteFileUtil;
import com.bringit.experiment.ui.design.UserDesign;
import com.bringit.experiment.ui.design.UserDesign;
import com.opencsv.CSVReader;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;

import com.vaadin.ui.TextField;

import com.vaadin.ui.Upload;

import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Panel;

import com.vaadin.ui.Window;

public class UserForm extends UserDesign {

	private SysUser user;
	private List<SysRole> roles =  new SysRoleDao().getAllSysRoles();
	private int lastNewItemId = 0;
	private List<Integer> dbIdOfUserRoleItemsToDelete = new ArrayList<Integer>();
	private List<UserRole> userRoles;
	public UserForm(int usrId)
	{
		this.tblUserRoles.setContainerDataSource(null);
		this.tblUserRoles.addContainerProperty("*", CheckBox.class, null);
		this.tblUserRoles.addContainerProperty("Role", ComboBox.class, null);		
		this.tblUserRoles.addContainerProperty("Default", CheckBox.class, null);	
		this.tblUserRoles.setPageLength(0);
		
		if(usrId == -1) //New
		{
			this.btnDelete.setEnabled(false);
			this.user = new SysUser();
			this.chxActiveDirectory.setValue(true);
			this.txtPassword.setEnabled(false);
		}
		else
		{
			user = new SysUserDao().getUserById(usrId);
			this.txtUserName.setValue(user.getUserName());
			this.chxActiveDirectory.setValue(user.isActiveDirectoryUser());
			if(user.isActiveDirectoryUser()){
				this.txtPassword.setEnabled(false);
			}else{
				this.txtPassword.setEnabled(true);
				this.txtPassword.setValue(user.getUserPass());
			}
			

			this.userRoles = new UserRoleDao().getUserRolesByUser(user);
			
			Object[] itemValues = new Object[2];
			for(int i=0; i<this.userRoles.size(); i++)
			{		
				
				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				itemValues[0] = chxSelect;

				ComboBox cbxUserRole = new ComboBox("");
				for(int j=0; j<roles.size(); j++)
				{
					cbxUserRole.addItem(roles.get(j).getRoleId());
					cbxUserRole.setItemCaption(roles.get(j).getRoleId(), roles.get(j).getRoleName() + " [ " + roles.get(j).getRoleDescription() + " ]");
					cbxUserRole.setWidth(100, Unit.PERCENTAGE);
				}
				
				cbxUserRole.setNullSelectionAllowed(true);
				cbxUserRole.addStyleName("tiny");
				if(this.userRoles.get(i).getSysRole() != null)
					cbxUserRole.setValue(this.userRoles.get(i).getSysRole().getRoleId());
				itemValues[1] = cbxUserRole;
				
				this.tblUserRoles.addItem(itemValues, this.userRoles.get(i).getUserRoleId());
			}
	
		}
		chxActiveDirectory.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(chxActiveDirectory.getValue()){
					txtPassword.setEnabled(false);
				}else{
					txtPassword.setEnabled(true);
				}
				
			}   
	    });
		btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onSave();
			}

		});
		btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				closeModalWindow();
			}


		});
		btnDelete.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onDelete();
			}

		});
		
	}
	
	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
	@SuppressWarnings("deprecation")
	protected void onSave() {
		Collection itemIds = this.tblUserRoles.getContainerDataSource().getItemIds();
		boolean validateReqFieldsResult = validateRequiredFields();		
		boolean validateNonRepeatedRolesResult = validateNonRepeatedRoles();
		boolean validateDefaultRoleResult = validateDefaultRole();
		
		//---Validate Required Fields---//
		if(itemIds.size() > 0 && validateReqFieldsResult && validateNonRepeatedRolesResult && validateDefaultRoleResult)
		{
			SysUser sessionUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
			
			//Save user
			this.user.setUserName(this.txtUserName.getValue());
			this.user.setActiveDirectoryUser(this.chxActiveDirectory.getValue());
			this.user.setUserPass(this.txtPassword.getValue());

			if(this.user.getUserId() != null ) {
				new SysUserDao().updateSysUser(user);
			} else
			{
				new SysUserDao().addSysUser(user);

			}
			

			UserRoleDao usrRolDao = new UserRoleDao();
			for (Object itemIdObj : itemIds) 
			{
				
				int itemId = (int)itemIdObj;
				Item tblRowItem = this.tblUserRoles.getContainerDataSource().getItem(itemId);
				
				UserRole usrRole = new UserRole();
				usrRole.setSysRole(new SysRoleDao().getRoleById((int)((ComboBox)(tblRowItem.getItemProperty("Role").getValue())).getValue()));			
				usrRole.setSysUser(user);
				usrRole.setDefaultRole(((CheckBox)(tblRowItem.getItemProperty("Default").getValue())).getValue());
				
				if(itemId > 0)
				{
					usrRole.setUserRoleId(itemId);
					
					usrRolDao.updateUserRole(usrRole);
				}
				else
					usrRolDao.addUserRole(usrRole);
				
			}
			
			
		
			closeModalWindow();
		}
		else
		{
			if(itemIds.size() <= 0)
				this.getUI().showNotification("There are no csv columns mapped on your User", Type.WARNING_MESSAGE);
			else if(!validateReqFieldsResult)
				this.getUI().showNotification("Please fill in all required Fields", Type.WARNING_MESSAGE);
			else if(!validateNonRepeatedRolesResult)
				this.getUI().showNotification("You can only select a role once", Type.WARNING_MESSAGE);
			else if(!validateDefaultRoleResult)
				this.getUI().showNotification("You should select a single default role", Type.WARNING_MESSAGE);
		
		}
	}
	
	private boolean validateRequiredFields()
	{
		if(!this.txtUserName.isValid()) return false;	
		if(this.chxActiveDirectory.getValue()){
			if(!this.txtPassword.isValid()) return false;
		}
		return true;
	}
	

	private boolean validateNonRepeatedRoles()
	{
		List<Integer> selectedRoles = new ArrayList<Integer>();
		
		Collection itemIds = this.tblUserRoles.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblUserRoles.getContainerDataSource().getItem(itemId);
			
			if(((ComboBox)(tblRowItem.getItemProperty("Role").getValue())).getValue() != null)
			{
				if(selectedRoles.indexOf(((int)((ComboBox)(tblRowItem.getItemProperty("Role").getValue())).getValue())) > -1)
					return false;
				else
					selectedRoles.add(((int)((ComboBox)(tblRowItem.getItemProperty("Role").getValue())).getValue()));
			}
		}
		return true;
	}
	
	private boolean validateDefaultRole()
	{
		List<Integer> selectedRoles = new ArrayList<Integer>();
		int cont = 0;
		Collection itemIds = this.tblUserRoles.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = this.tblUserRoles.getContainerDataSource().getItem(itemId);
			
			if(((CheckBox)(tblRowItem.getItemProperty("Default").getValue())).getValue())
			{
				cont = cont +1;
			}
		}
		if(cont!=1){
			return false;
		}
		return true;
	}
	
	/*
	private void fillNodes(String[] columns) {
		
		if(this.user.getUserId() != null && this.user.getUserId() > 0)
		{
			//Remove deprecated Nodes from UI and store Ids to delete from DB at saving
			int loopNodeCnt = 0;
			Collection itemIds = this.tblCsvCols.getContainerDataSource().getItemIds();
			
			for (Object itemIdObj : itemIds) 
			{	
				int itemId = (int)itemIdObj;
				if(itemId > 0)
					dbIdOfUserNodeItemsToDelete.add(itemId);
			}			
			
			this.tblCsvCols.setContainerDataSource(null);
			this.tblCsvCols.addContainerProperty("*", CheckBox.class, null);
			this.tblCsvCols.addContainerProperty("Csv Column", TextField.class, null);
			this.tblCsvCols.addContainerProperty("Experiment Field", ComboBox.class, null);		
			this.tblCsvCols.setPageLength(0);
			
		}
		
		ExperimentDao expdao = new ExperimentDao();
		Experiment expNew = expdao.getExperimentById((int)(this.comboCsvTExperiment.getValue()));
		expFields = new ExperimentFieldDao().getActiveExperimentFields(expNew);
	
		Object[] itemValues = new Object[3];
		for(int i=0; i<columns.length; i++)
		{
			CheckBox chxSelect = new CheckBox();
			chxSelect.setVisible(false);
			itemValues[0] = chxSelect;
			
			TextField txtCsvColName = new TextField();
			txtCsvColName.setValue(columns[i]);
			txtCsvColName.setReadOnly(true);
			txtCsvColName.addStyleName("tiny");
			txtCsvColName.setWidth(100, Unit.PERCENTAGE);
			itemValues[1] = txtCsvColName;
			

			ComboBox cbxExpFields = new ComboBox("");
			for(int j=0; j<expFields.size(); j++)
			{
				cbxExpFields.addItem(expFields.get(j).getExpFieldId());
				cbxExpFields.setItemCaption(expFields.get(j).getExpFieldId(), expFields.get(j).getExpFieldName() + " [ " + expFields.get(j).getExpFieldType() + " ]");
				cbxExpFields.setWidth(100, Unit.PERCENTAGE);
			}
			
			cbxExpFields.setNullSelectionAllowed(true);
			cbxExpFields.addStyleName("tiny");
			itemValues[2] = cbxExpFields;
			
			this.lastNewItemId = this.lastNewItemId - 1;
			this.tblCsvCols.addItem(itemValues, this.lastNewItemId);
			
		}
		
	}

	private void fillCombos() {
		//Experiments
		for(int j=0; j<experiments.size(); j++)
		{
			this.comboCsvTExperiment.addItem(experiments.get(j).getExpId());
			this.comboCsvTExperiment.setItemCaption(experiments.get(j).getExpId(), experiments.get(j).getExpName());
			//this.comboCsvTExperiment.setWidth(100, Unit.PIXELS);
		}
		
		this.comboCsvTExperiment.setNullSelectionAllowed(false);
		this.comboCsvTExperiment.setImmediate(true);
		this.comboCsvTExperiment.addStyleName("small");
		
		
		//Contract Manufacturer
		
		this.cbxContractManufacturer.setNullSelectionAllowed(true);
		this.cbxContractManufacturer.setImmediate(true);
		this.cbxContractManufacturer.addStyleName("small");
		
		for(int i=0; contractManufacturers!=null && i<contractManufacturers.size(); i++)
		{
			this.cbxContractManufacturer.addItem(contractManufacturers.get(i).getCmId());
			this.cbxContractManufacturer.setItemCaption(contractManufacturers.get(i).getCmId(), contractManufacturers.get(i).getCmName());
		}
		
		//File Repos
		for(int j=0; j<repos.size(); j++)
		{
			this.comboCsvTinRepo.addItem(repos.get(j).getFileRepoId());
			this.comboCsvTinRepo.setItemCaption(repos.get(j).getFileRepoId(), "(" + repos.get(j).getFileRepoName() + ") " + repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			//this.comboCsvTinRepo.setWidth(100, Unit.PIXELS);
			
			this.comboCsvoutRepo.addItem(repos.get(j).getFileRepoId());
			this.comboCsvoutRepo.setItemCaption(repos.get(j).getFileRepoId(), "(" + repos.get(j).getFileRepoName() + ") " + repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			//this.comboCsvoutRepo.setWidth(100, Unit.PIXELS);
			
			this.comboCsvTerrRepo.addItem(repos.get(j).getFileRepoId());
			this.comboCsvTerrRepo.setItemCaption(repos.get(j).getFileRepoId(), "(" + repos.get(j).getFileRepoName() + ") " + repos.get(j).getFileRepoHost()+":"+repos.get(j).getFileRepoPath());
			//this.comboCsvTerrRepo.setWidth(100, Unit.PIXELS);
		}
		
		
		this.comboCsvTinRepo.setNullSelectionAllowed(true);
		this.comboCsvTinRepo.setImmediate(true);
		this.comboCsvTinRepo.addStyleName("small");
		
		this.comboCsvoutRepo.setNullSelectionAllowed(true);
		this.comboCsvoutRepo.setImmediate(true);
		this.comboCsvoutRepo.addStyleName("small");
		
		this.comboCsvTerrRepo.setNullSelectionAllowed(true);
		this.comboCsvTerrRepo.setImmediate(true);
		this.comboCsvTerrRepo.addStyleName("small");
		
		//Jobs
		for(int j=0; j<jobs.size(); j++)
		{
			this.comboCsvjobScheduler.addItem(jobs.get(j).getJobExecRepeatId());
			this.comboCsvjobScheduler.setItemCaption(jobs.get(j).getJobExecRepeatId(), jobs.get(j).getJobExecRepeatName());
		}
		
		this.comboCsvjobScheduler.setNullSelectionAllowed(true);
		this.comboCsvjobScheduler.setImmediate(true);
		this.comboCsvjobScheduler.addStyleName("small");
		
		//Hour Execution Start
		this.cbxStartHour.setNullSelectionAllowed(false);
		for(int i=0; i<24; i++)
		{
			this.cbxStartHour.addItem(i);
			this.cbxStartHour.setItemCaption(i, (i<10 ? "0" :"") + i + ":00");
		}
		
	}
	*/
	private void onDelete()
	{	 
		new SysUserDao().deleteSysUser(this.user.getUserId());

		closeModalWindow();
    }
	
}
