package com.bringit.experiment.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReport;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportDao {

	public void addVwHorizontalReport(ViewHorizontalReport vwHorizontalReport) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReport);
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

    public void deleteVwHorizontalReport(int vwHorizontalReportId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReport vwHorizontalReport = (ViewHorizontalReport)session.load(ViewHorizontalReport.class, new Integer(vwHorizontalReportId));
            session.delete(vwHorizontalReport);
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

    public void updateVwHorizontalReport(ViewHorizontalReport vwHorizontalReport) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReport);
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
    
    @SuppressWarnings("unused")
	public ViewHorizontalReport getVwHorizontalRptById(int vwHorizontalRptId) {
    	ViewHorizontalReport vwHorizontalRpt = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from ViewHorizontalReport where VwHorizontalRptId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", vwHorizontalRptId);
            vwHorizontalRpt = (ViewHorizontalReport) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRpt;
    }
    
}
