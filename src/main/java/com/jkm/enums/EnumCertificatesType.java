package com.jkm.enums;

/**
 * Created by yulong.zhang on 2016/10/27.
 *
 * 证件类型
 */
public enum EnumCertificatesType {

    ID_CARD(1, "身份证"),

    PASSPORT(2, "护照"),

    TAIWAN_PASS(3, "台湾通行证"),

    HONGKONG_AND_MACAO_PASS(4, "港澳通行证");


    private int id;

    private String value;

    EnumCertificatesType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
