package com.bringit.experiment.spc;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import org.apache.commons.lang.RandomStringUtils;

import com.quinncurtis.chart2djava.*;
import com.quinncurtis.spcchartjava.*;


public class FrequencyHistogram extends FrequencyHistogramChart{

	static final long serialVersionUID = 1075702098935713616L;

	private double lowerLimit;
	private double upperLimit;
	private double [] freqLimits;
	private double [] freqValues;
	
	public FrequencyHistogram() {
	    try {
	    	//InitializeGraph();
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
	  }	  

	public void initializeCustom(double lowerLimit, double upperLimit, double [] freqLimits, double [] freqValues)
	{
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.freqLimits = freqLimits;
		this.freqValues = freqValues;
		InitializeGraph();
	}
	
		private void InitializeGraph()
		{

			// Frequency bins
			//double [] freqLimits = {19.5, 24.5, 35.5, 50};
			// data to be sorted into frequency bins
			//double [] freqValues = {21,22,23,24,25,26,27,28};

			// Initialize histogram
			this.initFrequencyHistogram(this.freqLimits, this.freqValues);
			// Set bar orientation
			this.getMainTitle().setTextString("Frequency Histogram of Selected Data");
			this.setFont(new Font("Serif", Font.PLAIN, 12));
			
			// Build chart
			this.setChartOrientation(ChartObj.VERT_DIR);
			this.setBarFillColor(ChartColors.LIGHTYELLOW);
			
			this.addFrequencyHistogramControlLine(this.lowerLimit,new ChartAttribute(ChartColors.LIGHTGREEN, 2));
			this.addFrequencyHistogramControlLine(this.upperLimit,new ChartAttribute(ChartColors.LIGHTGREEN, 2));
			this.setAutoNormalCurve(true);
			
			this.getFrequencyHistogramPlot().setSegmentFillColor(4,new Color(127, 127, 255));
			//this.getMainTitle().rem
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
			 String filename = "C:\\Users\\Edgar Beltran\\Documents\\MicroVision\\Executable_JAR\\FrequencyApplication1.jpg";
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
		 
		 public BufferedImage getChartImage()
		 {
			 ChartBufferedImage chartImage = new ChartBufferedImage();
//			 chartImage.getBufferedImage()
			 chartImage.setChartViewComponent(this);
			 chartImage.render();
			 return chartImage.getBufferedImage();
		 }
		 
		 public String saveRandomJPEG()
		 {
			 String randomImageFileName = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), "jpg");
			 try
			 {
				 ChartBufferedImage savegraphJPEG = new ChartBufferedImage();
				 savegraphJPEG.setChartViewComponent(this);
				 System.out.println("Saving " + randomImageFileName);
			     savegraphJPEG.saveImageAsJPEG(randomImageFileName);
				 System.out.println("Saved " + randomImageFileName);
				 return randomImageFileName;
			 }
			 catch(Exception e)
			 {
				 return null;
			 }
		 }
	
}
