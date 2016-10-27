package com.jkm.base.common.spring.util;

import com.jkm.base.common.constant.BigDecimalConstant;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

/**
 * Created by hutao on 15/12/9.
 * 下午3:58
 */
@UtilityClass
public class AmountUtil {
    /**
     * 元转换成分
     *
     * @param amount 金额
     * @return 长整型，分为单位
     */
    public static long convertYuanToCent(final double amount) {
        return BigDecimal.valueOf(amount)
                .multiply(BigDecimalConstant.ONE_HUNDRED)
                .longValue();
    }

    /**
     * 分转换成元
     *
     * @param amount 金额
     * @return 2位精度double
     */
    public static double convertCentToYuan(final long amount) {
        return BigDecimal.valueOf(amount)
                .divide(BigDecimalConstant.ONE_HUNDRED, 2,
                        BigDecimalConstant.DEFAULT_ROUND_MODE)
                .doubleValue();
    }
}
