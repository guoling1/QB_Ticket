package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.OrderFormDetailDao;
import com.jkm.entity.OrderFormDetail;
import com.jkm.service.OrderFormDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/10/27.
 */
@Service
public class OrderFormDetailServiceImpl implements OrderFormDetailService {

    @Autowired
    private OrderFormDetailDao orderFormDetailDao;

    /**
     * {@inheritDoc}
     *
     * @param orderFormDetail
     */
    @Override
    public void add(final OrderFormDetail orderFormDetail) {
        this.orderFormDetailDao.insert(orderFormDetail);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormDetail
     * @return
     */
    @Override
    public int update(final OrderFormDetail orderFormDetail) {
        return this.orderFormDetailDao.update(orderFormDetail);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<OrderFormDetail> selectById(final long id) {
        return Optional.fromNullable(this.orderFormDetailDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormId
     * @return
     */
    @Override
    public List<OrderFormDetail> selectByOrderFormId(final long orderFormId) {
        return this.orderFormDetailDao.selectByOrderFormId(orderFormId);
    }
}
