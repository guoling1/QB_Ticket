package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RefundResultRecord extends BaseEntity {

	/**退款渠道(fastpay|alipay|weixin)**/
	private String refundChannel;

	/**退款流水号**/
	private String reqSn;

	/**退款金额**/
	private String amount;

	/**退款结果(1成功 2失败)**/
	private String refundResult;

	/**退款参数**/
	private String refundParams;

	/**退款结果参数**/
	private String resultParams;


}
