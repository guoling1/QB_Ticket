package com.jkm.scheduled;

import com.jkm.service.OrderFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by yulong.zhang on 2016/11/7.
 */
@Component
@Lazy(false)
public class TicketScheduled {

    @Autowired
    private OrderFormService orderFormService;
    /**
     * 处理超时支付的订单
     */
    @Scheduled(cron = "* 0/1 * * * ?")
    public void handleExpiredOrderForm() {
        this.orderFormService.handleExpiredOrderForm();
    }
}
