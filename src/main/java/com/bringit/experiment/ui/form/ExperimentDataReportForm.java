package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.vaadin.peter.contextmenu.ContextMenu;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItemClickEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItemClickListener;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedListener;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedOnComponentEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedOnTableFooterEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedOnTableHeaderEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedOnTableRowEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedOnTreeItemEvent;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.ui.design.ExperimentDataReportDesign;
import com.bringit.experiment.util.ExperimentUtil;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.addon.tableexport.ExcelExport;

//import com.vaadin.addon.tableexport.TableExport;

public class ExperimentDataReportForm extends ExperimentDataReportDesign{

	Experiment experiment = new Experiment();
	List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
	
	
	private final ContextMenuItemClickListener clickListener = new ContextMenuItemClickListener() {

		@Override
		public void contextMenuItemClicked(ContextMenuItemClickEvent event) {
			Notification.show(event.getSource().toString());
			System.out.println("Hello world");
		}
	};

	private final ContextMenuOpenedListener.ComponentListener openComponentListener = new ContextMenuOpenedListener.ComponentListener() {

		@Override
		public void onContextMenuOpenFromComponent(
				ContextMenuOpenedOnComponentEvent event) {
			event.getContextMenu().removeAllItems();
			event.getContextMenu().addItem("Empty space");
			event.getContextMenu().open(event.getX(), event.getY());
			System.out.println("Hello world");
		}
	};

	private final ContextMenuOpenedListener.TableListener openListener = new ContextMenuOpenedListener.TableListener() {

		@Override
		public void onContextMenuOpenFromRow(
				ContextMenuOpenedOnTableRowEvent event) {
			event.getContextMenu().removeAllItems();
			event.getContextMenu().addItem("Item " + event.getItemId());
			System.out.println("Hello world");
		}

		@Override
		public void onContextMenuOpenFromHeader(
				ContextMenuOpenedOnTableHeaderEvent event) {
			event.getContextMenu().removeAllItems();
			event.getContextMenu().addItem("Item " + event.getPropertyId());
			System.out.println("Hello world");
		}

		@Override
		public void onContextMenuOpenFromFooter(
				ContextMenuOpenedOnTableFooterEvent event) {
			event.getContextMenu().addItem("Item " + event.getPropertyId());
			System.out.println("Hello world");
		}
	};

	private final ContextMenuOpenedListener.TreeListener treeItemListener = new ContextMenuOpenedListener.TreeListener() {

		@Override
		public void onContextMenuOpenFromTreeItem(
				ContextMenuOpenedOnTreeItemEvent event) {
			Notification.show("Tree item clicked " + event.getItemId());
			System.out.println("Hello world");
		}
	};
	
	
	public ExperimentDataReportForm(int experimentId)
	{
		experiment = new ExperimentDao().getExperimentById(experimentId);
		experimentFields = new ExperimentFieldDao().getActiveExperimentFields(experiment);
		
		String sqlSelectQuery =  ExperimentUtil.buildSqlSelectQueryByExperiment(experiment, experimentFields);
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
		
		/*
		tblExperimentDataReport.addItemClickListener(new ItemClickEvent.ItemClickListener() 
	    {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick())
                	openDataViewRecordCRUDModalWindow(Integer.parseInt(event.getItemId().toString()));
            }
        });
		*/

		ContextMenu tableContextMenu = new ContextMenu();
		tableContextMenu.addContextMenuComponentListener(openComponentListener);
		tableContextMenu.addContextMenuTableListener(openListener);
		tableContextMenu.addItem("Table test item #1");
		
		tableContextMenu.setOpenAutomatically(false);
		tableContextMenu.setAsTableContextMenu(tblExperimentDataReport);
		
		//this.addComponent((Component) tableContextMenu);
		/*
		ContextMenu tableContextMenu = new ContextMenu();
		tableContextMenu.addContextMenuComponentListener(openComponentListener);
		tableContextMenu.addContextMenuTableListener(openListener);
		tableContextMenu.addItem("Table test item #1");
		tableContextMenu.setAsTableContextMenu(tblExperimentDataReport);
		*/
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
				sqlSelectQuery = ExperimentUtil.buildDateFilteredSqlSelectQueryByExperiment(experiment, experimentFields, (String)this.cbxExperimentDataReportFilters.getValue(), 
						this.txtSearch.getValue(), (String)this.cbxDateFieldsFilter.getValue(), (String)this.cbxDateFilterOperators.getValue(), this.dtFilter1.getValue(), this.dtFilter2.getValue());
			else
				sqlSelectQuery = ExperimentUtil.buildDateFilteredSqlSelectQueryByExperiment(experiment, experimentFields, null, null, 
						 (String)this.cbxDateFieldsFilter.getValue(), (String)this.cbxDateFilterOperators.getValue(), this.dtFilter1.getValue(), this.dtFilter2.getValue());
			//this.txtSearch.setValue(sqlSelectQuery);
			experimentDataResults = new ExecuteQueryDao().getSqlSelectQueryResults(sqlSelectQuery);
			
			if(experimentDataResults != null)
				VaadinControls.bindDbViewRsToVaadinTable(tblExperimentDataReport, experimentDataResults, 1);
		}
		else if(this.cbxExperimentDataReportFilters.getValue() != null )
		{
			String sqlSelectQuery =  ExperimentUtil.buildFilteredSqlSelectQueryByExperiment(experiment, experimentFields, (String)this.cbxExperimentDataReportFilters.getValue(), this.txtSearch.getValue());
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
			final ExcelExport xlsExport;
			xlsExport = new ExcelExport(tblExperimentDataReport, this.lblExperimentTitle.getValue().trim());
			xlsExport.setExportFileName(this.lblExperimentTitle.getValue().trim() + ".xls");
			xlsExport.setDisplayTotals(false);
			xlsExport.export();
		}
	}
	
	public void openDataViewRecordCRUDModalWindow(int experimentRecordId)
	{
		 Window dataViewRecordCRUDModalWindow = new Window("View Record");
		 dataViewRecordCRUDModalWindow.setModal(true);
		 dataViewRecordCRUDModalWindow.setResizable(false);
		 dataViewRecordCRUDModalWindow.setContent(new ExperimentDataViewRecordForm(this.experiment, this.experimentFields, experimentRecordId));
		 dataViewRecordCRUDModalWindow.setWidth(940, Unit.PIXELS);
		 dataViewRecordCRUDModalWindow.setHeight(760, Unit.PIXELS);
		 dataViewRecordCRUDModalWindow.center();
		 dataViewRecordCRUDModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				//reloadXmlTemplateDbViewResults();
			}
		});
		 this.getUI().addWindow(dataViewRecordCRUDModalWindow);
    }
}
