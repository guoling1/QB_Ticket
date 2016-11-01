package com.jkm.service.hy.helper;


import com.jkm.util.BytesHexConverterUtil;
import com.jkm.util.Md5Util;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 *  Created by yuxiang on 2016-09-28.
 */
@UtilityClass
public class HySdkSignUtil {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HySdkSignUtil.class);

    public static final String SIGN_KEY = "sign";

    /**
     * 生成签名
     *
     * @param method
     * @param key
     * @param reqTime 请求时间，格式：yyyyMMddHHmmss（非空）
     * @return
     */
    public static String sign(@NonNull final String method,
                              @NonNull final String reqTime,
                              @NonNull final String key) {
        final String signedStr = getSignedStr(method, reqTime, key);
        log.debug("签名字符串:" + signedStr);
        return BytesHexConverterUtil.bytesToHexStr(Md5Util.md5Digest(signedStr
                .getBytes(Charset.forName("utf-8"))));
    }


    /**
     * 生成签名
     *
     * @param needSignStr
     * @return
     */
    public static String sign(@NonNull final String needSignStr) {
        log.debug("签名字符串:{" + needSignStr + "}");
        return BytesHexConverterUtil.bytesToHexStr(Md5Util.md5Digest(needSignStr
                .getBytes(Charset.forName("utf-8"))));
    }


    private static String getSignedStr(final String method, final String reqTime,
                                       final String key) {
       final String content = HySdkConstans.PARTNERID + method + reqTime +
               BytesHexConverterUtil.bytesToHexStr(Md5Util.md5Digest(HySdkConstans.SIGN_KEY.getBytes(Charset.forName("utf-8"))));
        return content;
    }

    /**
     * 签名
     *
     * @param method
     * @param reqTime
     * @param key
     * @return
     */
    public static boolean checkSign(@NonNull final String method,
                                    @NonNull final String reqTime,
                                    @NonNull final String key,
                                    @NonNull final String sign) {
        try {
            final String expectSign = sign(method, reqTime, key);
            log.debug("期望签名:"+ expectSign + "实际签名:" + sign);
            return Objects.equals(expectSign, sign);
        } catch (final Throwable e) {
            log.error("检查签名异常:"+ e.getMessage() + e);
            return false;
        }
    }
    /*public static boolean checkSign(final CanCheckSignResponse response) {
        return checkSign(YstSdkSerializeUtil.converterObjectToMap(response),
                YsSdkConstants.SIGN_KEY, response.getSign());
    }*/
}
