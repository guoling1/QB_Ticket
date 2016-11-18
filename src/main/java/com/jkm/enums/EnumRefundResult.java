package com.jkm.enums;

import lombok.Getter;

/**
 * Created by Administrator on 2016/11/18.
 */
public enum EnumRefundResult {
    /**
     * 退款结果
     * S-成功
     * E-异常
     * U-已受理
     * T-连接失败（超时）
     * F-失败
     */

    SUCCESS("S", "成功"),
    EXCEPTION("E","异常"),
    HANDLE("U","已受理"),
    TIMEOUT("T","超时"),
    FAIL("F","失败");

    @Getter
    private String id;
    @Getter
    private String value;

    EnumRefundResult(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
