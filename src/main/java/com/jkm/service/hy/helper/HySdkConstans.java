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
     * 帐号
     */
    public static final String PARTNERID;
    /**
     * 服务网关
     */
    public static final String SERVICE_GATEWAY_URL;

    /**
     * 占座结果回调url
     */
    public static final String RESERVE_TICKET_NOTIFY_URL;

    /**
     * 确认出票回调url
     */
    public static final String  CONFIRM_TICKET_NOTIFY_URL;

    /**
     * 线上线下退票回调url
     */
    public static final String REFUND_TICKET_NOTIFY_URL;

    /**
     * 签名密钥
     */
    public static final String SIGN_KEY;

    static {
        final HySdkConfig hySdkConfig = getHySdkConfig();
        PARTNERID = hySdkConfig.partnerid();
        Preconditions.checkState(!Strings.isNullOrEmpty(PARTNERID), "帐号");
        RESERVE_TICKET_NOTIFY_URL = hySdkConfig.reserveTicketNotifyUrl();
        Preconditions.checkState(!Strings.isNullOrEmpty(RESERVE_TICKET_NOTIFY_URL), "占座回调");
        SIGN_KEY = hySdkConfig.signKey();
        Preconditions.checkState(!Strings.isNullOrEmpty(SIGN_KEY), "秘钥加载异常");
        CONFIRM_TICKET_NOTIFY_URL = hySdkConfig.confirmTicketNotifyUrl();
        REFUND_TICKET_NOTIFY_URL = hySdkConfig.refundTicketNotifyUrl();
        SERVICE_GATEWAY_URL = hySdkConfig.serviceGatewayUrl();
        Preconditions.checkState(!Strings.isNullOrEmpty(SERVICE_GATEWAY_URL), "服务网关加载异常");
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
         * 帐号
         */
        @Key("third.channel.hy.partnerid")
        @DefaultValue("")
        String partnerid();

        /**
         * 签名秘钥
         */
        @Key("third.channel.hy.sign.key")
        @DefaultValue("")
        String signKey();

        /**
         * 占座回调url
         */
        @Key("third.channel.hy.reserve.ticket.notify.url")
        @DefaultValue("")
        String reserveTicketNotifyUrl();

        /**
         * 退票回调url
         */
        @Key("third.channel.hy.refund.ticket.notify.url")
        @DefaultValue("")
        String refundTicketNotifyUrl();

        /**
         * 确认出票回调url
         */
        @Key("third.channel.hy.confirm.ticket.notify.url")
        @DefaultValue("")
        String confirmTicketNotifyUrl();

        /**
         *网关服务地址
         */
        @Key("third.channel.hy.service.gateway.url")
        @DefaultValue("")
        String serviceGatewayUrl();
    }
}