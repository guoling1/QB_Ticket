package com.jkm.util.rabbitmq;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;

public class Receiver extends BaseConnector implements Consumer {

    public Receiver(String queueName) throws IOException {
        super(queueName);
        channel.basicConsume(queueName, true,this);
    }

    /**
     * 下面这些方法都是实现Consumer接口的
     **/
    //当消费者注册完成自动调用
    public void handleConsumeOk(String consumerTag) {
        System.out.println("Consumer "+consumerTag +" registered");
    }
    //当消费者接收到消息会自动调用
    public void handleDelivery(String consumerTag, Envelope env,
                               BasicProperties props, byte[] body) throws IOException {
        JSONObject jo = (JSONObject)SerializationUtils.deserialize(body);
        System.out.println("Message ( "
                + "userId : " + jo.getLong("userId")
                + " , sendId : " +jo.getLong("sendId")
                + " ) received.");

    }
    //下面这些方法可以暂时不用理会
    public void handleCancelOk(String consumerTag) {
    }
    public void handleCancel(String consumerTag) throws IOException {
    }
    public void handleShutdownSignal(String consumerTag,
                                     ShutdownSignalException sig) {
    }
    public void handleRecoverOk(String consumerTag) {
    }
}