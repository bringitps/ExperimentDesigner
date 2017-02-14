package com.bringit.experiment.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	public String getProperty(String propertyName) 
	{
		  
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
				input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
				prop.load(input);
				return prop.getProperty(propertyName);
				
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		  return null;
	  }
}
