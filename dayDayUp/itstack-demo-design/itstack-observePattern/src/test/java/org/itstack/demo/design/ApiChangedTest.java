package org.itstack.demo.design;

import com.alibaba.fastjson.JSON;
import org.itstack.demo.design.change.event.LotteryResult;
import org.itstack.demo.design.change.event.LotteryService;
import org.itstack.demo.design.change.event.LotteryServiceImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiChangedTest {

    private Logger logger = LoggerFactory.getLogger(ApiChangedTest.class);

    @Test
    public void test() {
        LotteryService lotteryService = new LotteryServiceImpl();
        LotteryResult result = lotteryService.draw("2765789109876");
        logger.info("测试结果：{}", JSON.toJSONString(result));
    }

}
