package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.dal.HibernateUtil;
import com.bringit.experiment.util.HibernateXmlConfigSupport;

public class SysRoleDao {

	private String dialectXmlFile = new HibernateXmlConfigSupport().getHibernateDialectXmlConfigFile();
	
	public void addSysRole(SysRole sysRole) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(sysRole);
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

    public void deleteSysRole(int roleId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            SysRole sysRole = (SysRole)session.load(SysRole.class, new Integer(roleId));
            session.delete(sysRole);
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

    public void updateSysRole(SysRole sysRole) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            session.update(sysRole);
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
	public List<SysRole> getAllSysRoles() {
        List<SysRole> sysRoles = new ArrayList<SysRole>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            sysRoles = session.createQuery("from SysRole").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return sysRoles;
    }

    @SuppressWarnings("unused")
	public SysRole getRoleById(int roleId) {
        SysRole sysRole = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from SysRole where RoleId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", roleId);
            sysRole = (SysRole) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return sysRole;
    }
    @SuppressWarnings("unused")
	public SysRole getRoleByRoleName(String roleName) {
        SysRole sysRole = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from SysRole where RoleName = :RoleName";
            Query query = session.createQuery(queryString);
            query.setString("RoleName", roleName);
            sysRole = (SysRole) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return sysRole;
    }
	
    @SuppressWarnings({ "unused", "unchecked" })
   	public List<SysRole> getAdDefaultSysRoles() {
           List<SysRole> sysRoles = new ArrayList<SysRole>();
           Transaction trns = null;
           Session session = HibernateUtil.getSessionFactory().openSession();
   		// Session session = HibernateUtil.openSession(dialectXmlFile);
           try {
               trns = session.beginTransaction();
               sysRoles = session.createQuery("from SysRole where IsActiveDirectoryDefaultRole='true'").list();
           } catch (RuntimeException e) {
               e.printStackTrace();
           } finally {
               session.flush();
               session.close();
           }
           return sysRoles;
       }
    
}
