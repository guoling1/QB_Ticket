package com.jkm.entity.fusion;

public class AuthenData {
	/**
	 * 商户号
	 */
	private String mercId;
	/**
	 * 交易流水号
	 */
	private String reqSn;
	/**
	 * 同步通知页面路径
	 */
	private String retUrl;
	/**
	 * 异步通知页面路径
	 */
	private String notifyUrl;
	/**
	 * 卡号（必传）
	 */
	private String crdNo;
	/**
	 *银行账户名（必传）
	 */
	private String capCrdNm;
	/**
	 * 金额（必传）
	 */
	private String amount;
	/**
	 * 证件类型（默认"00"）
	 */
	private String idType;
	/**
	 * 证件号（必传）
	 */
	private String idNo;
	/**
	 * 手机号（必传）
	 */
	private String phoneNo;
	/**
	 * 手机验证码
	 */
	private String verifyCode;
	/**
	 * 执行步骤
	 */
	private String step;
	/**
	 * 令牌
	 */
	private String token;

	public String getMercId() {
		return mercId;
	}

	public void setMercId(String mercId) {
		this.mercId = mercId;
	}

	public String getReqSn() {
		return reqSn;
	}

	public void setReqSn(String reqSn) {
		this.reqSn = reqSn;
	}

	public String getRetUrl() {
		return retUrl;
	}

	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getCrdNo() {
		return crdNo;
	}

	public void setCrdNo(String crdNo) {
		this.crdNo = crdNo;
	}

	public String getCapCrdNm() {
		return capCrdNm;
	}

	public void setCapCrdNm(String capCrdNm) {
		this.capCrdNm = capCrdNm;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
