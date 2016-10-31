package com.jkm.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/10/26.
 */
public enum EnumPassenger {


    ADULT(1, "成人"),

    CHILDREN(2, "儿童");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumPassenger(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
