package com.heima.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 权限框架的配置类
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true) //开启注解得权限管理
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 数据库的密码加密方式
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        //代表使用SecurityConfig 默认  BCrypt
        return new BCryptPasswordEncoder();
    }

    /**
     * Security 配置信息都可以写在这里面
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//进行认证配置
                .antMatchers("/login.html").permitAll() //设置哪些路径可以直接访问，不需要认证
                .antMatchers("/css/**").permitAll() //设置哪些路径可以直接访问，不需要认证
                .antMatchers("/img/**").permitAll() //设置哪些路径可以直接访问，不需要认证
                .antMatchers("/js/**").permitAll() //设置哪些路径可以直接访问，不需要认证
                .antMatchers("/plugins/**").permitAll() //设置哪些路径可以直接访问，不需要认证
                .anyRequest() // 任何请求
                .authenticated() // 都需要身份验证
                .and()
                .formLogin()
                .loginPage("/login.html") //自定义登录页面
                .loginProcessingUrl("/login") //自定义登录请求
                .successForwardUrl("/main.html") //自定登录成功后跳转的页面
                .usernameParameter("username") //自定义登录的用户名KEy
                .passwordParameter("password") //自定登录的密码名 key
                .defaultSuccessUrl("/main.html")//登录成功后默认的跳转页面路径
                .and().csrf().disable();//关闭cors
    }
}
