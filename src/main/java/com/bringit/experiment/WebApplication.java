package com.bringit.experiment;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.SysRole;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.bll.UserRole;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.SysRoleDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.dao.UserRoleDao;
import com.bringit.experiment.spc.FrequencyHistogramApplet;
import com.bringit.experiment.ui.form.LoginForm;
import com.bringit.experiment.ui.form.MainForm;
import com.bringit.experiment.ui.form.SysUserSettingsForm;
import com.bringit.experiment.ui.form.XmlDataFileProcessForm;
import com.bringit.experiment.util.Config;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Theme("mytheme")
@Push
//@Widgetset("AppWidgetset")
public class WebApplication extends UI {

	
	private Grid grid = new Grid();
	
	
	private final static String[] views = {"LoginFormDesign"};

	private final static String PKG = "com.bringit.experiment.ui.design.";
	
	private LoginForm loginForm = new LoginForm(this);
	private MainForm mainForm = new MainForm(this, null);
	
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		
       UI.getCurrent().setLocale(new Locale.Builder().setLanguage("en").setRegion("US").build());
		buildContent();
	}

	private void buildContent()
	{
		this.setContent(null);
		SysUser sysUserSession = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		SysRole sysRoleSession = (SysRole)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("RoleSession");
		
		//--- Loading BringIT Logo ---//
	    String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	    FileResource imgResource = new FileResource(new File(basePath + "/WEB-INF/classes/images/customer_logo.png"));
	    Image imgLogo = new Image("", imgResource);
	    
		final VerticalLayout headerLayout = new VerticalLayout();
		final HorizontalLayout contentLayout = new HorizontalLayout((sysUserSession != null) ? mainForm : loginForm);
		//final VerticalLayout footerLayout = new VerticalLayout(new Label("BringIT | Professional Services"));
		
		if (sysUserSession != null)
		{
			
			//--- Page Header Layout ---//
			GridLayout headerGrid = new GridLayout();
			headerGrid.setRows(1);
			headerGrid.setColumns(2);
			MarginInfo mg = new MarginInfo(false, true, false, true);
			headerGrid.setMargin(mg);
			
			headerGrid.setSizeFull();
			
			headerGrid.addComponent(imgLogo, 0, 0);
			headerGrid.setComponentAlignment(imgLogo, Alignment.BOTTOM_LEFT);
			
			MenuBar mnuSession = new MenuBar();
			mnuSession.setStyleName("borderless");
			mnuSession.addItem(sysUserSession.getUserFullName() + " - " + sysRoleSession.getRoleName(), FontAwesome.USER, null);
			mnuSession.getItems().get(0).addItem("My Account", FontAwesome.GEAR, sysUserSettingsCmd);		 
			mnuSession.getItems().get(0).addItem("Logout", FontAwesome.SIGN_OUT, signOutCmd);
			mnuSession.getItems().get(0).addSeparator();
			
			List<UserRole> availableUserRoles = new UserRoleDao().getUserRolesByUser(sysUserSession);
			for(int i=0; i<availableUserRoles.size(); i++)
			{
				if(availableUserRoles.get(i).getSysRole().getRoleId() != sysRoleSession.getRoleId())
					mnuSession.getItems().get(0).addItem("Change Role: " + availableUserRoles.get(i).getSysRole().getRoleName(), FontAwesome.EXCHANGE, changeUserRole);	
			}
			//mnuSession.getItems().get(0).addItem("Change Role 1: ", FontAwesome.EXCHANGE, changeUserRole);	
			//mnuSession.getItems().get(0).addItem("Change Role 2: ", FontAwesome.EXCHANGE, changeUserRole);	
			
			headerGrid.addComponent(mnuSession, 1, 0);
			headerGrid.setComponentAlignment(mnuSession, Alignment.BOTTOM_RIGHT);
			

			VerticalLayout headerNonVisibleSeparatorLayout = new VerticalLayout();
			headerNonVisibleSeparatorLayout.setWidth(100, Unit.PERCENTAGE);
			headerNonVisibleSeparatorLayout.setHeight(1, Unit.PIXELS); 
			headerNonVisibleSeparatorLayout.setStyleName("borderless");
			
			VerticalLayout headerBlueSeparatorLayout = new VerticalLayout();
			headerBlueSeparatorLayout.setWidth(100, Unit.PERCENTAGE);
			headerBlueSeparatorLayout.setHeight(10, Unit.PIXELS); 
			headerBlueSeparatorLayout.setStyleName("valo-menu-title");
			
			headerLayout.addComponent(headerGrid);
			headerLayout.addComponent(headerNonVisibleSeparatorLayout);
			headerLayout.addComponent(headerBlueSeparatorLayout);
			mainForm.updateMenuAccess();
		}
		else
		{
			//--- Check if it's a first time launching the system ---//
			if(new SysUserDao().getAllSysUsers().size() <= 0)
			{
				SysUser rootUser = new SysUser();
				rootUser.setUserName("root");
				rootUser.setUserPass("P4sst0f0rg3t!");
				rootUser.setUserFullName("Root User");
				rootUser.setIsActiveDirectoryUser(false);
				new SysUserDao().addSysUser(rootUser);
				
				SysRole adminRole = new SysRole();
				adminRole.setRoleName("sys_admin");
				adminRole.setRoleDescription("BringIT Sys Root User");
				
				SysRoleDao roleDao = new SysRoleDao();
				roleDao.addSysRole(adminRole);
				
				UserRole rootRole = new UserRole();
				rootRole.setSysRole(adminRole);
				rootRole.setSysUser(rootUser);
				rootRole.setDefaultRole(true);
				new UserRoleDao().addUserRole(rootRole);
				
				Config configuration = new Config();
				if(configuration.getProperty("dbms").equals("sqlserver"))
				{
					File sqlExecFolder = new File(basePath + "/WEB-INF/classes/sql");
					File[] sqlExecFiles = sqlExecFolder.listFiles();
					
					for (int i = 0; i < sqlExecFiles.length; i++) 
					{
					    if (sqlExecFiles[i].isFile()) 
					    {
					        System.out.println("Execution of SQL File " + sqlExecFiles[i].getName());
					        new ExecuteQueryDao().executeQueryFile(sqlExecFolder.getAbsolutePath() + "\\" + sqlExecFiles[i].getName());
					        System.out.println("SQL File Executed" + sqlExecFiles[i].getName());
						}
					}					
				}
				
				JobExecutionRepeat jobExecRepeat15min = new JobExecutionRepeat();
				jobExecRepeat15min.setJobExecRepeatName("15 minutes");
				jobExecRepeat15min.setJobExecRepeatMilliseconds(900000);
				new JobExecutionRepeatDao().addJobExecutionRepeat(jobExecRepeat15min);
			
				JobExecutionRepeat jobExecRepeat30min = new JobExecutionRepeat();
				jobExecRepeat30min.setJobExecRepeatName("30 minutes");
				jobExecRepeat30min.setJobExecRepeatMilliseconds(1800000);
				new JobExecutionRepeatDao().addJobExecutionRepeat(jobExecRepeat30min);
			
				JobExecutionRepeat jobExecRepeat45min = new JobExecutionRepeat();
				jobExecRepeat45min.setJobExecRepeatName("45 minutes");
				jobExecRepeat45min.setJobExecRepeatMilliseconds(2700000);
				new JobExecutionRepeatDao().addJobExecutionRepeat(jobExecRepeat45min);
			
				JobExecutionRepeat jobExecRepeat1hr = new JobExecutionRepeat();
				jobExecRepeat1hr.setJobExecRepeatName("1 hour");
				jobExecRepeat1hr.setJobExecRepeatMilliseconds(3600000);
				new JobExecutionRepeatDao().addJobExecutionRepeat(jobExecRepeat1hr);
			
				JobExecutionRepeat jobExecRepeat2hrs = new JobExecutionRepeat();
				jobExecRepeat2hrs.setJobExecRepeatName("2 hours");
				jobExecRepeat2hrs.setJobExecRepeatMilliseconds(7200000);
				new JobExecutionRepeatDao().addJobExecutionRepeat(jobExecRepeat2hrs);
			
				JobExecutionRepeat jobExecRepeat5hrs = new JobExecutionRepeat();
				jobExecRepeat5hrs.setJobExecRepeatName("5 hours");
				jobExecRepeat5hrs.setJobExecRepeatMilliseconds(18000000);
				new JobExecutionRepeatDao().addJobExecutionRepeat(jobExecRepeat5hrs);
			
				JobExecutionRepeat jobExecRepeat12hrs = new JobExecutionRepeat();
				jobExecRepeat12hrs.setJobExecRepeatName("12 hours");
				jobExecRepeat12hrs.setJobExecRepeatMilliseconds(43200000);
				new JobExecutionRepeatDao().addJobExecutionRepeat(jobExecRepeat12hrs);
			
				JobExecutionRepeat jobExecRepeat24hrs = new JobExecutionRepeat();
				jobExecRepeat24hrs.setJobExecRepeatName("24 hours");
				jobExecRepeat24hrs.setJobExecRepeatMilliseconds(86400000);
				new JobExecutionRepeatDao().addJobExecutionRepeat(jobExecRepeat24hrs);
				
			}
			
			GridLayout headerGrid = new GridLayout();
			headerGrid.setRows(1);
			headerGrid.setColumns(1);
			headerGrid.setSizeFull();
			headerGrid.addComponent(imgLogo, 0, 0);
			headerGrid.setComponentAlignment(imgLogo, Alignment.BOTTOM_CENTER);
			headerLayout.addComponent(headerGrid);
		}
		
		final VerticalLayout mainLayout = new VerticalLayout(headerLayout, contentLayout/*, footerLayout*/);
		mainLayout.setSizeFull();
		mainLayout.setExpandRatio(contentLayout, 1);
		contentLayout.setSizeFull();
		setContent(mainLayout);
		
	}

	MenuBar.Command signOutCmd = new MenuBar.Command() {
        public void menuSelected(MenuItem selectedItem) {
        	closeUserSession();
        }

    };


	MenuBar.Command changeUserRole = new MenuBar.Command() {
        public void menuSelected(MenuItem selectedItem) {
        	String changedRoleName = selectedItem.getText().split(":")[1].trim();
    		SysRole sysRole = new SysRoleDao().getRoleByRoleName(changedRoleName);
    		SysUser sysUser = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
    		changeUserRole(sysUser, sysRole);
        }
    };

	MenuBar.Command sysUserSettingsCmd = new MenuBar.Command() {
        public void menuSelected(MenuItem selectedItem) {

        	 Window sysUserSettingsModalWindow = new Window("User Settings");
        	 sysUserSettingsModalWindow.setModal(true);
        	 sysUserSettingsModalWindow.setResizable(false);
        	 sysUserSettingsModalWindow.setContent(new SysUserSettingsForm());
        	 sysUserSettingsModalWindow.setWidth(993, Unit.PIXELS);
        	 sysUserSettingsModalWindow.setHeight(310, Unit.PIXELS);
        	 sysUserSettingsModalWindow.center();
        	 sysUserSettingsModalWindow.addCloseListener(new Window.CloseListener() {
    			
    			@Override
    			public void windowClose(CloseEvent e) {
    				// TODO Auto-generated method stub
    				buildContent();
    			}
    		});
    		addWindow(sysUserSettingsModalWindow);
        }

    };
    
    public void changeUserRole(SysUser sysUser, SysRole sysRole)
    {
    	VaadinService.getCurrentRequest().getWrappedSession().setAttribute("UserSession", sysUser);
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("RoleSession", sysRole);
		mainForm = new MainForm(this, null);
		buildContent();
    }
    
	public void createUserSession(SysUser sysUser) 
	{
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("UserSession", sysUser);
		
		UserRole sysUserDefaultRole = new UserRoleDao().getDefaultUserRoleByUserId(sysUser.getUserId()); 
		if(sysUserDefaultRole == null)
		{
			List<UserRole> userRoles = new UserRoleDao().getUserRolesByUser(sysUser);
			if(userRoles != null)
				sysUserDefaultRole = userRoles.get(0);
		}
		
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("RoleSession", sysUserDefaultRole.getSysRole());
		
		buildContent();
	}
	
	public void reloadMainForm(String selectedForm)
	{
		mainForm = new MainForm(this, selectedForm);
		buildContent();
	}
	
	public void closeUserSession()
	{
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("UserSession", null);
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("RoleSession", null);
		mainForm = new MainForm(this, null);
		buildContent();
	}
	
	
	@WebServlet(urlPatterns = "/*", name = "WebApplicationServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = WebApplication.class, productionMode = true, widgetset = "com.bringit.experiment.widgetset.VaadinchartsWidgetset")
	public static class WebApplicationServlet extends VaadinServlet {
	}
	
}
