package com.zzheads.CompShop.web.api;

import com.google.gson.Gson;
import com.zzheads.CompShop.model.User;
import com.zzheads.CompShop.service.UserService;
import com.zzheads.CompShop.utils.EmailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Scope("request")
@Controller
public class MailApi {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


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

    @RequestMapping (path = "/sendagain", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody public String sendMailAgain (@RequestBody String email) {
        Gson gson = new Gson();
        User user = userService.findByName(email);
        if (user != null) {
            String confirm = passwordEncoder.encode(user.getId().toString() + user.getUsername() + user.getPassword());
            String body = EmailTemplate.getHtmlCode(user.getUsername(), confirm);
            MailApi.sendMail(user.getUsername(), "www.zdelivery.ru", body, "Пожалуйста активируйте ваш аккаунт на сайте zdelivery.ru");
            return (gson.toJson("success"));
        }
        return gson.toJson("failure");
    }
}
