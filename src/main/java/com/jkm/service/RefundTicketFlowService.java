package com.jkm.service;

import com.jkm.entity.RefundTicketFlow;

/**
 * Created by yuxiang on 2016-11-02.
 */
public interface RefundTicketFlowService {

    void init(RefundTicketFlow refundTicketFlow);

    RefundTicketFlow getByTicketNo(String ticketNo);

    void update(RefundTicketFlow flow);
}
