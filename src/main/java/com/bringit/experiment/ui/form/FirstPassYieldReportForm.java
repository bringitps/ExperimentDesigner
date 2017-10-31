package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.FirstPassYieldReportDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class FirstPassYieldReportForm extends FirstPassYieldReportDesign{

	public FirstPassYieldReportForm()
	{
		ResultSet fpyRptViewResults = new DataBaseViewDao().getViewResults("vwFirstPassYieldReport");
		
		if(fpyRptViewResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblFirstPassYieldReports, fpyRptViewResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxFirstPassYieldReportViewFilters, fpyRptViewResults);
		}
		
		//Elements events
		this.tblFirstPassYieldReports.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick())
                	openFpyReportBuilderCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
            }
        });
	
		this.btnAddFirstPassYieldReport.addClickListener(new Button.ClickListener() {
		
			@Override
			public void buttonClick(ClickEvent event) {
				openFpyReportBuilderCRUDModalWindow(-1);
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
		if(cbxFirstPassYieldReportViewFilters.getValue() != null)
		{
			ResultSet fpyRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwFirstPassYieldReport", (String)cbxFirstPassYieldReportViewFilters.getValue(), this.txtSearch.getValue());

			if(fpyRptViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblFirstPassYieldReports, fpyRptViewResults, 1);
		}
	}
	

	public void openFpyReportBuilderCRUDModalWindow(int fpyReportId)
	{

		Window fpyReportBuilderModalWindow = new Window("First Pass Yield Report Builder");
		fpyReportBuilderModalWindow.setModal(true);
		fpyReportBuilderModalWindow.setResizable(false);
		fpyReportBuilderModalWindow.setContent(new FirstPassYieldReportBuilderForm(fpyReportId));
		fpyReportBuilderModalWindow.setWidth(993, Unit.PIXELS);
		fpyReportBuilderModalWindow.setHeight(660, Unit.PIXELS);
		fpyReportBuilderModalWindow.center();
 		fpyReportBuilderModalWindow.addCloseListener(new Window.CloseListener() {
				
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadFpyReportDbViewResults();
			}
		});
		 this.getUI().addWindow(fpyReportBuilderModalWindow);
    }
	
	private void reloadFpyReportDbViewResults()
	{
		ResultSet fpyRptViewResults = null;
		if(this.cbxFirstPassYieldReportViewFilters.getValue() != null)
			fpyRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwFirstPassYieldReport", (String)this.cbxFirstPassYieldReportViewFilters.getValue(), this.txtSearch.getValue());
		else
			fpyRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwFirstPassYieldReport", (String)this.cbxFirstPassYieldReportViewFilters.getValue(), this.txtSearch.getValue());
		
		if(fpyRptViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(tblFirstPassYieldReports, fpyRptViewResults, 1);
	}
	
}
