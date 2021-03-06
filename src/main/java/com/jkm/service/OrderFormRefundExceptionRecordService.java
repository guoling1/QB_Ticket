package com.jkm.service;

import com.google.common.base.Optional;
import com.jkm.entity.OrderFormRefundExceptionRecord;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/11.
 */
public interface OrderFormRefundExceptionRecordService {

    /**
     * 插入
     *
     * @param orderFormRefundExceptionRecord
     */
    void insert(OrderFormRefundExceptionRecord orderFormRefundExceptionRecord);

    /**
     * 更新
     *
     * @param orderFormRefundExceptionRecord
     * @return
     */
    int update(OrderFormRefundExceptionRecord orderFormRefundExceptionRecord);

    /**
     * 将记录标记为已处理状态
     *
     * @param id
     * @return
     */
    int markProcessedById(long id);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<OrderFormRefundExceptionRecord> selectById(long id);

    /**
     * 按支付流水号查询
     *
     * @param paymentSn
     * @return
     */
    Optional<OrderFormRefundExceptionRecord> selectByPaymentSn(String paymentSn);

    /**
     * 查询所有未处理的异常记录（status=0 表示未处理， status = 1 表示已经处理）
     *
     * @return
     */
    List<OrderFormRefundExceptionRecord> selectAllExceptionRecord();
}
