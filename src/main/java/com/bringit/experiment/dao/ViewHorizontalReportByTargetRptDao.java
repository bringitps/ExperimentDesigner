package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportByTargetRpt;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportByTargetRptDao {
public void addVwHorizontalReportByTargetRpt(ViewHorizontalReportByTargetRpt vwHorizontalReportByTargetRpt) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportByTargetRpt);
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

    public void deleteVwHorizontalReportByTargetRpt(int vwHorizontalReportByTargetRptId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportByTargetRpt vwHorizontalReportByTargetRpt = (ViewHorizontalReportByTargetRpt)session.load(ViewHorizontalReportByTargetRpt.class, new Integer(vwHorizontalReportByTargetRptId));
            session.delete(vwHorizontalReportByTargetRpt);
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

    public void updateVwHorizontalReportByTargetRpt(ViewHorizontalReportByTargetRpt vwHorizontalReportByTargetRpt) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportByTargetRpt);
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
    public List<ViewHorizontalReportByTargetRpt> getAllVwHorizontalReportByTargetRptById(Integer vwHorizontalReportId) {
        List<ViewHorizontalReportByTargetRpt> vwHorizontalRptByTargetRpts = new ArrayList<ViewHorizontalReportByTargetRpt>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptByTargetRpts = session.createQuery("from ViewHorizontalReportByTargetRpt where VwHorizontalReportId = " + vwHorizontalReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptByTargetRpts;
    }  
}
