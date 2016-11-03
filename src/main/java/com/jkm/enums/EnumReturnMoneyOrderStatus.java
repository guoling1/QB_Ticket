package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-03.
 */
public enum EnumReturnMoneyOrderStatus {

    INIT(0, "初始化"),
    RETURN_MONEY_SUCCESS(1, "退款成功"),
    RETURN_MONEY_FAIL(2, "退款失败");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumReturnMoneyOrderStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
