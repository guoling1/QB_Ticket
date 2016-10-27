package com.jkm.base.common.spring.constant;

import java.math.BigDecimal;

/**
 * Created by hutao on 15/6/23.
 * 下午6:12
 */
public final class BigDecimalConstant {
    /**
     * 结果精度
     */
    public static final int DEFAULT_RESULT_SCALA = 2;
    /**
     * 除法结果精度
     */
    public static final int DEFAULT_DIVIDE_SCALA = 10;
    /**
     * 默认四舍五入的方式
     */
    public static final int DEFAULT_ROUND_MODE = BigDecimal.ROUND_HALF_EVEN;
    /**
     *
     */
    public static final BigDecimal YEAR_DAY_COUNT = new BigDecimal("360");
    /**
     *
     */
    public static final BigDecimal YEAR_MONTH_COUNT = new BigDecimal("12");
    /**
     *
     */
    public static final BigDecimal MONTH_DAY_COUNT = new BigDecimal("30");
    /**
     *
     */
    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private BigDecimalConstant() {

    }
}
