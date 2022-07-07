package org.itstack.demo.design.change.impl;

import org.itstack.demo.design.change.AuthInfo;
import org.itstack.demo.design.change.AuthLink;
import org.itstack.demo.design.before.AuthService;

import java.util.Date;

/**
 * 一级负责人
 */
public class Level1AuthLink extends AuthLink {

    public Level1AuthLink(String levelUserId, String levelUserName) {
        super(levelUserId, levelUserName);
    }

    public AuthInfo doAuth(String uId, String orderId, Date authDate) {
        Date date = AuthService.queryAuthInfo(levelUserId, orderId);
        if (null == date) {
            //todo 这个infos 参数 后面都是 统一的参数 是构造函数那边对应的 String数组
            return new AuthInfo("0001", "单号：", orderId, " 状态：待一级审批负责人 ", levelUserName);
        }
        AuthLink next = super.next();
        if (null == next) {
            return new AuthInfo("0000", "单号：", orderId, " 状态：一级审批完成负责人", " 时间：", f.format(date), " 审批人：", levelUserName);
        }

        return next.doAuth(uId, orderId, authDate);
    }

}
