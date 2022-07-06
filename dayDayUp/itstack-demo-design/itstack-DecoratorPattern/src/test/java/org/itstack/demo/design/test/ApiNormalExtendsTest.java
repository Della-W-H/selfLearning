package org.itstack.demo.design.test;

import org.itstack.demo.design.before.LoginSsoDecorator;
import org.junit.Test;

/**
 * 继承类的实现⽅式也是⼀个⽐较通⽤的⽅式，通过继承后重写⽅法，并发将⾃⼰的逻辑覆盖进去。如果
 * 是⼀些简单的场景且不需要不断维护和扩展的，此类实现并不会有什么，也不会导致⼦类过多。
 */
public class ApiNormalExtendsTest {

    @Test
    public void test_LoginSsoDecorator() {
        LoginSsoDecorator ssoDecorator = new LoginSsoDecorator();
        String request = "1successhuahua";
        boolean success = ssoDecorator.preHandle(request, "ewcdqwt40liuiu", "t");
        System.out.println("登录校验：" + request + (success ? " 放行" : " 拦截"));
    }

}
