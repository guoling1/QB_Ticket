package com.jkm.service.impl;

import com.jkm.service.TicketService;
import com.jkm.service.ys.entity.YsRefundCallbackResponse;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Service
public class TicketServiceImpl implements TicketService{


    /**
     * {@inheritDoc}
     *
     * @param response
     */
    @Override
    public void handleRefundCallbackMsg(YsRefundCallbackResponse response) {

    }
}
