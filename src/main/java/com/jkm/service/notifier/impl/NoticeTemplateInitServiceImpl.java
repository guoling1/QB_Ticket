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

        addSmsTemplate(EnumNoticeType.LOGIN,
                "【" + platformName + "】您的登录验证码为${code}，请勿泄漏。");
        addSmsTemplate(EnumNoticeType.MODIFY_PASSWORD,
                "【" + platformName + "】您的重设密码验证码为${code}，请勿泄漏。");
        addSmsTemplate(EnumNoticeType.RETRIEVE_PASSWORD,
                "【" + platformName + "】您的找回密码验证码为${code}，请勿泄漏。");

        addSmsTemplate(EnumNoticeType.BIND_MOBILE,
                "【" + platformName + "】您的绑定手机验证码为${code}，请勿泄漏。");
        addSmsTemplate(EnumNoticeType.UNBIND_MOBILE,
                "【" + platformName + "】您的解绑手机验证码为${code}，请勿泄漏。");
        addSmsTemplate(EnumNoticeType.SET_TRADING_PASSWORD,
                "【" + platformName + "】您的设置交易密码验证码为${code}，请勿泄漏。");
        addSmsTemplate(EnumNoticeType.OPEN_NOPWD,
                "【" + platformName + "】您的设置免密支付验证码为${code}，请勿泄漏。");
        addSmsTemplate(EnumNoticeType.UNBIND_WEIXIN,
                "【" + platformName + "】您的解绑微信验证码为${code}，请勿泄漏。");
        addSmsTemplate(EnumNoticeType.UNBIND_BANK_CARD,
                "【" + platformName + "】您的解绑银行卡验证码为${code}，请勿泄漏。");
        addSmsTemplate(EnumNoticeType.WITHDRAW,
                "【" + platformName + "】您的提现验证码为${code}，请勿泄漏。");

        addSmsTemplate(EnumNoticeType.ADMIN_MODIFY_PASSWORD,
                "【" + platformName + "】你的后台登录密码被修改为${code}，请勿泄漏。");
        addSmsTemplate(EnumNoticeType.ADMIN_LOGIN,
                "【" + platformName + "】后台登录验证码为${code}，请勿泄漏。");
        addSmsTemplate(EnumNoticeType.ADMIN_CLOSE_TOTP,
                "【" + platformName + "】关闭二次验证功能验证码为${code}，请勿泄漏。");

        addSmsTemplate(EnumNoticeType.MANUAL_RECHARGE,
                "【" + platformName + "】后台手工充值成功，充值金额${amount}。");
        addSmsTemplate(EnumNoticeType.WITHDRAW_FAIL,
                "【" + platformName + "】你的提现订单号为${sn}的提现订单提现失败。");
        addSmsTemplate(EnumNoticeType.WITHDRAW_SUCCESS,
                "【" + platformName + "】你的提现订单号为${sn}的提现订单提现成功。");
        addSmsTemplate(EnumNoticeType.FAIL_BID,
                "【" + platformName + "】很抱歉，您的${amount}元投资，因借款未达成流标，资金已退回至账户余额，请您选择其它标的进行投资。");
        addSmsTemplate(EnumNoticeType.START_INTEREST,
                "【" + platformName + "】您的${amount}元投资，已经于${valueDate}开始计息，请登录平台查看收款计划。");
        addSmsTemplate(EnumNoticeType.REPAYMENT,
                "【" + platformName + "】您的账户于${date}日收到本息回款共${amount}元，第${current}/${total}期。温馨提示，回款及时再投资，巧达收益最大化。");
        addSmsTemplate(EnumNoticeType.INVITE_SUCCESS,
                "【" + platformName + "】你对编号为${sn}的标的投资成功,投资金额为${amount}。");
    }

    private void addSmsTemplate(final EnumNoticeType noticeType,
                                final String template) {
        final SmsTemplate smsTemplate = new SmsTemplate();
        smsTemplate.setMessageTemplate(template);
        smsTemplate.setNoticeType(noticeType.getId());
        this.messageTemplateService.addTemplate(smsTemplate);

    }
}
