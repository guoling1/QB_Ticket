package com.jkm.service.ys.entity;


import com.jkm.service.ys.helper.YsSdkConstants;
import com.jkm.util.DateFormatUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2016/10/25.
 *
 * 火车票 回调参数
 */
@Data
public class YsTrainTicketBookingCallbackResponse extends YsSdkResponse {
    /**
     * 请求流水号（接入商流水号）
     */
    private String termTransID;

    /**
     * 平台交易流水（YS流水号）
     */
    private String transID;

    /**
     * 车次号。 值如 "G101"
     */
    private String trainNo;

    /**
     *出发站点Code，值如 "beijingbei"
     */
    private String departStationCode;

    /**
     * 目的站点Code，值如 "nanjingnan"
     */
    private String arriveStationCode;

    /**
     *出发日期，值如 "2016-05-18 07:00"
     */
    private Date departDateTime;

    /**
     * 到达时间，值如 "2016-05-18 11:14"
     */
    private Date arriveDateTime;

    /**
     *取票号，值如 "EA12345678"
     */
    private String pickNo;

    /**
     * 实扣保险费
     */
    private BigDecimal insuranceFee;

    /**
     * 实扣技术服务费
     */
    private BigDecimal technicalFee;

    /**
     * 出票乘客列表， JSON格式，
     */
    private List<passenger> passengers;

    @Data
    public class passenger{

        /**
         * 乘车人乘客 ID 如 TAD13012620093, 注意：退票时需要传回乘客 ID
         */
        private String id;

        /**
         * 乘客类型， 1 为成人 2 为儿童
         *
         * {@link com.jkm.enums.EnumPassenger}
         */
        private int type;

        /**
         * 乘车人姓名 例如: "\u5f20\u4e09 "
         */
        private String name;

        /**
         * 证件号码 例如: "610423195608261254"
         */
        private String cardNo;

        /**
         * 座位类型 : 9
         *
         * {@link com.jkm.enums.EnumTrainTicketSeatType}
         */
        private int seatType;

        /**
         * 车厢，座位 （ Unicode 编码） 14 车厢， 084 座
         */
        private String seatName;

        /**
         * 座位价格，值如"1403.00"
         */
        private BigDecimal price;
    }

//    terminalID=04800001&factoryID=1011&reqDateTime=20151028162422
//            & termTransID=123 & transID =456&key=000000
    public boolean isSignCorrect() {

        return true;
    }

    public String getSignStr() {
        return new StringBuilder().append("terminalID").append("=").append(this.getTermTransID())
                .append("&").append("factoryID").append("=").append(YsSdkConstants.FACTORY_ID)
                .append("&").append("reqDateTime").append("=").append(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss))
                .append("&").append("termTransID").append("=").append(this.getTermTransID())
                .append("&").append("transID").append("=").append("")
                .append("&").append("key").append("=").append(YsSdkConstants.SIGN_KEY).toString();
    }
}
