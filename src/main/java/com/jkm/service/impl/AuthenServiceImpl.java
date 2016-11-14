package com.jkm.service.impl;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jkm.dao.SendMessageCountRecordDao;
import com.jkm.entity.*;
import com.jkm.entity.fusion.*;
import com.jkm.entity.fusion.body.*;
import com.jkm.entity.fusion.detail.*;
import com.jkm.entity.fusion.head.RequestHead;
import com.jkm.entity.fusion.head.RequestHead20003;
import com.jkm.entity.fusion.head.RequestHead20005;
import com.jkm.entity.helper.UserBankCardSupporter;
import com.jkm.enums.EnumGrabTicketStatus;
import com.jkm.enums.EnumOrderFormStatus;
import com.jkm.enums.notifier.EnumVerificationCodeType;
import com.jkm.helper.TicketMessageParams.SendPaymentParam;
import com.jkm.service.*;
import com.jkm.service.notifier.SmsAuthService;
import com.jkm.util.*;
import com.jkm.util.fusion.*;
import com.jkm.util.mq.MqConfig;
import com.jkm.util.mq.MqProducer;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
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
	@Autowired
	private SmsAuthService smsAuthService;
	@Autowired
	private TicketSendMessageService ticketSendMessageService;
	@Autowired
	private GrabTicketFormService grabTicketFormService;
	@Autowired
	private SendMessageCountRecordDao sendMessageCountRecordDao;
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
				ret.put("retCode", "3000");
				ret.put("retMsg", "请不要重复提交订单");
				return ret;
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
					ret.put("retCode", response100005.getInfo().getRetCode());
					ret.put("retMsg", response100005.getInfo().getErrMsg());
					ret.put("retData",jo);
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
			} else {//连接超时
				ret.put("retCode", "5000");
				ret.put("retMsg", "付款接口连接失败");

			}
			logger.debug("****************" + response2
					+ "*********************");

		} catch (Exception e) {
			ret.put("retCode", "4000");
			ret.put("retMsg", "参数有误");
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
		head.setReqSn(requestData.getReqSn());
		head.setSignedMsg("signedMsg");
		RequestBody100005 body = new RequestBody100005();
		RequestDetail100005 detail = new RequestDetail100005();
		detail.setMERCHANT_ID(HzSdkConstans.MERC_ID);
		detail.setSEND_TIME(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMddhhmmss));
		detail.setSEND_DT(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMdd));
		detail.setCARD_NO(requestData.getCrdNo());
		detail.setACCOUNT_NAME(requestData.getCapCrdNm());
		detail.setAMOUNT(requestData.getAmount());
		detail.setID_TYPE("00");//只支持身份证
		detail.setID(requestData.getIdNo());//证件号
		detail.setTEL(requestData.getPhoneNo());//手机号
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
					JSONObject jo = new JSONObject();
					jo.put("reqSn",request100003.getInfo().getReqSn());
					ret.put("retCode", true);
					ret.put("retMsg", response100003.getInfo().getErrMsg());
					ret.put("retData", jo);
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
			logger.debug(e.getMessage());
			ret.put("retCode", false);
			ret.put("retMsg", "参数有误");
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
		head.setReqSn(requestData.getReqSn());
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

	/**
	 * 支付订单查询
	 * @param requestData
	 * @return
	 */
	@Override
	public Map<String, Object> queryQuickPay(QueryQuickPayData requestData) {
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			Request200005 request200005 = createQueryQuickPay(requestData);
			String xml = XmlUtil.toXML(request200005);
			logger.debug("****************xml生成authen*********************-"+ xml);
			// 加签
			logger.debug("****************xml加签*********************");
			xml = signatureService.addSignatrue(xml);
			// 加压加密
			logger.debug("****************xml加压加密*********************");
			String Base64 = GZipUtil.gzipString(xml);
			// 通讯使用HTTPS进行通讯
			logger.debug("****************xml通讯使用HTTPS进行通讯*********************");
			String response1 = HttpUtils.sendPostMessage(Base64, HzSdkConstans.FASTPAY_QUERY_URL,
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
				// 将解压解密后的结果转化为Response200005对象
				Response200005 response200005 = XmlUtil.fromXML(response2,
						Response200005.class);

				if ("0000".equals(response200005.getInfo().getRetCode())) {
					JSONObject jo = new JSONObject();
					jo.put("payinOrdNo",response200005.getBody().getRetDetail().getPAYIN_ORD_NO());//交易流水号
					jo.put("mercOrdNo",response200005.getBody().getRetDetail().getMERC_ORD_NO());
					jo.put("orderStatus",response200005.getBody().getRetDetail().getORDER_STATUS());
					ret.put("retCode", response200005.getInfo().getRetCode());
					ret.put("retMsg", response200005.getInfo().getErrMsg());
					ret.put("retData",jo);
				} else {
					ret.put("retCode", response200005.getInfo().getRetCode());
					ret.put("retMsg", response200005.getInfo().getErrMsg());
				}
			} else {
				ret.put("retCode", "-1000");
				ret.put("retMsg", "订单查询接口连接失败");
			}
			logger.debug("****************" + response2
					+ "*********************");

		} catch (Exception e) {
			logger.debug(e.getMessage());
			ret.put("retCode", "-1001");
			ret.put("retMsg", "参数有误");
		}
		return ret;
	}
	private Request200005 createQueryQuickPay(QueryQuickPayData requestData) {
		Request200005 queryQuickPay = new Request200005();
		RequestHead20005 head = new RequestHead20005();
		head.setTrxCode("200005");//固定
		head.setVersion("01");//固定
		head.setDataType(Constants.DATA_TYPE_XML);//固定
		head.setLevel(Constants.LEVEL_0);//固定
		head.setReqSn(requestData.getReqSn());
		head.setSignedMsg("signedMsg");
		head.setMerchantId(HzSdkConstans.MERC_ID);
		RequestBody200005 body = new RequestBody200005();
		RequestDetail200005 detail = new RequestDetail200005();
		detail.setMERCHANT_ID(HzSdkConstans.MERC_ID);
		detail.setMERC_ORD_NO(requestData.getMercOrdNo());
		detail.setMERC_ORD_DT(requestData.getMercOrdDt());
		body.setQueryTrans(detail);
		queryQuickPay.setBody(body);
		queryQuickPay.setInfo(head);
		return queryQuickPay;
	}
	/**
	 * 退款单查询
	 * @param requestData
	 * @return
	 */
	@Override
	public Map<String, Object> queryRefund(QueryRefundData requestData) {
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			Request200003 request200003 = createQueryRefund(requestData);
			String xml = XmlUtil.toXML(request200003);
			logger.debug("****************xml生成authen*********************-"+ xml);
			// 加签
			logger.debug("****************xml加签*********************");
			xml = signatureService.addSignatrue(xml);
			// 加压加密
			logger.debug("****************xml加压加密*********************");
			String Base64 = GZipUtil.gzipString(xml);
			// 通讯使用HTTPS进行通讯
			logger.debug("****************xml通讯使用HTTPS进行通讯*********************");
			String response1 = HttpUtils.sendPostMessage(Base64, HzSdkConstans.QUERY_REFUND_URL,
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
				// 将解压解密后的结果转化为Response200003对象
				Response200003 response200003 = XmlUtil.fromXML(response2,
						Response200003.class);

				if ("0000".equals(response200003.getInfo().getRetCode())) {
					JSONObject jo = new JSONObject();
					jo.put("sn",response200003.getBody().getTransDetail().getSN());
					jo.put("orderStatus",response200003.getBody().getTransDetail().getORD_STS());
					ret.put("retCode", response200003.getInfo().getRetCode());
					ret.put("retMsg", response200003.getInfo().getErrMsg());
					ret.put("retData",jo);
				} else {
					ret.put("retCode", response200003.getInfo().getRetCode());
					ret.put("retMsg", response200003.getInfo().getErrMsg());
				}
			} else {
				ret.put("retCode", "-1000");
				ret.put("retMsg", "退款单接口连接失败");
			}
			logger.debug("****************" + response2
					+ "*********************");

		} catch (Exception e) {
			logger.debug(e.getMessage());
			ret.put("retCode", "-1001");
			ret.put("retMsg", "参数有误");
		}
		return ret;
	}

	private Request200003 createQueryRefund(QueryRefundData requestData) {
		Request200003 queryRefund = new Request200003();
		RequestHead20003 head = new RequestHead20003();
		head.setTrxCode("200003");//固定
		head.setVersion("01");//固定
		head.setDataType(Constants.DATA_TYPE_XML);//固定
		head.setLevel(Constants.LEVEL_0);//固定
		head.setReqSn(requestData.getReqSn());
		head.setSignedMsg("signedMsg");
		head.setMerchantId(HzSdkConstans.MERC_ID);
		RequestBody200003 body = new RequestBody200003();
		RequestDetail200003 detail = new RequestDetail200003();
		detail.setMERCHANT_ID(HzSdkConstans.MERC_ID);
		detail.setQUERY_SN(requestData.getQuerySn());
		detail.setQUERY_DATE(requestData.getQueryDate());
		body.setTransDetail(detail);
		queryRefund.setBody(body);
		queryRefund.setInfo(head);
		return queryRefund;
	}

	@Override
	public JSONObject toPay(JSONObject requestData) {
		JSONObject jo = new JSONObject();
		Preconditions.checkNotNull(requestData.get("crdNo"),"缺少银行卡号");
		Preconditions.checkNotNull(requestData.get("orderId"),"订单号不能为空");
		Preconditions.checkNotNull(requestData.get("capCrdNm"),"缺少开户姓名");
		Preconditions.checkNotNull(requestData.get("idNo"),"缺少身份证号");
		Preconditions.checkNotNull(requestData.get("phoneNo"),"缺少手机号");
		Preconditions.checkNotNull(requestData.get("isAgree"),"请勾选并同意协议");
		Preconditions.checkNotNull(requestData.get("vCode"),"请输入验证码");
		Preconditions.checkNotNull(requestData.get("bankCode"),"卡宾不能为空");
		Preconditions.checkNotNull(requestData.get("nonceStr"),"随机参数有误");
//		Preconditions.checkNotNull(requestData.get("sn"),"短信序列码不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getLong("orderId")+""), "订单信息不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("crdNo")), "卡号不能不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("capCrdNm")), "开户姓名不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("idNo")), "身份证号不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("phoneNo")), "手机号不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("bankCode")), "卡宾不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("vCode")), "验证码不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("nonceStr")), "随机参数有误");
//		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getLong("sn")+""), "短信序列码不能为空");

//		Pair<Integer, String> codeStatus = smsAuthService.checkVerifyCode(requestData.getString("phoneNo"),requestData.getString("vCode"),EnumVerificationCodeType.PAYMENT);
//		int resultType = codeStatus.getKey();
//		if(resultType!=1){
//			jo.put("result",false);
//			jo.put("message",codeStatus.getValue());
//			return jo;
//		}
//		//防止刷验证码
//		int count = sendMessageCountRecordDao.selctCountBySn(requestData.getLong("sn"));
//		if(count>3){
//			jo.put("result",false);
//			jo.put("message","验证码已失效");
//			return jo;
//		}
//		SendMessageCountRecord sendMessageCountRecord= new SendMessageCountRecord();
//		sendMessageCountRecord.setUid(requestData.getString("appid")+"_"+requestData.getString("uid"));
//		sendMessageCountRecord.setMessageTemplateId((long)EnumVerificationCodeType.PAYMENT.getId());
//		sendMessageCountRecord.setMobile(requestData.getString("phoneNo"));
//		sendMessageCountRecord.setSn(requestData.getLong("sn"));
//		sendMessageCountRecordDao.insertSelective(sendMessageCountRecord);
		if(!ValidationUtil.checkBankCard(requestData.getString("crdNo"))){
			jo.put("result",false);
			jo.put("message","银行卡号不正确");
			return jo;
		}
		if(!ValidationUtil.isIdCard(requestData.getString("idNo"))){
			jo.put("result",false);
			jo.put("message","身份证号不正确");
			return jo;
		}
		if(!ValidationUtil.isMobile(requestData.getString("phoneNo"))){
			jo.put("result",false);
			jo.put("message","手机号不正确");
			return jo;
		}
		if(requestData.getInt("isAgree")!=0){
			jo.put("result",false);
			jo.put("message","请同意协议");
			return jo;
		}

		Optional<OrderForm>  orderFormOptional = orderFormService.selectById(requestData.getLong("orderId"));
		Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderFormOptional.get().getId() + "]不存在");
		if(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_GOING.getId()==orderFormOptional.get().getStatus()){
			jo.put("result",false);
			jo.put("message","该订单已提交，请耐心等待结果");
			return jo;
		}
		if(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_SUCCESS.getId()==orderFormOptional.get().getStatus()){
			jo.put("result",false);
			jo.put("message","该订单已支付完毕");
			return jo;
		}
		BigDecimal amount = orderFormOptional.get().getTotalPrice();
		AuthenData authenData = new AuthenData();
		authenData.setAmount(amount+"");
		authenData.setPhoneNo(requestData.getString("phoneNo"));
		authenData.setCrdNo(requestData.getString("crdNo"));
		authenData.setCapCrdNm(requestData.getString("capCrdNm"));
		authenData.setIdNo(requestData.getString("idNo"));
		authenData.setReqSn(SnGenerator.generate());
		authenData.setAppId(requestData.getString("appid"));
		authenData.setNonceStr(requestData.getString("nonceStr"));
		orderFormOptional.get().setStatus(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_GOING.getId());
		orderFormService.updateStatus(orderFormOptional.get());
		Map<String, Object> ret = this.fastPay(authenData);
		if("0000".equals(ret.get("retCode").toString())){//支付成功
			BindCard bindCard = new BindCard();
			bindCard.setUid(requestData.getString("appid")+"_"+requestData.getString("uid"));
			bindCard.setCardNo(UserBankCardSupporter.encryptCardNo(requestData.getString("crdNo")));
			bindCard.setAccountName(requestData.getString("capCrdNm"));
			bindCard.setCardType("00");
			bindCard.setCardId(UserBankCardSupporter.encryptCardId(requestData.getString("idNo")));
			bindCard.setPhone(requestData.getString("phoneNo"));
			bindCard.setBankCode(requestData.getString("bankCode"));
			bindCard.setStatus(0);
			bindCardService.insertBindCard(bindCard);
			jo.put("result",true);
			jo.put("data",ret.get("retData"));
			jo.put("message","支付成功");
			JSONObject mqJo = new JSONObject();
			mqJo.put("reqSn",authenData.getReqSn());
			mqJo.put("dt", DateFormatUtil.format(new Date(), "yyyyMMdd"));
			mqJo.put("sendCount",0);
			mqJo.put("orderId",requestData.getLong("orderId"));
			MqProducer.sendMessage(mqJo,MqConfig.FAST_PAY_QUERY,2000);
		}else if("5000".equals(ret.get("retCode").toString())){
			jo.put("result",false);
			jo.put("message","付款接口连接失败,等待10s重新链接");
			JSONObject mqJo = new JSONObject();
			mqJo.put("reqSn",authenData.getReqSn());
			mqJo.put("dt", DateFormatUtil.format(new Date(), "yyyyMMdd"));
			mqJo.put("sendCount",0);
			mqJo.put("orderId",requestData.getLong("orderId"));
			MqProducer.sendMessage(mqJo,MqConfig.FAST_PAY_QUERY,2000);
		}else if("3000".equals(ret.get("retCode").toString())){
			jo.put("result",false);
			jo.put("message",ret.get("retMsg"));
		}else{//支付失败
			jo.put("result",false);
			jo.put("message",ret.get("retMsg"));
			ticketService.handleCustomerPayMsg(orderFormOptional.get().getId(),ret.get("reqSn").toString(),false);
		}
		return jo;
	}

	@Override
	public JSONObject toPayByCid(JSONObject requestData) {
		JSONObject jo = new JSONObject();
		Preconditions.checkNotNull(requestData.get("orderId"),"订单号不能为空");
		Preconditions.checkNotNull(requestData.get("cId"),"银行信息不能为空");
		Preconditions.checkNotNull(requestData.get("nonceStr"),"随机参数有误");
		Preconditions.checkNotNull(requestData.get("vCode"),"请输入验证码");
		Preconditions.checkNotNull(requestData.get("sn"),"短信序列码不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getLong("orderId")+""), "订单号不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("nonceStr")), "随机参数有误");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getLong("cId")+""), "银行信息不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("vCode")), "验证码不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getLong("sn")+""), "短信序列码不能为空");
		BindCard bindCard = bindCardService.selectByPrimaryKey(requestData.getLong("cId"));
		if(bindCard==null){
			jo.put("result",false);
			jo.put("message","无此银行卡信息");
			return jo;
		}

		Pair<Integer, String> codeStatus = smsAuthService.checkVerifyCode(bindCard.getPhone(),requestData.getString("vCode"),EnumVerificationCodeType.PAYMENT);
		int resultType = codeStatus.getKey();
		if(resultType!=1){
			jo.put("result",false);
			jo.put("message",codeStatus.getValue());
			return jo;
		}

		int count = sendMessageCountRecordDao.selctCountBySn(requestData.getLong("sn"));
		if(count>3){
			jo.put("result",false);
			jo.put("message","验证码已失效");
			return jo;
		}
		SendMessageCountRecord sendMessageCountRecord= new SendMessageCountRecord();
		sendMessageCountRecord.setUid(requestData.getString("appid")+"_"+requestData.getString("uid"));
		sendMessageCountRecord.setMessageTemplateId((long)EnumVerificationCodeType.PAYMENT.getId());
		sendMessageCountRecord.setMobile(bindCard.getPhone());
		sendMessageCountRecord.setSn(requestData.getLong("sn"));
		sendMessageCountRecordDao.insertSelective(sendMessageCountRecord);

		Optional<OrderForm>  orderFormOptional = orderFormService.selectById(requestData.getLong("orderId"));
		Preconditions.checkState(orderFormOptional.isPresent(), "订单[" + orderFormOptional.get().getId() + "]不存在");
		if(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_GOING.getId()==orderFormOptional.get().getStatus()){
			jo.put("result",false);
			jo.put("message","该订单已提交，请耐心等待结果");
			return jo;
		}
		if(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_SUCCESS.getId()==orderFormOptional.get().getStatus()){
			jo.put("result",false);
			jo.put("message","该订单已支付完毕");
			return jo;
		}
		BigDecimal amount = orderFormOptional.get().getTotalPrice();
		AuthenData authenData = new AuthenData();
		authenData.setAmount(amount+"");
		authenData.setPhoneNo(bindCard.getPhone());
		authenData.setCrdNo(UserBankCardSupporter.decryptCardNo(bindCard.getCardNo()));
		authenData.setCapCrdNm(bindCard.getAccountName());
		authenData.setIdNo(UserBankCardSupporter.decryptCardId(bindCard.getCardId()));
		authenData.setReqSn(SnGenerator.generate());
		authenData.setAppId(requestData.getString("appid"));
		authenData.setNonceStr(requestData.getString("nonceStr"));
		orderFormOptional.get().setStatus(EnumOrderFormStatus.ORDER_FORM_CUSTOMER_PAY_GOING.getId());
		orderFormService.updateStatus(orderFormOptional.get());
		Map<String, Object> ret = this.fastPay(authenData);
		if("0000".equals(ret.get("retCode").toString())){//支付成功
			jo.put("result",true);
			jo.put("data",ret.get("retData"));
			jo.put("message","支付成功,可能会有一段时间的延迟");
			JSONObject mqJo = new JSONObject();
			mqJo.put("reqSn",authenData.getReqSn());
			mqJo.put("dt", DateFormatUtil.format(new Date(), "yyyyMMdd"));
			mqJo.put("sendCount",0);
			mqJo.put("orderId",requestData.getLong("orderId"));
			MqProducer.sendMessage(mqJo,MqConfig.FAST_PAY_QUERY,2000);
		}else if("5000".equals(ret.get("retCode").toString())){
			jo.put("result",false);
			jo.put("message","付款接口连接失败");
			JSONObject mqJo = new JSONObject();
			mqJo.put("reqSn",authenData.getReqSn());
			mqJo.put("dt", DateFormatUtil.format(new Date(), "yyyyMMdd"));
			mqJo.put("sendCount",0);
			mqJo.put("orderId",requestData.getLong("orderId"));
			MqProducer.sendMessage(mqJo,MqConfig.FAST_PAY_QUERY,2000);
		}else if("3000".equals(ret.get("retCode").toString())){
			jo.put("result",false);
			jo.put("message",ret.get("retMsg"));
		}else{//支付失败
			jo.put("result",false);
			jo.put("message",ret.get("retMsg"));
			ticketService.handleCustomerPayMsg(orderFormOptional.get().getId(),ret.get("reqSn").toString(),false);
		}
		return jo;
	}

	@Override
	public JSONObject toPayGrab(JSONObject requestData) throws Exception {
		JSONObject jo = new JSONObject();
		Preconditions.checkNotNull(requestData.get("crdNo"),"缺少银行卡号");
		Preconditions.checkNotNull(requestData.get("orderId"),"订单号不能为空");
		Preconditions.checkNotNull(requestData.get("capCrdNm"),"缺少开户姓名");
		Preconditions.checkNotNull(requestData.get("idNo"),"缺少身份证号");
		Preconditions.checkNotNull(requestData.get("phoneNo"),"缺少手机号");
		Preconditions.checkNotNull(requestData.get("isAgree"),"请勾选并同意协议");
		Preconditions.checkNotNull(requestData.get("vCode"),"请输入验证码");
		Preconditions.checkNotNull(requestData.get("bankCode"),"卡宾不能为空");
		Preconditions.checkNotNull(requestData.get("nonceStr"),"随机参数有误");
		Preconditions.checkNotNull(requestData.get("sn"),"短信序列码不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getLong("orderId")+""), "订单信息不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("crdNo")), "卡号不能不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("capCrdNm")), "开户姓名不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("idNo")), "身份证号不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("phoneNo")), "手机号不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("bankCode")), "卡宾不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("vCode")), "验证码不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("nonceStr")), "随机参数有误");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getLong("sn")+""), "短信序列码不能为空");

		Pair<Integer, String> codeStatus = smsAuthService.checkVerifyCode(requestData.getString("phoneNo"),requestData.getString("vCode"),EnumVerificationCodeType.PAYMENT);
		int resultType = codeStatus.getKey();
		if(resultType!=1){
			jo.put("result",false);
			jo.put("message",codeStatus.getValue());
			return jo;
		}

		int count = sendMessageCountRecordDao.selctCountBySn(requestData.getLong("sn"));
		if(count>3){
			jo.put("result",false);
			jo.put("message","验证码已失效");
			return jo;
		}
		SendMessageCountRecord sendMessageCountRecord= new SendMessageCountRecord();
		sendMessageCountRecord.setUid(requestData.getString("appid")+"_"+requestData.getString("uid"));
		sendMessageCountRecord.setMessageTemplateId((long)EnumVerificationCodeType.PAYMENT.getId());
		sendMessageCountRecord.setMobile(requestData.getString("phoneNo"));
		sendMessageCountRecord.setSn(requestData.getLong("sn"));
		sendMessageCountRecordDao.insertSelective(sendMessageCountRecord);


		if(!ValidationUtil.checkBankCard(requestData.getString("crdNo"))){
			jo.put("result",false);
			jo.put("message","银行卡号不正确");
			return jo;
		}
		if(!ValidationUtil.isIdCard(requestData.getString("idNo"))){
			jo.put("result",false);
			jo.put("message","身份证号不正确");
			return jo;
		}
		if(!ValidationUtil.isMobile(requestData.getString("phoneNo"))){
			jo.put("result",false);
			jo.put("message","手机号不正确");
			return jo;
		}
		if(requestData.getInt("isAgree")!=0){
			jo.put("result",false);
			jo.put("message","请同意协议");
			return jo;
		}

		Optional<GrabTicketForm> grabTicketFormOptional = grabTicketFormService.selectById(requestData.getLong("orderId"));
		Preconditions.checkState(grabTicketFormOptional.isPresent(), "订单[" + grabTicketFormOptional.get().getId() + "]不存在");
		if(EnumGrabTicketStatus.GRAB_FORM_PAY_ING.getId()==grabTicketFormOptional.get().getStatus()){
			jo.put("result",false);
			jo.put("message","该订单已提交，请耐心等待结果");
			return jo;
		}
		if(EnumGrabTicketStatus.GRAB_FORM_PAY_SUCCESS.getId()==grabTicketFormOptional.get().getStatus()){
			jo.put("result",false);
			jo.put("message","该订单已支付完毕");
			return jo;
		}
		BigDecimal amount = grabTicketFormOptional.get().getTotalPrice();
		grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_PAY_ING,requestData.getLong("orderId"));

		AuthenData authenData = new AuthenData();
		authenData.setAmount(amount+"");
		authenData.setPhoneNo(requestData.getString("phoneNo"));
		authenData.setCrdNo(requestData.getString("crdNo"));
		authenData.setCapCrdNm(requestData.getString("capCrdNm"));
		authenData.setIdNo(requestData.getString("idNo"));
		authenData.setReqSn(SnGenerator.generate());
		authenData.setNonceStr(requestData.getString("nonceStr"));
		authenData.setAppId(requestData.getString("appid"));

		Map<String, Object> ret = this.fastPay(authenData);
		if("0000".equals(ret.get("retCode").toString())){//支付成功
			BindCard bindCard = new BindCard();
			bindCard.setUid(requestData.getString("appid")+"_"+requestData.getString("uid"));
			bindCard.setCardNo(UserBankCardSupporter.encryptCardNo(requestData.getString("crdNo")));
			bindCard.setAccountName(requestData.getString("capCrdNm"));
			bindCard.setCardType("00");
			bindCard.setCardId(UserBankCardSupporter.encryptCardId(requestData.getString("idNo")));
			bindCard.setPhone(requestData.getString("phoneNo"));
			bindCard.setBankCode(requestData.getString("bankCode"));
			bindCard.setStatus(0);
			bindCardService.insertBindCard(bindCard);
			jo.put("result",true);
			jo.put("data",ret.get("retData"));
			jo.put("message","支付成功");

			JSONObject mqJo = new JSONObject();
			mqJo.put("reqSn",authenData.getReqSn());
			mqJo.put("dt", DateFormatUtil.format(new Date(), "yyyyMMdd"));
			mqJo.put("sendCount",0);
			mqJo.put("orderId",requestData.getLong("orderId"));
			MqProducer.sendMessage(mqJo,MqConfig.FAST_PAY_GRAB_QUERY,2000);
		}else if("5000".equals(ret.get("retCode").toString())){
			jo.put("result",false);
			jo.put("message","付款接口连接失败,等待10s重新链接");
			JSONObject mqJo = new JSONObject();
			mqJo.put("reqSn",authenData.getReqSn());
			mqJo.put("dt", DateFormatUtil.format(new Date(), "yyyyMMdd"));
			mqJo.put("sendCount",0);
			mqJo.put("orderId",requestData.getLong("orderId"));
			MqProducer.sendMessage(mqJo,MqConfig.FAST_PAY_GRAB_QUERY,2000);
		}else if("3000".equals(ret.get("retCode").toString())){
			jo.put("result",false);
			jo.put("message",ret.get("retMsg"));
		}else{//支付失败
			jo.put("result",false);
			jo.put("message",ret.get("retMsg"));
			ticketService.handleGrabCustomerPayMsg(requestData.getLong("orderId"),ret.get("reqSn").toString(),false);
		}
		return jo;
	}

	@Override
	public JSONObject toPayGrabByCid(JSONObject requestData) throws Exception {
		JSONObject jo = new JSONObject();
		Preconditions.checkNotNull(requestData.get("orderId"),"订单号不能为空");
		Preconditions.checkNotNull(requestData.get("cId"),"银行信息不能为空");
		Preconditions.checkNotNull(requestData.get("nonceStr"),"随机参数有误");
		Preconditions.checkNotNull(requestData.get("vCode"),"请输入验证码");
		Preconditions.checkNotNull(requestData.get("sn"),"短信序列码不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getLong("orderId")+""), "订单号不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("nonceStr")), "随机参数有误");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getLong("cId")+""), "银行信息不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getString("vCode")), "验证码不能为空");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(requestData.getLong("sn")+""), "短信序列码不能为空");
		BindCard bindCard = bindCardService.selectByPrimaryKey(requestData.getLong("cId"));
		if(bindCard==null){
			jo.put("result",false);
			jo.put("message","无此银行卡信息");
			return jo;
		}
		Pair<Integer, String> codeStatus = smsAuthService.checkVerifyCode(bindCard.getPhone(),requestData.getString("vCode"),EnumVerificationCodeType.PAYMENT);
		int resultType = codeStatus.getKey();
		if(resultType!=1){
			jo.put("result",false);
			jo.put("message",codeStatus.getValue());
			return jo;
		}

		int count = sendMessageCountRecordDao.selctCountBySn(requestData.getLong("sn"));
		if(count>3){
			jo.put("result",false);
			jo.put("message","验证码已失效");
			return jo;
		}
		SendMessageCountRecord sendMessageCountRecord= new SendMessageCountRecord();
		sendMessageCountRecord.setUid(requestData.getString("appid")+"_"+requestData.getString("uid"));
		sendMessageCountRecord.setMessageTemplateId((long)EnumVerificationCodeType.PAYMENT.getId());
		sendMessageCountRecord.setMobile(bindCard.getPhone());
		sendMessageCountRecord.setSn(requestData.getLong("sn"));
		sendMessageCountRecordDao.insertSelective(sendMessageCountRecord);


		Optional<GrabTicketForm> grabTicketFormOptional = grabTicketFormService.selectById(requestData.getLong("orderId"));
		Preconditions.checkState(grabTicketFormOptional.isPresent(), "订单[" + grabTicketFormOptional.get().getId() + "]不存在");
		if(EnumGrabTicketStatus.GRAB_FORM_PAY_ING.getId()==grabTicketFormOptional.get().getStatus()){
			jo.put("result",false);
			jo.put("message","该订单已提交，请耐心等待结果");
			return jo;
		}
		if(EnumGrabTicketStatus.GRAB_FORM_PAY_SUCCESS.getId()==grabTicketFormOptional.get().getStatus()){
			jo.put("result",false);
			jo.put("message","该订单已支付完毕");
			return jo;
		}

		BigDecimal amount = grabTicketFormOptional.get().getTotalPrice();
		grabTicketFormService.updateStatusById(EnumGrabTicketStatus.GRAB_FORM_PAY_ING,requestData.getLong("orderId"));


		AuthenData authenData = new AuthenData();
		authenData.setAmount(amount+"");
		authenData.setPhoneNo(bindCard.getPhone());
		authenData.setCrdNo(UserBankCardSupporter.decryptCardNo(bindCard.getCardNo()));
		authenData.setCapCrdNm(bindCard.getAccountName());
		authenData.setIdNo(UserBankCardSupporter.decryptCardId(bindCard.getCardId()));
		authenData.setReqSn(SnGenerator.generate());
		authenData.setNonceStr(requestData.getString("nonceStr"));
		authenData.setAppId(requestData.getString("appid"));
		Map<String, Object> ret = this.fastPay(authenData);
		if("0000".equals(ret.get("retCode").toString())){//支付成功
			jo.put("result",true);
			jo.put("data",ret.get("retData"));
			jo.put("message","支付成功,可能会有一段时间的延迟");
			JSONObject mqJo = new JSONObject();
			mqJo.put("reqSn",authenData.getReqSn());
			mqJo.put("dt", DateFormatUtil.format(new Date(), "yyyyMMdd"));
			mqJo.put("sendCount",0);
			mqJo.put("orderId",requestData.getLong("orderId"));
			MqProducer.sendMessage(mqJo,MqConfig.FAST_PAY_GRAB_QUERY,2000);
		}else if("5000".equals(ret.get("retCode").toString())){
			jo.put("result",false);
			jo.put("message","付款接口连接失败");
			JSONObject mqJo = new JSONObject();
			mqJo.put("reqSn",authenData.getReqSn());
			mqJo.put("dt", DateFormatUtil.format(new Date(), "yyyyMMdd"));
			mqJo.put("sendCount",0);
			mqJo.put("orderId",requestData.getLong("orderId"));
			MqProducer.sendMessage(mqJo,MqConfig.FAST_PAY_GRAB_QUERY,2000);
		}else if("3000".equals(ret.get("retCode").toString())){
			jo.put("result",false);
			jo.put("message",ret.get("retMsg"));
		}else{//支付失败
			jo.put("result",false);
			jo.put("message",ret.get("retMsg"));
			ticketService.handleGrabCustomerPayMsg(requestData.getLong("orderId"),ret.get("reqSn").toString(),false);
		}
		return jo;
	}

	@Override
	public JSONObject getCode(JSONObject requestData) {
		JSONObject jo = new JSONObject();
		Preconditions.checkNotNull(requestData.get("phone"),"手机号不能为空");
		Preconditions.checkNotNull(requestData.get("amount"),"支付金额不能为空");
		if(!ValidationUtil.isMobile(requestData.getString("phone"))){
			jo.put("result",false);
			jo.put("message","手机号格式不正确");
			return requestData;
		}
		SendPaymentParam sendPaymentParam = new SendPaymentParam();
		sendPaymentParam.setAmount(requestData.getString("amount"));

		Pair<Integer, String> codeStatus = smsAuthService.getVerifyCode(requestData.getString("phone"),EnumVerificationCodeType.PAYMENT);
		int resultType = codeStatus.getKey();
		if(resultType!=1){
			jo.put("result",false);
			jo.put("message",codeStatus.getValue());
			return jo;
		}
		sendPaymentParam.setCode(codeStatus.getValue());
		sendPaymentParam.setMobile(requestData.getString("phone"));
		sendPaymentParam.setUid(requestData.getString("uid"));
		long sn = ticketSendMessageService.sendPaymentMessage(sendPaymentParam);
		jo.put("result",true);
		jo.put("data",sn);
		jo.put("message","发送验证码成功");
		return jo;
	}
}
