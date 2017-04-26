package com.bringit.experiment.dao;

import com.bringit.experiment.bll.SysSmtp;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.SmtpUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vf-root on 14/4/17.
 */
public class SmtpDao {

    public void addSysSmtp(SysSmtp sysSmtp) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);

        try {
            trns = session.beginTransaction();
            sysSmtp.setModifiedDate(new Date());
            List<SysSmtp> lstEmailConfig = getSmtipConfig();
            if (lstEmailConfig.size() == 0) {
                session.save(sysSmtp);
            } else {
                SysSmtp sysSmtpConfig = lstEmailConfig.get(0);
                sysSmtp.setIdSmtp(sysSmtpConfig.getIdSmtp());
                session.update(sysSmtp);
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

    public void updateSmtpConfig(SysSmtp sysSmtp) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            sysSmtp.setModifiedDate(new Date());
            trns = session.beginTransaction();
            session.update(sysSmtp);
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
    public List<SysSmtp> getSmtipConfig() {
        List<SysSmtp> emailConfig = new ArrayList<SysSmtp>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            emailConfig = session.createQuery("from SysSmtp").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return emailConfig;
    }

    @SuppressWarnings("unused")
    public SysSmtp getSmtpById(int idSmtp) {
        SysSmtp emailConfig = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from SysSmtp where idSmtp = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", idSmtp);
            emailConfig = (SysSmtp) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return emailConfig;
    }

    public Map<String, Object> sendEmailToCM(String email) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "success");

        SmtpUtil smtpUtil = new SmtpUtil();
        List<SysSmtp> lstEmailConfig = getSmtipConfig();
        if (lstEmailConfig.size() > 0) {
            smtpUtil.sendMail("Email Sent To CM Successfully", email, "Email To CM", lstEmailConfig.get(0));
        } else {
            map.put("status", "error");
            map.put("message", "Please Update SMTP Config Details");
        }
        return map;
    }
}
