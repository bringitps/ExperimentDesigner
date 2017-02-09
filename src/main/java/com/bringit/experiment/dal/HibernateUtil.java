package com.bringit.experiment.dal;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil 
{
	 @SuppressWarnings("deprecation")
	 private static SessionFactory buildSessionFactory(String configFile){
        try {
            return new Configuration().configure(configFile).buildSessionFactory();
        }
        catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
	 }

    public static Session openSession(String configFile){
       SessionFactory sf = buildSessionFactory(configFile);
       return sf.openSession();
    }
}
