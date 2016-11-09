package com.jkm.dao;

import com.jkm.entity.ChargeMoneyOrder;
import org.apache.ibatis.annotations.Param;

/**
 * Created by yuxiang on 2016-11-02.
 */
public interface ChargeMoneyOrderDao {

    /**
     * 插入
     */
    void insert(ChargeMoneyOrder chargeMoneyOrder);

    /**
     * 更新
     *
     * @param chargeMoneyOrder
     * @return
     */
    int update(ChargeMoneyOrder chargeMoneyOrder);

    /**
     * 按id查询收款记录
     *
     * @param id
     * @return
     */
    ChargeMoneyOrder selectById(@Param("id") long id);

    /**
     * 按id查询收款记录
     *
     * @param id
     * @return
     */
    ChargeMoneyOrder selectByIdWithLock(@Param("id") long id);


    /**
     * 按订单id查询收款记录
     *
     * @param orderFormId
     * @return
     */
    ChargeMoneyOrder selectByOrderFormId(@Param("orderFormId") long orderFormId);

    /**
     * 查询
     *
     * @param grabTicketFormId
     * @return
     */
    ChargeMoneyOrder selectByGrabTicketFormId(long grabTicketFormId);
}
