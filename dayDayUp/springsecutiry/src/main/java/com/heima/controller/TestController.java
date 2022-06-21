package com.heima.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    /**
     *  test 用户特有的方法
     * @return
     */
    @GetMapping("/test")
    //必须满足此权限才能访问
    @PreAuthorize("hasAnyAuthority('goods_test')")
    //@PreAuthorize("hasRole('goods_test')")
    public String test(){
        System.out.println("我执行了test方法....");
        return "success";
    }

    /**
     * 一般账户都能查询
     * @return
     */
    @GetMapping("/find")
    public String find(){
        System.out.println("我执行了find方法....");
        return "success";
    }

    /**
     * 管理员才能新增 admin
     * @return
     */
    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('goods_add')")
    public String add(){
        System.out.println("我执行了add方法....");
        return "success";
    }

    /**
     * 管理员才能新增 admin
     * @return
     */
    @PreAuthorize("hasAnyAuthority('goods_remove')")
    @GetMapping("/remove")
    public String remove(){
        System.out.println("我执行了remove方法....");
        return "success";
    }

}
