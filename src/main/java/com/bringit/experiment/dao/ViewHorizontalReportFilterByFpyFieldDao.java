package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportFilterByFpyField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportFilterByFpyFieldDao {
public void addVwHorizontalReportFilterByFpyField(ViewHorizontalReportFilterByFpyField vwHorizontalReportFilterByFpyField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportFilterByFpyField);
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

    public void deleteVwHorizontalReportFilterByFpyField(int vwHorizontalReportFilterByFpyFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportFilterByFpyField vwHorizontalReportFilterByFpyField = (ViewHorizontalReportFilterByFpyField)session.load(ViewHorizontalReportFilterByFpyField.class, new Integer(vwHorizontalReportFilterByFpyFieldId));
            session.delete(vwHorizontalReportFilterByFpyField);
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

    public void updateVwHorizontalReportFilterByFpyField(ViewHorizontalReportFilterByFpyField vwHorizontalReportFilterByFpyField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportFilterByFpyField);
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
    public List<ViewHorizontalReportFilterByFpyField> getAllVwHorizontalReportFiltersByFpyRptId(Integer vwHorizontalRptByFpyId) {
        List<ViewHorizontalReportFilterByFpyField> vwHorizontalRptFiltersByFpy = new ArrayList<ViewHorizontalReportFilterByFpyField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptFiltersByFpy = session.createQuery("from ViewHorizontalReportFilterByFpyField where VwHorizontalFpyRptId = " + vwHorizontalRptByFpyId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptFiltersByFpy;
    }
}
