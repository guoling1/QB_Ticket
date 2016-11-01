package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/1.
 *
 * 火车票的类型
 */
public enum EnumTrainTicketType {

    ADULT("1", "成人票"),

    CHILDREN("2", "儿童票"),

    STUDENT("3", " 学生票"),

    MAIMED_SOLDIER("4", "残军票");

    @Getter
    private String id;
    @Getter
    private String value;

    EnumTrainTicketType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
