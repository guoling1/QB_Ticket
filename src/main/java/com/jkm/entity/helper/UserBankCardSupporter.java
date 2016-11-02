package com.jkm.entity.helper;


import com.jkm.util.AESUtil;

/**
 * Created by yuxiang on 2016-09-22.
 */
public class UserBankCardSupporter {
    /**
     * 禁止构造
     */
    private UserBankCardSupporter() {
    }
    /**
     * 解密密码信息
     *
     * @return 银行卡号信息明文
     */
    static public String decryptPwd(final String pwd) {
        return AESUtil.decrypt(pwd, BankConstants.getConfig().userBankCardKey());
    }

    /**
     * 加密密码信息
     *
     * @return 银行卡号信息密文
     */
    static public String encryptPwd(final String pwd) {
        return AESUtil.encrypt(pwd, BankConstants.getConfig().userBankCardKey());
    }
    /**
     * 解密银行卡记录加密的银行卡号信息
     *
     * @return 银行卡号信息明文
     */
    static public String decryptCardNo(final String cardNo) {
        return AESUtil.decrypt(cardNo, BankConstants.getConfig().userBankCardKey());
    }

    /**
     * 加密银行卡记录的银行卡号信息
     *
     * @return 银行卡号信息密文
     */
    static public String encryptCardNo(final String cardNo) {
        return AESUtil.encrypt(cardNo, BankConstants.getConfig().userBankCardKey());
    }

    /**
     * 解密身份证号信息
     *
     * @return
     */
    static public String decryptCardId(final String cardId) {
        return AESUtil.decrypt(cardId, BankConstants.getConfig().userBankCardKey());
    }

    /**
     * 加密身份证号信息
     *
     * @return
     */
    static public String encryptCardId(final String cardId) {
        return AESUtil.encrypt(cardId, BankConstants.getConfig().userBankCardKey());
    }


    /**
     * 解密银行卡记录加密的手机号信息
     *
     * @return 手机号信息明文
     */
    static public String decryptMobile(final String mobile) {
        return AESUtil.decrypt(mobile, BankConstants.getConfig().userBankCardKey());
    }

    /**
     * 加密银行卡记录的手机号信息
     *
     * @return 手机号信息密文
     */
    static public String encryptMobile(final String mobile) {
        return AESUtil.encrypt(mobile, BankConstants.getConfig().userBankCardKey());
    }
}
