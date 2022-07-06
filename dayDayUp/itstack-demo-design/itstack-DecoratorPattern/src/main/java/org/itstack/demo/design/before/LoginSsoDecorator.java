package org.itstack.demo.design.before;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 继承类的实现⽅式也是⼀个⽐较通⽤的⽅式，通过继承后重写⽅法，并发将⾃⼰的逻辑覆盖进去。如果
 *
 * 是⼀些简单的场景且不需要不断维护和扩展的，此类实现并不会有什么，也不会导致⼦类过多。
 */
public class LoginSsoDecorator extends SsoInterceptor {

    private static Map<String, String> authMap = new ConcurrentHashMap<String, String>();

    static {
        authMap.put("huahua", "queryUserInfo");
        authMap.put("doudou", "queryUserInfo");
    }

    @Override
    public boolean preHandle(String request, String response, Object handler) {

        // 模拟获取cookie
        String ticket = request.substring(1, 8);
        // 模拟校验
        boolean success = ticket.equals("success");

        if (!success) return false;

        String userId = request.substring(9);
        String method = authMap.get(userId);

        // 模拟方法校验
        return "queryUserInfo".equals(method);
    }

}
