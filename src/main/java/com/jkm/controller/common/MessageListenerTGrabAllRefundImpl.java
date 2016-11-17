package com.jkm.controller.common;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.jkm.entity.OrderFormRefundExceptionRecord;
import com.jkm.entity.fusion.QueryRefundData;
import com.jkm.service.AuthenService;
import com.jkm.service.OrderFormRefundExceptionRecordService;
import com.jkm.service.TicketService;
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
 * Created by yuxiang on 2016-11-11.
 * 处理抢票单全额退款的消息
 */
public class MessageListenerTGrabAllRefundImpl implements MessageListener{

    private static Logger log = Logger.getLogger(MessageListenerTGrabAllRefundImpl.class);
    @Autowired
    private AuthenService authenService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private OrderFormRefundExceptionRecordService orderFormRefundExceptionRecordService;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        try {
            final String body = new String(message.getBody(),"UTF-8");
            final JSONObject jo = JSONObject.fromObject(body);
            log.info("消费[抢票单[" + jo.getLong("grabTicketFormId") + "]退款在请求中的消息]");

            final QueryRefundData queryRefundData = new QueryRefundData();
            queryRefundData.setReqSn(SnGenerator.generate());
            queryRefundData.setQuerySn(jo.getString("reqSn"));
            queryRefundData.setQueryDate(jo.getString("reqSn").substring(0, 8));
            final Map<String, Object> resultMap =  authenService.queryRefund(queryRefundData);
            if("0000".equals(resultMap.get("retCode").toString())){
                if("S".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//成功
                    this.ticketService.handleGrabAllRefundResult(jo.getLong("grabTicketFormId"),true, "退款成功");
                } else if ("U".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//处理中 再次发送请求
                    this.handleRetrySendMq(jo);
                } else if ("F".equals(((JSONObject)resultMap.get("retData")).getString("orderStatus"))){//失败
                    this.ticketService.handleGrabAllRefundResult(jo.getLong("grabTicketFormId"),false, "退款失败");
                }
            } else if ("-1000".equals(resultMap.get("retCode").toString())) {
                this.handleRetrySendMq(jo);
            }
        } catch (UnsupportedEncodingException e) {
            log.info("消费[抢票差价退款在请求中的消息]异常", e);
        }
        return Action.CommitMessage;
    }

    private void handleRetrySendMq(final JSONObject jo) {
        log.info("抢票单[" + jo.getLong("grabTicketFormId") + "]重发退款消费消息");
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
            this.recordExceptionOrderFormRefund(jo);
            this.ticketService.handleGrabAllRefundResult(jo.getLong("grabTicketFormId"), false, "退款失败");
        }
    }


    /**
     * 记录异常的代购订单退款
     */
    private void recordExceptionOrderFormRefund(final JSONObject jo) {
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
