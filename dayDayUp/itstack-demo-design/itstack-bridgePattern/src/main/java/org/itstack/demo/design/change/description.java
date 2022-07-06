package org.itstack.demo.design.change;

/**
 * @author wanghong
 * @date 2022/7/6
 * @apiNote
 *
 * 与上⾯的ifelse实现⽅式相⽐，这⾥的调⽤⽅式变得整洁、⼲净、易使⽤； new WxPay(new
 * PayFaceMode()) 、 new ZfbPay(new PayFingerprintMode())
 * 外部的使⽤接⼝的⽤户不需要关⼼具体的实现，只按需选择使⽤即可。
 *
 * ⽬前以上优化主要针对桥接模式的使⽤进⾏重构 if 逻辑部分，关于调⽤部分可以使⽤ 抽象⼯⼚ 或
 * 策略模式 配合map结构，将服务配置化。因为这⾥主要展示 桥接模式 ，所以就不在额外多加代
 * 码，避免喧宾夺主。
 */
public class description {
}
