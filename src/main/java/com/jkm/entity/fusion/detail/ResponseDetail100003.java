package com.jkm.entity.fusion.detail;

import javax.xml.bind.annotation.*;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "RET_DETAIL", propOrder={"ORD_NO", "MERC_ID", "ORD_DT"
		, "REFUND_AMOUNT", "RET_CODE", "ERR_MSG"})
public class ResponseDetail100003 {
	@XmlElement(name="ORD_NO")
	private String ORD_NO;
	@XmlElement(name="MERC_ID")
	private String MERC_ID;
	@XmlElement(name="ORD_DT")
	private String ORD_DT;
	@XmlElement(name="REFUND_AMOUNT")
	private String REFUND_AMOUNT;
	@XmlElement(name="RET_CODE")
	private String RET_CODE;
	@XmlElement(name="ERR_MSG")
	private String ERR_MSG;

	public String getORD_NO() {
		return ORD_NO;
	}

	public void setORD_NO(String ORD_NO) {
		this.ORD_NO = ORD_NO;
	}

	public String getMERC_ID() {
		return MERC_ID;
	}

	public void setMERC_ID(String MERC_ID) {
		this.MERC_ID = MERC_ID;
	}

	public String getORD_DT() {
		return ORD_DT;
	}

	public void setORD_DT(String ORD_DT) {
		this.ORD_DT = ORD_DT;
	}

	public String getREFUND_AMOUNT() {
		return REFUND_AMOUNT;
	}

	public void setREFUND_AMOUNT(String REFUND_AMOUNT) {
		this.REFUND_AMOUNT = REFUND_AMOUNT;
	}

	public String getRET_CODE() {
		return RET_CODE;
	}

	public void setRET_CODE(String RET_CODE) {
		this.RET_CODE = RET_CODE;
	}

	public String getERR_MSG() {
		return ERR_MSG;
	}

	public void setERR_MSG(String ERR_MSG) {
		this.ERR_MSG = ERR_MSG;
	}
}