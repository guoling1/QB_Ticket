package com.jkm.service.notifier;

import com.jkm.helper.notifier.SendMessageParams;

import java.util.List;

/**
 * Created by huangwei on 9/10/15.
 * 发送短信
 */
public interface SendMessageService {
    /**
     * 发送短信消息
     *
     * @param params
     * @return
     */
    long sendMessage(final SendMessageParams params);

//    long sendInstantMessage(final SendMessageParams params);
//
//    void asyncSendInstantMessage(final SendMessageParams params);
//
//    void sendInstantMessage(final List<SendMessageParams> paramsList);
//
//    void asyncSendInstantMessage(final List<SendMessageParams> paramsList);
//
//    long sendTimedMessage(final SendMessageParams params);
//
//    void asyncSendTimedMessage(final SendMessageParams params);
//
//    void sendTimedMessage(final List<SendMessageParams> paramsList);
//
//    void asyncSTimedMessage(final List<SendMessageParams> paramsList);
}
