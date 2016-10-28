package com.jkm.service;

import com.jkm.controller.helper.request.RequestBookTicket;
import com.jkm.service.ys.entity.YsRefundCallbackResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-10-27.
 */
public interface TicketService {


    /**
     * 订票步骤一：初始化 票（保存）
     *
     * @return
     */
    void bookTicket(RequestBookTicket requestBookTicket);

    /**
     * 订票步骤三：(步骤二 客户付款后)，发送订票请求
     *
     * @return
     */
    Pair<Boolean, String> requestBookTicket(long orderFormId);

    /**
     *  退票接口
     * @return
     */
    Pair<Boolean,String> refund(long id);

    /**
     * 处理退票回调的异步通知
     *
     * @param response
     */
     void handleRefundCallbackMsg(YsRefundCallbackResponse response);


}
