package com.jkm.helper.notifier;

/**
 * Created on 16/8/7.
 *
 * @author hutao
 * @version 1.0
 */

import com.jkm.enums.notifier.EnumSendType;
import com.jkm.util.reactor.TaskEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class SendMsgEvent implements TaskEvent {
    private EnumSendType sendType;
    @Singular("addSendMessageParam")
    private List<SendMessageParams> sendMessageParamsList;
}
