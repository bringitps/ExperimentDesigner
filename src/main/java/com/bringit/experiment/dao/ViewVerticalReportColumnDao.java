package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportByFtyRpt;
import com.bringit.experiment.bll.ViewVerticalReportColumn;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportColumnDao {

	public void addVwVerticalReportColumn(ViewVerticalReportColumn vwVerticalReportColumn) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportColumn);
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

    public void deleteVwVerticalReportColumn(int vwVerticalReportColumnId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportColumn vwVerticalReportColumn = (ViewVerticalReportColumn)session.load(ViewVerticalReportColumn.class, new Integer(vwVerticalReportColumnId));
            session.delete(vwVerticalReportColumn);
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

    public void updateVwVerticalReportColumn(ViewVerticalReportColumn vwVerticalReportColumn) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportColumn);
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
    public List<ViewVerticalReportColumn> getAllVwVerticalReportColumnsByRptId(Integer vwVerticalReportId) {
        List<ViewVerticalReportColumn> vwVerticalRptColumns = new ArrayList<ViewVerticalReportColumn>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptColumns = session.createQuery("from ViewVerticalReportColumn where VwVerticalReportId = " + vwVerticalReportId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptColumns;
    }  
    
}
