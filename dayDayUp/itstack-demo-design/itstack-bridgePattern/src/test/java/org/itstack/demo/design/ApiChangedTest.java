package org.itstack.demo.design;

import org.itstack.demo.design.change.channel.Pay;
import org.itstack.demo.design.change.channel.WxPay;
import org.itstack.demo.design.change.channel.ZfbPay;
import org.itstack.demo.design.change.mode.PayFaceMode;
import org.itstack.demo.design.change.mode.PayFingerprintMode;
import org.junit.Test;

import java.math.BigDecimal;

public class ApiChangedTest {

    @Test
    public void test_pay() {

        System.out.println("\r\n模拟测试场景；微信支付、人脸方式。");
        Pay wxPay = new WxPay(new PayFaceMode());
        wxPay.transfer("weixin_1092033111", "100000109893", new BigDecimal(100));

        System.out.println("\r\n模拟测试场景；支付宝支付、指纹方式。");
        Pay zfbPay = new ZfbPay(new PayFingerprintMode());
        zfbPay.transfer("jlu19dlxo111","100000109894",new BigDecimal(100));

    }

}
