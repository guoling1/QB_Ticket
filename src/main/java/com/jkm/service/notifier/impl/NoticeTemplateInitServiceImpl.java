package com.jkm.service.notifier.impl;

import com.jkm.entity.notifier.SmsTemplate;
import com.jkm.enums.notifier.EnumNoticeType;
import com.jkm.helper.notifier.NotifierConstants;
import com.jkm.service.notifier.MessageTemplateService;
import com.jkm.service.notifier.NoticeTemplateInitService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by konglingxin on 15/11/24.
 */
@Service
public class NoticeTemplateInitServiceImpl implements NoticeTemplateInitService {
    @Autowired
    private MessageTemplateService messageTemplateService;

    @Override
    public void initTemplate() {
        final List<SmsTemplate> messageTemplate = this.messageTemplateService.getMessageTemplate();
        if (!CollectionUtils.isEmpty(messageTemplate)) {
            return;
        }

        final String platformName = NotifierConstants.getNotifierConfig().platformName();

        addSmsTemplate(EnumNoticeType.PAYMENT_CODE,
                "支付验证码${code}，五分钟内有效。您本次支付${amount}元，千万不要把验证码告诉其他人【" + platformName + "】");

        addSmsTemplate(EnumNoticeType.BUY_TICKET_SUCCESS,
                "您预订的${trainStationAndTrainNo}次列车出票成功，于${runTime}从${startStation}发车，12306取票号${ticketNo}【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.BUY_TICKET_FAIL,
                "很抱歉，您预订的${trainStationAndTrainNo}次列车出票失败，资金已全额退至您支付银行卡，请注意查收【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.GRAB_TICKET_SUCCESS_HAVE_RESIDUE_YES,
                "已抢到${trainStationAndTrainNo}次列车，于${runTime}从${startStation}发车，12306订单号${ticketNo}。余款${residueAmount}元已退至您支付银行卡，请注意查收【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.GRAB_TICKET_SUCCESS_HAVE_RESIDUE_NO,
                "已抢到${trainStationAndTrainNo}次列车，于${runTime}从${startStation}发车，12306订单号${ticketNo}【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.GRAB_TICKET_FAIL,
                "未抢到${dateAndTrainStation}的列车，系统已取消抢票。资金已全额退至您支付银行卡，请注意查收【" + platformName + "】");
        addSmsTemplate(EnumNoticeType.GRAB_CANCEL,
                "您已取消抢票（${dateAndTrainStation}），资金已全额退至您支付银行卡，2小时内到账，请注意查收【钱包%252b%252b】");
        addSmsTemplate(EnumNoticeType.RETURN_TICKET_ONLINE,
                "乘客${userName}退票已申请（${dateAndTrainStationAndTrainNo}），将根据铁路局实退金额为您退款，铁路局退款需要1-15个工作日，请注意查收【钱包%252b%252b】");
        addSmsTemplate(EnumNoticeType.RETURN_TICKET_DOWN,
                "乘客${userName}退票成功（${dateAndTrainStationAndTrainNo}），票款及出票套餐${amount}元退至您支付银行卡，2小时内到账，请注意查收【钱包%252b%252b】");
    }

    private void addSmsTemplate(final EnumNoticeType noticeType,
                                final String template) {
        final SmsTemplate smsTemplate = new SmsTemplate();
        smsTemplate.setMessageTemplate(template);
        smsTemplate.setNoticeType(noticeType.getId());
        this.messageTemplateService.addTemplate(smsTemplate);

    }
}
