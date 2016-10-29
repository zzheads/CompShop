package com.zzheads.CompShop.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataConfig {

    @Bean
    public BasicDataSource dataSource() throws URISyntaxException {

//          Use it for deploying

//        URI dbUri = new URI(System.getenv("DATABASE_URL"));
//        String username = dbUri.getUserInfo().split(":")[0];
//        String password = dbUri.getUserInfo().split(":")[1];
//        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        String dbUrl = "jdbc:postgresql://localhost:5432/compshop";
        String username = "zheads";
        String password = "xela1723014220";
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }
}
