package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.dao.SystemSettingsDao;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ExperimentManagementForm extends ExperimentManagementDesign {

	private SystemSettings systemSettings;

	public ExperimentManagementForm() {
		
		this.systemSettings = new SystemSettingsDao().getCurrentSystemSettings();
		this.lblTitle.setValue("- " + this.systemSettings.getExperimentPluralLabel() + " Management");
		
		
		ResultSet experimentViewResults = new DataBaseViewDao().getViewResults("vwExperiment");
		
		if(experimentViewResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblExperiments, experimentViewResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxExperimentViewFilters, experimentViewResults);
		}
		
		tblExperiments.addItemClickListener(new ItemClickEvent.ItemClickListener() 
		    {
	            public void itemClick(ItemClickEvent event) {
	                if (event.isDoubleClick())
	                	openExperimentCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
	            }
	        });
		
		btnAddExperiment.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openExperimentCRUDModalWindow(-1);
			}
		});
		
		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterDbViewResults();
			}
			});
		
	}
	
	public void openExperimentCRUDModalWindow(int ExperimentId)
	{
		 Window experimentCRUDModalWindow = new Window(this.systemSettings.getExperimentLabel());
		 experimentCRUDModalWindow.setModal(true);
		 experimentCRUDModalWindow.setResizable(false);
		 experimentCRUDModalWindow.setContent(new ExperimentForm(ExperimentId));
		 experimentCRUDModalWindow.setWidth(993, Unit.PIXELS);
		 experimentCRUDModalWindow.setHeight(660, Unit.PIXELS);
		 experimentCRUDModalWindow.center();
		 experimentCRUDModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadExperimentDbViewResults();
			}
		});
		 this.getUI().addWindow(experimentCRUDModalWindow);
    }
	
	private void reloadExperimentDbViewResults()
	{
		//this.getUI().doRefresh(VaadinService.getCurrentRequest());
		ResultSet experimentViewResults = null;
		if(this.cbxExperimentViewFilters.getValue() != null)
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwExperiment", (String)this.cbxExperimentViewFilters.getValue(), this.txtSearch.getValue());
		else
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwExperiment", (String)this.cbxExperimentViewFilters.getValue(), this.txtSearch.getValue());
		
		if(experimentViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(tblExperiments, experimentViewResults, 1);
	}
	
	private void filterDbViewResults()
	{
		if(this.cbxExperimentViewFilters.getValue() != null)
		{
			ResultSet experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwExperiment", (String)this.cbxExperimentViewFilters.getValue(), this.txtSearch.getValue());

			if(experimentViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblExperiments, experimentViewResults, 1);
		}
	}
}
