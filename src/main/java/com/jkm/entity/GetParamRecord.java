package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class GetParamRecord extends BaseEntity {

	/**appid**/
	private String appId;

	/**不长于50位**/
	private String nonceStr;


}
