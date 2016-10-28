package com.jkm.enums;

/**
 * Created by yulong.zhang on 2016/10/27.
 *
 * 错误码
 *
 * 注意： 0000 表示成功， 0007 指上游超时， 1004 表示业务已受理正在处理中， 1027 表示业
 * 务已受理；其它代码都表示订单状态为失败。
 * 为了不造成损失， 超时与未知（未收到返回） 的交易请先当成功处理。
 */
public enum EnumResultCode {


    SUCCESS("0000", "成功"),

    FAIL("0001", "不成功"),

    INVALID_BANK_CODE("0002", "无效银行代码"),

    INVALID_VARIETY("0003", "无效品种"),

    DATA_NOT_AUTHORIZED("0004", "数据未授权"),

    /**
     * （ YS 手动暂停了业务）
     */
    SYSTEM_MAINTENANCE("0005", "系统维护中"),

    SYSTEM_FAILURE("0006", "系统故障"),

    NETWORK_TIMEOUT("0007", "网络超时"),

    LINE_BUSY("0008", "线路忙"),

    PACKET_DATA_FORMAT_ERROR("0009", "报文数据格式错"),

    REPEAT_THE_FLOW_NUMBER("0010", "流水号重复"),

    THE_SPECIES_WILL_NOT_BE_ACCEPTED("0011", "该品种暂不受理"),

    SYSTEM_BUSY_AND_TEMPORARILY_NOT_ACCEPTED("1001", "系统忙，暂不受理"),

    WRONG_ACCOUNT_PASSWORD("1002", "帐号密码有误"),

    ACCOUNT_ARREARS_OR_INVALID_CAN_NOT_BE_TRADED("1003", "帐号欠费或无效，无法交易"),

    BUSINESS_HAS_BEEN_ACCEPTED_AND_IS_IN_PROCESSING("1004", "业务已受理，正在处理中"),

    COMMODITY_PRICING_STRATEGY_IS_NOT_SET("1005", "商品价格策略未设置"),

    WRONG_AMOUNT("1006", "金额错误"),

    NOT_SUFFICIENT_FUNDS("1007", "余额不足"),

    DATABASE_ERROR("1008", "数据库错误"),

    NEED_TO_CHECK_THE_ARREARS("1009", "需要先查欠费"),

    CHECK_WITHOUT_RECORD("1010", "查无记录"),

    INVALID_BANK_CARD_ACCOUNT("1011", "无效银行卡帐户"),

    BANK_CARD_ACCOUNT_HAS_BEEN_FROZEN("1012", "银行卡帐户已冻结"),

    BANK_CARD_ACCOUNT_NOT_AUTHORIZED("1013", "银行卡帐户未授权"),

    BANK_CARD_ACCOUNT_TEMPORARILY_NOT_SUPPORTED("1014", "银行卡帐户暂不支持"),

    BANK_CARD_NUMBER_IS_NOT_BOUND("1015", "银行卡号未绑定"),

    MAC_CHECK_ERROR("1016", "MAC 校验错"),

    INVALID_TERMINAL_NUMBER("1017", "无效终端号"),

    NO_ELECTRONIC_COUPONS("1018", "没有电子券"),

    INVALID_VALUE("1019", "无效数量值"),

    NO_THIS_MERCHANT("1020", "无此商户"),

    REPEATED_CUSTOMIZATION("1021", "重复定制"),

    SINGLE_BRUSH_AMOUNT_OVER_THE_MAXIMUM_AMOUNT_OF_CREDIT("1022", "单笔刷卡金额超上限"),

    TERMINAL_PAY_BY_CARD_NUMBER_SUPER_CEILING("1023", "终端刷卡笔数超上限"),

    BANK_CARD_DAILY_TRANSACTION_AMOUNT_EXCEEDS_THE_MAXIMUM_LIMIT("1024", "银行卡日交易累计金额超上限"),

    TIME_HAS_BEEN_REVERSED("1025", "已过冲正时间"),

    BEYOND_THE_CORRECT_NUMBER("1026", "超出冲正次数"),

    SYSTEM_BUSY_BUSINESS_SUSPENDED("2101", "系统忙，业务暂停受理"),

    /**
     * （发生在查询时，当客户端收到此响应，当成功
     * 处理，无需再查，等第二天对账）
     */
    ACCEPTED_SUCCESS_WAIT_FOR_THE_RESULTS_OF_RECONCILIATION("1027", "已受理成功，等对账出结果");


    private String code;

    private String msg;


    EnumResultCode(final String code, final String msg) {
        this.code = code;
        this.msg = msg;
    }
}
