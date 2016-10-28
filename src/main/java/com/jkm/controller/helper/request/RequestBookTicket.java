package com.jkm.controller.helper.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/28.
 *
 * 订票请求参数
 */
@Data
@Builder
public class RequestBookTicket {

    /**
     * 用户id
     */
    private long uid;

    /**
     * 查询车次时的 queryKey
     */
    private String queryKey;

    /**
     * 请求流水号(接入商流水号)
     */
    private String termTransId;

    /**
     * 固定 ： 1 单程
     */
//    private int journeyType = 1;

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
     * 出发日期，值如 "2016-05-18 07:00"
     */
    private Date departDateTime;

    /**
     *  到達日期，值如 "2016-05-18 07:00"
     */
    private Date arriveDateTime;

    /**
     * 保险ID，值如 1 当为0时表示不购买保险
     * 当ID不为0时，默认乘客列表中的所有乘客都购买保险
     *
     * {@link com.jkm.enums.EnumTrainTicketInsure}
     */
    private int insureId;

    /**
     *  单个保险价格，值如 20 当为0时表示不购买保险
     */
    private BigDecimal insurePrice;

    /**
     * 是否合并占座和出票通知， 固定： 1
     */
//    private int isMergeNotice = 1;

    /**
     * 无票时是否接受无座， 0 接受、 1 不接受
     *
     * {@link com.jkm.enums.EnumTrainTicketIsAcceptNoSeat}
     */
    private int isAcceptNoSeat;

    /**
     *  联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机
     */
    private String contactMobile;

    /**
     * 票价
     */
    private BigDecimal price;

    /**
     * 乘客列表， JSON格式
     */
    private List<passenger> passengers;

    @Data
    public class passenger{

        /**
         * 联系人id
         */
        private long contractFormId;

        /**
         * 座位类型 : 9
         *
         * {@link com.jkm.enums.EnumTrainTicketSeatType}
         */
        private int seatType;

        /**
         * 座位价格，值如"1403.0"
         */
        private BigDecimal ticketPrice;
    }

}
