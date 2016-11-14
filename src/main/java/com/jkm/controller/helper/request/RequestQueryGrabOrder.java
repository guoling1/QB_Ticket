package com.jkm.controller.helper.request;

import lombok.Data;

/**
 * Created by yuxiang on 2016-11-14.
 */
@Data
public class RequestQueryGrabOrder {

    private String appid;

    /**
     * 联系人的用户id
     */
    private String uid;

    /**
     * 订单id
     */
    private long grabTicketFormId;
}
