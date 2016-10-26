package com.jkm.enums;

/**
 * Created by yulong.zhang on 2016/10/26.
 *
 * 火车票座位类型
 */
public enum EnumTrainTicketSeatType {

    NO_SEAT(1, "无座"),

    HARD_SEAT(2, "硬座"),

    SOFT_SEAT(3, "软座"),

    SECOND_CLASS_SEAT(4, "二等座"),

    FIRST_CLASS_SEAT(5, "一等座"),

    SECOND_CLASS_SOFT_SEAT(6, "二等软座"),

    FIRST_CLASS_SOFT_SEAT(7, "一等软座"),

    SPECIAL_CLASS_SEAT(8, "特等座"),

    BUSINESS_SEAT(9, "商务座"),

    HARD_SLEEPER_UPPER(10, "硬卧上铺"),

    HARD_SLEEPER_MIDDLE(11, "硬卧中铺"),

    HARD_SLEEPER_LOWER(12, "硬卧下铺"),

    SOFT_SLEEPER_LOWER(13, "软卧下铺"),

    SOFT_SLEEPER_UPPER(14, "软卧上铺"),

    HIGH_GRADE_SOFT_SLEEPER(15, "高级软卧"),

    MOVE_SLEEPER_UPPER(16, "动卧上铺"),

    MOVE_SLEEPER_LOWER(17, "动卧下铺"),

    HIGH_GRADE_MOVE_SLEEPER_UPPER(18, "高级动卧上铺"),

    HIGH_GRADE_MOVE_SLEEPER_LOWER(19, "高级动卧下铺"),

    other(20, "其他");



    private int id;

    private String value;

    EnumTrainTicketSeatType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
