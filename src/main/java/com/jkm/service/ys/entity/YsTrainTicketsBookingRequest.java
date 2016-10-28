package com.jkm.service.ys.entity;

import com.jkm.service.ys.helper.YsSdkConstants;
import com.jkm.util.DateFormatUtil;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2016/10/25.
 *
 * 火车车票订票受理接口  -- 请求参数
 */
@Data
public class YsTrainTicketsBookingRequest extends YsSdkRequest{

    /**
     * 查询车次时的 queryKey
     */
    private String queryKey;

    /**
     * 请求流水号
     */
    private String termTransID;

    /**
     * 固定 ： 1 单程
     */
    private int journeyType = 1;

    /**
     * 车次号。 值如 "G101"
     */
    private String trainNo;

    /**
     * 出发站点Code，值如 "beijingbei"
     */
    private String departStationCode;

    /**
     * 目的站点Code，值如 "nanjingnan"
     */
    private String arriveStationCode;

    /**
     * 出发日期，值如 "2016-05-18"
     */
    private String departDate;

    /**
     *  出发时间，值如 "07:00"
     */
    private String departTime;

    /**
     * 保险ID，值如 1 当为0时表示不购买保险
     * 当ID不为0时，默认乘客列表中的所有乘客都购买保险
     *
     * {@link com.jkm.enums.EnumTrainTicketInsure}
     */
    private int insureID = 0;

    /**
     *  单个保险价格，值如 20 当为0时表示不购买保险
     */
    private String insurePrice = "0";

    /**
     * 是否合并占座和出票通知， 固定： 1
     */
    private int isMergeNotice = 1;

    /**
     * 无票时是否接受无座， 0 接受、 1 不接受
     *
     * {@link com.jkm.enums.EnumTrainTicketIsAcceptNoSeat}
     */
    private int isAcceptNoSeat = 1;

    /**
     *  联系人姓名（ Unicode编码）
     */
    private String contactName;

    /**
     * 联系人手机
     */
    private String contactMobile;

    /**
     * 乘客列表， JSON格式
     */
    private List<passenger> passengers;


    @Data
    public class passenger{

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
         *  证件类型 1 为身份证 2 为护照 3 台湾通行证 4 港澳通行证
         *
         *  {@link com.jkm.enums.EnumCertificatesType}
         */
        private int cardType;

        /**
         * 证件号码 例如: "610423195608261254"
         */
        private String cardNo;

        /**
         *  出生日 例如： "1990-08-26"
         */
        private String birth;

        /**
         * 座位类型 : 9
         *
         * {@link com.jkm.enums.EnumTrainTicketSeatType}
         */
        private int seatType;

        /**
         * 座位价格，值如"1403.0"
         */
        private String ticketPrice;
    }

    /**
     * 获得订票签名字符串
     *
     * @return
     */
    public String getSignStr() {
        return new StringBuilder().append("terminalID").append("=").append(this.getTermTransID())
                .append("&").append("factoryID").append("=").append(this.getFactoryID())
                .append("&").append("reqDateTime").append("=").append(DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMddHHmmss))
                .append("&").append("termTransID").append("=").append(this.getTermTransID())
                .append("&").append("queryKey").append("=").append(this.getQueryKey())
                .append("&").append("key").append("=").append(YsSdkConstants.SIGN_KEY).toString();
    }

}
