package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.ViewHorizontalReport;
import com.bringit.experiment.bll.ViewHorizontalReportJobData;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Constants;

public class ViewHorizontalReportJobDataDao {

	public ViewHorizontalReportJobData addViewHorizontalReportJobData(ViewHorizontalReportJobData vwHorizontalRptJobData) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(vwHorizontalRptJobData);
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
        return vwHorizontalRptJobData;
    }

    public void deleteViewHorizontalReportJob(int vwHorizontalRptJobId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        // Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ViewHorizontalReportJobData vwHorizontalRptJobData = (ViewHorizontalReportJobData) session.load(ViewHorizontalReportJobData.class, new Integer(vwHorizontalRptJobId));
            session.delete(vwHorizontalRptJobData);
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

    public void updateVwHorizontalRptDataJob(ViewHorizontalReportJobData vwHorizontalRptJobData) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwHorizontalRptJobData);
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
    public List<ViewHorizontalReportJobData> getAllViewHorizontalRptDataJobs() {
        List<ViewHorizontalReportJobData> vwHorizontalRptJobDatas = new ArrayList<ViewHorizontalReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptJobDatas = session.createQuery("from ViewHorizontalReportJobData").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptJobDatas;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<ViewHorizontalReportJobData> getActiveVwHorizontalRptDataJobs(Integer vwHorizontalRptId) {
        List<ViewHorizontalReportJobData> vwHorizontalRptJobDatas = new ArrayList<ViewHorizontalReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwHorizontalRptJobDatas = session.createQuery("from ViewHorizontalReportJobData where IsCompleted=0 and ViewHorizontalReportId="+vwHorizontalRptId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwHorizontalRptJobDatas;
    }

    public ViewHorizontalReportJobData createJob(String type, SysUser sysUser, Integer vwHorizontalRptId) {
    	ViewHorizontalReportJobData vwHorizontalRptJobData = new ViewHorizontalReportJobData();
        ViewHorizontalReportDao vwHorizontalRptDao= new ViewHorizontalReportDao();
        vwHorizontalRptJobData.setCreatedDate(new Date());
        vwHorizontalRptJobData.setStatus(Constants.JOB_RUNNING);
        if (sysUser != null) {
        	vwHorizontalRptJobData.setCreatedBy(sysUser);
        }
        if(vwHorizontalRptId !=null) {
        	vwHorizontalRptJobData.setVwHorizontalReport(vwHorizontalRptDao.getVwHorizontalRptById(vwHorizontalRptId));
        }else {
        	vwHorizontalRptJobData.setVwHorizontalReport(null);
        }

        vwHorizontalRptJobData.setIsCompleted(false);
        if ((Constants.Auto).equalsIgnoreCase(type)) {
        	vwHorizontalRptJobData.setIsAutoScheduler(true);
        } else {
        	vwHorizontalRptJobData.setIsAutoScheduler(false);
        }
        return this.addViewHorizontalReportJobData(vwHorizontalRptJobData);
    }

    public void updateVwHorizontalJobStatus(ViewHorizontalReportJobData vwHorizontalRptJobData, String status) {
    	vwHorizontalRptJobData.setIsCompleted(true);
    	vwHorizontalRptJobData.setLastModifiedDate(new Date());
    	vwHorizontalRptJobData.setStatus(status);
        this.updateVwHorizontalRptDataJob(vwHorizontalRptJobData);
    }

    public void vwHorizontalProcedureJob() {
        vwHorizontalProcedureJob(null);
    }


    public Map<String,Object> vwHorizontalProcedureJob(Integer vwHorizontalRptId) {

        Map<String,Object> map= new HashMap<>();
        ViewHorizontalReportDao vwHorizontalReportDao = new ViewHorizontalReportDao();
        ViewHorizontalReportJobData vwHorizontalReportJobData = new ViewHorizontalReportJobData();

        try {
        	System.out.println("Start of Refreshing View Horizontal Report data: " + new Date());
            List<ViewHorizontalReport> lstVwHorizontalRpt = new ArrayList<>();
            if (vwHorizontalRptId == null || vwHorizontalRptId <= 0) {
            	lstVwHorizontalRpt = vwHorizontalReportDao.getAllViewHorizontalReports();
            } else {
            	lstVwHorizontalRpt.add(vwHorizontalReportDao.getVwHorizontalRptById(vwHorizontalRptId));
            }

            for (ViewHorizontalReport vwHorizontalReport : lstVwHorizontalRpt) {
                if (this.getActiveVwHorizontalRptDataJobs(vwHorizontalReport.getVwHorizontalRptId()).size() <= 0) {
                	System.out.println("Refreshing View Horizontal Report data: " + vwHorizontalReport.getVwHorizontalRptName());
                	vwHorizontalReportJobData = this.createJob(Constants.Auto, null, vwHorizontalReport.getVwHorizontalRptId());
                    map=vwHorizontalReportDao.executeVwHorizontalRptProcedure(vwHorizontalReportJobData, vwHorizontalReport);
                } else {
                    map.put("statusMessage",Constants.JOB_NOT_EXECUTED );
                    map.put("status",Constants.ERROR);
                    vwHorizontalReportJobData = this.createJob(Constants.Auto, null, vwHorizontalReport.getVwHorizontalRptId());
                    this.updateVwHorizontalJobStatus(vwHorizontalReportJobData, Constants.JOB_NOT_EXECUTED);
                }
            }
        	System.out.println("End of View Horizontal Report data: " + new Date());
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return map;
    }
}
