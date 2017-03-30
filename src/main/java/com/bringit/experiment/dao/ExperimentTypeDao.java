package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ExperimentType;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class ExperimentTypeDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addExperimentType(ExperimentType experimentType) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(experimentType);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }

    public void deleteExperimentType(int experimentTypeId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ExperimentType experimentType = (ExperimentType)session.load(ExperimentType.class, new Integer(experimentTypeId));
            session.delete(experimentType);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }

    public void updateExperimentType(ExperimentType experimentType) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(experimentType);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
    }

    @SuppressWarnings({ "unchecked", "unused" })
	public List<ExperimentType> getAllExperimentTypes() {
        List<ExperimentType> experimentTypes = new ArrayList<ExperimentType>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experimentTypes = session.createQuery("from ExperimentType").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentTypes;
    }
    
    @SuppressWarnings("unused")
	public ExperimentType getExperimentTypeById(int expId) {
        ExperimentType experimentType = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from ExperimentType where ExpTypeId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", expId);
            experimentType = (ExperimentType) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentType;
    }    
}
