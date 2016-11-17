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

	/**支付流水号**/
	private String reqSn;

	/**支付金额(分)**/
	private Long amount;

	/**支付参数**/
	private String payParams;

	/**返回参数**/
	private String resultParams;

	/**支付结果
            S-成功
       E-异常     U-处理中
            N-待支付
            F-失败
            **/
	private String payResult;



}
