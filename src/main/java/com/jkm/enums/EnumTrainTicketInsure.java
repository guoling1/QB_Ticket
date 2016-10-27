package com.jkm.enums;

/**
 * Created by yulong.zhang on 2016/10/26.
 *
 * 保险类型
 */
public enum EnumTrainTicketInsure {

    INSURE_BUY_NO(0, 0, "不卖"),

    /**
     * 20 元保险由新华人寿提供(出行关爱交通意外伤害保险 A 款 )
     */
    INSURE_BUY_YES(1, 20, "卖");

    private int id;

    private int price;

    private String value;

    EnumTrainTicketInsure(final int id, final int price, final String value) {
        this.id = id;
        this.price = price;
        this.value = value;
    }
}
