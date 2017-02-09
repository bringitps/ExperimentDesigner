package com.bringit.experiment.dal;

import java.util.Date;

import com.bringit.experiment.bll.Experiment;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.ExperimentDao;
import com.bringit.experiment.dao.SysUserDao;

public class Test {
	 
	 public static int createSysUserTest()
	 {
	     SysUserDao sysUserDao = new SysUserDao();
		 
	     SysUser sysUser = new SysUser();
		 sysUser.setUserName("BringIT");
		 sysUser.setUserPass("123456");
		 
		 sysUserDao.addSysUser(sysUser);
		 
		 return sysUser.getUserId();
	 }
	 
	 public static int createExperimentTest()
	 {
		 SysUserDao sysUserDao = new SysUserDao();
		 
	     SysUser sysUser = new SysUser();
		 sysUser.setUserName("Edgar Beltrán");
		 sysUser.setUserPass("123456");
		 
		 sysUserDao.addSysUser(sysUser);
 
	     SysUser sysUserErik = new SysUser();
	     sysUserErik.setUserName("Erik Tete Samario");
	     sysUserErik.setUserPass("123456");
		 
		 sysUserDao.addSysUser(sysUserErik);
		 
		 
		 ExperimentDao experimentDao = new ExperimentDao();
		 
		 Experiment experiment = new Experiment();
		 experiment.setExpComments("Test Experiment Comments");
		 experiment.setExpInstructions("Test Experiment Instructions");
		 experiment.setExpIsActive(true);
		 experiment.setExpName("Test Experiment Name");
		 experiment.setModifiedDate(new Date());
		 experiment.setCreatedBy(sysUser);
		 experiment.setLastModifiedBy(sysUserErik);
		 
		 experimentDao.addExperiment(experiment);
		 return experiment.getExpId();
		 
	 }
	
}
