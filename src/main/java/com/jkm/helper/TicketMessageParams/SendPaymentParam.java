package com.jkm.helper.TicketMessageParams;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/8.
 */
@Data
public class SendPaymentParam {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

    /**
     * 金额
     */
    private String amount;


}
