package com.bringit.experiment.ui.form;



import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.XmlTemplateDao;
import com.bringit.experiment.ui.design.XmlDataFileLoadDesign;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class XmlDataFileLoadForm extends XmlDataFileLoadDesign{

	public XmlDataFileLoadForm() {

		btnProcessXmlDataFile.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openProcessXmlDataFileModalWindow();
			}
		});
		
	}
	
	public void openProcessXmlDataFileModalWindow()
	{
		 Window processXmlDataFileModalWindow = new Window("Process Xml Data File");
		 processXmlDataFileModalWindow.setModal(true);
		 processXmlDataFileModalWindow.setResizable(false);
		 processXmlDataFileModalWindow.setContent(new XmlDataFileProcessForm());
		 processXmlDataFileModalWindow.setWidth(940, Unit.PIXELS);
		 processXmlDataFileModalWindow.setHeight(760, Unit.PIXELS);
		 processXmlDataFileModalWindow.center();
		 processXmlDataFileModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				//reloadExperimentDbViewResults();
			}
		});
		 this.getUI().addWindow(processXmlDataFileModalWindow);
    }
	
	
}
