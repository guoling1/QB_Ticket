/**
 * Copyright (C) 2010-2016 Alibaba Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jkm.controller.common;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.google.common.base.Optional;
import com.jkm.entity.OrderForm;
import com.jkm.entity.fusion.QueryQuickPayData;
import com.jkm.entity.fusion.QueryRefundData;
import com.jkm.service.AuthenService;
import com.jkm.service.OrderFormService;
import com.jkm.service.TicketService;
import com.jkm.util.SnGenerator;
import com.jkm.util.mq.MqConfig;
import com.jkm.util.mq.MqProducer;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * MQ消息处理类
 */
public class MessageListenerOfTicketImpl implements MessageListener {
    @Autowired
    private AuthenService authenService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private OrderFormService orderFormService;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        try {
//            System.out.println(new Date() + " Receive message, Topic is:" +
//                    message.getTopic() + ", MsgId is:" + message.getMsgID()+",body is:"+new String(message.getBody(),"UTF-8")+" tags is:"
//                    +message.getTag());
            String body = new String(message.getBody(),"UTF-8");
            JSONObject jo = JSONObject.fromObject(body);
            this.orderFormService.handleExpiredOrderForm(jo.getLong("orderFormId"));

        } catch (UnsupportedEncodingException e) {
        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }
}
