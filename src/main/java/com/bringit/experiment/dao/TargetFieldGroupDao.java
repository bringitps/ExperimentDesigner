package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.TargetFieldGroup;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class TargetFieldGroupDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addTargetFieldGroup(TargetFieldGroup targetFieldGroup) {

        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(targetFieldGroup);
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

    public void deleteTargetFieldGroup(int targetFieldGroupId) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            TargetFieldGroup targetFieldGroup = (TargetFieldGroup)session.load(TargetFieldGroup.class, new Integer(targetFieldGroupId));
            session.delete(targetFieldGroup);
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

    public void updateTargetFieldGroup(TargetFieldGroup targetFieldGroup) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(targetFieldGroup);
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
	public List<TargetFieldGroup> getAllTargetFieldGroups() {
        List<TargetFieldGroup> targetFieldGroups = new ArrayList<TargetFieldGroup>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            targetFieldGroups = session.createQuery("from TargetFieldGroup").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetFieldGroups;
    }

    @SuppressWarnings("unused")
	public TargetFieldGroup getTargetFieldGroupById(int targetFieldGroupId) {
    	TargetFieldGroup targetFieldGroup = null;
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from TargetFieldGroup where TargetFieldGroupId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", targetFieldGroupId);
            targetFieldGroup = (TargetFieldGroup) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return targetFieldGroup;
    }


}
