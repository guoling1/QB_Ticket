package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.OrderFormRefundExceptionRecordDao;
import com.jkm.entity.OrderFormRefundExceptionRecord;
import com.jkm.service.OrderFormRefundExceptionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/11.
 */
@Service
public class OrderFormRefundExceptionRecordServiceImpl implements OrderFormRefundExceptionRecordService {

    @Autowired
    private OrderFormRefundExceptionRecordDao orderFormRefundExceptionRecordDao;
    /**
     * {@inheritDoc}
     *
     * @param orderFormRefundExceptionRecord
     */
    @Override
    @Transactional
    public void insert(final OrderFormRefundExceptionRecord orderFormRefundExceptionRecord) {
        this.orderFormRefundExceptionRecordDao.insert(orderFormRefundExceptionRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormRefundExceptionRecord
     * @return
     */
    @Override
    @Transactional
    public int update(final OrderFormRefundExceptionRecord orderFormRefundExceptionRecord) {
        return this.orderFormRefundExceptionRecordDao.update(orderFormRefundExceptionRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int markProcessedById(final long id) {
        return this.orderFormRefundExceptionRecordDao.markProcessedById(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<OrderFormRefundExceptionRecord> selectById(final long id) {
        return Optional.fromNullable(this.orderFormRefundExceptionRecordDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSn
     * @return
     */
    @Override
    public Optional<OrderFormRefundExceptionRecord> selectByPaymentSn(final String paymentSn) {
        return Optional.fromNullable(this.orderFormRefundExceptionRecordDao.selectByPaymentSn(paymentSn));
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<OrderFormRefundExceptionRecord> selectAllExceptionRecord() {
        return this.orderFormRefundExceptionRecordDao.selectAllExceptionRecord();
    }
}
