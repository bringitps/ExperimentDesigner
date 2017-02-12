package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ExperimentMeasureFieldValueUpdateLog;
import com.bringit.experiment.dal.HibernateUtil;

public class ExperimentMeasureFieldValueUpdateLogDao {

	private String dialectXmlFile = "mssql-hibernate.cfg.xml";
	
	public void addExperimentMeasureFieldValueUpdateLog(ExperimentMeasureFieldValueUpdateLog experimentMeasureFieldValueUpdateLog) {

        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(experimentMeasureFieldValueUpdateLog);
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

    public void deleteExperimentMeasureFieldValueUpdateLog(int experimentMeasureFieldValueUpdateLogId) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ExperimentMeasureFieldValueUpdateLog experimentMeasureFieldValueUpdateLog = (ExperimentMeasureFieldValueUpdateLog)session.load(ExperimentMeasureFieldValueUpdateLog.class, new Integer(experimentMeasureFieldValueUpdateLogId));
            session.delete(experimentMeasureFieldValueUpdateLog);
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

    public void updateExperimentMeasureFieldValueUpdateLog(ExperimentMeasureFieldValueUpdateLog experimentMeasureFieldValueUpdateLog) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(experimentMeasureFieldValueUpdateLog);
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
	public List<ExperimentMeasureFieldValueUpdateLog> getAllExperimentMeasureFieldValueUpdateLogs() {
        List<ExperimentMeasureFieldValueUpdateLog> experimentMeasures = new ArrayList<ExperimentMeasureFieldValueUpdateLog>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experimentMeasures = session.createQuery("from ExperimentMeasureFieldValueUpdateLog").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentMeasures;
    }

    @SuppressWarnings("unused")
	public ExperimentMeasureFieldValueUpdateLog getExperimentMeasureById(int expMeasureFieldValueUpdateLogId) {
    	ExperimentMeasureFieldValueUpdateLog experimentMeasureFieldValueUpdateLog = null;
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from ExperimentMeasureFieldValueUpdateLog where ExpMeasureFieldValueUpdateLogId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", expMeasureFieldValueUpdateLogId);
            experimentMeasureFieldValueUpdateLog = (ExperimentMeasureFieldValueUpdateLog) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentMeasureFieldValueUpdateLog;
    }
	
}
