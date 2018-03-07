package com.bringit.experiment.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportColumnByEnrichment;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportColumnByEnrichmentDao {

	public void addVwVerticalReportColumnByEnrichment(ViewVerticalReportColumnByEnrichment vwVerticalReportColumnByEnrichment) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportColumnByEnrichment);
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

    public void deleteVwVerticalReportColumnByEnrichment(int vwVerticalReportColumnByEnrichmentId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportColumnByEnrichment vwVerticalReportColumnByEnrichment = (ViewVerticalReportColumnByEnrichment)session.load(ViewVerticalReportColumnByEnrichment.class, new Integer(vwVerticalReportColumnByEnrichmentId));
            session.delete(vwVerticalReportColumnByEnrichment);
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

    public void updateVwVerticalReportColumnByEnrichment(ViewVerticalReportColumnByEnrichment vwVerticalReportColumnByEnrichment) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportColumnByEnrichment);
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
