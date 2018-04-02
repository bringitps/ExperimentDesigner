package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.FinalPassYieldInfoField;
import com.bringit.experiment.bll.FinalPassYieldReport;
import com.bringit.experiment.bll.FinalPassYieldReportJobData;
import com.bringit.experiment.bll.ViewVerticalReport;
import com.bringit.experiment.bll.ViewVerticalReportJobData;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.Constants;

public class ViewVerticalReportDao {

	public void addVwVerticalReport(ViewVerticalReport vwVerticalReport) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwVerticalReport);
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

    public void deleteVwVerticalReport(int vwVerticalReportId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewVerticalReport vwVerticalReport = (ViewVerticalReport)session.load(ViewVerticalReport.class, new Integer(vwVerticalReportId));
            session.delete(vwVerticalReport);
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

    public void updateVwVerticalReport(ViewVerticalReport vwVerticalReport) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalReport);
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
    
    @SuppressWarnings("unused")
	public ViewVerticalReport getVwVerticalRptById(int vwVerticalRptId) {
    	ViewVerticalReport vwVerticalRpt = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from ViewVerticalReport where VwVerticalRptId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", vwVerticalRptId);
            vwVerticalRpt = (ViewVerticalReport) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRpt;
    }
    
    @SuppressWarnings({"unchecked", "unused"})
    public List<ViewVerticalReport> getAllViewVerticalReports() {
        List<ViewVerticalReport> vwVerticalReports = new ArrayList<ViewVerticalReport>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            vwVerticalReports = session.createQuery("from ViewVerticalReport where VwVerticalRptIsActive = 'true'").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalReports;
    }
    
    public Map<String,Object> executeVwVerticalRptProcedure(ViewVerticalReportJobData vwVerticalRptJobData, ViewVerticalReport vwVerticalRpt) {
        Map<String,Object> map= new HashMap<>();
        ViewVerticalReportJobDataDao viewVerticalReportJobDataDao = new ViewVerticalReportJobDataDao();
        String statusMessage = Constants.JOB_FINISHED;

        map.put("statusMessage", statusMessage);
        map.put("status", Constants.SUCCESS);
        try {


            List<String> lstVwVerticalRptBean;
            lstVwVerticalRptBean = new ArrayList<>();
            lstVwVerticalRptBean.add(vwVerticalRpt.getVwVerticalRptId().toString());
            new ExecuteQueryDao().executeUpdateStoredProcedure("spVerticalReport", lstVwVerticalRptBean);

            vwVerticalRpt.setVwVerticalRptDbTableLastUpdate(new Date());
            this.updateVwVerticalReport(vwVerticalRpt);

            statusMessage = Constants.JOB_FINISHED;
        } catch (Exception ex) {
            statusMessage = Constants.JOB_EXCEPTION;

            map.put("statusMessage", statusMessage);
            map.put("status", Constants.ERROR);
            ex.printStackTrace();
        } finally {
        	viewVerticalReportJobDataDao.updateVwVerticalJobStatus(vwVerticalRptJobData, statusMessage);
        }

        return map;
    }
    
    
    
}
