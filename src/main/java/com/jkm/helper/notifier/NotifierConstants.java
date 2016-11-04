package com.jkm.helper.notifier;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by hutao on 16/3/3.
 * 下午2:20
 */
@UtilityClass
public class NotifierConstants {
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
         * 平台名称
         *
         * @return
         */
        @Key("platform.name")
        @DefaultValue("联想金融")
        String platformName();
    }
}
