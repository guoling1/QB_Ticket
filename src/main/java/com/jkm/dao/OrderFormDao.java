package com.jkm.dao;

import com.jkm.entity.OrderForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/27.
 */
public interface OrderFormDao {

    /**
     * 插入订单
     *
     * @param orderForm
     */
    void insert(OrderForm orderForm);

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
    OrderForm selectById(@Param("id") long id);

    /**
     *按id查询订单
     *
     * @param id
     * @return
     */
    OrderForm selectByIdWithLock(@Param("id") long id);

    /**
     * 按用户id查询订单
     *
     * @param uid
     * @return
     */
    List<OrderForm> selectByUid(@Param("uid") String uid);

    /**
     *按流水号查询订单
     *
     * @param termTransId
     * @return
     */
    OrderForm selectByTermTransId(@Param("termTransId") String termTransId);


}
