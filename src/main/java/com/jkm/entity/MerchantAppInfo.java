package com.jkm.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 
 * 商户应用
 * 
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantAppInfo extends BaseEntity{


	/**
	 * 商户编码
	 */
	private Long merchantId;

	/**
	*OPENID
	*/
	private String openId;

	/**
	*SECRETKEY
	*/
	private String secretKey;

	/**
	*应用名称
	*/
	private String appName;

	/**
	 * 通知地址
	 */
	private String callBackUrl;

	/**
	*排序
	*/
	private Integer sn;

	/**
	*应用类型 wap|web|app
	*/
	private String type;

}
