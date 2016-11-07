package com.jkm.entity.fusion;

import com.jkm.entity.fusion.body.RequestBody100005;
import com.jkm.entity.fusion.head.RequestHead;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG",propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Request200005 {
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

