package com.jkm.helper.notifier;

import com.jkm.util.reactor.ProcessorRegister;
import com.jkm.util.reactor.TaskReactor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created on 16/8/7.
 *
 * @author hutao
 * @version 1.0
 */
@Component
@Slf4j
public class SendMsgEventProcessorRegister implements ProcessorRegister {
    @Autowired
    private SendMsgEventProcessor sendMsgEventProcessor;

    @Override
    public void register(final TaskReactor taskReactor) {
        log.info("注册短信事件[SendMsgEvent]处理器[SendMsgEventProcessor]");
        taskReactor.addEventProcessor(SendMsgEvent.class, this.sendMsgEventProcessor);
    }
}
