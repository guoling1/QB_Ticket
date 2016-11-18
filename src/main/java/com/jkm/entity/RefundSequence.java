package com.jkm.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 
 * 退款流水
 * 
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class RefundSequence extends BaseEntity{

	/**fastPay|alipay|weixin**/
	private String payChannel;
	/**
	 * 商户订单号
	 */
	private String orderId;

	/**退款流水号**/
	private String refundSn;

	/**支付流水号**/
	private String reqSn;

	/**退款金额**/
	private long amount;

	/**退款参数**/
	private String refundParams;

	/**返回参数**/
	private String resultParams;

	/**
	 * 退款结果
	 * S-成功
	 * E-异常
	 * U-已受理
	 * T-连接失败（超时）
	 * F-失败
	 */
	private String refundResult;

}
