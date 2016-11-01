package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.ResponseDetail100004;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY", propOrder={"retDetail"})
public class ResponseBody100004 {
	@XmlElement(name="RET_DETAIL")
	private ResponseDetail100004 retDetail;

	public ResponseDetail100004 getRetDetail() {
		return retDetail;
	}

	public void setRetDetail(ResponseDetail100004 retDetail) {
		this.retDetail = retDetail;
	}
}

