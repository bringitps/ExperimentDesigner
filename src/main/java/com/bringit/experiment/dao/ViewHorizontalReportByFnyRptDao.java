package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportByFnyRpt;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportByFnyRptDao {

public void addVwHorizontalReportByFnyRpt(ViewHorizontalReportByFnyRpt vwHorizontalReportByFnyRpt) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportByFnyRpt);
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

    public void deleteVwHorizontalReportByFnyRpt(int vwHorizontalReportByFnyRptId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportByFnyRpt vwHorizontalReportByFnyRpt = (ViewHorizontalReportByFnyRpt)session.load(ViewHorizontalReportByFnyRpt.class, new Integer(vwHorizontalReportByFnyRptId));
            session.delete(vwHorizontalReportByFnyRpt);
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

    public void updateVwHorizontalReportByFnyRpt(ViewHorizontalReportByFnyRpt vwHorizontalReportByFnyRpt) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportByFnyRpt);
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
    public List<ViewHorizontalReportByFnyRpt> getAllVwHorizontalReportByFnyRptById(Integer vwHorizontalReportId) {
        List<ViewHorizontalReportByFnyRpt> vwHorizontalRptByFnyRpts = new ArrayList<ViewHorizontalReportByFnyRpt>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptByFnyRpts = session.createQuery("from ViewHorizontalReportByFnyRpt where VwHorizontalReportId = " + vwHorizontalReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptByFnyRpts;
    } 
}
