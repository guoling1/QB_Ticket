package com.jkm.service.ys.entity;

import lombok.Data;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Data
public class YsSdkRequest {

    /**
     *终端号；YS 分配，如“04800001”
     */
    private String terminalID;
    /**
     *企业编号；YS 分配，如“1011
     */
    private String factoryID;
    /**
     *请求时间格式： YYYYMMDDHHMMSS
     */
    private String reqDateTime;
    /**
     *数字签名，查看
     */
    private String sign;
}
