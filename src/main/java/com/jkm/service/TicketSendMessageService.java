package com.jkm.service;


import com.jkm.helper.TicketMessageParams.*;

/**
 * Created by yulong.zhang on 2016/11/8.
 */
public interface TicketSendMessageService {

    /**
     * 支付发送验证码
     *
     * @param sendPaymentParam
     */
    void sendPaymentMessage(SendPaymentParam sendPaymentParam);

    /**
     * 出票成功短信
     *
     * @param sendBuyTicketSuccessParam
     */
    void sendBuyTicketSuccessMessage(SendBuyTicketSuccessParam sendBuyTicketSuccessParam);

    /**
     * 出票失败短信
     *
     * @param sendBuyTicketFailParam
     */
    void sendBuyTicketFailMessage(SendBuyTicketFailParam sendBuyTicketFailParam);

    /**
     * 抢票成功短信（有余款）
     *
     * @param sendGrabTicketSuccessParam
     */
    void sendGrabTicketSuccessHaveResidueMessage(SendGrabTicketSuccessParam sendGrabTicketSuccessParam);


    /**
     * 抢票成功短信（无余款）
     *
     * @param sendGrabTicketSuccessParam
     */
    void sendGrabTicketSuccessHaveNotResidueMessage(SendGrabTicketSuccessParam sendGrabTicketSuccessParam);

    /**
     *抢票失败短信
     *
     * @param sendGrabTicketFailParam
     */
    void sendGrabTicketFailMessage(SendGrabTicketFailParam sendGrabTicketFailParam);
}
