package com.bringit.experiment.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bringit.experiment.bll.ExperimentField;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UserRole;
import com.bringit.experiment.dal.HibernateUtil;

public class UserRoleDao {

	public void addUserRole(UserRole usrRole) {
    	
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		//Session session = HibernateUtil.openSession(dialectXmlFile);
        
        try {
            trns = session.beginTransaction();
            session.save(usrRole);
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

    public void deleteUserRole(int UserRoleId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            UserRole userRole = (UserRole)session.load(UserRole.class, new Integer(UserRoleId));
            session.delete(userRole);
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

    public void updateUserRole(UserRole usrRole) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            session.update(usrRole);
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
	public List<UserRole> getAllUserRoles() {
        List<UserRole> usrRoles = new ArrayList<UserRole>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
		// Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            usrRoles = session.createQuery("from UserRole").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return usrRoles;
    }

    @SuppressWarnings("unused")
	public UserRole getUserRoleById(int usrRoleId) {
        UserRole sysRole = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            String queryString = "from UserRole where UserRoleId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", usrRoleId);
            sysRole = (UserRole) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return sysRole;
    }
    @SuppressWarnings("unused")
	public UserRole getDefaultUserRoleByUserId(int usrRoleId) {
        UserRole sysRole = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            String queryString = "from UserRole where UserRoleId = :id and IsDefaultRole = 1";
            Query query = session.createQuery(queryString);
            query.setInteger("id", usrRoleId);
            sysRole = (UserRole) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return sysRole;
    }
    @SuppressWarnings({ "unchecked", "unused" })
	public List<UserRole> getUserRolesByUser(SysUser user) {
        List<UserRole> userRoles = new ArrayList<UserRole>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            userRoles = session.createQuery("from UserRole where UserId = :id").setInteger("id", user.getUserId()).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return userRoles;
    }
    
    @SuppressWarnings({ "unchecked", "unused" })
	public List<UserRole> getUserRolesByRole(SysRole role) {
        List<UserRole> userRoles = new ArrayList<UserRole>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            userRoles = session.createQuery("from userRoles where RoleId = :id").setInteger("id", role.getRoleId()).list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return userRoles;
    }
	
}
