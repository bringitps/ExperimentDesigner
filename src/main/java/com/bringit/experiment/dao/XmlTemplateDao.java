package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.XmlTemplate;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class XmlTemplateDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addXmlTemplate(XmlTemplate xmlTemplate) {

        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(xmlTemplate);
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

    public void deleteXmlTemplate(int xmlTemplateId) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            XmlTemplate xmlTemplate = (XmlTemplate)session.load(XmlTemplate.class, new Integer(xmlTemplateId));
            session.delete(xmlTemplate);
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

    public void updateXmlTemplate(XmlTemplate xmlTemplate) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(xmlTemplate);
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
	public List<XmlTemplate> getAllXmlTemplates() {
        List<XmlTemplate> xmlTemplates = new ArrayList<XmlTemplate>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            xmlTemplates = session.createQuery("from XmlTemplate").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return xmlTemplates;
    }

    @SuppressWarnings({ "unchecked", "unused" })
	public List<XmlTemplate> getAllActiveXmlTemplates() {
        List<XmlTemplate> xmlTemplates = new ArrayList<XmlTemplate>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            xmlTemplates = session.createQuery("from XmlTemplate where XmlTemplateIsActive = 1").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return xmlTemplates;
    }

    @SuppressWarnings("unused")
	public XmlTemplate getXmlTemplateById(int xmlTemplateId) {
    	XmlTemplate xmlTemplate = null;
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from XmlTemplate where XmlTemplateId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", xmlTemplateId);
            xmlTemplate = (XmlTemplate) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return xmlTemplate;
    }
    
	public XmlTemplate getXmlTemplateByExperimentId(int expId) {
    	XmlTemplate xmlTemplate = null;
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from XmlTemplate where ExperimentId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", expId);
            xmlTemplate = (XmlTemplate) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return xmlTemplate;
    }
	
	 @SuppressWarnings({ "unchecked", "unused" })
		public List<XmlTemplate> getXmlTemplatesByFileRepoId(int fileRepoId)  {
	        List<XmlTemplate> xmlTemplates = new ArrayList<XmlTemplate>();
	        Transaction trns = null;
	        Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            xmlTemplates = session.createQuery("from XmlTemplate where InboundFileRepoId = :id or ProcessedFileRepoId = :id or ExceptionFileRepoId = :id").setInteger("id", fileRepoId).list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return xmlTemplates;
	    }

	 @SuppressWarnings({ "unchecked", "unused" })
		public List<XmlTemplate> getXmlTemplatesByJobExecRepeatId(int jobExecRepeatId)  {
	        List<XmlTemplate> xmlTemplates = new ArrayList<XmlTemplate>();
	        Transaction trns = null;
	        Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            xmlTemplates = session.createQuery("from XmlTemplate where JobExecRepeatId = :id").setInteger("id", jobExecRepeatId).list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return xmlTemplates;
	    }
	
}
