package com.jkm.controller.helper.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-06.
 */
@Data
public class ResponseGrabTicket {

    /**
     * 抢票单id
     */
    private long grabTicketFormId;

    /**
     * 订单支付总价格
     */
    private BigDecimal price;
}
