package com.jkm.entity.fusion.detail;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "RET_DETAIL", propOrder={"payInOrdNo", "mercId", "mercOrdNo"
		, "ordDt", "ordExpDt", "busiType", "aplAmt", "ccy", "use", "orderStatus"
		, "errMsg" })

public class ResponseDetail200005 {
	
	@XmlElement(name="PAYIN_ORD_NO")
	private String payInOrdNo;
	@XmlElement(name="MERCHANT_ID")
	private String mercId;
	@XmlElement(name="MERC_ORD_NO")
	private String mercOrdNo;
	@XmlElement(name="ORD_DT")
	private String ordDt;
	@XmlElement(name="ORD_EXP_DT")
	private String ordExpDt;
	@XmlElement(name="BUSI_TYPE")
	private String busiType;
	@XmlElement(name="APL_AMT")
	private String aplAmt;
	@XmlElement(name="CCY")
	private String ccy;
	@XmlElement(name="USE")
	private String use;
	@XmlElement(name="ORDER_STATUS")
	private String orderStatus;
	@XmlElement(name="ERR_MSG")
	private String errMsg;
	public String getPayInOrdNo() {
		return payInOrdNo;
	}
	public void setPayInOrdNo(String payInOrdNo) {
		this.payInOrdNo = payInOrdNo;
	}
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
	public String getOrdDt() {
		return ordDt;
	}
	public void setOrdDt(String ordDt) {
		this.ordDt = ordDt;
	}
	public String getOrdExpDt() {
		return ordExpDt;
	}
	public void setOrdExpDt(String ordExpDt) {
		this.ordExpDt = ordExpDt;
	}
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
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
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResponseDetail200005 [payInOrdNo=");
		builder.append(payInOrdNo);
		builder.append(", mercId=");
		builder.append(mercId);
		builder.append(", mercOrdNo=");
		builder.append(mercOrdNo);
		builder.append(", ordDt=");
		builder.append(ordDt);
		builder.append(", ordExpDt=");
		builder.append(ordExpDt);
		builder.append(", busiType=");
		builder.append(busiType);
		builder.append(", aplAmt=");
		builder.append(aplAmt);
		builder.append(", ccy=");
		builder.append(ccy);
		builder.append(", use=");
		builder.append(use);
		builder.append(", orderStatus=");
		builder.append(orderStatus);
		builder.append(", errMsg=");
		builder.append(errMsg);
		builder.append("]");
		return builder.toString();
	}
}
