package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-02.
 */
public enum EnumChargeMoneyOrderStatus {

    INIT(0, "未付款"),
    REFUND_TICKET_SUCCESS(1, "付款成功"),
    REFUND_TICKET_FAIL(2, "付款失败");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumChargeMoneyOrderStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
