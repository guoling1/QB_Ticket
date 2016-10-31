package com.jkm.util.fusion;

/**
 * 系统使用常量
 * @author wujh
 *
 */
public interface Constants {
	
	// 通讯报文字符集
	public static final String transfer_charset = "GBK";
	
	// 加密相关的字符集
	public static final String encrypt_charset = "UTF-8";
	
	/** 数据格式 */
	public static String DATA_TYPE_XML = "0";
	
	/** 处理级别 */
	public static String LEVEL_0 = "0";
	
	/**
	 * 访问成功编码
	 */
	public static final String  RESPONSE_CODE_SUCCESS = "PWM00000";
	
	/**
	 * 访问失败编码
	 */
	public static final String  RESPONSE_CODE_FAIL = "F00001";

	/**
	 * 快捷支付地址
	 */
	public static final String  FASTPAY_URL = "http://103.25.21.35:11111/gateway/quick/fastPay";
	/**
	 * 快捷支付查询地址
	 */
	public static final String  FASTPAY_QUERY_URL = "http://103.25.21.35:11111/gateway/query/queryQuickPay";
	/**
	 * 单笔退款地址
	 */
	public static final String  SINGLE_REFUND_URL = "http://103.25.21.35:11111/gateway/refund/singleRefund";
	/**
	 * 银行卡鉴权
	 */
	public static final String  CARD_AUTH = "http://103.25.21.35:11111/gateway/auth/cardAuth";
	/**
	 * 商户号
	 */
	public static final String  MERC_ID = "800010000020019";
}
