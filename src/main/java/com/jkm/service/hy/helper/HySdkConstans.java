package com.jkm.service.hy.helper;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by yuxiang on 2016-10-31.
 */
@UtilityClass
public class HySdkConstans {

    /**
     * 查询的帐号
     */
    public static final String QUERY_PARTNER_ID;

    /**
     * 查询的签名密钥
     */
    public static final String QUERY_SIGN_KEY;

    /**
     * 订单的帐号
     */
    public static final String ORDER_PARTNER_ID;

    /**
     * 订单的签名密钥
     */
    public static final String ORDER_SIGN_KEY;


    /**
     *用户的账号
     */
    public static final String USERNAME;
    /**
     * 签名密钥
     */
    public static final String MAN;
    /**
     * 签名密钥
     */
    public static final String CHILD;
    /**
     * 签名密钥
     */
    public static final String PERSON;
    /**
     * 保险签名密钥
     */
    public static final String POLICY_SIGN_KEY;
    /**
     * 服务网关
     */
    public static final String SERVICE_GATEWAY_URL;

    /**
     * 订单提交回调url
     */
    public static final String  SUBMIT_TICKET_NOTIFY_URL;

    /**
     * 确认出票回调url
     */
    public static final String  CONFIRM_TICKET_NOTIFY_URL;

    /**
     * 线上线下退票回调url
     */
    public static final String REFUND_TICKET_NOTIFY_URL;

    /**
     * 12306账号验证
     */
    public static final String ACCOUNT_VALIDATE_URL;

    /**
     * 查询12306常用联系人
     */
    public static final String ACCOUNT_CONTACT_QUERY;

    /**
     * 火车票保险网关服务地址
     */
    public static final String POLICY_GATEWAY_URL;



    static {
        final HySdkConfig hySdkConfig = getHySdkConfig();
        QUERY_PARTNER_ID = hySdkConfig.queryPartnerId();
        Preconditions.checkState(!Strings.isNullOrEmpty(QUERY_PARTNER_ID), "查询帐号");
        QUERY_SIGN_KEY = hySdkConfig.querySignKey();
        Preconditions.checkState(!Strings.isNullOrEmpty(QUERY_SIGN_KEY), "查询秘钥加载异常");
        ORDER_PARTNER_ID = hySdkConfig.orderPartnerId();
        Preconditions.checkState(!Strings.isNullOrEmpty(ORDER_PARTNER_ID), "订单帐号");
        ORDER_SIGN_KEY = hySdkConfig.orderSignKey();
        Preconditions.checkState(!Strings.isNullOrEmpty(ORDER_SIGN_KEY), "订单秘钥加载异常");
        USERNAME = hySdkConfig.username();
        Preconditions.checkState(!Strings.isNullOrEmpty(USERNAME), "用户的帐号");
        SUBMIT_TICKET_NOTIFY_URL = hySdkConfig.submitTicketNotifyUrl();
        CONFIRM_TICKET_NOTIFY_URL = hySdkConfig.confirmTicketNotifyUrl();
        REFUND_TICKET_NOTIFY_URL = hySdkConfig.refundTicketNotifyUrl();
        SERVICE_GATEWAY_URL = hySdkConfig.serviceGatewayUrl();
        Preconditions.checkState(!Strings.isNullOrEmpty(SERVICE_GATEWAY_URL), "服务网关加载异常");
        ACCOUNT_VALIDATE_URL = hySdkConfig.accountValidateUrl();
        Preconditions.checkState(!Strings.isNullOrEmpty(ACCOUNT_VALIDATE_URL), "12306账号验证异常");
        ACCOUNT_CONTACT_QUERY = hySdkConfig.accountContactQuery();
        Preconditions.checkState(!Strings.isNullOrEmpty(ACCOUNT_CONTACT_QUERY), "12306常用联系人异常");
        MAN = hySdkConfig.man();
        CHILD = hySdkConfig.child();
        PERSON = hySdkConfig.person();
        POLICY_GATEWAY_URL = hySdkConfig.policyGatewayUrl();
        POLICY_SIGN_KEY = hySdkConfig.policySignKey();
    }

    /**
     * 获得银胜配置
     */
    public static HySdkConfig getHySdkConfig() {
        return ConfigCache.getOrCreate(HySdkConfig.class);
    }
    /**
     * 银胜配置
     */
    @org.aeonbits.owner.Config.Sources("classpath:hy_sdk.properties")
    private interface HySdkConfig extends org.aeonbits.owner.Config {

        /**
         * 查询帐号
         */
        @Key("third.channel.hy.query.partner.id")
        @DefaultValue("")
        String queryPartnerId();

        /**
         * 用户帐号
         */
        @Key("third.channel.hy.username")
        @DefaultValue("")
        String username();

        /**
         * 保险产品代码(20元 成人)
         */
        @Key("third.channel.hy.policy.man")
        @DefaultValue("")
        String man();

        /**
         * 保险产品代码(20元 儿童)
         */
        @Key("third.channel.hy.policy.child")
        @DefaultValue("")
        String child();

        /**
         * 保险产品代码(30元)
         */
        @Key("third.channel.hy.policy.person")
        @DefaultValue("")
        String person();

        /**
         * 查询签名秘钥
         */
        @Key("third.channel.hy.query.sign.key")
        @DefaultValue("")
        String querySignKey();

        /**
         * 订单帐号
         */
        @Key("third.channel.hy.order.partner.id")
        @DefaultValue("")
        String orderPartnerId();

        /**
         * 订单签名秘钥
         */
        @Key("third.channel.hy.order.sign.key")
        @DefaultValue("")
        String orderSignKey();

        /**
         * 保险签名秘钥
         */
        @Key("third.channel.hy.policy.sign.key")
        @DefaultValue("")
        String policySignKey();

        /**
         * 占座回调url
         */
        @Key("third.channel.hy.submit.ticket.notify.url")
        @DefaultValue("")
        String submitTicketNotifyUrl();

        /**
         * 确认出票回调url
         */
        @Key("third.channel.hy.confirm.ticket.notify.url")
        @DefaultValue("")
        String confirmTicketNotifyUrl();


        /**
         * 退票回调url
         */
        @Key("third.channel.hy.refund.ticket.notify.url")
        @DefaultValue("")
        String refundTicketNotifyUrl();

        /**
         *网关服务地址
         */
        @Key("third.channel.hy.service.gateway.url")
        @DefaultValue("")
        String serviceGatewayUrl();

        /**
         *12306账号验证
         */
        @Key("third.channel.hy.account.validate.url")
        @DefaultValue("")
        String accountValidateUrl();

        /**
         *查询12306常用联系人
         */
        @Key("third.channel.hy.account.contact.query")
        @DefaultValue("")
        String accountContactQuery();

        /**
         *火车票保险网关服务地址
         */
        @Key("third.channel.hy.policy.gateway.url")
        @DefaultValue("")
        String policyGatewayUrl();
    }
}
