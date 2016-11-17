package com.jkm.service;

import com.jkm.entity.PaySequence;


/**
 * 
 * 支付流水
 * 
 **/

public interface PaySequenceService {


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	PaySequence  selectByPrimaryKey(Long id);


	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(PaySequence record);

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective(PaySequence record);


}