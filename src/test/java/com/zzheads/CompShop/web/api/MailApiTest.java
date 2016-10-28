package com.zzheads.CompShop.web.api;

import com.zzheads.CompShop.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MailApiTest {

    @Test
    public void sendMail() throws Exception {
        MailApi.sendMail("zzheads@gmail.com", "zzheads@gmail.com", "Test", "Test");
    }

}