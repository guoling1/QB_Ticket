package com.jkm.enums.notifier;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by konglingxin on 3/11/15.
 * 通知类型
 */
@Getter
public enum EnumNoticeType {
    //########## 公共消息 ##########

    LOGIN(101, "登录"),
    MODIFY_PASSWORD(102, "修改密码"),
    RETRIEVE_PASSWORD(103, "找回密码"),

    //########## 用户后台消息 ##########

    BIND_MOBILE(200, "绑定手机号"),
    UNBIND_MOBILE(202, "解绑手机"),
    SET_TRADING_PASSWORD(203, "设置交易密码"),
    OPEN_NOPWD(204, "设置免密支付"),
    UNBIND_WEIXIN(205, "解绑微信"),
    UNBIND_BANK_CARD(206, "解绑银行卡"),
    WITHDRAW(207, "提现"),

    //########## 运营后台消息 ##########

    ADMIN_LOGIN(301, "后台登陆"),
    ADMIN_MODIFY_PASSWORD(302, "后台修改密码"),
    ADMIN_CLOSE_TOTP(303, "关闭二次验证"),

    //########## 商户后台消息 ##########

    //########## 系统消息 ##########

    MANUAL_RECHARGE(501, "后台手工充值成功"),
    WITHDRAW_FAIL_AUDIT(502, "提现审核失败"),
    WITHDRAW_FAIL(503, "提现失败"),
    WITHDRAW_SUCCESS(504, "提现成功"),
    FAIL_BID(505, "流标"),
    START_INTEREST(506, "计息提醒"),
    REPAYMENT(507, "投资人收到还款"),
    INVITE_SUCCESS(508, "投资成功"),;


    private static final Map<Integer, EnumNoticeType> ENUM_MAP = new HashMap<>();

    static {
        for (final EnumNoticeType enumNoticeType : EnumNoticeType.values()) {
            ENUM_MAP.put(enumNoticeType.getId(), enumNoticeType);
        }
    }

    private int id;
    private String desc;

    /**
     * 构造函数
     *
     * @param id
     * @param desc
     */
    EnumNoticeType(final int id, final String desc) {
        this.id = id;
        this.desc = desc;
    }

    /**
     * convertor
     *
     * @param id
     * @return
     */
    public static EnumNoticeType integer2Enum(final int id) {
        return ENUM_MAP.get(id);
    }
}
