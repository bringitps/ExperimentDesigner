package com.bringit.experiment.ui.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TabSheet;
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
public class FirstPassYieldReportBuilderDesign extends VerticalLayout {
	protected VerticalLayout layoutHeaderBtns;
	protected Button btnSave;
	protected Button btnCancel;
	protected CheckBox chxActive;
	protected VerticalLayout fpyRptLayout;
	protected GridLayout gridMainData;
	protected TextField txtFpyRptName;
	protected TextField txtFpyRptCustomId;
	protected ComboBox cbxExperiment;
	protected TextField txtFpyDescription;
	protected GridLayout gbxYieldFields;
	protected ComboBox cbxDateTimeField;
	protected ComboBox cbxSerialNumberField;
	protected ComboBox cbxResultField;
	protected GridLayout gbxResultValues;
	protected TextField txtFpyRptPassValue;
	protected TextField txtFpyRptFailValue;
	protected GridLayout gbxTimeRange;
	protected TextField txtGroupTimeRange;
	protected CheckBox chxGroupByTimeRange;
	protected VerticalLayout colGroupLayout;
	protected HorizontalLayout fpyFieldGroupsLayout;
	protected TabSheet tabColumnGroups;
	protected VerticalLayout infoFieldsLayout;
	protected HorizontalSplitPanel pnlInfoFields;
	protected Table tblInformationFields;
	protected GridLayout gridBtnsRptInfoFields;
	protected Button btnAddField;
	protected Button btnRemoveField;
	protected VerticalLayout btnDeleteLayout;
	protected Button btnDelete;

	public FirstPassYieldReportBuilderDesign() {
		Design.read(this);
	}
}
