package com.jkm.service;

import com.jkm.entity.MerchantInfo;

/**
 * Created by Administrator on 2016/11/21.
 */
public interface MerchantInfoService {
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    MerchantInfo selectByPrimaryKey(Long id);

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective(MerchantInfo record);

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKeySelective(MerchantInfo record);
}
