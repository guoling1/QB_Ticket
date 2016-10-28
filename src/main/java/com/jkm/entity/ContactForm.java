package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Administrator on 2016/10/27.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContactForm extends BaseEntity {
    /**
     * 用户id
     */
    private long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 乘客生日
     */
    private String birth;

    /**
     * 证件类型
     */
    private int cardType;

    /**
     * 证件号
     */
    private String cardNo;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 旅客类型
     */
    private int userType;

}
