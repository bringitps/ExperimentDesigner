package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportByExperiment;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportByExperimentDao {

	public void addVwHorizontalReportByExperiment(ViewHorizontalReportByExperiment vwHorizontalReportByExperiment) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportByExperiment);
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

    public void deleteVwHorizontalReportByExperiment(int vwHorizontalReportByExperimentId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportByExperiment vwHorizontalReportByExperiment = (ViewHorizontalReportByExperiment)session.load(ViewHorizontalReportByExperiment.class, new Integer(vwHorizontalReportByExperimentId));
            session.delete(vwHorizontalReportByExperiment);
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

    public void updateVwHorizontalReportByExperiment(ViewHorizontalReportByExperiment vwHorizontalReportByExperiment) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportByExperiment);
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
    public List<ViewHorizontalReportByExperiment> getAllVwHorizontalReportByExperimentByRptId(Integer vwHorizontalReportId) {
        List<ViewHorizontalReportByExperiment> vwHorizontalRptByExps = new ArrayList<ViewHorizontalReportByExperiment>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptByExps = session.createQuery("from ViewHorizontalReportByExperiment where VwHorizontalReportId = " + vwHorizontalReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptByExps;
    }
	
	
}
