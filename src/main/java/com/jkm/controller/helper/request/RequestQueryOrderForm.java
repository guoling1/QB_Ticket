package com.jkm.controller.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/10/31.
 *
 * 查询我的订单 -- 入参
 */
@Data
public class RequestQueryOrderForm {

    /**
     * 联系人的用户id
     */
    private String uid;

    /**
     * 订单id
     */
    private long orderFormId;
}
