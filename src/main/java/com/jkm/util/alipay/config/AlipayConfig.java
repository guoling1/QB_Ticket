package com.jkm.util.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String partner = "2088421947044115";

	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = partner;

	//商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL75DDlpaPyg0PoK\n" +
			"2wFpsLozetb5WYkos/7eKG7tmdPm6CaINlS8YuOpuLUg/IgBis6z+zt7dmBlCYiI\n" +
			"xy7B75zz182CZ9m1jTfOxMnysgr4l29pf4+sO8akAQV0db1ZFJ8n1W7jFCSK7vrQ\n" +
			"4vo8b3JOOr93RUjNa978jfXHD78pAgMBAAECgYEAmm3CWNrIryFMXCZTGz1gT/I5\n" +
			"L4XLxYJIq5zyjWFNt3FN4XrclIESZo9kcfQOwdydZrFcQ6l+/Ew3/VS93S2umJMP\n" +
			"WaVJm5oRWYrkX3FsEUZytDR3B9ExeKTvS1e1mjvLaLLVSu9WPtyuTBmX9H1Q5ary\n" +
			"jbQIFV5U2jp/lva1ZAECQQDnVETvt0rfFX7wJdLjU2xKZnqSZSh0Jk8oXZz6+LJh\n" +
			"gAcCype51/HuAg2JhxAaUo/1gl7dMQHjmTpfcfJaL6cJAkEA01b5Gj0ViNOQAIC7\n" +
			"3g/KoFZZ9RhLtAKjVS/50jExmtNtglID1LvAmQk/3McG4Om5TpMywdD9hBSlHoVU\n" +
			"6lA/IQJAXmgbwiDxo11ocqVdDX0sQqjT+Q7jlZNU1sptmFcKpZPUSKVZinTAvTuF\n" +
			"Ps0qGzP6oF1WTbsEZFhSZEVifL/Z0QJARgTNuSy9wrxdPF1qYNMYjfCuEy7+feBV\n" +
			"3vZrLYGQ7rvXz1J+5G9kCw/ucaYbGE83Ttoj+eTU6XqxjiAclxkJIQJAFLj8Mezs\n" +
			"OrrQuw/N1CEfBg46c+b9xOHdpnBgFXVbqxFkw2AwBWsUzWjJwoT//lwmiI+yh6uP\n" +
			"LgGZNDmLhLnPMA==";

	// 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String alipay_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://127.0.0.1/alipay/notifyUrl";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://127.0.0.1/alipay/returnUrl";

	// 签名方式
	public static String sign_type = "RSA";

	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

	// 支付类型 ，无需修改
	public static String payment_type = "1";

	// 调用的接口名，无需修改
	public static String service = "create_direct_pay_by_user";
//	public static String service = "alipay.wap.create.direct.pay.by.user";

//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

//↓↓↓↓↓↓↓↓↓↓ 请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 防钓鱼时间戳  若要使用请调用类文件submit中的query_timestamp函数
	public static String anti_phishing_key = "";

	// 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
	public static String exter_invoke_ip = "";

//↑↑↑↑↑↑↑↑↑↑请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
}

