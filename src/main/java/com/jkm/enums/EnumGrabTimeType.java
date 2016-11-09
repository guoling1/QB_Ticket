package com.jkm.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-08.
 */
public enum EnumGrabTimeType {

    /**
     * 抢至发车前3小时
     */
    GRAB_BEGIN_THREE(1, "抢至发车前3小时" , 3),

    /**
     * 抢至发车前6小时
     */
    GRAB_BEGIN_SIX(2, "抢至发车前6小时", 6),

    /**
     * 抢至发车前一天
     */
    GRAB_BEGIN_TWENTY_FOUR(3, "抢至发车前一天", 24),

    /**
     * 抢至发车前三天
     */
    GRAB_BEGIN_SEVENTY_TWO(4, "抢至发车前三天", 72);

    @Getter
    private int id;
    @Getter
    private String value;
    @Getter
    private int hour;

    EnumGrabTimeType(final int id, final String value, final int hour) {
        this.id = id;
        this.value = value;
        this.hour = hour;
    }

    private static final ImmutableMap<Integer, EnumGrabTimeType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumGrabTimeType> builder = new ImmutableMap.Builder<>();
        for (EnumGrabTimeType status : EnumGrabTimeType.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static final EnumGrabTimeType of(final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknow EnumGrabTimeType[%s]", status);
        return INIT_MAP.get(status);
    }
}
