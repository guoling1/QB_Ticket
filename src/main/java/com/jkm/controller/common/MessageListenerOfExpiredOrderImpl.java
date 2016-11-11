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
import com.jkm.service.GrabTicketFormService;
import com.jkm.service.OrderFormService;
import com.jkm.util.mq.MqConfig;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;

/**
 * MQ消息处理类
 */
public class MessageListenerOfExpiredOrderImpl implements MessageListener {

    private static Logger log = Logger.getLogger(MessageListenerOfExpiredOrderImpl.class);

    @Autowired
    private OrderFormService orderFormService;
    @Autowired
    private GrabTicketFormService grabTicketFormService;
    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        try {
            final String body = new String(message.getBody(),"UTF-8");
            final JSONObject jo = JSONObject.fromObject(body);
            log.info("消费[过了支付时间的订单[" + jo.getLong("orderFormId") + "]]");
            this.orderFormService.handleExpiredOrderForm(jo.getLong("orderFormId"));
            if ((MqConfig.TICKET_CANCEL_EXPIRED_ORDER).equals(message.getTag())){  //订单到期取消

                this.orderFormService.handleExpiredOrderForm(jo.getLong("orderFormId"));
            }else if (MqConfig.TICKET_CANCEL_EXPIRED_GRAB_ORDER.equals(message.getTag())) {//取消到期抢票订单

                this.grabTicketFormService.handleExpiredOGrabForm(jo.getLong("grabTicketFormId"));
            }else if (MqConfig.NO_PACKAGE_WAIT_REFUND.equals(message.getTag())) {//抢票订单没买套餐自动退款

                this.grabTicketFormService.handleNoPackageWaitRefund(jo.getLong("grabTicketFormId"));
            }

        } catch (UnsupportedEncodingException e) {
            log.info("消费[过了支付时间的订单]异常", e);
        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }
}
