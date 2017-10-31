package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.FirstPassYieldInfoField;
import com.bringit.experiment.bll.FirstPassYieldReport;
import com.bringit.experiment.bll.FirstPassYieldReportJobData;
import com.bringit.experiment.bll.TargetReport;
import com.bringit.experiment.bll.TargetReportJobData;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.Constants;

public class FirstPassYieldReportDao {

		public void addFirstPassYieldReport(FirstPassYieldReport fpyReport) {

	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();

	        try {
	            trns = session.beginTransaction();
	            session.save(fpyReport);
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

	    public void deleteFirstPassYieldReport(int fpyReportId) {
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        try {
	            trns = session.beginTransaction();
	            FirstPassYieldReport fpyReport = (FirstPassYieldReport) session.load(FirstPassYieldReport.class, new Integer(fpyReportId));
	            session.delete(fpyReport);
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

	    public void updateFirstPassYieldReport(FirstPassYieldReport fpyReport) {
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        try {
	            trns = session.beginTransaction();
	            session.update(fpyReport);
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
	    public FirstPassYieldReport getFirstPassYieldReportById(int fpyReportId) {
	    	FirstPassYieldReport fpyReport = null;
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        try {
	            trns = session.beginTransaction();
	            String queryString = "from FirstPassYieldReport where FpyReportId = :id";
	            Query query = session.createQuery(queryString);
	            query.setInteger("id", fpyReportId);
	            fpyReport = (FirstPassYieldReport) query.uniqueResult();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return fpyReport;
	    }
	    
	    @SuppressWarnings({"unchecked", "unused"})
	    public List<FirstPassYieldReport> getAllFirstPassYieldReports() {
	        List<FirstPassYieldReport> fpyReports = new ArrayList<FirstPassYieldReport>();
	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        //Session session = HibernateUtil.openSession(dialectXmlFile);
	        try {
	            trns = session.beginTransaction();
	            fpyReports = session.createQuery("from FirstPassYieldReport where FpyReportIsActive = 'true'").list();
	        } catch (RuntimeException e) {
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return fpyReports;
	    }
	    
	    public boolean saveDBFpyRptTable(FirstPassYieldReport fpyRpt, List<FirstPassYieldInfoField> fpyInfoFields) {
	    	deleteDBFpyRptTable(fpyRpt);
	    	
	    	String query = null;
	        boolean successfulExecution = true;

	        List<String> dbRptTableCols = new ArrayList<String>();
	        List<String> dbRptTableTypes = new ArrayList<String>();

	        dbRptTableCols.add("fpy_experiment");
	        dbRptTableTypes.add("varchar(MAX)");
	        
	        dbRptTableCols.add("fpy_date_time");
	        dbRptTableTypes.add("datetime");

	        dbRptTableCols.add("fpy_serial_number");
	        dbRptTableTypes.add("varchar(MAX)");
	        
	        dbRptTableCols.add("fpy_result");
	        dbRptTableTypes.add("varchar(MAX)");	        
	        
	        for (int i = 0; i < fpyInfoFields.size(); i++) 
	        {           
	        	dbRptTableCols.add(fpyInfoFields.get(i).getExperimentField().getExpDbFieldNameId());
	            dbRptTableTypes.add(fpyInfoFields.get(i).getExperimentField().getExpFieldType());	             
	        }

	        Config configuration = new Config();


	        if (configuration.getProperty("dbms").equals("sqlserver")) {
	            String csvTableCols = "";
	            for (int i = 0; i < dbRptTableCols.size(); i++) {
	                csvTableCols += dbRptTableCols.get(i) + " " + dbRptTableTypes.get(i);

	                if ((i + 1) < dbRptTableCols.size())
	                    csvTableCols += ",";
	            }

	            query = " IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='" + fpyRpt.getFpyReportDbRptTableNameId() + "' AND xtype='U') ";
	            query += " CREATE TABLE " + fpyRpt.getFpyReportDbRptTableNameId();
	            query += " (FpyRecordId int NOT NULL PRIMARY KEY," + csvTableCols + ");";
	        } else
	            return false;

	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();

	        try {
	            trns = session.beginTransaction();
	            session.createSQLQuery(query).executeUpdate();
	            session.getTransaction().commit();
	        } catch (RuntimeException e) {
	            if (trns != null) {
	                trns.rollback();
	            }
	            successfulExecution = false;
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return successfulExecution;
	    }

	    public boolean deleteDBFpyRptTable(FirstPassYieldReport fpyRpt) {
	        String query = null;
	        boolean successfulExecution = true;

	        Config configuration = new Config();
	        if (configuration.getProperty("dbms").equals("sqlserver")) {
	            query = " IF EXISTS (SELECT * FROM sysobjects WHERE name='" + fpyRpt.getFpyReportDbRptTableNameId() + "' AND xtype='U') ";
	            query += " DROP TABLE " + fpyRpt.getFpyReportDbRptTableNameId() + ";";
	        } else
	            return false;

	        Transaction trns = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();

	        try {
	            trns = session.beginTransaction();
	            session.createSQLQuery(query).executeUpdate();
	            session.getTransaction().commit();
	        } catch (RuntimeException e) {
	            if (trns != null) {
	                trns.rollback();
	            }
	            successfulExecution = false;
	            e.printStackTrace();
	        } finally {
	            session.flush();
	            session.close();
	        }
	        return successfulExecution;
	    }    
	    
	    public Map<String,Object> executeFpyRptProcedure(FirstPassYieldReportJobData fpyRptJobData, FirstPassYieldReport fpyRpt) {
	        Map<String,Object> map= new HashMap<>();
	        FirstPassYieldReportJobDataDao firstPassYieldReportJobDataDao = new FirstPassYieldReportJobDataDao();
	        String statusMessage = Constants.JOB_FINISHED;

	        map.put("statusMessage", statusMessage);
	        map.put("status", Constants.SUCCESS);
	        try {


	            List<String> lstFpyRptBean;
	            lstFpyRptBean = new ArrayList<>();
	            lstFpyRptBean.add(fpyRpt.getFpyReportId().toString());
	            new ExecuteQueryDao().executeUpdateStoredProcedure("spFpyReportBuilder", lstFpyRptBean);

	            fpyRpt.setFpyReportDbRptTableLastUpdate(new Date());
	            this.updateFirstPassYieldReport(fpyRpt);

	            statusMessage = Constants.JOB_FINISHED;
	        } catch (Exception ex) {
	            statusMessage = Constants.JOB_EXCEPTION;

	            map.put("statusMessage", statusMessage);
	            map.put("status", Constants.ERROR);
	            ex.printStackTrace();
	        } finally {
	        	firstPassYieldReportJobDataDao.updateFpyJobStatus(fpyRptJobData, statusMessage);
	        }

	        return map;
	    }
	    
	    
	    	
}
