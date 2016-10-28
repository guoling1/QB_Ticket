package com.jkm.service;

import com.jkm.service.ys.entity.YsRefundCallbackResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-10-27.
 */
public interface TicketService {


    /**
     *  退票接口
     * @return
     */
    Pair<Boolean,String> refund();

    /**
     * 处理退票回调的异步通知
     *
     * @param response
     */
     void handleRefundCallbackMsg(YsRefundCallbackResponse response);


}