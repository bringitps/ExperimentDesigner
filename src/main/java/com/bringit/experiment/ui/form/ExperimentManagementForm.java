package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ExperimentManagementForm extends ExperimentManagementDesign {
	
	public ExperimentManagementForm() {
		ResultSet experimentViewResults = new DataBaseViewDao().getViewResults("vwExperiment");
		VaadinControls.bindDbViewRsToVaadinTable(tblExperiments, experimentViewResults, 1);
		
		tblExperiments.addItemClickListener(new ItemClickEvent.ItemClickListener() 
		    {
	            public void itemClick(ItemClickEvent event) {
	                if (event.isDoubleClick())
	                	openExperimentCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
	            }
	        });
		
		btnAddExperiment.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openExperimentCRUDModalWindow(-1);
			}
		});
	}
	
	public void openExperimentCRUDModalWindow(int ExperimentId)
	{
		 Window experimentCRUDModalWindow = new Window("Experiment");
		 experimentCRUDModalWindow.setModal(true);
		 experimentCRUDModalWindow.setResizable(false);
		 experimentCRUDModalWindow.setContent(new ExperimentForm(ExperimentId));
		 experimentCRUDModalWindow.setWidth(940, Unit.PIXELS);
		 experimentCRUDModalWindow.setHeight(760, Unit.PIXELS);
		 experimentCRUDModalWindow.center();
		 experimentCRUDModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadExperimentDbViewResults();
			}
		});
		 this.getUI().addWindow(experimentCRUDModalWindow);
    }
	
	private void reloadExperimentDbViewResults()
	{
		ResultSet experimentViewResults = new DataBaseViewDao().getViewResults("vwExperiment");
		VaadinControls.bindDbViewRsToVaadinTable(tblExperiments, experimentViewResults, 1);
	}
}
