package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 
 * 支付流水
 * 
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class PaySequence extends BaseEntity{

	/**fastpay|alipay|weixin**/
	private String payChannel;
	/**
	 * 商户订单号
	 */
	private String orderId;

	/**支付流水号**/
	private String reqSn;

	/**支付金额(分)**/
	private long amount;

	/**支付参数**/
	private String payParams;

	/**返回参数**/
	private String resultParams;

	/**
	 * 支付结果
	 * S-成功
	 * E-异常
	 * U-处理中
	 *T-连接失败（超时）
	 * N-待支付
	 * F-失败
	 */
	private String payResult;



}
