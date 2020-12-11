package com.hospital.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
//        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");
    }

}
