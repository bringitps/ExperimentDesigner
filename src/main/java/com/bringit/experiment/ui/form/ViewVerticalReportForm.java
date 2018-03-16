package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.ViewVerticalReportDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class ViewVerticalReportForm extends ViewVerticalReportDesign{
	
	public ViewVerticalReportForm()
	{
		ResultSet vwVerticalRptViewResults = new DataBaseViewDao().getViewResults("vwVerticalReport");
		
		if(vwVerticalRptViewResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblViewVerticalReports, vwVerticalRptViewResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxViewVerticalReportViewFilters, vwVerticalRptViewResults);
		}
		
		//Elements events
		this.tblViewVerticalReports.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick())
                	openVwVerticalReportBuilderCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
            }
        });
	
		this.btnAddVwVerticalReport.addClickListener(new Button.ClickListener() {
		
			@Override
			public void buttonClick(ClickEvent event) {
				openVwVerticalReportBuilderCRUDModalWindow(-1);
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
		if(cbxViewVerticalReportViewFilters.getValue() != null)
		{
			ResultSet vwVerticalRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwVerticalReport", (String)cbxViewVerticalReportViewFilters.getValue(), this.txtSearch.getValue());

			if(vwVerticalRptViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblViewVerticalReports, vwVerticalRptViewResults, 1);
		}
	}
	

	public void openVwVerticalReportBuilderCRUDModalWindow(int vwVerticalReportId)
	{

		Window vwVerticalReportBuilderModalWindow = new Window("View Vertical Report Builder");
		vwVerticalReportBuilderModalWindow.setModal(true);
		vwVerticalReportBuilderModalWindow.setResizable(false);
		vwVerticalReportBuilderModalWindow.setContent(new ViewVerticalReportBuilderForm(vwVerticalReportId));
		vwVerticalReportBuilderModalWindow.setWidth(993, Unit.PIXELS);
		vwVerticalReportBuilderModalWindow.setHeight(660, Unit.PIXELS);
		vwVerticalReportBuilderModalWindow.center();
		vwVerticalReportBuilderModalWindow.addCloseListener(new Window.CloseListener() {
				
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadVwVerticalReportDbViewResults();
			}
		});
		 this.getUI().addWindow(vwVerticalReportBuilderModalWindow);
    }
	
	private void reloadVwVerticalReportDbViewResults()
	{
		ResultSet vwVerticalRptViewResults = null;
		if(this.cbxViewVerticalReportViewFilters.getValue() != null)
			vwVerticalRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwVerticalReport", (String)this.cbxViewVerticalReportViewFilters.getValue(), this.txtSearch.getValue());
		else
			vwVerticalRptViewResults = new DataBaseViewDao().getFilteredViewResults("vwVerticalReport", (String)this.cbxViewVerticalReportViewFilters.getValue(), this.txtSearch.getValue());
		
		if(vwVerticalRptViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(tblViewVerticalReports, vwVerticalRptViewResults, 1);
	}
	

}
