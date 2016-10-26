package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.RequestDetail200005;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * 快捷支付结果查询
 * @author wujh
 *
 */
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY", propOrder={"queryTrans"})
public class RequestBody200005 {
	/** 
	 * @Fields queryTrans : 返回明细列表信息
	 */
	@XmlElement(name="QUERY_TRANS")
	private RequestDetail200005 queryTrans;

	public RequestDetail200005 getQueryTrans() {
		return queryTrans;
	}

	public void setQueryTrans(RequestDetail200005 queryTrans) {
		this.queryTrans = queryTrans;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RequestBody200005 [queryTrans=");
		builder.append(queryTrans);
		builder.append("]");
		return builder.toString();
	}
	
}
