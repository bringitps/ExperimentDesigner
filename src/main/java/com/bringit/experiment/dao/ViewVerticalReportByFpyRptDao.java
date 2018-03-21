package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportByExperiment;
import com.bringit.experiment.bll.ViewVerticalReportByFpyRpt;
import com.bringit.experiment.bll.ViewVerticalReportByFtyRpt;
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

    @SuppressWarnings({"unchecked", "unused"})
    public List<ViewVerticalReportByFpyRpt> getAllVwVerticalReportByFpyRptById(Integer vwVerticalReportId) {
        List<ViewVerticalReportByFpyRpt> vwVerticalRptByFpyRpts = new ArrayList<ViewVerticalReportByFpyRpt>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptByFpyRpts = session.createQuery("from ViewVerticalReportByFpyRpt where VwVerticalReportId = " + vwVerticalReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptByFpyRpts;
    } 
}
