package com.jkm.helper.TicketMessageParams;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/8.
 */
@Data
public class SendBuyTicketFailParam {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 出发站点
     */
    private String startStation;

    /**
     * 到达站点
     */
    private String endStation;

    /**
     * 车次
     */
    private String trainNo;
}
