package com.jkm.entity;

import com.jkm.entity.helper.UserBankCardSupporter;
import com.jkm.enums.EnumGrabTicketStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class BindCard extends BaseEntity{
	/**
	 * 三方用户id
	 */
	private String uid;

	/**
	 * 银行卡号
	 */
	private String cardNo;
	/**
	 * 卡bin
	 */
	private String bankCode;

	/**
	 * 银行卡姓名
	 */
	private String accountName;

	/**
	 * 00：身份证,1: 户口簿，2：护照,3.军官证,4.士兵证，
	 * 5. 港澳居民来往内地通行证,6. 台湾同胞来往内地通行证,
	 * 7. 临时身份证,8. 外国人居留证,9. 警官证, X.其他证件
	 */
	private String cardType;

	/**
	 * 身份证号
	 */
	private String cardId;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 支付密码
	 */
	private String payPwd;

	/**
	 * 解密后的银行卡
	 * @param cardNo
	 * @return
	 */
	public String cardNoOrg(String cardNo) {
		return UserBankCardSupporter.decryptCardNo(cardNo);
	}
	/**
	 * 解密后的身份证
	 * @param cardNo
	 * @return
	 */
	public String cardIdOrg(String cardId) {
		return UserBankCardSupporter.decryptCardId(cardId);
	}

}
