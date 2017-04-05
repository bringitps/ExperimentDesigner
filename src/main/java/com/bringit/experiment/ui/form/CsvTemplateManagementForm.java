package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.util.List;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.remote.RemoteFileUtil;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.bringit.experiment.ui.design.CsvTemplateManagementDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class CsvTemplateManagementForm extends CsvTemplateManagementDesign  {

	Integer selectedRecordId = -1;
	public CsvTemplateManagementForm() {
		ResultSet vwCsvTemplateResults = new DataBaseViewDao().getViewResults("vwCsvTemplate");
		if(vwCsvTemplateResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblCsvTemplate, vwCsvTemplateResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxCsvTemplateViewFilters, vwCsvTemplateResults);
		}
		
		tblCsvTemplate.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick())
                	openCsvTemplateCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
            }
        });
		
		btnAddCsvTemplate.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openCsvTemplateCRUDModalWindow(-1);
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
		
		List<String> csvStrDataCsvScheduledJobs = RemoteFileUtil.getInstance().getCsvStrDataOfCsvScheduledJobs();
		
		for(int i=0; csvStrDataCsvScheduledJobs != null && i<csvStrDataCsvScheduledJobs.size(); i++)
		{
			if(csvStrDataCsvScheduledJobs.get(i).split(",")[0].trim().equals(this.selectedRecordId.toString().trim()))
			{
				this.getUI().showNotification("Next Execution Time: " + csvStrDataCsvScheduledJobs.get(i).split(",")[1], Notification.TYPE_HUMANIZED_MESSAGE);
				return;
			}
		}
		
		this.getUI().showNotification("Template without Next Executions Scheduled.", Notification.TYPE_HUMANIZED_MESSAGE);
		
	}
	
	public void openCsvTemplateCRUDModalWindow(int csvTemplateId)
	{
		 Window csvTemplateCRUDModalWindow = new Window("Csv Template");
		 csvTemplateCRUDModalWindow.setModal(true);
		 csvTemplateCRUDModalWindow.setResizable(false);
		 csvTemplateCRUDModalWindow.setContent(new CsvTemplateForm(csvTemplateId));
		 csvTemplateCRUDModalWindow.setWidth(993, Unit.PIXELS);
		 csvTemplateCRUDModalWindow.setHeight(660, Unit.PIXELS);
		 csvTemplateCRUDModalWindow.center();
		 csvTemplateCRUDModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadCsvTemplateDbViewResults();
			}
		});
		 this.getUI().addWindow(csvTemplateCRUDModalWindow);
    }

	private void reloadCsvTemplateDbViewResults()
	{
		ResultSet experimentViewResults = null;
		if(this.cbxCsvTemplateViewFilters.getValue() != null)
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwCsvTemplate", (String)this.cbxCsvTemplateViewFilters.getValue(), this.txtSearch.getValue());
		else
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwCsvTemplate", (String)this.cbxCsvTemplateViewFilters.getValue(), this.txtSearch.getValue());
		
		if(experimentViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(this.tblCsvTemplate, experimentViewResults, 1);
	}
	

	private void filterDbViewResults()
	{
		if(this.cbxCsvTemplateViewFilters.getValue() != null)
		{
			ResultSet experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwCsvTemplate", (String)this.cbxCsvTemplateViewFilters.getValue(), this.txtSearch.getValue());

			if(experimentViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(this.tblCsvTemplate, experimentViewResults, 1);
		}
	}
}
