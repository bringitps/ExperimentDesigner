package com.bringit.experiment.ui.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
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
public class TargetReportBuilderDesign extends VerticalLayout {
	protected VerticalLayout layoutHeaderBtns;
	protected Button btnSave;
	protected Button btnCancel;
	protected CheckBox chxActive;
	protected VerticalLayout targetRptLayout;
	protected GridLayout gridMainData;
	protected ComboBox cbxExperiment;
	protected CheckBox chxWhatIf;
	protected TextField txtTargetRptName;
	protected TextField txtTargetRptDescription;
	protected GridLayout gridDefaultDateFilter;
	protected ComboBox cbxDefaultDateRangeSelectExpField;
	protected PopupDateField dtDefaultFilterFrom;
	protected PopupDateField dtDefaultFilterTo;
	protected VerticalLayout colGroupLayout;
	protected Button btnAddColumnGroup;
	protected Button btnDeleteColumnGroup;
	protected HorizontalLayout targetFieldGroupsLayout;
	protected TabSheet tabColumnGroups;
	protected VerticalLayout btnDeleteLayout;
	protected Button btnDelete;

	public TargetReportBuilderDesign() {
		Design.read(this);
	}
}
