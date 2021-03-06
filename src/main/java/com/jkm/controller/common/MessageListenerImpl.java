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
import com.jkm.entity.OrderFormRefundExceptionRecord;
import com.jkm.entity.fusion.QueryQuickPayData;
import com.jkm.entity.fusion.QueryRefundData;
import com.jkm.enums.EnumPayResult;
import com.jkm.enums.EnumRefundResult;
import com.jkm.service.*;
import com.jkm.util.SnGenerator;
import com.jkm.util.mq.MqConfig;
import com.jkm.util.mq.MqProducer;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * MQ消息处理类
 */
public class MessageListenerImpl implements MessageListener {
    private static Logger log = Logger.getLogger(MessageListenerImpl.class);
    @Autowired
    private AuthenService authenService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private OrderFormService orderFormService;
    @Autowired
    private GrabTicketFormService grabTicketFormService;
    @Autowired
    private OrderFormRefundExceptionRecordService orderFormRefundExceptionRecordService;
    @Autowired
    private PaySequenceService paySequenceService;
    @Autowired
    private RefundSequenceService refundSequenceService;
    @Autowired
    private PayExceptionRecordService payExceptionRecordService;
    @Autowired
    private RefundExceptionRecordService refundExceptionRecordService;
    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        //快捷支付结果查询 S-成功，U-处理中，N-待支付，F-失败
        //单笔退款结果查询 S-成功，U-已受理，S1-等待通道确认结果，F-失败
        try {
            String body = new String(message.getBody(),"UTF-8");
            JSONObject jo = JSONObject.fromObject(body);
            if(MqConfig.FAST_PAY_QUERY.equals(message.getTag())){//流水单支付结果查询
                log.info("流水号为【"+jo.getString("reqSn")+"】的fastPayQuery支付结果");
                QueryQuickPayData queryQuickPayData = new QueryQuickPayData();
                queryQuickPayData.setReqSn(SnGenerator.generate());
                queryQuickPayData.setMercOrdNo(jo.getString("reqSn"));
                queryQuickPayData.setMercOrdDt(jo.getString("dt"));
                Map<String, Object> resultMap =  authenService.queryQuickPay(queryQuickPayData);
                if("0000".equals(resultMap.get("retCode").toString())){
                    if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                        paySequenceService.updatePayResultByReqSn(EnumPayResult.SUCCESS.getId(),jo.getString("reqSn"));
                        ticketService.handleCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),true);
                    }else if("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                        if(jo.getInt("sendCount")<12){
                            jo.put("sendCount",jo.getInt("sendCount")+1);
                            MqProducer.sendMessage(jo,MqConfig.FAST_PAY_QUERY,5000);//再次发请求
                        }else if(jo.getInt("sendCount")>12&&jo.getInt("sendCount")<25){
                            jo.put("sendCount",jo.getInt("sendCount")+1);
                            MqProducer.sendMessage(jo,MqConfig.FAST_PAY_QUERY,60000);//再次发请求
                        }else{
                            paySequenceService.updatePayResultByReqSn(EnumPayResult.HANDLE.getId(),jo.getString("reqSn"));
                            payExceptionRecordService.insertByCondition(jo.getLong("orderId")+"",jo.getString("reqSn"),EnumRefundResult.HANDLE.getId());
                            ticketService.handleCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                        }
                    }else if("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                        paySequenceService.updatePayResultByReqSn(EnumPayResult.FAIL.getId(),jo.getString("reqSn"));
                        ticketService.handleCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                    }
                }else if("-1000".equals(resultMap.get("retCode").toString())){//连接超时
                    if(jo.getInt("sendCount")<5){
                        jo.put("sendCount",jo.getInt("sendCount")+1);
                        MqProducer.sendMessage(jo,MqConfig.FAST_PAY_QUERY,5000);//再次发请求
                    }else if(jo.getInt("sendCount")>5&&jo.getInt("sendCount")<25){
                        jo.put("sendCount",jo.getInt("sendCount")+1);
                        MqProducer.sendMessage(jo,MqConfig.FAST_PAY_QUERY,60000);//再次发请求
                    }else{
                        paySequenceService.updatePayResultByReqSn(EnumPayResult.TIMEOUT.getId(),jo.getString("reqSn"));
                        payExceptionRecordService.insertByCondition(jo.getLong("orderId")+"",jo.getString("reqSn"),EnumRefundResult.EXCEPTION.getId());
                        ticketService.handleCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                    }
                }
            }else if(MqConfig.FAST_PAY_GRAB_QUERY.equals(message.getTag())){//快捷支付抢票单查询
                log.info("流水号为【"+jo.getString("reqSn")+"】的fastPayGrabQuery支付结果");
                QueryQuickPayData queryQuickPayData = new QueryQuickPayData();
                queryQuickPayData.setReqSn(SnGenerator.generate());
                queryQuickPayData.setMercOrdNo(jo.getString("reqSn"));
                queryQuickPayData.setMercOrdDt(jo.getString("dt"));
                Map<String, Object> resultMap =  authenService.queryQuickPay(queryQuickPayData);
                if("0000".equals(resultMap.get("retCode").toString())){
                    if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                        paySequenceService.updatePayResultByReqSn(EnumPayResult.SUCCESS.getId(),jo.getString("reqSn"));
                        ticketService.handleGrabCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),true);
                    }else if("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                        if(jo.getInt("sendCount")<30) {
                            jo.put("sendCount", jo.getInt("sendCount") + 1);
                            MqProducer.sendMessage(jo, MqConfig.FAST_PAY_GRAB_QUERY, 10000);//再次发请求
                        }else if(jo.getInt("sendCount")>5&&jo.getInt("sendCount")<15){
                            jo.put("sendCount",jo.getInt("sendCount")+1);
                            MqProducer.sendMessage(jo,MqConfig.FAST_PAY_GRAB_QUERY,60000);//再次发请求
                        }else{
                            paySequenceService.updatePayResultByReqSn(EnumPayResult.HANDLE.getId(),jo.getString("reqSn"));
                            payExceptionRecordService.insertByCondition(jo.getLong("orderId")+"",jo.getString("reqSn"),EnumRefundResult.HANDLE.getId());
                            ticketService.handleGrabCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                        }
                    }else if("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                        paySequenceService.updatePayResultByReqSn(EnumPayResult.FAIL.getId(),jo.getString("reqSn"));
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
                        paySequenceService.updatePayResultByReqSn(EnumPayResult.TIMEOUT.getId(),jo.getString("reqSn"));
                        payExceptionRecordService.insertByCondition(jo.getLong("orderId")+"",jo.getString("reqSn"),EnumRefundResult.EXCEPTION.getId());
                        ticketService.handleGrabCustomerPayMsg(jo.getLong("orderId"),jo.getString("reqSn"),false);
                    }
                }
            }else if((MqConfig.TICKET_CANCEL_EXPIRED_ORDER).equals(message.getTag())){
                log.info("消费[过了支付时间的订单[" + jo.getLong("orderFormId") + "]]");
                this.orderFormService.handleExpiredOrderForm(jo.getLong("orderFormId"));
            }else if(MqConfig.TICKET_CANCEL_EXPIRED_GRAB_ORDER.equals(message.getTag())){
                log.info("消费[过了支付时间的抢票订单的消息[" + jo.getLong("grabTicketFormId") + "]]");
                this.grabTicketFormService.handleExpiredOGrabForm(jo.getLong("grabTicketFormId"));
            }else if(MqConfig.NO_PACKAGE_WAIT_REFUND.equals(message.getTag())){
                log.info("消费[抢票订单没买套餐自动退款的消息[" + jo.getLong("grabTicketFormId") + "]]");
                this.grabTicketFormService.handleNoPackageWaitRefund(jo.getLong("grabTicketFormId"));
            }else if (MqConfig.GRAB_FORM_FAIL_WAIT_REFUND.equals(message.getTag())) {
                log.info("消费[抢票下单失败的消息[" + jo.getLong("grabTicketFormId") + "]]");
                this.grabTicketFormService.handleNoPackageWaitRefund(jo.getLong("grabTicketFormId"));
            } else if (MqConfig.TICKET_HANDLE_REFUND_ORDER_RESULT.equals(message.getTag())) {
                log.info("消费[订单[" + jo.getLong("orderFormId") + "]退款在请求中的消息]");
                final QueryRefundData queryRefundData = new QueryRefundData();
                queryRefundData.setReqSn(SnGenerator.generate());
                queryRefundData.setQuerySn(jo.getString("reqSn"));
                queryRefundData.setQueryDate(jo.getString("reqSn").substring(0, 8));
                final Map<String, Object> resultMap =  authenService.queryRefund(queryRefundData);
                if("0000".equals(resultMap.get("retCode").toString())){
                    if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                        refundSequenceService.updateRefundResultByReqSn(EnumRefundResult.SUCCESS.getId(),jo.getString("reqSn"));
                        this.ticketService.handleOrderFormRefundResult(jo.getLong("orderFormId"), true, "退款成功");
                    } else if ("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))||"S1".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                        this.handleRetrySendMq(jo,EnumRefundResult.HANDLE.getId());
                    } else if ("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                        refundSequenceService.updateRefundResultByReqSn(EnumRefundResult.FAIL.getId(),jo.getString("reqSn"));
                        this.ticketService.handleOrderFormRefundResult(jo.getLong("orderFormId"), false, "退款失败");
                    }
                } else if ("-1000".equals(resultMap.get("retCode").toString())) {
                    this.handleRetrySendMq(jo,EnumRefundResult.EXCEPTION.getId());
                }
            }else if(MqConfig.RETURN_TICKET_REFUND_ING.equals(message.getTag())){
                log.info("消费[小订单[" + jo.getLong("orderFormDetailId") + "]退款在请求中的消息]");
                final QueryRefundData queryRefundData = new QueryRefundData();
                queryRefundData.setReqSn(SnGenerator.generate());
                queryRefundData.setQuerySn(jo.getString("reqSn"));
                queryRefundData.setQueryDate(jo.getString("reqSn").substring(0, 8));
                final Map<String, Object> resultMap =  authenService.queryRefund(queryRefundData);
                if("0000".equals(resultMap.get("retCode").toString())){
                    if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                        refundSequenceService.updateRefundResultByReqSn(EnumRefundResult.SUCCESS.getId(),jo.getString("reqSn"));
                        this.ticketService.handleOrderFormDeatailRefundResult(jo.getLong("orderFormDetailId"), jo.getString("reqToken"),true, "退款成功");
                    } else if ("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))||"S1".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                        this.handleRetrySendDetailMq(jo,EnumRefundResult.HANDLE.getId());
                    } else if ("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                        refundSequenceService.updateRefundResultByReqSn(EnumRefundResult.FAIL.getId(),jo.getString("reqSn"));
                        this.ticketService.handleOrderFormDeatailRefundResult(jo.getLong("orderFormDetailId"), jo.getString("reqToken"),false, "退款失败");
                    }
                } else if ("-1000".equals(resultMap.get("retCode").toString())) {
                    this.handleRetrySendDetailMq(jo,EnumRefundResult.EXCEPTION.getId());
                }
            }else if(MqConfig.GRAB_TICKET_REFUND_PART.equals(message.getTag())){
                log.info("消费[抢票单差价退款[" + jo.getLong("grabTicketFormId") + "]退款在请求中的消息]");

                final QueryRefundData queryRefundData = new QueryRefundData();
                queryRefundData.setReqSn(SnGenerator.generate());
                queryRefundData.setQuerySn(jo.getString("reqSn"));
                queryRefundData.setQueryDate(jo.getString("reqSn").substring(0, 8));
                final Map<String, Object> resultMap =  authenService.queryRefund(queryRefundData);
                if("0000".equals(resultMap.get("retCode").toString())){
                    if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                        refundSequenceService.updateRefundResultByReqSn(EnumRefundResult.SUCCESS.getId(),jo.getString("reqSn"));
                        this.ticketService.handleGrabPartRefundResult(jo.getLong("grabTicketFormId"),true, "退款成功");
                    } else if ("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))||"S1".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                        this.handleRetrySendGrabPartMq(jo,EnumRefundResult.HANDLE.getId());
                    } else if ("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                        refundSequenceService.updateRefundResultByReqSn(EnumRefundResult.FAIL.getId(),jo.getString("reqSn"));
                        this.ticketService.handleGrabPartRefundResult(jo.getLong("grabTicketFormId"),false, "退款失败");
                    }
                } else if ("-1000".equals(resultMap.get("retCode").toString())) {
                    this.handleRetrySendGrabPartMq(jo,EnumRefundResult.EXCEPTION.getId());
                }
            }else if(MqConfig.GRAB_TICKET_REFUND_ALL.equals(message.getTag())){
                log.info("消费[抢票失败全额退款[" + jo.getLong("grabTicketFormId") + "]退款在请求中的消息]");

                final QueryRefundData queryRefundData = new QueryRefundData();
                queryRefundData.setReqSn(SnGenerator.generate());
                queryRefundData.setQuerySn(jo.getString("reqSn"));
                queryRefundData.setQueryDate(jo.getString("reqSn").substring(0, 8));
                final Map<String, Object> resultMap =  authenService.queryRefund(queryRefundData);
                if("0000".equals(resultMap.get("retCode").toString())){
                    if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                        refundSequenceService.updateRefundResultByReqSn(EnumRefundResult.SUCCESS.getId(),jo.getString("reqSn"));
                        this.ticketService.handleGrabAllRefundResult(jo.getLong("grabTicketFormId"),true, "退款成功");
                    } else if ("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))||"S1".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                        this.handleRetrySendRepeatMq(jo,EnumRefundResult.HANDLE.getId());
                    } else if ("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                        refundSequenceService.updateRefundResultByReqSn(EnumRefundResult.FAIL.getId(),jo.getString("reqSn"));
                        this.ticketService.handleGrabAllRefundResult(jo.getLong("grabTicketFormId"),false, "退款失败");
                    }
                } else if ("-1000".equals(resultMap.get("retCode").toString())) {
                    this.handleRetrySendRepeatMq(jo,EnumRefundResult.EXCEPTION.getId());
                }
            }else if(MqConfig.CANCEL_GRAB_TICKET_REFUND_ALL.equals(message.getTag())){
                log.info("消费[取消抢票单全额退款[" + jo.getLong("grabTicketFormId") + "]退款在请求中的消息]");

                final QueryRefundData queryRefundData = new QueryRefundData();
                queryRefundData.setReqSn(SnGenerator.generate());
                queryRefundData.setQuerySn(jo.getString("reqSn"));
                queryRefundData.setQueryDate(jo.getString("reqSn").substring(0, 8));
                final Map<String, Object> resultMap =  authenService.queryRefund(queryRefundData);
                if("0000".equals(resultMap.get("retCode").toString())){
                    if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                        refundSequenceService.updateRefundResultByReqSn(EnumRefundResult.SUCCESS.getId(),jo.getString("reqSn"));
                        this.ticketService.handleCancelGrabRefundResult(jo.getLong("grabTicketFormId"),true, "退款成功");
                    } else if ("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))||"S1".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                        this.handleRetrySendCancelMq(jo,EnumRefundResult.HANDLE.getId());
                    } else if ("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                        refundSequenceService.updateRefundResultByReqSn(EnumRefundResult.FAIL.getId(),jo.getString("reqSn"));
                        this.ticketService.handleCancelGrabRefundResult(jo.getLong("grabTicketFormId"),false, "退款失败");
                    }
                } else if ("-1000".equals(resultMap.get("retCode").toString())) {
                    this.handleRetrySendCancelMq(jo,EnumRefundResult.EXCEPTION.getId());
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }

    private void handleRetrySendMq(final JSONObject jo,String refundResult) {
        log.info("订单[" + jo.getLong("orderFormId") + "]重发退款消费消息");
        if (jo.getInt("sendCount") < 5) {
            jo.put("sendCount", jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.TICKET_HANDLE_REFUND_ORDER_RESULT, 2000);//再次发请求
        } else if (jo.getInt("sendCount") >= 5 && jo.getInt("sendCount") < 10) {
            jo.put("sendCount",jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.TICKET_HANDLE_REFUND_ORDER_RESULT, 60 * 1000);//再次发请求
        }  else if (jo.getInt("sendCount") >= 10 && jo.getInt("sendCount") < 15) {
            jo.put("sendCount",jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.TICKET_HANDLE_REFUND_ORDER_RESULT, 5 * 60 * 1000);//再次发请求
        } else {
            refundSequenceService.updateRefundResultByReqSn(refundResult,jo.getString("reqSn"));
            refundExceptionRecordService.insertByCondition(jo.getLong("orderFormId")+"",jo.getString("reqSn"),refundResult);
            this.recordExceptionOrderFormRefund(jo);
            this.ticketService.handleOrderFormRefundResult(jo.getLong("orderFormId"), false, "退款失败");
        }
    }


    /**
     * 记录异常的代购订单退款
     */
    private void recordExceptionOrderFormRefund(final JSONObject jo) {
        log.info("订单[" + jo.getLong("orderFormId") + "]退款失败，记录代购订单退款异常记录");
        final OrderFormRefundExceptionRecord record = new OrderFormRefundExceptionRecord();
        record.setOrderFormId(jo.getLong("orderFormId"));
        record.setGrabOrderFormId(0);
        record.setPaymentSn(jo.getString("reqSn"));
        record.setRefundAmount(new BigDecimal(jo.getString("refundAmount")));
        record.setStatus(0);
        record.setRemark("代购订单退款异常");
        this.orderFormRefundExceptionRecordService.insert(record);
    }

    private void handleRetrySendDetailMq(final JSONObject jo,String refundResult) {
        log.info("订单[" + jo.getLong("orderFormDetailId") + "]重发退款消费消息");
        if (jo.getInt("sendCount") < 5) {
            jo.put("sendCount", jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.RETURN_TICKET_REFUND_ING, 2000);//再次发请求
        } else if (jo.getInt("sendCount") >= 5 && jo.getInt("sendCount") < 10) {
            jo.put("sendCount",jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.RETURN_TICKET_REFUND_ING, 60 * 1000);//再次发请求
        }  else if (jo.getInt("sendCount") >= 10 && jo.getInt("sendCount") < 15) {
            jo.put("sendCount",jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.RETURN_TICKET_REFUND_ING, 5 * 60 * 1000);//再次发请求
        } else {
            refundSequenceService.updateRefundResultByReqSn(refundResult,jo.getString("reqSn"));
            refundExceptionRecordService.insertByCondition(jo.getLong("orderFormDetailId")+"",jo.getString("reqSn"),refundResult);
            this.recordExceptionOrderForDetailmRefund(jo);
            this.ticketService.handleOrderFormDeatailRefundResult(jo.getLong("orderFormDetailId"),jo.getString("reqToken"), false, "退款失败");
        }
    }


    /**
     * 记录异常的代购订单退款
     */
    private void recordExceptionOrderForDetailmRefund(final JSONObject jo) {
        log.info("出票订单[" + jo.getLong("orderFormDetailId") + "]退款失败，记录订单退款异常记录");
        final OrderFormRefundExceptionRecord record = new OrderFormRefundExceptionRecord();
        record.setOrderFormDetailId(jo.getLong("orderFormDetailId"));
        record.setPaymentSn(jo.getString("reqSn"));
        record.setRefundAmount(new BigDecimal(jo.getString("refundAmount")));
        record.setStatus(0);
        record.setRemark("出票订单退款异常");
        this.orderFormRefundExceptionRecordService.insert(record);
    }

    /**
     * 差价退款
     */
    private void handleRetrySendGrabPartMq(final JSONObject jo,String refundResult) {
        log.info("消费抢票单差价退款[" + jo.getLong("grabTicketFormId") + "]重发退款消费消息");
        if (jo.getInt("sendCount") < 5) {
            jo.put("sendCount", jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.GRAB_TICKET_REFUND_PART, 2000);//再次发请求
        } else if (jo.getInt("sendCount") >= 5 && jo.getInt("sendCount") < 10) {
            jo.put("sendCount",jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.GRAB_TICKET_REFUND_PART, 60 * 1000);//再次发请求
        }  else if (jo.getInt("sendCount") >= 10 && jo.getInt("sendCount") < 15) {
            jo.put("sendCount",jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.GRAB_TICKET_REFUND_PART, 5 * 60 * 1000);//再次发请求
        } else {
            refundSequenceService.updateRefundResultByReqSn(refundResult,jo.getString("reqSn"));
            refundExceptionRecordService.insertByCondition(jo.getLong("grabTicketFormId")+"",jo.getString("reqSn"),refundResult);
            this.recordExceptionOrderFormGrabRefund(jo);
            this.ticketService.handleGrabPartRefundResult(jo.getLong("grabTicketFormId"), false, "退款失败");
        }
    }


    /**
     * 记录异常的代购订单退款
     */
    private void recordExceptionOrderFormGrabRefund(final JSONObject jo) {
        log.info("抢票订单[" + jo.getLong("grabTicketFormId") + "]退款失败，记录订单退款异常记录");
        final OrderFormRefundExceptionRecord record = new OrderFormRefundExceptionRecord();
        record.setGrabOrderFormId(jo.getLong("grabTicketFormId"));
        record.setPaymentSn(jo.getString("reqSn"));
        record.setRefundAmount(new BigDecimal(jo.getString("refundAmount")));
        record.setStatus(0);
        record.setRemark("抢票订单退款异常");
        this.orderFormRefundExceptionRecordService.insert(record);
    }

    /**
     * 抢票失败全额退款
     * @param jo
     * @param refundResult
     */
    private void handleRetrySendRepeatMq(final JSONObject jo,String refundResult) {
        log.info("抢票单抢票失败全额退款[" + jo.getLong("grabTicketFormId") + "]重发退款消费消息");
        if (jo.getInt("sendCount") < 5) {
            jo.put("sendCount", jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.GRAB_TICKET_REFUND_ALL, 2000);//再次发请求
        } else if (jo.getInt("sendCount") >= 5 && jo.getInt("sendCount") < 10) {
            jo.put("sendCount",jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.GRAB_TICKET_REFUND_ALL, 60 * 1000);//再次发请求
        }  else if (jo.getInt("sendCount") >= 10 && jo.getInt("sendCount") < 15) {
            jo.put("sendCount",jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.GRAB_TICKET_REFUND_ALL, 5 * 60 * 1000);//再次发请求
        } else {
            refundSequenceService.updateRefundResultByReqSn(refundResult,jo.getString("reqSn"));
            refundExceptionRecordService.insertByCondition(jo.getLong("grabTicketFormId")+"",jo.getString("reqSn"),refundResult);
            this.recordExceptionOrderFormRepeatRefund(jo);
            this.ticketService.handleGrabAllRefundResult(jo.getLong("grabTicketFormId"), false, "退款失败");
        }
    }


    /**
     * 记录异常的代购订单退款
     */
    private void recordExceptionOrderFormRepeatRefund(final JSONObject jo) {
        log.info("抢票订单[" + jo.getLong("grabTicketFormId") + "]退款失败，记录订单退款异常记录");
        final OrderFormRefundExceptionRecord record = new OrderFormRefundExceptionRecord();
        record.setGrabOrderFormId(jo.getLong("grabTicketFormId"));
        record.setPaymentSn(jo.getString("reqSn"));
        record.setRefundAmount(new BigDecimal(jo.getString("refundAmount")));
        record.setStatus(0);
        record.setRemark("抢票订单退款异常");
        this.orderFormRefundExceptionRecordService.insert(record);
    }

    /**
     * 取消抢票单全额退款
     * @param jo
     * @param refundResult
     */
    private void handleRetrySendCancelMq(final JSONObject jo,String refundResult) {
        log.info("取消抢票单全额退款[" + jo.getLong("grabTicketFormId") + "]重发退款消费消息");
        if (jo.getInt("sendCount") < 5) {
            jo.put("sendCount", jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.CANCEL_GRAB_TICKET_REFUND_ALL, 2000);//再次发请求
        } else if (jo.getInt("sendCount") >= 5 && jo.getInt("sendCount") < 10) {
            jo.put("sendCount",jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.CANCEL_GRAB_TICKET_REFUND_ALL, 60 * 1000);//再次发请求
        }  else if (jo.getInt("sendCount") >= 10 && jo.getInt("sendCount") < 15) {
            jo.put("sendCount",jo.getInt("sendCount") + 1);
            MqProducer.sendMessage(jo, MqConfig.CANCEL_GRAB_TICKET_REFUND_ALL, 5 * 60 * 1000);//再次发请求
        } else {
            refundSequenceService.updateRefundResultByReqSn(refundResult,jo.getString("reqSn"));
            refundExceptionRecordService.insertByCondition(jo.getLong("grabTicketFormId")+"",jo.getString("reqSn"),refundResult);
            this.recordExceptionOrderFormPartRefund(jo);
            this.ticketService.handleCancelGrabRefundResult(jo.getLong("grabTicketFormId"), false, "退款失败");
        }
    }


    /**
     * 记录异常的代购订单退款
     */
    private void recordExceptionOrderFormPartRefund(final JSONObject jo) {
        log.info("抢票订单[" + jo.getLong("grabTicketFormId") + "]退款失败，记录订单退款异常记录");
        final OrderFormRefundExceptionRecord record = new OrderFormRefundExceptionRecord();
        record.setGrabOrderFormId(jo.getLong("grabTicketFormId"));
        record.setPaymentSn(jo.getString("reqSn"));
        record.setRefundAmount(new BigDecimal(jo.getString("refundAmount")));
        record.setStatus(0);
        record.setRemark("抢票订单退款异常");
        this.orderFormRefundExceptionRecordService.insert(record);
    }
}
