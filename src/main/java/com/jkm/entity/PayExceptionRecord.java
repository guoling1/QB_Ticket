package com.jkm.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class PayExceptionRecord extends BaseEntity{

	/**
	 * 商户订单id
	 */
	private String orderId;
	/**
	 * fastpay|alipay|weixin
	 */
	private String payChannel;
	/**
	 * 支付流水号
	 */
	private String reqSn;

	/**
	* E异常 U处理中
	*/
	private String type;

}
