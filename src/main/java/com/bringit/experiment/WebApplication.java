package com.bringit.experiment;

import java.io.File;
import java.util.Arrays;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import com.bringit.experiment.bll.JobExecutionRepeat;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.ExecuteQueryDao;
import com.bringit.experiment.dao.JobExecutionRepeatDao;
import com.bringit.experiment.dao.SysUserDao;
import com.bringit.experiment.ui.form.LoginForm;
import com.bringit.experiment.ui.form.MainForm;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.PasswordEncrypter;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@Push
public class WebApplication extends UI {

	private Grid grid = new Grid();
	
	
	private final static String[] views = {"LoginFormDesign"};

	private final static String PKG = "com.bringit.experiment.ui.design.";
	
	private LoginForm loginForm = new LoginForm(this);
	private MainForm mainForm = new MainForm(this);
	
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		buildContent();
	}

	private void buildContent()
	{
		SysUser sysUserSession = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
		
		//--- Loading BringIT Logo ---//
	    String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	    FileResource imgResource = new FileResource(new File(basePath + "/WEB-INF/classes/images/logo_bring_2.png"));
	    Image imgLogo = new Image("", imgResource);
	    
		final VerticalLayout headerLayout = new VerticalLayout();
		final HorizontalLayout contentLayout = new HorizontalLayout((sysUserSession != null) ? mainForm : loginForm);
		final VerticalLayout footerLayout = new VerticalLayout(new Label("BringIT | Professional Services"));
		
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
			mnuSession.addItem(sysUserSession.getUserName(), FontAwesome.USER, null);
			mnuSession.getItems().get(0).addItem("My Account", FontAwesome.GEAR, null);		//Phase 2 
			mnuSession.getItems().get(0).addItem("Logout", FontAwesome.SIGN_OUT, signOutCmd);
			
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
		}
		else
		{
			//--- Check if it's a first time launching the system ---//
			if(new SysUserDao().getAllSysUsers().size() <= 0)
			{
				SysUser rootUser = new SysUser();
				rootUser.setUserName("root");
				rootUser.setUserPass("1234");
				
				new SysUserDao().addSysUser(rootUser);

				Config configuration = new Config();
				if(configuration.getProperty("dbms").equals("sqlserver"))
				{
					new ExecuteQueryDao().executeQueryFile(basePath + "/WEB-INF/classes/sql/SQL_SERVER_CREATE_VwExperiment_STMT.sql");
					new ExecuteQueryDao().executeQueryFile(basePath + "/WEB-INF/classes/sql/SQL_SERVER_CREATE_VwXmlTemplate_STMT.sql");
				}
				
				JobExecutionRepeat jobExecRepeat15min = new JobExecutionRepeat();
				jobExecRepeat15min.setJobExecRepeatName("15 minutes");
				jobExecRepeat15min.setJobExecRepeatMilliseconds(90000);
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
		
		final VerticalLayout mainLayout = new VerticalLayout(headerLayout, contentLayout, footerLayout);
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
    
	public void createUserSession(SysUser sysUser) 
	{
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("UserSession", sysUser);
		buildContent();
	}
	
	
	public void closeUserSession()
	{
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("UserSession", null);
		mainForm = new MainForm(this);
		buildContent();
	}
		
	@WebServlet(urlPatterns = "/*", name = "WebApplicationServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = WebApplication.class, productionMode = false)
	public static class WebApplicationServlet extends VaadinServlet {
	}
	
}
