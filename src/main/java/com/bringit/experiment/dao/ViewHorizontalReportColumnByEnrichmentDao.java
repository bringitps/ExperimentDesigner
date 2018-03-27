package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportColumnByEnrichment;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportColumnByEnrichmentDao {
public void addVwHorizontalReportColumnByEnrichment(ViewHorizontalReportColumnByEnrichment vwHorizontalReportColumnByEnrichment) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportColumnByEnrichment);
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

    public void deleteVwHorizontalReportColumnByEnrichment(int vwHorizontalReportColumnByEnrichmentId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportColumnByEnrichment vwHorizontalReportColumnByEnrichment = (ViewHorizontalReportColumnByEnrichment)session.load(ViewHorizontalReportColumnByEnrichment.class, new Integer(vwHorizontalReportColumnByEnrichmentId));
            session.delete(vwHorizontalReportColumnByEnrichment);
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

    public void updateVwHorizontalReportColumnByEnrichment(ViewHorizontalReportColumnByEnrichment vwHorizontalReportColumnByEnrichment) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportColumnByEnrichment);
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
    public List<ViewHorizontalReportColumnByEnrichment> getAllVwHorizontalRptColsByEnrichmentByColId(Integer vwHorizontalRptColumnId) {
        List<ViewHorizontalReportColumnByEnrichment> vwHorizontalRptColEnrichments = new ArrayList<ViewHorizontalReportColumnByEnrichment>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptColEnrichments = session.createQuery("from ViewHorizontalReportColumnByEnrichment where VwHorizontalRptColumnId = " + vwHorizontalRptColumnId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptColEnrichments;
    }  
}
