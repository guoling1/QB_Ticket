package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-02.
 */
public enum EnumRefundTicketFlowStatus {

    INIT(0, "退票中"),
    REFUND_TICKET_SUCCESS(1, "退票成功"),
    REFUND_TICKET_FAIL(2, "退票失败"),
    TICKET_REFUND_ING(3,"退款中"),
    TICKET_REFUND_SUCCESS(4, "退款成功"),
    TICKET_REFUND_FAIL(5, "退款失败");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumRefundTicketFlowStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
