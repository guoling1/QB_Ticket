package com.jkm.entity.notifier;

import com.jkm.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 短信验证码
 *
 * tb_verification_code
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerificationCode extends BaseEntity {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 验证码
     */
    private String code;
    /**
     * 验证码类型:{@link com.jkm.enums.notifier.EnumVerificationCodeType}
     */
    private int type;
    /**
     * 重试次数
     */
    private int retryCount;

}
