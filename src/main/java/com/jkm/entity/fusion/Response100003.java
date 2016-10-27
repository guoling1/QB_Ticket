package com.jkm.entity.fusion;

import com.jkm.entity.fusion.body.ResponseBody100003;
import com.jkm.entity.fusion.head.ResponseHead;


import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG", propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Response100003 {
	// 响应报文头
		@XmlElement(name="INFO")
		private ResponseHead info;
		// 响应报文体信息
		@XmlElement(name="BODY")
		private ResponseBody100003 body;
		public ResponseHead getInfo() {
			return info;
		}
		public void setInfo(ResponseHead info) {
			this.info = info;
		}
		public ResponseBody100003 getBody() {
			return body;
		}
		public void setBody(ResponseBody100003 body) {
			this.body = body;
		}
}

