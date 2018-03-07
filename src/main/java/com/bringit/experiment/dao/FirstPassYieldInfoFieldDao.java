package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.FirstPassYieldInfoField;
import com.bringit.experiment.dal.HibernateUtil;

public class FirstPassYieldInfoFieldDao {

	public void addFirstPassYieldInfoField(FirstPassYieldInfoField fpyInfoField) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(fpyInfoField);
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

    public void deleteFirstPassYieldInfoField(int fpyInfoFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            FirstPassYieldInfoField fpyInfoField = (FirstPassYieldInfoField) session.load(FirstPassYieldInfoField.class, new Integer(fpyInfoFieldId));
            session.delete(fpyInfoField);
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

    public void updateFirstPassYieldInfoField(FirstPassYieldInfoField fpyInfoField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(fpyInfoField);
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
    
    @SuppressWarnings({"unchecked", "unused"})
    public List<FirstPassYieldInfoField> getFirstPassYieldInfoFieldByReportById(Integer fpyReportId) {
        List<FirstPassYieldInfoField> fpyInfoFields = new ArrayList<FirstPassYieldInfoField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            fpyInfoFields = session.createQuery("from FirstPassYieldInfoField where FpyReportId = " + fpyReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return fpyInfoFields;
    }

    @SuppressWarnings("unused")
    public FirstPassYieldInfoField getFirstPassYieldInfoFieldById(int fpyInfoFieldId) {
    	FirstPassYieldInfoField fpyInfoField = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            String queryString = "from FirstPassYieldInfoField where FpyInfoFieldId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", fpyInfoFieldId);
            fpyInfoField = (FirstPassYieldInfoField) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return fpyInfoField;
    }
    
}
