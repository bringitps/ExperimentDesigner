package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewHorizontalReport;
import com.bringit.experiment.bll.ViewHorizontalReportJobData;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Constants;

public class ViewHorizontalReportDao {

	public void addVwHorizontalReport(ViewHorizontalReport vwHorizontalReport) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalReport);
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

    public void deleteVwHorizontalReport(int vwHorizontalReportId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ViewHorizontalReport vwHorizontalReport = (ViewHorizontalReport)session.load(ViewHorizontalReport.class, new Integer(vwHorizontalReportId));
            session.delete(vwHorizontalReport);
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

    public void updateVwHorizontalReport(ViewHorizontalReport vwHorizontalReport) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalReport);
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
    public List<ViewHorizontalReport> getAllViewHorizontalReports() {
        List<ViewHorizontalReport> vwHorizontalReports = new ArrayList<ViewHorizontalReport>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            vwHorizontalReports = session.createQuery("from ViewHorizontalReport where VwHorizontalRptIsActive = 'true'").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalReports;
    }
    
    @SuppressWarnings("unused")
	public ViewHorizontalReport getVwHorizontalRptById(int vwHorizontalRptId) {
    	ViewHorizontalReport vwHorizontalRpt = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from ViewHorizontalReport where VwHorizontalRptId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", vwHorizontalRptId);
            vwHorizontalRpt = (ViewHorizontalReport) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRpt;
    }
    
    public Map<String,Object> executeVwHorizontalRptProcedure(ViewHorizontalReportJobData vwHorizontalRptJobData, ViewHorizontalReport vwHorizontalRpt) {
        Map<String,Object> map= new HashMap<>();
        ViewHorizontalReportJobDataDao viewHorizontalReportJobDataDao = new ViewHorizontalReportJobDataDao();
        String statusMessage = Constants.JOB_FINISHED;

        map.put("statusMessage", statusMessage);
        map.put("status", Constants.SUCCESS);
        try {


            List<String> lstVwHorizontalRptBean;
            lstVwHorizontalRptBean = new ArrayList<>();
            lstVwHorizontalRptBean.add(vwHorizontalRpt.getVwHorizontalRptId().toString());
            new ExecuteQueryDao().executeUpdateStoredProcedure("spHorizontalReport", lstVwHorizontalRptBean);

            vwHorizontalRpt.setVwHorizontalRptDbTableLastUpdate(new Date());
            this.updateVwHorizontalReport(vwHorizontalRpt);

            statusMessage = Constants.JOB_FINISHED;
        } catch (Exception ex) {
            statusMessage = Constants.JOB_EXCEPTION;

            map.put("statusMessage", statusMessage);
            map.put("status", Constants.ERROR);
            ex.printStackTrace();
        } finally {
        	viewHorizontalReportJobDataDao.updateVwHorizontalJobStatus(vwHorizontalRptJobData, statusMessage);
        }

        return map;
    }
    
}
