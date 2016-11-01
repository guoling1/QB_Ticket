package com.jkm.service.hy.entity;

import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeAlias;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/1.
 */
@Data
public class HyConfirmOrderCallbackResponse {


    /**
     * 商户订单号
     */
    @HySdkSerializeAlias(name = "orderid")
    private String orderId;

    /**
     * 空铁订单号
     */
    @HySdkSerializeAlias(name = "transactionid")
    private String transactionId;

    /**
     * 签名
     */
    private String sign;

    /**
     * 请求时间
     */
    @HySdkSerializeAlias(name = "reqtime")
    private String reqTime;

    /**
     * 是否出票成功（Y/N）
     */
    @HySdkSerializeAlias(name = "isSuccess")
    private String isSuccess;

    private String code;

    private String msg;

}
