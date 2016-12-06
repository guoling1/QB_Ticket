package com.jkm.dao;

import com.jkm.entity.MerchantAppInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * MerchantAppInfoDao数据库操作接口类
 * 
 **/

public interface MerchantAppInfoDao{


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	MerchantAppInfo  selectByPrimaryKey(@Param("id") Long id);

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
	MerchantAppInfo selectByOpenId(@Param("openId") String openId);
	/**
	 *
	 * 查询SecretKey（根据OpenId查询）
	 *
	 **/
	String selectSecretKeyByOpenId(String openId);
}