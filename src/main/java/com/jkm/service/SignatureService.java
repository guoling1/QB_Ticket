package com.jkm.service;

/**
 * 系统签名处理
 * @author wujh
 *
 */
public interface SignatureService {
	/**
	 * 通过原始报文和商户号对报文进行验签
	 * @param merchatId 商户号
	 * @param orgMsg 系统请求报文
	 * @param remoteIP 客户IP
	 * @return
	 */
	public boolean isSignature(String orgMsg);
	
	/**
	 * 响应报文加签处理
	 * @param orgMsg
	 * @return
	 */
	public String addSignatrue(String orgMsg);
}
