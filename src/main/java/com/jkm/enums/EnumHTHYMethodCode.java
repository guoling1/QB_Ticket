package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/10/31.
 *
 * HTHY method 枚举类
 */
public enum EnumHTHYMethodCode {

    SUBMIT_ORDER_FORM(1, "train_order", "火车票订单提交"),

    CONFIRM_ORDER_FORM(2, "train_confirm", "确认出票"),

    CANCEL_ORDER_FORM(3, "train_cancel", "取消订单"),

    QUERY_ORDER_FORM(4, "return_ticket", "查询订单"),

    RETURN_TICKET(5, "return_ticket", "线上退票"),

    POST_POLICY_ORDER(6, "PostPolicyOrder", "提交保险单"),

    CANCEL_POLICY_ORDER(7 , "CancelPolicyOrder", "撤消保险单"),

    QUERY_POLICY_ORDER(8, "QueryPolicyOrder", "查询投保单状态"),

    QIANG_PIAO_ORDER(9, "qiang_piao_order", "抢票下单");

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
