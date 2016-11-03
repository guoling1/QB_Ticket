package com.jkm.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/1.
 *
 * 火车票的类型
 */
public enum EnumTrainTicketType {

    ADULT("1", "成人票"),

    CHILDREN("2", "儿童票"),

    STUDENT("3", " 学生票"),

    MAIMED_SOLDIER("4", "残军票");

    @Getter
    private String id;
    @Getter
    private String value;

    EnumTrainTicketType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }

    private final static ImmutableMap<String, EnumTrainTicketType> INIT_MAP;

    static {
        final ImmutableMap.Builder<String, EnumTrainTicketType> builder = new ImmutableMap.Builder<>();
        for (EnumTrainTicketType type : EnumTrainTicketType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP =  builder.build();
    }

    public static EnumTrainTicketType of (final String type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown EnumTrainTicketType [%s]", type);
        return INIT_MAP.get(type);
    }
}
