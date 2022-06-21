package com.tuling.shiroAuth.controller;

import com.tuling.shiroAuth.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/common/")
@RestController
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Object login(String username,String password){
        Map<String,String> errorCode = new HashMap<>();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
//        token.setRememberMe(true);
        Subject user = SecurityUtils.getSubject();
        if(user.isAuthenticated()){
            return "already login";
        }else{
            try{
                user.login(token);
                user.getSession().setAttribute("currentUser",user.getPrincipal());
                return "login succeed";
            } catch (UnknownAccountException uae) {
                logger.info("There is no user with username of " + token.getPrincipal());
                errorCode.put("errorMsg","不存在的用户名");
            } catch (IncorrectCredentialsException ice) {
                logger.info("Password for account " + token.getPrincipal() + " was incorrect!");
                errorCode.put("errorMsg","密码不正确");
            } catch (LockedAccountException lae) {
                logger.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
                errorCode.put("errorMsg","账号被锁定");
            } catch(AuthenticationException authe){
                logger.info("Authentication error ",authe);
                errorCode.put("errorMsg",authe.getMessage());
            }
            return errorCode;
        }
    }

    @RequestMapping("/getCurrentUser")
    public Object getCurrentUser(){
        Subject subject = SecurityUtils.getSubject();
        return subject.getSession().getAttribute("currentUser");
    }

    @RequestMapping("/noauth")
    public String noAuth(){
        return "未经授权，无法访问。";
    }
}
