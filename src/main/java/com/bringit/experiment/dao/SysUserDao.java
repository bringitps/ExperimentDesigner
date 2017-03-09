package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;
import com.bringit.experiment.util.PasswordEncrypter;

public class SysUserDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addSysUser(SysUser sysUser) {


    	String encryptedPassword = PasswordEncrypter.encryptPassword(sysUser.getUserPass());
    	sysUser.setUserPass(encryptedPassword);
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(sysUser);
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

    public void deleteSysUser(int userId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            SysUser sysUser = (SysUser)session.load(SysUser.class, new Integer(userId));
            session.delete(sysUser);
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

    public void updateSysUser(SysUser sysUser) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(sysUser);
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

    @SuppressWarnings({ "unused", "unchecked" })
	public List<SysUser> getAllSysUsers() {
        List<SysUser> sysUsers = new ArrayList<SysUser>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            sysUsers = session.createQuery("from SysUser").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return sysUsers;
    }

    @SuppressWarnings("unused")
	public SysUser getUserById(int userId) {
        SysUser sysUser = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from SysUser where id = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", userId);
            sysUser = (SysUser) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return sysUser;
    }
    @SuppressWarnings("unused")
	public SysUser getUserByUserName(String userName) {
        SysUser sysUser = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from SysUser where UserName = :UserName";
            Query query = session.createQuery(queryString);
            query.setString("UserName", userName);
            sysUser = (SysUser) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return sysUser;
    }
	
    @SuppressWarnings("unused")
	public SysUser getUserByUserNameAndPassword(String userName, String password) {
    	
    	String encryptedPassword = PasswordEncrypter.encryptPassword(password);
    	
        SysUser sysUser = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from SysUser where UserName = :UserName and UserPass =:EncryptedPassword";
            Query query = session.createQuery(queryString);
            query.setString("UserName", userName);
            query.setString("EncryptedPassword", encryptedPassword);
            sysUser = (SysUser) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return sysUser;
    }
}
