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
import com.jkm.entity.BindCard;
import com.jkm.entity.OrderForm;
import com.jkm.entity.fusion.QueryQuickPayData;
import com.jkm.entity.fusion.QueryRefundData;
import com.jkm.entity.fusion.SingleRefundData;
import com.jkm.service.*;
import com.jkm.util.DateFormatUtil;
import com.jkm.util.SnGenerator;
import com.jkm.util.mq.MqConfig;
import com.jkm.util.mq.MqProducer;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * MQ消息处理类
 */
public class MessageListenerImpl implements MessageListener {
    @Autowired
    private AuthenService authenService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private OrderFormService orderFormService;
    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        System.out.println(new Date() + " Receive message, Topic is:" +
                message.getTopic() + ", MsgId is:" + message.getMsgID());
        try {
            if(MqConfig.FAST_PAY_QUERY.equals(message.getTag())){//流水单支付结果查询
                String body = new String(message.getBody(),"UTF-8");
                JSONObject jo = JSONObject.fromObject(body);
                QueryQuickPayData queryQuickPayData = new QueryQuickPayData();
                queryQuickPayData.setReqSn(SnGenerator.generate());
                queryQuickPayData.setMercOrdNo(jo.getString("reqSn"));
                queryQuickPayData.setMercOrdDt(jo.getString("dt"));
                Map<String, Object> resultMap =  authenService.queryQuickPay(queryQuickPayData);
                if("0000".equals(resultMap.get("retCode").toString())){
                    if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                        ticketService.handleCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),true);
                    }else if("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                        if(jo.getInt("sendCount")<5){
                            jo.put("sendCount",jo.getInt("sendCount")+1);
                            MqProducer.sendMessage(jo,MqConfig.FAST_PAY_QUERY,10000);//再次发请求
                        }else if(jo.getInt("sendCount")>5&&jo.getInt("sendCount")<15){
                            jo.put("sendCount",jo.getInt("sendCount")+1);
                            MqProducer.sendMessage(jo,MqConfig.FAST_PAY_QUERY,60000);//再次发请求
                        }else{
                            ticketService.handleCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                        }
                    }else if("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                        ticketService.handleCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                    }
                }else if("-1000".equals(resultMap.get("retCode").toString())){//连接超时
                    if(jo.getInt("sendCount")<5){
                        jo.put("sendCount",jo.getInt("sendCount")+1);
                        MqProducer.sendMessage(jo,MqConfig.FAST_PAY_QUERY,10000);//再次发请求
                    }else if(jo.getInt("sendCount")>5&&jo.getInt("sendCount")<15){
                        jo.put("sendCount",jo.getInt("sendCount")+1);
                        MqProducer.sendMessage(jo,MqConfig.FAST_PAY_QUERY,60000);//再次发请求
                    }else{
                        ticketService.handleCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                    }
                }
            }else if(MqConfig.FAST_PAY_GRAB_QUERY.equals(message.getTag())){//快捷支付抢票单查询
                String body = new String(message.getBody(),"UTF-8");
                JSONObject jo = JSONObject.fromObject(body);
                QueryQuickPayData queryQuickPayData = new QueryQuickPayData();
                queryQuickPayData.setReqSn(SnGenerator.generate());
                queryQuickPayData.setMercOrdNo(jo.getString("reqSn"));
                queryQuickPayData.setMercOrdDt(jo.getString("dt"));
                Map<String, Object> resultMap =  authenService.queryQuickPay(queryQuickPayData);
                if("0000".equals(resultMap.get("retCode").toString())){
                    if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                        ticketService.handleGrabCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),true);
                    }else if("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                        if(jo.getInt("sendCount")<30) {
                            jo.put("sendCount", jo.getInt("sendCount") + 1);
                            MqProducer.sendMessage(jo, MqConfig.FAST_PAY_GRAB_QUERY, 10000);//再次发请求
                        }else if(jo.getInt("sendCount")>5&&jo.getInt("sendCount")<15){
                            jo.put("sendCount",jo.getInt("sendCount")+1);
                            MqProducer.sendMessage(jo,MqConfig.FAST_PAY_GRAB_QUERY,60000);//再次发请求
                        }else{
                            ticketService.handleGrabCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                        }
                    }else if("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                        ticketService.handleGrabCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                    }
                }else if("-1000".equals(resultMap.get("retCode").toString())){//连接超时
                    if(jo.getInt("sendCount")<5) {
                        jo.put("sendCount",jo.getInt("sendCount")+1);
                        MqProducer.sendMessage(jo,MqConfig.FAST_PAY_GRAB_QUERY,10000);//再次发请求
                    }else if(jo.getInt("sendCount")>5&&jo.getInt("sendCount")<15){
                        jo.put("sendCount",jo.getInt("sendCount")+1);
                        MqProducer.sendMessage(jo,MqConfig.FAST_PAY_GRAB_QUERY,60000);//再次发请求
                    }else{
                        ticketService.handleGrabCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {

        } catch (Exception e) {

        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }
}
