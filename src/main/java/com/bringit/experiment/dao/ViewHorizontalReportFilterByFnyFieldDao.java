package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportFilterByFnyField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportFilterByFnyFieldDao {

	public void addVwHorizontalReportFilterByFnyField(ViewHorizontalReportFilterByFnyField vwHorizontalReportFilterByFnyField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportFilterByFnyField);
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

    public void deleteVwHorizontalReportFilterByFnyField(int vwHorizontalReportFilterByFnyFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportFilterByFnyField vwHorizontalReportFilterByFnyField = (ViewHorizontalReportFilterByFnyField)session.load(ViewHorizontalReportFilterByFnyField.class, new Integer(vwHorizontalReportFilterByFnyFieldId));
            session.delete(vwHorizontalReportFilterByFnyField);
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

    public void updateVwHorizontalReportFilterByFnyField(ViewHorizontalReportFilterByFnyField vwHorizontalReportFilterByFnyField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportFilterByFnyField);
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
    public List<ViewHorizontalReportFilterByFnyField> getAllVwHorizontalReportFiltersByFnyRptId(Integer vwHorizontalRptByFnyId) {
        List<ViewHorizontalReportFilterByFnyField> vwHorizontalRptFiltersByFny = new ArrayList<ViewHorizontalReportFilterByFnyField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptFiltersByFny = session.createQuery("from ViewHorizontalReportFilterByFnyField where VwHorizontalFnyRptId = " + vwHorizontalRptByFnyId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptFiltersByFny;
    }
}
