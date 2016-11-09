package com.jkm.service;

import com.jkm.entity.RefundTicketFlow;

/**
 * Created by yuxiang on 2016-11-02.
 */
public interface RefundTicketFlowService {
    /**
     * 初始化
     * @param refundTicketFlow
     */
    void init(RefundTicketFlow refundTicketFlow);

    /**
     * 根据票号查
     * @param ticketNo
     * @return
     */
    RefundTicketFlow getByTicketNo(String ticketNo);

    /**
     * 更新
     * @param flow
     */
    void update(RefundTicketFlow flow);
}
