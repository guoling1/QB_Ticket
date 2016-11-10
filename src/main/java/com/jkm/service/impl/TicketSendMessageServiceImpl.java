package com.jkm.service.impl;

import com.jkm.enums.notifier.EnumNoticeType;
import com.jkm.enums.notifier.EnumUserType;
import com.jkm.helper.TicketMessageParams.*;
import com.jkm.helper.notifier.SendMessageParams;
import com.jkm.service.TicketSendMessageService;
import com.jkm.service.notifier.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by yulong.zhang on 2016/11/8.
 */
@Service
public class TicketSendMessageServiceImpl implements TicketSendMessageService {

    @Autowired
    private SendMessageService sendMessageService;

    /**
     * {@inheritDoc}
     *
     * @param sendPaymentParam
     */
    @Override
    @Transactional
    public void sendPaymentMessage(final SendPaymentParam sendPaymentParam) {
        final HashMap<String, String> data = new HashMap<>();
        data.put("code", sendPaymentParam.getCode());
        data.put("amount", sendPaymentParam.getAmount());
        final SendMessageParams sendMessageParams = SendMessageParams.builder()
                .uid(sendPaymentParam.getUid())
                .userType(EnumUserType.FOREGROUND_USER)
                .noticeType(EnumNoticeType.PAYMENT_CODE)
                .mobile(sendPaymentParam.getMobile())
                .data(data).sendTime(new Timestamp(System.currentTimeMillis()))
                .build();
        this.sendMessageService.sendMessage(sendMessageParams);
    }

    /**
     * {@inheritDoc}
     *
     * @param sendBuyTicketSuccessParam
     */
    @Override
    public void sendBuyTicketSuccessMessage(final SendBuyTicketSuccessParam sendBuyTicketSuccessParam) {
        final HashMap<String, String> data = new HashMap<>();
        final String trainStationAndTrainNo = sendBuyTicketSuccessParam.getStartStation() + "-"
                + sendBuyTicketSuccessParam.getEndStation() + sendBuyTicketSuccessParam.getTrainNo();
        final String startDate = sendBuyTicketSuccessParam.getStartDate();
        final String[] dateSplit = startDate.split("-");
        final String runTime = dateSplit[1] + "月" + dateSplit[2] + "日" + sendBuyTicketSuccessParam.getStartTime();
        data.put("trainStationAndTrainNo", trainStationAndTrainNo);
        data.put("runTime", runTime);
        data.put("startStation", sendBuyTicketSuccessParam.getStartStation());
        data.put("ticketNo", sendBuyTicketSuccessParam.getTicketNo());
        final SendMessageParams sendMessageParams = SendMessageParams.builder()
                .uid(sendBuyTicketSuccessParam.getUid())
                .userType(EnumUserType.FOREGROUND_USER)
                .noticeType(EnumNoticeType.BUY_TICKET_SUCCESS)
                .mobile(sendBuyTicketSuccessParam.getMobile())
                .data(data)
                .build();
        this.sendMessageService.sendMessage(sendMessageParams);
    }

    /**
     * {@inheritDoc}
     *
     * @param sendBuyTicketFailParam
     */
    @Override
    public void sendBuyTicketFailMessage(final SendBuyTicketFailParam sendBuyTicketFailParam) {
        final HashMap<String, String> data = new HashMap<>();
        final String trainStationAndTrainNo = sendBuyTicketFailParam.getStartStation() + "-"
                + sendBuyTicketFailParam.getEndStation() + sendBuyTicketFailParam.getTrainNo();
        data.put("trainStationAndTrainNo", trainStationAndTrainNo);
        final SendMessageParams sendMessageParams = SendMessageParams.builder()
                .uid(sendBuyTicketFailParam.getUid())
                .userType(EnumUserType.FOREGROUND_USER)
                .noticeType(EnumNoticeType.BUY_TICKET_FAIL)
                .mobile(sendBuyTicketFailParam.getMobile())
                .data(data)
                .build();
        this.sendMessageService.sendMessage(sendMessageParams);
    }

    /**
     * {@inheritDoc}
     *
     * @param sendGrabTicketSuccessParam
     */
    @Override
    public void sendGrabTicketSuccessHaveResidueMessage(final SendGrabTicketSuccessParam sendGrabTicketSuccessParam) {
        final HashMap<String, String> data = new HashMap<>();
        final String trainStationAndTrainNo = sendGrabTicketSuccessParam.getStartStation() + "-"
                + sendGrabTicketSuccessParam.getEndStation() + sendGrabTicketSuccessParam.getTrainNo();
        final String startDate = sendGrabTicketSuccessParam.getStartDate();
        final String[] dateSplit = startDate.split("-");
        final String runTime = dateSplit[1] + "月" + dateSplit[2] + "日" + sendGrabTicketSuccessParam.getStartTime();
        data.put("trainStationAndTrainNo", trainStationAndTrainNo);
        data.put("runTime", runTime);
        data.put("startStation", sendGrabTicketSuccessParam.getStartStation());
        data.put("ticketNo", sendGrabTicketSuccessParam.getTicketNo());
        data.put("residueAmount", sendGrabTicketSuccessParam.getResidueAmount());
        final SendMessageParams sendMessageParams = SendMessageParams.builder()
                .uid(sendGrabTicketSuccessParam.getUid())
                .userType(EnumUserType.FOREGROUND_USER)
                .noticeType(EnumNoticeType.GRAB_TICKET_SUCCESS_HAVE_RESIDUE_YES)
                .mobile(sendGrabTicketSuccessParam.getMobile())
                .data(data)
                .build();
        this.sendMessageService.sendMessage(sendMessageParams);
    }

    /**
     * {@inheritDoc}
     *
     * @param sendGrabTicketSuccessParam
     */
    @Override
    public void sendGrabTicketSuccessHaveNotResidueMessage(final SendGrabTicketSuccessParam sendGrabTicketSuccessParam) {
        final HashMap<String, String> data = new HashMap<>();
        final String trainStationAndTrainNo = sendGrabTicketSuccessParam.getStartStation() + "-"
                + sendGrabTicketSuccessParam.getEndStation() + sendGrabTicketSuccessParam.getTrainNo();
        final String startDate = sendGrabTicketSuccessParam.getStartDate();
        final String[] dateSplit = startDate.split("-");
        final String runTime = dateSplit[1] + "月" + dateSplit[2] + "日" + sendGrabTicketSuccessParam.getStartTime();
        data.put("trainStationAndTrainNo", trainStationAndTrainNo);
        data.put("runTime", runTime);
        data.put("startStation", sendGrabTicketSuccessParam.getStartStation());
        data.put("ticketNo", sendGrabTicketSuccessParam.getTicketNo());
        final SendMessageParams sendMessageParams = SendMessageParams.builder()
                .uid(sendGrabTicketSuccessParam.getUid())
                .userType(EnumUserType.FOREGROUND_USER)
                .noticeType(EnumNoticeType.GRAB_TICKET_SUCCESS_HAVE_RESIDUE_NO)
                .mobile(sendGrabTicketSuccessParam.getMobile())
                .data(data)
                .build();
        this.sendMessageService.sendMessage(sendMessageParams);
    }

    /**
     * {@inheritDoc}
     *
     * @param sendGrabTicketFailParam
     */
    @Override
    public void sendGrabTicketFailMessage(final SendGrabTicketFailParam sendGrabTicketFailParam) {
        final HashMap<String, String> data = new HashMap<>();
        final String startDate = sendGrabTicketFailParam.getStartDate();
        final String[] dateSplit = startDate.split("-");
        final String dateAndTrainStation = dateSplit[1] + "月" + dateSplit[2] + "日" +  sendGrabTicketFailParam.getStartStation() + "-"
                + sendGrabTicketFailParam.getEndStation();

        data.put("dateAndTrainStation", dateAndTrainStation);
        final SendMessageParams sendMessageParams = SendMessageParams.builder()
                .uid(sendGrabTicketFailParam.getUid())
                .userType(EnumUserType.FOREGROUND_USER)
                .noticeType(EnumNoticeType.GRAB_TICKET_FAIL)
                .mobile(sendGrabTicketFailParam.getMobile())
                .data(data)
                .build();
        this.sendMessageService.sendMessage(sendMessageParams);
    }
}
