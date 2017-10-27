package com.bringit.experiment.dal;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class HibernateUtil 
{  
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
}
