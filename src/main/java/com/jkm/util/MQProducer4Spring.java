package com.jkm.util;


import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.jkm.entity.fusion.QueryRefundData;
import com.jkm.util.mq.MqConfig;
import net.sf.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * MQ 使用Spring发送普通消息
 */
public class MQProducer4Spring {

    public static void main(String[] args) throws UnsupportedEncodingException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("producer.xml");
        ProducerBean producer = (ProducerBean) ctx.getBean("producers");
        System.out.println("Producer Started");
        JSONObject mqJo = new JSONObject();


            mqJo.put("reqSn","11111");
            Message message = new Message(MqConfig.TOPIC, MqConfig.SINGLE_REFUND_QUERY, mqJo.toString().getBytes("UTF8"));
            SendResult sendResult = producer.send(message);
            if (sendResult != null) {
                System.out.println(new Date() + " Send mq message success! Topic is:" + MqConfig.TOPIC + "msgId is: " + sendResult.getMessageId());
            }

    }


}
