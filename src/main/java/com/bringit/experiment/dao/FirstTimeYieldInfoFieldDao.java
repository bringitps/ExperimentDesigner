package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.FirstTimeYieldInfoField;
import com.bringit.experiment.dal.HibernateUtil;

public class FirstTimeYieldInfoFieldDao {
	
	public void addFirstTimeYieldInfoField(FirstTimeYieldInfoField ftyInfoField) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(ftyInfoField);
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

    public void deleteFirstTimeYieldInfoField(int ftyInfoFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            FirstTimeYieldInfoField ftyInfoField = (FirstTimeYieldInfoField) session.load(FirstTimeYieldInfoField.class, new Integer(ftyInfoFieldId));
            session.delete(ftyInfoField);
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

    public void updateFirstTimeYieldInfoField(FirstTimeYieldInfoField ftyInfoField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(ftyInfoField);
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
    public List<FirstTimeYieldInfoField> getFirstTimeYieldInfoFieldByReportById(Integer ftyReportId) {
        List<FirstTimeYieldInfoField> ftyInfoFields = new ArrayList<FirstTimeYieldInfoField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ftyInfoFields = session.createQuery("from FirstTimeYieldInfoField where FtyReportId = " + ftyReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return ftyInfoFields;
    }

    @SuppressWarnings("unused")
    public FirstTimeYieldInfoField getFirstTimeYieldInfoFieldById(int ftyInfoFieldId) {
    	FirstTimeYieldInfoField ftyInfoField = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            String queryString = "from FirstPassYieldInfoField where FpyInfoFieldId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", ftyInfoFieldId);
            ftyInfoField = (FirstTimeYieldInfoField) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return ftyInfoField;
    }
    
}
