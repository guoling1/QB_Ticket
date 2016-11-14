package com.jkm.entity;

import com.jkm.enums.EnumBuyTicketPackageType;
import com.jkm.enums.EnumGrabTicketStatus;
import com.jkm.enums.EnumGrabTimeType;
import com.jkm.enums.EnumTrainTicketSeatType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-04.
 *
 * tb_grab_ticket_form
 * {@link EnumGrabTicketStatus}
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GrabTicketForm extends BaseEntity {
    /**
     * 用户id
     */
    private String uid;
    /**
     * jkm使用方订单号
     */
    private String orderId;
    /**
     *交易订单号(对方提供)
     */
    private String transactionId;
    /**
     * 联系手机,通知出票信息
     */
    private String phone;
    /**
     * 支付流水号
     */
    private String paymentSn;
    /**
     * 抢票数量
     */
    private int ticketNum;
    /**
     * 抢票火车票的总价格(预付款)
     */
    private BigDecimal grabTicketTotalPrice;
    /**
     * 抢票订单总价格(预付款)
     */
    private BigDecimal grabTotalPrice;
    /**
     * 实际收取火车票的总价格
     */
    private BigDecimal ticketTotalPrice;
    /**
     * 实际收取订单总价格
     */
    private BigDecimal totalPrice;
    /**
     * 抢票出发日期 2015-09-09
     */
    private String grabStartTime;
    /**
     * 抢票时效类型
     * {@link EnumGrabTimeType}
     */
    private int grabTimeType;
    /**
     * 最早发车时间 2015-09-10 13:00
     */
    private String firstStartTime;
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
     * {@link EnumTrainTicketSeatType}
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
     * 抢票成功发车日期 2016-10-08
     */
    private String startDate;

    /**
     * 抢票成功到达日期 2016-10-09
     */
    private String endDate;

    /**
     * 抢票成功发车时间 21:35
     */
    private String startTime;

    /**
     * 抢票成功到达时间 10:04
     */
    private String endTime;

    /**
     * 运行时间
     */
    private String runTime;

    /**
     * 抢票成功车次
     */
    private String checi;

    /**
     * 取票单号（电子单号）
     */
    private String orderNumber;

    /**
     *乘客信息 (id, 姓名, 身份证号, 乘客类型(成人, 儿童) )
     */
    private String passengerInfo;

    /**
     * 备注
     */
    private String remark;


    public boolean isPaySuccess() {

        return EnumGrabTicketStatus.GRAB_FORM_PAY_SUCCESS.getId() == this.getStatus();
    }

    public boolean isCanCancel(){

        return EnumGrabTicketStatus.GRAB_FORM_PAY_WAIT.getId() == this.getStatus();
    }

    public boolean isCanCancelBeforeCharge(){
        return EnumGrabTicketStatus.GRAB_FORM_PAY_WAIT.getId() == this.getStatus()
                && EnumGrabTicketStatus.GRAB_FORM_PAY_ING.getId() != this.getStatus();
    }
}
