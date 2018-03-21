package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportByExperiment;
import com.bringit.experiment.bll.ViewVerticalReportByFnyRpt;
import com.bringit.experiment.bll.ViewVerticalReportByFpyRpt;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportByFnyRptDao {


	public void addVwVerticalReportByFnyRpt(ViewVerticalReportByFnyRpt vwVerticalReportByFnyRpt) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportByFnyRpt);
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

    public void deleteVwVerticalReportByFnyRpt(int vwVerticalReportByFnyRptId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportByFnyRpt vwVerticalReportByFnyRpt = (ViewVerticalReportByFnyRpt)session.load(ViewVerticalReportByFnyRpt.class, new Integer(vwVerticalReportByFnyRptId));
            session.delete(vwVerticalReportByFnyRpt);
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

    public void updateVwVerticalReportByFnyRpt(ViewVerticalReportByFnyRpt vwVerticalReportByFnyRpt) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportByFnyRpt);
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
    public List<ViewVerticalReportByFnyRpt> getAllVwVerticalReportByFnyRptById(Integer vwVerticalReportId) {
        List<ViewVerticalReportByFnyRpt> vwVerticalRptByFnyRpts = new ArrayList<ViewVerticalReportByFnyRpt>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptByFnyRpts = session.createQuery("from ViewVerticalReportByFnyRpt where VwVerticalReportId = " + vwVerticalReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptByFnyRpts;
    } 
    
}
