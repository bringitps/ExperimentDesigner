package com.bringit.experiment.ui.form;

import java.util.ArrayList;
import java.util.List;

import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UserRole;
import com.bringit.experiment.dao.SysRoleDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.UserRoleDao;
import com.bringit.experiment.ui.design.SysUserSettingsDesign;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class SysUserSettingsForm extends SysUserSettingsDesign {

	SysUser sysUser;
	
	public SysUserSettingsForm()
	{
		sysUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		
		this.txtFullName.setValue(sysUser.getUserFullName() != null ? sysUser.getUserFullName() : "");
		this.txtUsername.setValue(sysUser.getUserName() != null ? sysUser.getUserName() : "");
		this.txtEmail.setValue(sysUser.getUserEmail() != null ? sysUser.getUserEmail() : "");
		
		if(!sysUser.getIsActiveDirectoryUser())
			this.txtPassword.setValue(sysUser.getUserPass() != null ? sysUser.getUserPass() : "");
		else
			this.txtPassword.setVisible(false);
		
		List<UserRole> userRoles = new UserRoleDao().getUserRolesByUser(sysUser);
		Integer defaultRoleId = -1;
		
		for(int j=0; userRoles != null && j<userRoles.size(); j++)
		{
			if(userRoles.get(j).isDefaultRole())
			{
				defaultRoleId = userRoles.get(j).getSysRole().getRoleId();
				break;
			}
		}
		
		for(int i=0; userRoles != null && i<userRoles.size(); i++)
		{
			this.cbxDefaultRole.addItem(userRoles.get(i).getSysRole().getRoleId());
			this.cbxDefaultRole.setItemCaption(userRoles.get(i).getSysRole().getRoleId(), userRoles.get(i).getSysRole().getRoleName());
		}
		
		if(defaultRoleId != -1)
			this.cbxDefaultRole.select(defaultRoleId);		
		
		btnSave.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				onSave();
			}

		});

		btnCancel.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				closeModalWindow();
			}

		});
	}
	
	private void onSave()
	{
		sysUser.setUserEmail(txtEmail.getValue());
		sysUser.setUserFullName(txtFullName.getValue());
		if(txtFullName.getValue().isEmpty())
		{
			this.getUI().showNotification("Fullname must be set.", Type.WARNING_MESSAGE);
			return;
		}
		
		if(!sysUser.getIsActiveDirectoryUser())
		{
			if(txtPassword.getValue().isEmpty())
			{
				this.getUI().showNotification("Password must be set.", Type.WARNING_MESSAGE);
				return;
			}
			sysUser.setUserPass(txtPassword.getValue());
		}
		
		boolean isDefaultRoleChanged = true;
		
		new SysUserDao().updateSysUser(sysUser);

		List<UserRole> userRoles = new UserRoleDao().getUserRolesByUser(sysUser);
		Integer defaultRoleId = -1;
		SysRole defaultSysRole = null;
		
		if(cbxDefaultRole.getValue() != null)
			defaultRoleId = Integer.parseInt(cbxDefaultRole.getValue().toString());
		
		for(int i = 0; userRoles != null && i < userRoles.size(); i++)
		{
			if(defaultRoleId != -1 && defaultRoleId == userRoles.get(i).getSysRole().getRoleId())
			{
				if(userRoles.get(i).isDefaultRole())
					isDefaultRoleChanged = false;
				
				userRoles.get(i).setDefaultRole(true);
				defaultSysRole = new SysRoleDao().getRoleById(defaultRoleId);
			}
			else
				userRoles.get(i).setDefaultRole(false);
			
			new UserRoleDao().updateUserRole(userRoles.get(i));
		}	

		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("UserSession", sysUser);
		
		if(isDefaultRoleChanged)
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute("RoleSession", defaultSysRole);
		
		closeModalWindow();
	}

	private void closeModalWindow()
	{
		//Close Parent Modal Window
		((Window)this.getParent()).close();
	}
	
}
