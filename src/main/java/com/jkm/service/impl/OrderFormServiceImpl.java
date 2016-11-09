package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.OrderFormDao;
import com.jkm.entity.OrderForm;
import com.jkm.entity.RefundOrderFlow;
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
    public void handleExpiredOrderForm(final long orderFormId) {
        final OrderForm orderForm = this.orderFormDao.selectById(orderFormId);
        if (orderForm.isCanCancelOrder()) {
            this.ticketService.cancelOrder(orderFormId);
        }
    }
}
