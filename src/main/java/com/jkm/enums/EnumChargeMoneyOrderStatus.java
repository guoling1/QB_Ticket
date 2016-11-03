package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-02.
 */
public enum EnumChargeMoneyOrderStatus {

    INIT(1, "未付款"),

    PAYMENT_TICKET_SUCCESS(2, "付款成功"),

    PAYMENT_TICKET_FAIL(3, "付款失败");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumChargeMoneyOrderStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
