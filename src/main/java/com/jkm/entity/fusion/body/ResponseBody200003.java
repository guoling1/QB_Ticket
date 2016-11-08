package com.jkm.entity.fusion.body;

import com.jkm.entity.fusion.detail.ResponseDetail100003;
import com.jkm.entity.fusion.detail.ResponseDetail200003;

import javax.xml.bind.annotation.*;


@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "BODY", propOrder={"transDetail"})
public class ResponseBody200003 {
	@XmlElement(name="TRANS_DETAIL")
	private ResponseDetail200003 transDetail;

	public ResponseDetail200003 getTransDetail() {
		return transDetail;
	}

	public void setTransDetail(ResponseDetail200003 transDetail) {
		this.transDetail = transDetail;
	}
}

