package com.jkm.entity.fusion;


import com.jkm.entity.fusion.body.RequestBody200005;
import com.jkm.entity.fusion.head.RequestHead;

import javax.xml.bind.annotation.*;
/**
 * 查询请求
 * @author feng.xi
 *
 */
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "AIPG", propOrder={"info", "body"})
@XmlRootElement(name="AIPG")
public class Request200005 {
	// 报文头
		@XmlElement(name="INFO")
		private RequestHead info;
		// 请求报文体
		@XmlElement(name="BODY")
		private RequestBody200005 body;
		public RequestHead getInfo() {
			return info;
		}
		public void setInfo(RequestHead info) {
			this.info = info;
		}
		public RequestBody200005 getBody() {
			return body;
		}
		public void setBody(RequestBody200005 body) {
			this.body = body;
		}
}
