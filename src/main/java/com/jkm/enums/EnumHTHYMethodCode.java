package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/10/31.
 *
 * HTHY method 枚举类
 */
public enum EnumHTHYMethodCode {

    SUBMIT_ORDER_FORM(1, "train_order", "火车票订单提交"),
    RETURN_TICKET(2, "return_ticket", "线上退票");
    @Getter
    private int id;

    @Getter
    private String code;

    @Getter
    private String name;

    EnumHTHYMethodCode(final int id, final String code, final String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}