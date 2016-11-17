package com.jkm.dao;

import com.jkm.entity.RefundSequence;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 退款流水
 * 
 **/

public interface RefundSequenceDao {


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	RefundSequence  selectByPrimaryKey(@Param("id") Long id);

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
	int insert(RefundSequence record);

	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(RefundSequence record);

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective(RefundSequence record);

	/**
	 * 
	 * 修改（根据主键ID修改）
	 * 
	 **/
	int updateByPrimaryKey(RefundSequence record);

}