package com.bringit.experiment.util;

public class HibernateXmlConfigSupport {

	public String getHibernateDialectXmlConfigFile()
	{
		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
			return "mssql-hibernate.cfg.xml";
		else
			return null;
	}
}
