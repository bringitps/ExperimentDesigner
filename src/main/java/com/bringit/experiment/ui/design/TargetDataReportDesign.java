package com.bringit.experiment.ui.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
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
public class TargetDataReportDesign extends VerticalLayout {
	protected Label lblTargetRptTitle;
	protected Label lblLastRefreshDate;
	protected Button btnRefreshButton;
	protected HorizontalLayout layoutApplyFilters;
	protected Button btnApplyFilters;
	protected HorizontalLayout layoutDateFilter;
	protected GridLayout gridDateFilters;
	protected ComboBox cbxDateFieldsFilter;
	protected PopupDateField dtFilter1;
	protected PopupDateField dtFilter2;
	protected HorizontalLayout layoutExpField;
	protected ComboBox cbxExpFieldFilter;
	protected TextField txtExpFieldFilter;
	protected HorizontalLayout layoutContractManufacturer;
	protected ComboBox cbxContractManufacturer;
	protected Button btnViewChart;
	protected Button btnExportExcel;
	protected Table tblTargetDataReport;

	public TargetDataReportDesign() {
		Design.read(this);
	}
}
