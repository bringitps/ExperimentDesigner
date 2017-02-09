package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.dal.HibernateUtil;

public class ExperimentFieldDao {

	private String dialectXmlFile = "mssql-hibernate.cfg.xml";
	
	public void addExperimentField(ExperimentField experimentField) {

        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(experimentField);
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

    public void deleteExperimentField(int experimentFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ExperimentField experimentField = (ExperimentField)session.load(ExperimentField.class, new Integer(experimentFieldId));
            session.delete(experimentField);
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

    public void updateExperimentField(ExperimentField experimentField) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(experimentField);
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
	public List<ExperimentField> getAllExperimentFields() {
        List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experimentFields = session.createQuery("from ExperimentField").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentFields;
    }

    @SuppressWarnings("unused")
	public ExperimentField getExperimentFieldById(int experimentFieldId) {
    	ExperimentField experimentField = null;
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from Experiment where ExpFieldId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", experimentFieldId);
            experimentField = (ExperimentField) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentField;
    }
	
}
