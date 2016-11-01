package com.jkm.service.hy.entity;

import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeAlias;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/1.
 *
 * 提交订单 -- 相应参数
 */
@Data
public class HySubmitOrderResponse extends HySdkResponse {

    /**
     * 商户订单号
     */
    @HySdkSerializeAlias(name = "transactionid")
    private String transactionId;

    /**
     * 空铁订单号
     */
    @HySdkSerializeAlias(name = "orderid")
    private String orderId;

}
