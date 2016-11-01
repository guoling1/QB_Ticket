package com.jkm.service.hy.entity;

import com.jkm.service.hy.helper.serialize.annotation.HySdkSerializeAlias;
import lombok.Data;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Data
public class HySdkRequest {

    /**
     * 账号
     */
    @HySdkSerializeAlias(name = "partnerid")
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
    @HySdkSerializeAlias(name = "reqtime")
    private String reqTime;

    /**
     * 签名
     *
     * 数字签名=md5(partnerid+method+reqtime+md5(key))，
     * 其中key 由我方分配。md5算法得到的字符串全部为小写
     */
    private String sign;

}
