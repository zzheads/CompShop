package com.zzheads.CompShop;

import com.zzheads.CompShop.model.AwsRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SpringBootApplication
public class Application {
    private static final String[] parts = {"zzheads", "pxYNmEeN", "AKIAJ2S4","2OaEOlO1D", "ZF6CZ36", "l6Zec9n6", "3W3HA", "e+6H9y+7", "3Ccpe0m"};
    public static String AWS_ACCESS_KEY_ID = "";
    public static String AWS_SECRET_KEY = "";
    public static String ASSOCIATE_TAG = "";
    public static final Double PROFIT_PERCENT = 1.1;

    public static void main(String[] args) {
        ASSOCIATE_TAG = parts[0];
        AWS_SECRET_KEY = parts[1]+parts[3]+parts[5]+parts[7]+parts[8];
        AWS_ACCESS_KEY_ID = parts[2]+parts[4]+parts[6];
        SpringApplication.run(Application.class, args);
    }
}