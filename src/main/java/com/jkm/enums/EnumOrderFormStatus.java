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
     * 订单初始化
     */
    ORDER_FORM_INITIALIZATION(1, "订单初始化"),

    /**
     * 客户付款成功
     */
    ORDER_FORM_CUSTOMER_PAY_SUCCESS(2, "客户付款成功"),

    /**
     * 订单请求中
     */
    ORDER_FORM_REQUESTING(3, "订单请求中"),

    /**
     * 订单请求成功
     */
    ORDER_FORM_REQUEST_SUCCESS(4, "订单请求成功"),

    /**
     * 订单购买成功
     */
    ORDER_FORM_SUCCESS(5, "订单购买成功"),

    /**
     * 订单购买失败
     */
    ORDER_FORM_FAIL(6, "订单购买失败"),

    /**
     * 订单购买失败
     */
    ORDER_FORM_HAVE_BEEN_RETURN_TICKET(7, "订单已经退票");

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
