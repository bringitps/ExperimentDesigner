package com.bringit.experiment.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReport;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportDao {

	public void addVwVerticalReport(ViewVerticalReport vwVerticalReport) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReport);
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

    public void deleteVwVerticalReport(int vwVerticalReportId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReport vwVerticalReport = (ViewVerticalReport)session.load(ViewVerticalReport.class, new Integer(vwVerticalReportId));
            session.delete(vwVerticalReport);
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

    public void updateVwVerticalReport(ViewVerticalReport vwVerticalReport) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReport);
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
	public ViewVerticalReport getVwVerticalRptById(int vwVerticalRptId) {
    	ViewVerticalReport vwVerticalRpt = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from ViewVerticalReport where VwVerticalRptId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", vwVerticalRptId);
            vwVerticalRpt = (ViewVerticalReport) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRpt;
    }
    
}
