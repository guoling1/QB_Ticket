package com.jkm.dao;

import com.jkm.entity.PaySequence;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 支付流水
 * 
 **/

public interface PaySequenceDao {


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	PaySequence  selectByPrimaryKey(@Param("id") Long id);


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


	/**
	 * 根据流水号更改订单状态
	 */
	void updatePayResultByReqSn(@Param("payResult") String payResult,@Param("reqSn") String reqSn);


}