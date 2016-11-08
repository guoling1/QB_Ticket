package com.jkm.entity.fusion.detail;

import javax.xml.bind.annotation.*;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "TRANS_DETAIL",propOrder={"MERCHANT_ID","MERC_ORD_NO","MERC_ORD_DT"})
public class RequestDetail200005 {
	@XmlElement(name = "MERCHANT_ID")
	private String MERCHANT_ID;
	@XmlElement(name = "MERC_ORD_NO")
	private String MERC_ORD_NO;
	@XmlElement(name = "MERC_ORD_DT")
	private String MERC_ORD_DT;

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

	public String getMERC_ORD_DT() {
		return MERC_ORD_DT;
	}

	public void setMERC_ORD_DT(String MERC_ORD_DT) {
		this.MERC_ORD_DT = MERC_ORD_DT;
	}
}

