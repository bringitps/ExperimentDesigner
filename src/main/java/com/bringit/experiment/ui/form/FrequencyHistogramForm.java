package com.bringit.experiment.ui.form;

import com.bringit.experiment.ui.design.FrequencyHistogramDesign;
import com.quinncurtis.chart2djava.ChartBufferedImage;
import com.quinncurtis.chart2djava.ChartPrint;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import com.bringit.experiment.spc.*;

public class FrequencyHistogramForm extends FrequencyHistogramDesign{

	FrequencyHistogram freqHistogram;
	public FrequencyHistogramForm()
	{
		/*
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "C:\\Users\\Edgar Beltran\\Documents\\MicroVision\\Executable_JAR\\itt_rules.jar");
        pb.directory(new File("C:\\Users\\Edgar Beltran\\Documents\\MicroVision\\"));
        try {
            Process p = pb.start();
            try {
                System.out.println("JAR open.");
				p.waitFor();
	            System.out.println("JAR closed.");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
		*/
		
		/*System.out.println("Running");
		

		 System.out.println("About to build image");	
		 FrequencyHistogramApplet freqHistApplet = new FrequencyHistogramApplet();
		 freqHistApplet.initDemo();
		 freqHistApplet.start();
		*/
		
		 System.out.println("About to build image");	
		 FrequencyHistogramApplet freqHistApplet = new FrequencyHistogramApplet();
		 freqHistApplet.initDemo();
		 freqHistApplet.start();
	/*	freqHistogram = new FrequencyHistogram();
		freqHistogram.getBounds().setBounds(0, 0, 300, 300);
		
		//JPanel contentPane = new JPanel(); 
  	    //contentPane.add(freqHistogram, BorderLayout.CENTER);

		ChartBufferedImage savegraphJPEG = new ChartBufferedImage();
		//savegraphJPEG.getBufferedImage().getBounds().setBounds(0, 0, 300, 300);
		 savegraphJPEG.setChartViewComponent(freqHistogram);
		 //savegraphJPEG.render();
		 savegraphJPEG.saveImageAsJPEG("C:\\apache-tomcat-8.5.20\\webapps\\Testing.jpg");
		 
		//this.spcChartLayout.addComponent((Component)contentPane);
		

		// Image as a file resource
		//FileResource resource = new FileResource(new File("FrequencyApplication1.jpg"));

		//frequencyHistogramJPG = new Image("Image from file", resource);
		
		//frequencyHistogramJPG.set
		//this.
		//ChartPrint printgraph = new ChartPrint();*/
		
	}
	
}
