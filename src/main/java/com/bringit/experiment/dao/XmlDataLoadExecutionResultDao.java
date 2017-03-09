package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.XmlDataLoadExecutionResult;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class XmlDataLoadExecutionResultDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addXmlDataLoadExecutionResult(XmlDataLoadExecutionResult xmlDataLoadExecutionResult) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(xmlDataLoadExecutionResult);
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

    public void deleteXmlDataLoadExecutionResult(int xmlDataLoadExecutionResultId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            XmlDataLoadExecutionResult xmlDataLoadExecutionResult = (XmlDataLoadExecutionResult)session.load(XmlDataLoadExecutionResult.class, new Integer(xmlDataLoadExecutionResultId));
            session.delete(xmlDataLoadExecutionResult);
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

    public void updateXmlDataLoadExecutionResult(XmlDataLoadExecutionResult xmlDataLoadExecutionResult) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(xmlDataLoadExecutionResult);
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
	public List<XmlDataLoadExecutionResult> getAllXmlDataLoadExecutionResults() {
        List<XmlDataLoadExecutionResult> xmlDataLoadExecutionResults = new ArrayList<XmlDataLoadExecutionResult>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            xmlDataLoadExecutionResults = session.createQuery("from XmlDataLoadExecutionResult").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return xmlDataLoadExecutionResults;
    }

    @SuppressWarnings("unused")
	public XmlDataLoadExecutionResult getXmlDataLoadExecutionResultById(int expId) {
    	XmlDataLoadExecutionResult xmlDataLoadExecutionResult = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from XmlDataLoadExecutionResult where XmlDataLoadExecId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", expId);
            xmlDataLoadExecutionResult = (XmlDataLoadExecutionResult) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return xmlDataLoadExecutionResult;
    }
	
}
