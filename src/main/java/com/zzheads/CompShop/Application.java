package com.zzheads.CompShop;

import com.zzheads.CompShop.model.AwsRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SpringBootApplication
public class Application {
    public static String AWS_ACCESS_KEY_ID = "";
    public static String AWS_SECRET_KEY = "";
    public static final String ASSOCIATE_TAG = "zzheads";
    public static final Double PROFIT_PERCENT = 1.1;

    public static void main(String[] args) {
        File file = new File("awscreds.txt");
        try(FileReader reader = new FileReader(file)) {
            int lengthAccessKey = 20;
            char[] buffer = new char[(int)file.length()];
            reader.read(buffer);
            for (int i=0;i<lengthAccessKey;i++) {
                AWS_ACCESS_KEY_ID += buffer[i];
            }
            for (int i=lengthAccessKey;i<file.length();i++) {
                AWS_SECRET_KEY += buffer[i];
            }
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
        SpringApplication.run(Application.class, args);
    }
}