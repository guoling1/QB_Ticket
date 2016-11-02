package com.jkm.service.hy;

import com.jkm.entity.OrderForm;
import com.jkm.service.hy.entity.HyReturnTicketRequest;
import com.jkm.service.hy.entity.HyReturnTicketResponse;
import com.jkm.service.hy.entity.HySubmitOrderRequest;
import com.jkm.service.hy.entity.HySubmitOrderResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by yuxiang on 2016-11-01.
 */
public interface HySdkService {

    /**
     * 订单提交
     *
     * @param orderform
     * @param passengers
     * @return
     */
    JSONObject submitOrderImpl(OrderForm orderform, JSONArray passengers);

    /**
     * 确认出票
     *
     * @param orderId
     * @param transactionId
     * @return
     */
    JSONObject confirmTrainTicket(String orderId, String transactionId);

    /**
     * 取消订单
     *
     * @param orderId
     * @param transactionId
     * @return
     */
    JSONObject cancelOrder(String orderId, String transactionId);

    /**
     * 线上退票
     */
    HyReturnTicketResponse returnTicket(final HyReturnTicketRequest request);

}
