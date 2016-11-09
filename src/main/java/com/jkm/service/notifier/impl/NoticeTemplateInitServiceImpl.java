package com.jkm.service.notifier.impl;

import com.jkm.entity.notifier.SmsTemplate;
import com.jkm.enums.notifier.EnumNoticeType;
import com.jkm.helper.notifier.NotifierConstants;
import com.jkm.service.notifier.MessageTemplateService;
import com.jkm.service.notifier.NoticeTemplateInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by konglingxin on 15/11/24.
 */
@Service
public class NoticeTemplateInitServiceImpl implements NoticeTemplateInitService {
    @Autowired
    private MessageTemplateService messageTemplateService;

    @Override
    public void initTemplate() {
        final String platformName = NotifierConstants.getNotifierConfig().platformName();

        addSmsTemplate(EnumNoticeType.PAYMENT_CODE,
                "支付验证码${code}，五分钟内有效。您本次支付${amount}元，千万不要把验证码告诉其他人【" + platformName + "】");

        addSmsTemplate(EnumNoticeType.BUY_TICKET_SUCCESS,
                "您预订的${trainStationAndTrainNo}次列车出票成功，于${runTime}从${startStation}发车，12306取票号${ticketNo}【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.BUY_TICKET_FAIL,
                "很抱歉，您预订的${trainStationAndTrainNo}次列车出票失败，资金已全额退至您支付银行卡，请注意查收【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.GRAB_TICKET_SUCCESS_HAVE_RESIDUE_YES,
                "已抢到${trainStationAndTrainNo}次列车，于${runTime}从北京南站发车，12306订单号${ticketNo}。余款${residueAmount}元已退至您支付银行卡，请注意查收【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.GRAB_TICKET_SUCCESS_HAVE_RESIDUE_NO,
                "已抢到${trainStationAndTrainNo}次列车，于${runTime}从北京南站发车，12306订单号${ticketNo}【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.GRAB_TICKET_FAIL,
                "未抢到${dateAndTrainStation}的列车，系统已取消抢票。资金已全额退至您支付银行卡，请注意查收【" + platformName + "】");
    }

    private void addSmsTemplate(final EnumNoticeType noticeType,
                                final String template) {
        final SmsTemplate smsTemplate = new SmsTemplate();
        smsTemplate.setMessageTemplate(template);
        smsTemplate.setNoticeType(noticeType.getId());
        this.messageTemplateService.addTemplate(smsTemplate);

    }
}
