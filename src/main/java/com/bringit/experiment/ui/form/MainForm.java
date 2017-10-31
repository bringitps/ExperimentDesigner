package com.bringit.experiment.ui.form;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.FirstTimeYieldReport;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.FirstPassYieldReportDao;
import com.bringit.experiment.dao.FirstTimeYieldReportDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.ui.design.MainFormDesign;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ItemStyleGenerator;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MainForm extends MainFormDesign {

    @SuppressWarnings("unused")
    private WebApplication webApplication;
    private SystemSettings systemSettings;
    SysUser sysUserSession = (SysUser) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");


    @SuppressWarnings("deprecation")
    public MainForm(WebApplication webApplication, String selectedForm) {
        this.webApplication = webApplication;

        this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
        
        treeMainMenu.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
  		
        for (Object id : treeMainMenu.getItemIds()) 
        {
    	  	if("Experiments".equals(id.toString().trim()))
    	  		treeMainMenu.setItemCaption(id, this.systemSettings.getExperimentPluralLabel());
    	
    	  	if("Manage Experiments".equals(id.toString().trim()))
    	  		treeMainMenu.setItemCaption(id, "Manage " + this.systemSettings.getExperimentPluralLabel());
    	
    	  	if("Experiment Types".equals(id.toString().trim()))
    	  		treeMainMenu.setItemCaption(id, this.systemSettings.getExperimentTypePluralLabel());
       
    	}
        
        
        treeMainMenu.addContainerProperty("isExperimentDataReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("experimentId", Integer.class, null);

        for (Object id : treeMainMenu.rootItemIds()) {
            treeMainMenu.expandItemsRecursively(id);
        }

        treeMainMenu.collapseItem("Reports Builder");
        treeMainMenu.collapseItem("Configuration");
            
        List<Experiment> experimentsAvailable = new ExperimentDao().getActiveExperiments();
        if (experimentsAvailable != null) {
            for (int i = 0; i < experimentsAvailable.size(); i++) {
                Item item = treeMainMenu.addItem(experimentsAvailable.get(i).getExpName());
                item.getItemProperty("isExperimentDataReport").setValue(true);
                item.getItemProperty("experimentId").setValue(experimentsAvailable.get(i).getExpId());
                treeMainMenu.setParent(experimentsAvailable.get(i).getExpName(), "Data");
            }
        }

        treeMainMenu.addContainerProperty("isTargetReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("targetReportId", Integer.class, null);

        List<TargetReport> targetReportsAvailable = new TargetReportDao().getAllActiveTargetReports();
        if (targetReportsAvailable != null) {
            for (int i = 0; i < targetReportsAvailable.size(); i++) {
                Item item = treeMainMenu.addItem(targetReportsAvailable.get(i).getTargetReportName());
                item.getItemProperty("isTargetReport").setValue(true);
                item.getItemProperty("targetReportId").setValue(targetReportsAvailable.get(i).getTargetReportId());
                treeMainMenu.setParent(targetReportsAvailable.get(i).getTargetReportName(), "Target Reports");
            }
        }

        treeMainMenu.addContainerProperty("isFpyReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("fpyReportId", Integer.class, null);

        List<FirstPassYieldReport> fpyReportsAvailable = new FirstPassYieldReportDao().getAllFirstPassYieldReports();
        if (fpyReportsAvailable != null) {
            for (int i = 0; i < fpyReportsAvailable.size(); i++) {
                Item item = treeMainMenu.addItem(fpyReportsAvailable.get(i).getFpyReportName());
                item.getItemProperty("isFpyReport").setValue(true);
                item.getItemProperty("fpyReportId").setValue(fpyReportsAvailable.get(i).getFpyReportId());
                treeMainMenu.setParent(fpyReportsAvailable.get(i).getFpyReportName(), "First Pass Yield Reports");
            }
        }    

        treeMainMenu.addContainerProperty("isFtyReport", Boolean.class, null);
        treeMainMenu.addContainerProperty("ftyReportId", Integer.class, null);

        List<FirstTimeYieldReport> ftyReportsAvailable = new FirstTimeYieldReportDao().getAllFirstTimeYieldReports();
        if (ftyReportsAvailable != null) {
            for (int i = 0; i < ftyReportsAvailable.size(); i++) {
                Item item = treeMainMenu.addItem(ftyReportsAvailable.get(i).getFtyReportName());
                item.getItemProperty("isFtyReport").setValue(true);
                item.getItemProperty("ftyReportId").setValue(ftyReportsAvailable.get(i).getFtyReportId());
                treeMainMenu.setParent(ftyReportsAvailable.get(i).getFtyReportName(), "First Time Yield Reports");
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
                                && treeMainMenu.getItem(mainMenuItemIds.get(i)).getItemProperty("isFtyReport").getValue() == null)) {
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
                && treeItemClicked.getItemProperty("isFtyReport").getValue() == null)) {
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
                case "first time yield report":
                    formContentLayout.removeAllComponents();
                    formContentLayout.addComponent(new FirstTimeYieldReportForm());
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
        } else if (treeItemClicked.getItemProperty("ftyReportId").getValue() != null) {
            formContentLayout.removeAllComponents();
            formContentLayout.addComponent(new FirstTimeYieldReportDataForm((Integer) treeItemClicked.getItemProperty("ftyReportId").getValue()));
        }
    }
}
