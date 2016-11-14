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
    private long orderFormId =0;

    /**
     * 抢票的id
     */
    private long grabOrderFormId =0;

    /**
     * 出票单
     */
    private long orderFormDetailId =0;

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
