package com.jkm.service.notifier.impl;

import com.jkm.dao.notifier.MessageTemplateDao;
import com.jkm.entity.notifier.SmsTemplate;
import com.jkm.enums.notifier.EnumNoticeType;
import com.jkm.service.notifier.MessageTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by konglingxin on 15/11/4.
 */

@Slf4j
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {
    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Override
    public long addTemplate(final SmsTemplate messageTemplate) {

        final SmsTemplate template = this.messageTemplateDao.getTemplateByType(messageTemplate.getNoticeType());
        if (template != null) {
            this.messageTemplateDao.modifyMessageTemplate(template.getId(), messageTemplate.getMessageTemplate());
            return template.getId();
        }
        this.messageTemplateDao.addTemplate(messageTemplate);
        return messageTemplate.getId();
    }

    @Override
    public List<SmsTemplate> getMessageTemplate() {

        return this.messageTemplateDao.getMessageTemplate();
    }

    @Override
    public int modifyMessageTemplate(final long messageTemplateId, final String smsTemplate) {

        return this.messageTemplateDao.modifyMessageTemplate(messageTemplateId, smsTemplate);
    }

    @Override
    public SmsTemplate getTemplateByType(final EnumNoticeType type) {

        return this.messageTemplateDao.getTemplateByType(type.getId());
    }

    @Override
    public SmsTemplate getMessageTemplateById(final long id) {
        return this.messageTemplateDao.getMessageTemplateById(id);
    }
}
