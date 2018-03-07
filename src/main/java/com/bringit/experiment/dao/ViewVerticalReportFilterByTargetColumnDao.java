package com.bringit.experiment.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportFilterByTargetColumn;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportFilterByTargetColumnDao {

public void addVwVerticalReportFilterByTargetColumn(ViewVerticalReportFilterByTargetColumn vwVerticalReportFilterByTargetColumn) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportFilterByTargetColumn);
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

    public void deleteVwVerticalReportFilterByTargetColumn(int vwVerticalReportFilterByTargetColumnId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportFilterByTargetColumn vwVerticalReportFilterByTargetColumn = (ViewVerticalReportFilterByTargetColumn)session.load(ViewVerticalReportFilterByTargetColumn.class, new Integer(vwVerticalReportFilterByTargetColumnId));
            session.delete(vwVerticalReportFilterByTargetColumn);
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

    public void updateVwVerticalReportFilterByTargetColumn(ViewVerticalReportFilterByTargetColumn vwVerticalReportFilterByTargetColumn) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportFilterByTargetColumn);
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
