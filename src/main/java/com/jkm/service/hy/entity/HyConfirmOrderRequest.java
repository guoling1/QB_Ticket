package com.jkm.service.hy.entity;

import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeAlias;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeNoNull;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/1.
 *
 * 确认出票  --  请求参数
 */
@Data
public class HyConfirmOrderRequest extends HySdkRequest {

    /**
     * 客户订单号
     */
    @HySdkSerializeNoNull
    @HySdkSerializeAlias(name = "orderid")
    private String orderId;

    /**
     * 空铁订单号
     */
    @HySdkSerializeNoNull
    @HySdkSerializeAlias(name = "transactionid")
    private String transactionId;
}
