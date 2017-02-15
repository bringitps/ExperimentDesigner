package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class JobExecutionRepeatDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addJobExecutionRepeat(JobExecutionRepeat jobExecutionRepeat) {

        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(jobExecutionRepeat);
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

    public void deleteJobExecutionRepeat(int jobExecutionRepeatId) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            JobExecutionRepeat jobExecutionRepeat = (JobExecutionRepeat)session.load(JobExecutionRepeat.class, new Integer(jobExecutionRepeatId));
            session.delete(jobExecutionRepeat);
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

    public void updateJobExecutionRepeat(JobExecutionRepeat jobExecutionRepeat) {
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(jobExecutionRepeat);
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

    @SuppressWarnings({ "unchecked", "unused" })
	public List<JobExecutionRepeat> getAllJobExecutionRepeats() {
        List<JobExecutionRepeat> jobExecutionRepeats = new ArrayList<JobExecutionRepeat>();
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            jobExecutionRepeats = session.createQuery("from JobExecutionRepeat").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return jobExecutionRepeats;
    }

    @SuppressWarnings("unused")
	public JobExecutionRepeat getJobExecutionRepeatById(int jobId) {
    	JobExecutionRepeat jobExecutionRepeat = null;
        Transaction trns = null;
        Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from JobExecutionRepeat where JobExecRepeatId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", jobId);
            jobExecutionRepeat = (JobExecutionRepeat) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return jobExecutionRepeat;
    }
	
}
