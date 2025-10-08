package com.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CrmApplication extends SpringBootServletInitializer {

    // This method is required for WAR deployment
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CrmApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
        System.out.println("Project is Running...!");
    }
}
