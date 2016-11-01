package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/10/26.
 *
 * 火车票座位类型
 */
public enum EnumTrainTicketSeatType {

    /**
     * 无座
     */
    NO_SEAT("0", "无座"),

    /**
     * 硬座
     */
    HARD_SEAT("1", "硬座"),

    /**
     * 软座
     */
    SOFT_SEAT("2", "软座"),

    /**
     * 硬卧
     */
    HARD_SLEEPER("3", "硬卧"),

    /**
     * 软卧
     */
    SOFT_SLEEPER("4", "软卧"),

    /**
     * 高级软卧
     */
    HIGH_GRADE_SOFT_SLEEPER("6", "高级软卧"),

    /**
     * 二等座
     */
    SECOND_CLASS_SEAT("O", "二等座"),

    /**
     * 一等座
     */
    FIRST_CLASS_SEAT("M", "一等座"),

    /**
     * 特等座
     */
    SPECIAL_CLASS_SEAT("P", "特等座"),

    /**
     * 商务座
     */
    BUSINESS_SEAT("9", "商务座");

    @Getter
    private String id;
    @Getter
    private String value;

    EnumTrainTicketSeatType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
