package com.jkm.service.ys.helper;


import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by yuxiang on 2016/10/27.
 */
@UtilityClass
public class YsSdkConstants {

    /**
     * 终端号
     */
    public static final String TERMINAL_ID;
    /**
     * 企业编号
     */
    public static final String FACTORY_ID;
    /**
     * 服务网关
     */
    public static final String SERVICE_GATEWAY_URL;

    /**
     * 订票回调url
     */
    public static final String BOOK_TICKET_NOTIFY_URL;

    /**
     * 退票回调url
     */
    public static final String REFUND_TICKET_NOTIFY_URL;

    /**
     * 签名密钥
     */
    public static final String SIGN_KEY;

    static {
        final YsSdkConfig ysSdkConfig = getYsSdkConfig();
        TERMINAL_ID = ysSdkConfig.terminalID();
        Preconditions.checkState(!Strings.isNullOrEmpty(TERMINAL_ID), "终端号");
        FACTORY_ID = ysSdkConfig.factoryId();
        Preconditions.checkState(!Strings.isNullOrEmpty(FACTORY_ID), "企业编号");
        SIGN_KEY = ysSdkConfig.signKey();
        Preconditions.checkState(!Strings.isNullOrEmpty(SIGN_KEY), "秘钥加载异常");
        BOOK_TICKET_NOTIFY_URL = ysSdkConfig.bookTicketNotifyUrl();
        REFUND_TICKET_NOTIFY_URL = ysSdkConfig.refundTicketNotifyUrl();
        SERVICE_GATEWAY_URL = ysSdkConfig.serviceGatewayUrl();
        Preconditions.checkState(!Strings.isNullOrEmpty(SERVICE_GATEWAY_URL), "服务网关加载异常");
    }

    /**
     * 获得银胜配置
     */
    public static YsSdkConfig getYsSdkConfig() {
        return ConfigCache.getOrCreate(YsSdkConfig.class);
    }
    /**
     * 银胜配置
     */
    @org.aeonbits.owner.Config.Sources("classpath:ys_sdk.properties")
    private interface YsSdkConfig extends org.aeonbits.owner.Config {

        /**
         * 终端号
         */
        @Key("third.channel.ys.terminal.id")
        @DefaultValue("")
        String terminalID();

        /**
         * 企业编号
         */
        @Key("third.channel.ys.factory.id")
        @DefaultValue("")
        String factoryId();

        /**
         * 签名秘钥
         */
        @Key("third.channel.ys.sign.key")
        @DefaultValue("")
        String signKey();

        /**
         * 订票回调url
         */
        @Key("third.channel.ys.book.ticket.notify.url")
        @DefaultValue("")
        String bookTicketNotifyUrl();

        /**
         * 退票回调url
         */
        @Key("third.channel.ys.refund.ticket.notify.url")
        @DefaultValue("")
        String refundTicketNotifyUrl();


        /**
         *网关服务地址
         */
        @Key("third.channel.ys.service.gateway.url")
        @DefaultValue("")
        String serviceGatewayUrl();
    }
}
