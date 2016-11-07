package com.jkm.dao;

import com.jkm.entity.PayResultRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * PayResultRecordMapper数据库操作接口类
 * 
 **/

public interface PayResultRecordDao {


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	PayResultRecord  selectByPrimaryKey(@Param("id") Long id);

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
	int insert(PayResultRecord record);

	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(PayResultRecord record);

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective(PayResultRecord record);

	/**
	 * 
	 * 修改（根据主键ID修改）
	 * 
	 **/
	int updateByPrimaryKey(PayResultRecord record);

}