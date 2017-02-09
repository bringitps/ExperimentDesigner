package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ExperimentMeasure;
import com.bringit.experiment.dal.HibernateUtil;

public class ExperimentMeasureDao {

	private String dialectXmlFile = "mssql-hibernate.cfg.xml";
	
	public void addExperimentMeasure(ExperimentMeasure experimentMeasure) {

        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(experimentMeasure);
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

    public void deleteExperimentMeasure(int experimentMeasureId) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ExperimentMeasure experimentMeasure = (ExperimentMeasure)session.load(ExperimentMeasure.class, new Integer(experimentMeasureId));
            session.delete(experimentMeasure);
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

    public void updateExperimentMeasure(ExperimentMeasure experimentMeasure) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(experimentMeasure);
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
	public List<ExperimentMeasure> getAllExperimentMeasures() {
        List<ExperimentMeasure> experimentMeasures = new ArrayList<ExperimentMeasure>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experimentMeasures = session.createQuery("from ExperimentMeasure").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentMeasures;
    }

    @SuppressWarnings("unused")
	public ExperimentMeasure getExperimentMeasureById(int experimentMeasureId) {
    	ExperimentMeasure experimentMeasure = null;
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from ExperimentMeasure where ExpMeasureId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", experimentMeasureId);
            experimentMeasure = (ExperimentMeasure) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentMeasure;
    }
	
}
