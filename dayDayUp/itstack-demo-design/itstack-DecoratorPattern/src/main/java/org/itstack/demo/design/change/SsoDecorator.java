package org.itstack.demo.design.change;

import org.itstack.demo.design.before.HandlerInterceptor;

/**
 * 抽象类 装饰器
 */
public abstract class SsoDecorator implements HandlerInterceptor {

    private HandlerInterceptor handlerInterceptor;

    //无参构造方法的私有

    private SsoDecorator(){}

    //todo 抽象类 对继承接口的 注入 那么意味着抽象类的实现类 可以 直接调用 被继承接口的实现类的方法 从而 对 接口原有方法进行了 无改变的增强操作

    public SsoDecorator(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

    public boolean preHandle(String request, String response, Object handler) {
        return handlerInterceptor.preHandle(request, response, handler);
    }

}
