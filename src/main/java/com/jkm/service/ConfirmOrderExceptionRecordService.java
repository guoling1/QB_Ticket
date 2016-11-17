package com.jkm.service;

import com.jkm.entity.ConfirmOrderExceptionRecord;

/**
 * Created by yulong.zhang on 2016/11/17.
 */
public interface ConfirmOrderExceptionRecordService {

    /**
     * 添加
     *
     * @param confirmOrderExceptionRecord
     * @return
     */
    long add(ConfirmOrderExceptionRecord confirmOrderExceptionRecord);

    /**
     * 标记为已处理
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
    ConfirmOrderExceptionRecord selectById(long id);


    /**
     * 按id查询
     *
     * @param orderFormId
     * @return
     */
    ConfirmOrderExceptionRecord selectByOrderFormId(long orderFormId);
}
