package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.SystemSettings;
import com.bringit.experiment.dal.HibernateUtil;

public class SystemSettingsDao {

    public void addSystemSettings(SystemSettings systemSettings) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);

        try {
            trns = session.beginTransaction();
            systemSettings.setModifiedDate(new Date());
            List<SystemSettings> systemSettingsList = getSystemSettingsList();
            if (systemSettingsList.size() == 0) {
                session.save(systemSettings);
            } else {
                SystemSettings systemSettingsConfig = systemSettingsList.get(0);
                systemSettings.setSystemSettingsId(systemSettingsConfig.getSystemSettingsId());
                session.update(systemSettings);
            }
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

    public void updateSystemSettings(SystemSettings systemSettings) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
        	systemSettings.setModifiedDate(new Date());
            trns = session.beginTransaction();
            session.update(systemSettings);
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
    
    @SuppressWarnings({"unused"})
    public List<SystemSettings> getSystemSettingsList() {
        List<SystemSettings> systemSettingsList = new ArrayList<SystemSettings>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            systemSettingsList = session.createQuery("from SystemSettings").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return systemSettingsList;
    }

    @SuppressWarnings("unused")
    public SystemSettings getSystemSettingsById(int systemSettingsId) {
        SystemSettings systemSettings = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from SysSmtp where SystemSettingsId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", systemSettingsId);
            systemSettings = (SystemSettings) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return systemSettings;
    }
    
    @SuppressWarnings("unused")
    public SystemSettings getCurrentSystemSettings() {
        SystemSettings systemSettings = null;
        List<SystemSettings> systemSettingsList = new ArrayList<SystemSettings>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            systemSettingsList = session.createQuery("from SystemSettings").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        
        if(systemSettingsList != null && systemSettingsList.size() > 0)
        	systemSettings = systemSettingsList.get(0);
        else
        {
        	systemSettings = new SystemSettings();
        	systemSettings.setExperimentLabel("Experiment");
        	systemSettings.setExperimentPluralLabel("Experiments");
        	systemSettings.setExperimentTypeLabel("Experiment Type");
        	systemSettings.setExperimentTypePluralLabel("Experiment Types");
        }
        return systemSettings;
    }
   
}
