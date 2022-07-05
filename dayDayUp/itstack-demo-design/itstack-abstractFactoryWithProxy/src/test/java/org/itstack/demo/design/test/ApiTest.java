package org.itstack.demo.design.test;


import org.itstack.demo.design.before.CacheService;
import org.itstack.demo.design.before.impl.CacheServiceImpl2;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test_CacheService() {

        CacheService cacheService = new CacheServiceImpl2();

        cacheService.set("user_name_01", "小傅哥", 1);
        String val01 = cacheService.get("user_name_01", 1);
        System.out.println("测试结果：" + val01);

    }

}