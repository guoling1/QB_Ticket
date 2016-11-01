package com.jkm.entity.helper;

import org.aeonbits.owner.ConfigCache;

/**
 * Created by yuxiang on 2016-09-22.
 */
public class BankConstants {
    /**
     * bank config
     *
     * @return
     */
    public static BankConfig getConfig() {
        return ConfigCache.getOrCreate(BankConfig.class);
    }

    @org.aeonbits.owner.Config.Sources("classpath:bank.properties")
    public interface BankConfig extends org.aeonbits.owner.Config {
        /**
         * 账户签名key
         *
         * @return
         */
        @Key("tb.user.bank.card.key")
        @DefaultValue("OHJfeHhoXEs1PotTP0JX2BZy3z1TvjAE")
        String userBankCardKey();
    }
}
