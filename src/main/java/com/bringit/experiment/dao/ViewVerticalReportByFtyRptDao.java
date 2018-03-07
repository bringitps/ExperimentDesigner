package com.bringit.experiment.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportByFtyRpt;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportByFtyRptDao {

	public void addVwVerticalReportByFtyRpt(ViewVerticalReportByFtyRpt vwVerticalReportByFtyRpt) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportByFtyRpt);
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

    public void deleteVwVerticalReportByFtyRpt(int vwVerticalReportByFtyRptId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportByFtyRpt vwVerticalReportByFtyRpt = (ViewVerticalReportByFtyRpt)session.load(ViewVerticalReportByFtyRpt.class, new Integer(vwVerticalReportByFtyRptId));
            session.delete(vwVerticalReportByFtyRpt);
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

    public void updateVwVerticalReportByFtyRpt(ViewVerticalReportByFtyRpt vwVerticalReportByFtyRpt) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportByFtyRpt);
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
