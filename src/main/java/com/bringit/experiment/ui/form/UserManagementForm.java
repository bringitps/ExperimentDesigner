package com.bringit.experiment.ui.form;

import java.sql.ResultSet;

import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.ui.design.ExperimentManagementDesign;
import com.bringit.experiment.ui.design.UserManagementDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class UserManagementForm extends UserManagementDesign  {
	
	public UserManagementForm() {
		ResultSet vwUserResults = new DataBaseViewDao().getViewResults("vwUser");
		if(vwUserResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblUser, vwUserResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxUserViewFilters, vwUserResults);
		}
		
		tblUser.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick())
                	openUserCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
            }
        });
		
		btnAddUser.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openUserCRUDModalWindow(-1);
			}
		});

		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterDbViewResults();
			}
			});
	}
	public void openUserCRUDModalWindow(int userId)
	{
		 Window userCRUDModalWindow = new Window("Users");
		 userCRUDModalWindow.setModal(true);
		 userCRUDModalWindow.setResizable(false);
		 userCRUDModalWindow.setContent(new UserForm(userId));
		 userCRUDModalWindow.setWidth(940, Unit.PIXELS);
		 userCRUDModalWindow.setHeight(760, Unit.PIXELS);
		 userCRUDModalWindow.center();
		 userCRUDModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				reloadUserDbViewResults();
			}
		});
		 this.getUI().addWindow(userCRUDModalWindow);
    }

	private void reloadUserDbViewResults()
	{
		ResultSet experimentViewResults = null;
		if(this.cbxUserViewFilters.getValue() != null)
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwUser", (String)this.cbxUserViewFilters.getValue(), this.txtSearch.getValue());
		else
			experimentViewResults = new DataBaseViewDao().getFilteredViewResults("vwUser", (String)this.cbxUserViewFilters.getValue(), this.txtSearch.getValue());
		
		if(experimentViewResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(this.tblUser, experimentViewResults, 1);
	}
	

	private void filterDbViewResults()
	{
		if(this.cbxUserViewFilters.getValue() != null)
		{
			ResultSet userViewResults = new DataBaseViewDao().getFilteredViewResults("vwUser", (String)this.cbxUserViewFilters.getValue(), this.txtSearch.getValue());

			if(userViewResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(this.tblUser, userViewResults, 1);
		}
	}
}
