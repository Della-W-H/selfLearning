package org.itstack.demo.design.products;

import org.itstack.demo.design.products.store.ICommodity;
import org.itstack.demo.design.products.store.impl.CardCommodityService;
import org.itstack.demo.design.products.store.impl.CouponCommodityService;
import org.itstack.demo.design.products.store.impl.GoodsCommodityService;

public class StoreFactory {

    public ICommodity getCommodityService(Integer commodityType) {
        if (null == commodityType) return null;
        if (1 == commodityType) return new CouponCommodityService();
        if (2 == commodityType) return new GoodsCommodityService();
        if (3 == commodityType) return new CardCommodityService();
        throw new RuntimeException("不存在的商品服务类型");
    }

}
