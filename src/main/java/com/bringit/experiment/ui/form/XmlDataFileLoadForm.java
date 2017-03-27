package com.bringit.experiment.ui.form;



import java.sql.ResultSet;
import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.ui.design.XmlDataFileLoadDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class XmlDataFileLoadForm extends XmlDataFileLoadDesign{

	public XmlDataFileLoadForm() {

		ResultSet vwXmlDataLoadExecResults = new DataBaseViewDao().getViewResults("vwXmlDataLoadExecutionResult");
		if(vwXmlDataLoadExecResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblXmlDataFileLoads, vwXmlDataLoadExecResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxXmlDataFileLoadsViewFilters, vwXmlDataLoadExecResults);
		}
		btnProcessXmlDataFile.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openProcessXmlDataFileModalWindow();
			}
		});

		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterDbViewResults();
			}
			});
	}
	
	public void openProcessXmlDataFileModalWindow()
	{
		 Window processXmlDataFileModalWindow = new Window("Process Xml Data File");
		 processXmlDataFileModalWindow.setModal(true);
		 processXmlDataFileModalWindow.setResizable(false);
		 processXmlDataFileModalWindow.setContent(new XmlDataFileProcessForm());
		 processXmlDataFileModalWindow.setWidth(993, Unit.PIXELS);
		 processXmlDataFileModalWindow.setHeight(310, Unit.PIXELS);
		 processXmlDataFileModalWindow.center();
		 processXmlDataFileModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				filterDbViewResults();
			}
		});
		 this.getUI().addWindow(processXmlDataFileModalWindow);
    }
	

	private void filterDbViewResults()
	{
		if(cbxXmlDataFileLoadsViewFilters.getValue() != null)
		{
			ResultSet experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwXmlDataLoadExecutionResult", (String)cbxXmlDataFileLoadsViewFilters.getValue(), txtSearch.getValue());

			if(experimentViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblXmlDataFileLoads, experimentViewResults, 1);
		}
	}
	
}
