package com.jkm.service;

import com.jkm.controller.helper.request.RequestBookTicket;
import com.jkm.service.ys.entity.YsRefundCallbackResponse;
import com.jkm.service.ys.entity.YsTrainTicketBookingCallbackResponse;
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
     * 订票步骤四： 回调，确定是否订票成功
     *
     * @return
     */
    Pair<Boolean, String> handleBookTicketCallbackResponse(YsTrainTicketBookingCallbackResponse response);

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
     void handleRefundCallbackMsg(YsRefundCallbackResponse response);


}
