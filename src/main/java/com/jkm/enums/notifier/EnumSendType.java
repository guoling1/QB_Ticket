package com.jkm.enums.notifier;

import lombok.Getter;

/**
 * Created by konglingxin on 3/11/15.
 * 用户类型
 */
@Getter
public enum EnumSendType {

    INSTANT(1, "立即"),

    TIMED(2, "定时");

    private int id;
    private String desc;

    /**
     * 构造函数
     *
     * @param id
     * @param desc
     */
    EnumSendType(final int id, final String desc) {
        this.id = id;
        this.desc = desc;
    }
}
