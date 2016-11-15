package com.jkm.enums.notifier;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by konglingxin on 3/11/15.
 */
@Getter
public enum EnumVerificationCodeType {

    /**
     * 登录验证码
     */
    LOGIN(1, EnumNoticeType.LOGIN, 5),

    /**
     * 重设密码
     */
    MODIFY_PASSWORD(2, EnumNoticeType.MODIFY_PASSWORD, 3),

    /**
     * 找回免密
     */
    RETRIEVE_PASSWORD(3, EnumNoticeType.RETRIEVE_PASSWORD, 3),


    /**
     * 支付
     */
    PAYMENT(4, EnumNoticeType.PAYMENT_CODE, 100),
    /**
     * 根据银行卡id支付
     */
    PAYMENT_CID(5,EnumNoticeType.PAYMENT_CODE,100),
    /**
     * 抢票单支付
     */
    PAYMENT_GRAP(6, EnumNoticeType.PAYMENT_CODE, 100),
    /**
     * 抢票单根据银行卡id支付
     */
    PAYMENT_GRAPCID(7, EnumNoticeType.PAYMENT_CODE, 100);


    private static final Map<Integer, EnumVerificationCodeType> TYPE_MAP = new HashMap<>();

    static {
        for (final EnumVerificationCodeType enumVerificationCodeType : EnumVerificationCodeType.values()) {
            TYPE_MAP.put(enumVerificationCodeType.getId(), enumVerificationCodeType);
        }
    }

    private int id;

    /**
     * 短信类型
     */
    private EnumNoticeType smsType;

    /**
     * 每日发送次数
     */
    private int dailyCount;

    EnumVerificationCodeType(final int id,
                             final EnumNoticeType smsType,
                             final int dailyCount) {
        this.id = id;
        this.smsType = smsType;
        this.dailyCount = dailyCount;
    }

    /**
     * 根据id获取enum
     *
     * @param id
     * @return
     */
    public static final EnumVerificationCodeType integer2enum(final int id) {
        return TYPE_MAP.get(id);
    }
}
