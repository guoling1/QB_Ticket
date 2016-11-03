package com.jkm.service.hy;

import com.jkm.service.hy.entity.*;
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
     * 查询订单
     *
     * @param orderId
     * @param transactionId
     * @return
     */
    JSONObject QueryOrder(String orderId, String transactionId);

    /**
     * 线上退票
     */
    HyReturnTicketResponse returnTicket(final HyReturnTicketRequest request, final JSONArray tickets);

    /**
     * 提交保险单
     */
    JSONObject postPolicyOrder(final HyPostPolicyOrderRequest request);

    /**
     * 撤消保险订单
     */
    JSONObject cancelPolicyOrder(final HyCancelPolicyOrderRequest request);

    /**
     * 查询投保单状态
     */
    JSONObject queryPolicyOrder(final HyQueryPolicyOrderRequest request);

}
