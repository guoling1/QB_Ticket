package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/11/11.
 *
 * 订单退款异常记录
 *
 * tb_order_form_refund_exception_record
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderFormRefundExceptionRecord extends BaseEntity {

    /**
     * 订单id
     */
    private long orderFormId;

    /**
     * 抢票的id
     */
    private long grabOrderFormId;

    /**
     * 是否是抢票单  0：代购票单， 1：抢票单
     */
    private int isGrab;

    /**
     * 支付流水号
     */
    private String paymentSn;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款异常描述
     */
    private String remark;
}
