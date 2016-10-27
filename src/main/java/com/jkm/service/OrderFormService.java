package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.OrderForm;
import org.apache.ibatis.annotations.Param;

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
    Optional<OrderForm> selectById(@Param("id") long id);

    /**
     *按id查询订单
     *
     * @param id
     * @return
     */
    Optional<OrderForm> selectByIdWithLock(@Param("id") long id);

    /**
     * 按用户id查询订单
     *
     * @param uid
     * @return
     */
    List<OrderForm> selectByUid(@Param("uid") long uid);

    /**
     *按流水号查询订单
     *
     * @param termTransId
     * @return
     */
    Optional<OrderForm> selectByTermTransId(@Param("termTransId") String termTransId);

}
