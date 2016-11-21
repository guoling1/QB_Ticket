package com.jkm.dao;


import com.jkm.entity.RefundExceptionRecord;
import org.apache.ibatis.annotations.Param;

public interface RefundExceptionRecordDao {
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    RefundExceptionRecord selectByPrimaryKey (@Param("id") Long id );

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
}
