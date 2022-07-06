package org.itstack.demo.design.test;

import org.itstack.demo.design.change.LoginSsoDecorator;
import org.itstack.demo.design.before.SsoInterceptor;
import org.junit.Test;

public class ApiChangedTest {

    @Test
    public void test_LoginSsoDecorator() {
        LoginSsoDecorator ssoDecorator = new LoginSsoDecorator(new SsoInterceptor());
        String request = "1successhuahua";
        boolean success = ssoDecorator.preHandle(request, "ewcdqwt40liuiu", "t");
        System.out.println("登录校验：" + request + (success ? " 放行" : " 拦截"));
    }

}
