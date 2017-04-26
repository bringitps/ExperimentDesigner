package com.bringit.experiment.util;

import com.bringit.experiment.bll.SysSmtp;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Administrator on 4/15/2017.
 */
public class SmtpUtil {

    public Boolean sendMail(String body, String sendTo, String subject,
            SysSmtp smtpConfig) {

        Boolean status = false;

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", smtpConfig.getSmtpGateway());
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");


            if (smtpConfig.getSmtpPort() == 465) {
                props.put("mail.smtp.socketFactory.port", smtpConfig.getSmtpPort());
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            } else {
                props.put("mail.smtp.port", smtpConfig.getSmtpPort());
            }


            Authenticator auth = new SMTPAuthenticator(smtpConfig.getSmtpLoginUser(), smtpConfig.getSmtpLoginPassword());
            Session mailSession = Session.getInstance(props, auth);
            mailSession.setDebug(true);

            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setContent(body, "text/html");
            message.setFrom(new InternetAddress(smtpConfig.getSmtpLoginUser()));

            message.setSubject(subject);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            message.setReplyTo(new Address[] { new InternetAddress(smtpConfig.getReplyAddress()) });

            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            status = true;
        } catch (Exception ex) {
            status = false;
            ex.printStackTrace();
        }

        return status;
    }


    private class SMTPAuthenticator extends Authenticator {

        private String username;
        private String password;

        public SMTPAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}
