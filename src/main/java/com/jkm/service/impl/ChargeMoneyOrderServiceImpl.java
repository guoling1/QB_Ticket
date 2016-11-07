package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.ChargeMoneyOrderDao;
import com.jkm.entity.ChargeMoneyOrder;
import com.jkm.service.ChargeMoneyOrderService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-11-02.
 */
@Service
class ChargeMoneyOrderServiceImpl implements ChargeMoneyOrderService {

    @Autowired
    private ChargeMoneyOrderDao chargeMoneyOrderDao;

    /**
     *{@inheritDoc}
     *
     * @param chargeMoneyOrder
     */
    @Override
    public void init(final ChargeMoneyOrder chargeMoneyOrder) {
        this.chargeMoneyOrderDao.insert(chargeMoneyOrder);
    }

    /**
     * {@inheritDoc}
     *
     * @param chargeMoneyOrder
     * @return
     */
    @Override
    public int update(final ChargeMoneyOrder chargeMoneyOrder) {
        return this.chargeMoneyOrderDao.update(chargeMoneyOrder);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ChargeMoneyOrder> selectById(final long id) {
        return Optional.fromNullable(this.chargeMoneyOrderDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ChargeMoneyOrder> selectByIdWithLock(final long id) {
        return Optional.fromNullable(this.chargeMoneyOrderDao.selectByIdWithLock(id));
    }


    /**
     * {@inheritDoc}
     *
     * @param orderFormId
     * @return
     */
    @Override
    public Optional<ChargeMoneyOrder> selectByOrderFormId(final long orderFormId) {
        return Optional.fromNullable(this.chargeMoneyOrderDao.selectByOrderFormId(orderFormId));
    }
}
