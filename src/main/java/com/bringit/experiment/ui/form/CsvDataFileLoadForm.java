package com.bringit.experiment.ui.form;



import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.dao.CsvTemplateDao;
import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.CsvDataFileLoadDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class CsvDataFileLoadForm extends CsvDataFileLoadDesign{

	private SystemSettings systemSettings;
	
	public CsvDataFileLoadForm() {
		
		ResultSet vwCsvDataLoadExecResults = new DataBaseViewDao().getViewResults("vwCsvDataLoadExecutionResult");
		if(vwCsvDataLoadExecResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblCsvDataFileLoads, vwCsvDataLoadExecResults, 1);
			tblCsvDataFileLoads.setSortContainerPropertyId("Execution Date");
			tblCsvDataFileLoads.setSortAscending(false);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxCsvDataFileLoadsViewFilters, vwCsvDataLoadExecResults);
			cbxCsvDataFileLoadsViewFilters.setValue(null);
		}
		
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		Collection cbxMenuItemIds = cbxCsvDataFileLoadsViewFilters.getContainerDataSource().getItemIds();
			
		for (Object cbxMenuItemId : cbxMenuItemIds) 
		{	
			if("Experiment".equals(cbxMenuItemId.toString().trim()))
				cbxCsvDataFileLoadsViewFilters.setItemCaption(cbxMenuItemId, this.systemSettings.getExperimentLabel());
     	  	
     	  	if("Experiment Type".equals(cbxMenuItemId.toString().trim()))
     	  		cbxCsvDataFileLoadsViewFilters.setItemCaption(cbxMenuItemId, this.systemSettings.getExperimentTypeLabel());
     	}
		
		
		btnProcessCsvDataFile.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openProcessCsvDataFileModalWindow();
			}
		});

		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterDbViewResults();
			}
			});
		
		this.btnViewResultFiles.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openFileProcessViewerModalWindow();
			}
		});
		
	}
	
	public void openProcessCsvDataFileModalWindow()
	{
		 Window processCsvDataFileModalWindow = new Window("Process Csv Data File");
		 processCsvDataFileModalWindow.setModal(true);
		 processCsvDataFileModalWindow.setResizable(false);
		 processCsvDataFileModalWindow.setContent(new CsvDataFileProcessForm());
		 processCsvDataFileModalWindow.setWidth(993, Unit.PIXELS);
		 processCsvDataFileModalWindow.setHeight(310, Unit.PIXELS);
		 processCsvDataFileModalWindow.center();
		 processCsvDataFileModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				filterDbViewResults();
			}
		});
		 this.getUI().addWindow(processCsvDataFileModalWindow);
    }

	public void openFileProcessViewerModalWindow()
	{
		if(this.tblCsvDataFileLoads.getValue() == null)
			return;
		
		int selectedDataFileLoadId = Integer.parseInt(this.tblCsvDataFileLoads.getValue().toString());
		
		Window fileProcessViewerModalWindow = new Window("Csv Data File Load - Result Files");
		fileProcessViewerModalWindow.setModal(true);
		fileProcessViewerModalWindow.setResizable(false);
		fileProcessViewerModalWindow.setContent(new DataFileProcessViewerForm(selectedDataFileLoadId, -1));
		fileProcessViewerModalWindow.setWidth(993, Unit.PIXELS);
		fileProcessViewerModalWindow.setHeight(310, Unit.PIXELS);
		fileProcessViewerModalWindow.center();
		this.getUI().addWindow(fileProcessViewerModalWindow);
    }
	
	private void filterDbViewResults()
	{
		if(cbxCsvDataFileLoadsViewFilters.getValue() != null)
		{
			ResultSet experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwCsvDataLoadExecutionResult", (String)cbxCsvDataFileLoadsViewFilters.getValue(), txtSearch.getValue());

			if(experimentViewResults != null)
			{
				this.tblCsvDataFileLoads.setContainerDataSource(null);
				VaadinControls.bindDbViewRsToVaadinTable(tblCsvDataFileLoads, experimentViewResults, 1);
				this.tblCsvDataFileLoads.setSortContainerPropertyId("Execution Date");
				this.tblCsvDataFileLoads.setSortAscending(true);
				System.out.println("Sorted");
				this.tblCsvDataFileLoads.setSortAscending(false);
			}
		}
		else
		{
			ResultSet vwCsvDataLoadExecResults = new DataBaseViewDao().getViewResults("vwCsvDataLoadExecutionResult");
			if(vwCsvDataLoadExecResults != null)
			{
				this.tblCsvDataFileLoads.setContainerDataSource(null);
				VaadinControls.bindDbViewRsToVaadinTable(this.tblCsvDataFileLoads, vwCsvDataLoadExecResults, 1);
				this.tblCsvDataFileLoads.setSortContainerPropertyId("Execution Date");
				this.tblCsvDataFileLoads.setSortAscending(true);
				System.out.println("Sorted");
				this.tblCsvDataFileLoads.setSortAscending(false);
			}
		}
		
		//tblCsvDataFileLoads.setSortContainerPropertyId("Execution Date");
		//tblCsvDataFileLoads.setSortAscending(false);	
	}
	
}
