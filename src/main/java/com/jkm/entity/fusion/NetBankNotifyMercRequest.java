package com.jkm.entity.fusion;


/**
 * @ClassName: NetBankNotifyMercRequest
 * @Description:网银支付后台通知接收请求信息
 * @author hanmy001@ulic.com.cn
 * @date 2016年7月24日 下午2:07:40
 * 
 */
public class NetBankNotifyMercRequest {

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
	 * @Fields orderStauts : 订单状态
	 */
	private String status;

	/**
	 * @Fields payInOrdNo : 支付订单号
	 */
	private String payInOrdNo;

	/**
	 * @Fields payDate :支付日期
	 */
	private String payDate;
	/**
	 * @Fields payTime : 支付时间
	 */
	private String payTime;

	private String merchantSign;

	private String respCode;
	private String respMsg;

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
	public String getPayInOrdNo() {
		return payInOrdNo;
	}

	public void setPayInOrdNo(String payInOrdNo) {
		this.payInOrdNo = payInOrdNo;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getMerOrdDate() {
		return merOrdDate;
	}

	public void setMerOrdDate(String merOrdDate) {
		this.merOrdDate = merOrdDate;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getMerchantSign() {
		return merchantSign;
	}

	public void setMerchantSign(String merchantSign) {
		this.merchantSign = merchantSign;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NetBankNotifyMercRequest [mercId=");
		builder.append(mercId);
		builder.append(", mercOrdNo=");
		builder.append(mercOrdNo);
		builder.append(", merOrdDate=");
		builder.append(merOrdDate);
		builder.append(", status=");
		builder.append(status);
		builder.append(", payInOrdNo=");
		builder.append(payInOrdNo);
		builder.append(", payDate=");
		builder.append(payDate);
		builder.append(", payTime=");
		builder.append(payTime);
		builder.append(", merchantSign=");
		builder.append(merchantSign);
		builder.append(", respCode=");
		builder.append(respCode);
		builder.append(", respMsg=");
		builder.append(respMsg);
		builder.append("]");
		return builder.toString();
	}

}
