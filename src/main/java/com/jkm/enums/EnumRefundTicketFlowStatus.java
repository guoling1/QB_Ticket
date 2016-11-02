package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-02.
 */
public enum EnumRefundTicketFlowStatus {

    INIT(0, "初始化退款流水单"),
    REFUND_TICKET_SUCCESS(1, "退款成功"),
    REFUND_TICKET_FAIL(2, "退款失败");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumRefundTicketFlowStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
