package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.FirstTimeYieldReportDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;

public class FirstTimeYieldReportForm extends FirstTimeYieldReportDesign{
	
	public FirstTimeYieldReportForm()
	{
		ResultSet ftyRptViewResults = new DataBaseViewDao().getViewResults("vwFirstTimeYieldReport");
		
		if(ftyRptViewResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblFirstTimeYieldReports, ftyRptViewResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxFirstTimeYieldReportViewFilters, ftyRptViewResults);
		}

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
	
}
