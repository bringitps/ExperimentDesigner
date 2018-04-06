package com.bringit.experiment.spc;

import java.awt.*;

public class FrequencyHistogramApplet extends javax.swing.JApplet {
  
    static final long serialVersionUID = -8860524626577921865L;
    FrequencyHistogram freqapplication1 = new FrequencyHistogram();

	public void init() {
        initDemo();
    }
    
    public void start() {

      	System.out.println("Demo started");
      	freqapplication1.saveJPEG();
    }
   
    public void initDemo()
    {
      	// This skips the frame window and places the MainPanel directly 
      	// in the content pane of the Applet
        Container contentPane = this.getContentPane();
        contentPane.add(freqapplication1, BorderLayout.CENTER);
    }
}