package com.jkm.util;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

@UtilityClass
public class ReadProperties {

    /**
     * account config
     *
     * @return
     */
    public static NotifierConfig getNotifierConfig() {
        return ConfigCache.getOrCreate(NotifierConfig.class);
    }

    @org.aeonbits.owner.Config.Sources("classpath:notifier.properties")
    public interface NotifierConfig extends org.aeonbits.owner.Config {
        /**
         * appid
         *
         * @return
         */
        @Key("appid")
        @DefaultValue("wx23d3b3d674076e2e")
        String appid();

        /**
         * secret
         *
         * @return
         */
        @Key("secret")
        @DefaultValue("2da8ace3ac93da9a34404c8ff9ada924")
        String secret();

        /**
         * hcpAppid
         *
         * @return
         */
        @Key("hcpAppid")
        @DefaultValue("1013")
        String hcpAppid();

        /**
         * hcpSecret   预留
         * @return
         */
        @Key("hcpSecret")
        @DefaultValue("")
        String hcpSecret();
    }
}
