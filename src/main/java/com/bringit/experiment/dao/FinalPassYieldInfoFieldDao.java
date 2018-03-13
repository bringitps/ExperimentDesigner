package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.FinalPassYieldInfoField;
import com.bringit.experiment.dal.HibernateUtil;

public class FinalPassYieldInfoFieldDao {

	public void addFinalPassYieldInfoField(FinalPassYieldInfoField fnyInfoField) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(fnyInfoField);
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

    public void deleteFinalPassYieldInfoField(int fnyInfoFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            FinalPassYieldInfoField fnyInfoField = (FinalPassYieldInfoField) session.load(FinalPassYieldInfoField.class, new Integer(fnyInfoFieldId));
            session.delete(fnyInfoField);
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

    public void updateFinalPassYieldInfoField(FinalPassYieldInfoField fnyInfoField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(fnyInfoField);
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
    public List<FinalPassYieldInfoField> getFinalPassYieldInfoFieldByReportById(Integer fnyReportId) {
        List<FinalPassYieldInfoField> fnyInfoFields = new ArrayList<FinalPassYieldInfoField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            fnyInfoFields = session.createQuery("from FinalPassYieldInfoField where FnyReportId = " + fnyReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return fnyInfoFields;
    }

    @SuppressWarnings("unused")
    public FinalPassYieldInfoField getFinalPassYieldInfoFieldById(int fnyInfoFieldId) {
    	FinalPassYieldInfoField fnyInfoField = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            String queryString = "from FinalPassYieldInfoField where FnyInfoFieldId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", fnyInfoFieldId);
            fnyInfoField = (FinalPassYieldInfoField) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return fnyInfoField;
    }

	
	
}
