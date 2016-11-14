
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
    /**
     * 快捷支付查询
     */
    public static final String FAST_PAY_QUERY = "fastPayQuery";
    /**
     * 快捷支付抢票单查询
     */
    public static final String FAST_PAY_GRAB_QUERY = "fastPayGrabQuery";

    /**
     * 取消失去有效支付时间的订单
     */
    public static final String TICKET_CANCEL_EXPIRED_ORDER = "ticketCancelExpiredOrder";

    /**
     * 处理 退款申请发送成功 等待最后结果的订单
     */
    public static final String TICKET_HANDLE_REFUND_ORDER_RESULT = "ticketHandleRefundOrderResult";

    /**
     * 取消失去有效支付时间的抢票单
     */
    public static final String TICKET_CANCEL_EXPIRED_GRAB_ORDER = "ticketCancelExpiredGrabOrder";

    /**
     * 客户未购买套餐到期自动退款
     */
    public static final String NO_PACKAGE_WAIT_REFUND = "noPackageWaitRefund";

    /**
     * 抢票下单失败,到期自动退款
     */
    public static final String GRAB_FORM_FAIL_WAIT_REFUND = "grabFormFailWaitRefund";

    /**
     * 退票退款处理请求中的消息
     */
    public static final String RETURN_TICKET_REFUND_ING = "returnTicketRefundIng";

    /**
     * 抢票差价退款的消息
     */
    public static final String GRAB_TICKET_REFUND_PART = "grabTicketRefundPart";

    /**
     * 抢票全额退款的消息
     */
    public static final String GRAB_TICKET_REFUND_ALL = "grabTicketRefundAll";


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
     * 获得配置
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
