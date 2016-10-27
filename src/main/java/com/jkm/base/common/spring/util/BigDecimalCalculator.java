package com.jkm.base.common.spring.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by huangwei on 5/22/15.
 */
public final class BigDecimalCalculator {
    /**
     * 默认除法精度
     */
    private static final int DEFAULT_DIVIDE_SCALE = 2;

    private BigDecimalCalculator() {
    }

    /**
     * 加法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double add(final double v1, final double v2) {
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 格式化到scale位精度
     *
     * @return
     */
    public static double format(final double amount, final int scale) {
        return new BigDecimal(Double.toString(amount)).setScale(
                scale, RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * 格式化 0.00
     */
    public static double format(final double amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(amount));
    }

    /**
     * 减法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double substract(final double v1, final double v2) {
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double multiply(final double v1, final double v2) {
        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * <pre>
     *     提供（相对）精确的除法运算,当发生除不尽的情况时,
     *     精确到小数点以后2位,以后的数字RoundingMode.HALF_EVEN
     * </pre>
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double divide(final double v1, final double v2) {
        return divide(v1, v2, DEFAULT_DIVIDE_SCALE);
    }

    /**
     * <pre>
     *     提供（相对）精确的除法运算.
     *     当发生除不尽的情况时,由scale参数指定精度,以后的数字HALF_EVEN.
     * </pre>
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static double divide(final double v1, final double v2, final int scale) {
        return divide(v1, v2, scale, RoundingMode.HALF_EVEN);
    }

    /**
     * <pre>
     *     提供（相对）精确的除法运算.
     *     当发生除不尽的情况时,由scale参数指 定精度,以后的数字 RoundingMode.HALF_EVEN
     * </pre>
     *
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static double divide(final double v1, final double v2, final int scale, final RoundingMode roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }

        final BigDecimal b1 = new BigDecimal(Double.toString(v1));
        final BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, roundingMode).doubleValue();
    }

    /**
     * 提供精确的小数位处理
     *
     * @param v
     * @param scale
     * @return
     */
    public static double round(final double v, final int scale, final RoundingMode roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return new BigDecimal(Double.toString(v)).setScale(scale, roundingMode).doubleValue();
    }

    /**
     * pow
     *
     * @param v
     * @param n
     * @param scale
     * @param roundingMode
     * @return
     */
    public static double pow(final double v, final int n, final int scale, final RoundingMode roundingMode) {
        return new BigDecimal(Double.toString(v)).pow(n).setScale(scale, roundingMode).doubleValue();
    }

    /**
     * <pre>
     *     提供（相对）精确的幂函数.
     *     由scale参数指 定精度,以后的数字 RoundingMode.HALF_EVEN
     * </pre>
     *
     * @param v
     * @param n
     * @return
     */
    public static double pow(final double v, final int n) {
        return new BigDecimal(Double.toString(v)).pow(n).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }


}
