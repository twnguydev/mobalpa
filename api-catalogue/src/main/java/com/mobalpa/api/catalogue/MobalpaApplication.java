package com.mobalpa.api.catalogue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MobalpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobalpaApplication.class, args);
    }
}