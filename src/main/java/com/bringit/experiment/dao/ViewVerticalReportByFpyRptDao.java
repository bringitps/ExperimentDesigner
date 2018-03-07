package com.bringit.experiment.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportByFpyRpt;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportByFpyRptDao {
	
	public void addVwVerticalReportByFpyRpt(ViewVerticalReportByFpyRpt vwVerticalReportByFpyRpt) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportByFpyRpt);
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

    public void deleteVwVerticalReportByFpyRpt(int vwVerticalReportByFpyRptId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportByFpyRpt vwVerticalReportByFpyRpt = (ViewVerticalReportByFpyRpt)session.load(ViewVerticalReportByFpyRpt.class, new Integer(vwVerticalReportByFpyRptId));
            session.delete(vwVerticalReportByFpyRpt);
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

    public void updateVwVerticalReportByFpyRpt(ViewVerticalReportByFpyRpt vwVerticalReportByFpyRpt) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportByFpyRpt);
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

}
