package com.zzheads.CompShop.web.api;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Scope("request")
@Controller
public class MailApi {

    public static final String GMAIL_USERNAME = "zdeliveryshop";
    public static final String GMAIL_PASSWORD = "zdelivery1234567890";

    public static void sendMail(String to, String from, String body, String subject) {
        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GMAIL_USERNAME, GMAIL_PASSWORD);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session); // email message
            message.setFrom(new InternetAddress(from)); // setting header fields
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject); // subject line
            message.setContent(body, "text/html; charset=UTF-8");

            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
