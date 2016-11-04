package com.jkm.service;

import com.jkm.entity.ReturnMoneyOrder;
import com.jkm.enums.EnumReturnMoneyOrderStatus;

/**
 * Created by yuxiang on 2016-11-02.
 */
public interface ReturnMoneyOrderService {

    /**
     *
     * @param returnMoneyOrder
     */
    void init(ReturnMoneyOrder returnMoneyOrder);

    /**
     * 更新状态
     * @param id
     * @param status
     */
    void updateStatusById(long id, EnumReturnMoneyOrderStatus status);
}
