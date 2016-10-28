package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.ResponseDetail100003;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY", propOrder={"retDetail"})
public class ResponseBody100003 {
	@XmlElement(name="RET_DETAIL")
	private ResponseDetail100003 retDetail;

	public ResponseDetail100003 getRetDetail() {
		return retDetail;
	}

	public void setRetDetail(ResponseDetail100003 retDetail) {
		this.retDetail = retDetail;
	}
}

