package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.CsvTemplateColumns;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class CsvTemplateColumnsDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addCsvTemplateColumns(CsvTemplateColumns csvTemplateColumns) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(csvTemplateColumns);
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

    public void deleteCsvTemplateColumns(int csvTemplateColumnsId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            CsvTemplateColumns csvTemplateColumns = (CsvTemplateColumns)session.load(CsvTemplateColumns.class, new Integer(csvTemplateColumnsId));
            session.delete(csvTemplateColumns);
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

    public void updateCsvTemplateColumns(CsvTemplateColumns csvTemplateColumns) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(csvTemplateColumns);
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
	public List<CsvTemplateColumns> getAllCsvTemplateColumnss() {
        List<CsvTemplateColumns> csvTemplateColumnss = new ArrayList<CsvTemplateColumns>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            csvTemplateColumnss = session.createQuery("from CsvTemplateColumns").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return csvTemplateColumnss;
    }

    @SuppressWarnings("unused")
	public CsvTemplateColumns getCsvTemplateColumnsById(int colId) {
    	CsvTemplateColumns csvTemplateColumns = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from CsvTemplateColumns where CsvTemplateColumnsId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", colId);
            csvTemplateColumns = (CsvTemplateColumns) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return csvTemplateColumns;
    }

	public List<CsvTemplateColumns> getAllCsvTemplateColumnsMappingDetailsByTemplateId(int csvTemplateId) {
		List<CsvTemplateColumns> nodes = new ArrayList<CsvTemplateColumns>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            //nodes = session.createQuery("from CsvTemplateColumns where CsvTemplateId ="+csvTemplateId + " and ExpFieldId is not null").list();
            nodes = session.createQuery("from CsvTemplateColumns where CsvTemplateId ="+csvTemplateId ).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return nodes;
	}
	
	public List<CsvTemplateColumns> getAllCsvTemplateColumnssByTemplateId(int csvTemplateId) {
		List<CsvTemplateColumns> nodes = new ArrayList<CsvTemplateColumns>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            nodes = session.createQuery("from CsvTemplateColumns where CsvTemplateId ="+csvTemplateId + " and ExpFieldId is not null").list();
            
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return nodes;
	}
	
}
