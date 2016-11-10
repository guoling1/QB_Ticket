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
public class MessageListenerOfRefundImpl implements MessageListener {
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


            System.out.println("msgId ====="+message.getMsgID());
            String body = new String(message.getBody(),"UTF-8");
            JSONObject jo = JSONObject.fromObject(body);
            QueryRefundData queryRefundData = new QueryRefundData();
            queryRefundData.setReqSn(SnGenerator.generate());
            queryRefundData.setQuerySn(jo.getString("reqSn"));
            Map<String, Object> resultMap =  authenService.queryRefund(queryRefundData);
            if("0000".equals(resultMap.get("retCode").toString())){
                Optional<OrderForm> orderFormOptional = orderFormService.selectByReqSn(jo.getString("reqSn"));
                if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                    ticketService.handleCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),true);
                }else if("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                    MqProducer.sendMessage(jo,MqConfig.SINGLE_REFUND_QUERY,10000);//再次发请求
                }else if("N".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//待支付

                }else if("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                    ticketService.handleCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                }
            }else if("-1000".equals(resultMap.get("retCode").toString())){
                MqProducer.sendMessage(jo,MqConfig.SINGLE_REFUND_QUERY,10000);//再次发请求
            }
        } catch (UnsupportedEncodingException e) {
        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }
}
