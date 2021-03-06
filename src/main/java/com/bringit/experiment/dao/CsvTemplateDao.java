package com.bringit.experiment.dao;

import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.CsvTemplate;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CsvTemplateDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addCsvTemplate(CsvTemplate csvTemplate) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(csvTemplate);
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

    public void deleteCsvTemplate(int csvTemplateId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            CsvTemplate csvTemplate = (CsvTemplate)session.load(CsvTemplate.class, new Integer(csvTemplateId));
            session.delete(csvTemplate);
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

    public void updateCsvTemplate(CsvTemplate csvTemplate) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(csvTemplate);
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
	public List<CsvTemplate> getAllCsvTemplates() {
        List<CsvTemplate> csvTemplates = new ArrayList<CsvTemplate>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            csvTemplates = session.createQuery("from CsvTemplate").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return csvTemplates;
    }

    @SuppressWarnings("unused")
	public CsvTemplate getCsvTemplateById(int csvId) {
    	CsvTemplate csvTemplate = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from CsvTemplate where CsvTemplateId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id",csvId);
            csvTemplate = (CsvTemplate) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return csvTemplate;
    }
    
	public CsvTemplate getCsvTemplateByExperimentId(int expId) {
    	CsvTemplate csvTemplate = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from CsvTemplate where ExperimentId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", expId);
            csvTemplate = (CsvTemplate) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return csvTemplate;
    }
	
	 @SuppressWarnings({ "unchecked", "unused" })
		public List<CsvTemplate> getCsvTemplatesByFileRepoId(int fileRepoId)  {
	        List<CsvTemplate> csvTemplates = new ArrayList<CsvTemplate>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
			//Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            csvTemplates = session.createQuery("from CsvTemplate where InboundFileRepoId = :id or ProcessedFileRepoId = :id or ExceptionFileRepoId = :id").setInteger("id", fileRepoId).list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return csvTemplates;
	    }

	 @SuppressWarnings({ "unchecked", "unused" })
		public List<CsvTemplate> getCsvTemplatesByJobExecRepeatId(int jobExecRepeatId)  {
	        List<CsvTemplate> csvTemplates = new ArrayList<CsvTemplate>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
			//Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            csvTemplates = session.createQuery("from CsvTemplate where JobExecRepeatId = :id").setInteger("id", jobExecRepeatId).list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return csvTemplates;
	    }

		public List<CsvTemplate> getAllActiveCsvTemplates() {
	        List<CsvTemplate> csvTemplates = new ArrayList<CsvTemplate>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
			// Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            csvTemplates = session.createQuery("from CsvTemplate where CsvTemplateIsActive='true'").list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return csvTemplates;
		}

		public List<CsvTemplate> getAllScheduledCsvTemplates() {
	        List<CsvTemplate> csvTemplates = new ArrayList<CsvTemplate>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
			// Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            csvTemplates = session.createQuery("from CsvTemplate where CsvTemplateIsActive='true' and CsvTemplateNotScheduled != 'true'").list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return csvTemplates;
		}

	
	 @SuppressWarnings({ "unchecked", "unused" })
		public List<CsvTemplate> getCsvTemplatesByCmId(int ContractManufacturerId)  {
	        List<CsvTemplate> csvTemplates = new ArrayList<CsvTemplate>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
			//Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            csvTemplates = session.createQuery("from CsvTemplate where CmId = :id").setInteger("id", ContractManufacturerId).list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return csvTemplates;
	    }

	 @SuppressWarnings({ "unchecked", "unused" })
		public List<CsvTemplate> getAllCsvTemplatesByContractManager(List<ContractManufacturer> contractManufacturerList)  {
	        List<CsvTemplate> csvTemplates = new ArrayList<CsvTemplate>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
			//Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            csvTemplates = session.createQuery("from CsvTemplate where CsvTemplateIsActive='true' and contractManufacturer in :id").setParameterList("id", contractManufacturerList).list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return csvTemplates;
	    }
	
		public CsvTemplate getActiveCsvTemplateByName(String csvTemplateName) {
	    	CsvTemplate csvTemplate = null;
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
			// Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            String queryString = "from CsvTemplate where CsvTemplateIsActive = 'true' and CsvTemplateName = :name";
	            Query query = session.createQuery(queryString);
	            query.setString("name", csvTemplateName);
	            csvTemplate = (CsvTemplate) query.uniqueResult();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return csvTemplate;
	    }
}
