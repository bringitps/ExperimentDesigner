package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ExperimentMeasureFieldValue;
import com.bringit.experiment.dal.HibernateUtil;

public class ExperimentMeasureFieldValueDao {

	private String dialectXmlFile = "mssql-hibernate.cfg.xml";
	
	public void addExperimentMeasureFieldValue(ExperimentMeasureFieldValue experimentMeasureFieldValue) {

        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(experimentMeasureFieldValue);
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

    public void deleteExperimentMeasureFieldValue(int experimentMeasureFieldValueId) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ExperimentMeasureFieldValue experimentMeasureFieldValue = (ExperimentMeasureFieldValue)session.load(ExperimentMeasureFieldValue.class, new Integer(experimentMeasureFieldValueId));
            session.delete(experimentMeasureFieldValue);
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

    public void updateExperimentMeasureFieldValue(ExperimentMeasureFieldValue experimentMeasureFieldValue) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(experimentMeasureFieldValue);
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
	public List<ExperimentMeasureFieldValue> getAllExperimentMeasureFieldValues() {
        List<ExperimentMeasureFieldValue> experimentMeasureFieldValues = new ArrayList<ExperimentMeasureFieldValue>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experimentMeasureFieldValues = session.createQuery("from ExperimentMeasureFieldValue").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentMeasureFieldValues;
    }

    @SuppressWarnings("unused")
	public ExperimentMeasureFieldValue getExperimentMeasureFieldValueById(int experimentMeasureFieldValueId) {
    	ExperimentMeasureFieldValue experimentMeasureFieldValue = null;
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from ExperimentMeasureFieldValue where ExpMeasureFieldValueId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", experimentMeasureFieldValueId);
            experimentMeasureFieldValue = (ExperimentMeasureFieldValue) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentMeasureFieldValue;
    }
	
}
