package com.jkm.entity.fusion.detail;

import javax.xml.bind.annotation.*;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "RET_DETAIL", propOrder={"PAYIN_ORD_NO", "MERCHANT_ID", "MERC_ORD_NO"
		, "ORD_DT", "ORD_EXP_DT", "BUSI_TYPE", "APL_AMT", "CCY", "USE", "ORDER_STATUS", "MSG"})
public class ResponseDetail200005 {
	@XmlElement(name="PAYIN_ORD_NO")
	private String PAYIN_ORD_NO;
	@XmlElement(name="MERCHANT_ID")
	private String MERCHANT_ID;
	@XmlElement(name="MERC_ORD_NO")
	private String MERC_ORD_NO;
	@XmlElement(name="ORD_DT")
	private String ORD_DT;
	@XmlElement(name="ORD_EXP_DT")
	private String ORD_EXP_DT;
	@XmlElement(name="BUSI_TYPE")
	private String BUSI_TYPE;
	@XmlElement(name="APL_AMT")
	private String APL_AMT;
	@XmlElement(name="CCY")
	private String CCY;
	@XmlElement(name="USE")
	private String USE;
	@XmlElement(name="ORDER_STATUS")
	private String ORDER_STATUS;
	@XmlElement(name="MSG")
	private String MSG;

	public String getPAYIN_ORD_NO() {
		return PAYIN_ORD_NO;
	}

	public void setPAYIN_ORD_NO(String PAYIN_ORD_NO) {
		this.PAYIN_ORD_NO = PAYIN_ORD_NO;
	}

	public String getMERCHANT_ID() {
		return MERCHANT_ID;
	}

	public void setMERCHANT_ID(String MERCHANT_ID) {
		this.MERCHANT_ID = MERCHANT_ID;
	}

	public String getMERC_ORD_NO() {
		return MERC_ORD_NO;
	}

	public void setMERC_ORD_NO(String MERC_ORD_NO) {
		this.MERC_ORD_NO = MERC_ORD_NO;
	}

	public String getORD_DT() {
		return ORD_DT;
	}

	public void setORD_DT(String ORD_DT) {
		this.ORD_DT = ORD_DT;
	}

	public String getORD_EXP_DT() {
		return ORD_EXP_DT;
	}

	public void setORD_EXP_DT(String ORD_EXP_DT) {
		this.ORD_EXP_DT = ORD_EXP_DT;
	}

	public String getBUSI_TYPE() {
		return BUSI_TYPE;
	}

	public void setBUSI_TYPE(String BUSI_TYPE) {
		this.BUSI_TYPE = BUSI_TYPE;
	}

	public String getAPL_AMT() {
		return APL_AMT;
	}

	public void setAPL_AMT(String APL_AMT) {
		this.APL_AMT = APL_AMT;
	}

	public String getCCY() {
		return CCY;
	}

	public void setCCY(String CCY) {
		this.CCY = CCY;
	}

	public String getUSE() {
		return USE;
	}

	public void setUSE(String USE) {
		this.USE = USE;
	}

	public String getORDER_STATUS() {
		return ORDER_STATUS;
	}

	public void setORDER_STATUS(String ORDER_STATUS) {
		this.ORDER_STATUS = ORDER_STATUS;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String MSG) {
		this.MSG = MSG;
	}
}