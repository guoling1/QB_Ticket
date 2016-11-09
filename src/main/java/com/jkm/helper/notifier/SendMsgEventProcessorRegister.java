package com.jkm.helper.notifier;

import com.jkm.util.reactor.ProcessorRegister;
import com.jkm.util.reactor.TaskReactor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created on 16/8/7.
 *
 * @author hutao
 * @version 1.0
 */
@Component
public class SendMsgEventProcessorRegister implements ProcessorRegister {
    private static Logger log = Logger.getLogger(SendMsgEventProcessorRegister.class);

    @Autowired
    private SendMsgEventProcessor sendMsgEventProcessor;

    @Override
    public void register(final TaskReactor taskReactor) {
        log.info("注册短信事件[SendMsgEvent]处理器[SendMsgEventProcessor]");
        taskReactor.addEventProcessor(SendMsgEvent.class, this.sendMsgEventProcessor);
    }
}
