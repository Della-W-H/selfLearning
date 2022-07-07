package org.itstack.demo.design.change;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack) @2020
 *
 * 关于享元模式的设计可以着᯿学习享元⼯⼚的设计，在⼀些有⼤量᯿复对象可复⽤的场景下，使⽤
 * 此场景在服务端减少接⼝的调⽤，在客户端减少内存的占⽤。是这个设计模式的主要应⽤⽅式。
 * 另外通过 map 结构的使⽤⽅式也可以看到，使⽤⼀个固定id来存放和获取对象，是⾮常关键的点。
 * ⽽且不只是在享元模式中使⽤，⼀些其他⼯⼚模式、适配器模式、组合模式中都可以通过map结
 * 构存放服务供外部获取，减少if else的判断使⽤。
 * 当然除了这种设计的减少内存的使⽤优点外，也有它带来的缺点，在⼀些复杂的业务处理场景，很
 * 不容易区分出内部和外部状态，就像我们活动信息部分与库存变化部分。如果不能很好的拆分，就
 * 会把享元⼯⼚设计的⾮常混乱，难以维护。
 */
public class ActivityFactory {

    static Map<Long, Activity> activityMap = new HashMap<Long, Activity>();

    public static Activity getActivity(Long id) {
        Activity activity = activityMap.get(id);
        if (null == activity) {
            // 模拟从实际业务应用从接口中获取活动信息
            activity = new Activity();
            activity.setId(10001L);
            activity.setName("图书嗨乐");
            activity.setDesc("图书优惠券分享激励分享活动第二期");
            activity.setStartTime(new Date());
            activity.setStopTime(new Date());
            activityMap.put(id, activity);
        }
        return activity;
    }

}
