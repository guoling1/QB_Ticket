package com.jkm.service.notifier.impl;

import com.google.common.base.Preconditions;
import com.jkm.dao.notifier.MessageTemplateDao;
import com.jkm.dao.notifier.SendMessageRecordDao;
import com.jkm.entity.notifier.SendMessageRecord;
import com.jkm.entity.notifier.SmsTemplate;
import com.jkm.enums.notifier.EnumUserType;
import com.jkm.exception.NoticeSendException;
import com.jkm.helper.notifier.NotifierConstants;
import com.jkm.helper.notifier.SendMessageParams;
import com.jkm.service.notifier.SendMessageService;
import com.jkm.util.VelocityStringTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Created by konglingxin on 15/10/9.
 */

@Service
public class SendMessageServiceImpl implements SendMessageService {
    private static Logger log = Logger.getLogger(SendMessageServiceImpl.class);

    @Autowired
    private SendMessageRecordDao sendMessageRecordDao;

    @Autowired
    private MessageTemplateDao messageTemplateDao;

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
            return sendRealMessage(params.getUid(), params.getUserType(),
                    params.getMobile(), params.getData(), messageTemplate);
        }

        private void checkSendMessageParams(final SendMessageParams params) {
            final Pair<Boolean, String> checkResult = params.checkParamsCorrect();
            Preconditions.checkArgument(checkResult.getLeft(), checkResult.getRight());
        }


        /**
         * 调用第三方接口发送短信
         *
         * @param uid
         * @param userType
         *@param mobile
         * @param data
         * @param messageTemplate    @return
         */
        private long sendRealMessage(final String uid,
                                     final EnumUserType userType,
                                     final String mobile,
                                     final Map data,
                                     final SmsTemplate messageTemplate) {

            final String content = VelocityStringTemplate.process(messageTemplate.getMessageTemplate(), data);

            final String sn;
            final String url = generateUrl(mobile, content);
            log.info("uid[" + uid +"]" + "发送短信内容[" + content + "]");
            try {
                sn = Request.Get(url).execute().returnContent().asString();
            } catch (Exception e) {
                log.error("用户[" + uid + "]发送短信失败", e);
                throw new NoticeSendException("短信消息发送失败，失败原因:" + e.getMessage(), e);
            }

            final SendMessageRecord sendMessageRecord = recordSendMessage(uid, userType, mobile, messageTemplate, content, sn, 1);
            return sendMessageRecord.getId();
        }

        /**
         * 记录发送消息内容
         *
         * @param uid
         * @param userType
         *@param mobile
         * @param messageTemplate
         * @param content
         * @param sn     @return
         */
        private SendMessageRecord recordSendMessage(final String uid,
                                                    final EnumUserType userType,
                                                    final String mobile,
                                                    final SmsTemplate messageTemplate,
                                                    final String content,
                                                    final String sn,
                                                    final int status) {
            final SendMessageRecord sendRecord = new SendMessageRecord();
            sendRecord.setUid(uid);
            sendRecord.setUserType(userType.getId());
            sendRecord.setMobile(mobile);
            sendRecord.setContent(content);
            sendRecord.setMessageTemplateId(messageTemplate.getId());
            sendRecord.setSn(sn);
            sendRecord.setStatus(status);
            sendMessageRecordDao.insert(sendRecord);
            return sendRecord;
        }

        /**
         * 生成短信发送的url
         *
         * @param mobile
         * @param content
         * @return
         */
        private String generateUrl(final String mobile, final String content) {
            final String sn = NotifierConstants.getNotifierConfig().sn();
            final String pwd = NotifierConstants.getNotifierConfig().password();


            final Map<String, String> params = new HashMap<>();
            params.put("sn", sn);
            params.put("pwd", pwd);
            params.put("mobile", mobile);
            params.put("content", content);

            final StringBuilder urlBuilder = new StringBuilder("http://sdk2.entinfo.cn:8061/mdsmssend.ashx?");
            for (final Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
            }
            return urlBuilder.substring(0, urlBuilder.length() - 1);
        }
}
