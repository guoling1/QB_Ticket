package com.jkm.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class RefundExceptionRecord extends BaseEntity{

	/**
	 *商户订单id
	 */
	private String orderId;

	/**
	 *fastpay|alipay|weixin
	 */
	private String payChannel;

	/**
	 *退款流水号
	 */
	private String refundSn;

	/**
	 * E异常 U已受理
	 */
	private String type;

}
