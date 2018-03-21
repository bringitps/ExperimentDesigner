package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReportColumnByFnyField;
import com.bringit.experiment.dal.HibernateUtil;

public class ViewVerticalReportColumnByFnyFieldDao {

	public void addVwVerticalReportColumnByFnyField(ViewVerticalReportColumnByFnyField vwVerticalReportColumnByFnyField) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReportColumnByFnyField);
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

    public void deleteVwVerticalReportColumnByFnyField(int vwVerticalReportColumnByFnyFieldId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReportColumnByFnyField vwVerticalReportColumnByFnyField = (ViewVerticalReportColumnByFnyField)session.load(ViewVerticalReportColumnByFnyField.class, new Integer(vwVerticalReportColumnByFnyFieldId));
            session.delete(vwVerticalReportColumnByFnyField);
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

    public void updateVwVerticalReportColumnByFnyField(ViewVerticalReportColumnByFnyField vwVerticalReportColumnByFnyField) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReportColumnByFnyField);
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
    public List<ViewVerticalReportColumnByFnyField> getAllVwVerticalRptColsByFnyFieldByColId(Integer vwVerticalRptColumnId) {
        List<ViewVerticalReportColumnByFnyField> vwVerticalRptColsByFnyField = new ArrayList<ViewVerticalReportColumnByFnyField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptColsByFnyField = session.createQuery("from ViewVerticalReportColumnByFnyField where VwVerticalRptColumnId = " + vwVerticalRptColumnId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptColsByFnyField;
    } 
}
