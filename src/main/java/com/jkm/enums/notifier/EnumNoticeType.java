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

    //########## 支付消息 ##########

    PAYMENT_CODE(201, "支付验证码"),


    //########## 火车票消息 ##########

    BUY_TICKET_SUCCESS(301, "出票成功"),

    BUY_TICKET_FAIL(302, "出票失败"),

    GRAB_TICKET_SUCCESS_HAVE_RESIDUE_YES(303, "抢票成功(有余款)"),

    GRAB_TICKET_SUCCESS_HAVE_RESIDUE_NO(304, "抢票成功(无余款)"),

    GRAB_TICKET_FAIL(305, "抢票失败"),

    GRAB_CANCEL(306, "用户主动取消抢票"),

    RETURN_TICKET_ONLINE(307, "用户在线申请退票"),

    RETURN_TICKET_DOWN(308, "用户线下退款或改签");


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
