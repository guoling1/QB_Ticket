package com.jkm.service;

import net.sf.json.JSONObject;

/**
 * Created by yulong.zhang on 2016/11/17.
 */
public interface DaiGouTrainTicketService {

    /**
     * 从三方查询订单状态
     *
     * @param orderId
     * @param outOrderId
     * @return
     */
    JSONObject queryOrder(String orderId, String outOrderId);
}
