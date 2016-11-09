package com.jkm.service;


import com.jkm.helper.TicketMessageParams.*;

/**
 * Created by yulong.zhang on 2016/11/8.
 */
public interface TicketSendMessageService {

    /**
     * 支付发送验证码
     *
     * @param sendPaymentParam1
     */
    void sendPaymentMessage(SendPaymentParam1 sendPaymentParam1);

    /**
     * 出票成功短信
     *
     * @param sendBuyTicketSuccessParam
     */
    void buyTicketSuccessMessage(SendBuyTicketSuccessParam sendBuyTicketSuccessParam);

    /**
     * 出票失败短信
     *
     * @param sendBuyTicketFailParam
     */
    void buyTicketFailMessage(SendBuyTicketFailParam sendBuyTicketFailParam);

    /**
     * 抢票成功短信（有余款）
     *
     * @param sendGrabTicketSuccessParam
     */
    void grabTicketSuccessHaveResidueMessage(SendGrabTicketSuccessParam sendGrabTicketSuccessParam);


    /**
     * 抢票成功短信（无余款）
     *
     * @param sendGrabTicketSuccessParam
     */
    void grabTicketSuccessHaveNotResidueMessage(SendGrabTicketSuccessParam sendGrabTicketSuccessParam);

    /**
     *抢票失败短信
     *
     * @param sendGrabTicketFailParam
     */
    void grabTicketFailMessage(SendGrabTicketFailParam sendGrabTicketFailParam);
}
