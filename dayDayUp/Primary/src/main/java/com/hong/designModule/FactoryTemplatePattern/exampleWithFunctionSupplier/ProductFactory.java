package com.hong.designModule.FactoryTemplatePattern.exampleWithFunctionSupplier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author wanghong
 * @date 2022/7/1
 * @apiNote
 */
public class ProductFactory {
    /*final static Map<String, Supplier<Product>> map = new HashMap<String, Supplier<Product>>(){{
        map.put("loan",Loan::new);
        map.put("stock",Stock::new);
        map.put("bond",Bond::new);   todo 会报初始化error why？
    }};*/

    final static Map<String, Supplier<Product>> map = new HashMap<>();
    static {
        map.put("loan",Loan::new);
        map.put("stock",Stock::new);
        map.put("bond",Bond::new);
    }

    public static Product createProduct(String name){
        Supplier<Product> p = map.get(name);
        if (p != null)return p.get();
        throw new IllegalArgumentException("No such product "+name);
    }
}
