package com.bringit.experiment.ui.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
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
public class CsvDataFileProcessDesign extends VerticalLayout {
	protected Button btnRun;
	protected Button btnCancel;
	protected VerticalLayout crudLayout;
	protected ComboBox cbxCsvTemplate;
	protected Upload upCsvDataFile;
	protected Label lblLoadedFile;
	protected TextArea txtCsvDataFileLoadResults;

	public CsvDataFileProcessDesign() {
		Design.read(this);
	}
}
