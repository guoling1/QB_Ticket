package com.jkm.dao;

import com.jkm.entity.OrderForm;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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
     * 按订单ids查询订单
     *
     * @param ids
     * @return
     */
    List<OrderForm> selectByIds(@Param("ids") List<Long> ids);

    /**
     * 按用户id查询订单
     *
     * @param uid
     * @return
     */
    List<OrderForm> selectByUid(@Param("uid") String uid);

    /**
     * 按流水号查询订单
     *
     * @param orderId
     * @return
     */
    OrderForm selectByOrderId(@Param("orderId") String orderId);


    /**
     * 按合众交易流水号流水号查询订单
     *
     * @param reqSn
     * @return
     */
    OrderForm selectByReqSn(@Param("reqSn") String reqSn);
    /**
     * 更新订单状态
     *
     * @param orderForm
     * @return
     */
    int updateStatus(OrderForm orderForm);

    /**
     * 查询过了支付时间的订单
     *
     * @param expireDate
     * @param statusList
     * @return
     */
    List<OrderForm> selectExpiredOrderForms(@Param("expireDate") Date expireDate, @Param("statusList") List<Integer> statusList);
}
