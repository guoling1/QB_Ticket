package com.jkm.entity;

import com.jkm.enums.EnumChargeMoneyOrderStatus;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-02.
 *
 * tb_charge_order
 * 收款单(火车票　＋　出票套餐费　＋　抢票套餐费用)
 *
 * {@link EnumChargeMoneyOrderStatus}
 */
@Data
public class ChargeMoneyOrder extends BaseEntity{

    /**
     * 订单ID
     */
    private long orderFormId;
    /**
     * 用户总付款金额(每个订单)
     */
    private BigDecimal totalAmount;
    /**
     * 出票套餐价格
     */
    private BigDecimal buyTicketPackage;
    /**
     * 抢票套餐价格
     */
    private BigDecimal grabTicketPackage;

}
