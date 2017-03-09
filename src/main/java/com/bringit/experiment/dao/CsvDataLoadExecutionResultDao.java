package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.CsvDataLoadExecutionResult;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class CsvDataLoadExecutionResultDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addCsvDataLoadExecutionResult(CsvDataLoadExecutionResult csvDataLoadExecutionResult) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(csvDataLoadExecutionResult);
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

    public void deleteCsvDataLoadExecutionResult(int csvDataLoadExecutionResultId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            CsvDataLoadExecutionResult csvDataLoadExecutionResult = (CsvDataLoadExecutionResult)session.load(CsvDataLoadExecutionResult.class, new Integer(csvDataLoadExecutionResultId));
            session.delete(csvDataLoadExecutionResult);
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

    public void updateCsvDataLoadExecutionResult(CsvDataLoadExecutionResult csvDataLoadExecutionResult) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(csvDataLoadExecutionResult);
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
	public List<CsvDataLoadExecutionResult> getAllCsvDataLoadExecutionResults() {
        List<CsvDataLoadExecutionResult> csvDataLoadExecutionResults = new ArrayList<CsvDataLoadExecutionResult>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            csvDataLoadExecutionResults = session.createQuery("from CsvDataLoadExecutionResult").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return csvDataLoadExecutionResults;
    }

    @SuppressWarnings("unused")
	public CsvDataLoadExecutionResult getCsvDataLoadExecutionResultById(int loadId) {
    	CsvDataLoadExecutionResult csvDataLoadExecutionResult = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from CsvDataLoadExecutionResult where CsvDataLoadExecId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", loadId);
            csvDataLoadExecutionResult = (CsvDataLoadExecutionResult) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return csvDataLoadExecutionResult;
    }
	
}
