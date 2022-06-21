package com.tuling.shiroAuth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobile")
public class MobileController {
    /**
     * 代表一个查手机号的后台接口。
     * @return
     */
    @GetMapping("/query")
    public String query(){
        return "mobile";
    }
}