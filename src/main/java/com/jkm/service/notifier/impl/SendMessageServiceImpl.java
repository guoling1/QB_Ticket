package com.jkm.service.notifier.impl;

import com.google.common.base.Preconditions;
import com.jkm.dao.notifier.MessageTemplateDao;
import com.jkm.dao.notifier.SendMessageRecordDao;
import com.jkm.entity.notifier.SendMessageRecord;
import com.jkm.entity.notifier.SmsTemplate;
import com.jkm.enums.notifier.EnumSendType;
import com.jkm.enums.notifier.EnumUserType;
import com.jkm.exception.NoticeSendException;
import com.jkm.helper.notifier.SendMessageParams;
import com.jkm.helper.notifier.SendMsgEvent;
import com.jkm.notifiersdk.entity.SendInstantSmsParams;
import com.jkm.notifiersdk.entity.SendTimedSmsParams;
import com.jkm.notifiersdk.service.YmSmsSdkService;
import com.jkm.service.notifier.SendMessageService;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.VelocityStringTemplate;
import com.jkm.util.reactor.AbstractTaskProcessor;
import com.jkm.util.reactor.TaskReactor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by konglingxin on 15/10/9.
 */
@Slf4j
@Service
public class SendMessageServiceImpl implements SendMessageService, InitializingBean, DisposableBean {
    @Autowired
    private SendMessageRecordDao sendMessageRecordDao;

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Autowired
    private YmSmsSdkService ymSmsSdkService;

    private TaskReactor taskReactor = new TaskReactor(30, 20000);

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.taskReactor = new TaskReactor(10, 20000);
        this.taskReactor.addEventProcessor(SendMsgEvent.class, new AbstractTaskProcessor<SendMsgEvent>() {
            @Override
            protected void process(final SendMsgEvent event) {
                if (isEventValid(event)) {
                    switch (event.getSendType()) {
                        case INSTANT:
                            sendInstantMessage(event.getSendMessageParamsList());
                            break;
                        case TIMED:
                            sendTimedMessage(event.getSendMessageParamsList());
                            break;
                    }
                }
            }

            private boolean isEventValid(final SendMsgEvent event) {
                return event.getSendType() != null
                        && event.getSendMessageParamsList() != null
                        && !event.getSendMessageParamsList().isEmpty();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() throws Exception {
        if (this.taskReactor != null) {
            this.taskReactor.shutdown();
        }
    }
/**
 * 发送短信消息
 *
 * @param uid
 * @param mobile
 * @param noticeType
 * @param params
 * @return
 */
    /**
     * {@inheritDoc}
     */
    @Override
    public long sendMessage(final SendMessageParams params) {
        checkSendMessageParams(params);
        final SmsTemplate messageTemplate = this.messageTemplateDao.getTemplateByType(params.getNoticeType().getId());
        Preconditions.checkNotNull(messageTemplate, "[%s]消息模板为空", params.getNoticeType().getDesc());
        return sendRealMessage(params, messageTemplate);
    }

    private void checkSendMessageParams(final SendMessageParams params) {
        final Pair<Boolean, String> checkResult = params.checkParamsCorrect();
        Preconditions.checkArgument(checkResult.getLeft(), checkResult.getRight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long sendInstantMessage(final SendMessageParams params) {
        return sendMessage(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long sendTimedMessage(final SendMessageParams params) {
        checkSendMessageParams(params);
        Preconditions.checkNotNull(params.getSendTime(), "发送时间不能为空");
        final SmsTemplate messageTemplate = this.messageTemplateDao.getTemplateByType(params.getNoticeType().getId());
        Preconditions.checkNotNull(messageTemplate, "[%s]消息模板为空", params.getNoticeType().getDesc());
        return sendRealTimedMessage(params, messageTemplate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendInstantMessage(final List<SendMessageParams> paramsList) {
        for (final SendMessageParams params : paramsList) {
            sendInstantMessage(params);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void asyncSendInstantMessage(final SendMessageParams params) {
        checkSendMessageParams(params);
        this.taskReactor.addEvent(SendMsgEvent.builder().addSendMessageParam(params)
                .sendType(EnumSendType.INSTANT)
                .build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void asyncSendInstantMessage(final List<SendMessageParams> paramsList) {
        if (!paramsList.isEmpty()) {
            for (final SendMessageParams params : paramsList) {
                checkSendMessageParams(params);
            }
            this.taskReactor.addEvent(SendMsgEvent.builder().sendMessageParamsList(paramsList)
                    .sendType(EnumSendType.INSTANT)
                    .build());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void asyncSendTimedMessage(final SendMessageParams params) {
        checkSendMessageParams(params);
        Preconditions.checkNotNull(params.getSendTime(), "发送时间不能为空");
        this.taskReactor.addEvent(SendMsgEvent.builder().addSendMessageParam(params)
                .sendType(EnumSendType.TIMED)
                .build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void asyncSTimedMessage(final List<SendMessageParams> paramsList) {
        if (!paramsList.isEmpty()) {
            for (final SendMessageParams params : paramsList) {
                checkSendMessageParams(params);
                Preconditions.checkNotNull(params.getSendTime(), "发送时间不能为空");
            }
            this.taskReactor.addEvent(SendMsgEvent.builder().sendMessageParamsList(paramsList)
                    .sendType(EnumSendType.TIMED)
                    .build());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendTimedMessage(final List<SendMessageParams> paramsList) {
        for (final SendMessageParams params : paramsList) {
            sendTimedMessage(params);
        }
    }

    /**
     * 调用第三方接口发送短信
     *
     * @param params
     * @param messageTemplate @return
     */
    private long sendRealMessage(final SendMessageParams params,
                                 final SmsTemplate messageTemplate) {

        final String content = VelocityStringTemplate.process(messageTemplate.getMessageTemplate(), params.getData());

        log.info("send msg[{}] to user[{}],mobile[{}]", content, params.getUid(), params.getMobile());
        try {
            this.ymSmsSdkService.sendInstantSms(SendInstantSmsParams.builder()
                    .message(content)
                    .phone(params.getMobile())
                    .build());
            log.debug("用户[id={},mobile={}]发送短信[content={}]成功:{}", params.getUid(), params.getMobile(), content);
        } catch (final Throwable e) {
            log.error("用户[" + params.getUid() + "]发送短信失败:{}", e.getMessage(), e);
            throw new NoticeSendException("短信消息发送失败，失败原因:" + e.getMessage(), e);
        }
        final SendMessageRecord sendMessageRecord = recordSendMessage(params.getUid(),
                params.getUserType(), params.getMobile(), messageTemplate, content, "", new Date(), 1);
        return sendMessageRecord.getId();
    }

    /**
     * 调用第三方接口发送短信
     *
     * @param params
     * @param messageTemplate @return
     */
    private long sendRealTimedMessage(final SendMessageParams params,
                                      final SmsTemplate messageTemplate) {

        final String content = VelocityStringTemplate.process(messageTemplate.getMessageTemplate(), params.getData());

        log.info("send msg[{}] to user[{}],mobile[{}]", content, params.getUid(), params.getMobile());
        try {
            final String sendtime = DateFormatUtil.format(params.getSendTime(), DateFormatUtil.yyyyMMddHHmmss);
            this.ymSmsSdkService.sendTimedSms(SendTimedSmsParams.builder()
                    .message(content)
                    .phone(params.getMobile())
                    .sendtime(sendtime)
                    .build());
            log.debug("用户[id={},mobile={}]发送定时短信[content={},time={}]成功:{}",
                    params.getUid(), params.getMobile(), sendtime, content);
        } catch (final Throwable e) {
            log.error("用户[" + params.getUid() + "]发送短信失败:{}", e.getMessage(), e);
            throw new NoticeSendException("短信消息发送失败，失败原因:" + e.getMessage(), e);
        }
        final SendMessageRecord sendMessageRecord = recordSendMessage(params.getUid(),
                params.getUserType(), params.getMobile(), messageTemplate, content, "", params.getSendTime(), 1);
        return sendMessageRecord.getId();
    }

    /**
     * 记录发送消息内容
     *
     * @param uid
     * @param userType
     * @param mobile
     * @param messageTemplate
     * @param content
     * @param sn              @return
     */
    private SendMessageRecord recordSendMessage(final long uid,
                                                final EnumUserType userType,
                                                final String mobile,
                                                final SmsTemplate messageTemplate,
                                                final String content,
                                                final String sn,
                                                final Date sendTime,
                                                final int status) {
        final SendMessageRecord sendRecord = new SendMessageRecord();
        sendRecord.setUid(uid);
        sendRecord.setUserType(userType.getId());
        sendRecord.setMobile(mobile);
        sendRecord.setContent(content);
        sendRecord.setMessageTemplateId(messageTemplate.getId());
        sendRecord.setSn(sn);
        sendRecord.setSendTime(sendTime);
        sendRecord.setStatus(status);
        this.sendMessageRecordDao.insert(sendRecord);
        return sendRecord;
    }
}
