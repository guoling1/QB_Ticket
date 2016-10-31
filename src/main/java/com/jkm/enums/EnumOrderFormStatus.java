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
     * 订单-占座成功
     */
    ORDER_FORM_OCCUPY_SEAT_TRUE(2, "占座成功"),

    /**
     * 订单-占座成功
     */
    ORDER_FORM_OCCUPY_SEAT_FAIL(3, "占座失败"),

    /**
     * 客户付款成功
     */
    ORDER_FORM_CUSTOMER_PAY_SUCCESS(4, "客户付款成功"),

    /**
     * 确认出票请求中
     */
    ORDER_FORM_CONFIRM_TICKET_REQUESTING(5, "确认出票请求中"),

    /**
     * 确认出票请求成功
     */
    ORDER_FORM_CONFIRM_TICKET_REQUEST_SUCCESS(6, "确认出票请求成功"),

    /**
     * 出票成功
     */
    ORDER_FORM_TICKET_SUCCESS(7, "出票成功"),

    /**
     * 出票失败
     */
    ORDER_FORM_TICKET_FAIL(8, "出票失败"),

    /**
     * 订单已经退票
     */
    ORDER_FORM_HAVE_BEEN_RETURN_TICKET(9, "订单已经退票");

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
