package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.FirstPassYieldReportJobData;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Constants;

public class FirstPassYieldReportJobDataDao {

	    public FirstPassYieldReportJobData addFpyRptDataJob(FirstPassYieldReportJobData fpyRptJobData) {

	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();

	        try {
	            trns = session.beginTransaction();
	            session.save(fpyRptJobData);
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
	        return fpyRptJobData;
	    }

	    public void deleteFpyRptDataJob(int fpyRptDataId) {
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        // Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            FirstPassYieldReportJobData fpyRptJobData = (FirstPassYieldReportJobData) session.load(FirstPassYieldReportJobData.class, new Integer(fpyRptDataId));
	            session.delete(fpyRptJobData);
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

	    public void updateFpyRptDataJob(FirstPassYieldReportJobData fpyRptJobData) {
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        try {
	            trns = session.beginTransaction();
	            session.update(fpyRptJobData);
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
	    public List<FirstPassYieldReportJobData> getAllFpyRptDataJobs() {
	        List<FirstPassYieldReportJobData> fpyRptJobDatas = new ArrayList<FirstPassYieldReportJobData>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        try {
	            trns = session.beginTransaction();
	            fpyRptJobDatas = session.createQuery("from FirstPassYieldReportJobData").list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return fpyRptJobDatas;
	    }

	    @SuppressWarnings({"unchecked", "unused"})
	    public List<FirstPassYieldReportJobData> getActiveFpyRptDataJobs(Integer fpyRptId) {
	        List<FirstPassYieldReportJobData> fpyRptJobDatas = new ArrayList<FirstPassYieldReportJobData>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        try {
	            trns = session.beginTransaction();
	            fpyRptJobDatas = session.createQuery("from FirstPassYieldReportJobData where IsCompleted=0 and FpyReportId="+fpyRptId).list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return fpyRptJobDatas;
	    }

	    public FirstPassYieldReportJobData createJob(String type, SysUser sysUser, Integer fpyRptId) {
	    	FirstPassYieldReportJobData fpyRptJobData = new FirstPassYieldReportJobData();
	        FirstPassYieldReportDao fpyReportDao= new FirstPassYieldReportDao();
	        fpyRptJobData.setCreatedDate(new Date());
	        fpyRptJobData.setStatus(Constants.JOB_RUNNING);
	        if (sysUser != null) {
	        	fpyRptJobData.setCreatedBy(sysUser);
	        }
	        if(fpyRptId !=null) {
	        	fpyRptJobData.setFirstPassYieldReport(fpyReportDao.getFirstPassYieldReportById(fpyRptId));
	        }else {
	        	fpyRptJobData.setFirstPassYieldReport(null);
	        }

	        fpyRptJobData.setIsCompleted(false);
	        if ((Constants.Auto).equalsIgnoreCase(type)) {
	        	fpyRptJobData.setIsAutoScheduler(true);
	        } else {
	        	fpyRptJobData.setIsAutoScheduler(false);
	        }
	        return this.addFpyRptDataJob(fpyRptJobData);
	    }

	    public void updateFpyJobStatus(FirstPassYieldReportJobData fpyRptJobData, String status) {
	    	fpyRptJobData.setIsCompleted(true);
	    	fpyRptJobData.setLastModifiedDate(new Date());
	    	fpyRptJobData.setStatus(status);
	        this.updateFpyRptDataJob(fpyRptJobData);
	    }

	    public void fpyProcedureJob() {
	        fpyProcedureJob(null);
	    }


	    public Map<String,Object> fpyProcedureJob(Integer fpyRptId) {

	        Map<String,Object> map= new HashMap<>();
	        FirstPassYieldReportDao fpyReportDao = new FirstPassYieldReportDao();
	        FirstPassYieldReportJobData fpyReportJobData = new FirstPassYieldReportJobData();

	        try {
	        	System.out.println("Start of Refreshing First Pass Yield Report data: " + new Date());
	            List<FirstPassYieldReport> lstFpyRpt = new ArrayList<>();
	            if (fpyRptId == null || fpyRptId <= 0) {
	            	lstFpyRpt = fpyReportDao.getAllFirstPassYieldReports();
	            } else {
	            	lstFpyRpt.add(fpyReportDao.getFirstPassYieldReportById(fpyRptId));
	            }

	            for (FirstPassYieldReport fpyReport : lstFpyRpt) {
	                if (this.getActiveFpyRptDataJobs(fpyReport.getFpyReportId()).size() <= 0) {
	                	System.out.println("Refreshing First Pass Yield Report data: " + fpyReport.getFpyReportName());
	                	fpyReportJobData = this.createJob(Constants.Auto, null, fpyReport.getFpyReportId());
	                    map=fpyReportDao.executeFpyRptProcedure(fpyReportJobData, fpyReport);
	                } else {
	                    map.put("statusMessage",Constants.JOB_NOT_EXECUTED );
	                    map.put("status",Constants.ERROR);
	                    fpyReportJobData = this.createJob(Constants.Auto, null, fpyReport.getFpyReportId());
	                    this.updateFpyJobStatus(fpyReportJobData, Constants.JOB_NOT_EXECUTED);
	                }
	            }
	        	System.out.println("End of Refreshing First Pass Yield Report data: " + new Date());
	        } catch (Exception ex) {

	            ex.printStackTrace();
	        }
	        return map;
	    }
	
	
}
