package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-03.
 */
public enum EnumCardType {

    SECOND_ID_CARD(1, "二代身份证"),

    ARMY_CARD(2, "军人证件"),

    PASSPORT(3, "护照"),

    OTHER(9, "其它类型");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumCardType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
