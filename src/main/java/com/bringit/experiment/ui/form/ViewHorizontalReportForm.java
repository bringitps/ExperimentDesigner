package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.ViewHorizontalReportBuilderDesign;
import com.bringit.experiment.ui.design.ViewHorizontalReportDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class ViewHorizontalReportForm extends ViewHorizontalReportDesign {

	
	public ViewHorizontalReportForm()
	{
		ResultSet vwHorizontalRptViewResults = new DataBaseViewDao().getViewResults("vwHorizontalReport");
		
		if(vwHorizontalRptViewResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblViewHorizontalReports, vwHorizontalRptViewResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxViewHorizontalReportViewFilters, vwHorizontalRptViewResults);
		}
		
		//Elements events
		this.tblViewHorizontalReports.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick())
                	openVwHorizontalReportBuilderCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
            }
        });
	
		this.btnAddVwHorizontalReport.addClickListener(new Button.ClickListener() {
		
			@Override
			public void buttonClick(ClickEvent event) {
				openVwHorizontalReportBuilderCRUDModalWindow(-1);
			}
		});
		
		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterDbViewResults();
			}
		});
	}

	private void filterDbViewResults()
	{
		if(cbxViewHorizontalReportViewFilters.getValue() != null)
		{
			ResultSet vwHorizontalRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwHorizontalReport", (String)cbxViewHorizontalReportViewFilters.getValue(), this.txtSearch.getValue());

			if(vwHorizontalRptViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblViewHorizontalReports, vwHorizontalRptViewResults, 1);
		}
	}
	

	public void openVwHorizontalReportBuilderCRUDModalWindow(int vwHorizontalReportId)
	{

		Window vwHorizontalReportBuilderModalWindow = new Window("View Horizontal Report Builder");
		vwHorizontalReportBuilderModalWindow.setModal(true);
		vwHorizontalReportBuilderModalWindow.setResizable(false);
		vwHorizontalReportBuilderModalWindow.setContent(new ViewHorizontalReportBuilderForm(vwHorizontalReportId));
		vwHorizontalReportBuilderModalWindow.setWidth(993, Unit.PIXELS);
		vwHorizontalReportBuilderModalWindow.setHeight(660, Unit.PIXELS);
		vwHorizontalReportBuilderModalWindow.center();
		vwHorizontalReportBuilderModalWindow.addCloseListener(new Window.CloseListener() {
				
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadVwHorizontalReportDbViewResults();
			}
		});
		 this.getUI().addWindow(vwHorizontalReportBuilderModalWindow);
    }
	
	private void reloadVwHorizontalReportDbViewResults()
	{
		ResultSet vwHorizontalRptViewResults = null;
		if(this.cbxViewHorizontalReportViewFilters.getValue() != null)
			vwHorizontalRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwHorizontalReport", (String)this.cbxViewHorizontalReportViewFilters.getValue(), this.txtSearch.getValue());
		else
			vwHorizontalRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwHorizontalReport", (String)this.cbxViewHorizontalReportViewFilters.getValue(), this.txtSearch.getValue());
		
		if(vwHorizontalRptViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(tblViewHorizontalReports, vwHorizontalRptViewResults, 1);
	}
	
	
}
