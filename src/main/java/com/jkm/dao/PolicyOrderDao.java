package com.jkm.dao;

import com.jkm.entity.PolicyOrder;
import org.apache.ibatis.annotations.Param;

/**
 * Created by yuxiang on 2016-11-02.
 */
public interface PolicyOrderDao {

    /**
     * 插入保单
     * @param policyOrder
     */
    void insert(PolicyOrder policyOrder);

    /**
     * 更新保单状态
     */
    void updateStatusById(@Param("status") int status, @Param("id") long id);
}
