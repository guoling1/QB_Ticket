package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class PayResultRecord extends BaseEntity {

	/**支付渠道(fastpay|alipay|weixin)**/
	private String payChannel;

	/**流水号**/
	private String reqSn;

	/**支付金额**/
	private String amount;

	/**支付结果（1成功2失败3.处理中）**/
	private String payResult;

	/**支付参数**/
	private String payParams;

	/**支付结果参数**/
	private String resultParams;


}
