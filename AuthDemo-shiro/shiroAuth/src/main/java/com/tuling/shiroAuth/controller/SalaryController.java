package com.tuling.shiroAuth.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salary")
public class SalaryController {
    /**
     * 代表一个查薪水的后台接口。
     * @return
     */
    @RequiresPermissions("salary")
    @GetMapping("/query")
    public String query(){
        return "salary";
    }
}