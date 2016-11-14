package com.jkm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class SendMessageCountRecord extends BaseEntity {

	/**用户编码**/
	private String uid;

	/**手机号**/
	private String mobile;

	/**消息模板id**/
	private Long messageTemplateId;

	/**发送短信返回码**/
	private Long sn;


}
