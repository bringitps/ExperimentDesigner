package com.bringit.experiment.dal;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class HibernateUtil 
{   
	/*
	 //@SuppressWarnings("deprecation")
	 private static SessionFactory buildSessionFactory(String configFile){
        try {
        	Configuration  configuration = new Configuration().configure(configFile);
       	 	ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
       	 	return new Configuration().configure(configFile).buildSessionFactory(serviceRegistry);
            //return new Configuration().configure(configFile).buildSessionFactory();
        }
        catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
	 }

    public static Session openSession(String configFile){
       SessionFactory sf = buildSessionFactory(configFile);
       return sf.openSession();
    }
    */
	private static final SessionFactory sessionFactory;

	static {
		try {
			Configuration configuration = new Configuration().configure(new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile());
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			sessionFactory = new Configuration().configure(new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile()).buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Session Factory could not be created." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	/*
	private static final SessionFactory sessionFactory;
	 
	 static {
	 try {
		 
	 Configuration  configuration = new Configuration().configure(new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile());
	 ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
	 sessionFactory = new Configuration().configure(new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile()).buildSessionFactory(serviceRegistry);
	 } catch (Throwable ex) {
	 System.err.println("Session Factory could not be created." + ex);
	 throw new ExceptionInInitializerError(ex);
	 }
	 }
	 
	 public static SessionFactory getSessionFactory() {
	 return sessionFactory;
	 }*/
}
