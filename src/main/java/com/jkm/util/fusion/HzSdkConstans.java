package com.jkm.util.fusion;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;


@UtilityClass
public class HzSdkConstans {
    /**
     * 商户号
     */
    public static final String MERC_ID;
    /**
     * 快捷支付地址
     */
    public static final String  FASTPAY_URL;
    /**
     * 快捷支付查询地址
     */
    public static final String  FASTPAY_QUERY_URL;
    /**
     * 单笔退款地址
     */
    public static final String  SINGLE_REFUND_URL;
    /**
     * 银行卡鉴权
     */
    public static final String  CARD_AUTH;


    static {
        final HzSdkConfig hzSdkConfig = getHzSdkConfig();
        MERC_ID = hzSdkConfig.mercId();
        Preconditions.checkState(!Strings.isNullOrEmpty(MERC_ID), "商户号");
        FASTPAY_URL = hzSdkConfig.fastPayUrl();
        Preconditions.checkState(!Strings.isNullOrEmpty(FASTPAY_URL), "快捷支付地址");
        FASTPAY_QUERY_URL = hzSdkConfig.fastPayQueryUrl();
        Preconditions.checkState(!Strings.isNullOrEmpty(FASTPAY_QUERY_URL), "快捷支付查询地址");
        SINGLE_REFUND_URL = hzSdkConfig.singleRefundUrl();
        Preconditions.checkState(!Strings.isNullOrEmpty(SINGLE_REFUND_URL), "单笔退款地址");
        CARD_AUTH = hzSdkConfig.cardAuth();
        Preconditions.checkState(!Strings.isNullOrEmpty(CARD_AUTH), "银行卡鉴权地址");
    }

    /**
     * 获得银胜配置
     */
    public static HzSdkConfig getHzSdkConfig() {
        return ConfigCache.getOrCreate(HzSdkConfig.class);
    }
    /**
     * 合众配置
     */
    @org.aeonbits.owner.Config.Sources("classpath:hz_sdk.properties")
    private interface HzSdkConfig extends org.aeonbits.owner.Config {

        /**
         * 商户号
         */
        @Key("merc_id")
        @DefaultValue("")
        String mercId();

        /**
         * 快捷支付地址
         */
        @Key("fastPay_url")
        @DefaultValue("")
        String fastPayUrl();

        /**
         * 快捷支付查询地址
         */
        @Key("fastPay_query_url")
        @DefaultValue("")
        String fastPayQueryUrl();

        /**
         * 单笔退款地址
         */
        @Key("single_refund_url")
        @DefaultValue("")
        String singleRefundUrl();


        /**
         * 银行卡鉴权
         */
        @Key("card_auth")
        @DefaultValue("")
        String cardAuth();
    }
}
