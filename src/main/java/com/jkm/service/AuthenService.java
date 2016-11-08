package com.jkm.service;

import com.jkm.entity.fusion.AuthenData;
import com.jkm.entity.fusion.CardAuthData;
import com.jkm.entity.fusion.SingleRefundData;
import net.sf.json.JSONObject;

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
	 * 银行卡鉴权
	 * @param requestData
	 * @return
	 */
	public Map<String, Object> cardAuth(CardAuthData requestData);

	/**
	 * 立即支付
	 * @param requestData
	 * @return
	 */
	public Map<String, Object> toPay(JSONObject requestData);

	/**
	 * 支付订单查询
	 * @param requestData
	 * @return
	 */
	public Map<String, Object> queryQuickPay(JSONObject requestData);
	/**
	 * 单笔退款结果查询
	 * @param requestData
	 * @return
	 */
	public Map<String, Object> queryRefund(JSONObject requestData);
}
