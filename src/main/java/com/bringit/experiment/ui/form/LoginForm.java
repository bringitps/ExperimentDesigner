package com.bringit.experiment.ui.form;

import javax.security.auth.login.LoginException;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.SysUserDao;
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
		String authOption = (String) cbxAuthOption.getValue();
		if (authOption.toUpperCase().equals("WINDOWS AUTHENTICATION")){
			ActiveDirectoryAuthentication adAuth = new ActiveDirectoryAuthentication();
			try {
				if(adAuth.authenticate(txtUsername.getValue(), txtPassword.getValue())){
					//we need to check if the user is registered to use the app 
					SysUser loggedUser = new SysUserDao().getUserByUserNameAndPassword(txtUsername.getValue(), txtPassword.getValue());
					if(loggedUser != null)
						webApplication.createUserSession(loggedUser);
					else
						//Register user automatically? or give them a point of contact to request access?
						lblLoginError.setValue("User exists on LDAP, but is not registered to use this app. Please contact Edgar Beltran.");
						lblLoginError.setVisible(true);
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
