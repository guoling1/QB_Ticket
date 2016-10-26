package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.ResponseDetail200005;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY", propOrder={"retDetail"})
public class ResponseBody200005 {
	@XmlElement(name="RET_DETAIL")
	private ResponseDetail200005 retDetail;

	public ResponseDetail200005 getRetDetail() {
		return retDetail;
	}

	public void setRetDetail(ResponseDetail200005 retDetail) {
		this.retDetail = retDetail;
	}
}
