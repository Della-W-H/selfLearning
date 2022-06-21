package com.tuling.basicAuth.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Component
public class MyWebAppConfigurer implements WebMvcConfigurer {

    @Resource
    private LogInterceptor logInterceptor;
    @Resource
    private AuthInterceptor authInterceptor;
    //配置权限拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").order(20);
        registry.addInterceptor(logInterceptor).addPathPatterns("/**").order(10);
    }
    //简单配置启动页面
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("redirect:/index.html");
    }
}