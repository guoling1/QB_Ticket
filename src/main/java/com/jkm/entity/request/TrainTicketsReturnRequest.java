package com.jkm.entity.request;

import lombok.Data;

/**
 * Created by zhangbin on 2016/10/25.
 */
@Data
public class TrainTicketsReturnRequest {
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
