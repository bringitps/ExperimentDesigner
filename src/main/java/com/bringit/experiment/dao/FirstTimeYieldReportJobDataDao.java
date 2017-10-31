package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.FirstTimeYieldReport;
import com.bringit.experiment.bll.FirstTimeYieldReportJobData;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Constants;

public class FirstTimeYieldReportJobDataDao {

    public FirstTimeYieldReportJobData addFtyRptDataJob(FirstTimeYieldReportJobData ftyRptJobData) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(ftyRptJobData);
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
        return ftyRptJobData;
    }

    public void deleteFtyRptDataJob(int ftyRptDataId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        // Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            FirstTimeYieldReportJobData ftyRptJobData = (FirstTimeYieldReportJobData) session.load(FirstTimeYieldReportJobData.class, new Integer(ftyRptDataId));
            session.delete(ftyRptJobData);
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

    public void updateFtyRptDataJob(FirstTimeYieldReportJobData ftyRptJobData) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(ftyRptJobData);
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
    public List<FirstTimeYieldReportJobData> getAllFtyRptDataJobs() {
        List<FirstTimeYieldReportJobData> ftyRptJobDatas = new ArrayList<FirstTimeYieldReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ftyRptJobDatas = session.createQuery("from FirstTimeYieldReportJobData").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return ftyRptJobDatas;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<FirstTimeYieldReportJobData> getActiveFtyRptDataJobs(Integer ftyRptId) {
        List<FirstTimeYieldReportJobData> ftyRptJobDatas = new ArrayList<FirstTimeYieldReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            ftyRptJobDatas = session.createQuery("from FirstTimeYieldReportJobData where IsCompleted=0 and FtyReportId="+ftyRptId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return ftyRptJobDatas;
    }

    public FirstTimeYieldReportJobData createJob(String type, SysUser sysUser, Integer ftyRptId) {
    	FirstTimeYieldReportJobData ftyRptJobData = new FirstTimeYieldReportJobData();
        FirstTimeYieldReportDao ftyReportDao= new FirstTimeYieldReportDao();
        ftyRptJobData.setCreatedDate(new Date());
        ftyRptJobData.setStatus(Constants.JOB_RUNNING);
        if (sysUser != null) {
        	ftyRptJobData.setCreatedBy(sysUser);
        }
        if(ftyRptId !=null) {
        	ftyRptJobData.setFirstTimeYieldReport(ftyReportDao.getFirstTimeYieldReportById(ftyRptId));
        }else {
        	ftyRptJobData.setFirstTimeYieldReport(null);
        }

        ftyRptJobData.setIsCompleted(false);
        if ((Constants.Auto).equalsIgnoreCase(type)) {
        	ftyRptJobData.setIsAutoScheduler(true);
        } else {
        	ftyRptJobData.setIsAutoScheduler(false);
        }
        return this.addFtyRptDataJob(ftyRptJobData);
    }

    public void updateFtyJobStatus(FirstTimeYieldReportJobData ftyRptJobData, String status) {
    	ftyRptJobData.setIsCompleted(true);
    	ftyRptJobData.setLastModifiedDate(new Date());
    	ftyRptJobData.setStatus(status);
        this.updateFtyRptDataJob(ftyRptJobData);
    }

    public void ftyProcedureJob() {
        ftyProcedureJob(null);
    }


    public Map<String,Object> ftyProcedureJob(Integer ftyRptId) {

        Map<String,Object> map= new HashMap<>();
        FirstTimeYieldReportDao ftyReportDao = new FirstTimeYieldReportDao();
        FirstTimeYieldReportJobData ftyReportJobData = new FirstTimeYieldReportJobData();

        try {
        	System.out.println("Start of Refreshing First Time Yield Report data: " + new Date());
            List<FirstTimeYieldReport> lstFtyRpt = new ArrayList<>();
            if (ftyRptId == null || ftyRptId <= 0) {
            	lstFtyRpt = ftyReportDao.getAllFirstTimeYieldReports();
            } else {
            	lstFtyRpt.add(ftyReportDao.getFirstTimeYieldReportById(ftyRptId));
            }

            for (FirstTimeYieldReport ftyReport : lstFtyRpt) {
                if (this.getActiveFtyRptDataJobs(ftyReport.getFtyReportId()).size() <= 0) {
                	System.out.println("Refreshing First Time Yield Report data: " + ftyReport.getFtyReportName());
                	ftyReportJobData = this.createJob(Constants.Auto, null, ftyReport.getFtyReportId());
                    map=ftyReportDao.executeFtyRptProcedure(ftyReportJobData, ftyReport);
                } else {
                    map.put("statusMessage",Constants.JOB_NOT_EXECUTED );
                    map.put("status",Constants.ERROR);
                    ftyReportJobData = this.createJob(Constants.Auto, null, ftyReport.getFtyReportId());
                    this.updateFtyJobStatus(ftyReportJobData, Constants.JOB_NOT_EXECUTED);
                }
            }
        	System.out.println("End of Refreshing First Pass Yield Report data: " + new Date());
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return map;
    }
}
