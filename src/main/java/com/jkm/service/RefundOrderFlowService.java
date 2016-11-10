package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.GrabTicketForm;
import com.jkm.entity.RefundOrderFlow;

/**
 * Created by yulong.zhang on 2016/11/3.
 */
public interface RefundOrderFlowService {

    /**
     * 插入
     *
     * @param refundOrderFlow
     */
    void insert(RefundOrderFlow refundOrderFlow);

    /**
     * 更新
     *
     * @param refundOrderFlow
     * @return
     */
    int update(RefundOrderFlow refundOrderFlow);

    /**
     * 按id 查询
     *
     * @param id
     * @return
     */
    Optional<RefundOrderFlow> selectById(long id);

    /**
     * 按订单id 查询
     *
     * @param id
     * @return
     */
    Optional<RefundOrderFlow> selectByOrderFormId(long id);

    /**
     * 按抢票单id查询
     *
     * @param grabTicketFormId
     * @return
     */
    Optional<RefundOrderFlow> selectByGrabTicketFormId(long grabTicketFormId);
}
