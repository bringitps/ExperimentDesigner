package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.ui.design.ExperimentDataReportDesign;
import com.bringit.experiment.util.ExperimentUtil;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.addon.tableexport.ExcelExport;

//import com.vaadin.addon.tableexport.TableExport;

public class ExperimentDataReportForm extends ExperimentDataReportDesign{

	Experiment experiment = new Experiment();
	public ExperimentDataReportForm(int experimentId)
	{
		experiment = new ExperimentDao().getExperimentById(experimentId);
		String sqlSelectQuery =  ExperimentUtil.buildSqlSelectQueryByExperiment(experiment);
		ResultSet experimentDataResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectQuery);
		if(experimentDataResults != null)
		{
			VaadinControls.bindDbViewRsToVaadinTable(tblExperimentDataReport, experimentDataResults, 1);
			VaadinControls.bindDbViewStringFiltersToVaadinComboBox(cbxExperimentDataReportFilters, experimentDataResults);
			VaadinControls.bindDbViewDateFiltersToVaadinComboBox(cbxDateFieldsFilter, experimentDataResults);
		}

		if(cbxDateFieldsFilter.getItemIds().size() <= 0)
			gridDateFilters.setVisible(false);
		else
			fillCbxDateFilterOperators();
		
		enableDateFilterComponents(false);
		
		txtSearch.addShortcutListener(new ShortcutListener("Enter Shortcut", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				filterExperimentDataResults();
			}
			});
		
		cbxDateFilterOperators.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbxDateFilterOperators.getValue()!=null && cbxDateFilterOperators.getValue().equals("between"))
					showFilter2(true);
				else
					showFilter2(false);
			}   
	    });
		
		btnOkDateFilters.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				filterExperimentDataResults();
			}

		});
		
		chxDateFilters.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(chxDateFilters.getValue()!=null && chxDateFilters.getValue())
					enableDateFilterComponents(true);
				else
					enableDateFilterComponents(false);
			}   
	    });
		
		btnExportExcel.addClickListener(new Button.ClickListener() {
	
			@Override
			public void buttonClick(ClickEvent event) {
				exportExperimentDataReportToExcel();
			}
		
		});
		
	}
	
	private void filterExperimentDataResults()
	{
		if(this.cbxDateFieldsFilter.getValue() != null && this.cbxDateFilterOperators.getValue() != null
				&& this.dtFilter1.getValue() != null)
		{
			if(this.cbxDateFilterOperators.getValue().equals("between") && this.dtFilter2.getValue() == null)
			{
				this.getUI().showNotification("From Date and To Date should be set.", Type.WARNING_MESSAGE);
				return;
			}
			
			ResultSet experimentDataResults = null;
			String sqlSelectQuery = null;
			
			if(this.cbxExperimentDataReportFilters.getValue() != null)
				sqlSelectQuery = ExperimentUtil.buildDateFilteredSqlSelectQueryByExperiment(experiment, (String)this.cbxExperimentDataReportFilters.getValue(), 
						this.txtSearch.getValue(), (String)this.cbxDateFieldsFilter.getValue(), (String)this.cbxDateFilterOperators.getValue(), this.dtFilter1.getValue(), this.dtFilter2.getValue());
			else
				sqlSelectQuery = ExperimentUtil.buildDateFilteredSqlSelectQueryByExperiment(experiment, null, null, 
						 (String)this.cbxDateFieldsFilter.getValue(), (String)this.cbxDateFilterOperators.getValue(), this.dtFilter1.getValue(), this.dtFilter2.getValue());
			//this.txtSearch.setValue(sqlSelectQuery);
			experimentDataResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectQuery);
			
			if(experimentDataResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblExperimentDataReport, experimentDataResults, 1);
		}
		else if(this.cbxExperimentDataReportFilters.getValue() != null )
		{
			String sqlSelectQuery =  ExperimentUtil.buildFilteredSqlSelectQueryByExperiment(experiment, (String)this.cbxExperimentDataReportFilters.getValue(), this.txtSearch.getValue());
			ResultSet experimentDataResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectQuery);
			
			if(experimentDataResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblExperimentDataReport, experimentDataResults, 1);
		}
		
	}
	
	private void fillCbxDateFilterOperators()
	{
		cbxDateFilterOperators.setContainerDataSource(null);
		
		cbxDateFilterOperators.addItem("on");
		cbxDateFilterOperators.setItemCaption("on", "on");
		
		cbxDateFilterOperators.addItem("onorbefore");
		cbxDateFilterOperators.setItemCaption("onorbefore", "on or before");

		cbxDateFilterOperators.addItem("onorafter");
		cbxDateFilterOperators.setItemCaption("onorafter", "on or after");

		cbxDateFilterOperators.addItem("before");
		cbxDateFilterOperators.setItemCaption("before", "before");

		cbxDateFilterOperators.addItem("after");
		cbxDateFilterOperators.setItemCaption("after", "after");
		
		cbxDateFilterOperators.addItem("between");
		cbxDateFilterOperators.setItemCaption("between", "is between");

		cbxDateFilterOperators.select("between");
	}

	private void showFilter2(boolean visible)
	{
		dtFilter2.setValue(null);
		dtFilter2.setVisible(visible);
	}
	
	private void enableDateFilterComponents(boolean enabled)
	{
		this.cbxDateFieldsFilter.setEnabled(enabled);
		this.cbxDateFilterOperators.setEnabled(enabled);
		this.dtFilter1.setEnabled(enabled);
		this.dtFilter2.setEnabled(enabled);
	}
	
	private void exportExperimentDataReportToExcel()
	{
		if(this.tblExperimentDataReport.getItemIds() != null)
		{
			final ExcelExport excelExport;
			excelExport = new ExcelExport(tblExperimentDataReport, this.lblExperimentTitle.getValue().trim());
			excelExport.setExportFileName(this.lblExperimentTitle.getValue().trim() + ".xls");
			excelExport.setDisplayTotals(false);
			excelExport.export();
			
		}
	}
	
}
