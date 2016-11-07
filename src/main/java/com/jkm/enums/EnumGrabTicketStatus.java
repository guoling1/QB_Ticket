package com.jkm.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-07.
 */
public enum EnumGrabTicketStatus {

    /**
     * 抢票订单已删除
     */
    GRAB_FORM_DELETE(-1, "订单已删除"),

    /**
     * 抢票订单初始化
     */
    GRAB_FORM_INIT(1, "订单初始化"),

    /**
     * 客户付款成功
     */
    GRAB_FORM_PAY_WAIT(2, "等待客户付款中"),

    /**
     * 客户付款成功
     */
    GRAB_FORM_PAY_SUCCESS(3, "客户付款成功"),

    /**
     * 客户付款超时未支付
     */
    GRAB_FORM_PAY_OVERTIME(4, "客户超时未支付"),

    /**
     * 抢票订单请求中
     */
    GRAB_FORM_REQUESTING(5, "抢票下单请求中"),

    /**
     * 抢票订单下单请求成功
     */
    GRAB_FORM_REQUEST_SUCCESS(6, "抢票下单成功"),

    /**
     * 抢票订单下单请求失败
     */
    GRAB_FORM_REQUEST_FAIL(7, "抢票下单失败"),

    /**
     * 抢票失败
     */
    GRAB_FORM_FAIL(8, "抢票失败"),

    /**
     * 抢票成功并出票成功
     */
    GRAB_FORM_SUCCESS(9, "抢票成功并出票成功"),

    /**
     * 抢票订单取消
     */
    ORDER_FORM_CANCEL(10, "订单取消");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumGrabTicketStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumGrabTicketStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumGrabTicketStatus> builder = new ImmutableMap.Builder<>();
        for (EnumGrabTicketStatus status : EnumGrabTicketStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static final EnumGrabTicketStatus of(final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknow enumOrderFormStatus[%s]", status);
        return INIT_MAP.get(status);
    }
}