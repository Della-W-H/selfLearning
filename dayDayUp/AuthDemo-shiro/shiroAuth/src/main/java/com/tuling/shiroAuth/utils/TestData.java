package com.tuling.shiroAuth.utils;

import com.tuling.shiroAuth.bean.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TestData {

    private List<User> allUser;

    /**
     * 模拟数据库获取到的数据。
     * admin用户 拥有admin角色，拥有mobile和salary两个资源。
     * mobile用户，拥有mobile角色，拥有mobile资源。
     * worker用户，拥有worker角色，没有资源。
     * @return
     */
    public List<User> getAllUser(){
        if(null == allUser){
            allUser = new ArrayList<>();


            allUser.add(new User("admin","admin","15777777777",Arrays.asList("admin"),Arrays.asList("mobile","salary")));
            allUser.add(new User("manager","manager","15888888888",Arrays.asList("manager"),Arrays.asList("salary")));
            allUser.add(new User("worker","worker","15999999999",Arrays.asList("worker"),Arrays.asList("worker")));
        }
        return allUser;
    }
}
