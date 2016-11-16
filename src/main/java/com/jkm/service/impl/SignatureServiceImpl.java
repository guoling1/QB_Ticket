package com.jkm.service.impl;

import com.jkm.service.SignatureService;
import com.jkm.util.fusion.UlpayRaTools;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service
public class SignatureServiceImpl  implements SignatureService {
	private static Logger logger = Logger.getLogger(SignatureServiceImpl.class);

	public boolean isSignature(String orgMsg) {
		if (StringUtils.isEmpty(orgMsg)) {
			return false;
		}
		StringBuffer orgBuf = new StringBuffer();
		String startSign = "<SIGNED_MSG>";
		String endSign = "</SIGNED_MSG>";
		int startIndex = orgMsg.indexOf(startSign);
		if(startIndex <=0 ){
			return false;
		}
		int endIndex = orgMsg.indexOf(endSign);
		if(endIndex <=0 ){
			return false;
		}
		orgBuf.append(orgMsg.substring(0, startIndex)).append(startSign).append(endSign)
				.append(orgMsg.substring(endIndex + endSign.length()));
		String signStr = orgMsg.substring(startIndex + startSign.length(), endIndex);
		Map<String, String> signRet = UlpayRaTools.getInstance().verify(signStr, orgBuf.toString());
		logger.info("验签结果==>{"+signRet+"}");
		if (!UlpayRaTools.SUCCESS.equals(signRet.get(UlpayRaTools.RET_CODE))) {
			logger.error("验签失败 ==>"+signRet.get(UlpayRaTools.RET_MSG));
			throw new IllegalArgumentException(signRet.get(UlpayRaTools.RET_MSG));
		}
		return true;
	}

	public String addSignatrue(String orgMsg) {
		String signLabel = "<SIGNED_MSG>signedMsg</SIGNED_MSG>";
		String signLabelEmpty = "<SIGNED_MSG></SIGNED_MSG>";
		orgMsg = orgMsg.replace(signLabel, signLabelEmpty);
		StringBuffer signBuffer = new StringBuffer("<SIGNED_MSG>").append(UlpayRaTools.getInstance().sign(orgMsg))
				.append("</SIGNED_MSG>");
		return orgMsg.replace(signLabelEmpty, signBuffer.toString());
	}
}
