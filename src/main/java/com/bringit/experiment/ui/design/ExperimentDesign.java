package com.bringit.experiment.ui.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
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
public class ExperimentDesign extends VerticalLayout {
	protected Button btnSave;
	protected Button btnCancel;
	protected VerticalLayout crudLayout;
	protected GridLayout gridMainData;
	protected TextField txtExpName;
	protected TextField txtExpDbTableNameId;
	protected CheckBox chxActive;
	protected TextArea txtExpInstructions;
	protected TextArea txtExpComments;
	protected Button btnAddField;
	protected Button btnDeleteField;
	protected Table tblExperimentFields;
	protected Upload upImage;
	protected Button btnDeleteImage;
	//protected VerticalLayout vlViewer;
	protected Button btnDelete;

	public ExperimentDesign() {
		Design.read(this);
	}
}
