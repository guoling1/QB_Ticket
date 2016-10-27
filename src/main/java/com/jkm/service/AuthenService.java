package com.jkm.service;

import com.jkm.entity.fusion.AuthenData;
import com.jkm.entity.fusion.FastQueryData;
import com.jkm.entity.fusion.SingleRefundData;


import java.util.Map;


public interface AuthenService {

	/**
	 * 快捷支付
	 * @param requestData
	 * @return
	 */
	public Map<String, Object> fastPay(AuthenData requestData);

	/**
	 * 单笔退款
	 * @param requestData
	 * @return
	 */
	public Map<String, Object> singlRefund(SingleRefundData requestData);

	/**
	 * 快捷支付查询
	 * @param requestData
	 * @return
	 */
	public Map<String, Object> fastPayQuery(FastQueryData requestData);

}
