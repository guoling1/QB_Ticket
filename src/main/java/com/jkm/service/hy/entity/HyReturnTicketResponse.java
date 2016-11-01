package com.jkm.service.hy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by yuxiang on 2016-11-01.
 */
@Data
public class HyReturnTicketResponse extends HySdkResponse{

    /**
     *使用方订单号
     */
    @JsonProperty("orderid")
    private String orderId;
    /**
     *取票单号
     */
    @JsonProperty("ordernumber")
    private String orderNumber;
    /**
     *备注
     */
    @JsonProperty("tooltip")
    private String toolTip;
    /**
     *请求特征(将请求传入的值直接回传)
     */
    @JsonProperty("reqtoken")
    private String reqToken;
}
