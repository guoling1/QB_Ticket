package com.jkm.entity.fusion;

import com.jkm.entity.fusion.body.RequestBody100005;
import com.jkm.entity.fusion.head.RequestHead;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG",propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Request100005 {
		@XmlElement(name="INFO")
		private RequestHead info;
		@XmlElement(name="BODY")
		private RequestBody100005 body;

		public RequestHead getInfo() {
			return info;
		}

		public void setInfo(RequestHead info) {
			this.info = info;
		}

		public RequestBody100005 getBody() {
			return body;
		}

		public void setBody(RequestBody100005 body) {
			this.body = body;
		}

}

