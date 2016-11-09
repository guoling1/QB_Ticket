package com.jkm.service;

import com.jkm.controller.helper.request.RequestGrabTicket;
import com.jkm.controller.helper.request.RequestSubmitOrder;
import com.jkm.entity.OrderForm;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

/**
 * Created by yuxiang on 2016-10-27.
 */
public interface TicketService {


    /**
     * 订单提交
     *
     * @return
     */
    Triple<Boolean, String, Long> submitOrder(RequestSubmitOrder requestSubmitOrder);


    /**
     * 订单提交--回调函数
     *
     * @return
     */
    void handleSubmitOrderCallbackResponse(JSONObject jsonObject);

    /**
     * 确认出票
     *
     * @param orderForm
     * @return
     */
    void confirmOrder(OrderForm orderForm);

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
     * @param jsonObject
     */
    void handleRefundCallbackMsg(JSONObject jsonObject);

    /**
     * 客户支付结果处理接口
     *
     * @param orderFormId
     * @param paymentSn
     * @param isPaySuccess
     */
    void handleCustomerPayMsg(long orderFormId, String paymentSn, boolean isPaySuccess);

    /**
     * 抢票受理
     * @return
     */
    Pair<Boolean,String> grabTicket(final RequestGrabTicket req);

    /**
     * 抢票客户支付结果处理接口
     *
     * @param grabTicketFormId
     * @param paymentSn
     * @param isPaySuccess
     */
    void handleGrabCustomerPayMsg(long grabTicketFormId, String paymentSn, boolean isPaySuccess) throws Exception;

    /**
     * 处理抢票回调通知
     * @param jsonParams
     */
    void handleGrabCallBackMsg(JSONObject jsonParams);

    /**
     * 取消抢票
     * @param grabTicketFormId
     * @return
     */
    Pair<Boolean,String> cancelGrabTicket(long grabTicketFormId) throws Exception;

    /**
     * 针对退款结果是退款中，或者失败的订单 处理退款结果
     *
     * @param orderFormId   订单id
     * @param isRefundSuccess  是否退款成功
     * @param msg   结果描述
     */
    void handleOrderFormRefundResult(long orderFormId, boolean isRefundSuccess, String msg);
}
