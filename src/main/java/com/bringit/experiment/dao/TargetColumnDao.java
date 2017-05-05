package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.TargetColumn;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class TargetColumnDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addTargetColumn(TargetColumn targetColumn) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(targetColumn);
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

    public void deleteTargetColumn(int targetColumnId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            TargetColumn targetColumn = (TargetColumn)session.load(TargetColumn.class, new Integer(targetColumnId));
            session.delete(targetColumn);
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

    public void updateTargetColumn(TargetColumn targetColumn) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//  Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(targetColumn);
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
	public List<TargetColumn> getAllTargetColumns() {
        List<TargetColumn> targetColumns = new ArrayList<TargetColumn>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            targetColumns = session.createQuery("from TargetColumn").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetColumns;
    }

    @SuppressWarnings("unused")
	public TargetColumn getTargetColumnById(int targetColumnId) {
    	TargetColumn targetField = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from TargetColumn where TargetColumnId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", targetColumnId);
            targetField = (TargetColumn) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetField;
    }
    
    /*
    @SuppressWarnings("unused")
	public TargetColumn getTargetColumnByLabelAndTargetReportId(String targetColumnLabel, int targetRptId) {
    	TargetColumn targetField = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from TargetColumn INNER JOIN TargetColumnGroup ON TargetColumn.TargetColumnGroupId = TargetColumnGroup.TargetColumnGroupId where TargetColumn.TargetColumnLabel = :targetColLabel AND TargetColumnGroup.TargetReportId = :targetRptId";
            Query query = session.createQuery(queryString);
            query.setString("targetColLabel", targetColumnLabel);
            query.setInteger("targetRptId", targetRptId);
            targetField = (TargetColumn) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetField;
    }
    */
    
    @SuppressWarnings({ "unchecked", "unused" })
   	public List<TargetColumn> getTargetColumnsByColGroupById(int targetRptColumnGroupId) {
           List<TargetColumn> targetColumns = new ArrayList<TargetColumn>();
           Transaction trns = null;
           Session session = HibernateUtil.getSessionFactory().openSession();
   		//Session session = HibernateUtil.openSession(dialectXmlFile);
           try {
               trns = session.beginTransaction();
               targetColumns = session.createQuery("from TargetColumn where TargetColumnGroupId = " + targetRptColumnGroupId + " order by TargetColumnPosition").list();
           } catch (RuntimeException e) {
               e.printStackTrace();
           } finally {
               session.flush();
               session.close();
           }
           return targetColumns;
      }
}
