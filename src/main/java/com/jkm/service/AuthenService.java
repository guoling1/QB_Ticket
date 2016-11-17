package com.jkm.service;

import com.jkm.entity.fusion.*;
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
	 * 支付订单查询
	 * @param requestData
	 * @return
	 */
	public Map<String, Object> queryQuickPay(QueryQuickPayData requestData);
	/**
	 * 单笔退款结果查询
	 * @param requestData
	 * @return
	 */
	public Map<String, Object> queryRefund(QueryRefundData requestData);

	/**
	 * 立即支付
	 * @param requestData
	 * @return
	 */
	public JSONObject toPay(JSONObject requestData);
	/**
	 * 多次支付
	 * @param requestData
	 * @return
	 */
	public JSONObject toPayByCid(JSONObject requestData);
	/**
	 * 抢票单立即支付
	 * @param requestData
	 * @return
	 */
	public JSONObject toPayGrab(JSONObject requestData) throws Exception;
	/**
	 * 多次支付
	 * @param requestData
	 * @return
	 */
	public JSONObject toPayGrabByCid(JSONObject requestData) throws Exception;
	/**
	 * 获取短信验证按
	 * @param requestData
	 * @return
	 */
	public JSONObject getCode(JSONObject requestData);
}
