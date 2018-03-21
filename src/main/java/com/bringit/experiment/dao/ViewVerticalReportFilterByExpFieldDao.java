package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportFilterByExpField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportFilterByExpFieldDao {

public void addVwVerticalReportFilterByExpField(ViewVerticalReportFilterByExpField vwVerticalReportFilterByExpField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportFilterByExpField);
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

    public void deleteVwVerticalReportFilterByExpField(int vwVerticalReportFilterByExpFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportFilterByExpField vwVerticalReportFilterByExpField = (ViewVerticalReportFilterByExpField)session.load(ViewVerticalReportFilterByExpField.class, new Integer(vwVerticalReportFilterByExpFieldId));
            session.delete(vwVerticalReportFilterByExpField);
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

    public void updateVwVerticalReportFilterByExpField(ViewVerticalReportFilterByExpField vwVerticalReportFilterByExpField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportFilterByExpField);
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
    public List<ViewVerticalReportFilterByExpField> getAllVwVerticalReportFiltersByExpRptId(Integer vwVerticalRptByExpId) {
        List<ViewVerticalReportFilterByExpField> vwVerticalRptFiltersByExp = new ArrayList<ViewVerticalReportFilterByExpField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptFiltersByExp = session.createQuery("from ViewVerticalReportFilterByExpField where VwVerticalReportByExperimentId = " + vwVerticalRptByExpId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptFiltersByExp;
    }
}
