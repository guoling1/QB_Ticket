package com.jkm.entity.notifier;

import com.jkm.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by konglingxin on 3/11/15.
 * 短信发送记录
 * tb_send_message_record
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendMessageRecord extends BaseEntity {
    /**
     * 接收者的用户id
     */
    private long uid;
    /**
     * {@link com.jkm.enums.notifier.EnumUserType}
     */
    private int userType;
    /**
     * 手机号 TODO 加密
     */
    private String mobile;
    /**
     * 通知模板id
     */
    private long messageTemplateId;
    /**
     * 短信内容
     */
    private String content;
    private Date sendTime;
    /**
     * 短信通道返回序列号
     */
    private String sn;

}
