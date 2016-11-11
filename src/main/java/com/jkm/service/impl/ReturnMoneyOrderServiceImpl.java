package com.jkm.service.impl;

import com.jkm.dao.ReturnMoneyOrderDao;
import com.jkm.entity.ReturnMoneyOrder;
import com.jkm.enums.EnumReturnMoneyOrderStatus;
import com.jkm.service.ReturnMoneyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-11-02.
 */
@Service
public class ReturnMoneyOrderServiceImpl implements ReturnMoneyOrderService {

    @Autowired
    private ReturnMoneyOrderDao returnMoneyOrderDao;

    /**
     *初始化
     */
    @Override
    public void init(ReturnMoneyOrder returnMoneyOrder) {
        this.returnMoneyOrderDao.insert(returnMoneyOrder);
    }

    /**
     * {@inheritDoc}
     * @param id
     * @param status
     */
    @Override
    public void updateStatusById(long id, EnumReturnMoneyOrderStatus status) {
        this.returnMoneyOrderDao.updateStatusById(id, status.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param orderFormDetailId
     * @return
     */
    @Override
    public ReturnMoneyOrder selectByOrderFormDetailId(long orderFormDetailId) {
        return this.returnMoneyOrderDao.selectByOrderFormDetailId(orderFormDetailId);
    }


}
