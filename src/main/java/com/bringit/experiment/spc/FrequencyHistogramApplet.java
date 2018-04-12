package com.bringit.experiment.spc;

import java.awt.*;

public class FrequencyHistogramApplet extends javax.swing.JApplet {
  
    static final long serialVersionUID = -8860524626577921865L;
    
    
    
    public FrequencyHistogram freqHistogram = new FrequencyHistogram();

	public void init() {
        initDemo();
    }
    
    public void start() {

    }
   
    public void initDemo()
    {
      	// This skips the frame window and places the MainPanel directly 
      	// in the content pane of the Applet
        Container contentPane = this.getContentPane();
        contentPane.add(freqHistogram, BorderLayout.CENTER);
    }
}