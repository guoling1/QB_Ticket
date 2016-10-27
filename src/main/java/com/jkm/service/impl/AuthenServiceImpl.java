package com.jkm.service.impl;

import com.jkm.entity.fusion.*;
import com.jkm.entity.fusion.body.RequestBody100003;
import com.jkm.entity.fusion.body.RequestBody100005;
import com.jkm.entity.fusion.body.RequestBody200005;
import com.jkm.entity.fusion.detail.RequestDetail100003;
import com.jkm.entity.fusion.detail.RequestDetail100005;
import com.jkm.entity.fusion.detail.RequestDetail200005;
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
import java.util.Map;

/**
 * 鉴权
 * 
 * @author feng.xi
 * 
 */
@Service
public class AuthenServiceImpl implements AuthenService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SignatureService signatureService;

	@Override
	public Map<String, Object> fastPay(AuthenData requestData) {
		Map<String, Object> ret = new HashMap<String, Object>();
		// PayUnionExcelReader reader = new PayUnionExcelReader();
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
		head.setTrxCode("100005");
		head.setVersion("01");
		head.setDataType(Constants.DATA_TYPE_XML);
		head.setLevel(Constants.LEVEL_0);
		if ("1".equals(requestData.getStep())) {
			head.setReqSn(requestData.getReqSn());
		} else {
			head.setReqSn(DateUtils.getDateString(new Date(),
					DateUtils.formate_string_yyyyMMddhhmmss));
		}

		head.setSignedMsg("signedMsg");

		RequestBody100005 body = new RequestBody100005();
		RequestDetail100005 detail = new RequestDetail100005();
		detail.setCRD_TYP(requestData.getCrdTyp());
		detail.setMERCHANT_ID(requestData.getMercId());
		detail.setACCOUNT_NAME(requestData.getCapCrdNm());
		detail.setCARD_NO(requestData.getCrdNo());
		detail.setCRE_CVN2(requestData.getCvn2());
		detail.setCRE_VAL_DATE(requestData.getExpireDate());
		detail.setID(requestData.getIdNo());
		detail.setID_TYPE(requestData.getIdTyp());
		detail.setMERCHANT_ID(detail.getMERCHANT_ID());
		detail.setNOTIFY_URL("");
		detail.setRET_URL("");
		detail.setSEND_DT(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMdd));
		detail.setSEND_TIME(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMddhhmmss));
		detail.setSTEP_NO(requestData.getStep());
		detail.setTEL(requestData.getPhoneNo());
		detail.setAMOUNT(requestData.getAmount());

		if (!"0".equals(requestData.getStep())) {
			detail.setTOKEN(requestData.getToken());
			detail.setTEL_CAPTCHA(requestData.getVerifyCode());
		}
		body.setTransDetail(detail);
		authen.setBody(body);
		authen.setInfo(head);
		return authen;
	}

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

	@Override
	public Map<String, Object> fastPayQuery(FastQueryData requestData) {
		Map<String, Object> ret = new HashMap<String, Object>();
		// PayUnionExcelReader reader = new PayUnionExcelReader();
		try {
			Request200005 request200005 = createFastPayQuery(requestData);
			String xml = XmlUtil.toXML(request200005);
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
			String response1 = HttpUtils.sendPostMessage(Base64,
					Constants.FASTPAY_QUERY_URL, Constants.transfer_charset);
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
				Response200005 response200005 = XmlUtil.fromXML(response2,
						Response200005.class);

				if ("0000".equals(response200005.getInfo().getRetCode())) {
					ret.put("retCode", true);
					ret.put("retMsg", response200005.getInfo().getErrMsg());
					ret.put("retXml", response2);
				} else {
					ret.put("retCode", false);
					ret.put("retMsg", response200005.getInfo().getErrMsg());
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

	private Request200005 createFastPayQuery(FastQueryData requestData) {
		Request200005 request = new Request200005();
		RequestHead head = new RequestHead();
		head.setTrxCode("200005");
		head.setVersion("01");
		head.setDataType(Constants.DATA_TYPE_XML);
		head.setLevel(Constants.LEVEL_0);
		head.setReqSn(DateUtils.getDateString(new Date(),
				DateUtils.formate_string_yyyyMMddhhmmss));
		head.setSignedMsg("signedMsg");
		head.setMercId(requestData.getMercId());
		RequestBody200005 body = new RequestBody200005();
		RequestDetail200005 detail = new RequestDetail200005();
		detail.setMercOrdDt(requestData.getOrdDt());
		detail.setMercOrdNo(requestData.getQuerySn());
		detail.setMerchantId(requestData.getMercId());
		body.setQueryTrans(detail);
		request.setBody(body);
		request.setInfo(head);
		return request;
	}

}
