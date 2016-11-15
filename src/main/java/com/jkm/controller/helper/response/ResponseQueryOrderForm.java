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
public class ResponseQueryOrderForm {

    /**
     * 订单id
     */
    private long orderFormId;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 单张火车票正常价格
     */
    private BigDecimal price;

    /**
     * 火车票总价格价格
     */
    private BigDecimal ticketTotalPrice;

    /**
     * 订单总价格
     */
    private BigDecimal totalPrice;

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
     * 车次
     */
    private String checi;

    /**
     * 坐席code
     *
     * {@link com.jkm.enums.EnumTrainTicketSeatType}
     */
    private String zwCode;

    /**
     * 坐席code
     *
     * {@link com.jkm.enums.EnumTrainTicketSeatType}
     */
    private String zwName;

    /**
     * 发车日期
     */
    private String startDate;

    /**
     * 到达日期
     */
    private String endDate;

    /**
     * 发车时间
     */
    private String startTime;

    /**
     * 到达时间
     */
    private String endTime;

    /**
     * 历时
     */
    private String runTime;

    /**
     * 支付到期时间
     */
    private Date expireTime;

    /**
     * 状态
     */
    private int status;

    /**
     * 提示
     */
    private String remark;
    /**
     * 乘客
     */
    private List<passenger> passengers;

    @Data
    public class passenger{
        /**
         *  乘车人姓名
         */
        private String name;

        /**
         * 证件号
         */
        private String passportSeNo;

        /**
         *  票类型
         *
         *  {@link com.jkm.enums.EnumTrainTicketType}
         */
        private String piaoType;

        /**
         *  票类型名
         *
         *  {@link com.jkm.enums.EnumTrainTicketType}
         */
        private String piaoTypeName;

        /**
         *车厢，座位 （Unicode 编码） 14 车厢，084 座
         */
        private String cxin;

        /**
         *火车站出票价格
         */
        private BigDecimal price;

        /**
         * 状态
         */
        private int status;
    }
}
