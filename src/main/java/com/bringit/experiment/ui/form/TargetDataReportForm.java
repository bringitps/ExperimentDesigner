package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.dao.DataBaseViewDao;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.ui.design.TargetDataReportDesign;
import com.bringit.experiment.util.ExperimentUtil;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;

public class TargetDataReportForm extends TargetDataReportDesign{

	private TargetReport targetRpt = new TargetReport();
	Experiment experiment = new Experiment();
	List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
	List<ContractManufacturer> contractManufacturers = new ArrayList<ContractManufacturer>();
	
	public TargetDataReportForm(Integer targetDataReportId)
	{
		targetRpt = new TargetReportDao().getTargetReportById(targetDataReportId);
		lblTargetRptTitle.setValue(lblTargetRptTitle.getValue() + " - " + targetRpt.getTargetReportName());
		
		experimentFields = new ExperimentFieldDao().getActiveExperimentFields(targetRpt.getExperiment());
		
		cbxDateFieldsFilter.setContainerDataSource(null);
		cbxExpFieldFilter.setContainerDataSource(null);

		cbxDateFieldsFilter.addItem("CreatedDate");
		cbxDateFieldsFilter.setItemCaption("CreatedDate", "Created Date");

		cbxDateFieldsFilter.addItem("LastModifiedDate");
		cbxDateFieldsFilter.setItemCaption("LastModifiedDate", "Last Modified Date");

		cbxDateFieldsFilter.setInvalidAllowed(false);
		cbxExpFieldFilter.setInvalidAllowed(false);
		
		for(int i=0; experimentFields != null &&i<experimentFields.size(); i++)
		{
			if(experimentFields.get(i).getExpFieldType().trim().contains("date"))
			{
				cbxDateFieldsFilter.addItem(experimentFields.get(i).getExpDbFieldNameId());
				cbxDateFieldsFilter.setItemCaption(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());
			}
			else 	if(experimentFields.get(i).getExpFieldType().trim().startsWith("varchar") || experimentFields.get(i).getExpFieldType().trim().startsWith("char")
					|| experimentFields.get(i).getExpFieldType().trim().startsWith("text") ||experimentFields.get(i).getExpFieldType().trim().startsWith("nvarchar") 
					|| experimentFields.get(i).getExpFieldType().trim().startsWith("nchar") || experimentFields.get(i).getExpFieldType().trim().startsWith("ntext"))
		
			{
				cbxExpFieldFilter.addItem(experimentFields.get(i).getExpDbFieldNameId());
				cbxExpFieldFilter.setItemCaption(experimentFields.get(i).getExpDbFieldNameId(), experimentFields.get(i).getExpFieldName());
			}
		}
		
		contractManufacturers = new ContractManufacturerDao().getAllContractManufacturers();
		for(int i=0; contractManufacturers!=null && i<contractManufacturers.size(); i++)
		{
			cbxContractManufacturer.addItem(contractManufacturers.get(i).getCmId());
			cbxContractManufacturer.setItemCaption(contractManufacturers.get(i).getCmId(), contractManufacturers.get(i).getCmName());
		}
		cbxContractManufacturer.setInvalidAllowed(false);
		
		//Call SP with TargetReportId
		List<String> spParams = new ArrayList<String>();
		spParams.add(targetRpt.getTargetReportId().toString());
		spParams.add(targetRpt.getExperiment().getExpId().toString());
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
		spParams.add("");
				
		ResultSet spResults = new ExecuteQueryDao().executeStoredProcedure("spTargetReportBuilder", spParams);
		
		if(spResults != null)
			VaadinControls.bindDbViewRsToVaadinTable(tblTargetDataReport, spResults, 1);
		
		this.btnApplyFilters.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				/*
				 * spTargetReportBuilder 
			 		@TargetReportId NVARCHAR(MAX),
					@ExperimentId NVARCHAR(MAX),
					@DateFieldName NVARCHAR(MAX),
					@FromDate NVARCHAR(MAX),
					@ToDate NVARCHAR(MAX),
					@CmId NVARCHAR(MAX),
					@ExpFieldName NVARCHAR(MAX),
					@ExpFieldValue NVARCHAR(MAX)
				 */
				
				String dateFieldName = "";
				String fromDate = "";
				String toDate = "";
				String cmId = "";
				String expFieldName = "";
				String expFieldValue = "";
				
				if(cbxContractManufacturer.getValue() != null && !cbxContractManufacturer.getValue().toString().isEmpty())
					cmId = cbxContractManufacturer.getValue().toString().trim();
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
				if(cbxDateFieldsFilter.getValue() != null && !cbxDateFieldsFilter.getValue().toString().isEmpty()
						&& dtFilter1.getValue() != null && dtFilter2.getValue() != null)
				{
					dateFieldName = cbxDateFieldsFilter.getValue().toString();
					fromDate = df.format(dtFilter1.getValue());
					Date toDate24hours = dtFilter2.getValue();
					toDate24hours.setHours(23);
					toDate24hours.setMinutes(59);
					toDate24hours.setSeconds(59);
					toDate = df.format(toDate24hours);
				}
				
				if(cbxExpFieldFilter.getValue() != null && !cbxExpFieldFilter.getValue().toString().isEmpty()
						&& txtExpFieldFilter.getValue() != null && !txtExpFieldFilter.getValue().isEmpty())
				{
					expFieldName = cbxExpFieldFilter.getValue().toString();
					expFieldValue = txtExpFieldFilter.getValue();
				}
				
				//Call SP with TargetReportId
				List<String> spParams = new ArrayList<String>();
				spParams.add(targetRpt.getTargetReportId().toString());
				spParams.add(targetRpt.getExperiment().getExpId().toString());
				spParams.add(dateFieldName);
				spParams.add(fromDate);
				spParams.add(toDate);
				spParams.add(cmId);
				spParams.add(expFieldName);
				spParams.add(expFieldValue);
						
				ResultSet spResults = new ExecuteQueryDao().executeStoredProcedure("spTargetReportBuilder", spParams);
				
				if(spResults != null)
					VaadinControls.bindDbViewRsToVaadinTable(tblTargetDataReport, spResults, 1);
				
			}

		});

		this.btnViewChart.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				openViewChartModalWindow();
			}

		});

		this.btnExportExcel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				exportExperimentDataReportToExcel();
			}

		});
	}
	
	
	private void openViewChartModalWindow()
	{
		 Window viewChartModalWindow = new Window(this.targetRpt.getTargetReportName());
		 viewChartModalWindow.setModal(true);
		 viewChartModalWindow.setResizable(false);
		 viewChartModalWindow.setContent(new TargetDataChartForm(this.targetRpt, this.experiment, this.experimentFields, this.contractManufacturers, cbxDateFieldsFilter.getValue(), dtFilter1.getValue(), dtFilter2.getValue(), cbxContractManufacturer.getValue(), cbxExpFieldFilter.getValue(), txtExpFieldFilter.getValue()));
		 viewChartModalWindow.setWidth(90, Unit.PERCENTAGE);
		 viewChartModalWindow.setHeight(90, Unit.PERCENTAGE);
		 viewChartModalWindow.center();
		 viewChartModalWindow.addCloseListener(new Window.CloseListener() {
			
			@Override
			public void windowClose(CloseEvent e) {
				// TODO Auto-generated method stub
				//filterExperimentDataResults();
			}
		});
		 this.getUI().addWindow(viewChartModalWindow);
    }
	
	private void exportExperimentDataReportToExcel()
	{
		if(tblTargetDataReport.getItemIds() != null)
		{
			final ExcelExport xlsExport;
			xlsExport = new ExcelExport(tblTargetDataReport, targetRpt.getTargetReportName().trim());
			xlsExport.setExportFileName(targetRpt.getTargetReportName().trim() + ".xls");
			xlsExport.setDisplayTotals(false);
			xlsExport.export();
		}
	}
}
