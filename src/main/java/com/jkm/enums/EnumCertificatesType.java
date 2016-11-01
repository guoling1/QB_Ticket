package com.jkm.enums;

/**
 * Created by yulong.zhang on 2016/10/27.
 *
 * 证件类型
 */
public enum EnumCertificatesType {

    SECOND_ID_CARD("1", "二代身份证"),

    first_ID_CARD("2", "一代身份证"),

    HONGKONG_AND_MACAO_PASS("C", "港澳通行证"),

    TAIWAN_PASS("G", "台湾通行证"),

    PASSPORT("B", "护照");


    private String id;

    private String value;

    EnumCertificatesType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
