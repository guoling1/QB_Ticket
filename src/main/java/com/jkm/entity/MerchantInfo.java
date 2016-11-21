package com.jkm.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 
 * 商户信息
 * 
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class MerchantInfo extends BaseEntity{

	/**
	*公司名称
	*/
	private String companyName;

	/**
	*注册地址
	*/
	private String regAddress;

	/**
	*营业执照
	*/
	private String businessLicensePic;

	/**
	*组织机构代码
	*/
	private String orgCertPic;

	/**
	*负责人姓名
	*/
	private String inChargeName;

	/**
	*负责人邮箱
	*/
	private String inChargetEmail;

	/**
	*负责人qq
	*/
	private String inChargetQq;

	/**
	*负责人电话
	*/
	private String inChargetPhone;

	/**
	*类型
	*/
	private String type;


}
