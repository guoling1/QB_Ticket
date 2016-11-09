package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.ChargeMoneyOrder;

/**
 * Created by yuxiang on 2016-11-02.
 */
public interface ChargeMoneyOrderService {

    /**
     * 初始化 商户收款记录
     *
     * @param chargeMoneyOrder
     */
    void init(ChargeMoneyOrder chargeMoneyOrder);

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
    Optional<ChargeMoneyOrder> selectById(long id);

    /**
     * 按id查询收款记录
     *
     * @param id
     * @return
     */
    Optional<ChargeMoneyOrder> selectByIdWithLock(long id);

    /**
     * 按订单id查询收款记录
     *
     * @param orderFormId
     * @return
     */
    Optional<ChargeMoneyOrder> selectByOrderFormId(long orderFormId);

    /**
     * 按抢票单id查询收款记录
     *
     * @param grabTicketFormId
     * @return
     */
    Optional<ChargeMoneyOrder> selectByGrabTicketFormId(long grabTicketFormId);
}
