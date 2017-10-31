package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.FirstTimeYieldReportDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class FirstTimeYieldReportForm extends FirstTimeYieldReportDesign{
	
	public FirstTimeYieldReportForm()
	{
		ResultSet ftyRptViewResults = new DataBaseViewDao().getViewResults("vwFirstTimeYieldReport");
		
		if(ftyRptViewResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblFirstTimeYieldReports, ftyRptViewResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxFirstTimeYieldReportViewFilters, ftyRptViewResults);
		}

		//Elements events
		this.tblFirstTimeYieldReports.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick())
                	openFtyReportBuilderCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
            }
        });
	
		this.btnAddFirstTimeYieldReport.addClickListener(new Button.ClickListener() {
		
			@Override
			public void buttonClick(ClickEvent event) {
				openFtyReportBuilderCRUDModalWindow(-1);
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
		if(cbxFirstTimeYieldReportViewFilters.getValue() != null)
		{
			ResultSet ftyRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwFirstTimeYieldReport", (String)cbxFirstTimeYieldReportViewFilters.getValue(), this.txtSearch.getValue());

			if(ftyRptViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblFirstTimeYieldReports, ftyRptViewResults, 1);
		}
	}
	

	public void openFtyReportBuilderCRUDModalWindow(int ftyReportId)
	{

		Window ftyReportBuilderModalWindow = new Window("First Time Yield Report Builder");
		ftyReportBuilderModalWindow.setModal(true);
		ftyReportBuilderModalWindow.setResizable(false);
		ftyReportBuilderModalWindow.setContent(new FirstTimeYieldReportBuilderForm(ftyReportId));
		ftyReportBuilderModalWindow.setWidth(993, Unit.PIXELS);
		ftyReportBuilderModalWindow.setHeight(660, Unit.PIXELS);
		ftyReportBuilderModalWindow.center();
 		ftyReportBuilderModalWindow.addCloseListener(new Window.CloseListener() {
				
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadFpyReportDbViewResults();
			}
		});
		 this.getUI().addWindow(ftyReportBuilderModalWindow);
    }
	
	private void reloadFpyReportDbViewResults()
	{
		ResultSet ftyRptViewResults = null;
		if(this.cbxFirstTimeYieldReportViewFilters.getValue() != null)
			ftyRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwFirstTimeYieldReport", (String)this.cbxFirstTimeYieldReportViewFilters.getValue(), this.txtSearch.getValue());
		else
			ftyRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwFirstTimeYieldReport", (String)this.cbxFirstTimeYieldReportViewFilters.getValue(), this.txtSearch.getValue());
		
		if(ftyRptViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(tblFirstTimeYieldReports, ftyRptViewResults, 1);
	}
	
}
