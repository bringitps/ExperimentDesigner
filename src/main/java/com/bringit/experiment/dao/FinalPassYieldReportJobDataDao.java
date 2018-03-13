package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.FinalPassYieldReport;
import com.bringit.experiment.bll.FinalPassYieldReportJobData;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Constants;

public class FinalPassYieldReportJobDataDao {

    public FinalPassYieldReportJobData addFnyRptDataJob(FinalPassYieldReportJobData fnyRptJobData) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(fnyRptJobData);
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
        return fnyRptJobData;
    }

    public void deleteFnyRptDataJob(int fnyRptDataId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        // Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            FinalPassYieldReportJobData fnyRptJobData = (FinalPassYieldReportJobData) session.load(FinalPassYieldReportJobData.class, new Integer(fnyRptDataId));
            session.delete(fnyRptJobData);
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

    public void updateFnyRptDataJob(FinalPassYieldReportJobData fnyRptJobData) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(fnyRptJobData);
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
    public List<FinalPassYieldReportJobData> getAllFnyRptDataJobs() {
        List<FinalPassYieldReportJobData> fnyRptJobDatas = new ArrayList<FinalPassYieldReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            fnyRptJobDatas = session.createQuery("from FinalPassYieldReportJobData").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return fnyRptJobDatas;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<FinalPassYieldReportJobData> getActiveFnyRptDataJobs(Integer fnyRptId) {
        List<FinalPassYieldReportJobData> fnyRptJobDatas = new ArrayList<FinalPassYieldReportJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            fnyRptJobDatas = session.createQuery("from FinalPassYieldReportJobData where IsCompleted=0 and FnyReportId="+fnyRptId).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return fnyRptJobDatas;
    }

    public FinalPassYieldReportJobData createJob(String type, SysUser sysUser, Integer fnyRptId) {
    	FinalPassYieldReportJobData fnyRptJobData = new FinalPassYieldReportJobData();
        FinalPassYieldReportDao fnyReportDao= new FinalPassYieldReportDao();
        fnyRptJobData.setCreatedDate(new Date());
        fnyRptJobData.setStatus(Constants.JOB_RUNNING);
        if (sysUser != null) {
        	fnyRptJobData.setCreatedBy(sysUser);
        }
        if(fnyRptId !=null) {
        	fnyRptJobData.setFinalPassYieldReport(fnyReportDao.getFinalPassYieldReportById(fnyRptId));
        }else {
        	fnyRptJobData.setFinalPassYieldReport(null);
        }

        fnyRptJobData.setIsCompleted(false);
        if ((Constants.Auto).equalsIgnoreCase(type)) {
        	fnyRptJobData.setIsAutoScheduler(true);
        } else {
        	fnyRptJobData.setIsAutoScheduler(false);
        }
        return this.addFnyRptDataJob(fnyRptJobData);
    }

    public void updateFnyJobStatus(FinalPassYieldReportJobData fnyRptJobData, String status) {
    	fnyRptJobData.setIsCompleted(true);
    	fnyRptJobData.setLastModifiedDate(new Date());
    	fnyRptJobData.setStatus(status);
        this.updateFnyRptDataJob(fnyRptJobData);
    }

    public void fnyProcedureJob() {
        fnyProcedureJob(null);
    }


    public Map<String,Object> fnyProcedureJob(Integer fnyRptId) {

        Map<String,Object> map= new HashMap<>();
        FinalPassYieldReportDao fnyReportDao = new FinalPassYieldReportDao();
        FinalPassYieldReportJobData fnyReportJobData = new FinalPassYieldReportJobData();

        try {
        	System.out.println("Start of Refreshing Final Pass Yield Report data: " + new Date());
            List<FinalPassYieldReport> lstFnyRpt = new ArrayList<>();
            if (fnyRptId == null || fnyRptId <= 0) {
            	lstFnyRpt = fnyReportDao.getAllFinalPassYieldReports();
            } else {
            	lstFnyRpt.add(fnyReportDao.getFinalPassYieldReportById(fnyRptId));
            }

            for (FinalPassYieldReport fnyReport : lstFnyRpt) {
                if (this.getActiveFnyRptDataJobs(fnyReport.getFnyReportId()).size() <= 0) {
                	System.out.println("Refreshing Final Pass Yield Report data: " + fnyReport.getFnyReportName());
                	fnyReportJobData = this.createJob(Constants.Auto, null, fnyReport.getFnyReportId());
                    map=fnyReportDao.executeFnyRptProcedure(fnyReportJobData, fnyReport);
                } else {
                    map.put("statusMessage",Constants.JOB_NOT_EXECUTED );
                    map.put("status",Constants.ERROR);
                    fnyReportJobData = this.createJob(Constants.Auto, null, fnyReport.getFnyReportId());
                    this.updateFnyJobStatus(fnyReportJobData, Constants.JOB_NOT_EXECUTED);
                }
            }
        	System.out.println("End of Refreshing Final Pass Yield Report data: " + new Date());
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return map;
    }
}
