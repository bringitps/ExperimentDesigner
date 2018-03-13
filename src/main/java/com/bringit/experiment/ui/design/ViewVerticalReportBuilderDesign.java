package com.bringit.experiment.ui.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
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
public class ViewVerticalReportBuilderDesign extends VerticalLayout {
	protected VerticalLayout layoutHeaderBtns;
	protected Button btnSave;
	protected Button btnCancel;
	protected CheckBox chxActive;
	protected VerticalLayout ftyRptLayout;
	protected GridLayout gridMainData;
	protected TextField txtFtyRptName;
	protected TextField txtFtyRptCustomId;
	protected TextField txtFtyDescription;
	protected TextField txtFtyRptRefreshInterval;
	protected VerticalLayout colGroupLayout;
	protected HorizontalLayout ftyFieldGroupsLayout;
	protected TabSheet tabColumnGroups;
	protected VerticalLayout lytReportDataSources;
	protected AbsoluteLayout pnlExperimentDataSource;
	protected AbsoluteLayout pnlFpyReportDataSource;
	protected AbsoluteLayout pnlFtyReportDataSource;
	protected AbsoluteLayout pnlTargetReportDataSource;
	protected VerticalLayout reportFiltersLayout;
	protected HorizontalSplitPanel pnlReportFilters;
	protected Table tblReportFilters;
	protected GridLayout gridBtnsRptFilters;
	protected Button btnAddFilter;
	protected Button btnRemoveFilter;
	protected VerticalLayout infoFieldsLayout;
	protected HorizontalSplitPanel pnlInfoFields;
	protected Table tblReportColumns;
	protected GridLayout gridBtnsRptInfoFields;
	protected Button btnAddRptColumn;
	protected Button btnRemoveRptColumn;
	protected VerticalLayout columnsEnrichmentLayout;
	protected HorizontalSplitPanel pnlColumnsEnrichment;
	protected Table tblColumnsEnrichment;
	protected GridLayout gridBtnsRptColumnsEnrichment;
	protected Button btnAddEnrichment;
	protected Button btnRemoveEnrichment;
	protected VerticalLayout lytReportColumns2;
	protected VerticalLayout btnDeleteLayout;
	protected Button btnDelete;

	public ViewVerticalReportBuilderDesign() {
		Design.read(this);
	}
}
