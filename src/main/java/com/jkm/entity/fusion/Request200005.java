package com.jkm.entity.fusion;

import com.jkm.entity.fusion.body.RequestBody200005;
import com.jkm.entity.fusion.head.RequestHead;
import com.jkm.entity.fusion.head.RequestHead20005;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG",propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Request200005 {
	@XmlElement(name="INFO")
	private RequestHead20005 info;
	@XmlElement(name="BODY")
	private RequestBody200005 body;

	public RequestHead20005 getInfo() {
		return info;
	}

	public void setInfo(RequestHead20005 info) {
		this.info = info;
	}

	public RequestBody200005 getBody() {
		return body;
	}

	public void setBody(RequestBody200005 body) {
		this.body = body;
	}
}

