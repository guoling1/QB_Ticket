package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.RequestDetail100005;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY",propOrder={"transDetail"})
public class RequestBody100005 {
	
	@XmlElement(name="TRANS_DETAIL")
	private RequestDetail100005 transDetail;

	public RequestDetail100005 getTransDetail() {
		return transDetail;
	}

	public void setTransDetail(RequestDetail100005 transDetail) {
		this.transDetail = transDetail;
	}
	
	
}

