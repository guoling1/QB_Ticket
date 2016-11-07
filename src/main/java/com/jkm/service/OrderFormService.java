package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.OrderForm;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/27.
 */
public interface OrderFormService {

    /**
     * 插入订单
     *
     * @param orderForm
     */
    long add(OrderForm orderForm);

    /**
     * 更新订单
     *
     * @param orderForm
     * @return
     */
    int update(OrderForm orderForm);

    /**
     *按id查询订单
     *
     * @param id
     * @return
     */
    Optional<OrderForm> selectById(long id);

    /**
     *按id查询订单
     *
     * @param id
     * @return
     */
    Optional<OrderForm> selectByIdWithLock(long id);

    /**
     * 按订单ids查询订单
     *
     * @param ids
     * @return
     */
    List<OrderForm> selectByIds(List<Long> ids);

    /**
     * 按用户id查询订单
     *
     * @param uid
     * @return
     */
    List<OrderForm> selectByUid(String uid);

    /**
     *按流水号查询订单
     *
     * @param orderId
     * @return
     */
    Optional<OrderForm> selectByOrderId(String orderId);

    /**
     *按合众交易流水号查询订单
     *
     * @param reqSn
     * @return
     */
    Optional<OrderForm> selectByReqSn(String reqSn);

    /**
     * 更新订单状态
     *
     * @param orderForm
     * @return
     */
    int updateStatus(OrderForm orderForm);

}
