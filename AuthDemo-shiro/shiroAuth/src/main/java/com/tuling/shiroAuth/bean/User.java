package com.tuling.shiroAuth.bean;

import java.util.List;
import java.util.UUID;

public class User {

    private String userId;
    private String userName;
    private String userPass;

    private String mobile;

    private List<String> userRoles;
    private List<String> userPerms;

    public User(){

    }
    public User(String userName, String userPass, List<String> userRoles, List<String> userPerms) {
        this.userId = UUID.randomUUID().toString();
        this.userName = userName;
        this.userPass = userPass;
        this.userRoles = userRoles;
        this.userPerms = userPerms;
    }

    public User(String userName, String userPass, String mobile, List<String> userRoles, List<String> userPerms) {
        this.userName = userName;
        this.userPass = userPass;
        this.mobile = mobile;
        this.userRoles = userRoles;
        this.userPerms = userPerms;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    public List<String> getUserPerms() {
        return userPerms;
    }

    public void setUserPerms(List<String> userPerms) {
        this.userPerms = userPerms;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
