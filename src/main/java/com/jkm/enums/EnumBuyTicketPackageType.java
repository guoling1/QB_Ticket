package com.jkm.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/2.
 *
 * 出票套餐 类型
 */
public enum EnumBuyTicketPackageType {

    TICKET_PACKAGE_FIRST(1, "0元套餐", "0"),

    TICKET_PACKAGE_SECOND(2, "20元套餐", "0.20"),

    TICKET_PACKAGE_THIRD(3, "30元套餐", "0.30");

    @Getter
    private int id;
    @Getter
    private String value;
    @Getter
    private String price;

    EnumBuyTicketPackageType(final int id, final String value, final String price) {
        this.id = id;
        this.value = value;
        this.price = price;
    }

    private final static ImmutableMap<Integer, EnumBuyTicketPackageType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumBuyTicketPackageType> builder = new ImmutableMap.Builder<>();
        for (EnumBuyTicketPackageType type : EnumBuyTicketPackageType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumBuyTicketPackageType of (final int type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown EnumBuyTicketPackageType[%s]", type);
        return INIT_MAP.get(type);
    }
}
