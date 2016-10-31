package com.jkm.controller.helper.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/31.
 *
 * 查询我的订单 -- 出参
 */
@Data
public class ResponseQueryMyOrderForm {

    /**
     * 订单id
     */
    private long orderFormId;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 火车票正常价格
     */
    private BigDecimal price;

    /**
     * 订单总价格
     */
    private BigDecimal totalPrice;

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
     * 出发日期，值如 "2016-05-18"
     */
    private Date departDate;

    /**
     * 出发时间
     */
    private String departTime;

    /**
     *  到達日期，值如 "2016-05-18"
     */
    private Date arriveDate;

    /**
     * 到达时间
     */
    private String arriveTime;

    /**
     *  联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机
     */
    private String contactMobile;

    /**
     * 状态
     */
    private int status;

    /**
     * 乘客
     */
    private List<passenger> passengers;

    @Data
    public class passenger{
        /**
         *  乘车人姓名
         */
        private String passengerName;

        /**
         *  乘客类型，1 为成人 2 为儿童
         */
        private int passengerType;

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

    }
}
