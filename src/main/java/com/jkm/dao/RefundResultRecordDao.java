package com.jkm.dao;

import com.jkm.entity.RefundResultRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * RefundResultRecordMapper数据库操作接口类
 * 
 **/

public interface RefundResultRecordDao {


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	RefundResultRecord  selectByPrimaryKey(@Param("id") Long id);

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
	int insert(RefundResultRecord record);

	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(RefundResultRecord record);

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective(RefundResultRecord record);

	/**
	 * 
	 * 修改（根据主键ID修改）
	 * 
	 **/
	int updateByPrimaryKey(RefundResultRecord record);

}