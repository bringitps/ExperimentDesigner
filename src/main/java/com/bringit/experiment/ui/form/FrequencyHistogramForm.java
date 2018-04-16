package com.bringit.experiment.ui.form;

import com.bringit.experiment.ui.design.FrequencyHistogramDesign;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;

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
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


@JavaScript({"vaadin://themes/mytheme/QCSPCChartGWTWar/chartdefSimple.js", "vaadin://themes/mytheme/QCSPCChartGWTWar/qcspcchartgwt/qcspcchartgwt.nocache.js"})
@StyleSheet({"vaadin://themes/mytheme/QCSPCChartGWTWar/QCSPCChartGWT.css"})
public class FrequencyHistogramForm extends FrequencyHistogramDesign{

	public FrequencyHistogramForm()
	{
		

		ThemeResource tr = new ThemeResource("QCSPCChartGWTWar/SPCSimple.html");
		BrowserFrame embedded = new BrowserFrame("SPC Chart Loaded", tr);
		embedded.setWidth("800px");
		embedded.setHeight("600px");
		this.spcChartLayout.addComponent(embedded);
		
		//this.spcChartLayout.addComponent(new Label("<iframe src='javascript:\"\"' id=\"qcspcchartgwt\" tabindex=\"-1\" style=\"position: absolute; width: 0px; height: 0px; border: none; left: -1000px; top: -1000px;\"></iframe>", ContentMode.HTML));
		
		//this.spcChartLayout.addComponent(new Label("<div id=\"qcspcchargwt\">   </div>", ContentMode.HTML));
		
		/*
		BrowserFrame embedded = new BrowserFrame("SPC Chart Loaded", new ExternalResource("https://www.google.com"));
		embedded.setWidth("600px");
		embedded.setHeight("400px");
		this.spcChartLayout.addComponent(embedded);*/
		/*
		URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
		File file = new File(location.getPath());
		
		String currentDirectory = file.getAbsolutePath();
		System.out.println(currentDirectory + "\\QCSPCChartGWTWar\\SPCExampleScripts.html");
		BrowserFrame browser = new BrowserFrame("", new ExternalResource(currentDirectory + "\\QCSPCChartGWTWar\\SPCExampleScripts.html"));
			browser.setWidth("600px");
			browser.setHeight("400px");
			browser.setVisible(true);
			this.spcChartLayout.addComponent(browser);
		*/
		
		/*
		this.btnApplyFilters.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {		
				//Page.getCurrent().getJavaScript().execute("$(\"#spc_div\").append($(\"#qcspcchargwt\"))");
				//$( "div.demo-container" ).html();
				//Page.getCurrent().getJavaScript().execute("alert($(\"#qcspcchargwt\").html())");
				Page.getCurrent().getJavaScript().execute("defineChartUsingJSON()");
			}//

		});
		*/
		
		
		
		//Page.getCurrent().getJavaScript().execute("alert($(\"#qcspcchargwt\").text());");
		//Page.getCurrent().getJavaScript().execute("$(\"#qcspcchargwt\").appendTo(\"#spc_div\")");

		//Page.getCurrent().getJavaScript().execute("function defineChartUsingJSON( ) { alert('function'); var s = JSON.stringify(TimeXBarR); return s; }");

		/*
		ThemeResource tr = new ThemeResource("QCSPCChartGWTWar/SPCSimple.html");
		BrowserFrame embedded = new BrowserFrame("SPC Chart Loaded", tr);
		embedded.setWidth("600px");
		embedded.setHeight("400px");*/
		
		/*
		final String filename = "report-" + System.currentTimeMillis()
        + ".html";
		final StreamResource stream = new StreamResource(
        new StreamResource.StreamSource() {
            public InputStream getStream() {
            	System.out.println(getFrequencyHistogramHtmlCode());
                return new ByteArrayInputStream((getFrequencyHistogramHtmlCode()).getBytes());
            }
        }, filename);
		
				

		BrowserFrame browser = new BrowserFrame("html", stream);
		browser.setWidth("600px");
		browser.setHeight("400px");
		browser.setVisible(true);
		this.spcChartLayout.addComponent(browser);
		*/
		
		
		/*
		URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
		File file = new File(location.getPath());
		
		String currentDirectory = file.getAbsolutePath();
		BrowserFrame browser = new BrowserFrame("", new ExternalResource(currentDirectory + "\\QCSPCChartGWTWar\\SPCExampleScripts.html"));
			browser.setWidth("600px");
			browser.setHeight("400px");
			browser.setVisible(true);
			this.spcChartLayout.addComponent(browser);
			
		
		/*
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
		 */
	}
	
	private String getFrequencyHistogramHtmlCode()
	{
		
		String htmlCode = "<iframe src='javascript:\"\"' id=\"qcspcchartgwt\" tabindex=\"-1\" style=\"position: absolute; width: 0px; height: 0px; border: none; left: -1000px; top: -1000px;\"></iframe>";
		/*		
		"<html> " +
		  "<head> " +
		 
		   "<script> " +


		    "function defineChartUsingJSON( ) " +
		   "{ " +
		     "var s = JSON.stringify(TimeXBarR); " +
		     "return s; " +
		   "} " +
		   
		    "</script>   " +
		  
		    "<!--                                           --> " +
		    "<!-- This script loads your compiled module.   --> " +
		    "<!-- If you add any GWT meta tags, they must   --> " +
		    "<!-- be added before this line.                --> " +
		    "<!--                                           --> " +
		  "</head> " +
		"</html>";
		*/
		
		return htmlCode;
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
