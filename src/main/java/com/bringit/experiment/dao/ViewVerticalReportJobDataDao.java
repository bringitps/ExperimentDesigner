package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ViewVerticalReport;
import com.bringit.experiment.bll.ViewVerticalReportJobData;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Constants;

public class ViewVerticalReportJobDataDao {

	public ViewVerticalReportJobData addViewVerticalReportJobData(ViewVerticalReportJobData vwVerticalRptJobData) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(vwVerticalRptJobData);
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
        return vwVerticalRptJobData;
    }

    public void deleteViewVerticalReportJob(int vwVerticalRptJobId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        // Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ViewVerticalReportJobData vwVerticalRptJobData = (ViewVerticalReportJobData) session.load(ViewVerticalReportJobData.class, new Integer(vwVerticalRptJobId));
            session.delete(vwVerticalRptJobData);
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

    public void updateVwVerticalRptDataJob(ViewVerticalReportJobData vwVerticalRptJobData) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(vwVerticalRptJobData);
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
    public List<ViewVerticalReportJobData> getAllViewVerticalRptDataJobs() {
        List<ViewVerticalReportJobData> vwVerticalRptJobDatas = new ArrayList<ViewVerticalReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptJobDatas = session.createQuery("from ViewVerticalReportJobData").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptJobDatas;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<ViewVerticalReportJobData> getActiveVwVerticalRptDataJobs(Integer vwVerticalRptId) {
        List<ViewVerticalReportJobData> vwVerticalRptJobDatas = new ArrayList<ViewVerticalReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            vwVerticalRptJobDatas = session.createQuery("from ViewVerticalReportJobData where IsCompleted=0 and ViewVerticalReportId="+vwVerticalRptId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return vwVerticalRptJobDatas;
    }

    public ViewVerticalReportJobData createJob(String type, SysUser sysUser, Integer vwVerticalRptId) {
    	ViewVerticalReportJobData vwVerticalRptJobData = new ViewVerticalReportJobData();
        ViewVerticalReportDao vwVerticalRptDao= new ViewVerticalReportDao();
        vwVerticalRptJobData.setCreatedDate(new Date());
        vwVerticalRptJobData.setStatus(Constants.JOB_RUNNING);
        if (sysUser != null) {
        	vwVerticalRptJobData.setCreatedBy(sysUser);
        }
        if(vwVerticalRptId !=null) {
        	vwVerticalRptJobData.setVwVerticalReport(vwVerticalRptDao.getVwVerticalRptById(vwVerticalRptId));
        }else {
        	vwVerticalRptJobData.setVwVerticalReport(null);
        }

        vwVerticalRptJobData.setIsCompleted(false);
        if ((Constants.Auto).equalsIgnoreCase(type)) {
        	vwVerticalRptJobData.setIsAutoScheduler(true);
        } else {
        	vwVerticalRptJobData.setIsAutoScheduler(false);
        }
        return this.addViewVerticalReportJobData(vwVerticalRptJobData);
    }

    public void updateVwVerticalJobStatus(ViewVerticalReportJobData vwVerticalRptJobData, String status) {
    	vwVerticalRptJobData.setIsCompleted(true);
    	vwVerticalRptJobData.setLastModifiedDate(new Date());
    	vwVerticalRptJobData.setStatus(status);
        this.updateVwVerticalRptDataJob(vwVerticalRptJobData);
    }

    public void vwVerticalProcedureJob() {
        vwVerticalProcedureJob(null);
    }


    public Map<String,Object> vwVerticalProcedureJob(Integer vwVerticalRptId) {

        Map<String,Object> map= new HashMap<>();
        ViewVerticalReportDao vwVerticalReportDao = new ViewVerticalReportDao();
        ViewVerticalReportJobData vwVerticalReportJobData = new ViewVerticalReportJobData();

        try {
        	System.out.println("Start of Refreshing View Vertical Report data: " + new Date());
            List<ViewVerticalReport> lstVwVerticalRpt = new ArrayList<>();
            if (vwVerticalRptId == null || vwVerticalRptId <= 0) {
            	lstVwVerticalRpt = vwVerticalReportDao.getAllViewVerticalReports();
            } else {
            	lstVwVerticalRpt.add(vwVerticalReportDao.getVwVerticalRptById(vwVerticalRptId));
            }

            for (ViewVerticalReport vwVerticalReport : lstVwVerticalRpt) {
                if (this.getActiveVwVerticalRptDataJobs(vwVerticalReport.getVwVerticalRptId()).size() <= 0) {
                	System.out.println("Refreshing View Vertical Report data: " + vwVerticalReport.getVwVerticalRptName());
                	vwVerticalReportJobData = this.createJob(Constants.Auto, null, vwVerticalReport.getVwVerticalRptId());
                    map=vwVerticalReportDao.executeVwVerticalRptProcedure(vwVerticalReportJobData, vwVerticalReport);
                } else {
                    map.put("statusMessage",Constants.JOB_NOT_EXECUTED );
                    map.put("status",Constants.ERROR);
                    vwVerticalReportJobData = this.createJob(Constants.Auto, null, vwVerticalReport.getVwVerticalRptId());
                    this.updateVwVerticalJobStatus(vwVerticalReportJobData, Constants.JOB_NOT_EXECUTED);
                }
            }
        	System.out.println("End of View Vertical Report data: " + new Date());
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return map;
    }
}
