package com.jkm.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2016-10-27.
 */
public enum EnumOrderFormDetailStatus {

    /**
     * 票初始化
     */
    TICKET_INITIALIZATION(1, "票初始化"),

    /**
     * 出票成功
     */
    TICKET_BUY_SUCCESS(2, "出票成功"),

    /**
     * 出票失败
     */
    TICKET_BUY_FAIL(3, "出票失败"),

    /**
     * 退票请求中
     */
    TICKET_RETURN_REQUESTING(4, "退票中"),

    /**
     * 退票请求成功
     */
    TICKET_RETURN_REQUEST_SUCCESS(5, "退票请求成功"),

    /**
     * 退票成功
     */
    TICKET_RETURN_SUCCESS(6, "退票成功"),

    /**
     * 退票失败
     */
    TICKET_RETURN_FAIL(7, "退票失败"),

    /**
     * 订单取消
     */
    TICKET_ORDER_CANCEL(8, "订单取消");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumOrderFormDetailStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumOrderFormDetailStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumOrderFormDetailStatus> builder = new ImmutableMap.Builder<>();
        for (EnumOrderFormDetailStatus status : EnumOrderFormDetailStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static final EnumOrderFormDetailStatus of(final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknow enumOrderFormStatus[%s]", status);
        return INIT_MAP.get(status);
    }
}
