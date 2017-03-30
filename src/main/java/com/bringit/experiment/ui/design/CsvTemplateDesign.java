package com.bringit.experiment.ui.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
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
public class CsvTemplateDesign extends VerticalLayout {
	protected Button btnSave;
	protected Button btnCancel;
	protected CheckBox chxActive;
	protected VerticalLayout crudLayout;
	protected ComboBox comboCsvTExperiment;
	protected Upload upCsv;
	protected TextField txtCsvTName;
	protected ComboBox cbxContractManufacturer;
	protected TextArea txtCsvTComments;
	protected Accordion csvTJobAndMap;
	protected VerticalLayout csvTJobLayout;
	protected DateField dtCsvTend;
	protected ComboBox comboCsvjobScheduler;
	protected ComboBox cbxStartHour;
	protected DateField dtCsvTstart;
	protected CheckBox chxNotScheduled;
	protected ComboBox comboCsvTinRepo;
	protected TextField txtCsvTPrefix;
	protected ComboBox comboCsvoutRepo;
	protected ComboBox comboCsvTerrRepo;
	protected VerticalLayout csvTMapLayout;
	protected Table tblCsvCols;
	protected Button btnDelete;

	public CsvTemplateDesign() {
		Design.read(this);
	}
}
