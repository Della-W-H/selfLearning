package com.della.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.della.mapper.AdminMapper;
import com.della.mapper.ResourceMapper;
import com.della.pojo.Admin;
import com.della.pojo.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要负责用户的登录业务逻辑
 *  1. 认证：我是谁？ who am I?
 *  2. 授权：你能干啥？ what can i do
 */
@Service("userService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;


    @Autowired
    private ResourceMapper resourceMapper;

    /**
     * @param username  用户登录的用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("用户登录的用户名:" + username);

        //1.查询数据库用户的信息
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getLoginName,username);
        Admin admin = adminMapper.selectOne(wrapper);
        if(admin == null){
            throw new RuntimeException("此用户不存在");
        }
        //2.把密码交给Security 来校验
        /**
         * 参数1：用户名
         * 参数2：数据库的密码
         * 参数3：权限列表
         */
        List<Resource> resouceByLoginName = resourceMapper.findResouceByLoginName(username);

        List<GrantedAuthority> auth = new ArrayList<>();
        for (Resource resource : resouceByLoginName) {
            auth.add(new SimpleGrantedAuthority(resource.getResKey()));
        }
        //模拟假数据
       /* if(admin.getLoginName().equals("admin")){
            //权限暂时不管
            auth.add(new SimpleGrantedAuthority("goods_add"));
            auth.add(new SimpleGrantedAuthority("goods_remove"));
            auth.add(new SimpleGrantedAuthority("goods_find"));
        }else{
            auth.add(new SimpleGrantedAuthority("goods_test"));
            auth.add(new SimpleGrantedAuthority("goods_find"));
        }*/
        return new User(admin.getLoginName(),admin.getPassword(),auth);
    }
}
