package com.jkm.base.common.spring.util;

/**
 * Created by huangwei on 7/23/15.
 */
public final class DataFormatUtil {
    private DataFormatUtil() {
    }

    /**
     * 将手机后中间6位隐去
     *
     * @param mobile
     * @return
     */
    public static String formatMobile(final String mobile) {
        return mobile.substring(0, 3) + "******" + mobile.substring(9);
    }
}
