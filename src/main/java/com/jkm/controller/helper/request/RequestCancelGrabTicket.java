package com.jkm.controller.helper.request;

import lombok.Data;

/**
 * Created by yuxiang on 2016-11-08.
 */
@Data
public class RequestCancelGrabTicket {

    /**
     * 小订单号
     */
    private long grabTicketFormId;
}
