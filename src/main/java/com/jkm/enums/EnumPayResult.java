package com.jkm.enums;

import lombok.Getter;

/**
 * Created by Administrator on 2016/11/18.
 */
public enum EnumPayResult {
    /**
     * 支付结果
     * S-成功
     * E-异常
     * U-处理中
     *T-连接失败（超时）
     * N-待支付
     * F-失败
     */

    SUCCESS("S", "成功"),
    EXCEPTION("E","异常"),
    HANDLE("U","处理中"),
    TIMEOUT("T","超时"),
    UNPAID("N","待支付"),
    FAIL("F","失败");

    @Getter
    private String id;
    @Getter
    private String value;

    EnumPayResult(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
