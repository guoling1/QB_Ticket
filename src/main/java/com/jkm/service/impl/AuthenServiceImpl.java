package com.jkm.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.entity.*;
import com.jkm.entity.fusion.*;
import com.jkm.entity.fusion.body.RequestBody100003;
import com.jkm.entity.fusion.body.RequestBody100004;
import com.jkm.entity.fusion.body.RequestBody100005;
import com.jkm.entity.fusion.detail.RequestDetail100003;
import com.jkm.entity.fusion.detail.RequestDetail100004;
import com.jkm.entity.fusion.detail.RequestDetail100005;
import com.jkm.entity.fusion.head.RequestHead;
import com.jkm.enums.EnumOrderFormStatus;
import com.jkm.service.*;
import com.jkm.util.SnGenerator;
import com.jkm.util.fusion.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
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
	private PayResultRecordService payResultRecordService;
	@Autowired
	private RefundResultRecordService refundResultRecordService;
	@Autowired
	private OrderFormService orderFormService;
	@Autowired
	private TicketService ticketService;
	@Autowired
	private GetParamRecordService getParamRecordService;
	@Autowired
	private BindCardService bindCardService;
	/**
	 * 快捷支付
	 * @param requestData
	 * @return
	 */
	@Override
	public Map<String, Object> fastPay(AuthenData requestData) {
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			GetParamRecord gr = new GetParamRecord();
			gr.setStatus(0);
			gr.setAppId(requestData.getAppId());
			gr.setNonceStr(requestData.getNonceStr());
			int count = getParamRecordService.selectByCondition(gr);
			if(count>0){
				ret.put("retCode", false);
				ret.put("retMsg", "请不要重复提交订单");
			}else{
				getParamRecordService.insertSelective(gr);
			}
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
			String response1 = HttpUtils.sendPostMessage(Base64, HzSdkConstans.FASTPAY_URL,
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
					JSONObject jo = new JSONObject();
					jo.put("reqSn",request100005.getInfo().getReqSn());
					jo.put("amount",request100005.getBody().getTransDetail().getAMOUNT());
//					ret.put("reqSn",request100005.getInfo().getReqSn());
//					ret.put("token", response100005.getBody().getRetDetail()
//							.getTOKEN());
					ret.put("retCode", true);
					ret.put("retMsg", response100005.getInfo().getErrMsg());
					ret.put("retData",jo);
//					ret.put("retXml", response2);
					PayResultRecord payResultRecord = new PayResultRecord();
					payResultRecord.setStatus(0);
					payResultRecord.setAmount(request100005.getBody().getTransDetail().getAMOUNT());
					payResultRecord.setPayChannel("fastpay");
					payResultRecord.setPayParams(xml);
					payResultRecord.setPayResult("1");
					payResultRecord.setReqSn(request100005.getInfo().getReqSn());
					payResultRecord.setResultParams(response100005.toString());
					payResultRecordService.insertSelective(payResultRecord);
				} else {
					ret.put("retCode", false);
					ret.put("retMsg", response100005.getInfo().getErrMsg());
//					ret.put("retXml", response2);
					PayResultRecord payResultRecord = new PayResultRecord();
					payResultRecord.setStatus(0);
					payResultRecord.setAmount(request100005.getBody().getTransDetail().getAMOUNT());
					payResultRecord.setPayChannel("fastpay");
					payResultRecord.setPayParams(xml);
					payResultRecord.setPayResult("2");
					payResultRecord.setReqSn(request100005.getInfo().getReqSn());
					payResultRecord.setResultParams(response100005.toString());
					payResultRecordService.insertSelective(payResultRecord);
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
//		if ("1".equals(requestData.getStep())) {
//			head.setReqSn(requestData.getReqSn());
//		} else {
			head.setReqSn(requestData.getReqSn());
//		}
		head.setSignedMsg("signedMsg");
		RequestBody100005 body = new RequestBody100005();
		RequestDetail100005 detail = new RequestDetail100005();
		detail.setMERCHANT_ID(HzSdkConstans.MERC_ID);
		detail.setSEND_TIME(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMddhhmmss));
		detail.setSEND_DT(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMdd));
		/**
		 * 每次必传部分
		 */
		detail.setCARD_NO(requestData.getCrdNo());
		detail.setACCOUNT_NAME(requestData.getCapCrdNm());
		detail.setAMOUNT(requestData.getAmount());
		detail.setID_TYPE("00");//只支持身份证
		detail.setID(requestData.getIdNo());//证件号
		detail.setTEL(requestData.getPhoneNo());//手机号
		detail.setCRE_VAL_DATE("");
		detail.setCRE_CVN2("");
//		detail.setSTEP_NO(requestData.getStep());
//		if (!"0".equals(requestData.getStep())) {
//			detail.setTEL_CAPTCHA(requestData.getVerifyCode());
//			detail.setTOKEN(requestData.getToken());
//		}
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
			String response1 = HttpUtils.sendPostMessage(Base64, HzSdkConstans.SINGLE_REFUND_URL,
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
//					ret.put("reqSn", request100003.getInfo().getReqSn());
					JSONObject jo = new JSONObject();
					jo.put("reqSn",request100003.getInfo().getReqSn());
					ret.put("retCode", true);
					ret.put("retMsg", response100003.getInfo().getErrMsg());
					ret.put("retData", jo);
//					ret.put("retXml", response2);

					RefundResultRecord refundResultRecord = new RefundResultRecord();
					refundResultRecord.setStatus(0);
					refundResultRecord.setAmount(request100003.getBody().getTransDetail().getREFUND_AMOUNT());
					refundResultRecord.setRefundChannel("fastpay");
					refundResultRecord.setRefundParams(xml);
					refundResultRecord.setRefundResult("1");
					refundResultRecord.setReqSn(request100003.getInfo().getReqSn());
					refundResultRecord.setResultParams(response100003.toString());
					refundResultRecordService.insertSelective(refundResultRecord);
				} else {
					ret.put("retCode", false);
					ret.put("retMsg", response100003.getInfo().getErrMsg());
					ret.put("retData", null);
//					ret.put("retXml", response2);
					RefundResultRecord refundResultRecord = new RefundResultRecord();
					refundResultRecord.setStatus(0);
					refundResultRecord.setAmount(request100003.getBody().getTransDetail().getREFUND_AMOUNT());
					refundResultRecord.setRefundChannel("fastpay");
					refundResultRecord.setRefundParams(xml);
					refundResultRecord.setRefundResult("2");
					refundResultRecord.setReqSn(request100003.getInfo().getReqSn());
					refundResultRecord.setResultParams(response100003.toString());
					refundResultRecordService.insertSelective(refundResultRecord);
				}
			} else {
				ret.put("retCode", false);
				ret.put("retMsg", "单笔退款接口连接失败");
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
		head.setTrxCode("100003");
		head.setVersion("01");
		head.setDataType(Constants.DATA_TYPE_XML);
		head.setLevel(Constants.LEVEL_0);
		head.setReqSn(SnGenerator.generate());
		head.setSignedMsg("signedMsg");

		RequestBody100003 body = new RequestBody100003();
		RequestDetail100003 detail = new RequestDetail100003();
		detail.setORG_SN(requestData.getOrgSn());
		detail.setORG_DATE(requestData.getOrgDate());
		detail.setREFUND_AMOUNT(requestData.getRefundAmount());
		detail.setORG_AMOUNT(requestData.getOrgAmount());
		detail.setMERCHANT_ID(HzSdkConstans.MERC_ID);
		detail.setREFUND_ORD_NO(head.getReqSn());
		detail.setORD_DATE(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMdd));
		detail.setREFUND_REASON(requestData.getRefundReason());
		detail.setTRANS_TYPE("1");
		body.setTransDetail(detail);
		singleRefund.setBody(body);
		singleRefund.setInfo(head);
		return singleRefund;
	}

	/**
	 * 银行卡鉴权
	 * @param requestData
	 * @return
	 */
	@Override
	public Map<String, Object> cardAuth(CardAuthData requestData) {
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			Request100004 request100004 = createCardAuth(requestData);
			String xml = XmlUtil.toXML(request100004);
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
			String response1 = HttpUtils.sendPostMessage(Base64, HzSdkConstans.CARD_AUTH,
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
				Response100004 response100004 = XmlUtil.fromXML(response2,
						Response100004.class);

				if ("0000".equals(response100004.getInfo().getRetCode())) {
					ret.put("reqSn", request100004.getInfo().getReqSn());
					ret.put("retCode", true);
					ret.put("retMsg", response100004.getInfo().getErrMsg());
					ret.put("retXml", response2);
				} else {
					ret.put("retCode", false);
					ret.put("retMsg", response100004.getInfo().getErrMsg());
					ret.put("retXml", response2);
				}
			} else {
				ret.put("retCode", false);
				ret.put("retMsg", "银行卡鉴权接口连接失败");
			}
			logger.debug("****************" + response2
					+ "*********************");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}


	private Request100004 createCardAuth(CardAuthData requestData) {
		Request100004 cardAuth = new Request100004();
		RequestHead head = new RequestHead();
		head.setTrxCode("100004");
		head.setVersion("01");
		head.setDataType(Constants.DATA_TYPE_XML);
		head.setLevel(Constants.LEVEL_0);
		head.setReqSn(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMddhhmmss));
		head.setSignedMsg("signedMsg");

		RequestBody100004 body = new RequestBody100004();
		RequestDetail100004 detail = new RequestDetail100004();
		detail.setMERCHANT_ID(HzSdkConstans.MERC_ID);
		detail.setSEND_TIME(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMddhhmmss));
		detail.setSEND_DT(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMdd));
		detail.setCARD_NO(requestData.getCrdNo());
		detail.setACCOUNT_NAME(requestData.getAccountName());
		detail.setID_TYPE(requestData.getIdType());
		detail.setID(requestData.getIdNo());
		detail.setTEL(requestData.getPhoneNo());
		body.setTransDetail(detail);
		cardAuth.setBody(body);
		cardAuth.setInfo(head);
		return cardAuth;
	}

	@Override
	public Map<String, Object> toPay(JSONObject requestData) {
		JSONObject jo = new JSONObject();
		Optional<OrderForm>  orderFormOptional = orderFormService.selectById(requestData.getLong("orderId"));
		Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderFormOptional.get().getId() + "]不存在");
		BigDecimal amount = orderFormOptional.get().getTotalPrice();
		AuthenData authenData = new AuthenData();
		authenData.setAmount(amount+"");
		authenData.setPhoneNo(requestData.getString("phoneNo"));
		authenData.setCrdNo(requestData.getString("crdNo"));
		authenData.setCapCrdNm(requestData.getString("capCrdNm"));
		authenData.setIdNo(requestData.getString("idNo"));
		authenData.setReqSn(SnGenerator.generate());
		orderFormOptional.get().setStatus(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_GOING.getId());
		orderFormService.updateStatus(orderFormOptional.get());
		Map<String, Object> ret = this.fastPay(authenData);
		if((boolean)ret.get("retCode")==true){//支付成功
			ticketService.handleCustomerPayMsg(orderFormOptional.get().getId(),ret.get("reqSn").toString(),true);
			BindCard bindCard = new BindCard();
			bindCard.setUid(requestData.getString("appid")+"_"+requestData.getString("uid"));
			bindCard.setCardNo(requestData.getString("crdNo"));
			bindCard.setAccountName(requestData.getString("capCrdNm"));
			bindCard.setCardType("00");
			bindCard.setCardId(requestData.getString("idNo"));
			bindCard.setPhone(requestData.getString("phoneNo"));
			bindCardService.insertSelective(bindCard);
		}else{//支付失败
			orderFormOptional.get().setStatus(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_FAIL.getId());
			orderFormService.updateStatus(orderFormOptional.get());
		}
		return ret;
	}

	/**
	 * 支付订单查询
	 * @param requestData
	 * @return
	 */
	@Override
	public Map<String, Object> queryQuickPay(JSONObject requestData) {
		return null;
	}

	/**
	 * 退款单查询
	 * @param requestData
	 * @return
	 */
	@Override
	public Map<String, Object> queryRefund(JSONObject requestData) {
		return null;
	}
}
