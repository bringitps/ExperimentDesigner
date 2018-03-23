package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportByFtyRpt;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportByFtyRptDao {
	public void addVwHorizontalReportByFtyRpt(ViewHorizontalReportByFtyRpt vwHorizontalReportByFtyRpt) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportByFtyRpt);
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

    public void deleteVwHorizontalReportByFtyRpt(int vwHorizontalReportByFtyRptId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportByFtyRpt vwHorizontalReportByFtyRpt = (ViewHorizontalReportByFtyRpt)session.load(ViewHorizontalReportByFtyRpt.class, new Integer(vwHorizontalReportByFtyRptId));
            session.delete(vwHorizontalReportByFtyRpt);
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

    public void updateVwHorizontalReportByFtyRpt(ViewHorizontalReportByFtyRpt vwHorizontalReportByFtyRpt) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportByFtyRpt);
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
    public List<ViewHorizontalReportByFtyRpt> getAllVwHorizontalReportByFtyRptById(Integer vwHorizontalReportId) {
        List<ViewHorizontalReportByFtyRpt> vwHorizontalRptByFtyRpts = new ArrayList<ViewHorizontalReportByFtyRpt>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptByFtyRpts = session.createQuery("from ViewHorizontalReportByFtyRpt where VwHorizontalReportId = " + vwHorizontalReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptByFtyRpts;
    }  
}
