package com.jkm.service;


import com.jkm.entity.RefundResultRecord;

public interface RefundResultRecordService {
    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective(RefundResultRecord record);
}
