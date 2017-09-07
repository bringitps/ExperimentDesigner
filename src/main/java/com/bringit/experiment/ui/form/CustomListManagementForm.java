package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.CustomListManagementDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class CustomListManagementForm extends CustomListManagementDesign{

	public CustomListManagementForm() {
		
		ResultSet customListViewResults = new DataBaseViewDao().getViewResults("vwCustomList");
		
		if(customListViewResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblCustomList, customListViewResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxCustomListViewFilters, customListViewResults);
		}

		tblCustomList.addItemClickListener(new ItemClickEvent.ItemClickListener() 
		    {
	            public void itemClick(ItemClickEvent event) {
	                if (event.isDoubleClick())
	                	openCustomListCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
	            }
	        });
		
		btnAddCustomList.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openCustomListCRUDModalWindow(-1);
			}
		});
		
		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterDbViewResults();
			}
			});
	} 
	
	public void openCustomListCRUDModalWindow(int CustomListId)
	{
		 Window experimentCRUDModalWindow = new Window("Custom List");
		 experimentCRUDModalWindow.setModal(true);
		 experimentCRUDModalWindow.setResizable(false);
		 experimentCRUDModalWindow.setContent(new CustomListForm(CustomListId));
		 experimentCRUDModalWindow.setWidth(993, Unit.PIXELS);
		 experimentCRUDModalWindow.setHeight(660, Unit.PIXELS);
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
		//this.getUI().doRefresh(VaadinService.getCurrentRequest());
		ResultSet customListViewResults = null;
		if(this.cbxCustomListViewFilters.getValue() != null)
			customListViewResults = new DataBaseViewDao().getFilteredViewResults("vwCustomList", (String)this.cbxCustomListViewFilters.getValue(), this.txtSearch.getValue());
		else
			customListViewResults = new DataBaseViewDao().getFilteredViewResults("vwCustomList", (String)this.cbxCustomListViewFilters.getValue(), this.txtSearch.getValue());
		
		if(customListViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(this.tblCustomList, customListViewResults, 1);
	}
	
	private void filterDbViewResults()
	{
		if(this.cbxCustomListViewFilters.getValue() != null)
		{
			ResultSet customListViewResults = new DataBaseViewDao().getFilteredViewResults("vwCustomList", (String)this.cbxCustomListViewFilters.getValue(), this.txtSearch.getValue());

			if(customListViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(this.tblCustomList, customListViewResults, 1);
		}
	}
}
