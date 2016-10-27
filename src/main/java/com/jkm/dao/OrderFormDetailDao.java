package com.jkm.dao;

import com.jkm.entity.OrderFormDetail;
import org.apache.ibatis.annotations.Param;

/**
 * Created by yuxiang on 2016-10-27.
 */
public interface OrderFormDetailDao {

    /**
     * 插入订单
     *
     * @param orderFormDetail
     */
    void insert(OrderFormDetail orderFormDetail);

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
    OrderFormDetail selectById(@Param("id") long id);

}
