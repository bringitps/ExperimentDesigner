package com.bringit.experiment.ui.form;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.FinalPassYieldReport;
import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.FirstTimeYieldReport;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.bll.ViewHorizontalReport;
import com.bringit.experiment.bll.ViewVerticalReport;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.FinalPassYieldReportDao;
import com.bringit.experiment.dao.FirstPassYieldReportDao;
import com.bringit.experiment.dao.FirstTimeYieldReportDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.dao.ViewHorizontalReportDao;
import com.bringit.experiment.dao.ViewVerticalReportDao;
import com.bringit.experiment.ui.design.MainFormDesign;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ItemStyleGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MainForm extends MainFormDesign {

    @SuppressWarnings("unused")
    private WebApplication webApplication;
    private SystemSettings systemSettings;
    SysUser sysUserSession = (SysUser) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");

    //--- Loading BringIT Logo ---//
    String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    FileResource imgResource = new FileResource(new File(basePath + "/WEB-INF/classes/images/customer_logo_menu_v.png"));
    
    @SuppressWarnings("deprecation")
    public MainForm(WebApplication webApplication, String selectedForm) {

    	
    	this.webApplication = webApplication;

        this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
        
        customerLogo.setIcon(imgResource);

        treeMainMenu.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        treeMainMenu.addContainerProperty("isExperimentDataReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("experimentId", Integer.class, null);
        treeMainMenu.addContainerProperty("isTargetReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("targetReportId", Integer.class, null);
        treeMainMenu.addContainerProperty("isFpyReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("fpyReportId", Integer.class, null);
        treeMainMenu.addContainerProperty("isFnyReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("fnyReportId", Integer.class, null);
        treeMainMenu.addContainerProperty("isFtyReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("ftyReportId", Integer.class, null);
        treeMainMenu.addContainerProperty("isVwVerticalReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("isVwHorizontalReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("vwVerticalReportId", Integer.class, null);
        treeMainMenu.addContainerProperty("vwHorizontalReportId", Integer.class, null);

        
        for (Object id : treeMainMenu.getItemIds()) 
        {
    	  	if("Experiments".equals(id.toString().trim()))
    	  		treeMainMenu.setItemCaption(id, this.systemSettings.getExperimentPluralLabel());
    	
    	  	if("Manage Experiments".equals(id.toString().trim()))
    	  		treeMainMenu.setItemCaption(id, "Manage " + this.systemSettings.getExperimentPluralLabel());
    	
    	  	if("Experiment Types".equals(id.toString().trim()))
    	  		treeMainMenu.setItemCaption(id, this.systemSettings.getExperimentTypePluralLabel());       
    	}        
      
        for (Object id : treeMainMenu.rootItemIds()) {
            treeMainMenu.collapseItemsRecursively(id);
        }
        
        /*for (Object itemId: treeMainMenu.getItemIds())
        	treeMainMenu.expandItem(itemId);*/
		
        treeMainMenu.expandItem("Menu");
        
        List<String> dataMenuItemAdded = new ArrayList<String>();
        
        /*treeMainMenu.collapseItem("Experiments");
        treeMainMenu.collapseItem("Scheduled Jobs");
        treeMainMenu.collapseItem("Data Visualization");
        treeMainMenu.collapseItem("Reports Builder");
        treeMainMenu.collapseItem("Configuration");*/
            
        List<Experiment> experimentsAvailable = new ExperimentDao().getActiveExperiments();
        if (experimentsAvailable != null) {
            for (int i = 0; i < experimentsAvailable.size(); i++) {
            	if(experimentsAvailable.get(i).getExpName() != null && !experimentsAvailable.get(i).getExpName().isEmpty() && dataMenuItemAdded.indexOf(experimentsAvailable.get(i).getExpName()) == -1)
            	{
            		dataMenuItemAdded.add(experimentsAvailable.get(i).getExpName()); 
            		Item item = treeMainMenu.addItem(experimentsAvailable.get(i).getExpName());
            		item.getItemProperty("isExperimentDataReport").setValue(true);
            		item.getItemProperty("experimentId").setValue(experimentsAvailable.get(i).getExpId());
            		treeMainMenu.setParent(experimentsAvailable.get(i).getExpName(), "Data");
            	}
            }
        }

        List<TargetReport> targetReportsAvailable = new TargetReportDao().getAllActiveTargetReports();
        if (targetReportsAvailable != null) {
            for (int i = 0; i < targetReportsAvailable.size(); i++) {
            	if(targetReportsAvailable.get(i).getTargetReportName() != null && !targetReportsAvailable.get(i).getTargetReportName().isEmpty() && dataMenuItemAdded.indexOf(targetReportsAvailable.get(i).getTargetReportName()) == -1)
            	{
            		dataMenuItemAdded.add(targetReportsAvailable.get(i).getTargetReportName()); 
            		Item item = treeMainMenu.addItem(targetReportsAvailable.get(i).getTargetReportName());
            		item.getItemProperty("isTargetReport").setValue(true);
            		item.getItemProperty("targetReportId").setValue(targetReportsAvailable.get(i).getTargetReportId());
            		treeMainMenu.setParent(targetReportsAvailable.get(i).getTargetReportName(), "Target Reports");
            	}
            }
        }
        
        List<FirstPassYieldReport> fpyReportsAvailable = new FirstPassYieldReportDao().getAllFirstPassYieldReports();
        if (fpyReportsAvailable != null) {
            for (int i = 0; i < fpyReportsAvailable.size(); i++) {
            	if(fpyReportsAvailable.get(i).getFpyReportName() != null && !fpyReportsAvailable.get(i).getFpyReportName().isEmpty() && dataMenuItemAdded.indexOf(fpyReportsAvailable.get(i).getFpyReportName()) == -1)
            	{
            		dataMenuItemAdded.add(fpyReportsAvailable.get(i).getFpyReportName()); 
            		Item item = treeMainMenu.addItem(fpyReportsAvailable.get(i).getFpyReportName());
            		item.getItemProperty("isFpyReport").setValue(true);
               		item.getItemProperty("fpyReportId").setValue(fpyReportsAvailable.get(i).getFpyReportId());
               		treeMainMenu.setParent(fpyReportsAvailable.get(i).getFpyReportName(), "First Pass Yield Reports");
            	}
            }
        }   

        List<FinalPassYieldReport> fnyReportsAvailable = new FinalPassYieldReportDao().getAllFinalPassYieldReports();
        if (fnyReportsAvailable != null) {
            for (int i = 0; i < fnyReportsAvailable.size(); i++) {
            	if(fnyReportsAvailable.get(i).getFnyReportName() != null && !fnyReportsAvailable.get(i).getFnyReportName().isEmpty() && dataMenuItemAdded.indexOf(fnyReportsAvailable.get(i).getFnyReportName()) == -1)
            	{
            		dataMenuItemAdded.add(fnyReportsAvailable.get(i).getFnyReportName()); 
	                Item item = treeMainMenu.addItem(fnyReportsAvailable.get(i).getFnyReportName());
	                item.getItemProperty("isFnyReport").setValue(true);
	                item.getItemProperty("fnyReportId").setValue(fnyReportsAvailable.get(i).getFnyReportId());
	                treeMainMenu.setParent(fnyReportsAvailable.get(i).getFnyReportName(), "Final Pass Yield Reports");
            	}
            }
        }  
        
        List<FirstTimeYieldReport> ftyReportsAvailable = new FirstTimeYieldReportDao().getAllFirstTimeYieldReports();
        if (ftyReportsAvailable != null) {
            for (int i = 0; i < ftyReportsAvailable.size(); i++) {
            	if(ftyReportsAvailable.get(i).getFtyReportName() != null && !ftyReportsAvailable.get(i).getFtyReportName().isEmpty() && dataMenuItemAdded.indexOf(ftyReportsAvailable.get(i).getFtyReportName()) == -1)
            	{
            		dataMenuItemAdded.add(ftyReportsAvailable.get(i).getFtyReportName()); 
	                Item item = treeMainMenu.addItem(ftyReportsAvailable.get(i).getFtyReportName());
	                item.getItemProperty("isFtyReport").setValue(true);
	                item.getItemProperty("ftyReportId").setValue(ftyReportsAvailable.get(i).getFtyReportId());
	                treeMainMenu.setParent(ftyReportsAvailable.get(i).getFtyReportName(), "First Time Yield Reports");
	           
            	}
            }
        }         

        List<ViewVerticalReport> vwVerticalReportsAvailable = new ViewVerticalReportDao().getAllViewVerticalReports();
        if (vwVerticalReportsAvailable != null) {
            for (int i = 0; i < vwVerticalReportsAvailable.size(); i++) {
            	if(vwVerticalReportsAvailable.get(i).getVwVerticalRptName() != null && !vwVerticalReportsAvailable.get(i).getVwVerticalRptName().isEmpty() && dataMenuItemAdded.indexOf(vwVerticalReportsAvailable.get(i).getVwVerticalRptName()) == -1)
            	{
            		dataMenuItemAdded.add(vwVerticalReportsAvailable.get(i).getVwVerticalRptName());            		
                	Item item = treeMainMenu.addItem(vwVerticalReportsAvailable.get(i).getVwVerticalRptName());
            		item.getItemProperty("isVwVerticalReport").setValue(true);
            		item.getItemProperty("vwVerticalReportId").setValue(vwVerticalReportsAvailable.get(i).getVwVerticalRptId());
            		treeMainMenu.setParent(vwVerticalReportsAvailable.get(i).getVwVerticalRptName(), "Vertical View Reports");
            	}
            }
        }   

        List<ViewHorizontalReport> vwHorizontalReportsAvailable = new ViewHorizontalReportDao().getAllViewHorizontalReports();
        if (vwHorizontalReportsAvailable != null) {
            for (int i = 0; i < vwHorizontalReportsAvailable.size(); i++) {
            	if(vwHorizontalReportsAvailable.get(i).getVwHorizontalRptName() != null && !vwHorizontalReportsAvailable.get(i).getVwHorizontalRptName().isEmpty() && dataMenuItemAdded.indexOf(vwHorizontalReportsAvailable.get(i).getVwHorizontalRptName()) == -1)
            	{
            		dataMenuItemAdded.add(vwHorizontalReportsAvailable.get(i).getVwHorizontalRptName());            		
                	Item item = treeMainMenu.addItem(vwHorizontalReportsAvailable.get(i).getVwHorizontalRptName());
            		item.getItemProperty("isVwHorizontalReport").setValue(true);
            		item.getItemProperty("vwHorizontalReportId").setValue(vwHorizontalReportsAvailable.get(i).getVwHorizontalRptId());
            		treeMainMenu.setParent(vwHorizontalReportsAvailable.get(i).getVwHorizontalRptName(), "Horizontal View Reports");
            	}
            }
        } 
        
        treeMainMenu.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.getButton() == ItemClickEvent.BUTTON_LEFT)
                    setFormContent(event.getItemId().toString(), treeMainMenu.getItem(event.getItemId()));
            }
        });

        if (selectedForm != null)
            setFormContent(selectedForm, null);
    }
    
    public void setPanelSessionMenuBar(MenuBar mnuSession)
    {
    	this.panelSession.setContent(mnuSession);
    }

    public void updateMenuLabels()
    {
    	 for (Object id : treeMainMenu.getItemIds()) 
         {
     	  	if("Experiments".equals(id.toString().trim()))
     	  		treeMainMenu.setItemCaption(id, this.systemSettings.getExperimentPluralLabel());
     	
     	  	if("Manage Experiments".equals(id.toString().trim()))
     	  		treeMainMenu.setItemCaption(id, "Manage " + this.systemSettings.getExperimentPluralLabel());
     	
     	  	if("Experiment Types".equals(id.toString().trim()))
     	  		treeMainMenu.setItemCaption(id, this.systemSettings.getExperimentTypePluralLabel());
         }
    }
    
    public void updateMenuAccess() {

        SysRole sysRoleSession = (SysRole) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");
        if (sysRoleSession != null) {
            if (!"sys_admin".equals(sysRoleSession.getRoleName())) {
                if (sysRoleSession.getRoleMenuAccess() != null && !sysRoleSession.getRoleMenuAccess().trim().isEmpty()) {
                    //-- Starts Hide/Show Not granted or granted Menu Items --//

                    List<String> mnuAccessTrimmed = new ArrayList<String>();
                    List<String> mnuParentToKeep = new ArrayList<String>();

                    String[] mnuAccess = sysRoleSession.getRoleMenuAccess().split("\n");

                    for (int i = 0; i < mnuAccess.length; i++) {
                        System.out.println("Menu Access:" + mnuAccess[i]);
                        if (!mnuAccess[i].contains("/") && mnuParentToKeep.indexOf(mnuAccess[i].trim()) == -1)
                            mnuParentToKeep.add(mnuAccess[i].trim());
                        else {
                            String[] mnuAccessFullTree = mnuAccess[i].split("/");
                            mnuAccessTrimmed.add(mnuAccessFullTree[mnuAccessFullTree.length - 1].trim());

                            for (int j = 0; j < mnuAccessFullTree.length - 1; j++) {
                                if (!mnuAccessFullTree[j].trim().isEmpty() && mnuParentToKeep.indexOf(mnuAccessFullTree[j].trim()) == -1)
                                    mnuParentToKeep.add(mnuAccessFullTree[j].trim());
                            }
                        }
                    }


                    System.out.println("Menu Parent To Keep: " + mnuParentToKeep);

                    //Get Root Item
                    String rootItemId = "";
                    for (Object id : treeMainMenu.rootItemIds())
                        rootItemId = id.toString().trim();

                    List<String> mainMenuItemIds = new ArrayList<String>();
                    for (Object id : treeMainMenu.getItemIds()) {
                        if (!id.toString().trim().equals(rootItemId))
                            mainMenuItemIds.add(id.toString().trim());
                    }

                    //Remove Not Granted Parents
                    for (int i = 0; i < mainMenuItemIds.size(); i++) {
                        if (treeMainMenu.getItem(mainMenuItemIds.get(i)) != null && treeMainMenu.getChildren(mainMenuItemIds.get(i)) != null
                                && mnuParentToKeep.indexOf(mainMenuItemIds.get(i).toString().trim()) == -1
                                && mnuAccessTrimmed.indexOf(mainMenuItemIds.get(i).toString().trim()) == -1) {
                            System.out.println("Removing: " + mainMenuItemIds.get(i));
                            treeMainMenu.removeItem(mainMenuItemIds.get(i));
                        }
                    }

                    //Remove Not Granted Children
                    for (int i = 0; i < mainMenuItemIds.size(); i++) {
                        if (treeMainMenu.getParent(mainMenuItemIds.get(i)) != null
                                && (treeMainMenu.getItem(mainMenuItemIds.get(i)).getItemProperty("isExperimentDataReport").getValue() == null
                                && treeMainMenu.getItem(mainMenuItemIds.get(i)).getItemProperty("isTargetReport").getValue() == null
                                && treeMainMenu.getItem(mainMenuItemIds.get(i)).getItemProperty("isFpyReport").getValue() == null
                                && treeMainMenu.getItem(mainMenuItemIds.get(i)).getItemProperty("isFnyReport").getValue() == null
                                && treeMainMenu.getItem(mainMenuItemIds.get(i)).getItemProperty("isFtyReport").getValue() == null
                                && treeMainMenu.getItem(mainMenuItemIds.get(i)).getItemProperty("isVwVerticalReport").getValue() == null
                                && treeMainMenu.getItem(mainMenuItemIds.get(i)).getItemProperty("isVwHorizontalReport").getValue() == null)) {
                            if (mnuAccessTrimmed.indexOf(mainMenuItemIds.get(i).toString().trim()) == -1) {
                                if (treeMainMenu.getChildren(mainMenuItemIds.get(i)) == null)
                                    treeMainMenu.removeItem(mainMenuItemIds.get(i));

                            }
                        }
                    }

                    List<String> mainMenuWithoutParentItemIds = new ArrayList<String>();
                    for (Object id : treeMainMenu.rootItemIds()) {
                        if (!id.toString().trim().equals(rootItemId))
                            mainMenuWithoutParentItemIds.add(id.toString().trim());
                    }

                    for (int i = 0; i < mainMenuWithoutParentItemIds.size(); i++)
                        treeMainMenu.removeItem(mainMenuWithoutParentItemIds.get(i));

                    //-- Finishes Hide/Show Not granted or granted Menu Items --//

                    treeMainMenu.setVisible(true);
                } else
                    treeMainMenu.setVisible(false);
            }
        }
    }

    private void setFormContent(String itemClickedText, Item treeItemClicked) {
    	
        if (treeItemClicked == null || (treeItemClicked.getItemProperty("isExperimentDataReport").getValue() == null
                && treeItemClicked.getItemProperty("isTargetReport").getValue() == null
                && treeItemClicked.getItemProperty("isFpyReport").getValue() == null
                && treeItemClicked.getItemProperty("isFnyReport").getValue() == null
                && treeItemClicked.getItemProperty("isFtyReport").getValue() == null
                && treeItemClicked.getItemProperty("isVwVerticalReport").getValue() == null
                && treeItemClicked.getItemProperty("isVwHorizontalReport").getValue() == null)) {
            switch (itemClickedText.toLowerCase()) {
                case "manage experiments":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new ExperimentManagementForm());
                    break;
                case "manage xmltemplates":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new XmlTemplateManagementForm());
                    break;
                case "manage csvtemplates":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new CsvTemplateManagementForm());
                    break;
                case "unit of measures":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new UnitOfMeasureConfigForm());
                    break;
                case "files repositories":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new FilesRepositoriesConfigForm());
                    break;
                case "job exec repeat":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new JobExecutionRepeatConfigForm());
                    break;
                case "xml data file loads":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new XmlDataFileLoadForm());
                    break;
                case "csv data file loads":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new CsvDataFileLoadForm());
                    break;
                case "target report":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new TargetReportForm());
                    break;
                case "first pass yield report":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new FirstPassYieldReportForm());
                    break;
                case "final pass yield report":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new FinalPassYieldReportForm());
                    break;
                case "first time yield report":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new FirstTimeYieldReportForm());
                    break;
                case "vertical view report":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new ViewVerticalReportForm());
                    break;
                case "horizontal view report":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new ViewHorizontalReportForm());
                    break;
                case "contract manufacturers":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new ContractManufacturerConfigForm());
                    break;
                case "users":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new SysUserConfigForm());
                    break;
                case "user roles":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new SysRoleConfigForm(treeMainMenu));
                    break;
                case "experiment types":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new ExperimentTypeConfigForm());
                    break;
                case "smtp":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new SmtpConfigForm());
                    break;
                case "general settings":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new SystemSettingsForm());
                    break;
                case "custom lists":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new CustomListManagementForm());
                    break;
                case "spc frequency histogram":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new FrequencyHistogramForm());
                    break;
                case "spc x-bar r":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new XBarRForm());
                    break;
                default:
                    break;
            }
        } else if (treeItemClicked.getItemProperty("experimentId").getValue() != null) {
            formContentLayout.removeAllComponents();
            formContentLayout.addComponent(new ExperimentDataReportForm((Integer) treeItemClicked.getItemProperty("experimentId").getValue()));
        } else if (treeItemClicked.getItemProperty("targetReportId").getValue() != null) {
            formContentLayout.removeAllComponents();
            formContentLayout.addComponent(new TargetDataReportForm((Integer) treeItemClicked.getItemProperty("targetReportId").getValue()));
        } else if (treeItemClicked.getItemProperty("fpyReportId").getValue() != null) {
            formContentLayout.removeAllComponents();
            formContentLayout.addComponent(new FirstPassYieldReportDataForm((Integer) treeItemClicked.getItemProperty("fpyReportId").getValue()));
        } else if (treeItemClicked.getItemProperty("fnyReportId").getValue() != null) {
            formContentLayout.removeAllComponents();
            formContentLayout.addComponent(new FinalPassYieldReportDataForm((Integer) treeItemClicked.getItemProperty("fnyReportId").getValue()));
        } else if (treeItemClicked.getItemProperty("ftyReportId").getValue() != null) {
            formContentLayout.removeAllComponents();
            formContentLayout.addComponent(new FirstTimeYieldReportDataForm((Integer) treeItemClicked.getItemProperty("ftyReportId").getValue()));
        } else if (treeItemClicked.getItemProperty("vwVerticalReportId").getValue() != null) {
            formContentLayout.removeAllComponents();
            formContentLayout.addComponent(new ViewVerticalReportDataForm((Integer) treeItemClicked.getItemProperty("vwVerticalReportId").getValue()));
        } else if (treeItemClicked.getItemProperty("vwHorizontalReportId").getValue() != null) {
            formContentLayout.removeAllComponents();
            formContentLayout.addComponent(new ViewHorizontalReportDataForm((Integer) treeItemClicked.getItemProperty("vwHorizontalReportId").getValue()));
        }
    }
}