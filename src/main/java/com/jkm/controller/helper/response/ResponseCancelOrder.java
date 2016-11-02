package com.jkm.controller.helper.response;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/2.
 *
 * 取消订单--出参
 */
@Data
public class ResponseCancelOrder {

    /**
     * 订单id
     */
    private long orderFormId;

    /**
     * 提示信息
     */
    private String msg;
}
