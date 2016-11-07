package com.jkm.helper.notifier;

import com.jkm.service.notifier.SendMessageService;
import com.jkm.util.reactor.AbstractTaskProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created on 16/8/9.
 *
 * @author hutao
 * @version 1.0
 */
@Component
@Slf4j
public class SendMsgEventProcessor extends AbstractTaskProcessor<SendMsgEvent> {
    @Autowired
    private SendMessageService sendMessageService;

    @Override
    protected void process(final SendMsgEvent msgEvent) {
        switch (msgEvent.getSendType()) {
            case INSTANT:
                log.debug("处理发送短信事件");
                this.sendMessageService.sendInstantMessage(msgEvent.getSendMessageParamsList());
            case TIMED:
                log.debug("处理定时发送短信事件");
                this.sendMessageService.sendTimedMessage(msgEvent.getSendMessageParamsList());
        }
    }
}
