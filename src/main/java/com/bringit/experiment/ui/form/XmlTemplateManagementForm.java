package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

import org.hibernate.annotations.Type;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.remote.RemoteFileUtil;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.bringit.experiment.ui.design.XmlTemplateManagementDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window.CloseEvent;

public class XmlTemplateManagementForm extends XmlTemplateManagementDesign  {
	
	Integer selectedRecordId = -1;
	
	public XmlTemplateManagementForm() {
		ResultSet vwXmlTemplateResults = new DataBaseViewDao().getViewResults("vwXmlTemplate");
		if(vwXmlTemplateResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblXmlTemplate, vwXmlTemplateResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxXmlTemplateViewFilters, vwXmlTemplateResults);
		}
		
		tblXmlTemplate.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
            	selectedRecordId = Integer.parseInt(event.getItemId().toString());
                if (event.isDoubleClick())
                	openXmlTemplateCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
                else 
                	tblXmlTemplate.select(event.getItemId());
            }
        });
		
		btnAddXmlTemplate.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openXmlTemplateCRUDModalWindow(-1);
			}
		});

		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterDbViewResults();
			}
			});
		
		btnViewNextExecution.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				viewNextExecution();
			}
		
		});		
	}
	
	private void viewNextExecution()
	{
		if(this.selectedRecordId == -1)
			return;
		
		List<String> csvStrDataXmlScheduledJobs = RemoteFileUtil.getInstance().getCsvStrDataOfXmlScheduledJobs();
		
		for(int i=0; csvStrDataXmlScheduledJobs != null && i<csvStrDataXmlScheduledJobs.size(); i++)
		{
			if(csvStrDataXmlScheduledJobs.get(i).split(",")[0].trim().equals(this.selectedRecordId.toString().trim()))
			{
				this.getUI().showNotification("Next Execution Time: " + csvStrDataXmlScheduledJobs.get(i).split(",")[1], Notification.TYPE_HUMANIZED_MESSAGE);
				return;
			}
		}
		
		this.getUI().showNotification("Template without Next Executions Scheduled.", Notification.TYPE_HUMANIZED_MESSAGE);
		
	}
	
	public void openXmlTemplateCRUDModalWindow(int xmlTemplateId)
	{
		 Window xmlTemplateCRUDModalWindow = new Window("Xml Template");
		 xmlTemplateCRUDModalWindow.setModal(true);
		 xmlTemplateCRUDModalWindow.setResizable(false);
		 xmlTemplateCRUDModalWindow.setContent(new XmlTemplateForm(xmlTemplateId));
		 xmlTemplateCRUDModalWindow.setWidth(993, Unit.PIXELS);
		 xmlTemplateCRUDModalWindow.setHeight(660, Unit.PIXELS);
		 xmlTemplateCRUDModalWindow.center();
		 xmlTemplateCRUDModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadXmlTemplateDbViewResults();
			}
		});
		 this.getUI().addWindow(xmlTemplateCRUDModalWindow);
    }

	private void reloadXmlTemplateDbViewResults()
	{
		ResultSet experimentViewResults = null;
		if(this.cbxXmlTemplateViewFilters.getValue() != null)
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwXmlTemplate", (String)this.cbxXmlTemplateViewFilters.getValue(), this.txtSearch.getValue());
		else
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwXmlTemplate", (String)this.cbxXmlTemplateViewFilters.getValue(), this.txtSearch.getValue());
		
		if(experimentViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(this.tblXmlTemplate, experimentViewResults, 1);
	}
	

	private void filterDbViewResults()
	{
		if(this.cbxXmlTemplateViewFilters.getValue() != null)
		{
			ResultSet experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwXmlTemplate", (String)this.cbxXmlTemplateViewFilters.getValue(), this.txtSearch.getValue());

			if(experimentViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(this.tblXmlTemplate, experimentViewResults, 1);
		}
	}
}
