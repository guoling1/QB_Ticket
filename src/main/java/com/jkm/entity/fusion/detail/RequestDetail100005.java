package com.jkm.entity.fusion.detail;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "TRANS_DETAIL",propOrder={"CRD_TYP","BUSINESS_CODE","MERCHANT_ID","SEND_TIME","SEND_DT","RET_URL","NOTIFY_URL",
		"CARD_NO","ACCOUNT_NAME","ID_TYPE","ID","TEL","CRE_VAL_DATE","CRE_CVN2","TEL_CAPTCHA","STEP_NO","TOKEN","AMOUNT"})
public class RequestDetail100005 {
	@XmlElement(name = "CRD_TYP")
	private String CRD_TYP;
	@XmlElement(name = "BUSINESS_CODE")
	private String BUSINESS_CODE;
	@XmlElement(name = "MERCHANT_ID")
	private String MERCHANT_ID;
	@XmlElement(name = "SEND_TIME")
	private String SEND_TIME;
	@XmlElement(name = "SEND_DT")
	private String SEND_DT;
	@XmlElement(name = "RET_URL")
	private String RET_URL;
	@XmlElement(name = "NOTIFY_URL")
	private String NOTIFY_URL;
	@XmlElement(name = "CARD_NO")
	private String CARD_NO;
	@XmlElement(name = "ACCOUNT_NAME")
	private String ACCOUNT_NAME;
	@XmlElement(name = "ID_TYPE")
	private String ID_TYPE;
	@XmlElement(name = "ID")
	private String ID;
	@XmlElement(name = "TEL")
	private String TEL;
	@XmlElement(name = "CRE_VAL_DATE")
	private String CRE_VAL_DATE;
	@XmlElement(name = "CRE_CVN2")
	private String CRE_CVN2;
	@XmlElement(name = "TEL_CAPTCHA")
	private String TEL_CAPTCHA;
	@XmlElement(name = "STEP_NO")
	private String STEP_NO;
	@XmlElement(name = "TOKEN")
	private String TOKEN;
	@XmlElement(name = "AMOUNT")
	private String AMOUNT;
	public String getAMOUNT() {
		return AMOUNT;
	}
    
	public void setAMOUNT(String aMOUNT) {
		AMOUNT = aMOUNT;
	}

	public String getBUSINESS_CODE() {
		return BUSINESS_CODE;
	}

	public void setBUSINESS_CODE(String bUSINESS_CODE) {
		BUSINESS_CODE = bUSINESS_CODE;
	}

	public String getMERCHANT_ID() {
		return MERCHANT_ID;
	}

	public void setMERCHANT_ID(String mERCHANT_ID) {
		MERCHANT_ID = mERCHANT_ID;
	}

	public String getSEND_TIME() {
		return SEND_TIME;
	}

	public void setSEND_TIME(String sEND_TIME) {
		SEND_TIME = sEND_TIME;
	}

	public String getSEND_DT() {
		return SEND_DT;
	}

	public void setSEND_DT(String sEND_DT) {
		SEND_DT = sEND_DT;
	}

	public String getRET_URL() {
		return RET_URL;
	}

	public void setRET_URL(String rET_URL) {
		RET_URL = rET_URL;
	}

	public String getNOTIFY_URL() {
		return NOTIFY_URL;
	}

	public void setNOTIFY_URL(String nOTIFY_URL) {
		NOTIFY_URL = nOTIFY_URL;
	}

	public String getCARD_NO() {
		return CARD_NO;
	}

	public void setCARD_NO(String cARD_NO) {
		CARD_NO = cARD_NO;
	}

	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}

	public void setACCOUNT_NAME(String aCCOUNT_NAME) {
		ACCOUNT_NAME = aCCOUNT_NAME;
	}

	public String getID_TYPE() {
		return ID_TYPE;
	}

	public void setID_TYPE(String iD_TYPE) {
		ID_TYPE = iD_TYPE;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTEL() {
		return TEL;
	}

	public void setTEL(String tEL) {
		TEL = tEL;
	}

	public String getCRE_VAL_DATE() {
		return CRE_VAL_DATE;
	}

	public void setCRE_VAL_DATE(String cRE_VAL_DATE) {
		CRE_VAL_DATE = cRE_VAL_DATE;
	}

	public String getCRE_CVN2() {
		return CRE_CVN2;
	}

	public void setCRE_CVN2(String cRE_CVN2) {
		CRE_CVN2 = cRE_CVN2;
	}

	public String getTEL_CAPTCHA() {
		return TEL_CAPTCHA;
	}

	public void setTEL_CAPTCHA(String tEL_CAPTCHA) {
		TEL_CAPTCHA = tEL_CAPTCHA;
	}

	public String getSTEP_NO() {
		return STEP_NO;
	}

	public void setSTEP_NO(String sTEP_NO) {
		STEP_NO = sTEP_NO;
	}

	public String getTOKEN() {
		return TOKEN;
	}

	public void setTOKEN(String tOKEN) {
		TOKEN = tOKEN;
	}

	public String getCRD_TYP() {
		return CRD_TYP;
	}

	public void setCRD_TYP(String cRD_TYP) {
		CRD_TYP = cRD_TYP;
	}
	

}

