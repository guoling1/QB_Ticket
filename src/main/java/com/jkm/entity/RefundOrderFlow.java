package com.jkm.entity;

import com.jkm.enums.EnumRefundOrderFlowStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/11/3.
 *
 * 订单退款单
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RefundOrderFlow extends BaseEntity {

    /**
     * 订单id
     */
    private long orderFormId;

    /**
     * 订单支付流失号
     */
    private String paymentSn;

    /**
     * 订单日期
     */
    private String orderDate;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 原订单金额
     */
    private BigDecimal originalAmount;

    /**
     * 退款理由
     */
    private String refundReason;

    /**
     * 是否是 退款已成功
     *
     * @return
     */
    public boolean isRefundSuccess() {
        return this.getStatus() == EnumRefundOrderFlowStatus.REFUND_SUCCESS.getId();
    }
}
