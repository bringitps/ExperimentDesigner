package com.bringit.experiment.ui.form;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.bringit.experiment.ui.design.MainFormDesign;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;

@SuppressWarnings("serial")
public class MainForm extends MainFormDesign {
	
	@SuppressWarnings("unused")
	private WebApplication webApplication;
	SysUser sysUserSession = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
	
	
	@SuppressWarnings("deprecation")
	public MainForm(WebApplication webApplication) {
	    this.webApplication = webApplication;

	    for (Object id : treeMainMenu.rootItemIds()) {
	    	treeMainMenu.expandItemsRecursively(id);
        }
	    
	    treeMainMenu.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.getButton() == ItemClickEvent.BUTTON_LEFT)
                {
                	//TO Improve: Save Design Name into Tree Node to be able to use 2 Tree Nodes with same name
                	//webApplication.showNotification("Item Id:" + event.getItemId().toString(), Notification.TYPE_HUMANIZED_MESSAGE);
                	setFormContent(event.getItemId().toString());
                }
            }
        });
	}
	
	private void setFormContent(String treeItemClickedId)
	{
		 switch (treeItemClickedId.toLowerCase()) {
         case "manage experiments":  
     				formContentLayout.removeAllComponents();
     				formContentLayout.addComponent(new ExperimentManagementForm());
                  	break;
         case "manage xmltemplates":  
     				formContentLayout.removeAllComponents();
     				formContentLayout.addComponent(new XmlTemplateManagementForm());
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
         default:
         			break;
		 }
	}
}
