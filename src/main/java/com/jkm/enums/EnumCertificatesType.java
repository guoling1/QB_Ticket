package com.jkm.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

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

    @Getter
    private String id;
    @Getter
    private String value;

    EnumCertificatesType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }

    private final static ImmutableMap<String, EnumCertificatesType> INIT_MAP;

    static {
        final ImmutableMap.Builder<String, EnumCertificatesType> builder = new ImmutableMap.Builder<>();
        for (EnumCertificatesType type : EnumCertificatesType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }


    public static EnumCertificatesType of(final String type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown EnumCertificatesType [%s]", type);
        return INIT_MAP.get(type);
    }
}
