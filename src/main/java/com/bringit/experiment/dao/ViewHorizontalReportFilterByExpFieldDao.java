package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportFilterByExpField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportFilterByExpFieldDao {

	public void addVwHorizontalReportFilterByExpField(ViewHorizontalReportFilterByExpField vwHorizontalReportFilterByExpField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportFilterByExpField);
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

    public void deleteVwHorizontalReportFilterByExpField(int vwHorizontalReportFilterByExpFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportFilterByExpField vwHorizontalReportFilterByExpField = (ViewHorizontalReportFilterByExpField)session.load(ViewHorizontalReportFilterByExpField.class, new Integer(vwHorizontalReportFilterByExpFieldId));
            session.delete(vwHorizontalReportFilterByExpField);
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

    public void updateVwHorizontalReportFilterByExpField(ViewHorizontalReportFilterByExpField vwHorizontalReportFilterByExpField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportFilterByExpField);
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
    public List<ViewHorizontalReportFilterByExpField> getAllVwHorizontalReportFiltersByExpRptId(Integer vwHorizontalRptByExpId) {
        List<ViewHorizontalReportFilterByExpField> vwHorizontalRptFiltersByExp = new ArrayList<ViewHorizontalReportFilterByExpField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptFiltersByExp = session.createQuery("from ViewHorizontalReportFilterByExpField where VwHorizontalReportByExperimentId = " + vwHorizontalRptByExpId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptFiltersByExp;
    }

	
}
