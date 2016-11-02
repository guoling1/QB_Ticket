package com.jkm.controller.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/2.
 *
 * 取消订单--入参
 */
@Data
public class RequestCancelOrder {
    /**
     * 订单id
     */
    private long orderFormId;
}
