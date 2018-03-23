package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportFilterByFpyField;
import com.bringit.experiment.bll.ViewVerticalReportFilterByFtyField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportFilterByFtyFieldDao {

	public void addVwVerticalReportFilterByFtyField(ViewVerticalReportFilterByFtyField vwVerticalReportFilterByFtyField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportFilterByFtyField);
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

    public void deleteVwVerticalReportFilterByFtyField(int vwVerticalReportFilterByFtyFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportFilterByFtyField vwVerticalReportFilterByFtyField = (ViewVerticalReportFilterByFtyField)session.load(ViewVerticalReportFilterByFtyField.class, new Integer(vwVerticalReportFilterByFtyFieldId));
            session.delete(vwVerticalReportFilterByFtyField);
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

    public void updateVwVerticalReportFilterByFtyField(ViewVerticalReportFilterByFtyField vwVerticalReportFilterByFtyField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportFilterByFtyField);
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
    public List<ViewVerticalReportFilterByFtyField> getAllVwVerticalReportFiltersByFtyRptId(Integer vwVerticalRptByFtyId) {
        List<ViewVerticalReportFilterByFtyField> vwVerticalRptFiltersByFty = new ArrayList<ViewVerticalReportFilterByFtyField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptFiltersByFty = session.createQuery("from ViewVerticalReportFilterByFtyField where VwVerticalFtyRptId = " + vwVerticalRptByFtyId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptFiltersByFty;
    }
}
