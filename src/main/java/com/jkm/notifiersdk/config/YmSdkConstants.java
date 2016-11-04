package com.jkm.notifiersdk.config;

import org.aeonbits.owner.ConfigCache;

/**
 * Created on 16/7/22.
 *
 * @author hutao
 * @version 1.0
 */
public class YmSdkConstants {
    public static final String SEND_TIME_SMS_URL = "http://hprpt2.eucp.b2m.cn:8080/sdkproxy/sendtimesms.action";

    /**
     * hfb sdk config
     *
     * @return
     */
    public static YmSdkConfig getYmSdkConfig() {
        return ConfigCache.getOrCreate(YmSdkConfig.class);
    }

    /**
     *
     */
    @org.aeonbits.owner.Config.Sources("classpath:sdk/ym_sdk.properties")
    public interface YmSdkConfig extends org.aeonbits.owner.Config {
        /**
         * 用户序列号
         *
         * @return
         */
        @Key("ym.sdk.sms.cd.key")
        @DefaultValue("8SDK-EMY-6699-RISSR")
        String cdKey();

        /**
         * 用户密码
         *
         * @return
         */
        @Key("ym.sdk.sms.password")
        @DefaultValue("483118")
        String password();

        /**
         * 企业名称(最多60字节)
         *
         * @return
         */
        @Key("ym.sdk.sms.company.ename")
        @DefaultValue("北京金开门信息技术有限公司")
        String ename();


        /**
         * 联系人姓名(最多20字节)
         *
         * @return
         */
        @Key("ym.sdk.sms.company.linkman")
        @DefaultValue("陈杰智")
        String linkman();

        /**
         * 联系电话(最多20字节)
         *
         * @return
         */
        @Key("ym.sdk.sms.company.phonenum")
        @DefaultValue("13096313005")
        String phonenum();

        /**
         * 联系手机(最多15字节)
         *
         * @return
         */
        @Key("ym.sdk.sms.company.mobile")
        @DefaultValue("13096313005")
        String mobile();

        /**
         * 电子邮件(最多60字节)
         *
         * @return
         */
        @Key("ym.sdk.sms.company.email")
        @DefaultValue("jiezhichen@jinkaimen.com")
        String email();

        /**
         * 联系传真(最多20字节)
         *
         * @return
         */
        @Key("ym.sdk.sms.company.fax")
        @DefaultValue("12213")
        String fax();

        /**
         * 公司地址(最多60字节)
         *
         * @return
         */
        @Key("ym.sdk.sms.company.address")
        @DefaultValue("北京市")
        String address();

        /**
         * 邮政编码(最多6字节)
         *
         * @return
         */
        @Key("ym.sdk.sms.company.postcode")
        @DefaultValue("100000")
        String postcode();


    }
}
