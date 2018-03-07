package com.bringit.experiment.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportColumnByFpyField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportColumnByFpyFieldDao {

	public void addVwVerticalReportColumnByFpyField(ViewVerticalReportColumnByFpyField vwVerticalReportColumnByFpyField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportColumnByFpyField);
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

    public void deleteVwVerticalReportColumnByFpyField(int vwVerticalReportColumnByFpyFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportColumnByFpyField vwVerticalReportColumnByFpyField = (ViewVerticalReportColumnByFpyField)session.load(ViewVerticalReportColumnByFpyField.class, new Integer(vwVerticalReportColumnByFpyFieldId));
            session.delete(vwVerticalReportColumnByFpyField);
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

    public void updateVwVerticalReportColumnByFpyField(ViewVerticalReportColumnByFpyField vwVerticalReportColumnByFpyField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportColumnByFpyField);
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
