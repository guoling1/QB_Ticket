package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yuxiang on 2016-10-27.
 *
 * 订单详情
 *
 * tb_order_form_detail
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderFormDetail extends BaseEntity{
    /**
     * 订单ID
     */
    private long orderFormId;

    /**
     * 乘车人乘客 ID
     */
    private String passengerId;

    /**
     *  乘车人姓名
     */
    private String passengerName;

    /**
     *  乘客类型，1 为成人 2 为儿童
     */
    private int passengerType;

    /**
     *  证件号码
     */
    private String cardNo;

    /**
     * 座位类型
     *
     * {@link com.jkm.enums.EnumTrainTicketSeatType}
     */
    private int seatType;

    /**
     *车厢，座位 （Unicode 编码） 14 车厢，084 座
     */
    private String seatName;

    /**
     *火车站出票价格
     */
    private BigDecimal price;

    /**
     *取票号，值如 "EA12345678"
     */
    private String pickNo;

    /**
     * 固定 ： 1 单程
     */
    private int journeyType = 1;

    /**
     * 车次号。 值如 "G101"
     */
    private String trainNo;

    /**
     * 出发站点
     */
    private String departStation;

    /**
     * 目的站点
     */
    private String arriveStation;

    /**
     * 出发站点Code，值如 "beijingbei"
     */
    private String departStationCode;

    /**
     * 目的站点Code，值如 "nanjingnan"
     */
    private String arriveStationCode;

    /**
     * 出发日期
     */
    private Date departDateTime;

    /**
     * 到达时间
     */
    private Date arriveDateTime;

}
