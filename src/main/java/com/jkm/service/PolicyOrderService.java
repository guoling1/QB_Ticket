package com.jkm.service;

import com.jkm.entity.PolicyOrder;
import com.jkm.enums.EnumPolicyOrderStatus;

/**
 * Created by yuxiang on 2016-11-02.
 */
public interface PolicyOrderService {

    /**
     * 初始化保单
     *
     * @param policyOrder
     */
    void init(PolicyOrder policyOrder);

    /**
     * 根据小订单ID查询保单
     */
    PolicyOrder getByOrderFormDetailId(long orderFormDetailId);

    /**
     * 更新状态
     */
    void updateStatusById(long id , EnumPolicyOrderStatus status);

    /**
     * 批量购买保险单
     *
     * @param orderFormId
     */
    void batchBuyPolicy(long orderFormId);
}
