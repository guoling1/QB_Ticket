package com.jkm.entity.fusion;

import com.jkm.entity.fusion.body.ResponseBody100003;
import com.jkm.entity.fusion.body.ResponseBody200003;
import com.jkm.entity.fusion.head.ResponseHead;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG", propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Response200003 {
	// 响应报文头
	@XmlElement(name="INFO")
	private ResponseHead info;
	// 响应报文体信息
	@XmlElement(name="BODY")
	private ResponseBody200003 body;

	public ResponseHead getInfo() {
		return info;
	}

	public void setInfo(ResponseHead info) {
		this.info = info;
	}

	public ResponseBody200003 getBody() {
		return body;
	}

	public void setBody(ResponseBody200003 body) {
		this.body = body;
	}
}

