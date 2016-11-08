package com.jkm.entity.fusion;

import com.jkm.entity.fusion.body.ResponseBody200005;
import com.jkm.entity.fusion.head.ResponseHead;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG", propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Response200005 {
	// 响应报文头
	@XmlElement(name="INFO")
	private ResponseHead info;
	// 响应报文体信息
	@XmlElement(name="BODY")
	private ResponseBody200005 body;

	public ResponseHead getInfo() {
		return info;
	}

	public void setInfo(ResponseHead info) {
		this.info = info;
	}

	public ResponseBody200005 getBody() {
		return body;
	}

	public void setBody(ResponseBody200005 body) {
		this.body = body;
	}
}

