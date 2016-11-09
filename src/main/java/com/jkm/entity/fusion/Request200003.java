package com.jkm.entity.fusion;

import com.jkm.entity.fusion.body.RequestBody100003;
import com.jkm.entity.fusion.body.RequestBody200003;
import com.jkm.entity.fusion.head.RequestHead;
import com.jkm.entity.fusion.head.RequestHead20003;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG",propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Request200003 {
	@XmlElement(name="INFO")
	private RequestHead20003 info;
	@XmlElement(name="BODY")
	private RequestBody200003 body;

	public RequestHead20003 getInfo() {
		return info;
	}

	public void setInfo(RequestHead20003 info) {
		this.info = info;
	}

	public RequestBody200003 getBody() {
		return body;
	}

	public void setBody(RequestBody200003 body) {
		this.body = body;
	}
}

