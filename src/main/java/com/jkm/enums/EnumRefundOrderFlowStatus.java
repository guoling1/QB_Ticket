package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/3.
 *
 * 订单退款单状态
 */
public enum  EnumRefundOrderFlowStatus {


    INIT(1, "待退款"),

    REFUND_SUCCESS(2, "退款成功"),

    REFUND_FAIL(3, "退款失败");


    @Getter
    private int id;

    @Getter
    private String value;


    EnumRefundOrderFlowStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
