package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.OrderFormDetail;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/27.
 */
public interface OrderFormDetailService {

    /**
     * 插入乘客车票
     *
     * @param orderFormDetail
     */
    void add(OrderFormDetail orderFormDetail);

    /**
     * 更新乘客车票
     *
     * @param orderFormDetail
     * @return
     */
    int update(OrderFormDetail orderFormDetail);

    /**
     *按id查询车票
     *
     * @param id
     * @return
     */
    Optional<OrderFormDetail> selectById(long id);

    /**
     * 按订单id查询所有车票
     *
     * @return
     */
    List<OrderFormDetail> selectByOrderFormId(long orderFormId);
}
