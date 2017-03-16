package com.bringit.experiment.ui.form;

import java.util.List;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.remote.RemoteFileUtil;
import com.bringit.experiment.ui.design.MainFormDesign;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class MainForm extends MainFormDesign {
	
	@SuppressWarnings("unused")
	private WebApplication webApplication;
	SysUser sysUserSession = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
	
	
	@SuppressWarnings("deprecation")
	public MainForm(WebApplication webApplication) {
	    this.webApplication = webApplication;
	    
	    txtScheduledJobsInfo.setValue(RemoteFileUtil.getInstance().getScheduledJobs().toString());
	    //txtScheduledJobsInfo.setVisible(false);

	    treeMainMenu.addContainerProperty("isExperimentDataReport", Boolean.class, null);
	    treeMainMenu.addContainerProperty("experimentId", Integer.class, null);
	    
	    for (Object id : treeMainMenu.rootItemIds()) {
	    	treeMainMenu.expandItemsRecursively(id);
	    }
	    
	    List<Experiment> experimentsAvailable = new ExperimentDao().getActiveExperiments();
	    if(experimentsAvailable != null)
	    {
	    	for(int i=0; i<experimentsAvailable.size(); i++)
	    	{
	    		Item item = treeMainMenu.addItem(experimentsAvailable.get(i).getExpName());
	    		item.getItemProperty("isExperimentDataReport").setValue(true);
	    		item.getItemProperty("experimentId").setValue(experimentsAvailable.get(i).getExpId());
	    		treeMainMenu.setParent(experimentsAvailable.get(i).getExpName(), "Data");
	    	}
	    }

	    treeMainMenu.addContainerProperty("isTargetReport", Boolean.class, null);
	    treeMainMenu.addContainerProperty("targetReportId", Integer.class, null);
		
	    List<TargetReport> targetReportsAvailable = new TargetReportDao().getAllActiveTargetReports();
	    if(targetReportsAvailable != null)
	    {
	    	for(int i=0; i<targetReportsAvailable.size(); i++)
	    	{
	    		Item item = treeMainMenu.addItem(targetReportsAvailable.get(i).getTargetReportName());
	    		item.getItemProperty("isTargetReport").setValue(true);
	    		item.getItemProperty("targetReportId").setValue(targetReportsAvailable.get(i).getTargetReportId());
	    		treeMainMenu.setParent(targetReportsAvailable.get(i).getTargetReportName(), "Target Reports");
	    	}
	    }
	    	    
	    treeMainMenu.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.getButton() == ItemClickEvent.BUTTON_LEFT)
                	setFormContent(event.getItemId().toString(), treeMainMenu.getItem(event.getItemId()));
            }
        });
	}
	
	private void setFormContent(String itemClickedText, Item treeItemClicked)
	{
		if(treeItemClicked.getItemProperty("isExperimentDataReport").getValue() == null 
				&& treeItemClicked.getItemProperty("isTargetReport").getValue() == null )
    	{
			 switch (itemClickedText.toLowerCase()) 
			 {
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
			 	case "contract manufacturers":  
     				formContentLayout.removeAllComponents();
     				formContentLayout.addComponent(new ContractManufacturerConfigForm());
			 		break;
			 	default:
	         			break;
			 }
    	}
    	else if(treeItemClicked.getItemProperty("experimentId").getValue() != null)
    	{
    		formContentLayout.removeAllComponents();
			formContentLayout.addComponent(new ExperimentDataReportForm((Integer)treeItemClicked.getItemProperty("experimentId").getValue()));
        }
    	else if(treeItemClicked.getItemProperty("targetReportId").getValue() != null)
    	{
    		formContentLayout.removeAllComponents();
			formContentLayout.addComponent(new TargetDataReportForm((Integer)treeItemClicked.getItemProperty("targetReportId").getValue()));
        }
	}
}
