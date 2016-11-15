package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends BaseEntity{
    /**
     * 三方用户编码
     */
    private String uid;
    /**
     * 商户appid
     */
    private String appId;
    /**
     * 12306账户
     */
    private String account;
    /**
     * 12306密码
     */
    private String pwd;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 身份证号
     */
    private String cardId;
    /**
     * 银行卡号
     */
    private String cardNo;
    /**
     * 真实姓名
     */
    private String realName;
}
