package com.jkm.entity.fusion;

public class QueryQuickPayData {
	/**
	 * 商户号
	 */
	private String mercId;
	/**
	 * 交易流水号
	 */
	private String reqSn;
	/**
	 * 商户订单编号
	 */
	private String mercOrdNo;
	/**
	 * 商户订单日期
	 */
	private String mercOrdDt;

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

	public String getMercOrdNo() {
		return mercOrdNo;
	}

	public void setMercOrdNo(String mercOrdNo) {
		this.mercOrdNo = mercOrdNo;
	}

	public String getMercOrdDt() {
		return mercOrdDt;
	}

	public void setMercOrdDt(String mercOrdDt) {
		this.mercOrdDt = mercOrdDt;
	}
}
