package com.jkm.service.impl;

import com.jkm.dao.RefundTicketFlowDao;
import com.jkm.entity.RefundTicketFlow;
import com.jkm.service.RefundTicketFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-11-02.
 */
@Service
public class RefundTicketFlowServiceImpl implements RefundTicketFlowService {

    @Autowired
    private RefundTicketFlowDao refundTicketFlowDao;
    /**
     * {@inheritDoc}
     *
     * @param refundTicketFlow
     */
    @Override
    public void init(RefundTicketFlow refundTicketFlow) {
        this.refundTicketFlowDao.insert(refundTicketFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param ticketNo
     * @return
     */
    @Override
    public RefundTicketFlow getByTicketNo(String ticketNo) {
        return this.refundTicketFlowDao.selectByTicketNo(ticketNo);
    }

    /**
     * {@inheritDoc}
     *
     * @param flow
     */
    @Override
    public void update(RefundTicketFlow flow) {
        this.refundTicketFlowDao.update(flow);
    }

    /**
     * {@inheritDoc}
     *
     * @param reqtoken
     * @return
     */
    @Override
    public RefundTicketFlow getByReqToken(String reqtoken) {
        return this.refundTicketFlowDao.getByReqToken(reqtoken);
    }
}
