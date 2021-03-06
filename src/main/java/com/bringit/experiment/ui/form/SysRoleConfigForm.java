package com.bringit.experiment.ui.form;

import com.bringit.experiment.bll.CmForSysRole;
import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.CmForSysRoleDao;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.dao.SysRoleDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.UserRoleDao;
import com.bringit.experiment.ui.design.SysRoleConfigDesign;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SysRoleConfigForm extends SysRoleConfigDesign {

	private int lastNewItemId = 0;
	private List<Integer> dbIdOfItemsToDelete = new ArrayList<Integer>();
	private Tree treeMainMenu;// = new ArrayList<String>();
	private SystemSettings systemSettings;
	   
	public SysRoleConfigForm(Tree treeMainMenu)
	{
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
        
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
		tblSysRole.addContainerProperty("Contract Manufacturer", Panel.class, null);
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
		
		Object[] itemValues = new Object[6];

		List<SysRole> sysRoles = new SysRoleDao().getAllSysRoles();
		
		for(int i=0; sysRoles != null && i<sysRoles.size(); i++)
		{
			if (filterName.isEmpty() || (filterName.trim().equals("Name") && sysRoles.get(i).getRoleName().toLowerCase().contains(filterValue.trim().toLowerCase()))
				|| (filterName.trim().equals("Description")
				&& sysRoles.get(i).getRoleDescription().toLowerCase().contains(filterValue.trim().toLowerCase())))

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


				Panel pnlCntManufacturer = new Panel("Contract Manufacturer");
				VerticalLayout layoutPnlCntManufacturer = new VerticalLayout();
				OptionGroup optGrpCntManufacturer = new OptionGroup();
				optGrpCntManufacturer.setMultiSelect(true);
				optGrpCntManufacturer.setStyleName("small");
				pnlCntManufacturer.setHeight(200, Unit.PIXELS);
				pnlCntManufacturer.setWidth(100, Unit.PERCENTAGE);
				pnlCntManufacturer.setStyleName("well");
				pnlCntManufacturer.setCaption("");

				
			    for (Object id : treeMainMenu.getItemIds()) 
			    {
					if(treeMainMenu.getParent(id) != null && (treeMainMenu.getItem(id).getItemProperty("isExperimentDataReport").getValue() == null 
							&& (treeMainMenu.getItem(id).getItemProperty("isTargetReport").getValue() == null)
							&& (treeMainMenu.getItem(id).getItemProperty("isFpyReport").getValue() == null)
							&& (treeMainMenu.getItem(id).getItemProperty("isFtyReport").getValue() == null)))
					{
						if(treeMainMenu.getChildren(id) != null && !isDynamicChildren(treeMainMenu.getChildren(id)))
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

			    Collection menuItemIds = optGrpMnuAccess.getContainerDataSource().getItemIds();
				
				for (Object menuItemId : menuItemIds) 
				{	
					if("Experiments".equals(menuItemId.toString().trim()))
						optGrpMnuAccess.setItemCaption(menuItemId, this.systemSettings.getExperimentPluralLabel());
		     	
		     	  	if("Manage Experiments".equals(menuItemId.toString().trim()))
		     	  		optGrpMnuAccess.setItemCaption(menuItemId, "Manage " + this.systemSettings.getExperimentPluralLabel());
		     	
		     	  	if("Experiment Types".equals(menuItemId.toString().trim()))
		     	  		optGrpMnuAccess.setItemCaption(menuItemId, this.systemSettings.getExperimentTypePluralLabel());
		     	  	
		    		if(menuItemId.toString().trim().startsWith("Experiments /"))
						optGrpMnuAccess.setItemCaption(menuItemId, optGrpMnuAccess.getItemCaption(menuItemId).replace("Experiments /", this.systemSettings.getExperimentPluralLabel() + " /"));
		     	
		    		if(menuItemId.toString().trim().endsWith("/ Manage Experiments"))
						optGrpMnuAccess.setItemCaption(menuItemId, optGrpMnuAccess.getItemCaption(menuItemId).replace("/ Manage Experiments", "/ Manage " + this.systemSettings.getExperimentPluralLabel()));
		     	
		    		if(menuItemId.toString().trim().endsWith("/ Experiment Types"))
						optGrpMnuAccess.setItemCaption(menuItemId, optGrpMnuAccess.getItemCaption(menuItemId).replace("/ Experiment Types", "/ " + this.systemSettings.getExperimentTypePluralLabel()));
		     	}
			    			    
				setContractManagerOptions(optGrpCntManufacturer, sysRoles.get(i));

				layoutPnlMnuAccess.addComponent(optGrpMnuAccess);
				pnlMnuAccess.setContent(layoutPnlMnuAccess);

				layoutPnlCntManufacturer.addComponent(optGrpCntManufacturer);
				pnlCntManufacturer.setContent(layoutPnlCntManufacturer);
				    
				itemValues[4] = pnlMnuAccess;
				itemValues[5] = pnlCntManufacturer;

				if (!sysRoles.get(i).getRoleName().equals("sys_admin"))
					tblSysRole.addItem(itemValues, sysRoles.get(i).getRoleId());
			}
		}
	}
		
	private void addSysRoleRow()
	{
		Object[] itemValues = new Object[6];
		
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


		Panel pnlContManager = new Panel("Contract Manufacturer");
		VerticalLayout layoutPnlContManager = new VerticalLayout();
		OptionGroup optGrpContManager = new OptionGroup();
		optGrpContManager.setMultiSelect(true);
		optGrpContManager.setStyleName("small");
		pnlContManager.setHeight(200, Unit.PIXELS);
		pnlContManager.setWidth(100, Unit.PERCENTAGE);
		pnlContManager.setStyleName("well");
		pnlContManager.setCaption("");
		    
	    for (Object id : treeMainMenu.getItemIds()) 
	    {
			if(treeMainMenu.getParent(id) != null && (treeMainMenu.getItem(id).getItemProperty("isExperimentDataReport").getValue() == null 
					&& (treeMainMenu.getItem(id).getItemProperty("isTargetReport").getValue() == null)
					&& (treeMainMenu.getItem(id).getItemProperty("isFpyReport").getValue() == null)
					&& (treeMainMenu.getItem(id).getItemProperty("isFtyReport").getValue() == null)))
			{
				if(treeMainMenu.getChildren(id) != null && !isDynamicChildren(treeMainMenu.getChildren(id)))
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
	    
	    Collection menuItemIds = optGrpMnuAccess.getContainerDataSource().getItemIds();
		
		for (Object menuItemId : menuItemIds) 
		{	
			if("Experiments".equals(menuItemId.toString().trim()))
				optGrpMnuAccess.setItemCaption(menuItemId, this.systemSettings.getExperimentPluralLabel());
     	
     	  	if("Manage Experiments".equals(menuItemId.toString().trim()))
     	  		optGrpMnuAccess.setItemCaption(menuItemId, "Manage " + this.systemSettings.getExperimentPluralLabel());
     	
     	  	if("Experiment Types".equals(menuItemId.toString().trim()))
     	  		optGrpMnuAccess.setItemCaption(menuItemId, this.systemSettings.getExperimentTypePluralLabel());
     
    		if(menuItemId.toString().trim().startsWith("Experiments /"))
				optGrpMnuAccess.setItemCaption(menuItemId, optGrpMnuAccess.getItemCaption(menuItemId).replace("Experiments /", this.systemSettings.getExperimentPluralLabel() + " /"));
     	
    		if(menuItemId.toString().trim().endsWith("/ Manage Experiments"))
				optGrpMnuAccess.setItemCaption(menuItemId, optGrpMnuAccess.getItemCaption(menuItemId).replace("/ Manage Experiments", "/ Manage " + this.systemSettings.getExperimentPluralLabel()));
     	
    		if(menuItemId.toString().trim().endsWith("/ Experiment Types"))
				optGrpMnuAccess.setItemCaption(menuItemId, optGrpMnuAccess.getItemCaption(menuItemId).replace("/ Experiment Types", "/ " + this.systemSettings.getExperimentTypePluralLabel()));
     	}
	    
	    layoutPnlMnuAccess.addComponent(optGrpMnuAccess);
		pnlMnuAccess.setContent(layoutPnlMnuAccess);

		setContractManagerOptions(optGrpContManager, null);

		layoutPnlContManager.addComponent(optGrpContManager);
		pnlContManager.setContent(layoutPnlContManager);
		    
		itemValues[4] = pnlMnuAccess;
		itemValues[5] = pnlContManager;

		this.lastNewItemId = this.lastNewItemId - 1;
		tblSysRole.addItem(itemValues, this.lastNewItemId);
		tblSysRole.select(this.lastNewItemId);
	}

	private void setContractManagerOptions(OptionGroup optGrpContManager, SysRole sysRole) {

		List selectedCM =  new ArrayList<>();
		List<CmForSysRole> availableCmForSysRoles;
		if (sysRole != null) {
			availableCmForSysRoles = new CmForSysRoleDao().getListOfCmForSysRoleBysysRoleId(sysRole.getRoleId());
			if (availableCmForSysRoles != null) {
				availableCmForSysRoles.forEach(s -> selectedCM.add(s.getContractManufacturer().getCmName()));
			}
		}

		List<ContractManufacturer> availableContractManufacturers;

		availableContractManufacturers = new ContractManufacturerDao().getAllContractManufacturers();
		if (availableContractManufacturers != null) {
			for (ContractManufacturer contractManufacturer : availableContractManufacturers) {
				optGrpContManager.addItem(contractManufacturer.getCmName());
				if (selectedCM.indexOf(contractManufacturer.getCmName()) >= 0)
					optGrpContManager.select(contractManufacturer.getCmName());
			}
		}
	}

	private boolean isDynamicChildren(Collection<?> mnuItemChildren) {
		for (Object id : mnuItemChildren) {
			if (treeMainMenu.getItem(id).getItemProperty("isExperimentDataReport").getValue() == null
					&& (treeMainMenu.getItem(id).getItemProperty("isTargetReport").getValue() == null)
					&& (treeMainMenu.getItem(id).getItemProperty("isFpyReport").getValue() == null)
					&& (treeMainMenu.getItem(id).getItemProperty("isFtyReport").getValue() == null))
				return false;
		}
		return true;
	}

	private void deleteSysRoleRow() {
		boolean hasUsersLinked = false;
		if (tblSysRole.getValue() != null && (int) tblSysRole.getValue() > 0) {
			if (new UserRoleDao().getUserRolesByRole(new SysRoleDao().getRoleById((int) tblSysRole.getValue())).size() <= 0)
				dbIdOfItemsToDelete.add((int) tblSysRole.getValue());
			else
				hasUsersLinked = true;
		}

		if (!hasUsersLinked && tblSysRole.getValue() != null)
			tblSysRole.removeItem((int) tblSysRole.getValue());
		else if (hasUsersLinked)
			this.getUI().showNotification("User Role record can not be deleted. \nThere are Users linked.", Type.WARNING_MESSAGE);

	}

	private void saveSysRoleRows() {
		boolean validateRequiredFieldsResult = validateRequiredFields();
		boolean validateDuplicatedNamesResult = validateDuplicatedNames();
		boolean validateNonSysAdminNameResult = validateNonSysAdminName();

		if (validateRequiredFieldsResult && validateDuplicatedNamesResult && validateNonSysAdminNameResult) {
			SysRoleDao sysRoleDao = new SysRoleDao();

			//Delete Items in DB
			for (int i = 0; i < dbIdOfItemsToDelete.size(); i++)
				sysRoleDao.deleteSysRole((int) dbIdOfItemsToDelete.get(i));

			Collection itemIds = tblSysRole.getContainerDataSource().getItemIds();

			for (Object itemIdObj : itemIds) {
				int itemId = (int) itemIdObj;
				Item tblRowItem = tblSysRole.getContainerDataSource().getItem(itemId);

				SysRole sysRole = new SysRole();
				sysRole.setRoleName(((TextField) (tblRowItem.getItemProperty("Name").getValue())).getValue());
				sysRole.setRoleDescription(((TextField) (tblRowItem.getItemProperty("Description").getValue())).getValue());
				sysRole.setIsActiveDirectoryDefaultRole(((CheckBox) (tblRowItem.getItemProperty("Default Role?").getValue())).getValue());

				Panel pnlMnuAccess = ((Panel) (tblRowItem.getItemProperty("Menu Access").getValue()));
				VerticalLayout layoutPnlMnuAccess = (VerticalLayout) pnlMnuAccess.getContent();
				OptionGroup optGrpMnuAccess = (OptionGroup) layoutPnlMnuAccess.getComponent(0);

				Set<Item> selectedOptGrpMnuAccess = (Set<Item>) optGrpMnuAccess.getValue();
				for (Object selectedOptionGroup : selectedOptGrpMnuAccess)
					sysRole.setRoleMenuAccess((sysRole.getRoleMenuAccess() == null ? "" : sysRole.getRoleMenuAccess()) + selectedOptionGroup.toString() + "\n");

				Panel pnlCntManufacturer = ((Panel) (tblRowItem.getItemProperty("Contract Manufacturer").getValue()));
				VerticalLayout layoutPnlCntManufacturer = (VerticalLayout) pnlCntManufacturer.getContent();
				OptionGroup optGrpCntManufacturer = (OptionGroup) layoutPnlCntManufacturer.getComponent(0);



				if (itemId > 0) {
					sysRole.setRoleId(itemId);
					sysRoleDao.updateSysRole(sysRole);
				} else
					sysRoleDao.addSysRole(sysRole);

				Set<Item> selectedOptGrpCntManufacturer = (Set<Item>) optGrpCntManufacturer.getValue();
				List<Integer> contractManufacturerList = new ArrayList<>();
				for (Object selectedOptionGroup : selectedOptGrpCntManufacturer) {
					System.out.println("selectedOptionGroup.toString() = " + selectedOptionGroup.toString());
					List<ContractManufacturer> availableCmForSysRoles = new ContractManufacturerDao().getAllContractManufacturers();
					if (availableCmForSysRoles != null) {

						List<ContractManufacturer> contractManufacturerList1 = availableCmForSysRoles.stream()
								.filter((contractManufacturer) -> contractManufacturer.getCmName().equalsIgnoreCase(selectedOptionGroup.toString()))
								.collect(Collectors.toList());

						contractManufacturerList1.forEach(x-> contractManufacturerList.add(x.getCmId()));
					}
				}

				new CmForSysRoleDao().addUpdateCmForSysRoleList(contractManufacturerList, sysRole.getRoleId());
			}

			this.getUI().showNotification("Data Saved.", Type.HUMANIZED_MESSAGE);
			cbxSysRoleFilters.select("Name");
			txtSearch.setValue("");
			loadTblData("", "");
		} else if (!validateRequiredFieldsResult)
			this.getUI().showNotification("Name and Description must be set for New User Role Records", Type.WARNING_MESSAGE);
		else if (!validateDuplicatedNamesResult)
			this.getUI().showNotification("Name of User Role can not be duplicated.", Type.WARNING_MESSAGE);
		else if (!validateNonSysAdminNameResult)
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
