package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.OrderFormDao;
import com.jkm.entity.OrderForm;
import com.jkm.service.OrderFormService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/27.
 */
@Service
public class OrderFormServiceImpl implements OrderFormService {

    @Autowired
    private OrderFormDao orderFormDao;

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
        return this.orderFormDao.updateStatus(orderForm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleExpiredOrderForm() {
//        List<OrderForm> orderForms = this.orderFormDao.selectExpiredOrderForms();
    }
}
