package com.jkm.helper.TicketMessageParams;

import lombok.Data;

/**
 * Created by yuxiang on 2016-11-16.
 */
@Data
public class SendReturnTicketDownParam {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 乘客姓名
     */
    private String userName;

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
    /**
     * 退款金额
     */
    private String amount;
    /**
     * 出发日期 yyyy-MM-dd
     */
    private String startDate;
}
