package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.TargetColumn;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.TargetColumnDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.ui.design.TargetDataChartDesign;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.PlotOptionsScatter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Sizeable.Unit;

public class TargetDataChartForm extends TargetDataChartDesign {
	
	private TargetReport targetRpt = new TargetReport();
	Experiment experiment = new Experiment();
	List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
	List<ContractManufacturer> contractManufacturers = new ArrayList<ContractManufacturer>();
	
	public TargetDataChartForm(TargetReport targetRpt, Experiment experiment, List<ExperimentField> experimentFields, List<ContractManufacturer> contractManufacturers, Object selectedDateFieldName, Date fromDate, Date toDate, Object selectedCm, Object selectedExpFieldName, String expFieldValue)
	{
		this.targetRpt = targetRpt;
		this.experiment = experiment;
		this.experimentFields = experimentFields;
		this.contractManufacturers = contractManufacturers;
		//new ExperimentFieldDao().getActiveExperimentFields(targetRpt.getExperiment());
		
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
		
		for(int i=0; contractManufacturers!=null && i<contractManufacturers.size(); i++)
		{
			cbxContractManufacturer.addItem(contractManufacturers.get(i).getCmId());
			cbxContractManufacturer.setItemCaption(contractManufacturers.get(i).getCmId(), contractManufacturers.get(i).getCmName());
		}
		cbxContractManufacturer.setInvalidAllowed(false);
		

		List<TargetColumn> targetColumns = new TargetColumnDao().getTargetColumnsByTargetReportId(targetRpt.getTargetReportId());
		for(int i=0; targetColumns!=null && i<targetColumns.size(); i++)
		{
			cbxValueX.addItem(targetColumns.get(i).getTargetColumnId());
			cbxValueX.setItemCaption(targetColumns.get(i).getTargetColumnId(), targetColumns.get(i).getTargetColumnLabel());
			
			cbxValueY.addItem(targetColumns.get(i).getTargetColumnId());
			cbxValueY.setItemCaption(targetColumns.get(i).getTargetColumnId(), targetColumns.get(i).getTargetColumnLabel());
		}
		
		cbxValueX.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbxValueX.getValue()!=null){
					TargetColumn selectedTargetColumn = new TargetColumnDao().getTargetColumnById((Integer)cbxValueX.getValue());
					txtOffsetValueX.setValue(selectedTargetColumn.getTargetColumnOffset().toString());
					txtTargetValueX.setValue(selectedTargetColumn.getTargetColumnGoalValue().toString());
				}
				
			}   
	    });

		cbxValueY.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbxValueY.getValue()!=null){
					TargetColumn selectedTargetColumn = new TargetColumnDao().getTargetColumnById((Integer)cbxValueY.getValue());
					txtOffsetValueY.setValue(selectedTargetColumn.getTargetColumnOffset().toString());
					txtTargetValueY.setValue(selectedTargetColumn.getTargetColumnGoalValue().toString());
				}
				
			}   
	    });
		
		if(selectedDateFieldName != null) cbxDateFieldsFilter.setValue(selectedDateFieldName);
		if(fromDate != null) dtFilter1.setValue(fromDate);
		if(toDate != null) dtFilter2.setValue(toDate);
		
		if(selectedExpFieldName != null) cbxExpFieldFilter.setValue(selectedExpFieldName);
		if(expFieldValue != null && !expFieldValue.isEmpty()) txtExpFieldFilter.setValue(expFieldValue);
		
		if(selectedCm != null) cbxContractManufacturer.setValue(selectedCm);
		
		Chart chart = new Chart(ChartType.SCATTER);
		chart.setWidth(100, Unit.PERCENTAGE);
		chart.setHeight(100, Unit.PERCENTAGE);

		// Modify the default configuration a bit
		Configuration conf = chart.getConfiguration();
		conf.setTitle(targetRpt.getTargetReportName());
		conf.getLegend().setEnabled(false); // Disable legend

		PlotOptionsScatter options = new PlotOptionsScatter();
		// ... Give overall plot options here ...
		conf.setPlotOptions(options);

		DataSeries series = new DataSeries();
		for (int i=0; i<300; i++) {
		    double lng = Math.random() * 2 * Math.PI;
		    double lat = Math.random() * Math.PI - Math.PI/2;
		    double x   = Math.cos(lat) * Math.sin(lng);
		    double y   = Math.sin(lat);
		    double z   = Math.cos(lng) * Math.cos(lat);

		    DataSeriesItem point = new DataSeriesItem(x,y);
		    Marker marker = new Marker();
		    // Make settings as described later
		    point.setMarker(marker);
		    series.add(point);
		}
		conf.addSeries(series);
		
		layoutChart.addComponent(chart);
	}

	
}
