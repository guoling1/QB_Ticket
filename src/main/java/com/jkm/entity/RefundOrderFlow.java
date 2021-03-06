package com.jkm.entity;

import com.jkm.enums.EnumRefundOrderFlowStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/11/3.
 *
 * {@link EnumRefundOrderFlowStatus}
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
     * 抢票单id
     */
    private long grabTicketFormId = 0;

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
     * 退款结果
     */
    private String msg;

    /**
     * 是否是 退款已成功
     *
     * @return
     */
    public boolean isRefundSuccess() {
        return this.getStatus() == EnumRefundOrderFlowStatus.REFUND_SUCCESS.getId();
    }
}
