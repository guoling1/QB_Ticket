package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.RequestDetail200005;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY",propOrder={"queryTrans"})
public class RequestBody200005 {
	
	@XmlElement(name="QUERY_TRANS")
	private RequestDetail200005 queryTrans;

	public RequestDetail200005 getQueryTrans() {
		return queryTrans;
	}

	public void setQueryTrans(RequestDetail200005 queryTrans) {
		this.queryTrans = queryTrans;
	}
}

