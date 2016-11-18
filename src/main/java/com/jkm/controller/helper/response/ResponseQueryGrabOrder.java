package com.jkm.controller.helper.response;

import com.jkm.enums.EnumGrabTimeType;
import com.jkm.enums.EnumTrainTicketSeatType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-13.
 */
@Data
public class ResponseQueryGrabOrder {
    /**
     * 用户id
     */
    private String uid;

    /**
     * 抢票单id
     */
    private long grabTicketFormId;

    /**
     * 是否抢到票(0否,1是)
     */
    private int isGrab;

    /**
     * 抢票火车票的总价格(预付款)
     */
    private BigDecimal grabTotalPrice;
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
     * 抢票车次
     */
    private String trainCodes;
    /**
     * 座位类型 (字符串拼接, 以,隔离)
     * {@link EnumTrainTicketSeatType}
     */
    private String seatTypes;
    /**
     *乘客信息 (id, 姓名, 身份证号, 乘客类型(成人, 儿童) )
     */
    private String passengerInfo;
    ////////////////////////////////////

    /**
     * 单张火车票正常价格 (已抢到)
     */
    private BigDecimal price;

    /**
     * 火车票总价格价格 (已抢到)
     */
    private BigDecimal ticketTotalPrice;

    /**
     * 订单总价格(已抢到)
     */
    private BigDecimal totalPrice;

    /**
     * 出发站名称(已抢到)
     */
    private String fromStationName;

    /**
     * 出发站简码(已抢到)
     */
    private String fromStationCode;

    /**
     * 到达站名称(已抢到)
     */
    private String toStationName;

    /**
     * 到达站简码(已抢到)
     */
    private String toStationCode;

    /**
     * 车次(已抢到)
     */
    private String checi;

    /**
     * 坐席code(已抢到)
     *
     * {@link com.jkm.enums.EnumTrainTicketSeatType}
     */
   // private String zwCode;

    /**
     * 坐席code(已抢到)
     *
     * {@link com.jkm.enums.EnumTrainTicketSeatType}
     */
   // private String zwName;

    /**
     * 发车日期(已抢到)
     */
    private String startDate;

    /**
     * 到达日期(已抢到)
     */
    private String endDate;

    /**
     * 发车时间(已抢到)
     */
    private String startTime;

    /**
     * 到达时间(已抢到)
     */
    private String endTime;

    /**
     * 历时(已抢到)
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
     * 乘客
     */
    private List<ResponseQueryGrabOrder.passenger> passengers;

    @Data
    public class passenger{
        /**
         * 小订单id
         */
        private long orderFormDetailId;
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
