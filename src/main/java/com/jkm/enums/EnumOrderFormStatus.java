package com.jkm.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/10/27.
 *
 * 订单状态
 */

public enum EnumOrderFormStatus {

    /**
     * 订单已删除
     */
    ORDER_FORM_DELETE(-1, "订单已删除"),

    /**
     * 订单初始化
     */
    ORDER_FORM_INITIALIZATION(1, "订单初始化"),

    /**
     * 订单-占座请求
     */
    ORDER_FORM_OCCUPY_SEAT_REQUESTING(2, "占座请求中"),

    /**
     * 订单-占座成功
     */
    ORDER_FORM_OCCUPY_SEAT_TRUE(3, "占座成功"),

    /**
     * 订单-占座成功
     */
    ORDER_FORM_OCCUPY_SEAT_FAIL(4, "占座失败"),

    /**
     * 订单-占座成功
     */
    ORDER_FORM_CUSTOMER_PAY_GOING(5, "支付中"),

    /**
     * 客户付款成功
     */
    ORDER_FORM_CUSTOMER_PAY_SUCCESS(6, "客户付款成功"),

    /**
     * 客户付款失败
     */
    ORDER_FORM_CUSTOMER_PAY_FAIL(7, "客户付款失败"),

    /**
     * 确认出票请求失败
     */
    ORDER_FORM_CONFIRM_TICKET_REQUEST_FAIL(8, "确认出票请求失败"),

    /**
     * 确认出票请求成功
     */
    ORDER_FORM_CONFIRM_TICKET_REQUEST_SUCCESS(9, "确认出票请求成功"),

    /**
     * 出票成功
     */
    ORDER_FORM_TICKET_SUCCESS(10, "出票成功"),

    /**
     * 出票失败
     */
    ORDER_FORM_TICKET_FAIL(11, "出票失败"),

    /**
     * 订单已经退票
     */
    ORDER_FORM_HAVE_BEEN_RETURN_TICKET(12, "订单已经退票"),

    /**
     * 订单取消
     */
    ORDER_FORM_CANCEL(13, "订单取消");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumOrderFormStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumOrderFormStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumOrderFormStatus> builder = new ImmutableMap.Builder<>();
        for (EnumOrderFormStatus status : EnumOrderFormStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static final EnumOrderFormStatus of(final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknow enumOrderFormStatus[%s]", status);
        return INIT_MAP.get(status);
    }

}
