package org.itstack.demo.design.changed.factory.impl;

import org.itstack.demo.design.changed.factory.ICacheAdapter;
import org.itstack.demo.design.matter.IIR;

import java.util.concurrent.TimeUnit;

/**
 * 适配器处理IIR集群源
 */
public class IIRCacheAdapter implements ICacheAdapter {

    //此处 被适配对象是new出来的一般而言实际工程中不太可能会这样

    private IIR iir = new IIR();

    public String get(String key) {
        return iir.get(key);
    }

    public void set(String key, String value) {
        iir.set(key, value);
    }

    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        iir.setExpire(key, value, timeout, timeUnit);
    }

    public void del(String key) {
        iir.del(key);
    }

}
