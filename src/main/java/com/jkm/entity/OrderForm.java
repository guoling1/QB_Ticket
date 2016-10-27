package com.jkm.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/10/27.
 *
 * 订单
 *
 * tb_order_form
 *
 * {@link com.jkm.enums.EnumOrderFormStatus}
 */
@Data
public class OrderForm extends BaseEntity {

    /**
     * 查询车次时的 queryKey
     */
    private String queryKey;

    /**
     * 请求流水号
     */
    private String termTransId;

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
    private int insureId;

    /**
     *  单个保险价格，值如 20 当为0时表示不购买保险
     */
    private String insurePrice;

    /**
     * 是否合并占座和出票通知， 固定： 1
     */
    private int isMergeNotice = 1;

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
}
