package com.jkm.service.hy.entity;

import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeAlias;
import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeNoNull;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/1.
 */
@Data
public class HyConfirmOrderResponse extends HySdkResponse {


    /**
     * 暂为空
     */
    private String changeserial;

    /**
     * 取票号
     */
    @HySdkSerializeNoNull
    @HySdkSerializeAlias(name = "ordernumber")
    private String orderNumber;

    /**
     * 商户订单号
     */
    @HySdkSerializeNoNull
    @HySdkSerializeAlias(name = "orderid")
    private String orderId;

}
