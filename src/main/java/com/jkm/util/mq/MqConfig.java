
package com.jkm.util.mq;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.aeonbits.owner.ConfigCache;

@UtilityClass
public class MqConfig {
    /**
     * 启动测试之前请替换如下 XXX 为您的配置
     */
    public static final String TOPIC;
    public static final String PRODUCER_ID;
    public static final String CONSUMER_ID;
    public static final String ACCESS_KEY;
    public static final String SECRET_KEY;
    public static final String ONSADDR;
    public static final String SINGLE_REFUND_QUERY = "singlRefundQuery";//单笔退款查询
    public static final String FAST_PAY_QUERY = "fastPayQuery";//快捷支付退款

    static {
        final MqConfigs mqConfigs = getMqConfigs();
        TOPIC = mqConfigs.topic();
        Preconditions.checkState(!Strings.isNullOrEmpty(TOPIC), "Topic");
        PRODUCER_ID = mqConfigs.producerId();
        Preconditions.checkState(!Strings.isNullOrEmpty(PRODUCER_ID), "PRODUCER_ID");
        CONSUMER_ID = mqConfigs.consumerId();
        Preconditions.checkState(!Strings.isNullOrEmpty(CONSUMER_ID), "CONSUMER_ID");
        ACCESS_KEY = mqConfigs.accessKey();
        Preconditions.checkState(!Strings.isNullOrEmpty(ACCESS_KEY), "ACCESS_KEY");
        SECRET_KEY = mqConfigs.secretKey();
        Preconditions.checkState(!Strings.isNullOrEmpty(SECRET_KEY), "SECRET_KEY");
        ONSADDR = mqConfigs.onsaddr();
        Preconditions.checkState(!Strings.isNullOrEmpty(ONSADDR), "ONSADDR");
    }
    /**
     * 获得银胜配置
     */
    public static MqConfigs getMqConfigs() {
        return ConfigCache.getOrCreate(MqConfigs.class);
    }

    @org.aeonbits.owner.Config.Sources("classpath:mq.properties")
    private interface MqConfigs extends org.aeonbits.owner.Config {


        @Key("mq.topic")
        @DefaultValue("")
        public abstract String topic();


        @Key("mq.producerId")
        @DefaultValue("")
        public abstract String producerId();


        @Key("mq.consumerId")
        @DefaultValue("")
        public abstract String consumerId();


        @Key("mq.accessKey")
        @DefaultValue("")
        public abstract String accessKey();

        @Key("mq.secretKey")
        @DefaultValue("")
        public abstract String secretKey();

        @Key("mq.onsaddr")
        @DefaultValue("")
        public abstract String onsaddr();
    }
}
