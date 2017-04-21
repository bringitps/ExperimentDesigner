package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class ExperimentDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addExperiment(Experiment exp) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(exp);
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

    public void deleteExperiment(int expId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            Experiment exp = (Experiment)session.load(Experiment.class, new Integer(expId));
            session.delete(exp);
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

    public void updateExperiment(Experiment exp) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(exp);
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
	public List<Experiment> getAllExperiments() {
        List<Experiment> experiments = new ArrayList<Experiment>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experiments = session.createQuery("from Experiment").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experiments;
    }
    
    @SuppressWarnings({ "unchecked", "unused" })
	public List<Experiment> getActiveExperiments() {
        List<Experiment> experiments = new ArrayList<Experiment>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experiments = session.createQuery("from Experiment where ExpIsActive = 'true'").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experiments;
    }

    @SuppressWarnings("unused")
	public Experiment getExperimentById(int expId) {
        Experiment exp = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from Experiment where ExpId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", expId);
            exp = (Experiment) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return exp;
    }
    
    @SuppressWarnings("unused")
	public Experiment getExperimentByName(String expName) {
        Experiment exp = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String query = "from Experiment where ExpName ='"+expName.trim()+ "' and ExpIsActive = 'true'";
            exp = (Experiment) (session.createQuery(query).list()).get(0);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return exp;
    }
    
    public boolean updateDBDataTable(Experiment experiment)
    {
    	String query = null;
    	boolean successfulExecution = true;
    	
		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			query = " IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='" + experiment.getExpDbTableNameId() + "' AND xtype='U') ";
			query += " CREATE TABLE " + experiment.getExpDbTableNameId();
			query += " (RecordId int IDENTITY(1,1) NOT NULL PRIMARY KEY,";
			query += " Comments text ,";
			query += " CmId int ,";
			query += " CreatedBy int ,";
			query += " LastModifiedBy int ,";
			query += " DataFileId int ,";
			query += " CreatedDate datetime ,";
			query += " LastModifiedDate datetime ,";
			query += " FOREIGN KEY (CmId) REFERENCES ContractManufacturer(CmId) ON DELETE CASCADE, ";
			query += " FOREIGN KEY (CreatedBy) REFERENCES SysUser(UserId), ";
			query += " FOREIGN KEY (LastModifiedBy) REFERENCES SysUser(UserId), ";
			query += " FOREIGN KEY (DataFileId) REFERENCES DataFile(DataFileId));";
		}
    	else
    		return false;

		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            successfulExecution = false;
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
		return successfulExecution;
    }
    
    public boolean deleteDBRptTable(Experiment experiment)
    {
    	String query = null;
    	boolean successfulExecution = true;
    	
		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			query = " IF EXISTS (SELECT * FROM sysobjects WHERE name='" + experiment.getExpDbRptTableNameId() + "' AND xtype='U') ";
			query += " DROP TABLE " + experiment.getExpDbRptTableNameId() + ";";
		}
    	else
    		return false;

		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		
        try {
            trns = session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            successfulExecution = false;
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
		return successfulExecution;    
    }
    
    public boolean updateDBRptTable(Experiment experiment)
    {
    	String query = null;
    	boolean successfulExecution = true;
    	
		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			query = " IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='" + experiment.getExpDbRptTableNameId() + "' AND xtype='U') ";
			query += " CREATE TABLE " + experiment.getExpDbRptTableNameId();
			query += " (RecordId int NOT NULL PRIMARY KEY,";
			query += " Comments text,";
			query += " CmName varchar(255),";
			query += " CreatedBy varchar(255) ,";
			query += " LastModifiedBy varchar(255),";
			query += " DataFileName varchar(255),";
			query += " CreatedDate datetime ,";
			query += " LastModifiedDate datetime);";
		}
    	else
    		return false;

		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		
        try {
            trns = session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            successfulExecution = false;
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
		return successfulExecution;
    }
    
}
