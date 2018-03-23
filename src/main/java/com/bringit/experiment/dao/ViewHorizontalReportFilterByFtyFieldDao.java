package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportFilterByFtyField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportFilterByFtyFieldDao {
public void addVwHorizontalReportFilterByFtyField(ViewHorizontalReportFilterByFtyField vwHorizontalReportFilterByFtyField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportFilterByFtyField);
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

    public void deleteVwHorizontalReportFilterByFtyField(int vwHorizontalReportFilterByFtyFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportFilterByFtyField vwHorizontalReportFilterByFtyField = (ViewHorizontalReportFilterByFtyField)session.load(ViewHorizontalReportFilterByFtyField.class, new Integer(vwHorizontalReportFilterByFtyFieldId));
            session.delete(vwHorizontalReportFilterByFtyField);
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

    public void updateVwHorizontalReportFilterByFtyField(ViewHorizontalReportFilterByFtyField vwHorizontalReportFilterByFtyField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportFilterByFtyField);
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
    public List<ViewHorizontalReportFilterByFtyField> getAllVwHorizontalReportFiltersByFtyRptId(Integer vwHorizontalRptByFtyId) {
        List<ViewHorizontalReportFilterByFtyField> vwHorizontalRptFiltersByFty = new ArrayList<ViewHorizontalReportFilterByFtyField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptFiltersByFty = session.createQuery("from ViewHorizontalReportFilterByFtyField where VwHorizontalFtyRptId = " + vwHorizontalRptByFtyId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptFiltersByFty;
    }
}
