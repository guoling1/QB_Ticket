package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.jkm.dao.OrderFormDetailDao;
import com.jkm.entity.OrderForm;
import com.jkm.entity.OrderFormDetail;
import com.jkm.service.OrderFormDetailService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
     * @param remark
     * @param status
     * @param orderFormId
     * @return
     */
    @Override
    public int updateStatusByOrderFormId(final String remark, final int status, final long orderFormId) {
        return this.orderFormDetailDao.updateStatusByOrderFormId(remark, status, orderFormId);
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

    /**
     * {@inheritDoc}
     *
     * @param orderFormIds
     * @return
     */
    @Override
    public List<OrderFormDetail> selectByOrderFormIds(final List<Long> orderFormIds) {
        if (CollectionUtils.isEmpty(orderFormIds)) {
            return Collections.emptyList();
        }
        return this.orderFormDetailDao.selectByOrderFormIds(orderFormIds);
    }

    /**
     * {@inheritDoc}
     *
     * @param cardNo
     * @param passengerType
     * @return
     */
    @Override
    public Optional<OrderFormDetail> selectByCardNoAndPassengerType(final String cardNo, final int passengerType) {
        return Optional.fromNullable(this.orderFormDetailDao.selectByCardNoAndPassengerType(cardNo, passengerType));
    }
}
