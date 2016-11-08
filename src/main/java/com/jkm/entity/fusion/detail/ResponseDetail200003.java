package com.jkm.entity.fusion.detail;

import javax.xml.bind.annotation.*;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "TRANS_DETAIL", propOrder={"SN", "AMOUNT", "ACCOUNT_NAME"
		, "CUST_USERID", "ORD_STS", "REMARK", "RET_CODE", "ERR_MSG"})
public class ResponseDetail200003 {
	@XmlElement(name="SN")
	private String SN;
	@XmlElement(name="AMOUNT")
	private String AMOUNT;
	@XmlElement(name="ACCOUNT_NAME")
	private String ACCOUNT_NAME;
	@XmlElement(name="CUST_USERID")
	private String CUST_USERID;
	@XmlElement(name="ORD_STS")
	private String ORD_STS;
	@XmlElement(name="REMARK")
	private String REMARK;
	@XmlElement(name="RET_CODE")
	private String RET_CODE;
	@XmlElement(name="ERR_MSG")
	private String ERR_MSG;

	public String getSN() {
		return SN;
	}

	public void setSN(String SN) {
		this.SN = SN;
	}

	public String getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(String AMOUNT) {
		this.AMOUNT = AMOUNT;
	}

	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}

	public void setACCOUNT_NAME(String ACCOUNT_NAME) {
		this.ACCOUNT_NAME = ACCOUNT_NAME;
	}

	public String getCUST_USERID() {
		return CUST_USERID;
	}

	public void setCUST_USERID(String CUST_USERID) {
		this.CUST_USERID = CUST_USERID;
	}

	public String getORD_STS() {
		return ORD_STS;
	}

	public void setORD_STS(String ORD_STS) {
		this.ORD_STS = ORD_STS;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
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