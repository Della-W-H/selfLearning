package org.itstack.demo.design.before;

public interface HandlerInterceptor {

    boolean preHandle(String request, String response, Object handler);

}
