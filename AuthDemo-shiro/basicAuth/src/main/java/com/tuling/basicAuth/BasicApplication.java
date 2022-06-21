package com.tuling.basicAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.tuling.basicAuth")
public class BasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class,args);
    }
}
