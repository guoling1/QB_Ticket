package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.OrderFormDao;
import com.jkm.entity.OrderForm;
import com.jkm.enums.EnumOrderFormDetailStatus;
import com.jkm.enums.EnumOrderFormStatus;
import com.jkm.service.OrderFormDetailService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/27.
 */
@Service
public class OrderFormServiceImpl implements OrderFormService {

    private static Logger log = Logger.getLogger(OrderFormServiceImpl.class);

    @Autowired
    private OrderFormDao orderFormDao;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private OrderFormDetailService orderFormDetailService;

    /**
     * {@inheritDoc}
     *
     * @param orderForm
     */
    @Override
    public long add(final OrderForm orderForm) {
        this.orderFormDao.insert(orderForm);
        return orderForm.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param orderForm
     * @return
     */
    @Override
    public int update(final OrderForm orderForm) {
        return this.orderFormDao.update(orderForm);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<OrderForm> selectById(final long id) {
        return Optional.fromNullable(this.orderFormDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<OrderForm> selectByIdWithLock(final long id) {
        return Optional.fromNullable(this.orderFormDao.selectByIdWithLock(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param ids
     * @return
     */
    @Override
    public List<OrderForm> selectByIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return this.orderFormDao.selectByIds(ids);
    }

    /**
     * {@inheritDoc}
     *
     * @param uid
     * @return
     */
    @Override
    public List<OrderForm> selectByUid(final String uid) {
        return this.orderFormDao.selectByUid(uid);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @return
     */
    @Override
    public Optional<OrderForm> selectByOrderId(final String orderId) {
        return Optional.fromNullable(this.orderFormDao.selectByOrderId(orderId));
    }

    /**
     * {@inheritDoc}
     *
     * @param reqSn
     * @return
     */
    @Override
    public Optional<OrderForm> selectByReqSn(final String reqSn) {
        return Optional.fromNullable(this.orderFormDao.selectByReqSn(reqSn));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderForm
     * @return
     */
    @Override
    public int updateStatus(final OrderForm orderForm) {
        orderForm.setRemark(EnumOrderFormStatus.of(orderForm.getStatus()).getValue());
        return this.orderFormDao.updateStatus(orderForm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleExpiredOrderForm() {
        final ArrayList<Integer> statusList = new ArrayList<>();
        statusList.add(EnumOrderFormStatus.ORDER_FORM_OCCUPY_SEAT_TRUE.getId());
        statusList.add(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_FAIL.getId());
        final List<OrderForm> orderForms = this.orderFormDao.selectExpiredOrderForms(new Date(), statusList);
        if (!CollectionUtils.isEmpty(orderForms)) {
            for (OrderForm orderForm : orderForms) {
                final Pair<Boolean, String> resultPair = this.ticketService.cancelOrder(orderForm.getId());
                if (!resultPair.getLeft()) {//如果取消不成功，主动取消订单
                    log.info("定时处理过期未支付的订单[" + orderForm.getId() + "]，请求第三方取消订单失败，主动取消订单---begin");
                    orderForm.setStatus(EnumOrderFormStatus.ORDER_FORM_CANCEL.getId());
                    orderForm.setRemark(EnumOrderFormStatus.ORDER_FORM_CANCEL.getValue());
                    this.orderFormDao.update(orderForm);
                    this.orderFormDetailService.updateStatusByOrderFormId(EnumOrderFormDetailStatus.TICKET_ORDER_CANCEL.getValue(),
                            EnumOrderFormDetailStatus.TICKET_ORDER_CANCEL.getId(), orderForm.getId());
                    log.info("定时处理过期未支付的订单[" + orderForm.getId() + "]，请求第三方取消订单失败，主动取消订单---done");
                }
            }
        }
    }
}
