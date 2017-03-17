package com.bringit.experiment.ui.form;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.TargetColumn;
import com.bringit.experiment.bll.TargetColumnGroup;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.dao.ContractManufacturerDao;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.ExperimentFieldDao;
import com.bringit.experiment.dao.TargetColumnDao;
import com.bringit.experiment.dao.TargetColumnGroupDao;
import com.bringit.experiment.dao.TargetReportDao;
import com.bringit.experiment.ui.design.TargetDataChartDesign;
import com.bringit.experiment.util.VaadinControls;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.MarkerSymbolEnum;
import com.vaadin.addon.charts.model.PlotOptionsScatter;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

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
		
		this.tblTargetDataReport.setVisible(false);
		
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
		

		List<TargetColumn> targetColumns = new ArrayList<TargetColumn>();
		List<TargetColumnGroup> targetColumnGroups = new TargetColumnGroupDao().getTargetColumnGroupsByReportId(targetRpt.getTargetReportId());
		
		for(int i=0; targetColumnGroups!=null && i<targetColumnGroups.size(); i++)
		{
			List<TargetColumn> targetGroupCols = new TargetColumnDao().getTargetColumnsByColGroupById(targetColumnGroups.get(i).getTargetColumnGroupId());
			
			for(int j=0; targetGroupCols!=null && j<targetGroupCols.size(); j++)
				targetColumns.add(targetGroupCols.get(j));
		}
		
		for(int i=0; targetColumns!=null && i<targetColumns.size(); i++)
		{
			if(!targetColumns.get(i).getTargetColumnIsInfo())
			{
				cbxValueX.addItem(targetColumns.get(i).getTargetColumnId());
				cbxValueX.setItemCaption(targetColumns.get(i).getTargetColumnId(), targetColumns.get(i).getTargetColumnLabel());
			
				cbxValueY.addItem(targetColumns.get(i).getTargetColumnId());
				cbxValueY.setItemCaption(targetColumns.get(i).getTargetColumnId(), targetColumns.get(i).getTargetColumnLabel());
			}
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
		
		
		btnApplyFilters.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(cbxValueX.getValue() != null && cbxValueY.getValue() != null)
					buildTargetChart();
			}

		});
	
	}

	private void buildTargetChart()
	{
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
		{	
			VaadinControls.bindDbViewRsToVaadinTable(tblTargetDataReport, spResults, 1);

			layoutChart.removeAllComponents();
			
			Chart targetChart = new Chart(ChartType.SCATTER);
			targetChart.setSizeFull();
	
			Configuration targetChartConf = targetChart.getConfiguration();
			targetChartConf.setTitle(targetRpt.getTargetReportName());
			targetChartConf.getLegend().setEnabled(false);
			targetChartConf.setExporting(true);
			
			Tooltip tooltip = new Tooltip();
			tooltip.setShared(true);
		    tooltip.setFormatter("this.point.name");
		    targetChartConf.setTooltip(tooltip);
			
			//--- Start: Building Pass Rectangle --//
			
			double minValueX = Double.parseDouble(txtTargetValueX.getValue()) -  Double.parseDouble(txtOffsetValueX.getValue());
			double minValueY = Double.parseDouble(txtTargetValueY.getValue()) -  Double.parseDouble(txtOffsetValueY.getValue());
			double maxValueX = Double.parseDouble(txtTargetValueX.getValue()) +  Double.parseDouble(txtOffsetValueX.getValue());
			double maxValueY = Double.parseDouble(txtTargetValueY.getValue()) +  Double.parseDouble(txtOffsetValueY.getValue());
			
			DataSeries passAreaSeries = new DataSeries();
			passAreaSeries.setName("Pass Area");
			passAreaSeries.add(new DataSeriesItem(minValueX,minValueY));
		    passAreaSeries.add(new DataSeriesItem(minValueX,maxValueY));
		    passAreaSeries.add(new DataSeriesItem(maxValueX,maxValueY));
		    passAreaSeries.add(new DataSeriesItem(maxValueX,minValueY));
		    passAreaSeries.add(new DataSeriesItem(minValueX,minValueY));
	
	        PlotOptionsSpline plotTargetRectangle = new PlotOptionsSpline();
	        plotTargetRectangle.setColor(new SolidColor(Color.MAGENTA.getCSS()));
	        passAreaSeries.setPlotOptions(plotTargetRectangle);
	        targetChartConf.addSeries(passAreaSeries);
			//--- End: Building Pass Rectangle --//
	
	        
	        //--- Start: Building Values Points --//
	        DataSeries valuesSeries = new DataSeries();
				    
	
			DataSeriesItem pointTargetValue = new DataSeriesItem( Double.parseDouble(txtTargetValueX.getValue()), Double.parseDouble(txtTargetValueY.getValue()));
			pointTargetValue.setName("Target Value");
			pointTargetValue.setColor(new SolidColor(Color.GREEN.getCSS()));
			Marker markerTargetValue = new Marker();
			markerTargetValue.setSymbol(MarkerSymbolEnum.DIAMOND);
			markerTargetValue.setRadius(10);
			pointTargetValue.setMarker(markerTargetValue);
			valuesSeries.add(pointTargetValue);
				
		    Marker markerPointValue = new Marker();
		    markerPointValue.setSymbol(MarkerSymbolEnum.DIAMOND);
		    markerPointValue.setRadius(5);
		    //markerPointValue.setFillColor(new SolidColor(Color.MAGENTA.getCSS()));
		    DataSeriesItem newValue = new DataSeriesItem( Double.parseDouble(txtTargetValueX.getValue()), Double.parseDouble(txtTargetValueY.getValue()));
		  
		    String[] tblColumnHeaders = tblTargetDataReport.getColumnHeaders();

			String colXName = cbxValueX.getItemCaption(cbxValueX.getValue());
			String colYName = cbxValueY.getItemCaption(cbxValueY.getValue());

			String valueX = "";
			String toolTipHeaderX = "";
			String valueY = "";
			String toolTipHeaderY = "";
			
			String toolTipBody = "";
			
		    Collection itemIds = tblTargetDataReport.getContainerDataSource().getItemIds();
			int cnt=1;
			for (Object itemIdObj : itemIds) 
			{	
				toolTipBody = "";
				
				Item tblRowItem = tblTargetDataReport.getContainerDataSource().getItem(itemIdObj.toString());
				
				for(int i=0; i<tblColumnHeaders.length; i++)
				{
					if(tblColumnHeaders[i].trim().equals(colXName))
					{
						//System.out.println( colXName + " - " + (String)(tblRowItem.getItemProperty(colXName).getValue()));
						
						toolTipHeaderX = "<b>" + colXName + " [X]:</b> " + (String)(tblRowItem.getItemProperty(colXName).getValue());
						valueX = (String)(tblRowItem.getItemProperty(colXName).getValue());
					}
					if(tblColumnHeaders[i].trim().equals(colYName))
					{
						toolTipHeaderY = "<b>" + colYName + " [Y]:</b> " + (String)(tblRowItem.getItemProperty(colYName).getValue());
						valueY = (String)(tblRowItem.getItemProperty(colYName).getValue()); 
					}
					
					//System.out.println("Column Name: "  + tblColumnHeaders[i].trim()  + "Column Value: " + (String)(tblRowItem.getItemProperty(tblColumnHeaders[i]).getValue()));
					if(!tblColumnHeaders[i].trim().equals(colXName) && !tblColumnHeaders[i].trim().equals(colYName))
						toolTipBody +=  tblColumnHeaders[i].trim() + ": " + (String)(tblRowItem.getItemProperty(tblColumnHeaders[i]).getValue()) + "<br>";
				}
				
				if(valueX != null && valueY != null && !valueX.isEmpty() && !valueY.isEmpty())
				{
					newValue = new DataSeriesItem(Double.parseDouble(valueX), Double.parseDouble(valueY));
					newValue.setColor(new SolidColor(Color.BLUE.getCSS()));
					newValue.setName(toolTipHeaderX+"<br>"+toolTipHeaderY+"<br><br>"+toolTipBody);
					valuesSeries.add(newValue);
				}
			}
		    
			/*
		    for(int i=0; i<10; i++)
		    {
		    	newValue = new DataSeriesItem(i*1.5, i+1.5);
		    	newValue.setColor(new SolidColor(Color.BLUE.getCSS()));
		    	newValue.setName("Testing<br>NewLine");
				valuesSeries.add(newValue);
				   
			}
	
		    for(int i=3; i<9; i++)
		    {
		    	newValue = new DataSeriesItem(i*0.5, i+1);
		    	newValue.setColor(new SolidColor(Color.BLUE.getCSS()));
		    	newValue.setName("Testing<br>NewLine");
				valuesSeries.add(newValue);
				   
			}
			*/
			
			PlotOptionsScatter plotValues = new PlotOptionsScatter();
			valuesSeries.setPlotOptions(plotValues);
		    targetChartConf.addSeries(valuesSeries);
		    
			//--- End: Building Values Points --//
		    
		    tblTargetDataReport.setVisibleColumns(new Object[] {cbxValueX.getItemCaption(cbxValueX.getValue()), cbxValueY.getItemCaption(cbxValueY.getValue())});
			tblTargetDataReport.setVisible(true);
			lblChartInstruction.setVisible(false);		
			layoutChart.addComponent(targetChart);
			
			
		}
		else
			this.getUI().showNotification("No data found for selected criteria.", Type.WARNING_MESSAGE);
	}
	
}
