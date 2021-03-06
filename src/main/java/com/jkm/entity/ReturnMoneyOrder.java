package com.jkm.entity;

import com.jkm.enums.EnumReturnMoneyOrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yuxiang on 2016-11-02.
 *
 * 车票退款单,
 * tb_return_money_order
 * {@link EnumReturnMoneyOrderStatus}
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReturnMoneyOrder extends BaseEntity{

    /**
     * 订单ID
     */
    private long orderFormDetailId;
    /**
     * 大订单支付流水号 *
     */
    private String  orderFormSn;
    /**
     * 退款说明(出票失败退款, 抢票失败退款, 线上退票退款, 线下退票退款, 抢票成功差价退款, 取消抢票退款)
     */
    private String  remark;
    /**
     * 原商户订单日期(订单购买成功时间) *
     */
    private Date orgDate;
    /**
     * 实际退款总金额 *
     */
    private BigDecimal returnTotalMoney;
    /**
     * 原订单金额 *
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
