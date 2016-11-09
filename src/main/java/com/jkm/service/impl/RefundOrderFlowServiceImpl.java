package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.RefundOrderFlowDao;
import com.jkm.entity.RefundOrderFlow;
import com.jkm.service.RefundOrderFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2016/11/3.
 */
@Service
public class RefundOrderFlowServiceImpl implements RefundOrderFlowService {

    @Autowired
    private RefundOrderFlowDao refundOrderFlowDao;

    /**
     * {@inheritDoc}
     *
     * @param refundOrderFlow
     */
    @Override
    public void insert(final RefundOrderFlow refundOrderFlow) {
        this.refundOrderFlowDao.insert(refundOrderFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param refundOrderFlow
     * @return
     */
    @Override
    public int update(final RefundOrderFlow refundOrderFlow) {
        return this.refundOrderFlowDao.update(refundOrderFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<RefundOrderFlow> selectById(final long id) {
        return Optional.fromNullable(this.refundOrderFlowDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<RefundOrderFlow> selectByOrderFormId(final long id) {
        return Optional.fromNullable(this.refundOrderFlowDao.selectByOrderFormId(id));
    }
}
