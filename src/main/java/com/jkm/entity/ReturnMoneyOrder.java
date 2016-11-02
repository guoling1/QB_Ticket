package com.jkm.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-02.
 */
@Data
public class ReturnMoneyOrder extends BaseEntity{

    /**
     * 订单ID
     */
    private long orderFormDetailId;
    /**
     * 实际退款总金额
     */
    private BigDecimal returnTotalMoney;
    /**
     * 原订单金额
     */
    private BigDecimal orgMoney;
    /**
     * 乘客票款实退金额
     */
    private BigDecimal returnTicketMoney;
    /**
     * 退还出票套餐金额
     */
    private BigDecimal returnBuyTicketPackage;
    /**
     * 退还抢票套餐金额
     */
    private BigDecimal returnGrabTicketPackage;
}
