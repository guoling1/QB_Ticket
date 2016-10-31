package com.jkm.service.impl;

import com.jkm.dao.QbBindCardDao;
import com.jkm.entity.QbBindCard;
import com.jkm.entity.fusion.*;
import com.jkm.entity.fusion.body.RequestBody100003;
import com.jkm.entity.fusion.body.RequestBody100005;
import com.jkm.entity.fusion.detail.RequestDetail100003;
import com.jkm.entity.fusion.detail.RequestDetail100005;
import com.jkm.entity.fusion.head.RequestHead;
import com.jkm.service.AuthenService;
import com.jkm.service.SignatureService;
import com.jkm.util.fusion.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鉴权
 */
@Service
public class AuthenServiceImpl implements AuthenService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SignatureService signatureService;
	@Autowired
	private QbBindCardDao qbBindCardDao;

	/**
	 * 快捷支付
	 * @param requestData
	 * @return
	 */
	@Override
	public Map<String, Object> fastPay(AuthenData requestData) {
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			Request100005 request100005 = createFastPay(requestData);
			String xml = XmlUtil.toXML(request100005);
			logger.debug("****************xml生成authen*********************-"
					+ xml);

			// 加签
			logger.debug("****************xml加签*********************");
			xml = signatureService.addSignatrue(xml);
			// 加压加密
			logger.debug("****************xml加压加密*********************");
			String Base64 = GZipUtil.gzipString(xml);
			// 通讯使用HTTPS进行通讯
			logger.debug("****************xml通讯使用HTTPS进行通讯*********************");
			String response1 = HttpUtils.sendPostMessage(Base64, Constants.FASTPAY_URL,
					Constants.transfer_charset);
			// 解压解密返回信息
			String response2 = null;
			if (StringUtils.isNotEmpty(response1)) {
				logger.debug("****************xml通讯返回*********************");
				response2 = GZipUtil.ungzipString(response1);
				boolean isSuc = signatureService.isSignature(response2);
				if (isSuc) {
					ret.put("signMsg", "验签成功！！");
					logger.error("验签成功！！");
				} else {
					ret.put("signMsg", "验签失败！！");
					logger.error("验签失败！！");
				}
				// 将解压解密后的结果转化为Response100000对象
				Response100005 response100005 = XmlUtil.fromXML(response2,
						Response100005.class);

				if ("0000".equals(response100005.getInfo().getRetCode())) {
					ret.put("reqSn", request100005.getInfo().getReqSn());
					ret.put("token", response100005.getBody().getRetDetail()
							.getTOKEN());
					ret.put("retCode", true);
					ret.put("retMsg", response100005.getInfo().getErrMsg());
					ret.put("retXml", response2);
				} else {
					ret.put("retCode", false);
					ret.put("retMsg", response100005.getInfo().getErrMsg());
					ret.put("retXml", response2);
				}
			} else {
				ret.put("retCode", false);
				ret.put("retMsg", "付款接口连接失败");
			}
			logger.debug("****************" + response2
					+ "*********************");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	private Request100005 createFastPay(AuthenData requestData) {
		Request100005 authen = new Request100005();
		RequestHead head = new RequestHead();
		head.setTrxCode("100005");//固定
		head.setVersion("01");//固定
		head.setDataType(Constants.DATA_TYPE_XML);//固定
		head.setLevel(Constants.LEVEL_0);//固定
		if ("1".equals(requestData.getStep())) {
			head.setReqSn(requestData.getReqSn());
		} else {
			head.setReqSn(DateUtils.getDateString(new Date(),
					DateUtils.formate_string_yyyyMMddhhmmss));
		}
		head.setSignedMsg("signedMsg");
		RequestBody100005 body = new RequestBody100005();
		RequestDetail100005 detail = new RequestDetail100005();
		detail.setMERCHANT_ID("800010000020019");
		detail.setSEND_TIME(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMddhhmmss));
		detail.setSEND_DT(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMdd));
		/**
		 * 每次必传部分
		 */
		detail.setRET_URL(requestData.getRetUrl());
		detail.setNOTIFY_URL(requestData.getNotifyUrl());
		detail.setCARD_NO(requestData.getCrdNo());
		detail.setACCOUNT_NAME(requestData.getCapCrdNm());
		detail.setAMOUNT(requestData.getAmount());
		detail.setID_TYPE("00");//只支持身份证
		detail.setID(requestData.getIdNo());//证件号
		detail.setTEL(requestData.getPhoneNo());
		detail.setCRE_VAL_DATE("");
		detail.setCRE_CVN2("");
		detail.setSTEP_NO(requestData.getStep());
		if (!"0".equals(requestData.getStep())) {
			detail.setTEL_CAPTCHA(requestData.getVerifyCode());
			detail.setTOKEN(requestData.getToken());
		}
		body.setTransDetail(detail);
		authen.setBody(body);
		authen.setInfo(head);
		return authen;
	}

	/**
	 * 单笔退款
	 * @param requestData
	 * @return
	 */
	@Override
	public Map<String, Object> singlRefund(SingleRefundData requestData) {
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			Request100003 request100003 = createSingleRefund(requestData);
			String xml = XmlUtil.toXML(request100003);
			logger.debug("****************xml生成authen*********************-"
					+ xml);

			// 加签
			logger.debug("****************xml加签*********************");
			xml = signatureService.addSignatrue(xml);
			// 加压加密
			logger.debug("****************xml加压加密*********************");
			String Base64 = GZipUtil.gzipString(xml);
			// 通讯使用HTTPS进行通讯
			logger.debug("****************xml通讯使用HTTPS进行通讯*********************");
			String response1 = HttpUtils.sendPostMessage(Base64, Constants.SINGLE_REFUND_URL,
					Constants.transfer_charset);
			// 解压解密返回信息
			String response2 = null;
			if (StringUtils.isNotEmpty(response1)) {
				logger.debug("****************xml通讯返回*********************");
				response2 = GZipUtil.ungzipString(response1);
				boolean isSuc = signatureService.isSignature(response2);
				if (isSuc) {
					ret.put("signMsg", "验签成功！！");
					logger.error("验签成功！！");
				} else {
					ret.put("signMsg", "验签失败！！");
					logger.error("验签失败！！");
				}
				// 将解压解密后的结果转化为Response100003对象
				Response100003 response100003 = XmlUtil.fromXML(response2,
						Response100003.class);

				if ("0000".equals(response100003.getInfo().getRetCode())) {
					ret.put("reqSn", request100003.getInfo().getReqSn());
					ret.put("retCode", true);
					ret.put("retMsg", response100003.getInfo().getErrMsg());
					ret.put("retXml", response2);
				} else {
					ret.put("retCode", false);
					ret.put("retMsg", response100003.getInfo().getErrMsg());
					ret.put("retXml", response2);
				}
			} else {
				ret.put("retCode", false);
				ret.put("retMsg", "付款接口连接失败");
			}
			logger.debug("****************" + response2
					+ "*********************");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	private Request100003 createSingleRefund(SingleRefundData requestData) {
		Request100003 singleRefund = new Request100003();
		RequestHead head = new RequestHead();
		head.setTrxCode("100005");
		head.setVersion("01");
		head.setDataType(Constants.DATA_TYPE_XML);
		head.setLevel(Constants.LEVEL_0);
		head.setReqSn(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMddhhmmss));
		head.setSignedMsg("signedMsg");

		RequestBody100003 body = new RequestBody100003();
		RequestDetail100003 detail = new RequestDetail100003();
		detail.setORG_SN(requestData.getOrgSn());
		detail.setORG_DATE(requestData.getOrgDate());
		detail.setREFUND_AMOUNT(requestData.getRefundAmount());
		detail.setORG_AMOUNT(requestData.getOrgAmount());
		detail.setMERCHANT_ID(requestData.getMercId());
		detail.setREFUND_ORD_NO(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMddhhmmss));
		detail.setORD_DATE(requestData.getOrdDate());
		detail.setREFUND_REASON(requestData.getRefundReason());
		detail.setTRANS_TYPE("1");
		body.setSinglerefundDetail(detail);
		singleRefund.setBody(body);
		singleRefund.setInfo(head);
		return singleRefund;
	}

	/**
	 * 绑定银行卡
	 * @param qbBindCard
	 * @return
	 */
	@Override
	public long bindCard(QbBindCard qbBindCard) {
		return qbBindCardDao.insert(qbBindCard);
	}

	/**
	 * 删除银行卡
	 * @param id
	 */
	@Override
	public void deleteCard(long id) {
		qbBindCardDao.delete(id);
	}

	/**
	 * 银行卡列表
	 * @param uid
	 * @return
	 */
	@Override
	public List<QbBindCard> cardList(String uid) {
		List<QbBindCard> cardList = qbBindCardDao.select(uid);
		return cardList;
	}




}
