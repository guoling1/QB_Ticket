package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.RequestDetail100004;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY",propOrder={"transDetail"})
public class RequestBody100004 {
	
	@XmlElement(name="TRANS_DETAIL")
	private RequestDetail100004 transDetail;

	public RequestDetail100004 getTransDetail() {
		return transDetail;
	}

	public void setTransDetail(RequestDetail100004 transDetail) {
		this.transDetail = transDetail;
	}
}

