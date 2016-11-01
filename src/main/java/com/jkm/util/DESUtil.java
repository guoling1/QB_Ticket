package com.jkm.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * Created by Administrator on 2016/11/1.
 */
public class DESUtil {
    private static final String DES = "DES";

    private static final String key = "v66r9ogtcvtxv3v4xq3gog8fqdbhwmt0";

    /**
     */
    public static String decrypt(String data) {
        try {
            return new String(decrypt(Base64.decodeBase64(data), key.getBytes()), "UTF-8");
        }
        catch (Exception e) {
            return data;//加密未上线时>>密文存在问题，解密失败，直接返回data
        }
    }

    /**
     */
    public static String encrypt(String data) throws Exception {
        return Base64.encodeBase64URLSafeString(encrypt(data.getBytes("UTF-8"), key.getBytes()));
    }

    /**
     * 加密
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        //生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        //从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        //创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        //Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        //用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        //返回
        return cipher.doFinal(data);
    }

    /**
     * 解密
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        //生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        //从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        //创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        //Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        //用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        //返回
        return cipher.doFinal(data);
    }



}
