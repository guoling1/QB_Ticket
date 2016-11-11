package com.jkm.dao;

import com.jkm.entity.RefundTicketFlow;
import org.apache.ibatis.annotations.Param;

/**
 * Created by yuxiang on 2016-11-02.
 */
public interface RefundTicketFlowDao {

    /**
     * 插入流水
     * @param refundTicketFlow
     */
    void insert(RefundTicketFlow refundTicketFlow);

    /**
     *
     */
    void updateStatus(@Param("id") long id, @Param("status") int status);

    /**
     *
     * @param ticketNo
     * @return
     */
    RefundTicketFlow selectByTicketNo(@Param("ticketNo") String ticketNo);

    /**
     *
     * @param flow
     */
    void update(RefundTicketFlow flow);

    /**
     *
     * @param reqtoken
     * @return
     */
    RefundTicketFlow getByReqToken(@Param("reqtoken") String reqtoken);
}
