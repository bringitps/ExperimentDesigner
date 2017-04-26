package com.bringit.experiment.dao;

import com.bringit.experiment.bll.CmForSysRole;
import com.bringit.experiment.bll.ContractManufacturer;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.dal.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CmForSysRoleDao {


    public void addCmForSysRole(CmForSysRole cmForSysRole) {

        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.save(cmForSysRole);
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

    public void updateCmForSysRole(CmForSysRole cmForSysRole) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            session.update(cmForSysRole);
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


    public void deleteCmForSysRole(List<Integer> cmLst, int roleId) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        // Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String qur = "DELETE from CmForSysRole where contractManufacturer not in :cmList and userRole=:userRole";

            if (cmLst.size() == 0) {
                qur = "DELETE from CmForSysRole where userRole=:userRole";
            }

            Query q=session.createSQLQuery(qur);
            if (cmLst.size() > 0)
                q.setParameterList("cmList",cmLst);
            q.setParameter("userRole",roleId);
            q.executeUpdate();

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
    public List<CmForSysRole> getCmForSysRole() {
        List<CmForSysRole> cmForSysRole = new ArrayList<CmForSysRole>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            trns = session.beginTransaction();
            cmForSysRole = session.createQuery("from CmForSysRole").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return cmForSysRole;
    }

    @SuppressWarnings("unused")
    public CmForSysRole getCmForSysRoleById(int cmRoleId) {
        CmForSysRole cmForSysRole = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from CmForSysRole where cmRoleId = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", cmRoleId);
            cmForSysRole = (CmForSysRole) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return cmForSysRole;
    }

    public List<CmForSysRole> getListOfCmForSysRoleBysysRoleId(int sysRoleId) {
        List<CmForSysRole> cmForSysRole = new ArrayList<CmForSysRole>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from CmForSysRole where userRole.id = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", sysRoleId);
            cmForSysRole = query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return cmForSysRole;
    }

    public List<ContractManufacturer> getListOfCmForBysysRoleId(int sysRoleId) {
        List<ContractManufacturer> contractManufacturer = new ArrayList<ContractManufacturer>();
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            trns = session.beginTransaction();
            String queryString = "SELECT  contractManufacturer from CmForSysRole where userRole = :id";
            Query query = session.createQuery(queryString);
            query.setInteger("id", sysRoleId);
            contractManufacturer = query.list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return contractManufacturer;
    }


    public CmForSysRole getCmForSysRoleById(int cmRoleId, int cmId) {
        CmForSysRole cmForSysRole = null;
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Session session = HibernateUtil.openSession(dialectXmlFile);
        try {
            trns = session.beginTransaction();
            String queryString = "from CmForSysRole where userRole = :id and contractManufacturer=:contractManufacturer";
            Query query = session.createQuery(queryString);
            query.setInteger("id", cmRoleId);
            query.setInteger("contractManufacturer", cmId);
            cmForSysRole = (CmForSysRole) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return cmForSysRole;
    }

    public void addUpdateCmForSysRoleList(List<Integer> cmList, Integer roleId) {
        CmForSysRole cmForSysRole;
        ContractManufacturerDao contractManufacturerDao;
        SysRoleDao sysRoleDao= new SysRoleDao();
        SysRole sysRole=sysRoleDao.getRoleById(roleId);

       for(Integer cmId:cmList)
       {
           if(getCmForSysRoleById(sysRole.getRoleId(), cmId)==null) {
               cmForSysRole = new CmForSysRole();
               contractManufacturerDao = new ContractManufacturerDao();
               cmForSysRole.setContractManufacturer(contractManufacturerDao.getContractManufacturerById(cmId));
               cmForSysRole.setUserRole(sysRole);
               addCmForSysRole(cmForSysRole);
           }
           
       }
        deleteCmForSysRole(cmList,sysRole.getRoleId());
    }

    public static void main(String[] args) {
        CmForSysRoleDao cmForSysRoleDao= new CmForSysRoleDao();
        cmForSysRoleDao.getListOfCmForBysysRoleId(1002);
    }
}
