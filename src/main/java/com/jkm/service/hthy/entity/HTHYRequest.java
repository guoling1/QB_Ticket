package com.jkm.service.hthy.entity;

import com.jkm.service.hthy.helper.serialize.annotation.HTHYSdkSerializeAlias;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/10/31.
 *
 * base request params
 */
@Data
public class HTHYRequest {

    /**
     * 账号
     */
    @HTHYSdkSerializeAlias(name = "partnerid")
    private String partnerId;

    /**
     * 操作功能
     *
     * {@link com.jkm.enums.EnumHTHYMethodCode}
     *
     *  对应枚举中 code
     */
    private String method;

    /**
     * 请求时间 （yyyyMMddHHmmss）
     */
    @HTHYSdkSerializeAlias(name = "reqtime")
    private String reqTime;

    /**
     * 签名
     */
    private String sign;
}
