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
     * 绑定手机号
     */
    BIND_MOBILE(1, EnumNoticeType.BIND_MOBILE, 5),
    /**
     * 登录验证码
     */
    LOGIN(2, EnumNoticeType.LOGIN, 5),

    /**
     * 解绑手机
     */
    UNBIND_MOBILE(3, EnumNoticeType.UNBIND_MOBILE, 5),

    /**
     * 设置交易密码
     */
    SET_TRADING_PASSWORD(4, EnumNoticeType.SET_TRADING_PASSWORD, 5),

    /**
     * 设置免密支付
     */
    OPEN_NOPWD(5, EnumNoticeType.OPEN_NOPWD, 5),

    /**
     * 解绑微信
     */
    UNBIND_WEIXIN(6, EnumNoticeType.UNBIND_WEIXIN, 5),

    /**
     * 解绑银行卡
     */
    UNBIND_BANK_CARD(7, EnumNoticeType.UNBIND_BANK_CARD, 5),

    /**
     * 取现
     */
    WITHDRAW(8, EnumNoticeType.WITHDRAW, 20),

    /**
     * 重设密码
     */
    MODIFY_PASSWORD(9, EnumNoticeType.MODIFY_PASSWORD, 3),

    /**
     * 找回免密
     */
    RETRIEVE_PASSWORD(10, EnumNoticeType.RETRIEVE_PASSWORD, 3),

    /**
     * 后台管理员登录
     */
    ADMIN_LOGIN(101, EnumNoticeType.ADMIN_LOGIN, 20),
    /**
     * 关闭二次验证
     */
    ADMIN_CLOSE_TOTP(102, EnumNoticeType.ADMIN_CLOSE_TOTP, 5),
    /**
     * 管理员修改员工密码
     */
    ADMIN_MODIFY_PASSWORD(103, EnumNoticeType.ADMIN_MODIFY_PASSWORD, 5),;


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
