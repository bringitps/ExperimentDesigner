package com.bringit.experiment.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportColumnByFtyField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportColumnByFtyFieldDao {
	

	public void addVwVerticalReportColumnByFtyField(ViewVerticalReportColumnByFtyField vwVerticalReportColumnByFtyField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportColumnByFtyField);
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

    public void deleteVwVerticalReportColumnByFtyField(int vwVerticalReportColumnByFtyFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportColumnByFtyField vwVerticalReportColumnByFtyField = (ViewVerticalReportColumnByFtyField)session.load(ViewVerticalReportColumnByFtyField.class, new Integer(vwVerticalReportColumnByFtyFieldId));
            session.delete(vwVerticalReportColumnByFtyField);
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

    public void updateVwVerticalReportColumnByFtyField(ViewVerticalReportColumnByFtyField vwVerticalReportColumnByFtyField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportColumnByFtyField);
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
