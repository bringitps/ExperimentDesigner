package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.bringit.experiment.ui.design.XmlTemplateManagementDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class XmlTemplateManagementForm extends XmlTemplateManagementDesign  {
	
	public XmlTemplateManagementForm() {
		ResultSet vwXmlTemplateResults = new DataBaseViewDao().getViewResults("vwXmlTemplate");
		if(vwXmlTemplateResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblXmlTemplate, vwXmlTemplateResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxXmlTemplateViewFilters, vwXmlTemplateResults);
		}
		
		tblXmlTemplate.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick())
                	openXmlTemplateCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
            }
        });
		
		btnAddXmlTemplate.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openXmlTemplateCRUDModalWindow(-1);
			}
		});

		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterDbViewResults();
			}
			});
	}
	public void openXmlTemplateCRUDModalWindow(int xmlTemplateId)
	{
		 Window xmlTemplateCRUDModalWindow = new Window("Xml Template");
		 xmlTemplateCRUDModalWindow.setModal(true);
		 xmlTemplateCRUDModalWindow.setResizable(false);
		 xmlTemplateCRUDModalWindow.setContent(new XmlTemplateForm(xmlTemplateId));
		 xmlTemplateCRUDModalWindow.setWidth(993, Unit.PIXELS);
		 xmlTemplateCRUDModalWindow.setHeight(660, Unit.PIXELS);
		 xmlTemplateCRUDModalWindow.center();
		 xmlTemplateCRUDModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadXmlTemplateDbViewResults();
			}
		});
		 this.getUI().addWindow(xmlTemplateCRUDModalWindow);
    }

	private void reloadXmlTemplateDbViewResults()
	{
		ResultSet experimentViewResults = null;
		if(this.cbxXmlTemplateViewFilters.getValue() != null)
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwXmlTemplate", (String)this.cbxXmlTemplateViewFilters.getValue(), this.txtSearch.getValue());
		else
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwXmlTemplate", (String)this.cbxXmlTemplateViewFilters.getValue(), this.txtSearch.getValue());
		
		if(experimentViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(this.tblXmlTemplate, experimentViewResults, 1);
	}
	

	private void filterDbViewResults()
	{
		if(this.cbxXmlTemplateViewFilters.getValue() != null)
		{
			ResultSet experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwXmlTemplate", (String)this.cbxXmlTemplateViewFilters.getValue(), this.txtSearch.getValue());

			if(experimentViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(this.tblXmlTemplate, experimentViewResults, 1);
		}
	}
}
