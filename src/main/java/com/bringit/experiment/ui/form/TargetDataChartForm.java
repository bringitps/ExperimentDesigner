package com.bringit.experiment.ui.form;

import com.bringit.experiment.ui.design.TargetDataChartDesign;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.PlotOptionsScatter;

public class TargetDataChartForm extends TargetDataChartDesign {

	public TargetDataChartForm()
	{
		Chart chart = new Chart(ChartType.SCATTER);
		chart.setWidth("500px");
		chart.setHeight("500px");

		// Modify the default configuration a bit
		Configuration conf = chart.getConfiguration();
		conf.setTitle("Random Sphere");
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
