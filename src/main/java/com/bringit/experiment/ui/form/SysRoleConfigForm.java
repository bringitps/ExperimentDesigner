package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.SysRoleDao;
import com.bringit.experiment.dao.UserRoleDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.ui.design.SysRoleConfigDesign;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;

public class SysRoleConfigForm extends SysRoleConfigDesign {

	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();
	private Tree treeMainMenu;// = new ArrayList<String>();
	
	public SysRoleConfigForm(Tree treeMainMenu)
	{

		this.treeMainMenu = treeMainMenu;
		
		loadTblData("","");
				
		//this.menuOptionList = menuOptionList; 
		this.btnAddSysRole.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addSysRoleRow();
			}

		});
	
		this.btnDeleteSysRole.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deleteSysRoleRow();
			}

		});
		
		this.btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				saveSysRoleRows();
			}

		});
		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				if(cbxSysRoleFilters.getValue() != null) 
					loadTblData(cbxSysRoleFilters.getValue().toString(), txtSearch.getValue());				
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
		
		tblSysRole.setContainerDataSource(null);
		tblSysRole.addContainerProperty("*", CheckBox.class, null);
		tblSysRole.addContainerProperty("Name", TextField.class, null);
		tblSysRole.addContainerProperty("Description", TextField.class, null);
		tblSysRole.addContainerProperty("Default Role?", CheckBox.class, null);
		tblSysRole.addContainerProperty("Menu Access", Panel.class, null);
		tblSysRole.setEditable(true);
		tblSysRole.setPageLength(0);
		tblSysRole.setColumnWidth("*", 20);

		cbxSysRoleFilters.setContainerDataSource(null);
		cbxSysRoleFilters.addItem("Name");
		cbxSysRoleFilters.addItem("Description");
		
		if(filterName.isEmpty())
			cbxSysRoleFilters.select("Name");
		else
			cbxSysRoleFilters.select(filterName.trim());
		
		Object[] itemValues = new Object[5];

		List<SysRole> sysRoles = new SysRoleDao().getAllSysRoles();
		
		for(int i=0; sysRoles != null && i<sysRoles.size(); i++)
		{
			if(filterName.isEmpty() || (filterName.trim().equals("Name") && sysRoles.get(i).getRoleName().toLowerCase().contains(filterValue.trim().toLowerCase()))
				|| (filterName.trim().equals("Description") && sysRoles.get(i).getRoleDescription().toLowerCase().contains(filterValue.trim().toLowerCase())))
			{	
				CheckBox chxSelect = new CheckBox();
				chxSelect.setVisible(false);
				chxSelect.setWidth(10, Unit.PIXELS);
				itemValues[0] = chxSelect;

				TextField txtRoleName = new TextField();
				txtRoleName.setStyleName("tiny");
				txtRoleName.setValue(sysRoles.get(i).getRoleName());			
				txtRoleName.setWidth(95, Unit.PERCENTAGE);
				itemValues[1] = txtRoleName;
				
				TextField txtRoleDescription = new TextField();
				txtRoleDescription.setStyleName("tiny");
				txtRoleDescription.setValue(sysRoles.get(i).getRoleDescription());			
				txtRoleDescription.setWidth(95, Unit.PERCENTAGE);
				itemValues[2] = txtRoleDescription;
		
				CheckBox chxIsAdDefaultRole = new CheckBox();
				chxIsAdDefaultRole.setValue(sysRoles.get(i).getIsActiveDirectoryDefaultRole());
				itemValues[3] = chxIsAdDefaultRole;
				
				List<String> mnuAccessList = new ArrayList<String>();
				if(sysRoles.get(i).getRoleMenuAccess() != null)
					mnuAccessList = Arrays.asList(sysRoles.get(i).getRoleMenuAccess().split("\n"));
				
				Panel pnlMnuAccess = new Panel("Menu Access");
				VerticalLayout layoutPnlMnuAccess = new VerticalLayout();
				OptionGroup optGrpMnuAccess = new OptionGroup();
				optGrpMnuAccess.setMultiSelect(true);
				optGrpMnuAccess.setStyleName("small");
				pnlMnuAccess.setHeight(200, Unit.PIXELS);
				pnlMnuAccess.setWidth(100, Unit.PERCENTAGE);
				pnlMnuAccess.setStyleName("well");
				pnlMnuAccess.setCaption("");
				    
				
			    for (Object id : treeMainMenu.getItemIds()) 
			    {
					if(treeMainMenu.getParent(id) != null && (treeMainMenu.getItem(id).getItemProperty("isExperimentDataReport").getValue() == null 
							&& (treeMainMenu.getItem(id).getItemProperty("isTargetReport").getValue() == null)))
					{
						if(treeMainMenu.getChildren(id) != null)
						{
							optGrpMnuAccess.addItem(id);
							optGrpMnuAccess.setItemIcon(id, treeMainMenu.getItemIcon(id));
							if(mnuAccessList.indexOf(id.toString()) >= 0)
								optGrpMnuAccess.select(id);
						}
						else
						{
							optGrpMnuAccess.addItem(treeMainMenu.getParent(id).toString() + " / " + id);
							optGrpMnuAccess.setItemIcon(treeMainMenu.getParent(id).toString() + " / " + id, treeMainMenu.getItemIcon(id));

							if(mnuAccessList.indexOf(treeMainMenu.getParent(id).toString() + " / " + id) >= 0)
								optGrpMnuAccess.select(treeMainMenu.getParent(id).toString() + " / " + id);
						}
					}					
			    }		
			    
			    layoutPnlMnuAccess.addComponent(optGrpMnuAccess);
				pnlMnuAccess.setContent(layoutPnlMnuAccess);
				    
				itemValues[4] = pnlMnuAccess;
				
				if(!sysRoles.get(i).getRoleName().equals("sys_admin"))
					tblSysRole.addItem(itemValues, sysRoles.get(i).getRoleId());
			}
		}
	}
	
	private void addSysRoleRow()
	{
		Object[] itemValues = new Object[5];
		
		CheckBox chxSelect = new CheckBox();
		chxSelect.setVisible(false);
		itemValues[0] = chxSelect;
		
		TextField txtRoleName = new TextField();
		txtRoleName.setStyleName("tiny");
		txtRoleName.setValue("");			
		txtRoleName.setWidth(95, Unit.PERCENTAGE);
		itemValues[1] = txtRoleName;
		
		TextField txtRoleDescription = new TextField();
		txtRoleDescription.setStyleName("tiny");
		txtRoleDescription.setValue("");			
		txtRoleDescription.setWidth(95, Unit.PERCENTAGE);
		itemValues[2] = txtRoleDescription;


		CheckBox chxIsAdDefaultRole = new CheckBox();
		chxIsAdDefaultRole.setValue(false);
		itemValues[3] = chxIsAdDefaultRole;
		
		Panel pnlMnuAccess = new Panel("Menu Access");
		VerticalLayout layoutPnlMnuAccess = new VerticalLayout();
		OptionGroup optGrpMnuAccess = new OptionGroup();
		optGrpMnuAccess.setMultiSelect(true);
		optGrpMnuAccess.setStyleName("small");
		pnlMnuAccess.setHeight(200, Unit.PIXELS);
		pnlMnuAccess.setWidth(100, Unit.PERCENTAGE);
		pnlMnuAccess.setStyleName("well");
		pnlMnuAccess.setCaption("");
		    
	    for (Object id : treeMainMenu.getItemIds()) 
	    {
			if(treeMainMenu.getParent(id) != null && (treeMainMenu.getItem(id).getItemProperty("isExperimentDataReport").getValue() == null 
					&& (treeMainMenu.getItem(id).getItemProperty("isTargetReport").getValue() == null)))
			{
				if(treeMainMenu.getChildren(id) != null)
				{
					optGrpMnuAccess.addItem(id);
					optGrpMnuAccess.setItemIcon(id, treeMainMenu.getItemIcon(id));
				}
				else
				{
					optGrpMnuAccess.addItem(treeMainMenu.getParent(id).toString() + " / " + id);
					optGrpMnuAccess.setItemIcon(treeMainMenu.getParent(id).toString() + " / " + id, treeMainMenu.getItemIcon(id));
				}
			}					
	    }		
	    
	    layoutPnlMnuAccess.addComponent(optGrpMnuAccess);
		pnlMnuAccess.setContent(layoutPnlMnuAccess);
		    
		itemValues[4] = pnlMnuAccess;
		
		this.lastNewItemId = this.lastNewItemId - 1;
		tblSysRole.addItem(itemValues, this.lastNewItemId);
		tblSysRole.select(this.lastNewItemId);
	}
	
	private void deleteSysRoleRow()
	{
		boolean hasUsersLinked = false;
		if(tblSysRole.getValue() != null && (int)tblSysRole.getValue() > 0 )
		{
			if(new UserRoleDao().getUserRolesByRole(new SysRoleDao().getRoleById((int)tblSysRole.getValue())).size() <= 0)
				dbIdOfItemsToDelete.add((int)tblSysRole.getValue());
			else
				hasUsersLinked = true;
		}
		
		if(!hasUsersLinked && tblSysRole.getValue() != null)
			tblSysRole.removeItem((int)tblSysRole.getValue());
		else if(hasUsersLinked)
			this.getUI().showNotification("User Role record can not be deleted. \nThere are Users linked.", Type.WARNING_MESSAGE);
			
	}
		
	private void saveSysRoleRows()
	{
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateDuplicatedNamesResult = validateDuplicatedNames();
		boolean validateNonSysAdminNameResult = validateNonSysAdminName();
		
		if(validateRequiredFieldsResult && validateDuplicatedNamesResult && validateNonSysAdminNameResult)
		{
			SysRoleDao sysRoleDao = new SysRoleDao();
		
			//Delete Items in DB
			for(int i = 0; i<dbIdOfItemsToDelete.size(); i++)
				sysRoleDao.deleteSysRole((int)dbIdOfItemsToDelete.get(i));
		
			Collection itemIds = tblSysRole.getContainerDataSource().getItemIds();
			
			for (Object itemIdObj : itemIds) 
			{
				int itemId = (int)itemIdObj;
				Item tblRowItem = tblSysRole.getContainerDataSource().getItem(itemId);
				
				SysRole sysRole = new SysRole();
				sysRole.setRoleName(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
				sysRole.setRoleDescription(((TextField)(tblRowItem.getItemProperty("Description").getValue())).getValue());
				sysRole.setIsActiveDirectoryDefaultRole(((CheckBox)(tblRowItem.getItemProperty("Default Role?").getValue())).getValue());
				
				Panel pnlMnuAccess = ((Panel)(tblRowItem.getItemProperty("Menu Access").getValue()));
				VerticalLayout layoutPnlMnuAccess = (VerticalLayout)pnlMnuAccess.getContent();
				OptionGroup optGrpMnuAccess = (OptionGroup)layoutPnlMnuAccess.getComponent(0);
				
				Set<Item> selectedOptGrpMnuAccess = (Set<Item>)optGrpMnuAccess.getValue();
				for (Object selectedOptionGroup : selectedOptGrpMnuAccess)
					sysRole.setRoleMenuAccess((sysRole.getRoleMenuAccess() == null ? "" : sysRole.getRoleMenuAccess()) + selectedOptionGroup.toString() + "\n");
				
				if(itemId > 0)
				{
					sysRole.setRoleId(itemId);
					sysRoleDao.updateSysRole(sysRole);
				}
				else
					sysRoleDao.addSysRole(sysRole);
			}

			this.getUI().showNotification("Data Saved.", Type.HUMANIZED_MESSAGE);
			cbxSysRoleFilters.select("Name");
			txtSearch.setValue("");
			loadTblData("","");
		}
		else if(!validateRequiredFieldsResult)
			this.getUI().showNotification("Name and Description must be set for New User Role Records", Type.WARNING_MESSAGE);
		else if(!validateDuplicatedNamesResult)
			this.getUI().showNotification("Name of User Role can not be duplicated.", Type.WARNING_MESSAGE);
		else if(!validateNonSysAdminNameResult)
			this.getUI().showNotification("'sys_admin' is Non-Eligible for Name.", Type.WARNING_MESSAGE);
	}
		
	private boolean validateRequiredFields()
	{	
		Collection itemIds = tblSysRole.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblSysRole.getContainerDataSource().getItem(itemId);
			
			if(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue().isEmpty())
			{
				tblSysRole.select(itemId);
				return false;
			}
			if(((TextField)(tblRowItem.getItemProperty("Description").getValue())).getValue().isEmpty())
			{
				tblSysRole.select(itemId);
				return false;
			}
		}
		
		return true;
	}
	
	private boolean validateDuplicatedNames()
	{
		List<String> sysRoleNames = new ArrayList<String>();
		
		Collection itemIds = tblSysRole.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblSysRole.getContainerDataSource().getItem(itemId);
			
			if(sysRoleNames.indexOf(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue()) >= 0)
			{
				tblSysRole.select(itemId);
				return false;
			}
			else
				sysRoleNames.add(((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue());
		}
		
		return true;
	}
	
	private boolean validateNonSysAdminName()
	{	
		Collection itemIds = tblSysRole.getContainerDataSource().getItemIds();
		
		for (Object itemIdObj : itemIds) 
		{	
			int itemId = (int)itemIdObj;
			Item tblRowItem = tblSysRole.getContainerDataSource().getItem(itemId);
			
			if((((TextField)(tblRowItem.getItemProperty("Name").getValue())).getValue()).equals("sys_admin"))
			{
				tblSysRole.select(itemId);
				return false;
			}
		}
		
		return true;
	}
	
}
