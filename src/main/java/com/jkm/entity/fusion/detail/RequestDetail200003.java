package com.jkm.entity.fusion.detail;

import javax.xml.bind.annotation.*;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "TRANS_DETAIL",propOrder={"QUERY_SN","QUERY_DATE","MERCHANT_ID"})
public class RequestDetail200003 {
	@XmlElement(name = "QUERY_SN")
	private String QUERY_SN;
	@XmlElement(name = "QUERY_DATE")
	private String QUERY_DATE;
	@XmlElement(name = "MERCHANT_ID")
	private String MERCHANT_ID;

	public String getQUERY_SN() {
		return QUERY_SN;
	}

	public void setQUERY_SN(String QUERY_SN) {
		this.QUERY_SN = QUERY_SN;
	}

	public String getQUERY_DATE() {
		return QUERY_DATE;
	}

	public void setQUERY_DATE(String QUERY_DATE) {
		this.QUERY_DATE = QUERY_DATE;
	}

	public String getMERCHANT_ID() {
		return MERCHANT_ID;
	}

	public void setMERCHANT_ID(String MERCHANT_ID) {
		this.MERCHANT_ID = MERCHANT_ID;
	}
}

