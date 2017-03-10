package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.TargetColumnGroup;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class TargetFieldGroupDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addTargetFieldColumn(TargetColumnGroup targetFieldColumn) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(targetFieldColumn);
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

    public void deleteTargetColumnGroup(int targetColumnGroupId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            TargetColumnGroup targetColumnGroup = (TargetColumnGroup)session.load(TargetColumnGroup.class, new Integer(targetColumnGroupId));
            session.delete(targetColumnGroup);
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

    public void updateTargetColumnGroup(TargetColumnGroup targetColumnGroup) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(targetColumnGroup);
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
	public List<TargetColumnGroup> getAllTargetFieldColumns() {
        List<TargetColumnGroup> targetColumnGroups = new ArrayList<TargetColumnGroup>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            targetColumnGroups = session.createQuery("from TargetColumnGroup").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetColumnGroups;
    }

    @SuppressWarnings("unused")
	public TargetColumnGroup getTargetColumnGroupById(int targetColumnGroupId) {
    	TargetColumnGroup targetColumnGroup = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from TargetColumnGroup where TargetColumnGroupId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", targetColumnGroupId);
            targetColumnGroup = (TargetColumnGroup) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetColumnGroup;
    }


}
