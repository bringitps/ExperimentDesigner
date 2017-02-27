package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.bringit.experiment.ui.design.XmlTemplateManagementDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class XmlTemplateManagementForm extends XmlTemplateManagementDesign  {
	
	public XmlTemplateManagementForm() {
		ResultSet vwXmlTemplateResults = new DataBaseViewDao().getViewResults("vwXmlTemplate");
		if(vwXmlTemplateResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblXmlTemplate, vwXmlTemplateResults, 1);
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
	}
	public void openXmlTemplateCRUDModalWindow(int xmlTemplateId)
	{
		 Window experimentCRUDModalWindow = new Window("Xml Template");
		 experimentCRUDModalWindow.setModal(true);
		 experimentCRUDModalWindow.setResizable(false);
		 experimentCRUDModalWindow.setContent(new XmlTemplateForm(xmlTemplateId));
		 experimentCRUDModalWindow.setWidth(940, Unit.PIXELS);
		 experimentCRUDModalWindow.setHeight(760, Unit.PIXELS);
		 experimentCRUDModalWindow.center();
		 this.getUI().addWindow(experimentCRUDModalWindow);
    }
	
}
