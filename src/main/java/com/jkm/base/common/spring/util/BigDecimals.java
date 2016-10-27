package com.jkm.base.common.spring.util;

import java.math.BigDecimal;

/**
 * Created by hutao on 15/9/15.
 * 下午7:24
 */
public final class BigDecimals {
    private BigDecimals() {
    }

    /**
     * double －》 BigDecimal
     *
     * @param value
     * @return
     */
    public static BigDecimal valueOf(final double value) {
        return new BigDecimal(String.valueOf(value));
    }
}
