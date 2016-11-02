package com.jkm.entity;

import com.jkm.enums.EnumPolicyOrderStatus;
import lombok.Data;

import java.util.Date;

/**
 * Created by yuxiang on 2016-11-02.
 * th_policy_order
 * {@link EnumPolicyOrderStatus}
 */
@Data
public class PolicyOrder extends BaseEntity {

    /**
     * 一张车票只能买一张保险单
     *  车票详情Id
     */
    private long orderFormDetailId;
    /**
     *保险产品代码
     */
    private String insProductNo;
    /**
     *航班日期（列车开车时间）
     *(yyyy-MM-dd HH:mm:ss)
     */
    private Date flightDate;
    /**
     *航班号（车次）
     */
    private String flightNumber;
    /**
     *交易流水号
     */
    private String serialNo;
    /**
     *保险人姓名
     */
    private String contractName;
    /**
     *用户类型（见参数列表）
     */
    private String contractType;
    /**
     *性别(M: 男 F: 女)
     */
    private String gender;
    /**
     *证件类型（见参数列表）
     */
    private int cardType;
    /**
     *证件号码
     */
    private String cardNo;
    /**
     * 出生日期
     *(yyyy-MM-dd)
     */
    private Date birthday;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 返回流水号
     */
    private String policyNo;
    /**
     *保险凭证号
     */
    private String printNo;
    /**
     *保单号
     */
    private String applyNo;
}
