package com.jkm.dao;

import com.jkm.entity.OrderFormDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yuxiang on 2016-10-27.
 */
public interface OrderFormDetailDao {

    /**
     * 插入乘客车票订单
     *
     * @param orderFormDetail
     */
    void insert(OrderFormDetail orderFormDetail);

    /**
     * 更新乘客车票订单
     *
     * @param orderFormDetail
     * @return
     */
    int update(OrderFormDetail orderFormDetail);

    /**
     * 按订单更新所有车票订单状态
     *
     * @param remark
     * @param status
     * @param orderFormId
     * @return
     */
    int updateStatusByOrderFormId(@Param("remark") String remark, @Param("status") int status, @Param("orderFormId") long orderFormId);

    /**
     *按id查询乘客车票
     *
     * @param id
     * @return
     */
    OrderFormDetail selectById(@Param("id") long id);

    /**
     * 按订单id查询所有车票
     *
     * @param orderFormId
     * @return
     */
    List<OrderFormDetail> selectByOrderFormId(@Param("orderFormId") long orderFormId);

    /**
     * 按订单id查询所有车票
     *
     * @param orderFormIds
     * @return
     */
    List<OrderFormDetail> selectByOrderFormIds(@Param("orderFormIds") List<Long> orderFormIds);

    /**
     * 按订单号和乘客id,票类型
     *
     * @param orderId
     * @param passengerId
     * @param piaoType
     * @return
     */
    OrderFormDetail selectByOrderFormIdAndPassengerIdAndPiaoType(@Param("orderId") String orderId, @Param("passengerId") int passengerId, @Param("piaoType") String piaoType);

    /**
     *
     * @param id
     */
    void updateStatusById(@Param("id") long id, @Param("status") int status, @Param("remark") String remark);
}
