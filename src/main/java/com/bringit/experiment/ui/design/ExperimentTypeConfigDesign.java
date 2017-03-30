package com.bringit.experiment.ui.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
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
public class ExperimentTypeConfigDesign extends VerticalLayout {
	protected Label lblFormTitle;
	protected TextField txtSearch;
	protected ComboBox cbxExperimentTypeFilters;
	protected Button btnSave;
	protected Button btnCancel;
	protected GridLayout gridButtons;
	protected Button btnAddExpType;
	protected Button btnDeleteExpType;
	protected Table tblExperimentType;

	public ExperimentTypeConfigDesign() {
		Design.read(this);
	}
}
