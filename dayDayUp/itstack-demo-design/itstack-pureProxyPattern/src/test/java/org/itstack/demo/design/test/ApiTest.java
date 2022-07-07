package org.itstack.demo.design.test;

import org.itstack.demo.design.IUserDao;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * 关于这部分代理模式的讲解我们采⽤了开发⼀个关于 mybatis-spring 中间件中部分核⼼功能来
 * 体现代理模式的强⼤之处，所以涉及到了⼀些关于代理类的创建以及spring中bean的注册这些知
 * 识点，可能在平常的业务开发中都是很少⽤到的，但是在中间件开发中确实⾮常常⻅的操作。
 * 代理模式除了开发中间件外还可以是对服务的包装，物联⽹组件等等，让复杂的各项服务变为轻量
 * 级调⽤、缓存使⽤。你可以理解为你家⾥的电灯开关，我们不能操作220v电线的⼈⾁连接，但是
 * 可以使⽤开关，避免触电。
 * 代理模式的设计⽅式可以让代码更加整洁、⼲净易于维护，虽然在这部分开发中额外增加了很多类
 * 也包括了⾃⼰处理bean的注册等，但是这样的中间件复⽤性极⾼也更加智能，可以⾮常⽅便的扩
 * 展到各个服务应⽤中。
 *
 * todo 代理模式是为了实现对象的控制，因为被代理的对象往往难以直接获得或者是其内部不想暴露出来。 2、装饰模式是以对客户端透明的方式扩展对象的功能，是继承方案的一个替代方案；代理模式则是给一个对象提供一个代理对象，并由代理对象来控制对原有对象的引用；
 * */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_IUserDao() {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring-config.xml");
        IUserDao userDao = beanFactory.getBean("userDao", IUserDao.class);
        String res = userDao.queryUserInfo("100001");
        logger.info("测试结果：{}", res);
    }

}
