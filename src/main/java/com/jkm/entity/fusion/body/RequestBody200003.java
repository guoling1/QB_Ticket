package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.RequestDetail200003;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY",propOrder={"transDetail"})
public class RequestBody200003 {
	
	@XmlElement(name="TRANS_DETAIL")
	private RequestDetail200003 transDetail;

	public RequestDetail200003 getTransDetail() {
		return transDetail;
	}

	public void setTransDetail(RequestDetail200003 transDetail) {
		this.transDetail = transDetail;
	}
}

