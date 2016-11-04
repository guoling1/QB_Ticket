package com.jkm.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-02.
 */
public enum EnumPolicyOrderStatus {

    INIT(0, "初始化保险单"),
    POLICY_BUY_SUCCESS(1, "保险单购买成功"),
    POLICY_BUY_FAIL(2, "保险单购买失败"),
    POLICY_RETURN_SUCCESS(3, "保险单退保成功"),
    POLICY_RETURN_FAIL(4, "保险单退保失败");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumPolicyOrderStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumPolicyOrderStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumPolicyOrderStatus> builder = new ImmutableMap.Builder<>();
        for (EnumPolicyOrderStatus status : EnumPolicyOrderStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static final EnumPolicyOrderStatus of(final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknow enumPolicyOrderStatus[%s]", status);
        return INIT_MAP.get(status);
    }
}
