package org.itstack.demo.design.test;

import org.itstack.demo.design.change.LoginSsoDecorator;
import org.itstack.demo.design.before.SsoInterceptor;
import org.junit.Test;

public class ApiChangedTest {

    @Test
    public void test_LoginSsoDecorator() {
        //todo 这个就是一般装饰器 模式 的表现形式 注意他和 ApiNormalExtendsText中使用的区别
        //这意味 装饰器对原有对象没有破坏其封装性
        LoginSsoDecorator ssoDecorator = new LoginSsoDecorator(new SsoInterceptor());
        String request = "1successhuahua";
        boolean success = ssoDecorator.preHandle(request, "ewcdqwt40liuiu", "t");
        System.out.println("登录校验：" + request + (success ? " 放行" : " 拦截"));
    }

}
