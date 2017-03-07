package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ExperimentFieldValueUpdateLog;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class ExperimentFieldValueUpdateLogDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
		
	public void addExperimentFieldValueUpdateLog(ExperimentFieldValueUpdateLog experimentFieldValueUpdateLog) {

        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(experimentFieldValueUpdateLog);
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

    public void deleteExperimentFieldValueUpdateLog(int experimentFieldValueUpdateLogId) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ExperimentFieldValueUpdateLog experimentFieldValueUpdateLog = (ExperimentFieldValueUpdateLog)session.load(ExperimentFieldValueUpdateLog.class, new Integer(experimentFieldValueUpdateLogId));
            session.delete(experimentFieldValueUpdateLog);
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

    public void updateExperimentFieldValueUpdateLog(ExperimentFieldValueUpdateLog experimentFieldValueUpdateLog) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(experimentFieldValueUpdateLog);
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
	public List<ExperimentFieldValueUpdateLog> getAllExperimentFieldValueUpdateLogs() {
        List<ExperimentFieldValueUpdateLog> experimentFieldValueUpdateLogs = new ArrayList<ExperimentFieldValueUpdateLog>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experimentFieldValueUpdateLogs = session.createQuery("from ExperimentFieldValueUpdateLog").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentFieldValueUpdateLogs;
    }

    @SuppressWarnings("unused")
	public ExperimentFieldValueUpdateLog getExperimentFieldValueUpdateLogById(int expFieldValueUpdateLogId) {
    	ExperimentFieldValueUpdateLog experimentFieldValueUpdateLog = null;
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from ExperimentMeasureFieldValueUpdateLog where ExpFieldValueUpdateLogId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", expFieldValueUpdateLogId);
            experimentFieldValueUpdateLog = (ExperimentFieldValueUpdateLog) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentFieldValueUpdateLog;
    }
	
}
