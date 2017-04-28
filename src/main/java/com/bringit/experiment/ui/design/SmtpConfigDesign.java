package com.bringit.experiment.ui.design;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/**
 * !! DO NOT EDIT THIS FILE !!
 * <p>
 * This class is generated by Vaadin Designer and will be overwritten.
 * <p>
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class SmtpConfigDesign extends VerticalLayout {
    protected TextField primarySmtpGateway;
    protected TextField smtpPort;
    protected TextField smtpLoginUser;
    protected PasswordField smtpLoginPassword;
    protected TextField pop3ServerAddress;
    protected TextField pop3PortNumber;
    protected TextField pop3UserName;
    protected PasswordField pop3UserPassword;
    protected TextField replyAddress;
    protected TextField emailTo;
    protected Button btnSave;
    protected Button btnCancel;
    protected Button btnSendEmail;

    public SmtpConfigDesign() {
        Design.read(this);
    }
}