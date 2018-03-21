package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

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
	

    @SuppressWarnings({"unchecked", "unused"})
    public List<ViewVerticalReportColumnByExpField> getAllVwVerticalRptColsByExpFieldByColId(Integer vwVerticalRptColumnId) {
        List<ViewVerticalReportColumnByExpField> vwVerticalRptColsByExpField = new ArrayList<ViewVerticalReportColumnByExpField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptColsByExpField = session.createQuery("from ViewVerticalReportColumnByExpField where VwVerticalRptColumnId = " + vwVerticalRptColumnId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptColsByExpField;
    }  
    
}
