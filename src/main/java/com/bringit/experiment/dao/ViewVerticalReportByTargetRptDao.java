package com.bringit.experiment.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportByTargetRpt;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportByTargetRptDao {

	public void addVwVerticalReportByTargetRpt(ViewVerticalReportByTargetRpt vwVerticalReportByTargetRpt) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportByTargetRpt);
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

    public void deleteVwVerticalReportByTargetRpt(int vwVerticalReportByTargetRptId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportByTargetRpt vwVerticalReportByTargetRpt = (ViewVerticalReportByTargetRpt)session.load(ViewVerticalReportByTargetRpt.class, new Integer(vwVerticalReportByTargetRptId));
            session.delete(vwVerticalReportByTargetRpt);
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

    public void updateVwVerticalReportByTargetRpt(ViewVerticalReportByTargetRpt vwVerticalReportByTargetRpt) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportByTargetRpt);
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
