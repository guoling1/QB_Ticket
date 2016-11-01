package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.RequestDetail100003;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY",propOrder={"transDetail"})
public class RequestBody100003 {
	
	@XmlElement(name="TRANS_DETAIL")
	private RequestDetail100003 transDetail;

	public RequestDetail100003 getTransDetail() {
		return transDetail;
	}

	public void setTransDetail(RequestDetail100003 transDetail) {
		this.transDetail = transDetail;
	}
}

