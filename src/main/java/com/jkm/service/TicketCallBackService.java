package com.jkm.service;

import com.jkm.entity.GrabTicketForm;
import com.jkm.entity.OrderForm;
import com.jkm.entity.RefundTicketFlow;

/**
 * Created by yuxiang on 2016-12-12.
 */
public interface TicketCallBackService {

    /**
     * 正常代购回调
     * @param orderForm
     */
    void orderFormCallBack(OrderForm orderForm);

    /**
     * 抢票回调
     * @param grabTicketForm
     */
    void grabFormCallBack(GrabTicketForm grabTicketForm);

    /**
     *退票回调
     * @param refundTicketFlow
     */
    void refundTicketCallBack(RefundTicketFlow refundTicketFlow);
}
