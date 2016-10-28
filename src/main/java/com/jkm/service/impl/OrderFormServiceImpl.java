package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.OrderFormDao;
import com.jkm.entity.OrderForm;
import com.jkm.service.OrderFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @param termTransId
     * @return
     */
    @Override
    public Optional<OrderForm> selectByTermTransId(final String termTransId) {
        return Optional.fromNullable(this.orderFormDao.selectByTermTransId(termTransId));
    }
}
