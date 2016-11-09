package com.jkm.util.mq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.jkm.util.BeanUtils;
import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class MqProducer {
    public static void sendMessage(JSONObject requestData,String mqType,long delayTime){
        ProducerBean producer = (ProducerBean) BeanUtils.getBean("producer");
        try {
            Message message = new Message(MqConfig.TOPIC, mqType, requestData.toString().getBytes("UTF8"));
            message.setStartDeliverTime(System.currentTimeMillis() + delayTime);//毫秒
            producer.send(message);
        } catch (UnsupportedEncodingException e) {
            System.out.print("消息发送失败"+e.getMessage());
        }
    }
}
