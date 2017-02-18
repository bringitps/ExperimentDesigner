package com.bringit.experiment.ui.form;

import java.io.File;
import java.util.List;

import javax.swing.Icon;

import com.bringit.experiment.WebApplication;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.ui.design.MainFormDesign;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class MainForm extends MainFormDesign {
	
	private WebApplication webApplication;
	SysUser sysUserSession = (SysUser)VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");
	
	
	public MainForm(WebApplication webApplication) {
	    this.webApplication = webApplication;

	    for (Object id : treeMainMenu.rootItemIds()) {
	    	treeMainMenu.expandItemsRecursively(id);
        }
	    
	    mnuSession.getItems().get(0).setIcon(FontAwesome.USER);
	    mnuSession.getItems().get(0).getChildren().get(0).setIcon(FontAwesome.GEAR);
	    mnuSession.getItems().get(0).getChildren().get(1).setIcon(FontAwesome.SIGN_OUT);
	    mnuSession.getItems().get(0).getChildren().get(1).setCommand(signOutCmd);
	    
	 // Find the application directory
	    String basepath = VaadinService.getCurrent()
	                      .getBaseDirectory().getAbsolutePath();

	    // Image as a file resource
	    FileResource resource = new FileResource(new File(basepath +
	                            "/WEB-INF/classes/images/logo_bring_2.png"));

	    // Show the image in the application
	    Image image = new Image("", resource);
	    
	    cssLayTopLeft.addComponent(image);
	    
	    //mnuSession.addItem("Quit Drinking", null, null);
	    
	    //mnuSession.addItem("Logged User", FontAwesome.USER, logoutCmd);
	    
		//this.mnuSession.addItem("", command)
	    //lblLoginError.setVisible(false);
	    //btnLogin.setClickShortcut(KeyCode.ENTER);
	    //btnLogin.addClickListener(e->this.doLogin());
	    
	}


	MenuBar.Command signOutCmd = new MenuBar.Command() {
        public void menuSelected(MenuItem selectedItem) {
        	webApplication.closeUserSession();
        }

    };
    
    public void setLoggedUserName(String loggedUserName)
    {
    	mnuSession.getItems().get(0).setText(loggedUserName);
    }
}
