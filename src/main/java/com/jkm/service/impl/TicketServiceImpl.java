package com.jkm.service.impl;

import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import com.jkm.service.ys.YsSdkService;
import com.jkm.service.ys.entity.YsRefundCallbackResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Service
public class TicketServiceImpl implements TicketService{

    @Autowired
    private OrderFormDetailService orderFormDetailService;
    @Autowired
    private OrderFormService orderFormService;
    @Autowired
    private YsSdkService ysSdkService;
    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Pair<Boolean, String> refund() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param response
     */
    @Override
    public void handleRefundCallbackMsg(YsRefundCallbackResponse response) {

    }
}
