package com.jkm.enums;

import lombok.Getter;

/**
 * Created by Administrator on 2016/11/2.
 */
public enum EnumSex {
    MALE(0, "男"),

    FEMALE(1, "女");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumSex(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
