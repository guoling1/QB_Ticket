package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.RequestDetail100003;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY",propOrder={"singlerefundDetail"})
public class RequestBody100003 {
	
	@XmlElement(name="SINGLEREFUND_DETAIL")
	private RequestDetail100003 singlerefundDetail;

	public RequestDetail100003 getSinglerefundDetail() {
		return singlerefundDetail;
	}

	public void setSinglerefundDetail(RequestDetail100003 singlerefundDetail) {
		this.singlerefundDetail = singlerefundDetail;
	}
}

