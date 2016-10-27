package com.jkm.common.third.channel.ys.service.helper;

import com.jkm.base.common.spring.util.BytesHexConverterUtil;
import com.jkm.base.common.spring.util.TripleDESUtil;
import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;

/**
 * 此项目，没有加密
 */
@UtilityClass
public class YsSdkEncryptUtil {
    /**
     * 加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String encrypt(final String content,
                                 final byte[] key) {
        final byte[] encryptBytes = TripleDESUtil.encrypt(
                content.getBytes(Charset.forName("GBK")), key);
        return BytesHexConverterUtil.bytesToHexStr(encryptBytes);
    }

    /**
     * 解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(final String content,
                                 final byte[] key) {
        final byte[] decryptBytes = TripleDESUtil.decrypt(
                BytesHexConverterUtil.hexStrToBytes(content), key);
        return new String(decryptBytes, Charset.forName("GBK"));
    }
}
