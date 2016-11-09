package com.jkm.service.impl;

import com.jkm.helper.TicketMessageParams.*;
import com.jkm.service.TicketSendMessageService;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2016/11/8.
 */
@Service
public class TicketSendMessageServiceImpl implements TicketSendMessageService {

    @Override
    public void sendPaymentMessage(final SendPaymentParam1 sendPaymentParam1) {

    }

    @Override
    public void buyTicketSuccessMessage(final SendBuyTicketSuccessParam sendBuyTicketSuccessParam) {

    }

    @Override
    public void buyTicketFailMessage(final SendBuyTicketFailParam sendBuyTicketFailParam) {

    }

    @Override
    public void grabTicketSuccessHaveResidueMessage(final SendGrabTicketSuccessParam sendGrabTicketSuccessParam) {

    }

    @Override
    public void grabTicketSuccessHaveNotResidueMessage(final SendGrabTicketSuccessParam sendGrabTicketSuccessParam) {

    }

    @Override
    public void grabTicketFailMessage(final SendGrabTicketFailParam sendGrabTicketFailParam) {

    }
}
