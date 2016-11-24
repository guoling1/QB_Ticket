package com.jkm.service;


import com.jkm.entity.PayExceptionRecord;

public interface PayExceptionRecordService {
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    PayExceptionRecord selectByPrimaryKey (Long id );

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective( PayExceptionRecord record );

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKeySelective(PayExceptionRecord record );
    /**
     *
     * 添加 （根据条件）
     *
     **/
    int insertByCondition(String orderId,String reqSn,String type);
}
