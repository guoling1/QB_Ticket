package com.jkm.service.hy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by yuxiang on 2016-11-01.
 */
@Data
public class HyReturnTicketRequest extends HySdkRequest {

    /**
     *使用方订单号
     */
    @JsonProperty("orderid")
    private String orderId;
    /**
     *我方交易单号
     */
    @JsonProperty("transactionid")
    private String transactionId;
    /**
     *取票单号
     */
    @JsonProperty("ordernumber")
    private String orderNumber;
    /**
     *请求特征（唯一）
     */
    @JsonProperty("reqtoken")
    private String reqToken;
    /**
     *异步通知接口回调地址
     */
    @JsonProperty("callbackurl")
    private String callBackUrl;
    /**
     *车票信息（json字符串数组形式
     */
    @JsonProperty("tickets")
    private List<PassengerInfo> tickets;
    /**
     *
     */
    @JsonProperty("loginusername")
    private String loginUserName;
    /**
     *
     */
    @JsonProperty("loginuserpassword")
    private String loginUserPassword;

}


