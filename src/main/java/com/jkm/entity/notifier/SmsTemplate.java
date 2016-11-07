package com.jkm.entity.notifier;

import com.jkm.entity.BaseEntity;
import com.jkm.enums.notifier.EnumNoticeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by konglingxin on 3/11/15.
 * 通知模板
 * tb_message_template
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsTemplate extends BaseEntity {
    /**
     * 通知类型:{@link com.jkm.enums.notifier.EnumNoticeType}
     */
    private int noticeType;

    /**
     * 短信模板
     */
    private String messageTemplate;


    /**
     * 制定参数的构造函数
     *
     * @param smsTemplate
     * @param noticeType
     */
    public SmsTemplate(final String smsTemplate, final EnumNoticeType noticeType) {
        this.messageTemplate = smsTemplate;
        this.noticeType = noticeType.getId();
    }

    /**
     * 初始化模板构造器
     *
     * @param noticeType
     * @param smsTemplate
     */
    public SmsTemplate(final EnumNoticeType noticeType, final String smsTemplate) {
        this.noticeType = noticeType.getId();
        this.messageTemplate = smsTemplate;
    }

    /**
     * 获取通知的枚举类型
     *
     * @return
     */
    public EnumNoticeType getEnumNoticeType() {
        return EnumNoticeType.integer2Enum(getNoticeType());
    }

}
