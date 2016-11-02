package com.jkm.controller.helper.response;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/10/28.
 *
 * 订票出参
 */
@Data
public class ResponseSubmitOrder {

    /**
     * 订单id
     */
    private long orderFormId;

    /**
     * 提示内容
     */
    private String msg;
}
