package com.jkm.helper;

import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

/**
 * Created by yulong.zhang on 2016/11/3.
 *
 * 保险策略工具
 */
@UtilityClass
public class InsurancePolicyUtil {


    public static final boolean isOpenPolicy;

    static {
        final Insurance insuranceConfig = getInsuranceConfig();
        isOpenPolicy = insuranceConfig.isOpenPolicy();
    }


    public static Insurance getInsuranceConfig() {
        return ConfigCache.getOrCreate(Insurance.class);
    }

    @org.aeonbits.owner.Config.Sources("classpath:insurance.properties")
    private interface Insurance extends org.aeonbits.owner.Config {

        @Key("is_open_policy")
        @DefaultValue("false")
        boolean isOpenPolicy();
    }
}
