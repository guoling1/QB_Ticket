package com.jkm.entity.fusion;


/**
 * @ClassName: NetBankRequest
 * @Description: 商户网关-网银支付请求信息
 * @author hanmy001@ulic.com.cn
 * @date 2016年7月19日 下午3:03:22
 * 
 */
public class NetBankPayRequest {
	/**
	 * @Fields mercId : 商户编号
	 */
	private String mercId;
	/**
	 * @Fields mercOrdNo : 商户订单号
	 */
	private String mercOrdNo;

	/**
	 * @Fields mercOrdDt : 商户订单日期
	 */
	private String merOrdDate;

	/**
	 * @Fields ordExpDt : 订单过期日期
	 */
	private String ordExpDt;
	/**
	 * @Fields aplAmt : 交易金额
	 */
	private String aplAmt;

	/**
	 * @Fields ccy : 币种
	 */
	private String ccy;
	/**
	 * @Fields busiType : 业务类型
	 */
	private String busType;
	/**
	 * @Fields bankCode : 银行代码
	 */
	private String bankCode;
	/**
	 * @Fields notifyUrl : 通知商户地址
	 */
	private String notifyUrl;
	/**
	 * @Fields pageReturnUrl : 前台通知商户地址
	 */
	private String retUrl;
	/**
	 * @Fields useOf : 用途
	 */
	private String useOf;
	
	/** 
	 * @Fields signmsg : 加签字段
	 */
	private String merchantSign;

	public String getMercId() {
		return mercId;
	}

	public void setMercId(String mercId) {
		this.mercId = mercId;
	}

	public String getMercOrdNo() {
		return mercOrdNo;
	}

	public void setMercOrdNo(String mercOrdNo) {
		this.mercOrdNo = mercOrdNo;
	}

	public String getMerOrdDate() {
		return merOrdDate;
	}

	public void setMerOrdDate(String merOrdDate) {
		this.merOrdDate = merOrdDate;
	}

	public String getOrdExpDt() {
		return ordExpDt;
	}

	public void setOrdExpDt(String ordExpDt) {
		this.ordExpDt = ordExpDt;
	}

	public String getAplAmt() {
		return aplAmt;
	}

	public void setAplAmt(String aplAmt) {
		this.aplAmt = aplAmt;
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getRetUrl() {
		return retUrl;
	}

	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}

	public String getUseOf() {
		return useOf;
	}

	public void setUseOf(String useOf) {
		this.useOf = useOf;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NetBankPayRequest [mercId=");
		builder.append(mercId);
		builder.append(", mercOrdNo=");
		builder.append(mercOrdNo);
		builder.append(", merOrdDate=");
		builder.append(merOrdDate);
		builder.append(", ordExpDt=");
		builder.append(ordExpDt);
		builder.append(", aplAmt=");
		builder.append(aplAmt);
		builder.append(", ccy=");
		builder.append(ccy);
		builder.append(", busType=");
		builder.append(busType);
		builder.append(", bankCode=");
		builder.append(bankCode);
		builder.append(", notifyUrl=");
		builder.append(notifyUrl);
		builder.append(", retUrl=");
		builder.append(retUrl);
		builder.append(", useOf=");
		builder.append(useOf);
		builder.append(", merchantSign=");
		builder.append(merchantSign);
		builder.append("]");
		return builder.toString();
	}

	public String getMerchantSign() {
		return merchantSign;
	}

	public void setMerchantSign(String merchantSign) {
		this.merchantSign = merchantSign;
	}


}