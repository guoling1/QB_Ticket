package com.jkm.dao;

import com.jkm.entity.ReturnMoneyOrder;
import org.apache.ibatis.annotations.Param;

/**
 * Created by yuxiang on 2016-11-02.
 */
public interface ReturnMoneyOrderDao {

    /**
     * 插入
     */
    void insert(ReturnMoneyOrder returnMoneyOrder);

    /**
     * 更新
     * @param id
     * @param status
     */
    void updateStatusById(@Param("id") long id, @Param("status") int status);
}
