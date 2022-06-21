package com.tuling.shiroAuth.service;

import com.tuling.shiroAuth.bean.User;
import com.tuling.shiroAuth.utils.TestData;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Resource
    private TestData testData;

    public User getUserByUserName(String username){
        List<User> queryUsers = testData.getAllUser().stream().filter(user -> username.equals(user.getUserName())).collect(Collectors.toList());
        if(null != queryUsers && queryUsers.size()>0){
            try {
                return (User)BeanUtils.cloneBean(queryUsers.get(0));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }

    public User getUserByMobile(String mobile){
        List<User> queryUsers = testData.getAllUser().stream().filter(user -> mobile.equals(user.getMobile())).collect(Collectors.toList());
        if(null != queryUsers && queryUsers.size()>0){
            return queryUsers.get(0);
        }else{
            return null;
        }
    }
}
