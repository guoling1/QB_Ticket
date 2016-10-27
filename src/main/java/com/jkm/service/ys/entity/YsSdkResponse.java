package com.jkm.service.ys.entity;

import lombok.Data;

/**
 * Created by yuxiang on 2016-10-27.
 */
@Data
public class YsSdkResponse {

    /**
     *交易状态
     */
    private String status;

    /**
     *交易结果描述
     */
    private String msg;

    /**
     *数字签名
     */
    private String sign;
}
