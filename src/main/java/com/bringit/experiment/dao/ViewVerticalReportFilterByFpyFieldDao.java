package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportFilterByFnyField;
import com.bringit.experiment.bll.ViewVerticalReportFilterByFpyField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportFilterByFpyFieldDao {

	public void addVwVerticalReportFilterByFpyField(ViewVerticalReportFilterByFpyField vwVerticalReportFilterByFpyField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportFilterByFpyField);
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

    public void deleteVwVerticalReportFilterByFpyField(int vwVerticalReportFilterByFpyFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportFilterByFpyField vwVerticalReportFilterByFpyField = (ViewVerticalReportFilterByFpyField)session.load(ViewVerticalReportFilterByFpyField.class, new Integer(vwVerticalReportFilterByFpyFieldId));
            session.delete(vwVerticalReportFilterByFpyField);
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

    public void updateVwVerticalReportFilterByFpyField(ViewVerticalReportFilterByFpyField vwVerticalReportFilterByFpyField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportFilterByFpyField);
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
    public List<ViewVerticalReportFilterByFpyField> getAllVwVerticalReportFiltersByFpyRptId(Integer vwVerticalRptByFpyId) {
        List<ViewVerticalReportFilterByFpyField> vwVerticalRptFiltersByFpy = new ArrayList<ViewVerticalReportFilterByFpyField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptFiltersByFpy = session.createQuery("from ViewVerticalReportFilterByFpyField where VwVerticalFpyRptId = " + vwVerticalRptByFpyId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptFiltersByFpy;
    }
}
