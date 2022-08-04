package com.della;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@MapperScan("com.della.mapper")
@EnableWebSecurity
public class Application {
    public static void main(String[] args) {
        /**
         * 权限框架会内置提供一个登录页面
         * 如果需要使用数据库，那么需要设置secutry 配置信息
         */
        SpringApplication.run(Application.class,args);
    }
}
