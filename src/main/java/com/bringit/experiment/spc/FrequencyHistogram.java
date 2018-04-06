package com.bringit.experiment.spc;
import java.awt.Color;

import com.quinncurtis.chart2djava.*;
import com.quinncurtis.spcchartjava.*;


public class FrequencyHistogram extends FrequencyHistogramChart{

	static final long serialVersionUID = 1075702098935713616L;


	public FrequencyHistogram() {
	    try {
	    	InitializeGraph();
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
	  }	  

		private void InitializeGraph()
		{

			// Frequency bins
			double [] freqLimits = {19.5, 24.5, 29.5, 34.5, 39.5, 44.5, 49.5, 54.5, 59.5};
			// data to be sorted into frequency bins
			double [] freqValues = {32,44,44,42,57,
									   26,51,23,33,27,
									   42,46,43,45,44,
									   53,37,25,38,44,
									   36,40,36,48,56,
									   47,40,58,45,38,
									   32,39,43,31,45,
									   41,37,31,39,33,
									   20,50,33,50,51,
									   28,51,40,52,43};

			// Initialize histogram
			this.initFrequencyHistogram(freqLimits, freqValues);
			// Set bar orientation
			this.getMainTitle().setTextString("Frequency Histogram of Selected Data");
			// Build chart
			this.setChartOrientation(ChartObj.VERT_DIR);
			this.setBarFillColor(ChartColors.LIGHTYELLOW);
			
			this.addFrequencyHistogramControlLine(20.0,new ChartAttribute(ChartColors.LIGHTGREEN, 2));
			this.addFrequencyHistogramControlLine(60.0,new ChartAttribute(ChartColors.LIGHTGREEN, 2));
			this.setAutoNormalCurve( true);
			
			this.getFrequencyHistogramPlot().setSegmentFillColor(4,new Color(127, 127, 255));
			this.buildChart();
			
			//this.saveJPEG();
		}


		  public void print(ChartPrint printobj)
		  {
		    printobj.setPrintChartView(this);
		    printobj.startPrint();
		  }
	

		 public void saveJPEG()
		  {
			 System.out.println("Saving image");
			 String filename = "FrequencyApplication1.jpg";
			 ChartBufferedImage savegraphJPEG = new ChartBufferedImage();
			 savegraphJPEG.setChartViewComponent(this);
		    //savegraphJPEG.render();
			 savegraphJPEG.saveImageAsJPEG(filename);/*
		    ImageFileChooser imagefilechooser = new ImageFileChooser(this);
		    if (imagefilechooser.process(filename))
		    {
		    filename = imagefilechooser.getSelectedFilename();
		    savegraphJPEG.setChartViewComponent(this);
		    savegraphJPEG.render();
		    savegraphJPEG.saveImageAsJPEG(filename);
		    }*/
			 System.out.println("Saved");
		  }			
	
}
