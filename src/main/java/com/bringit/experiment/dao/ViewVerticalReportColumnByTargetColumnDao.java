package com.bringit.experiment.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportColumnByTargetColumn;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportColumnByTargetColumnDao {

	public void addVwVerticalReportColumnByTargetColumn(ViewVerticalReportColumnByTargetColumn vwVerticalReportColumnByTargetColumn) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportColumnByTargetColumn);
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

    public void deleteVwVerticalReportColumnByTargetColumn(int vwVerticalReportColumnByTargetColumnId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportColumnByTargetColumn vwVerticalReportColumnByTargetColumn = (ViewVerticalReportColumnByTargetColumn)session.load(ViewVerticalReportColumnByTargetColumn.class, new Integer(vwVerticalReportColumnByTargetColumnId));
            session.delete(vwVerticalReportColumnByTargetColumn);
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

    public void updateVwVerticalReportColumnByTargetColumn(ViewVerticalReportColumnByTargetColumn vwVerticalReportColumnByTargetColumn) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportColumnByTargetColumn);
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
