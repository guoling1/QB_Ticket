package com.jkm.dao;

import com.jkm.entity.ConfirmOrderExceptionRecord;
import org.apache.ibatis.annotations.Param;

/**
 * Created by yulong.zhang on 2016/11/17.
 */
public interface ConfirmOrderExceptionRecordDao {

    /**
     * 添加
     *
     * @param confirmOrderExceptionRecord
     * @return
     */
    void insert(ConfirmOrderExceptionRecord confirmOrderExceptionRecord);

    /**
     * 将记录标记为已处理状态
     *
     * @param id
     * @return
     */
    int markProcessedById(@Param("id") long id);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    ConfirmOrderExceptionRecord selectById(@Param("id") long id);


    /**
     * 按id查询
     *
     * @param orderFormId
     * @return
     */
    ConfirmOrderExceptionRecord selectByOrderFormId(@Param("orderFormId") long orderFormId);
}
