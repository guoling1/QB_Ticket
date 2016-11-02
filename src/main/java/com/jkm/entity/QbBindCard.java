package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class QbBindCard extends BaseEntity{
    /**
     * 三方用户id
     */
    private String uid;
    /**
     * 银行卡号
     */
    private String cardNo;
    /**
     * 银行卡姓名
     */
    private String accountName;
    /**
     * 证件类型
     */
    private String cardType;
    /**
     * 证件号
     */
    private String cardId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 支付密码
     */
    private String payPwd;
}
