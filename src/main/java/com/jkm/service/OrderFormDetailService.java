package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.GrabTicketForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.enums.EnumOrderFormDetailStatus;
import com.jkm.enums.EnumOrderFormStatus;

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
     * 按订单id更新所有乘客车票状态
     *
     * @param remark
     * @param status
     * @param orderFormId
     * @return
     */
    int updateStatusByOrderFormId(String remark, int status, long orderFormId);

    /**
     *按id查询车票
     *
     * @param id
     * @return
     */
    Optional<OrderFormDetail> selectById(long id);

    /**
     *按id查询车票
     *
     * @param id
     * @return
     */
    Optional<OrderFormDetail> selectByIdWithLock(long id);

    /**
     * 按订单id查询所有车票
     *
     * @return
     */
    List<OrderFormDetail> selectByOrderFormId(long orderFormId);

    /**
     * 按订单ids查询所有车票
     *
     * @return
     */
    List<OrderFormDetail> selectByOrderFormIds(List<Long> orderFormIds);

    /**
     * 按订单号和乘客id,票类型
     *
     * @param orderId
     * @param passengerId
     * @param piaoType
     * @return
     */
    Optional<OrderFormDetail> selectByOrderFormIdAndPassengerIdAndPiaoType(long orderId, int passengerId, String piaoType);

    /**
     * 根新状态
     */
    void updateStatusById(long id , EnumOrderFormDetailStatus status);

    /**
     * 查询
     * @param grabTicketFormId
     * @return
     */
    List<OrderFormDetail> selectByGrabTicketFormId(long grabTicketFormId);

    /**
     * 查询
     * @param orderFormId
     * @param orderFormRefundSuccess
     * @return
     */
    List<OrderFormDetail> selectAllTicketsNoReFund(long orderFormId, EnumOrderFormDetailStatus orderFormRefundSuccess);

    /**
     * 查询
     * @param orderFormId
     * @return
     */
    long selectOrderFormNum(long orderFormId);

    /**
     *
     * @param grabOrderFormId
     * @param ticketReturnSuccess
     * @return
     */
    List<OrderFormDetail> selectAllTicketsNoReFundGrab(long grabOrderFormId, EnumOrderFormDetailStatus ticketReturnSuccess);

    /**
     *
     * @param grabOrderFormId
     * @return
     */
    long selectGrabFormNumGrab(long grabOrderFormId);

    /**
     * 根据票号查
     * @param ticketNo
     * @return
     */
    Optional<OrderFormDetail> selectByTicketNo(String ticketNo);

    /**
     * 根据抢票单号查
     * @param grabTicketFormIds
     * @return
     */
    List<OrderFormDetail> selectByGrabTicketFormIds(List<Long> grabTicketFormIds);
}
