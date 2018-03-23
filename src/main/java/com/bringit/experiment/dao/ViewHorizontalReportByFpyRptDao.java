package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportByFpyRpt;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportByFpyRptDao {
public void addVwHorizontalReportByFpyRpt(ViewHorizontalReportByFpyRpt vwHorizontalReportByFpyRpt) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportByFpyRpt);
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

    public void deleteVwHorizontalReportByFpyRpt(int vwHorizontalReportByFpyRptId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportByFpyRpt vwHorizontalReportByFpyRpt = (ViewHorizontalReportByFpyRpt)session.load(ViewHorizontalReportByFpyRpt.class, new Integer(vwHorizontalReportByFpyRptId));
            session.delete(vwHorizontalReportByFpyRpt);
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

    public void updateVwHorizontalReportByFpyRpt(ViewHorizontalReportByFpyRpt vwHorizontalReportByFpyRpt) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportByFpyRpt);
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
    public List<ViewHorizontalReportByFpyRpt> getAllVwHorizontalReportByFpyRptById(Integer vwHorizontalReportId) {
        List<ViewHorizontalReportByFpyRpt> vwHorizontalRptByFpyRpts = new ArrayList<ViewHorizontalReportByFpyRpt>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptByFpyRpts = session.createQuery("from ViewHorizontalReportByFpyRpt where VwHorizontalReportId = " + vwHorizontalReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptByFpyRpts;
    } 
}
