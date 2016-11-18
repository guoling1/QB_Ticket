package com.jkm.service;

import com.jkm.entity.RefundSequence;

/**
 * 
 * 退款流水
 * 
 **/

public interface RefundSequenceService {


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	RefundSequence  selectByPrimaryKey(Long id);

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
	 * 根据流水号更改订单状态
	 * @param refundResult
	 * @param refundSn
	 */
	void updateRefundResultByReqSn(String refundResult,String refundSn);
}