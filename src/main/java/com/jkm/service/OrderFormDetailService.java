package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.OrderFormDetail;

/**
 * Created by yulong.zhang on 2016/10/27.
 */
public interface OrderFormDetailService {

    /**
     * 插入订单
     *
     * @param orderFormDetail
     */
    void add(OrderFormDetail orderFormDetail);

    /**
     * 更新订单
     *
     * @param orderFormDetail
     * @return
     */
    int update(OrderFormDetail orderFormDetail);

    /**
     *按id查询订单
     *
     * @param id
     * @return
     */
    Optional<OrderFormDetail> selectById(long id);
}
