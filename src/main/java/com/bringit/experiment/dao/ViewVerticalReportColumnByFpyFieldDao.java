package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

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
	
    @SuppressWarnings({"unchecked", "unused"})
    public List<ViewVerticalReportColumnByFpyField> getAllVwVerticalRptColsByFpyFieldByColId(Integer vwVerticalRptColumnId) {
        List<ViewVerticalReportColumnByFpyField> vwVerticalRptColsByFpyField = new ArrayList<ViewVerticalReportColumnByFpyField>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptColsByFpyField = session.createQuery("from ViewVerticalReportColumnByFpyField where VwVerticalRptColumnId = " + vwVerticalRptColumnId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptColsByFpyField;
    }
    
}
