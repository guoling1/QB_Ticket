package com.jkm.service.impl;

import com.jkm.dao.ChargeMoneyOrderDao;
import com.jkm.entity.ChargeMoneyOrder;
import com.jkm.service.ChargeMoneyOrderService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yuxiang on 2016-11-02.
 */
@Data
class ChargeMoneyOrderServiceImpl implements ChargeMoneyOrderService{

    @Autowired
    private ChargeMoneyOrderDao chargeMoneyOrderDao;
    /**
     *
     * @param chargeMoneyOrder
     */
    @Override
    public void init(ChargeMoneyOrder chargeMoneyOrder) {
        this.chargeMoneyOrderDao.insert(chargeMoneyOrder);
    }
}
