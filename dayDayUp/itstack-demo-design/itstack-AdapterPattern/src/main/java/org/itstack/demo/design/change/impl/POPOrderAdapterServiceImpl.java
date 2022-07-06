package org.itstack.demo.design.change.impl;

import org.itstack.demo.design.change.OrderAdapterService;
import org.itstack.demo.design.service.POPOrderService;

public class POPOrderAdapterServiceImpl implements OrderAdapterService {

    private POPOrderService popOrderService = new POPOrderService();

    public boolean isFirst(String uId) {
        return popOrderService.isFirstOrder(uId);
    }

}
