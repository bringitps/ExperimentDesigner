package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class ExperimentFieldDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
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
    /*
    @SuppressWarnings({ "unchecked", "unused" })
	public List<ExperimentField> getActiveExperimentFields() {
        List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experimentFields = session.createQuery("from ExperimentField where ExpFieldIsActive=true").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentFields;
    }
    */
    @SuppressWarnings({ "unchecked", "unused" })
	public List<ExperimentField> getActiveExperimentFields(Experiment firstExperiment) {
		List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experimentFields = session.createQuery("from ExperimentField where ExpId ="+firstExperiment.getExpId()).list();
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
            String queryString = "from ExperimentField where ExpFieldId = :id";
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

    @SuppressWarnings({ "unchecked", "unused" })
	public List<ExperimentField> getAllExperimentFieldsByExperiment(Experiment experiment) {
        List<ExperimentField> experimentFields = new ArrayList<ExperimentField>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            experimentFields = session.createQuery("from ExperimentField where ExpId = :id").setInteger("id", experiment.getExpId()).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentFields;
    }
    
    
    public boolean updateDBDataTableField(ExperimentField experimentField)
    {

    	String query = null;
    	boolean successfulExecution = true;

		Config configuration = new Config();
		if(configuration.getProperty("dbms").equals("sqlserver"))
		{
			if(experimentField.isExpFieldIsActive())
			{
				query = " IF NOT EXISTS (SELECT * ";
				query += " FROM INFORMATION_SCHEMA.COLUMNS WHERE ";
				query += " TABLE_NAME = '" + experimentField.getExperiment().getExpDbTableNameId() + "' ";
				query +=  " AND COLUMN_NAME = '" + experimentField.getExpDbFieldNameId() + "') ";
				query +=  " ALTER TABLE " + experimentField.getExperiment().getExpDbTableNameId() + " "; 
				query +=  " ADD " + experimentField.getExpDbFieldNameId() + " " + experimentField.getExpFieldType() + ";";
				
			}
			else
			{
				query = " IF EXISTS (SELECT * ";
				query += " FROM INFORMATION_SCHEMA.COLUMNS WHERE ";
				query += " TABLE_NAME = '" + experimentField.getExperiment().getExpDbTableNameId() + "' ";
				query +=  " AND COLUMN_NAME = '" + experimentField.getExpDbFieldNameId() + "') ";
				query +=  " ALTER TABLE '" + experimentField.getExperiment().getExpDbTableNameId() + "' "; 
				query +=  " DROP COLUMN '" + experimentField.getExpDbFieldNameId() + "';";
			}
		}
    	else
    		return false;
		
		Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
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
