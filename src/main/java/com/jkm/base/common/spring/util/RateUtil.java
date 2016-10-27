package com.jkm.base.common.spring.util;

import com.jkm.base.common.constant.BigDecimalConstant;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

/**
 * Created on 16/7/15.
 *
 * @author hutao
 * @version 1.0
 */
@UtilityClass
public class RateUtil {
    public static BigDecimal convertYearRateToDayRate(final BigDecimal yearRate) {
        return yearRate.divide(BigDecimalConstant.YEAR_DAY_COUNT,
                BigDecimalConstant.DEFAULT_DIVIDE_SCALA,
                BigDecimalConstant.DEFAULT_ROUND_MODE);
    }

    public static BigDecimal convertYearRateToMonthRate(final BigDecimal yearRate) {
        return yearRate.divide(BigDecimalConstant.YEAR_MONTH_COUNT,
                BigDecimalConstant.DEFAULT_DIVIDE_SCALA,
                BigDecimalConstant.DEFAULT_ROUND_MODE);
    }

    public static BigDecimal caculateInterestByYearRateAndDayCount(final BigDecimal amount,
                                                                   final BigDecimal yearRate,
                                                                   final int dayCount) {
        return amount.multiply(convertYearRateToDayRate(yearRate)).multiply(BigDecimal.valueOf(dayCount));
    }
}
