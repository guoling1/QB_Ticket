package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/10/31.
 *
 * HTHY method 枚举类
 */
public enum EnumHTHYMethodCode {

    SUBMIT_ORDER_FORM(1, "火车票订单提交接口");

    @Getter
    private int id;

    @Getter
    private String value;

    EnumHTHYMethodCode(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
