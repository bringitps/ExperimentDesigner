package com.bringit.experiment.ui.form;

import java.util.List;

import javax.security.auth.login.LoginException;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UserRole;
import com.bringit.experiment.dao.SysRoleDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.UserRoleDao;
import com.bringit.experiment.ldap.ActiveDirectoryAuthentication;
import com.bringit.experiment.ui.design.LoginFormDesign;
import com.vaadin.event.ShortcutAction.KeyCode;

public class LoginForm extends LoginFormDesign {
	
	private WebApplication webApplication;

	public LoginForm(WebApplication webApplication) {
	    this.webApplication = webApplication;
	    lblLoginError.setVisible(false);
	    btnLogin.setClickShortcut(KeyCode.ENTER);
	    btnLogin.addClickListener(e->this.doLogin()); 
	    txtPassword.addFocusListener(e->this.lblLoginError.setVisible(false));
		cbxAuthOption.select("WINDOWS AUTHENTICATION");
	}

	private void doLogin() {

		/*
		//Root Password
		if(txtUsername.getValue().equals("sys_admin") && txtPassword.getValue().equals("br1n61T2oI7"))
		{
			//Create User Session
			SysUser sysAdminUser = new SysUser();
			sysAdminUser.setUserId(-1);
			sysAdminUser.setUserName(txtUsername.getValue());
			sysAdminUser.setUserPass(txtPassword.getValue());
			webApplication.createUserSession(sysAdminUser);
		}
		else
		{*/
		String authOption =  cbxAuthOption.getValue() != null ? (String) cbxAuthOption.getValue() : "";
		if (authOption.toUpperCase().equals("WINDOWS AUTHENTICATION")){
			ActiveDirectoryAuthentication adAuth = new ActiveDirectoryAuthentication();
			try {
				if(adAuth.authenticate(txtUsername.getValue(), txtPassword.getValue())){
					//we need to check if the user is registered to use the app 
					//SysUser loggedUser = new SysUserDao().getUserByUserNameAndPassword(txtUsername.getValue(), txtPassword.getValue());
					SysUser loggedUser = new SysUserDao().getUserByUserName(txtUsername.getValue());
					if(loggedUser != null)
						webApplication.createUserSession(loggedUser);
					else
					{
						List<SysRole> adDefaultSysRoles = new SysRoleDao().getAdDefaultSysRoles();
						if(adDefaultSysRoles != null && adDefaultSysRoles.size() > 0)
						{
							//Register User and Assign AD Default Roles
							loggedUser = new SysUser();
							loggedUser.setIsActiveDirectoryUser(true);
							loggedUser.setUserName(txtUsername.getValue());
							loggedUser.setUserPass("");
							
							new SysUserDao().addSysUser(loggedUser);
							SysUser newAdUser = new SysUserDao().getUserByUserName(txtUsername.getValue());
							
							for(int i=0; i<adDefaultSysRoles.size(); i++)
							{
								UserRole newAdUserRole = new UserRole();
								newAdUserRole.setSysRole(adDefaultSysRoles.get(i));
								newAdUserRole.setSysUser(newAdUser);
								if(i==0)
									newAdUserRole.setDefaultRole(true);
								new UserRoleDao().addUserRole(newAdUserRole);
							}
							
							webApplication.createUserSession(newAdUser);							
						}
						else
						{
							lblLoginError.setValue("Default Roles for Active Directory Users not found. Please contact your System Administrator.");
							lblLoginError.setVisible(true);
						}
					}							
				}
			} catch (LoginException e) {
			    lblLoginError.setValue(e.getMessage());
				lblLoginError.setVisible(true);
				e.printStackTrace();
			}
		}else{
			SysUser loggedUser = new SysUserDao().getUserByUserNameAndPassword(txtUsername.getValue(), txtPassword.getValue());
			if(loggedUser != null)
				webApplication.createUserSession(loggedUser);
			else
				lblLoginError.setVisible(true);
		}

		//}
	}
	
}
