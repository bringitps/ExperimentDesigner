package com.bringit.experiment.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportFilterByFnyField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportFilterByFnyFieldDao {


	public void addVwVerticalReportFilterByFnyField(ViewVerticalReportFilterByFnyField vwVerticalReportFilterByFnyField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportFilterByFnyField);
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

    public void deleteVwVerticalReportFilterByFnyField(int vwVerticalReportFilterByFnyFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportFilterByFnyField vwVerticalReportFilterByFnyField = (ViewVerticalReportFilterByFnyField)session.load(ViewVerticalReportFilterByFnyField.class, new Integer(vwVerticalReportFilterByFnyFieldId));
            session.delete(vwVerticalReportFilterByFnyField);
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

    public void updateVwVerticalReportFilterByFnyField(ViewVerticalReportFilterByFnyField vwVerticalReportFilterByFnyField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportFilterByFnyField);
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
