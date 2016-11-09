package com.jkm.dao;

import com.jkm.entity.GetParamRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * GetParamRecordMapper数据库操作接口类
 * 
 **/

public interface GetParamRecordDao {


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	GetParamRecord  selectByPrimaryKey(@Param("id") Long id);

	/**
	 * 
	 * 删除（根据主键ID删除）
	 * 
	 **/
	int deleteByPrimaryKey(@Param("id") Long id);

	/**
	 * 
	 * 添加
	 * 
	 **/
	int insert(GetParamRecord record);

	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(GetParamRecord record);

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective(GetParamRecord record);

	/**
	 * 
	 * 修改（根据主键ID修改）
	 * 
	 **/
	int updateByPrimaryKey(GetParamRecord record);
	/**
	 *
	 * 根据条件查询
	 *
	 **/
	int selectByCondition(GetParamRecord record);

}