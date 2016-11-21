package com.jkm.dao;

import com.jkm.entity.MerchantInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * MerchantInfoDao数据库操作接口类
 * 
 **/

public interface MerchantInfoDao{


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	MerchantInfo  selectByPrimaryKey(@Param("id") Long id);

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