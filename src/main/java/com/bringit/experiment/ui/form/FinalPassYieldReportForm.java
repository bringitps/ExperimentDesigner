package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.FinalPassYieldReportDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class FinalPassYieldReportForm extends FinalPassYieldReportDesign{

	public FinalPassYieldReportForm()
	{
		ResultSet fnyRptViewResults = new DataBaseViewDao().getViewResults("vwFinalPassYieldReport");
		
		if(fnyRptViewResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblFinalPassYieldReports, fnyRptViewResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxFinalPassYieldReportViewFilters, fnyRptViewResults);
		}
		
		//Elements events
		this.tblFinalPassYieldReports.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick())
                	openFnyReportBuilderCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
            }
        });
	
		this.btnAddFinalPassYieldReport.addClickListener(new Button.ClickListener() {
		
			@Override
			public void buttonClick(ClickEvent event) {
				openFnyReportBuilderCRUDModalWindow(-1);
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
		if(cbxFinalPassYieldReportViewFilters.getValue() != null)
		{
			ResultSet fnyRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwFinalPassYieldReport", (String)cbxFinalPassYieldReportViewFilters.getValue(), this.txtSearch.getValue());

			if(fnyRptViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblFinalPassYieldReports, fnyRptViewResults, 1);
		}
	}
	

	public void openFnyReportBuilderCRUDModalWindow(int fnyReportId)
	{

		Window fnyReportBuilderModalWindow = new Window("Final Pass Yield Report Builder");
		fnyReportBuilderModalWindow.setModal(true);
		fnyReportBuilderModalWindow.setResizable(false);
		fnyReportBuilderModalWindow.setContent(new FinalPassYieldReportBuilderForm(fnyReportId));
		fnyReportBuilderModalWindow.setWidth(993, Unit.PIXELS);
		fnyReportBuilderModalWindow.setHeight(660, Unit.PIXELS);
		fnyReportBuilderModalWindow.center();
		fnyReportBuilderModalWindow.addCloseListener(new Window.CloseListener() {
				
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadFnyReportDbViewResults();
			}
		});
		 this.getUI().addWindow(fnyReportBuilderModalWindow);
    }
	
	private void reloadFnyReportDbViewResults()
	{
		ResultSet fnyRptViewResults = null;
		if(this.cbxFinalPassYieldReportViewFilters.getValue() != null)
			fnyRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwFinalPassYieldReport", (String)this.cbxFinalPassYieldReportViewFilters.getValue(), this.txtSearch.getValue());
		else
			fnyRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwFinalPassYieldReport", (String)this.cbxFinalPassYieldReportViewFilters.getValue(), this.txtSearch.getValue());
		
		if(fnyRptViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(tblFinalPassYieldReports, fnyRptViewResults, 1);
	}
	
}
