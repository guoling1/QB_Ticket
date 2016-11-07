package com.jkm.entity;

import com.jkm.enums.EnumBuyTicketPackageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-04.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GrabTicketForm extends BaseEntity {
    /**
     * 用户id
     */
    private String uid;
    /**
     * 联系手机,通知出票信息
     */
    private String phone;
    /**
     * 支付流水号
     */
    private String paymentSn;
    /**
     * 抢票单张车票价格
     */
    private BigDecimal grabPrice;
    /**
     * 抢票火车票的总价格
     */
    private BigDecimal grabTicketTotalPrice;
    /**
     * 抢票订单总价格
     */
    private BigDecimal grabTotalPrice;
    /**
     * 实际单张车票价格
     */
    private BigDecimal price;
    /**
     * 实际火车票的总价格
     */
    private BigDecimal ticketTotalPrice;
    /**
     * 实际订单总价格
     */
    private BigDecimal totalPrice;
    /**
     * 抢票出发日期 2015-09-09 13:00
     */
    private String grabStartTime;
    /**
     * 抢票截至日期 2015-09-10 13:00
     */
    private String grabEndTime;
    /**
     * 出发站名称
     */
    private String fromStationName;

    /**
     * 出发站简码
     */
    private String fromStationCode;

    /**
     * 到达站名称
     */
    private String toStationName;

    /**
     * 到达站简码
     */
    private String toStationCode;
    /**
     * 抢票车次
     */
    private String trainCodes;
    /**
     * 座位类型 (字符串拼接, 以,隔离)
     */
    private String seatTypes;
    /**
     * 抢票套餐
     * {@link com.jkm.enums.EnumGrabTicketPackageType}
     */
    private int grabTicketPackage;
    /**
     * 出票套餐
     * {@link EnumBuyTicketPackageType}
     */
    private int buyTicketPackage;
    /**
     * 发车日期
     */
    private String startDate;

    /**
     * 到达日期
     */
    private String endDate;

    /**
     * 抢票成功发车时间
     */
    private String startTime;

    /**
     * 抢票成功到达时间
     */
    private String endTime;

    /**
     * 抢票成功历时
     */
    private String runTime;

    /**
     * 抢票成功车次
     */
    private String checi;

    /**
     *乘客信息 (id, 姓名, 身份证号, 乘客类型(成人, 儿童) )
     */
    private String passengerInfo;

    /**
     * 备注
     */
    private String remark;
}
