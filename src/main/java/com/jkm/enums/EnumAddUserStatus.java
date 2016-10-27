package com.jkm.enums;

/**
 * Created by Administrator on 2016/10/27.
 */
public enum EnumAddUserStatus {

    AUDIT_PASS(0,"审核通过"),
    AUDIT_NOT_PASS(1,"审核未通过");

    private int id;
    private String value;

    EnumAddUserStatus(final int id, final String value) {
        this.id = id;
        this.value = value;

    }
}
