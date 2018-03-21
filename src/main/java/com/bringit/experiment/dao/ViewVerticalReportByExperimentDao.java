package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportByExperiment;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportByExperimentDao {
	
	public void addVwVerticalReportByExperiment(ViewVerticalReportByExperiment vwVerticalReportByExperiment) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportByExperiment);
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

    public void deleteVwVerticalReportByExperiment(int vwVerticalReportByExperimentId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportByExperiment vwVerticalReportByExperiment = (ViewVerticalReportByExperiment)session.load(ViewVerticalReportByExperiment.class, new Integer(vwVerticalReportByExperimentId));
            session.delete(vwVerticalReportByExperiment);
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

    public void updateVwVerticalReportByExperiment(ViewVerticalReportByExperiment vwVerticalReportByExperiment) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportByExperiment);
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
    public List<ViewVerticalReportByExperiment> getAllVwVerticalReportByExperimentByRptId(Integer vwVerticalReportId) {
        List<ViewVerticalReportByExperiment> vwVerticalRptByExps = new ArrayList<ViewVerticalReportByExperiment>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptByExps = session.createQuery("from ViewVerticalReportByExperiment where VwVerticalReportId = " + vwVerticalReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptByExps;
    }    
    
}
