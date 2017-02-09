package com.bringit.experiment.dal;

import com.bringit.experiment.bll.SysUser;
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
}
