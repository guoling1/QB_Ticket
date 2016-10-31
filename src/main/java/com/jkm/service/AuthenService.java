package com.jkm.service;

import com.jkm.entity.QbBindCard;
import com.jkm.entity.fusion.AuthenData;
import com.jkm.entity.fusion.SingleRefundData;


import java.util.List;
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
	 * 绑定银行卡
	 * @param qbBindCard
	 * @return
	 */
	public long bindCard(QbBindCard qbBindCard);

	/**
	 * 删除银行卡
	 * @param qbBindCard
	 * @return
	 */
	public void deleteCard(long id);

	/**
	 * 银行卡列表
	 * @param uid
	 * @return
	 */
	public List<QbBindCard> cardList(String uid);
}
