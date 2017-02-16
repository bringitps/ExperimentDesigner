package com.bringit.experiment;

import java.util.Arrays;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
public class WebApplication extends UI {

	private final static String[] views = {"Login"};

	private final static String PKG = "com.bringit.experiment.ui.";

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		try {
			UI.getCurrent().setContent((Component) Class.forName(PKG + "Login").newInstance());
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		/*
		new Navigator(this, this);
		getNavigator().setErrorView(TemplateListingView.class);
		// if (getNavigator().getState().isEmpty()) {
		//
		// }

		getNavigator().navigateTo("Login");*/
	}

	public static class MyViewChangerView extends VerticalLayout implements View {

		@Override
		public void enter(ViewChangeEvent event) {
			try {
				UI.getCurrent().setContent((Component) Class.forName(PKG + event.getViewName()).newInstance());
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static class TemplateListingView extends VerticalLayout implements View {

		public TemplateListingView() {
			setMargin(true);
		}

		@Override
		public void enter(ViewChangeEvent event) {
			ListSelect list = new ListSelect();
			addComponent(list);
			Arrays.asList(views).forEach(list::addItem);
			list.addValueChangeListener(valueChange -> UI.getCurrent().getNavigator()
					.navigateTo((String) valueChange.getProperty().getValue()));
		}

	}

	@WebServlet(urlPatterns = "/*", name = "WebApplicationServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = WebApplication.class, productionMode = true)
	public static class WebApplicationServlet extends VaadinServlet {
	}
	
}
