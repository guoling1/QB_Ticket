package com.jkm.service;

import com.jkm.controller.helper.request.RequestBookTicket;
import com.jkm.service.hy.entity.HyRefundCallbackResponse;
import com.jkm.service.ys.entity.YsRefundCallbackResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yuxiang on 2016-10-27.
 */
public interface TicketService {


    /**
     * 订单提交
     *
     * @return
     */
    long submitOrder(RequestBookTicket requestBookTicket);


    /**
     * 订单提交--回调函数
     *
     * @return
     */
    void handleSubmitOrderCallbackResponse(JSONObject jsonObject);

    /**
     * 确认出票
     *
     * @param orderFormId
     * @return
     */
    Pair<Boolean, String> confirmOrder(long orderFormId);

    /**
     * 确认出票--回调函数
     *
     * @param jsonObject
     */
    void handleConfirmOrderCallbackResponse(JSONObject jsonObject);

    /**
     * 取消订单
     *
     * @param orderFormId
     * @return
     */
    Pair<Boolean, String> cancelOrder(long orderFormId);

    /**
     *  退票接口
     * @return
     */
    Pair<Boolean,String> refund(long orderFormDetailId);

    /**
     * 处理退票回调的异步通知
     *
     * @param response
     */
     void handleRefundCallbackMsg(HyRefundCallbackResponse response);

}
