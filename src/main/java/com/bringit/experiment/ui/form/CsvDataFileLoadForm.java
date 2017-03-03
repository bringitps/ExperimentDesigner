package com.bringit.experiment.ui.form;



import java.util.List;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.CsvTemplateDao;
import com.bringit.experiment.ui.design.CsvDataFileLoadDesign;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class CsvDataFileLoadForm extends CsvDataFileLoadDesign{

	public CsvDataFileLoadForm() {

		btnProcessCsvDataFile.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openProcessCsvDataFileModalWindow();
			}
		});
		
	}
	
	public void openProcessCsvDataFileModalWindow()
	{
		 Window processCsvDataFileModalWindow = new Window("Process Csv Data File");
		 processCsvDataFileModalWindow.setModal(true);
		 processCsvDataFileModalWindow.setResizable(false);
		 processCsvDataFileModalWindow.setContent(new CsvDataFileProcessForm());
		 processCsvDataFileModalWindow.setWidth(940, Unit.PIXELS);
		 processCsvDataFileModalWindow.setHeight(760, Unit.PIXELS);
		 processCsvDataFileModalWindow.center();
		 processCsvDataFileModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				//reloadExperimentDbViewResults();
			}
		});
		 this.getUI().addWindow(processCsvDataFileModalWindow);
    }
	
	
}
