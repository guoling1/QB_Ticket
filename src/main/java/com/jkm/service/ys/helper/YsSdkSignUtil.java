package com.jkm.service.ys.helper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jkm.util.BytesHexConverterUtil;
import com.jkm.util.MD5Util;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *  Created by yuxiang on 2016-09-28.
 */
@UtilityClass
public class YsSdkSignUtil {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(YsSdkSignUtil.class);

    public static final String SIGN_KEY = "sign";

    /**
     * 生成签名
     *
     * @param params
     * @param key
     * @return
     */
    public static String sign(@NonNull final Map<String, String> params,
                              @NonNull final String key) {
        final String signedStr = getSignedStr(params, key);
        log.debug("签名字符串:" + signedStr);
        return BytesHexConverterUtil.bytesToHexStr(MD5Util.md5Digest(signedStr
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
        return BytesHexConverterUtil.bytesToHexStr(MD5Util.md5Digest(needSignStr
                .getBytes(Charset.forName("utf-8"))));
    }


    private static String getSignedStr(final Map<String, String> params,
                                       final String key) {
        Preconditions.checkArgument(!params.isEmpty(), "待签名数据不能为空");
        final List<String> paramsKeyList = Lists.newArrayList(params.keySet());
        Collections.sort(paramsKeyList);
        final StringBuilder content = new StringBuilder();
        for (final String paramsKey : paramsKeyList) {
            if (!Objects.equals(paramsKey, SIGN_KEY)) {
                content.append(paramsKey + '=' + params.get(paramsKey));
                content.append('&');
            }
        }
        content.append("key=" + key);
        return content.toString();
    }

    /**
     * 签名
     *
     * @param params
     * @param key
     * @return
     */
    public static boolean checkSign(@NonNull final Map<String, String> params,
                                    @NonNull final String key,
                                    @NonNull final String sign) {
        try {
            final String expectSign = sign(params, key);
            log.debug("期望签名:"+ expectSign + "实际签名:" + sign);
            return Objects.equals(expectSign, sign);
        } catch (final Throwable e) {
            log.error("检查签名异常:"+ e.getMessage() + e);
            return false;
        }
    }

    /**
     * 签名
     *
     * @param needSignStr
     * @param sign
     * @return
     */
    public static boolean checkSign(@NonNull final String needSignStr,
                                    @NonNull final String sign) {
        try {
            final String expectSign = sign(needSignStr);
//            log.debug("期望签名:{},实际签名:{}", expectSign, sign);
            return Objects.equals(expectSign, sign);
        } catch (final Throwable e) {
//            log.error("检查签名异常:{}", e.getMessage(), e);
            return false;
        }
    }

    /*public static boolean checkSign(final CanCheckSignResponse response) {
        return checkSign(YstSdkSerializeUtil.converterObjectToMap(response),
                YsSdkConstants.SIGN_KEY, response.getSign());
    }*/
}
