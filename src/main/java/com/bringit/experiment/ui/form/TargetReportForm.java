package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.TargetReportDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class TargetReportForm extends TargetReportDesign {

	public TargetReportForm()
	{
		ResultSet targetRptViewResults = new DataBaseViewDao().getViewResults("vwTargetReport");
		
		if(targetRptViewResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblTargetReports, targetRptViewResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxTargetReportViewFilters, targetRptViewResults);
		}
		
		tblTargetReports.addItemClickListener(new ItemClickEvent.ItemClickListener() 
		    {
	            public void itemClick(ItemClickEvent event) {
	                if (event.isDoubleClick())
	                	openTargetReportCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
	            }
	        });
		
		btnAddTargetReport.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openTargetReportCRUDModalWindow(-1);
			}
		});
		
		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterDbViewResults();
			}
			});
	}
	
	public void openTargetReportCRUDModalWindow(int targetReportId)
	{

		Window targetReportModalWindow = new Window("Xml Template");
 		targetReportModalWindow.setModal(true);
 		targetReportModalWindow.setResizable(false);
 		targetReportModalWindow.setContent(new TargetReportBuilderForm(targetReportId));
 		targetReportModalWindow.setWidth(940, Unit.PIXELS);
 		targetReportModalWindow.setHeight(760, Unit.PIXELS);
 		targetReportModalWindow.center();
 		targetReportModalWindow.addCloseListener(new Window.CloseListener() {
				
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadTargetReportDbViewResults();
			}
		});
		 this.getUI().addWindow(targetReportModalWindow);
    }
	
	private void reloadTargetReportDbViewResults()
	{
		ResultSet experimentViewResults = null;
		if(this.cbxTargetReportViewFilters.getValue() != null)
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwTargetReport", (String)this.cbxTargetReportViewFilters.getValue(), this.txtSearch.getValue());
		else
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwTargetReport", (String)this.cbxTargetReportViewFilters.getValue(), this.txtSearch.getValue());
		
		if(experimentViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(tblTargetReports, experimentViewResults, 1);
	}
	
	private void filterDbViewResults()
	{
		if(this.cbxTargetReportViewFilters.getValue() != null)
		{
			ResultSet experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwTargetReport", (String)this.cbxTargetReportViewFilters.getValue(), this.txtSearch.getValue());

			if(experimentViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblTargetReports, experimentViewResults, 1);
		}
	}
}
