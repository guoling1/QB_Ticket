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
     * 抢票单id
     */
    private long grabTicketFormId;
    /**
     * 用户总付款金额(每个订单)
     */
    private BigDecimal totalAmount;
    /**
     * 出票套餐费
     */
    private BigDecimal buyTicketPackage;
    /**
     * 抢票套餐费
     */
    private BigDecimal grabTicketPackage;

    /**
     * 付款成功
     *
     * @return
     */
    public boolean isPaySuccess() {
        return this.getStatus() == EnumChargeMoneyOrderStatus.PAYMENT_TICKET_SUCCESS.getId();
    }

}
