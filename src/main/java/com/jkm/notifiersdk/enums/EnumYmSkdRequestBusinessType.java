package com.jkm.notifiersdk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created on 16/7/22.
 *
 * @author hutao
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum EnumYmSkdRequestBusinessType {
    REGISTER_COMPANY(1, "http://hprpt2.eucp.b2m.cn:8080/sdkproxy/registdetailinfo.action", "注册企业信息"),
    REGISTER_SN(2, "http://hprpt2.eucp.b2m.cn:8080/sdkproxy/regist.action", "注册序列号"),
    UN_REGISTER_SN(3, "http://hprpt2.eucp.b2m.cn:8080/sdkproxy/logout.action", "注销序列号"),
    SEND_INSTANT_SMS(4, "http://hprpt2.eucp.b2m.cn:8080/sdkproxy/sendsms.action", "发送即时短信"),
    SEND_TIMED_SMS(5, "http://hprpt2.eucp.b2m.cn:8080/sdkproxy/sendtimesms.action", "发送定时短信");
    private final int id;
    private final String url;
    private final String msg;
}
