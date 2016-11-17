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
    long sendPaymentMessage(SendPaymentParam sendPaymentParam);

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

    /**
     * 抢票（用户主动取消）
     *
     * @param sendCancelGrabTicketParam
     */
    void sendCancelGrabTicketMessage(SendCancelGrabTicketParam sendCancelGrabTicketParam);

    /**
     * 用户在线申请退票成功：
     *
     * @param sendReturnTicketOnlineParam
     */
    void sendReturnTicketOnlineMessage(SendReturnTicketOnlineParam sendReturnTicketOnlineParam);

    /**
     * 收到供应商退票成功通知
     *
     * @param sendReturnTicketDownParam
     */
    void sendReturnTicketDownMessage(SendReturnTicketDownParam sendReturnTicketDownParam);
}
