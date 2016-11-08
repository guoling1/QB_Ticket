package com.jkm.helper.notifier;

import com.jkm.service.notifier.SendMessageService;
import com.jkm.util.reactor.AbstractTaskProcessor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created on 16/8/9.
 *
 * @author hutao
 * @version 1.0
 */
@Component
public class SendMsgEventProcessor extends AbstractTaskProcessor<SendMsgEvent> {
    private static Logger log = Logger.getLogger(SendMsgEventProcessor.class);

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
