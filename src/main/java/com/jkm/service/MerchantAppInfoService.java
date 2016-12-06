package com.jkm.service;

import com.jkm.entity.MerchantAppInfo;

/**
 * Created by Administrator on 2016/11/21.
 */
public interface MerchantAppInfoService {
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    MerchantAppInfo selectByPrimaryKey(Long id);

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective(MerchantAppInfo record);

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKeySelective(MerchantAppInfo record);
    /**
     *
     * 查询（根据OpenId查询）
     *
     **/
    MerchantAppInfo selectByOpenId(String openId);
    /**
     *
     * 查询Key（根据OpenId查询）
     *
     */
    String selectSecretKeyByOpenId(String openId);
}
