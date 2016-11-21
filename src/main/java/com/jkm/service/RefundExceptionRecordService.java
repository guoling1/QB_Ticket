package com.jkm.service;


import com.jkm.entity.RefundExceptionRecord;

public interface RefundExceptionRecordService {
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    RefundExceptionRecord selectByPrimaryKey (Long id );

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective( RefundExceptionRecord record );

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKeySelective( RefundExceptionRecord record );
    /**
     *
     * 添加 （根据条件）
     *
     **/
    int insertByCondition(String orderId,String reqSn,String type);
}
