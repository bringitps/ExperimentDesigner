package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.apache.commons.lang.StringUtils;

import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UserRole;
import com.bringit.experiment.dao.SysRoleDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.UserRoleDao;
import com.bringit.experiment.ui.design.SysUserConfigDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class SysUserConfigForm extends SysUserConfigDesign{

	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();
	private List<SysRole> sysRoles = new SysRoleDao().getAllSysRoles();
	
	public SysUserConfigForm()
	{		
		loadTblData("","");
				
		this.btnAddSysUser.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addSysUserRow();
			}

		});
	
		this.btnDeleteSysUser.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deleteSysUserRow();
			}

		});
		
		this.btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				saveSysUserRows();
			}

		});
		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				if(cbxSysUserFilters.getValue() != null) 
					loadTblData(cbxSysUserFilters.getValue().toString(), txtSearch.getValue());				
			}
			});

		btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				 loadTblData("","");
			}

		});
	}
	
	@SuppressWarnings("deprecation")
	private void loadTblData(String filterName, String filterValue)
	{
		dbIdOfItemsToDelete = new ArrayList<Integer>();
		
		tblSysUser.setContainerDataSource(null);
		tblSysUser.addContainerProperty("*", CheckBox.class, null);
		tblSysUser.addContainerProperty("Full Name", TextField.class, null);
		tblSysUser.addContainerProperty("E-mail", TextField.class, null);
		tblSysUser.addContainerProperty("Username", TextField.class, null);
		tblSysUser.addContainerProperty("Password", PasswordField.class, null);
		tblSysUser.addContainerProperty("AD Login?", CheckBox.class, null);
		tblSysUser.addContainerProperty("Roles", Panel.class, null);
		tblSysUser.addContainerProperty("Default Role", ComboBox.class, null);
		tblSysUser.setEditable(true);
		tblSysUser.setPageLength(0);
		tblSysUser.setColumnWidth("*", 20);

		cbxSysUserFilters.setContainerDataSource(null);
		cbxSysUserFilters.addItem("Full Name");
		cbxSysUserFilters.addItem("E-mail");
		cbxSysUserFilters.addItem("Username");
		
		if(filterName.isEmpty())
			cbxSysUserFilters.select("FullName");
		else
			cbxSysUserFilters.select(filterName.trim());
		
		Object[] itemValues = new Object[8];

		List<SysUser> sysUsers = new SysUserDao().getAllActiveSysUsers();
		
		for(int i=0; sysUsers != null && i<sysUsers.size(); i++)
		{
			if(filterName.isEmpty() || (filterName.trim().equals("Full Name") && sysUsers.get(i).getUserFullName().toLowerCase().contains(filterValue.trim().toLowerCase()))
					|| (filterName.trim().equals("E-mail") && sysUsers.get(i).getUserEmail().toLowerCase().contains(filterValue.trim().toLowerCase()))
					|| (filterName.trim().equals("Username") && sysUsers.get(i).getUserName().toLowerCase().contains(filterValue.trim().toLowerCase())))
			{	
				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				chxSelect.setWidth(10, Unit.PIXELS);
				itemValues[0] = chxSelect;

				TextField txtUserFullName = new TextField();
				txtUserFullName.setStyleName("tiny");
				txtUserFullName.setValue(sysUsers.get(i).getUserFullName());			
				txtUserFullName.setWidth(95, Unit.PERCENTAGE);
				itemValues[1] = txtUserFullName;

				TextField txtUserEmail = new TextField();
				txtUserEmail.setStyleName("tiny");
				txtUserEmail.setValue(sysUsers.get(i).getUserEmail());			
				txtUserEmail.setWidth(95, Unit.PERCENTAGE);
				itemValues[2] = txtUserEmail;

				TextField txtUserName = new TextField();
				txtUserName.setStyleName("tiny");
				txtUserName.setValue(sysUsers.get(i).getUserName());			
				txtUserName.setWidth(95, Unit.PERCENTAGE);
				
				txtUserName.addValidator(new Validator() {

		            public void validate(Object value) throws InvalidValueException {
		                if(!StringUtils.isAlphanumeric(((String) value).replaceAll("_", "")))
		                    throw new InvalidValueException("Only AlphaNumeric and Underscores are allowed for Username");
		            }
		            
		        });
				
				itemValues[3] = txtUserName;

				PasswordField txtUserPass = new PasswordField();
				txtUserPass.setStyleName("tiny");
				txtUserPass.setValue(sysUsers.get(i).getUserPass());			
				txtUserPass.setWidth(95, Unit.PERCENTAGE);
				itemValues[4] = txtUserPass;
				
				CheckBox chxAdLogin = new CheckBox();
				chxAdLogin.setValue(sysUsers.get(i).getIsActiveDirectoryUser());
				itemValues[5] = chxAdLogin;
				
				Panel pnlRoles = new Panel("Roles");
				VerticalLayout layoutPnlRoles = new VerticalLayout();
				OptionGroup optGrpRoles = new OptionGroup();
				optGrpRoles.setMultiSelect(true);
				optGrpRoles.setStyleName("small");
				pnlRoles.setHeight(80, Unit.PIXELS);
				pnlRoles.setWidth(100, Unit.PERCENTAGE);
				pnlRoles.setStyleName("well");
				pnlRoles.setCaption("");
				    
				for(int j=0; sysRoles != null && j<sysRoles.size(); j++)
				{
					if(!sysRoles.get(j).getRoleName().equals("sys_admin"))
					{
						optGrpRoles.addItem(sysRoles.get(j).getRoleId());
						optGrpRoles.setItemCaption(sysRoles.get(j).getRoleId(), sysRoles.get(j).getRoleName());
					}
				}
				
				layoutPnlRoles.addComponent(optGrpRoles);
				pnlRoles.setContent(layoutPnlRoles);
				    
				itemValues[6] = pnlRoles;
				
				ComboBox cbxDefaultRole = new ComboBox();
				cbxDefaultRole.setStyleName("tiny");
				cbxDefaultRole.setWidth(95, Unit.PERCENTAGE);
				
				optGrpRoles.addListener(new Property.ValueChangeListener() {

				    @Override
				    public void valueChange(ValueChangeEvent event) {
				    	Set<Item> selectedOptGrpMnuAccess = (Set<Item>)optGrpRoles.getValue();
				    	Integer selectedDefaultRole = cbxDefaultRole.getValue() != null ? Integer.parseInt(cbxDefaultRole.getValue().toString()) : -1;
				    	cbxDefaultRole.setContainerDataSource(null);
						
				    	for (Object selectedOptionGroup : selectedOptGrpMnuAccess)
						{
								cbxDefaultRole.addItem(Integer.parseInt(selectedOptionGroup.toString()));
								cbxDefaultRole.setItemCaption(Integer.parseInt(selectedOptionGroup.toString()), optGrpRoles.getItemCaption(Integer.parseInt(selectedOptionGroup.toString())));
								if(selectedDefaultRole == Integer.parseInt(selectedOptionGroup.toString()))
									cbxDefaultRole.select(selectedDefaultRole);
						}		  
				    }   
				}); 
				
				itemValues[7] = cbxDefaultRole;
				
				List<UserRole> userRoles = new UserRoleDao().getUserRolesByUser(sysUsers.get(i));
				List<Integer> selectedRoleMtx = new ArrayList<Integer>();
				List<Boolean> isDefaultRoleMtx = new ArrayList<Boolean>();
				
				for(int j=0; userRoles != null && j<userRoles.size(); j++)
				{
					selectedRoleMtx.add(userRoles.get(j).getSysRole().getRoleId());
					isDefaultRoleMtx.add(userRoles.get(j).isDefaultRole());
				}
				
				Collection optGrpRoleIds = optGrpRoles.getContainerDataSource().getItemIds();
				
				for (Object optGrpRoleId : optGrpRoleIds) 
				{
					if(selectedRoleMtx.indexOf((Integer)optGrpRoleId) >= 0)
					{
						optGrpRoles.select(optGrpRoleId);
						
						if(isDefaultRoleMtx.get(selectedRoleMtx.indexOf((Integer)optGrpRoleId)))
							cbxDefaultRole.select(optGrpRoleId);
					}
				}
				
				if(!sysUsers.get(i).getUserName().equals("root"))
					tblSysUser.addItem(itemValues, sysUsers.get(i).getUserId());
			}
		}
	}
	
	private void addSysUserRow()
	{

		Object[] itemValues = new Object[8];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		chxSelect.setWidth(10, Unit.PIXELS);
		itemValues[0] = chxSelect;

		TextField txtUserFullName = new TextField();
		txtUserFullName.setStyleName("tiny");
		txtUserFullName.setValue("");			
		txtUserFullName.setWidth(95, Unit.PERCENTAGE);
		itemValues[1] = txtUserFullName;

		TextField txtUserEmail = new TextField();
		txtUserEmail.setStyleName("tiny");
		txtUserEmail.setValue("");			
		txtUserEmail.setWidth(95, Unit.PERCENTAGE);
		itemValues[2] = txtUserEmail;

		TextField txtUserName = new TextField();
		txtUserName.setStyleName("tiny");
		txtUserName.setValue("");			
		txtUserName.setWidth(95, Unit.PERCENTAGE);
		
		txtUserName.addValidator(new Validator() {

            public void validate(Object value) throws InvalidValueException {
                if(!StringUtils.isAlphanumeric(((String) value).replaceAll("_", "")))
                    throw new InvalidValueException("Only AlphaNumeric and Underscores are allowed for Username");
            }
            
        });
		
		itemValues[3] = txtUserName;

		PasswordField txtUserPass = new PasswordField();
		txtUserPass.setStyleName("tiny");
		txtUserPass.setValue("");			
		txtUserPass.setWidth(95, Unit.PERCENTAGE);
		itemValues[4] = txtUserPass;
		
		CheckBox chxAdLogin = new CheckBox();
		itemValues[5] = chxAdLogin;
		
		Panel pnlRoles = new Panel("Roles");
		VerticalLayout layoutPnlRoles = new VerticalLayout();
		OptionGroup optGrpRoles = new OptionGroup();
		optGrpRoles.setMultiSelect(true);
		optGrpRoles.setStyleName("small");
		pnlRoles.setHeight(80, Unit.PIXELS);
		pnlRoles.setWidth(100, Unit.PERCENTAGE);
		pnlRoles.setStyleName("well");
		pnlRoles.setCaption("");
		    
		for(int j=0; sysRoles != null && j<sysRoles.size(); j++)
		{
			if(!sysRoles.get(j).getRoleName().equals("sys_admin"))
			{
				optGrpRoles.addItem(sysRoles.get(j).getRoleId());
				optGrpRoles.setItemCaption(sysRoles.get(j).getRoleId(), sysRoles.get(j).getRoleName());
			}
		}
		
		layoutPnlRoles.addComponent(optGrpRoles);
		pnlRoles.setContent(layoutPnlRoles);
		    
		itemValues[6] = pnlRoles;
		
		ComboBox cbxDefaultRole = new ComboBox();
		cbxDefaultRole.setStyleName("tiny");
		cbxDefaultRole.setWidth(95, Unit.PERCENTAGE);
		
		optGrpRoles.addListener(new Property.ValueChangeListener() {

		    @Override
		    public void valueChange(ValueChangeEvent event) {
		    	Set<Item> selectedOptGrpMnuAccess = (Set<Item>)optGrpRoles.getValue();
		    	Integer selectedDefaultRole = cbxDefaultRole.getValue() != null ? Integer.parseInt(cbxDefaultRole.getValue().toString()) : -1;
		    	cbxDefaultRole.setContainerDataSource(null);
				
		    	for (Object selectedOptionGroup : selectedOptGrpMnuAccess)
				{
						cbxDefaultRole.addItem(Integer.parseInt(selectedOptionGroup.toString()));
						cbxDefaultRole.setItemCaption(Integer.parseInt(selectedOptionGroup.toString()), optGrpRoles.getItemCaption(Integer.parseInt(selectedOptionGroup.toString())));
						if(selectedDefaultRole == Integer.parseInt(selectedOptionGroup.toString()))
							cbxDefaultRole.select(selectedDefaultRole);
				}		  
		    }   
		}); 
		
		itemValues[7] = cbxDefaultRole;
		
		
		this.lastNewItemId = this.lastNewItemId - 1;
		tblSysUser.addItem(itemValues, this.lastNewItemId);
		tblSysUser.select(this.lastNewItemId);
		
	}
	
	private void deleteSysUserRow()
	{
		dbIdOfItemsToDelete.add((int)tblSysUser.getValue());
		tblSysUser.removeItem((int)tblSysUser.getValue());			
	}
		
	private void saveSysUserRows()
	{
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateDuplicatedNamesResult = validateDuplicatedNames();
		boolean validateNonRootUserNameResult = validateNonRootUserName();
		boolean validateEmailAddressesResult = validateEmailAddresses();
		boolean validateUserNameFieldsResult = validateUserNameFields();
		
		if(validateRequiredFieldsResult && validateDuplicatedNamesResult && validateNonRootUserNameResult && validateEmailAddressesResult && validateUserNameFieldsResult)
		{
			SysUserDao sysUserDao = new SysUserDao();
		
			//Deactivate Users in DB
			for(int i = 0; i<dbIdOfItemsToDelete.size(); i++)
			{
				SysUser userToDeactivate = new SysUserDao().getUserById((int)dbIdOfItemsToDelete.get(i));
				userToDeactivate.setUserIsActive(false);
				sysUserDao.updateSysUser(userToDeactivate);
			}
		
			Collection itemIds = tblSysUser.getContainerDataSource().getItemIds();
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = tblSysUser.getContainerDataSource().getItem(itemId);
				
				SysUser sysUser = new SysUser();
				sysUser.setIsActiveDirectoryUser(((CheckBox)(tblRowItem.getItemProperty("AD Login?").getValue())).getValue());
				sysUser.setUserEmail(((TextField)(tblRowItem.getItemProperty("E-mail").getValue())).getValue());
				sysUser.setUserFullName(((TextField)(tblRowItem.getItemProperty("Full Name").getValue())).getValue());
				sysUser.setUserIsActive(true);
				sysUser.setUserName(((TextField)(tblRowItem.getItemProperty("Username").getValue())).getValue());
				sysUser.setUserPass(((PasswordField)(tblRowItem.getItemProperty("Password").getValue())).getValue());
				
				Panel pnlRoles = ((Panel)(tblRowItem.getItemProperty("Roles").getValue()));
				VerticalLayout layoutPnlRoles = (VerticalLayout)pnlRoles.getContent();
				OptionGroup optGrpRoles = (OptionGroup)layoutPnlRoles.getComponent(0);
				
				
				
				
				if(itemId > 0)
				{
					sysUser.setUserId(itemId);
					List<UserRole> userRoles = new UserRoleDao().getUserRolesByUser(sysUser);
					
					for(int j=0; userRoles != null && j<userRoles.size(); j++)
						new UserRoleDao().deleteUserRole(userRoles.get(j).getUserRoleId());

					sysUserDao.updateSysUser(sysUser);
				}
				else
					sysUserDao.addSysUser(sysUser);

				Integer defaultRoleId = -1;
				if(((ComboBox)tblRowItem.getItemProperty("Default Role").getValue()).getValue() != null)
					defaultRoleId = Integer.parseInt(((ComboBox)tblRowItem.getItemProperty("Default Role").getValue()).getValue().toString());
				System.out.println("Default Role Id: " + defaultRoleId + "\n");
				
				Set<Item> selectedOptGrpRoles = (Set<Item>)optGrpRoles.getValue();
				for (Object selectedOptGrpRole : selectedOptGrpRoles)
				{
					UserRole userRole = new UserRole();
					userRole.setSysUser(sysUser);
					userRole.setSysRole(new SysRoleDao().getRoleById(Integer.parseInt(selectedOptGrpRole.toString())));
					
					if(Integer.parseInt(selectedOptGrpRole.toString()) == defaultRoleId)
						userRole.setDefaultRole(true);
					else
						userRole.setDefaultRole(false);
					
					new UserRoleDao().addUserRole(userRole);
				}
			}

			this.getUI().showNotification("Data Saved.", Type.HUMANIZED_MESSAGE);
			cbxSysUserFilters.select("Name");
			txtSearch.setValue("");
			loadTblData("","");
		}
		else if(!validateRequiredFieldsResult)
			this.getUI().showNotification("Full Name, Username and E-mail must be set for New User Records", Type.WARNING_MESSAGE);
		else if(!validateDuplicatedNamesResult)
			this.getUI().showNotification("Username/E-mail of User can not be duplicated.", Type.WARNING_MESSAGE);
		else if(!validateNonRootUserNameResult)
			this.getUI().showNotification("'root' is Non-Eligible for Name.", Type.WARNING_MESSAGE);
		else if(!validateEmailAddressesResult)
			this.getUI().showNotification("Invalid E-mail addresses found. Please check.", Type.WARNING_MESSAGE);
		else if(!validateUserNameFieldsResult)
			this.getUI().showNotification("Only AlphaNumeric and Underscores are allowed for Usernames", Type.WARNING_MESSAGE);
	}
		
	private boolean validateRequiredFields()
	{	
		Collection itemIds = tblSysUser.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblSysUser.getContainerDataSource().getItem(itemId);
			
			if(((TextField)(tblRowItem.getItemProperty("Full Name").getValue())).getValue().isEmpty())
			{
				tblSysUser.select(itemId);
				return false;
			}
			if(((TextField)(tblRowItem.getItemProperty("Username").getValue())).getValue().isEmpty())
			{
				tblSysUser.select(itemId);
				return false;
			}
			if(((TextField)(tblRowItem.getItemProperty("E-mail").getValue())).getValue().isEmpty())
			{
				tblSysUser.select(itemId);
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateDuplicatedNames()
	{
		List<String> sysUserNames = new ArrayList<String>();
		List<String> sysUserEmails = new ArrayList<String>();
		
		Collection itemIds = tblSysUser.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblSysUser.getContainerDataSource().getItem(itemId);

			if(sysUserNames.indexOf(((TextField)(tblRowItem.getItemProperty("Username").getValue())).getValue()) >= 0)
			{
				tblSysUser.select(itemId);
				return false;
			}
			else
				sysUserNames.add(((TextField)(tblRowItem.getItemProperty("Username").getValue())).getValue());
			
			if(sysUserEmails.indexOf(((TextField)(tblRowItem.getItemProperty("E-mail").getValue())).getValue()) >= 0)
			{
				tblSysUser.select(itemId);
				return false;
			}
			else
				sysUserEmails.add(((TextField)(tblRowItem.getItemProperty("E-mail").getValue())).getValue());
		}
		
		return true;
	}
	
	private boolean validateNonRootUserName()
	{	
		Collection itemIds = tblSysUser.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblSysUser.getContainerDataSource().getItem(itemId);
			
			if((((TextField)(tblRowItem.getItemProperty("Username").getValue())).getValue()).equals("root"))
			{
				tblSysUser.select(itemId);
				return false;
			}
		}
		
		return true;
	}

	private boolean validateUserNameFields()
	{
		Collection itemIds = tblSysUser.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblSysUser.getContainerDataSource().getItem(itemId);
			String UserName = ((TextField)(tblRowItem.getItemProperty("Username").getValue())).getValue();
			
			if(UserName.isEmpty() || StringUtils.isEmpty(UserName))
			{
				tblSysUser.select(itemId);
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateEmailAddresses()
	{
		Collection itemIds = tblSysUser.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblSysUser.getContainerDataSource().getItem(itemId);
			
			if(!((TextField)(tblRowItem.getItemProperty("E-mail").getValue())).getValue().isEmpty() && !isValidEmailAddress(((TextField)(tblRowItem.getItemProperty("E-mail").getValue())).getValue()))
			{
				tblSysUser.select(itemId);
				return false;
			}
		}
		
		return true;
	}

	public boolean isValidEmailAddress(String email) 
	{
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}
}
