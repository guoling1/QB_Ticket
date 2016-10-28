package com.jkm.service.ys.entity;

import lombok.Builder;
import lombok.Data;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Data
@Builder
public class YsRefundTicketRequest extends YsSdkRequest{

    /**
     * 请求流水号
     */
    private String termTransID;

    /**
     * 平台交易流水
     */
    private String transID;

    /**
     * 退票乘车人的乘客ID
     */
    private String passengerID;
}
