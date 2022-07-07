package org.itstack.demo.design.test;

import com.alibaba.fastjson.JSON;
import org.itstack.demo.design.change.AuthLink;
import org.itstack.demo.design.before.AuthService;
import org.itstack.demo.design.change.impl.Level1AuthLink;
import org.itstack.demo.design.change.impl.Level2AuthLink;
import org.itstack.demo.design.change.impl.Level3AuthLink;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiChangedTest {

    private Logger logger = LoggerFactory.getLogger(ApiChangedTest.class);

    @Test
    public void test_AuthLink() throws ParseException {
        AuthLink authLink = new Level3AuthLink("1000013", "王工")
                .appendNext(new Level2AuthLink("1000012", "张经理")
                        .appendNext(new Level1AuthLink("1000011", "段总")));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        logger.info("测试结果：{}", JSON.toJSONString(authLink.doAuth("小傅哥", "1000998004813441", new Date())));
//
//        // 模拟三级负责人审批
        AuthService.auth("1000013", "1000998004813441");
//        logger.info("测试结果：{}", "模拟三级负责人审批，王工");
//        logger.info("测试结果：{}", JSON.toJSONString(authLink.doAuth("小傅哥", "1000998004813441", new Date())));
//
//        String date = "2020-06-06 13:05:33";
//        Date dateP = format.parse(date);
//        // 模拟二级负责人审批
        AuthService.auth("1000012", "1000998004813441");
//        logger.info("测试结果：{}", "模拟二级负责人审批，张经理");
//        logger.info("测试结果：{}", JSON.toJSONString(authLink.doAuth("小傅哥", "1000998004813441", dateP)));
        String date2 = "2020-06-18 13:05:33";
        Date dateP2 = format.parse(date2);
        // 模拟一级负责人审批
        AuthService.auth("1000011", "1000998004813441");
        logger.info("测试结果：{}", "模拟一级负责人审批，段总");
        logger.info("测试结果：{}", JSON.toJSONString(authLink.doAuth("小傅哥", "1000998004813441", dateP2)));

    }

    @Test
    public void test2() throws ParseException {
        String date = "2022-06-06 13:05:33";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateP = format.parse(date);
        System.out.println(dateP);
    }
}
