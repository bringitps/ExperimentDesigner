package com.bringit.experiment.ui.form;

import com.bringit.experiment.bll.SysSmtp;
import com.bringit.experiment.bll.SysUser;
import com.bringit.experiment.dao.SmtpDao;
import com.bringit.experiment.ui.design.SmtpConfigDesign;
import com.bringit.experiment.util.Config;
import com.bringit.experiment.util.DateUtil;
import com.bringit.experiment.util.SmtpUtil;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;

import java.util.List;

/**
 * Created by vf-root on 14/4/17.
 */
public class SmtpConfigForm extends SmtpConfigDesign {

    SysSmtp sysSmtp;

    public SmtpConfigForm() {

        List<SysSmtp> lstEmailConfig = new SmtpDao().getSmtipConfig();
        if (lstEmailConfig.size() > 0) {
            sysSmtp = lstEmailConfig.get(0);
            this.primarySmtpGateway.setValue(sysSmtp.getSmtpGateway());
            this.smtpPort.setValue(sysSmtp.getSmtpPort().toString());
            this.smtpLoginUser.setValue(sysSmtp.getSmtpLoginUser());
            this.smtpLoginPassword.setValue(sysSmtp.getSmtpLoginPassword());
            this.pop3ServerAddress.setValue(sysSmtp.getPop3ServerAddress());
            this.pop3PortNumber.setValue(sysSmtp.getPop3PortNumber().toString());
            this.pop3UserName.setValue(sysSmtp.getPop3UserName());
            this.pop3UserPassword.setValue(sysSmtp.getPop3UserPassword());
            this.replyAddress.setValue(sysSmtp.getReplyAddress());
        }

        this.btnSave.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveSmtpDetails();
            }

        });

        this.btnSendEmail.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                sendEmail();
            }
        });
    }


    private void saveSmtpDetails() {

        if (validateRequiredFields()) {
            SysUser sessionUser = (SysUser) VaadinService.getCurrentRequest().getWrappedSession().getAttribute("UserSession");

            if (sysSmtp == null) {
                sysSmtp = new SysSmtp();
                sysSmtp.setCreatedDate(DateUtil.getDate());
                sysSmtp.setCreatedBy(sessionUser);
            }

            sysSmtp.setSmtpGateway(this.primarySmtpGateway.getValue());
            sysSmtp.setSmtpPort(Integer.parseInt(this.smtpPort.getValue()));
            sysSmtp.setSmtpLoginUser(this.smtpLoginUser.getValue());
            sysSmtp.setSmtpLoginPassword(this.smtpLoginPassword.getValue());

            sysSmtp.setPop3ServerAddress(this.pop3ServerAddress.getValue());
            sysSmtp.setPop3PortNumber(Integer.parseInt(this.pop3PortNumber.getValue()));
            sysSmtp.setPop3UserName(this.pop3UserName.getValue());
            sysSmtp.setPop3UserPassword(this.pop3UserPassword.getValue());
            sysSmtp.setReplyAddress(this.replyAddress.getValue());

            sysSmtp.setLastModifiedBy(sessionUser);
            sysSmtp.setModifiedDate(DateUtil.getDate());
            new SmtpDao().addSysSmtp(sysSmtp);

            System.out.println("sysSmtp = " + sysSmtp);
            if (sysSmtp.getIdSmtp() != null) {
                this.getUI().showNotification("Data Updated Successfully.", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                this.getUI().showNotification("Data Saved.", Notification.Type.HUMANIZED_MESSAGE);
            }
        } else if (!validateRequiredFields()) {
            this.getUI().showNotification("Please Enter the mandatory fields", Notification.Type.WARNING_MESSAGE);
        }
    }

    public void sendEmail() {

        if (!validateFieldsRequiredBeforeEmailSend()) {
            return ;
        }

        if (!validateRequiredFields()) {
            this.getUI().showNotification("Please Enter the mandatory fields", Notification.Type.WARNING_MESSAGE);
            return ;
        }

        SmtpDao smtpDao = new SmtpDao();
        List<SysSmtp> lstEmailConfig = smtpDao.getSmtipConfig();

        String emailAddress = this.emailTo.getValue();
        SmtpUtil smtpUtil = new SmtpUtil();
        Config configuration = new Config();
        String body =configuration.getProperty("body");
        String subject = configuration.getProperty("subject");

        if (lstEmailConfig.size() > 0) {
            Boolean status = smtpUtil.sendMail(body, emailAddress, subject, lstEmailConfig.get(0));
            if (status) {
                this.getUI().showNotification("Email Sent Successfully.", Notification.Type.HUMANIZED_MESSAGE);
            } else {
                this.getUI().showNotification("Error while sending email Please Enter the Correct SMTP Config Details", Notification.Type.ERROR_MESSAGE);
            }
        } else {
            this.getUI().showNotification("Please Check the SMTP Config Details", Notification.Type.ERROR_MESSAGE);
        }
    }

    private boolean validateRequiredFields() {

        if (primarySmtpGateway.getValue().isEmpty()) {
            return false;
        }
        if (smtpPort.getValue().isEmpty()) {
            return false;
        }
        if (smtpLoginUser.getValue().isEmpty()) {
            return false;
        }
        if (smtpLoginPassword.getValue().isEmpty()) {
            return false;
        }
//        if (pop3ServerAddress.getValue().isEmpty()) {
//            return false;
//        }
//        if (pop3PortNumber.getValue().isEmpty()) {
//            return false;
//        }
//        if (pop3UserName.getValue().isEmpty()) {
//            return false;
//        }
//        if (pop3UserPassword.getValue().isEmpty()) {
//            return false;
//        }
//        if (replyAddress.getValue().isEmpty()) {
//            return false;
//        }

        return true;
    }

    private boolean validateFieldsRequiredBeforeEmailSend() {

        if (!validateRequiredFields()) {
            this.getUI().showNotification("Please Enter the mandatory fields", Notification.Type.WARNING_MESSAGE);
            return false;
        }

        if (emailTo.getValue().isEmpty()) {
            this.getUI().showNotification("Please Enter the Email To field value", Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
