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
public class XmlTemplateDesign extends VerticalLayout {
	protected Button btnSave;
	protected Button btnCancel;
	protected CheckBox chxActive;
	protected VerticalLayout crudLayout;
	protected ComboBox comboXmlTExperiment;
	protected Upload upXml;
	protected TextField txtXmlTName;
	protected ComboBox cbxContractManufacturer;
	protected TextArea txtXmlTComments;
	protected Accordion xmlTJobAndMap;
	protected VerticalLayout xmlTJobLayout;
	protected DateField startXmlTstart;
	protected ComboBox cbxStartHour;
	protected ComboBox comboXmljobScheduler;
	protected DateField endXmlTstart;
	protected ComboBox comboXmlTinRepo;
	protected TextField txtXmlTPrefix;
	protected ComboBox comboXmloutRepo;
	protected ComboBox comboXmlTerrRepo;
	protected VerticalLayout xmlTMapLayout;
	protected Table tblXmlNodes;
	protected Button btnDelete;

	public XmlTemplateDesign() {
		Design.read(this);
	}
}
