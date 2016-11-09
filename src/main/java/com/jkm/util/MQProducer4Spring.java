package com.jkm.util;


import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.jkm.util.mq.MqConfig;
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
        ProducerBean producer = (ProducerBean) ctx.getBean("producer");
        System.out.println("Producer Started");

        for (int i = 0; i < 10; i++) {
            Message message = new Message(MqConfig.TOPIC, MqConfig.SINGLE_REFUND_QUERY, "邢留杰是个大好人".getBytes("UTF8"));
            SendResult sendResult = producer.send(message);
            if (sendResult != null) {
                System.out.println(new Date() + " Send mq message success! Topic is:" + MqConfig.TOPIC + "msgId is: " + sendResult.getMessageId());
            }
        }
    }


}
