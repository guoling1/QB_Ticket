package com.jkm.notifiersdk.entity;

import lombok.Builder;
import lombok.Data;

/**
 * Created on 16/7/22.
 *
 * @author hutao
 * @version 1.0
 */
@Data
@Builder
public class SendInstantSmsParams extends BaseYmSdkRequest {
    /**
     * 手机号码（最多1000个），多个用英文逗号(,)隔开
     */
    private String phone;
    /**
     * 短信内容（UTF-8编码）（最多500个汉字或1000个纯英文）
     */
    private String message;
    /**
     * 附加号（最长10位，可置空）
     */
    private String addserial;
}
