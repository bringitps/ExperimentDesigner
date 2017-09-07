package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.CsvTemplateColumns;
import com.bringit.experiment.bll.CsvTemplateEnrichment;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class CsvTemplateEnrichmentDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addCsvTemplateEnrichment(CsvTemplateEnrichment csvTemplateEnrichment) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(csvTemplateEnrichment);
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

    public void deleteCsvTemplateEnrichment(int csvTemplateEnrichmentId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            CsvTemplateEnrichment csvTemplateEnrichment = (CsvTemplateEnrichment)session.load(CsvTemplateEnrichment.class, new Integer(csvTemplateEnrichmentId));
            session.delete(csvTemplateEnrichment);
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

    public void updateCsvTemplateEnrichment(CsvTemplateEnrichment csvTemplateEnrichment) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(csvTemplateEnrichment);
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

    @SuppressWarnings("unused")
	public CsvTemplateEnrichment getCsvTemplateEnrichmentById(int csvTemplateEnrichmentId) {
    	CsvTemplateEnrichment csvTemplateEnrichment = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from CsvTemplateEnrichment where CsvTemplateEnrichmentId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", csvTemplateEnrichmentId);
            csvTemplateEnrichment = (CsvTemplateEnrichment) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return csvTemplateEnrichment;
    }

	public List<CsvTemplateEnrichment> getAllCsvTemplateEnrichmentByTemplateId(int csvTemplateId) {
		List<CsvTemplateEnrichment> csvTemplateEnrichmentRules = new ArrayList<CsvTemplateEnrichment>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            csvTemplateEnrichmentRules = session.createQuery("from CsvTemplateEnrichment where CsvTemplateId ="+csvTemplateId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return csvTemplateEnrichmentRules;
	}
	
	
}
