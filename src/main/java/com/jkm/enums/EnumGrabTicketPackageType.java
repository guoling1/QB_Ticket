package com.jkm.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/2.
 *
 * 抢票套餐
 */
public enum EnumGrabTicketPackageType {

    TICKET_PACKAGE_FIRST(1, "0元套餐", "0"),

    TICKET_PACKAGE_SECOND(2, "10元套餐", "0.10"),

    TICKET_PACKAGE_THIRD(3, "20元套餐", "0.20"),

    TICKET_PACKAGE_FOUR(4, "30元套餐", "0.30"),

    TICKET_PACKAGE_FIVE(5, "66元套餐", "0.66");

    @Getter
    private int id;
    @Getter
    private String value;
    @Getter
    private String price;

    EnumGrabTicketPackageType(final int id, final String value, final String price) {
        this.id = id;
        this.value = value;
        this.price = price;
    }

    private final static ImmutableMap<Integer, EnumGrabTicketPackageType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumGrabTicketPackageType> builder = new ImmutableMap.Builder<>();
        for (EnumGrabTicketPackageType type : EnumGrabTicketPackageType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumGrabTicketPackageType of (final int type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown EnumGrabTicketPackageType[%s]", type);
        return INIT_MAP.get(type);
    }
}
