package com.bringit.experiment.ui.form;

import com.bringit.experiment.ui.design.FrequencyHistogramDesign;
import com.quinncurtis.chart2djava.ChartBufferedImage;
import com.quinncurtis.chart2djava.ChartPrint;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.bringit.experiment.spc.*;

public class FrequencyHistogramForm extends FrequencyHistogramDesign{

	FrequencyHistogram freqHistogram;
	public FrequencyHistogramForm()
	{
    	JFrame freqHistogramFrame = new JFrame();
    	freqHistogramFrame.setSize(1024, 768);

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
    	
        final FrequencyHistogramApplet freqHistogramApplet = new FrequencyHistogramApplet();
        freqHistogramApplet.freqHistogram.initializeCustom(20, 60, freqLimits, freqValues);
        freqHistogramFrame.getContentPane().add(freqHistogramApplet);

        freqHistogramFrame.setVisible(true);
        freqHistogramApplet.init();
        freqHistogramApplet.start();

        try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        String spcChartRandomImageFile =  freqHistogramApplet.freqHistogram.saveRandomJPEG();
        
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        if(spcChartRandomImageFile != null)
        {
			try {
				
				InputStream randomImageInputStream = new BufferedInputStream(new FileInputStream(spcChartRandomImageFile));
				BufferedImage imgFile = ImageIO.read(randomImageInputStream);

	        	Image image = new Image("", createCharImageStreamResource(imgFile, spcChartRandomImageFile));

	        	image.setWidth(576, Unit.PIXELS);
	        	image.setHeight(432, Unit.PIXELS);
	        	
	        	this.spcChartLayout.addComponent(image);
	        	
	        	//new File(spcChartRandomImageFile).delete();
	        	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        freqHistogramFrame.removeAll();
        freqHistogramFrame.dispose();
		 
	}
	
	private StreamResource createCharImageStreamResource(BufferedImage bufferedChartImage, String fileName) {
	    return new StreamResource(new StreamSource() {
	        @Override
	        public InputStream getStream() {
	        	
	        	System.out.println("Image size: " + bufferedChartImage.getHeight());
	        	
	            String text = "Date: " + DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM).format(new Date());

	            //BufferedImage bi = new BufferedImage(370, 30, BufferedImage.TYPE_3BYTE_BGR);
	            //bufferedChartImage.getGraphics().drawChars(text.toCharArray(), 0, text.length(), 10, 20);
	            
	            try {
	                ByteArrayOutputStream bos = new ByteArrayOutputStream();
	                ImageIO.write(bufferedChartImage, "jpg", bos);
	                return new ByteArrayInputStream(bos.toByteArray());
	            } catch (IOException e) {
	                e.printStackTrace();
	                return null;
	            }
	        }
	    }, fileName);
	}
	
	
}
