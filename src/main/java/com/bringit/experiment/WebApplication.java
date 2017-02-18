package com.bringit.experiment;

import java.util.Arrays;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.ui.form.LoginForm;
import com.bringit.experiment.ui.form.MainForm;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
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
		final VerticalLayout verticalLayout = new VerticalLayout();
		
		HorizontalLayout mainLayout = new HorizontalLayout((sysUserSession != null) ? mainForm : loginForm);
		if (sysUserSession != null) mainForm.setLoggedUserName(sysUserSession.getUserName());
				
		//mainLayout.setSpacing(true);
		mainLayout.setSizeFull();

		verticalLayout.addComponents(mainLayout);

		verticalLayout.setMargin(true);
	    setContent(verticalLayout);
	    
	    
	}
	
	public void createUserSession(SysUser sysUser) 
	{
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("UserSession", sysUser);
		buildContent();
	}
	
	public void closeUserSession()
	{
		VaadinService.getCurrentRequest().getWrappedSession().setAttribute("UserSession", null);
		buildContent();
	}
	
	@WebServlet(urlPatterns = "/*", name = "WebApplicationServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = WebApplication.class, productionMode = false)
	public static class WebApplicationServlet extends VaadinServlet {
	}
	
}
