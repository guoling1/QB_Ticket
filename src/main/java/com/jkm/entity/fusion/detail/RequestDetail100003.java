package com.jkm.entity.fusion.detail;

import javax.xml.bind.annotation.*;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "TRANS_DETAIL",propOrder={"ORG_SN","ORG_DATE","REFUND_AMOUNT","ORG_AMOUNT","MERCHANT_ID","REFUND_ORD_NO","ORD_DATE",
		"REFUND_REASON","TRANS_TYPE"})
public class RequestDetail100003 {
	@XmlElement(name = "ORG_SN")
	private String ORG_SN;
	@XmlElement(name = "ORG_DATE")
	private String ORG_DATE;
	@XmlElement(name = "REFUND_AMOUNT")
	private String REFUND_AMOUNT;
	@XmlElement(name = "ORG_AMOUNT")
	private String ORG_AMOUNT;
	@XmlElement(name = "MERCHANT_ID")
	private String MERCHANT_ID;
	@XmlElement(name = "REFUND_ORD_NO")
	private String REFUND_ORD_NO;
	@XmlElement(name = "ORD_DATE")
	private String ORD_DATE;
	@XmlElement(name = "REFUND_REASON")
	private String REFUND_REASON;
	@XmlElement(name = "TRANS_TYPE")
	private String TRANS_TYPE;

	public String getORG_SN() {
		return ORG_SN;
	}

	public void setORG_SN(String ORG_SN) {
		this.ORG_SN = ORG_SN;
	}

	public String getORG_DATE() {
		return ORG_DATE;
	}

	public void setORG_DATE(String ORG_DATE) {
		this.ORG_DATE = ORG_DATE;
	}

	public String getREFUND_AMOUNT() {
		return REFUND_AMOUNT;
	}

	public void setREFUND_AMOUNT(String REFUND_AMOUNT) {
		this.REFUND_AMOUNT = REFUND_AMOUNT;
	}

	public String getORG_AMOUNT() {
		return ORG_AMOUNT;
	}

	public void setORG_AMOUNT(String ORG_AMOUNT) {
		this.ORG_AMOUNT = ORG_AMOUNT;
	}

	public String getMERCHANT_ID() {
		return MERCHANT_ID;
	}

	public void setMERCHANT_ID(String MERCHANT_ID) {
		this.MERCHANT_ID = MERCHANT_ID;
	}

	public String getREFUND_ORD_NO() {
		return REFUND_ORD_NO;
	}

	public void setREFUND_ORD_NO(String REFUND_ORD_NO) {
		this.REFUND_ORD_NO = REFUND_ORD_NO;
	}

	public String getORD_DATE() {
		return ORD_DATE;
	}

	public void setORD_DATE(String ORD_DATE) {
		this.ORD_DATE = ORD_DATE;
	}

	public String getREFUND_REASON() {
		return REFUND_REASON;
	}

	public void setREFUND_REASON(String REFUND_REASON) {
		this.REFUND_REASON = REFUND_REASON;
	}

	public String getTRANS_TYPE() {
		return TRANS_TYPE;
	}

	public void setTRANS_TYPE(String TRANS_TYPE) {
		this.TRANS_TYPE = TRANS_TYPE;
	}
}

