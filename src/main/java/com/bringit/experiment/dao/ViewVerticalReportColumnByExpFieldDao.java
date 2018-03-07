package com.bringit.experiment.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportColumnByExpField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportColumnByExpFieldDao {

	public void addVwVerticalReportColumnByExpField(ViewVerticalReportColumnByExpField vwVerticalReportColumnByExpField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportColumnByExpField);
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

    public void deleteVwVerticalReportColumnByExpField(int vwVerticalReportColumnByExpFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportColumnByExpField vwVerticalReportColumnByExpField = (ViewVerticalReportColumnByExpField)session.load(ViewVerticalReportColumnByExpField.class, new Integer(vwVerticalReportColumnByExpFieldId));
            session.delete(vwVerticalReportColumnByExpField);
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

    public void updateVwVerticalReportColumnByExpField(ViewVerticalReportColumnByExpField vwVerticalReportColumnByExpField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportColumnByExpField);
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
