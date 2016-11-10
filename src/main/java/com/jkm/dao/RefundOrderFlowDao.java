package com.jkm.dao;

import com.jkm.entity.GrabTicketForm;
import com.jkm.entity.RefundOrderFlow;
import org.apache.ibatis.annotations.Param;

/**
 * Created by yulong.zhang on 2016/11/3.
 */
public interface RefundOrderFlowDao {

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
    RefundOrderFlow selectById(@Param("id") long id);

    /**
     * 按id 查询
     *
     * @param orderFormId
     * @return
     */
    RefundOrderFlow selectByOrderFormId(@Param("orderFormId") long orderFormId);

    /**
     * 按抢票单id查询
     *
     * @param grabTicketFormId
     * @return
     */
    RefundOrderFlow selectByGrabTicketFormId(@Param("grabTicketFormId") long grabTicketFormId);
}
