package com.jkm.entity.fusion.detail;

import javax.xml.bind.annotation.*;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "RET_DETAIL", propOrder={"ISFEE"})
public class ResponseDetail100004 {
	@XmlElement(name="ISFEE")
	private String ISFEE;

	public String getISFEE() {
		return ISFEE;
	}

	public void setISFEE(String ISFEE) {
		this.ISFEE = ISFEE;
	}
}