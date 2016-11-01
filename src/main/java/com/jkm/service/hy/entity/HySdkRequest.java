package com.jkm.service.hy.entity;

import lombok.Data;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Data
public class HySdkRequest {

    /**
     *账号（非空）
     */
    private String partnerid;
    /**
     *操作功能名（非空）
     */
    private String method;
    /**
     *请求时间，格式：yyyyMMddHHmmss（非空）
     */
    private String reqtime;
    /**
     *数字签名=md5(partnerid+method+reqtime+md5(key))，其中key 由我方分配。md5算法得到的字符串全部为小写
     */
    private String sign;
}
