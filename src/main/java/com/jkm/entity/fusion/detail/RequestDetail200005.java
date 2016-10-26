package com.jkm.entity.fusion.detail;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)  
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "QUERY_TRANS", propOrder={"mercOrdNo", "merchantId","mercOrdDt"})
public class RequestDetail200005 {
	
	/** 
	 * @Fields mercOrdNo : 商户订单编号
	 */
	@XmlElement(name="MERC_ORD_NO")
	private String mercOrdNo;
	/** 
	 * @Fields merchantId :商户代码 
	 */
	@XmlElement(name="MERCHANT_ID")
	private String merchantId;
	/** 
	 * @Fields mercOrdDt : 商户订单日期
	 */
	@XmlElement(name="MERC_ORD_DT")
	private String mercOrdDt;
	
	
	public String getMercOrdNo() {
		return mercOrdNo;
	}
	public void setMercOrdNo(String mercOrdNo) {
		this.mercOrdNo = mercOrdNo;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMercOrdDt() {
		return mercOrdDt;
	}
	public void setMercOrdDt(String mercOrdDt) {
		this.mercOrdDt = mercOrdDt;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RequestDetail200005 [mercOrdNo=");
		builder.append(mercOrdNo);
		builder.append(", merchantId=");
		builder.append(merchantId);
		builder.append(", mercOrdDt=");
		builder.append(mercOrdDt);
		builder.append("]");
		return builder.toString();
	}
	
}
