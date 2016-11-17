package com.jkm.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/11/17.
 *
 * 确认订单（确认出票） 异常记录
 *
 * tb_confirm_order_exception_record
 */
@Data
public class ConfirmOrderExceptionRecord extends BaseEntity {

    /**
     * 订单id
     */
    private long orderFormId;

    /**
     * 支付流水号
     */
    private String paymentSn;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 备注
     */
    private String remark;
}
