package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReportFilterByTargetColumn;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewHorizontalReportFilterByTargetColumnDao {
public void addVwHorizontalReportFilterByTargetColumn(ViewHorizontalReportFilterByTargetColumn vwHorizontalReportFilterByTargetColumn) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReportFilterByTargetColumn);
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

    public void deleteVwHorizontalReportFilterByTargetColumn(int vwHorizontalReportFilterByTargetColumnId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportFilterByTargetColumn vwHorizontalReportFilterByTargetColumn = (ViewHorizontalReportFilterByTargetColumn)session.load(ViewHorizontalReportFilterByTargetColumn.class, new Integer(vwHorizontalReportFilterByTargetColumnId));
            session.delete(vwHorizontalReportFilterByTargetColumn);
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

    public void updateVwHorizontalReportFilterByTargetColumn(ViewHorizontalReportFilterByTargetColumn vwHorizontalReportFilterByTargetColumn) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReportFilterByTargetColumn);
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
    public List<ViewHorizontalReportFilterByTargetColumn> getAllVwHorizontalReportFiltersByTargetRptId(Integer vwHorizontalRptByTargetId) {
        List<ViewHorizontalReportFilterByTargetColumn> vwHorizontalRptFiltersByTargetRpt = new ArrayList<ViewHorizontalReportFilterByTargetColumn>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptFiltersByTargetRpt = session.createQuery("from ViewHorizontalReportFilterByTargetColumn where VwHorizontalTargetRptId = " + vwHorizontalRptByTargetId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptFiltersByTargetRpt;
    }
}
