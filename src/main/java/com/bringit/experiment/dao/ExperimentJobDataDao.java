package com.bringit.experiment.dao;

import com.bringit.experiment.bll.ExperimentJobData;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Constants;
import com.bringit.experiment.util.HibernateXmlConfigSupport;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExperimentJobDataDao {

    private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();

    public ExperimentJobData addExperimentJob(ExperimentJobData exp) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.save(exp);
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
        return exp;
    }

    public void deleteExperimentJob(int expId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        // Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            ExperimentJobData exp = (ExperimentJobData) session.load(ExperimentJobData.class, new Integer(expId));
            session.delete(exp);
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

    public void updateExperimentJob(ExperimentJobData exp) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(exp);
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
    public List<ExperimentJobData> getAllExperimentJobs() {
        List<ExperimentJobData> experimentJobDatas = new ArrayList<ExperimentJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            experimentJobDatas = session.createQuery("from ExperimentJobData").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentJobDatas;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public List<ExperimentJobData> getActiveExperimentJobs() {
        List<ExperimentJobData> experimentJobDatas = new ArrayList<ExperimentJobData>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            experimentJobDatas = session.createQuery("from ExperimentJobData where IsCompleted =0").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return experimentJobDatas;
    }

    public ExperimentJobData createJob(String type, SysUser sysUser) {
        ExperimentJobData experimentJobData = new ExperimentJobData();
        experimentJobData.setCreatedDate(new Date());
        experimentJobData.setStatus(Constants.JOB_RUNNING);
        if (sysUser != null) {
            experimentJobData.setCreatedBy(sysUser);
        }
        experimentJobData.setIsCompleted(false);
        if ((Constants.Auto).equalsIgnoreCase(type)) {
            experimentJobData.setIsAutoScheduler(true);
        } else {
            experimentJobData.setIsAutoScheduler(false);
        }
        return this.addExperimentJob(experimentJobData);
    }

    public void updateExperimentJobStatus(ExperimentJobData experimentJobData, String status) {
        experimentJobData.setIsCompleted(true);
        experimentJobData.setLastModifiedDate(new Date());
        experimentJobData.setStatus(status);
        this.updateExperimentJob(experimentJobData);
    }


    public void experimentProcedureJob() {
        experimentProcedureJob(null);
    }

    public void experimentProcedureJob(Integer expId) {
        ExperimentDao experimentDao = new ExperimentDao();
        ExperimentJobData experimentJobData = new ExperimentJobData();

        try {
            if (this.getActiveExperimentJobs().size() <= 0) {
                experimentJobData = this.createJob(Constants.Auto, null);
                experimentDao.executeExperimentProcedure(experimentJobData, expId);
            } else {
                experimentJobData = this.createJob(Constants.Auto, null);
                this.updateExperimentJobStatus(experimentJobData, Constants.JOB_NOT_EXECUTED);
            }
        } catch (Exception ex) {
            this.updateExperimentJobStatus(experimentJobData, Constants.JOB_EXCEPTION);
            ex.printStackTrace();
        }
    }

}
