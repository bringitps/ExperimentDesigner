package com.bringit.experiment.ui.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class UserDesign extends VerticalLayout {
	protected Button btnSave;
	protected Button btnCancel;
	protected VerticalLayout crudLayout;
	protected TextField txtUserName;
	protected CheckBox chxActiveDirectory;
	protected PasswordField txtPassword;
	protected Table tblUserRoles;
	protected Button btnDelete;

	public UserDesign() {
		Design.read(this);
	}
}